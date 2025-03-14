package com.cd.incidenciasappfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
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
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/com/cd/incidenciasappfx/styles/login.css")).toExternalForm());
        stage.setScene(scene); // Asignamos la escena al stage
        stage.setTitle("Sistema de Registro de Incidencias");
        stage.setResizable(false);
        stage.show();

    }

    /**
     * Carga un archivo FXML de la carpeta `views`.
     *
     * @return Nodo raíz de la interfaz cargada.
     * @throws IOException Si el archivo no se encuentra.
     */
    private static Parent loadFXML() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/cd/incidenciasappfx/views/" + "login" + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
