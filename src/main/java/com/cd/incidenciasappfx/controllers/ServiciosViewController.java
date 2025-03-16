package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ControllerHelper;
import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import com.cd.incidenciasappfx.models.ServicioSerenazgo;
import com.cd.incidenciasappfx.service.IServiciosService;
import com.cd.incidenciasappfx.service.ServicioServiceImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class ServiciosViewController extends ControllerHelper<ServicioSerenazgo> implements Initializable {

    private IServiciosService serviciosService;
    @FXML
    protected TableView<ServicioSerenazgo> tabla;
    @FXML
    private TableColumn<ServicioSerenazgo, String> colServicio;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        serviciosService = new ServicioServiceImpl();
        configColumns();
        cargarServicios();
    }

    @FXML
    private void abrirModal0() {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoServicio.fxml",
                (NuevoServicioController modalController) -> {
                    modalController.setNumero(0);
                    modalController.setServicioViewController(this);
                },
                "Nuevo Servicio",
                tabla.getScene().getWindow());
    }

    private void configColumns() {
        tabla.getColumns().clear();
        tabla.getColumns().addAll(colServicio, colAccion);

        id.setCellValueFactory(data
                -> new ReadOnlyObjectWrapper<>(data.getValue().getIdServicio())
        );
        colServicio.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        configurarColumnaAccion(
                this::abrirModalActualizar,
                this::eliminarServicios);
    }

    private void abrirModalActualizar(ServicioSerenazgo s) {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoServicio.fxml",
                (NuevoServicioController controller) -> {
                    controller.setNumero(1);
                    controller.cargarCamposServicio(s);
                    controller.setServicioViewController(this);
                },
                "Actualizar Servicio",
                tabla.getScene().getWindow());
    }

    public void cargarServicios(){
        cargarTabla(tabla,serviciosService::findAll);
    }

    private void eliminarServicios(ServicioSerenazgo servicioSerenazgo) {
        eliminarRegistro(servicioSerenazgo, s -> serviciosService.delete(s), () -> cargarTabla(tabla,serviciosService::findAll),tabla);

    }

    @FXML
    private void exportarExcel() {
        exportarReporte("/com/cd/incidenciasappfx/report/SectorReportExcel.jrxml", "SectorReport.xlsx",
                serviciosService::findAll, new ExcelReportExporter());
    }

    @FXML
    private void exportarPDF() {
        exportarReporte("/com/cd/incidenciasappfx/report/SectorReportPdf.jrxml", "SectorReport.pdf",
                serviciosService::findAll, new PdfReportExporter());
    }

}