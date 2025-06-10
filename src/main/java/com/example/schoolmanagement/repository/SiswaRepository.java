package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.model.Siswa;
import com.example.schoolmanagement.model.User; // Import model User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SiswaRepository extends JpaRepository<Siswa, Long> {
    Optional<Siswa> findByUser(User user); // <--- TAMBAHKAN BARIS INI
    Optional<Siswa> findByNis(String nis);
    List<Siswa> findAll();
}