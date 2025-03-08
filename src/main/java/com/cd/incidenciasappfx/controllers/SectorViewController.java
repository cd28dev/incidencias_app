package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ControllerHelper;
import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import com.cd.incidenciasappfx.models.Sector;
import com.cd.incidenciasappfx.service.ISectorService;
import com.cd.incidenciasappfx.service.SectorServiceImpl;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * SectorViewController.java
 *
 * @author CDAA
 */
public class SectorViewController extends ControllerHelper<Sector> implements Initializable {

    private ISectorService sectorService;
    @FXML
    protected TableView<Sector> tabla;

    @FXML
    private TableColumn<Sector, Integer> colIdSector;
    @FXML
    private TableColumn<Sector, String> colSector;
    @FXML
    private TableColumn<Sector, Void> colAccion;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sectorService = new SectorServiceImpl();
        configColumns();
        cargarSectores();
    }

    @FXML
    private void abrirModal0() {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoSector.fxml",
                (NuevoSectorController modalController) -> {
                    modalController.setNumero(0);
                    modalController.setSectorViewController(this);
                },
                "Nuevo Sector",
                tabla.getScene().getWindow());
    }

    private void configColumns() {
        tabla.getColumns().clear();
        tabla.getColumns().addAll(colIdSector, colSector, colAccion);

        colIdSector.setCellValueFactory(data
                -> new ReadOnlyObjectWrapper<>(data.getValue().getId())
        );
        colSector.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        configurarColumnaAccion(
                colAccion,
                sector -> abrirModalActualizar(sector, 1),
                sector -> eliminarSector(sector));
    }

    private void abrirModalActualizar(Sector s, int number) {
        abrirModal("/com/cd/incidenciasappfx/views/NuevoSector.fxml",
                (NuevoSectorController controller) -> {
                    controller.setNumero(number);
                    controller.cargarCamposSector(s);
                    controller.setSectorViewController(this);
                },
                "Actualizar Sector",
                tabla.getScene().getWindow());
    }

    public void cargarSectores(){
        cargarTabla(tabla,sectorService::findAll);
    }
    
    private void eliminarSector(Sector sector) {
        eliminarRegistro(sector, s -> sectorService.delete(s), () -> cargarTabla(tabla,sectorService::findAll),tabla);

    }

    @FXML
    private void exportarExcel() {
        exportarReporte("/com/cd/incidenciasappfx/report/SectorReportExcel.jrxml", "SectorReport.xlsx",
                sectorService::findAll, new ExcelReportExporter());
    }

    @FXML
    private void exportarPDF() {
        exportarReporte("/com/cd/incidenciasappfx/report/SectorReportPdf.jrxml", "SectorReport.pdf",
                sectorService::findAll, new PdfReportExporter());
    }

}
