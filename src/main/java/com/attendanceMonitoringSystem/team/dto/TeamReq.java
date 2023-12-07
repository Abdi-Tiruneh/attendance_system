package com.attendanceMonitoringSystem.team.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class TeamReq {
    @NotBlank
    private String name;

    private String description;

    @NotNull
    private Long managerId;
}
