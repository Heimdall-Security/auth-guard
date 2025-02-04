package com.heimdallauth.server.exceptions;

public class DBNotReady extends RuntimeException {
    public DBNotReady(String message) {
        super(message);
    }
}
