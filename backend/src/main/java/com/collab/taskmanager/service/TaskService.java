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

    public TaskService(ProjectRepo projectRepo, TeamMembersRepo teamMembersRepo, TaskRepo taskRepo) {
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

        validateTeamMember(project.getTeam().getId(), user.getId());

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

    public GetTaskResponse getTaskById(Long id, UserPrincipal currentUser, Long projectId) {

        validateTeamMember(projectId,currentUser.getUser().getId());

        Task task = taskRepo.findById(id)
                .orElseThrow(TaskNotFoundException::new);
        return new GetTaskResponse(
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
                task.getDueDate()
        );
    }

    public Page<GetTaskResponse> getAllTasks(UserPrincipal currentUser, Long projectId, Pageable pageable) {

        validateTeamMember(projectId,currentUser.getUser().getId());

        return taskRepo.findAll(pageable)
                .map(task -> new GetTaskResponse(
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
                        task.getDueDate()
                ));
    }

    public void deleteTaskById(Long id, UserPrincipal currentUser) {
        User user = currentUser.getUser();

        Task task = taskRepo.findById(id)
                .orElseThrow(TaskNotFoundException::new);

        validateDeletePermission(task, user);

        taskRepo.delete(task);
    }

    public void updateTask(UserPrincipal currentUser, Long projectId, Long taskId, UpdateTaskRequest request) {

        User user = currentUser.getUser();

        Task task = taskRepo.findById(taskId)
                .orElseThrow(TaskNotFoundException::new);

        validateTeamMember(projectId,currentUser.getUser().getId());

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

    private void validateTeamMember(Long teamId, Long userId) {
        if (!teamMembersRepo.existsByTeamIdAndMemberId(teamId, userId)) {
            throw new UserIsNotTeamMemberException();
        }
    }
    private void validateDeletePermission(Task task, User user) {

        boolean isOwner = task.getCreatedBy().getId().equals(user.getId());

        boolean isAssignee = task.getAssignedUserId() != null &&
                task.getAssignedUserId().getId().equals(user.getId());

        if (!isOwner && !isAssignee) {
            throw new AccessDeniedException();
        }
    }

}
