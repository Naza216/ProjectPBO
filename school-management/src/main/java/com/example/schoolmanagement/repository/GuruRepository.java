package com.example.schoolmanagement.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.schoolmanagement.model.Guru;
public interface GuruRepository extends JpaRepository<Guru, Long> {}