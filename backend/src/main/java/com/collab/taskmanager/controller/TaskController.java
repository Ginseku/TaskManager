package com.collab.taskmanager.controller;

import com.collab.taskmanager.dto.request.CreateTaskRequest;
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

    @GetMapping() // /tasks?page=0&size=5 - will return first page with 5 tasks
    public ResponseEntity<Page<GetTaskResponse>> getAllTasks(@ParameterObject Pageable pageable) {
        return ResponseEntity.ok(taskService.getAllTasks(pageable));
    }

    @GetMapping("/{id}")
    public ResponseEntity<GetTaskResponse> getTaskById(@PathVariable Long id) {
        return ResponseEntity.ok(taskService.getTaskById(id));
    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable Long id) {

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        taskService.deleteTaskById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/assign")
    public void assignTask(
            @PathVariable Long id
    ) {

    }

    @PutMapping("/{id}/status")
    public void updateTaskStatus(
            @PathVariable Long id
    ) {

    }

    @GetMapping("/me")
    public void getUserTasks() {

    }

    @PatchMapping("/{date}")
    public void addDueDate(@PathVariable LocalDate date){

    }

}
