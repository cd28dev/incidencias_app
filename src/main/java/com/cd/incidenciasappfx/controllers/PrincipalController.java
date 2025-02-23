package com.cd.incidenciasappfx.controllers;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class PrincipalController implements Initializable {

//    @FXML
//    private FontAwesomeIconView iconDashboard;
//    @FXML
//    private FontAwesomeIconView iconIncidencias;
//    @FXML
//    private FontAwesomeIconView iconRoles;
//    @FXML
//    private FontAwesomeIconView iconDelitos;
//    @FXML
//    private FontAwesomeIconView iconTipoIntervencion;
//    @FXML
//    private FontAwesomeIconView iconTipoOcurrencia;
//    @FXML
//    private FontAwesomeIconView iconUnidadesApoyo;
//    @FXML
//    private FontAwesomeIconView iconServiciosSerenazgo;
//    @FXML
//    private FontAwesomeIconView iconEstadisticas;

    @FXML
    private VBox personasBox;
    @FXML
    private VBox datosBox;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        // Asignación manual de íconos para evitar problemas de carga en FXML
//        iconDashboard.setGlyphName("TACHOMETER");
//        iconIncidencias.setGlyphName("EXCLAMATION_CIRCLE");
//        iconRoles.setGlyphName("ID_BADGE");
//        iconDelitos.setGlyphName("BALANCE_SCALE");
//        iconTipoIntervencion.setGlyphName("EXCLAMATION_TRIANGLE");
//        iconTipoOcurrencia.setGlyphName("BOOK");
//        iconUnidadesApoyo.setGlyphName("TRUCK");
//        iconServiciosSerenazgo.setGlyphName("SHIELD");
//        iconEstadisticas.setGlyphName("FILE");
    }

    @FXML
    private void toggleGestionPersonas() {
        // Alternar la visibilidad
        boolean isVisible = personasBox.isVisible();
        personasBox.setVisible(!isVisible);

        // Si ahora está invisible, establecer 'managed' a false
        if (!personasBox.isVisible()) {
            personasBox.setManaged(false);
        } else {
            // Si está visible, establecer 'managed' a true
            personasBox.setManaged(true);
        }
    }

    // Método para alternar la visibilidad de la sección "Gestión de Datos"
    @FXML
    private void toggleGestionDatos() {
        datosBox.setManaged(true);
        datosBox.setVisible(!datosBox.isVisible());

    }

}
