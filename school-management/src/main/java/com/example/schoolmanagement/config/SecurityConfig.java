package com.example.schoolmanagement.config; // Pastikan nama paket ini benar

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable) // Cara baru menonaktifkan CSRF
            .authorizeHttpRequests(auth -> auth
                // Izinkan akses ke halaman utama, login, dan file statis (CSS/JS)
                .requestMatchers("/", "/login", "/register**", "/css/**", "/js/**", "/webjars/**").permitAll()
                // Atur hak akses berdasarkan peran
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/guru/**").hasRole("GURU")
                .requestMatchers("/siswa/**").hasRole("SISWA")
                // Semua request lain harus diautentikasi
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")        // Halaman login kustom
                .loginProcessingUrl("/login") // URL untuk proses login
                .defaultSuccessUrl("/", true) // **BAGIAN KUNCI**: Setelah login, selalu redirect ke halaman utama
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout") // URL untuk proses logout (lebih modern)
                .logoutSuccessUrl("/login?logout") // Halaman tujuan setelah logout
                .permitAll()
            );

        return http.build();
    }
}