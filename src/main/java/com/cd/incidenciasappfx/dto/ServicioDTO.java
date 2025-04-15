package com.cd.incidenciasappfx.dto;

public class ServicioDTO {
    private String servicio;
    private int cantidad;

    public ServicioDTO(String servicio, int cantidad) {
        this.servicio = servicio;
        this.cantidad = cantidad;
    }

    public String getServicio() {
        return servicio;
    }

    public int getCantidad() {
        return cantidad;
    }
}
