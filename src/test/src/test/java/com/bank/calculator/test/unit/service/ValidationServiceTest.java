package com.bank.calculator.test.unit.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.EmptySource;

import java.math.BigDecimal;

import com.bank.calculator.service.ValidationService;
import com.bank.calculator.service.impl.ValidationServiceImpl;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.constant.ErrorMessages;
import com.bank.calculator.test.category.UnitTest;
import com.bank.calculator.test.util.TestUtils;

/**
 * Unit test class for the ValidationService component that validates user inputs
 * in the Compound Interest Calculator application.
 */
@UnitTest
@DisplayName("Validation Service Tests")
class ValidationServiceTest {

    private ValidationService validationService;

    @BeforeEach
    void setUp() {
        validationService = new ValidationServiceImpl();
        TestUtils.logTestInfo("Setting up ValidationServiceTest");
    }

    // Tests for validatePrincipal method
    
    @Test
    @DisplayName("Should validate valid principal amount")
    void testValidatePrincipal_withValidInput_returnsValid() {
        String[] validPrincipals = {"1000", "5000", "10000.50", "999999.99"};
        
        for (String principal : validPrincipals) {
            ValidationResult result = validationService.validatePrincipal(principal);
            TestUtils.assertValidationResult(result, true, null);
        }
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"abc", "-1000", "1000.123"})
    @DisplayName("Should reject principal amount with invalid format")
    void testValidatePrincipal_withInvalidFormat_returnsInvalid(String invalidPrincipal) {
        ValidationResult result = validationService.validatePrincipal(invalidPrincipal);
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_FORMAT);
    }
    
    @Test
    @DisplayName("Should reject zero principal amount")
    void testValidatePrincipal_withZero_returnsInvalid() {
        ValidationResult result = validationService.validatePrincipal("0");
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_POSITIVE);
    }
    
    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Should reject null or empty principal amount")
    void testValidatePrincipal_withNullOrEmpty_returnsInvalid(String principalStr) {
        ValidationResult result = validationService.validatePrincipal(principalStr);
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_REQUIRED);
    }
    
    @Test
    @DisplayName("Should reject principal amount below minimum")
    void testValidatePrincipal_withBelowMinimum_returnsInvalid() {
        ValidationResult result = validationService.validatePrincipal("500");
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_MIN_REQUIRED);
    }
    
    @Test
    @DisplayName("Should reject principal amount above maximum")
    void testValidatePrincipal_withAboveMaximum_returnsInvalid() {
        ValidationResult result = validationService.validatePrincipal("1500000");
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_MAX_EXCEEDED);
    }
    
    // Tests for validateDuration method
    
    @Test
    @DisplayName("Should validate valid loan duration")
    void testValidateDuration_withValidInput_returnsValid() {
        String[] validDurations = {"1", "5", "10", "30"};
        
        for (String duration : validDurations) {
            ValidationResult result = validationService.validateDuration(duration);
            TestUtils.assertValidationResult(result, true, null);
        }
    }
    
    @ParameterizedTest
    @ValueSource(strings = {"abc", "-1", "2.5"})
    @DisplayName("Should reject loan duration with invalid format")
    void testValidateDuration_withInvalidInput_returnsInvalid(String invalidDuration) {
        ValidationResult result = validationService.validateDuration(invalidDuration);
        TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_FORMAT);
    }
    
    @Test
    @DisplayName("Should reject zero loan duration")
    void testValidateDuration_withZero_returnsInvalid() {
        ValidationResult result = validationService.validateDuration("0");
        TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_POSITIVE);
    }
    
    @ParameterizedTest
    @NullSource
    @EmptySource
    @DisplayName("Should reject null or empty loan duration")
    void testValidateDuration_withNullOrEmpty_returnsInvalid(String durationStr) {
        ValidationResult result = validationService.validateDuration(durationStr);
        TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_REQUIRED);
    }
    
    @Test
    @DisplayName("Should reject loan duration above maximum")
    void testValidateDuration_withAboveMaximum_returnsInvalid() {
        ValidationResult result = validationService.validateDuration("31");
        TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_MAX_EXCEEDED);
    }
    
    // Tests for validateAllInputs method
    
    @Test
    @DisplayName("Should validate when all inputs are valid")
    void testValidateAllInputs_withAllValid_returnsValid() {
        ValidationResult result = validationService.validateAllInputs("10000", "5");
        TestUtils.assertValidationResult(result, true, null);
    }
    
    @Test
    @DisplayName("Should reject when principal is invalid")
    void testValidateAllInputs_withInvalidPrincipal_returnsInvalid() {
        ValidationResult result = validationService.validateAllInputs("0", "5");
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_POSITIVE);
    }
    
    @Test
    @DisplayName("Should reject when duration is invalid")
    void testValidateAllInputs_withInvalidDuration_returnsInvalid() {
        ValidationResult result = validationService.validateAllInputs("10000", "0");
        TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_POSITIVE);
    }
    
    // Tests for validateCalculationInput method
    
    @Test
    @DisplayName("Should validate valid CalculationInput")
    void testValidateCalculationInput_withValidInput_returnsValid() {
        CalculationInput input = new CalculationInput(new BigDecimal("10000"), 5);
        ValidationResult result = validationService.validateCalculationInput(input);
        TestUtils.assertValidationResult(result, true, null);
    }
    
    @Test
    @DisplayName("Should reject null CalculationInput")
    void testValidateCalculationInput_withNullInput_returnsInvalid() {
        ValidationResult result = validationService.validateCalculationInput(null);
        TestUtils.assertValidationResult(result, false, ErrorMessages.INVALID_INPUT);
    }
    
    @Test
    @DisplayName("Should reject CalculationInput with invalid principal")
    void testValidateCalculationInput_withInvalidPrincipal_returnsInvalid() {
        CalculationInput input = new CalculationInput(new BigDecimal("500"), 5);
        ValidationResult result = validationService.validateCalculationInput(input);
        TestUtils.assertValidationResult(result, false, ErrorMessages.PRINCIPAL_MIN_REQUIRED);
    }
    
    @Test
    @DisplayName("Should reject CalculationInput with invalid duration")
    void testValidateCalculationInput_withInvalidDuration_returnsInvalid() {
        CalculationInput input = new CalculationInput(new BigDecimal("10000"), 31);
        ValidationResult result = validationService.validateCalculationInput(input);
        TestUtils.assertValidationResult(result, false, ErrorMessages.DURATION_MAX_EXCEEDED);
    }
}