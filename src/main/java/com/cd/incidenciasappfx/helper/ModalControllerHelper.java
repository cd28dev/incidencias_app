
package com.cd.incidenciasappfx.helper;

import java.lang.reflect.ParameterizedType;
import java.util.Optional;
import java.util.function.Function;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;


/**
 * ModalControllerHelper.java
 * 
 * @author CDAA
 * @param <T>
 */
public abstract class ModalControllerHelper<T> {
    private final Class<T> tipo = (Class<T>) ((ParameterizedType) getClass()
                .getGenericSuperclass()).getActualTypeArguments()[0];
    protected int numero;
    @FXML
    protected Button btnGuardar;
    @FXML
    protected Label lblTitle;
    
    
    
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
        
        lblTitle.setText("Actualizar "+tipo.getSimpleName());
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
            AlertHelper.mostrarAviso("Error inesperado al registrar", "/com/cd/incidenciasappfx/images/triangulo.png");
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
                return updateFunction.apply(entidad);
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
            AlertHelper.mostrarError("Error inesperado al actualizar");
        });

        new Thread(task).start();
    }
}
