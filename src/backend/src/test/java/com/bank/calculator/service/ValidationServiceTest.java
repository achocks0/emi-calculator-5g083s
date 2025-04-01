package com.bank.calculator.service;

import org.junit.jupiter.api.Test; // version 5.8.2
import org.junit.jupiter.api.BeforeEach; // version 5.8.2
import org.junit.jupiter.api.DisplayName; // version 5.8.2
import org.junit.jupiter.api.Assertions; // version 5.8.2
import org.junit.jupiter.params.ParameterizedTest; // version 5.8.2
import org.junit.jupiter.params.provider.ValueSource; // version 5.8.2
import org.junit.jupiter.params.provider.NullSource; // version 5.8.2
import org.junit.jupiter.params.provider.EmptySource; // version 5.8.2

import java.math.BigDecimal; // JDK 11

import com.bank.calculator.service.impl.ValidationServiceImpl;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.constant.ErrorMessages;

/**
 * Test class for ValidationService implementation that validates user inputs
 * for principal amount and loan duration in the Compound Interest Calculator application.
 */
@DisplayName("ValidationService Tests")
public class ValidationServiceTest {

    private ValidationService validationService;

    /**
     * Initializes the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        validationService = new ValidationServiceImpl();
    }

    /**
     * Tests that validatePrincipal returns a valid result for valid principal amounts.
     */
    @Test
    @DisplayName("Should return valid result for valid principal amount")
    void testValidatePrincipal_withValidInput_returnsValidResult() {
        // Valid principal amounts to test
        String[] validPrincipals = {"1000", "5000", "10000.50", "999999.99"};
        
        for (String principal : validPrincipals) {
            ValidationResult result = validationService.validatePrincipal(principal);
            Assertions.assertTrue(result.isValid(), "Expected valid result for principal: " + principal);
            Assertions.assertNull(result.getErrorMessage(), "Expected no error message for valid principal: " + principal);
        }
    }

    /**
     * Tests that validatePrincipal returns an invalid result for null or empty inputs.
     */
    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Should return invalid result for null or empty principal")
    void testValidatePrincipal_withNullOrEmpty_returnsInvalidResult(String principalStr) {
        ValidationResult result = validationService.validatePrincipal(principalStr);
        
        Assertions.assertFalse(result.isValid(), "Expected invalid result for null or empty principal");
        Assertions.assertEquals(ErrorMessages.PRINCIPAL_REQUIRED, result.getErrorMessage(), 
                "Expected appropriate error message for null or empty principal");
    }

    /**
     * Tests that validatePrincipal returns an invalid result for inputs with invalid format.
     */
    @ParameterizedTest
    @ValueSource(strings = {"abc", "123.456", "12,345", "-100"})
    @DisplayName("Should return invalid result for principal with invalid format")
    void testValidatePrincipal_withInvalidFormat_returnsInvalidResult(String principalStr) {
        ValidationResult result = validationService.validatePrincipal(principalStr);
        
        Assertions.assertFalse(result.isValid(), "Expected invalid result for invalid format: " + principalStr);
        
        // Different error messages based on the specific format issue
        if ("-100".equals(principalStr)) {
            Assertions.assertEquals(ErrorMessages.PRINCIPAL_FORMAT, result.getErrorMessage(),
                    "Expected format error message for negative principal");
        } else if ("abc".equals(principalStr)) {
            Assertions.assertEquals(ErrorMessages.PRINCIPAL_FORMAT, result.getErrorMessage(),
                    "Expected format error message for non-numeric principal");
        } else if ("123.456".equals(principalStr)) {
            Assertions.assertEquals(ErrorMessages.PRINCIPAL_FORMAT, result.getErrorMessage(),
                    "Expected format error message for principal with more than 2 decimal places");
        } else if ("12,345".equals(principalStr)) {
            Assertions.assertEquals(ErrorMessages.PRINCIPAL_FORMAT, result.getErrorMessage(),
                    "Expected format error message for principal with comma");
        }
    }

