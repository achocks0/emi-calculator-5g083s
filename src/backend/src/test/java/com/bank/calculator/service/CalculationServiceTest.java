package com.bank.calculator.service;

import org.junit.jupiter.api.BeforeEach; // JUnit 5.8.2
import org.junit.jupiter.api.DisplayName; // JUnit 5.8.2
import org.junit.jupiter.api.Test; // JUnit 5.8.2
import static org.junit.jupiter.api.Assertions.*; // JUnit 5.8.2

import java.math.BigDecimal; // JDK 11

import com.bank.calculator.service.impl.CalculationServiceImpl;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.exception.CalculationException;
import com.bank.calculator.util.BigDecimalUtils;

/**
 * Test class for CalculationService implementation that verifies the correctness of
 * compound interest and EMI calculations.
 */
@DisplayName("Calculation Service Tests")
public class CalculationServiceTest {

    private CalculationService calculationService;

    /**
     * Sets up the test environment before each test case.
     */
    @BeforeEach
    void setUp() {
        calculationService = new CalculationServiceImpl();
    }

    // Test cases for calculateCompoundInterest method

    @Test
    @DisplayName("Should calculate compound interest correctly with CalculationInput")
    void testCalculateCompoundInterestWithInput() {
        // Create test input
        BigDecimal principal = new BigDecimal("10000.00");
        int durationYears = 5;
        CalculationInput input = new CalculationInput(principal, durationYears);
        
        // Set interest rate explicitly to ensure test consistency
        input.setInterestRate(new BigDecimal("7.5"));
        
        // Calculate expected result
        // A = P(1 + r/n)^(nt) where r = 7.5/100, n = 12, t = 5
        BigDecimal expectedAmount = new BigDecimal("14356.10"); // Calculated expected value
        
        // Execute calculation
        BigDecimal result = calculationService.calculateCompoundInterest(input);
        
        // Verify result
        assertNotNull(result, "Result should not be null");
        assertTrue(BigDecimalUtils.isEqual(result.setScale(2, java.math.RoundingMode.HALF_UP), 
                  expectedAmount), "Compound interest calculation is incorrect");
    }
    
    @Test
    @DisplayName("Should calculate compound interest correctly with direct parameters")
    void testCalculateCompoundInterestWithParameters() {
        // Set up test data
        BigDecimal principal = new BigDecimal("25000.00");
        int durationYears = 3;
        BigDecimal interestRate = new BigDecimal("7.5");
        
        // Calculate expected result
        // A = P(1 + r/n)^(nt) where r = 7.5/100, n = 12, t = 3
        BigDecimal expectedAmount = new BigDecimal("31260.57"); // Calculated expected value
        
        // Execute calculation
        BigDecimal result = calculationService.calculateCompoundInterest(principal, durationYears, interestRate);
        
        // Verify result
        assertNotNull(result, "Result should not be null");
        assertTrue(BigDecimalUtils.isEqual(result.setScale(2, java.math.RoundingMode.HALF_UP), 
                  expectedAmount), "Compound interest calculation is incorrect");
    }
    
    @Test
    @DisplayName("Should return principal amount when interest rate is zero")
    void testCalculateCompoundInterestWithZeroInterestRate() {
        // Set up test data
        BigDecimal principal = new BigDecimal("15000.00");
        int durationYears = 4;
        BigDecimal interestRate = BigDecimal.ZERO;
        
        // Execute calculation
        BigDecimal result = calculationService.calculateCompoundInterest(principal, durationYears, interestRate);
        
        // Verify the result equals the principal (no interest)
        assertNotNull(result, "Result should not be null");
        assertTrue(BigDecimalUtils.isEqual(result, principal), 
                  "With zero interest rate, result should equal principal amount");
    }
    
    @Test
    @DisplayName("Should throw NullPointerException when input is null")
    void testCalculateCompoundInterestWithNullInput() {
        assertThrows(NullPointerException.class, () -> {
            calculationService.calculateCompoundInterest((CalculationInput)null);
        });
    }
    
    @Test
    @DisplayName("Should throw NullPointerException when principal is null")
    void testCalculateCompoundInterestWithNullPrincipal() {
        int durationYears = 5;
        BigDecimal interestRate = new BigDecimal("7.5");
        
        assertThrows(NullPointerException.class, () -> {
            calculationService.calculateCompoundInterest(null, durationYears, interestRate);
        });
    }
    
