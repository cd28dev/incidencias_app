
package com.cd.incidenciasappfx.models;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import java.util.Date;
import java.util.List;

/**
 *
 * @author CDAA
 */

@Entity
@Table(name = "incidencias")
public class Incidencia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idIncidencia;

    @Column(nullable = false)
    private String direccion;

    @Column(nullable = false)
    private String sector;

    private String urbanizacion;

    @Column(nullable = false)
    @Temporal(TemporalType.DATE)
    private Date fecha;

    @Column(nullable = false)
    @Temporal(TemporalType.TIME)
    private Date hora;

    @Column(columnDefinition = "TEXT")
    private String manifestacion;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(columnDefinition = "TEXT")
    private String resultadoFinal;

    @ManyToOne
    @JoinColumn(name = "id_unidad", nullable = false)
    private UnidadApoyo unidadApoyo;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

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

    public Incidencia() {
    }

    public Incidencia(int idIncidencia, String direccion, String sector, String urbanizacion, Date fecha, Date hora, String manifestacion, String descripcion, String resultadoFinal, UnidadApoyo unidadApoyo, Usuario usuario) {
        this.idIncidencia = idIncidencia;
        this.direccion = direccion;
        this.sector = sector;
        this.urbanizacion = urbanizacion;
        this.fecha = fecha;
        this.hora = hora;
        this.manifestacion = manifestacion;
        this.descripcion = descripcion;
        this.resultadoFinal = resultadoFinal;
        this.unidadApoyo = unidadApoyo;
        this.usuario = usuario;
    }

    public int getIdIncidencia() {
        return idIncidencia;
    }

    public void setIdIncidencia(int idIncidencia) {
        this.idIncidencia = idIncidencia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getUrbanizacion() {
        return urbanizacion;
    }

    public void setUrbanizacion(String urbanizacion) {
        this.urbanizacion = urbanizacion;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Date getHora() {
        return hora;
    }

    public void setHora(Date hora) {
        this.hora = hora;
    }

    public String getManifestacion() {
        return manifestacion;
    }

    public void setManifestacion(String manifestacion) {
        this.manifestacion = manifestacion;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getResultadoFinal() {
        return resultadoFinal;
    }

    public void setResultadoFinal(String resultadoFinal) {
        this.resultadoFinal = resultadoFinal;
    }

    public UnidadApoyo getUnidadApoyo() {
        return unidadApoyo;
    }

    public void setUnidadApoyo(UnidadApoyo unidadApoyo) {
        this.unidadApoyo = unidadApoyo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


}