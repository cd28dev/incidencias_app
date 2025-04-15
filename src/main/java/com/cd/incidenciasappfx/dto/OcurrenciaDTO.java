package com.cd.incidenciasappfx.dto;

public class OcurrenciaDTO {
    private String tipoOcurrencia;
    private int cantidad;

    public OcurrenciaDTO(String tipoOcurrencia, int cantidad) {
        this.tipoOcurrencia = tipoOcurrencia;
        this.cantidad = cantidad;
    }

    public String getTipoOcurrencia() {
        return tipoOcurrencia;
    }

    public int getCantidad() {
        return cantidad;
    }
}
