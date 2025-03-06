package com.cd.incidenciasappfx.helper;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * ControllerHelper.java
 *
 * @author CDAA
 * @param <T>
 */
public abstract class ControllerHelper<T> {
    
    @FXML
    protected TableView<T> tabla;

    protected <C> void abrirModal(String fxmlPath, Consumer<C> configurador, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            C modalController = loader.getController();
            configurador.accept(modalController);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.WINDOW_MODAL);
            if (tabla.getScene().getWindow() != null) {
                modalStage.initOwner(tabla.getScene().getWindow());
            }
            modalStage.setScene(new Scene(root));
            modalStage.setTitle(titulo);
            modalStage.setResizable(false);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected void cargarTabla(Supplier<List<T>> fetchFunction) {
        Task<List<T>> task = new Task<>() {
            @Override
            protected List<T> call() throws Exception {
                return fetchFunction.get(); // Obtiene los datos en segundo plano
            }
        };

        task.setOnSucceeded(event -> {
            ObservableList<T> dataList = FXCollections.observableArrayList(task.getValue());
            tabla.setItems(dataList); // Se actualiza la UI en el hilo principal
        });

        task.setOnFailed(event -> {
            task.getException().printStackTrace();
            AlertHelper.mostrarAviso("Error al cargar datos", "/com/cd/incidenciasappfx/images/triangulo.png");
        });

        new Thread(task).start();
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    protected void eliminarRegistro(T t, Function<T, Boolean> deleteFunction, Runnable onSuccess) {
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return deleteFunction.apply(t);
            }

            @Override
            protected void succeeded() {
                if (getValue()) {
                    AlertHelper.mostrarAviso("Registro eliminado", "/com/cd/incidenciasappfx/images/success.png");
                    onSuccess.run();
                } else {
                    AlertHelper.mostrarAviso("Error al eliminar registro", "/com/cd/incidenciasappfx/images/triangulo.png");
                }
            }

            @Override
            protected void failed() {
                AlertHelper.mostrarAviso("Error inesperado al eliminar registro", "/com/cd/incidenciasappfx/images/triangulo.png");
            }
        };

        new Thread(task).start();
    }

    protected void exportarReporte(String reportPath, String fileName, Supplier<List<?>> fetchFunction, ReportExporter exporter) {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Path documentsDir = Paths.get(System.getProperty("user.home"), "Documents");
                String outputPath = documentsDir.resolve(fileName).toString();

                List<?> dataList = fetchFunction.get();
                Map<String, Object> params = new HashMap<>();
                params.put("ReportTitle", "Reporte");

                JasperReportHelper.generateReport(reportPath, outputPath, params, dataList, exporter);
                return null;
            }

            @Override
            protected void succeeded() {
                AlertHelper.mostrarAviso("Reporte generado con éxito", "/com/cd/incidenciasappfx/images/success.png");
            }

            @Override
            protected void failed() {
                AlertHelper.mostrarAviso("Error al generar el reporte", "/com/cd/incidenciasappfx/images/triangulo.png");
            }
        };

        new Thread(task).start();
    }

    protected void configurarColumnaAccion(
            TableColumn<T, Void> colAccion,
            Consumer<T> onActualizar,
            Consumer<T> onEliminar) {

        colAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btnActualizar = crearBoton("update.png", "-fx-background-color: transparent;",
                    "-fx-background-color: rgba(0, 0, 0, 0.1); -fx-border-radius: 5px; -fx-background-radius: 5px;",
                    onActualizar, this);

            private final Button btnEliminar = crearBoton("delete.png", "-fx-background-color: transparent;",
                    "-fx-background-color: rgba(255, 0, 0, 0.2); -fx-border-radius: 5px; -fx-background-radius: 5px;",
                    onEliminar, this);

            private final HBox contenedorBotones = new HBox(10, btnActualizar, btnEliminar);

            {
                configurarContenedorBotones(contenedorBotones);
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(contenedorBotones);
                }
            }
        });
    }

    private Button crearBoton(String iconPath, String estiloDefault, String estiloHover, Consumer<T> accion, TableCell<T, Void> cell) {
        Button btn = new Button();
        ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/com/cd/incidenciasappfx/images/" + iconPath)));
        icon.setFitWidth(20);
        icon.setFitHeight(20);
        btn.setGraphic(icon);
        btn.setStyle(estiloDefault);

        btn.setOnAction(event -> {
            T item = cell.getTableView().getItems().get(cell.getIndex());
            accion.accept(item);
        });

        btn.setOnMouseEntered(event -> btn.setStyle(estiloHover));
        btn.setOnMouseExited(event -> btn.setStyle(estiloDefault));

        return btn;
    }

    private void configurarContenedorBotones(HBox contenedor) {
        contenedor.setAlignment(Pos.CENTER);
    }
}
