package com.bank.calculator.constant;

import java.math.BigDecimal; // JDK 11
import java.math.MathContext; // JDK 11
import java.math.RoundingMode; // JDK 11

/**
 * Contains constant values used for compound interest and EMI calculations throughout the application.
 * These constants include mathematical values, conversion factors, and precision settings for financial calculations.
 * <p>
 * The class provides standard values for:
 * - Financial calculations using BigDecimal for precision
 * - Interest rate defaults
 * - Time period conversions
 * - Precision and rounding configurations
 * - Input validation boundaries
 * - Currency formatting
 */
public final class CalculationConstants {

    /**
     * Represents the value zero as a BigDecimal constant.
     */
    public static final BigDecimal ZERO = BigDecimal.ZERO;

    /**
     * Represents the value one as a BigDecimal constant.
     */
    public static final BigDecimal ONE = BigDecimal.ONE;

    /**
     * Represents the value 100 as a BigDecimal constant.
     * Used for percentage conversions in interest rate calculations.
     */
    public static final BigDecimal HUNDRED = new BigDecimal("100");

    /**
     * The default annual interest rate used for calculations.
     * Set to 7.5% as per the banking division's standard rate.
     */
    public static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("7.5");

    /**
     * Number of months in a year.
     * Used for converting years to months in EMI calculations.
     */
    public static final int MONTHS_IN_YEAR = 12;

    /**
     * Monthly compounding frequency.
     * Used in the compound interest formula: A = P(1 + r/n)^(nt)
     * where n is the compounding frequency.
     */
    public static final int MONTHLY_COMPOUNDING = 12;

    /**
     * Precision used for internal financial calculations.
     * Higher precision is used for intermediate calculations to maintain accuracy.
     */
    public static final int CALCULATION_PRECISION = 10;

    /**
     * Precision used for currency display and final results.
     * Standard financial practice is to use 2 decimal places for currency.
     */
    public static final int CURRENCY_PRECISION = 2;

    /**
     * MathContext for internal calculations with high precision.
     * Uses HALF_UP rounding mode which is standard for financial calculations.
     */
    public static final MathContext CALCULATION_MATH_CONTEXT = new MathContext(CALCULATION_PRECISION, RoundingMode.HALF_UP);

    /**
     * MathContext for currency values with 2 decimal places.
     * Uses HALF_UP rounding mode for standard currency rounding.
     */
    public static final MathContext CURRENCY_MATH_CONTEXT = new MathContext(CURRENCY_PRECISION, RoundingMode.HALF_UP);

    /**
     * Minimum loan duration in years allowed for calculations.
     * Used for input validation.
     */
    public static final int MIN_DURATION_YEARS = 1;

    /**
     * Maximum loan duration in years allowed for calculations.
     * Used for input validation and to prevent unreasonably long loan periods.
     */
    public static final int MAX_DURATION_YEARS = 30;

    /**
     * Minimum principal amount allowed for loan calculations.
     * Used for input validation.
     */
    public static final BigDecimal MIN_PRINCIPAL_AMOUNT = new BigDecimal("1000.00");

    /**
     * Maximum principal amount allowed for loan calculations.
     * Used for input validation and to prevent excessively large loan amounts.
     */
    public static final BigDecimal MAX_PRINCIPAL_AMOUNT = new BigDecimal("1000000.00");

    /**
     * Currency symbol for USD used in formatting the monetary values.
     */
    public static final String CURRENCY_SYMBOL = "$";

    /**
     * Private constructor to prevent instantiation of this constants class.
     */
    private CalculationConstants() {
        throw new AssertionError("CalculationConstants class should not be instantiated");
    }
}