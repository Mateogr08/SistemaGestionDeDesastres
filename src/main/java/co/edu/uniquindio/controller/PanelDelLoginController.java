package co.edu.uniquindio.controller;

import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import co.edu.uniquindio.util.SceneSwitcher;
import co.edu.uniquindio.model.AppModel;
import co.edu.uniquindio.model.Usuario;

public class PanelDelLoginController {

    @FXML private TextField txtUsuario;
    @FXML private PasswordField txtPassword;

    @FXML
    public void initialize() {
        // Resetear borde al escribir
        txtUsuario.textProperty().addListener((obs, oldText, newText) -> resetBorders());
        txtPassword.textProperty().addListener((obs, oldText, newText) -> resetBorders());
    }

    @FXML
    public void login() {
        String usuario = txtUsuario.getText().trim();
        String password = txtPassword.getText().trim();

        Usuario u = AppModel.getInstance().getGestorUsuarios().autenticar(usuario, password);
        if (u != null) {
            Stage stage = (Stage) txtUsuario.getScene().getWindow();
            SceneSwitcher.switchTo(stage, "panelDelEstadoGeneral.fxml");
        } else {
            txtUsuario.setStyle("-fx-border-color: red;");
            txtPassword.setStyle("-fx-border-color: red;");
        }
    }

    private void resetBorders() {
        txtUsuario.setStyle("");
        txtPassword.setStyle("");
    }
}
