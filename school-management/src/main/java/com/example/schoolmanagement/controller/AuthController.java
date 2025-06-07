package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.model.Role;
import com.example.schoolmanagement.model.Siswa;
import com.example.schoolmanagement.model.User;
import com.example.schoolmanagement.repository.SiswaRepository;
import com.example.schoolmanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class AuthController {

    @Autowired
    private SiswaRepository siswaRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    // Menampilkan halaman registrasi
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("siswa", new Siswa());
        return "register";
    }

    // Memproses data dari form registrasi
    @PostMapping("/register/save")
    public String processRegistration(Siswa siswa, @RequestParam String password, RedirectAttributes redirectAttributes) {
        // Cek apakah username (NIS) sudah ada
        if (userRepository.findByUsername(siswa.getNis()).isPresent()) {
            redirectAttributes.addFlashAttribute("error", "Username (NIS) sudah terdaftar!");
            return "redirect:/register?error";
        }

        // Buat entitas User baru
        User user = new User();
        user.setNamaLengkap(siswa.getNama());
        user.setUsername(siswa.getNis());
        user.setPassword(passwordEncoder.encode(password));
        user.setRole(Role.SISWA);

        // Hubungkan siswa dengan user, lalu simpan siswa (user akan ikut tersimpan karena cascade)
        siswa.setUser(user);
        siswaRepository.save(siswa);

        redirectAttributes.addFlashAttribute("success", "Registrasi berhasil! Silakan login.");
        return "redirect:/login?success";
    }
}