package com.bank.calculator.model;

import java.io.Serializable; // JDK 11
import java.math.BigDecimal; // JDK 11
import java.util.Objects; // JDK 11

import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.util.BigDecimalUtils;
import com.bank.calculator.util.CurrencyUtils;

/**
 * Model class that encapsulates the results of EMI calculations.
 * This class stores the calculated EMI amount, total amount payable, total interest amount,
 * annual interest rate, and number of installments. It provides methods to access these values
 * in both raw and formatted forms for display in the UI.
 */
public class CalculationResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final BigDecimal emiAmount;
    private final BigDecimal totalAmount;
    private final BigDecimal interestAmount;
    private final BigDecimal annualInterestRate;
    private final int numberOfInstallments;
    
    /**
     * Constructs a new CalculationResult with the specified calculation results.
     *
     * @param emiAmount The monthly EMI amount
     * @param totalAmount The total amount payable over the loan duration
     * @param interestAmount The total interest amount payable over the loan duration
     * @param annualInterestRate The annual interest rate used for the calculation
     * @param numberOfInstallments The number of monthly installments
     * @throws NullPointerException if any of the BigDecimal parameters are null
     * @throws IllegalArgumentException if numberOfInstallments is less than or equal to zero
     */
    public CalculationResult(BigDecimal emiAmount, BigDecimal totalAmount, BigDecimal interestAmount,
                            BigDecimal annualInterestRate, int numberOfInstallments) {
        // Validate inputs
        Objects.requireNonNull(emiAmount, "EMI amount cannot be null");
        Objects.requireNonNull(totalAmount, "Total amount cannot be null");
        Objects.requireNonNull(interestAmount, "Interest amount cannot be null");
        Objects.requireNonNull(annualInterestRate, "Annual interest rate cannot be null");
        
        if (numberOfInstallments <= 0) {
            throw new IllegalArgumentException("Number of installments must be positive");
        }
        
        // Assign values
        this.emiAmount = emiAmount;
        this.totalAmount = totalAmount;
        this.interestAmount = interestAmount;
        this.annualInterestRate = annualInterestRate;
        this.numberOfInstallments = numberOfInstallments;
    }
    
    /**
     * Returns the monthly EMI amount.
     *
     * @return The monthly EMI amount
     */
    public BigDecimal getEmiAmount() {
        return emiAmount;
    }
    
    /**
     * Returns the total amount payable over the loan duration.
     *
     * @return The total amount payable
     */
    public BigDecimal getTotalAmount() {
        return totalAmount;
    }
    
    /**
     * Returns the total interest amount payable over the loan duration.
     *
     * @return The total interest amount
     */
    public BigDecimal getInterestAmount() {
        return interestAmount;
    }
    
    /**
     * Returns the annual interest rate used for the calculation.
     *
     * @return The annual interest rate
     */
    public BigDecimal getAnnualInterestRate() {
        return annualInterestRate;
    }
    
    /**
     * Returns the number of monthly installments.
     *
     * @return The number of monthly installments
     */
    public int getNumberOfInstallments() {
        return numberOfInstallments;
    }
    
    /**
     * Returns the monthly EMI amount formatted with currency precision.
     *
     * @return The formatted EMI amount with currency symbol
     */
    public String getFormattedEmiAmount() {
        return CurrencyUtils.formatAsCurrency(emiAmount);
    }
    
    /**
     * Returns the total amount payable formatted with currency precision.
     *
     * @return The formatted total amount with currency symbol
     */
    public String getFormattedTotalAmount() {
        return CurrencyUtils.formatAsCurrency(totalAmount);
    }
    
    /**
     * Returns the total interest amount formatted with currency precision.
     *
     * @return The formatted interest amount with currency symbol
     */
    public String getFormattedInterestAmount() {
        return CurrencyUtils.formatAsCurrency(interestAmount);
    }
    
    /**
     * Returns the annual interest rate as a percentage string.
     *
     * @return The annual interest rate as a percentage (e.g., "7.5%")
     */
    public String getFormattedAnnualInterestRate() {
        BigDecimal percentageRate = BigDecimalUtils.decimalToPercentage(annualInterestRate);
        return percentageRate.round(CalculationConstants.CURRENCY_MATH_CONTEXT).toPlainString() + "%";
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CalculationResult that = (CalculationResult) o;
        return numberOfInstallments == that.numberOfInstallments &&
               emiAmount.compareTo(that.emiAmount) == 0 &&
               totalAmount.compareTo(that.totalAmount) == 0 &&
               interestAmount.compareTo(that.interestAmount) == 0 &&
               annualInterestRate.compareTo(that.annualInterestRate) == 0;
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(emiAmount, totalAmount, interestAmount, annualInterestRate, numberOfInstallments);
    }
    
    @Override
    public String toString() {
        return "CalculationResult{" +
               "emiAmount=" + emiAmount +
               ", totalAmount=" + totalAmount +
               ", interestAmount=" + interestAmount +
               ", annualInterestRate=" + annualInterestRate +
               ", numberOfInstallments=" + numberOfInstallments +
               '}';
    }
}