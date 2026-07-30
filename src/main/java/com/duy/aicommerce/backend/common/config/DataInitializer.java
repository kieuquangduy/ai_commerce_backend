package com.duy.aicommerce.backend.common.config;

import com.duy.aicommerce.backend.role.entity.Role;
import com.duy.aicommerce.backend.role.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;

    public DataInitializer(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public void run(String... args) {
        initializeRoles();
    }

    private void initializeRoles() {
        createRoleIfNotExists("ADMIN");
        createRoleIfNotExists("SELLER");
        createRoleIfNotExists("CUSTOMER");
    }

    private void createRoleIfNotExists(String roleName) {
        roleRepository.findByName(roleName)
                .ifPresentOrElse(
                        role -> System.out.println("Role already exists: " + roleName),
                        () -> {
                            Role newRole = new Role();
                            newRole.setName(roleName);
                            roleRepository.save(newRole);

                            System.out.println("Created role: " + roleName);
                        }
                );

    }
}