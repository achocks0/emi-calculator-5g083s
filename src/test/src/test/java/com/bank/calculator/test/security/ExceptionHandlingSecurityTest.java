package com.bank.calculator.test.security;

import com.bank.calculator.test.category.SecurityTest;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.exception.CalculationException;
import com.bank.calculator.exception.ValidationException;
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.service.ValidationService;
import com.bank.calculator.controller.CalculatorController;
import com.bank.calculator.model.ValidationResult;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

/**
 * Security test class that verifies the application's exception handling mechanisms from a security perspective.
 * Tests ensure that exceptions are properly caught, do not leak sensitive information, and maintain
 * application security when unexpected inputs or error conditions occur.
 */
@SecurityTest
@DisplayName("Exception Handling Security Tests")
public class ExceptionHandlingSecurityTest {

    @Mock
    private ValidationService validationService;
    
    @Mock
    private CalculationService calculationService;
    
    @InjectMocks
    private CalculatorController controller;
    
    /**
     * Set up test environment before each test
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        TestUtils.logTestInfo("Setting up ExceptionHandlingSecurityTest");
    }
    
    /**
     * Tests that ValidationException is properly handled and does not leak sensitive information
     */
    @Test
    @DisplayName("Should handle ValidationException securely")
    void testValidationExceptionHandling() {
        // Set up mock validation service to throw ValidationException
        Mockito.when(validationService.validateAllInputs(Mockito.anyString(), Mockito.anyString()))
               .thenThrow(new ValidationException("E001", "Test validation error"));
        
        // Attempt to call controller.calculateEMI with invalid inputs
        Exception exception = Assertions.assertThrows(
            ValidationException.class, 
            () -> controller.calculateEMI("1000", "5")
        );
        
        // Verify that exception is caught and handled properly
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof ValidationException);
        
        ValidationException validationException = (ValidationException) exception;
        
        // Verify that error code and message do not contain sensitive information
        Assertions.assertEquals("E001", validationException.getErrorCode());
        Assertions.assertFalse(validationException.getErrorMessage().contains("SQL"));
        Assertions.assertFalse(validationException.getErrorMessage().contains("Exception:"));
        Assertions.assertFalse(validationException.getErrorMessage().contains("stack trace"));
        Assertions.assertFalse(validationException.getErrorMessage().contains("at com.bank"));
        
