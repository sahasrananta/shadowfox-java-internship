package com.shadowfox.calculator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private Calculator calc;

    @BeforeEach
    void setUp() {
        calc = new Calculator();
    }

    // --- Basic Operations ---
    @Test
    void testAdd() {
        assertEquals(new BigDecimal("5"), calc.add(new BigDecimal("2"), new BigDecimal("3")));
    }

    @Test
    void testAddPrecision() {
        // 0.1 + 0.2 must equal 0.3 exactly (BigDecimal test)
        BigDecimal result = calc.add(new BigDecimal("0.1"), new BigDecimal("0.2"));
        assertEquals(new BigDecimal("0.3"), result);
    }

    @Test
    void testSubtract() {
        assertEquals(new BigDecimal("7"), calc.subtract(new BigDecimal("10"), new BigDecimal("3")));
    }

    @Test
    void testMultiply() {
        assertEquals(new BigDecimal("12"), calc.multiply(new BigDecimal("3"), new BigDecimal("4")));
    }

    @Test
    void testDivide() {
        assertEquals(0, calc.divide(new BigDecimal("10"), new BigDecimal("4"))
                .compareTo(new BigDecimal("2.5")));
    }

    @Test
    void testDivideByZeroThrows() {
        assertThrows(ArithmeticException.class,
                () -> calc.divide(new BigDecimal("10"), BigDecimal.ZERO));
    }

    // --- Scientific Operations ---
    @Test
    void testSquareRoot() {
        assertEquals(0, calc.squareRoot(new BigDecimal("9"))
                .compareTo(new BigDecimal("3")));
    }

    @Test
    void testSquareRootNegativeThrows() {
        assertThrows(ArithmeticException.class,
                () -> calc.squareRoot(new BigDecimal("-1")));
    }

    @Test
    void testPower() {
        assertEquals(new BigDecimal("8"), calc.power(new BigDecimal("2"), 3));
    }

    @Test
    void testModulus() {
        assertEquals(new BigDecimal("1"), calc.modulus(new BigDecimal("10"), new BigDecimal("3")));
    }

    // --- Unit Conversions ---
    @Test
    void testCelsiusToFahrenheit() {
        // 100°C = 212°F
        assertEquals(0, calc.celsiusToFahrenheit(new BigDecimal("100"))
                .compareTo(new BigDecimal("212")));
    }

    @Test
    void testFahrenheitToCelsius() {
        // 32°F = 0°C
        assertEquals(0, calc.fahrenheitToCelsius(new BigDecimal("32"))
                .compareTo(new BigDecimal("0")));
    }

    @Test
    void testKmToMiles() {
        // 1 km ≈ 0.621371 miles
        BigDecimal result = calc.kmToMiles(new BigDecimal("1"));
        assertTrue(result.compareTo(new BigDecimal("0.62")) > 0);
        assertTrue(result.compareTo(new BigDecimal("0.63")) < 0);
    }

    @Test
    void testUsdToInr() {
        BigDecimal result = calc.usdToInr(new BigDecimal("1"));
        assertEquals(0, result.compareTo(new BigDecimal("83.50")));
    }
}