package com.collab.taskmanager.repos;


import com.collab.taskmanager.entities.Team;
import com.collab.taskmanager.entities.TeamMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamMembersRepo extends JpaRepository<TeamMember, Long> {
}
