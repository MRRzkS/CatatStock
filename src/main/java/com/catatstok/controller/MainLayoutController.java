package com.catatstok.controller;

import com.catatstok.App;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainLayoutController implements Initializable {

    @FXML
    private BorderPane mainBorderPane;

    @FXML
    private Button btnDashboard;
    @FXML
    private Button btnBarang;
    @FXML
    private Button btnStok;
    @FXML
    private Button btnKategori;
    @FXML
    private Button btnLaporan;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        showDashboard();
    }

    private void loadView(String fxml) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("view/" + fxml + ".fxml"));
            Parent view = fxmlLoader.load();
            mainBorderPane.setCenter(view);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setActiveButton(Button button) {
        btnDashboard.getStyleClass().remove("sidebar-button-selected");
        btnBarang.getStyleClass().remove("sidebar-button-selected");
        btnStok.getStyleClass().remove("sidebar-button-selected");
        btnKategori.getStyleClass().remove("sidebar-button-selected");
        btnLaporan.getStyleClass().remove("sidebar-button-selected");

        button.getStyleClass().add("sidebar-button-selected");
    }

    @FXML
    private void showDashboard() {
        loadView("DashboardView");
        setActiveButton(btnDashboard);
        
        // Try to refresh dashboard if it's the current view
        if (mainBorderPane.getCenter() != null) {
            // Since we reload the view every time, the controller is re-initialized.
            // So updateSummary() in initialize() is called.
            // However, if we wanted to be sure, we could access the controller.
            // But loadView creates a new instance. So initialize() IS called.
            // The issue might be that the DATA itself isn't triggering updates when stock changes.
            // But updateSummary() recalculates everything from DataService.
            // So simply reloading the view SHOULD work.
        }
    }

    @FXML
    private void showBarang() {
        loadView("ItemManagementView");
        setActiveButton(btnBarang);
    }

    @FXML
    private void showStok() {
        loadView("StockManagementView");
        setActiveButton(btnStok);
    }

    @FXML
    private void showKategori() {
        loadView("CategoryManagementView");
        setActiveButton(btnKategori);
    }

    @FXML
    private void showLaporan() {
        loadView("ReportView");
        setActiveButton(btnLaporan);
    }

    @FXML
    private void handleLogout() throws IOException {
        App.setRoot("view/LoginView");
    }
}
