package com.attendanceMonitoringSystem.vote;

import com.attendanceMonitoringSystem.vote.dto.CandidateVoteResult;
import com.attendanceMonitoringSystem.vote.dto.VoteRequest;

import java.util.List;


public interface VoteService {
    List<Vote> getVotesByCandidateId(Long candidateId);

    void vote(VoteRequest voteRequest);

    List<CandidateVoteResult> getVoteResultsByPositionId(Long positionId);
}


