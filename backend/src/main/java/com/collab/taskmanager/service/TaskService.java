package com.collab.taskmanager.service;

import com.collab.taskmanager.dto.request.CreateTaskRequest;
import com.collab.taskmanager.dto.request.UpdateTaskRequest;
import com.collab.taskmanager.dto.response.*;
import com.collab.taskmanager.entities.*;
import com.collab.taskmanager.enums.Role;
import com.collab.taskmanager.enums.Status;
import com.collab.taskmanager.exceptions.*;
import com.collab.taskmanager.repos.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class TaskService {

    private final ProjectRepo projectRepo;
    private final TeamMembersRepo teamMembersRepo;
    private final TaskRepo taskRepo;
    private final TaskPermissionService taskPermissionService;
    private final UserRepo userRepo;

    public TaskService(ProjectRepo projectRepo, TeamMembersRepo teamMembersRepo, TaskRepo taskRepo, TaskPermissionService taskPermissionService, UserRepo userRepo) {
        this.projectRepo = projectRepo;
        this.teamMembersRepo = teamMembersRepo;
        this.taskRepo = taskRepo;
        this.taskPermissionService = taskPermissionService;
        this.userRepo = userRepo;
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
                task.getAssignedUser() != null
                        ? new GetAssignedUser(
                        task.getAssignedUser().getId(),
                        task.getAssignedUser().getName()
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

        return taskRepo.findAllByProject_Id(projectId,pageable)
                .map(task -> new GetTaskResponse(
                        task.getId(),
                        task.getTitle(),
                        task.getDescription(),
                        task.getStatus(),
                        task.getPriority(),
                        task.getAssignedUser() != null
                                ? new GetAssignedUser(
                                task.getAssignedUser().getId(),
                                task.getAssignedUser().getName()
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


    public ResponseEntity<AssignTaskResponse> assignTask(Long taskId, String username, UserPrincipal currentUser) {
        try {
            Task task = taskRepo.findById(taskId).orElseThrow(TaskNotFoundException::new);
            TeamMember teamMember = teamMembersRepo.findByMemberName(username).orElseThrow(
                    () -> new UserNotFoundException(username, true)
            );

            if(!canAssignOrUnassign(currentUser.getUser(), task))
                throw new NotAllowedToAssignException("User " + currentUser.getUser().getName() + " is not allowed to assign this task");

            task.setAssignedUser(teamMember.getMember());
            taskRepo.save(task);

            return ResponseEntity.ok().body(new AssignTaskResponse(taskId, username, true));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new AssignTaskResponse(taskId, username, false));
        }

    }

    public ResponseEntity<UnassignTaskResponse> unassignTask(String taskTitle, UserPrincipal currentUser) {
        try {
            Task task = taskRepo.findByTitle(taskTitle).orElseThrow(TaskNotFoundException::new);

            if(!canAssignOrUnassign(currentUser.getUser(), task))
                throw new NotAllowedToAssignException("User " + currentUser.getUser().getName() + " is not allowed to unassign this task");

            task.setAssignedUser(null);
            taskRepo.save(task);

            return ResponseEntity.ok().body(new UnassignTaskResponse(taskTitle, true));
        }
        catch(Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new UnassignTaskResponse(taskTitle, false));
        }
    }

    public List<TaskResponse> getTasksAssignedToUser(String email) {
        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        List<Task> tasks = taskRepo.findByAssignedUser_Id(user.getId());

        return tasks.stream()
                .map(this::mapToResponse)
                .toList();

    }

    //Helpers

    private TaskResponse mapToResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus() != null ? task.getStatus().name() : null,
                task.getPriority() != null ? task.getPriority().name() : null,
                task.getDueDate(),
                task.getProject() != null ? task.getProject().getId() : null,
                task.getAssignedUser() != null ? task.getAssignedUser().getId() : null,
                task.getAssignedUser() != null ? task.getAssignedUser().getName() : null
        );
    }

    private boolean canAssignOrUnassign(User user, Task task) {
        if(user.getRole() == Role.ADMIN) return true;
        if(task.getCreatedBy().getId() == user.getId()) return true;

        return false;
    }
}
