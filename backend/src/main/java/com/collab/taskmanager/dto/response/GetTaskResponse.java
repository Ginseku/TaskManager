package com.collab.taskmanager.dto.response;

import com.collab.taskmanager.entities.User;
import com.collab.taskmanager.enums.Priority;
import com.collab.taskmanager.enums.Status;

import java.time.LocalDate;

public record GetTaskResponse(String name, String description, Status status, Priority priority, GetAssignedUser assignedUser, LocalDate dueDate) {
}
