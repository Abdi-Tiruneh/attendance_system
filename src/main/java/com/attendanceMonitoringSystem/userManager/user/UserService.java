package com.attendanceMonitoringSystem.userManager.user;

import com.attendanceMonitoringSystem.team.dto.TeamResponse;
import com.attendanceMonitoringSystem.userManager.user.dto.UserRegistrationReq;
import com.attendanceMonitoringSystem.userManager.user.dto.UserResponse;
import com.attendanceMonitoringSystem.userManager.user.dto.UserUpdateReq;

import java.util.List;
import java.util.Set;

public interface UserService {
    UserResponse register(UserRegistrationReq userReq, String roleName);

    UserResponse me();

    List<UserResponse> getAllUsers(String role);

    List<TeamResponse> getUserTeams(Long userId);

    UserResponse editUser(UserUpdateReq updateReq);

    UserResponse editUser(Long userId, UserUpdateReq updateReq);

    Users getUserByUsername(String email);

    Users getUserById(Long userId);

    List<UserResponse> getUsersById(Set<Long> userIdList);

    void deleteUser(Long id);
}
