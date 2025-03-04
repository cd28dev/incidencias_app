package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.App;
import com.cd.incidenciasappfx.models.Usuario;
import com.cd.incidenciasappfx.service.IUsuarioService;
import com.cd.incidenciasappfx.service.UsuarioServiceImpl;
import java.io.IOException;
import java.util.Optional;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Screen;
import javafx.stage.Stage;

/**
 *
 * @author CDAA
 */
public class LoginController {

    @FXML
    private TextField txtUsername;

    @FXML
    private PasswordField txtPassword;

    @FXML
    private Button btnLogin;

    private IUsuarioService userService;

    public LoginController() {
        this.userService = new UsuarioServiceImpl();
    }

    @FXML
    void onLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            System.out.println("❌ Debes ingresar usuario y contraseña.");
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
                abrirVentanaPrincipal(user);
            } else {
                System.out.println("❌ Credenciales incorrectas");
            }
        });

        task.setOnFailed(evento -> {
            System.out.println("⚠ Error al validar usuario");
            task.getException().printStackTrace();
        });

        new Thread(task).start();
    }

    private void abrirVentanaPrincipal(Usuario user) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/cd/incidenciasappfx/views/Principal.fxml"));
            Parent principalRoot = loader.load();

            PrincipalController principalController = loader.getController();
            principalController.setUserAutenticado(user);

            Stage stage = new Stage();
            Scene scene = new Scene(principalRoot);
            stage.setScene(scene);
            stage.setTitle("Sistema de Registro de Incidencias");

            Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
            stage.setX(screenBounds.getMinX());
            stage.setY(screenBounds.getMinY());
            stage.setWidth(screenBounds.getWidth());
            stage.setHeight(screenBounds.getHeight());
            stage.setMaximized(true);
            stage.setResizable(false);
            stage.centerOnScreen();

            // Cerrar la ventana de login
            Stage stageLogin = (Stage) btnLogin.getScene().getWindow();
            stageLogin.close();

            stage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
