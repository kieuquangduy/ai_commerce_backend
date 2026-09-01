package com.duy.aicommerce.backend.common.exeption;

import com.duy.aicommerce.backend.auth.exception.InvalidRefreshTokenException;
import com.duy.aicommerce.backend.auth.exception.InvalidVerificationToken;
import com.duy.aicommerce.backend.common.dto.ApiResponse;
import com.duy.aicommerce.backend.role.exception.RoleNotFoundException;
import com.duy.aicommerce.backend.user.exception.EmailExistException;
import com.duy.aicommerce.backend.user.exception.UserNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidRefreshTokenException.class)
    public ResponseEntity<ApiResponse> handleInvalidRefreshTokenException(InvalidRefreshTokenException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<ApiResponse> handleEmailExistException(EmailExistException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponse> handleRoleNotFoundException(RoleNotFoundException ex) {
        return  new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(InvalidVerificationToken.class)
    public ResponseEntity<ApiResponse> handleInvalidVerificationToken(InvalidVerificationToken ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
