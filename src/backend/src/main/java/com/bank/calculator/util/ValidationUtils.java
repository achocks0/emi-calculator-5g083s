package com.bank.calculator.util;

import java.math.BigDecimal;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import org.apache.commons.lang3.StringUtils; // version 3.12.0

import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.constant.ErrorMessages;
import com.bank.calculator.constant.CalculationConstants;

/**
 * Utility class providing static methods for validating user inputs
 * in the Compound Interest Calculator application.
 * <p>
 * This class contains methods for validating principal amount and loan duration
 * according to the application's requirements, ensuring that inputs are properly
 * formatted and within acceptable ranges.
 */
public final class ValidationUtils {

    /**
     * Regular expression pattern for validating principal amount.
     * Accepts positive numbers with up to 2 decimal places.
     */
    private static final Pattern PRINCIPAL_PATTERN = Pattern.compile("^\\d+(\\.\\d{1,2})?$");
    
    /**
     * Regular expression pattern for validating loan duration.
     * Accepts positive integers only.
     */
    private static final Pattern DURATION_PATTERN = Pattern.compile("^\\d+$");
    
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private ValidationUtils() {
        throw new UnsupportedOperationException("ValidationUtils utility class cannot be instantiated");
    }
    
    /**
     * Validates that the principal amount is a positive number with up to 2 decimal places
     * and within the allowed range.
     *
     * @param principalStr the principal amount as a string
     * @return a ValidationResult object indicating whether the principal amount is valid
     */
    public static ValidationResult validatePrincipal(String principalStr) {
        // Check if principal is null or empty
        if (StringUtils.isBlank(principalStr)) {
            return ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_REQUIRED);
        }
        
        // Check if principal has the correct format
        if (!PRINCIPAL_PATTERN.matcher(principalStr).matches()) {
            return ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_FORMAT);
        }
        
        try {
            // Parse the principal amount to BigDecimal
            BigDecimal principal = new BigDecimal(principalStr);
            
            // Check if principal is positive
            if (principal.compareTo(BigDecimal.ZERO) <= 0) {
                return ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_POSITIVE);
            }
            
            // Check if principal is within allowed range
            if (principal.compareTo(CalculationConstants.MIN_PRINCIPAL_AMOUNT) < 0) {
                return ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_MIN_REQUIRED);
            }
            
            if (principal.compareTo(CalculationConstants.MAX_PRINCIPAL_AMOUNT) > 0) {
                return ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_MAX_EXCEEDED);
            }
            
            // All validations passed
            return ValidationResult.createValid();
        } catch (NumberFormatException e) {
            // This should not happen if the regex validation passed,
            // but added as a safeguard
            return ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_FORMAT);
        }
    }
    
    /**
     * Validates that the loan duration is a positive integer within the allowed range.
     *
     * @param durationStr the loan duration as a string
     * @return a ValidationResult object indicating whether the loan duration is valid
     */
    public static ValidationResult validateDuration(String durationStr) {
        // Check if duration is null or empty
        if (StringUtils.isBlank(durationStr)) {
            return ValidationResult.createInvalid(ErrorMessages.DURATION_REQUIRED);
        }
        
        // Check if duration has the correct format
        if (!DURATION_PATTERN.matcher(durationStr).matches()) {
            return ValidationResult.createInvalid(ErrorMessages.DURATION_FORMAT);
        }
        
        try {
            // Parse the duration to int
            int duration = Integer.parseInt(durationStr);
            
            // Check if duration is positive
            if (duration <= 0) {
                return ValidationResult.createInvalid(ErrorMessages.DURATION_POSITIVE);
            }
            
            // Check if duration is within allowed range
            if (duration < CalculationConstants.MIN_DURATION_YEARS) {
                return ValidationResult.createInvalid(ErrorMessages.DURATION_MIN_REQUIRED);
            }
            
            if (duration > CalculationConstants.MAX_DURATION_YEARS) {
                return ValidationResult.createInvalid(ErrorMessages.DURATION_MAX_EXCEEDED);
            }
            
            // All validations passed
            return ValidationResult.createValid();
        } catch (NumberFormatException e) {
            // This should not happen if the regex validation passed,
            // but added as a safeguard
            return ValidationResult.createInvalid(ErrorMessages.DURATION_FORMAT);
        }
    }
    
    /**
     * Checks if a string represents a positive number.
     *
     * @param value the string to check
     * @return true if the string represents a positive number, false otherwise
     */
    private static boolean isPositiveNumber(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        
        try {
            BigDecimal number = new BigDecimal(value);
            return number.compareTo(BigDecimal.ZERO) > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
    /**
     * Checks if a string represents a positive integer.
     *
     * @param value the string to check
     * @return true if the string represents a positive integer, false otherwise
     */
    private static boolean isPositiveInteger(String value) {
        if (StringUtils.isBlank(value)) {
            return false;
        }
        
        if (!DURATION_PATTERN.matcher(value).matches()) {
            return false;
        }
        
        try {
            int number = Integer.parseInt(value);
            return number > 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}