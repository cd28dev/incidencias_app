package com.cd.incidenciasappfx.controllers;

import com.cd.incidenciasappfx.helper.ModalControllerHelper;
import com.cd.incidenciasappfx.models.*;
import com.cd.incidenciasappfx.service.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

import java.util.List;

public class NuevaIncidenciaController extends ModalControllerHelper<Incidencia> {

    @FXML private RadioButton rbSiApoyo;
    @FXML private RadioButton rbNoApoyo;
    public ToggleGroup groupApoyo;
    public ToggleGroup groupAgraviado;
    @FXML private RadioButton rbSiAgraviado;
    @FXML private RadioButton rbNoAgraviado;
    @FXML private ComboBox<String> cbSector;
    @FXML private ComboBox<String> cbUrbanizacion;
    @FXML private TextArea txtDescripcion;
    @FXML private TextField txtTelefonoInf;
    @FXML private TextField txtTelefonoAgr;
    @FXML private Button btnAnterior;
    @FXML private Button btnSiguiente;
    @FXML private TextField idAgraviado;
    @FXML private TextArea txtDescargoInf;
    @FXML private TextArea txtDescargoAgr;
    @FXML private TextField idInfractor;
    @FXML private TextField idIncidencia;
    @FXML private VBox sectionIncidencia;
    @FXML private VBox sectionInfractor;
    @FXML private VBox sectionAgraviado;
    @FXML private AnchorPane modalPane;
    @FXML private StackPane stackPane;
    @FXML private TextField txtDireccion;
    @FXML private DatePicker dpFecha;
    @FXML private ComboBox<String> cbOcurrencia;
    @FXML private ComboBox<String> cbIntervencion;
    @FXML private ComboBox<String> cbDelito;
    @FXML private TextField txtDniInfractor;
    @FXML private TextField txtNombresInfractor;
    @FXML private TextField txtApellidosInfractor;
    @FXML private CheckBox chkAgraviado;
    @FXML private TextField txtDniAgraviado;
    @FXML private TextField txtNombresAgraviado;
    @FXML private TextField txtApellidosAgraviado;
    @FXML private Button btnGuardar;

    private IIncidenciaService incidenciaService;
    private IncidenciasController incidenciasController;
    private ISectorService sectorService;
    private ITipoOcurrenciaService ocurrenciaService;
    private ITipoIntService intervencionService;
    private IDelitoService delitoService;
    private IUrbService urbService;

    public NuevaIncidenciaController() {}

    @FXML
    public void initialize() {
        incidenciaService = new IncidenciaServiceImpl();
        sectorService = new SectorServiceImpl();
        ocurrenciaService = new TipoOcurrenciaServiceImpl();
        intervencionService = new TipoIntServiceImpl();
        delitoService = new DelitoServiceImpl();
        urbService = new UrbServiceImpl();
        sectionInicial();
        btnInicial();
        cargarSector();
        cargarOcurrencias();
        cargarIntervenciones();
        cargarDelitos();
        addListeners();
    }

    private void btnInicial() {
        btnAnterior.setDisable(true);
        btnGuardar.setDisable(true);
        btnSiguiente.setDisable(true);
        btnSiguiente.setDisable(true);
        btnCancelar.setDisable(false);
        groupApoyo = new ToggleGroup();
        rbSiApoyo.setToggleGroup(groupApoyo);
        rbNoApoyo.setToggleGroup(groupApoyo);
        groupAgraviado = new ToggleGroup();
        rbSiAgraviado.setToggleGroup(groupAgraviado);
        rbNoAgraviado.setToggleGroup(groupAgraviado);
        
    }

    private void sectionInicial(){
        sectionIncidencia.setVisible(true);
        sectionInfractor.setVisible(false);
        sectionAgraviado.setVisible(false);
    }

