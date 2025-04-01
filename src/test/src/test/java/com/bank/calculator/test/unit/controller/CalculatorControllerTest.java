package com.bank.calculator.test.unit.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.ArgumentCaptor;
import org.mockito.ArgumentMatchers;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.lang.RuntimeException;

import com.bank.calculator.controller.CalculatorController;
import com.bank.calculator.service.ValidationService;
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.exception.ValidationException;
import com.bank.calculator.exception.CalculationException;
import com.bank.calculator.util.CurrencyUtils;
import com.bank.calculator.test.category.UnitTest;
import com.bank.calculator.test.util.TestUtils;

@DisplayName("Calculator Controller Unit Tests")
public class CalculatorControllerTest implements UnitTest {

    @Mock
    private ValidationService validationService;
    
    @Mock
    private CalculationService calculationService;
    
    @InjectMocks
    private CalculatorController controller;
    
    private static final String VALID_PRINCIPAL = "5000";
    private static final String VALID_DURATION = "5";
    private static final String INVALID_PRINCIPAL = "-100";
    private static final String INVALID_DURATION = "0";
    private static final String PRINCIPAL_ERROR_MESSAGE = "Principal amount must be positive";
    private static final String DURATION_ERROR_MESSAGE = "Loan duration must be a positive integer";
    private static final BigDecimal EXPECTED_EMI_AMOUNT = new BigDecimal("100.00");
    private static final String FORMATTED_EMI = "$100.00";
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    
    @AfterEach
    void tearDown() {
        Mockito.verifyNoMoreInteractions(validationService, calculationService);
        Mockito.reset(validationService, calculationService);
    }
    
    @Test
    @DisplayName("Should return valid result when inputs are valid")
    void testValidateInputs_WithValidInputs_ReturnsValidResult() {
        // Setup
        ValidationResult validResult = ValidationResult.createValid();
        Mockito.when(validationService.validateAllInputs(VALID_PRINCIPAL, VALID_DURATION))
               .thenReturn(validResult);
        
        // Execute
        ValidationResult result = controller.validateInputs(VALID_PRINCIPAL, VALID_DURATION);
        
        // Verify
        Mockito.verify(validationService).validateAllInputs(VALID_PRINCIPAL, VALID_DURATION);
        TestUtils.assertValidationResult(result, true, null);
    }
    
    @Test
    @DisplayName("Should return invalid result when principal is invalid")
    void testValidateInputs_WithInvalidPrincipal_ReturnsInvalidResult() {
        // Setup
        ValidationResult invalidResult = ValidationResult.createInvalid(PRINCIPAL_ERROR_MESSAGE);
        Mockito.when(validationService.validateAllInputs(INVALID_PRINCIPAL, VALID_DURATION))
               .thenReturn(invalidResult);
        
        // Execute
        ValidationResult result = controller.validateInputs(INVALID_PRINCIPAL, VALID_DURATION);
        
        // Verify
        Mockito.verify(validationService).validateAllInputs(INVALID_PRINCIPAL, VALID_DURATION);
        TestUtils.assertValidationResult(result, false, PRINCIPAL_ERROR_MESSAGE);
    }
    
    @Test
    @DisplayName("Should return invalid result when duration is invalid")
    void testValidateInputs_WithInvalidDuration_ReturnsInvalidResult() {
        // Setup
        ValidationResult invalidResult = ValidationResult.createInvalid(DURATION_ERROR_MESSAGE);
        Mockito.when(validationService.validateAllInputs(VALID_PRINCIPAL, INVALID_DURATION))
               .thenReturn(invalidResult);
        
        // Execute
        ValidationResult result = controller.validateInputs(VALID_PRINCIPAL, INVALID_DURATION);
        
        // Verify
        Mockito.verify(validationService).validateAllInputs(VALID_PRINCIPAL, INVALID_DURATION);
        TestUtils.assertValidationResult(result, false, DURATION_ERROR_MESSAGE);
    }
    
