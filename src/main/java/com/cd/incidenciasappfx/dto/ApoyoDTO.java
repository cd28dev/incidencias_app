package com.cd.incidenciasappfx.dto;

public class ApoyoDTO {
    private String tipoApoyo;
    private long total;

    // Constructor
    public ApoyoDTO(String tipoApoyo, long total) {
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

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
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
