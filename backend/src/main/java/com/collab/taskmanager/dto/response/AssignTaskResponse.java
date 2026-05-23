package com.collab.taskmanager.dto.response;

public record AssignTaskResponse(Long taskId, String username, boolean success) {
}
