package com.c2g4.SingHealthWebApp.JWT;

public class AuthenticationException extends RuntimeException{
    public AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }
}
