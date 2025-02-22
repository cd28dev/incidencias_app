
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
 *
 * @author CDAA
 */
@Entity
@Table(name = "tipos_ocurrencia")
public class TipoOcurrencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOcurrencia;

    @Column(unique = true, nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "ocurrencias")
    private List<Incidencia> incidencias;

    public int getIdOcurrencia() {
        return idOcurrencia;
    }

    public void setIdOcurrencia(int idOcurrencia) {
        this.idOcurrencia = idOcurrencia;
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
