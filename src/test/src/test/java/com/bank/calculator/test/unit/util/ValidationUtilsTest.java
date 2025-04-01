package com.bank.calculator.test.unit.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import java.math.BigDecimal;

import com.bank.calculator.util.ValidationUtils;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.constant.ErrorMessages;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.category.UnitTest;

/**
 * Unit tests for the ValidationUtils utility class.
 * These tests verify that the validation methods correctly identify valid and invalid inputs
 * according to the application's requirements.
 */
@DisplayName("ValidationUtils Unit Tests")
public class ValidationUtilsTest implements UnitTest {

    @BeforeEach
    public void setUp() {
        TestUtils.logTestInfo("Starting validation test");
    }

    @AfterEach
    public void tearDown() {
        TestUtils.logTestInfo("Completed validation test");
    }

    @Test
    @DisplayName("Should return valid result for valid principal amounts")
    public void testValidatePrincipal_withValidInput_returnsValid() {
        // Define valid principal amounts
        String[] validInputs = {"1000.00", "5000", "25000.50", "999999.99"};
        
        for (String input : validInputs) {
            ValidationResult result = ValidationUtils.validatePrincipal(input);
            TestUtils.assertValidationResult(result, true, null);
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Should return invalid result for null or empty principal")
    public void testValidatePrincipal_withNullOrEmpty_returnsInvalid(String principalStr) {
        ValidationResult result = ValidationUtils.validatePrincipal(principalStr);
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_REQUIRED);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1000", "-0.01", "-25000.50"})
    @DisplayName("Should return invalid result for negative principal amounts")
    public void testValidatePrincipal_withNegativeValue_returnsInvalid(String principalStr) {
        ValidationResult result = ValidationUtils.validatePrincipal(principalStr);
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_FORMAT);
    }

    @Test
    @DisplayName("Should return invalid result for zero principal amount")
    public void testValidatePrincipal_withZeroValue_returnsInvalid() {
        ValidationResult result = ValidationUtils.validatePrincipal("0");
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_POSITIVE);
        
        result = ValidationUtils.validatePrincipal("0.00");
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_POSITIVE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "1000.abc", "1,000.00", "1000.000"})
    @DisplayName("Should return invalid result for principal amounts with invalid format")
    public void testValidatePrincipal_withInvalidFormat_returnsInvalid(String principalStr) {
        ValidationResult result = ValidationUtils.validatePrincipal(principalStr);
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_FORMAT);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1.00", "500.00", "999.99"})
    @DisplayName("Should return invalid result for principal amounts below minimum")
    public void testValidatePrincipal_belowMinimum_returnsInvalid(String principalStr) {
        ValidationResult result = ValidationUtils.validatePrincipal(principalStr);
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_MIN_REQUIRED);
    }

    @ParameterizedTest
    @ValueSource(strings = {"1000000.01", "2000000.00", "9999999.99"})
    @DisplayName("Should return invalid result for principal amounts above maximum")
    public void testValidatePrincipal_aboveMaximum_returnsInvalid(String principalStr) {
        ValidationResult result = ValidationUtils.validatePrincipal(principalStr);
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_MAX_EXCEEDED);
    }

    @Test
    @DisplayName("Should return valid result for principal amounts at boundaries")
    public void testValidatePrincipal_atBoundaries_returnsValid() {
        ValidationResult result = ValidationUtils.validatePrincipal(CalculationConstants.MIN_PRINCIPAL_AMOUNT.toString());
        TestUtils.assertValidationResult(result, true, null);
        
        result = ValidationUtils.validatePrincipal(CalculationConstants.MAX_PRINCIPAL_AMOUNT.toString());
        TestUtils.assertValidationResult(result, true, null);
    }

    @Test
    @DisplayName("Should return valid result for valid loan durations")
    public void testValidateDuration_withValidInput_returnsValid() {
        // Define valid durations
        String[] validInputs = {"1", "5", "15", "30"};
        
        for (String input : validInputs) {
            ValidationResult result = ValidationUtils.validateDuration(input);
            TestUtils.assertValidationResult(result, true, null);
        }
    }

    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Should return invalid result for null or empty duration")
    public void testValidateDuration_withNullOrEmpty_returnsInvalid(String durationStr) {
        ValidationResult result = ValidationUtils.validateDuration(durationStr);
        TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_REQUIRED);
    }

    @ParameterizedTest
    @ValueSource(strings = {"-1", "-5", "-30"})
    @DisplayName("Should return invalid result for negative loan durations")
    public void testValidateDuration_withNegativeValue_returnsInvalid(String durationStr) {
        ValidationResult result = ValidationUtils.validateDuration(durationStr);
        TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_FORMAT);
    }

    @Test
    @DisplayName("Should return invalid result for zero loan duration")
    public void testValidateDuration_withZeroValue_returnsInvalid() {
        ValidationResult result = ValidationUtils.validateDuration("0");
        TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_POSITIVE);
    }

    @ParameterizedTest
    @ValueSource(strings = {"abc", "5.5", "2.0", "1,5"})
    @DisplayName("Should return invalid result for loan durations with invalid format")
    public void testValidateDuration_withInvalidFormat_returnsInvalid(String durationStr) {
        ValidationResult result = ValidationUtils.validateDuration(durationStr);
        TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_FORMAT);
    }

    @Test
    @DisplayName("Should return invalid result for loan durations below minimum")
    public void testValidateDuration_belowMinimum_returnsInvalid() {
        // Only test if minimum duration is greater than 1
        if (CalculationConstants.MIN_DURATION_YEARS > 1) {
            String belowMin = Integer.toString(CalculationConstants.MIN_DURATION_YEARS - 1);
            ValidationResult result = ValidationUtils.validateDuration(belowMin);
            TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_MIN_REQUIRED);
        }
    }

    @Test
    @DisplayName("Should return invalid result for loan durations above maximum")
    public void testValidateDuration_aboveMaximum_returnsInvalid() {
        String aboveMax = Integer.toString(CalculationConstants.MAX_DURATION_YEARS + 1);
        ValidationResult result = ValidationUtils.validateDuration(aboveMax);
        TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_MAX_EXCEEDED);
    }

    @Test
    @DisplayName("Should return valid result for loan durations at boundaries")
    public void testValidateDuration_atBoundaries_returnsValid() {
        ValidationResult result = ValidationUtils.validateDuration(Integer.toString(CalculationConstants.MIN_DURATION_YEARS));
        TestUtils.assertValidationResult(result, true, null);
        
        result = ValidationUtils.validateDuration(Integer.toString(CalculationConstants.MAX_DURATION_YEARS));
        TestUtils.assertValidationResult(result, true, null);
    }
}