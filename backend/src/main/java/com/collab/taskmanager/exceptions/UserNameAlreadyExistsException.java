package com.collab.taskmanager.exceptions;

public class UserNameAlreadyExistsException extends RuntimeException{
    public UserNameAlreadyExistsException(){
        super("User with this username already exist");
    }

}
