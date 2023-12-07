package com.attendanceMonitoringSystem.attendanceRecord.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class AttendanceRecordReq {
    @NotNull
    private Long teamId;

    @NotNull
    private Integer attendanceDuration;
}
