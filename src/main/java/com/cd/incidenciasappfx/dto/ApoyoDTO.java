package com.cd.incidenciasappfx.dto;

public class ApoyoDTO {
    private String tipoApoyo;
    private int total;

    // Constructor
    public ApoyoDTO(String tipoApoyo, int total) {
        this.tipoApoyo = tipoApoyo;
        this.total = total;
    }

    // Getters y Setters
    public String getTipoApoyo() {
        return tipoApoyo;
    }

    public void setTipoApoyo(String tipoApoyo) {
        this.tipoApoyo = tipoApoyo;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    @Override
    public String toString() {
        return "ApoyoDTO{" +
                "tipoApoyo='" + tipoApoyo + '\'' +
                ", total=" + total +
                '}';
    }
}
