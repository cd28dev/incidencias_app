package com.cd.incidenciasappfx.models;

import jakarta.persistence.*;

import java.io.Serializable;

/**
 * Urbanizacion.java
 *
 * @author CDAA
 */
@Entity
@Table(name = "urbanizaciones")
public class Urbanizacion implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_urbanizacion")
    private Integer id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_sector", nullable = false)
    private Sector sector;

    // Constructores, getters y setters
    public Urbanizacion() {
    }

    public Urbanizacion(String nombre, Sector sector) {
        this.nombre = nombre;
        this.sector = sector;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Sector getSector() {
        return sector;
    }

    public void setSector(Sector sector) {
        this.sector = sector;
    }
}
