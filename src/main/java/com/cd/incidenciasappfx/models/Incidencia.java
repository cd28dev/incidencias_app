
package com.cd.incidenciasappfx.models;

import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author CDAA
 */

@Entity
@Table(name = "incidencias")
public class Incidencia implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id_incidencia")
    private int idIncidencia;

    @Column(name="direccion",nullable = false)
    private String direccion;

    @Column(name="fecha_hora_incidencia",nullable = false)
    private LocalDateTime fecha_hora_incidencia;

    @Column(name="fecha_hora_registro",nullable = false)
    private LocalDateTime fecha_hora_registro;

    @Column(name="descripcion",columnDefinition = "TEXT")
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(name = "apoyo_policial", columnDefinition = "ENUM('Sí', 'No')")
    private ApoyoPolicial apoyo_policial;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario user;

    @OneToMany(mappedBy = "incidencia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Agraviado> agraviados;

    @OneToMany(mappedBy = "incidencia", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Infractor> infractores;

    @ManyToMany
    @JoinTable(
        name = "incidencias_ocurrencias",
        joinColumns = @JoinColumn(name = "id_incidencia"),
        inverseJoinColumns = @JoinColumn(name = "id_ocurrencia")
    )
    private List<TipoOcurrencia> ocurrencias;

    @ManyToMany
    @JoinTable(
        name = "incidencias_intervenciones",
        joinColumns = @JoinColumn(name = "id_incidencia"),
        inverseJoinColumns = @JoinColumn(name = "id_intervencion")
    )
    private List<TipoIntervencion> intervenciones;

    @ManyToMany
    @JoinTable(
        name = "incidencias_delitos",
        joinColumns = @JoinColumn(name = "id_incidencia"),
        inverseJoinColumns = @JoinColumn(name = "id_delito")
    )
    private List<Delito> delitos;

    @ManyToMany
    @JoinTable(
        name = "incidencias_servicios",
        joinColumns = @JoinColumn(name = "id_incidencia"),
        inverseJoinColumns = @JoinColumn(name = "id_servicio")
    )
    private List<ServicioSerenazgo> servicios;
    
    
    @ManyToMany
    @JoinTable(
        name = "incidencias_urbanizaciones",
        joinColumns = @JoinColumn(name = "id_incidencia"),
        inverseJoinColumns = @JoinColumn(name = "id_urbanizacion")
    )
    private List<Urbanizacion> urbanizaciones;

    public enum ApoyoPolicial {SI, NO};

    public Incidencia() {
        urbanizaciones = new ArrayList<>();
        servicios = new ArrayList<>();
        delitos = new ArrayList<>();
        infractores = new ArrayList<>();
        agraviados = new ArrayList<>();
        ocurrencias = new ArrayList<>();
        intervenciones = new ArrayList<>();
    }

    public Incidencia(int idIncidencia, String direccion, LocalDateTime fecha_hora_incidencia, String descripcion, Usuario usuario) {
        this.idIncidencia = idIncidencia;
        this.direccion = direccion;
        this.fecha_hora_incidencia = fecha_hora_incidencia;
        this.descripcion = descripcion;
        this.user = usuario;
    }

    public ApoyoPolicial getApoyo_policial() {
        return apoyo_policial;
    }

    public void setApoyo_policial(String apoyo_policial) {
        this.apoyo_policial = ApoyoPolicial.valueOf(apoyo_policial);
    }

    public List<Delito> getDelitos() {
        return delitos;
    }

    public void setDelitos(Delito delito) {
        this.delitos.add(delito);
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public LocalDateTime getFecha_hora_incidencia() {
        return fecha_hora_incidencia;
    }

    public void setFecha_hora_incidencia(LocalDateTime fecha_hora_incidencia) {
        this.fecha_hora_incidencia = fecha_hora_incidencia;
    }

    public LocalDateTime getFecha_hora_registro() {
        return fecha_hora_registro;
    }

    public void setFecha_hora_registro(LocalDateTime fecha_hora_registro) {
        this.fecha_hora_registro = fecha_hora_registro;
    }

    public int getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(int idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public List<TipoIntervencion> getIntervenciones() {
        return intervenciones;
    }

    public void setIntervenciones(TipoIntervencion intervencion) {
        this.intervenciones.add(intervencion);
    }

    public List<TipoOcurrencia> getOcurrencias() {
        return ocurrencias;
    }

    public void setOcurrencias(TipoOcurrencia ocurrencia) {
        this.ocurrencias.add(ocurrencia);
    }

    public List<ServicioSerenazgo> getServicios() {
        return servicios;
    }

    public void setServicios(ServicioSerenazgo servicio) {
        this.servicios.add(servicio);
    }

    public List<Urbanizacion> getUrbanizaciones() {
        return urbanizaciones;
    }

    public void setUrbanizaciones(Urbanizacion urbanizacion) {

        this.urbanizaciones.add(urbanizacion);
    }

    public Usuario getUser() {
        return user;
    }

    public List<Agraviado> getAgraviados() {
        return agraviados;
    }

    public void setAgraviados(Agraviado agraviado) {
        this.agraviados.add(agraviado);
    }

    public List<Infractor> getInfractores() {
        return infractores;
    }

    public void setInfractores(Infractor infractor) {
        this.infractores.add(infractor);
    }

    public void setUser(Usuario user) {
        this.user = user;
    }
}