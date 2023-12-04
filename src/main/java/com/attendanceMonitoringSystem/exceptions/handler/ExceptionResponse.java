package com.attendanceMonitoringSystem.exceptions.handler;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
@Getter
public class ExceptionResponse {
    private String timeStamp;
    private HttpStatus error;
    private String message;
    private String requestPath;
}