    /**
     * Tests that validatePrincipal returns an invalid result for principal amounts below the minimum allowed value.
     */
    @Test
    @DisplayName("Should return invalid result for principal below minimum")
    void testValidatePrincipal_withValueBelowMinimum_returnsInvalidResult() {
        // Test with value below minimum
        String belowMinPrincipal = "999.99";
        ValidationResult result = validationService.validatePrincipal(belowMinPrincipal);
        
        Assertions.assertFalse(result.isValid(), "Expected invalid result for principal below minimum");
        Assertions.assertEquals(ErrorMessages.PRINCIPAL_MIN_REQUIRED, result.getErrorMessage(),
                "Expected minimum value error message");
    }

    /**
     * Tests that validatePrincipal returns an invalid result for principal amounts above the maximum allowed value.
     */
    @Test
    @DisplayName("Should return invalid result for principal above maximum")
    void testValidatePrincipal_withValueAboveMaximum_returnsInvalidResult() {
        // Test with value above maximum
        String aboveMaxPrincipal = "1000000.01";
        ValidationResult result = validationService.validatePrincipal(aboveMaxPrincipal);
        
        Assertions.assertFalse(result.isValid(), "Expected invalid result for principal above maximum");
        Assertions.assertEquals(ErrorMessages.PRINCIPAL_MAX_EXCEEDED, result.getErrorMessage(),
                "Expected maximum value error message");
    }

    /**
     * Tests that validateDuration returns a valid result for valid loan durations.
     */
    @Test
    @DisplayName("Should return valid result for valid loan duration")
    void testValidateDuration_withValidInput_returnsValidResult() {
        // Valid loan durations to test
        String[] validDurations = {"1", "5", "15", "30"};
        
        for (String duration : validDurations) {
            ValidationResult result = validationService.validateDuration(duration);
            Assertions.assertTrue(result.isValid(), "Expected valid result for duration: " + duration);
            Assertions.assertNull(result.getErrorMessage(), "Expected no error message for valid duration: " + duration);
        }
    }

    /**
     * Tests that validateDuration returns an invalid result for null or empty inputs.
     */
    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Should return invalid result for null or empty duration")
    void testValidateDuration_withNullOrEmpty_returnsInvalidResult(String durationStr) {
        ValidationResult result = validationService.validateDuration(durationStr);
        
        Assertions.assertFalse(result.isValid(), "Expected invalid result for null or empty duration");
        Assertions.assertEquals(ErrorMessages.DURATION_REQUIRED, result.getErrorMessage(), 
                "Expected appropriate error message for null or empty duration");
    }

    /**
     * Tests that validateDuration returns an invalid result for inputs with invalid format.
     */
    @ParameterizedTest
    @ValueSource(strings = {"abc", "5.5", "10,5", "-5"})
    @DisplayName("Should return invalid result for duration with invalid format")
    void testValidateDuration_withInvalidFormat_returnsInvalidResult(String durationStr) {
        ValidationResult result = validationService.validateDuration(durationStr);
        
        Assertions.assertFalse(result.isValid(), "Expected invalid result for invalid format: " + durationStr);
        
        // Different error messages based on the specific format issue
        if ("-5".equals(durationStr)) {
            Assertions.assertEquals(ErrorMessages.DURATION_FORMAT, result.getErrorMessage(),
                    "Expected format error message for negative duration");
        } else if ("abc".equals(durationStr)) {
            Assertions.assertEquals(ErrorMessages.DURATION_FORMAT, result.getErrorMessage(),
                    "Expected format error message for non-numeric duration");
        } else if ("5.5".equals(durationStr)) {
            Assertions.assertEquals(ErrorMessages.DURATION_FORMAT, result.getErrorMessage(),
                    "Expected format error message for non-integer duration");
        } else if ("10,5".equals(durationStr)) {
            Assertions.assertEquals(ErrorMessages.DURATION_FORMAT, result.getErrorMessage(),
                    "Expected format error message for duration with comma");
        }
    }

