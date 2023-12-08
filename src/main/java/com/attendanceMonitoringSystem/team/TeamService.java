package com.attendanceMonitoringSystem.team;

import com.attendanceMonitoringSystem.exceptions.customExceptions.BadRequestException;
import com.attendanceMonitoringSystem.team.dto.TeamReq;
import com.attendanceMonitoringSystem.team.dto.TeamResponse;
import com.attendanceMonitoringSystem.userManager.user.UserRepository;
import com.attendanceMonitoringSystem.userManager.user.UserService;
import com.attendanceMonitoringSystem.userManager.user.Users;
import com.attendanceMonitoringSystem.userManager.user.dto.UserResponse;
import com.attendanceMonitoringSystem.utils.CurrentlyLoggedInUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;
    private final CurrentlyLoggedInUser loggedInUser;
    private final UserService userService;


    @Transactional
    public TeamResponse createTeam(TeamReq teamReq) {
        Users admin = loggedInUser.getUser();
        validateAdminUser(admin);

        Users user = userService.getUserById(teamReq.getManagerId());
        if (!user.getRole().getRoleName().equals("MANAGER"))
            throw new BadRequestException("The provided user is not manager.");

        Team team = new Team();
        team.setName(teamReq.getName());
        team.setDescription(teamReq.getDescription());
        team.setManager(user);

        team = teamRepository.save(team);

        return new TeamResponse(team.getId(), team.getName(), team.getManager().getFullName(), team.getDescription());
    }

    @Transactional
    public void enrollUsers(Long teamId, Set<Long> userIds) {
        Users managerOrAdmin = loggedInUser.getUser();

        // Only managers or admin users can enroll users
        validateManagerOrAdmin(managerOrAdmin);

        Team team = getTeam(teamId);

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

        Team team = getTeam(teamId);

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
    public TeamResponse updateTeam(Long teamId, TeamReq teamReq) {
        // Only admin users can update teams
        validateAdminUser(loggedInUser.getUser());

        Team team = getTeam(teamId);

        // Check if the managerId is provided and retrieve the user
        if (teamReq.getManagerId() != null) {
            Users user = userService.getUserById(teamReq.getManagerId());
            if (!user.getRole().getRoleName().equals("MANAGER")) {
                throw new BadRequestException("The provided user is not a manager.");
            }
            team.setManager(user);
        }

        // Update name and description if provided
        if (teamReq.getName() != null)
            team.setName(teamReq.getName());

        if (teamReq.getDescription() != null)
            team.setDescription(teamReq.getDescription());

        team = teamRepository.save(team);

        return new TeamResponse(team.getId(), team.getName(), team.getManager().getFullName(), team.getDescription());
    }


    public Set<UserResponse> getAllMembers(Long teamId) {
        Team team = getTeam(teamId);

        // Retrieve users from the database using user IDs
        List<Users> users = userRepository.findAllById(team.getEnrolledUsers());
        return users
                .stream()
                .map(UserResponse::toResponse)
                .collect(Collectors.toSet());
    }

    public List<TeamResponse> getAllTeams(Long managerId) {
        List<Team> teams;
        if (managerId != null)
            teams = teamRepository.findByManagerId(managerId);
        else
            teams = teamRepository.findAll();

        return teams.stream()
                .map(team -> new TeamResponse(team.getId(), team.getName(), team.getManager().getFullName(), team.getDescription()))
                .toList();
    }

    public Team getTeam(Long teamId) {
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new EntityNotFoundException("Team not found"));
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

    private void validateManagerOrAdmin(Users managerOrAdmin) {
        String role = managerOrAdmin.getRole().getRoleName();
        if (!"ADMIN".equals(role) && !"MANAGER".equals(role))
            throw new AccessDeniedException("Only managers or admin users can perform this operation");
    }

}
