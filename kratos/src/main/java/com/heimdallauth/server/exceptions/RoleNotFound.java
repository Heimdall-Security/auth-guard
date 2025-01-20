package com.heimdallauth.server.exceptions;

public class RoleNotFound extends RuntimeException {
    public String roleId;

    public RoleNotFound(String message) {
        super(message);
    }

    public RoleNotFound(String message, String roleId) {
        super(message);
        this.roleId = roleId;
    }
}
