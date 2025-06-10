package com.example.schoolmanagement.service;

import com.example.schoolmanagement.dto.RegistrationRequest;
import com.example.schoolmanagement.model.User;
import com.example.schoolmanagement.model.RoleEnum;
import com.example.schoolmanagement.model.Guru;
import com.example.schoolmanagement.model.Siswa;
import com.example.schoolmanagement.repository.UserRepository;
import com.example.schoolmanagement.repository.GuruRepository;
import com.example.schoolmanagement.repository.SiswaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GuruRepository guruRepository;
    @Autowired
    private SiswaRepository siswaRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Mendaftarkan user baru (Admin, Guru, atau Siswa) berdasarkan data dari DTO.
     */
    @Transactional
    public User registerNewUser(RegistrationRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("Username '" + request.getUsername() + "' sudah terdaftar!");
        }

        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRole(request.getRole());
        newUser.setNamaLengkap(request.getNamaLengkap());
        
        User savedUser = userRepository.save(newUser);

        if (request.getRole() == RoleEnum.GURU) {
            Guru newGuru = new Guru();
            newGuru.setNama(request.getNamaLengkap());
            newGuru.setNip(request.getNip());
            newGuru.setUser(savedUser);
            guruRepository.save(newGuru);
        } 
        else if (request.getRole() == RoleEnum.SISWA) {
            Siswa newSiswa = new Siswa();
            newSiswa.setNama(request.getNamaLengkap());
            newSiswa.setKelas(request.getKelas());
            newSiswa.setNis("SISWA-" + System.currentTimeMillis()); // Logic generate NIS sementara
            newSiswa.setUser(savedUser);
            
            newSiswa.setTanggalLahir(request.getTanggalLahir());
            newSiswa.setJenisKelamin(request.getJenisKelamin());
            newSiswa.setNamaOrangTua(request.getNamaOrangTua());
            
            siswaRepository.save(newSiswa);
        }

        return savedUser;
    }

    /**
     * Memperbarui data siswa berdasarkan ID dan data baru dari DTO.
     */
    @Transactional
    public void updateSiswa(Long siswaId, RegistrationRequest request) {
        Siswa siswa = siswaRepository.findById(siswaId)
                .orElseThrow(() -> new RuntimeException("Siswa dengan ID " + siswaId + " tidak ditemukan."));

        // Update data di entitas User
        User user = siswa.getUser();
        user.setUsername(request.getUsername());
        user.setNamaLengkap(request.getNamaLengkap());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);

        // Update data di entitas Siswa
        siswa.setNama(request.getNamaLengkap());
        siswa.setKelas(request.getKelas());
        siswa.setTanggalLahir(request.getTanggalLahir());
        siswa.setJenisKelamin(request.getJenisKelamin());
        siswa.setNamaOrangTua(request.getNamaOrangTua());
        siswaRepository.save(siswa);
    }

    /**
     * Memperbarui data guru berdasarkan ID dan data baru dari DTO.
     */
    @Transactional
    public void updateGuru(Long guruId, RegistrationRequest request) {
        Guru guru = guruRepository.findById(guruId)
                .orElseThrow(() -> new RuntimeException("Guru dengan ID " + guruId + " tidak ditemukan."));

        // Update data di entitas User
        User user = guru.getUser();
        user.setUsername(request.getUsername());
        user.setNamaLengkap(request.getNamaLengkap());
        if (request.getPassword() != null && !request.getPassword().isEmpty()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }
        userRepository.save(user);

        // Update data di entitas Guru
        guru.setNama(request.getNamaLengkap());
        guru.setNip(request.getNip());
        guruRepository.save(guru);
    }

    /**
     * Menghapus data siswa berdasarkan ID.
     * User yang berelasi akan ikut terhapus karena CascadeType.ALL.
     */
    @Transactional
    public void deleteSiswa(Long siswaId) {
        if (!siswaRepository.existsById(siswaId)) {
            throw new RuntimeException("Siswa dengan ID " + siswaId + " tidak ditemukan.");
        }
        siswaRepository.deleteById(siswaId);
    }

    /**
     * Menghapus data guru berdasarkan ID.
     * User yang berelasi akan ikut terhapus karena CascadeType.ALL.
     */
    @Transactional
    public void deleteGuru(Long guruId) {
        if (!guruRepository.existsById(guruId)) {
            throw new RuntimeException("Guru dengan ID " + guruId + " tidak ditemukan.");
        }
        guruRepository.deleteById(guruId);
    }

    // --- METODE PENDUKUNG LAINNYA ---

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public long countUsers() {
        return userRepository.count();
    }

    public long countSiswa() {
        return siswaRepository.count();
    }

    public long countGuru() {
        return guruRepository.count();
    }
}