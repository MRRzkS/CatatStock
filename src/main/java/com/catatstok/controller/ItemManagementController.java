package com.catatstok.controller;

import com.catatstok.model.DataService;
import com.catatstok.model.Item;
import com.catatstok.model.Category;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
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

// Controller untuk halaman Manajemen Barang
public class ItemManagementController {

    @FXML
    private TextField searchField;
    @FXML
    private ComboBox<String> categoryFilter;
    @FXML
    private TableView<Item> itemTable;
    @FXML
    private TableColumn<Item, String> skuColumn;
    @FXML
    private TableColumn<Item, String> nameColumn;
    @FXML
    private TableColumn<Item, String> categoryColumn;
    @FXML
    private TableColumn<Item, String> unitColumn;
    @FXML
    private TableColumn<Item, Integer> stockColumn;
    @FXML
    private TableColumn<Item, Void> actionColumn;

    private FilteredList<Item> filteredData;

    @FXML
    public void initialize() {
        // Menghubungkan kolom tabel dengan property di model Item
        skuColumn.setCellValueFactory(cellData -> cellData.getValue().skuProperty());
        nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
        categoryColumn.setCellValueFactory(cellData -> cellData.getValue().categoryProperty());
        unitColumn.setCellValueFactory(cellData -> cellData.getValue().unitProperty());
        stockColumn.setCellValueFactory(cellData -> cellData.getValue().stockProperty().asObject());

        // Menambahkan tombol Update dan Delete di setiap baris
        actionColumn.setCellFactory(param -> new TableCell<>() {
            private final Button updateBtn = new Button("Update");
            private final Button deleteBtn = new Button("Delete");
            private final HBox pane = new HBox(10, updateBtn, deleteBtn);

            {
                updateBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #4361ee; -fx-cursor: hand; -fx-padding: 0;");
                deleteBtn.setStyle("-fx-background-color: transparent; -fx-text-fill: #dc3545; -fx-cursor: hand; -fx-padding: 0;");
                
                deleteBtn.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    DataService.getInstance().deleteItem(item);
                });
                
                updateBtn.setOnAction(event -> {
                    Item item = getTableView().getItems().get(getIndex());
                    showItemDialog(item);
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

        // Memuat data dengan dukungan filter (FilteredList)
        filteredData = new FilteredList<>(DataService.getInstance().getItems(), p -> true);
        itemTable.setItems(filteredData);

        // Mengisi pilihan filter kategori
        updateCategoryFilter();
        DataService.getInstance().getCategories().addListener((ListChangeListener<Category>) c -> updateCategoryFilter());
        
        // Menambahkan listener untuk filter kategori
        categoryFilter.valueProperty().addListener((observable, oldValue, newValue) -> updatePredicate());

        // Menambahkan listener untuk pencarian (search)
        searchField.textProperty().addListener((observable, oldValue, newValue) -> updatePredicate());
    }

    private void updateCategoryFilter() {
        String currentSelection = categoryFilter.getValue();
        categoryFilter.getItems().clear();
        categoryFilter.getItems().add("Semua Kategori");
        DataService.getInstance().getCategories().forEach(c -> categoryFilter.getItems().add(c.getName()));
        
        if (currentSelection != null && categoryFilter.getItems().contains(currentSelection)) {
            categoryFilter.getSelectionModel().select(currentSelection);
        } else {
            categoryFilter.getSelectionModel().selectFirst();
        }
    }

    // Logic untuk memfilter data berdasarkan pencarian dan kategori
    private void updatePredicate() {
        String searchText = searchField.getText() != null ? searchField.getText().toLowerCase() : "";
        String selectedCategory = categoryFilter.getValue();

        filteredData.setPredicate(item -> {
            boolean matchesSearch = searchText.isEmpty() || 
                                    item.getName().toLowerCase().contains(searchText) || 
                                    item.getSku().toLowerCase().contains(searchText);
            
            boolean matchesCategory = selectedCategory == null || 
                                      selectedCategory.equals("Semua Kategori") || 
                                      item.getCategory().equals(selectedCategory);

            return matchesSearch && matchesCategory;
        });
    }

    @FXML
    private void handleAddItem() {
        showItemDialog(null);
    }

    private void showItemDialog(Item item) {
        try {
            FXMLLoader loader = new FXMLLoader(App.class.getResource("view/ItemDialog.fxml"));
            Parent page = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(item == null ? "Tambah Barang Baru" : "Update Barang");
            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.initOwner(itemTable.getScene().getWindow());
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
}
