package com.duy.aicommerce.backend.auth.repository;

import com.duy.aicommerce.backend.auth.entity.TokenType;
import com.duy.aicommerce.backend.auth.entity.VerificationToken;
import com.duy.aicommerce.backend.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
    void deleteByUserAndType(User user, TokenType type);

}
