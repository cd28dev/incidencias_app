
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
@Table(name = "tipos_intervencion")
public class TipoIntervencion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_intervencion")
    private int idIntervencion;

    @Column(unique = true, nullable = false)
    private String nombre;

    @ManyToMany(mappedBy = "intervenciones")
    private List<Incidencia> incidencias;

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

    public void setIncidencias(List<Incidencia> incidencias) {
        this.incidencias = incidencias;
    }


}
