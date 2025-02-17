package com.example.calculadora;

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
    @FXML
    private Button BotonRestar;

    // Metodo que se ejecuta al pulsar el boton
    @FXML
    private void pulsarBotones(javafx.event.ActionEvent event) {
        // Obtener el botón que generó el evento
        Button botonPresionado = (Button) event.getSource();
        String textoBoton = botonPresionado.getText();

        // Si se presiona el botón "C", se limpia la expresión
        if (textoBoton.equals("C")) {
            expresion = "";
            campoResultado.setText("");
        }
        // Si se presiona el botón "=", se calcula el resultado
        else if (textoBoton.equals("=")) {
            calcularResultado();
        }
        // Si es otro botón (número u operador), se agrega a la expresión
        else {
            expresion = expresion + textoBoton;
            campoResultado.setText(expresion);
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
