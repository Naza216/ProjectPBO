package com.example.schoolmanagement.config; // atau sesuaikan dengan nama package Anda

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalControllerAdvice {

    /**
     * Metode ini akan berjalan untuk setiap request dan secara otomatis 
     * menambahkan variabel "requestURI" ke dalam model Thymeleaf.
     */
    @ModelAttribute("requestURI")
    public String getRequestURI(final HttpServletRequest request) {
        return request.getRequestURI();
    }
}