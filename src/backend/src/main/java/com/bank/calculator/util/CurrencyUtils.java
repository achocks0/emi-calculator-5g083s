package com.bank.calculator.util;

import java.math.BigDecimal;         // JDK 11
import java.text.NumberFormat;       // JDK 11
import java.util.Currency;           // JDK 11
import java.util.Locale;             // JDK 11
import java.util.Objects;            // JDK 11
import java.util.regex.Pattern;      // JDK 11
import java.util.regex.Matcher;      // JDK 11

import com.bank.calculator.constant.CalculationConstants;    // Internal import
import com.bank.calculator.util.BigDecimalUtils;             // Internal import

/**
 * Utility class providing methods for currency formatting, parsing, and validation in the Compound Interest Calculator application.
 * This class ensures consistent currency representation across the application,
 * handling USD formatting with appropriate symbols and decimal precision.
 */
public final class CurrencyUtils {

    /**
     * Regular expression pattern for validating currency strings.
     * Accepts optional dollar sign followed by digits and optional decimal point with up to 2 decimal places.
     */
    private static final Pattern CURRENCY_PATTERN = Pattern.compile("^\\$?(\\d+(\\.\\d{1,2})?)$");

    /**
     * NumberFormat instance for USD currency formatting.
     */
    private static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(Locale.US);

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CurrencyUtils() {
        throw new AssertionError("CurrencyUtils class should not be instantiated");
    }

    /**
     * Formats a BigDecimal value as a USD currency string with the appropriate symbol and decimal places.
     *
     * @param value The BigDecimal value to format
     * @return The formatted currency string
     * @throws NullPointerException if value is null
     */
    public static String formatAsCurrency(BigDecimal value) {
        Objects.requireNonNull(value, "Value cannot be null");
        BigDecimal roundedValue = BigDecimalUtils.roundForCurrency(value);
        return CURRENCY_FORMAT.format(roundedValue);
    }

    /**
     * Formats a BigDecimal value as a currency string without the currency symbol.
     *
     * @param value The BigDecimal value to format
     * @return The formatted currency string without the currency symbol
     * @throws NullPointerException if value is null
     */
    public static String formatAsCurrencyWithoutSymbol(BigDecimal value) {
        Objects.requireNonNull(value, "Value cannot be null");
        BigDecimal roundedValue = BigDecimalUtils.roundForCurrency(value);
        
        // Format with specified number of decimal places but without the currency symbol
        return roundedValue.setScale(CalculationConstants.CURRENCY_PRECISION, 
                                    CalculationConstants.CURRENCY_MATH_CONTEXT.getRoundingMode())
                           .toPlainString();
    }

    /**
     * Parses a currency string into a BigDecimal value, removing any currency symbols.
     *
     * @param currencyString The currency string to parse
     * @return The parsed BigDecimal value
     * @throws IllegalArgumentException if the string cannot be parsed to a valid BigDecimal
     * @throws NullPointerException if currencyString is null
     */
    public static BigDecimal parseCurrencyValue(String currencyString) {
        if (currencyString == null || currencyString.trim().isEmpty()) {
            throw new IllegalArgumentException("Currency string cannot be null or empty");
        }
        
        // Clean the currency string by removing currency symbol, commas, and whitespace
        String cleanedString = cleanCurrencyString(currencyString);
        
        try {
            BigDecimal value = new BigDecimal(cleanedString);
            return BigDecimalUtils.roundForCurrency(value);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid currency format: " + currencyString, e);
        }
    }

    /**
     * Checks if a string is in a valid currency format.
     *
     * @param currencyString The string to validate
     * @return true if the string is in a valid currency format, false otherwise
     */
    public static boolean isValidCurrencyFormat(String currencyString) {
        if (currencyString == null || currencyString.trim().isEmpty()) {
            return false;
        }
        
        // Clean the currency string
        String cleanedString = cleanCurrencyString(currencyString);
        
        // Match against the currency pattern
        Matcher matcher = CURRENCY_PATTERN.matcher(cleanedString);
        return matcher.matches();
    }

    /**
     * Checks if a currency string represents a positive value.
     *
     * @param currencyString The currency string to check
     * @return true if the currency string represents a positive value, false otherwise
     */
    public static boolean isPositiveCurrencyValue(String currencyString) {
        if (!isValidCurrencyFormat(currencyString)) {
            return false;
        }
        
        try {
            BigDecimal value = parseCurrencyValue(currencyString);
            return BigDecimalUtils.isPositive(value);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    /**
     * Removes currency symbols, commas, and whitespace from a currency string.
     *
     * @param currencyString The currency string to clean
     * @return The cleaned currency string
     */
    private static String cleanCurrencyString(String currencyString) {
        if (currencyString == null || currencyString.trim().isEmpty()) {
            return "";
        }
        
        // Remove currency symbol, commas, and whitespace
        return currencyString.replace(CalculationConstants.CURRENCY_SYMBOL, "")
                             .replace(",", "")
                             .trim();
    }
}