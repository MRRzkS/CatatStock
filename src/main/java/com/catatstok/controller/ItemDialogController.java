package com.catatstok.controller;

import com.catatstok.model.Category;
import com.catatstok.model.DataService;
import com.catatstok.model.Item;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.StringConverter;

// Controller untuk dialog Tambah/Edit Barang
public class ItemDialogController {

    @FXML private Label titleLabel;
    @FXML private TextField skuField;
    @FXML private TextField nameField;
    @FXML private ComboBox<Category> categoryComboBox;
    @FXML private TextField unitField;
    @FXML private TextField stockField;

    private Stage dialogStage;
    private Item item;
    private boolean okClicked = false;

    @FXML
    public void initialize() {
        // Mengisi ComboBox kategori dengan data dari DataService
        categoryComboBox.setItems(DataService.getInstance().getCategories());
        categoryComboBox.setConverter(new StringConverter<Category>() {
            @Override
            public String toString(Category object) {
                return object == null ? null : object.getName();
            }

            @Override
            public Category fromString(String string) {
                return categoryComboBox.getItems().stream().filter(c -> c.getName().equals(string)).findFirst().orElse(null);
            }
        });
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    // Mengisi form jika dalam mode edit
    public void setItem(Item item) {
        this.item = item;

        if (item != null) {
            titleLabel.setText("Update Barang");
            skuField.setText(item.getSku());
            skuField.setDisable(true); // SKU tidak boleh diubah
            nameField.setText(item.getName());
            unitField.setText(item.getUnit());
            stockField.setText(String.valueOf(item.getStock()));
            stockField.setDisable(true); // Stok hanya bisa diubah lewat transaksi
            
            // Memilih kategori yang sesuai
            for (Category c : categoryComboBox.getItems()) {
                if (c.getName().equals(item.getCategory())) {
                    categoryComboBox.getSelectionModel().select(c);
                    break;
                }
            }
        } else {
            titleLabel.setText("Tambah Barang Baru");
            stockField.setText("0");
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    // Menyimpan data barang
    @FXML
    private void handleSave() {
        if (isInputValid()) {
            String sku = skuField.getText();
            String name = nameField.getText();
            String category = categoryComboBox.getValue().getName();
            String unit = unitField.getText();
            int stock = Integer.parseInt(stockField.getText());

            if (item != null) {
                // Update barang yang ada
                item.setName(name);
                item.setCategory(category);
                item.setUnit(unit);
                // Stok tidak diupdate langsung di sini untuk item yang sudah ada
            } else {
                // Buat barang baru
                item = new Item(sku, name, category, unit, stock);
                DataService.getInstance().addItem(item);
            }

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

        if (skuField.getText() == null || skuField.getText().length() == 0) {
            errorMessage += "No valid SKU!\n";
        }
        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid Name!\n";
        }
        if (categoryComboBox.getValue() == null) {
            errorMessage += "No valid Category!\n";
        }
        if (unitField.getText() == null || unitField.getText().length() == 0) {
            errorMessage += "No valid Unit!\n";
        }
        
        if (stockField.getText() == null || stockField.getText().length() == 0) {
             errorMessage += "No valid Stock!\n";
        } else {
            try {
                Integer.parseInt(stockField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "No valid Stock (must be an integer)!\n";
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
