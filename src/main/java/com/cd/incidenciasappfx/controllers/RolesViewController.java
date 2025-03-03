package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ExcelReportExporter;
import com.cd.incidenciasappfx.helper.JasperReportHelper;
import com.cd.incidenciasappfx.helper.PdfReportExporter;
import com.cd.incidenciasappfx.models.Rol;
import com.cd.incidenciasappfx.service.IRolesService;
import com.cd.incidenciasappfx.service.RolesServiceImpl;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
 * RolesViewController.java
 *
 * @author CDAA
 */
public class RolesViewController implements Initializable {

    private IRolesService rolService;

    @FXML
    private TableView<Rol> tablaRol;

    @FXML
    private TableColumn<Rol, Integer> colIdRol;
    @FXML
    private TableColumn<Rol, String> colRol;
    @FXML
    private TableColumn<Rol, Void> colAccion;

    @FXML
    private Button btnExportExcel;

    @FXML
    private Button btnExportPdf;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rolService = new RolesServiceImpl();
        configColumns();
        cargarRoles();
        tablaRol.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
    }

    @FXML
    private void abrirModal0() {
        abrirModal(0);
    }

    private void abrirModal(int numero) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/cd/incidenciasappfx/views/NuevoRol.fxml"));
            Parent root = loader.load();

            // Obtener el controlador del modal
            NuevoRolController modalController = loader.getController();

            // Pasar el número al controlador del modal
            modalController.setNumero(numero);
            modalController.setRolViewController(this);

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

        // Acción al hacer clic
        btn.setOnAction(event -> {
            Rol rol = cell.getTableView().getItems().get(cell.getIndex());
            Optional<Rol> rolOptional = rolService.findById(rol.getIdRol());
            if (rolOptional.isPresent()) {
                abrirModalActualizar(rolOptional.get(), 1);
            }

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
            Optional<Rol> rolOptional = rolService.findById(rol.getIdRol());
            if (rolOptional.isPresent()) {
                eliminarRol(rolOptional.get());
            }

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
        boolean eliminado = rolService.delete(r.getIdRol());
        if(eliminado){
            aviso("Rol eliminado", "/com/cd/incidenciasappfx/images/success.png");
            cargarRoles();
        }else{
            aviso("Error", "/com/cd/incidenciasappfx/images/triangulo.png");
        }
    }

    public void cargarRoles() {
        List<Rol> roles = rolService.findAll();
        ObservableList<Rol> rolesList = FXCollections.observableArrayList(roles);
        tablaRol.setItems(rolesList);
    }

    @FXML
    private void exportarExcel() {
        Path documentsDir = Paths.get(System.getProperty("user.home"), "Documents");
        String outputPath = documentsDir.resolve("RolReport.xlsx").toString();

        List<Rol> listaRoles = rolService.findAll();
        Map<String, Object> params = new HashMap<>();
        params.put("ReportTitle", "Reporte de Roles");

        JasperReportHelper.generateReport(
                "/com/cd/incidenciasappfx/report/UsuarioReportExcel.jrxml", outputPath,
                params,
                listaRoles,
                new ExcelReportExporter()
        );
    }

    @FXML
    private void exportarPDF() {
        Path documentsDir = Paths.get(System.getProperty("user.home"), "Documents");
        String outputPath = documentsDir.resolve("UsuarioReport.xlsx").toString();

        List<Rol> listaRoles = rolService.findAll();
        Map<String, Object> params = new HashMap<>();
        params.put("ReportTitle", "Reporte de Usuarios");

        JasperReportHelper.generateReport(
                "/com/cd/incidenciasappfx/report/UsuarioReportPdf.jrxml",
                outputPath,
                params,
                listaRoles,
                new PdfReportExporter()
        );
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
