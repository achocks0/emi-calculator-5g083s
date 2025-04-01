package com.bank.calculator.test.unit.model;

import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.category.UnitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

import java.math.BigDecimal;
import java.lang.NullPointerException;
import java.lang.IllegalArgumentException;

/**
 * Unit tests for the CalculationResult model class.
 * These tests verify that CalculationResult correctly initializes, validates inputs,
 * and provides proper formatting and comparison operations.
 */
public class CalculationResultTest implements UnitTest {

    private static final BigDecimal TEST_EMI_AMOUNT = new BigDecimal("483.65");
    private static final BigDecimal TEST_TOTAL_AMOUNT = new BigDecimal("29019.00");
    private static final BigDecimal TEST_INTEREST_AMOUNT = new BigDecimal("4019.00");
    private static final BigDecimal TEST_ANNUAL_INTEREST_RATE = new BigDecimal("0.075");
    private static final int TEST_NUMBER_OF_INSTALLMENTS = 60;

    @Test
    @DisplayName("Constructor should initialize fields with valid parameters")
    public void testConstructorWithValidParameters() {
        CalculationResult result = new CalculationResult(
                TEST_EMI_AMOUNT,
                TEST_TOTAL_AMOUNT,
                TEST_INTEREST_AMOUNT,
                TEST_ANNUAL_INTEREST_RATE,
                TEST_NUMBER_OF_INSTALLMENTS
        );

        TestUtils.assertBigDecimalEquals(TEST_EMI_AMOUNT, result.getEmiAmount());
        TestUtils.assertBigDecimalEquals(TEST_TOTAL_AMOUNT, result.getTotalAmount());
        TestUtils.assertBigDecimalEquals(TEST_INTEREST_AMOUNT, result.getInterestAmount());
        TestUtils.assertBigDecimalEquals(TEST_ANNUAL_INTEREST_RATE, result.getAnnualInterestRate());
        Assertions.assertEquals(TEST_NUMBER_OF_INSTALLMENTS, result.getNumberOfInstallments());
    }

