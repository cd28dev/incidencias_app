package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.AlertHelper;
import com.cd.incidenciasappfx.helper.ModalControllerHelper;

import com.cd.incidenciasappfx.models.Sector;
import com.cd.incidenciasappfx.models.Urbanizacion;
import com.cd.incidenciasappfx.service.ISectorService;
import com.cd.incidenciasappfx.service.IUrbService;
import com.cd.incidenciasappfx.service.SectorServiceImpl;
import com.cd.incidenciasappfx.service.UrbServiceImpl;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

/**
 * NuevoUrbController.java
 *
 * @author CDAA
 */
public class NuevoUrbController extends ModalControllerHelper<Urbanizacion> {

    private IUrbService urbService;
    private ISectorService sectorService;
    private UrbViewController urbViewController;

    @FXML
    private ComboBox<Sector> cbSector;

    public NuevoUrbController() {
    }

    @FXML
    public void initialize() {
        urbService = new UrbServiceImpl();
        sectorService = new SectorServiceImpl();
        deshabilitarButtons();
        addListeners();
        cargarSector();
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

    public void cargarCamposUrb(Urbanizacion u) {
        id.setText(String.valueOf(u.getId()));
        name.setText(u.getNombre());
    }

    @FXML
    public void save() {
        if (numero == 0) {
            registrarUrb();
        } else if (numero == 1) {
            actualizarUrb();
        }

    }

    public void setUrbViewController(UrbViewController controller) {
        this.urbViewController = controller;
    }

    private void registrarUrb() {
        String nameUrb = name.getText().trim();
        Sector sectorSeleccionado = cbSector.getValue();

        if (nameUrb.isEmpty()) {
            AlertHelper.mostrarError("El nombre de la Urb no puede estar vacío");
            return;
        }
        Urbanizacion u = new Urbanizacion();
        u.setNombre(nameUrb);
        u.setSector(sectorSeleccionado);

        registrarEntidad(
                u,
                urbService::save,
                urbViewController::cargarUrb
        );
    }

    private void actualizarUrb() {
        String idText = id.getText();
        String nameUrb = name.getText().trim();
        Sector sectorSeleccionado = cbSector.getValue();

        if (nameUrb.isEmpty()) {
            AlertHelper.mostrarError("El nombre de la Urb no puede estar vacío");
            return;
        }

        Urbanizacion u = new Urbanizacion();

        u.setId(Integer.parseInt(idText));
        u.setNombre(nameUrb);
        u.setSector(sectorSeleccionado);

        actualizarEntidad(
                u,
                urbService::update,
                urbViewController::cargarUrb
        );
    }

    private void cargarSector() {
        cargarDatos(cbSector, sectorService::findAll, Sector::getNombre);

    }
}
