package com.collab.taskmanager.controller;

import com.collab.taskmanager.entities.UserPrincipal;
import com.collab.taskmanager.service.ProjectService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


}
