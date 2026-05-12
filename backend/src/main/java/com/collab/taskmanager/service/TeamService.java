package com.collab.taskmanager.service;

import com.collab.taskmanager.dto.request.CreateTeamRequest;
import com.collab.taskmanager.dto.response.TeamMemberResponse;
import com.collab.taskmanager.dto.response.TeamResponse;
import com.collab.taskmanager.entities.Team;
import com.collab.taskmanager.entities.TeamMember;
import com.collab.taskmanager.entities.User;
import com.collab.taskmanager.entities.UserPrincipal;
import com.collab.taskmanager.enums.TeamRole;
import com.collab.taskmanager.exceptions.TeamMemberNotFoundException;
import com.collab.taskmanager.exceptions.TeamNotFoundException;
import com.collab.taskmanager.exceptions.UserNotFoundException;
import com.collab.taskmanager.repos.TeamMembersRepo;
import com.collab.taskmanager.repos.TeamRepo;
import com.collab.taskmanager.repos.UserRepo;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TeamService {

    private final TeamRepo teamRepo;
    private final UserRepo userRepo;
    private final TeamMembersRepo teamMembersRepo;

    public TeamService(TeamRepo teamRepo, UserRepo userRepo, TeamMembersRepo teamMembersRepo) {
        this.teamRepo = teamRepo;
        this.userRepo = userRepo;
        this.teamMembersRepo = teamMembersRepo;
    }

    public TeamResponse createTeam(User user, CreateTeamRequest request) {
        Team team = new Team();
        team.setName(request.name());
        team.setDescription(request.description());
        team.setCreatedBy(user);
        Team savedTeam = teamRepo.save(team);

        TeamMember member = new TeamMember();
        member.setMember(user);
        member.setTeam(team);
        member.setTeamRole(TeamRole.OWNER);
        teamMembersRepo.save(member);
        return new TeamResponse(savedTeam.getId(), savedTeam.getName());
    }

    public void addMember(UserPrincipal currentUser, Long userId, Long teamId) {
        Long currentUserId = currentUser.getUser().getId();
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        User userToAdd = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        TeamMember currentMember = teamMembersRepo.findByTeamIdAndMemberId(teamId, currentUserId)
                .orElseThrow(() -> new TeamMemberNotFoundException());

        //Check if current user is Owner
        if (currentMember.getTeamRole() != TeamRole.OWNER) {
            throw new RuntimeException("No permission");
        }

        //check if user in the team
        boolean exists = teamMembersRepo.existsByTeamIdAndMemberId(teamId, userId);
        if (exists) {
            throw new RuntimeException("Already in the team");
        }

        //add user into team
        TeamMember newMember = new TeamMember();
        newMember.setMember(userToAdd);
        newMember.setTeamRole(TeamRole.MEMBER);
        newMember.setTeam(team);

        //save changes
        teamMembersRepo.save(newMember);

    }

    public TeamResponse getTeam(Long teamId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new TeamNotFoundException(teamId));
        return new TeamResponse(team.getId(), team.getName());
    }

    public List<TeamResponse> getUserTeams(UserPrincipal user) {
        Long userId = user.getUser().getId();

        List<TeamMember> memberships =
                teamMembersRepo.findByMemberId(userId);

        return memberships.stream()
                .map(tm -> new TeamResponse(
                        tm.getTeam().getId(),
                        tm.getTeam().getName()
                ))
                .toList();
    }

    public List<TeamMemberResponse> getTeamMembers(Long teamId) {
        List<TeamMember> teamMember = teamMembersRepo.findByTeamId(teamId);
        return teamMember.stream()
                .map(tm -> new TeamMemberResponse(
                        tm.getMember().getId(),
                        tm.getMember().getName()
                ))
                .toList();
    }

    @Transactional
    public void removeUserFromTeamById(Long teamId, Long userId) {
        if (!teamMembersRepo.existsByTeamIdAndMemberId(teamId, userId)) {
            throw new TeamMemberNotFoundException();
        }
        teamMembersRepo.deleteByTeamIdAndMemberId(teamId,userId);
    }
}