        // Verify that stack trace is not exposed to the user interface
        Assertions.assertFalse(validationException.getErrorMessage().contains("/"));
        Assertions.assertFalse(validationException.getErrorMessage().contains("\\"));
        Assertions.assertFalse(validationException.getErrorMessage().contains("@"));
    }
    
    /**
     * Tests that CalculationException is properly handled and does not leak sensitive information
     */
    @Test
    @DisplayName("Should handle CalculationException securely")
    void testCalculationExceptionHandling() {
        // Set up mock validation service to return valid result
        ValidationResult validResult = ValidationResult.createValid();
        Mockito.when(validationService.validateAllInputs(Mockito.anyString(), Mockito.anyString()))
               .thenReturn(validResult);
        
        // Set up mock calculation service to throw CalculationException
        Mockito.when(calculationService.calculateEMI(Mockito.any()))
               .thenThrow(new CalculationException("E003", "Test calculation error"));
        
        // Attempt to call controller.calculateEMI with valid inputs
        Exception exception = Assertions.assertThrows(
            CalculationException.class, 
            () -> controller.calculateEMI("1000", "5")
        );
        
        // Verify that exception is caught and handled properly
        Assertions.assertNotNull(exception);
        Assertions.assertTrue(exception instanceof CalculationException);
        
        CalculationException calculationException = (CalculationException) exception;
        
        // Verify that error code and message do not contain sensitive information
        Assertions.assertEquals("E003", calculationException.getErrorCode());
        Assertions.assertFalse(calculationException.getErrorMessage().contains("SQL"));
        Assertions.assertFalse(calculationException.getErrorMessage().contains("Exception:"));
        Assertions.assertFalse(calculationException.getErrorMessage().contains("stack trace"));
        Assertions.assertFalse(calculationException.getErrorMessage().contains("at com.bank"));
        
        // Verify that stack trace is not exposed to the user interface
        Assertions.assertFalse(calculationException.getErrorMessage().contains("/"));
        Assertions.assertFalse(calculationException.getErrorMessage().contains("\\"));
        Assertions.assertFalse(calculationException.getErrorMessage().contains("@"));
    }
    
    /**
     * Tests that unexpected RuntimeExceptions are properly handled and do not leak sensitive information
     */
    @Test
    @DisplayName("Should handle unexpected RuntimeException securely")
    void testRuntimeExceptionHandling() {
        // Set up mock validation service to throw RuntimeException
        Mockito.when(validationService.validateAllInputs(Mockito.anyString(), Mockito.anyString()))
               .thenThrow(new RuntimeException("Unexpected internal error with sensitive/path/details.java:45"));
        
        // Attempt to call controller.calculateEMI with inputs
        Exception exception = Assertions.assertThrows(
            Exception.class, 
            () -> controller.calculateEMI("1000", "5")
        );
        
        // Verify that exception is caught and wrapped in appropriate application exception
        Assertions.assertNotNull(exception);
        
        // Verify that error message is generic and does not expose internal details
        Assertions.assertFalse(exception.getMessage().contains("sensitive/path"));
        Assertions.assertFalse(exception.getMessage().contains(".java:"));
        
        // Check that any of the controller's exceptions are used to wrap the original exception
        boolean isAppropriateException = exception instanceof ValidationException || 
                                        exception instanceof CalculationException;
        Assertions.assertTrue(isAppropriateException, "Exception should be wrapped in an application-specific exception");
    }
    
    /**
     * Tests that exception messages are sanitized and do not contain potentially sensitive information
     */
    @Test
    @DisplayName("Should sanitize exception messages")
    void testExceptionMessageSanitization() {
        // Set up mock validation service to throw exceptions with sensitive information
        Mockito.when(validationService.validateAllInputs(Mockito.eq("sql-injection"), Mockito.anyString()))
               .thenThrow(new RuntimeException("Error in SQL: SELECT * FROM users"));
        
        Mockito.when(validationService.validateAllInputs(Mockito.eq("stack-trace"), Mockito.anyString()))
               .thenThrow(new RuntimeException("Error in app\njava.lang.NullPointerException\n at com.bank.App.method(App.java:45)"));
        
        Mockito.when(validationService.validateAllInputs(Mockito.eq("file-path"), Mockito.anyString()))
               .thenThrow(new RuntimeException("Could not find file at C:\\Users\\admin\\config.properties"));
        
        Mockito.when(validationService.validateAllInputs(Mockito.eq("memory-address"), Mockito.anyString()))
               .thenThrow(new RuntimeException("Object instance User@3f8d3a72 is null"));
        
        // Test cases with different types of sensitive information
        String[][] testCases = {
            {"sql-injection", "SQL"},
            {"stack-trace", "at com.bank"},
            {"file-path", "C:\\Users"},
            {"memory-address", "@3f8d3a72"}
        };
        
        for (String[] testCase : testCases) {
            String input = testCase[0];
            String sensitiveContent = testCase[1];
            
            Exception exception = Assertions.assertThrows(
                Exception.class,
                () -> controller.calculateEMI(input, "5")
            );
            
            // Verify that the controller's exception handling sanitizes the message
            Assertions.assertFalse(
                exception.getMessage().contains(sensitiveContent),
                "Exception message should not contain sensitive content: " + sensitiveContent
            );
        }
    }
    
    /**
     * Tests that exceptions are properly logged without exposing sensitive information in user-facing components
     */
    @Test
    @DisplayName("Should log exceptions appropriately")
    void testExceptionLogging() {
        // Set up mock services to throw exceptions
        ValidationException validationException = new ValidationException("E001", "Test validation error");
        Mockito.when(validationService.validateAllInputs(Mockito.anyString(), Mockito.anyString()))
               .thenThrow(validationException);
        
        // Attempt operations that will cause exceptions
        Exception caughtException = null;
        try {
            controller.calculateEMI("invalid", "data");
        } catch (Exception e) {
            caughtException = e;
            TestUtils.logTestError("Expected exception during test", e);
        }
        
        // Verify exception was caught and is the expected type
        Assertions.assertNotNull(caughtException, "An exception should have been thrown");
        Assertions.assertTrue(caughtException instanceof ValidationException, 
                             "Exception should be of type ValidationException");
        
        // Verify the exception contains appropriate error information but no sensitive details
        ValidationException ve = (ValidationException) caughtException;
        Assertions.assertEquals("E001", ve.getErrorCode(), "Should have correct error code");
        Assertions.assertNotNull(ve.getErrorMessage(), "Should have an error message");
        Assertions.assertFalse(ve.getErrorMessage().contains("Exception:"), 
                              "Error message should not contain raw exception details");
                              
        // Test calculation exception logging
        CalculationException calculationException = new CalculationException("E003", "Test calculation error");
        Mockito.when(validationService.validateAllInputs(Mockito.anyString(), Mockito.anyString()))
               .thenReturn(ValidationResult.createValid());
        Mockito.when(calculationService.calculateEMI(Mockito.any()))
               .thenThrow(calculationException);
               
        caughtException = null;
        try {
            controller.calculateEMI("1000", "5");
        } catch (Exception e) {
            caughtException = e;
            TestUtils.logTestError("Expected calculation exception during test", e);
        }
        
        // Verify calculation exception handling
        Assertions.assertNotNull(caughtException, "A calculation exception should have been thrown");
        Assertions.assertTrue(caughtException instanceof CalculationException, 
                             "Exception should be of type CalculationException");
        
        CalculationException ce = (CalculationException) caughtException;
        Assertions.assertEquals("E003", ce.getErrorCode(), "Should have correct calculation error code");
        Assertions.assertFalse(ce.getErrorMessage().contains("stack trace"), 
                              "Error message should not contain stack trace information");
    }
    
    /**
     * Tests that extreme inputs that might cause exceptions are properly handled
     */
    @Test
    @DisplayName("Should handle extreme input values securely")
    void testExtremeInputExceptionHandling() {
        // Test with extremely large numbers
        String extremelyLargePrincipal = "999999999999999999999999999999";
        ValidationResult largeResult = new ValidationResult(false, "Principal amount exceeds maximum allowed value");
        Mockito.when(validationService.validateAllInputs(Mockito.eq(extremelyLargePrincipal), Mockito.anyString()))
               .thenReturn(largeResult);
        
        Exception largeException = Assertions.assertThrows(
            ValidationException.class, 
            () -> controller.calculateEMI(extremelyLargePrincipal, "5")
        );
        
        // Test with extremely small numbers
        String extremelySmallPrincipal = "0.00000000000000000001";
        ValidationResult smallResult = new ValidationResult(false, "Principal amount must be greater than zero");
        Mockito.when(validationService.validateAllInputs(Mockito.eq(extremelySmallPrincipal), Mockito.anyString()))
               .thenReturn(smallResult);
        
        Exception smallException = Assertions.assertThrows(
            ValidationException.class, 
            () -> controller.calculateEMI(extremelySmallPrincipal, "5")
        );
        
        // Test with maximum integer values
        String maximumIntegerValue = String.valueOf(Integer.MAX_VALUE);
        ValidationResult maxIntResult = ValidationResult.createValid();
        Mockito.when(validationService.validateAllInputs(Mockito.anyString(), Mockito.eq(maximumIntegerValue)))
               .thenReturn(maxIntResult);
        
        // In this case, the calculation might fail with large numbers
        CalculationException calcException = new CalculationException("Numeric overflow occurred during calculation");
        Mockito.when(calculationService.calculateEMI(Mockito.any()))
               .thenThrow(calcException);
        
        Exception maxException = Assertions.assertThrows(
            CalculationException.class, 
            () -> controller.calculateEMI("1000", maximumIntegerValue)
        );
        
        // Verify that all these exceptions are properly handled without leaking sensitive information
        Assertions.assertFalse(largeException.getMessage().contains("SQL"));
        Assertions.assertFalse(smallException.getMessage().contains("Exception:"));
        Assertions.assertFalse(maxException.getMessage().contains("stack trace"));
    }
}