    /**
     * Tests that validateDuration returns an invalid result for loan durations below the minimum allowed value.
     */
    @Test
    @DisplayName("Should return invalid result for duration below minimum")
    void testValidateDuration_withValueBelowMinimum_returnsInvalidResult() {
        // Test with value below minimum (assuming MIN_DURATION_YEARS is 1)
        String belowMinDuration = "0";
        ValidationResult result = validationService.validateDuration(belowMinDuration);
        
        Assertions.assertFalse(result.isValid(), "Expected invalid result for duration below minimum");
        Assertions.assertEquals(ErrorMessages.DURATION_POSITIVE, result.getErrorMessage(),
                "Expected minimum value error message");
    }

    /**
     * Tests that validateDuration returns an invalid result for loan durations above the maximum allowed value.
     */
    @Test
    @DisplayName("Should return invalid result for duration above maximum")
    void testValidateDuration_withValueAboveMaximum_returnsInvalidResult() {
        // Test with value above maximum (assuming MAX_DURATION_YEARS is 30)
        String aboveMaxDuration = "31";
        ValidationResult result = validationService.validateDuration(aboveMaxDuration);
        
        Assertions.assertFalse(result.isValid(), "Expected invalid result for duration above maximum");
        Assertions.assertEquals(ErrorMessages.DURATION_MAX_EXCEEDED, result.getErrorMessage(),
                "Expected maximum value error message");
    }

    /**
     * Tests that validateAllInputs returns a valid result when all inputs are valid.
     */
    @Test
    @DisplayName("Should return valid result when all inputs are valid")
    void testValidateAllInputs_withAllValidInputs_returnsValidResult() {
        // Valid inputs
        String validPrincipal = "10000";
        String validDuration = "5";
        
        ValidationResult result = validationService.validateAllInputs(validPrincipal, validDuration);
        
        Assertions.assertTrue(result.isValid(), "Expected valid result for valid inputs");
        Assertions.assertNull(result.getErrorMessage(), "Expected no error message for valid inputs");
    }

    /**
     * Tests that validateAllInputs returns an invalid result when principal amount is invalid.
     */
    @Test
    @DisplayName("Should return invalid result when principal is invalid")
    void testValidateAllInputs_withInvalidPrincipal_returnsInvalidResult() {
        // Invalid principal, valid duration
        String invalidPrincipal = "abc";
        String validDuration = "5";
        
        ValidationResult result = validationService.validateAllInputs(invalidPrincipal, validDuration);
        
        Assertions.assertFalse(result.isValid(), "Expected invalid result for invalid principal");
        Assertions.assertEquals(ErrorMessages.PRINCIPAL_FORMAT, result.getErrorMessage(),
                "Expected principal format error message");
    }

    /**
     * Tests that validateAllInputs returns an invalid result when loan duration is invalid.
     */
    @Test
    @DisplayName("Should return invalid result when duration is invalid")
    void testValidateAllInputs_withInvalidDuration_returnsInvalidResult() {
        // Valid principal, invalid duration
        String validPrincipal = "10000";
        String invalidDuration = "abc";
        
        ValidationResult result = validationService.validateAllInputs(validPrincipal, invalidDuration);
        
        Assertions.assertFalse(result.isValid(), "Expected invalid result for invalid duration");
        Assertions.assertEquals(ErrorMessages.DURATION_FORMAT, result.getErrorMessage(),
                "Expected duration format error message");
    }

    /**
     * Tests that validateCalculationInput returns a valid result for valid CalculationInput objects.
     */
    @Test
    @DisplayName("Should return valid result for valid CalculationInput")
    void testValidateCalculationInput_withValidInput_returnsValidResult() {
        // Valid CalculationInput
        CalculationInput validInput = new CalculationInput(
                new BigDecimal("10000"), // valid principal
                5 // valid duration
        );
        
        ValidationResult result = validationService.validateCalculationInput(validInput);
        
        Assertions.assertTrue(result.isValid(), "Expected valid result for valid CalculationInput");
        Assertions.assertNull(result.getErrorMessage(), "Expected no error message for valid CalculationInput");
    }

