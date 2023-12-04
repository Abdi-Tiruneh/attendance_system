package com.attendanceMonitoringSystem.userManager.user;

import com.attendanceMonitoringSystem.userManager.user.dto.UserRegistrationReq;
import com.attendanceMonitoringSystem.userManager.user.dto.UserResponse;
import com.attendanceMonitoringSystem.userManager.user.dto.UserUpdateReq;

import java.util.List;

public interface UserService {
    UserResponse register(UserRegistrationReq userReq, String roleName);

    UserResponse me();

    List<UserResponse> getAllUsers();

    UserResponse editUser(UserUpdateReq updateReq);

    Users getUserByUsername(String email);

    Users getUserById(Long userId);

    void deleteUser(Long id);
}
