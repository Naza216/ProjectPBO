package com.example.schoolmanagement.repository; // sesuaikan nama paket Anda

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.schoolmanagement.model.Role;
import com.example.schoolmanagement.model.User;

@Component
public class RoleRepository implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Cek dan buat user admin jika belum ada
        if (userRepository.findByUsername("admin").isEmpty()) {
            User admin = new User();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setNamaLengkap("Administrator");
            admin.setRole(Role.ADMIN); // Langsung set dari ENUM
            userRepository.save(admin);
        }
    }
}