package com.collab.taskmanager.exceptions;

public class TaskNotFoundException extends RuntimeException{
    public TaskNotFoundException(){
        super("Task not found");
    }
}
