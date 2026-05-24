package com.collab.taskmanager.dto.response;

public record DashboardStatsResponse(
        long teams,
        long projects,
        long assignedTasks,
        long createdTasks
) {
}
