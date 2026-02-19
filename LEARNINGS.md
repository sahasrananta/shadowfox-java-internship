# LEARNINGS.md — ShadowFox Calculator

## What Was the Hardest Bug?
Using `double` initially caused 0.1 + 0.2 to return 0.30000000000000004 instead of 0.3.
This is a classic floating-point precision problem in Java (and most programming languages).

## How Did I Fix It?
Switched all calculations to use `java.math.BigDecimal` instead of `double` or `float`.
BigDecimal stores exact decimal values, so 0.1 + 0.2 = 0.3 exactly.

Also used `RoundingMode.HALF_UP` for division to avoid infinite decimal expansion errors.

## What I Learned
1. **Never use `double` for money or precise calculations** — always use BigDecimal
2. **InputMismatchException** must be caught when reading Scanner input to prevent app crashes
3. **Swing event handling** — buttons use ActionListener via lambda expressions
4. **JUnit 5** — @BeforeEach sets up fresh objects before each test so tests are independent
5. **Maven** — pom.xml manages dependencies and build configuration cleanly