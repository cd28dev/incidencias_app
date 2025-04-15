package com.cd.incidenciasappfx.dto;

public class IntervencionDTO {
    private String tipoIntervencion;
    private int cantidad;

    public IntervencionDTO(String tipoIntervencion, int cantidad) {
        this.tipoIntervencion = tipoIntervencion;
        this.cantidad = cantidad;
    }

    public String getTipoIntervencion() {
        return tipoIntervencion;
    }

    public int getCantidad() {
        return cantidad;
    }
}
