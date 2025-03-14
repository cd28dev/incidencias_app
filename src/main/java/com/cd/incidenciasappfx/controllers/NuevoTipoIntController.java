package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.AlertHelper;
import com.cd.incidenciasappfx.helper.ModalControllerHelper;
import com.cd.incidenciasappfx.models.TipoIntervencion;
import com.cd.incidenciasappfx.service.ITipoIntService;
import com.cd.incidenciasappfx.service.TipoIntServiceImpl;
import javafx.fxml.FXML;

public class NuevoTipoIntController extends ModalControllerHelper<TipoIntervencion> {

    private ITipoIntService tipoIntService;
    private TipoIntViewController tipoIntViewController;

    public NuevoTipoIntController() {
    }

    @FXML
    public void initialize() {
        tipoIntService = new TipoIntServiceImpl();
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

    public void cargarCamposTipoInt(TipoIntervencion tipoIntervencion) {
        id.setText(String.valueOf(tipoIntervencion.getIdIntervencion()));
        name.setText(tipoIntervencion.getNombre());
    }

    @FXML
    public void save() {
        if (numero == 0) {
            registrarTipoIntervencion();
        } else if (numero == 1) {
            actualizarTipoIntervencion();
        }

    }

    public void setTipoIntViewController(TipoIntViewController controller) {

        this.tipoIntViewController = controller;
    }

    private void registrarTipoIntervencion() {
        String nameTipoInt = name.getText().trim();

        if (nameTipoInt.isEmpty()) {
            AlertHelper.mostrarError("El nombre del tipo no puede estar vacío");
            return;
        }

        TipoIntervencion ti = new TipoIntervencion();
        ti.setNombre(nameTipoInt);

        registrarEntidad(
                ti,
                tipoIntService::save,
                tipoIntViewController::cargarTipoIntervencion
        );
    }

    private void actualizarTipoIntervencion() {
        String idText = id.getText();
        String nameTipoInt = name.getText().trim();

        if (nameTipoInt.isEmpty()) {
            AlertHelper.mostrarError("El nombre del Tipo no puede estar vacío");
            return;
        }

        TipoIntervencion ti = new TipoIntervencion();
        ti.setNombre(nameTipoInt);
        ti.setIdIntervencion(Integer.parseInt(idText));

        actualizarEntidad(
                ti,
                tipoIntService::update,
                tipoIntViewController::cargarTipoIntervencion
        );
    }

}
