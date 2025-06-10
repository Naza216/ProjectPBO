package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.dto.NilaiInputForm;
import com.example.schoolmanagement.model.*;
import com.example.schoolmanagement.repository.*;
import com.example.schoolmanagement.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/guru")
public class GuruController {

    @Autowired
    private SiswaRepository siswaRepository;

    @Autowired
    private MataPelajaranRepository mataPelajaranRepository;

    @Autowired
    private NilaiRepository nilaiRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private GuruRepository guruRepository;


    @GetMapping("/dashboard")
    public String guruDashboard(Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        User loggedInUser = userService.findByUsername(principal.getUsername())
                                       .orElseThrow(() -> new RuntimeException("Logged in user not found in database."));

        model.addAttribute("totalSiswa", userService.countSiswa());
        model.addAttribute("totalGuru", userService.countGuru());
        model.addAttribute("totalMataPelajaran", mataPelajaranRepository.count());

        // Daftar siswa untuk tabel
        List<Siswa> listSiswa = siswaRepository.findAll();
        model.addAttribute("listSiswa", listSiswa);

        if (model.containsAttribute("successMessage")) {
            model.addAttribute("successMessage", model.asMap().get("successMessage"));
        }
        if (model.containsAttribute("errorMessage")) {
            model.addAttribute("errorMessage", model.asMap().get("errorMessage"));
        }

        return "guru/dashboard";
    }
    @GetMapping("/proses-deskripsi")
    public String prosesDeskripsi(Model model) {
        model.addAttribute("title", "Proses Deskripsi Rapor");
        return "guru/proses-deskripsi"; 
    }

    // --- MANAJEMEN SISWA (UNTUK GURU) ---
    @GetMapping("/manajemen-siswa")
    public String manajemenSiswaForGuru(Model model) {
        List<Siswa> siswas = siswaRepository.findAll();
        model.addAttribute("siswas", siswas);
        return "guru/manajemen-siswa";
    }

    @GetMapping("/capaian-nilai")
    public String capaianNilai(Model model) {
        model.addAttribute("title","Capaian Nilai Siswa");
        return "guru/capaian-nilai";
    }

    @GetMapping("/tujuan-pembelajaran")
    public String tujuanPembelajaran(Model model) {
        model.addAttribute("title","Tujuan Pembelajaran");
        return "guru/tujuan-pembelajaran";
    }

    
    


    @GetMapping("/input-nilai/{nis}")
    public String showInputNilaiForm(@PathVariable String nis, Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        Optional<Siswa> optionalSiswa = siswaRepository.findByNis(nis);
        if (optionalSiswa.isEmpty()) {
            return "redirect:/guru/manajemen-siswa?errorMessage=Siswa tidak ditemukan.";
        }
        Siswa siswa = optionalSiswa.get();

        model.addAttribute("siswa", siswa);
        model.addAttribute("listMataPelajaran", mataPelajaranRepository.findAll());

        // PERBAIKAN: Gunakan konstruktor tanpa argumen dan setter
        NilaiInputForm nilaiForm = new NilaiInputForm();
        nilaiForm.setNis(nis); // Set NIS
        // nilaiForm.setIdMataPelajaran(null); // Tidak perlu diset null, defaultnya sudah null
        // nilaiForm.setNilaiTugas(null);
        // nilaiForm.setNilaiUts(null);
        // nilaiForm.setNilaiUas(null);
        model.addAttribute("nilaiForm", nilaiForm);
        model.addAttribute("isEdit", false); // Menandakan ini form tambah

        return "guru/nilai-form";
    }

    // New: Menampilkan form untuk mengedit nilai
    @GetMapping("/edit-nilai/{nilaiId}")
    public String showEditNilaiForm(@PathVariable Long nilaiId, Model model, @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal) {
        Optional<Nilai> optionalNilai = nilaiRepository.findById(nilaiId);
        if (optionalNilai.isEmpty()) {
            return "redirect:/guru/manajemen-siswa?errorMessage=Nilai tidak ditemukan.";
        }
        Nilai nilai = optionalNilai.get();

        // Pastikan guru yang login adalah guru yang mengajar mata pelajaran ini (opsional tapi bagus)
        User loggedInUser = userService.findByUsername(principal.getUsername())
                                       .orElseThrow(() -> new RuntimeException("Logged in user not found."));
        Guru guru = guruRepository.findByUser(loggedInUser)
                                  .orElseThrow(() -> new RuntimeException("Guru entity not found for logged in user."));

        // Hanya izinkan edit jika guru yang login adalah guru pengajar nilai ini
        if (!nilai.getGuru().getId().equals(guru.getId())) {
             return "redirect:/guru/manajemen-siswa?errorMessage=Anda tidak memiliki izin untuk mengedit nilai ini.";
        }

        model.addAttribute("siswa", nilai.getSiswa());
        model.addAttribute("listMataPelajaran", mataPelajaranRepository.findAll());

        // PERBAIKAN: Gunakan konstruktor tanpa argumen dan setter
        NilaiInputForm nilaiForm = new NilaiInputForm();
        nilaiForm.setNis(nilai.getSiswa().getNis());
        nilaiForm.setIdMataPelajaran(nilai.getMataPelajaran().getId());
        nilaiForm.setNilaiTugas(nilai.getNilaiTugas());
        nilaiForm.setNilaiUts(nilai.getNilaiUts());
        nilaiForm.setNilaiUas(nilai.getNilaiUas());

        model.addAttribute("nilaiForm", nilaiForm);
        model.addAttribute("isEdit", true); // Menandakan ini form edit
        model.addAttribute("nilaiId", nilai.getId()); // ID nilai yang diedit

        return "guru/nilai-form"; // Reuses the same form for input and edit
    }


