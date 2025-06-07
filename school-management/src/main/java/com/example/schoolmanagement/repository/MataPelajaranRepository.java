package com.example.schoolmanagement.repository;
import org.springframework.data.jpa.repository.JpaRepository;

import com.example.schoolmanagement.model.MataPelajaran;
public interface MataPelajaranRepository extends JpaRepository<MataPelajaran, Long> {}