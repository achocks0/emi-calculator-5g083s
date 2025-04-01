package com.bank.calculator.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal; // JDK 11

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.bank.calculator.service.ValidationService;
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.exception.ValidationException;
import com.bank.calculator.exception.CalculationException;
import com.bank.calculator.util.CurrencyUtils;

/**
 * Unit test class for CalculatorController that verifies the controller's functionality 
 * for input validation, EMI calculation, and result formatting.
 */
public class CalculatorControllerTest {
    
    @Mock
    private ValidationService validationService;
    
    @Mock
    private CalculationService calculationService;
    
    @InjectMocks
    private CalculatorController controller;
    
    private String validPrincipalStr;
    private String validDurationStr;
    private String invalidPrincipalStr;
    private String invalidDurationStr;
    private BigDecimal validPrincipal;
    private int validDuration;
    private String principalErrorMessage;
    private String durationErrorMessage;
    
    @BeforeEach
    void setUp() {
        // Initialize mocks
        MockitoAnnotations.openMocks(this);
        
        // Set up test data
        validPrincipalStr = "5000.00";
        validDurationStr = "5";
        invalidPrincipalStr = "-1000";
        invalidDurationStr = "0";
        validPrincipal = new BigDecimal("5000.00");
        validDuration = 5;
        principalErrorMessage = "Principal amount must be a positive number";
        durationErrorMessage = "Loan duration must be a positive whole number";
    }
    
    @Test
    @DisplayName("Should return valid ValidationResult when inputs are valid")
    void testValidateInputsWithValidInputs() {
        // Arrange
        when(validationService.validateAllInputs(validPrincipalStr, validDurationStr))
            .thenReturn(ValidationResult.createValid());
        
        // Act
        ValidationResult result = controller.validateInputs(validPrincipalStr, validDurationStr);
        
        // Assert
        verify(validationService).validateAllInputs(validPrincipalStr, validDurationStr);
        assertTrue(result.isValid());
        assertNull(result.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid ValidationResult when principal is invalid")
    void testValidateInputsWithInvalidPrincipal() {
        // Arrange
        when(validationService.validateAllInputs(invalidPrincipalStr, validDurationStr))
            .thenReturn(ValidationResult.createInvalid(principalErrorMessage));
        
        // Act
        ValidationResult result = controller.validateInputs(invalidPrincipalStr, validDurationStr);
        
        // Assert
        verify(validationService).validateAllInputs(invalidPrincipalStr, validDurationStr);
        assertFalse(result.isValid());
        assertEquals(principalErrorMessage, result.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return invalid ValidationResult when duration is invalid")
    void testValidateInputsWithInvalidDuration() {
        // Arrange
        when(validationService.validateAllInputs(validPrincipalStr, invalidDurationStr))
            .thenReturn(ValidationResult.createInvalid(durationErrorMessage));
        
        // Act
        ValidationResult result = controller.validateInputs(validPrincipalStr, invalidDurationStr);
        
        // Assert
        verify(validationService).validateAllInputs(validPrincipalStr, invalidDurationStr);
        assertFalse(result.isValid());
        assertEquals(durationErrorMessage, result.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return correct CalculationResult when inputs are valid")
    void testCalculateEMIWithValidInputs() {
        // Arrange
        when(validationService.validateAllInputs(validPrincipalStr, validDurationStr))
            .thenReturn(ValidationResult.createValid());
        
        BigDecimal emiAmount = new BigDecimal("100.50");
        CalculationResult mockResult = mock(CalculationResult.class);
        when(mockResult.getEmiAmount()).thenReturn(emiAmount);
        
        when(calculationService.calculateEMI(any(CalculationInput.class)))
            .thenReturn(mockResult);
        
        // Act
        CalculationResult result = controller.calculateEMI(validPrincipalStr, validDurationStr);
        
        // Assert
        verify(validationService).validateAllInputs(validPrincipalStr, validDurationStr);
        
        ArgumentCaptor<CalculationInput> inputCaptor = ArgumentCaptor.forClass(CalculationInput.class);
        verify(calculationService).calculateEMI(inputCaptor.capture());
        
        CalculationInput capturedInput = inputCaptor.getValue();
        assertEquals(validPrincipal, capturedInput.getPrincipal());
        assertEquals(validDuration, capturedInput.getDurationYears());
        
        assertEquals(emiAmount, result.getEmiAmount());
    }
    
    @Test
    @DisplayName("Should throw ValidationException when inputs are invalid")
    void testCalculateEMIWithInvalidInputs() {
        // Arrange
        when(validationService.validateAllInputs(invalidPrincipalStr, validDurationStr))
            .thenReturn(ValidationResult.createInvalid(principalErrorMessage));
        
        // Act & Assert
        ValidationException exception = assertThrows(ValidationException.class, 
            () -> controller.calculateEMI(invalidPrincipalStr, validDurationStr));
        
        assertEquals(principalErrorMessage, exception.getErrorMessage());
        verify(calculationService, never()).calculateEMI(any(CalculationInput.class));
    }
    
    @Test
    @DisplayName("Should handle calculation errors correctly")
    void testCalculateEMIWithCalculationError() {
        // Arrange
        when(validationService.validateAllInputs(validPrincipalStr, validDurationStr))
            .thenReturn(ValidationResult.createValid());
        
        when(calculationService.calculateEMI(any(CalculationInput.class)))
            .thenThrow(new CalculationException("Calculation error"));
        
        // Act & Assert
        CalculationException exception = assertThrows(CalculationException.class, 
            () -> controller.calculateEMI(validPrincipalStr, validDurationStr));
        
        assertEquals("Calculation error", exception.getErrorMessage());
    }
    
    @Test
    @DisplayName("Should return correctly formatted string for valid result")
    void testFormatResultWithValidResult() {
        // Arrange
        CalculationResult mockResult = mock(CalculationResult.class);
        when(mockResult.getFormattedEmiAmount()).thenReturn("$100.50");
        
        // Act
        String formattedResult = controller.formatResult(mockResult);
        
        // Assert
        assertEquals("$100.50", formattedResult);
    }
    
    @Test
    @DisplayName("Should throw NullPointerException when result is null")
    void testFormatResultWithNullResult() {
        // Act & Assert
        assertThrows(NullPointerException.class, () -> controller.formatResult(null));
    }
}