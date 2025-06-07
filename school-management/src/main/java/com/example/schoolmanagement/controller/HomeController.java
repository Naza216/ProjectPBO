package com.example.schoolmanagement.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/login")
    public String home(HttpServletRequest request) {
        if (request.isUserInRole("ADMIN")) {
            return "redirect:/admin/dashboard";
        }
        if (request.isUserInRole("GURU")) {
            return "redirect:/guru/dashboard";
        }
        if (request.isUserInRole("SISWA")) {
            return "redirect:/siswa/dashboard";
        }
        // Jika sudah login tapi tidak punya peran yang dikenal, atau belum login
        return "redirect:/login";
    }
}