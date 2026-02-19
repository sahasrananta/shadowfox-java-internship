# ShadowFox Enhanced Calculator
> Beginner Level Task 1 | ShadowFox Java Internship

## Features
- ✅ Basic Operations: Addition, Subtraction, Multiplication, Division
- ✅ Scientific: Square Root, Power, Modulus
- ✅ Unit Conversions: Temperature (°C/°F), Distance (km/miles), Currency (USD/INR)
- ✅ BigDecimal precision (fixes floating-point bugs like 0.1 + 0.2)
- ✅ Full exception handling (divide by zero, invalid input)
- ✅ Console version + Swing GUI version
- ✅ JUnit 5 tests

## Project Structure
```
src/
├── main/java/com/shadowfox/calculator/
│   ├── Calculator.java      # Core logic (BigDecimal)
│   ├── Main.java            # Console application
│   └── CalculatorGUI.java   # Swing GUI application
└── test/java/com/shadowfox/calculator/
    └── CalculatorTest.java  # JUnit 5 tests
pom.xml
```

## How to Run

### Prerequisites
- Java 17+
- Maven 3.6+

### Console Version
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.shadowfox.calculator.Main"
```

### GUI Version
```bash
mvn compile
mvn exec:java -Dexec.mainClass="com.shadowfox.calculator.CalculatorGUI"
```

### Run Tests
```bash
mvn test
```

### Build JAR
```bash
mvn package
java -jar target/shadowfox-calculator.jar
```

## Key Concepts Learned
- `BigDecimal` vs `double` — precision matters in calculations
- Exception handling with `try-catch` for robust input validation
- Event-driven programming with Swing `ActionListener`
- JUnit 5 testing with `assertEquals` and `assertThrows`
- Maven project structure with `pom.xml`