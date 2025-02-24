/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package com.cd.incidenciasappfx.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableView;
import com.cd.incidenciasappfx.models.Usuario;
import com.cd.incidenciasappfx.service.IUsuarioService;
import com.cd.incidenciasappfx.service.UsuarioServiceImpl;
import java.io.IOException;
import java.util.List;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
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
    private void abrirModal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cd/incidenciasappfx/views/NuevoUsuario.fxml"));
            Parent root = loader.load();

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL); // Bloquear la ventana principal
            modalStage.setTitle("Nuevo Usuario");
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);

            // Mostrar el modal
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
            private final Button btnActualizar = new Button();
            private final Button btnEliminar = new Button();
            private final HBox contenedorBotones = new HBox(10, btnActualizar, btnEliminar);

            {
                // Ícono Actualizar
                ImageView iconActualizar = new ImageView(new Image(getClass().getResourceAsStream("/com/cd/incidenciasappfx/images/update.png")));
                iconActualizar.setFitWidth(20);
                iconActualizar.setFitHeight(20);
                btnActualizar.setGraphic(iconActualizar);
                btnActualizar.setStyle("-fx-background-color: transparent;");

                // Ícono Eliminar
                ImageView iconEliminar = new ImageView(new Image(getClass().getResourceAsStream("/com/cd/incidenciasappfx/images/delete.png")));
                iconEliminar.setFitWidth(20);
                iconEliminar.setFitHeight(20);
                btnEliminar.setGraphic(iconEliminar);
                btnEliminar.setStyle("-fx-background-color: transparent;");

                // Acción al hacer clic
                btnActualizar.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    System.out.println("Actualizar: " + usuario.getDni());
                });

                btnEliminar.setOnAction(event -> {
                    Usuario usuario = getTableView().getItems().get(getIndex());
                    System.out.println("Eliminar: " + usuario.getDni());
                });

                // Efecto Hover para btnActualizar
                btnActualizar.setOnMouseEntered(event -> btnActualizar.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1); -fx-border-radius: 5px; -fx-background-radius: 5px;"));
                btnActualizar.setOnMouseExited(event -> btnActualizar.setStyle("-fx-background-color: transparent;"));

                // Efecto Hover para btnEliminar
                btnEliminar.setOnMouseEntered(event -> btnEliminar.setStyle("-fx-background-color: rgba(255, 0, 0, 0.2); -fx-border-radius: 5px; -fx-background-radius: 5px;"));
                btnEliminar.setOnMouseExited(event -> btnEliminar.setStyle("-fx-background-color: transparent;"));

                contenedorBotones.setAlignment(Pos.CENTER);
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

    private void cargarUsuarios() {
        List<Usuario> users = userService.findAll();
        ObservableList<Usuario> usuariosList = FXCollections.observableArrayList(users);
        tablaUsuarios.setItems(usuariosList);
    }

}
