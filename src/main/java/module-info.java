module com.example.ns {
    requires javafx.controls;
    requires javafx.fxml;
    requires lombok;
    requires java.sql;
    requires com.pixelduke.fxskins;
    requires org.jetbrains.annotations;


    opens com.example.ns to javafx.fxml;
    exports com.example.ns;
    exports com.example.ns.controller;
    opens com.example.ns.controller to javafx.fxml;
}