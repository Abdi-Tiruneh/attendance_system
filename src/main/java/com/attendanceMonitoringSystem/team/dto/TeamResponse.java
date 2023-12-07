package com.attendanceMonitoringSystem.team.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class TeamResponse {
    private Long id;
    private String name;
    private String manager;
    private String description;
}
