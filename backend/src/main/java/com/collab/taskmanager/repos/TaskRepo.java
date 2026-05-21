package com.collab.taskmanager.repos;

import com.collab.taskmanager.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task, Long> {
    Page<Task> findAllByProjectId(Long projectId, Pageable pageable);
    Optional<Task> findByProject_IdAndId(Long projectId, Long taskId);
    List<Task> findByAssignedUserId(Long id);
    Optional<Task> findByTitle(String taskTitle);
}
