module com.mycompany.bibs {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.mycompany.bibs to javafx.fxml;
    exports com.mycompany.bibs;
}
