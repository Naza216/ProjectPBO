package com.example.schoolmanagement.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Entity
@Table(name = "siswa")
@Getter
@Setter
public class Siswa {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, unique = true)
    private String nis;

    @Column(length = 100)
    private String nama;
    
    @Column(length = 20)
    private String kelas;

    @OneToOne
    @JoinColumn(name = "users_id")
    private User user;


}