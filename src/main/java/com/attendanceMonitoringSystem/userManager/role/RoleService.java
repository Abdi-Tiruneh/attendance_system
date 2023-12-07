package com.attendanceMonitoringSystem.userManager.role;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    // Retrieves all roles.
    public List<Role> getRoles() {
        return roleRepository.findAll();
    }

    // Retrieves a role by id.
    public Role getRoleById(Long roleId) {
        return roleRepository.findById(roleId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found."));
    }

    public Role getRoleByRoleName(String roleName) {
        return roleRepository.findByRoleNameIgnoreCase(roleName)
                .orElseThrow(() -> new EntityNotFoundException("Role not found."));
    }

}
