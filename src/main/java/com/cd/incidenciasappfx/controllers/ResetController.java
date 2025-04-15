package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.models.Usuario;
import com.cd.incidenciasappfx.service.IUsuarioService;
import com.cd.incidenciasappfx.service.UsuarioServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.util.Optional;

public class ResetController {
    @FXML
    private Button btnGuardar, btnCancelar;
    @FXML
    private Label lblError;

    @FXML
    private TextField email;

    private IUsuarioService usuarioService;

    public ResetController() {
    }

    @FXML
    public void initialize() {
        validarCampos();
        usuarioService = new UsuarioServiceImpl();
        email.textProperty().addListener((observable, oldValue, newValue) -> {
            validarCampos();
        });
    }

    @FXML
    public void save() {
        String emailIngresado = email.getText();
        if (!esEmailValido(emailIngresado)) {
            mostrarMensaje("Correo inválido. Ingrese un email válido.");
            return;
        }

        Optional<Usuario> usuario = usuarioService.findByEmail(emailIngresado);
        if (usuario.isPresent()) {
            mostrarMensaje("Revise su bandeja de entrada.");
        } else {
            mostrarMensaje("Correo no registrado.");
        }
    }

    private void validarCampos() {
        boolean camposLlenos = !email.getText().trim().isEmpty();
        btnGuardar.setDisable(!camposLlenos);
    }

    private void mostrarMensaje(String mensaje) {
        lblError.setText(mensaje);
        lblError.setVisible(true);
    }

    private boolean esEmailValido(String email) {
        return email.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$");
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }
}
