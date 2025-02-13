package com.ampersand.groom.domain.auth.expection;

public class AuthException extends RuntimeException {
    public AuthException(String message) {
        super(message);
    }
}
