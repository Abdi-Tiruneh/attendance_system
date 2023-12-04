package com.attendanceMonitoringSystem.userManager.user;

import com.attendanceMonitoringSystem.exceptions.customExceptions.ResourceAlreadyExistsException;
import com.attendanceMonitoringSystem.exceptions.customExceptions.ResourceNotFoundException;
import com.attendanceMonitoringSystem.userManager.role.Role;
import com.attendanceMonitoringSystem.userManager.role.RoleService;
import com.attendanceMonitoringSystem.userManager.user.dto.UserRegistrationReq;
import com.attendanceMonitoringSystem.userManager.user.dto.UserResponse;
import com.attendanceMonitoringSystem.userManager.user.dto.UserUpdateReq;
import com.attendanceMonitoringSystem.utils.CurrentlyLoggedInUser;
import lombok.AllArgsConstructor;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@CacheConfig(cacheNames = "user")
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleService roleService;
    private final CurrentlyLoggedInUser inUser;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
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

    @Override
    @Transactional
    @CacheEvict(allEntries = true)
    public UserResponse editUser(UserUpdateReq updateReq) {
        Users user = inUser.getUser();

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
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
    }

    @Override
    public Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found."));
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
    @Cacheable
    public List<UserResponse> getAllUsers() {
        List<Users> users = userRepository.findAll(Sort.by(Sort.Order.asc("id")));
        return users.stream().
                map(UserResponse::toResponse).
                toList();
    }
}
