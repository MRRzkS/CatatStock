package com.catatstok.controller;

import com.catatstok.model.Category;
import com.catatstok.model.DataService;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import com.catatstok.App;
import java.io.IOException;

public class CategoryManagementController {

    @FXML
    private TableView<Category> categoryTable;
    @FXML
    private TableColumn<Category, String> idColumn;
    @FXML
    private TableColumn<Category, String> nameColumn;
    @FXML
    private TableColumn<Category, String> descriptionColumn;
    @FXML
    private TableColumn<Category, Void> actionColumn;

    @FXML
    public void initialize() {
        idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        // Action Column (Update/Delete)
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateBtn = new Button("Update");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(10, updateBtn, deleteBtn);

            {
                updateBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #4361ee; -fx-cursor: hand; -fx-padding: 0;");
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #dc3545; -fx-cursor: hand; -fx-padding: 0;");
                
                deleteBtn.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    DataService.getInstance().deleteCategory(category);
                });
                
                updateBtn.setOnAction(event -> {
                    Category category = getTableView().getItems().get(getIndex());
                    showCategoryDialog(category);
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    setGraphic(pane);
                }
            }
        });

        categoryTable.setItems(DataService.getInstance().getCategories());
    }

    @FXML
    private void handleAddCategory() {
        showCategoryDialog(null);
    }

    private void showCategoryDialog(Category category) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/CategoryDialog.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(category == null ? "Tambah Kategori Baru" : "Update Kategori");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(categoryTable.getScene().getWindow());
            Scene scene = new Scene(page);
            dialogStage.setScene(scene);

            CategoryDialogController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setCategory(category);

            dialogStage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
