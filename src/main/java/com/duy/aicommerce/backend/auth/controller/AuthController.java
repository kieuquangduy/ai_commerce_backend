package com.duy.aicommerce.backend.auth.controller;


import com.duy.aicommerce.backend.auth.dto.RegisterRequest;
import com.duy.aicommerce.backend.auth.service.AuthService;
import com.duy.aicommerce.backend.common.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> register(@RequestBody RegisterRequest request) throws Exception {
        authService.register(request);
        ApiResponse apiResponse = ApiResponse.success(null, "Đăng ký thành công");
        return new ResponseEntity<>(apiResponse, HttpStatus.CREATED);
    }

}
