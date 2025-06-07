package com.example.schoolmanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Entity
@Table(name = "rapor")
@Getter
@Setter
public class Rapor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50)
    private String semester;
    
    @Column(length = 50)
    private String tahunAjaran;

    @Column(precision = 20, scale = 6)
    private BigDecimal nilaiAkhirTotal;
    
    private Integer rangking;

    @ManyToOne
    @JoinColumn(name = "siswa_id")
    private Siswa siswa;
}