package com.collab.taskmanager.controller;

import com.collab.taskmanager.dto.request.NameAndDescriptionRequest;
import com.collab.taskmanager.dto.request.UpdateProjectRequest;
import com.collab.taskmanager.dto.response.GetProjectMembersNameResponse;
import com.collab.taskmanager.dto.response.ProjectResponse;
import com.collab.taskmanager.entities.UserPrincipal;
import com.collab.taskmanager.service.ProjectService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
@Tag(name = "Project", description = "Project endpoints")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }
    @Operation(
            summary = "Create project",
            description = "Create project by team Owner"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User or team not found"),
            @ApiResponse(responseCode = "409", description = "User is not Team member")
    })
    @PostMapping("/{teamId}")
    public void createProject(@PathVariable("teamId") Long teamId, @AuthenticationPrincipal UserPrincipal userPrincipal, @RequestBody NameAndDescriptionRequest req){
        projectService.createProject(teamId,userPrincipal,req);
    }

    @Operation(
            summary = "Get members",
            description = "Get all members who has access to the project"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project created successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "409", description = "User is not Team member")
    })
    @GetMapping("/{projectId}")
    public ResponseEntity<List<GetProjectMembersNameResponse>> getProjectMembers(@PathVariable Long projectId, @AuthenticationPrincipal UserPrincipal currentUser){
        return ResponseEntity.ok(projectService.getProjectMembers(projectId, currentUser));
    }

    @Operation(
            summary = "Update project",
            description = "Update project name or description"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project updated successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "User is not owner"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "409", description = "User is not team member")
    })
    @PatchMapping("/{projectId}")
    public ResponseEntity<Void> updateProject(
            @PathVariable Long projectId,
            @RequestBody UpdateProjectRequest request,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        projectService.updateProject(projectId, request, userPrincipal);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Delete project",
            description = "Delete project by id"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Project deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "User is not owner"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "409", description = "User is not team member")
    })
    @DeleteMapping("/{projectId}")
    public ResponseEntity<Void> deleteProject(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipal userPrincipal
    ) {

        projectService.deleteProject(projectId, userPrincipal);

        return ResponseEntity.noContent().build();
    }

    @Operation(
            summary = "Get project by id",
            description = "Returns project details"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Project found"),
            @ApiResponse(responseCode = "404", description = "Project not found"),
            @ApiResponse(responseCode = "403", description = "Access denied")
    })
    @GetMapping("/{projectId}/details")
    public ResponseEntity<ProjectResponse> getProjectById(
            @PathVariable Long projectId,
            @AuthenticationPrincipal UserPrincipal currentUser
    ) {
        return ResponseEntity.ok(projectService.getProjectById(projectId, currentUser));
    }

}
