package com.collab.taskmanager.dto.response;

import com.collab.taskmanager.enums.TeamRole;

public record GetProjectMembersNameResponse(String name, TeamRole role) {
}
