module com.TiLab {
    requires javafx.controls;
    requires javafx.fxml;

    opens com.TiLab to javafx.fxml;
    exports com.TiLab;
}