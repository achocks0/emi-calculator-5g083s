package com.bank.calculator.test.unit.ui.formatter;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.function.UnaryOperator;

import javafx.scene.control.TextFormatter;
import javafx.util.StringConverter;
import javafx.scene.control.TextFormatter.Change;

import org.mockito.Mockito;

import com.bank.calculator.ui.formatter.CurrencyFormatter;
import com.bank.calculator.util.CurrencyUtils;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.category.UnitTest;

/**
 * Unit test class for the CurrencyFormatter component that validates its currency 
 * formatting functionality for JavaFX UI components in the Compound Interest Calculator application.
 */
@DisplayName("Currency Formatter Tests")
public class CurrencyFormatterTest implements UnitTest {

    private TextFormatter<BigDecimal> formatter;
    private static final BigDecimal TEST_VALUE = new BigDecimal("1234.56");
    private static final String FORMATTED_TEST_VALUE = "$1,234.56";
    private static final String VALID_INPUT = "1234.56";
    private static final String VALID_INPUT_WITH_SYMBOL = "$1234.56";
    private static final String INVALID_INPUT_LETTERS = "abc";
    private static final String INVALID_INPUT_TOO_MANY_DECIMALS = "123.456";

    /**
     * Initializes test environment before each test
     */
    @BeforeEach
    public void setUp() {
        formatter = CurrencyFormatter.createCurrencyFormatter();
    }

    /**
     * Cleans up test environment after each test
     */
    @AfterEach
    public void tearDown() {
        formatter = null;
    }

    /**
     * Tests that the CurrencyFormatter.createCurrencyFormatter method creates a non-null TextFormatter
     */
    @Test
    @DisplayName("Should create a non-null TextFormatter")
    public void testFormatterCreation() {
        Assertions.assertNotNull(formatter, "Formatter should not be null");
        Assertions.assertTrue(formatter instanceof TextFormatter, "Formatter should be an instance of TextFormatter");
    }

    /**
     * Tests that the formatter's StringConverter correctly converts between BigDecimal and String
     */
    @Test
    @DisplayName("Should correctly convert between BigDecimal and String")
    public void testFormatterConverter() {
        StringConverter<BigDecimal> converter = formatter.getValueConverter();
        
        Assertions.assertNotNull(converter, "Converter should not be null");
        
        // Test toString conversion
        Assertions.assertEquals(FORMATTED_TEST_VALUE, converter.toString(TEST_VALUE), 
                "toString should format BigDecimal with currency symbol and proper formatting");
        
        // Test fromString conversion
        TestUtils.assertBigDecimalEquals(TEST_VALUE, converter.fromString(VALID_INPUT),
                "fromString should parse numeric string to correct BigDecimal");
        
        // Test fromString with currency symbol
        TestUtils.assertBigDecimalEquals(TEST_VALUE, converter.fromString(VALID_INPUT_WITH_SYMBOL),
                "fromString should handle input with currency symbol");
        
        // Test fromString with null
        Assertions.assertNull(converter.fromString(null),
                "fromString should return null for null input");
        
        // Test fromString with empty string
        Assertions.assertNull(converter.fromString(""),
                "fromString should return null for empty string");
    }

    /**
     * Tests that the formatter's filter correctly validates currency input
     */
    @Test
    @DisplayName("Should correctly validate currency input")
    public void testFormatterFilter() {
        UnaryOperator<Change> filter = formatter.getFilter();
        
        // Mock Change objects for testing
        Change validChange = Mockito.mock(Change.class);
        Mockito.when(validChange.getControlNewText()).thenReturn(VALID_INPUT);
        
        Change validChangeWithSymbol = Mockito.mock(Change.class);
        Mockito.when(validChangeWithSymbol.getControlNewText()).thenReturn(VALID_INPUT_WITH_SYMBOL);
        
        Change invalidLettersChange = Mockito.mock(Change.class);
        Mockito.when(invalidLettersChange.getControlNewText()).thenReturn(INVALID_INPUT_LETTERS);
        
        Change invalidDecimalsChange = Mockito.mock(Change.class);
        Mockito.when(invalidDecimalsChange.getControlNewText()).thenReturn(INVALID_INPUT_TOO_MANY_DECIMALS);
        
        // Valid input should be accepted
        Assertions.assertSame(validChange, filter.apply(validChange), 
                "Valid input should be accepted");
        
        // Valid input with symbol should be accepted
        Assertions.assertSame(validChangeWithSymbol, filter.apply(validChangeWithSymbol), 
                "Valid input with currency symbol should be accepted");
        
        // Invalid input with letters should be rejected
        Assertions.assertNull(filter.apply(invalidLettersChange), 
                "Invalid input with letters should be rejected");
        
        // Invalid input with too many decimals should be rejected
        Assertions.assertNull(filter.apply(invalidDecimalsChange), 
                "Invalid input with too many decimals should be rejected");
    }

