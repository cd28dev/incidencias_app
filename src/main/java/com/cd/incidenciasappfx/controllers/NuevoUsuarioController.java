package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.models.Rol;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import com.cd.incidenciasappfx.models.Usuario;
import com.cd.incidenciasappfx.service.IRolesService;
import com.cd.incidenciasappfx.service.IUsuarioService;
import com.cd.incidenciasappfx.service.RolesServiceImpl;
import com.cd.incidenciasappfx.service.UsuarioServiceImpl;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class NuevoUsuarioController {

    private IUsuarioService userService;
    private IRolesService rolService;
    private UsuariosViewController usuariosViewController;

    @FXML
    private Label lblNewUser;
    @FXML
    private TextField txtDocumento;
    @FXML
    private TextField txtNombres;
    @FXML
    private TextField txtApellidos;
    @FXML
    private TextField txtEmail;
    @FXML
    private TextField txtUsername;
    @FXML
    private ComboBox<String> cbRol;
    @FXML
    private Button btnSubirFoto;
    @FXML
    private Button btnGuardar;

    @FXML
    private ImageView imgPreview;

    private String fotoPath;

    @FXML
    public void initialize() {
        userService = new UsuarioServiceImpl();
        rolService = new RolesServiceImpl();
        deshabilitarButtons();
        addListeners();
        cargarRoles();
    }

    private void deshabilitarButtons() {
        btnSubirFoto.setDisable(true);
        btnGuardar.setDisable(true);
    }

    private void addListeners() {
        txtDocumento.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtNombres.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtApellidos.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtEmail.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtUsername.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        cbRol.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
    }

    private void validarCampos() {
        boolean camposLlenos = !txtDocumento.getText().trim().isEmpty()
                && !txtNombres.getText().trim().isEmpty()
                && !txtApellidos.getText().trim().isEmpty()
                && !txtEmail.getText().trim().isEmpty()
                && !txtUsername.getText().trim().isEmpty()
                && cbRol.getValue() != null;

        btnSubirFoto.setDisable(!camposLlenos);
        btnGuardar.setDisable(!camposLlenos);
    }

    public void cargarDatosUsuario(Usuario user) {
        Optional<Usuario> usuario = userService.findById(user.getDni());

        if (usuario.isPresent()) {
            Usuario u = usuario.get();

            txtDocumento.setText(u.getDni());
            txtNombres.setText(u.getNombre());
            txtApellidos.setText(u.getApellido());
            txtEmail.setText(u.getCorreo());
            txtUsername.setText(u.getUsuario());
            cbRol.setValue(u.getRol().getNombre());
        }
    }

    private void cargarRoles() {
        List<Rol> roles = rolService.findAll();

        ObservableList<String> listaRoles = FXCollections.observableArrayList(
                roles.stream().map(Rol::getNombre).toList()
        );

        cbRol.setItems(listaRoles);
    }

    public void changeTitle() {
        lblNewUser.setText("Actualizar Usuario");
        btnGuardar.setText("Update");
        btnGuardar.setStyle("-fx-background-color: #D4A017;");
    }

    @FXML
    public void saveUser() {
        String documento = txtDocumento.getText();
        String nombres = txtNombres.getText();
        String apellidos = txtApellidos.getText();
        String email = txtEmail.getText();
        String username = txtUsername.getText();
        String rolSeleccionado = cbRol.getValue();
        String foto = fotoPath;

        if (documento.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()
                || email.isEmpty() || username.isEmpty() || rolSeleccionado == null) {
            return;
        }

        Usuario usuario = new Usuario();
        Rol rol = new Rol();
        usuario.setDni(documento);
        usuario.setNombre(nombres);
        usuario.setApellido(apellidos);
        usuario.setCorreo(email);
        usuario.setUsuario(username);
        rol.setNombre(rolSeleccionado);
        usuario.setRol(rol);
        usuario.setFoto(foto);

        if (userService.save(usuario).isPresent()) {
            mostrarModal("Usuario guardado correctamente", "/com/cd/incidenciasappfx/images/success.png");
            closeModal(btnGuardar);
            usuariosViewController.cargarUsuarios();
        } else {
            mostrarModal("Hubo algún problema", "/com/cd/incidenciasappfx/images/triangulo.png");

        }

    }

    @FXML
    private String subirFoto() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));

        File file = fileChooser.showOpenDialog(btnSubirFoto.getScene().getWindow());
        if (file != null) {
            Image image = new Image(file.toURI().toString());
            imgPreview.setImage(image);
            fotoPath = copiarImagen(file);
        }

        return fotoPath;
    }

    private String copiarImagen(File file) {
        try {
            File directorio = new File("src/main/resources/com/cd/incidenciasappfx/fotos/");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            String nuevoNombre = UUID.randomUUID().toString() + "_" + file.getName();
            File destino = new File(directorio, nuevoNombre);

            Files.copy(file.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return "com/cd/incidenciasappfx/fotos/" + nuevoNombre;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void closeModal(Button btn) {
        Stage stage = (Stage) btn.getScene().getWindow();
        stage.close();
    }

    public void setUsuariosViewController(UsuariosViewController controller) {
        this.usuariosViewController = controller;
    }
    
    private void mostrarModal(String mensaje, String icono) {
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
