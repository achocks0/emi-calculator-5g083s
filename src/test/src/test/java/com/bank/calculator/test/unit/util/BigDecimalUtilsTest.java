package com.bank.calculator.test.unit.util;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.stream.Stream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.test.category.UnitTest;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.util.BigDecimalUtils;

/**
 * Unit tests for the BigDecimalUtils class that verify the correct behavior of utility methods
 * for BigDecimal operations with proper precision handling for financial calculations.
 */
@DisplayName("BigDecimalUtils Unit Tests")
class BigDecimalUtilsTest implements UnitTest {

    private static final BigDecimal TEST_VALUE_1 = new BigDecimal("10.12345");
    private static final BigDecimal TEST_VALUE_2 = new BigDecimal("5.54321");
    private static final BigDecimal NEGATIVE_TEST_VALUE = new BigDecimal("-3.75");
    private static final int TEST_SCALE = 2;
    private static final int TEST_EXPONENT = 3;

    @BeforeEach
    void setUp() {
        // No setup required for these tests
    }

    @AfterEach
    void tearDown() {
        // No teardown required for these tests
    }

    @Test
    @DisplayName("Should round BigDecimal to specified scale")
    void testRound() {
        // Arrange
        BigDecimal expected = new BigDecimal("10.12");
        
        // Act
        BigDecimal result = BigDecimalUtils.round(TEST_VALUE_1, TEST_SCALE);
        
        // Assert
        TestUtils.assertBigDecimalEquals(expected, result);
        assertEquals(TEST_SCALE, result.scale());
        
        // Test with different scale
        expected = new BigDecimal("10.123");
        result = BigDecimalUtils.round(TEST_VALUE_1, 3);
        TestUtils.assertBigDecimalEquals(expected, result);
        assertEquals(3, result.scale());
        
        // Test with zero scale
        expected = new BigDecimal("10");
        result = BigDecimalUtils.round(TEST_VALUE_1, 0);
        TestUtils.assertBigDecimalEquals(expected, result);
        assertEquals(0, result.scale());
        
        // Test with negative value
        expected = new BigDecimal("-3.75");
        result = BigDecimalUtils.round(NEGATIVE_TEST_VALUE, 2);
        TestUtils.assertBigDecimalEquals(expected, result);
        assertEquals(2, result.scale());
        
        // Test with negative scale - should throw exception
        assertThrows(ArithmeticException.class, () -> BigDecimalUtils.round(TEST_VALUE_1, -1));
    }

    @Test
    @DisplayName("Should round BigDecimal for currency display")
    void testRoundForCurrency() {
        // Arrange
        BigDecimal expected = new BigDecimal("10.12");
        
        // Act
        BigDecimal result = BigDecimalUtils.roundForCurrency(TEST_VALUE_1);
        
        // Assert
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test with value that needs rounding
        BigDecimal valueToRound = new BigDecimal("10.126");
        expected = new BigDecimal("10.13");
        result = BigDecimalUtils.roundForCurrency(valueToRound);
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test with negative value
        expected = new BigDecimal("-3.75");
        result = BigDecimalUtils.roundForCurrency(NEGATIVE_TEST_VALUE);
        TestUtils.assertBigDecimalEquals(expected, result);
    }

    @Test
    @DisplayName("Should round BigDecimal for calculation precision")
    void testRoundForCalculation() {
        // Act
        BigDecimal result = BigDecimalUtils.roundForCalculation(TEST_VALUE_1);
        
        // Assert
        TestUtils.assertBigDecimalEquals(TEST_VALUE_1, result);
        
        // Test with higher precision value
        BigDecimal highPrecisionValue = new BigDecimal("10.1234567890123456789");
        BigDecimal expected = new BigDecimal("10.1234567890");
        result = BigDecimalUtils.roundForCalculation(highPrecisionValue);
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test with negative value
        result = BigDecimalUtils.roundForCalculation(NEGATIVE_TEST_VALUE);
        TestUtils.assertBigDecimalEquals(NEGATIVE_TEST_VALUE, result);
    }

    @Test
    @DisplayName("Should add two BigDecimal values with proper precision")
    void testAdd() {
        // Arrange
        BigDecimal expected = new BigDecimal("15.66666");
        
        // Act
        BigDecimal result = BigDecimalUtils.add(TEST_VALUE_1, TEST_VALUE_2);
        
        // Assert
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test adding zero
        result = BigDecimalUtils.add(TEST_VALUE_1, BigDecimal.ZERO);
        TestUtils.assertBigDecimalEquals(TEST_VALUE_1, result);
        
        // Test adding negative value
        expected = new BigDecimal("6.37345");
        result = BigDecimalUtils.add(TEST_VALUE_1, NEGATIVE_TEST_VALUE);
        TestUtils.assertBigDecimalEquals(expected, result);
    }