    @Test
    @DisplayName("Should throw NullPointerException when interest rate is null")
    void testCalculateCompoundInterestWithNullInterestRate() {
        BigDecimal principal = new BigDecimal("10000.00");
        int durationYears = 5;
        
        assertThrows(NullPointerException.class, () -> {
            calculationService.calculateCompoundInterest(principal, durationYears, null);
        });
    }
    
    @Test
    @DisplayName("Should throw IllegalArgumentException when duration is invalid")
    void testCalculateCompoundInterestWithInvalidDuration() {
        BigDecimal principal = new BigDecimal("10000.00");
        BigDecimal interestRate = new BigDecimal("7.5");
        
        // Test with zero duration
        assertThrows(IllegalArgumentException.class, () -> {
            calculationService.calculateCompoundInterest(principal, 0, interestRate);
        });
        
        // Test with negative duration
        assertThrows(IllegalArgumentException.class, () -> {
            calculationService.calculateCompoundInterest(principal, -1, interestRate);
        });
    }
    
    // Test cases for calculateEMI method
    
    @Test
    @DisplayName("Should calculate EMI correctly with CalculationInput")
    void testCalculateEMIWithInput() {
        // Create test input
        BigDecimal principal = new BigDecimal("10000.00");
        int durationYears = 5;
        CalculationInput input = new CalculationInput(principal, durationYears);
        
        // Set interest rate explicitly to ensure test consistency
        input.setInterestRate(new BigDecimal("7.5"));
        
        // Execute calculation
        CalculationResult result = calculationService.calculateEMI(input);
        
        // Verify result
        assertNotNull(result, "Result should not be null");
        assertEquals(new BigDecimal("200.38").setScale(2, java.math.RoundingMode.HALF_UP), 
                    result.getEmiAmount().setScale(2, java.math.RoundingMode.HALF_UP), 
                    "EMI calculation is incorrect");
        
        // Additional validations on the result
        assertTrue(result.getTotalAmount().compareTo(principal) > 0, 
                  "Total amount should be greater than principal");
        
        BigDecimal expectedInterestAmount = result.getTotalAmount().subtract(principal);
        assertEquals(0, expectedInterestAmount.compareTo(result.getInterestAmount()), 
                    "Interest amount should be the difference between total amount and principal");
    }
    
    @Test
    @DisplayName("Should calculate EMI correctly with direct parameters")
    void testCalculateEMIWithParameters() {
        // Set up test data
        BigDecimal principal = new BigDecimal("25000.00");
        int durationYears = 3;
        BigDecimal interestRate = new BigDecimal("7.5");
        
        // Execute calculation
        CalculationResult result = calculationService.calculateEMI(principal, durationYears, interestRate);
        
        // Verify result
        assertNotNull(result, "Result should not be null");
        assertEquals(new BigDecimal("777.23").setScale(2, java.math.RoundingMode.HALF_UP), 
                    result.getEmiAmount().setScale(2, java.math.RoundingMode.HALF_UP), 
                    "EMI calculation is incorrect");
        
        // Additional validations on the result
        assertTrue(result.getTotalAmount().compareTo(principal) > 0, 
                  "Total amount should be greater than principal");
        
        BigDecimal expectedInterestAmount = result.getTotalAmount().subtract(principal);
        assertEquals(0, expectedInterestAmount.compareTo(result.getInterestAmount()), 
                    "Interest amount should be the difference between total amount and principal");
    }
    
    @Test
    @DisplayName("Should calculate EMI correctly with zero interest rate")
    void testCalculateEMIWithZeroInterestRate() {
        // Set up test data
        BigDecimal principal = new BigDecimal("15000.00");
        int durationYears = 4;
        BigDecimal interestRate = BigDecimal.ZERO;
        
        // Execute calculation
        CalculationResult result = calculationService.calculateEMI(principal, durationYears, interestRate);
        
        // Verify result
        assertNotNull(result, "Result should not be null");
        
        // With zero interest, EMI should be principal divided by number of months
        BigDecimal expectedEmi = principal.divide(new BigDecimal(durationYears * 12), 
                                                 java.math.RoundingMode.HALF_UP);
        
        assertEquals(expectedEmi.setScale(2, java.math.RoundingMode.HALF_UP), 
                    result.getEmiAmount().setScale(2, java.math.RoundingMode.HALF_UP), 
                    "EMI with zero interest should be principal divided by months");
        
        // Total amount should equal principal with zero interest
        assertEquals(principal.setScale(2, java.math.RoundingMode.HALF_UP), 
                    result.getTotalAmount().setScale(2, java.math.RoundingMode.HALF_UP), 
                    "Total amount should equal principal with zero interest");
        
        // Interest amount should be zero
        assertEquals(BigDecimal.ZERO.setScale(2, java.math.RoundingMode.HALF_UP), 
                    result.getInterestAmount().setScale(2, java.math.RoundingMode.HALF_UP), 
                    "Interest amount should be zero with zero interest rate");
    }
    
