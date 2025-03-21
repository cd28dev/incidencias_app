package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.AlertHelper;
import com.cd.incidenciasappfx.helper.ModalControllerHelper;
import com.cd.incidenciasappfx.models.Sector;
import com.cd.incidenciasappfx.service.ISectorService;
import com.cd.incidenciasappfx.service.SectorServiceImpl;
import javafx.fxml.FXML;

/**
 * NuevoSectorController.java
 *
 * @author CDAA
 */
public class NuevoSectorController extends ModalControllerHelper<Sector> {

    private ISectorService sectorService;
    private SectorViewController sectorViewController;

    public NuevoSectorController() {
    }

    @FXML
    public void initialize() {
        sectorService = new SectorServiceImpl();
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

    public void cargarCamposSector(Sector s) {
        id.setText(String.valueOf(s.getId()));
        name.setText(s.getNombre());
    }

    @FXML
    public void save() {
        if (numero == 0) {
            registrarSector();
        } else if (numero == 1) {
            actualizarSector();
        }

    }

    public void setSectorViewController(SectorViewController controller) {
        this.sectorViewController = controller;
    }

    private void registrarSector() {
        String nameSector = name.getText().trim();

        if (nameSector.isEmpty()) {
            AlertHelper.mostrarError("El nombre del Sector no puede estar vacío");
            return;
        }

        Sector s = new Sector();
        s.setNombre(nameSector);

        registrarEntidad(
                s,
                sectorService::save,
                sectorViewController::cargarSectores
        );
    }

    private void actualizarSector() {
        String idText = id.getText();
        String nameSector = name.getText().trim();

        if (nameSector.isEmpty()) {
            AlertHelper.mostrarError("El nombre del Sector no puede estar vacío");
            return;
        }

        Sector s= new Sector();
        s.setId(Integer.parseInt(idText));
        s.setNombre(nameSector);

        actualizarEntidad(
                s,
                sectorService::update,
                sectorViewController::cargarSectores
        );
    }

}
