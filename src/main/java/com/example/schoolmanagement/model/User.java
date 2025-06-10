package com.example.schoolmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password; // Akan menyimpan password yang dienkripsi

    @Enumerated(EnumType.STRING) // Simpan nama Enum sebagai String di database
    @Column(nullable = false)
    private RoleEnum role;

    @Column(name = "nama_lengkap") // Sesuaikan dengan nama kolom di database Anda
    private String namaLengkap;

    // Konstruktor
    public User() {}

    public User(String username, String password, RoleEnum role, String namaLengkap) {
        this.username = username;
        this.password = password;
        this.role = role;
        this.namaLengkap = namaLengkap;
    }

    // Getters
    public Long getId() { return id; }
    public String getUsername() { return username; }
    public String getPassword() { return password; }
    public RoleEnum getRole() { return role; }
    public String getNamaLengkap() { return namaLengkap; }

    // Setters
    public void setId(Long id) { this.id = id; }
    public void setUsername(String username) { this.username = username; }
    public void setPassword(String password) { this.password = password; }
    public void setRole(RoleEnum role) { this.role = role; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }
}