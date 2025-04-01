package com.bank.calculator.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

import com.bank.calculator.constant.CalculationConstants;

/**
 * Unit test class for BigDecimalUtils that verifies the correctness of all utility methods
 * for BigDecimal operations used in financial calculations.
 */
public class BigDecimalUtilsTest {

    @Test
    @DisplayName("Should throw NullPointerException when rounding null value")
    public void testRoundWithNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.round(null, 2));
    }

    @Test
    @DisplayName("Should round BigDecimal to specified scale")
    public void testRoundWithValidScale() {
        // Test rounding down
        BigDecimal value1 = new BigDecimal("10.1234");
        BigDecimal result1 = BigDecimalUtils.round(value1, 2);
        Assertions.assertEquals(new BigDecimal("10.12"), result1);
        Assertions.assertEquals(2, result1.scale());
        
        // Test rounding up
        BigDecimal value2 = new BigDecimal("10.125");
        BigDecimal result2 = BigDecimalUtils.round(value2, 2);
        Assertions.assertEquals(new BigDecimal("10.13"), result2);
        Assertions.assertEquals(2, result2.scale());
        
        // Test rounding to 0 decimal places
        BigDecimal value3 = new BigDecimal("10.5");
        BigDecimal result3 = BigDecimalUtils.round(value3, 0);
        Assertions.assertEquals(new BigDecimal("11"), result3);
        Assertions.assertEquals(0, result3.scale());
    }

    @Test
    @DisplayName("Should throw NullPointerException when rounding null value for currency")
    public void testRoundForCurrencyWithNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.roundForCurrency(null));
    }

    @Test
    @DisplayName("Should round BigDecimal for currency display with 2 decimal places")
    public void testRoundForCurrency() {
        // Test rounding down
        BigDecimal value1 = new BigDecimal("10.1234");
        BigDecimal result1 = BigDecimalUtils.roundForCurrency(value1);
        Assertions.assertEquals(new BigDecimal("10.12"), result1);
        
        // Test rounding up
        BigDecimal value2 = new BigDecimal("10.125");
        BigDecimal result2 = BigDecimalUtils.roundForCurrency(value2);
        Assertions.assertEquals(new BigDecimal("10.13"), result2);
        
        // Test exact value
        BigDecimal value3 = new BigDecimal("10.10");
        BigDecimal result3 = BigDecimalUtils.roundForCurrency(value3);
        Assertions.assertEquals(new BigDecimal("10.10"), result3);
    }

    @Test
    @DisplayName("Should throw NullPointerException when rounding null value for calculation")
    public void testRoundForCalculationWithNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.roundForCalculation(null));
    }

    @Test
    @DisplayName("Should round BigDecimal for internal calculations with appropriate precision")
    public void testRoundForCalculation() {
        BigDecimal value = new BigDecimal("123.4567890123456789");
        BigDecimal result = BigDecimalUtils.roundForCalculation(value);
        
        // Should be rounded to CALCULATION_PRECISION digits
        Assertions.assertEquals(CalculationConstants.CALCULATION_PRECISION, result.precision());
        
        // Check that the rounding was applied correctly with HALF_UP rounding mode
        BigDecimal expected = new BigDecimal("123.4567890").setScale(7, RoundingMode.HALF_UP);
        Assertions.assertEquals(expected, result);
    }

    @Test
    @DisplayName("Should throw NullPointerException when adding with null values")
    public void testAddWithNullValues() {
        BigDecimal validValue = new BigDecimal("100");
        
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.add(null, validValue));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.add(validValue, null));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.add(null, null));
    }

    @Test
    @DisplayName("Should add two BigDecimal values with proper precision")
    public void testAdd() {
        // Simple addition
        BigDecimal value1 = new BigDecimal("100");
        BigDecimal value2 = new BigDecimal("200");
        BigDecimal result1 = BigDecimalUtils.add(value1, value2);
        Assertions.assertEquals(new BigDecimal("300"), result1);
        
        // Addition with decimal places
        BigDecimal value3 = new BigDecimal("10.5");
        BigDecimal value4 = new BigDecimal("20.75");
        BigDecimal result2 = BigDecimalUtils.add(value3, value4);
        Assertions.assertEquals(new BigDecimal("31.25"), result2);
        
        // Addition with negative values
        BigDecimal value5 = new BigDecimal("100");
        BigDecimal value6 = new BigDecimal("-50");
        BigDecimal result3 = BigDecimalUtils.add(value5, value6);
        Assertions.assertEquals(new BigDecimal("50"), result3);
        
        // Addition with zero
        BigDecimal value7 = new BigDecimal("100");
        BigDecimal result4 = BigDecimalUtils.add(value7, CalculationConstants.ZERO);
        Assertions.assertEquals(new BigDecimal("100"), result4);
    }

    @Test
    @DisplayName("Should throw NullPointerException when subtracting with null values")
    public void testSubtractWithNullValues() {
        BigDecimal validValue = new BigDecimal("100");
        
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.subtract(null, validValue));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.subtract(validValue, null));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.subtract(null, null));
    }

    @Test
    @DisplayName("Should subtract one BigDecimal value from another with proper precision")
    public void testSubtract() {
        // Simple subtraction
        BigDecimal value1 = new BigDecimal("300");
        BigDecimal value2 = new BigDecimal("100");
        BigDecimal result1 = BigDecimalUtils.subtract(value1, value2);
        Assertions.assertEquals(new BigDecimal("200"), result1);
        
        // Subtraction with decimal places
        BigDecimal value3 = new BigDecimal("30.75");
        BigDecimal value4 = new BigDecimal("20.5");
        BigDecimal result2 = BigDecimalUtils.subtract(value3, value4);
        Assertions.assertEquals(new BigDecimal("10.25"), result2);
        
        // Subtraction resulting in negative value
        BigDecimal value5 = new BigDecimal("50");
        BigDecimal value6 = new BigDecimal("100");
        BigDecimal result3 = BigDecimalUtils.subtract(value5, value6);
        Assertions.assertEquals(new BigDecimal("-50"), result3);
        
        // Subtraction with zero
        BigDecimal value7 = new BigDecimal("100");
        BigDecimal result4 = BigDecimalUtils.subtract(value7, CalculationConstants.ZERO);
        Assertions.assertEquals(new BigDecimal("100"), result4);
    }

    @Test
    @DisplayName("Should throw NullPointerException when multiplying with null values")
    public void testMultiplyWithNullValues() {
        BigDecimal validValue = new BigDecimal("100");
        
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.multiply(null, validValue));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.multiply(validValue, null));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.multiply(null, null));
    }

    @Test
    @DisplayName("Should multiply two BigDecimal values with proper precision")
    public void testMultiply() {
        // Simple multiplication
        BigDecimal value1 = new BigDecimal("10");
        BigDecimal value2 = new BigDecimal("20");
        BigDecimal result1 = BigDecimalUtils.multiply(value1, value2);
        Assertions.assertEquals(new BigDecimal("200"), result1);
        
        // Multiplication with decimal places
        BigDecimal value3 = new BigDecimal("10.5");
        BigDecimal value4 = new BigDecimal("2.5");
        BigDecimal result2 = BigDecimalUtils.multiply(value3, value4);
        Assertions.assertEquals(new BigDecimal("26.25"), result2);
        
        // Multiplication with negative values
        BigDecimal value5 = new BigDecimal("10");
        BigDecimal value6 = new BigDecimal("-5");
        BigDecimal result3 = BigDecimalUtils.multiply(value5, value6);
        Assertions.assertEquals(new BigDecimal("-50"), result3);
        
        // Multiplication with zero
        BigDecimal value7 = new BigDecimal("100");
        BigDecimal result4 = BigDecimalUtils.multiply(value7, CalculationConstants.ZERO);
        Assertions.assertEquals(new BigDecimal("0"), result4);
        
        // Multiplication with one
        BigDecimal value8 = new BigDecimal("100");
        BigDecimal result5 = BigDecimalUtils.multiply(value8, CalculationConstants.ONE);
        Assertions.assertEquals(new BigDecimal("100"), result5);
    }

    @Test
    @DisplayName("Should throw NullPointerException when multiplying null value by int")
    public void testMultiplyByIntWithNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.multiplyByInt(null, 5));
    }

    @Test
    @DisplayName("Should multiply a BigDecimal value by an integer with proper precision")
    public void testMultiplyByInt() {
        // Simple multiplication
        BigDecimal value1 = new BigDecimal("10");
        BigDecimal result1 = BigDecimalUtils.multiplyByInt(value1, 5);
        Assertions.assertEquals(new BigDecimal("50"), result1);
        
        // Multiplication with decimal places
        BigDecimal value2 = new BigDecimal("10.5");
        BigDecimal result2 = BigDecimalUtils.multiplyByInt(value2, 2);
        Assertions.assertEquals(new BigDecimal("21"), result2);
        
        // Multiplication with negative integer
        BigDecimal value3 = new BigDecimal("10");
        BigDecimal result3 = BigDecimalUtils.multiplyByInt(value3, -5);
        Assertions.assertEquals(new BigDecimal("-50"), result3);
        
        // Multiplication with zero
        BigDecimal value4 = new BigDecimal("100");
        BigDecimal result4 = BigDecimalUtils.multiplyByInt(value4, 0);
        Assertions.assertEquals(new BigDecimal("0"), result4);
        
        // Multiplication with one
        BigDecimal value5 = new BigDecimal("100");
        BigDecimal result5 = BigDecimalUtils.multiplyByInt(value5, 1);
        Assertions.assertEquals(new BigDecimal("100"), result5);
    }

    @Test
    @DisplayName("Should throw NullPointerException when dividing with null values")
    public void testDivideWithNullValues() {
        BigDecimal validValue = new BigDecimal("100");
        
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.divide(null, validValue));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.divide(validValue, null));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.divide(null, null));
    }

    @Test
    @DisplayName("Should throw ArithmeticException when dividing by zero")
    public void testDivideByZero() {
        BigDecimal validValue = new BigDecimal("100");
        
        Assertions.assertThrows(ArithmeticException.class, () -> BigDecimalUtils.divide(validValue, BigDecimal.ZERO));
    }

    @Test
    @DisplayName("Should divide one BigDecimal value by another with proper precision")
    public void testDivide() {
        // Simple division
        BigDecimal value1 = new BigDecimal("100");
        BigDecimal value2 = new BigDecimal("20");
        BigDecimal result1 = BigDecimalUtils.divide(value1, value2);
        Assertions.assertEquals(new BigDecimal("5"), result1);
        
        // Division with decimal results
        BigDecimal value3 = new BigDecimal("10");
        BigDecimal value4 = new BigDecimal("3");
        BigDecimal result2 = BigDecimalUtils.divide(value3, value4);
        
        // This should have CALCULATION_PRECISION digits and be close to 3.333...
        Assertions.assertEquals(CalculationConstants.CALCULATION_PRECISION, result2.precision());
        Assertions.assertTrue(BigDecimalUtils.isGreaterThan(result2, new BigDecimal("3.3")));
        Assertions.assertTrue(BigDecimalUtils.isLessThan(result2, new BigDecimal("3.4")));
        
        // Division with negative values
        BigDecimal value5 = new BigDecimal("100");
        BigDecimal value6 = new BigDecimal("-20");
        BigDecimal result3 = BigDecimalUtils.divide(value5, value6);
        Assertions.assertEquals(new BigDecimal("-5"), result3);
        
        // Division by one
        BigDecimal value7 = new BigDecimal("100");
        BigDecimal result4 = BigDecimalUtils.divide(value7, CalculationConstants.ONE);
        Assertions.assertEquals(new BigDecimal("100"), result4);
    }

    @Test
    @DisplayName("Should throw NullPointerException when dividing null value by int")
    public void testDivideByIntWithNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.divideByInt(null, 5));
    }

    @Test
    @DisplayName("Should throw ArithmeticException when dividing by zero int")
    public void testDivideByIntZero() {
        BigDecimal validValue = new BigDecimal("100");
        
        Assertions.assertThrows(ArithmeticException.class, () -> BigDecimalUtils.divideByInt(validValue, 0));
    }

    @Test
    @DisplayName("Should divide a BigDecimal value by an integer with proper precision")
    public void testDivideByInt() {
        // Simple division
        BigDecimal value1 = new BigDecimal("100");
        BigDecimal result1 = BigDecimalUtils.divideByInt(value1, 20);
        Assertions.assertEquals(new BigDecimal("5"), result1);
        
        // Division with decimal results
        BigDecimal value2 = new BigDecimal("10");
        BigDecimal result2 = BigDecimalUtils.divideByInt(value2, 3);
        
        // This should have CALCULATION_PRECISION digits and be close to 3.333...
        Assertions.assertEquals(CalculationConstants.CALCULATION_PRECISION, result2.precision());
        Assertions.assertTrue(BigDecimalUtils.isGreaterThan(result2, new BigDecimal("3.3")));
        Assertions.assertTrue(BigDecimalUtils.isLessThan(result2, new BigDecimal("3.4")));
        
        // Division with negative integer
        BigDecimal value3 = new BigDecimal("100");
        BigDecimal result3 = BigDecimalUtils.divideByInt(value3, -20);
        Assertions.assertEquals(new BigDecimal("-5"), result3);
        
        // Division by one
        BigDecimal value4 = new BigDecimal("100");
        BigDecimal result4 = BigDecimalUtils.divideByInt(value4, 1);
        Assertions.assertEquals(new BigDecimal("100"), result4);
    }

    @Test
    @DisplayName("Should throw NullPointerException when raising null to a power")
    public void testPowWithNullBase() {
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.pow(null, 2));
    }

    @Test
    @DisplayName("Should return ONE when exponent is zero")
    public void testPowZeroExponent() {
        BigDecimal base1 = new BigDecimal("10");
        BigDecimal result1 = BigDecimalUtils.pow(base1, 0);
        Assertions.assertEquals(CalculationConstants.ONE, result1);
        
        BigDecimal base2 = new BigDecimal("-10");
        BigDecimal result2 = BigDecimalUtils.pow(base2, 0);
        Assertions.assertEquals(CalculationConstants.ONE, result2);
        
        BigDecimal base3 = BigDecimal.ZERO;
        BigDecimal result3 = BigDecimalUtils.pow(base3, 0);
        Assertions.assertEquals(CalculationConstants.ONE, result3);
    }

    @Test
    @DisplayName("Should return the base when exponent is one")
    public void testPowOneExponent() {
        BigDecimal base1 = new BigDecimal("10");
        BigDecimal result1 = BigDecimalUtils.pow(base1, 1);
        Assertions.assertEquals(base1, result1);
        
        BigDecimal base2 = new BigDecimal("-10");
        BigDecimal result2 = BigDecimalUtils.pow(base2, 1);
        Assertions.assertEquals(base2, result2);
        
        BigDecimal base3 = BigDecimal.ZERO;
        BigDecimal result3 = BigDecimalUtils.pow(base3, 1);
        Assertions.assertEquals(base3, result3);
    }

    @Test
    @DisplayName("Should correctly calculate positive integer powers")
    public void testPowPositiveExponent() {
        // Simple power
        BigDecimal base1 = new BigDecimal("2");
        BigDecimal result1 = BigDecimalUtils.pow(base1, 3);
        Assertions.assertEquals(new BigDecimal("8"), result1);
        
        // Power with decimal base
        BigDecimal base2 = new BigDecimal("1.5");
        BigDecimal result2 = BigDecimalUtils.pow(base2, 2);
        Assertions.assertEquals(new BigDecimal("2.25"), result2);
        
        // Larger exponent
        BigDecimal base3 = new BigDecimal("1.1");
        BigDecimal result3 = BigDecimalUtils.pow(base3, 10);
        // Expected result: 1.1^10 ≈ 2.5937425
        BigDecimal expected3 = new BigDecimal("2.5937425");
        // Allow small precision differences due to rounding
        Assertions.assertTrue(result3.subtract(expected3).abs().compareTo(new BigDecimal("0.0000001")) < 0);
    }

    @Test
    @DisplayName("Should correctly calculate negative integer powers")
    public void testPowNegativeExponent() {
        // Simple negative power
        BigDecimal base1 = new BigDecimal("2");
        BigDecimal result1 = BigDecimalUtils.pow(base1, -2);
        Assertions.assertEquals(new BigDecimal("0.25"), result1);
        
        // Negative power with decimal base
        BigDecimal base2 = new BigDecimal("10");
        BigDecimal result2 = BigDecimalUtils.pow(base2, -2);
        Assertions.assertEquals(new BigDecimal("0.01"), result2);
        
        // Larger negative exponent
        BigDecimal base3 = new BigDecimal("10");
        BigDecimal result3 = BigDecimalUtils.pow(base3, -3);
        Assertions.assertEquals(new BigDecimal("0.001"), result3);
    }

    @Test
    @DisplayName("Should throw NullPointerException when converting null percentage to decimal")
    public void testPercentageToDecimalWithNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.percentageToDecimal(null));
    }

    @Test
    @DisplayName("Should correctly convert percentage values to decimal")
    public void testPercentageToDecimal() {
        // 0%
        BigDecimal percentage1 = new BigDecimal("0");
        BigDecimal result1 = BigDecimalUtils.percentageToDecimal(percentage1);
        Assertions.assertEquals(new BigDecimal("0"), result1);
        
        // 7.5%
        BigDecimal percentage2 = new BigDecimal("7.5");
        BigDecimal result2 = BigDecimalUtils.percentageToDecimal(percentage2);
        Assertions.assertEquals(new BigDecimal("0.075"), result2);
        
        // 100%
        BigDecimal percentage3 = new BigDecimal("100");
        BigDecimal result3 = BigDecimalUtils.percentageToDecimal(percentage3);
        Assertions.assertEquals(new BigDecimal("1"), result3);
        
        // 150%
        BigDecimal percentage4 = new BigDecimal("150");
        BigDecimal result4 = BigDecimalUtils.percentageToDecimal(percentage4);
        Assertions.assertEquals(new BigDecimal("1.5"), result4);
        
        // Negative percentage
        BigDecimal percentage5 = new BigDecimal("-25");
        BigDecimal result5 = BigDecimalUtils.percentageToDecimal(percentage5);
        Assertions.assertEquals(new BigDecimal("-0.25"), result5);
    }

    @Test
    @DisplayName("Should throw NullPointerException when converting null decimal to percentage")
    public void testDecimalToPercentageWithNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.decimalToPercentage(null));
    }

    @Test
    @DisplayName("Should correctly convert decimal values to percentage")
    public void testDecimalToPercentage() {
        // 0
        BigDecimal decimal1 = new BigDecimal("0");
        BigDecimal result1 = BigDecimalUtils.decimalToPercentage(decimal1);
        Assertions.assertEquals(new BigDecimal("0.00"), result1);
        
        // 0.075
        BigDecimal decimal2 = new BigDecimal("0.075");
        BigDecimal result2 = BigDecimalUtils.decimalToPercentage(decimal2);
        Assertions.assertEquals(new BigDecimal("7.50"), result2);
        
        // 1
        BigDecimal decimal3 = new BigDecimal("1");
        BigDecimal result3 = BigDecimalUtils.decimalToPercentage(decimal3);
        Assertions.assertEquals(new BigDecimal("100.00"), result3);
        
        // 1.5
        BigDecimal decimal4 = new BigDecimal("1.5");
        BigDecimal result4 = BigDecimalUtils.decimalToPercentage(decimal4);
        Assertions.assertEquals(new BigDecimal("150.00"), result4);
        
        // Negative decimal
        BigDecimal decimal5 = new BigDecimal("-0.25");
        BigDecimal result5 = BigDecimalUtils.decimalToPercentage(decimal5);
        Assertions.assertEquals(new BigDecimal("-25.00"), result5);
    }

    @Test
    @DisplayName("Should throw NullPointerException when checking if null is zero")
    public void testIsZeroWithNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isZero(null));
    }

    @Test
    @DisplayName("Should correctly identify zero values")
    public void testIsZero() {
        // Exact zero
        BigDecimal value1 = BigDecimal.ZERO;
        Assertions.assertTrue(BigDecimalUtils.isZero(value1));
        
        // Zero with decimal places
        BigDecimal value2 = new BigDecimal("0.00");
        Assertions.assertTrue(BigDecimalUtils.isZero(value2));
        
        // Very small value (not zero)
        BigDecimal value3 = new BigDecimal("0.0001");
        Assertions.assertFalse(BigDecimalUtils.isZero(value3));
        
        // Negative value
        BigDecimal value4 = new BigDecimal("-10");
        Assertions.assertFalse(BigDecimalUtils.isZero(value4));
        
        // Positive value
        BigDecimal value5 = new BigDecimal("10");
        Assertions.assertFalse(BigDecimalUtils.isZero(value5));
    }

    @Test
    @DisplayName("Should throw NullPointerException when checking if null is positive")
    public void testIsPositiveWithNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isPositive(null));
    }

    @Test
    @DisplayName("Should correctly identify positive values")
    public void testIsPositive() {
        // Positive values
        BigDecimal value1 = new BigDecimal("10");
        Assertions.assertTrue(BigDecimalUtils.isPositive(value1));
        
        BigDecimal value2 = new BigDecimal("0.001");
        Assertions.assertTrue(BigDecimalUtils.isPositive(value2));
        
        // Zero
        BigDecimal value3 = BigDecimal.ZERO;
        Assertions.assertFalse(BigDecimalUtils.isPositive(value3));
        
        // Negative values
        BigDecimal value4 = new BigDecimal("-10");
        Assertions.assertFalse(BigDecimalUtils.isPositive(value4));
        
        BigDecimal value5 = new BigDecimal("-0.001");
        Assertions.assertFalse(BigDecimalUtils.isPositive(value5));
    }

    @Test
    @DisplayName("Should throw NullPointerException when checking if null is negative")
    public void testIsNegativeWithNullValue() {
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isNegative(null));
    }

    @Test
    @DisplayName("Should correctly identify negative values")
    public void testIsNegative() {
        // Negative values
        BigDecimal value1 = new BigDecimal("-10");
        Assertions.assertTrue(BigDecimalUtils.isNegative(value1));
        
        BigDecimal value2 = new BigDecimal("-0.001");
        Assertions.assertTrue(BigDecimalUtils.isNegative(value2));
        
        // Zero
        BigDecimal value3 = BigDecimal.ZERO;
        Assertions.assertFalse(BigDecimalUtils.isNegative(value3));
        
        // Positive values
        BigDecimal value4 = new BigDecimal("10");
        Assertions.assertFalse(BigDecimalUtils.isNegative(value4));
        
        BigDecimal value5 = new BigDecimal("0.001");
        Assertions.assertFalse(BigDecimalUtils.isNegative(value5));
    }

    @Test
    @DisplayName("Should throw NullPointerException when comparing with null values")
    public void testIsGreaterThanWithNullValues() {
        BigDecimal validValue = new BigDecimal("100");
        
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isGreaterThan(null, validValue));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isGreaterThan(validValue, null));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isGreaterThan(null, null));
    }

    @Test
    @DisplayName("Should correctly identify when one value is greater than another")
    public void testIsGreaterThan() {
        // Clearly greater
        BigDecimal value1 = new BigDecimal("100");
        BigDecimal value2 = new BigDecimal("50");
        Assertions.assertTrue(BigDecimalUtils.isGreaterThan(value1, value2));
        Assertions.assertFalse(BigDecimalUtils.isGreaterThan(value2, value1));
        
        // Slightly greater
        BigDecimal value3 = new BigDecimal("10.001");
        BigDecimal value4 = new BigDecimal("10");
        Assertions.assertTrue(BigDecimalUtils.isGreaterThan(value3, value4));
        Assertions.assertFalse(BigDecimalUtils.isGreaterThan(value4, value3));
        
        // Equal values
        BigDecimal value5 = new BigDecimal("10");
        BigDecimal value6 = new BigDecimal("10.00");
        Assertions.assertFalse(BigDecimalUtils.isGreaterThan(value5, value6));
        Assertions.assertFalse(BigDecimalUtils.isGreaterThan(value6, value5));
        
        // Comparison with zero
        BigDecimal value7 = new BigDecimal("0.001");
        BigDecimal value8 = BigDecimal.ZERO;
        Assertions.assertTrue(BigDecimalUtils.isGreaterThan(value7, value8));
        Assertions.assertFalse(BigDecimalUtils.isGreaterThan(value8, value7));
        
        // Negative values
        BigDecimal value9 = new BigDecimal("-10");
        BigDecimal value10 = new BigDecimal("-20");
        Assertions.assertTrue(BigDecimalUtils.isGreaterThan(value9, value10));
        Assertions.assertFalse(BigDecimalUtils.isGreaterThan(value10, value9));
    }

    @Test
    @DisplayName("Should throw NullPointerException when comparing with null values")
    public void testIsLessThanWithNullValues() {
        BigDecimal validValue = new BigDecimal("100");
        
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isLessThan(null, validValue));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isLessThan(validValue, null));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isLessThan(null, null));
    }

    @Test
    @DisplayName("Should correctly identify when one value is less than another")
    public void testIsLessThan() {
        // Clearly less
        BigDecimal value1 = new BigDecimal("50");
        BigDecimal value2 = new BigDecimal("100");
        Assertions.assertTrue(BigDecimalUtils.isLessThan(value1, value2));
        Assertions.assertFalse(BigDecimalUtils.isLessThan(value2, value1));
        
        // Slightly less
        BigDecimal value3 = new BigDecimal("10");
        BigDecimal value4 = new BigDecimal("10.001");
        Assertions.assertTrue(BigDecimalUtils.isLessThan(value3, value4));
        Assertions.assertFalse(BigDecimalUtils.isLessThan(value4, value3));
        
        // Equal values
        BigDecimal value5 = new BigDecimal("10");
        BigDecimal value6 = new BigDecimal("10.00");
        Assertions.assertFalse(BigDecimalUtils.isLessThan(value5, value6));
        Assertions.assertFalse(BigDecimalUtils.isLessThan(value6, value5));
        
        // Comparison with zero
        BigDecimal value7 = BigDecimal.ZERO;
        BigDecimal value8 = new BigDecimal("0.001");
        Assertions.assertTrue(BigDecimalUtils.isLessThan(value7, value8));
        Assertions.assertFalse(BigDecimalUtils.isLessThan(value8, value7));
        
        // Negative values
        BigDecimal value9 = new BigDecimal("-20");
        BigDecimal value10 = new BigDecimal("-10");
        Assertions.assertTrue(BigDecimalUtils.isLessThan(value9, value10));
        Assertions.assertFalse(BigDecimalUtils.isLessThan(value10, value9));
    }

    @Test
    @DisplayName("Should throw NullPointerException when comparing with null values")
    public void testIsEqualWithNullValues() {
        BigDecimal validValue = new BigDecimal("100");
        
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isEqual(null, validValue));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isEqual(validValue, null));
        Assertions.assertThrows(NullPointerException.class, () -> BigDecimalUtils.isEqual(null, null));
    }

    @Test
    @DisplayName("Should correctly identify when two values are equal")
    public void testIsEqual() {
        // Exactly equal
        BigDecimal value1 = new BigDecimal("100");
        BigDecimal value2 = new BigDecimal("100");
        Assertions.assertTrue(BigDecimalUtils.isEqual(value1, value2));
        Assertions.assertTrue(BigDecimalUtils.isEqual(value2, value1));
        
        // Equal with different scales
        BigDecimal value3 = new BigDecimal("10");
        BigDecimal value4 = new BigDecimal("10.00");
        Assertions.assertTrue(BigDecimalUtils.isEqual(value3, value4));
        Assertions.assertTrue(BigDecimalUtils.isEqual(value4, value3));
        
        // Not equal
        BigDecimal value5 = new BigDecimal("10");
        BigDecimal value6 = new BigDecimal("10.01");
        Assertions.assertFalse(BigDecimalUtils.isEqual(value5, value6));
        Assertions.assertFalse(BigDecimalUtils.isEqual(value6, value5));
        
        // Zero equality
        BigDecimal value7 = BigDecimal.ZERO;
        BigDecimal value8 = new BigDecimal("0.00");
        Assertions.assertTrue(BigDecimalUtils.isEqual(value7, value8));
        Assertions.assertTrue(BigDecimalUtils.isEqual(value8, value7));
    }
}