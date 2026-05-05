package com.collab.taskmanager.entities;

import com.collab.taskmanager.enums.TeamRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "team_member")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TeamMember {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id")
    private Team team;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User member;
    @Enumerated(EnumType.STRING)
    private TeamRole teamRole;
    private LocalDateTime joinedAt;
    @PrePersist
    protected void onCreate() {
        joinedAt = LocalDateTime.now();
    }
}
