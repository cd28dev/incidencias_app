package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.models.Incidencia;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class DetalleIncidenciaController {

    @FXML private Label lblServicio;
    @FXML private Label lblDelito;
    @FXML private Label lblIntervencion;
    @FXML private AnchorPane modalPane;
    @FXML private Label lblTitle;
    @FXML private Label lblSector;
    @FXML private Label lblUrbanizacion;
    @FXML private Label lblDireccion;
    @FXML private Label lblFecha;
    @FXML private Label lblOcurrencia;
    @FXML private Label lblDescripcion;
    @FXML private Label lblDniInfractor;
    @FXML private Label lblNombresInfractor;
    @FXML private Label lblApellidosInfractor;
    @FXML private Label lblTelefonoInf;
    @FXML private Label lblDescargoInf;
    @FXML private Label lblDniAgraviado;
    @FXML private Label lblNombresAgraviado;
    @FXML private Label lblApellidosAgraviado;
    @FXML private Label lblDescargoAgr;
    @FXML private Label lblTelefonoAgr;
    @FXML private Button btnCerrar;

    private IncidenciasController incidenciasController;


    public DetalleIncidenciaController() {}


    public void setIncidenciaViewController(IncidenciasController incidenciasController) {
        this.incidenciasController = incidenciasController;
    }

    public void cargarDetalleIncidencia(Incidencia incidencia) {
        if (incidencia != null) {
            cargarDetalle(incidencia);
        }
    }

    private void cargarDetalle(Incidencia incidencia) {
        if (incidencia == null) return;

        lblDireccion.setText(incidencia.getDireccion());
        lblFecha.setText(incidencia.getFecha_hora_incidencia().toLocalDate().toString());
        lblOcurrencia.setText(incidencia.getOcurrencias().get(0).getNombre());
        lblIntervencion.setText(incidencia.getIntervenciones().get(0).getNombre());
        lblDelito.setText(incidencia.getDelitos().get(0).getNombre());
        lblSector.setText(incidencia.getUrbanizaciones().get(0).getSector().getNombre());
        lblUrbanizacion.setText(incidencia.getUrbanizaciones().get(0).getNombre());
        lblServicio.setText(incidencia.getServicios().get(0).getNombre());
        lblDescripcion.setText(incidencia.getDescripcion());

        if (!incidencia.getInfractores().isEmpty()) {
            lblDniInfractor.setText(incidencia.getInfractores().get(0).getPersona().getDocumento());
            lblNombresInfractor.setText(incidencia.getInfractores().get(0).getPersona().getNombres());
            lblApellidosInfractor.setText(incidencia.getInfractores().get(0).getPersona().getApellidos());
            lblDescargoInf.setText(incidencia.getInfractores().get(0).getObservaciones());
            lblTelefonoInf.setText(incidencia.getInfractores().get(0).getPersona().getTelefono());
        }

        if (!incidencia.getAgraviados().isEmpty()) {
            lblDniAgraviado.setText(incidencia.getAgraviados().get(0).getPersona().getDocumento());
            lblNombresAgraviado.setText(incidencia.getAgraviados().get(0).getPersona().getNombres());
            lblApellidosAgraviado.setText(incidencia.getAgraviados().get(0).getPersona().getApellidos());
            lblDescargoAgr.setText(incidencia.getAgraviados().get(0).getObservaciones());
            lblTelefonoAgr.setText(incidencia.getAgraviados().get(0).getPersona().getTelefono());
        }
    }
}
