package com.attendanceMonitoringSystem.userManager.auth;

import com.attendanceMonitoringSystem.userManager.auth.dto.ChangePassword;
import com.attendanceMonitoringSystem.userManager.auth.dto.ResetPassword;
import com.attendanceMonitoringSystem.userManager.user.UserRepository;
import com.attendanceMonitoringSystem.userManager.user.UserService;
import com.attendanceMonitoringSystem.userManager.user.Users;
import com.attendanceMonitoringSystem.utils.CurrentlyLoggedInUser;
import com.attendanceMonitoringSystem.exceptions.customExceptions.BadRequestException;
import com.attendanceMonitoringSystem.exceptions.customExceptions.ForbiddenException;
import com.attendanceMonitoringSystem.utils.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final CurrentlyLoggedInUser loggedInUser;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public ResponseEntity<ApiResponse> changePassword(ChangePassword changePassword) {
        Users user = loggedInUser.getUser();
        validateOldPassword(user, changePassword.getOldPassword());
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        userRepository.save(user);

        return ApiResponse.success("Password Changed Successfully!");
    }

    // this method for admin.
    @Override
    @Transactional(rollbackFor = Exception.class)
//    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ApiResponse> resetPassword(String email, ResetPassword resetPassword) {
        Users inUser = loggedInUser.getUser();

        if (!inUser.getRole().getRoleName().equalsIgnoreCase("ADMIN"))
            throw new ForbiddenException("Access Denied: Only administrators are authorized to reset password.");

        Users user = userService.getUserByUsername(email);

        user.setPassword(passwordEncoder.encode(resetPassword.getNewPassword()));
        userRepository.save(user);

        return ApiResponse.success("Password has been reset successfully!");
    }

    private void validateOldPassword(Users user, String oldPassword) {
        boolean isPasswordMatch = passwordEncoder.matches(oldPassword, user.getPassword());
        if (!isPasswordMatch)
            throw new BadRequestException("Incorrect old Password!");
    }

}
