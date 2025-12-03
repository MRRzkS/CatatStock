package com.catatstok.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import com.catatstok.model.DataService;
import com.catatstok.model.Item;
import com.catatstok.model.StockTransaction;
import javafx.collections.ListChangeListener;
import java.time.format.DateTimeFormatter;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import com.catatstok.App;
import java.io.IOException;

public class DashboardController {

    @FXML
    private Label totalItemsLabel;

    @FXML
    private Label totalStockLabel;

    @FXML
    private Label todayTransactionsLabel;

    @FXML
    private ListView<String> recentActivityList;

    @FXML
    private ListView<String> lowStockList;

    @FXML
    public void initialize() {
        updateSummary();
        
        // Listen for changes
        DataService.getInstance().getItems().addListener((ListChangeListener<Item>) c -> updateSummary());
        DataService.getInstance().getTransactions().addListener((ListChangeListener<StockTransaction>) c -> updateSummary());
    }

    private void updateSummary() {
        int totalItems = DataService.getInstance().getItems().size();
        int totalStock = DataService.getInstance().getItems().stream().mapToInt(Item::getStock).sum();
        long todayTransactions = DataService.getInstance().getTransactions().stream()
                .filter(t -> t.getDate().toLocalDate().equals(java.time.LocalDate.now()))
                .count();

        totalItemsLabel.setText(String.valueOf(totalItems));
        totalStockLabel.setText(String.valueOf(totalStock));
        todayTransactionsLabel.setText(String.valueOf(todayTransactions));
        
        // Recent Activity
        recentActivityList.getItems().clear();
        DataService.getInstance().getTransactions().stream().limit(5).forEach(t -> {
            String activity = String.format("[%s] %s %s (%d)", 
                t.getDate().format(DateTimeFormatter.ofPattern("HH:mm")),
                t.getType(), t.getItemSku(), t.getQuantity());
            recentActivityList.getItems().add(activity);
        });

        // Low Stock (< 10)
        lowStockList.getItems().clear();
        DataService.getInstance().getItems().stream()
                .filter(i -> i.getStock() < 10)
                .forEach(i -> {
                    lowStockList.getItems().add(i.getName() + " (Stok: " + i.getStock() + ")");
                });
    }

    @FXML
    private void goToAddItem() {
        showItemDialog(null);
    }

    @FXML
    private void goToInbound() {
        showStockDialog(true);
    }

    @FXML
    private void goToOutbound() {
        showStockDialog(false);
    }

    private void showItemDialog(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/ItemDialog.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(item == null ? "Tambah Barang Baru" : "Update Barang");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(totalItemsLabel.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            ItemDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setItem(item);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void showStockDialog(boolean isInbound) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/StockDialog.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(isInbound ? "Catat Stok Masuk" : "Catat Stok Keluar");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(totalItemsLabel.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            StockDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setType(isInbound);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
