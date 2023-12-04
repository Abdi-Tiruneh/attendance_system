package com.attendanceMonitoringSystem.userManager.auth;

import com.attendanceMonitoringSystem.userManager.auth.dto.ChangePassword;
import com.attendanceMonitoringSystem.userManager.auth.dto.ResetPassword;
import com.attendanceMonitoringSystem.utils.ApiResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public interface AuthService {

    ResponseEntity<ApiResponse> changePassword(ChangePassword changePassword);

    ResponseEntity<ApiResponse> resetPassword(String phoneNumber, ResetPassword resetPassword);

}
