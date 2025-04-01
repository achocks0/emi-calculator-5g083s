package com.bank.calculator.service.impl;

import java.math.BigDecimal; // JDK 11
import java.util.Objects; // JDK 11
import org.apache.commons.lang3.StringUtils; // version 3.12.0

import com.bank.calculator.service.ValidationService;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.util.ValidationUtils;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.constant.ErrorMessages;

/**
 * Implementation of the ValidationService interface that provides methods for validating
 * user inputs in the Compound Interest Calculator application. This class validates
 * principal amount and loan duration inputs to ensure they meet the application's
 * requirements before being used in calculations.
 */
public class ValidationServiceImpl implements ValidationService {
    
    /**
     * Default constructor for ValidationServiceImpl.
     */
    public ValidationServiceImpl() {
        // Default constructor
    }

    /**
     * Validates that the principal amount is a positive number with up to 2 decimal places
     * and within allowed range.
     *
     * @param principalStr The principal amount as a string
     * @return A ValidationResult object indicating whether the principal amount is valid
     */
    @Override
    public ValidationResult validatePrincipal(String principalStr) {
        return ValidationUtils.validatePrincipal(principalStr);
    }

    /**
     * Validates that the loan duration is a positive integer within allowed range.
     *
     * @param durationStr The loan duration as a string
     * @return A ValidationResult object indicating whether the loan duration is valid
     */
    @Override
    public ValidationResult validateDuration(String durationStr) {
        return ValidationUtils.validateDuration(durationStr);
    }

    /**
     * Validates all inputs required for calculation.
     *
     * @param principalStr The principal amount as a string
     * @param durationStr The loan duration as a string
     * @return A ValidationResult object indicating whether all inputs are valid
     */
    @Override
    public ValidationResult validateAllInputs(String principalStr, String durationStr) {
        // Validate principal amount
        ValidationResult principalValidation = validatePrincipal(principalStr);
        if (!principalValidation.isValid()) {
            return principalValidation;
        }
        
        // Validate loan duration
        ValidationResult durationValidation = validateDuration(durationStr);
        if (!durationValidation.isValid()) {
            return durationValidation;
        }
        
        // All validations passed
        return ValidationResult.createValid();
    }

    /**
     * Validates a CalculationInput object.
     *
     * @param input The CalculationInput object to validate
     * @return A ValidationResult object indicating whether the calculation input is valid
     */
    @Override
    public ValidationResult validateCalculationInput(CalculationInput input) {
        // Check if input is null
        if (input == null) {
            return ValidationResult.createInvalid(ErrorMessages.INVALID_INPUT);
        }
        
        // Get principal and validate
        BigDecimal principal = input.getPrincipal();
        if (principal.compareTo(CalculationConstants.MIN_PRINCIPAL_AMOUNT) < 0) {
            return ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_MIN_REQUIRED);
        }
        if (principal.compareTo(CalculationConstants.MAX_PRINCIPAL_AMOUNT) > 0) {
            return ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_MAX_EXCEEDED);
        }
        
        // Get duration and validate
        int duration = input.getDurationYears();
        if (duration < CalculationConstants.MIN_DURATION_YEARS) {
            return ValidationResult.createInvalid(ErrorMessages.DURATION_MIN_REQUIRED);
        }
        if (duration > CalculationConstants.MAX_DURATION_YEARS) {
            return ValidationResult.createInvalid(ErrorMessages.DURATION_MAX_EXCEEDED);
        }
        
        // All validations passed
        return ValidationResult.createValid();
    }
}