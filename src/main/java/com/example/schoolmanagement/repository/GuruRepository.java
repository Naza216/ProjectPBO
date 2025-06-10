package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.model.Guru;
import com.example.schoolmanagement.model.User; // Import model User
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GuruRepository extends JpaRepository<Guru, Long> {
    Optional<Guru> findByUser(User user); // <--- TAMBAHKAN BARIS INI
    Optional<Guru> findByNip(String nip);
}