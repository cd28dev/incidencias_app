package com.cd.incidenciasappfx.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "agraviados")
public class Agraviado implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_agraviado")
    private Integer idAgraviado;

    @ManyToOne
    @JoinColumn(name = "id_incidencia", nullable = false)
    private Incidencia incidencia;

    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;

    @ManyToOne
    @JoinColumn(name = "id_persona")
    private Persona persona;


    public Agraviado() {
        persona = new Persona();
    }

    public Integer getIdAgraviado() {
        return idAgraviado;
    }

    public void setIdAgraviado(Integer idAgraviado) {
        this.idAgraviado = idAgraviado;
    }

    public Incidencia getIncidencia() {
        return incidencia;
    }

    public void setIncidencia(Incidencia incidencia) {
        this.incidencia = incidencia;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Persona getPersona() {
        return this.persona;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }
}
