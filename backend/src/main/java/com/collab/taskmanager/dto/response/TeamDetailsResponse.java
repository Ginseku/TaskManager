package com.collab.taskmanager.dto.response;

public record TeamDetailsResponse(
        Long id,
        String name,
        boolean canManageMembers) {
}
