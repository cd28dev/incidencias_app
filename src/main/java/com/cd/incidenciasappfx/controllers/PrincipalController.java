package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.App;
import com.cd.incidenciasappfx.models.Usuario;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import com.cd.incidenciasappfx.service.IUsuarioService;
import com.cd.incidenciasappfx.service.UsuarioServiceImpl;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class PrincipalController implements Initializable {

    @FXML
    public Label lblError;
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

    // Campos para el login
    @FXML
    public AnchorPane loginPane;

    @FXML
    private TextField txtUsername;
    @FXML
    private PasswordField txtPassword;
    @FXML
    private Button btnLogin;

    private final IUsuarioService userService;

    public PrincipalController() {
        userService = new UsuarioServiceImpl();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        Platform.runLater(System::gc); // Forzar recolección de basura
        mostrarLogin();
    }
    private void mostrarLogin() {
        loginPane.setVisible(true);
        loginPane.setManaged(true);
        navbar.setVisible(false);
        navbar.setManaged(false);
        contentArea.setVisible(false);
        contentArea.setManaged(false);
    }
    private void mostrarSistema() {
        loginPane.setVisible(false);
        loginPane.setManaged(false);
        navbar.setVisible(true);
        navbar.setManaged(true);
        contentArea.setVisible(true);
        contentArea.setManaged(true);

        loadView("InicioView");
    }

    @FXML
    void onLogin() {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();
        if (username.isEmpty() || password.isEmpty()) {
            lblError.setText("Debes ingresar usuario y contraseña");
            lblError.setVisible(true);
            return;
        }

        Task<Optional<Usuario>> task = new Task<>() {
            @Override
            protected Optional<Usuario> call() {
                return userService.validarUsuario(username, password);
            }
        };

        task.setOnSucceeded(evento -> {
            Optional<Usuario> userOptional = task.getValue();
            if (userOptional.isPresent()) {
                Usuario user = userOptional.get();
                setUserAutenticado(user);
                mostrarSistema();
            } else {
                lblError.setText("Credenciales incorrectas");
            }
        });

        task.setOnFailed(evento -> {
            System.out.println("⚠ Error al validar usuario");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
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
    @FXML
    public void handleDelitos() {
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("DelitosView");
    }

    @FXML
    public void handleTipoIntervencion() {
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("TipoIntervencionView");
    }
    @FXML
    public void handleTipoOcurrencia() {
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("TipoOcurrenciaView");
    }
    @FXML
    public void handleServicios() {
        Platform.runLater(System::gc); // Forzar recolección de basura
        loadView("ServiciosView");
    }

    @FXML
    public void onForgotPassword(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cd/incidenciasappfx/views/ResetPassword.fxml"));
            Parent root = loader.load();

            // Obtener el controlador de la vista
            ResetController resetController = loader.getController();

            // Crear el Stage (Ventana Modal)
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Bloquea la ventana principal hasta que esta se cierre
            stage.initStyle(StageStyle.UNDECORATED); // Opcional: Quita la barra de título
            stage.setTitle("Restablecer Contraseña");
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar la vista de restablecimiento de contraseña.");
        }
    }

}
