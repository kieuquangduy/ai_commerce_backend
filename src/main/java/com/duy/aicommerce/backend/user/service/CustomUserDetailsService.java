package com.duy.aicommerce.backend.user.service;

import com.duy.aicommerce.backend.user.entity.CustomerUserDetails;
import com.duy.aicommerce.backend.user.entity.User;
import com.duy.aicommerce.backend.user.exception.EmailExistException;
import com.duy.aicommerce.backend.user.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String email) throws EmailExistException {
        User user = userRepository.findByEmail(email).orElseThrow(() -> new EmailExistException("Không tìm thấy email"));

        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(
                        "ROLE_" + role.getName()
                        )

                )
                .toList();
        return new CustomerUserDetails(user.getId(), user.getEmail(), user.getPassword(),user.isVerified(), authorities);
    }
}
