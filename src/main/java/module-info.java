module com.fahze.demojavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;
    requires javafx.swing;
    requires javafx.graphics;


    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    requires java.sql;
    requires org.xerial.sqlitejdbc;

    requires org.apache.logging.log4j;
    requires org.slf4j;

    opens com.fahze.demojavafx to javafx.fxml;
    exports com.fahze.demojavafx;

    opens com.fahze.demojavafx.db to javafx.fxml;
    exports com.fahze.demojavafx.db;

    // Ajout pour les graphiques
    opens com.fahze.demojavafx to javafx.base;
}