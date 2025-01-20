package com.heimdallauth.server.exceptions;

public class GroupNotFound extends RuntimeException {
    public final String groupId;

    public GroupNotFound(String message, String groupId) {
        super(message);
        this.groupId = groupId;
    }
}
