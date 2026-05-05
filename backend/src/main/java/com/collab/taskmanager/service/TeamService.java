package com.collab.taskmanager.service;

import com.collab.taskmanager.entities.Team;
import com.collab.taskmanager.entities.TeamMember;
import com.collab.taskmanager.entities.User;
import com.collab.taskmanager.enums.TeamRole;
import com.collab.taskmanager.repos.TeamMembersRepo;
import com.collab.taskmanager.repos.TeamRepo;
import com.collab.taskmanager.repos.UserRepo;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

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

    public ResponseEntity<String> createTeam(User user){
        Team team = new Team();
        team.setName("Test team");
        team.setDescription("Test team description");
        team.setCreatedBy(user);
        teamRepo.save(team);

        TeamMember member = new TeamMember();
        member.setMember(user);
        member.setTeam(team);
        member.setTeamRole(TeamRole.OWNER);
        teamMembersRepo.save(member);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
