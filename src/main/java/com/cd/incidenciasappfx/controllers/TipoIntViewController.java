package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ControllerHelper;
import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import com.cd.incidenciasappfx.models.TipoIntervencion;
import com.cd.incidenciasappfx.service.ITipoIntService;
import com.cd.incidenciasappfx.service.TipoIntServiceImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class TipoIntViewController extends ControllerHelper<TipoIntervencion> implements Initializable {

    private ITipoIntService tipoIntService;
    @FXML
    protected TableView<TipoIntervencion> tabla;
    @FXML
    private TableColumn<TipoIntervencion, String> colInt;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tipoIntService = new TipoIntServiceImpl();
        configColumns();
        cargarTipoIntervencion();
    }

    @FXML
    private void abrirModal0() {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoTipoInt.fxml",
                (NuevoTipoIntController modalController) -> {
                    modalController.setNumero(0);
                    modalController.setTipoIntViewController(this);
                },
                "Nuevo T. Intervencion",
                tabla.getScene().getWindow());
    }

    private void configColumns() {
        tabla.getColumns().clear();
        tabla.getColumns().addAll(colInt, colAccion);

        id.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getIdIntervencion()));
        colInt.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        configurarColumnaAccion(
                this::abrirModalActualizar,
                this::eliminarTipoIntervencion);
    }

    private void abrirModalActualizar(TipoIntervencion tipoIntervencion) {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoTipoInt.fxml",
                (NuevoTipoIntController controller) -> {
                    controller.setNumero(1);
                    controller.cargarCamposTipoInt(tipoIntervencion);
                    controller.setTipoIntViewController(this);
                },
                "Actualizar T. Intervencion",
                tabla.getScene().getWindow());
    }

    public void cargarTipoIntervencion() {
        cargarTabla(tabla, tipoIntService::findAll);
    }

    private void eliminarTipoIntervencion(TipoIntervencion tipoIntervencion) {
        eliminarRegistro(tipoIntervencion, r -> tipoIntService.delete(r), () -> cargarTabla(tabla, tipoIntService::findAll), tabla);
    }

    @FXML
    private void exportarExcel() {
        exportarReporte("/com/cd/incidenciasappfx/report/UsuarioReportExcel.jrxml", "RolReport.xlsx", tipoIntService::findAll, new ExcelReportExporter());
    }

    @FXML
    private void exportarPDF() {
        exportarReporte("/com/cd/incidenciasappfx/report/RolReportPdf.jrxml", "RolReport.pdf", tipoIntService::findAll, new PdfReportExporter());
    }

}
