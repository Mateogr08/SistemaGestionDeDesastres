package co.edu.uniquindio;

import javafx.application.Application;
import javafx.stage.Stage;
import co.edu.uniquindio.util.SceneSwitcher;
import co.edu.uniquindio.model.AppModel;

public class Main extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        AppModel.getInstance().cargarDatosPrueba();
        SceneSwitcher.switchTo(stage, "panelDelLogin.fxml");
    }

    public static void main(String[] args) {
        launch(args);
    }
}