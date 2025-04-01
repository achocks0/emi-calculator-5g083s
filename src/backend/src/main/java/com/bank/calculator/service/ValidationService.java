package com.bank.calculator.service;

import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.constant.CalculationConstants;

/**
 * Service interface that defines methods for validating user inputs in the 
 * Compound Interest Calculator application. This interface provides methods 
 * for validating principal amount and loan duration inputs, ensuring they meet 
 * the application's requirements before being used in calculations.
 */
public interface ValidationService {
    
    /**
     * Validates that the principal amount is a positive number with up to 2 decimal places
     * and within allowed range.
     *
     * @param principalStr The principal amount as a string
     * @return A ValidationResult object indicating whether the principal amount is valid
     */
    ValidationResult validatePrincipal(String principalStr);
    
    /**
     * Validates that the loan duration is a positive integer within allowed range.
     *
     * @param durationStr The loan duration as a string
     * @return A ValidationResult object indicating whether the loan duration is valid
     */
    ValidationResult validateDuration(String durationStr);
    
    /**
     * Validates all inputs required for calculation.
     *
     * @param principalStr The principal amount as a string
     * @param durationStr The loan duration as a string
     * @return A ValidationResult object indicating whether all inputs are valid
     */
    ValidationResult validateAllInputs(String principalStr, String durationStr);
    
    /**
     * Validates a CalculationInput object.
     *
     * @param input The CalculationInput object to validate
     * @return A ValidationResult object indicating whether the calculation input is valid
     */
    ValidationResult validateCalculationInput(CalculationInput input);
}