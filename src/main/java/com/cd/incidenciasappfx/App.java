package com.cd.incidenciasappfx;

import com.cd.incidenciasappfx.repository.JpaUtil;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.Objects;

/**
 * Aplicación principal para el sistema de registro de incidencias.
 */
public class App extends Application {

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(loadFXML());
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/cd/incidenciasappfx/styles/style.css")).toExternalForm());
        stage.setScene(scene); // Asignamos la escena al stage
        stage.setTitle("Sistema de Registro de Incidencias");
        Rectangle2D screenBounds = Screen.getPrimary().getVisualBounds();
        stage.setX(screenBounds.getMinX());
        stage.setY(screenBounds.getMinY());
        stage.setWidth(screenBounds.getWidth());
        stage.setHeight(screenBounds.getHeight());
        stage.setMaximized(true);
        stage.setResizable(false);
        stage.centerOnScreen();
        stage.show();

    }

    /**
     * Carga un archivo FXML de la carpeta `views`.
     *
     * @return Nodo raíz de la interfaz cargada.
     * @throws IOException Si el archivo no se encuentra.
     */
    private static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/cd/incidenciasappfx/views/" + "Principal" + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        JpaUtil.shutdown(); // Cierra todas las conexiones de JPA
        System.exit(0);
    }
}
