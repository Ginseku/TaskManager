package com.collab.taskmanager.exceptions;

public class ProjectNotFoundException extends RuntimeException {
    public ProjectNotFoundException(){
        super("Project not found");
    }
}
