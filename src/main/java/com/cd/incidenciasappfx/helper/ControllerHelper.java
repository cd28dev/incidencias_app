package com.cd.incidenciasappfx.helper;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

/**
 * ControllerHelper.java
 *
 * @author CDAA
 * @param <T>
 */
public abstract class ControllerHelper<T> {
    @FXML
    protected Button btnBuscar;
    @FXML
    protected TextField txtBuscar;
    @FXML
    protected Button btnNuevo;
    @FXML
    protected Button btnExportPdf;
    @FXML
    protected Button btnExportExcel;
    @FXML
    protected TableColumn<T,Integer> id;
    @FXML
    protected TableColumn<T, Void> colAccion;

    private static final ExecutorService executor = Executors.newCachedThreadPool();

    protected <C> void abrirModal(String fxmlPath, Consumer<C> configurador, String titulo, Window owner) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();

            C modalController = loader.getController();
            configurador.accept(modalController);

            Stage modalStage = new Stage();
            modalStage.initStyle(StageStyle.UNDECORATED);
            modalStage.initModality(Modality.WINDOW_MODAL);
            if (owner != null) {
                modalStage.initOwner(owner);
            }
            modalStage.setScene(new Scene(root));
            modalStage.setTitle(titulo);
            modalStage.setResizable(false);
            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            Platform.runLater(System::gc); // Forzar recolección de basura
        }
    }

    public void cargarTabla(TableView<T> tabla, Supplier<List<T>> fetchFunction) {
        Task<List<T>> task = getTask(tabla, fetchFunction);

        executor.execute(task); // Forzar recolección de basura
        tabla.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tabla.setSelectionModel(null);

        colAccion.setMinWidth(100);
        colAccion.setMaxWidth(100);
        colAccion.setPrefWidth(100);
        colAccion.setResizable(false);

        tabla.getColumns().forEach(col -> {
            if (col != colAccion) {
                col.setMinWidth(50);  // Tamaño mínimo razonable
                col.setPrefWidth(50);
            }
        });
    }

    private static <T> Task<List<T>> getTask(TableView<T> tabla, Supplier<List<T>> fetchFunction) {
        Task<List<T>> task = new Task<>() {
            @Override
            protected List<T> call() throws Exception {
                return fetchFunction.get(); // Obtiene los datos en segundo plano
            }
        };

        task.setOnSucceeded(event -> {
            List<T> result = task.getValue();
            Platform.runLater(() -> tabla.setItems(FXCollections.observableArrayList(result)));
        });

        task.setOnFailed(event -> {
            Throwable ex = task.getException();
            Platform.runLater(() -> AlertHelper.mostrarAviso(
                    "Error al cargar datos: " + ex.getMessage(),
                    "/com/cd/incidenciasappfx/images/triangulo.png"
            ));
        });
        return task;
    }

    protected void eliminarRegistro(T t, Function<T, Boolean> deleteFunction, Runnable onSuccess, TableView<T> tabla) {
        if (!AlertHelper.mostrarConfirmacionEliminacion()) {
            return;
        }

        tabla.setDisable(true);

        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return deleteFunction.apply(t);
            }

            @Override
            protected void succeeded() {
                tabla.setDisable(false); // Reactivar la UI
                if (getValue()) {
                    AlertHelper.mostrarAviso("Registro eliminado", "/com/cd/incidenciasappfx/images/success.png");
                    onSuccess.run();
                } else {
                    AlertHelper.mostrarAviso("Error al eliminar registro", "/com/cd/incidenciasappfx/images/triangulo.png");
                }
            }

            @Override
            protected void failed() {
                tabla.setDisable(false); // Reactivar la UI
                getException().printStackTrace();
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
            Consumer<T> onActualizar,
            Consumer<T> onEliminar) {

        colAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btnActualizar;
            private final Button btnEliminar;
            private final HBox contenedorBotones;

            {
                btnActualizar = crearBoton("update.png",
                        "-fx-background-color: rgba(0, 0, 0, 0.1); -fx-border-radius: 5px; -fx-background-radius: 5px;",
                        onActualizar, this);

                btnEliminar = crearBoton("delete.png",
                        "-fx-background-color: rgba(255, 0, 0, 0.2); -fx-border-radius: 5px; -fx-background-radius: 5px;",
                        onEliminar, this);

                contenedorBotones = new HBox(10, btnActualizar, btnEliminar);
                configurarContenedorBotones(contenedorBotones);
                // 🔹 Aplicar estilos correctamente después de la inicialización
                Platform.runLater(() -> {
                    btnActualizar.setStyle("-fx-background-color: transparent;");
                    btnEliminar.setStyle("-fx-background-color: transparent;");
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);

                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(contenedorBotones);
                    T rowData = getTableView().getItems().get(getIndex());
                    btnActualizar.setOnAction(event -> onActualizar.accept(rowData));
                    btnEliminar.setOnAction(event -> onEliminar.accept(rowData));
                }
            }

        });
    }

    private Button crearBoton(String iconPath, String estiloHover, Consumer<T> accion, TableCell<T, Void> cell) {
        Button btn = new Button();

        // Obtener la imagen de los recursos
        InputStream imageStream = getClass().getResourceAsStream("/com/cd/incidenciasappfx/images/" + iconPath);

        if (imageStream == null) {
            System.err.println("No se encontró la imagen: " + iconPath);
            btn.setText("X");
            return btn;
        }

        try {
            ImageView icon = new ImageView(new Image(imageStream));
            icon.setFitWidth(20);
            icon.setFitHeight(20);
            btn.setGraphic(icon);
        } catch (Exception e) {
            System.err.println("Error al cargar la imagen: " + iconPath + " - " + e.getMessage());
        }

        btn.setOnAction(event -> {
            T item = cell.getTableView().getItems().get(cell.getIndex());
            accion.accept(item);
        });

        btn.setOnMouseEntered(event -> btn.setStyle(estiloHover));
        btn.setOnMouseExited(event -> btn.setStyle("-fx-background-color: transparent;"));

        return btn;
    }

    private void configurarContenedorBotones(HBox contenedor) {
        contenedor.setAlignment(Pos.CENTER);
    }
}
