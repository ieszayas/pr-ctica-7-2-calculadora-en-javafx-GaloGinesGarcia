package controlador;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.Stack;

public class Controlador {

    @FXML
    private Button botonUno;
    @FXML
    private Button botonDos;
    @FXML
    private Button botonTres;
    @FXML
    private Button botonCuatro;
    @FXML
    private Button botonCinco;
    @FXML
    private Button botonSeis;
    @FXML
    private Button botonSiete;
    @FXML
    private Button botonOcho;
    @FXML
    private Button botonNueve;
    @FXML
    private Button botonCero;
    @FXML
    private Button botonSumar;
    @FXML
    private Button botonMultiplicar;
    @FXML
    private Button botonDivision;
    @FXML
    private Button botonResultado;
    @FXML
    private Button botonC;
    @FXML
    private Label campoResultado;

    private String expresion = ""; // Para almacenar la expresión
    private Double memoria = null; //Variable para almacenar el resultado en la memoria MC+
    @FXML
    private Button BotonRestar;
    @FXML
    private Button botonPunto;
    @FXML
    private Button botonM;
    @FXML
    private Button botonRevertirSigno;
    @FXML
    private Button botonMC;


    // Metodo que se ejecuta al pulsar el boton
    @FXML
    private void pulsarBotones(javafx.event.ActionEvent event) {
        Button botonPresionado = (Button) event.getSource();
        String textoBoton = botonPresionado.getText();

        switch (textoBoton) {
            case "C":
                expresion = "";
                campoResultado.setText("");
                break;
            case "=":
                calcularResultado();
                break;
            case "MC": // Botón para restar 1 al valor actual
                restarUno();
                break;
            case "M+": // Botón para guardar el valor en memoria
                guardarEnMemoria();
                break;
            case ".": // Botón para agregar punto decimal
                agregarPunto();
                break;
            default: // Para los números y operadores
                expresion += textoBoton;
                campoResultado.setText(expresion);
                break;
        }
    }

    // Metodo que se calcuar para poner el resultado "="
    private void calcularResultado() {
        try {
            double resultado = evaluarExpresion(expresion);
            campoResultado.setText(String.valueOf(resultado)); // Mostrar el resultado
            expresion = String.valueOf(resultado); // Permitir operaciones continuas con el resultado
        } catch (Exception e) {
            campoResultado.setText("Error"); // En caso de error en la expresión
            expresion = ""; // Limpiar la expresión
        }
    }

    // Metodo para evaluar la expresion básica
    private double evaluarExpresion(String expresion) {
        // Usamos ArrayList en lugar de Stack
        ArrayList<Double> operandos = new ArrayList<>();
        ArrayList<Character> operadores = new ArrayList<>();

        int i = 0;
        while (i < expresion.length()) {
            char c = expresion.charAt(i);

            if (Character.isDigit(c)) {
                // Si encontramos un número, lo agregamos a la lista de operandos
                StringBuilder numero = new StringBuilder();
                while (i < expresion.length() && (Character.isDigit(expresion.charAt(i)) || expresion.charAt(i) == '.')) {
                    numero.append(expresion.charAt(i));
                    i++;
                }
                operandos.add(Double.parseDouble(numero.toString()));
            } else if (c == '+' || c == '-' || c == '*' || c == '/') {
                // Si encontramos un operador, lo agregamos a la lista de operadores
                while (!operadores.isEmpty() && tienePrioridad(c, operadores.get(operadores.size() - 1))) {
                    realizarOperacion(operandos, operadores);
                }
                operadores.add(c);
                i++;
            } else {
                i++; // Ignorar caracteres no válidos
            }
        }

        // Realizar las operaciones restantes
        while (!operadores.isEmpty()) {
            realizarOperacion(operandos, operadores);
        }

        return operandos.get(operandos.size() - 1); // Último número es el resultado
    }

    // Metodo para dar prioridad a los operadores.
    private boolean tienePrioridad(char operadorActual, char operadorArrayList) {
        if (operadorArrayList == '*' || operadorArrayList == '/') {
            return true;
        }
        if ((operadorActual == '+' || operadorActual == '-') && (operadorArrayList == '+' || operadorArrayList == '-')) {
            return true;
        }
        return false;
    }

    private void restarUno() {
        if (!campoResultado.getText().isEmpty()) {
            try {
                double valorActual = Double.parseDouble(campoResultado.getText());
                valorActual -= 1;
                campoResultado.setText(String.valueOf(valorActual));
                expresion = String.valueOf(valorActual);
            } catch (NumberFormatException e) {
                campoResultado.setText("Error");
                expresion = "";
            }
        }
    }

    // Método para guardar el valor actual en memoria (M+)
    private void guardarEnMemoria() {
        if (!campoResultado.getText().isEmpty()) {
            try {
                memoria = Double.parseDouble(campoResultado.getText());
            } catch (NumberFormatException e) {
                campoResultado.setText("Error");
            }
        }
    }

    // Método para agregar un punto decimal solo si no hay otro en el número actual
    private void agregarPunto() {
        if (!expresion.isEmpty()) {
            // Verificar si el número actual ya tiene un punto
            String[] partes = expresion.split("[+\\-*/]");
            String ultimoNumero = partes[partes.length - 1];

            if (!ultimoNumero.contains(".")) {
                expresion += ".";
                campoResultado.setText(expresion);
            }
        } else {
            expresion = "0.";
            campoResultado.setText(expresion);
        }
    }

    // Metodo para realizar operaciones.
    private void realizarOperacion(ArrayList<Double> operandos, ArrayList<Character> operadores) {
        double b = operandos.remove(operandos.size() - 1);
        double a = operandos.remove(operandos.size() - 1);
        char operador = operadores.remove(operadores.size() - 1);

        double resultado = 0;
        switch (operador) {
            case '+':
                resultado = a + b;
                break;
            case '-':
                resultado = a - b;
                break;
            case '*':
                resultado = a * b;
                break;
            case '/':
                if (b != 0) {
                    resultado = a / b;
                } else {
                    throw new ArithmeticException("División por cero");
                }
                break;
        }
        operandos.add(resultado);
    }
}
