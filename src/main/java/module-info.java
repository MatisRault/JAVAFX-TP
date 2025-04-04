module com.fahze.demojavafx {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.web;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires net.synedra.validatorfx;
    requires org.kordamp.ikonli.javafx;
    requires eu.hansolo.tilesfx;

    requires java.sql;
    requires org.xerial.sqlitejdbc;

    opens com.fahze.demojavafx to javafx.fxml;
    exports com.fahze.demojavafx;

    opens com.fahze.demojavafx.db to javafx.fxml;
    exports com.fahze.demojavafx.db;
}