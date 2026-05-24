package com.collab.taskmanager.repos;

import com.collab.taskmanager.entities.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeamRepo extends JpaRepository<Team, Long> {
    boolean existsByCreatedBy(Long id);
    long countByMembersMemberId(Long userId);
}
