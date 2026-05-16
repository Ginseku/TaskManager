package com.collab.taskmanager.controller;

import com.collab.taskmanager.dto.request.CreateTaskRequest;
import com.collab.taskmanager.dto.request.UpdateTaskRequest;
import com.collab.taskmanager.dto.response.GetTaskResponse;
import com.collab.taskmanager.entities.UserPrincipal;
import com.collab.taskmanager.service.TaskService;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

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

    @GetMapping("/{projectId}/{id}")
    public ResponseEntity<GetTaskResponse> getTaskById(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long projectId,@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id,currentUser,projectId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id, @AuthenticationPrincipal UserPrincipal currentUser) {
        taskService.deleteTaskById(id,currentUser);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/assign")
    public void assignTask(
            @PathVariable Long id
    ) {

    }

    @PutMapping("/{projectId}/{taskId}")
    public void updateTask(@AuthenticationPrincipal UserPrincipal currentUser, @PathVariable Long projectId,@PathVariable Long taskId, @RequestBody UpdateTaskRequest request) {
        taskService.updateTask(currentUser,projectId,taskId,request);
    }

    @GetMapping("/me")
    public void getUserTasks() {

    }

}
