package com.shadowfox.calculator;

import java.math.BigDecimal;
import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {

    static Calculator calc = new Calculator();
    static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.println("║   ShadowFox Enhanced Calculator      ║");
        System.out.println("╚══════════════════════════════════════╝");

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = getIntInput("Enter choice: ");

            switch (choice) {
                case 1 -> basicOperations();
                case 2 -> scientificOperations();
                case 3 -> unitConversions();
                case 0 -> {
                    System.out.println("Thank you for using ShadowFox Calculator!");
                    running = false;
                }
                default -> System.out.println("❌ Invalid choice. Try again.\n");
            }
        }
        scanner.close();
    }

    static void printMainMenu() {
        System.out.println("\n========== MAIN MENU ==========");
        System.out.println("[1] Basic Operations (+, -, *, /)");
        System.out.println("[2] Scientific Operations (√, ^, %)");
        System.out.println("[3] Unit Conversions");
        System.out.println("[0] Exit");
        System.out.println("================================");
    }

    static void basicOperations() {
        System.out.println("\n--- Basic Operations ---");
        BigDecimal a = getBigDecimalInput("Enter first number: ");
        System.out.println("[1] Add  [2] Subtract  [3] Multiply  [4] Divide");
        int op = getIntInput("Choose operation: ");

        try {
            BigDecimal result = switch (op) {
                case 1 -> calc.add(a, getBigDecimalInput("Enter second number: "));
                case 2 -> calc.subtract(a, getBigDecimalInput("Enter second number: "));
                case 3 -> calc.multiply(a, getBigDecimalInput("Enter second number: "));
                case 4 -> calc.divide(a, getBigDecimalInput("Enter second number: "));
                default -> throw new IllegalArgumentException("Invalid operation");
            };
            System.out.println("✅ Result: " + calc.formatResult(result));
        } catch (ArithmeticException e) {
            System.out.println("❌ Math Error: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("❌ " + e.getMessage());
        }
    }

    static void scientificOperations() {
        System.out.println("\n--- Scientific Operations ---");
        System.out.println("[1] Square Root (√)");
        System.out.println("[2] Power (base ^ exponent)");
        System.out.println("[3] Modulus (remainder)");
        int op = getIntInput("Choose operation: ");

        try {
            BigDecimal result;
            switch (op) {
                case 1 -> {
                    BigDecimal a = getBigDecimalInput("Enter number: ");
                    result = calc.squareRoot(a);
                    System.out.println("✅ √" + a + " = " + calc.formatResult(result));
                }
                case 2 -> {
                    BigDecimal base = getBigDecimalInput("Enter base: ");
                    int exp = getIntInput("Enter exponent (integer): ");
                    result = calc.power(base, exp);
                    System.out.println("✅ " + base + "^" + exp + " = " + calc.formatResult(result));
                }
                case 3 -> {
                    BigDecimal a = getBigDecimalInput("Enter first number: ");
                    BigDecimal b = getBigDecimalInput("Enter second number: ");
                    result = calc.modulus(a, b);
                    System.out.println("✅ " + a + " % " + b + " = " + calc.formatResult(result));
                }
                default -> System.out.println("❌ Invalid option.");
            }
        } catch (ArithmeticException e) {
            System.out.println("❌ Math Error: " + e.getMessage());
        }
    }

    static void unitConversions() {
        System.out.println("\n--- Unit Conversions ---");
        System.out.println("[1] Celsius → Fahrenheit");
        System.out.println("[2] Fahrenheit → Celsius");
        System.out.println("[3] Km → Miles");
        System.out.println("[4] Miles → Km");
        System.out.println("[5] USD → INR");
        System.out.println("[6] INR → USD");
        int op = getIntInput("Choose conversion: ");

        BigDecimal input = getBigDecimalInput("Enter value: ");
        BigDecimal result;
        String label;

        switch (op) {
            case 1 -> { result = calc.celsiusToFahrenheit(input); label = "°F"; System.out.println("✅ " + input + "°C = " + calc.formatResult(result) + label); }
            case 2 -> { result = calc.fahrenheitToCelsius(input); label = "°C"; System.out.println("✅ " + input + "°F = " + calc.formatResult(result) + label); }
            case 3 -> { result = calc.kmToMiles(input); label = " miles"; System.out.println("✅ " + input + " km = " + calc.formatResult(result) + label); }
            case 4 -> { result = calc.milesToKm(input); label = " km"; System.out.println("✅ " + input + " miles = " + calc.formatResult(result) + label); }
            case 5 -> { result = calc.usdToInr(input); label = " INR"; System.out.println("✅ $" + input + " = ₹" + calc.formatResult(result)); }
            case 6 -> { result = calc.inrToUsd(input); label = " USD"; System.out.println("✅ ₹" + input + " = $" + calc.formatResult(result)); }
            default -> System.out.println("❌ Invalid option.");
        }
    }

    // --- Input Helpers with Error Handling ---

    static BigDecimal getBigDecimalInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                String input = scanner.next();
                return new BigDecimal(input);
            } catch (NumberFormatException e) {
                System.out.println("❌ Invalid number! Please enter a valid number (e.g. 3.14)");
            }
        }
    }

    static int getIntInput(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return scanner.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("❌ Please enter a whole number!");
                scanner.next(); // clear bad input
            }
        }
    }
}