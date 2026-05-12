package com.collab.taskmanager.controller;

import com.collab.taskmanager.dto.response.GetProjectMembersNameResponse;
import com.collab.taskmanager.entities.UserPrincipal;
import com.collab.taskmanager.service.ProjectService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/project")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/{teamId}/")
    public void createProject(@PathVariable("teamId") Long teamId, @AuthenticationPrincipal UserPrincipal userPrincipal){
        projectService.createProject(teamId,userPrincipal);
    }

    @GetMapping("/{projectId}/")
    public ResponseEntity<List<GetProjectMembersNameResponse>> getProjectMembers(@PathVariable Long projectId, @AuthenticationPrincipal UserPrincipal currentUser){
        return ResponseEntity.ok(projectService.getProjectMembers(projectId, currentUser));
    }

}
