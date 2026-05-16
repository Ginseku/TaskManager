package com.collab.taskmanager.service;

import com.collab.taskmanager.entities.Task;
import com.collab.taskmanager.entities.User;
import com.collab.taskmanager.exceptions.AccessDeniedException;
import com.collab.taskmanager.exceptions.UserIsNotTeamMemberException;
import com.collab.taskmanager.repos.TeamMembersRepo;
import org.springframework.stereotype.Service;

@Service
public class TaskPermissionService {

    private final TeamMembersRepo teamMembersRepo;

    public TaskPermissionService(TeamMembersRepo teamMembersRepo) {
        this.teamMembersRepo = teamMembersRepo;
    }

    public void validateTeamMember(Long teamId, Long userId) {
        if (!teamMembersRepo.existsByTeamIdAndMemberId(teamId, userId)) {
            throw new UserIsNotTeamMemberException();
        }
    }

    public void validateDeletePermission(Task task, User user) {

        boolean isOwner = task.getCreatedBy().getId().equals(user.getId());

        boolean isAssignee = task.getAssignedUserId() != null &&
                task.getAssignedUserId().getId().equals(user.getId());

        if (!isOwner && !isAssignee) {
            throw new AccessDeniedException();
        }
    }
}
