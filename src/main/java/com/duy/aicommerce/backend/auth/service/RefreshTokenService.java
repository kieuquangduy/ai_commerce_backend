package com.duy.aicommerce.backend.auth.service;

import com.duy.aicommerce.backend.auth.entity.RefreshToken;
import com.duy.aicommerce.backend.auth.exception.InvalidRefreshTokenException;
import com.duy.aicommerce.backend.auth.repository.RefreshTokenRepository;
import com.duy.aicommerce.backend.common.security.JwtProperties;
import com.duy.aicommerce.backend.user.entity.CustomerUserDetails;
import com.duy.aicommerce.backend.user.entity.User;
import com.duy.aicommerce.backend.user.exception.UserNotFoundException;
import com.duy.aicommerce.backend.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RefreshTokenService {
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;
    private final JwtProperties jwtProperties;


    public RefreshToken createRefreshToken(CustomerUserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername()).orElseThrow(
                () -> new UserNotFoundException("Không tìm thấy người dùng"));

        String token = UUID.randomUUID().toString();
        RefreshToken refreshToken = RefreshToken.builder()
                .token(token)
                .user(user)
                .expiredAt(LocalDateTime.now().plusSeconds(jwtProperties.getRefreshExpiration() / 1000))
                .revoked(false)
                .build();

        refreshTokenRepository.save(refreshToken);

        return refreshToken;
    }

    public RefreshToken validateRefreshToken(String token) {

        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() ->
                        new InvalidRefreshTokenException("Refresh token không hợp lệ"));

        if (refreshToken.isRevoked()) {
            throw new InvalidRefreshTokenException("Refresh token đã bị revoke");
        }

        if (refreshToken.getExpiredAt().isBefore(LocalDateTime.now())) {
            throw new InvalidRefreshTokenException("Refresh token đã hết hạn");
        }

        return refreshToken;
    }
    public void revokeToken(String token) {
        RefreshToken Rtoken = refreshTokenRepository.findByToken(token).orElseThrow(
                () -> new InvalidRefreshTokenException("Không tìm thấy token"));
        Rtoken.setRevoked(true);
    }
}
