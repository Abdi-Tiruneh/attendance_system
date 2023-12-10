package com.attendanceMonitoringSystem.attendanceRecord;

import com.attendanceMonitoringSystem.attendanceRecord.dto.AttendanceRecordReq;
import com.attendanceMonitoringSystem.utils.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;


@RestController
@RequestMapping("/api/v1/attendance-records")
@Tag(name = "Attendance Record API.")
public class AttendanceRecordController {
    private final AttendanceRecordService attendanceRecordService;

    public AttendanceRecordController(AttendanceRecordService attendanceRecordService) {
        this.attendanceRecordService = attendanceRecordService;
    }

    @GetMapping("/teams/{teamId}")
    public ResponseEntity<Set<AttendanceRecord>> getAttendanceRecords(@PathVariable Long teamId) {
        return ResponseEntity.ok(attendanceRecordService.getAttendanceRecords(teamId));
    }


    @GetMapping("/team/{teamId}/user/{userId}")
    public ResponseEntity<Set<AttendanceRecord>> getUnapprovedAttendanceRecords(@PathVariable Long teamId, @PathVariable Long userId) {
        return ResponseEntity.ok(attendanceRecordService.getUnapprovedAttendanceRecords(teamId, userId));
    }

    @PostMapping
    public ResponseEntity<?> createAttendanceRecords(@RequestBody @Valid AttendanceRecordReq attendanceRecordReq) {
        attendanceRecordService.createAttendanceRecords(attendanceRecordReq);
        return ApiResponse.success("Created Successfully");
    }

    @PutMapping("/{id}/fill")
    public ResponseEntity<?> fillAttendanceRecords(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceRecordService.fillAttendanceRecords(id));
    }

    @PutMapping("/approve/team/{teamId}")
    public ResponseEntity<?> approveAttendanceRecords(@PathVariable Long teamId, @RequestBody Set<AttendanceRecord> recordsToApprove) {
        attendanceRecordService.approveAttendanceRecords(teamId, recordsToApprove);
        return ApiResponse.success("Approved Successfully");
    }

}