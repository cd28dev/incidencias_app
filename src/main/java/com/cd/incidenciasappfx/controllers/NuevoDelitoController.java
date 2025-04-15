package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.AlertHelper;
import com.cd.incidenciasappfx.helper.ModalControllerHelper;
import com.cd.incidenciasappfx.models.Delito;
import com.cd.incidenciasappfx.service.DelitoServiceImpl;
import com.cd.incidenciasappfx.service.IDelitoService;
import javafx.fxml.FXML;


public class NuevoDelitoController extends ModalControllerHelper<Delito> {
    private IDelitoService delitoService;
    private DelitosViewController delitoViewController;

    public NuevoDelitoController() {
    }

    @FXML
    public void initialize() {
        delitoService = new DelitoServiceImpl();
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

    public void cargarCamposDelito(Delito delito) {
        id.setText(String.valueOf(delito.getIdDelito()));
        name.setText(delito.getNombre());
    }

    public void setDelitoViewController(DelitosViewController controller) {
        this.delitoViewController = controller;
    }

    @FXML
    private void save() {
        if (numero == 0) {
            registrarDelito();
        } else if (numero == 1) {
            actualizarDelito();
        }
    }

    private void registrarDelito() {
        String nameDelito = name.getText().trim();

        if (nameDelito.isEmpty()) {
            AlertHelper.mostrarError("El nombre del delito no puede estar vacío");
            return;
        }

        Delito delito = new Delito();
        delito.setNombre(nameDelito);

        registrarEntidad(
                delito,
                delitoService::save,
                delitoViewController::cargarDelitos
        );
    }

    private void actualizarDelito() {
        String idText = id.getText();
        String nameDelito = name.getText().trim();

        if (nameDelito.isEmpty()) {
            AlertHelper.mostrarError("El nombre del delito no puede estar vacío");
            return;
        }

        Delito delito = new Delito();
        delito.setIdDelito(Integer.parseInt(idText));
        delito.setNombre(nameDelito);

        actualizarEntidad(
                delito,
                delitoService::update,
                delitoViewController::cargarDelitos
        );
    }
}
