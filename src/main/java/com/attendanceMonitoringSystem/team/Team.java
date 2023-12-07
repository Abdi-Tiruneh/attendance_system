package com.attendanceMonitoringSystem.team;

import com.attendanceMonitoringSystem.userManager.user.Users;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "AM_teams")
@SQLDelete(sql = "UPDATE AM_teams SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Data
@NoArgsConstructor
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String description;

    @ManyToOne
    @JoinColumn(name = "manager_id")
    @JsonIgnore
    private Users manager;

    private Set<Long> enrolledUsers = new HashSet<>();

    @Column(name = "deleted")
    @JsonIgnore
    private boolean deleted;

}

