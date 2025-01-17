package com.heimdallauth.server.exceptions;

public class UserProfileNotFound extends RuntimeException {
    public UserProfileNotFound(String message) {
        super(message);
    }
}
