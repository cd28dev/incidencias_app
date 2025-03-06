package com.cd.incidenciasappfx.helper;

import com.cd.incidenciasappfx.controllers.ModalSmallController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

/**
 * AlertHelper.java
 *
 * @author CDAA
 */
public class AlertHelper {

    public static void mostrarAviso(String mensaje, String icono) {
        try {
            FXMLLoader loader = new FXMLLoader(AlertHelper.class.getResource("/com/cd/incidenciasappfx/views/modal_small.fxml"));
            Parent root = loader.load();

            ModalSmallController controller = loader.getController();
            controller.setMessage(mensaje, icono);

            Stage stage = new Stage();
            stage.setTitle("Aviso");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setResizable(false);
            stage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void mostrarExito(String mensaje) {
        mostrarAviso(mensaje, "/com/cd/incidenciasappfx/images/success.png");
    }

    public static void mostrarError(String mensaje) {
        mostrarAviso(mensaje, "/com/cd/incidenciasappfx/images/triangulo.png");
    }
}
