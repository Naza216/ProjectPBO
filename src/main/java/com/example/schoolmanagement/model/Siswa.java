package com.example.schoolmanagement.model;

import jakarta.persistence.*;
import java.util.Date; // atau java.time.LocalDate

@Entity
@Table(name = "siswa")
public class Siswa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nis;

    @Column(nullable = false)
    private String nama;

    @Column(nullable = false)
    private String kelas;

    // --- FIELD BARU DITAMBAHKAN DI SINI ---
    @Column(name = "tanggal_lahir")
    private String tanggalLahir; // Atau gunakan 'private Date tanggalLahir;'

    @Column(name = "jenis_kelamin")
    private String jenisKelamin;
    
    @Column(name = "nama_orang_tua")
    private String namaOrangTua;
    // --- AKHIR PENAMBAHAN FIELD ---

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public Siswa() {}

    public Siswa(String nis, String nama, String kelas, User user) {
        this.nis = nis;
        this.nama = nama;
        this.kelas = kelas;
        this.user = user;
    }

    // --- GETTER DAN SETTER ---
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNis() { return nis; }
    public void setNis(String nis) { this.nis = nis; }

    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }

    public String getKelas() { return kelas; }
    public void setKelas(String kelas) { this.kelas = kelas; }

    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }

    // --- GETTER DAN SETTER UNTUK FIELD BARU ---
    public String getTanggalLahir() { return tanggalLahir; }
    public void setTanggalLahir(String tanggalLahir) { this.tanggalLahir = tanggalLahir; }
    
    public String getJenisKelamin() { return jenisKelamin; }
    public void setJenisKelamin(String jenisKelamin) { this.jenisKelamin = jenisKelamin; }

    public String getNamaOrangTua() { return namaOrangTua; }
    public void setNamaOrangTua(String namaOrangTua) { this.namaOrangTua = namaOrangTua; }
}