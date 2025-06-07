package com.example.schoolmanagement.model;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "nilai")
@Getter
@Setter
public class Nilai {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal nilaiTugas;

    @Column(precision = 5, scale = 2)
    private BigDecimal nilaiUts;
    
    @Column(precision = 5, scale = 2)
    private BigDecimal nilaiUas;

    @Column(precision = 5, scale = 2)
    private BigDecimal nilaiAkhir;

    @ManyToOne
    @JoinColumn(name = "siswa_id")
    private Siswa siswa;
    
    @ManyToOne
    @JoinColumn(name = "matpel_id")
    private MataPelajaran mataPelajaran;

    @ManyToOne
    @JoinColumn(name = "guru_id")
    private Guru guru;
}