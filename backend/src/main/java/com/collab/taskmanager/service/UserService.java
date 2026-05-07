package com.collab.taskmanager.service;

import com.collab.taskmanager.dto.Mapper;
import com.collab.taskmanager.dto.response.GetMeResponse;
import com.collab.taskmanager.dto.response.UserDTO;
import com.collab.taskmanager.dto.response.SearchResponse;
import com.collab.taskmanager.dto.response.UserDTO;
import com.collab.taskmanager.entities.User;
import com.collab.taskmanager.exceptions.UserNotFoundException;
import com.collab.taskmanager.repos.UserRepo;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepo userRepo;
    private final Mapper mapper;

    public UserService(UserRepo userRepo, Mapper mapper) {
        this.userRepo = userRepo;
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

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().map(mapper::toUserDTO).collect(Collectors.toList());
    }

    public SearchResponse searchUser(String email) {
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException(email));
        return new SearchResponse(user.getId(),user.getName());
    }
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().map(mapper::toUserDTO).collect(Collectors.toList());
    }

}
