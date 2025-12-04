package com.catatstok.model;

import javafx.beans.property.*;

// Model class untuk Barang/Item
public class Item {
    // Property menggunakan tipe JavaFX untuk data binding otomatis ke TableView
    private final StringProperty sku;
    private final StringProperty name;
    private final StringProperty category;
    private final StringProperty unit;
    private final IntegerProperty stock;

    public Item() {
        this("", "", "", "", 0);
    }

    // Constructor utama untuk inisialisasi barang baru
    public Item(String sku, String name, String category, String unit, int stock) {
        this.sku = new SimpleStringProperty(sku);
        this.name = new SimpleStringProperty(name);
        this.category = new SimpleStringProperty(category);
        this.unit = new SimpleStringProperty(unit);
        this.stock = new SimpleIntegerProperty(stock);
    }

    // Getter, Setter, dan Property Accessor untuk SKU
    public String getSku() { return sku.get(); }
    public void setSku(String sku) { this.sku.set(sku); }
    public StringProperty skuProperty() { return sku; }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    public StringProperty nameProperty() { return name; }

    public String getCategory() { return category.get(); }
    public void setCategory(String category) { this.category.set(category); }
    public StringProperty categoryProperty() { return category; }

    public String getUnit() { return unit.get(); }
    public void setUnit(String unit) { this.unit.set(unit); }
    public StringProperty unitProperty() { return unit; }

    public int getStock() { return stock.get(); }
    public void setStock(int stock) { this.stock.set(stock); }
    public IntegerProperty stockProperty() { return stock; }
}
