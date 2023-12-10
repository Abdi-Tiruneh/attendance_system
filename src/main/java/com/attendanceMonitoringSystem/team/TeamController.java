package com.attendanceMonitoringSystem.team;

import com.attendanceMonitoringSystem.team.dto.TeamReq;
import com.attendanceMonitoringSystem.team.dto.TeamResponse;
import com.attendanceMonitoringSystem.userManager.user.dto.UserResponse;
import com.attendanceMonitoringSystem.utils.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;


@RestController
@RequestMapping("/api/v1/teams")
@Tag(name = "Team API.")
public class TeamController {

    private final TeamService teamService;

    public TeamController(TeamService teamService) {
        this.teamService = teamService;
    }


    @GetMapping("/{id}/members")
    public ResponseEntity<Set<UserResponse>> getMe(@PathVariable Long id) {
        return ResponseEntity.ok(teamService.getAllMembers(id));
    }

    @GetMapping
    public ResponseEntity<List<TeamResponse>> getAllTeams(@RequestParam(required = false) Long managerId) {
        return ResponseEntity.ok(teamService.getAllTeams(managerId));
    }

    @PostMapping
    public ResponseEntity<TeamResponse> createTeam(@RequestBody @Valid TeamReq teamReq) {
        TeamResponse response = teamService.createTeam(teamReq);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TeamResponse> createTeam(@PathVariable Long id, @RequestBody @Valid TeamReq teamReq) {
        TeamResponse response = teamService.updateTeam(id, teamReq);
        return ResponseEntity.ok(response);
    }


    @PutMapping("/{id}/remove-users")
    public ResponseEntity<?> removeUsersFromTeam(@PathVariable Long id, @RequestBody Set<Long> userIds) {
        teamService.removeUsersFromTeam(id, userIds);
        return ApiResponse.success("Removed Successfully");
    }

    @PutMapping("/{id}/enroll-users")
    public ResponseEntity<?> enrollUsers(@PathVariable Long id, @RequestBody Set<Long> userIds) {
        teamService.enrollUsers(id, userIds);
        return ApiResponse.success("Enrolled Successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        teamService.deleteTeam(id);
        return ApiResponse.success("Deleted Successfully");
    }

}