    @Test
    @DisplayName("Constructor should throw NullPointerException when emiAmount is null")
    public void testConstructorWithNullEmiAmount() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new CalculationResult(
                    null,
                    TEST_TOTAL_AMOUNT,
                    TEST_INTEREST_AMOUNT,
                    TEST_ANNUAL_INTEREST_RATE,
                    TEST_NUMBER_OF_INSTALLMENTS
            );
        });
    }

    @Test
    @DisplayName("Constructor should throw NullPointerException when totalAmount is null")
    public void testConstructorWithNullTotalAmount() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new CalculationResult(
                    TEST_EMI_AMOUNT,
                    null,
                    TEST_INTEREST_AMOUNT,
                    TEST_ANNUAL_INTEREST_RATE,
                    TEST_NUMBER_OF_INSTALLMENTS
            );
        });
    }

    @Test
    @DisplayName("Constructor should throw NullPointerException when interestAmount is null")
    public void testConstructorWithNullInterestAmount() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new CalculationResult(
                    TEST_EMI_AMOUNT,
                    TEST_TOTAL_AMOUNT,
                    null,
                    TEST_ANNUAL_INTEREST_RATE,
                    TEST_NUMBER_OF_INSTALLMENTS
            );
        });
    }

    @Test
    @DisplayName("Constructor should throw NullPointerException when annualInterestRate is null")
    public void testConstructorWithNullAnnualInterestRate() {
        Assertions.assertThrows(NullPointerException.class, () -> {
            new CalculationResult(
                    TEST_EMI_AMOUNT,
                    TEST_TOTAL_AMOUNT,
                    TEST_INTEREST_AMOUNT,
                    null,
                    TEST_NUMBER_OF_INSTALLMENTS
            );
        });
    }

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException when numberOfInstallments is zero")
    public void testConstructorWithZeroInstallments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new CalculationResult(
                    TEST_EMI_AMOUNT,
                    TEST_TOTAL_AMOUNT,
                    TEST_INTEREST_AMOUNT,
                    TEST_ANNUAL_INTEREST_RATE,
                    0
            );
        });
    }

    @Test
    @DisplayName("Constructor should throw IllegalArgumentException when numberOfInstallments is negative")
    public void testConstructorWithNegativeInstallments() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> {
            new CalculationResult(
                    TEST_EMI_AMOUNT,
                    TEST_TOTAL_AMOUNT,
                    TEST_INTEREST_AMOUNT,
                    TEST_ANNUAL_INTEREST_RATE,
                    -1
            );
        });
    }

    @Test
    @DisplayName("getFormattedEmiAmount should return EMI amount formatted as USD currency")
    public void testGetFormattedEmiAmount() {
        CalculationResult result = new CalculationResult(
                TEST_EMI_AMOUNT,
                TEST_TOTAL_AMOUNT,
                TEST_INTEREST_AMOUNT,
                TEST_ANNUAL_INTEREST_RATE,
                TEST_NUMBER_OF_INSTALLMENTS
        );
        
        String formatted = result.getFormattedEmiAmount();
        Assertions.assertTrue(formatted.contains("$"));
        Assertions.assertTrue(formatted.contains("483.65"));
    }

    @Test
    @DisplayName("getFormattedTotalAmount should return total amount formatted as USD currency")
    public void testGetFormattedTotalAmount() {
        CalculationResult result = new CalculationResult(
                TEST_EMI_AMOUNT,
                TEST_TOTAL_AMOUNT,
                TEST_INTEREST_AMOUNT,
                TEST_ANNUAL_INTEREST_RATE,
                TEST_NUMBER_OF_INSTALLMENTS
        );
        
        String formatted = result.getFormattedTotalAmount();
        Assertions.assertTrue(formatted.contains("$"));
        Assertions.assertTrue(formatted.contains("29,019.00") || formatted.contains("29019.00"));
    }

    @Test
    @DisplayName("getFormattedInterestAmount should return interest amount formatted as USD currency")
    public void testGetFormattedInterestAmount() {
        CalculationResult result = new CalculationResult(
                TEST_EMI_AMOUNT,
                TEST_TOTAL_AMOUNT,
                TEST_INTEREST_AMOUNT,
                TEST_ANNUAL_INTEREST_RATE,
                TEST_NUMBER_OF_INSTALLMENTS
        );
        
        String formatted = result.getFormattedInterestAmount();
        Assertions.assertTrue(formatted.contains("$"));
        Assertions.assertTrue(formatted.contains("4,019.00") || formatted.contains("4019.00"));
    }

    @Test
    @DisplayName("getFormattedAnnualInterestRate should return annual interest rate formatted as percentage")
    public void testGetFormattedAnnualInterestRate() {
        CalculationResult result = new CalculationResult(
                TEST_EMI_AMOUNT,
                TEST_TOTAL_AMOUNT,
                TEST_INTEREST_AMOUNT,
                TEST_ANNUAL_INTEREST_RATE,
                TEST_NUMBER_OF_INSTALLMENTS
        );
        
        String formatted = result.getFormattedAnnualInterestRate();
        Assertions.assertTrue(formatted.contains("%"));
        Assertions.assertTrue(formatted.contains("7.5"));
    }

    @Test
    @DisplayName("equals and hashCode should work correctly")
    public void testEqualsAndHashCode() {
        CalculationResult result1 = new CalculationResult(
                TEST_EMI_AMOUNT,
                TEST_TOTAL_AMOUNT,
                TEST_INTEREST_AMOUNT,
                TEST_ANNUAL_INTEREST_RATE,
                TEST_NUMBER_OF_INSTALLMENTS
        );
        
        CalculationResult result2 = new CalculationResult(
                TEST_EMI_AMOUNT,
                TEST_TOTAL_AMOUNT,
                TEST_INTEREST_AMOUNT,
                TEST_ANNUAL_INTEREST_RATE,
                TEST_NUMBER_OF_INSTALLMENTS
        );
        
        CalculationResult differentResult = new CalculationResult(
                new BigDecimal("500.00"),
                TEST_TOTAL_AMOUNT,
                TEST_INTEREST_AMOUNT,
                TEST_ANNUAL_INTEREST_RATE,
                TEST_NUMBER_OF_INSTALLMENTS
        );
        
        // Test equality
        Assertions.assertEquals(result1, result2);
        Assertions.assertNotEquals(result1, differentResult);
        
        // Test hash code
        Assertions.assertEquals(result1.hashCode(), result2.hashCode());
        Assertions.assertNotEquals(result1.hashCode(), differentResult.hashCode());
    }

    @Test
    @DisplayName("toString should return a string containing all field values")
    public void testToString() {
        CalculationResult result = new CalculationResult(
                TEST_EMI_AMOUNT,
                TEST_TOTAL_AMOUNT,
                TEST_INTEREST_AMOUNT,
                TEST_ANNUAL_INTEREST_RATE,
                TEST_NUMBER_OF_INSTALLMENTS
        );
        
        String str = result.toString();
        
        // Check that the string contains all field values
        Assertions.assertTrue(str.contains(TEST_EMI_AMOUNT.toString()));
        Assertions.assertTrue(str.contains(TEST_TOTAL_AMOUNT.toString()));
        Assertions.assertTrue(str.contains(TEST_INTEREST_AMOUNT.toString()));
        Assertions.assertTrue(str.contains(TEST_ANNUAL_INTEREST_RATE.toString()));
        Assertions.assertTrue(str.contains(Integer.toString(TEST_NUMBER_OF_INSTALLMENTS)));
    }
}