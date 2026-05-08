package com.collab.taskmanager.exceptions;

public class TeamNotFound extends RuntimeException{
    public TeamNotFound(Long teamId){
        super("Team with " + teamId + " was not found");
    }
}
