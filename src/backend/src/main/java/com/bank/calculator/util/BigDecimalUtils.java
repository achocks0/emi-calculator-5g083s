package com.bank.calculator.util;

import java.math.BigDecimal;    // JDK 11
import java.math.MathContext;   // JDK 11
import java.math.RoundingMode;  // JDK 11
import java.util.Objects;       // JDK 11

import com.bank.calculator.constant.CalculationConstants;   // Internal import

/**
 * Utility class providing helper methods for BigDecimal operations with proper precision handling for financial calculations.
 * This class ensures consistent rounding behavior, precision management, and simplifies common mathematical operations 
 * needed for compound interest and EMI calculations.
 * <p>
 * All methods in this class validate inputs and provide appropriate precision handling for financial calculations.
 * The class leverages predefined math contexts from {@link com.bank.calculator.constant.CalculationConstants} to ensure 
 * consistent rounding behavior across the application.
 */
public final class BigDecimalUtils {

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private BigDecimalUtils() {
        throw new AssertionError("BigDecimalUtils class should not be instantiated");
    }

    /**
     * Rounds a BigDecimal value to the specified scale using HALF_UP rounding mode.
     *
     * @param value The BigDecimal value to round
     * @param scale The number of decimal places to round to
     * @return The rounded BigDecimal value with the specified scale
     * @throws NullPointerException if value is null
     */
    public static BigDecimal round(BigDecimal value, int scale) {
        Objects.requireNonNull(value, "Value cannot be null");
        return value.setScale(scale, RoundingMode.HALF_UP);
    }

    /**
     * Rounds a BigDecimal value for currency display with 2 decimal places using CURRENCY_MATH_CONTEXT.
     *
     * @param value The BigDecimal value to round
     * @return The rounded BigDecimal value with currency precision
     * @throws NullPointerException if value is null
     */
    public static BigDecimal roundForCurrency(BigDecimal value) {
        Objects.requireNonNull(value, "Value cannot be null");
        return value.round(CalculationConstants.CURRENCY_MATH_CONTEXT);
    }

    /**
     * Rounds a BigDecimal value for internal calculations using CALCULATION_MATH_CONTEXT.
     *
     * @param value The BigDecimal value to round
     * @return The rounded BigDecimal value with calculation precision
     * @throws NullPointerException if value is null
     */
    public static BigDecimal roundForCalculation(BigDecimal value) {
        Objects.requireNonNull(value, "Value cannot be null");
        return value.round(CalculationConstants.CALCULATION_MATH_CONTEXT);
    }

    /**
     * Adds two BigDecimal values with proper precision handling for financial calculations.
     *
     * @param augend The first value
     * @param addend The value to add
     * @return The sum of the two values with proper precision
     * @throws NullPointerException if either augend or addend is null
     */
    public static BigDecimal add(BigDecimal augend, BigDecimal addend) {
        Objects.requireNonNull(augend, "Augend cannot be null");
        Objects.requireNonNull(addend, "Addend cannot be null");
        BigDecimal result = augend.add(addend);
        return roundForCalculation(result);
    }

    /**
     * Subtracts one BigDecimal value from another with proper precision handling for financial calculations.
     *
     * @param minuend The value to subtract from
     * @param subtrahend The value to subtract
     * @return The difference between the two values with proper precision
     * @throws NullPointerException if either minuend or subtrahend is null
     */
    public static BigDecimal subtract(BigDecimal minuend, BigDecimal subtrahend) {
        Objects.requireNonNull(minuend, "Minuend cannot be null");
        Objects.requireNonNull(subtrahend, "Subtrahend cannot be null");
        BigDecimal result = minuend.subtract(subtrahend);
        return roundForCalculation(result);
    }

    /**
     * Multiplies two BigDecimal values with proper precision handling for financial calculations.
     *
     * @param multiplicand The first value
     * @param multiplier The value to multiply by
     * @return The product of the two values with proper precision
     * @throws NullPointerException if either multiplicand or multiplier is null
     */
    public static BigDecimal multiply(BigDecimal multiplicand, BigDecimal multiplier) {
        Objects.requireNonNull(multiplicand, "Multiplicand cannot be null");
        Objects.requireNonNull(multiplier, "Multiplier cannot be null");
        BigDecimal result = multiplicand.multiply(multiplier);
        return roundForCalculation(result);
    }

    /**
     * Multiplies a BigDecimal value by an integer with proper precision handling for financial calculations.
     *
     * @param multiplicand The BigDecimal value
     * @param multiplier The integer multiplier
     * @return The product of the BigDecimal and integer with proper precision
     * @throws NullPointerException if multiplicand is null
     */
    public static BigDecimal multiplyByInt(BigDecimal multiplicand, int multiplier) {
        Objects.requireNonNull(multiplicand, "Multiplicand cannot be null");
        BigDecimal result = multiplicand.multiply(new BigDecimal(multiplier));
        return roundForCalculation(result);
    }

    /**
     * Divides one BigDecimal value by another with proper precision handling for financial calculations.
     *
     * @param dividend The value to be divided
     * @param divisor The value to divide by
     * @return The quotient of the division with proper precision
     * @throws NullPointerException if either dividend or divisor is null
     * @throws ArithmeticException if divisor is zero
     */
    public static BigDecimal divide(BigDecimal dividend, BigDecimal divisor) {
        Objects.requireNonNull(dividend, "Dividend cannot be null");
        Objects.requireNonNull(divisor, "Divisor cannot be null");
        if (isZero(divisor)) {
            throw new ArithmeticException("Division by zero");
        }
        return dividend.divide(divisor, CalculationConstants.CALCULATION_MATH_CONTEXT);
    }

