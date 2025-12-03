module com.catatstok {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.controlsfx.controls;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires com.fasterxml.jackson.databind;
    requires com.fasterxml.jackson.datatype.jsr310;
    requires com.fasterxml.jackson.core;

    opens com.catatstok.model to com.fasterxml.jackson.databind;

    opens com.catatstok to javafx.fxml;
    opens com.catatstok.controller to javafx.fxml;
    
    exports com.catatstok;
    exports com.catatstok.controller;
}
