
package com.cd.incidenciasappfx.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import java.util.List;

/**
 * ${name}.java
 * 
 * @author ${user}
 */
@Entity
@Table(name = "delitos")
public class Delito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idDelito;

    @Column(unique = true, nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "delitos")
    private List<Incidencia> incidencias;

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

    public void setIncidencias(List<Incidencia> incidencias) {
        this.incidencias = incidencias;
    }
    
    
}
