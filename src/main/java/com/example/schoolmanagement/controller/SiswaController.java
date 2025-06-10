package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.model.Siswa;
import com.example.schoolmanagement.model.Nilai;
import com.example.schoolmanagement.model.User; // Import model User Anda
import com.example.schoolmanagement.repository.SiswaRepository;
import com.example.schoolmanagement.repository.NilaiRepository;
import com.example.schoolmanagement.service.UserService; // Import UserService
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.security.core.userdetails.UsernameNotFoundException; // Import ini
import org.springframework.security.core.userdetails.UserDetails; // Import ini


import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/siswa")
public class SiswaController {

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private NilaiRepository nilaiRepository;

    @Autowired
    private UserService userService;

    @GetMapping("/dashboard")
    public String siswaDashboard(Model model, @AuthenticationPrincipal UserDetails principal) { // <-- Ganti 'User' menjadi 'UserDetails'
        // Langkah 1: Dapatkan username dari principal Spring Security
        String username = principal.getUsername();

        // Langkah 2: Gunakan UserService untuk mendapatkan objek User dari model Anda
        User loggedInUser = userService.findByUsername(username)
                                       .orElseThrow(() -> new UsernameNotFoundException("Current logged-in user not found in DB: " + username));

        // Langkah 3: Gunakan objek User dari model Anda untuk mencari Siswa
        Optional<Siswa> optionalSiswa = siswaRepository.findByUser(loggedInUser);

        Siswa siswa = null;
        List<Nilai> listNilai = null;

        if (optionalSiswa.isPresent()) {
            siswa = optionalSiswa.get();
            listNilai = nilaiRepository.findBySiswa(siswa);
        } else {
            model.addAttribute("errorMessage", "Data siswa tidak ditemukan untuk akun ini.");
            return "redirect:/logout";
        }

        model.addAttribute("siswa", siswa);
        model.addAttribute("listNilai", listNilai);

        return "siswa/dashboard";
    }

    @GetMapping("/cetak-rapor")
    public String cetakRapor(Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
            String username = principal.getUsername();
        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Siswa siswa = siswaRepository.findByUser(user)
                .orElseThrow(() -> new RuntimeException("Siswa not found"));
        List<Nilai> daftarNilai = nilaiRepository.findBySiswa(siswa);
    
        model.addAttribute("siswa", siswa);
        model.addAttribute("listNilai", daftarNilai);
    
        return "siswa/cetak-rapor";
    }

    @GetMapping("/lihat-rapor")
    public String lihatRapor(Model model, @AuthenticationPrincipal UserDetails principal) { // <-- Ganti 'User' menjadi 'UserDetails'
        // Langkah 1: Dapatkan username dari principal Spring Security
        String username = principal.getUsername();

        // Langkah 2: Gunakan UserService untuk mendapatkan objek User dari model Anda
        User loggedInUser = userService.findByUsername(username)
                                       .orElseThrow(() -> new UsernameNotFoundException("Current logged-in user not found in DB: " + username));

        // Langkah 3: Gunakan objek User dari model Anda untuk mencari Siswa
        Optional<Siswa> optionalSiswa = siswaRepository.findByUser(loggedInUser);

        Siswa siswa = null;
        List<Nilai> listNilai = null;

        if (optionalSiswa.isPresent()) {
            siswa = optionalSiswa.get();
            listNilai = nilaiRepository.findBySiswa(siswa);
        } else {
            model.addAttribute("errorMessage", "Data siswa tidak ditemukan untuk akun ini.");
            return "redirect:/logout";
        }

        model.addAttribute("siswa", siswa);
        model.addAttribute("listNilai", listNilai);

        return "siswa/lihat-rapor";
    }
}