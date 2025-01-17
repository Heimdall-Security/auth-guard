package com.heimdallauth.server.exceptions;

public class UserProfileAlreadyExist extends RuntimeException {
    public UserProfileAlreadyExist(String message) {
        super(message);
    }
}
