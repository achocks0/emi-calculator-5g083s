package com.bank.calculator.exception;

import com.bank.calculator.constant.ErrorMessages;
import com.bank.calculator.model.ValidationResult;

/**
 * Custom exception class for handling validation errors that occur during input validation.
 * This exception is thrown when user inputs fail validation checks, such as when
 * principal amount or loan duration values are invalid.
 */
public class ValidationException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    
    private String errorCode;
    private String errorMessage;
    
    /**
     * Constructs a new ValidationException with the default error message.
     */
    public ValidationException() {
        super(ErrorMessages.INVALID_INPUT);
        this.errorCode = ErrorMessages.ERROR_CODE_PREFIX + "001";
        this.errorMessage = ErrorMessages.INVALID_INPUT;
    }
    
    /**
     * Constructs a new ValidationException with a specific error message.
     *
     * @param message the detail message
     */
    public ValidationException(String message) {
        super(message);
        this.errorCode = ErrorMessages.ERROR_CODE_PREFIX + "001";
        this.errorMessage = message;
    }
    
    /**
     * Constructs a new ValidationException with a specific error message and cause.
     *
     * @param message the detail message
     * @param cause the cause of the exception
     */
    public ValidationException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = ErrorMessages.ERROR_CODE_PREFIX + "001";
        this.errorMessage = message;
    }
    
    /**
     * Constructs a new ValidationException with a specific error code and message.
     *
     * @param errorCode the error code
     * @param message the detail message
     */
    public ValidationException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.errorMessage = message;
    }
    
    /**
     * Constructs a new ValidationException from a ValidationResult.
     *
     * @param validationResult the validation result containing the error message
     */
    public ValidationException(ValidationResult validationResult) {
        super(validationResult.getErrorMessage());
        this.errorCode = ErrorMessages.ERROR_CODE_PREFIX + "001";
        this.errorMessage = validationResult.getErrorMessage();
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