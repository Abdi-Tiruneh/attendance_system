package com.attendanceMonitoringSystem.userManager.auth;

import com.attendanceMonitoringSystem.userManager.auth.dto.ChangePassword;
import com.attendanceMonitoringSystem.userManager.auth.dto.ResetPassword;
import com.attendanceMonitoringSystem.utils.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth API.")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PutMapping({"/change-password"})
    public ResponseEntity<ApiResponse> changePassword(@RequestBody @Valid ChangePassword changePassword) {
        return authService.changePassword(changePassword);
    }

    @PutMapping({"/reset-password/{username}"})
    public ResponseEntity<ApiResponse> changePassword(@PathVariable String username, @RequestBody @Valid ResetPassword resetPassword) {
        return authService.resetPassword(username, resetPassword);
    }

}


