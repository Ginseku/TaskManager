package com.collab.taskmanager.dto.response;

import java.time.LocalDate;

public record TaskResponse(
        Long id,
        String title,
        String description,
        String status,
        String priority,
        LocalDate dueDate,
        Long projectId,
        Long assignedUserId,
        String assignedUserName
) {
}
