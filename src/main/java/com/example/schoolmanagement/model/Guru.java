package com.example.schoolmanagement.model;

import jakarta.persistence.*;

@Entity
@Table(name = "guru")
public class Guru {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = true, unique = true)
    private String nip;

    @Column(nullable = false)
    private String nama;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY) // CascadeType.ALL untuk menyimpan user bersama guru
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    public Guru() {}

    public Guru(String nip, String nama, User user) {
        this.nip = nip;
        this.nama = nama;
        this.user = user;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNip() { return nip; }
    public void setNip(String nip) { this.nip = nip; }
    public String getNama() { return nama; }
    public void setNama(String nama) { this.nama = nama; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}