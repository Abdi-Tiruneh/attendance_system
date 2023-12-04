package com.attendanceMonitoringSystem.userManager.auth.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ResetPassword {
    @Size(min = 6, message = "Password must be at least 4 characters long")
    @NotEmpty(message = "newPassword is required")
    private String newPassword;
}
