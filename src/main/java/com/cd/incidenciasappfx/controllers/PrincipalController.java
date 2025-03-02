package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.App;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PrincipalController implements Initializable {

    @FXML
    private StackPane contentArea;

    @FXML
    private VBox personasBox;
    @FXML
    private VBox datosBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        // Alternar la visibilidad
        boolean isVisible = personasBox.isVisible();
        personasBox.setVisible(!isVisible);

        // Si ahora está invisible, establecer 'managed' a false
        if (!personasBox.isVisible()) {
            personasBox.setManaged(false);
        } else {
            // Si está visible, establecer 'managed' a true
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
        loadView("InicioView"); // Archivo FXML de la vista de inicio
    }

    @FXML
    private void handleUsuarios(MouseEvent event) {
        loadView("UsuariosView"); // Archivo FXML de la vista de usuarios
    }

    @FXML
    private void handleRoles() {
        loadView("RolesView"); // Archivo FXML de la vista de roles
    }

}
