package com.bank.calculator.ui.formatter;

import java.math.BigDecimal;             // JDK 11
import java.util.function.UnaryOperator; // JDK 11
import java.util.regex.Pattern;          // JDK 11
import java.util.regex.Matcher;          // JDK 11

import javafx.scene.control.TextFormatter; // JavaFX 11
import javafx.util.StringConverter;        // JavaFX 11

import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.util.CurrencyUtils;

/**
 * Utility class that provides currency formatting capabilities for JavaFX UI components.
 * This class handles real-time formatting and validation of currency inputs in the
 * Compound Interest Calculator application, ensuring proper USD formatting with the
 * dollar sign and two decimal places.
 */
public final class CurrencyFormatter {

    /**
     * Regular expression pattern for validating currency input.
     * Accepts optional dollar sign followed by digits and optional decimal point with up to 2 decimal places.
     */
    private static final Pattern CURRENCY_INPUT_PATTERN = Pattern.compile("^\\$?(\\d*(\\.\\d{0,2})?)$");

    /**
     * Private constructor to prevent instantiation of utility class.
     *
     * @throws IllegalStateException if constructor is called
     */
    private CurrencyFormatter() {
        throw new IllegalStateException("Utility class should not be instantiated");
    }

    /**
     * Creates a TextFormatter for currency input fields that formats and validates input in real-time.
     * The formatter ensures that inputs adhere to proper currency format with dollar sign and
     * up to two decimal places, providing immediate validation feedback during user input.
     *
     * @return A TextFormatter configured for currency input
     */
    public static TextFormatter<BigDecimal> createCurrencyFormatter() {
        StringConverter<BigDecimal> converter = createCurrencyConverter();
        UnaryOperator<TextFormatter.Change> filter = createCurrencyFilter();
        
        return new TextFormatter<>(converter, null, filter);
    }

    /**
     * Creates a filter function that validates currency input in real-time.
     * The filter ensures that only valid currency characters and format are accepted,
     * preventing invalid inputs like multiple decimal points or non-numeric characters.
     *
     * @return A filter function for currency input validation
     */
    private static UnaryOperator<TextFormatter.Change> createCurrencyFilter() {
        return change -> {
            String newText = change.getControlNewText();
            
            // If the new text matches our pattern or is empty, accept the change
            if (newText.isEmpty() || CURRENCY_INPUT_PATTERN.matcher(newText).matches()) {
                return change;
            }
            
            // Otherwise reject the change
            return null;
        };
    }

    /**
     * Creates a StringConverter for converting between String and BigDecimal with currency formatting.
     * The converter handles the transformation between displayed currency strings (with $ symbol)
     * and the underlying BigDecimal values used for calculations.
     *
     * @return A converter for currency values
     */
    private static StringConverter<BigDecimal> createCurrencyConverter() {
        return new StringConverter<BigDecimal>() {
            @Override
            public String toString(BigDecimal value) {
                if (value == null) {
                    return "";
                }
                return CurrencyUtils.formatAsCurrency(value);
            }
            
            @Override
            public BigDecimal fromString(String string) {
                if (string == null || string.trim().isEmpty()) {
                    return null;
                }
                
                try {
                    return CurrencyUtils.parseCurrencyValue(string);
                } catch (IllegalArgumentException e) {
                    // Return null for invalid input
                    return null;
                }
            }
        };
    }
}