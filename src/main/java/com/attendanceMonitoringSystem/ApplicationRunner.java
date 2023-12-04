package com.attendanceMonitoringSystem;

import com.attendanceMonitoringSystem.userManager.role.Role;
import com.attendanceMonitoringSystem.userManager.role.RoleRepository;
import com.attendanceMonitoringSystem.userManager.user.UserRepository;
import com.attendanceMonitoringSystem.userManager.user.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@Configuration
@ConditionalOnProperty(prefix = "database", name = "seed", havingValue = "true")
@RequiredArgsConstructor
@Slf4j
public class ApplicationRunner {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    /**
     * Initializes the database with preloaded data upon application startup.
     */
    @Bean
    CommandLineRunner initDatabase() {
        return args -> {
            try {
                // Create and save roles
                List<Role> roles = createAttendanceSystemRoles();
                roles = roleRepository.saveAll(roles);

                // Create and save user
                Users johnDoe = createUser(roles.get(0));
                userRepository.save(johnDoe);

                log.info("ApplicationRunner => Preloaded organization, roles and admin user");
            } catch (Exception ex) {
                log.error("ApplicationRunner Preloading Error: {}", ex.getMessage());
                throw new RuntimeException("ApplicationRunner Preloading Error ", ex);
            }
        };
    }

    private List<Role> createAttendanceSystemRoles() {
        Role admin = new Role("ADMIN", "Full control and access to all features of the attendance monitoring system");
        Role manager = new Role("MANAGER", "Manages attendance for specific teams, departments, or projects");
        Role user = new Role("USER", "Monitors and views personal attendance records, and uses the attendance system");

        return List.of(admin, manager, user);
    }

    private Users createUser(Role role) {
        return Users.builder()
                .username("john@admin.com")
                .fullName("John Doe")
                .password(passwordEncoder.encode("123456"))
                .fullName("Joe Doe")
                .role(role)
                .build();
    }
}