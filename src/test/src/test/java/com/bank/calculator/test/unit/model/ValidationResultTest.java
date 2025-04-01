package com.bank.calculator.test.unit.model;

import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.test.category.UnitTest;
import com.bank.calculator.test.util.TestUtils;

import org.junit.jupiter.api.Test; // JUnit 5.8.2
import org.junit.jupiter.api.DisplayName; // JUnit 5.8.2
import org.junit.jupiter.api.Assertions; // JUnit 5.8.2

import java.lang.NullPointerException; // JDK 11

/**
 * Unit tests for {@link ValidationResult} class.
 * Verifies the functionality of the ValidationResult model including factory methods,
 * validation status checks, error message handling, and object equality.
 */
public class ValidationResultTest implements UnitTest {

    @Test
    @DisplayName("Should create a valid validation result with no error message")
    public void testCreateValid() {
        // Log test execution for debugging
        TestUtils.logTestInfo("Testing ValidationResult.createValid()");
        
        // Create a valid validation result
        ValidationResult result = ValidationResult.createValid();
        
        // Assert that the result is valid
        Assertions.assertTrue(result.isValid(), "Result should be valid");
        
        // Assert that the error message is null
        Assertions.assertNull(result.getErrorMessage(), "Error message should be null for valid result");
        
        // Use TestUtils to verify both conditions in one call
        TestUtils.assertValidationResult(result, true, null);
    }

    @Test
    @DisplayName("Should create an invalid validation result with the specified error message")
    public void testCreateInvalid() {
        // Log test execution for debugging
        TestUtils.logTestInfo("Testing ValidationResult.createInvalid()");
        
        // Define test error message
        String errorMessage = "Test error message";
        
        // Create an invalid validation result
        ValidationResult result = ValidationResult.createInvalid(errorMessage);
        
        // Assert that the result is invalid
        Assertions.assertFalse(result.isValid(), "Result should be invalid");
        
        // Assert that the error message matches the input
        Assertions.assertEquals(errorMessage, result.getErrorMessage(), "Error message should match the input");
        
        // Use TestUtils to verify both conditions in one call
        TestUtils.assertValidationResult(result, false, errorMessage);
    }

    @Test
    @DisplayName("Should throw NullPointerException when creating invalid result with null error message")
    public void testCreateInvalidWithNullErrorMessage() {
        // Log test execution for debugging
        TestUtils.logTestInfo("Testing ValidationResult.createInvalid() with null error message");
        
        // Assert that NullPointerException is thrown
        Assertions.assertThrows(NullPointerException.class, () -> {
            ValidationResult.createInvalid(null);
        }, "NullPointerException should be thrown when error message is null");
    }

    @Test
    @DisplayName("Should correctly implement equals and hashCode")
    public void testEqualsAndHashCode() {
        // Log test execution for debugging
        TestUtils.logTestInfo("Testing ValidationResult equals and hashCode");
        
        // Create test instances
        ValidationResult validResult1 = ValidationResult.createValid();
        ValidationResult validResult2 = ValidationResult.createValid();
        ValidationResult invalidResult1 = ValidationResult.createInvalid("Error 1");
        ValidationResult invalidResult2 = ValidationResult.createInvalid("Error 1");
        ValidationResult invalidResult3 = ValidationResult.createInvalid("Error 2");
        
        // Test reflexivity: result.equals(result)
        Assertions.assertEquals(validResult1, validResult1, "Object should equal itself");
        
        // Test symmetry: result1.equals(result2) implies result2.equals(result1)
        Assertions.assertEquals(validResult1, validResult2, "Two valid results should be equal");
        Assertions.assertEquals(validResult2, validResult1, "Equals should be symmetric");
        
        Assertions.assertEquals(invalidResult1, invalidResult2, "Two invalid results with same error should be equal");
        Assertions.assertEquals(invalidResult2, invalidResult1, "Equals should be symmetric");
        
        // Test transitivity: if result1.equals(result2) and result2.equals(result3), then result1.equals(result3)
        // We would need three equivalent objects to test this fully
        
        // Test consistency with hashCode
        Assertions.assertEquals(validResult1.hashCode(), validResult2.hashCode(), 
                "Equal objects should have equal hash codes");
        Assertions.assertEquals(invalidResult1.hashCode(), invalidResult2.hashCode(), 
                "Equal objects should have equal hash codes");
        
        // Test inequality cases
        Assertions.assertNotEquals(validResult1, invalidResult1, 
                "Valid and invalid results should not be equal");
        Assertions.assertNotEquals(invalidResult1, invalidResult3, 
                "Results with different error messages should not be equal");
        
        // Test null handling
        Assertions.assertNotEquals(validResult1, null, "Object should not equal null");
        
        // Test different types
        Assertions.assertNotEquals(validResult1, new Object(), "Object should not equal different type");
    }

    @Test
    @DisplayName("Should provide meaningful toString implementation")
    public void testToString() {
        // Log test execution for debugging
        TestUtils.logTestInfo("Testing ValidationResult toString");
        
        // Create test instances
        ValidationResult validResult = ValidationResult.createValid();
        ValidationResult invalidResult = ValidationResult.createInvalid("Test error");
        
        // Get string representations
        String validString = validResult.toString();
        String invalidString = invalidResult.toString();
        
        // Assert that string representations contain expected information
        Assertions.assertTrue(validString.contains("valid=true"), 
                "String representation of valid result should indicate validity");
        Assertions.assertTrue(validString.contains("errorMessage='null'"), 
                "String representation should show null error message");
        
        Assertions.assertTrue(invalidString.contains("valid=false"), 
                "String representation of invalid result should indicate invalidity");
        Assertions.assertTrue(invalidString.contains("errorMessage='Test error'"), 
                "String representation should include error message");
    }
}