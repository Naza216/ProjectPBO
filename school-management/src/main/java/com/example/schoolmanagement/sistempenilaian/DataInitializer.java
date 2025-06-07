package com.example.schoolmanagement.sistempenilaian; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.schoolmanagement.model.Role;
import com.example.schoolmanagement.model.User;
import com.example.schoolmanagement.repository.UserRepository;

/**
 * Class ini berjalan secara otomatis sekali saat aplikasi startup.
 * Tujuannya adalah untuk mengisi data awal yang penting.
 */
@Component
public class DataInitializer implements CommandLineRunner {

    // Kita hanya butuh UserRepository dan PasswordEncoder
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        
        // Cek apakah user 'admin' sudah ada di database.
        // Jika belum ada, maka kita buat.
        if (userRepository.findByUsername("admin").isEmpty()) {
                
                // 1. Buat object User baru
            User admin = new User();
            
            // 2. Set semua propertinya
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123")); // Password di-enkripsi!
            admin.setNamaLengkap("Administrator Utama");
            
            // 3. Set rolenya langsung dari ENUM, ini bagian yang berubah
            admin.setRole(Role.ADMIN);
            
            // 4. Simpan user admin ke database
            userRepository.save(admin);

            System.out.println(">>> User admin berhasil dibuat!");
        }
    }
}