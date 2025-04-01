package com.bank.calculator.service.impl;

import java.math.BigDecimal; // JDK 11
import java.util.Objects; // JDK 11
import java.util.logging.Logger; // JDK 11
import java.util.logging.Level; // JDK 11

import com.bank.calculator.service.CalculationService;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.exception.CalculationException;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.util.BigDecimalUtils;

/**
 * Implementation of the CalculationService interface that provides concrete implementations 
 * of compound interest and EMI calculation methods.
 * This class contains the core financial calculation logic for the Compound Interest Calculator application,
 * ensuring precise calculations with proper handling of decimal precision for currency values.
 */
public class CalculationServiceImpl implements CalculationService {

    private static final Logger LOGGER = Logger.getLogger(CalculationServiceImpl.class.getName());
    
    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal calculateCompoundInterest(CalculationInput input) throws CalculationException {
        Objects.requireNonNull(input, "CalculationInput cannot be null");
        
        BigDecimal principal = input.getPrincipal();
        int durationYears = input.getDurationYears();
        BigDecimal interestRate = input.getInterestRate();
        
        return calculateCompoundInterest(principal, durationYears, interestRate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public BigDecimal calculateCompoundInterest(BigDecimal principal, int durationYears, BigDecimal interestRate) 
            throws CalculationException {
        try {
            // Validate inputs
            validateInputs(principal, durationYears, interestRate);
            
            LOGGER.log(Level.INFO, "Calculating compound interest for Principal: {0}, Duration: {1} years, Interest Rate: {2}%", 
                new Object[]{principal, durationYears, interestRate});
            
            // Convert annual interest rate to decimal form (r/100)
            BigDecimal rateDecimal = BigDecimalUtils.percentageToDecimal(interestRate);
            
            // Calculate rate per compounding period (r/n)
            BigDecimal ratePerPeriod = BigDecimalUtils.divide(rateDecimal, 
                new BigDecimal(CalculationConstants.MONTHLY_COMPOUNDING));
            
            // Calculate total number of compounding periods (n*t)
            int totalPeriods = CalculationConstants.MONTHLY_COMPOUNDING * durationYears;
            
            // Calculate compound factor (1 + r/n)^(nt)
            BigDecimal onePlusRatePerPeriod = BigDecimalUtils.add(CalculationConstants.ONE, ratePerPeriod);
            BigDecimal compoundFactor = BigDecimalUtils.pow(onePlusRatePerPeriod, totalPeriods);
            
            // Calculate final amount (P * compound factor)
            BigDecimal finalAmount = BigDecimalUtils.multiply(principal, compoundFactor);
            finalAmount = BigDecimalUtils.roundForCalculation(finalAmount);
            
            LOGGER.log(Level.INFO, "Compound interest calculation result: {0}", finalAmount);
            
            return finalAmount;
        } catch (ArithmeticException e) {
            LOGGER.log(Level.SEVERE, "Error calculating compound interest", e);
            throw new CalculationException("Error calculating compound interest: " + e.getMessage(), e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalculationResult calculateEMI(CalculationInput input) throws CalculationException {
        Objects.requireNonNull(input, "CalculationInput cannot be null");
        
        BigDecimal principal = input.getPrincipal();
        int durationYears = input.getDurationYears();
        BigDecimal interestRate = input.getInterestRate();
        
        return calculateEMI(principal, durationYears, interestRate);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public CalculationResult calculateEMI(BigDecimal principal, int durationYears, BigDecimal interestRate) 
            throws CalculationException {
        try {
            // Validate inputs
            validateInputs(principal, durationYears, interestRate);
            
            LOGGER.log(Level.INFO, "Calculating EMI for Principal: {0}, Duration: {1} years, Interest Rate: {2}%", 
                new Object[]{principal, durationYears, interestRate});
            
            // Convert annual interest rate to monthly rate in decimal form (r/12/100)
            BigDecimal annualRateDecimal = BigDecimalUtils.percentageToDecimal(interestRate);
            BigDecimal monthlyRate = BigDecimalUtils.divide(annualRateDecimal, 
                new BigDecimal(CalculationConstants.MONTHS_IN_YEAR));
            
            // Convert loan duration from years to months (t*12)
            int totalMonths = convertYearsToMonths(durationYears);
            
            BigDecimal emiAmount;
            
            // Check if monthly rate is zero (special case)
            if (BigDecimalUtils.isZero(monthlyRate)) {
                // Simple division for zero interest rate
                emiAmount = BigDecimalUtils.divide(principal, new BigDecimal(totalMonths));
            } else {
                // Calculate (1 + r)^n
                BigDecimal onePlusMonthlyRate = BigDecimalUtils.add(CalculationConstants.ONE, monthlyRate);
                BigDecimal rateFactorPower = BigDecimalUtils.pow(onePlusMonthlyRate, totalMonths);
                
                // Calculate numerator: P × r × (1 + r)^n
                BigDecimal numerator = BigDecimalUtils.multiply(principal, monthlyRate);
                numerator = BigDecimalUtils.multiply(numerator, rateFactorPower);
                
                // Calculate denominator: (1 + r)^n - 1
                BigDecimal denominator = BigDecimalUtils.subtract(rateFactorPower, CalculationConstants.ONE);
                
                // Calculate EMI: [P × r × (1 + r)^n]/[(1 + r)^n - 1]
                emiAmount = BigDecimalUtils.divide(numerator, denominator);
            }
            
            emiAmount = BigDecimalUtils.roundForCalculation(emiAmount);
            
            // Calculate total amount payable (EMI * number of months)
            BigDecimal totalAmount = BigDecimalUtils.multiply(emiAmount, new BigDecimal(totalMonths));
            
            // Calculate total interest amount (total amount - principal)
            BigDecimal interestAmount = BigDecimalUtils.subtract(totalAmount, principal);
            
            LOGGER.log(Level.INFO, "EMI calculation result: EMI={0}, Total={1}, Interest={2}", 
                new Object[]{emiAmount, totalAmount, interestAmount});
            
            return new CalculationResult(emiAmount, totalAmount, interestAmount, interestRate, totalMonths);
        } catch (ArithmeticException e) {
            LOGGER.log(Level.SEVERE, "Error calculating EMI", e);
            throw new CalculationException("Error calculating EMI: " + e.getMessage(), e);
        }
    }

    /**
     * Validates the input parameters for calculations.
     *
     * @param principal The principal amount
     * @param durationYears The loan duration in years
     * @param interestRate The annual interest rate
     * @throws IllegalArgumentException if any input is invalid
     * @throws NullPointerException if principal or interestRate is null
     */
    private void validateInputs(BigDecimal principal, int durationYears, BigDecimal interestRate) {
        Objects.requireNonNull(principal, "Principal amount cannot be null");
        Objects.requireNonNull(interestRate, "Interest rate cannot be null");
        
        if (durationYears <= 0) {
            throw new IllegalArgumentException("Loan duration must be greater than zero");
        }
    }

    /**
     * Converts a duration in years to months.
     *
     * @param years The duration in years
     * @return The duration in months
     */
    private int convertYearsToMonths(int years) {
        return years * CalculationConstants.MONTHS_IN_YEAR;
    }
}