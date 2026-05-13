package com.collab.taskmanager.controller;

import com.collab.taskmanager.dto.request.AddMemberRequest;
import com.collab.taskmanager.dto.request.CreateTeamRequest;
import com.collab.taskmanager.dto.response.ProjectResponse;
import com.collab.taskmanager.dto.response.TeamDetailsResponse;
import com.collab.taskmanager.dto.response.TeamMemberResponse;
import com.collab.taskmanager.dto.response.TeamResponse;
import com.collab.taskmanager.entities.UserPrincipal;
import com.collab.taskmanager.service.TeamService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(
            summary = "Create team",
            description = "Creates a new team for authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team successfully created"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @PostMapping("/createTeam")
    public ResponseEntity<TeamResponse> createTeam(
            @AuthenticationPrincipal UserPrincipal userDetail,
            @RequestBody CreateTeamRequest request){

        return ResponseEntity.ok(
                teamService.createTeam(userDetail.getUser(), request)
        );
    }


    @Operation(
            summary = "Get user teams",
            description = "Returns all teams for authenticated user"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Teams retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping
    public List<TeamResponse> getMyTeams(
            @AuthenticationPrincipal UserPrincipal user) {

        return teamService.getUserTeams(user);
    }


    @Operation(
            summary = "Get team by id",
            description = "Returns information about specific team"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Team found"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @GetMapping("/{teamId}")
    public TeamDetailsResponse getTeam(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long teamId) {
        return teamService.getTeam(user, teamId);
    }


    @Operation(
            summary = "Get team members",
            description = "Returns all members of specified team"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Members retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Team not found")
    })
    @GetMapping("/{teamId}/getMembers")
    public ResponseEntity<List<TeamMemberResponse>> getTeamMembers(
            @PathVariable Long teamId){

        return ResponseEntity.ok(
                teamService.getTeamMembers(teamId)
        );
    }


    @Operation(
            summary = "Add member to team",
            description = "Adds user to specified team"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User added successfully"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User or team not found")
    })
    @PostMapping("/{teamId}/members")
    public ResponseEntity<String> addMember(
            @AuthenticationPrincipal UserPrincipal userDetails,
            @PathVariable Long teamId,
            @RequestBody AddMemberRequest request){

        teamService.addMember(
                userDetails,
                request.userId(),
                teamId
        );

        return ResponseEntity.ok("User added");
    }

    @Operation(
            summary = "Remove user from team",
            description = "Removes specified user from specified team"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User removed successfully"),
            @ApiResponse(responseCode = "404", description = "User or team member not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @DeleteMapping("/{teamId}/members/{userId}")
    public ResponseEntity<Void> removeUserFromTeamById(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long userId,
            @PathVariable Long teamId){
        teamService.removeUserFromTeamById(currentUser, teamId,userId);
        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get team projects",
            description = "Returns all projects belonging to a specific team"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projects retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Team not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{teamId}/projects")
    public ResponseEntity<List<ProjectResponse>> getTeamProjects(
            @AuthenticationPrincipal UserPrincipal user,
            @PathVariable Long teamId
    ) {
        return ResponseEntity.ok(
                teamService.getTeamProjects(user, teamId)
        );
    }
}