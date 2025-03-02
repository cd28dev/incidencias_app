/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.JasperReportHelper;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import com.cd.incidenciasappfx.models.Usuario;
import com.cd.incidenciasappfx.service.IUsuarioService;
import com.cd.incidenciasappfx.service.UsuarioServiceImpl;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author CDAA
 */
public class UsuariosViewController implements Initializable {

    private IUsuarioService userService;

    @FXML
    private TableView<Usuario> tablaUsuarios;

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

    @FXML
    private Button btnExportExcel;

    @FXML
    private Button btnExportPdf;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        userService = new UsuarioServiceImpl();
        configColumns();
        cargarUsuarios();
        tablaUsuarios.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tablaUsuarios.getColumns().removeIf(col -> col.getText() == null || col.getText().trim().isEmpty());
        tablaUsuarios.setFocusTraversable(false);
    }

    @FXML
    private void abrirModal0() {
        abrirModal(0);
    }
    

    private void abrirModal(int numero) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cd/incidenciasappfx/views/NuevoUsuario.fxml"));
            Parent root = loader.load();

            // Obtener el controlador del modal
            NuevoUsuarioController modalController = loader.getController();

            // Pasar el número al controlador del modal
            modalController.setNumero(numero);
            modalController.setUsuariosViewController(this);
            
            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal
            modalStage.setTitle("Nuevo Usuario");
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);

            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configColumns() {
        tablaUsuarios.getColumns().clear();
        tablaUsuarios.getColumns().addAll(colDoc, colNombres, colApellidos, colEmail, colUsername, colRol, colAccion);

        colDoc.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDni()));
        colNombres.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        colApellidos.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getApellido()));
        colEmail.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCorreo()));
        colUsername.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getUsuario()));
        colRol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getRol().getNombre()));

        configurarColumnaAccion();

    }

    private void configurarColumnaAccion() {
        colAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btnActualizar = crearBotonActualizar(this);
            private final Button btnEliminar = crearBotonEliminar(this);
            private final HBox contenedorBotones = new HBox(10, btnActualizar, btnEliminar);

            {
                configurarContenedorBotones(contenedorBotones);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(contenedorBotones);
                }
            }
        });
    }

    /**
     * Crea y configura el botón de actualizar.
     */
    private Button crearBotonActualizar(TableCell<Usuario, Void> cell) {
        Button btn = new Button();
        ImageView iconActualizar = new ImageView(new Image(getClass().getResourceAsStream("/com/cd/incidenciasappfx/images/update.png")));
        iconActualizar.setFitWidth(20);
        iconActualizar.setFitHeight(20);
        btn.setGraphic(iconActualizar);
        btn.setStyle("-fx-background-color: transparent;");

        // Acción al hacer clic
        btn.setOnAction(event -> {
            Usuario usuario = cell.getTableView().getItems().get(cell.getIndex());
            Optional<Usuario> userOptional = userService.findById(usuario.getDni());
            if(userOptional.isPresent()){
                abrirModalActualizar(userOptional.get(),1);
            }
            
        });

        // Efecto Hover
        btn.setOnMouseEntered(event -> btn.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1); -fx-border-radius: 5px; -fx-background-radius: 5px;"));
        btn.setOnMouseExited(event -> btn.setStyle("-fx-background-color: transparent;"));

        return btn;
    }

    private Button crearBotonEliminar(TableCell<Usuario, Void> cell) {
        Button btn = new Button();
        ImageView iconEliminar = new ImageView(new Image(getClass().getResourceAsStream("/com/cd/incidenciasappfx/images/delete.png")));
        iconEliminar.setFitWidth(20);
        iconEliminar.setFitHeight(20);
        btn.setGraphic(iconEliminar);
        btn.setStyle("-fx-background-color: transparent;");

        // Acción al hacer clic
        btn.setOnAction(event -> eliminarUsuario());

        // Efecto Hover
        btn.setOnMouseEntered(event -> btn.setStyle("-fx-background-color: rgba(255, 0, 0, 0.2); -fx-border-radius: 5px; -fx-background-radius: 5px;"));
        btn.setOnMouseExited(event -> btn.setStyle("-fx-background-color: transparent;"));

        return btn;
    }

    private void configurarContenedorBotones(HBox contenedor) {
        contenedor.setAlignment(Pos.CENTER);
    }

    private void abrirModalActualizar(Usuario usuario, int number) {
        try {
            // Cargar el archivo FXML del modal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cd/incidenciasappfx/views/NuevoUsuario.fxml"));
            Parent root = loader.load();

            // Obtener el controlador del modal
            NuevoUsuarioController controller = loader.getController();
            
            controller.setNumero(number);
            
            controller.cargarDatosUsuario(usuario);
            controller.setUsuariosViewController(this);
            controller.changeTitle();
            // Crear la nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Actualizar Usuario");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tablaUsuarios.getScene().getWindow());
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarUsuario() {
        Usuario usuario = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (usuario != null) {
            System.out.println("Eliminar: " + usuario.getDni());
        } else {
            System.out.println("No hay usuario seleccionado.");
        }
    }

    public void cargarUsuarios() {
        List<Usuario> users = userService.findAll();
        ObservableList<Usuario> usuariosList = FXCollections.observableArrayList(users);
        tablaUsuarios.setItems(usuariosList);
    }

    @FXML
    private void exportarExcel() {
        Path documentsDir = Paths.get(System.getProperty("user.home"), "Documents");
        String outputPath = documentsDir.resolve("UsuarioReport.xlsx").toString();

        List<Usuario> listaUsuarios = userService.findAll();
        Map<String, Object> params = new HashMap<>();
        params.put("ReportTitle", "Reporte de Usuarios");

        JasperReportHelper.generateReport(
                "/com/cd/incidenciasappfx/report/UsuarioReportExcel.jrxml", outputPath,
                params,
                listaUsuarios,
                new ExcelReportExporter()
        );
    }

    @FXML
    private void exportarPDF() {
        Path documentsDir = Paths.get(System.getProperty("user.home"), "Documents");
        String outputPath = documentsDir.resolve("UsuarioReport.xlsx").toString();

        List<Usuario> listaUsuarios = userService.findAll();
        Map<String, Object> params = new HashMap<>();
        params.put("ReportTitle", "Reporte de Usuarios");

        JasperReportHelper.generateReport(
                "/com/cd/incidenciasappfx/report/UsuarioReportPdf.jrxml",
                outputPath,
                params,
                listaUsuarios,
                new PdfReportExporter()
        );
    }

}