    @Test
    @DisplayName("Should throw NullPointerException when input is null")
    void testCalculateEMIWithNullInput() {
        assertThrows(NullPointerException.class, () -> {
            calculationService.calculateEMI((CalculationInput)null);
        });
    }
    
    @Test
    @DisplayName("Should throw NullPointerException when principal is null")
    void testCalculateEMIWithNullPrincipal() {
        int durationYears = 5;
        BigDecimal interestRate = new BigDecimal("7.5");
        
        assertThrows(NullPointerException.class, () -> {
            calculationService.calculateEMI(null, durationYears, interestRate);
        });
    }
    
    @Test
    @DisplayName("Should throw NullPointerException when interest rate is null")
    void testCalculateEMIWithNullInterestRate() {
        BigDecimal principal = new BigDecimal("10000.00");
        int durationYears = 5;
        
        assertThrows(NullPointerException.class, () -> {
            calculationService.calculateEMI(principal, durationYears, null);
        });
    }
    
    @Test
    @DisplayName("Should throw IllegalArgumentException when duration is invalid")
    void testCalculateEMIWithInvalidDuration() {
        BigDecimal principal = new BigDecimal("10000.00");
        BigDecimal interestRate = new BigDecimal("7.5");
        
        // Test with zero duration
        assertThrows(IllegalArgumentException.class, () -> {
            calculationService.calculateEMI(principal, 0, interestRate);
        });
        
        // Test with negative duration
        assertThrows(IllegalArgumentException.class, () -> {
            calculationService.calculateEMI(principal, -1, interestRate);
        });
    }
    
    @Test
    @DisplayName("Should calculate EMI correctly with large values")
    void testCalculateEMIWithLargeValues() {
        // Set up test data with large principal and long duration
        BigDecimal principal = new BigDecimal("1000000.00"); // $1 million
        int durationYears = 30; // 30 years
        BigDecimal interestRate = new BigDecimal("7.5");
        
        // Execute calculation
        CalculationResult result = calculationService.calculateEMI(principal, durationYears, interestRate);
        
        // Verify basic expectations
        assertNotNull(result, "Result should not be null");
        assertTrue(result.getEmiAmount().compareTo(BigDecimal.ZERO) > 0, 
                  "EMI amount should be positive");
        assertTrue(result.getTotalAmount().compareTo(principal) > 0, 
                  "Total amount should be greater than principal");
        assertTrue(result.getInterestAmount().compareTo(BigDecimal.ZERO) > 0, 
                  "Interest amount should be positive");
        
        // $1 million for 30 years at 7.5% should have EMI around $6,992.36
        BigDecimal expectedEmi = new BigDecimal("6992.36");
        assertEquals(0, expectedEmi.compareTo(result.getEmiAmount().setScale(2, java.math.RoundingMode.HALF_UP)), 
                    "EMI for large values calculation is incorrect");
    }
    
    @Test
    @DisplayName("Should match sample calculations from technical specification")
    void testCalculateEMIWithSampleValues() {
        // Test case 1: $10,000 for 5 years at 7.5%
        BigDecimal principal1 = new BigDecimal("10000.00");
        int durationYears1 = 5;
        BigDecimal interestRate = new BigDecimal("7.5");
        
        CalculationResult result1 = calculationService.calculateEMI(principal1, durationYears1, interestRate);
        
        assertEquals(new BigDecimal("200.38").setScale(2, java.math.RoundingMode.HALF_UP), 
                    result1.getEmiAmount().setScale(2, java.math.RoundingMode.HALF_UP), 
                    "EMI for $10,000 for 5 years at 7.5% should be $200.38");
        
        // Test case 2: $25,000 for 3 years at 7.5%
        BigDecimal principal2 = new BigDecimal("25000.00");
        int durationYears2 = 3;
        
        CalculationResult result2 = calculationService.calculateEMI(principal2, durationYears2, interestRate);
        
        assertEquals(new BigDecimal("777.23").setScale(2, java.math.RoundingMode.HALF_UP), 
                    result2.getEmiAmount().setScale(2, java.math.RoundingMode.HALF_UP), 
                    "EMI for $25,000 for 3 years at 7.5% should be $777.23");
    }
}