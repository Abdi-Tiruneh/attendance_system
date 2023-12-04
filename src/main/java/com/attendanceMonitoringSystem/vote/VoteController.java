package com.attendanceMonitoringSystem.vote;

import com.attendanceMonitoringSystem.utils.ApiResponse;
import com.attendanceMonitoringSystem.vote.dto.CandidateVoteResult;
import com.attendanceMonitoringSystem.vote.dto.VoteRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/votes")
@Tag(name = "Vote API.")
public class VoteController {

    private final VoteService voteService;

    public VoteController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PostMapping("/vote")
    public ResponseEntity<?> vote(@RequestBody @Valid VoteRequest voteRequest) {
        voteService.vote(voteRequest);
        return ApiResponse.success("Voted Successfully");

    }

    @GetMapping("/results/{positionId}")
    public ResponseEntity<List<CandidateVoteResult>> getVoteResultsByPositionId(@PathVariable Long positionId) {
        List<CandidateVoteResult> results = voteService.getVoteResultsByPositionId(positionId);
        return ResponseEntity.ok(results);
    }

}


