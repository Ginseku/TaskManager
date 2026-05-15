package com.collab.taskmanager.service;

import com.collab.taskmanager.dto.request.CreateTaskRequest;
import com.collab.taskmanager.entities.*;
import com.collab.taskmanager.enums.Status;
import com.collab.taskmanager.exceptions.ProjectNotFoundException;
import com.collab.taskmanager.exceptions.UserIsNotTeamMemberException;
import com.collab.taskmanager.exceptions.UserNotFoundException;
import com.collab.taskmanager.repos.*;
import org.springframework.stereotype.Service;

@Service
public class TaskService {

    private final TeamRepo teamRepo;
    private final ProjectRepo projectRepo;
    private final TeamMembersRepo teamMembersRepo;
    private final TaskRepo taskRepo;

    public TaskService(TeamRepo teamRepo, ProjectRepo projectRepo, TeamMembersRepo teamMembersRepo, TaskRepo taskRepo) {
        this.teamRepo = teamRepo;
        this.projectRepo = projectRepo;
        this.teamMembersRepo = teamMembersRepo;
        this.taskRepo = taskRepo;
    }

    public void createTask(UserPrincipal currentUser, Long projectId, CreateTaskRequest request) {
        User user = currentUser.getUser();

        Project project = projectRepo.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        if (project.getTeam().getId() == null) {
            throw new IllegalArgumentException("Project does not belong to this team");
        }

        if (!teamMembersRepo.existsByTeamIdAndMemberId(project.getTeam().getId(),user.getId())){
            throw new UserIsNotTeamMemberException();
        }

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .priority(request.priority())
                .status(Status.TODO)
                .createdBy(user)
                .project(project)
                .build();
        taskRepo.save(task);
    }
}
