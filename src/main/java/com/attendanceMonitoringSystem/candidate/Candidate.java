package com.attendanceMonitoringSystem.candidate;

import com.attendanceMonitoringSystem.position.Position;
import com.attendanceMonitoringSystem.userManager.user.Users;
import com.attendanceMonitoringSystem.vote.Vote;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ES_candidate")
@SQLDelete(sql = "UPDATE ES_candidate SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Data
@NoArgsConstructor
public class Candidate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    @OneToMany(mappedBy = "candidate", cascade = CascadeType.ALL)
    private List<Vote> votes = new ArrayList<>();

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "deleted")
    private boolean deleted;

    public Candidate(Users user, Position position) {
        this.user = user;
        this.position = position;
    }

}
