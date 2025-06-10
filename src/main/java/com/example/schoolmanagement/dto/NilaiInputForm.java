package com.example.schoolmanagement.dto;

public class NilaiInputForm {
    private String nis;
    private Long idMataPelajaran;
    private Integer nilaiTugas;
    private Integer nilaiUts;
    private Integer nilaiUas;

    public NilaiInputForm() {}
    public NilaiInputForm(String nis) { this.nis = nis; }

    public String getNis() { return nis; }
    public void setNis(String nis) { this.nis = nis; }
    public Long getIdMataPelajaran() { return idMataPelajaran; }
    public void setIdMataPelajaran(Long idMataPelajaran) { this.idMataPelajaran = idMataPelajaran; }
    public Integer getNilaiTugas() { return nilaiTugas; }
    public void setNilaiTugas(Integer nilaiTugas) { this.nilaiTugas = nilaiTugas; }
    public Integer getNilaiUts() { return nilaiUts; }
    public void setNilaiUts(Integer nilaiUts) { this.nilaiUts = nilaiUts; }
    public Integer getNilaiUas() { return nilaiUas; }
    public void setNilaiUas(Integer nilaiUas) { this.nilaiUas = nilaiUas; }
}