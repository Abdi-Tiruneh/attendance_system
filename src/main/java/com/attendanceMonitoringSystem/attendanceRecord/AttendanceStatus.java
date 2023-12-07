package com.attendanceMonitoringSystem.attendanceRecord;

public enum AttendanceStatus {
    PRESENT("Present"),
    ABSENT("Absent"),
    TO_BE_FILLED("To Be Filled");
    private final String status;

    AttendanceStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }
}
