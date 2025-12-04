package com.catatstok.controller;

import com.catatstok.App;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import java.io.IOException;

// Controller untuk halaman Login
public class LoginController {

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    // Method yang dipanggil saat tombol Login ditekan
    @FXML
    private void handleLogin() throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        // Mock authentication (Login sederhana tanpa database user)
        if ("admin".equals(username) && "admin".equals(password)) {
            App.setRoot("view/MainLayout"); // Pindah ke halaman utama jika sukses
        } else {
            errorLabel.setText("Username atau password salah!");
            errorLabel.setVisible(true); // Tampilkan pesan error
        }
    }
}
