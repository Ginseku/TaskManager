package com.collab.taskmanager.controller;

import com.collab.taskmanager.entities.User;
import com.collab.taskmanager.entities.UserPrincipal;
import com.collab.taskmanager.service.TeamService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/team")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }

    @PostMapping("/createTeam")
    public ResponseEntity<String> createTeam(@AuthenticationPrincipal UserPrincipal userDetail){
        return teamService.createTeam(userDetail.getUser());
    }


}
