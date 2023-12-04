package com.attendanceMonitoringSystem.exceptions.customExceptions;

public class ForbiddenException extends RuntimeException {
    public ForbiddenException(String message) {
        super(message);
    }
}
