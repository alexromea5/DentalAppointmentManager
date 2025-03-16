module com.example.demo {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.xerial.sqlitejdbc;
    requires org.slf4j;
    requires javafaker;

    opens com.example.demo to javafx.fxml;
    exports com.example.demo;
}