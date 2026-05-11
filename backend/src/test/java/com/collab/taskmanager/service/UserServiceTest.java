package com.collab.taskmanager.service;

import com.collab.taskmanager.dto.Mapper;
import com.collab.taskmanager.dto.response.GetMeResponse;
import com.collab.taskmanager.dto.response.SearchResponse;
import com.collab.taskmanager.entities.User;
import com.collab.taskmanager.enums.Role;
import com.collab.taskmanager.exceptions.UserNotFoundException;
import com.collab.taskmanager.repos.UserRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private Mapper mapper;

    @Mock
    private Authentication authentication;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepo,mapper);
    }

    @Test
    void getMe_ShouldReturnUserInfo() {
        // given
        String email = "test@gmail.com";

        User user = new User();
        user.setId(1L);
        user.setName("Nikita");
        user.setEmail(email);
        user.setRole(Role.USER);

        when(authentication.getName()).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        GetMeResponse response = userService.getMe(authentication);

        // then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Nikita", response.name());
        assertEquals(email, response.email());
        assertEquals(Role.USER, response.role());

        verify(authentication).getName();
        verify(userRepo).findByEmail(email);
    }

    @Test
    void getMe_ShouldThrowException_WhenUserNotFound() {
        // given
        String email = "test@gmail.com";

        when(authentication.getName()).thenReturn(email);
        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        // when + then
        assertThrows(UserNotFoundException.class,
                () -> userService.getMe(authentication));

        verify(authentication).getName();
        verify(userRepo).findByEmail(email);
    }

    @Test
    void searchUser_ShouldReturnUser() {
        // given
        String email = "test@gmail.com";

        User user = new User();
        user.setId(1L);
        user.setName("Nikita");

        when(userRepo.findByEmail(email)).thenReturn(Optional.of(user));

        // when
        SearchResponse response = userService.searchUserByUsername(email);

        // then
        assertNotNull(response);
        assertEquals(1L, response.id());
        assertEquals("Nikita", response.name());

        verify(userRepo).findByEmail(email);
    }

    @Test
    void searchUser_ShouldThrowException_WhenUserNotFound() {
        // given
        String email = "test@gmail.com";

        when(userRepo.findByEmail(email)).thenReturn(Optional.empty());

        // when + then
        assertThrows(UserNotFoundException.class,
                () -> userService.searchUserByUsername(email));

        verify(userRepo).findByEmail(email);
    }
}