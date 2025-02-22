
package com.cd.incidenciasappfx.models;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.List;

/**
 *
 * @author CDAA
 */
@Entity
@Table(name = "unidades_apoyo")
public class UnidadApoyo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idUnidad;

    @Column(unique = true, nullable = false)
    private String nombre;
    
    @OneToMany(mappedBy = "unidadApoyo", cascade = CascadeType.ALL)
    private List<Incidencia> incidencias;

    public int getIdUnidad() {
        return idUnidad;
    }

    public void setIdUnidad(int idUnidad) {
        this.idUnidad = idUnidad;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    
}
