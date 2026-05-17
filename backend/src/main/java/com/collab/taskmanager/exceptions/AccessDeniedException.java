package com.collab.taskmanager.exceptions;

public class AccessDeniedException extends RuntimeException {
    public AccessDeniedException(){
        super("Only task owner or assignee can delete task");
    }

}
