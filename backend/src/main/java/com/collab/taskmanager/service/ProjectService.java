package com.collab.taskmanager.service;

import com.collab.taskmanager.dto.request.NameAndDescriptionRequest;
import com.collab.taskmanager.dto.request.UpdateProjectRequest;
import com.collab.taskmanager.dto.response.GetProjectMembersNameResponse;
import com.collab.taskmanager.dto.response.ProjectResponse;
import com.collab.taskmanager.entities.*;
import com.collab.taskmanager.enums.TeamRole;
import com.collab.taskmanager.exceptions.*;
import com.collab.taskmanager.repos.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;

@Service
@Transactional
public class ProjectService {

    private final ProjectRepo projectRepo;
    private final UserRepo userRepo;
    private final TeamRepo teamRepo;
    private final TeamMembersRepo teamMembersRepo;
    private final TaskRepo taskRepo;

    public ProjectService(ProjectRepo projectRepo, UserRepo userRepo, TeamRepo teamRepo, TeamMembersRepo teamMembersRepo, TaskRepo taskRepo) {
        this.projectRepo = projectRepo;
        this.userRepo = userRepo;
        this.teamRepo = teamRepo;
        this.teamMembersRepo = teamMembersRepo;
        this.taskRepo = taskRepo;
    }

    public void createProject(Long teamId, UserPrincipal userPrincipal, NameAndDescriptionRequest req) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));

        User user = userPrincipal.getUser();

        validateOwner(teamId,user.getId());

        Project project = new Project();
        project.setName(req.name());
        project.setDescription(req.description());
        project.setCreatedBy(user);
        project.setTeam(team);
        projectRepo.save(project);
    }


    public List<GetProjectMembersNameResponse> getProjectMembers(Long projectId, UserPrincipal currentUser) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        Team team = project.getTeam();

        User user = currentUser.getUser();

        if (!teamMembersRepo.existsByTeamIdAndMemberId(team.getId(), user.getId())){
            throw new UserIsNotTeamMemberException();
        }

        List<TeamMember> members = teamMembersRepo.findByTeamId(team.getId());
        HashMap<String, List<String>> userTasks = new HashMap<>();
        for (TeamMember member : members) {
            List<Task> tasks = taskRepo.findByAssignedUserId(member.getMember().getId());
            List<String> tasksNames = tasks.stream().map(Task::getTitle).toList();
            userTasks.put(member.getMember().getName(), tasksNames);
        }

        return members.stream().map(member -> new GetProjectMembersNameResponse(
                    member.getMember().getName(),
                    member.getTeamRole(),
                    userTasks.get(member.getMember().getName())
                ))
                .toList();

    }


    public void updateProject(Long projectId, UpdateProjectRequest request, UserPrincipal currentUser) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        validateOwner(project.getTeam().getId(), currentUser.getUser().getId());

        if (request.getName() != null && !request.getName().isBlank()) {
            project.setName(request.getName());
        }

        if (request.getDescription() != null && !request.getDescription().isBlank()) {
            project.setDescription(request.getDescription());
        }

        projectRepo.save(project);
    }

    public void deleteProject(Long projectId, UserPrincipal currentUser) {

        Project project = projectRepo.findById(projectId)
                .orElseThrow(ProjectNotFoundException::new);

        validateOwner(project.getTeam().getId(), currentUser.getUser().getId());

        projectRepo.delete(project);
    }

    private void validateOwner(Long teamId, Long userId) {

        TeamMember teamMember = teamMembersRepo
                .findByTeamIdAndMemberId(teamId, userId)
                .orElseThrow(UserIsNotTeamMemberException::new);

        if (teamMember.getTeamRole() != TeamRole.OWNER) {
            throw new UserIsNotOwnerException();
        }
    }

    public ProjectResponse getProjectById(Long projectId, UserPrincipal user) {
        Project project = projectRepo.findById(projectId)
                .orElseThrow(() -> new ProjectNotFoundException());

        return new ProjectResponse(
                project.getId(),
                project.getName(),
                project.getDescription()
        );
    }
}
