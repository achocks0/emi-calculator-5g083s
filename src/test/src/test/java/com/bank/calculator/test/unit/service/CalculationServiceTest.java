package com.bank.calculator.test.unit.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Stream;

import com.bank.calculator.service.CalculationService;
import com.bank.calculator.service.impl.CalculationServiceImpl;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.exception.CalculationException;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.fixture.CalculationTestFixture;

/**
 * Unit test class for testing the CalculationService implementation.
 * This class verifies the correctness of compound interest and EMI calculations,
 * handles edge cases, tests input validation, and ensures that exceptions
 * are properly thrown for invalid inputs.
 */
public class CalculationServiceTest {
    
    private CalculationService calculationService;
    
    private static final BigDecimal STANDARD_PRINCIPAL = new BigDecimal("10000.00");
    private static final int STANDARD_DURATION = 5;
    private static final BigDecimal STANDARD_INTEREST_RATE = new BigDecimal("7.5");
    private static final BigDecimal EXPECTED_STANDARD_EMI = new BigDecimal("200.38");
    private static final BigDecimal ZERO_INTEREST_RATE = BigDecimal.ZERO;
    
    @BeforeEach
    public void setUp() {
        calculationService = new CalculationServiceImpl();
    }
    
    @AfterEach
    public void tearDown() {
        calculationService = null;
    }
    
    @Test
    @DisplayName("Should calculate compound interest correctly with valid input")
    public void testCalculateCompoundInterestWithValidInput() {
        // Create input with standard values
        CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, STANDARD_DURATION);
        
        // Calculate compound interest
        BigDecimal result = calculationService.calculateCompoundInterest(input);
        
        // Assert the result
        Assertions.assertNotNull(result, "Result should not be null");
        Assertions.assertTrue(result.compareTo(STANDARD_PRINCIPAL) > 0, "Compound interest should be greater than principal");
        
