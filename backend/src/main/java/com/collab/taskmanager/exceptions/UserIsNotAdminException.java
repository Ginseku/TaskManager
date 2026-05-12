package com.collab.taskmanager.exceptions;

public class UserIsNotAdminException extends RuntimeException{
    public UserIsNotAdminException(String email){
        super("User " + email + " is not an admin");
    }
}