    /**
     * Tests the integration between the formatter's converter and filter
     */
    @Test
    @DisplayName("Should integrate converter and filter correctly")
    public void testFormatterIntegration() {
        // Test scenario: simulate user entering valid input
        Change validChange = Mockito.mock(Change.class);
        Mockito.when(validChange.getControlNewText()).thenReturn(VALID_INPUT);
        
        // 1. Check if filter accepts the change
        Change result = formatter.getFilter().apply(validChange);
        Assertions.assertSame(validChange, result, "Filter should accept valid input");
        
        // 2. Convert the text to BigDecimal
        BigDecimal convertedValue = formatter.getValueConverter().fromString(VALID_INPUT);
        Assertions.assertNotNull(convertedValue, "Converter should parse valid input");
        TestUtils.assertBigDecimalEquals(TEST_VALUE, convertedValue, "Converted value should match expected amount");
        
        // 3. Format the value back to string
        String formattedValue = formatter.getValueConverter().toString(convertedValue);
        Assertions.assertEquals(FORMATTED_TEST_VALUE, formattedValue, 
                "Formatted value should include currency symbol and formatting");
        
        // Test with invalid input
        Change invalidChange = Mockito.mock(Change.class);
        Mockito.when(invalidChange.getControlNewText()).thenReturn(INVALID_INPUT_LETTERS);
        
        result = formatter.getFilter().apply(invalidChange);
        Assertions.assertNull(result, "Filter should reject invalid input");
    }

    /**
     * Tests that the formatter correctly handles zero values
     */
    @Test
    @DisplayName("Should handle zero values correctly")
    public void testFormatterWithZeroValue() {
        StringConverter<BigDecimal> converter = formatter.getValueConverter();
        
        // Test toString with zero
        Assertions.assertEquals("$0.00", converter.toString(BigDecimal.ZERO), 
                "toString should format zero as $0.00");
        
        // Test fromString with zero
        TestUtils.assertBigDecimalEquals(BigDecimal.ZERO, converter.fromString("0"), 
                "fromString should parse '0' as BigDecimal.ZERO");
        
        // Test fromString with formatted zero
        TestUtils.assertBigDecimalEquals(BigDecimal.ZERO, converter.fromString("$0.00"), 
                "fromString should parse '$0.00' as BigDecimal.ZERO");
    }

    /**
     * Tests that the formatter correctly handles large currency values
     */
    @Test
    @DisplayName("Should handle large currency values correctly")
    public void testFormatterWithLargeValues() {
        BigDecimal largeValue = new BigDecimal("1000000.00");
        String expectedFormattedValue = "$1,000,000.00";
        StringConverter<BigDecimal> converter = formatter.getValueConverter();
        
        // Test toString with large value
        Assertions.assertEquals(expectedFormattedValue, converter.toString(largeValue), 
                "toString should format large values with commas");
        
        // Test fromString with large value (with commas)
        TestUtils.assertBigDecimalEquals(largeValue, converter.fromString("1,000,000.00"), 
                "fromString should parse large values with commas");
        
        // Test fromString with large value (without commas)
        TestUtils.assertBigDecimalEquals(largeValue, converter.fromString("1000000.00"), 
                "fromString should parse large values without commas");
    }

    /**
     * Tests that the formatter's converter handles invalid input gracefully
     */
    @Test
    @DisplayName("Should handle invalid input gracefully in converter")
    public void testFormatterWithInvalidInputHandling() {
        StringConverter<BigDecimal> converter = formatter.getValueConverter();
        
        // Test fromString with invalid input (letters)
        Assertions.assertNull(converter.fromString(INVALID_INPUT_LETTERS),
                "fromString should return null for invalid input with letters");
        
        // Test fromString with invalid decimal format
        Assertions.assertNull(converter.fromString(INVALID_INPUT_TOO_MANY_DECIMALS),
                "fromString should return null for input with too many decimal places");
    }
}