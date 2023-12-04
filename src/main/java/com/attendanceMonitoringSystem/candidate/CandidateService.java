package com.attendanceMonitoringSystem.candidate;

import com.attendanceMonitoringSystem.candidate.dto.CandidateReq;
import com.attendanceMonitoringSystem.candidate.dto.CandidateResponse;

import java.util.List;

public interface CandidateService {

    List<CandidateResponse> getAllCandidates();

    Candidate getCandidateById(Long id);

    List<CandidateResponse> getCandidatesByPositionId(Long positionId);

    CandidateResponse createCandidate(CandidateReq candidateReq);

    void deleteCandidate(Long id);
}

