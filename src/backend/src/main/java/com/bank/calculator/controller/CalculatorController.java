package com.bank.calculator.controller;

import java.math.BigDecimal; // JDK 11
import java.util.logging.Logger; // JDK 11
import java.util.logging.Level; // JDK 11
import java.util.Objects; // JDK 11

import com.bank.calculator.service.ValidationService;
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.exception.ValidationException;
import com.bank.calculator.exception.CalculationException;
import com.bank.calculator.util.CurrencyUtils;

/**
 * Controller class that coordinates between the UI and service layers in the Compound Interest Calculator application.
 * This class handles user interactions, validates inputs, performs calculations, and formats results for display.
 */
public class CalculatorController {
    
    private final ValidationService validationService;
    private final CalculationService calculationService;
    private static final Logger LOGGER = Logger.getLogger(CalculatorController.class.getName());
    
    /**
     * Constructs a new CalculatorController with the specified validation and calculation services.
     *
     * @param validationService the service for validating user inputs
     * @param calculationService the service for performing calculations
     * @throws NullPointerException if either validationService or calculationService is null
     */
    public CalculatorController(ValidationService validationService, CalculationService calculationService) {
        Objects.requireNonNull(validationService, "ValidationService cannot be null");
        Objects.requireNonNull(calculationService, "CalculationService cannot be null");
        this.validationService = validationService;
        this.calculationService = calculationService;
        LOGGER.info("CalculatorController initialized");
    }
    
    /**
     * Validates the principal amount and loan duration inputs.
     *
     * @param principalStr the principal amount as a string
     * @param durationStr the loan duration as a string
     * @return a ValidationResult object indicating whether inputs are valid
     */
    public ValidationResult validateInputs(String principalStr, String durationStr) {
        LOGGER.log(Level.INFO, "Validating inputs: principal={0}, duration={1}", new Object[]{principalStr, durationStr});
        
        ValidationResult result = validationService.validateAllInputs(principalStr, durationStr);
        
        if (result.isValid()) {
            LOGGER.info("Input validation successful");
        } else {
            LOGGER.log(Level.WARNING, "Input validation failed: {0}", result.getErrorMessage());
        }
        
        return result;
    }
    
    /**
     * Calculates the EMI based on validated principal amount and loan duration.
     *
     * @param principalStr the principal amount as a string
     * @param durationStr the loan duration as a string
     * @return a CalculationResult containing the EMI amount and other calculation details
     * @throws ValidationException if inputs are invalid
     * @throws CalculationException if an error occurs during calculation
     */
    public CalculationResult calculateEMI(String principalStr, String durationStr) {
        LOGGER.log(Level.INFO, "Calculating EMI for: principal={0}, duration={1}", new Object[]{principalStr, durationStr});
        
        // Validate inputs first
        ValidationResult validationResult = validateInputs(principalStr, durationStr);
        if (!validationResult.isValid()) {
            LOGGER.log(Level.WARNING, "Validation failed: {0}", validationResult.getErrorMessage());
            throw new ValidationException(validationResult);
        }
        
        try {
            // Parse the validated inputs
            BigDecimal principal = CurrencyUtils.parseCurrencyValue(principalStr);
            int duration = Integer.parseInt(durationStr);
            
            // Create input object and perform calculation
            CalculationInput input = new CalculationInput(principal, duration);
            CalculationResult result = calculationService.calculateEMI(input);
            
            LOGGER.log(Level.INFO, "EMI calculation successful: {0}", result.getEmiAmount());
            return result;
        } catch (NumberFormatException e) {
            // This should generally not happen if validation is successful
            LOGGER.log(Level.SEVERE, "Number format error during calculation", e);
            throw new ValidationException("Invalid number format: " + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            LOGGER.log(Level.SEVERE, "Illegal argument error during calculation", e);
            throw new ValidationException("Invalid argument: " + e.getMessage(), e);
        } catch (CalculationException e) {
            LOGGER.log(Level.SEVERE, "Calculation error", e);
            throw e; // Re-throw calculation exceptions
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during calculation", e);
            throw new CalculationException("An unexpected error occurred during calculation", e);
        }
    }
    
    /**
     * Formats the calculation result for display.
     *
     * @param result the calculation result to format
     * @return the formatted EMI amount as a currency string
     * @throws NullPointerException if result is null
     */
    public String formatResult(CalculationResult result) {
        Objects.requireNonNull(result, "Calculation result cannot be null");
        
        String formattedEmi = result.getFormattedEmiAmount();
        LOGGER.log(Level.INFO, "Formatted result: {0}", formattedEmi);
        
        return formattedEmi;
    }
}