    /**
     * Divides a BigDecimal value by an integer with proper precision handling for financial calculations.
     *
     * @param dividend The BigDecimal value to be divided
     * @param divisor The integer divisor
     * @return The quotient of the division with proper precision
     * @throws NullPointerException if dividend is null
     * @throws ArithmeticException if divisor is zero
     */
    public static BigDecimal divideByInt(BigDecimal dividend, int divisor) {
        Objects.requireNonNull(dividend, "Dividend cannot be null");
        if (divisor == 0) {
            throw new ArithmeticException("Division by zero");
        }
        return dividend.divide(new BigDecimal(divisor), CalculationConstants.CALCULATION_MATH_CONTEXT);
    }

    /**
     * Raises a BigDecimal value to an integer power with proper precision handling for financial calculations.
     *
     * @param base The base value
     * @param exponent The exponent to raise the base to
     * @return The result of raising base to the power of exponent with proper precision
     * @throws NullPointerException if base is null
     */
    public static BigDecimal pow(BigDecimal base, int exponent) {
        Objects.requireNonNull(base, "Base cannot be null");
        
        // Handle special cases for efficiency
        if (exponent == 0) {
            return CalculationConstants.ONE;
        }
        if (exponent == 1) {
            return base;
        }
        
        // Handle negative exponent: 1 / (base^|exponent|)
        if (exponent < 0) {
            return divide(CalculationConstants.ONE, pow(base, -exponent));
        }
        
        // For small positive exponents, use repeated multiplication
        // For larger exponents, use BigDecimal.pow
        BigDecimal result;
        if (exponent <= 10) {
            result = base;
            for (int i = 1; i < exponent; i++) {
                result = multiply(result, base);
            }
        } else {
            result = base.pow(exponent, CalculationConstants.CALCULATION_MATH_CONTEXT);
        }
        
        return roundForCalculation(result);
    }

    /**
     * Converts a percentage value to its decimal equivalent (e.g., 7.5% to 0.075).
     *
     * @param percentage The percentage value to convert
     * @return The decimal equivalent of the percentage value
     * @throws NullPointerException if percentage is null
     */
    public static BigDecimal percentageToDecimal(BigDecimal percentage) {
        Objects.requireNonNull(percentage, "Percentage cannot be null");
        BigDecimal result = divide(percentage, CalculationConstants.HUNDRED);
        return roundForCalculation(result);
    }

    /**
     * Converts a decimal value to its percentage equivalent (e.g., 0.075 to 7.5).
     *
     * @param decimal The decimal value to convert
     * @return The percentage equivalent of the decimal value
     * @throws NullPointerException if decimal is null
     */
    public static BigDecimal decimalToPercentage(BigDecimal decimal) {
        Objects.requireNonNull(decimal, "Decimal cannot be null");
        BigDecimal result = multiply(decimal, CalculationConstants.HUNDRED);
        return roundForCurrency(result);
    }

    /**
     * Checks if a BigDecimal value is equal to zero.
     *
     * @param value The value to check
     * @return true if the value is zero, false otherwise
     * @throws NullPointerException if value is null
     */
    public static boolean isZero(BigDecimal value) {
        Objects.requireNonNull(value, "Value cannot be null");
        return value.compareTo(CalculationConstants.ZERO) == 0;
    }

    /**
     * Checks if a BigDecimal value is greater than zero.
     *
     * @param value The value to check
     * @return true if the value is positive, false otherwise
     * @throws NullPointerException if value is null
     */
    public static boolean isPositive(BigDecimal value) {
        Objects.requireNonNull(value, "Value cannot be null");
        return value.compareTo(CalculationConstants.ZERO) > 0;
    }

    /**
     * Checks if a BigDecimal value is less than zero.
     *
     * @param value The value to check
     * @return true if the value is negative, false otherwise
     * @throws NullPointerException if value is null
     */
    public static boolean isNegative(BigDecimal value) {
        Objects.requireNonNull(value, "Value cannot be null");
        return value.compareTo(CalculationConstants.ZERO) < 0;
    }

    /**
     * Checks if one BigDecimal value is greater than another.
     *
     * @param value1 The first value to compare
     * @param value2 The second value to compare
     * @return true if value1 is greater than value2, false otherwise
     * @throws NullPointerException if either value1 or value2 is null
     */
    public static boolean isGreaterThan(BigDecimal value1, BigDecimal value2) {
        Objects.requireNonNull(value1, "First value cannot be null");
        Objects.requireNonNull(value2, "Second value cannot be null");
        return value1.compareTo(value2) > 0;
    }

    /**
     * Checks if one BigDecimal value is less than another.
     *
     * @param value1 The first value to compare
     * @param value2 The second value to compare
     * @return true if value1 is less than value2, false otherwise
     * @throws NullPointerException if either value1 or value2 is null
     */
    public static boolean isLessThan(BigDecimal value1, BigDecimal value2) {
        Objects.requireNonNull(value1, "First value cannot be null");
        Objects.requireNonNull(value2, "Second value cannot be null");
        return value1.compareTo(value2) < 0;
    }

    /**
     * Checks if two BigDecimal values are equal.
     *
     * @param value1 The first value to compare
     * @param value2 The second value to compare
     * @return true if value1 is equal to value2, false otherwise
     * @throws NullPointerException if either value1 or value2 is null
     */
    public static boolean isEqual(BigDecimal value1, BigDecimal value2) {
        Objects.requireNonNull(value1, "First value cannot be null");
        Objects.requireNonNull(value2, "Second value cannot be null");
        return value1.compareTo(value2) == 0;
    }
}