/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ControllerHelper;
import static com.cd.incidenciasappfx.helper.ControllerHelper.cargarTabla;
import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import com.cd.incidenciasappfx.models.Delito;
import com.cd.incidenciasappfx.models.Rol;
import com.cd.incidenciasappfx.service.IRolesService;
import com.cd.incidenciasappfx.service.RolesServiceImpl;
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
    
    private IDelitosService delitoService;
    @FXML
    protected TableView<Delito> tabla;
    
    @FXML
    private TableColumn<Delito, Integer> colIdDelito;
    @FXML
    private TableColumn<Delito, String> colDelito;
    @FXML
    private TableColumn<Delito, Void> colAccion;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        delitoService = new DelitosServiceImpl();
        configColumns();
        cargarRoles();
    }
    
    @FXML
    private void abrirModal0() {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoRol.fxml",
                (NuevoRolController modalController) -> {
                    modalController.setNumero(0);
                    modalController.setRolViewController(this);
                },
                "Nuevo Rol",
                tabla.getScene().getWindow());
    }
    
    private void configColumns() {
        tabla.getColumns().clear();
        tabla.getColumns().addAll(colIdRol, colRol, colAccion);
        
        colIdRol.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getIdRol()));
        colRol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        configurarColumnaAccion(
                colAccion,
                delito -> abrirModalActualizar(delito, 1),
                delito -> eliminarRol(delito));
    }
    
    private void abrirModalActualizar(Rol rol, int number) {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoRol.fxml",
                (NuevoRolController controller) -> {
                    controller.setNumero(number);
                    controller.cargarCamposRol(rol);
                    controller.setRolViewController(this);
                },
                "Actualizar Rol",
                tabla.getScene().getWindow());
    }
    
    public void cargarRoles() {
        cargarTabla(tabla, rolService::findAll);
    }
    
    private void eliminarRol(Rol rol) {
        eliminarRegistro(rol, r -> rolService.delete(r), () -> cargarTabla(tabla, rolService::findAll), tabla);
    }
    
    @FXML
    private void exportarExcel() {
        exportarReporte("/com/cd/incidenciasappfx/report/UsuarioReportExcel.jrxml", "RolReport.xlsx", rolService::findAll, new ExcelReportExporter());
    }
    
    @FXML
    private void exportarPDF() {
        exportarReporte("/com/cd/incidenciasappfx/report/RolReportPdf.jrxml", "RolReport.pdf", rolService::findAll, new PdfReportExporter());
    }
    
}
