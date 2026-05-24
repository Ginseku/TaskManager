package com.collab.taskmanager.repos;

import com.collab.taskmanager.entities.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TaskRepo extends JpaRepository<Task, Long> {
    Page<Task> findAllByProject_Id(Long projectId, Pageable pageable);
    List<Task> findByAssignedUser_IdAndProject_Id(Long userId, Long projectId);
    Optional<Task> findByProject_IdAndId(Long projectId, Long taskId);
    //List<Task> findByAssignedUserId(Long id);
    Optional<Task> findByTitle(String taskTitle);
    long countByAssignedUser_Id(Long userId);
    long countByCreatedBy_Id(Long userId);
    List<Task> findByAssignedUser_Id(Long userId);
}
