package com.example.schoolmanagement.dto;

import com.example.schoolmanagement.model.RoleEnum;

public class RegistrationRequest {
    private String namaLengkap;
    private String username;
    private String password;
    private RoleEnum role;
    
    // Field untuk data tambahan Siswa
    private String tanggalLahir;
    private String jenisKelamin;
    private String namaOrangTua;
    private String kelas; // <-- FIELD INI SANGAT PENTING
    
    // Field untuk data tambahan Guru
    private String nip; // <-- FIELD INI SANGAT PENTING

    public RegistrationRequest() {}

    // Getter dan Setter untuk semua field

    public String getNamaLengkap() { return namaLengkap; }
    public void setNamaLengkap(String namaLengkap) { this.namaLengkap = namaLengkap; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public RoleEnum getRole() { return role; }
    public void setRole(RoleEnum role) { this.role = role; }

    public String getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; }

    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getNamaOrangTua() { return namaOrangTua; }
    public void setNamaOrangTua(String namaOrangTua) { this.namaOrangTua = namaOrangTua; }
    
    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }

    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }
}