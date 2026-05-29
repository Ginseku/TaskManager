package com.collab.taskmanager.repos;

import com.collab.taskmanager.dto.response.ProjectResponse;
import com.collab.taskmanager.entities.Project;
import com.collab.taskmanager.entities.UserPrincipal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProjectRepo extends JpaRepository<Project, Long> {
    List<Project> findByTeamId(Long teamId);
    long countByTeamMembersMemberId(Long userId);
}
