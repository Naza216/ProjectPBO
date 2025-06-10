package com.example.schoolmanagement.util;

import com.example.schoolmanagement.dto.RegistrationRequest; // <-- IMPORT DTO
import com.example.schoolmanagement.model.*;
import com.example.schoolmanagement.repository.*;
import com.example.schoolmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    private UserService userService; // Gunakan UserService untuk registrasi user
    @Autowired
    private MataPelajaranRepository mataPelajaranRepository;
    @Autowired
    private NilaiRepository nilaiRepository;
    @Autowired
    private SiswaRepository siswaRepository;
    @Autowired
    private GuruRepository guruRepository;

    @Override
    public void run(String... args) throws Exception {
        // Hanya tambahkan data jika database benar-benar kosong
        if (userService.countUsers() == 0) {
            System.out.println("Loading initial data...");

            // 1. Buat User Admin
            RegistrationRequest adminRequest = new RegistrationRequest();
            adminRequest.setUsername("admin");
            adminRequest.setPassword("admin");
            adminRequest.setNamaLengkap("Administrator Utama");
            adminRequest.setRole(RoleEnum.ADMIN);
            userService.registerNewUser(adminRequest);
            System.out.println("Created Admin User: admin");

            // 2. Buat User Guru
            RegistrationRequest guruRequest = new RegistrationRequest();
            guruRequest.setUsername("guru");
            guruRequest.setPassword("guru");
            guruRequest.setNamaLengkap("Yadi Sembako");
            guruRequest.setRole(RoleEnum.GURU);
            guruRequest.setNip("198501012010011001"); // Menambahkan contoh NIP
            User guruUser = userService.registerNewUser(guruRequest);
            System.out.println("Created Guru User: guru");

            // 3. Buat User Siswa
            RegistrationRequest siswaRequest = new RegistrationRequest();
            siswaRequest.setUsername("mikhaeldarsin");
            siswaRequest.setPassword("siswa");
            siswaRequest.setNamaLengkap("Mikhael Darsin");
            siswaRequest.setRole(RoleEnum.SISWA);
            siswaRequest.setKelas("XII IPS 1"); // Menambahkan contoh Kelas
            siswaRequest.setTanggalLahir("2005-02-02"); // Menambahkan contoh Tgl Lahir
            siswaRequest.setJenisKelamin("Laki-Laki"); // Menambahkan contoh JK
            siswaRequest.setNamaOrangTua("Darsin"); // Menambahkan contoh Nama Ortu
            User siswaUser = userService.registerNewUser(siswaRequest);
            System.out.println("Created Siswa User: mikhaeldarsin");

            // 4. Tambahkan Mata Pelajaran
            MataPelajaran ipa = mataPelajaranRepository.save(new MataPelajaran("Ilmu Pengetahuan Alam"));
            mataPelajaranRepository.save(new MataPelajaran("Matematika"));
            mataPelajaranRepository.save(new MataPelajaran("Bahasa Indonesia"));
            System.out.println("Added Mata Pelajaran.");

            // 5. Tambahkan Contoh Nilai
            // Ambil entitas Guru dan Siswa yang sudah pasti ada setelah registrasi
            Guru guru = guruRepository.findByUser(guruUser).orElse(null);
            Siswa siswa = siswaRepository.findByUser(siswaUser).orElse(null);

            if (siswa != null && guru != null) {
                Nilai nilaiSiswa = new Nilai();
                nilaiSiswa.setSiswa(siswa);
                nilaiSiswa.setMataPelajaran(ipa); // Menggunakan mapel IPA
                nilaiSiswa.setGuru(guru);
                nilaiSiswa.setNilaiTugas(78);
                nilaiSiswa.setNilaiUts(99);
                nilaiSiswa.setNilaiUas(100);
                nilaiRepository.save(nilaiSiswa);
                System.out.println("Added sample Nilai for Siswa.");
            }

            System.out.println("Initial data loading complete.");

        } else {
            System.out.println("Users already exist. Skipping initial data loading.");
        }
    }
}