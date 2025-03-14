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
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class PrincipalController implements Initializable {

    @FXML
    private HBox navbar;
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
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("InicioView");
    }

    @FXML
    public void loadView(String fxmlFile) {
        Platform.runLater(System::gc); // Forzar recolección de basura
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

        personasBox.setManaged(personasBox.isVisible());
    }

    // Método para alternar la visibilidad de la sección "Gestión de Datos"
    @FXML
    private void toggleGestionDatos() {
        datosBox.setManaged(true);
        datosBox.setVisible(!datosBox.isVisible());

    }

    @FXML
    private void handleInicio() {
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("InicioView");
    }

    @FXML
    private void handleUsuarios() {
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("UsuariosView");
    }

    @FXML
    private void handleRoles() {
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("RolesView");
    }
    
    @FXML
    private void handleSectores(){
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("SectorView");
    }
    
    @FXML   
    private void handleUrb(){
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("UrbanizacionView");
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

    public void handleDelitos() {
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("DelitosView");
    }

    public void handleTipoIntervencion() {
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("TipoIntervencionView");
    }
}
