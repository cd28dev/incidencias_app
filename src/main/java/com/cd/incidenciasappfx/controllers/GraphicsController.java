package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.dto.*;
import com.cd.incidenciasappfx.service.GraphicsServiceImpl;
import com.cd.incidenciasappfx.service.IGraphicService;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;

import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.util.Duration;

import java.net.URL;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GraphicsController implements Initializable {

    @FXML
    private ComboBox<String> comboTipoGrafico;

    @FXML
    private StackPane chartContainer;

    private IGraphicService graphicService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {}

    public GraphicsController() {
        graphicService = new GraphicsServiceImpl();
    }

    @FXML
    public void mostrarGraficoSeleccionado(ActionEvent actionEvent) {
        String seleccion = comboTipoGrafico.getSelectionModel().getSelectedItem();
        chartContainer.getChildren().clear();
        ejecutarGraficoEnSegundoPlano(seleccion);
    }

    private void ejecutarGraficoEnSegundoPlano(String seleccion) {
        Task<BarChart<?, ?>> task = new Task<>() {
            @Override
            protected BarChart<?, ?> call() throws Exception {
                return switch (seleccion) {
                    case "Por Ocurrencia" -> generarGraficoPorOcurrencia();
                    case "Por Intervención" -> generarGraficoPorIntervencion();
                    case "Por Apoyo" -> generarGraficoPorApoyo();
                    case "Por Servicio" -> generarGraficoPorServicio();
                    case "Por Urbanización" -> generarGraficoPorUrbanizacion();
                    case "Por Sector" -> generarGraficoPorSector();
                    case "Por Delito" -> generarGraficoPorDelito();
                    default -> null;
                };
            }
        };

        task.setOnSucceeded(e -> {
            BarChart<?, ?> chart = task.getValue();
            if (chart != null) {
                chart.getStylesheets().add(getClass().getResource("/com/cd/incidenciasappfx/styles/chart-style.css").toExternalForm());
                aplicarEstiloCategoria(chart);
                chartContainer.getChildren().add(chart);
            }
        });
        task.setOnFailed(e -> task.getException().printStackTrace());
        new Thread(task).start();
    }



    private <T> BarChart<?, ?> generarGrafico(String titulo, List<T> datos,
                                              Function<T, String> obtenerX, Function<T, Integer> obtenerY,
                                              boolean horizontal) {
        if (horizontal) {
            NumberAxis xAxis = new NumberAxis();
            CategoryAxis yAxis = new CategoryAxis();
            BarChart<Number, String> chart = new BarChart<>(xAxis, yAxis);

            var agrupadoPorCategoria = datos.stream().collect(Collectors.groupingBy(obtenerX));

            for (var entry : agrupadoPorCategoria.entrySet()) {
                String nombreCategoria = entry.getKey();
                List<T> datosCategoria = entry.getValue();

                XYChart.Series<Number, String> serie = new XYChart.Series<>();

                for (T dato : datosCategoria) {
                    serie.getData().add(new XYChart.Data<>(obtenerY.apply(dato), nombreCategoria));
                    serie.setName(nombreCategoria);
                }
                chart.getData().add(serie);
            }

            chart.setTitle(titulo);
            chart.setLegendVisible(true);
            chart.setLegendSide(Side.RIGHT);
            chart.setLegendSide(Side.BOTTOM);
            chart.setCategoryGap(10);
            return chart;

        } else {
            CategoryAxis xAxis = new CategoryAxis();
            NumberAxis yAxis = new NumberAxis();
            BarChart<String, Number> chart = new BarChart<>(xAxis, yAxis);

            var agrupadoPorCategoria = datos.stream().collect(Collectors.groupingBy(obtenerX));

            for (var entry : agrupadoPorCategoria.entrySet()) {
                String nombreCategoria = entry.getKey();
                List<T> datosCategoria = entry.getValue();

                XYChart.Series<String, Number> serie = new XYChart.Series<>();

                for (T dato : datosCategoria) {
                    serie.getData().add(new XYChart.Data<>(nombreCategoria, obtenerY.apply(dato)));
                    serie.setName(nombreCategoria);
                }
                chart.getData().add(serie);
            }
            chart.setTitle(titulo);
            chart.setLegendVisible(true);
            chart.setLegendSide(javafx.geometry.Side.RIGHT);
            chart.setCategoryGap(10);
            return chart;
        }
    }


    private BarChart<?, ?> generarGraficoPorServicio() throws SQLException {
        List<ServicioDTO> datos = graphicService.obtenerServiciosPorTipo();
        return generarGrafico("Incidencias por Servicio", datos,
                ServicioDTO::getServicio, ServicioDTO::getCantidad, true);
    }

    private BarChart<?, ?> generarGraficoPorApoyo() throws SQLException {
        List<ApoyoDTO> datos = graphicService.obtenerIncidenciasConApoyoPolicial();
        return generarGrafico("Incidencias por Apoyo Policial", datos,
                ApoyoDTO::getTipoApoyo, ApoyoDTO::getTotal, false);
    }

    private BarChart<?, ?> generarGraficoPorOcurrencia() throws SQLException {
        List<OcurrenciaDTO> datos = graphicService.obtenerIncidenciasPorTipoOcurrencia();
        return generarGrafico("Incidencias por Tipos de Ocurrencia", datos,
                OcurrenciaDTO::getTipoOcurrencia, OcurrenciaDTO::getCantidad, false);
    }

    private BarChart<?, ?> generarGraficoPorUrbanizacion() throws SQLException {
        List<UrbanizacionDTO> datos = graphicService.obtenerIncidenciasPorUrbanizacion();
        return generarGrafico("Incidencias por Urb.", datos,
                UrbanizacionDTO::getUrbanizacion, UrbanizacionDTO::getCantidad, false);
    }

    private BarChart<?, ?> generarGraficoPorSector() throws SQLException {
        List<SectorDTO> datos = graphicService.obtenerIncidenciasPorSector();
        return generarGrafico("Incidencias por Sector", datos,
                SectorDTO::getSector, SectorDTO::getCantidad, false);
    }

    private BarChart<?, ?> generarGraficoPorDelito() throws SQLException {
        List<DelitoDTO> datos = graphicService.obtenerIncidenciasPorDelito();
        return generarGrafico("Incidencias por Delitos", datos,
                DelitoDTO::getDelito, DelitoDTO::getCantidad, true);
    }

    private BarChart<?, ?> generarGraficoPorIntervencion() throws SQLException {
        List<IntervencionDTO> datos = graphicService.obtenerIntervencionesPorTipo();
        return generarGrafico("Incidencias por Tipos de Intervencion", datos,
                IntervencionDTO::getTipoIntervencion, IntervencionDTO::getCantidad, true);
    }


    private void aplicarEstiloCategoria(BarChart<?, ?> chart) {
        // --- Cálculo dinámico del categoryGap ---
        int cantidadCategorias = chart.getData().stream().flatMap(series -> series.getData().stream()).map(XYChart.Data::getXValue)
                .collect(Collectors.toSet())
                .size();

        double gapCalculado = Math.max(10, 200 - (cantidadCategorias * 15));
        chart.setCategoryGap(gapCalculado);
        chart.setBarGap(2);

        // --- Ajuste dinámico del tamaño del chart ---
        double anchoBase = 600;
        double anchoPorCategoria = 50;
        double nuevoAncho = anchoBase + (cantidadCategorias * anchoPorCategoria);

        chart.setPrefWidth(nuevoAncho);
        chart.setMinWidth(nuevoAncho);
        chart.setMaxWidth(nuevoAncho);

        // --- Fuente de los ejes ---
        if (chart.getXAxis() instanceof CategoryAxis categoryAxis) {
            categoryAxis.setTickLabelFont(Font.font("Poppins", FontWeight.NORMAL, 15));
            categoryAxis.setTickLabelsVisible(false);
            categoryAxis.setTickMarkVisible(false);
        }
        if (chart.getYAxis() instanceof CategoryAxis categoryAxis) {
            categoryAxis.setTickLabelFont(Font.font("Poppins", FontWeight.NORMAL, 15));
            categoryAxis.setTickLabelsVisible(false);
            categoryAxis.setTickMarkVisible(false);
        }
        if (chart.getXAxis() instanceof NumberAxis numberAxis) {
            numberAxis.setTickLabelFont(Font.font("Poppins", FontWeight.NORMAL, 15));

        }
        if (chart.getYAxis() instanceof NumberAxis numberAxis) {
            numberAxis.setTickLabelFont(Font.font("Poppins", FontWeight.NORMAL, 15));
        }
        // --- Color de fondo ---
        chart.setStyle("-fx-background-color: transparent;");

        // --- Título del gráfico ---
        Node title = chart.lookup(".chart-title");
        if (title != null) {
            title.setStyle("-fx-font-size: 16px; -fx-font-weight: bold; -fx-font-family: 'Poppins';");
        }

        // --- Reorganizar la leyenda en múltiples columnas ---
        reorganizarLeyendaEnColumnas(chart, 5); // 5 columnas
    }

    private void reorganizarLeyendaEnColumnas(BarChart<?, ?> chart, int columnasDeseadas) {
        PauseTransition espera = new PauseTransition(Duration.millis(300)); // espera inicial

        espera.setOnFinished(event -> {
            Platform.runLater(() -> {
                Node legend = chart.lookup(".chart-legend");

                if (legend instanceof VBox vbox && !vbox.getChildren().isEmpty()) {
                    List<Node> items = new ArrayList<>(vbox.getChildren());

                    int filas = (int) Math.ceil((double) items.size() / columnasDeseadas);
                    GridPane grid = new GridPane();
                    grid.setHgap(20);
                    grid.setVgap(5);

                    for (int i = 0; i < items.size(); i++) {
                        int col = i / filas;
                        int row = i % filas;
                        grid.add(items.get(i), col, row);
                    }

                    vbox.getChildren().setAll(grid);
                } else {
                    // Retry if the legend wasn't found or empty
                    reorganizarLeyendaEnColumnas(chart, columnasDeseadas);
                }
            });
        });

        espera.play();
    }


}
