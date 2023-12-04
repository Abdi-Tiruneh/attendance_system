package com.attendanceMonitoringSystem.vote;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoteRepository extends JpaRepository<Vote, Long> {
    List<Vote> findByCandidateId(Long candidateId);

    boolean existsByVoterIdAndPositionId(Long voterId,Long positionId);
}