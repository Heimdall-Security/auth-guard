package com.heimdallauth.server.exceptions;

public class TemplateDoesNotExists extends RuntimeException {
    public TemplateDoesNotExists(String message) {
        super(message);
    }
}
