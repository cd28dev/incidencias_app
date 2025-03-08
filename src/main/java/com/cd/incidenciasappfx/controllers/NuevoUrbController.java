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
import javafx.scene.control.TextField;

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
    private TextField idUrb;
    @FXML
    private TextField txtUrb;
    @FXML
    private ComboBox<String> cbUrb;

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
        txtUrb.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
    }

    private void validarCampos() {
        boolean camposLlenos = !txtUrb.getText().trim().isEmpty();
        btnGuardar.setDisable(!camposLlenos);
    }

    public void cargarCamposUrb(Urbanizacion u) {
        idUrb.setText(String.valueOf(u.getId()));
        txtUrb.setText(u.getNombre());
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
        String nameUrb = txtUrb.getText().trim();
        String sectorSeleccionado = cbUrb.getValue();

        if (nameUrb.isEmpty()) {
            AlertHelper.mostrarError("El nombre de la Urb no puede estar vacío");
            return;
        }
        Sector s = new Sector();
        Urbanizacion u = new Urbanizacion();
        u.setNombre(nameUrb);
        s.setNombre(sectorSeleccionado);
        u.setSector(s);

        registrarEntidad(
                u,
                urbService::save,
                urbViewController::cargarUrb
        );
    }

    private void actualizarUrb() {
        String idText = idUrb.getText();
        String nameUrb = txtUrb.getText().trim();
        String sectorSeleccionado = cbUrb.getValue();

        if (nameUrb.isEmpty()) {
            AlertHelper.mostrarError("El nombre de la Urb no puede estar vacío");
            return;
        }

        Urbanizacion u = new Urbanizacion();
        Sector s = new Sector();
        s.setNombre(sectorSeleccionado);
        
        u.setId(Integer.parseInt(idText));
        u.setNombre(nameUrb);
        u.setSector(s);
        
        actualizarEntidad(
                u,
                urbService::update,
                urbViewController::cargarUrb
        );
    }
    
    private void cargarSector() {
        Task<List<Sector>> task = new Task<>() {
            @Override
            protected List<Sector> call() throws Exception {
                return sectorService.findAll();
            }
        };

        task.setOnSucceeded(event -> {
            List<Sector> sectores = task.getValue();
            if (sectores != null) {
                ObservableList<String> listSectores = FXCollections.observableArrayList(
                        sectores.stream().map(Sector::getNombre).toList()
                );
                cbUrb.setItems(listSectores);
            }
        });

        new Thread(task).start();
    }
}
