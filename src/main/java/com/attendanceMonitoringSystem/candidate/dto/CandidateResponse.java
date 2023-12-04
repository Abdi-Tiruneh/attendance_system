package com.attendanceMonitoringSystem.candidate.dto;

import com.attendanceMonitoringSystem.candidate.Candidate;
import com.attendanceMonitoringSystem.userManager.user.Users;
import com.attendanceMonitoringSystem.userManager.user.dto.UserResponse;
import lombok.Data;

@Data
public class CandidateResponse {

    private Long id;
    private String user;
    private String position;
    private Integer voteCount;

    public static CandidateResponse toResponse(Candidate candidate) {

        CandidateResponse candidateResponse = new CandidateResponse();
        candidateResponse.setId(candidate.getId());
        candidateResponse.setUser(candidate.getUser().getFullName());
        candidateResponse.setPosition(candidate.getPosition().getName());
        candidateResponse.setVoteCount(candidate.getVotes() != null ? candidate.getVotes().size() : 0);

        return candidateResponse;
    }

}
