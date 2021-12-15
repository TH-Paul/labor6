module labor {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.databind;


    opens map to javafx.fxml;
    exports map;
}