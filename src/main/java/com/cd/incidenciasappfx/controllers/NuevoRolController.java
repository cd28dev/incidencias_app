package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.AlertHelper;
import com.cd.incidenciasappfx.helper.ModalControllerHelper;
import com.cd.incidenciasappfx.models.Rol;
import com.cd.incidenciasappfx.service.IRolesService;
import com.cd.incidenciasappfx.service.RolesServiceImpl;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

/**
 * NuevoRolController.java
 *
 * @author CDAA
 */
public class NuevoRolController extends ModalControllerHelper<Rol> {

    private IRolesService rolService;
    private RolesViewController rolViewController;
    @FXML
    private TextField idRol;
    @FXML
    private TextField txtRol;


    public NuevoRolController() {

    }

    @FXML
    public void initialize() {
        rolService = new RolesServiceImpl();
        deshabilitarButtons();
        addListeners();
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

    public void cargarCamposRol(Rol r) {
        idRol.setText(String.valueOf(r.getIdRol()));
        txtRol.setText(r.getNombre());
    }

    public void setRolViewController(RolesViewController controller) {
        this.rolViewController = controller;
    }

    @FXML
    private void save() {
        if (numero == 0) {
            registrarRol();
        } else if (numero == 1) {
            actualizarRol();
        }
    }

    private void registrarRol() {
        String nameRol = txtRol.getText().trim();

        if (nameRol.isEmpty()) {
            AlertHelper.mostrarError("El nombre del rol no puede estar vacío");
            return;
        }

        Rol rol = new Rol();
        rol.setNombre(nameRol);

        registrarEntidad(
                rol,
                rolService::save,
                rolViewController::cargarRoles
        );
    }

    private void actualizarRol() {
        String idText = idRol.getText();
        String nameRol = txtRol.getText().trim();

        if (nameRol.isEmpty()) {
            AlertHelper.mostrarError("El nombre del rol no puede estar vacío");
            return;
        }

        Rol rol = new Rol();
        rol.setIdRol(Integer.parseInt(idText));
        rol.setNombre(nameRol);

        actualizarEntidad(
                rol,
                rolService::update,
                rolViewController::cargarRoles
       );
    }

}
