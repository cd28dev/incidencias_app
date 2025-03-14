/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ControllerHelper;
import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import com.cd.incidenciasappfx.models.Delito;
import com.cd.incidenciasappfx.service.DelitoServiceImpl;
import com.cd.incidenciasappfx.service.IDelitoService;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * @author CDAA
 */
public class DelitosViewController extends ControllerHelper<Delito> implements Initializable {
    
    private IDelitoService delitoService;
    @FXML
    protected TableView<Delito> tabla;
    @FXML
    private TableColumn<Delito, String> colDelito;
    @FXML
    private TableColumn<Delito, Void> colAccion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        delitoService = new DelitoServiceImpl();
        configColumns();
        cargarDelitos();
    }
    
    @FXML
    private void abrirModal0() {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoDelito.fxml",
                (NuevoDelitoController modalController) -> {
                    modalController.setNumero(0);
                    modalController.setDelitoViewController(this);
                },
                "Nuevo Delito",
                tabla.getScene().getWindow());
    }
    
    private void configColumns() {
        tabla.getColumns().clear();
        tabla.getColumns().addAll(colDelito, colAccion);

        id.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getIdDelito()));
        colDelito.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        configurarColumnaAccion(
                this::abrirModalActualizar,
                this::eliminarDelito);
    }
    
    private void abrirModalActualizar(Delito delito) {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoDelito.fxml",
                (NuevoDelitoController controller) -> {
                    controller.setNumero(1);
                    controller.cargarCamposDelito(delito);
                    controller.setDelitoViewController(this);
                },
                "Actualizar Delito",
                tabla.getScene().getWindow());
    }
    
    public void cargarDelitos() {
        cargarTabla(tabla, delitoService::findAll);
    }
    
    private void eliminarDelito(Delito delito) {
        eliminarRegistro(delito, r -> delitoService.delete(r), () -> cargarTabla(tabla, delitoService::findAll), tabla);
    }
    
    @FXML
    private void exportarExcel() {
        exportarReporte("/com/cd/incidenciasappfx/report/UsuarioReportExcel.jrxml", "RolReport.xlsx", delitoService::findAll, new ExcelReportExporter());
    }
    
    @FXML
    private void exportarPDF() {
        exportarReporte("/com/cd/incidenciasappfx/report/RolReportPdf.jrxml", "RolReport.pdf", delitoService::findAll, new PdfReportExporter());
    }
    
}
