package com.cd.incidenciasappfx.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * ${name}.java
 *
 * @author ${user}
 */
@Entity
@Table(name = "delitos")
public class Delito implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_delito")
    private int idDelito;

    @Column(name = "nombre", unique = true, nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "delitos")
    private List<Incidencia> incidencias;

    public Delito() {
        incidencias = new ArrayList<Incidencia>();
    }

    public int getIdDelito() {
        return idDelito;
    }

    public void setIdDelito(int idDelito) {
        this.idDelito = idDelito;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Incidencia> getIncidencias() {
        return incidencias;
    }

    public void setIncidencias(Incidencia incidencia) {
        this.incidencias.add(incidencia);
    }


}
