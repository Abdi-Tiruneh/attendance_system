package com.attendanceMonitoringSystem.userManager.user.dto;

import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserUpdateReq {
    @Size(min = 2, message = "Full name must be at least 2 characters")
    private String fullName;

    private String username;
}
