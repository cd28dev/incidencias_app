package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.AlertHelper;
import com.cd.incidenciasappfx.helper.ModalControllerHelper;
import com.cd.incidenciasappfx.models.ServicioSerenazgo;
import com.cd.incidenciasappfx.service.IServiciosService;
import com.cd.incidenciasappfx.service.ServicioServiceImpl;
import javafx.fxml.FXML;

public class NuevoServicioController  extends ModalControllerHelper<ServicioSerenazgo> {

    private IServiciosService serviciosService;
    private ServiciosViewController serviciosViewController;

    public NuevoServicioController() {
    }

    @FXML
    public void initialize() {
        serviciosService = new ServicioServiceImpl();
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

    public void cargarCamposServicio(ServicioSerenazgo s) {
        id.setText(String.valueOf(s.getIdServicio()));
        name.setText(s.getNombre());
    }

    @FXML
    public void save() {
        if (numero == 0) {
            registrarServicio();
        } else if (numero == 1) {
            actualizarServicio();
        }

    }

    public void setServicioViewController(ServiciosViewController controller) {
        this.serviciosViewController = controller;
    }

    private void registrarServicio() {
        String nameServicio = name.getText().trim();

        if (nameServicio.isEmpty()) {
            AlertHelper.mostrarError("El nombre del Servicio no puede estar vacío");
            return;
        }

        ServicioSerenazgo s = new ServicioSerenazgo();
        s.setNombre(nameServicio);

        registrarEntidad(
                s,
                serviciosService::save,
                serviciosViewController::cargarServicios
        );
    }

    private void actualizarServicio() {
        String idText = id.getText();
        String nameServicio = name.getText().trim();

        if (nameServicio.isEmpty()) {
            AlertHelper.mostrarError("El nombre del Servicio no puede estar vacío");
            return;
        }

        ServicioSerenazgo s= new ServicioSerenazgo();
        s.setIdServicio(Integer.parseInt(idText));
        s.setNombre(nameServicio);

        actualizarEntidad(
                s,
                serviciosService::update,
                serviciosViewController::cargarServicios
        );
    }

}
