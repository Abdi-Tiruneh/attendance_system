package com.attendanceMonitoringSystem.userManager.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserRegistrationReq {

    @NotBlank(message = "Full name is required")
    @Size(min = 2, message = "Full name must be at least 2 characters")
    private String fullName;

    @NotBlank(message = "UserName is required")
    private String username;

    @NotBlank(message = "password is required")
    @Size(min = 4, max = 6, message = "password must be between 6 and 20 characters")
    private String password;
}