    @PostMapping("/simpan-nilai")
    public String simpanNilai(@ModelAttribute NilaiInputForm nilaiForm,
                              @RequestParam(value = "nilaiId", required = false) Long nilaiId, // Untuk identifikasi saat update
                              @AuthenticationPrincipal org.springframework.security.core.userdetails.User principal,
                              RedirectAttributes redirectAttributes) {

        Optional<Siswa> optionalSiswa = siswaRepository.findByNis(nilaiForm.getNis());
        Optional<MataPelajaran> optionalMataPelajaran = mataPelajaranRepository.findById(nilaiForm.getIdMataPelajaran());

        if (optionalSiswa.isEmpty() || optionalMataPelajaran.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Siswa atau Mata Pelajaran tidak ditemukan.");
            return "redirect:/guru/manajemen-siswa";
        }

        User loggedInUser = userService.findByUsername(principal.getUsername())
                                       .orElseThrow(() -> new RuntimeException("Logged in user not found."));
        Guru guru = guruRepository.findByUser(loggedInUser)
                                  .orElseThrow(() -> new RuntimeException("Guru entity not found for logged in user."));


        Nilai nilai;
        if (nilaiId != null) { // Jika nilaiId ada, ini adalah update
            nilai = nilaiRepository.findById(nilaiId)
                                   .orElseThrow(() -> new RuntimeException("Nilai tidak ditemukan untuk update."));
            // Pastikan guru yang mengupdate adalah guru yang benar
            if (!nilai.getGuru().getId().equals(guru.getId())) {
                 redirectAttributes.addFlashAttribute("errorMessage", "Anda tidak memiliki izin untuk mengedit nilai ini.");
                 return "redirect:/guru/manajemen-siswa";
            }
        } else { // Jika nilaiId tidak ada, ini adalah tambah baru
            nilai = new Nilai();
            nilai.setSiswa(optionalSiswa.get());
            nilai.setMataPelajaran(optionalMataPelajaran.get());
            nilai.setGuru(guru); // Guru penginput adalah guru yang login
        }

        nilai.setNilaiTugas(nilaiForm.getNilaiTugas());
        nilai.setNilaiUts(nilaiForm.getNilaiUts());
        nilai.setNilaiUas(nilaiForm.getNilaiUas());

        nilaiRepository.save(nilai);

        if (nilaiId != null) {
            redirectAttributes.addFlashAttribute("successMessage", "Nilai berhasil diperbarui untuk " + optionalSiswa.get().getNama() + ".");
        } else {
            redirectAttributes.addFlashAttribute("successMessage", "Nilai berhasil disimpan untuk " + optionalSiswa.get().getNama() + ".");
        }
        return "redirect:/guru/manajemen-siswa";
    }

    // New: Menampilkan detail nilai siswa (Cek Nilai)
    @GetMapping("/cek-nilai/{nis}")
    public String cekNilaiSiswa(@PathVariable String nis, Model model) {
        Optional<Siswa> optionalSiswa = siswaRepository.findByNis(nis);
        if (optionalSiswa.isEmpty()) {
            return "redirect:/guru/manajemen-siswa?errorMessage=Siswa tidak ditemukan.";
        }
        Siswa siswa = optionalSiswa.get();
        List<Nilai> daftarNilai = nilaiRepository.findBySiswa(siswa);

        model.addAttribute("siswa", siswa);
        model.addAttribute("daftarNilai", daftarNilai);
        return "guru/cek-nilai"; // Akan merender guru/cek-nilai.html
    }


    // Existing: Placeholder for other management pages
    @GetMapping("/manajemen-guru") // Ini manajemen guru untuk guru itu sendiri (bukan admin)
    public String manajemenGuru() {
        return "guru/manajemen-guru";
    }

    @GetMapping("/manajemen-mapel") // Ini manajemen mapel untuk guru itu sendiri (bukan admin)
    public String manajemenMapel() {
        return "guru/manajemen-mapel";
    }

    // New: Menghapus nilai (jika Menggunakan Tombol Hapus di cek-nilai.html)
    @PostMapping("/hapus-nilai/{id}")
    public String hapusNilai(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            Optional<Nilai> nilaiOptional = nilaiRepository.findById(id);
            if (nilaiOptional.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Nilai tidak ditemukan.");
            } else {
                Nilai nilai = nilaiOptional.get();
                // Optional: Pastikan guru yang login adalah guru yang menginput nilai ini sebelum menghapus
                // Jika ingin menambahkan cek otorisasi, Anda perlu @AuthenticationPrincipal di sini
                // User loggedInUser = userService.findByUsername(principal.getUsername()).orElseThrow(() -> new RuntimeException("..."));
                // Guru guru = guruRepository.findByUser(loggedInUser).orElseThrow(() -> new RuntimeException("..."));
                // if (!nilai.getGuru().getId().equals(guru.getId())) { /* ... */ }

                nilaiRepository.delete(nilai);
                redirectAttributes.addFlashAttribute("successMessage", "Nilai mata pelajaran " + nilai.getMataPelajaran().getNamaMataPelajaran() + " untuk " + nilai.getSiswa().getNama() + " berhasil dihapus.");
            }
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus nilai: " + e.getMessage());
        }
        // Setelah hapus, kembali ke halaman cek nilai siswa atau dashboard guru
        // Jika Anda ingin kembali ke halaman cek nilai siswa, Anda perlu NIS siswa
        // return "redirect:/guru/cek-nilai/" + nilai.getSiswa().getNis();
        return "redirect:/guru/manajemen-siswa"; // Kembali ke daftar siswa
    }
}