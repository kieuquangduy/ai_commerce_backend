package com.duy.aicommerce.backend.common.security;

import com.duy.aicommerce.backend.user.entity.CustomerUserDetails;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtService jwtService;


    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        final String header = request.getHeader("Authorization");
        final String userEmail;

        if (header == null || !header.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;

        }

        try {
            String token = header.substring(7);

            Claims claims = jwtService.extractAllClaims(token);

            userEmail = claims.getSubject();

            UserDetails userDetails =
                    userDetailsService.loadUserByUsername(userEmail);

            if (jwtService.isTokenValid(token, (CustomerUserDetails) userDetails)) {

                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                SecurityContextHolder.getContext()
                        .setAuthentication(authentication);
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
        chain.doFilter(request, response);
    }
}
