package com.collab.taskmanager.repos;

import com.collab.taskmanager.entities.User;
import com.collab.taskmanager.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    boolean existByRole(Role role);
}
