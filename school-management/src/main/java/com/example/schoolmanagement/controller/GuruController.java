package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.model.Guru;
import com.example.schoolmanagement.model.MataPelajaran;
import com.example.schoolmanagement.model.Nilai;
import com.example.schoolmanagement.model.Siswa;
import com.example.schoolmanagement.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.math.RoundingMode;

@Controller
@RequestMapping("/guru")
public class GuruController {
    @Autowired private UserRepository userRepository;
    @Autowired private SiswaRepository siswaRepository;
    @Autowired private NilaiRepository nilaiRepository;

    // Helper method untuk mendapatkan entitas Guru yang sedang login
    private Guru getAuthenticatedGuru(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan")).getGuru();
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        // Tampilkan semua siswa agar guru bisa memilih siapa yang akan dinilai
        model.addAttribute("listSiswa", siswaRepository.findAll());
        return "guru/dashboard";
    }

    @GetMapping("/nilai/input/{siswaId}")
    public String showNilaiForm(@PathVariable Long siswaId, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Guru guru = getAuthenticatedGuru(userDetails);
        Siswa siswa = siswaRepository.findById(siswaId).orElseThrow();
        
        // Asumsi nilai yang diinput selalu baru, untuk edit perlu logika tambahan
        Nilai nilai = new Nilai();
        nilai.setSiswa(siswa);

        model.addAttribute("nilai", nilai);
        model.addAttribute("siswa", siswa);
        model.addAttribute("listMapel", guru.getMataPelajaran()); // Ambil mapel yang diajar guru ini
        
        return "guru/nilai-form";
    }

    @PostMapping("/nilai/save")
    public String saveNilai(Nilai nilai, @AuthenticationPrincipal UserDetails userDetails, RedirectAttributes redirectAttributes) {
        Guru guru = getAuthenticatedGuru(userDetails);
        nilai.setGuru(guru); // Set guru yang sedang login sebagai pemberi nilai

        // Contoh perhitungan nilai akhir (bobot bisa disesuaikan)
        // 20% tugas, 30% UTS, 50% UAS
        BigDecimal nilaiTugas = nilai.getNilaiTugas().multiply(new BigDecimal("0.2"));
        BigDecimal nilaiUts = nilai.getNilaiUts().multiply(new BigDecimal("0.3"));
        BigDecimal nilaiUas = nilai.getNilaiUas().multiply(new BigDecimal("0.5"));
        BigDecimal nilaiAkhir = nilaiTugas.add(nilaiUts).add(nilaiUas);
        
        nilai.setNilaiAkhir(nilaiAkhir.setScale(2, RoundingMode.HALF_UP));

        nilaiRepository.save(nilai);
        redirectAttributes.addFlashAttribute("message", "Nilai untuk siswa " + nilai.getSiswa().getNama() + " berhasil disimpan.");
        return "redirect:/guru/dashboard";
    }
}