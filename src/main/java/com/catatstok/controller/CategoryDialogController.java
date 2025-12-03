package com.catatstok.controller;

import com.catatstok.model.Category;
import com.catatstok.model.DataService;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CategoryDialogController {

    @FXML private Label titleLabel;
    @FXML private TextField idField;
    @FXML private TextField nameField;
    @FXML private TextField descriptionField;

    private Stage dialogStage;
    private Category category;
    private boolean okClicked = false;

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }

    public void setCategory(Category category) {
        this.category = category;

        if (category != null) {
            titleLabel.setText("Update Kategori");
            idField.setText(category.getId());
            nameField.setText(category.getName());
            descriptionField.setText(category.getDescription());
        } else {
            titleLabel.setText("Tambah Kategori Baru");
            idField.setText(DataService.getInstance().generateNextCategoryId());
        }
    }

    public boolean isOkClicked() {
        return okClicked;
    }

    @FXML
    private void handleSave() {
        if (isInputValid()) {
            String name = nameField.getText();
            String description = descriptionField.getText();

            if (category != null) {
                category.setName(name);
                category.setDescription(description);
            } else {
                // ID is already generated and displayed in idField
                category = new Category(idField.getText(), name, description);
                DataService.getInstance().addCategory(category);
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

        if (nameField.getText() == null || nameField.getText().length() == 0) {
            errorMessage += "No valid Name!\n";
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