    private void addListeners() {
        if(sectionIncidencia.isVisible()) {
            cbSector.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            cbUrbanizacion.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            cbDelito.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            cbIntervencion.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            cbOcurrencia.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            txtDescripcion.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            txtDireccion.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            dpFecha.valueProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            groupApoyo.selectedToggleProperty().addListener((obs, oldVal, newVal) -> validarCampos());

            cbSector.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
                if (newValue != null) {
                    cargarUrbanizaciones(newValue);
                }
            });
        }else if(sectionInfractor.isVisible()) {
            txtDniInfractor.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            txtNombresInfractor.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            txtTelefonoInf.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            txtApellidosInfractor.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            txtDescargoInf.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            groupAgraviado.selectedToggleProperty().addListener((obs, oldVal, newVal) -> validarCampos());

        }else if(sectionAgraviado.isVisible()) {
            txtDniAgraviado.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            txtNombresAgraviado.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            txtTelefonoAgr.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            txtApellidosAgraviado.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
            txtDescargoAgr.textProperty().addListener((obs, oldVal, newVal) -> validarCampos());
        }
    }

    private void validarCampos() {
        boolean camposLlenos = true;

        if (sectionIncidencia.isVisible()) {
            lblTitle.setText("Nueva Incidencia");
            camposLlenos = cbSector.getValue() != null &&
                    cbUrbanizacion.getValue() != null &&
                    cbDelito.getValue() != null &&
                    cbIntervencion.getValue() != null &&
                    cbOcurrencia.getValue() != null &&
                    !txtDescripcion.getText().trim().isEmpty() &&
                    !txtDireccion.getText().trim().isEmpty() &&
                    dpFecha.getValue() != null &&
                    groupApoyo.getSelectedToggle() != null;
            btnSiguiente.setDisable(!camposLlenos);
            btnGuardar.setDisable(true);
            btnAnterior.setDisable(true);
        } else if (sectionInfractor.isVisible()) {
            lblTitle.setText("Datos Infractor");
            RadioButton selectedRadioButton = (RadioButton) groupAgraviado.getSelectedToggle();
            btnAnterior.setDisable(false);
            btnSiguiente.setDisable(true);

            camposLlenos = !txtDniInfractor.getText().trim().isEmpty() &&
                    !txtNombresInfractor.getText().trim().isEmpty() &&
                    !txtTelefonoInf.getText().trim().isEmpty() &&
                    !txtApellidosInfractor.getText().trim().isEmpty() &&
                    !txtDescargoInf.getText().trim().isEmpty() &&
                    selectedRadioButton != null;

            String valorSeleccionado = (selectedRadioButton != null) ? selectedRadioButton.getText() : "";

            if (camposLlenos) {
                if ("Sí".equals(valorSeleccionado)) {
                    btnSiguiente.setDisable(false);
                    btnGuardar.setDisable(true);
                } else if ("No".equals(valorSeleccionado)) {
                    btnSiguiente.setDisable(true);
                    btnGuardar.setDisable(false);
                }
            } else {
                btnSiguiente.setDisable(true);
                btnGuardar.setDisable(true);
            }
        }
        else if (sectionAgraviado.isVisible()) {
            lblTitle.setText("Datos Agraviado");
            btnSiguiente.setDisable(true);
            btnAnterior.setDisable(false);
            camposLlenos = !txtDniAgraviado.getText().trim().isEmpty() &&
                    !txtNombresAgraviado.getText().trim().isEmpty() &&
                    !txtTelefonoAgr.getText().trim().isEmpty() &&
                    !txtApellidosAgraviado.getText().trim().isEmpty() &&
                    !txtDescargoAgr.getText().trim().isEmpty();
            btnGuardar.setDisable(!camposLlenos);
        }
    }


    public void cargarCamposIncidencia(Incidencia incidencia) {
        idIncidencia.setText(String.valueOf(incidencia.getIdIncidencia()));
        txtDireccion.setText(incidencia.getDireccion());
        dpFecha.setValue(incidencia.getFecha_hora_incidencia().toLocalDate());
        cbOcurrencia.setValue(incidencia.getOcurrencias().get(0).getNombre());
        cbIntervencion.setValue(incidencia.getIntervenciones().get(0).getNombre());
        cbDelito.setValue(incidencia.getDelitos().get(0).getNombre());
    }

    public void cargarCamposInfractor(Incidencia incidencia) {
        idInfractor.setText(String.valueOf(incidencia.getInfractores().get(0).getIdInfractor()));
        txtDniInfractor.setText(incidencia.getInfractores().get(0).getPersona().getDocumento());
        txtNombresInfractor.setText(incidencia.getInfractores().get(0).getPersona().getNombres());
        txtApellidosInfractor.setText(incidencia.getInfractores().get(0).getPersona().getApellidos());
        txtDescargoInf.setText(incidencia.getInfractores().get(0).getObservaciones());
    }

    public void cargarCamposAgraviado(Incidencia incidencia) {
        idAgraviado.setText(String.valueOf(incidencia.getAgraviados().get(0).getIdAgraviado()));
        txtDniAgraviado.setText(incidencia.getAgraviados().get(0).getPersona().getDocumento());
        txtNombresAgraviado.setText(incidencia.getAgraviados().get(0).getPersona().getNombres());
        txtApellidosAgraviado.setText(incidencia.getAgraviados().get(0).getPersona().getApellidos());
        txtDescargoAgr.setText(incidencia.getAgraviados().get(0).getObservaciones());
    }

    @FXML
    public void close() {
    }

    @FXML
    public void volver() {
        if(sectionInfractor.isVisible() && !sectionAgraviado.isVisible()) {
            sectionIncidencia.setVisible(true);
            sectionInfractor.setVisible(false);
            sectionAgraviado.setVisible(false);
        } else if (sectionAgraviado.isVisible() && !sectionInfractor.isVisible()) {
            sectionInfractor.setVisible(true);
            sectionAgraviado.setVisible(false);
        }
        btnSiguiente.setDisable(false);
        addListeners();
    }

    @FXML
    public void siguiente() {
        if(sectionIncidencia.isVisible() && !sectionInfractor.isVisible() && !sectionAgraviado.isVisible()) {
            sectionIncidencia.setVisible(false);
            sectionInfractor.setVisible(true);
        } else if (sectionInfractor.isVisible() && !sectionIncidencia.isVisible() && !sectionAgraviado.isVisible()) {
            sectionInfractor.setVisible(false);
            sectionAgraviado.setVisible(true);
        }
        btnAnterior.setDisable(false);
        btnSiguiente.setDisable(true);
        addListeners();
    }

    @FXML
    public void save() {
    }

    public void cargarCamposIncidencias(Incidencia incidencia) {
        cargarCamposIncidencia(incidencia);
        cargarCamposInfractor(incidencia);
        if(incidencia.getAgraviados() != null && !incidencia.getAgraviados().isEmpty()) {
            cargarCamposAgraviado(incidencia);
        }
    }

    public void setIncidenciaViewController(IncidenciasController incidenciasController) {
        this.incidenciasController = incidenciasController;
    }

    private void cargarSector() {
        Task<List<Sector>> task = new Task<>() {
            @Override
            protected List<Sector> call() throws Exception {
                return sectorService.findAll();
            }
        };

        task.setOnSucceeded(event -> {
            List<Sector> sectores = task.getValue();
            if (sectores != null) {
                ObservableList<String> listSectores = FXCollections.observableArrayList(
                        sectores.stream().map(Sector::getNombre).toList()
                );
                cbSector.setItems(listSectores);
            }
        });

        new Thread(task).start();
    }

    private void cargarOcurrencias(){
        Task<List<TipoOcurrencia>> task = new Task<List<TipoOcurrencia>>() {
            @Override
            protected List<TipoOcurrencia> call() throws Exception {
                return ocurrenciaService.findAll();
            }
        };

        task.setOnSucceeded(event -> {
            List<TipoOcurrencia> ocurrencias = task.getValue();
            if (ocurrencias != null) {
                ObservableList<String> listOcurrencias = FXCollections.observableArrayList(
                        ocurrencias.stream().map(TipoOcurrencia::getNombre).toList()
                );
                cbOcurrencia.setItems(listOcurrencias);
            }
        });

        new Thread(task).start();
    }

    private void cargarIntervenciones(){
        Task<List<TipoIntervencion>>task = new Task<List<TipoIntervencion>>() {
            @Override
            protected List<TipoIntervencion> call() throws Exception {
                return intervencionService.findAll();
            }
        };

        task.setOnSucceeded(event -> {
            List<TipoIntervencion> intervenciones = task.getValue();
            if (intervenciones != null) {
                ObservableList<String> listIntervenciones = FXCollections.observableArrayList(
                        intervenciones.stream().map(TipoIntervencion::getNombre).toList()
                );
                cbIntervencion.setItems(listIntervenciones);
            }
        });

        new Thread(task).start();
    }

    private void cargarDelitos(){
        Task<List<Delito>>task = new Task<List<Delito>>() {
            @Override
            protected List<Delito> call() throws Exception {
                return delitoService.findAll();
            }
        };

        task.setOnSucceeded(event -> {
            List<Delito> delitos = task.getValue();
            if (delitos != null) {
                ObservableList<String> listDelitos = FXCollections.observableArrayList(
                        delitos.stream().map(Delito::getNombre).toList()
                );
                cbDelito.setItems(listDelitos);
            }
        });

        new Thread(task).start();
    }

    private void cargarUrbanizaciones(String nameSector){
        Task<List<Urbanizacion>>task = new Task<List<Urbanizacion>>() {
            @Override
            protected List<Urbanizacion> call() throws Exception {
                return urbService.findBySector(nameSector);
            }
        };

        task.setOnSucceeded(event -> {
            List<Urbanizacion> urbanizaciones = task.getValue();
            if (urbanizaciones != null) {
                ObservableList<String> listDelitos = FXCollections.observableArrayList(
                        urbanizaciones.stream().map(Urbanizacion::getNombre).toList()
                );
                cbUrbanizacion.setItems(listDelitos);
            }
        });

        new Thread(task).start();
    }
}
