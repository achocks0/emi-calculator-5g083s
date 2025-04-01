package com.bank.calculator.test.unit.util;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.CsvSource;

import com.bank.calculator.util.CurrencyUtils;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.category.UnitTest;

@DisplayName("Currency Utils Test")
public class CurrencyUtilsTest implements UnitTest {

    private static final BigDecimal TEST_VALUE = new BigDecimal("1234.56");
    private static final String FORMATTED_CURRENCY = "$1,234.56";
    private static final String FORMATTED_CURRENCY_WITHOUT_SYMBOL = "1,234.56";

    @Test
    @DisplayName("Should format BigDecimal as currency with symbol")
    public void testFormatAsCurrency() {
        String formatted = CurrencyUtils.formatAsCurrency(TEST_VALUE);
        Assertions.assertEquals(FORMATTED_CURRENCY, formatted);
    }

    @Test
    @DisplayName("Should format zero as currency with symbol")
    public void testFormatAsCurrencyWithZero() {
        BigDecimal zero = BigDecimal.ZERO;
        String formatted = CurrencyUtils.formatAsCurrency(zero);
        Assertions.assertEquals("$0.00", formatted);
    }

    @Test
    @DisplayName("Should format negative value as currency with symbol")
    public void testFormatAsCurrencyWithNegativeValue() {
        BigDecimal negativeValue = TEST_VALUE.negate();
        String formatted = CurrencyUtils.formatAsCurrency(negativeValue);
        Assertions.assertEquals("-$1,234.56", formatted);
    }

    @Test
    @DisplayName("Should format BigDecimal as currency without symbol")
    public void testFormatAsCurrencyWithoutSymbol() {
        String formatted = CurrencyUtils.formatAsCurrencyWithoutSymbol(TEST_VALUE);
        Assertions.assertEquals(FORMATTED_CURRENCY_WITHOUT_SYMBOL, formatted);
    }

    @Test
    @DisplayName("Should parse currency string to BigDecimal")
    public void testParseCurrencyValue() {
        BigDecimal parsed = CurrencyUtils.parseCurrencyValue(FORMATTED_CURRENCY);
        TestUtils.assertBigDecimalEquals(TEST_VALUE, parsed);
    }

    @Test
    @DisplayName("Should parse currency string without symbol to BigDecimal")
    public void testParseCurrencyValueWithoutSymbol() {
        BigDecimal parsed = CurrencyUtils.parseCurrencyValue(FORMATTED_CURRENCY_WITHOUT_SYMBOL);
        TestUtils.assertBigDecimalEquals(TEST_VALUE, parsed);
    }

    @Test
    @DisplayName("Should parse currency string with commas to BigDecimal")
    public void testParseCurrencyValueWithCommas() {
        String currencyWithCommas = "$1,234.56";
        BigDecimal parsed = CurrencyUtils.parseCurrencyValue(currencyWithCommas);
        TestUtils.assertBigDecimalEquals(TEST_VALUE, parsed);
    }

    @Test
    @DisplayName("Should throw exception when parsing invalid currency format")
    public void testParseCurrencyValueWithInvalidFormat() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CurrencyUtils.parseCurrencyValue("$abc");
        });
    }

    @ParameterizedTest
    @ValueSource(strings = {"$1234.56", "1234.56", "$1,234.56", "1,234.56", "$0.00", "0"})
    @DisplayName("Should return true for valid currency formats")
    public void testIsValidCurrencyFormat(String currencyString) {
        Assertions.assertTrue(CurrencyUtils.isValidCurrencyFormat(currencyString));
    }

    @ParameterizedTest
    @ValueSource(strings = {"", "abc", "$abc", "12.345", "$-123"})
    @DisplayName("Should return false for invalid currency formats")
    public void testIsInvalidCurrencyFormat(String currencyString) {
        Assertions.assertFalse(CurrencyUtils.isValidCurrencyFormat(currencyString));
    }

    @ParameterizedTest
    @ValueSource(strings = {"$1234.56", "1234.56", "$1,234.56", "1,234.56", "$0.01", "0.01"})
    @DisplayName("Should return true for positive currency values")
    public void testIsPositiveCurrencyValue(String currencyString) {
        Assertions.assertTrue(CurrencyUtils.isPositiveCurrencyValue(currencyString));
    }

    @ParameterizedTest
    @ValueSource(strings = {"$0.00", "0", "$0", "abc", ""})
    @DisplayName("Should return false for non-positive currency values")
    public void testIsNotPositiveCurrencyValue(String currencyString) {
        Assertions.assertFalse(CurrencyUtils.isPositiveCurrencyValue(currencyString));
    }

    @Test
    @DisplayName("Should throw NullPointerException for null inputs")
    public void testNullInputHandling() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            CurrencyUtils.formatAsCurrency(null);
        });
        
        Assertions.assertThrows(NullPointerException.class, () -> {
            CurrencyUtils.formatAsCurrencyWithoutSymbol(null);
        });
        
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            CurrencyUtils.parseCurrencyValue(null);
        });
        
        Assertions.assertFalse(CurrencyUtils.isValidCurrencyFormat(null));
        Assertions.assertFalse(CurrencyUtils.isPositiveCurrencyValue(null));
    }

    @Test
    @DisplayName("Should correctly round currency values to 2 decimal places")
    public void testRoundingBehavior() {
        BigDecimal valueWithMoreDecimals = new BigDecimal("1234.5678");
        String formatted = CurrencyUtils.formatAsCurrency(valueWithMoreDecimals);
        Assertions.assertEquals("$1,234.57", formatted); // Should round to 2 decimal places
        
        BigDecimal parsed = CurrencyUtils.parseCurrencyValue(formatted);
        BigDecimal expected = new BigDecimal("1234.57");
        TestUtils.assertBigDecimalEquals(expected, parsed, TestUtils.DELTA);
    }
}