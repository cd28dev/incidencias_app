package com.cd.incidenciasappfx.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;


/**
 * ModalSmallController.java
 *
 * @author CDAA
 */
public class ModalSmallController {
    @FXML
    private Label lblMessage;

    @FXML
    private ImageView iconMessage;

    @FXML
    private Button btnAceptar;

    @FXML
    public void initialize() {
        btnAceptar.setOnAction(event -> cerrarModal());
    }

    public void setMessage(String message, String iconPath) {
        lblMessage.setText(message);
        if (iconPath != null) {
            iconMessage.setImage(new Image(getClass().getResourceAsStream(iconPath)));
        }
    }

    private void cerrarModal() {
        Stage stage = (Stage) btnAceptar.getScene().getWindow();
        stage.close();
    }
}
