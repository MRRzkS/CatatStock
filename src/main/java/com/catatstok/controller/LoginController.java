package com.catatstok.controller;

import com.catatstok.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Mock authentication
        if ("admin".equals(username) && "admin".equals(password)) {
            App.setRoot("view/MainLayout");
        } else {
            errorLabel.setText("Username atau password salah!");
            errorLabel.setVisible(true);
        }
    }
}
