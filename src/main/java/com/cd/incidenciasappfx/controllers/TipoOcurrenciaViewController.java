package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ControllerHelper;
import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import com.cd.incidenciasappfx.models.TipoOcurrencia;
import com.cd.incidenciasappfx.service.ITipoOcurrenciaService;
import com.cd.incidenciasappfx.service.TipoOcurrenciaServiceImpl;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

import java.net.URL;
import java.util.ResourceBundle;

public class TipoOcurrenciaViewController extends ControllerHelper<TipoOcurrencia> implements Initializable {

    private ITipoOcurrenciaService tipoOcurrenciaService;

    @FXML
    protected TableView<TipoOcurrencia> tabla;
    @FXML
    public TableColumn<TipoOcurrencia, String> colOcu;


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        tipoOcurrenciaService = new TipoOcurrenciaServiceImpl();
        configColumns();
        cargarTipoOcurrencia();
    }

    @FXML
    private void abrirModal0() {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoTipoOcurrencia.fxml",
                (NuevoTipoOcuController modalController) -> {
                    modalController.setNumero(0);
                    modalController.setTipoOcuViewController(this);
                },
                "Nuevo T. Intervencion",
                tabla.getScene().getWindow());
    }

    private void configColumns() {
        tabla.getColumns().clear();
        tabla.getColumns().addAll(colOcu, colAccion);

        id.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getIdOcurrencia()));
        colOcu.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        configurarColumnaAccion(
                this::abrirModalActualizar,
                this::eliminarTipoOcurrencia);
    }

    private void abrirModalActualizar(TipoOcurrencia tipoOcurrencia) {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoTipoOcurrencia.fxml",
                (NuevoTipoOcuController controller) -> {
                    controller.setNumero(1);
                    controller.cargarCamposTipoOcurrencia(tipoOcurrencia);
                    controller.setTipoOcuViewController(this);
                },
                "Actualizar T. Ocurrencia",
                tabla.getScene().getWindow());
    }

    public void cargarTipoOcurrencia() {
        cargarTabla(tabla, tipoOcurrenciaService::findAll);
    }

    private void eliminarTipoOcurrencia(TipoOcurrencia tipoOcurrencia) {
        eliminarRegistro(tipoOcurrencia, r -> tipoOcurrenciaService.delete(r), () -> cargarTabla(tabla, tipoOcurrenciaService::findAll), tabla);
    }

    @FXML
    private void exportarExcel() {
        exportarReporte("/com/cd/incidenciasappfx/report/UsuarioReportExcel.jrxml", "RolReport.xlsx", tipoOcurrenciaService::findAll, new ExcelReportExporter());
    }

    @FXML
    private void exportarPDF() {
        exportarReporte("/com/cd/incidenciasappfx/report/RolReportPdf.jrxml", "RolReport.pdf", tipoOcurrenciaService::findAll, new PdfReportExporter());
    }

}

