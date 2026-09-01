package com.duy.aicommerce.backend.common.security;

import com.duy.aicommerce.backend.user.entity.CustomerUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import java.util.Base64;
import java.util.Date;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProperties jwtProperties;


    public String generateAccessToken(CustomerUserDetails user) {

        return Jwts.builder()
                .subject(user.getUsername())
                .claim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getAccessExpiration()))
                .signWith(getSigningKey())
                .compact();
    }

    public String extractUserName(String token) {return extractAllClaims(token).getSubject();}

    public Claims extractAllClaims(String token) {
        return Jwts.parser()
                .verifyWith(getSigningKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        Claims claims = extractAllClaims(token);
        String email = extractUserName(token);
        if(email.equals(userDetails.getUsername()) && claims.getExpiration().after(new Date())) {
            return true;
        }
        return false;
    }





    // Hàm Helper
    private SecretKey getSigningKey() {
        byte[] keyBytes = Base64.getDecoder()
                .decode(jwtProperties.getSecret());

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
