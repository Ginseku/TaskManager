package com.collab.taskmanager.controller;

import com.collab.taskmanager.dto.request.AddMemberRequest;
import com.collab.taskmanager.dto.request.CreateTeamRequest;
import com.collab.taskmanager.dto.response.TeamResponse;
import com.collab.taskmanager.entities.UserPrincipal;
import com.collab.taskmanager.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/teams")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/createTeam")
    public ResponseEntity<TeamResponse> createTeam(@AuthenticationPrincipal UserPrincipal userDetail, @RequestBody CreateTeamRequest request){
        return teamService.createTeam(userDetail.getUser(), request);
    }

    @GetMapping
    public List<TeamResponse> getMyTeams(@AuthenticationPrincipal UserPrincipal user) {
        return teamService.getUserTeams(user);
    }

    @GetMapping("/{teamId}")
    public TeamResponse getTeam(@PathVariable Long teamId) {
        return teamService.getTeam(teamId);
    }

    @PostMapping("/{teamId}/members")
    public ResponseEntity<String> addMember(
            @AuthenticationPrincipal UserPrincipal userDetails,
            @PathVariable Long teamId,
            @RequestBody AddMemberRequest request){
        teamService.addMember(userDetails,request.userId(),teamId);
        return ResponseEntity.ok("User added");
    }

}
