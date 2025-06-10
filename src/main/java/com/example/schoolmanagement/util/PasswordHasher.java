package com.example.schoolmanagement.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordHasher {
    public static void main(String[] args) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        System.out.println("--- Hashes untuk Pengguna Default ---");
        System.out.println("Password mentah 'admin': admin");
        System.out.println("Hash 'admin': " + encoder.encode("admin"));
        System.out.println("------------------------------------");
        System.out.println("Password mentah 'guru': guru");
        System.out.println("Hash 'guru': " + encoder.encode("guru"));
        System.out.println("------------------------------------");
        System.out.println("Password mentah 'siswa': siswa");
        System.out.println("Hash 'siswa': " + encoder.encode("siswa"));
        System.out.println("------------------------------------");
    }
}