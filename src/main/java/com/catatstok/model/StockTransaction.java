package com.catatstok.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// Model class untuk mencatat Transaksi Stok (Masuk/Keluar)
public class StockTransaction {
    private final ObjectProperty<LocalDateTime> date; // Waktu transaksi
    private final StringProperty type; // Jenis: "Masuk" atau "Keluar"
    private final StringProperty itemSku; // SKU barang yang terlibat
    private final IntegerProperty quantity; // Jumlah barang
    private final StringProperty user; // User yang melakukan transaksi

    public StockTransaction() {
        this(LocalDateTime.now(), "", "", 0, "");
    }

    public StockTransaction(LocalDateTime date, String type, String itemSku, int quantity, String user) {
        this.date = new SimpleObjectProperty<>(date);
        this.type = new SimpleStringProperty(type);
        this.itemSku = new SimpleStringProperty(itemSku);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.user = new SimpleStringProperty(user);
    }

    public LocalDateTime getDate() { return date.get(); }
    public ObjectProperty<LocalDateTime> dateProperty() { return date; }
    
    // Helper method untuk menampilkan tanggal dalam format yang mudah dibaca
    public String getFormattedDate() {
        return date.get().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }

    public String getType() { return type.get(); }
    public StringProperty typeProperty() { return type; }

    public String getItemSku() { return itemSku.get(); }
    public StringProperty itemSkuProperty() { return itemSku; }

    public int getQuantity() { return quantity.get(); }
    public IntegerProperty quantityProperty() { return quantity; }

    public String getUser() { return user.get(); }
    public StringProperty userProperty() { return user; }
}
