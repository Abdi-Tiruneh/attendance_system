package com.attendanceMonitoringSystem.userManager.user;

import com.attendanceMonitoringSystem.exceptions.customExceptions.ForbiddenException;
import com.attendanceMonitoringSystem.team.dto.TeamResponse;
import com.attendanceMonitoringSystem.userManager.user.dto.UserRegistrationReq;
import com.attendanceMonitoringSystem.userManager.user.dto.UserResponse;
import com.attendanceMonitoringSystem.userManager.user.dto.UserUpdateReq;
import com.attendanceMonitoringSystem.utils.CurrentlyLoggedInUser;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
@Tag(name = "User API.")
public class UserController {

    private final UserService userService;
    private final CurrentlyLoggedInUser loggedInUser;

    public UserController(UserService userService, CurrentlyLoggedInUser loggedInUser) {
        this.userService = userService;
        this.loggedInUser = loggedInUser;
    }

    @GetMapping("/me")
    public ResponseEntity<UserResponse> getMe() {
        return ResponseEntity.ok(userService.me());
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers(@RequestParam(required = false) String role) {
        return ResponseEntity.ok(userService.getAllUsers(role));
    }

    @GetMapping("/{userId}/teams")
    public ResponseEntity<List<TeamResponse>> getUserTeams(@PathVariable Long userId) {
        return ResponseEntity.ok(userService.getUserTeams(userId));
    }

    @PostMapping
    public ResponseEntity<UserResponse> register(@RequestBody @Valid UserRegistrationReq userReq) {
        UserResponse user = userService.register(userReq, "USER");
        return ResponseEntity.status(HttpStatus.CREATED).body(user);
    }

    @PostMapping("/manager")
    public ResponseEntity<UserResponse> registerManager(@RequestBody @Valid UserRegistrationReq userReq) {
        Users user = loggedInUser.getUser();
        if (!user.getRole().getRoleName().equalsIgnoreCase("ADMIN"))
            throw new ForbiddenException("Access Denied: Only administrators are authorized to create managers.");

        UserResponse response = userService.register(userReq, "MANAGER");
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping
    public ResponseEntity<UserResponse> editUser(@RequestBody @Valid UserUpdateReq updateReq) {
        return ResponseEntity.ok(userService.editUser(updateReq));
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> editUser(@PathVariable Long userId, @RequestBody @Valid UserUpdateReq updateReq) {
        return ResponseEntity.ok(userService.editUser(userId, updateReq));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}


