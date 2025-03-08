package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ControllerHelper;
import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import com.cd.incidenciasappfx.models.Urbanizacion;
import com.cd.incidenciasappfx.service.IUrbService;
import com.cd.incidenciasappfx.service.UrbServiceImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * UrbViewController.java
 *
 * @author CDAA
 */
public class UrbViewController extends ControllerHelper<Urbanizacion> implements Initializable {

    private IUrbService urbService;

    @FXML
    protected TableView<Urbanizacion> tabla;
    @FXML
    private TableColumn<Urbanizacion, Integer> colIdUrb;
    @FXML
    private TableColumn<Urbanizacion, String> colUrb;
    @FXML
    private TableColumn<Urbanizacion, String> colSector;

    @FXML
    private TableColumn<Urbanizacion, Void> colAccion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        urbService = new UrbServiceImpl();
        configColumns();
        cargarUrb();
    }

    @FXML
    private void abrirModal0() {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoUrb.fxml",
                (NuevoUrbController modalController) -> {
                    modalController.setNumero(0);
                    modalController.setUrbViewController(this);
                },
                "Nuevo Sector",
                tabla.getScene().getWindow());
    }

    private void configColumns() {
        tabla.getColumns().clear();
        tabla.getColumns().addAll(colIdUrb,colUrb,colSector, colAccion);

        colIdUrb.setCellValueFactory(data
                -> new ReadOnlyObjectWrapper<>(data.getValue().getId())
        );
        colUrb.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colSector.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getSector().getNombre()));

        configurarColumnaAccion(
                colAccion,
                urb -> abrirModalActualizar(urb, 1),
                urb -> eliminarSector(urb));
    }

    private void abrirModalActualizar(Urbanizacion u, int number) {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoUrb.fxml",
                (NuevoUrbController controller) -> {
                    controller.setNumero(number);
                    controller.cargarCamposUrb(u);
                    controller.setUrbViewController(this);
                },
                "Actualizar Urbanizacion",
                tabla.getScene().getWindow());
    }

    public void cargarUrb() {
        cargarTabla(tabla, urbService::findAll);
    }

    private void eliminarSector(Urbanizacion urb) {
        eliminarRegistro(urb, u -> urbService.delete(u), () -> cargarTabla(tabla,urbService::findAll),tabla);

    }

    @FXML
    private void exportarExcel() {
        exportarReporte("/com/cd/incidenciasappfx/report/SectorReportExcel.jrxml", "SectorReport.xlsx",
                urbService::findAll, new ExcelReportExporter());
    }

    @FXML
    private void exportarPDF() {
        exportarReporte("/com/cd/incidenciasappfx/report/SectorReportPdf.jrxml", "SectorReport.pdf",
                urbService::findAll, new PdfReportExporter());
    }

}
