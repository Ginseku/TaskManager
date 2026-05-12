package com.collab.taskmanager.service;

import com.collab.taskmanager.entities.*;
import com.collab.taskmanager.enums.TeamRole;
import com.collab.taskmanager.exceptions.TeamNotFound;
import com.collab.taskmanager.exceptions.UserIsNotAdmin;
import com.collab.taskmanager.exceptions.UserNotFoundException;
import com.collab.taskmanager.repos.ProjectRepo;
import com.collab.taskmanager.repos.TeamMembersRepo;
import com.collab.taskmanager.repos.TeamRepo;
import com.collab.taskmanager.repos.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final TeamRepo teamRepo;
    private final TeamMembersRepo teamMembersRepo;

    public ProjectService(ProjectRepo projectRepo, UserRepo userRepo, TeamRepo teamRepo, TeamMembersRepo teamMembersRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.teamRepo = teamRepo;
        this.teamMembersRepo = teamMembersRepo;
    }

    public void createProject(Long teamId, UserPrincipal userPrincipal) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow( () -> new TeamNotFound(teamId));

        User user = userRepo.findById(userPrincipal.getUser().getId())
                .orElseThrow( () -> new UserNotFoundException(userPrincipal.getUser().getId()));

        TeamMember teamMember = teamMembersRepo.findByTeamIdAndMemberId(teamId, user.getId())
                .orElseThrow(() -> new RuntimeException("User is not team member"));

        if (teamMember.getTeamRole() != TeamRole.OWNER){
            throw new RuntimeException("User is not Owner");
        }

        Project project = new Project();
        project.setName("Test");
        project.setDescription("TestDesc");
        project.setCreatedBy(user);
        project.setTeam(team);
        projectRepo.save(project);
    }


}
