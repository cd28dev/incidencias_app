package com.cd.incidenciasappfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

/**
 * Aplicación principal para el sistema de registro de incidencias.
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("login"));
        scene.getStylesheets().add(getClass().getResource("/com/cd/incidenciasappfx/styles/login.css").toExternalForm());
        stage.setScene(scene); // Asignamos la escena al stage
        stage.setTitle("Sistema de Registro de Incidencias");
        stage.setResizable(false); 
        stage.show();
    }

    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    /**
     * Carga un archivo FXML de la carpeta `views`.
     * @param fxml Nombre del archivo FXML (sin extensión).
     * @return Nodo raíz de la interfaz cargada.
     * @throws IOException Si el archivo no se encuentra.
     */
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/com/cd/incidenciasappfx/views/" + fxml + ".fxml"));
        return fxmlLoader.load();
    }

    public static void main(String[] args) {
        launch();
    }
}