    @Test
    @DisplayName("Should subtract two BigDecimal values with proper precision")
    void testSubtract() {
        // Arrange
        BigDecimal expected = new BigDecimal("4.58024");
        
        // Act
        BigDecimal result = BigDecimalUtils.subtract(TEST_VALUE_1, TEST_VALUE_2);
        
        // Assert
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test subtracting zero
        result = BigDecimalUtils.subtract(TEST_VALUE_1, BigDecimal.ZERO);
        TestUtils.assertBigDecimalEquals(TEST_VALUE_1, result);
        
        // Test subtracting to get negative result
        expected = new BigDecimal("-4.58024");
        result = BigDecimalUtils.subtract(TEST_VALUE_2, TEST_VALUE_1);
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test subtracting negative value (which is adding)
        expected = new BigDecimal("13.87345");
        result = BigDecimalUtils.subtract(TEST_VALUE_1, NEGATIVE_TEST_VALUE);
        TestUtils.assertBigDecimalEquals(expected, result);
    }

    @Test
    @DisplayName("Should multiply two BigDecimal values with proper precision")
    void testMultiply() {
        // Arrange
        BigDecimal expected = new BigDecimal("56.1153269645");
        
        // Act
        BigDecimal result = BigDecimalUtils.multiply(TEST_VALUE_1, TEST_VALUE_2);
        
        // Assert
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test multiplying by zero
        result = BigDecimalUtils.multiply(TEST_VALUE_1, BigDecimal.ZERO);
        TestUtils.assertBigDecimalEquals(BigDecimal.ZERO, result);
        
        // Test multiplying by negative value
        expected = new BigDecimal("-37.9629375");
        result = BigDecimalUtils.multiply(TEST_VALUE_1, NEGATIVE_TEST_VALUE);
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test multiplying two negative values
        expected = new BigDecimal("14.0625");
        result = BigDecimalUtils.multiply(NEGATIVE_TEST_VALUE, NEGATIVE_TEST_VALUE);
        TestUtils.assertBigDecimalEquals(expected, result);
    }

    @Test
    @DisplayName("Should multiply BigDecimal by integer with proper precision")
    void testMultiplyByInt() {
        // Arrange
        BigDecimal expected = new BigDecimal("30.37035");
        int multiplier = 3;
        
        // Act
        BigDecimal result = BigDecimalUtils.multiplyByInt(TEST_VALUE_1, multiplier);
        
        // Assert
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test multiplying by zero
        result = BigDecimalUtils.multiplyByInt(TEST_VALUE_1, 0);
        TestUtils.assertBigDecimalEquals(BigDecimal.ZERO, result);
        
        // Test multiplying by negative integer
        expected = new BigDecimal("-20.2469");
        result = BigDecimalUtils.multiplyByInt(TEST_VALUE_1, -2);
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test multiplying negative value by integer
        expected = new BigDecimal("-11.25");
        result = BigDecimalUtils.multiplyByInt(NEGATIVE_TEST_VALUE, 3);
        TestUtils.assertBigDecimalEquals(expected, result);
    }

