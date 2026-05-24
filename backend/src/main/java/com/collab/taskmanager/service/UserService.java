package com.collab.taskmanager.service;

import com.collab.taskmanager.dto.Mapper;
import com.collab.taskmanager.dto.response.DashboardStatsResponse;
import com.collab.taskmanager.dto.response.GetMeResponse;
import com.collab.taskmanager.dto.response.GetUsernameAndIdResponse;
import com.collab.taskmanager.dto.response.SearchResponse;
import com.collab.taskmanager.entities.User;
import com.collab.taskmanager.exceptions.UserNotFoundException;
import com.collab.taskmanager.repos.ProjectRepo;
import com.collab.taskmanager.repos.TaskRepo;
import com.collab.taskmanager.repos.TeamRepo;
import com.collab.taskmanager.repos.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final TeamRepo teamRepo;
    private final ProjectRepo projectRepo;
    private final TaskRepo taskRepo;
    private final Mapper mapper;

    public UserService(UserRepo userRepo, TeamRepo teamRepo, ProjectRepo projectRepo, TaskRepo taskRepo, Mapper mapper) {
        this.userRepo = userRepo;
        this.teamRepo = teamRepo;
        this.projectRepo = projectRepo;
        this.taskRepo = taskRepo;
        this.mapper = mapper;
    }

    public GetMeResponse getMe(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));

        return new GetMeResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole()
        );
    }

    public List<GetUsernameAndIdResponse> getAllUsers() {
        return userRepo.findAll().stream().map(mapper::toUserDTO).collect(Collectors.toList());
    }

    //    public SearchResponse searchUser(String email) {
//        User user = userRepo.findByEmail(email)
//                .orElseThrow(() -> new UserNotFoundException(email));
//        return new SearchResponse(user.getId(),user.getName());
//    }
    public SearchResponse searchUserByUsername(String username) {
        User user = userRepo.findByName(username)
                .orElseThrow(() -> new RuntimeException("User with this username not found"));
        return new SearchResponse(user.getId(), user.getName());
    }

    public DashboardStatsResponse getDashboardStats(String email) {

        User user = userRepo.findByEmail(email).orElseThrow(() -> new RuntimeException("User not found"));

        long teams = teamRepo.countByMembersMemberId(user.getId());
        long projects = projectRepo.countByTeamMembersMemberId(user.getId());
        long assignedTasks = taskRepo.countByAssignedUser_Id(user.getId());
        long createdTasks = taskRepo.countByCreatedBy_Id(user.getId());

        return new DashboardStatsResponse(
                teams,
                projects,
                assignedTasks,
                createdTasks
        );
    }
}
