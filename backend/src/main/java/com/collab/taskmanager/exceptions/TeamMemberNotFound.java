package com.collab.taskmanager.exceptions;

public class TeamMemberNotFound extends RuntimeException{
    public TeamMemberNotFound(){
        super("Not a team member");
    }
}
