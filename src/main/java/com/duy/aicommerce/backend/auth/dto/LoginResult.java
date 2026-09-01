package com.duy.aicommerce.backend.auth.dto;

import com.duy.aicommerce.backend.auth.entity.RefreshToken;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResult {
    private LoginResponse loginResponse;
    private String refreshToken;
}
