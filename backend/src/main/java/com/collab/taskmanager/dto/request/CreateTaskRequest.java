package com.collab.taskmanager.dto.request;

import com.collab.taskmanager.enums.Priority;
import com.collab.taskmanager.enums.Status;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateTaskRequest(@NotBlank @NotNull String title, String description,Priority priority) {
}
