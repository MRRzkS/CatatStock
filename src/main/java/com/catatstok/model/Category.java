package com.catatstok.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

// Model class untuk merepresentasikan Kategori Barang
public class Category {
    // Menggunakan StringProperty agar bisa binding dengan UI JavaFX secara real-time
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty description;

    // Constructor default
    public Category() {
        this("", "", "");
    }

    public Category(String id, String name) {
        this(id, name, "");
    }

    // Constructor lengkap
    public Category(String id, String name, String description) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
    }

    // Getter dan Setter untuk ID
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }
    @JsonIgnore // Mengabaikan property ini saat serialisasi JSON
    public StringProperty idProperty() { return id; }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }
    @JsonIgnore
    public StringProperty nameProperty() { return name; }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }
    @JsonIgnore
    public StringProperty descriptionProperty() { return description; }
}
