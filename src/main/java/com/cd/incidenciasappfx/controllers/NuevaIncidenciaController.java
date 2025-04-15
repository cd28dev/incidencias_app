package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.AlertHelper;
import com.cd.incidenciasappfx.helper.ModalControllerHelper;
import com.cd.incidenciasappfx.models.*;
import com.cd.incidenciasappfx.service.*;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public class NuevaIncidenciaController extends ModalControllerHelper<Incidencia> {

    public ToggleGroup groupApoyo;
    public ToggleGroup groupAgraviado;

    private RadioButton selectedRadioButton;
    @FXML
    private RadioButton rbSiAgraviado;
    @FXML
    private RadioButton rbNoAgraviado;
    @FXML
    private RadioButton rbSiApoyo;
    @FXML
    private RadioButton rbNoApoyo;

    @FXML
    private ComboBox<Sector> cbSector;
    @FXML
    private ComboBox<ServicioSerenazgo> cbServicio;
    @FXML
    private ComboBox<Urbanizacion> cbUrbanizacion;
    @FXML
    private ComboBox<TipoOcurrencia> cbOcurrencia;
    @FXML
    private ComboBox<TipoIntervencion> cbIntervencion;
    @FXML
    private ComboBox<Delito> cbDelito;

    @FXML
    private TextArea txtDescripcion;
    @FXML
    private TextArea txtDescargoInf;
    @FXML
    private TextArea txtDescargoAgr;

    @FXML
    private Button btnAnterior;
    @FXML
    private Button btnSiguiente;

    @FXML
    private VBox sectionIncidencia;
    @FXML
    private VBox sectionInfractor;
    @FXML
    private VBox sectionAgraviado;
    @FXML
    private AnchorPane modalPane;
    @FXML
    private StackPane stackPane;

    @FXML
    private DatePicker dpFecha;

    @FXML
    private TextField txtDniInfractor;
    @FXML
    private TextField txtNombresInfractor;
    @FXML
    private TextField txtApellidosInfractor;
    @FXML
    private TextField txtDniAgraviado;
    @FXML
    private TextField txtNombresAgraviado;
    @FXML
    private TextField txtApellidosAgraviado;
    @FXML
    private TextField txtTelefonoInf;
    @FXML
    private TextField txtTelefonoAgr;
    @FXML
    private TextField idInfractor;
    @FXML
    private TextField idIncidencia;
    @FXML
    private TextField txtDireccion;
    @FXML
    private TextField idAgraviado;

    private IIncidenciaService incidenciaService;
    private IncidenciasController incidenciasController;
    private final ISectorService sectorService;
    private final ITipoOcurrenciaService ocurrenciaService;
    private final ITipoIntService intervencionService;
    private final IDelitoService delitoService;
    private final IUrbService urbService;
    private final IServiciosService serviciosService;

    public NuevaIncidenciaController() {
        incidenciaService = new IncidenciaServiceImpl();
        sectorService = new SectorServiceImpl();
        ocurrenciaService = new TipoOcurrenciaServiceImpl();
        intervencionService = new TipoIntServiceImpl();
        delitoService = new DelitoServiceImpl();
        urbService = new UrbServiceImpl();
        serviciosService = new ServicioServiceImpl();
    }

    @FXML
    public void initialize() {
        inicializarUI();
        agregarListeners();
        cargarCombos();
    }

    @FXML
    public void close() {
    }

    @FXML
    public void volver() {
        if (sectionInfractor.isVisible()) {
            cambiarSeccion(sectionIncidencia);
            lblTitle.setText("Nueva Incidencia");
        } else if (sectionAgraviado.isVisible()) {
            cambiarSeccion(sectionInfractor);
            lblTitle.setText("Datos del infractor");
        }
        btnSiguiente.setDisable(false);
    }

    @FXML
    public void siguiente() {
        if (sectionIncidencia.isVisible()) {
            cambiarSeccion(sectionInfractor);
            lblTitle.setText("Datos del Infractor");
        } else if (sectionInfractor.isVisible()) {
            cambiarSeccion(sectionAgraviado);
            lblTitle.setText("Datos del Agraviado");
        }
        btnAnterior.setDisable(false);
        btnSiguiente.setDisable(true);
    }

    @FXML
    public void save() {
        if (numero == 0) {
            registrarIncidencia();
        } else if (numero == 1) {
            actualizarIncidencia();
        }

    }

    private void registrarIncidencia() {
        Usuario usuarioLogueado = SesionUsuario.getUsuarioActual();
        Sector sectorSeleccionado = cbSector.getValue();
        ServicioSerenazgo servicioSerenazgo = cbServicio.getValue();
        Urbanizacion urbanizacionSeleccionado = cbUrbanizacion.getValue();
        TipoIntervencion intervencionSeleccionado = cbIntervencion.getValue();
        Delito delitoSeleccionado = cbDelito.getValue();
        TipoOcurrencia ocurrenciaSeleccionado = cbOcurrencia.getValue();
        String descripcion = txtDescripcion.getText();
        String direccion = txtDireccion.getText();
        LocalDate fecha = dpFecha.getValue();
        LocalDateTime fecha_hora = fecha.atTime(LocalTime.now());

        Toggle selectedToggle = groupApoyo.getSelectedToggle();
        RadioButton selectedRadio = (RadioButton) selectedToggle;
        String apoyoSeleccionado = selectedRadio.getText().toUpperCase().replace(" ", "_"); // Transforma el texto

        String dni_inf = txtDniInfractor.getText();
        String nombre_inf = txtNombresInfractor.getText();
        String apellido_inf = txtApellidosInfractor.getText();
        String telefono_inf = txtTelefonoInf.getText();
        String descargo_inf = txtDescargoInf.getText();
        String dni_agr = txtDniAgraviado.getText();
        String nombre_agr = txtNombresAgraviado.getText();
        String apellido_agr = txtApellidosAgraviado.getText();
        String telefono_agr = txtTelefonoAgr.getText();
        String descargo_agr = txtDescargoInf.getText();

        if (dni_inf.isEmpty()) {
            AlertHelper.mostrarError("Campos obligatorios");
            return;
        }
        urbanizacionSeleccionado.setSector(sectorSeleccionado);

        Incidencia incidencia = new Incidencia();
        incidencia.setDescripcion(descripcion);
        incidencia.setDireccion(direccion);
        incidencia.setApoyo_policial(apoyoSeleccionado);
        incidencia.setFecha_hora_incidencia(fecha_hora);
        incidencia.setServicios(servicioSerenazgo);


        incidencia.setDelitos(delitoSeleccionado);
        incidencia.setOcurrencias(ocurrenciaSeleccionado);
        incidencia.setIntervenciones(intervencionSeleccionado);
        incidencia.setUrbanizaciones(urbanizacionSeleccionado);

        Persona persona_inf = new Persona();
        persona_inf.setNombres(nombre_inf);
        persona_inf.setApellidos(apellido_inf);
        persona_inf.setTelefono(telefono_inf);
        persona_inf.setDocumento(dni_inf);
        Infractor infractor = new Infractor();
        infractor.setPersona(persona_inf);
        infractor.setObservaciones(descargo_inf);
        incidencia.setInfractores(infractor);

        Persona persona_agr = new Persona();
        persona_agr.setNombres(nombre_agr);
        persona_agr.setApellidos(apellido_agr);
        persona_agr.setTelefono(telefono_agr);
        persona_agr.setDocumento(dni_agr);
        Agraviado agr = new Agraviado();
        agr.setPersona(persona_agr);
        agr.setObservaciones(descargo_agr);
        incidencia.setAgraviados(agr);
        incidencia.setUser(usuarioLogueado);

        registrarEntidad(
                incidencia,
                incidenciaService::save,
                incidenciasController::cargarIncidencias
        );
    }

    private void actualizarIncidencia() {
        Usuario usuarioLogueado = SesionUsuario.getUsuarioActual();
        int id_incidencia = Integer.parseInt(idIncidencia.getText());
        int id_infractor = Integer.parseInt(idInfractor.getText());
        int id_agraviado = Integer.parseInt(idAgraviado.getText());

        Sector sectorSeleccionado = cbSector.getValue();
        ServicioSerenazgo servicioSerenazgo = cbServicio.getValue();
        Urbanizacion urbanizacionSeleccionado = cbUrbanizacion.getValue();
        TipoIntervencion intervencionSeleccionado = cbIntervencion.getValue();
        Delito delitoSeleccionado = cbDelito.getValue();
        TipoOcurrencia ocurrenciaSeleccionado = cbOcurrencia.getValue();
        String descripcion = txtDescripcion.getText();
        String direccion = txtDireccion.getText();
        LocalDate fecha = dpFecha.getValue();
        LocalDateTime fecha_hora = fecha.atTime(LocalTime.now());
        Toggle selectedToggle = groupApoyo.getSelectedToggle();
        RadioButton selectedRadio = (RadioButton) selectedToggle;
        String apoyoSeleccionado = selectedRadio.getText().toUpperCase().replace(" ", "_"); // Transforma el texto

        String dni_inf = txtDniInfractor.getText();
        String nombre_inf = txtNombresInfractor.getText();
        String apellido_inf = txtApellidosInfractor.getText();
        String telefono_inf = txtTelefonoInf.getText();
        String descargo_inf = txtDescargoInf.getText();
        String dni_agr = txtDniAgraviado.getText();
        String nombre_agr = txtNombresAgraviado.getText();
        String apellido_agr = txtApellidosAgraviado.getText();
        String telefono_agr = txtTelefonoInf.getText();
        String descargo_agr = txtDescargoInf.getText();

        if (dni_inf.isEmpty()) {
            AlertHelper.mostrarError("Campos obligatorios");
            return;
        }

        urbanizacionSeleccionado.setSector(sectorSeleccionado);

        Incidencia incidencia = new Incidencia();
        incidencia.setIdIncidencia(id_incidencia);
        incidencia.setDescripcion(descripcion);
        incidencia.setDireccion(direccion);
        incidencia.setApoyo_policial(apoyoSeleccionado);
        incidencia.setFecha_hora_incidencia(fecha_hora);
        incidencia.setServicios(servicioSerenazgo);

        incidencia.setDelitos(delitoSeleccionado);
        incidencia.setOcurrencias(ocurrenciaSeleccionado);
        incidencia.setIntervenciones(intervencionSeleccionado);
        incidencia.setUrbanizaciones(urbanizacionSeleccionado);

        Persona persona_inf = new Persona();
        persona_inf.setNombres(nombre_inf);
        persona_inf.setApellidos(apellido_inf);
        persona_inf.setTelefono(telefono_inf);
        persona_inf.setDocumento(dni_inf);
        Infractor infractor = new Infractor();
        infractor.setIdInfractor(id_infractor);
        infractor.setPersona(persona_inf);
        infractor.setObservaciones(descargo_inf);
        incidencia.setInfractores(infractor);

        Persona persona_agr = new Persona();
        persona_agr.setNombres(nombre_agr);
        persona_agr.setApellidos(apellido_agr);
        persona_agr.setTelefono(telefono_agr);
        persona_agr.setDocumento(dni_agr);
        Agraviado agr = new Agraviado();
        agr.setIdAgraviado(id_agraviado);
        agr.setPersona(persona_agr);
        agr.setObservaciones(descargo_agr);
        incidencia.setAgraviados(agr);

        incidencia.setUser(usuarioLogueado);

        actualizarEntidad(incidencia, incidenciaService::update, incidenciasController::cargarIncidencias);
    }

    private void inicializarUI() {
        btnAnterior.setDisable(true);
        btnGuardar.setDisable(true);
        btnSiguiente.setDisable(true);
        btnCancelar.setDisable(false);

        groupApoyo = new ToggleGroup();
        rbSiApoyo.setToggleGroup(groupApoyo);
        rbNoApoyo.setToggleGroup(groupApoyo);

        groupAgraviado = new ToggleGroup();
        rbSiAgraviado.setToggleGroup(groupAgraviado);
        rbNoAgraviado.setToggleGroup(groupAgraviado);

        sectionInicial();
    }

    private void sectionInicial() {
        sectionIncidencia.setVisible(true);
        sectionInfractor.setVisible(false);
        sectionAgraviado.setVisible(false);
    }

    private void agregarListeners() {
        agregarListenersIncidencia();
        agregarListenersInfractor();
        agregarListenersAgraviado();
    }

    private void agregarListenersIncidencia() {
        agregarListenersGenerico(cbSector.valueProperty());
        agregarListenersGenerico(cbUrbanizacion.valueProperty());
        agregarListenersGenerico(cbDelito.valueProperty());
        agregarListenersGenerico(cbIntervencion.valueProperty());
        agregarListenersGenerico(cbOcurrencia.valueProperty());
        agregarListenersGenerico(txtDescripcion.textProperty());
        agregarListenersGenerico(txtDireccion.textProperty());
        agregarListenersGenerico(dpFecha.valueProperty());
        agregarListenersGenerico(groupApoyo.selectedToggleProperty());

        cbSector.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null) {
                cargarUrbanizaciones(newVal);
            }
        });
    }

    private void agregarListenersInfractor() {
        agregarListenersGenerico(txtDniInfractor.textProperty());
        agregarListenersGenerico(txtNombresInfractor.textProperty());
        agregarListenersGenerico(txtTelefonoInf.textProperty());
        agregarListenersGenerico(txtApellidosInfractor.textProperty());
        agregarListenersGenerico(txtDescargoInf.textProperty());
        agregarListenersGenerico(groupAgraviado.selectedToggleProperty());
    }

    private void agregarListenersAgraviado() {
        agregarListenersGenerico(txtDniAgraviado.textProperty());
        agregarListenersGenerico(txtNombresAgraviado.textProperty());
        agregarListenersGenerico(txtTelefonoAgr.textProperty());
        agregarListenersGenerico(txtApellidosAgraviado.textProperty());
        agregarListenersGenerico(txtDescargoAgr.textProperty());
    }

    private void validarCampos() {
        boolean camposLlenos = true;

        if (sectionIncidencia.isVisible()) {
            camposLlenos = validarSeccionIncidencia();
            btnSiguiente.setDisable(!camposLlenos);
            btnGuardar.setDisable(true);
            btnAnterior.setDisable(true);
        } else if (sectionInfractor.isVisible()) {
            btnAnterior.setDisable(false);
            btnSiguiente.setDisable(true);
            camposLlenos = validarSeccionInfractor();
            String valorSeleccionado = (selectedRadioButton != null) ? selectedRadioButton.getText() : "";
            if (camposLlenos) {
                btnSiguiente.setDisable(!"Si".equals(valorSeleccionado));
                btnGuardar.setDisable(!"No".equals(valorSeleccionado));
            } else {
                btnSiguiente.setDisable(true);
                btnGuardar.setDisable(true);
            }
        } else if (sectionAgraviado.isVisible()) {
            btnSiguiente.setDisable(true);
            btnAnterior.setDisable(false);
            camposLlenos = validarSeccionAgraviado();
            btnGuardar.setDisable(!camposLlenos);
        }
    }

    private boolean esTextoValido(Control campo) {
        if (campo instanceof TextField) {
            return !((TextField) campo).getText().trim().isEmpty();
        } else if (campo instanceof TextArea) {
            return !((TextArea) campo).getText().trim().isEmpty();
        }
        return false;
    }

    private <T> boolean esComboBoxValido(ComboBox<T> combo) {
        return combo.getValue() != null;
    }

    private boolean validarSeccionIncidencia() {
        return esComboBoxValido(cbSector) &&
                esComboBoxValido(cbUrbanizacion) &&
                esComboBoxValido(cbDelito) &&
                esComboBoxValido(cbIntervencion) &&
                esComboBoxValido(cbOcurrencia) &&
                esTextoValido(txtDescripcion) &&
                esTextoValido(txtDireccion) &&
                dpFecha.getValue() != null &&
                groupApoyo.getSelectedToggle() != null;
    }

    private boolean validarSeccionInfractor() {
        selectedRadioButton = (RadioButton) groupAgraviado.getSelectedToggle();
        return esTextoValido(txtDniInfractor) &&
                esTextoValido(txtNombresInfractor) &&
                esTextoValido(txtTelefonoInf) &&
                esTextoValido(txtApellidosInfractor) &&
                esTextoValido(txtDescargoInf) &&
                selectedRadioButton != null;
    }

    private boolean validarSeccionAgraviado() {
        return esTextoValido(txtDniAgraviado) &&
                esTextoValido(txtNombresAgraviado) &&
                esTextoValido(txtTelefonoAgr) &&
                esTextoValido(txtApellidosAgraviado) &&
                esTextoValido(txtDescargoAgr);
    }

    private void cargarCampos(Incidencia incidencia) {
        if (incidencia == null) return;

        idIncidencia.setText(String.valueOf(incidencia.getIdIncidencia()));
        txtDireccion.setText(incidencia.getDireccion());
        dpFecha.setValue(incidencia.getFecha_hora_incidencia().toLocalDate());
        cbOcurrencia.setValue(incidencia.getOcurrencias().get(0));
        cbIntervencion.setValue(incidencia.getIntervenciones().get(0));
        cbDelito.setValue(incidencia.getDelitos().get(0));
        cbSector.setValue(incidencia.getUrbanizaciones().get(0).getSector());
        cbUrbanizacion.setValue(incidencia.getUrbanizaciones().get(0));
        cbServicio.setValue(incidencia.getServicios().get(0));
        txtDescripcion.setText(incidencia.getDescripcion());
        seleccionarRadio(incidencia.getApoyo_policial().name(), groupApoyo);

        if (!incidencia.getInfractores().isEmpty()) {
            idInfractor.setText(String.valueOf(incidencia.getInfractores().get(0).getIdInfractor()));
            txtDniInfractor.setText(incidencia.getInfractores().get(0).getPersona().getDocumento());
            txtNombresInfractor.setText(incidencia.getInfractores().get(0).getPersona().getNombres());
            txtApellidosInfractor.setText(incidencia.getInfractores().get(0).getPersona().getApellidos());
            txtDescargoInf.setText(incidencia.getInfractores().get(0).getObservaciones());
            txtTelefonoInf.setText(incidencia.getInfractores().get(0).getPersona().getTelefono());
            seleccionarRadio(incidencia.getAgraviados(), groupAgraviado);
        }

        if (!incidencia.getAgraviados().isEmpty()) {
            idAgraviado.setText(String.valueOf(incidencia.getAgraviados().get(0).getIdAgraviado()));
            txtDniAgraviado.setText(incidencia.getAgraviados().get(0).getPersona().getDocumento());
            txtNombresAgraviado.setText(incidencia.getAgraviados().get(0).getPersona().getNombres());
            txtApellidosAgraviado.setText(incidencia.getAgraviados().get(0).getPersona().getApellidos());
            txtDescargoAgr.setText(incidencia.getAgraviados().get(0).getObservaciones());
            txtTelefonoAgr.setText(incidencia.getAgraviados().get(0).getPersona().getTelefono());
        }
    }

    private void cambiarSeccion(VBox mostrar) {
        sectionIncidencia.setVisible(false);
        sectionInfractor.setVisible(false);
        sectionAgraviado.setVisible(false);

        mostrar.setVisible(true);
    }

    public void cargarCamposIncidencias(Incidencia incidencia) {
        if (incidencia != null) {
            cargarCampos(incidencia);
        }
    }


    private void cargarDetalle() {

    }


    public void setIncidenciaViewController(IncidenciasController incidenciasController) {
        this.incidenciasController = incidenciasController;
    }


    private void cargarSector() {
        cargarDatos(cbSector, sectorService::findAll, Sector::getNombre);
    }

    private void cargarOcurrencias() {
        cargarDatos(cbOcurrencia, ocurrenciaService::findAll, TipoOcurrencia::getNombre);
    }

    private void cargarIntervenciones() {
        cargarDatos(cbIntervencion, intervencionService::findAll, TipoIntervencion::getNombre);
    }

    private void cargarDelitos() {
        cargarDatos(cbDelito, delitoService::findAll, Delito::getNombre);
    }

    private void cargarUrbanizaciones(Sector sector) {
        cargarDatos(cbUrbanizacion, () -> urbService.findBySector(sector.getNombre()), Urbanizacion::getNombre);
    }

    private void cargarServicios() {
        cargarDatos(cbServicio, serviciosService::findAll, ServicioSerenazgo::getNombre);
    }


    private void agregarListenersGenerico(ObservableValue<?> propiedad) {
        propiedad.addListener((obs, oldVal, newVal) -> validarCampos());
    }

    private void cargarCombos() {
        cargarIntervenciones();
        cargarSector();
        cargarDelitos();
        cargarOcurrencias();
        cargarServicios();
    }

    private void seleccionarRadio(String valor, ToggleGroup grupo) {
        for (Toggle toggle : grupo.getToggles()) {
            RadioButton radioButton = (RadioButton) toggle;
            if (radioButton.getText().trim().toLowerCase().equals(valor.trim().toLowerCase())) {
                grupo.selectToggle(radioButton);
                break;
            }
        }
    }

    private void seleccionarRadio(Object obj, ToggleGroup grupo) {
        for (Toggle toggle : grupo.getToggles()) {
            RadioButton radioButton = (RadioButton) toggle;

            if (obj != null) {
                grupo.selectToggle(radioButton);
                break;
            }
        }

        // Si el objeto es null, buscar el RadioButton con texto "No"
        if (obj == null) {
            for (Toggle toggle : grupo.getToggles()) {
                RadioButton radioButton = (RadioButton) toggle;
                if ("No".equals(radioButton.getText())) {
                    grupo.selectToggle(radioButton);
                    break;
                }
            }
        }
    }

}