    /**
     * Tests that validateCalculationInput returns an invalid result for null input.
     */
    @Test
    @DisplayName("Should return invalid result for null CalculationInput")
    void testValidateCalculationInput_withNullInput_returnsInvalidResult() {
        ValidationResult result = validationService.validateCalculationInput(null);
        
        Assertions.assertFalse(result.isValid(), "Expected invalid result for null CalculationInput");
        Assertions.assertEquals(ErrorMessages.INVALID_INPUT, result.getErrorMessage(),
                "Expected invalid input error message");
    }

    /**
     * Tests that validateCalculationInput returns an invalid result when CalculationInput has invalid principal.
     */
    @Test
    @DisplayName("Should return invalid result when CalculationInput has invalid principal")
    void testValidateCalculationInput_withInvalidPrincipal_returnsInvalidResult() {
        // Test with principal below minimum
        CalculationInput belowMinPrincipalInput = new CalculationInput(
                CalculationConstants.MIN_PRINCIPAL_AMOUNT.subtract(BigDecimal.ONE), // below min
                5 // valid duration
        );
        
        ValidationResult belowMinResult = validationService.validateCalculationInput(belowMinPrincipalInput);
        
        Assertions.assertFalse(belowMinResult.isValid(), "Expected invalid result for principal below minimum");
        Assertions.assertEquals(ErrorMessages.PRINCIPAL_MIN_REQUIRED, belowMinResult.getErrorMessage(),
                "Expected minimum principal error message");
        
        // Test with principal above maximum
        CalculationInput aboveMaxPrincipalInput = new CalculationInput(
                CalculationConstants.MAX_PRINCIPAL_AMOUNT.add(BigDecimal.ONE), // above max
                5 // valid duration
        );
        
        ValidationResult aboveMaxResult = validationService.validateCalculationInput(aboveMaxPrincipalInput);
        
        Assertions.assertFalse(aboveMaxResult.isValid(), "Expected invalid result for principal above maximum");
        Assertions.assertEquals(ErrorMessages.PRINCIPAL_MAX_EXCEEDED, aboveMaxResult.getErrorMessage(),
                "Expected maximum principal error message");
    }

    /**
     * Tests that validateCalculationInput returns an invalid result when CalculationInput has invalid duration.
     */
    @Test
    @DisplayName("Should return invalid result when CalculationInput has invalid duration")
    void testValidateCalculationInput_withInvalidDuration_returnsInvalidResult() {
        // Test with duration below minimum
        CalculationInput belowMinDurationInput = new CalculationInput(
                new BigDecimal("10000"), // valid principal
                CalculationConstants.MIN_DURATION_YEARS - 1 // below min
        );
        
        ValidationResult belowMinResult = validationService.validateCalculationInput(belowMinDurationInput);
        
        Assertions.assertFalse(belowMinResult.isValid(), "Expected invalid result for duration below minimum");
        Assertions.assertEquals(ErrorMessages.DURATION_MIN_REQUIRED, belowMinResult.getErrorMessage(),
                "Expected minimum duration error message");
        
        // Test with duration above maximum
        CalculationInput aboveMaxDurationInput = new CalculationInput(
                new BigDecimal("10000"), // valid principal
                CalculationConstants.MAX_DURATION_YEARS + 1 // above max
        );
        
        ValidationResult aboveMaxResult = validationService.validateCalculationInput(aboveMaxDurationInput);
        
        Assertions.assertFalse(aboveMaxResult.isValid(), "Expected invalid result for duration above maximum");
        Assertions.assertEquals(ErrorMessages.DURATION_MAX_EXCEEDED, aboveMaxResult.getErrorMessage(),
                "Expected maximum duration error message");
    }
}