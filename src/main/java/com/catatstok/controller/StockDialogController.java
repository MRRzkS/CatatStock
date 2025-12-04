package com.catatstok.controller;

import com.catatstok.model.DataService;
import com.catatstok.model.Item;
import com.catatstok.model.StockTransaction;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDateTime;

// Controller untuk dialog pencatatan Stok Masuk/Keluar
public class StockDialogController {

    @FXML private Label titleLabel;
    @FXML private ComboBox<Item> itemComboBox;
    @FXML private TextField quantityField;
    @FXML private Button saveButton;

    private Stage dialogStage;
    private boolean isInbound;
    private boolean okClicked = false;

    @FXML
    public void initialize() {
        itemComboBox.setItems(DataService.getInstance().getItems());
        itemComboBox.setConverter(new StringConverter<Item>() {
            @Override
            public String toString(Item object) {
                return object == null ? null : object.getSku() + " - " + object.getName();
            }

            @Override
            public Item fromString(String string) {
                return null;
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // Mengatur jenis transaksi (Masuk atau Keluar) dan menyesuaikan tampilan
    public void setType(boolean isInbound) {
        this.isInbound = isInbound;
        if (isInbound) {
            titleLabel.setText("Catat Stok Masuk (Inbound)");
            saveButton.getStyleClass().add("button-success");
            saveButton.getStyleClass().remove("button-danger");
        } else {
            titleLabel.setText("Catat Stok Keluar (Outbound)");
            saveButton.getStyleClass().add("button-danger");
            saveButton.getStyleClass().remove("button-success");
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    // Menyimpan transaksi stok
    @FXML
    private void handleSave() {
        if (isInputValid()) {
            Item selectedItem = itemComboBox.getValue();
            int quantity = Integer.parseInt(quantityField.getText());
            String type = isInbound ? "Stok Masuk" : "Stok Keluar";

            // Membuat object transaksi baru
            StockTransaction transaction = new StockTransaction(
                LocalDateTime.now(),
                type,
                selectedItem.getSku(),
                quantity,
                "Admin"
            );

            // Menambahkan transaksi ke DataService (stok barang akan otomatis terupdate di sana)
            DataService.getInstance().addTransaction(transaction);

            okClicked = true;
            dialogStage.close();
        }
    }

    @FXML
    private void handleCancel() {
        dialogStage.close();
    }

    private boolean isInputValid() {
        String errorMessage = "";

        if (itemComboBox.getValue() == null) {
            errorMessage += "No Item selected!\n";
        }
        
        if (quantityField.getText() == null || quantityField.getText().length() == 0) {
             errorMessage += "No valid Quantity!\n";
        } else {
            try {
                int qty = Integer.parseInt(quantityField.getText());
                if (qty <= 0) {
                    errorMessage += "Quantity must be greater than 0!\n";
                }
            } catch (NumberFormatException e) {
                errorMessage += "No valid Quantity (must be an integer)!\n";
            }
        }

        if (errorMessage.length() == 0) {
            return true;
        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(dialogStage);
            alert.setTitle("Invalid Fields");
            alert.setHeaderText("Please correct invalid fields");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
    }
}
