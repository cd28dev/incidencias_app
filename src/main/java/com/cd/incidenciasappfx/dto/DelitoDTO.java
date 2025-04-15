package com.cd.incidenciasappfx.dto;

public class DelitoDTO {
    private String delito;
    private int cantidad;

    public DelitoDTO(String delito, int cantidad) {
        this.delito = delito;
        this.cantidad = cantidad;
    }

    public String getDelito() {
        return delito;
    }

    public int getCantidad() {
        return cantidad;
    }
}
