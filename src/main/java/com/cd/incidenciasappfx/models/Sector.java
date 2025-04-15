package com.cd.incidenciasappfx.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 * Sector.java
 *
 * @author CDAA
 */
@Entity
@Table(name = "sectores")
public class Sector implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_sector")
    private Integer id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "sector", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Urbanizacion> urbanizaciones;

    // Constructores, getters y setters
    public Sector() {
        urbanizaciones = new ArrayList<Urbanizacion>();
    }

    public Sector(String nombre) {
        this.nombre = nombre;
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

    public List<Urbanizacion> getUrbanizaciones() {
        return urbanizaciones;
    }

    public void setUrbanizaciones(Urbanizacion urbanizacion) {
        this.urbanizaciones.add(urbanizacion);
    }

}
