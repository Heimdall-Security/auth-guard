package com.heimdallauth.server.exceptions;

import java.util.List;

public class RoleMappingExists extends RuntimeException {
    public String mappedTo;
    public List<String> ids;

    public RoleMappingExists(String message) {
        super(message);
    }

    public RoleMappingExists(String mappedTo, List<String> ids) {
        this.mappedTo = mappedTo;
        this.ids = ids;
    }

    public RoleMappingExists(String message, String mappedTo, List<String> ids) {
        super(message);
        this.mappedTo = mappedTo;
        this.ids = ids;
    }
}
