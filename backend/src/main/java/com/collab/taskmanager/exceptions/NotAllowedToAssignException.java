package com.collab.taskmanager.exceptions;

public class NotAllowedToAssignException extends RuntimeException {
    public NotAllowedToAssignException(String message) {
        super(message);
    }
}
