package com.attendanceMonitoringSystem.attendanceRecord;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

@Entity
@Table(name = "AM_attendance_record")
@SQLDelete(sql = "UPDATE AM_attendance_record SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
@Data
@NoArgsConstructor
public class AttendanceRecord {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId;

    private Long teamId;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    private boolean approved;

    @Column(name = "deleted")
    @JsonIgnore
    private boolean deleted;
}
