package com.duy.aicommerce.backend.common.exeption;

import com.duy.aicommerce.backend.common.dto.ApiResponse;
import com.duy.aicommerce.backend.role.exception.RoleNotFoundException;
import com.duy.aicommerce.backend.user.exception.EmailExistException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailExistException.class)
    public ResponseEntity<ApiResponse> handleEmailExistException(EmailExistException ex) {
        return new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ApiResponse> handleRoleNotFoundException(RoleNotFoundException ex) {
        return  new ResponseEntity<>(ApiResponse.error(ex.getMessage()), HttpStatus.BAD_REQUEST);
    }
}
