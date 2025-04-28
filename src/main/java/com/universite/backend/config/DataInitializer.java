package com.universite.backend.config;

import com.universite.backend.entity.Admin;
import com.universite.backend.entity.Role;
import com.universite.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Optional<com.universite.backend.entity.User> existingAdmin = userRepository.findByEmail("admin@admin.com");
        if (existingAdmin.isPresent()) {
            System.out.println("Admin déjà existant, aucune action nécessaire.");
            return;
        }

        Admin admin = new Admin();
        admin.setUsername("admin");
        admin.setEmail("admin@admin.com");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setRole(Role.ADMIN);
        admin.setDepartment("Scolarité");

        userRepository.save(admin);
        System.out.println("Admin créé avec succès !");
    }
}
