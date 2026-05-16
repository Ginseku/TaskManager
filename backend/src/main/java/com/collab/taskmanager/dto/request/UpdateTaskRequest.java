package com.collab.taskmanager.dto.request;

import com.collab.taskmanager.enums.Priority;
import com.collab.taskmanager.enums.Status;

import java.time.LocalDate;

public record UpdateTaskRequest(String name, String description, Status status, Priority priority,LocalDate dueDate) {
}
