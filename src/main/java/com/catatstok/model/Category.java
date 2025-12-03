package com.catatstok.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class Category {
    private final StringProperty id;
    private final StringProperty name;
    private final StringProperty description;

    public Category() {
        this("", "", "");
    }

    public Category(String id, String name) {
        this(id, name, "");
    }

    public Category(String id, String name, String description) {
        this.id = new SimpleStringProperty(id);
        this.name = new SimpleStringProperty(name);
        this.description = new SimpleStringProperty(description);
    }

    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }
    @JsonIgnore
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
