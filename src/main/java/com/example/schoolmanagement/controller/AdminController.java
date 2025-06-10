package com.example.schoolmanagement.controller;

import com.example.schoolmanagement.model.*;
import com.example.schoolmanagement.dto.RegistrationRequest;
import com.example.schoolmanagement.service.UserService;
import com.example.schoolmanagement.repository.GuruRepository;
import com.example.schoolmanagement.repository.SiswaRepository;
import com.example.schoolmanagement.repository.MataPelajaranRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.data.domain.Sort;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired private UserService userService;
    @Autowired private GuruRepository guruRepository;
    @Autowired private SiswaRepository siswaRepository;
    @Autowired private MataPelajaranRepository mataPelajaranRepository;

    @GetMapping("/dashboard")
    public String adminDashboard(Model model) {
        model.addAttribute("totalGuru", guruRepository.count());
        model.addAttribute("totalSiswa", siswaRepository.count());
        model.addAttribute("totalMapel", mataPelajaranRepository.count());
        // --- PERBAIKAN: Menyamakan nama variabel dengan yang ada di template ---
        model.addAttribute("totalPengguna", userService.countUsers());
        return "admin/dashboard";
    }

    // --- MANAJEMEN GURU ---
    @GetMapping("/manajemen-guru")
    public String manajemenGuru(Model model) {
        model.addAttribute("gurus", guruRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
        return "admin/manajemen-guru";
    }

    @GetMapping("/manajemen-guru/tambah")
    public String showAddGuruForm(Model model) {
        RegistrationRequest request = new RegistrationRequest();
        request.setRole(RoleEnum.GURU);
        model.addAttribute("registrationRequest", request);
        model.addAttribute("formTitle", "Tambah Guru Baru");
        model.addAttribute("actionUrl", "/admin/manajemen-guru/simpan");
        model.addAttribute("isSiswa", false);
        model.addAttribute("isEdit", false);
        return "admin/form-guru-siswa";
    }

    @GetMapping("/manajemen-guru/edit/{id}")
    public String showEditGuruForm(@PathVariable Long id, Model model) {
        Optional<Guru> guruOptional = guruRepository.findById(id);
        if (guruOptional.isEmpty()) {
            return "redirect:/admin/manajemen-guru?error=Guru not found";
        }
        Guru guru = guruOptional.get();
        RegistrationRequest req = new RegistrationRequest();
        req.setUsername(guru.getUser().getUsername());
        req.setNamaLengkap(guru.getNama());
        req.setRole(RoleEnum.GURU);
        req.setPassword("");
        req.setNip(guru.getNip());

        model.addAttribute("registrationRequest", req);
        model.addAttribute("formTitle", "Edit Data Guru");
        model.addAttribute("actionUrl", "/admin/manajemen-guru/update/" + id);
        model.addAttribute("isEdit", true);
        model.addAttribute("isSiswa", false);
        return "admin/form-guru-siswa";
    }

    @PostMapping("/manajemen-guru/simpan")
    public String simpanGuru(@ModelAttribute RegistrationRequest registrationRequest, RedirectAttributes redirectAttributes) {
        try {
            registrationRequest.setRole(RoleEnum.GURU);
            userService.registerNewUser(registrationRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Guru " + registrationRequest.getNamaLengkap() + " berhasil ditambahkan.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/manajemen-guru";
    }

    @PostMapping("/manajemen-guru/update/{id}")
    public String updateGuru(@PathVariable Long id, @ModelAttribute RegistrationRequest registrationRequest, RedirectAttributes redirectAttributes) {
        try {
            userService.updateGuru(id, registrationRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Data Guru " + registrationRequest.getNamaLengkap() + " berhasil diperbarui.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/manajemen-guru";
    }

    @PostMapping("/manajemen-guru/hapus/{id}")
    public String hapusGuru(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteGuru(id);
            redirectAttributes.addFlashAttribute("successMessage", "Guru berhasil dihapus.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus guru: " + e.getMessage());
        }
        return "redirect:/admin/manajemen-guru";
    }

    // --- MANAJEMEN SISWA ---
    @GetMapping("/manajemen-siswa")
    public String manajemenSiswa(Model model) {
        model.addAttribute("siswas", siswaRepository.findAll(Sort.by(Sort.Direction.ASC, "id")));
        return "admin/manajemen-siswa";
    }

    @GetMapping("/manajemen-siswa/tambah")
    public String showAddSiswaForm(Model model) {
        RegistrationRequest request = new RegistrationRequest();
        request.setRole(RoleEnum.SISWA);
        model.addAttribute("registrationRequest", request);
        model.addAttribute("formTitle", "Tambah Siswa Baru");
        model.addAttribute("actionUrl", "/admin/manajemen-siswa/simpan");
        model.addAttribute("isSiswa", true);
        model.addAttribute("isEdit", false);
        return "admin/form-guru-siswa";
    }

    @GetMapping("/manajemen-siswa/edit/{id}")
    public String showEditSiswaForm(@PathVariable Long id, Model model) {
        Optional<Siswa> siswaOptional = siswaRepository.findById(id);
        if (siswaOptional.isEmpty()) {
            return "redirect:/admin/manajemen-siswa?error=Siswa not found";
        }
        Siswa siswa = siswaOptional.get();
        RegistrationRequest req = new RegistrationRequest();
        req.setUsername(siswa.getUser().getUsername());
        req.setNamaLengkap(siswa.getNama());
        req.setRole(RoleEnum.SISWA);
        req.setPassword("");

        req.setKelas(siswa.getKelas());
        req.setTanggalLahir(siswa.getTanggalLahir());
        req.setJenisKelamin(siswa.getJenisKelamin());
        req.setNamaOrangTua(siswa.getNamaOrangTua());

        model.addAttribute("registrationRequest", req);
        model.addAttribute("formTitle", "Edit Data Siswa");
        model.addAttribute("actionUrl", "/admin/manajemen-siswa/update/" + id);
        model.addAttribute("isEdit", true);
        model.addAttribute("isSiswa", true);
        return "admin/form-guru-siswa";
    }

    @PostMapping("/manajemen-siswa/simpan")
    public String simpanSiswa(@ModelAttribute RegistrationRequest registrationRequest, RedirectAttributes redirectAttributes) {
        try {
            registrationRequest.setRole(RoleEnum.SISWA);
            userService.registerNewUser(registrationRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Siswa " + registrationRequest.getNamaLengkap() + " berhasil ditambahkan.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/manajemen-siswa";
    }

    @PostMapping("/manajemen-siswa/update/{id}")
    public String updateSiswa(@PathVariable Long id, @ModelAttribute RegistrationRequest registrationRequest, RedirectAttributes redirectAttributes) {
        try {
            userService.updateSiswa(id, registrationRequest);
            redirectAttributes.addFlashAttribute("successMessage", "Data Siswa " + registrationRequest.getNamaLengkap() + " berhasil diperbarui.");
        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/admin/manajemen-siswa";
    }

    @PostMapping("/manajemen-siswa/hapus/{id}")
    public String hapusSiswa(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteSiswa(id);
            redirectAttributes.addFlashAttribute("successMessage", "Siswa berhasil dihapus.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus siswa: " + e.getMessage());
        }
        return "redirect:/admin/manajemen-siswa";
    }

    // --- MANAJEMEN MATA PELAJARAN ---
    @GetMapping("/manajemen-mapel")
    public String manajemenMapel(Model model) {
        List<MataPelajaran> mapels = mataPelajaranRepository.findAll();
        model.addAttribute("mapels", mapels);
        return "admin/manajemen-mapel";
    }

    @GetMapping("/manajemen-mapel/tambah")
    public String showAddMapelForm(Model model) {
        model.addAttribute("mataPelajaran", new MataPelajaran());
        model.addAttribute("formTitle", "Tambah Mata Pelajaran Baru");
        model.addAttribute("actionUrl", "/admin/manajemen-mapel/simpan");
        return "admin/form-mapel";
    }

    @GetMapping("/manajemen-mapel/edit/{id}")
    public String showEditMapelForm(@PathVariable Long id, Model model) {
        Optional<MataPelajaran> mapelOptional = mataPelajaranRepository.findById(id);
        if (mapelOptional.isEmpty()) {
            return "redirect:/admin/manajemen-mapel?error=Mata pelajaran not found";
        }
        MataPelajaran mataPelajaran = mapelOptional.get();

        model.addAttribute("mataPelajaran", mataPelajaran);
        model.addAttribute("formTitle", "Edit Mata Pelajaran");
        model.addAttribute("actionUrl", "/admin/manajemen-mapel/update/" + id);
        model.addAttribute("isEdit", true);

        return "admin/form-mapel";
    }

    @PostMapping("/manajemen-mapel/simpan")
    public String simpanMapel(@ModelAttribute MataPelajaran mataPelajaran, RedirectAttributes redirectAttributes) {
        try {
            mataPelajaranRepository.save(mataPelajaran);
            redirectAttributes.addFlashAttribute("successMessage", "Mata pelajaran " + mataPelajaran.getNamaMataPelajaran() + " berhasil ditambahkan.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menambahkan mata pelajaran: " + e.getMessage());
        }
        return "redirect:/admin/manajemen-mapel";
    }

    @PostMapping("/manajemen-mapel/update/{id}")
    public String updateMapel(@PathVariable Long id, @ModelAttribute MataPelajaran mataPelajaran, RedirectAttributes redirectAttributes) {
        try {
            Optional<MataPelajaran> existingMapel = mataPelajaranRepository.findById(id);
            if (existingMapel.isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "Mata pelajaran tidak ditemukan.");
                return "redirect:/admin/manajemen-mapel";
            }
            MataPelajaran mapelToUpdate = existingMapel.get();
            mapelToUpdate.setNamaMataPelajaran(mataPelajaran.getNamaMataPelajaran());
            mataPelajaranRepository.save(mapelToUpdate);
            redirectAttributes.addFlashAttribute("successMessage", "Mata pelajaran " + mapelToUpdate.getNamaMataPelajaran() + " berhasil diperbarui.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal memperbarui mata pelajaran: " + e.getMessage());
        }
        return "redirect:/admin/manajemen-mapel";
    }

    @PostMapping("/manajemen-mapel/hapus/{id}")
    public String hapusMapel(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            mataPelajaranRepository.deleteById(id);
            redirectAttributes.addFlashAttribute("successMessage", "Mata pelajaran berhasil dihapus.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", "Gagal menghapus mata pelajaran: " + e.getMessage());
        }
        return "redirect:/admin/manajemen-mapel";
    }
}