package com.bank.calculator.model;

import java.io.Serializable; // JDK 11
import java.math.BigDecimal; // JDK 11
import java.util.Objects; // JDK 11

import com.bank.calculator.constant.CalculationConstants;

/**
 * Model class that encapsulates the input parameters for compound interest and EMI calculations.
 * This class stores the principal amount, loan duration in years, and interest rate
 * that will be used for calculations.
 */
public class CalculationInput implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private BigDecimal principal;
    private int durationYears;
    private BigDecimal interestRate;
    
    /**
     * Constructs a new CalculationInput with the specified principal amount and loan duration,
     * using the default interest rate.
     *
     * @param principal The principal amount for the loan
     * @param durationYears The loan duration in years
     * @throws NullPointerException if principal is null
     * @throws IllegalArgumentException if durationYears is zero or negative
     */
    public CalculationInput(BigDecimal principal, int durationYears) {
        Objects.requireNonNull(principal, "Principal amount cannot be null");
        if (durationYears <= 0) {
            throw new IllegalArgumentException("Loan duration must be greater than zero");
        }
        
        this.principal = principal;
        this.durationYears = durationYears;
        this.interestRate = CalculationConstants.DEFAULT_INTEREST_RATE;
    }
    
    /**
     * Returns the principal amount.
     *
     * @return The principal amount
     */
    public BigDecimal getPrincipal() {
        return principal;
    }
    
    /**
     * Returns the loan duration in years.
     *
     * @return The loan duration in years
     */
    public int getDurationYears() {
        return durationYears;
    }
    
    /**
     * Returns the annual interest rate.
     *
     * @return The annual interest rate
     */
    public BigDecimal getInterestRate() {
        return interestRate;
    }
    
    /**
     * Sets the annual interest rate.
     *
     * @param interestRate The annual interest rate to set
     * @throws NullPointerException if interestRate is null
     */
    public void setInterestRate(BigDecimal interestRate) {
        Objects.requireNonNull(interestRate, "Interest rate cannot be null");
        this.interestRate = interestRate;
    }
    
    /**
     * Returns the principal amount formatted with currency precision.
     *
     * @return The principal amount with currency precision
     */
    public BigDecimal getFormattedPrincipal() {
        return principal.round(CalculationConstants.CURRENCY_MATH_CONTEXT);
    }
    
    /**
     * Returns the annual interest rate as a percentage string.
     *
     * @return The annual interest rate as a percentage
     */
    public String getFormattedInterestRate() {
        return interestRate.multiply(CalculationConstants.HUNDRED)
                .round(CalculationConstants.CURRENCY_MATH_CONTEXT)
                .toString() + "%";
    }
    
    /**
     * Compares this CalculationInput to the specified object for equality.
     *
     * @param o The object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalculationInput that = (CalculationInput) o;
        return durationYears == that.durationYears &&
                Objects.equals(principal, that.principal) &&
                Objects.equals(interestRate, that.interestRate);
    }
    
    /**
     * Returns a hash code value for this CalculationInput.
     *
     * @return A hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(principal, durationYears, interestRate);
    }
    
    /**
     * Returns a string representation of this CalculationInput.
     *
     * @return A string representation
     */
    @Override
    public String toString() {
        return "CalculationInput{" +
                "principal=" + principal +
                ", durationYears=" + durationYears +
                ", interestRate=" + interestRate +
                '}';
    }
}