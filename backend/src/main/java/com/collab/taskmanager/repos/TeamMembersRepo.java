package com.collab.taskmanager.repos;


import com.collab.taskmanager.entities.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeamMembersRepo extends JpaRepository<TeamMember, Long> {
    boolean existsByTeamIdAndMemberId(Long teamId, Long currentUserId);
    Optional<TeamMember> findByTeamIdAndMemberId(Long teamId, Long currentUserId);
    List<TeamMember> findByMemberId(Long memberId);
    List<TeamMember> findByTeamId(Long teamId);
}
