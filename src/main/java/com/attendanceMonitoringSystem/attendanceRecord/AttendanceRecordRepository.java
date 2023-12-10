package com.attendanceMonitoringSystem.attendanceRecord;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface AttendanceRecordRepository extends JpaRepository<AttendanceRecord, Long> {
    Optional<AttendanceRecord> findByIdAndUserId(Long id, Long userId);

    Optional<AttendanceRecord> findByTeamIdAndUserIdAndDate(Long teamId, Long userId, LocalDateTime date);

    Set<AttendanceRecord> findByTeamIdAndUserId(Long teamId, Long userId, Sort id);

    List<AttendanceRecord> findByStatusAndDateBefore(AttendanceStatus attendanceStatus, LocalDateTime currentDate);

    List<AttendanceRecord> findByTeamIdAndUserIdInAndApprovedIsFalseAndStatusNot(
            Long teamId, Set<Long> userIds, AttendanceStatus status);

    List<AttendanceRecord> findByTeamIdAndApprovedIsFalseAndStatusNot(Long teamId, AttendanceStatus status);

}

