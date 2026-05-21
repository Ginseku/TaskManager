package com.collab.taskmanager.dto.response;

import com.collab.taskmanager.enums.TeamRole;

import java.util.List;

public record GetProjectMembersNameResponse(String name, TeamRole role, List<String> tasks) {
}
