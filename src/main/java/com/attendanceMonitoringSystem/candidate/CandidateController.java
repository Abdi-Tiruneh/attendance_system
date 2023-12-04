package com.attendanceMonitoringSystem.candidate;

import com.attendanceMonitoringSystem.candidate.dto.CandidateReq;
import com.attendanceMonitoringSystem.candidate.dto.CandidateResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/candidates")
@Tag(name = "Candidate API.")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @GetMapping
    public List<CandidateResponse> getAllCandidates() {
        return candidateService.getAllCandidates();
    }


    @GetMapping("/position/{positionId}")
    public ResponseEntity<List<CandidateResponse>> getCandidatesByPositionId(@PathVariable Long positionId) {
        return ResponseEntity.ok(candidateService.getCandidatesByPositionId(positionId));
    }

    @PostMapping
    public ResponseEntity<CandidateResponse> createCandidate(@RequestBody @Valid CandidateReq candidateReq) {
        CandidateResponse createdCandidate = candidateService.createCandidate(candidateReq);
        return new ResponseEntity<>(createdCandidate, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCandidate(@PathVariable Long id) {
        candidateService.deleteCandidate(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}


