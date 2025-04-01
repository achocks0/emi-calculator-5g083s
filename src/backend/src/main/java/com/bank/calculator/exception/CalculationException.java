package com.bank.calculator.exception;

import com.bank.calculator.constant.ErrorMessages;

/**
 * Custom exception class for handling calculation errors that occur during 
 * compound interest and EMI calculations. This exception is thrown when 
 * mathematical operations fail or produce invalid results during the calculation process.
 */
public class CalculationException extends RuntimeException {
    // JDK 11
    private static final long serialVersionUID = 1L;
    
    private String errorCode;
    private String errorMessage;
    
    /**
     * Constructs a new CalculationException with the default error message.
     */
    public CalculationException() {
        super(ErrorMessages.CALCULATION_ERROR);
        this.errorCode = ErrorMessages.ERROR_CODE_PREFIX + "003";
        this.errorMessage = ErrorMessages.CALCULATION_ERROR;
    }
    
    /**
     * Constructs a new CalculationException with a specific error message.
     * 
     * @param message the detail message
     */
    public CalculationException(String message) {
        super(message);
        this.errorCode = ErrorMessages.ERROR_CODE_PREFIX + "003";
        this.errorMessage = message;
    }
    
    /**
     * Constructs a new CalculationException with a specific error message and cause.
     * 
     * @param message the detail message
     * @param cause the cause of this exception
     */
    public CalculationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorMessages.ERROR_CODE_PREFIX + "003";
        this.errorMessage = message;
    }
    
    /**
     * Constructs a new CalculationException with a specific error code and message.
     * 
     * @param errorCode the error code for this exception
     * @param message the detail message
     */
    public CalculationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }
    
    /**
     * Returns the error code associated with this exception.
     * 
     * @return the error code
     */
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * Returns the error message associated with this exception.
     * 
     * @return the error message
     */
    public String getErrorMessage() {
        return errorMessage;
    }
}