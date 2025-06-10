package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.dto.RegistrationRequest;
import com.example.schoolmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class HomeController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated() &&
            !(authentication.getPrincipal() instanceof String && "anonymousUser".equals(authentication.getPrincipal()))) {

            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
                return "redirect:/admin/dashboard";
            } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_GURU"))) {
                return "redirect:/guru/dashboard";
            } else if (userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_SISWA"))) {
                return "redirect:/siswa/dashboard";
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
                        @RequestParam(value = "logout", required = false) String logout,
                        Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Username atau password salah.");
        }
        if (logout != null) {
            model.addAttribute("logoutMessage", "Anda telah berhasil logout.");
        }
        return "login";
    }

    // Metode untuk menampilkan form register tidak perlu diubah
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("registrationRequest", new RegistrationRequest());
        return "register";
    }

    // ======================================================
    // --- PERBAIKAN DI SINI ---
    // ======================================================
    @PostMapping("/register")
    public String registerUser(@ModelAttribute("registrationRequest") RegistrationRequest registrationRequest, RedirectAttributes redirectAttributes) {
        try {
            // Panggil service dengan satu objek DTO yang sudah lengkap
            userService.registerNewUser(registrationRequest);

            redirectAttributes.addFlashAttribute("successMessage", "Registrasi berhasil! Silakan login.");
            return "redirect:/login";
        } catch (RuntimeException e) {
            // Kirim pesan error yang lebih spesifik ke halaman register
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            // Kembalikan data yang sudah diisi agar user tidak perlu mengetik ulang
            redirectAttributes.addFlashAttribute("registrationRequest", registrationRequest);
            return "redirect:/register";
        }
    }
}