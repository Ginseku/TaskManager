package com.collab.taskmanager.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @PostMapping()
    public void createTask() {

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

}
