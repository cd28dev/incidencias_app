package com.cd.incidenciasappfx.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author CDAA
 */

@Entity
@Table(name = "tipos_intervencion")
public class TipoIntervencion implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_intervencion")
    private int idIntervencion;

    @Column(unique = true, nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "intervenciones")
    private List<Incidencia> incidencias;

    public TipoIntervencion() {
        incidencias = new ArrayList<Incidencia>();
    }

    public int getIdIntervencion() {
        return idIntervencion;
    }

    public void setIdIntervencion(int idIntervencion) {
        this.idIntervencion = idIntervencion;
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
