package com.collab.taskmanager.exceptions;

public class UserIsNotTeamMemberException extends RuntimeException{
    public UserIsNotTeamMemberException(){
        super("User not in the team");
    }
}
