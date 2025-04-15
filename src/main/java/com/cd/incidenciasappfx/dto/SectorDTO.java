package com.cd.incidenciasappfx.dto;

public class SectorDTO {
    private String sector;
    private int cantidad;

    public SectorDTO(String sector, int cantidad) {
        this.sector = sector;
        this.cantidad = cantidad;
    }

    public String getSector() {
        return sector;
    }

    public int getCantidad() {
        return cantidad;
    }
}
