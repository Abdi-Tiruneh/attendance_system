package com.attendanceMonitoringSystem.userManager.user.dto;

import com.attendanceMonitoringSystem.userManager.user.Users;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserResponse {

    private Long userId;
    private String fullName;
    private String username;
    private String role;
    private LocalDateTime lastLoggedIn;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static UserResponse toResponse(Users user) {
        return UserResponse.builder()
                .userId(user.getId())
                .fullName(user.getFullName())
                .username(user.getUsername())
                .role(user.getRole().getRoleName())
                .lastLoggedIn(user.getLastLoggedIn())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}
