package com.bank.calculator.constant;

/**
 * Contains standardized error messages used throughout the Compound Interest Calculator application.
 * This class centralizes all error messages to ensure consistency in error reporting across the application.
 * 
 * These messages are used for:
 * - Input validation errors (principal amount and loan duration)
 * - Calculation errors (interest rates, division by zero, etc.)
 * - System errors (unexpected exceptions)
 * 
 * By maintaining messages in a single location, we ensure consistent user experience
 * and simplify future modifications to error text.
 */
public final class ErrorMessages {

    /**
     * Prefix for error codes in the application.
     */
    public static final String ERROR_CODE_PREFIX = "E";
    
    /**
     * Generic invalid input message.
     */
    public static final String INVALID_INPUT = "Invalid input. Please check your entries and try again.";
    
    /**
     * General calculation error message.
     */
    public static final String CALCULATION_ERROR = "An error occurred during calculation. Please try again.";
    
    /**
     * System error message for unexpected exceptions.
     */
    public static final String SYSTEM_ERROR = "System error. Please restart the application.";
    
    // Principal amount validation messages
    
    /**
     * Error message when principal amount is missing.
     */
    public static final String PRINCIPAL_REQUIRED = "Principal amount is required.";
    
    /**
     * Error message when principal amount is not positive.
     */
    public static final String PRINCIPAL_POSITIVE = "Principal amount must be a positive number.";
    
    /**
     * Error message when principal amount format is invalid.
     */
    public static final String PRINCIPAL_FORMAT = "Principal amount must be a number with up to 2 decimal places.";
    
    /**
     * Error message when principal amount is below minimum threshold.
     */
    public static final String PRINCIPAL_MIN_REQUIRED = "Principal amount must be at least $1,000.00.";
    
    /**
     * Error message when principal amount exceeds maximum threshold.
     */
    public static final String PRINCIPAL_MAX_EXCEEDED = "Principal amount cannot exceed $1,000,000.00.";
    
    // Loan duration validation messages
    
    /**
     * Error message when loan duration is missing.
     */
    public static final String DURATION_REQUIRED = "Loan duration is required.";
    
    /**
     * Error message when loan duration is not positive.
     */
    public static final String DURATION_POSITIVE = "Loan duration must be a positive whole number.";
    
    /**
     * Error message when loan duration format is invalid.
     */
    public static final String DURATION_FORMAT = "Loan duration must be a whole number.";
    
    /**
     * Error message when loan duration is below minimum threshold.
     */
    public static final String DURATION_MIN_REQUIRED = "Loan duration must be at least 1 year.";
    
    /**
     * Error message when loan duration exceeds maximum threshold.
     */
    public static final String DURATION_MAX_EXCEEDED = "Loan duration cannot exceed 30 years.";
    
    // Interest rate validation messages
    
    /**
     * Error message when interest rate is zero.
     */
    public static final String ZERO_INTEREST_RATE = "Interest rate cannot be zero for compound interest calculation.";
    
    /**
     * Error message when interest rate is negative.
     */
    public static final String NEGATIVE_INTEREST_RATE = "Interest rate cannot be negative.";
    
    // Calculation error messages
    
    /**
     * Error message for division by zero error.
     */
    public static final String DIVISION_BY_ZERO = "Cannot divide by zero during calculation.";
    
    /**
     * Error message for numeric overflow.
     */
    public static final String NUMERIC_OVERFLOW = "Numeric overflow occurred during calculation. Try smaller values.";
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ErrorMessages() {
        // Utility class should not be instantiated
        throw new UnsupportedOperationException("ErrorMessages utility class cannot be instantiated");
    }
}