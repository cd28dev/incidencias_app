package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.AlertHelper;
import com.cd.incidenciasappfx.helper.ImageHelper;
import com.cd.incidenciasappfx.helper.ModalControllerHelper;
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
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class NuevoUsuarioController extends ModalControllerHelper<Usuario> {

    private IUsuarioService userService;
    private IRolesService rolService;
    private UsuariosViewController usuariosViewController;

    @FXML
    private TextField txtDocumento;
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
    private ImageView imgPreview;

    private String fotoPath;

    public NuevoUsuarioController() {
    }

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
        name.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtApellidos.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtEmail.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        txtUsername.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        cbRol.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
    }

    private void validarCampos() {
        boolean camposLlenos = !txtDocumento.getText().trim().isEmpty()
                && !name.getText().trim().isEmpty()
                && !txtApellidos.getText().trim().isEmpty()
                && !txtEmail.getText().trim().isEmpty()
                && !txtUsername.getText().trim().isEmpty()
                && cbRol.getValue() != null;

        btnSubirFoto.setDisable(!camposLlenos);
        btnGuardar.setDisable(!camposLlenos);
    }

    public void cargarCamposUsuario(Usuario u) {
        idUser.setText(String.valueOf(u.getIdUsuario()));
        txtDocumento.setText(u.getDni());
        name.setText(u.getNombre());
        txtApellidos.setText(u.getApellido());
        txtEmail.setText(u.getCorreo());
        txtUsername.setText(u.getUsuario());
        cbRol.setValue(u.getRol().getNombre());

        if (u.getFoto() != null && !u.getFoto().isEmpty()) {
            fotoPath = u.getFoto();

            Image image = ImageHelper.cargarImagen(fotoPath);

            imgPreview.setImage(image);
        }
    }

    private void cargarRoles() {
        Task<List<Rol>> task = new Task<>() {
            @Override
            protected List<Rol> call() throws Exception {
                return rolService.findAll();
            }
        };

        task.setOnSucceeded(event -> {
            List<Rol> roles = task.getValue();
            if (roles != null) {
                ObservableList<String> listaRoles = FXCollections.observableArrayList(
                        roles.stream().map(Rol::getNombre).toList()
                );
                cbRol.setItems(listaRoles);
            }
        });

        new Thread(task).start();
    }

    @FXML
    private String subirFoto() {
        String username = txtUsername.getText();
        fotoPath = ImageHelper.subirFoto(username);

        if (fotoPath != null) {
            imgPreview.setImage(ImageHelper.cargarImagen(fotoPath));
        }

        //vamooooooooo perú csm
        return fotoPath;
    }

    public void setUsuariosViewController(UsuariosViewController controller) {
        this.usuariosViewController = controller;
    }

    @FXML
    private void save() {
        if (numero == 0) {
            registrarUser();
        } else if (numero == 1) {
            actualizarUser();
        }
    }

    private void registrarUser() {
        String documento = txtDocumento.getText();
        String nombres = name.getText();
        String apellidos = txtApellidos.getText();
        String email = txtEmail.getText();
        String username = txtUsername.getText();
        String rolSeleccionado = cbRol.getValue();
        String foto = fotoPath;

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            AlertHelper.mostrarError("Correo inválido");
            return;
        }

        Usuario usuario = new Usuario();
        Rol rol = new Rol();
        rol.setNombre(rolSeleccionado);
        usuario.setDni(documento);
        usuario.setNombre(nombres);
        usuario.setApellido(apellidos);
        usuario.setCorreo(email);
        usuario.setUsuario(username);
        usuario.setRol(rol);
        usuario.setFoto(foto);

        registrarEntidad(usuario, userService::save, usuariosViewController::cargarUsuarios);
    }

    private void actualizarUser() {
        String id = idUser.getText();
        String documento = txtDocumento.getText();
        String nombres = name.getText();
        String apellidos = txtApellidos.getText();
        String email = txtEmail.getText();
        String username = txtUsername.getText();
        String rolSeleccionado = cbRol.getValue();
        String foto = fotoPath;

        if (documento.isEmpty() || nombres.isEmpty() || apellidos.isEmpty()
                || email.isEmpty() || username.isEmpty() || rolSeleccionado == null) {
            AlertHelper.mostrarError("Todos los campos son obligatorios");
            return;
        }

        if (!email.matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            AlertHelper.mostrarError("Correo inválido");
            return;
        }

        Usuario usuario = new Usuario();
        Rol rol = new Rol();
        rol.setNombre(rolSeleccionado);
        usuario.setIdUsuario(Integer.parseInt(id));
        usuario.setDni(documento);
        usuario.setNombre(nombres);
        usuario.setApellido(apellidos);
        usuario.setCorreo(email);
        usuario.setUsuario(username);
        usuario.setRol(rol);
        usuario.setFoto(foto);

       
        actualizarEntidad(usuario,userService::update,usuariosViewController::cargarUsuarios);
    }

}

