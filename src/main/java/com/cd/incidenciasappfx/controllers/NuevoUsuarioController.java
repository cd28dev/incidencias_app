package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ImageHelper;
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

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
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
    private TextField idUser;

    @FXML
    private ComboBox<String> cbRol;
    @FXML
    private Button btnSubirFoto;
    @FXML
    private Button btnGuardar;

    @FXML
    private ImageView imgPreview;

    private String fotoPath;

    private int numero;

    @FXML
    public void initialize() {
        userService = new UsuarioServiceImpl();
        rolService = new RolesServiceImpl();
        deshabilitarButtons();
        addListeners();
        cargarRoles();
    }

    public void setNumero(int numero) {
        this.numero = numero;
        procesarNumero();
    }

    private void procesarNumero() {
        if (numero == 1) {
            changeTitle();
        }
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

    public void cargarDatosUsuario(Usuario u) {
        idUser.setText(String.valueOf(u.getIdUsuario()));
        txtDocumento.setText(u.getDni());
        txtNombres.setText(u.getNombre());
        txtApellidos.setText(u.getApellido());
        txtEmail.setText(u.getCorreo());
        txtUsername.setText(u.getUsuario());
        cbRol.setValue(u.getRol().getNombre());

        if (u.getFoto() != null && !u.getFoto().isEmpty()) {
            System.out.println("Cargando imagen desde: " + u.getFoto());
            fotoPath = u.getFoto();

            Image image = ImageHelper.cargarImagen(fotoPath);

            if (image != null) {
                imgPreview.setImage(image);
            } else {
                System.out.println("Advertencia: La imagen no existe en la ruta especificada.");
                imgPreview.setImage(null); // Puedes establecer una imagen por defecto aquí
            }
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
        if (numero == 0) {
            registrarUser();
        } else if (numero == 1) {
            updateUser();
        }

    }

    @FXML
    private String subirFoto() {
        String username = txtUsername.getText();
        fotoPath = ImageHelper.subirFoto(username);

        if (fotoPath != null) {
            imgPreview.setImage(ImageHelper.cargarImagen(fotoPath));
        }

        return fotoPath;
    }

    private String copiarImagen(File file) {
        String username = txtUsername.getText();

        try {
            File directorio = new File("fotos");
            if (!directorio.exists()) {
                directorio.mkdirs();
            }

            String extension = file.getName().substring(file.getName().lastIndexOf("."));
            String nuevoNombre = username + "_profile" + extension;

            File destino = new File(directorio, nuevoNombre);

            Files.copy(file.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);

            return "fotos/" + nuevoNombre;
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

    public void updateUser() {
        String id = idUser.getText();
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
        usuario.setIdUsuario(Integer.parseInt(id));
        usuario.setDni(documento);
        usuario.setNombre(nombres);
        usuario.setApellido(apellidos);
        usuario.setCorreo(email);
        usuario.setUsuario(username);
        rol.setNombre(rolSeleccionado);
        usuario.setRol(rol);
        usuario.setFoto(foto);

        if (userService.update(usuario).isPresent()) {
            mostrarModal("Usuario actualizado", "/com/cd/incidenciasappfx/images/success.png");
            closeModal(btnGuardar);
            usuariosViewController.cargarUsuarios();
        } else {
            mostrarModal("Hubo algún problema", "/com/cd/incidenciasappfx/images/triangulo.png");

        }

    }

    public void registrarUser() {
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
            mostrarModal("Usuario registrado", "/com/cd/incidenciasappfx/images/success.png");
            closeModal(btnGuardar);
            usuariosViewController.cargarUsuarios();
        } else {
            mostrarModal("Hubo algún problema", "/com/cd/incidenciasappfx/images/triangulo.png");

        }

    }

}
