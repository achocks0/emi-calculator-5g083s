package com.bank.calculator.util;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.constant.ErrorMessages;
import com.bank.calculator.model.ValidationResult;

/**
 * Unit test class for ValidationUtils that verifies the correctness of all validation methods
 * used to validate user inputs in the Compound Interest Calculator application.
 */
@DisplayName("ValidationUtils Tests")
public class ValidationUtilsTest {

    // Tests for validatePrincipal method

    @Test
    @DisplayName("Should return invalid result with appropriate error message when principal is null")
    public void testValidatePrincipalWithNullValue() {
        ValidationResult result = ValidationUtils.validatePrincipal(null);
        
        assertFalse(result.isValid());
        assertEquals(ErrorMessages.PRINCIPAL_REQUIRED, result.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when principal is empty")
    public void testValidatePrincipalWithEmptyValue() {
        ValidationResult result = ValidationUtils.validatePrincipal("");
        
        assertFalse(result.isValid());
        assertEquals(ErrorMessages.PRINCIPAL_REQUIRED, result.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when principal has invalid format")
    public void testValidatePrincipalWithInvalidFormat() {
        // Test with alphabetic characters
        ValidationResult result1 = ValidationUtils.validatePrincipal("abc");
        assertFalse(result1.isValid());
        assertEquals(ErrorMessages.PRINCIPAL_FORMAT, result1.getErrorMessage());
        
        // Test with more than 2 decimal places
        ValidationResult result2 = ValidationUtils.validatePrincipal("123.456");
        assertFalse(result2.isValid());
        assertEquals(ErrorMessages.PRINCIPAL_FORMAT, result2.getErrorMessage());
        
        // Test with comma separator
        ValidationResult result3 = ValidationUtils.validatePrincipal("123,456");
        assertFalse(result3.isValid());
        assertEquals(ErrorMessages.PRINCIPAL_FORMAT, result3.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when principal is negative")
    public void testValidatePrincipalWithNegativeValue() {
        // Note: Negative values are caught by format validation in ValidationUtils
        ValidationResult result1 = ValidationUtils.validatePrincipal("-100");
        assertFalse(result1.isValid());
        assertEquals(ErrorMessages.PRINCIPAL_FORMAT, result1.getErrorMessage());
        
        ValidationResult result2 = ValidationUtils.validatePrincipal("-0.01");
        assertFalse(result2.isValid());
        assertEquals(ErrorMessages.PRINCIPAL_FORMAT, result2.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when principal is zero")
    public void testValidatePrincipalWithZeroValue() {
        // Test with zero
        ValidationResult result1 = ValidationUtils.validatePrincipal("0");
        assertFalse(result1.isValid());
        assertEquals(ErrorMessages.PRINCIPAL_POSITIVE, result1.getErrorMessage());
        
        // Test with zero decimal
        ValidationResult result2 = ValidationUtils.validatePrincipal("0.00");
        assertFalse(result2.isValid());
        assertEquals(ErrorMessages.PRINCIPAL_POSITIVE, result2.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when principal is below minimum")
    public void testValidatePrincipalBelowMinimum() {
        // Create a value just below the minimum
        String belowMin = CalculationConstants.MIN_PRINCIPAL_AMOUNT.subtract(new BigDecimal("0.01")).toString();
        
        ValidationResult result = ValidationUtils.validatePrincipal(belowMin);
        assertFalse(result.isValid());
        assertEquals(ErrorMessages.PRINCIPAL_MIN_REQUIRED, result.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when principal is above maximum")
    public void testValidatePrincipalAboveMaximum() {
        // Create a value just above the maximum
        String aboveMax = CalculationConstants.MAX_PRINCIPAL_AMOUNT.add(new BigDecimal("0.01")).toString();
        
        ValidationResult result = ValidationUtils.validatePrincipal(aboveMax);
        assertFalse(result.isValid());
        assertEquals(ErrorMessages.PRINCIPAL_MAX_EXCEEDED, result.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return valid result when principal is valid")
    public void testValidatePrincipalWithValidValues() {
        // Test minimum value
        ValidationResult result1 = ValidationUtils.validatePrincipal(CalculationConstants.MIN_PRINCIPAL_AMOUNT.toString());
        assertTrue(result1.isValid());
        assertNull(result1.getErrorMessage());
        
        // Test maximum value
        ValidationResult result2 = ValidationUtils.validatePrincipal(CalculationConstants.MAX_PRINCIPAL_AMOUNT.toString());
        assertTrue(result2.isValid());
        assertNull(result2.getErrorMessage());
        
        // Test middle value
        ValidationResult result3 = ValidationUtils.validatePrincipal("5000.50");
        assertTrue(result3.isValid());
        assertNull(result3.getErrorMessage());
    }
    
    // Tests for validateDuration method
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when duration is null")
    public void testValidateDurationWithNullValue() {
        ValidationResult result = ValidationUtils.validateDuration(null);
        
        assertFalse(result.isValid());
        assertEquals(ErrorMessages.DURATION_REQUIRED, result.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when duration is empty")
    public void testValidateDurationWithEmptyValue() {
        ValidationResult result = ValidationUtils.validateDuration("");
        
        assertFalse(result.isValid());
        assertEquals(ErrorMessages.DURATION_REQUIRED, result.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when duration has invalid format")
    public void testValidateDurationWithInvalidFormat() {
        // Test with alphabetic characters
        ValidationResult result1 = ValidationUtils.validateDuration("abc");
        assertFalse(result1.isValid());
        assertEquals(ErrorMessages.DURATION_FORMAT, result1.getErrorMessage());
        
        // Test with decimal
        ValidationResult result2 = ValidationUtils.validateDuration("5.5");
        assertFalse(result2.isValid());
        assertEquals(ErrorMessages.DURATION_FORMAT, result2.getErrorMessage());
        
        // Test with comma separator
        ValidationResult result3 = ValidationUtils.validateDuration("10,5");
        assertFalse(result3.isValid());
        assertEquals(ErrorMessages.DURATION_FORMAT, result3.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when duration is negative")
    public void testValidateDurationWithNegativeValue() {
        // Note: Negative values are caught by format validation in ValidationUtils
        ValidationResult result1 = ValidationUtils.validateDuration("-5");
        assertFalse(result1.isValid());
        assertEquals(ErrorMessages.DURATION_FORMAT, result1.getErrorMessage());
        
        ValidationResult result2 = ValidationUtils.validateDuration("-1");
        assertFalse(result2.isValid());
        assertEquals(ErrorMessages.DURATION_FORMAT, result2.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when duration is zero")
    public void testValidateDurationWithZeroValue() {
        ValidationResult result = ValidationUtils.validateDuration("0");
        assertFalse(result.isValid());
        assertEquals(ErrorMessages.DURATION_POSITIVE, result.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when duration is below minimum")
    public void testValidateDurationBelowMinimum() {
        // Only test if MIN_DURATION_YEARS is greater than 1
        if (CalculationConstants.MIN_DURATION_YEARS > 1) {
            String belowMin = String.valueOf(CalculationConstants.MIN_DURATION_YEARS - 1);
            
            ValidationResult result = ValidationUtils.validateDuration(belowMin);
            assertFalse(result.isValid());
            assertEquals(ErrorMessages.DURATION_MIN_REQUIRED, result.getErrorMessage());
        }
    }
    
    @Test
    @DisplayName("Should return invalid result with appropriate error message when duration is above maximum")
    public void testValidateDurationAboveMaximum() {
        String aboveMax = String.valueOf(CalculationConstants.MAX_DURATION_YEARS + 1);
        
        ValidationResult result = ValidationUtils.validateDuration(aboveMax);
        assertFalse(result.isValid());
        assertEquals(ErrorMessages.DURATION_MAX_EXCEEDED, result.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return valid result when duration is valid")
    public void testValidateDurationWithValidValues() {
        // Test minimum value
        ValidationResult result1 = ValidationUtils.validateDuration(String.valueOf(CalculationConstants.MIN_DURATION_YEARS));
        assertTrue(result1.isValid());
        assertNull(result1.getErrorMessage());
        
        // Test maximum value
        ValidationResult result2 = ValidationUtils.validateDuration(String.valueOf(CalculationConstants.MAX_DURATION_YEARS));
        assertTrue(result2.isValid());
        assertNull(result2.getErrorMessage());
        
        // Test middle value
        ValidationResult result3 = ValidationUtils.validateDuration("5");
        assertTrue(result3.isValid());
        assertNull(result3.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return false when checking if null is a positive number")
    public void testIsPositiveNumberWithNullValue() {
        assertFalse(ValidationUtils.isPositiveNumber(null));
    }
    
    @Test
    @DisplayName("Should return false when checking if empty string is a positive number")
    public void testIsPositiveNumberWithEmptyValue() {
        assertFalse(ValidationUtils.isPositiveNumber(""));
    }
    
    @Test
    @DisplayName("Should return false when checking if non-numeric string is a positive number")
    public void testIsPositiveNumberWithInvalidFormat() {
        assertFalse(ValidationUtils.isPositiveNumber("abc"));
        assertFalse(ValidationUtils.isPositiveNumber("123abc"));
        assertFalse(ValidationUtils.isPositiveNumber("123,456"));
    }
    
    @Test
    @DisplayName("Should return false when checking if negative number is a positive number")
    public void testIsPositiveNumberWithNegativeValue() {
        assertFalse(ValidationUtils.isPositiveNumber("-100"));
        assertFalse(ValidationUtils.isPositiveNumber("-0.01"));
    }
    
    @Test
    @DisplayName("Should return false when checking if zero is a positive number")
    public void testIsPositiveNumberWithZeroValue() {
        assertFalse(ValidationUtils.isPositiveNumber("0"));
        assertFalse(ValidationUtils.isPositiveNumber("0.00"));
    }
    
    @Test
    @DisplayName("Should return true when checking if positive number is a positive number")
    public void testIsPositiveNumberWithValidValues() {
        assertTrue(ValidationUtils.isPositiveNumber("1"));
        assertTrue(ValidationUtils.isPositiveNumber("100"));
        assertTrue(ValidationUtils.isPositiveNumber("0.01"));
        assertTrue(ValidationUtils.isPositiveNumber("1000.50"));
    }
    
    @Test
    @DisplayName("Should return false when checking if null is a positive integer")
    public void testIsPositiveIntegerWithNullValue() {
        assertFalse(ValidationUtils.isPositiveInteger(null));
    }
    
    @Test
    @DisplayName("Should return false when checking if empty string is a positive integer")
    public void testIsPositiveIntegerWithEmptyValue() {
        assertFalse(ValidationUtils.isPositiveInteger(""));
    }
    
    @Test
    @DisplayName("Should return false when checking if non-integer string is a positive integer")
    public void testIsPositiveIntegerWithInvalidFormat() {
        assertFalse(ValidationUtils.isPositiveInteger("abc"));
        assertFalse(ValidationUtils.isPositiveInteger("5.5"));
        assertFalse(ValidationUtils.isPositiveInteger("10,5"));
    }
    
    @Test
    @DisplayName("Should return false when checking if negative integer is a positive integer")
    public void testIsPositiveIntegerWithNegativeValue() {
        assertFalse(ValidationUtils.isPositiveInteger("-5"));
        assertFalse(ValidationUtils.isPositiveInteger("-1"));
    }
    
    @Test
    @DisplayName("Should return false when checking if zero is a positive integer")
    public void testIsPositiveIntegerWithZeroValue() {
        assertFalse(ValidationUtils.isPositiveInteger("0"));
    }
    
    @Test
    @DisplayName("Should return true when checking if positive integer is a positive integer")
    public void testIsPositiveIntegerWithValidValues() {
        assertTrue(ValidationUtils.isPositiveInteger("1"));
        assertTrue(ValidationUtils.isPositiveInteger("5"));
        assertTrue(ValidationUtils.isPositiveInteger("100"));
    }
}