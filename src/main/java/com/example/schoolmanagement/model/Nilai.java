package com.example.schoolmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "nilai")
public class Nilai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "siswa_id", nullable = false)
    private Siswa siswa;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mata_pelajaran_id", nullable = false)
    private MataPelajaran mataPelajaran;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "guru_id", nullable = false)
    private Guru guru;

    @Column(nullable = false)
    private Integer nilaiTugas;

    @Column(nullable = false)
    private Integer nilaiUts;

    @Column(nullable = false)
    private Integer nilaiUas;

    public Nilai() {}

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Siswa getSiswa() { return siswa; }
    public void setSiswa(Siswa siswa) { this.siswa = siswa; }
    public MataPelajaran getMataPelajaran() { return mataPelajaran; }
    public void setMataPelajaran(MataPelajaran mataPelajaran) { this.mataPelajaran = mataPelajaran; }
    public Guru getGuru() { return guru; }
    public void setGuru(Guru guru) { this.guru = guru; }
    public Integer getNilaiTugas() { return nilaiTugas; }
    public void setNilaiTugas(Integer nilaiTugas) { this.nilaiTugas = nilaiTugas; }
    public Integer getNilaiUts() { return nilaiUts; }
    public void setNilaiUts(Integer nilaiUts) { this.nilaiUts = nilaiUts; }
    public Integer getNilaiUas() { return nilaiUas; }
    public void setNilaiUas(Integer nilaiUas) { this.nilaiUas = nilaiUas; }

    public double hitungNilaiAkhir() {
        return (nilaiTugas * 0.3) + (nilaiUts * 0.3) + (nilaiUas * 0.4);
    }
}