package com.collab.taskmanager.exceptions;

public class UserIsNotOwnerException extends RuntimeException{
    public UserIsNotOwnerException(){
        super("User is not Owner");
    }
}
