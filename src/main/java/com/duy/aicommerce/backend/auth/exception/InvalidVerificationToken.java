package com.duy.aicommerce.backend.auth.exception;

public class InvalidVerificationToken extends RuntimeException {
  public InvalidVerificationToken(String message) {
    super(message);
  }
}
