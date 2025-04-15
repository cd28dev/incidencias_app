package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ControllerHelper;
import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import com.cd.incidenciasappfx.models.Incidencia;
import com.cd.incidenciasappfx.service.IIncidenciaService;
import com.cd.incidenciasappfx.service.IncidenciaServiceImpl;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ResourceBundle;

/**
 * @author CDAA
 */
public class IncidenciasController extends ControllerHelper<Incidencia> implements Initializable {
    @FXML
    private TableView<Incidencia> tabla;

    @FXML
    public TableColumn<Incidencia, LocalDateTime> colFecha;
    @FXML
    public TableColumn<Incidencia, String> colUbicacion;
    @FXML
    public TableColumn<Incidencia, String> colTipoReporte;
    @FXML
    public TableColumn<Incidencia, String> colTipoIntervencion;
    @FXML
    public TableColumn<Incidencia, String> colServicio;
    @FXML
    public TableColumn<Incidencia, String> colInfraccion;
    @FXML
    public TableColumn<Incidencia, String> colDetalle;

    private IIncidenciaService incidenciaService;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        incidenciaService = new IncidenciaServiceImpl();
        configColumns();
        cargarIncidencias();
    }

    @FXML
    private void abrirModal0() {
        abrirModal("/com/cd/incidenciasappfx/views/NuevaIncidencia.fxml",
                (NuevaIncidenciaController modalController) -> {
                    modalController.setNumero(0);
                    modalController.setIncidenciaViewController(this);
                },
                "Nueva Incidencia",
                tabla.getScene().getWindow());
    }

    private void configColumns() {
        tabla.getColumns().clear();
        tabla.getColumns().addAll(colFecha, colUbicacion, colTipoReporte, colTipoIntervencion, colServicio, colInfraccion, colDetalle, colAccion);

        colFecha.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFecha_hora_incidencia()));
        colUbicacion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDireccion()));
        colTipoReporte.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getOcurrencias().get(0).getNombre()));
        colTipoIntervencion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getIntervenciones().get(0).getNombre()));
        colServicio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getServicios().get(0).getNombre()));
        colInfraccion.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDelitos().get(0).getNombre()));

        configurarColumnaAccion(this::abrirModalActualizar, this::eliminarIncidencia);
        configurarColumnaVer(colDetalle, this::abrilModalDetalle);

    }

    private void abrirModalActualizar(Incidencia incidencia) {
        abrirModal("/com/cd/incidenciasappfx/views/NuevaIncidencia.fxml",
                (NuevaIncidenciaController controller) -> {
                    controller.setNumero(1);
                    controller.cargarCamposIncidencias(incidencia);
                    controller.setIncidenciaViewController(this);
                },
                "Actualizar Incidencia",
                tabla.getScene().getWindow()
        );
    }

    private void abrilModalDetalle(Incidencia incidencia) {
        abrirModal("/com/cd/incidenciasappfx/views/DetalleIncidencia.fxml",
                (DetalleIncidenciaController controller) -> {
                    controller.cargarDetalleIncidencia(incidencia);
                    controller.setIncidenciaViewController(this);
                },
                "Detalle",
                tabla.getScene().getWindow()
        );
    }

    public void cargarIncidencias() {
        cargarTabla(tabla, incidenciaService::findAll);
    }

    private void eliminarIncidencia(Incidencia incidencia) {
        eliminarRegistro(incidencia, incidenciaService::delete, () -> cargarTabla(tabla, incidenciaService::findAll), tabla);

    }

    @FXML
    private void exportarExcel() {
        exportarReporte("/com/cd/incidenciasappfx/report/UsuarioReportExcel.jrxml", "UsuarioReport.xlsx",
                incidenciaService::findAll, new ExcelReportExporter());
    }

    @FXML
    private void exportarPDF() {
        exportarReporte("/com/cd/incidenciasappfx/report/UsuarioReportPdf.jrxml", "UsuarioReport.pdf",
                incidenciaService::findAll, new PdfReportExporter());
    }

}
