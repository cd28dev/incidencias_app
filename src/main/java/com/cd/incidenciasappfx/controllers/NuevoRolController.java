package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.models.Rol;
import com.cd.incidenciasappfx.service.IRolesService;
import com.cd.incidenciasappfx.service.RolesServiceImpl;
import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * NuevoRolController.java
 *
 * @author CDAA
 */
public class NuevoRolController {

    private IRolesService rolService;
    private RolesViewController rolViewController;

    @FXML
    private Label lblNewRol;
    @FXML
    private TextField idRol;
    @FXML
    private TextField txtRol;

    @FXML
    private Button btnGuardar;

    private int numero;

    @FXML
    public void initialize() {
        rolService = new RolesServiceImpl();
        deshabilitarButtons();
        addListeners();
    }

    public void setNumero(int numero) {
        this.numero = numero;
        procesarNumero();
    }

    private void procesarNumero() {
        if (numero == 1) {
            changeTitle();
        }
    }

    private void deshabilitarButtons() {
        btnGuardar.setDisable(true);
    }

    private void addListeners() {
        txtRol.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
    }

    private void validarCampos() {
        boolean camposLlenos = !txtRol.getText().trim().isEmpty();
        btnGuardar.setDisable(!camposLlenos);
    }

    public void cargarDatosRol(Rol r) {
        idRol.setText(String.valueOf(r.getIdRol()));
        txtRol.setText(r.getNombre());
    }


    public void changeTitle() {
        lblNewRol.setText("Actualizar Rol");
        btnGuardar.setText("Update");
        btnGuardar.setStyle("-fx-background-color: #D4A017;");
    }

    @FXML
    public void saveRol() {
        if (numero == 0) {
            registrarRol();
        } else if (numero == 1) {
            updateRol();
        }

    }

    private void closeModal(Button btn) {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }

    public void setRolViewController(RolesViewController controller) {
        this.rolViewController = controller;
    }

    private void mostrarModal(String mensaje, String icono) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cd/incidenciasappfx/views/modal_small.fxml"));
            Parent root = loader.load();

            ModalSmallController controller = loader.getController();
            controller.setMessage(mensaje, icono);

            Stage stage = new Stage();
            stage.setTitle("Aviso");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updateRol() {
        String id = idRol.getText();
        String nameRol = txtRol.getText();

        if (nameRol.isEmpty()) {
            return;
        }
        Rol rol = new Rol();
        rol.setIdRol(Integer.valueOf(id));
        rol.setNombre(nameRol);

        if (rolService.update(rol).isPresent()) {
            mostrarModal("Rol actualizado", "/com/cd/incidenciasappfx/images/success.png");
            closeModal(btnGuardar);
            rolViewController.cargarRoles();
        } else {
            mostrarModal("Hubo algún problema", "/com/cd/incidenciasappfx/images/triangulo.png");
        }

    }

    public void registrarRol() {
        String nameRol = txtRol.getText();


        if (nameRol.isEmpty()) {
            return;
        }

        if (nameRol.isEmpty()) {
            return;
        }
        Rol rol = new Rol();
        rol.setNombre(nameRol);

        if (rolService.save(rol).isPresent()) {
            mostrarModal("Rol registrado", "/com/cd/incidenciasappfx/images/success.png");
            closeModal(btnGuardar);
            rolViewController.cargarRoles();
        } else {
            mostrarModal("Hubo algún problema", "/com/cd/incidenciasappfx/images/triangulo.png");

        }

    }

}
