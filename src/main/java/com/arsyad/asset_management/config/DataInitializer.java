package com.arsyad.asset_management.config;

import com.arsyad.asset_management.entity.User;
import com.arsyad.asset_management.entity.RoleName;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import com.arsyad.asset_management.repository.RoleRepository;
import com.arsyad.asset_management.repository.UserRepository;
import com.arsyad.asset_management.entity.Role;

@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {

        // Insert roles jika belum ada
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(roleName);
                        return roleRepository.save(role);
                    });
        }

        // Create default admin
        if (userRepository.findByEmail("admin@admin.com").isEmpty()) {

            Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow();

            User admin = new User();
            admin.setName("Super Admin");
            admin.setEmail("admin@admin.com");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole(adminRole);

            userRepository.save(admin);
        }
    }
}
