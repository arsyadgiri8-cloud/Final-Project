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

        // Insert roles
        for (RoleName roleName : RoleName.values()) {
            roleRepository.findByName(roleName)
                    .orElseGet(() -> {
                        Role role = new Role();
                        role.setName(roleName);
                        return roleRepository.save(role);
                    });
        }

        // ADMIN
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

        // EMPLOYEE
        if (userRepository.findByEmail("employee@email.com").isEmpty()) {

            Role employeeRole = roleRepository.findByName(RoleName.EMPLOYEE)
                    .orElseThrow();

            User employee = new User();
            employee.setName("Employee");
            employee.setEmail("employee@email.com");
            employee.setPassword(passwordEncoder.encode("123456"));
            employee.setRole(employeeRole);

            userRepository.save(employee);
        }

        // TECHNICIAN
        if (userRepository.findByEmail("tech@email.com").isEmpty()) {

            Role techRole = roleRepository.findByName(RoleName.TECHNICIAN)
                    .orElseThrow();

            User technician = new User();
            technician.setName("Technician");
            technician.setEmail("tech@email.com");
            technician.setPassword(passwordEncoder.encode("123456"));
            technician.setRole(techRole);

            userRepository.save(technician);
        }
    }
}
