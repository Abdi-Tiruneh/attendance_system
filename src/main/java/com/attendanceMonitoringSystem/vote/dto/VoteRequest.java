package com.attendanceMonitoringSystem.vote.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class VoteRequest {

    @NotNull
    private Long voterId;

    @NotNull
    private Long candidateId;

    @NotNull
    private Long positionId;
}
