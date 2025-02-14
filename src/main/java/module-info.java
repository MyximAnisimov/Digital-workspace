module org.example.digitaldrawer {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.bootstrapfx.core;
    requires eu.hansolo.tilesfx;

    opens org.example.digitaldrawer to javafx.fxml;
    exports org.example.digitaldrawer;
    exports org.example.digitaldrawer.panels;
    opens org.example.digitaldrawer.panels to javafx.fxml;
    exports org.example.digitaldrawer.controllers;
    opens org.example.digitaldrawer.controllers to javafx.fxml;
}