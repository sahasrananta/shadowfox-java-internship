package com.shadowfox.calculator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.math.BigDecimal;

public class CalculatorGUI extends JFrame {

    private JTextField display;
    private Calculator calc = new Calculator();
    private String currentInput = "";
    private String operator = "";
    private BigDecimal firstOperand = null;
    private boolean newInput = true;

    public CalculatorGUI() {
        setTitle("ShadowFox Calculator");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 600);
        setResizable(false);
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(30, 30, 30));
        setLayout(new BorderLayout(10, 10));

        buildUI();
        setVisible(true);
    }

    private void buildUI() {
        // --- Display ---
        display = new JTextField("0");
        display.setFont(new Font("Arial", Font.BOLD, 32));
        display.setHorizontalAlignment(JTextField.RIGHT);
        display.setBackground(new Color(20, 20, 20));
        display.setForeground(Color.WHITE);
        display.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
        display.setEditable(false);
        add(display, BorderLayout.NORTH);

        // --- Button Panel ---
        JPanel panel = new JPanel(new GridLayout(7, 4, 5, 5));
        panel.setBackground(new Color(30, 30, 30));
        panel.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        String[] buttons = {
                "C",     "±",      "%",      "÷",
                "7",     "8",      "9",      "×",
                "4",     "5",      "6",      "−",
                "1",     "2",      "3",      "+",
                "0",     ".",      "x²",     "=",
                "√",     "°C→°F",  "°F→°C",  "km→mi",
                "mi→km", "",       "",       ""
        };

        for (String text : buttons) {
            JButton btn = createButton(text);
            panel.add(btn);
        }

        add(panel, BorderLayout.CENTER);

        // --- Label ---
        JLabel label = new JLabel("ShadowFox | BigDecimal Precision", SwingConstants.CENTER);
        label.setForeground(new Color(100, 100, 100));
        label.setFont(new Font("Arial", Font.PLAIN, 11));
        label.setBorder(BorderFactory.createEmptyBorder(0, 0, 5, 0));
        add(label, BorderLayout.SOUTH);
    }

    private JButton createButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 16));
        btn.setFocusPainted(false);
        btn.setBorder(BorderFactory.createEmptyBorder());
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));

        // Empty buttons — invisible placeholders
        if (text.isEmpty()) {
            btn.setBackground(new Color(30, 30, 30));
            btn.setEnabled(false);
            return btn;
        }

        // Color coding
        Color bg, fg;
        switch (text) {
            case "=" -> { bg = new Color(255, 149, 0); fg = Color.WHITE; }
            case "C" -> { bg = new Color(180, 50, 50); fg = Color.WHITE; }
            case "÷", "×", "−", "+" -> { bg = new Color(80, 80, 80); fg = new Color(255, 149, 0); }
            case "√", "x²", "±", "%" -> { bg = new Color(60, 60, 60); fg = new Color(100, 200, 255); }
            case "°C→°F", "°F→°C", "km→mi", "mi→km" -> { bg = new Color(40, 80, 60); fg = new Color(100, 255, 150); }
            default -> { bg = new Color(50, 50, 50); fg = Color.WHITE; }
        }
        btn.setBackground(bg);
        btn.setForeground(fg);
        btn.setOpaque(true);

        // Hover effect
        btn.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) { btn.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e) { btn.setBackground(bg); }
        });

        btn.addActionListener(e -> handleButton(text));
        return btn;
    }

    private void handleButton(String text) {
        try {
            switch (text) {
                case "C" -> { currentInput = ""; firstOperand = null; operator = ""; display.setText("0"); newInput = true; }
                case "±" -> {
                    BigDecimal val = new BigDecimal(display.getText());
                    display.setText(calc.formatResult(val.negate()));
                }
                case "." -> {
                    if (!currentInput.contains(".")) {
                        currentInput += ".";
                        display.setText(currentInput.isEmpty() ? "0." : currentInput);
                    }
                }
                case "=" -> calculate();
                case "÷", "×", "−", "+" -> {
                    firstOperand = new BigDecimal(display.getText());
                    operator = text;
                    newInput = true;
                }
                case "√" -> {
                    BigDecimal val = new BigDecimal(display.getText());
                    display.setText(calc.formatResult(calc.squareRoot(val)));
                    newInput = true;
                }
                case "x²" -> {
                    BigDecimal val = new BigDecimal(display.getText());
                    display.setText(calc.formatResult(calc.power(val, 2)));
                    newInput = true;
                }
                case "%" -> {
                    BigDecimal val = new BigDecimal(display.getText());
                    display.setText(calc.formatResult(val.divide(new BigDecimal("100"))));
                    newInput = true;
                }
                case "°C→°F" -> {
                    BigDecimal val = new BigDecimal(display.getText());
                    display.setText(calc.formatResult(calc.celsiusToFahrenheit(val)) + " °F");
                    newInput = true;
                }
                case "°F→°C" -> {
                    BigDecimal val = new BigDecimal(display.getText());
                    display.setText(calc.formatResult(calc.fahrenheitToCelsius(val)) + " °C");
                    newInput = true;
                }
                case "km→mi" -> {
                    BigDecimal val = new BigDecimal(display.getText());
                    display.setText(calc.formatResult(calc.kmToMiles(val)) + " mi");
                    newInput = true;
                }
                case "mi→km" -> {
                    BigDecimal val = new BigDecimal(display.getText());
                    display.setText(calc.formatResult(calc.milesToKm(val)) + " km");
                    newInput = true;
                }
                default -> { // digit pressed
                    if (newInput) { currentInput = text; newInput = false; }
                    else currentInput += text;
                    display.setText(currentInput);
                }
            }
        } catch (ArithmeticException ex) {
            display.setText("Error: " + ex.getMessage());
            newInput = true;
        } catch (NumberFormatException ex) {
            display.setText("Error");
            newInput = true;
        }
    }

    private void calculate() {
        if (firstOperand == null || operator.isEmpty()) return;
        try {
            BigDecimal second = new BigDecimal(display.getText());
            BigDecimal result = switch (operator) {
                case "+" -> calc.add(firstOperand, second);
                case "−" -> calc.subtract(firstOperand, second);
                case "×" -> calc.multiply(firstOperand, second);
                case "÷" -> calc.divide(firstOperand, second);
                default -> second;
            };
            display.setText(calc.formatResult(result));
            currentInput = calc.formatResult(result);
            firstOperand = null;
            operator = "";
            newInput = true;
        } catch (ArithmeticException ex) {
            display.setText("Error: " + ex.getMessage());
            newInput = true;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(CalculatorGUI::new);
    }
}