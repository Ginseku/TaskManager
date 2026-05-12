package com.collab.taskmanager.service;

import com.collab.taskmanager.dto.request.CreateTeamRequest;
import com.collab.taskmanager.dto.response.TeamDetailsResponse;
import com.collab.taskmanager.dto.response.TeamMemberResponse;
import com.collab.taskmanager.dto.response.TeamResponse;
import com.collab.taskmanager.entities.Team;
import com.collab.taskmanager.entities.TeamMember;
import com.collab.taskmanager.entities.User;
import com.collab.taskmanager.entities.UserPrincipal;
import com.collab.taskmanager.enums.TeamRole;
import com.collab.taskmanager.exceptions.TeamMemberNotFound;
import com.collab.taskmanager.exceptions.TeamNotFound;
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
                .orElseThrow(() -> new TeamNotFound(teamId));
        User userToAdd = userRepo.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId));
        TeamMember currentMember = teamMembersRepo.findByTeamIdAndMemberId(teamId, currentUserId)
                .orElseThrow(() -> new TeamMemberNotFound());

        //Check if current user is Owner or Admin
        /*if (currentMember.getTeamRole() != TeamRole.OWNER) {
            throw new RuntimeException("No permission");
        }*/
        if (!canManageTeam(currentUser, team)) {
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

    public TeamDetailsResponse getTeam(UserPrincipal currentUser, Long teamId) {
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new TeamNotFound(teamId));
        return new TeamDetailsResponse(
                team.getId(),
                team.getName(),
                canManageTeam(currentUser, team)
        );
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
    public void removeUserFromTeamById(UserPrincipal currentUser, Long teamId, Long userId) {
        /*
        if (!teamMembersRepo.existsByTeamIdAndMemberId(teamId, userId)) {
            throw new TeamMemberNotFound();
        }
        teamMembersRepo.deleteByTeamIdAndMemberId(teamId,userId);*/
        Team team = teamRepo.findById(teamId)
                .orElseThrow(() -> new TeamNotFound(teamId));

        if (!canManageTeam(currentUser, team)) {
            throw new RuntimeException("No permission");
        }
        if (!teamMembersRepo.existsByTeamIdAndMemberId(teamId, userId)) {
            throw new TeamMemberNotFound();
        }

        teamMembersRepo.deleteByTeamIdAndMemberId(teamId, userId);
    }

    //Helper for checking Team management
    private boolean canManageTeam(UserPrincipal currentUser, Team team) {
        return team.getCreatedBy().getId().equals(currentUser.getUser().getId())
                || currentUser.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
