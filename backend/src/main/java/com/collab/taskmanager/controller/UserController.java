package com.collab.taskmanager.controller;

import com.collab.taskmanager.dto.response.*;
import com.collab.taskmanager.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
@Tag(name = "User Service", description = "Users endpoints")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }


    @ApiResponse(responseCode = "200", description = "Successes")
    @ApiResponse(responseCode = "401", description = "Unauthorized - user is not authenticated")
    @ApiResponse(responseCode = "403", description = "Forbidden")
    @GetMapping("/me")
    public ResponseEntity<GetMeResponse> getMe(Authentication authentication){
        return new ResponseEntity<>(
                userService.getMe(authentication),
                HttpStatus.OK
        );
    }


    @Operation(
            summary = "Get all users",
            description = "Returns a list of all users"
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Users fetched successfully"
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "Internal server error"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Unauthorized - user is not authenticated"
            )
    })
    @GetMapping("/getAll")
    public ResponseEntity<Response<List<GetUsernameAndIdResponse>>> getAllUsers() {
        return new ResponseEntity<>(
                Response.success(userService.getAllUsers()), HttpStatus.OK
        );
    }

//    @Operation(
//            summary = "Search user by email",
//            description = "Returns basic user information for the specified email address"
//    )
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "User found"),
//            @ApiResponse(responseCode = "404", description = "User not found")
//    })
//    @GetMapping("/search")
//    public SearchResponse searchUserByEmail(@RequestParam String email){
//        return userService.searchUser(email);
//    }
@Operation(
            summary = "Search user by username",
            description = "Returns basic user information for the specified username"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/search")
    public SearchResponse searchUserByUsername(@RequestParam String username){
        return userService.searchUserByUsername(username);
    }

    @Operation(
            summary = "Get dashboard statistics",
            description = "Returns dashboard statistics for the authenticated user including total teams, projects, and tasks"
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Dashboard statistics retrieved successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @GetMapping("/me/dashboard")
    public DashboardStatsResponse getDashboard(
            Authentication authentication
    ) {
        return userService.getDashboardStats(authentication.getName());
    }

}
