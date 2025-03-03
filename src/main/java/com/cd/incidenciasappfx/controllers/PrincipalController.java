package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.App;
import com.cd.incidenciasappfx.models.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PrincipalController implements Initializable {

    private Usuario usuario;

    @FXML
    private StackPane contentArea;

    @FXML
    private VBox personasBox;
    @FXML
    private VBox datosBox;
    @FXML
    private Label lblGestionDatos;
    @FXML
    private Label lblGestionPersonas;
    @FXML
    private Label userActivo;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        loadView("InicioView");
    }

    @FXML
    public void loadView(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/cd/incidenciasappfx/views/" + fxmlFile + ".fxml"));
            Parent view = loader.load();

            contentArea.getChildren().clear(); // Limpia el contenido anterior
            contentArea.getChildren().add(view); // Agrega la nueva vista

        } catch (IOException e) {
            e.printStackTrace(); // Manejo de errores en caso de que no cargue el archivo
        }
    }

    @FXML
    private void toggleGestionPersonas() {
        boolean isVisible = personasBox.isVisible();
        personasBox.setVisible(!isVisible);

        if (!personasBox.isVisible()) {
            personasBox.setManaged(false);
        } else {

            personasBox.setManaged(true);
        }
    }

    // Método para alternar la visibilidad de la sección "Gestión de Datos"
    @FXML
    private void toggleGestionDatos() {
        datosBox.setManaged(true);
        datosBox.setVisible(!datosBox.isVisible());

    }

    @FXML
    private void handleInicio(MouseEvent event) {
        loadView("InicioView");
    }

    @FXML
    private void handleUsuarios(MouseEvent event) {
        loadView("UsuariosView");
    }

    @FXML
    private void handleRoles() {
        loadView("RolesView");
    }

    public void setUserAutenticado(Usuario user) {
        this.usuario = user;

        // Asegurar que la ejecución ocurra en el hilo principal de JavaFX
        Platform.runLater(() -> {
            if (usuario.getRol() != null) {
                if ("ROLE_ADMIN".equals(usuario.getRol().getNombre())) {
                    lblGestionDatos.setVisible(true);
                    lblGestionPersonas.setVisible(true);
                } else if ("ROLE_OPERADOR".equals(usuario.getRol().getNombre())) {
                    lblGestionDatos.setVisible(false);
                    lblGestionPersonas.setVisible(false);
                }
            }
        });
        userActivo.setText("Bienvenido, "+usuario.getRol().getNombre());
    }

}
