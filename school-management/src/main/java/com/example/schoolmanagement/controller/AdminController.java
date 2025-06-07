package com.example.schoolmanagement.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.schoolmanagement.model.Guru;
import com.example.schoolmanagement.model.Role;
import com.example.schoolmanagement.model.Siswa;
import com.example.schoolmanagement.model.User;
import com.example.schoolmanagement.repository.GuruRepository;
import com.example.schoolmanagement.repository.SiswaRepository;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private SiswaRepository siswaRepository;
    @Autowired private GuruRepository guruRepository;
    @Autowired private PasswordEncoder passwordEncoder;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long jumlahSiswa = siswaRepository.count();
        long jumlahGuru = guruRepository.count();
        // Anda bisa menambahkan count untuk mapel juga
        // long jumlahMapel = mataPelajaranRepository.count();
    
        model.addAttribute("jumlahSiswa", jumlahSiswa);
        model.addAttribute("jumlahGuru", jumlahGuru);
        // model.addAttribute("jumlahMapel", jumlahMapel);
        return "admin/dashboard";
    }

    @PostMapping("/siswa/save")
    public String saveSiswa(@ModelAttribute("siswa") Siswa siswa) {
        // Jika siswa baru (ID null), buat user baru
        if (siswa.getId() == null) {
            User user = new User();
            user.setNamaLengkap(siswa.getNama());
            user.setUsername(siswa.getNis()); // NIS sebagai username
            user.setPassword(passwordEncoder.encode("siswa123")); // Password default
            user.setRole(Role.SISWA);
            // Hubungkan user dengan siswa
            siswa.setUser(user);
        } else {
            // Jika edit, update nama di user juga
            Siswa existingSiswa = siswaRepository.findById(siswa.getId()).orElseThrow();
            User user = existingSiswa.getUser();
            user.setNamaLengkap(siswa.getNama());
            siswa.setUser(user);
        }
        
        siswaRepository.save(siswa);
        return "redirect:/admin/siswa";
    }
    
@GetMapping("/siswa/edit/{id}")
public String showEditSiswaForm(@PathVariable("id") Long id, Model model) {
    Siswa siswa = siswaRepository.findById(id).orElseThrow();
    model.addAttribute("siswa", siswa);
    return "admin/siswa-form";
    }
    
@GetMapping("/siswa/delete/{id}")
public String deleteSiswa(@PathVariable("id") Long id) {
        // CascadeType.ALL dan orphanRemoval=true di User akan menghapus Siswa juga
        Siswa siswa = siswaRepository.findById(id).orElseThrow();
        siswaRepository.delete(siswa);
        return "redirect:/admin/siswa";
    }
@PostMapping("/guru/save")
public String saveGuru(Guru guru, RedirectAttributes redirectAttributes) {
    if (guru.getId() == null) { // Jika guru baru
        User user = new User();
        user.setNamaLengkap(guru.getNama());
        // Jadikan NIP sebagai username, pastikan NIP unik
        user.setUsername(String.valueOf(guru.getNip()));
        user.setPassword(passwordEncoder.encode("guru123")); // Default password
        user.setRole(Role.GURU);
        guru.setUser(user);
        redirectAttributes.addFlashAttribute("message", "Guru berhasil ditambahkan.");
    } else { // Jika edit
        Guru existingGuru = guruRepository.findById(guru.getId()).orElseThrow();
        User user = existingGuru.getUser();
        user.setNamaLengkap(guru.getNama());
        guru.setUser(user);
        guru.setNip(existingGuru.getNip()); // Jaga agar NIP tidak berubah saat edit
        redirectAttributes.addFlashAttribute("message", "Data guru berhasil diubah.");
    }
        guruRepository.save(guru);
        return "redirect:/admin/guru";
}

@GetMapping("/guru/edit/{id}")
public String showEditGuruForm(@PathVariable("id") Long id, Model model) {
    Guru guru = guruRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("ID Guru tidak valid: " + id));
    model.addAttribute("guru", guru);
    model.addAttribute("pageTitle", "Edit Data Guru");
    return "admin/guru-form";
}

@GetMapping("/guru/delete/{id}")
public String deleteGuru(@PathVariable("id") Long id, RedirectAttributes redirectAttributes) {
    guruRepository.deleteById(id);
    redirectAttributes.addFlashAttribute("message", "Data guru berhasil dihapus.");
    return "redirect:/admin/guru";
    }
}