package com.catatstok.model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.core.type.TypeReference;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.ArrayList;

public class DataService {
    private static DataService instance;
    private ObservableList<Item> items;
    private ObservableList<StockTransaction> transactions;
    private ObservableList<Category> categories;

    private DataService() {
        items = FXCollections.observableArrayList();
        transactions = FXCollections.observableArrayList();
        categories = FXCollections.observableArrayList();
        
        loadData();
        
        // Dummy data generation removed as per user request

    }

    private void saveData() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            mapper.writeValue(new File("items.json"), new ArrayList<>(items));
            mapper.writeValue(new File("transactions.json"), new ArrayList<>(transactions));
            mapper.writeValue(new File("categories.json"), new ArrayList<>(categories));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadData() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        try {
            File itemsFile = new File("items.json");
            if (itemsFile.exists()) {
                List<Item> loadedItems = mapper.readValue(itemsFile, new TypeReference<List<Item>>(){});
                items.setAll(loadedItems);
            }

            File transactionsFile = new File("transactions.json");
            if (transactionsFile.exists()) {
                List<StockTransaction> loadedTransactions = mapper.readValue(transactionsFile, new TypeReference<List<StockTransaction>>(){});
                transactions.setAll(loadedTransactions);
            }

            File categoriesFile = new File("categories.json");
            if (categoriesFile.exists()) {
                List<Category> loadedCategories = mapper.readValue(categoriesFile, new TypeReference<List<Category>>(){});
                categories.setAll(loadedCategories);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DataService getInstance() {
        if (instance == null) {
            instance = new DataService();
        }
        return instance;
    }

    public ObservableList<Item> getItems() {
        return items;
    }

    public void addItem(Item item) {
        items.add(item);
        saveData();
    }

    public void deleteItem(Item item) {
        items.remove(item);
        saveData();
    }

    public ObservableList<StockTransaction> getTransactions() {
        return transactions;
    }

    public void addTransaction(StockTransaction transaction) {
        // Update item stock FIRST, so listeners on transactions see updated stock
        for (Item item : items) {
            if (item.getSku().equals(transaction.getItemSku())) {
                if ("Stok Masuk".equals(transaction.getType())) {
                    item.setStock(item.getStock() + transaction.getQuantity());
                } else {
                    item.setStock(item.getStock() - transaction.getQuantity());
                }
                break;
            }
        }
        
        transactions.add(0, transaction); // Add to top, triggers listener
        saveData();
    }

    public ObservableList<Category> getCategories() {
        return categories;
    }

    public void addCategory(Category category) {
        categories.add(category);
        saveData();
    }

    public void deleteCategory(Category category) {
        categories.remove(category);
        saveData();
    }

    public String generateNextCategoryId() {
        int maxId = 0;
        for (Category c : categories) {
            String id = c.getId();
            if (id.startsWith("KAT-")) {
                try {
                    int num = Integer.parseInt(id.substring(4));
                    if (num > maxId) {
                        maxId = num;
                    }
                } catch (NumberFormatException e) {
                    // Ignore invalid formats
                }
            }
        }
        return String.format("KAT-%03d", maxId + 1);
    }
}
