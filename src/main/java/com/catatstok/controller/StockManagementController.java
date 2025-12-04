package com.catatstok.controller;

import com.catatstok.model.DataService;
import com.catatstok.model.StockTransaction;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import com.catatstok.App;
import java.io.IOException;

import java.time.LocalDateTime;

// Controller untuk halaman Riwayat Transaksi Stok
public class StockManagementController {

    @FXML
    private TableView<StockTransaction> transactionTable;
    @FXML
    private TableColumn<StockTransaction, String> dateColumn;
    @FXML
    private TableColumn<StockTransaction, String> typeColumn;
    @FXML
    private TableColumn<StockTransaction, String> skuColumn;
    @FXML
    private TableColumn<StockTransaction, Integer> qtyColumn;
    @FXML
    private TableColumn<StockTransaction, String> userColumn;

    @FXML
    public void initialize() {
        // Setup kolom tabel
        dateColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getFormattedDate()));
        typeColumn.setCellValueFactory(cellData -> cellData.getValue().typeProperty());
        skuColumn.setCellValueFactory(cellData -> cellData.getValue().itemSkuProperty());
        qtyColumn.setCellValueFactory(cellData -> cellData.getValue().quantityProperty().asObject());
        userColumn.setCellValueFactory(cellData -> cellData.getValue().userProperty());

        // Custom cell factory untuk memberi warna pada jenis transaksi (Masuk/Keluar)
        typeColumn.setCellFactory(column -> new TableCell<StockTransaction, String>() {
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (item == null || empty) {
                    setText(null);
                    setStyle("");
                } else {
                    setText(item);
                    if ("Stok Masuk".equals(item)) {
                        setTextFill(Color.web("#198754")); // Hijau untuk Stok Masuk
                        setStyle("-fx-font-weight: bold; -fx-background-color: #d1e7dd; -fx-background-radius: 15; -fx-padding: 2 10;");
                    } else {
                        setTextFill(Color.web("#dc3545")); // Merah untuk Stok Keluar
                        setStyle("-fx-font-weight: bold; -fx-background-color: #f8d7da; -fx-background-radius: 15; -fx-padding: 2 10;");
                    }
                }
            }
        });

        transactionTable.setItems(DataService.getInstance().getTransactions());
    }

    @FXML
    private void handleInbound() {
        showStockDialog(true);
    }

    @FXML
    private void handleOutbound() {
        showStockDialog(false);
    }

    private void showStockDialog(boolean isInbound) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/StockDialog.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(isInbound ? "Catat Stok Masuk" : "Catat Stok Keluar");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(transactionTable.getScene().getWindow());
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
