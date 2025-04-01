package com.bank.calculator.test.unit.model;

import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.test.category.UnitTest;
import com.bank.calculator.test.util.TestUtils;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;

/**
 * Test class for the CalculationInput model that verifies all functionality of the model class.
 */
@UnitTest
@DisplayName("CalculationInput Model Tests")
public class CalculationInputTest {

    private BigDecimal testPrincipal;
    private int testDuration;
    private BigDecimal testInterestRate;
    
    /**
     * Sets up test fixtures before each test.
     */
    @BeforeEach
    void setUp() {
        testPrincipal = new BigDecimal("10000.00");
        testDuration = 5;
        testInterestRate = new BigDecimal("7.5");
    }
    
    /**
     * Cleans up test fixtures after each test.
     */
    @AfterEach
    void tearDown() {
        testPrincipal = null;
        testDuration = 0;
        testInterestRate = null;
    }
    
    /**
     * Tests that the constructor correctly initializes the object with valid inputs.
     */
    @Test
    @DisplayName("Constructor should initialize object with valid inputs")
    void testConstructorWithValidInputs() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        
        TestUtils.assertBigDecimalEquals(testPrincipal, input.getPrincipal());
        Assertions.assertEquals(testDuration, input.getDurationYears());
        TestUtils.assertBigDecimalEquals(CalculationConstants.DEFAULT_INTEREST_RATE, input.getInterestRate());
    }
    
    /**
     * Tests that the constructor throws NullPointerException when principal is null.
     */
    @Test
    @DisplayName("Constructor should throw NullPointerException when principal is null")
    void testConstructorWithNullPrincipal() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new CalculationInput(null, testDuration);
        });
    }
    
    /**
     * Tests that the constructor throws IllegalArgumentException when duration is negative.
     */
    @Test
    @DisplayName("Constructor should throw IllegalArgumentException when duration is negative")
    void testConstructorWithNegativeDuration() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new CalculationInput(testPrincipal, -1);
        });
    }
    
    /**
     * Tests that the constructor throws IllegalArgumentException when duration is zero.
     */
    @Test
    @DisplayName("Constructor should throw IllegalArgumentException when duration is zero")
    void testConstructorWithZeroDuration() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new CalculationInput(testPrincipal, 0);
        });
    }
    
    /**
     * Tests that getPrincipal returns the correct value.
     */
    @Test
    @DisplayName("getPrincipal should return the correct principal amount")
    void testGetPrincipal() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        TestUtils.assertBigDecimalEquals(testPrincipal, input.getPrincipal());
    }
    
    /**
     * Tests that getDurationYears returns the correct value.
     */
    @Test
    @DisplayName("getDurationYears should return the correct loan duration")
    void testGetDurationYears() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        Assertions.assertEquals(testDuration, input.getDurationYears());
    }
    
    /**
     * Tests that getInterestRate returns the correct value.
     */
    @Test
    @DisplayName("getInterestRate should return the correct interest rate")
    void testGetInterestRate() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        TestUtils.assertBigDecimalEquals(CalculationConstants.DEFAULT_INTEREST_RATE, input.getInterestRate());
    }
    
    /**
     * Tests that setInterestRate correctly updates the interest rate.
     */
    @Test
    @DisplayName("setInterestRate should update the interest rate")
    void testSetInterestRate() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        input.setInterestRate(testInterestRate);
        TestUtils.assertBigDecimalEquals(testInterestRate, input.getInterestRate());
    }
    
    /**
     * Tests that setInterestRate throws NullPointerException when interest rate is null.
     */
    @Test
    @DisplayName("setInterestRate should throw NullPointerException when interest rate is null")
    void testSetInterestRateWithNull() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        Assertions.assertThrows(NullPointerException.class, () -> {
            input.setInterestRate(null);
        });
    }
    
    /**
     * Tests that getFormattedPrincipal returns the principal with correct currency formatting.
     */
    @Test
    @DisplayName("getFormattedPrincipal should return principal with currency formatting")
    void testGetFormattedPrincipal() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        BigDecimal expected = testPrincipal.round(CalculationConstants.CURRENCY_MATH_CONTEXT);
        TestUtils.assertBigDecimalEquals(expected, input.getFormattedPrincipal());
    }
    
    /**
     * Tests that getFormattedInterestRate returns the interest rate as a percentage string.
     */
    @Test
    @DisplayName("getFormattedInterestRate should return interest rate as percentage string")
    void testGetFormattedInterestRate() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        input.setInterestRate(testInterestRate);
        String expected = testInterestRate.multiply(CalculationConstants.HUNDRED)
                .round(CalculationConstants.CURRENCY_MATH_CONTEXT)
                .toString() + "%";
        Assertions.assertEquals(expected, input.getFormattedInterestRate());
    }
    
    /**
     * Tests that equals returns true when comparing an object to itself.
     */
    @Test
    @DisplayName("equals should return true when comparing an object to itself")
    void testEqualsWithSameObject() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        Assertions.assertTrue(input.equals(input));
    }
    
    /**
     * Tests that equals returns true when comparing two equal objects.
     */
    @Test
    @DisplayName("equals should return true when comparing two equal objects")
    void testEqualsWithEqualObjects() {
        CalculationInput input1 = new CalculationInput(testPrincipal, testDuration);
        CalculationInput input2 = new CalculationInput(testPrincipal, testDuration);
        Assertions.assertTrue(input1.equals(input2));
    }
    
    /**
     * Tests that equals returns false when comparing two different objects.
     */
    @Test
    @DisplayName("equals should return false when comparing two different objects")
    void testEqualsWithDifferentObjects() {
        CalculationInput input1 = new CalculationInput(testPrincipal, testDuration);
        CalculationInput input2 = new CalculationInput(testPrincipal, testDuration + 1);
        Assertions.assertFalse(input1.equals(input2));
    }
    
    /**
     * Tests that equals returns false when comparing with null.
     */
    @Test
    @DisplayName("equals should return false when comparing with null")
    void testEqualsWithNull() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        Assertions.assertFalse(input.equals(null));
    }
    
    /**
     * Tests that equals returns false when comparing with an object of a different class.
     */
    @Test
    @DisplayName("equals should return false when comparing with an object of a different class")
    void testEqualsWithDifferentClass() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        Assertions.assertFalse(input.equals("string"));
    }
    
    /**
     * Tests that hashCode returns the same value for equal objects.
     */
    @Test
    @DisplayName("hashCode should return the same value for equal objects")
    void testHashCodeConsistency() {
        CalculationInput input1 = new CalculationInput(testPrincipal, testDuration);
        CalculationInput input2 = new CalculationInput(testPrincipal, testDuration);
        Assertions.assertEquals(input1.hashCode(), input2.hashCode());
    }
    
    /**
     * Tests that toString returns a non-null, non-empty string.
     */
    @Test
    @DisplayName("toString should return a non-null, non-empty string")
    void testToString() {
        CalculationInput input = new CalculationInput(testPrincipal, testDuration);
        String result = input.toString();
        
        Assertions.assertNotNull(result);
        Assertions.assertFalse(result.isEmpty());
        Assertions.assertTrue(result.contains(testPrincipal.toString()));
        Assertions.assertTrue(result.contains(Integer.toString(testDuration)));
    }
    
    /**
     * Tests that the constructor accepts various valid durations.
     */
    @ParameterizedTest
    @ValueSource(ints = {1, 5, 10, 15, 20, 25, 30})
    @DisplayName("Constructor should accept various valid durations")
    void testParameterizedConstructorWithValidDurations(int duration) {
        CalculationInput input = new CalculationInput(testPrincipal, duration);
        Assertions.assertEquals(duration, input.getDurationYears());
    }
    
    /**
     * Tests that the constructor throws IllegalArgumentException for various invalid durations.
     */
    @ParameterizedTest
    @ValueSource(ints = {-10, -5, -1, 0})
    @DisplayName("Constructor should throw IllegalArgumentException for invalid durations")
    void testParameterizedConstructorWithInvalidDurations(int duration) {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new CalculationInput(testPrincipal, duration);
        });
    }
}