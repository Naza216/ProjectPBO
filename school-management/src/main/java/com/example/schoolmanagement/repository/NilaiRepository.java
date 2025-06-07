package com.example.schoolmanagement.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

import com.example.schoolmanagement.model.Nilai;
public interface NilaiRepository extends JpaRepository<Nilai, Long> {
    List<Nilai> findBySiswaId(Long siswaId);
}