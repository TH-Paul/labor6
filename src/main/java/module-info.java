module map {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires com.fasterxml.jackson.databind;
    requires javafx.graphics;


//    exports map.konsole to javafx.fxml;
//    //exports map to javafx.graphics;
//    exports map.controller;
    exports map;
    opens map.konsole to javafx.fxml;
    opens map.model to javafx.base;
}