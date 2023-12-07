package com.attendanceMonitoringSystem.attendanceRecord;

import com.attendanceMonitoringSystem.team.Team;
import com.attendanceMonitoringSystem.userManager.user.Users;
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

    @ManyToOne
    private Users user;

    private LocalDateTime date;

    @Enumerated(EnumType.STRING)
    private AttendanceStatus status;

    @ManyToOne
    private Team team;

}
