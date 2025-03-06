/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ControllerHelper;
import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import com.cd.incidenciasappfx.models.Usuario;
import com.cd.incidenciasappfx.service.IUsuarioService;
import com.cd.incidenciasappfx.service.UsuarioServiceImpl;
import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.TableColumn;

/**
 * FXML Controller class
 *
 * @author CDAA
 */
public class UsuariosViewController extends ControllerHelper<Usuario> implements Initializable {

    private IUsuarioService userService;

    @FXML
    private TableColumn<Usuario, String> colDoc;
    @FXML
    private TableColumn<Usuario, String> colNombres;
    @FXML
    private TableColumn<Usuario, String> colApellidos;
    @FXML
    private TableColumn<Usuario, String> colEmail;
    @FXML
    private TableColumn<Usuario, String> colUsername;
    @FXML
    private TableColumn<Usuario, String> colRol;
    @FXML
    private TableColumn<Usuario, Void> colAccion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userService = new UsuarioServiceImpl();
        configColumns();
        cargarUsuarios();
    }

    @FXML
    private void abrirModal0() {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoUsuario.fxml",
                (NuevoUsuarioController modalController) -> {
                    modalController.setNumero(0);
                    modalController.setUsuariosViewController(this);
                },
                "Nuevo Usuario");
    }

    private void configColumns() {
        tabla.getColumns().clear();
        tabla.getColumns().addAll(colDoc, colNombres, colApellidos, colEmail, colUsername, colRol, colAccion);

        colDoc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDni()));
        colNombres.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colApellidos.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getApellido()));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCorreo()));
        colUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsuario()));
        colRol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRol().getNombre()));

        configurarColumnaAccion(colAccion,
                usuario -> abrirModalActualizar(usuario, 1),
                usuario -> eliminarUsuario(usuario));

    }

    private void abrirModalActualizar(Usuario usuario, int number) {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoUsuario.fxml",
                (NuevoUsuarioController controller) -> {
                    controller.setNumero(number);
                    controller.cargarCamposUsuario(usuario);
                    controller.setUsuariosViewController(this);
                },
                "Actualizar Usuario");
    }

    public void cargarUsuarios(){
        cargarTabla(userService::findAll);
    }
    
    private void eliminarUsuario(Usuario u) {
        eliminarRegistro(u, userService::delete, () -> cargarTabla(userService::findAll));

    }

    @FXML
    private void exportarExcel() {
        exportarReporte("/com/cd/incidenciasappfx/report/UsuarioReportExcel.jrxml", "UsuarioReport.xlsx",
                userService::findAll, new ExcelReportExporter());
    }

    @FXML
    private void exportarPDF() {
        exportarReporte("/com/cd/incidenciasappfx/report/UsuarioReportPdf.jrxml", "UsuarioReport.pdf",
                userService::findAll, new PdfReportExporter());
    }

}
