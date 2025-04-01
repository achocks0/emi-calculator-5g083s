package com.bank.calculator.model;

import java.io.Serializable;
import java.util.Objects;

/**
 * Model class that encapsulates the result of input validation operations in the Compound Interest Calculator application.
 * This class provides information about whether validation was successful and, if not, the specific error message
 * describing the validation failure.
 */
public class ValidationResult implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private final boolean valid;
    private final String errorMessage;
    
    /**
     * Constructs a new ValidationResult with the specified validity and error message.
     *
     * @param valid indicates whether validation was successful
     * @param errorMessage the error message, or null if validation was successful
     */
    public ValidationResult(boolean valid, String errorMessage) {
        this.valid = valid;
        this.errorMessage = errorMessage;
    }
    
    /**
     * Returns whether the validation was successful.
     *
     * @return true if validation was successful, false otherwise
     */
    public boolean isValid() {
        return valid;
    }
    
    /**
     * Returns the error message describing the validation failure.
     *
     * @return the error message, or null if validation was successful
     */
    public String getErrorMessage() {
        return errorMessage;
    }
    
    /**
     * Creates a ValidationResult instance representing a successful validation with no error message.
     *
     * @return a ValidationResult instance with valid=true and no error message
     */
    public static ValidationResult createValid() {
        return new ValidationResult(true, null);
    }
    
    /**
     * Creates a ValidationResult instance representing a failed validation with the specified error message.
     *
     * @param errorMessage the error message describing the validation failure
     * @return a ValidationResult instance with valid=false and the specified error message
     * @throws NullPointerException if errorMessage is null
     */
    public static ValidationResult createInvalid(String errorMessage) {
        Objects.requireNonNull(errorMessage, "Error message cannot be null");
        return new ValidationResult(false, errorMessage);
    }
    
    /**
     * Compares this ValidationResult to the specified object for equality.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ValidationResult that = (ValidationResult) o;
        return valid == that.valid &&
               Objects.equals(errorMessage, that.errorMessage);
    }
    
    /**
     * Returns a hash code value for this ValidationResult.
     *
     * @return a hash code value
     */
    @Override
    public int hashCode() {
        return Objects.hash(valid, errorMessage);
    }
    
    /**
     * Returns a string representation of this ValidationResult.
     *
     * @return a string representation
     */
    @Override
    public String toString() {
        return "ValidationResult{" +
               "valid=" + valid +
               ", errorMessage='" + errorMessage + '\'' +
               '}';
    }
}