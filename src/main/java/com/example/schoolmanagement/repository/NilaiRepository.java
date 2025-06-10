package com.example.schoolmanagement.repository;

import com.example.schoolmanagement.model.Guru;
import com.example.schoolmanagement.model.Nilai;
import com.example.schoolmanagement.model.Siswa;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NilaiRepository extends JpaRepository<Nilai, Long> {
    List<Nilai> findBySiswa(Siswa siswa);
     long countByGuru(Guru guru);
}