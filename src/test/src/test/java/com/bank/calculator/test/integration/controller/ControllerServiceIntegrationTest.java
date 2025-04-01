package com.bank.calculator.test.integration.controller;

import com.bank.calculator.controller.CalculatorController;
import com.bank.calculator.service.ValidationService;
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.service.impl.ValidationServiceImpl;
import com.bank.calculator.service.impl.CalculationServiceImpl;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.exception.ValidationException;
import com.bank.calculator.exception.CalculationException;
import com.bank.calculator.test.category.IntegrationTest;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.fixture.CalculationTestFixture;
import com.bank.calculator.test.fixture.CalculationTestFixture.TestCase;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;

/**
 * Integration test class that verifies the interaction between the CalculatorController 
 * and its dependent services (ValidationService and CalculationService).
 * This test ensures that the controller correctly coordinates with these services to
 * validate inputs, perform calculations, and format results.
 */
@IntegrationTest
public class ControllerServiceIntegrationTest {
    
    private ValidationService validationService;
    private CalculationService calculationService;
    private CalculatorController controller;
    
    @BeforeEach
    void setUp() {
        validationService = new ValidationServiceImpl();
        calculationService = new CalculationServiceImpl();
        controller = new CalculatorController(validationService, calculationService);
        TestUtils.logTestInfo("Test setup complete: initialized services and controller");
    }
    
    @Test
    @DisplayName("Should validate valid inputs correctly")
    void testValidateInputsWithValidInputs() {
        String principalStr = "10000.00";
        String durationStr = "5";
        
        ValidationResult result = controller.validateInputs(principalStr, durationStr);
        
        TestUtils.assertValidationResult(result, true, null);
    }
    
    @Test
    @DisplayName("Should detect invalid principal amount")
    void testValidateInputsWithInvalidPrincipal() {
        String principalStr = "-1000";
        String durationStr = "5";
        
        ValidationResult result = controller.validateInputs(principalStr, durationStr);
        
        TestUtils.assertValidationResult(result, false, "principal");
    }
    
    @Test
    @DisplayName("Should detect invalid loan duration")
    void testValidateInputsWithInvalidDuration() {
        String principalStr = "10000.00";
        String durationStr = "0";
        
        ValidationResult result = controller.validateInputs(principalStr, durationStr);
        
        TestUtils.assertValidationResult(result, false, "duration");
    }
    
    @Test
    @DisplayName("Should calculate EMI correctly with valid inputs")
    void testCalculateEMIWithValidInputs() {
        String principalStr = "10000.00";
        String durationStr = "5";
        
        CalculationResult result = controller.calculateEMI(principalStr, durationStr);
        
        Assertions.assertNotNull(result, "Calculation result should not be null");
        TestUtils.assertBigDecimalEquals(new BigDecimal("200.38"), result.getEmiAmount());
        Assertions.assertTrue(result.getFormattedEmiAmount().contains("$"), 
                "Formatted EMI should contain currency symbol");
    }
    
    @Test
    @DisplayName("Should throw ValidationException when calculating with invalid inputs")
    void testCalculateEMIWithInvalidInputs() {
        String principalStr = "-1000";
        String durationStr = "5";
        
        ValidationException exception = assertThrows(ValidationException.class, 
                () -> controller.calculateEMI(principalStr, durationStr),
                "Should throw ValidationException for invalid inputs");
        
        Assertions.assertNotNull(exception.getErrorMessage(), 
                "Exception should contain error message");
    }
    
    @Test
    @DisplayName("Should format calculation result correctly")
    void testFormatResult() {
        String principalStr = "10000.00";
        String durationStr = "5";
        
        CalculationResult calcResult = controller.calculateEMI(principalStr, durationStr);
        String formattedResult = controller.formatResult(calcResult);
        
        Assertions.assertNotNull(formattedResult, "Formatted result should not be null");
        Assertions.assertTrue(formattedResult.contains("$"), 
                "Formatted result should contain currency symbol");
        Assertions.assertTrue(formattedResult.contains("200.38"), 
                "Formatted result should contain correct amount");
    }
    
    @Test
    @DisplayName("Should perform end-to-end calculation correctly")
    void testEndToEndCalculation() {
        String principalStr = "10000.00";
        String durationStr = "5";
        
        // First validate inputs
        ValidationResult validationResult = controller.validateInputs(principalStr, durationStr);
        TestUtils.assertValidationResult(validationResult, true, null);
        
        // Then calculate EMI
        CalculationResult calculationResult = controller.calculateEMI(principalStr, durationStr);
        TestUtils.assertBigDecimalEquals(new BigDecimal("200.38"), 
                calculationResult.getEmiAmount());
        
        // Finally format the result
        String formattedResult = controller.formatResult(calculationResult);
        Assertions.assertTrue(formattedResult.contains("$200.38"), 
                "Formatted result should show correct amount with currency symbol");
    }
    
    @Test
    @DisplayName("Should handle multiple test cases correctly")
    void testWithMultipleTestCases() {
        List<TestCase> testCases = CalculationTestFixture.createStandardTestCases();
        
        for (TestCase testCase : testCases) {
            // Convert input parameters to strings for controller
            String principalStr = testCase.getInput().getPrincipal().toString();
            String durationStr = String.valueOf(testCase.getInput().getDurationYears());
            
            // Validate inputs
            ValidationResult validationResult = controller.validateInputs(principalStr, durationStr);
            TestUtils.assertValidationResult(validationResult, true, null);
            
            // Calculate EMI
            CalculationResult calculationResult = controller.calculateEMI(principalStr, durationStr);
            TestUtils.assertBigDecimalEquals(testCase.getExpectedEmiAmount(), 
                    calculationResult.getEmiAmount(), 
                    new BigDecimal("0.01"));
            
            // Format result
            String formattedResult = controller.formatResult(calculationResult);
            Assertions.assertNotNull(formattedResult, 
                    "Formatted result should not be null for test case: " + testCase.getId());
            Assertions.assertTrue(formattedResult.contains("$"), 
                    "Formatted result should contain currency symbol");
        }
    }
}