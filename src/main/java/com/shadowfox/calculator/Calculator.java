package com.shadowfox.calculator;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Calculator {

    // Basic Operations
    public BigDecimal add(BigDecimal a, BigDecimal b) {
        return a.add(b);
    }

    public BigDecimal subtract(BigDecimal a, BigDecimal b) {
        return a.subtract(b);
    }

    public BigDecimal multiply(BigDecimal a, BigDecimal b) {
        return a.multiply(b);
    }

    public BigDecimal divide(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Cannot divide by zero!");
        }
        return a.divide(b, 10, RoundingMode.HALF_UP);
    }

    // Scientific Operations
    public BigDecimal squareRoot(BigDecimal a) {
        if (a.compareTo(BigDecimal.ZERO) < 0) {
            throw new ArithmeticException("Cannot take square root of a negative number!");
        }
        return a.sqrt(new MathContext(10));
    }

    public BigDecimal power(BigDecimal base, int exponent) {
        return base.pow(exponent);
    }

    public BigDecimal modulus(BigDecimal a, BigDecimal b) {
        if (b.compareTo(BigDecimal.ZERO) == 0) {
            throw new ArithmeticException("Cannot mod by zero!");
        }
        return a.remainder(b);
    }

    // Unit Conversions - Temperature
    public BigDecimal celsiusToFahrenheit(BigDecimal celsius) {
        // F = (C × 9/5) + 32
        return celsius.multiply(new BigDecimal("9"))
                .divide(new BigDecimal("5"), 10, RoundingMode.HALF_UP)
                .add(new BigDecimal("32"));
    }

    public BigDecimal fahrenheitToCelsius(BigDecimal fahrenheit) {
        // C = (F - 32) × 5/9
        return fahrenheit.subtract(new BigDecimal("32"))
                .multiply(new BigDecimal("5"))
                .divide(new BigDecimal("9"), 10, RoundingMode.HALF_UP);
    }

    // Unit Conversions - Distance
    public BigDecimal kmToMiles(BigDecimal km) {
        return km.multiply(new BigDecimal("0.621371")).setScale(6, RoundingMode.HALF_UP);
    }

    public BigDecimal milesToKm(BigDecimal miles) {
        return miles.multiply(new BigDecimal("1.609344")).setScale(6, RoundingMode.HALF_UP);
    }

    // Unit Conversions - Currency (static rates for demo)
    public BigDecimal usdToInr(BigDecimal usd) {
        return usd.multiply(new BigDecimal("83.50")).setScale(2, RoundingMode.HALF_UP);
    }

    public BigDecimal inrToUsd(BigDecimal inr) {
        return inr.divide(new BigDecimal("83.50"), 6, RoundingMode.HALF_UP);
    }

    // Format result - removes trailing zeros
    public String formatResult(BigDecimal result) {
        return result.stripTrailingZeros().toPlainString();
    }
}