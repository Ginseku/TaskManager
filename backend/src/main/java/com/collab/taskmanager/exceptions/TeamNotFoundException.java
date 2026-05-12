package com.collab.taskmanager.exceptions;

public class TeamNotFoundException extends RuntimeException{
    public TeamNotFoundException(Long teamId){
        super("Team with " + teamId + " was not found");
    }
}
