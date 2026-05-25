package com.collab.taskmanager.controller;

import com.collab.taskmanager.dto.request.CreateTaskRequest;
import com.collab.taskmanager.dto.request.UpdateTaskRequest;
import com.collab.taskmanager.dto.response.AssignTaskResponse;
import com.collab.taskmanager.dto.response.GetTaskResponse;
import com.collab.taskmanager.dto.response.TaskResponse;
import com.collab.taskmanager.dto.response.UnassignTaskResponse;
import com.collab.taskmanager.entities.Task;
import com.collab.taskmanager.entities.UserPrincipal;
import com.collab.taskmanager.service.TaskService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/{projectId}")
    public void createTask(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long projectId, @RequestBody CreateTaskRequest request) {
        taskService.createTask(currentUser,projectId,request);
    }

    @GetMapping("/{projectId}") // /tasks?page=0&size=5 - will return first page with 5 tasks
    public ResponseEntity<Page<GetTaskResponse>> getAllTasksByProjectId(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long projectId, @ParameterObject Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasksByProjectId(currentUser,projectId,pageable));
    }

    @GetMapping("/{projectId}/{taskId}")
    public ResponseEntity<GetTaskResponse> getTaskByTaskIdAndProjectId(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long projectId,@PathVariable Long taskId) {
        return ResponseEntity.ok(taskService.getTaskById(taskId,currentUser,projectId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal currentUser) {
        taskService.deleteTaskById(id,currentUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{taskId}/{username}/assign")
    public ResponseEntity<AssignTaskResponse> assignTask(
            @PathVariable Long taskId,
            @PathVariable String username,
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {
        return taskService.assignTask(taskId, username, currentUser);
    }

    @PutMapping("/{taskTitle}/unassign")
    public ResponseEntity<UnassignTaskResponse> unassignTask(
            @PathVariable String taskTitle,
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {
        return taskService.unassignTask(taskTitle, currentUser);
    }

    @PutMapping("/{projectId}/{taskId}")
    public void updateTask(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long projectId,@PathVariable Long taskId, @RequestBody UpdateTaskRequest request) {
        taskService.updateTask(currentUser,projectId,taskId,request);
    }

    @GetMapping("/me/{page}/{size}")
    public ResponseEntity<Page<TaskResponse>> getUserTasks(
            Authentication auth,
            @PathVariable int page,
            @PathVariable int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        return ResponseEntity.ok(taskService.getTasksAssignedToUser(auth.getName(), pageable));
    }

}
