package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.GestorEquipos;
import co.edu.uniquindio.model.AppModel;
import co.edu.uniquindio.model.Recurso;
import co.edu.uniquindio.model.Equipo;
import co.edu.uniquindio.model.Ubicacion;
import co.edu.uniquindio.util.SceneSwitcher;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;
import java.util.stream.Collectors;

public class PanelDeAdministracionController {

    @FXML private TableView<Recurso> tablaRecursos;
    @FXML private TableColumn<Recurso, String> colRecurso;
    @FXML private TableColumn<Recurso, Integer> colCantidad;
    @FXML private TableColumn<Recurso, String> colUbicacion;

    @FXML private TableView<Equipo> tablaEquipos;
    @FXML private TableColumn<Equipo, String> colEquipo;
    @FXML private TableColumn<Equipo, String> colIntegrantes;
    @FXML private TableColumn<Equipo, String> colZonaAsignada;

    @FXML private ComboBox<String> comboUbicaciones;

    @FXML private Button btnAsignarRecursos;
    @FXML private Button btnAsignarEquipo;
    @FXML private Button btnVolver;

    @FXML
    public void initialize() {
        // Columnas Recursos
        colRecurso.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colCantidad.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().getCantidadDisponible()).asObject());
        colUbicacion.setCellValueFactory(cell -> new SimpleStringProperty("Global"));

        // Columnas Equipos
        colEquipo.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getNombre()));
        colIntegrantes.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getIntegrantesString()));
        colZonaAsignada.setCellValueFactory(cell -> new SimpleStringProperty(
                cell.getValue().getZonaAsignada() != null ? cell.getValue().getZonaAsignada().getNombre() : "Sin asignar"
        ));

        cargarRecursos();
        cargarEquipos();
        cargarUbicaciones();
    }

    private void cargarRecursos() {
        ObservableList<Recurso> lista = FXCollections.observableArrayList(
                AppModel.getInstance().getGestorRecursos().getInventarioGlobal()
        );
        tablaRecursos.setItems(lista);
    }

    private void cargarEquipos() {
        ObservableList<Equipo> lista = FXCollections.observableArrayList(
                GestorEquipos.getListaEquipos()
        );
        tablaEquipos.setItems(lista);
    }

    private void cargarUbicaciones() {
        List<String> nombres = AppModel.getInstance().getGrafoRutas().getUbicaciones()
                .stream()
                .map(Ubicacion::getNombre)
                .collect(Collectors.toList());
        comboUbicaciones.setItems(FXCollections.observableArrayList(nombres));
    }

    @FXML
    private void onAsignarRecursos() {
        Recurso r = tablaRecursos.getSelectionModel().getSelectedItem();
        if (r == null) {
            mostrarAlert("Seleccione un recurso para asignar.");
            return;
        }
        r.disminuirCantidad(1);
        tablaRecursos.refresh();
        mostrarAlert("Se asignó 1 unidad de " + r.getNombre());
    }

    @FXML
    private void onAsignarEquipo() {
        Equipo equipo = tablaEquipos.getSelectionModel().getSelectedItem();
        String nombreUbicacion = comboUbicaciones.getSelectionModel().getSelectedItem();

        if (equipo == null) {
            mostrarAlert("Seleccione un equipo para asignar.");
            return;
        }
        if (nombreUbicacion == null) {
            mostrarAlert("Seleccione una ubicación para asignar el equipo.");
            return;
        }

        // Buscar la ubicación por nombre
        Ubicacion ubicacion = AppModel.getInstance().getGrafoRutas().getUbicaciones().stream()
                .filter(u -> u.getNombre().equals(nombreUbicacion))
                .findFirst()
                .orElse(null);

        if (ubicacion != null) {
            equipo.setZonaAsignada(ubicacion);
            tablaEquipos.refresh();
            mostrarAlert("Equipo " + equipo.getNombre() + " asignado a la zona " + ubicacion.getNombre());
        } else {
            mostrarAlert("Ubicación no encontrada.");
        }
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneSwitcher.switchTo(stage, "panelDelEstadoGeneral.fxml");
    }

    private void mostrarAlert(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg, ButtonType.OK);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
