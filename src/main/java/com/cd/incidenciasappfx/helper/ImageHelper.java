package com.cd.incidenciasappfx.helper;

import javafx.scene.image.Image;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

/**
 * ImageHelper.java
 *
 * @author CDAA
 */
public class ImageHelper {

    private static final String IMAGE_DIRECTORY = "fotos";

    public static String subirFoto(String username) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            return copiarImagen(file, username);
        }
        return null;
    }

    public static String copiarImagen(File file, String username) {
        try {
            File directorio = new File(IMAGE_DIRECTORY);
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            String extension = file.getName().substring(file.getName().lastIndexOf("."));
            String nuevoNombre = username + "_profile" + extension;

            File destino = new File(directorio, nuevoNombre);
            Files.copy(file.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return IMAGE_DIRECTORY + "/" + nuevoNombre;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Image cargarImagen(String imagePath) {
        File file = new File(imagePath);
        if (file.exists()) {
            return new Image(file.toURI().toString());
        }
        return null;
    }
}

