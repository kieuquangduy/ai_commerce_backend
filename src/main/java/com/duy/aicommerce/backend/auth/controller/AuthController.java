package com.duy.aicommerce.backend.auth.controller;


import com.duy.aicommerce.backend.auth.dto.*;
import com.duy.aicommerce.backend.auth.exception.InvalidVerificationToken;
import com.duy.aicommerce.backend.auth.service.AuthService;
import com.duy.aicommerce.backend.common.dto.ApiResponse;
import com.duy.aicommerce.backend.user.entity.CustomerUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;
import java.util.Map;

@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    //@Value
    private final String frontendURL = "http://localhost:8080";

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) throws Exception {
        authService.register(request);
        ApiResponse apiResponse = ApiResponse.success(null, "Đăng ký thành công");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

    @GetMapping("/verify_register_email")
    public RedirectView verifyMail(@RequestParam String token) {
        try {
            authService.verifyRegisterToken(token);
            return new RedirectView(frontendURL + "/login?verified=success");
        } catch (InvalidVerificationToken e) {
            return new RedirectView(frontendURL + "/login?verified=failure");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse> login(@RequestBody LoginRequest request, HttpServletResponse response) throws Exception {
        LoginResult result = authService.login(request);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", result.getRefreshToken())
                .httpOnly(true)
                .secure(false)
                .path("/user/auth")
                .maxAge(7*24*60*60)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());

        ApiResponse apiResponse = ApiResponse.success(result.getLoginResponse(), "Đăng nhập thành công");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponse> refresh(@CookieValue("refreshToken") String refreshToken) {
        LoginResponse loginResponse = authService.refresh(refreshToken);
        ApiResponse response = ApiResponse.success(loginResponse, "Refresh token thành công");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponse> logout(@CookieValue(value = "refreshToken", required = false) String refreshToken, HttpServletResponse response) throws Exception {
        if (refreshToken != null) {
            authService.logout(refreshToken);
        }
        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken",null)
                .httpOnly(true)
                .secure(false)
                .path("/user/auth")
                .maxAge(0)
                .sameSite("Lax")
                .build();
        response.addHeader("Set-Cookie", refreshTokenCookie.toString());
        ApiResponse apiResponse = ApiResponse.success(null, "Đăng xuất thành công");
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<ApiResponse> forgotPassword(@RequestParam String email) throws Exception {

            authService.sendResetPasswordEmail(email);

            ApiResponse response = ApiResponse.success(null, "Email đã được gửi");
            return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<ApiResponse> resetPassword(@RequestParam String token, @RequestBody ResetPasswordRequest request) throws Exception {
        authService.resetPassword(token, request);
        ApiResponse response = ApiResponse.success(null, "Đổi mật khẩu thành công");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse> me(Authentication authentication) {

        CustomerUserDetails userDetails =
                (CustomerUserDetails) authentication.getPrincipal();

        Map<String, Object> data = Map.of(
                "id", userDetails.getId(),
                "email", userDetails.getUsername(),
                "roles", userDetails.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
        );

        return ResponseEntity.ok(
                ApiResponse.success(data, "Lấy thông tin user thành công")
        );
    }

}
