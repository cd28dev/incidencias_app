package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.AlertHelper;
import com.cd.incidenciasappfx.helper.ModalControllerHelper;
import com.cd.incidenciasappfx.models.Rol;
import com.cd.incidenciasappfx.service.IRolesService;
import com.cd.incidenciasappfx.service.RolesServiceImpl;
import javafx.fxml.FXML;

/**
 * NuevoRolController.java
 *
 * @author CDAA
 */
public class NuevoRolController extends ModalControllerHelper<Rol> {

    private IRolesService rolService;
    private RolesViewController rolViewController;

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
        name.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
    }

    private void validarCampos() {
        boolean camposLlenos = !name.getText().trim().isEmpty();
        btnGuardar.setDisable(!camposLlenos);
    }

    public void cargarCamposRol(Rol r) {
        id.setText(String.valueOf(r.getIdRol()));
        name.setText(r.getNombre());
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
        String nameRol = name.getText().trim();

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
        String idText = id.getText();
        String nameRol = name.getText().trim();

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
