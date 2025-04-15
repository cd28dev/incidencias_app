package com.cd.incidenciasappfx.dto;

public class UrbanizacionDTO {
    private String urbanizacion;
    private int cantidad;

    public UrbanizacionDTO(String urbanizacion, int cantidad) {
        this.urbanizacion = urbanizacion;
        this.cantidad = cantidad;
    }

    public String getUrbanizacion() {
        return urbanizacion;
    }

    public int getCantidad() {
        return cantidad;
    }
}
