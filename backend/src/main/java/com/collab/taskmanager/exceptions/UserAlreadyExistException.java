package com.collab.taskmanager.exceptions;

public class UserAlreadyExistException extends RuntimeException{
    public UserAlreadyExistException(String email){
        super("User with this " + email + " already exist.");
    }
}
