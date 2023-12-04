package com.attendanceMonitoringSystem.vote;

import com.attendanceMonitoringSystem.candidate.Candidate;
import com.attendanceMonitoringSystem.position.Position;
import com.attendanceMonitoringSystem.userManager.user.Users;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "ES_vote")
@SQLDelete(sql = "UPDATE ES_vote SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Data
@NoArgsConstructor
public class Vote {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "voter_user_id", nullable = false)
    private Users voter;

    @ManyToOne
    @JoinColumn(name = "candidate_id", nullable = false)
    private Candidate candidate;

    @ManyToOne
    @JoinColumn(name = "position_id", nullable = false)
    private Position position;

    private LocalDateTime voteDateTime;

    @Column(name = "deleted")
    private boolean deleted;

    public Vote(Users voter, Candidate candidate, Position position) {
        this.voter = voter;
        this.candidate = candidate;
        this.position = position;
        this.voteDateTime = LocalDateTime.now();
    }
}


