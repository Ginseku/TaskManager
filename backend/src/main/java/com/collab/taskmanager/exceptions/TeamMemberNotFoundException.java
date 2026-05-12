package com.collab.taskmanager.exceptions;

public class TeamMemberNotFoundException extends RuntimeException{
    public TeamMemberNotFoundException(){
        super("Not a team member");
    }
}
