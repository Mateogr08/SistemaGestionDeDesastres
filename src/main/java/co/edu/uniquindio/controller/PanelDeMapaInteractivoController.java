package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.*;
import co.edu.uniquindio.util.SceneSwitcher;
import javafx.application.Platform;
import javafx.concurrent.Worker;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.Stage;
import java.util.List;
import java.util.stream.Collectors;

public class PanelDeMapaInteractivoController {

    @FXML
    private WebView webView;

    @FXML
    private ComboBox<String> cbOrigen, cbDestino;

    @FXML
    private Button btnCamino, btnCentrar, btnVolver;

    @FXML
    private TextField txtBuscar;

    private WebEngine webEngine;
    private AppModel appModel;

    @FXML
    public void initialize() {
        appModel = AppModel.getInstance();
        webEngine = webView.getEngine();
        webEngine.load(getClass().getResource("/html/mapa.html").toExternalForm());

        // Esperar a que cargue el HTML
        webEngine.getLoadWorker().stateProperty().addListener((obs, oldState, newState) -> {
            if (newState == Worker.State.SUCCEEDED) {
                cargarMarcadoresEnMapa();
                poblarComboBoxes();
            }
        });

        btnCamino.setOnAction(e -> mostrarCaminoMasCorto());
        btnCentrar.setOnAction(e -> buscarUbicacion());
    }

    /** Carga marcadores y rutas en el mapa */
    private void cargarMarcadoresEnMapa() {
        List<Ubicacion> ubicaciones = appModel.getGrafoRutas().getUbicaciones();

        for (Ubicacion u : ubicaciones) {
            String color = obtenerColorPorUrgencia(u.getNivelUrgencia());
            String recursos = u.recursosComoString();
            String equipos = appModel.getGestorEquipos().getListaEquipos().stream()
                    .filter(eq -> eq.getZonaAsignada() != null && eq.getZonaAsignada().equals(u))
                    .map(Equipo::getNombre)
                    .collect(Collectors.joining(", "));
            String popup = u.getNombre() + "<br>Tipo: " + u.getTipo() +
                    "<br>Afectados: " + u.getPersonasAfectadas() +
                    "<br>Recursos: " + recursos +
                    "<br>Equipos: " + (equipos.isEmpty() ? "Ninguno" : equipos);

            String js = String.format(
                    "agregarMarcadorAvanzado(%f, %f, '%s', '%s', '%s');",
                    u.getLatitud(), u.getLongitud(), u.getNombre(), popup, color
            );

            Platform.runLater(() -> webEngine.executeScript(js));
        }

        // Dibujar rutas existentes
        for (Ubicacion origen : ubicaciones) {
            for (Ruta ruta : appModel.getGrafoRutas().getAdyacencias().get(origen)) {
                if (ruta.isDisponible()) {
                    String jsRuta = String.format(
                            "agregarRuta(%f, %f, %f, %f);",
                            ruta.getOrigen().getLatitud(), ruta.getOrigen().getLongitud(),
                            ruta.getDestino().getLatitud(), ruta.getDestino().getLongitud()
                    );
                    Platform.runLater(() -> webEngine.executeScript(jsRuta));
                }
            }
        }
    }

    private String obtenerColorPorUrgencia(int nivel) {
        if (nivel >= 7) return "red";
        else if (nivel >= 4) return "orange";
        else return "blue";
    }

    /** Llena ComboBoxes de origen y destino */
    private void poblarComboBoxes() {
        List<Ubicacion> ubicaciones = appModel.getGrafoRutas().getUbicaciones();
        for (Ubicacion u : ubicaciones) {
            cbOrigen.getItems().add(u.getNombre());
            cbDestino.getItems().add(u.getNombre());
        }
    }

    /** Mostrar camino más corto resaltado */
    private void mostrarCaminoMasCorto() {
        String origenNombre = cbOrigen.getValue();
        String destinoNombre = cbDestino.getValue();
        if (origenNombre == null || destinoNombre == null) return;

        Ubicacion origen = appModel.getGrafoRutas().getUbicaciones().stream()
                .filter(u -> u.getNombre().equals(origenNombre))
                .findFirst().orElse(null);
        Ubicacion destino = appModel.getGrafoRutas().getUbicaciones().stream()
                .filter(u -> u.getNombre().equals(destinoNombre))
                .findFirst().orElse(null);

        if (origen == null || destino == null) return;

        List<Ubicacion> camino = appModel.getGrafoRutas().obtenerCaminoMasCorto(origen, destino);
        if (camino.isEmpty()) return;

        Platform.runLater(() -> webEngine.executeScript("limpiarCaminos();"));

        StringBuilder jsArray = new StringBuilder("[");
        for (Ubicacion u : camino) {
            jsArray.append("[").append(u.getLatitud()).append(",").append(u.getLongitud()).append("],");
        }
        jsArray.deleteCharAt(jsArray.length() - 1);
        jsArray.append("]");

        String js = "mostrarCaminoMasCorto(" + jsArray + ", 'red');";
        Platform.runLater(() -> webEngine.executeScript(js));
    }

    /** Búsqueda rápida de ubicación y centrado */
    private void buscarUbicacion() {
        String busqueda = txtBuscar.getText().trim();
        if (busqueda.isEmpty()) return;

        Ubicacion encontrada = appModel.getGrafoRutas().getUbicaciones().stream()
                .filter(u -> u.getNombre().equalsIgnoreCase(busqueda))
                .findFirst().orElse(null);

        if (encontrada != null) {
            String js = String.format("centrarEn(%f, %f);", encontrada.getLatitud(), encontrada.getLongitud());
            Platform.runLater(() -> webEngine.executeScript(js));
        }
    }

    @FXML
    private void onVolver() {
        Stage stage = (Stage) btnVolver.getScene().getWindow();
        SceneSwitcher.switchTo(stage, "panelDelEstadoGeneral.fxml");
    }
}
