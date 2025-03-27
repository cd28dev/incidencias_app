package com.cd.incidenciasappfx.models;

import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "personas")
public class Persona implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_persona")
    private Integer idPersona;

    @Column(name = "nombres", length = 100, nullable = false)
    private String nombres;

    @Column(name = "apellidos", length = 100, nullable = false)
    private String apellidos;

    @Column(name = "n_doc", columnDefinition = "CHAR(15)")
    private String documento;

    @Column(name = "telefono", columnDefinition = "CHAR(9)")
    private String telefono;


    public Persona() {
    }

    public Persona(String apellidos, String documento, Integer idPersona, String nombres, String telefono) {
        this.apellidos = apellidos;
        this.documento = documento;
        this.idPersona = idPersona;
        this.nombres = nombres;
        this.telefono = telefono;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Integer getIdPersona() {
        return idPersona;
    }

    public void setIdPersona(Integer idPersona) {
        this.idPersona = idPersona;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
