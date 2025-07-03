package com.example.user_service.exceptionHandler;

public class LoginUnauthorizedException extends RuntimeException{
    public LoginUnauthorizedException(String message) {
        super(message);
    }

    public LoginUnauthorizedException(String message, Throwable cause) {
        super(message, cause);
    }

    public LoginUnauthorizedException(Throwable cause) {
        super(cause);
    }
}
