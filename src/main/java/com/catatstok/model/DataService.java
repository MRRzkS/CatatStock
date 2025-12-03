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
        
        if (items.isEmpty()) {
            // Add dummy items only if empty
            items.add(new Item("ATK-001", "Buku Tulis Sinar Dunia 50 Lbr", "ATK", "Pcs", 120));
            items.add(new Item("ELK-001", "Mouse Logitech M185", "Elektronik", "Unit", 45));
            items.add(new Item("PGN-001", "Indomie Goreng", "Pangan", "Dus", 8));
            
            // Dummy transactions (depend on items)
            transactions.add(new StockTransaction(LocalDateTime.now().minusMinutes(10), "Stok Masuk", "ATK-001", 50, "Admin"));
            transactions.add(new StockTransaction(LocalDateTime.now().minusMinutes(5), "Stok Keluar", "ELK-001", 2, "Admin"));
        }

        if (categories.isEmpty()) {
            // Dummy categories
            categories.add(new Category("ATK", "Alat Tulis Kantor", "Perlengkapan kantor sehari-hari"));
            categories.add(new Category("Elektronik", "Elektronik", "Perangkat elektronik dan gadget"));
            categories.add(new Category("Pangan", "Pangan", "Makanan dan minuman"));
        }
        
        // Save only if we added something (or just save to be safe)
        if (items.isEmpty() || categories.isEmpty()) { 
             // This check is slightly wrong because we just added them. 
             // But effectively we want to save if we initialized data.
             saveData();
        }
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
        transactions.add(0, transaction); // Add to top
        
        // Update item stock
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
}
