package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.JasperReportHelper;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import com.cd.incidenciasappfx.models.Sector;
import com.cd.incidenciasappfx.service.ISectorService;
import com.cd.incidenciasappfx.service.SectorServiceImpl;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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

/**
 * SectorViewController.java
 *
 * @author CDAA
 */
public class SectorViewController implements Initializable {

    private ISectorService sectorService;

    @FXML
    private TableView<Sector> tablaSector;

    @FXML
    private TableColumn<Sector, Integer> colIdSector;
    @FXML
    private TableColumn<Sector, String> colSector;
    @FXML
    private TableColumn<Sector, Void> colAccion;

    @FXML
    private Button btnExportExcel;

    @FXML
    private Button btnExportPdf;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        sectorService = new SectorServiceImpl();
        configColumns();
        cargarSectores();
        tablaSector.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    private void abrirModal0() {
        abrirModal(0);
    }

    private void abrirModal(int numero) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cd/incidenciasappfx/views/NuevoSector.fxml"));
            Parent root = loader.load();

            // Obtener el controlador del modal
            NuevoSectorController modalController = loader.getController();

            // Pasar el número al controlador del modal
            modalController.setNumero(numero);
            modalController.setSectorViewController(this);

            Stage modalStage = new Stage();
            modalStage.initModality(Modality.APPLICATION_MODAL);
            modalStage.setTitle("Nuevo Rol");
            modalStage.setScene(new Scene(root));
            modalStage.setResizable(false);

            modalStage.showAndWait();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void configColumns() {
        tablaRol.getColumns().clear();
        tablaRol.getColumns().addAll(colIdRol, colRol, colAccion);

        colIdRol.setCellValueFactory(data
                -> new ReadOnlyObjectWrapper<>(data.getValue().getIdRol())
        );
        colRol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        configurarColumnaAccion();
    }

