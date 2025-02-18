module com.example.calculadora {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.scripting;


    opens controlador to javafx.fxml;
    exports controlador;
}