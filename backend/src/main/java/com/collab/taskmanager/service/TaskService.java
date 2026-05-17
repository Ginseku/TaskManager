package com.collab.taskmanager.service;

import com.collab.taskmanager.dto.request.CreateTaskRequest;
import com.collab.taskmanager.dto.request.UpdateTaskRequest;
import com.collab.taskmanager.dto.response.GetAssignedUser;
import com.collab.taskmanager.dto.response.GetTaskResponse;
import com.collab.taskmanager.entities.*;
import com.collab.taskmanager.enums.Status;
import com.collab.taskmanager.exceptions.AccessDeniedException;
import com.collab.taskmanager.exceptions.ProjectNotFoundException;
import com.collab.taskmanager.exceptions.TaskNotFoundException;
import com.collab.taskmanager.exceptions.UserIsNotTeamMemberException;
import com.collab.taskmanager.repos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TaskService {

    private final ProjectRepo projectRepo;
    private final TeamMembersRepo teamMembersRepo;
    private final TaskRepo taskRepo;
    private final TaskPermissionService taskPermissionService;

    public TaskService(ProjectRepo projectRepo, TeamMembersRepo teamMembersRepo, TaskRepo taskRepo, TaskPermissionService taskPermissionService) {
        this.projectRepo = projectRepo;
        this.teamMembersRepo = teamMembersRepo;
        this.taskRepo = taskRepo;
        this.taskPermissionService = taskPermissionService;
    }

    public void createTask(UserPrincipal currentUser, Long projectId, CreateTaskRequest request) {
        User user = currentUser.getUser();

        Project project = projectRepo.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);


        taskPermissionService.validateTeamMember(project.getTeam().getId(), user.getId());

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

    public GetTaskResponse getTaskById(Long taskId, UserPrincipal currentUser, Long projectId) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        taskPermissionService.validateTeamMember(project.getTeam().getId(),currentUser.getUser().getId());

        Task task = taskRepo.findByProject_IdAndId(project.getId(),taskId)
                .orElseThrow(TaskNotFoundException::new);
        return new GetTaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getPriority(),
                task.getAssignedUserId() != null
                        ? new GetAssignedUser(
                        task.getAssignedUserId().getId(),
                        task.getAssignedUserId().getName()
                )
                        : null,
                task.getDueDate(),
                task.getCreatedBy().getId()
        );
    }

    public Page<GetTaskResponse> getAllTasksByProjectId(UserPrincipal currentUser, Long projectId, Pageable pageable) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        taskPermissionService.validateTeamMember(project.getTeam().getId(),currentUser.getUser().getId());

        return taskRepo.findAllByProjectId(projectId,pageable)
                .map(task -> new GetTaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getAssignedUserId() != null
                                ? new GetAssignedUser(
                                task.getAssignedUserId().getId(),
                                task.getAssignedUserId().getName()
                        )
                                : null,
                        task.getDueDate(),
                        task.getCreatedBy().getId()
                ));
    }

    public void deleteTaskById(Long id, UserPrincipal currentUser) {
        User user = currentUser.getUser();

        Task task = taskRepo.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        taskPermissionService.validateDeletePermission(task, user);

        taskRepo.delete(task);
    }

    public void updateTask(UserPrincipal currentUser, Long projectId, Long taskId, UpdateTaskRequest request) {

        User user = currentUser.getUser();
        Project project = projectRepo.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);
        Task task = taskRepo.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);

        taskPermissionService.validateTeamMember(project.getTeam().getId(),currentUser.getUser().getId());

        // name
        if (request.name() != null && !request.name().isBlank()) {
            task.setTitle(request.name());
        }

        // description
        if (request.description() != null) {
            task.setDescription(request.description());
        }

        // status
        if (request.status() != null) {
            task.setStatus(request.status());
        }

        // priority
        if (request.priority() != null) {
            task.setPriority(request.priority());
        }

        // dueDate
        if (request.dueDate() != null) {
            task.setDueDate(request.dueDate());
        }

        taskRepo.save(task);

    }



}
