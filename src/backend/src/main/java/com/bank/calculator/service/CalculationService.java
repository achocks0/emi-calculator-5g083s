package com.bank.calculator.service;

import java.math.BigDecimal; // JDK 11

import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.exception.CalculationException;

/**
 * Service interface that defines the contract for compound interest and EMI calculation operations.
 * This interface provides methods for calculating compound interest and monthly EMI based on
 * principal amount, loan duration, and interest rate.
 * <p>
 * The calculations are based on standard financial formulas:
 * <ul>
 *   <li>Compound Interest: A = P(1 + r/n)^(nt)</li>
 *   <li>EMI: EMI = [P × r × (1 + r)^n]/[(1 + r)^n - 1]</li>
 * </ul>
 * where:
 * <ul>
 *   <li>A = Final amount</li>
 *   <li>P = Principal amount</li>
 *   <li>r = Annual interest rate (in decimal form)</li>
 *   <li>n = Number of times interest is compounded per year</li>
 *   <li>t = Time in years</li>
 * </ul>
 * <p>
 * All calculations are performed using BigDecimal to ensure precision in financial calculations.
 * Performance considerations include optimized BigDecimal usage and efficient algorithm 
 * implementation to ensure calculations complete within 200ms as per requirements.
 */
public interface CalculationService {
    
    /**
     * Calculates the compound interest based on the provided calculation input parameters.
     *
     * @param input The calculation input containing principal, duration, and interest rate
     * @return The final amount after applying compound interest
     * @throws CalculationException if there is an error during calculation
     * @throws NullPointerException if input is null
     */
    BigDecimal calculateCompoundInterest(CalculationInput input) throws CalculationException;
    
    /**
     * Calculates the compound interest based on the provided principal amount, duration, and interest rate.
     *
     * @param principal The principal amount for the loan
     * @param durationYears The loan duration in years
     * @param interestRate The annual interest rate
     * @return The final amount after applying compound interest
     * @throws CalculationException if there is an error during calculation
     * @throws NullPointerException if principal or interestRate is null
     * @throws IllegalArgumentException if durationYears is less than or equal to zero
     */
    BigDecimal calculateCompoundInterest(BigDecimal principal, int durationYears, BigDecimal interestRate) 
            throws CalculationException;
    
    /**
     * Calculates the Equated Monthly Installment (EMI) based on the provided calculation input parameters.
     *
     * @param input The calculation input containing principal, duration, and interest rate
     * @return The calculation result containing EMI amount and other details
     * @throws CalculationException if there is an error during calculation
     * @throws NullPointerException if input is null
     */
    CalculationResult calculateEMI(CalculationInput input) throws CalculationException;
    
    /**
     * Calculates the Equated Monthly Installment (EMI) based on the provided principal amount,
     * duration, and interest rate.
     *
     * @param principal The principal amount for the loan
     * @param durationYears The loan duration in years
     * @param interestRate The annual interest rate
     * @return The calculation result containing EMI amount and other details
     * @throws CalculationException if there is an error during calculation
     * @throws NullPointerException if principal or interestRate is null
     * @throws IllegalArgumentException if durationYears is less than or equal to zero
     */
    CalculationResult calculateEMI(BigDecimal principal, int durationYears, BigDecimal interestRate) 
            throws CalculationException;
}