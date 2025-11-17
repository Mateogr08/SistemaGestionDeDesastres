package co.edu.uniquindio.controller;

import co.edu.uniquindio.model.*;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import co.edu.uniquindio.util.SceneSwitcher;
import java.util.Map;

public class PanelDeEstadisticasController {

    @FXML private Label lblRecursos;
    @FXML private Label lblEvacuaciones;
    @FXML private Label lblEquiposActivos;

    @FXML private BarChart<String, Number> graficoBarras;
    @FXML private PieChart graficoCircular;

    @FXML
    public void initialize() {
        AppModel model = AppModel.getInstance();

        // Métricas principales
        int recursos = model.getGestorRecursos().getInventarioGlobal().stream()
                .mapToInt(Recurso::getCantidadDisponible).sum();
        int evacuaciones = model.getGestorEvacuacion().getZonasPendientes();
        int equipos = (int) model.getGestorRecursos().getInventarioGlobal().stream()
                .filter(r -> r.getTipo() == TipoRecurso.EQUIPO_RESCATE && r.getCantidadDisponible() > 0)
                .count();

        lblRecursos.setText(String.valueOf(recursos));
        lblEvacuaciones.setText(String.valueOf(evacuaciones));
        lblEquiposActivos.setText(String.valueOf(equipos));

        //Gráfico de barras: Recursos distribuidos por zona
        actualizarGraficoBarras(model);

        //Gráfico circular: Porcentaje de evacuaciones completadas
        actualizarGraficoCircular(model);
    }

    private void actualizarGraficoBarras(AppModel model) {
        graficoBarras.getData().clear();

        CategoryAxis xAxis = (CategoryAxis) graficoBarras.getXAxis();
        NumberAxis yAxis = (NumberAxis) graficoBarras.getYAxis();
        xAxis.setLabel("Ubicación");
        yAxis.setLabel("Cantidad de Recursos");

        XYChart.Series<String, Number> serie = new XYChart.Series<>();
        serie.setName("Recursos por Zona");

        Map<Ubicacion, Map<Recurso, Integer>> recursosPorZona = model.getGestorRecursos().getArbolDistribucion().obtenerResumenPorUbicacion();
        for (Ubicacion u : model.getGrafoRutas().getUbicaciones()) {
            int total = model.getGestorRecursos().obtenerRecursosPorUbicacion(u).stream()
                    .mapToInt(Recurso::getCantidadDisponible).sum();
            serie.getData().add(new XYChart.Data<>(u.getNombre(), total));
        }

        graficoBarras.getData().add(serie);
    }

    private void actualizarGraficoCircular(AppModel model) {
        graficoCircular.getData().clear();

        int evacuacionesPendientes = model.getGestorEvacuacion().getZonasPendientes();
        int totalZonas = model.getGrafoRutas().getUbicaciones().size(); // total zonas posibles
        int evacuacionesCompletadas = totalZonas - evacuacionesPendientes;

        PieChart.Data completadas = new PieChart.Data("Completadas", evacuacionesCompletadas);
        PieChart.Data pendientes = new PieChart.Data("Pendientes", evacuacionesPendientes);

        graficoCircular.setData(FXCollections.observableArrayList(completadas, pendientes));
    }

    @FXML
    public void volverAtras() {
        Stage stage = (Stage) lblRecursos.getScene().getWindow();
        SceneSwitcher.switchTo(stage, "panelDelEstadoGeneral.fxml");
    }
}
