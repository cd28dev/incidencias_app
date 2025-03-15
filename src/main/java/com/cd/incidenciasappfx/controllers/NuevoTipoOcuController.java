package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.AlertHelper;
import com.cd.incidenciasappfx.helper.ModalControllerHelper;
import com.cd.incidenciasappfx.models.TipoOcurrencia;
import com.cd.incidenciasappfx.service.ITipoOcurrenciaService;
import com.cd.incidenciasappfx.service.TipoOcurrenciaServiceImpl;
import javafx.fxml.FXML;

public class NuevoTipoOcuController extends ModalControllerHelper<TipoOcurrencia> {
    private ITipoOcurrenciaService tipoOcurrenciaService;
    private TipoOcurrenciaViewController tipoOcuViewController;

    public NuevoTipoOcuController() {
    }

    @FXML
    public void initialize() {
        tipoOcurrenciaService = new TipoOcurrenciaServiceImpl();
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

    public void cargarCamposTipoOcurrencia(TipoOcurrencia tipoOcurrencia) {
        id.setText(String.valueOf(tipoOcurrencia.getIdOcurrencia()));
        name.setText(tipoOcurrencia.getNombre());
    }

    @FXML
    public void save() {
        if (numero == 0) {
            registrarTipoOcurrencia();
        } else if (numero == 1) {
            actualizarTipoOcurrencia();
        }

    }

    public void setTipoOcuViewController(TipoOcurrenciaViewController controller) {

        this.tipoOcuViewController = controller;
    }

    private void registrarTipoOcurrencia() {
        String nameTipo = name.getText().trim();

        if (nameTipo.isEmpty()) {
            AlertHelper.mostrarError("El nombre del tipo no puede estar vacío");
            return;
        }

        TipoOcurrencia ti = new TipoOcurrencia();
        ti.setNombre(nameTipo);

        registrarEntidad(
                ti,
                tipoOcurrenciaService::save,
                tipoOcuViewController::cargarTipoOcurrencia
        );
    }

    private void actualizarTipoOcurrencia() {
        String idText = id.getText();
        String nameTipoInt = name.getText().trim();

        if (nameTipoInt.isEmpty()) {
            AlertHelper.mostrarError("El nombre del Tipo no puede estar vacío");
            return;
        }

        TipoOcurrencia ti = new TipoOcurrencia();
        ti.setNombre(nameTipoInt);
        ti.setIdOcurrencia(Integer.parseInt(idText));

        actualizarEntidad(
                ti,
                tipoOcurrenciaService::update,
                tipoOcuViewController::cargarTipoOcurrencia
        );
    }

}
