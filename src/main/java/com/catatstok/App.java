package com.catatstok;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * JavaFX App
 * 
 * Class utama untuk menjalankan aplikasi CatatStok.
 * Mewarisi class Application dari JavaFX.
 */
public class App extends Application {

    private static Scene scene;

    @Override
    public void start(Stage stage) throws IOException {
        // Mengatur scene awal dengan tampilan Login
        scene = new Scene(loadFXML("view/LoginView"), 1200, 800);
        
        // Memuat file CSS global untuk styling aplikasi
        scene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());
        
        // Mengatur judul window aplikasi
        stage.setTitle("CatatStok - Inventory Management");
        stage.setScene(scene);
        stage.show(); // Menampilkan window
    }

    // Method untuk mengganti tampilan root (berpindah halaman)
    public static void setRoot(String fxml) throws IOException {
        scene.setRoot(loadFXML(fxml));
    }

    // Helper method untuk memuat file FXML
    private static Parent loadFXML(String fxml) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        return fxmlLoader.load();
    }

    // Entry point aplikasi Java
    public static void main(String[] args) {
        launch(); // Menjalankan siklus hidup aplikasi JavaFX
    }
}
