package com.duy.aicommerce.backend.auth.service;

import com.duy.aicommerce.backend.auth.dto.*;
import com.duy.aicommerce.backend.auth.entity.RefreshToken;
import com.duy.aicommerce.backend.auth.entity.TokenType;
import com.duy.aicommerce.backend.auth.entity.VerificationToken;
import com.duy.aicommerce.backend.auth.exception.InvalidRefreshTokenException;
import com.duy.aicommerce.backend.auth.exception.InvalidVerificationToken;
import com.duy.aicommerce.backend.auth.repository.RefreshTokenRepository;
import com.duy.aicommerce.backend.auth.repository.VerificationTokenRepository;
import com.duy.aicommerce.backend.common.security.JwtProperties;
import com.duy.aicommerce.backend.common.security.JwtService;
import com.duy.aicommerce.backend.notification.service.EmailService;
import com.duy.aicommerce.backend.role.entity.Role;
import com.duy.aicommerce.backend.role.exception.RoleNotFoundException;
import com.duy.aicommerce.backend.role.repository.RoleRepository;
import com.duy.aicommerce.backend.user.entity.CustomerUserDetails;
import com.duy.aicommerce.backend.user.entity.User;
import com.duy.aicommerce.backend.user.exception.EmailExistException;
import com.duy.aicommerce.backend.user.exception.UserNotFoundException;
import com.duy.aicommerce.backend.user.repository.UserRepository;
import com.duy.aicommerce.backend.user.service.CustomUserDetailsService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserDetailsService userDetailsService;


    //@Value()
    private String frontendURL;

    @Transactional
    public void register(RegisterRequest request) throws Exception {

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if (existingUser.isPresent() && existingUser.get().isVerified()) {
            throw new EmailExistException("Email đã được sử dụng");
        }
        User user = existingUser.orElseGet(() ->
                User.builder()
                        .email(request.getEmail())
                        .build());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role customerRole = roleRepository.findByName("CUSTOMER").orElseThrow(() -> new RoleNotFoundException("Không tìm thấy role"));
        user.getRoles().add(customerRole);
        user.setVerified(false);
        userRepository.save(user);

        verificationTokenRepository.deleteByUserAndType(user, TokenType.VERIFY_EMAIL);

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .type(TokenType.VERIFY_EMAIL)
                .token(token)
                .user(user)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();
        verificationToken.setUsed(false);
        verificationTokenRepository.save(verificationToken);
        String link = "http://localhost:8080/user/auth/verify_email?token=" + token;
        emailService.sendVerificationEmail(user.getEmail(), link);
    }

    @Transactional
    public void verifyRegisterToken(String token) throws RuntimeException {
        VerificationToken existToken = verificationTokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidVerificationToken("Token không hợp lệ"));

        if (existToken.isUsed()) {
            throw new InvalidVerificationToken("Token đã được sử dụng");
        }
        if (existToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new InvalidVerificationToken("Token đã hết hạn");
        }

        User user = existToken.getUser();
        user.setVerified(true);

        existToken.setUsed(true);
    }

    @Transactional
    public LoginResult login(LoginRequest request) {


        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword());
        try {
            Authentication authentication = authenticationManager.authenticate(authRequest);

            CustomerUserDetails userDetails = (CustomerUserDetails) authentication.getPrincipal();

            String accessToken = jwtService.generateAccessToken(userDetails);


            LoginResponse loginResponse = LoginResponse.builder()
                    .id(userDetails.getId())
                    .email(userDetails.getUsername())
                    .roles(userDetails.getAuthorities().stream()
                            .map(GrantedAuthority::getAuthority)
                            .toList())
                    .accessToken(accessToken)
                    .tokenType("Bearer")
                    .build();

            LoginResult loginResult = LoginResult.builder()
                    .loginResponse(loginResponse)
                    .refreshToken(refreshTokenService.createRefreshToken(userDetails).getToken())
                    .build();
            return loginResult;

        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Tài khoản hoặc mật khẩu không chính xác");
        }
    }

    public LoginResponse refresh(String token) {

        RefreshToken refreshToken =
                refreshTokenService.validateRefreshToken(token);

        User user = refreshToken.getUser();

        CustomerUserDetails cusUser = (CustomerUserDetails) userDetailsService.loadUserByUsername(user.getEmail());

        String accessToken =
                jwtService.generateAccessToken(cusUser);

        return LoginResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .roles(user.getRoles().stream()
                        .map(role -> role.getName())
                        .toList())
                .accessToken(accessToken)
                .tokenType("Bearer")
                .build();
    }

    public void logout(String token) {
        refreshTokenService.revokeToken(token);
    }

    @Transactional
    public void sendResetPasswordEmail(String email) throws Exception {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new UserNotFoundException("Bạn chưa có tài khoản")
        );
        verificationTokenRepository.deleteByUserAndType(
                user,
                TokenType.RESET_PASSWORD
        );

        String token = UUID.randomUUID().toString();
        VerificationToken verificationToken = VerificationToken.builder()
                .type(TokenType.RESET_PASSWORD)
                .token(token)
                .user(user)
                .expiredAt(LocalDateTime.now().plusMinutes(10))
                .build();
        verificationToken.setUsed(false);
        verificationTokenRepository.save(verificationToken);

        String link = "http://localhost:8080/user/auth/reset_password?token=" + token;

        emailService.sendMissingPasswordEmail(email, link);
    }

    @Transactional
    public void resetPassword(String token, ResetPasswordRequest request) throws Exception {
        VerificationToken verificationToken = verificationTokenRepository.findByToken(token).orElseThrow(
                () -> new InvalidVerificationToken("Token không tồn tại")
        );
        if (verificationToken.isUsed()) {
            throw new InvalidVerificationToken("Token đã được sử dụng");
        }
        if(verificationToken.getType() != TokenType.RESET_PASSWORD) {
            throw new InvalidVerificationToken("Token không hợp lệ");
        }
        if(verificationToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new InvalidVerificationToken("Token đã hết hạn");
        }
        verificationToken.setUsed(true);
        User user = verificationToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        userRepository.save(user);
    }
}
