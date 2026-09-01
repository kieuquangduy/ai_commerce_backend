package com.duy.aicommerce.backend.auth.dto;

import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {
    private String accessToken;
    private Long id;
    private List<String> roles;
    private String email;
    private String tokenType;

}
