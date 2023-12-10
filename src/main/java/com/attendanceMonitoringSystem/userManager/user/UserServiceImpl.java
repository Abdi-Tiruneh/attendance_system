package com.attendanceMonitoringSystem.userManager.user;

import com.attendanceMonitoringSystem.exceptions.customExceptions.ResourceAlreadyExistsException;
import com.attendanceMonitoringSystem.team.Team;
import com.attendanceMonitoringSystem.team.TeamRepository;
import com.attendanceMonitoringSystem.team.dto.TeamResponse;
import com.attendanceMonitoringSystem.userManager.role.Role;
import com.attendanceMonitoringSystem.userManager.role.RoleService;
import com.attendanceMonitoringSystem.userManager.user.dto.UserRegistrationReq;
import com.attendanceMonitoringSystem.userManager.user.dto.UserResponse;
import com.attendanceMonitoringSystem.userManager.user.dto.UserUpdateReq;
import com.attendanceMonitoringSystem.utils.CurrentlyLoggedInUser;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CurrentlyLoggedInUser inUser;
    private final PasswordEncoder passwordEncoder;
    private final TeamRepository teamRepository;

    @Override
    @Transactional
    public UserResponse register(UserRegistrationReq userReq, String roleName) {
        if (userRepository.findByUsername(userReq.getUsername()).isPresent())
            throw new ResourceAlreadyExistsException("Username is already taken");

        Role role = roleService.getRoleByRoleName(roleName);

        Users user = Users.builder()
                .username(userReq.getUsername())
                .fullName(userReq.getFullName())
                .password(passwordEncoder.encode(userReq.getPassword()))
                .role(role)
                .build();

        user = userRepository.save(user);
        return UserResponse.toResponse(user);
    }

    //users edit their account
    @Override
    public UserResponse editUser(UserUpdateReq updateReq) {
        Users user = inUser.getUser();
        return editUser(updateReq, user);
    }


    //admin for other users
    @Override
    public UserResponse editUser(Long userId, UserUpdateReq updateReq) {
        validateAdminUser(inUser.getUser());
        Users user = getUserById(userId);
        return editUser(updateReq, user);
    }

    @Transactional
    private UserResponse editUser(UserUpdateReq updateReq, Users user) {
        if (updateReq.getFullName() != null)
            user.setFullName(updateReq.getFullName());

        // Update it if provided username is different from the current username
        if (updateReq.getUsername() != null && !user.getUsername().equals(updateReq.getUsername())) {
            // Check if the new username is already taken
            if (userRepository.findByUsername(updateReq.getUsername()).isPresent())
                throw new ResourceAlreadyExistsException("Username is already taken");

            user.setUsername(updateReq.getUsername());
        }

        user = userRepository.save(user);
        return UserResponse.toResponse(user);
    }


    @Override
    public Users getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    @Override
    public Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found."));
    }

    @Override
    public List<UserResponse> getUsersById(Set<Long> userIdList) {

        List<Users> users = userRepository.findAllById(userIdList);
        return users.stream()
                .map(UserResponse::toResponse)
                .toList();
    }

    @Override
    public void deleteUser(Long id) {
        getUserById(id);
        userRepository.deleteById(id);
    }

    @Override
    public UserResponse me() {
        Users user = inUser.getUser();
        return UserResponse.toResponse(user);
    }

    @Override
    public List<UserResponse> getAllUsers(String role) {
        List<Users> users;

        if (!isValidRole(role))
            users = userRepository.findAll(Sort.by(Sort.Order.asc("id")));
        else
            users = userRepository.findByRoleRoleName(role.toUpperCase(), Sort.by(Sort.Order.asc("id")));

        return users.stream()
                .map(UserResponse::toResponse)
                .toList();
    }

    @Override
    public List<TeamResponse> getUserTeams(Long userId) {
        Users user = getUserById(userId);
        if (user.getEnrolledTeams() == null)
            return new ArrayList<>();

        List<Team> teams = teamRepository.findAllById(user.getEnrolledTeams());
        return teams.stream()
                .map(team -> new TeamResponse(team.getId(), team.getName(), team.getManager().getFullName(), team.getDescription()))
                .toList();
    }

    private boolean isValidRole(String role) {
        return role != null && !role.isEmpty() &&
                (role.equalsIgnoreCase("ADMIN") ||
                        role.equalsIgnoreCase("MANAGER") ||
                        role.equalsIgnoreCase("USER"));
    }

    private void validateAdminUser(Users admin) {
        if (!admin.getRole().getRoleName().equals("ADMIN"))
            throw new AccessDeniedException("Only admin users can perform this operation");
    }


}
