package com.attendanceMonitoringSystem.attendanceRecord;

import com.attendanceMonitoringSystem.attendanceRecord.dto.AttendanceRecordReq;
import com.attendanceMonitoringSystem.exceptions.customExceptions.BadRequestException;
import com.attendanceMonitoringSystem.team.Team;
import com.attendanceMonitoringSystem.team.TeamRepository;
import com.attendanceMonitoringSystem.team.TeamService;
import com.attendanceMonitoringSystem.userManager.user.UserRepository;
import com.attendanceMonitoringSystem.userManager.user.UserService;
import com.attendanceMonitoringSystem.userManager.user.Users;
import com.attendanceMonitoringSystem.userManager.user.dto.UserResponse;
import com.attendanceMonitoringSystem.utils.CurrentlyLoggedInUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceRecordService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final CurrentlyLoggedInUser loggedInUser;
    private final UserService userService;
    private final TeamService teamService;
    private final AttendanceRecordRepository attendanceRecordRepository;

    @Transactional
    public void createAttendanceRecords(AttendanceRecordReq attendanceRecordReq) {
        Users managerOrAdmin = loggedInUser.getUser();

        // Only managers or admin users can create attendance record
        validateManagerOrAdmin(managerOrAdmin);

        Team team = teamService.getTeam(attendanceRecordReq.getTeamId());

        if (attendanceRecordReq.getAttendanceDuration() > 12)
            throw new BadRequestException("Attendance Duration must be a maximum of 12 months.");

        int duration = attendanceRecordReq.getAttendanceDuration() < 1 ? 7 : attendanceRecordReq.getAttendanceDuration() * 30;

        for (Long userId : team.getEnrolledUsers()) {
            LocalDate currentDate = LocalDate.now();
            for (int i = 0; i < duration; i++) {
                LocalDateTime currentDateTime = currentDate.atStartOfDay();

                // Check if an attendance record already exists for the specified day, user, and team
                if (attendanceRecordRepository.findByTeamIdAndUserIdAndDate(
                        attendanceRecordReq.getTeamId(), userId, currentDateTime).isEmpty()) {
                    AttendanceRecord attendanceRecord = new AttendanceRecord();
                    attendanceRecord.setUserId(userId);
                    attendanceRecord.setTeamId(team.getId());
                    attendanceRecord.setDate(currentDateTime);
                    attendanceRecord.setStatus(AttendanceStatus.TO_BE_FILLED);
                    attendanceRecord.setApproved(false);

                    attendanceRecordRepository.save(attendanceRecord);
                }

                currentDate = currentDate.plusDays(1);
            }
        }
    }


    @Transactional
    public Set<AttendanceRecord> getAttendanceRecords(Long teamId) {
        Users user = loggedInUser.getUser();
        Long userId = user.getId();
        return attendanceRecordRepository.findByTeamIdAndUserId(teamId, userId, Sort.by(Sort.Order.asc("id")));
    }

    @Transactional
    public AttendanceRecord fillAttendanceRecords(Long id) {
        Users user = loggedInUser.getUser();
        Long userId = user.getId();
        LocalDateTime currentDate = LocalDate.now().atStartOfDay();

        AttendanceRecord attendanceRecord = attendanceRecordRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new EntityNotFoundException("Attendance record not found"));

        if (!attendanceRecord.getDate().equals(currentDate))
            throw new BadRequestException("Attendance can only be filled for the current date.");

        if (attendanceRecord.getStatus() == AttendanceStatus.PRESENT)
            throw new BadRequestException("Attendance has already been marked as PRESENT for today.");

        attendanceRecord.setStatus(AttendanceStatus.PRESENT);

        return attendanceRecordRepository.save(attendanceRecord);
    }

    @Transactional
    public void enrollUsers(Long teamId, Set<Long> userIds) {
        Users managerOrAdmin = loggedInUser.getUser();

        // Only managers or admin users can enroll users
        validateManagerOrAdmin(managerOrAdmin);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        // Retrieve users from the database using user IDs
        List<Users> usersToEnroll = userRepository.findAllById(userIds);

        // Update each user and enroll in the team
        for (Users user : usersToEnroll) {
            user.enrollInClassTeam(team.getId());
            userRepository.save(user);
        }
        // Extract user IDs from the list of Users
        userIds = usersToEnroll.stream()
                .map(Users::getId)
                .collect(Collectors.toSet());

        team.getEnrolledUsers().addAll(userIds);

        teamRepository.save(team);
    }


    @Transactional
    public void removeUsersFromTeam(Long teamId, Set<Long> userIds) {
        Users managerOrAdmin = loggedInUser.getUser();

        // Only managers or admin users can remove users
        validateManagerOrAdmin(managerOrAdmin);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        // Retrieve users from the database using user IDs
        List<Users> usersToRemove = userRepository.findAllById(userIds);

        // Update each user and remove from the team
        for (Users user : usersToRemove) {
            user.leaveClassTeam(team.getId());
            userRepository.save(user);
        }

        // Extract user IDs from the list of Users
        userIds = usersToRemove.stream()
                .map(Users::getId)
                .collect(Collectors.toSet());

        // Remove users from the team
        team.getEnrolledUsers().removeAll(userIds);
        teamRepository.save(team);
    }

    @Transactional
    public void approveAttendanceRecords(Long teamId, Long userId) {
        Users inUserUser = loggedInUser.getUser();

        // Only managers can approve attendance records
        validateManagerUser(inUserUser);

        Team team = teamService.getTeam(teamId);

        if (!team.getManager().getId().equals(inUserUser.getId()))
            throw new AccessDeniedException("Only the team manager can approve attendance records.");

        List<AttendanceRecord> recordsToApprove = attendanceRecordRepository
                .findByTeamIdAndUserIdAndDateBeforeAndApprovedIsFalse(teamId, userId, LocalDate.now().atStartOfDay());

        if (recordsToApprove.isEmpty())
            throw new EntityNotFoundException("There is no record to approve");

        List<AttendanceRecord> approvedRecords = recordsToApprove.stream()
                .peek(record -> record.setApproved(true))
                .toList();

        attendanceRecordRepository.saveAll(approvedRecords);
    }

    public Set<UserResponse> getAllMembers(Long teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));

        // Retrieve users from the database using user IDs
        List<Users> users = userRepository.findAllById(team.getEnrolledUsers());
        return users
                .stream()
                .map(UserResponse::toResponse)
                .collect(Collectors.toSet());
    }


    public void deleteTeam(Long id) {
        Team team = teamRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
        teamRepository.delete(team);
    }

    private void validateAdminUser(Users admin) {
        if (!admin.getRole().getRoleName().equals("ADMIN"))
            throw new AccessDeniedException("Only admin users can perform this operation");
    }

    private void validateManagerUser(Users admin) {
        if (!admin.getRole().getRoleName().equals("MANAGER"))
            throw new AccessDeniedException("Only manager users can perform this operation");
    }

    private void validateManagerOrAdmin(Users managerOrAdmin) {
        String role = managerOrAdmin.getRole().getRoleName();
        if (!"ADMIN".equals(role) && !"MANAGER".equals(role))
            throw new AccessDeniedException("Only managers or admin users can perform this operation");
    }

}
