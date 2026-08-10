package com.duy.aicommerce.backend.auth.service;

import com.duy.aicommerce.backend.auth.dto.RegisterRequest;
import com.duy.aicommerce.backend.auth.entity.TokenType;
import com.duy.aicommerce.backend.auth.entity.VerificationToken;
import com.duy.aicommerce.backend.auth.repository.VerificationTokenRepository;
import com.duy.aicommerce.backend.notification.service.EmailService;
import com.duy.aicommerce.backend.role.entity.Role;
import com.duy.aicommerce.backend.role.exception.RoleNotFoundException;
import com.duy.aicommerce.backend.role.repository.RoleRepository;
import com.duy.aicommerce.backend.user.entity.User;
import com.duy.aicommerce.backend.user.exception.EmailExistException;
import com.duy.aicommerce.backend.user.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final VerificationTokenRepository verificationTokenRepository;

    public void register(RegisterRequest request) throws Exception {

        Optional<User> existingUser = userRepository.findByEmail(request.getEmail());

        if(existingUser.isPresent() && existingUser.get().getVerified()) {
            throw new EmailExistException("Email đã được sử dụng");
        }
        User user = existingUser.orElseGet(() ->
                User.builder()
                        .email(request.getEmail())
                .build());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Role customerRole =  roleRepository.findByName("CUSTOMER").orElseThrow(() -> new RoleNotFoundException("Không tìm thấy role"));
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
        String link = "http://localhost:8080/auth/register/verify_register_token?token=" + token;
        emailService.sendVerificationEmail(user.getEmail(),  link);
    }
}
