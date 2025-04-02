package com.cd.incidenciasappfx.helper;

import java.lang.reflect.ParameterizedType;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

/**
 * ModalControllerHelper.java
 *
 * @author CDAA
 * @param <T>
 */
public abstract class ModalControllerHelper<T> {
    @FXML
    protected TextField id;
    @FXML
    protected TextField name;
    @FXML
    protected Button btnGuardar;
    @FXML
    protected Button btnCancelar;
    @FXML
    protected Label lblTitle;

    private final Class<T> tipo = (Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0];
    protected int numero;


    public void setNumero(int numero) {
        this.numero = numero;
        procesarNumero();
    }

    private void procesarNumero() {
        if (numero == 1) {
            changeTitle();
        }
    }

    protected void changeTitle() {

        lblTitle.setText("Actualizar " + tipo.getSimpleName());
        btnGuardar.setText("Update");
        btnGuardar.setStyle("-fx-background-color: #D4A017;");
    }

    protected void closeModal(Button btn) {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }

    protected <T> void registrarEntidad(
            T entidad,
            Function<T, Optional<T>> saveFunction,
            Runnable onSuccess
    ) {
        Task<Optional<T>> task = new Task<>() {
            @Override
            protected Optional<T> call() {
                return saveFunction.apply(entidad);
            }
        };

        task.setOnSucceeded(event -> {
            if (task.getValue().isPresent()) {
                AlertHelper.mostrarAviso("Registro exitoso", "/com/cd/incidenciasappfx/images/success.png");
                closeModal(btnGuardar);
                onSuccess.run();
            } else {
                AlertHelper.mostrarAviso("Registro fallido", "/com/cd/incidenciasappfx/images/triangulo.png");
            }
        });

        task.setOnFailed(event -> {
            Throwable error = task.getException();
            if (error != null) {
                error.printStackTrace(); // Imprime el error en la consola para depuración
                AlertHelper.mostrarAviso("Error: " + error.getMessage(), "/com/cd/incidenciasappfx/images/triangulo.png");
            } else {
                AlertHelper.mostrarAviso("Error inesperado al registrar", "/com/cd/incidenciasappfx/images/triangulo.png");
            }
        });

        new Thread(task).start();
    }

    protected <T> void actualizarEntidad(
            T entidad,
            Function<T, Optional<T>> updateFunction,
            Runnable onSuccess
    ) {
        Task<Optional<T>> task = new Task<>() {
            @Override
            protected Optional<T> call() {
                try {
                    return updateFunction.apply(entidad);
                } catch (Exception e) {
                    e.printStackTrace(); // 📌 Imprime el error en la consola
                    throw new RuntimeException("Error al ejecutar updateFunction", e);
                }
            }
        };

        task.setOnSucceeded(event -> {
            if (task.getValue().isPresent()) {
                AlertHelper.mostrarExito("Actualizacion exitosa");
                closeModal(btnGuardar);
                onSuccess.run();
            } else {
                AlertHelper.mostrarError("Actualizacion fallida");
            }
        });

        task.setOnFailed(event -> {
            Throwable error = task.getException(); // 📌 Obtiene la excepción
            error.printStackTrace(); // 📌 Imprime el error en la consola
            AlertHelper.mostrarError("Error inesperado al actualizar");
        });

        new Thread(task).start();
    }

    @FXML
    private void closeWindow() {
        Stage stage = (Stage) btnCancelar.getScene().getWindow();
        stage.close();
    }

    protected  <E> void cargarDatos(ComboBox<E> comboBox, Supplier<List<E>> servicio, Function<E, String> mapeo) {
        Task<List<E>> task = new Task<>() {
            @Override
            protected List<E> call() {
                return servicio.get();
            }
        };

        task.setOnSucceeded(event -> {
            Platform.runLater(() -> {
                List<E> elementos = task.getValue();
                if (elementos != null) {
                    comboBox.getItems().setAll(elementos);
                }
            });
        });

        new Thread(task).start();

        // Configurar el StringConverter para mostrar solo el nombre en el ComboBox
        comboBox.setConverter(new StringConverter<E>() {
            @Override
            public String toString(E objeto) {
                return objeto != null ? mapeo.apply(objeto) : "";
            }

            @Override
            public E fromString(String string) {
                return comboBox.getItems().stream()
                        .filter(objeto -> mapeo.apply(objeto).equals(string))
                        .findFirst()
                        .orElse(null);
            }
        });
    }
}
