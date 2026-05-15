package com.collab.taskmanager.controller;

import com.collab.taskmanager.dto.request.CreateTaskRequest;
import com.collab.taskmanager.entities.UserPrincipal;
import com.collab.taskmanager.service.TaskService;
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

    @GetMapping()
    public void getAllTasks() {

    }

    @GetMapping("/{id}")
    public void getTaskById(@PathVariable Long id) {

    }

    @PutMapping("/{id}")
    public void updateTask(@PathVariable Long id) {

    }

    @DeleteMapping("/{id}")
    public void deleteTask(@PathVariable Long id) {

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
