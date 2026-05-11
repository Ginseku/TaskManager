package com.collab.taskmanager.exceptions;

public class UserNameAlreadyExist extends RuntimeException{
    public UserNameAlreadyExist(){
        super("User with this username already exist");
    }

}
