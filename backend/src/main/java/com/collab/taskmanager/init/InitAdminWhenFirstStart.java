package com.collab.taskmanager.init;

import com.collab.taskmanager.config.AdminProperties;
import com.collab.taskmanager.entities.User;
import com.collab.taskmanager.enums.Role;
import com.collab.taskmanager.repos.UserRepo;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class InitAdminWhenFirstStart implements CommandLineRunner {

    private final UserRepo userRepo;
    private final PasswordEncoder encoder;
    private final AdminProperties adminProperties;

    public InitAdminWhenFirstStart(UserRepo userRepo, PasswordEncoder encoder, AdminProperties adminProperties) {
        this.userRepo = userRepo;
        this.encoder = encoder;
        this.adminProperties = adminProperties;
    }

    @Override
    public void run(String... args) throws Exception {
       User user = new User();

       if (!userRepo.existsByRole(Role.ADMIN)){
           user.setName(adminProperties.getName());
           user.setPassword(encoder.encode(adminProperties.getPassword()));
           user.setEmail(adminProperties.getEmail());
           user.setRole(Role.ADMIN);
           userRepo.save(user);
       }

    }
}
