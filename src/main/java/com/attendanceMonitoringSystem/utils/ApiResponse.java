package com.attendanceMonitoringSystem.utils;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Data
@AllArgsConstructor
public class ApiResponse<T> {
    private HttpStatus httpStatus;
    private String message;

    public static ResponseEntity<ApiResponse> success(String message) {
        ApiResponse apiResponse = new ApiResponse(HttpStatus.OK, message);

        return ResponseEntity.ok(apiResponse);
    }

    public static ResponseEntity<ApiResponse> error(HttpStatus status, String message) {
        return ResponseEntity.status(status).body(new ApiResponse<>(status, message));
    }
}