    private void configurarColumnaAccion() {
        colAccion.setCellFactory(param -> new TableCell<>() {
            private final Button btnActualizar = crearBotonActualizar(this);
            private final Button btnEliminar = crearBotonEliminar(this);
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

    /**
     * Crea y configura el botón de actualizar.
     */
    private Button crearBotonActualizar(TableCell<Rol, Void> cell) {
        Button btn = new Button();
        ImageView iconActualizar = new ImageView(new Image(getClass().getResourceAsStream("/com/cd/incidenciasappfx/images/update.png")));
        iconActualizar.setFitWidth(20);
        iconActualizar.setFitHeight(20);
        btn.setGraphic(iconActualizar);
        btn.setStyle("-fx-background-color: transparent;");

        btn.setOnAction(event -> {
            Rol rol = cell.getTableView().getItems().get(cell.getIndex());
            abrirModalActualizar(rol, 1);

        });

        // Efecto Hover
        btn.setOnMouseEntered(event -> btn.setStyle("-fx-background-color: rgba(0, 0, 0, 0.1); -fx-border-radius: 5px; -fx-background-radius: 5px;"));
        btn.setOnMouseExited(event -> btn.setStyle("-fx-background-color: transparent;"));

        return btn;
    }

    private Button crearBotonEliminar(TableCell<Rol, Void> cell) {
        Button btn = new Button();
        ImageView iconEliminar = new ImageView(new Image(getClass().getResourceAsStream("/com/cd/incidenciasappfx/images/delete.png")));
        iconEliminar.setFitWidth(20);
        iconEliminar.setFitHeight(20);
        btn.setGraphic(iconEliminar);
        btn.setStyle("-fx-background-color: transparent;");

        // Acción al hacer clic
        btn.setOnAction(event -> {
            Rol rol = cell.getTableView().getItems().get(cell.getIndex());
            eliminarRol(rol);

        });
        // Efecto Hover
        btn.setOnMouseEntered(event -> btn.setStyle("-fx-background-color: rgba(255, 0, 0, 0.2); -fx-border-radius: 5px; -fx-background-radius: 5px;"));
        btn.setOnMouseExited(event -> btn.setStyle("-fx-background-color: transparent;"));

        return btn;
    }

    private void configurarContenedorBotones(HBox contenedor) {
        contenedor.setAlignment(Pos.CENTER);
    }

    private void abrirModalActualizar(Rol rol, int number) {
        try {
            // Cargar el archivo FXML del modal
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cd/incidenciasappfx/views/NuevoRol.fxml"));
            Parent root = loader.load();

            // Obtener el controlador del modal
            NuevoRolController controller = loader.getController();

            controller.setNumero(number);

            controller.cargarDatosRol(rol);
            controller.setRolViewController(this);
            // Crear la nueva ventana (Stage)
            Stage stage = new Stage();
            stage.setTitle("Actualizar Rol");
            stage.setScene(new Scene(root));
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(tablaRol.getScene().getWindow());
            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarRol(Rol r) {
        Task<Boolean> task = new Task<>() {
            @Override
            protected Boolean call() throws Exception {
                return rolService.delete(r.getIdRol());
            }

            @Override
            protected void succeeded() {
                if (getValue()) {
                    aviso("Rol eliminado", "/com/cd/incidenciasappfx/images/success.png");
                    cargarRoles(); // Recargar roles tras eliminación exitosa
                } else {
                    aviso("Error al eliminar rol", "/com/cd/incidenciasappfx/images/triangulo.png");
                }
            }

            @Override
            protected void failed() {
                aviso("Error inesperado al eliminar rol", "/com/cd/incidenciasappfx/images/triangulo.png");
            }
        };

        new Thread(task).start();
    }

    public void cargarRoles() {
        Task<List<Rol>> task = new Task<>() {
            @Override
            protected List<Rol> call() throws Exception {
                return rolService.findAll(); // Consulta en segundo plano
            }
        };

        task.setOnSucceeded(event -> {
            ObservableList<Rol> rolesList = FXCollections.observableArrayList(task.getValue());
            tablaRol.setItems(rolesList); // Se actualiza la UI en el hilo principal
        });

        task.setOnFailed(event -> {
            aviso("Error al cargar roles", "/com/cd/incidenciasappfx/images/triangulo.png");
        });

        new Thread(task).start(); // Iniciar la tarea en un hilo separado
    }

    @FXML
    private void exportarExcel() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Path documentsDir = Paths.get(System.getProperty("user.home"), "Documents");
                String outputPath = documentsDir.resolve("RolReport.xlsx").toString();

                List<Rol> listaRoles = rolService.findAll(); // Carga en segundo plano
                Map<String, Object> params = new HashMap<>();
                params.put("ReportTitle", "Reporte de Roles");

                JasperReportHelper.generateReport(
                        "/com/cd/incidenciasappfx/report/UsuarioReportExcel.jrxml",
                        outputPath,
                        params,
                        listaRoles,
                        new ExcelReportExporter()
                );
                return null;
            }

            @Override
            protected void succeeded() {
                aviso("Reporte Excel generado con éxito", "/com/cd/incidenciasappfx/images/success.png");
            }

            @Override
            protected void failed() {
                aviso("Error al generar el reporte", "/com/cd/incidenciasappfx/images/triangulo.png");
            }
        };

        new Thread(task).start();
    }

    @FXML
    private void exportarPDF() {
        Task<Void> task = new Task<>() {
            @Override
            protected Void call() throws Exception {
                Path documentsDir = Paths.get(System.getProperty("user.home"), "Documents");
                String outputPath = documentsDir.resolve("RolReport.pdf").toString(); // 🔹 Corregido el nombre del archivo

                List<Rol> listaRoles = rolService.findAll(); // 🔹 Cargar roles en segundo plano
                Map<String, Object> params = new HashMap<>();
                params.put("ReportTitle", "Reporte de Roles");

                JasperReportHelper.generateReport(
                        "/com/cd/incidenciasappfx/report/RolReportPdf.jrxml", // 🔹 Corregido el nombre del reporte
                        outputPath,
                        params,
                        listaRoles,
                        new PdfReportExporter()
                );

                return null;
            }

            @Override
            protected void succeeded() {
                aviso("Reporte PDF generado con éxito", "/com/cd/incidenciasappfx/images/success.png");
            }

            @Override
            protected void failed() {
                aviso("Error al generar el reporte", "/com/cd/incidenciasappfx/images/triangulo.png");
            }
        };

        new Thread(task).start();
    }

    private void aviso(String mensaje, String icono) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cd/incidenciasappfx/views/modal_small.fxml"));
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
}
