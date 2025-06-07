package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.model.Siswa;
import com.example.schoolmanagement.repository.NilaiRepository;
import com.example.schoolmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/siswa")
public class SiswaController {
    @Autowired private UserRepository userRepository;
    @Autowired private NilaiRepository nilaiRepository;

    // Helper method untuk mendapatkan entitas Siswa yang sedang login
    private Siswa getAuthenticatedSiswa(UserDetails userDetails) {
        return userRepository.findByUsername(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User tidak ditemukan")).getSiswa();
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Siswa siswa = getAuthenticatedSiswa(userDetails);
        model.addAttribute("siswa", siswa);
        model.addAttribute("listNilai", nilaiRepository.findBySiswaId(siswa.getId()));
        return "siswa/dashboard";
    }

    @GetMapping("/rapor/cetak")
    public String cetakRapor(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Siswa siswa = getAuthenticatedSiswa(userDetails);
        model.addAttribute("siswa", siswa);
        model.addAttribute("listNilai", nilaiRepository.findBySiswaId(siswa.getId()));
        
        // Mengembalikan halaman khusus untuk dicetak
        return "siswa/rapor-cetak";
    }
}