    @Test
    @DisplayName("Should return calculation result when inputs are valid")
    void testCalculateEMI_WithValidInputs_ReturnsCalculationResult() {
        // Setup
        ValidationResult validResult = ValidationResult.createValid();
        Mockito.when(validationService.validateAllInputs(VALID_PRINCIPAL, VALID_DURATION))
               .thenReturn(validResult);
        
        // Create a calculation result with the expected EMI amount
        CalculationResult expectedResult = Mockito.mock(CalculationResult.class);
        Mockito.when(expectedResult.getEmiAmount()).thenReturn(EXPECTED_EMI_AMOUNT);
        
        // Set up the calculation service to return the expected result
        Mockito.when(calculationService.calculateEMI(Mockito.any(CalculationInput.class)))
               .thenReturn(expectedResult);
        
        // Execute
        CalculationResult result = controller.calculateEMI(VALID_PRINCIPAL, VALID_DURATION);
        
        // Verify
        Mockito.verify(validationService).validateAllInputs(VALID_PRINCIPAL, VALID_DURATION);
        
        // Capture the CalculationInput passed to the service
        ArgumentCaptor<CalculationInput> inputCaptor = ArgumentCaptor.forClass(CalculationInput.class);
        Mockito.verify(calculationService).calculateEMI(inputCaptor.capture());
        
        // Verify the input values were correctly parsed and passed
        CalculationInput capturedInput = inputCaptor.getValue();
        Assertions.assertEquals(new BigDecimal(VALID_PRINCIPAL), capturedInput.getPrincipal());
        Assertions.assertEquals(Integer.parseInt(VALID_DURATION), capturedInput.getDurationYears());
        
        // Verify the result
        Assertions.assertSame(expectedResult, result);
        TestUtils.assertBigDecimalEquals(EXPECTED_EMI_AMOUNT, result.getEmiAmount());
    }
    
    @Test
    @DisplayName("Should throw ValidationException when inputs are invalid")
    void testCalculateEMI_WithInvalidInputs_ThrowsValidationException() {
        // Setup
        String errorMessage = "Invalid input";
        ValidationResult invalidResult = ValidationResult.createInvalid(errorMessage);
        Mockito.when(validationService.validateAllInputs(INVALID_PRINCIPAL, INVALID_DURATION))
               .thenReturn(invalidResult);
        
        // Execute and Verify
        ValidationException exception = Assertions.assertThrows(
            ValidationException.class,
            () -> controller.calculateEMI(INVALID_PRINCIPAL, INVALID_DURATION)
        );
        
        // Verify validation service was called but calculation service was not
        Mockito.verify(validationService).validateAllInputs(INVALID_PRINCIPAL, INVALID_DURATION);
        
        // Verify exception details
        Assertions.assertEquals(errorMessage, exception.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should throw CalculationException when calculation service throws exception")
    void testCalculateEMI_WhenCalculationServiceThrowsException_ThrowsCalculationException() {
        // Setup
        ValidationResult validResult = ValidationResult.createValid();
        Mockito.when(validationService.validateAllInputs(VALID_PRINCIPAL, VALID_DURATION))
               .thenReturn(validResult);
        
        // Set up calculation service to throw exception
        Mockito.when(calculationService.calculateEMI(Mockito.any(CalculationInput.class)))
               .thenThrow(new RuntimeException("Calculation error"));
        
        // Execute and Verify
        CalculationException exception = Assertions.assertThrows(
            CalculationException.class,
            () -> controller.calculateEMI(VALID_PRINCIPAL, VALID_DURATION)
        );
        
        // Verify services were called
        Mockito.verify(validationService).validateAllInputs(VALID_PRINCIPAL, VALID_DURATION);
        Mockito.verify(calculationService).calculateEMI(Mockito.any(CalculationInput.class));
        
        // Verify exception contains expected message
        Assertions.assertTrue(exception.getMessage().contains("unexpected error"));
    }
    
    @Test
    @DisplayName("Should return formatted currency string for valid result")
    void testFormatResult_WithValidResult_ReturnsFormattedString() {
        // Setup
        CalculationResult result = Mockito.mock(CalculationResult.class);
        Mockito.when(result.getFormattedEmiAmount()).thenReturn(FORMATTED_EMI);
        
        // Execute
        String formattedResult = controller.formatResult(result);
        
        // Verify
        Assertions.assertEquals(FORMATTED_EMI, formattedResult);
    }
    
    @Test
    @DisplayName("Should throw NullPointerException when result is null")
    void testFormatResult_WithNullResult_ThrowsNullPointerException() {
        // Execute and Verify
        Assertions.assertThrows(
            NullPointerException.class,
            () -> controller.formatResult(null)
        );
    }
}