        // Expected compound interest with 7.5% annual rate for 5 years
        BigDecimal expectedAmount = new BigDecimal("14356.29"); // Approximation
        TestUtils.assertBigDecimalEquals(expectedAmount, result);
    }
    
    @Test
    @DisplayName("Should calculate compound interest correctly with direct parameters")
    public void testCalculateCompoundInterestWithDirectParameters() {
        // Calculate compound interest with direct parameters
        BigDecimal result = calculationService.calculateCompoundInterest(
                STANDARD_PRINCIPAL, STANDARD_DURATION, STANDARD_INTEREST_RATE);
        
        // Assert the result
        Assertions.assertNotNull(result, "Result should not be null");
        Assertions.assertTrue(result.compareTo(STANDARD_PRINCIPAL) > 0, "Compound interest should be greater than principal");
        
        // Expected compound interest with 7.5% annual rate for 5 years
        BigDecimal expectedAmount = new BigDecimal("14356.29"); // Approximation
        TestUtils.assertBigDecimalEquals(expectedAmount, result);
    }
    
    @Test
    @DisplayName("Should calculate EMI correctly with valid input")
    public void testCalculateEMIWithValidInput() {
        // Create input with standard values
        CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, STANDARD_DURATION);
        
        // Calculate EMI
        CalculationResult result = calculationService.calculateEMI(input);
        
        // Assert the result
        Assertions.assertNotNull(result, "Result should not be null");
        TestUtils.assertBigDecimalEquals(EXPECTED_STANDARD_EMI, result.getEmiAmount());
        
        // Verify total amount is greater than principal
        Assertions.assertTrue(result.getTotalAmount().compareTo(STANDARD_PRINCIPAL) > 0, 
                "Total amount should be greater than principal");
        
        // Verify interest amount is total - principal
        BigDecimal expectedInterest = result.getTotalAmount().subtract(STANDARD_PRINCIPAL);
        TestUtils.assertBigDecimalEquals(expectedInterest, result.getInterestAmount());
    }
    
    @Test
    @DisplayName("Should calculate EMI correctly with direct parameters")
    public void testCalculateEMIWithDirectParameters() {
        // Calculate EMI with direct parameters
        CalculationResult result = calculationService.calculateEMI(
                STANDARD_PRINCIPAL, STANDARD_DURATION, STANDARD_INTEREST_RATE);
        
        // Assert the result
        Assertions.assertNotNull(result, "Result should not be null");
        TestUtils.assertBigDecimalEquals(EXPECTED_STANDARD_EMI, result.getEmiAmount());
        
        // Verify total amount is greater than principal
        Assertions.assertTrue(result.getTotalAmount().compareTo(STANDARD_PRINCIPAL) > 0, 
                "Total amount should be greater than principal");
        
        // Verify interest amount is total - principal
        BigDecimal expectedInterest = result.getTotalAmount().subtract(STANDARD_PRINCIPAL);
        TestUtils.assertBigDecimalEquals(expectedInterest, result.getInterestAmount());
    }
    
    @Test
    @DisplayName("Should calculate EMI correctly with zero interest rate")
    public void testCalculateEMIWithZeroInterestRate() {
        // Create input with zero interest rate
        CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, STANDARD_DURATION);
        input.setInterestRate(ZERO_INTEREST_RATE);
        
        // Calculate EMI
        CalculationResult result = calculationService.calculateEMI(input);
        
        // Assert the result
        Assertions.assertNotNull(result, "Result should not be null");
        
        // With zero interest, EMI should be principal divided by months
        BigDecimal expectedEmi = STANDARD_PRINCIPAL.divide(
                new BigDecimal(STANDARD_DURATION * 12), CalculationConstants.CALCULATION_MATH_CONTEXT);
        TestUtils.assertBigDecimalEquals(expectedEmi, result.getEmiAmount());
        
        // Total amount should equal principal
        TestUtils.assertBigDecimalEquals(STANDARD_PRINCIPAL, result.getTotalAmount());
        
        // Interest amount should be zero
        TestUtils.assertBigDecimalEquals(BigDecimal.ZERO, result.getInterestAmount());
    }
    
    @Test
    @DisplayName("Should throw exception when null input is provided for compound interest calculation")
    public void testCalculateCompoundInterestWithNullInput() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            calculationService.calculateCompoundInterest((CalculationInput)null);
        });
    }
    
    @Test
    @DisplayName("Should throw exception when null input is provided for EMI calculation")
    public void testCalculateEMIWithNullInput() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            calculationService.calculateEMI((CalculationInput)null);
        });
    }
    
    @Test
    @DisplayName("Should throw exception when null principal is provided for compound interest calculation")
    public void testCalculateCompoundInterestWithNullPrincipal() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            calculationService.calculateCompoundInterest(null, STANDARD_DURATION, STANDARD_INTEREST_RATE);
        });
    }
    
    @Test
    @DisplayName("Should throw exception when null interest rate is provided for compound interest calculation")
    public void testCalculateCompoundInterestWithNullInterestRate() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            calculationService.calculateCompoundInterest(STANDARD_PRINCIPAL, STANDARD_DURATION, null);
        });
    }
    
    @Test
    @DisplayName("Should throw exception when zero duration is provided for compound interest calculation")
    public void testCalculateCompoundInterestWithZeroDuration() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculationService.calculateCompoundInterest(STANDARD_PRINCIPAL, a0, STANDARD_INTEREST_RATE);
        });
    }
    
    @Test
    @DisplayName("Should throw exception when negative duration is provided for compound interest calculation")
    public void testCalculateCompoundInterestWithNegativeDuration() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            calculationService.calculateCompoundInterest(STANDARD_PRINCIPAL, -1, STANDARD_INTEREST_RATE);
        });
    }
    
    @ParameterizedTest
    @MethodSource("standardTestCases")
    @DisplayName("Should calculate EMI correctly for standard test cases")
    public void testParameterizedStandardCalculations(CalculationTestFixture.TestCase testCase) {
        // Get input from test case
        CalculationInput input = testCase.getInput();
        
        // Calculate EMI
        CalculationResult result = calculationService.calculateEMI(input);
        
        // Assert the results
        TestUtils.assertBigDecimalEquals(testCase.getExpectedEmiAmount(), result.getEmiAmount());
        TestUtils.assertBigDecimalEquals(testCase.getExpectedTotalAmount(), result.getTotalAmount());
        TestUtils.assertBigDecimalEquals(testCase.getExpectedInterestAmount(), result.getInterestAmount());
    }
    
    @ParameterizedTest
    @MethodSource("edgeCaseTestCases")
    @DisplayName("Should calculate EMI correctly for edge case test cases")
    public void testParameterizedEdgeCaseCalculations(CalculationTestFixture.TestCase testCase) {
        // Get input from test case
        CalculationInput input = testCase.getInput();
        
        // Calculate EMI
        CalculationResult result = calculationService.calculateEMI(input);
        
        // Assert the results
        TestUtils.assertBigDecimalEquals(testCase.getExpectedEmiAmount(), result.getEmiAmount());
        TestUtils.assertBigDecimalEquals(testCase.getExpectedTotalAmount(), result.getTotalAmount());
        TestUtils.assertBigDecimalEquals(testCase.getExpectedInterestAmount(), result.getInterestAmount());
    }
    
    @ParameterizedTest
    @MethodSource("boundaryTestCases")
    @DisplayName("Should calculate EMI correctly for boundary test cases")
    public void testParameterizedBoundaryCalculations(CalculationTestFixture.TestCase testCase) {
        // Get input from test case
        CalculationInput input = testCase.getInput();
        
        // Calculate EMI
        CalculationResult result = calculationService.calculateEMI(input);
        
        // Assert the results
        TestUtils.assertBigDecimalEquals(testCase.getExpectedEmiAmount(), result.getEmiAmount());
        TestUtils.assertBigDecimalEquals(testCase.getExpectedTotalAmount(), result.getTotalAmount());
        TestUtils.assertBigDecimalEquals(testCase.getExpectedInterestAmount(), result.getInterestAmount());
    }
    
    // Method sources for parameterized tests
    
    static Stream<CalculationTestFixture.TestCase> standardTestCases() {
        return CalculationTestFixture.createStandardTestCases().stream();
    }
    
    static Stream<CalculationTestFixture.TestCase> edgeCaseTestCases() {
        return CalculationTestFixture.createEdgeCaseTestCases().stream();
    }
    
    static Stream<CalculationTestFixture.TestCase> boundaryTestCases() {
        return CalculationTestFixture.createBoundaryTestCases().stream();
    }
}