    @Test
    @DisplayName("Should divide two BigDecimal values with proper precision")
    void testDivide() {
        // Arrange
        BigDecimal expected = new BigDecimal("1.82592");
        
        // Act
        BigDecimal result = BigDecimalUtils.divide(TEST_VALUE_1, TEST_VALUE_2);
        
        // Assert
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test dividing by one
        result = BigDecimalUtils.divide(TEST_VALUE_1, BigDecimal.ONE);
        TestUtils.assertBigDecimalEquals(TEST_VALUE_1, result);
        
        // Test dividing zero
        result = BigDecimalUtils.divide(BigDecimal.ZERO, TEST_VALUE_1);
        TestUtils.assertBigDecimalEquals(BigDecimal.ZERO, result);
        
        // Test division with negative values
        expected = new BigDecimal("-2.69959");
        result = BigDecimalUtils.divide(TEST_VALUE_1, NEGATIVE_TEST_VALUE);
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test division by zero - should throw ArithmeticException
        assertThrows(ArithmeticException.class, () -> BigDecimalUtils.divide(TEST_VALUE_1, BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should divide BigDecimal by integer with proper precision")
    void testDivideByInt() {
        // Arrange
        BigDecimal expected = new BigDecimal("3.37448");
        int divisor = 3;
        
        // Act
        BigDecimal result = BigDecimalUtils.divideByInt(TEST_VALUE_1, divisor);
        
        // Assert
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test dividing by one
        result = BigDecimalUtils.divideByInt(TEST_VALUE_1, 1);
        TestUtils.assertBigDecimalEquals(TEST_VALUE_1, result);
        
        // Test dividing zero
        result = BigDecimalUtils.divideByInt(BigDecimal.ZERO, 5);
        TestUtils.assertBigDecimalEquals(BigDecimal.ZERO, result);
        
        // Test division with negative divisor
        expected = new BigDecimal("-5.06173");
        result = BigDecimalUtils.divideByInt(TEST_VALUE_1, -2);
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test division by zero - should throw ArithmeticException
        assertThrows(ArithmeticException.class, () -> BigDecimalUtils.divideByInt(TEST_VALUE_1, 0));
    }

    @Test
    @DisplayName("Should raise BigDecimal to power with proper precision")
    void testPow() {
        // Arrange
        BigDecimal expected = new BigDecimal("1039.16976054");
        
        // Act
        BigDecimal result = BigDecimalUtils.pow(TEST_VALUE_1, TEST_EXPONENT);
        
        // Assert
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test power of zero (should return 1)
        result = BigDecimalUtils.pow(TEST_VALUE_1, 0);
        TestUtils.assertBigDecimalEquals(BigDecimal.ONE, result);
        
        // Test power of one (should return the base)
        result = BigDecimalUtils.pow(TEST_VALUE_1, 1);
        TestUtils.assertBigDecimalEquals(TEST_VALUE_1, result);
        
        // Test negative base and positive exponent
        expected = new BigDecimal("-52.734375");
        result = BigDecimalUtils.pow(NEGATIVE_TEST_VALUE, 3);
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test negative base and even exponent (positive result)
        expected = new BigDecimal("14.0625");
        result = BigDecimalUtils.pow(NEGATIVE_TEST_VALUE, 2);
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test negative exponent
        expected = new BigDecimal("0.09874");
        result = BigDecimalUtils.pow(TEST_VALUE_1, -2);
        TestUtils.assertBigDecimalEquals(expected, result);
    }

    @Test
    @DisplayName("Should convert percentage to decimal correctly")
    void testPercentageToDecimal() {
        // Arrange
        BigDecimal percentage = new BigDecimal("7.5");
        BigDecimal expected = new BigDecimal("0.075");
        
        // Act
        BigDecimal result = BigDecimalUtils.percentageToDecimal(percentage);
        
        // Assert
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test with zero percentage
        result = BigDecimalUtils.percentageToDecimal(BigDecimal.ZERO);
        TestUtils.assertBigDecimalEquals(BigDecimal.ZERO, result);
        
        // Test with negative percentage
        percentage = new BigDecimal("-5.25");
        expected = new BigDecimal("-0.0525");
        result = BigDecimalUtils.percentageToDecimal(percentage);
        TestUtils.assertBigDecimalEquals(expected, result);
    }

    @Test
    @DisplayName("Should convert decimal to percentage correctly")
    void testDecimalToPercentage() {
        // Arrange
        BigDecimal decimal = new BigDecimal("0.075");
        BigDecimal expected = new BigDecimal("7.5");
        
        // Act
        BigDecimal result = BigDecimalUtils.decimalToPercentage(decimal);
        
        // Assert
        TestUtils.assertBigDecimalEquals(expected, result);
        
        // Test with zero decimal
        result = BigDecimalUtils.decimalToPercentage(BigDecimal.ZERO);
        TestUtils.assertBigDecimalEquals(BigDecimal.ZERO, result);
        
        // Test with negative decimal
        decimal = new BigDecimal("-0.0525");
        expected = new BigDecimal("-5.25");
        result = BigDecimalUtils.decimalToPercentage(decimal);
        TestUtils.assertBigDecimalEquals(expected, result);
    }

    @Test
    @DisplayName("Should correctly identify zero values")
    void testIsZero() {
        // Act & Assert
        assertTrue(BigDecimalUtils.isZero(BigDecimal.ZERO));
        assertFalse(BigDecimalUtils.isZero(TEST_VALUE_1));
        assertFalse(BigDecimalUtils.isZero(TEST_VALUE_2));
        assertFalse(BigDecimalUtils.isZero(NEGATIVE_TEST_VALUE));
        
        // Test with very small value that rounds to zero
        BigDecimal verySmallValue = new BigDecimal("0.000000000000000000001");
        assertFalse(BigDecimalUtils.isZero(verySmallValue));
    }

    @Test
    @DisplayName("Should correctly identify positive values")
    void testIsPositive() {
        // Act & Assert
        assertTrue(BigDecimalUtils.isPositive(TEST_VALUE_1));
        assertTrue(BigDecimalUtils.isPositive(TEST_VALUE_2));
        assertFalse(BigDecimalUtils.isPositive(BigDecimal.ZERO));
        assertFalse(BigDecimalUtils.isPositive(NEGATIVE_TEST_VALUE));
        
        // Test with very small positive value
        BigDecimal verySmallPositive = new BigDecimal("0.000000000000000000001");
        assertTrue(BigDecimalUtils.isPositive(verySmallPositive));
    }

    @Test
    @DisplayName("Should correctly identify negative values")
    void testIsNegative() {
        // Act & Assert
        assertFalse(BigDecimalUtils.isNegative(TEST_VALUE_1));
        assertFalse(BigDecimalUtils.isNegative(TEST_VALUE_2));
        assertFalse(BigDecimalUtils.isNegative(BigDecimal.ZERO));
        assertTrue(BigDecimalUtils.isNegative(NEGATIVE_TEST_VALUE));
        
        // Test with very small negative value
        BigDecimal verySmallNegative = new BigDecimal("-0.000000000000000000001");
        assertTrue(BigDecimalUtils.isNegative(verySmallNegative));
    }

    @Test
    @DisplayName("Should correctly compare values for greater than")
    void testIsGreaterThan() {
        // Act & Assert
        assertTrue(BigDecimalUtils.isGreaterThan(TEST_VALUE_1, TEST_VALUE_2));
        assertFalse(BigDecimalUtils.isGreaterThan(TEST_VALUE_2, TEST_VALUE_1));
        assertFalse(BigDecimalUtils.isGreaterThan(TEST_VALUE_1, TEST_VALUE_1)); // Equal values
        assertTrue(BigDecimalUtils.isGreaterThan(TEST_VALUE_1, NEGATIVE_TEST_VALUE));
        assertTrue(BigDecimalUtils.isGreaterThan(BigDecimal.ZERO, NEGATIVE_TEST_VALUE));
        assertFalse(BigDecimalUtils.isGreaterThan(NEGATIVE_TEST_VALUE, BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should correctly compare values for less than")
    void testIsLessThan() {
        // Act & Assert
        assertFalse(BigDecimalUtils.isLessThan(TEST_VALUE_1, TEST_VALUE_2));
        assertTrue(BigDecimalUtils.isLessThan(TEST_VALUE_2, TEST_VALUE_1));
        assertFalse(BigDecimalUtils.isLessThan(TEST_VALUE_1, TEST_VALUE_1)); // Equal values
        assertFalse(BigDecimalUtils.isLessThan(TEST_VALUE_1, NEGATIVE_TEST_VALUE));
        assertFalse(BigDecimalUtils.isLessThan(BigDecimal.ZERO, NEGATIVE_TEST_VALUE));
        assertTrue(BigDecimalUtils.isLessThan(NEGATIVE_TEST_VALUE, BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should correctly compare values for equality")
    void testIsEqual() {
        // Act & Assert
        assertTrue(BigDecimalUtils.isEqual(TEST_VALUE_1, TEST_VALUE_1));
        assertTrue(BigDecimalUtils.isEqual(TEST_VALUE_2, TEST_VALUE_2));
        assertTrue(BigDecimalUtils.isEqual(NEGATIVE_TEST_VALUE, NEGATIVE_TEST_VALUE));
        assertTrue(BigDecimalUtils.isEqual(BigDecimal.ZERO, BigDecimal.ZERO));
        assertFalse(BigDecimalUtils.isEqual(TEST_VALUE_1, TEST_VALUE_2));
        assertFalse(BigDecimalUtils.isEqual(TEST_VALUE_1, NEGATIVE_TEST_VALUE));
        
        // Test with same value but different scale (should be equal)
        BigDecimal value1 = new BigDecimal("10.10");
        BigDecimal value2 = new BigDecimal("10.100");
        assertTrue(BigDecimalUtils.isEqual(value1, value2));
    }

    @Test
    @DisplayName("Should throw NullPointerException for null inputs")
    void testNullInputHandling() {
        // Act & Assert - test all methods with null inputs
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.round(null, TEST_SCALE));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.roundForCurrency(null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.roundForCalculation(null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.add(null, TEST_VALUE_2));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.add(TEST_VALUE_1, null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.subtract(null, TEST_VALUE_2));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.subtract(TEST_VALUE_1, null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.multiply(null, TEST_VALUE_2));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.multiply(TEST_VALUE_1, null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.multiplyByInt(null, 3));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.divide(null, TEST_VALUE_2));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.divide(TEST_VALUE_1, null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.divideByInt(null, 3));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.pow(null, TEST_EXPONENT));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.percentageToDecimal(null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.decimalToPercentage(null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.isZero(null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.isPositive(null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.isNegative(null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.isGreaterThan(null, TEST_VALUE_2));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.isGreaterThan(TEST_VALUE_1, null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.isLessThan(null, TEST_VALUE_2));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.isLessThan(TEST_VALUE_1, null));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.isEqual(null, TEST_VALUE_2));
        assertThrows(NullPointerException.class, () -> BigDecimalUtils.isEqual(TEST_VALUE_1, null));
    }

    // Parameterized tests to test multiple cases efficiently
    
    static Stream<Arguments> provideTestCasesForRounding() {
        return Stream.of(
            Arguments.of(new BigDecimal("10.12345"), 2, new BigDecimal("10.12")),
            Arguments.of(new BigDecimal("10.12545"), 2, new BigDecimal("10.13")),
            Arguments.of(new BigDecimal("10.12345"), 3, new BigDecimal("10.123")),
            Arguments.of(new BigDecimal("10.12345"), 0, new BigDecimal("10")),
            Arguments.of(new BigDecimal("-5.678"), 2, new BigDecimal("-5.68")),
            Arguments.of(new BigDecimal("0.00123"), 4, new BigDecimal("0.0012")),
            Arguments.of(new BigDecimal("999.999"), 2, new BigDecimal("1000.00"))
        );
    }

    static Stream<Arguments> provideTestCasesForArithmetic() {
        return Stream.of(
            // value1, value2, sum, difference, product, quotient
            Arguments.of(
                new BigDecimal("10"), new BigDecimal("5"), 
                new BigDecimal("15"), new BigDecimal("5"), 
                new BigDecimal("50"), new BigDecimal("2")
            ),
            Arguments.of(
                new BigDecimal("7.5"), new BigDecimal("2.5"), 
                new BigDecimal("10"), new BigDecimal("5"), 
                new BigDecimal("18.75"), new BigDecimal("3")
            ),
            Arguments.of(
                new BigDecimal("-10"), new BigDecimal("5"), 
                new BigDecimal("-5"), new BigDecimal("-15"), 
                new BigDecimal("-50"), new BigDecimal("-2")
            ),
            Arguments.of(
                new BigDecimal("0"), new BigDecimal("5"), 
                new BigDecimal("5"), new BigDecimal("-5"), 
                new BigDecimal("0"), new BigDecimal("0")
            )
        );
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForRounding")
    @DisplayName("Should round BigDecimal values correctly for various inputs")
    void testRoundParameterized(BigDecimal input, int scale, BigDecimal expected) {
        BigDecimal result = BigDecimalUtils.round(input, scale);
        TestUtils.assertBigDecimalEquals(expected, result);
        assertEquals(scale, result.scale());
    }

    @ParameterizedTest
    @MethodSource("provideTestCasesForArithmetic")
    @DisplayName("Should perform arithmetic operations correctly for various inputs")
    void testArithmeticOperationsParameterized(BigDecimal value1, BigDecimal value2, 
            BigDecimal expectedSum, BigDecimal expectedDifference, 
            BigDecimal expectedProduct, BigDecimal expectedQuotient) {
        
        // Test addition
        BigDecimal sum = BigDecimalUtils.add(value1, value2);
        TestUtils.assertBigDecimalEquals(expectedSum, sum);
        
        // Test subtraction
        BigDecimal difference = BigDecimalUtils.subtract(value1, value2);
        TestUtils.assertBigDecimalEquals(expectedDifference, difference);
        
        // Test multiplication
        BigDecimal product = BigDecimalUtils.multiply(value1, value2);
        TestUtils.assertBigDecimalEquals(expectedProduct, product);
        
        // Test division (skip if value2 is zero)
        if (!BigDecimalUtils.isZero(value2)) {
            BigDecimal quotient = BigDecimalUtils.divide(value1, value2);
            TestUtils.assertBigDecimalEquals(expectedQuotient, quotient);
        }
    }
}