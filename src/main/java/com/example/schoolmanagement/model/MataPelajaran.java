package com.example.schoolmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "mata_pelajaran")
public class MataPelajaran {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String namaMataPelajaran;

    public MataPelajaran() {}

    public MataPelajaran(String namaMataPelajaran) {
        this.namaMataPelajaran = namaMataPelajaran;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNamaMataPelajaran() { return namaMataPelajaran; }
    public void setNamaMataPelajaran(String namaMataPelajaran) { this.namaMataPelajaran = namaMataPelajaran; }
}