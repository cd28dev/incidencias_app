package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ControllerHelper;
import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
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
 * RolesViewController.java
 *
 * @author CDAA
 */
public class RolesViewController extends ControllerHelper<Rol> implements Initializable {

    private IRolesService rolService;
    @FXML
    protected TableView<Rol> tabla;
    @FXML
    private TableColumn<Rol, String> colRol;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rolService = new RolesServiceImpl();
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
        tabla.getColumns().addAll(colRol, colAccion);

        id.setCellValueFactory(data -> new ReadOnlyObjectWrapper<>(data.getValue().getIdRol()));
        colRol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        configurarColumnaAccion(
                this::abrirModalActualizar,
                this::eliminarRol);
    }

    private void abrirModalActualizar(Rol rol) {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoRol.fxml",
                (NuevoRolController controller) -> {
                    controller.setNumero(1);
                    controller.cargarCamposRol(rol);
                    controller.setRolViewController(this);
                },
                "Actualizar Rol",
                tabla.getScene().getWindow());
    }

    public void cargarRoles() {
        cargarTabla(tabla,rolService::findAll);
    }

    private void eliminarRol(Rol rol) {
        eliminarRegistro(rol, r -> rolService.delete(r), () -> cargarTabla(tabla,rolService::findAll),tabla);
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
