package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.App;
import com.cd.incidenciasappfx.models.Usuario;
import com.cd.incidenciasappfx.service.IUsuarioService;
import com.cd.incidenciasappfx.service.UsuarioServiceImpl;
import java.io.IOException;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
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

    Optional<Usuario> userOptional = userService.validarUsuario(username, password);

    if (userOptional.isPresent()) {
        Usuario user = userOptional.get();

        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("/com/cd/incidenciasappfx/views/Principal.fxml"));
            Parent principalRoot = loader.load();

            // Obtener el controlador y pasar el usuario autenticado
            PrincipalController principalController = loader.getController();
            principalController.setUserAutenticado(user);

            // Crear la nueva ventana
            Stage stage = new Stage();
            Scene scene = new Scene(principalRoot, 1200, 700); // Establece el tamaño
            stage.setScene(scene);
            stage.setTitle("Sistema de Registro de Incidencias");

            // Centramos la ventana en la pantalla
            stage.centerOnScreen();

            // Cerramos la ventana de login
            Stage stageLogin = (Stage) btnLogin.getScene().getWindow();
            stageLogin.close();

            stage.show(); // Mostramos la ventana principal

        } catch (IOException e) {
            e.printStackTrace();
        }
    } else {
        System.out.println("❌ Credenciales incorrectas");
    }
}

}
