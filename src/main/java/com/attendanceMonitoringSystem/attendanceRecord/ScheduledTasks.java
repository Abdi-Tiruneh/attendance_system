package com.attendanceMonitoringSystem.attendanceRecord;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ScheduledTasks {

    private final AttendanceRecordRepository attendanceRecordRepository;

    // Inject your repository through constructor
    public ScheduledTasks(AttendanceRecordRepository attendanceRecordRepository) {
        this.attendanceRecordRepository = attendanceRecordRepository;
    }

    @Scheduled(cron = "0 0 0 * * ?")  // Run every midnight
    @Transactional
    public void updatePastAttendanceRecords() {
        LocalDateTime currentDate = LocalDate.now().atStartOfDay();

        // Find attendance records with status TO_BE_FILLED and date before today
        List<AttendanceRecord> recordsToUpdate = attendanceRecordRepository
                .findByStatusAndDateBefore(AttendanceStatus.TO_BE_FILLED, currentDate);

        // Update status to ABSENT
        recordsToUpdate.forEach(record -> {
            record.setStatus(AttendanceStatus.ABSENT);
            attendanceRecordRepository.save(record);
        });
    }
}
