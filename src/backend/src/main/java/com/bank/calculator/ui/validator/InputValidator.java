package com.bank.calculator.ui.validator;

import com.bank.calculator.service.ValidationService;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.constant.ErrorMessages;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * UI component validator class that provides JavaFX-specific validation for user inputs 
 * in the Compound Interest Calculator application. This class integrates with JavaFX UI controls 
 * to provide real-time validation feedback as users enter principal amount and loan duration values.
 */
public class InputValidator {
    
    private final ValidationService validationService;
    private final StringProperty principalErrorProperty;
    private final StringProperty durationErrorProperty;
    
    private static final Logger LOGGER = Logger.getLogger(InputValidator.class.getName());
    private static final String CSS_ERROR_CLASS = "error-field";
    
    /**
     * Constructs a new InputValidator with the specified validation service.
     *
     * @param validationService the validation service to use for input validation
     * @throws NullPointerException if validationService is null
     */
    public InputValidator(ValidationService validationService) {
        Objects.requireNonNull(validationService, "ValidationService cannot be null");
        this.validationService = validationService;
        this.principalErrorProperty = new SimpleStringProperty("");
        this.durationErrorProperty = new SimpleStringProperty("");
        LOGGER.log(Level.INFO, "InputValidator initialized");
    }
    
    /**
     * Sets up real-time validation for the principal amount text field.
     *
     * @param principalField the text field for principal amount input
     * @param errorLabel the label to display validation error messages
     * @throws NullPointerException if principalField or errorLabel is null
     */
    public void setupPrincipalValidation(TextField principalField, Label errorLabel) {
        Objects.requireNonNull(principalField, "Principal field cannot be null");
        Objects.requireNonNull(errorLabel, "Error label cannot be null");
        
        principalField.textProperty().addListener((observable, oldValue, newValue) -> {
            ValidationResult result = validationService.validatePrincipal(newValue);
            if (!result.isValid()) {
                applyErrorStyle(principalField);
                errorLabel.setText(result.getErrorMessage());
                principalErrorProperty.set(result.getErrorMessage());
                LOGGER.log(Level.INFO, "Principal validation failed: {0}", result.getErrorMessage());
            } else {
                removeErrorStyle(principalField);
                errorLabel.setText("");
                principalErrorProperty.set("");
                LOGGER.log(Level.INFO, "Principal validation passed");
            }
        });
    }
    
    /**
     * Sets up real-time validation for the loan duration text field.
     *
     * @param durationField the text field for loan duration input
     * @param errorLabel the label to display validation error messages
     * @throws NullPointerException if durationField or errorLabel is null
     */
    public void setupDurationValidation(TextField durationField, Label errorLabel) {
        Objects.requireNonNull(durationField, "Duration field cannot be null");
        Objects.requireNonNull(errorLabel, "Error label cannot be null");
        
        durationField.textProperty().addListener((observable, oldValue, newValue) -> {
            ValidationResult result = validationService.validateDuration(newValue);
            if (!result.isValid()) {
                applyErrorStyle(durationField);
                errorLabel.setText(result.getErrorMessage());
                durationErrorProperty.set(result.getErrorMessage());
                LOGGER.log(Level.INFO, "Duration validation failed: {0}", result.getErrorMessage());
            } else {
                removeErrorStyle(durationField);
                errorLabel.setText("");
                durationErrorProperty.set("");
                LOGGER.log(Level.INFO, "Duration validation passed");
            }
        });
    }
    
    /**
     * Validates the principal amount text field and updates UI accordingly.
     *
     * @param principalField the text field for principal amount input
     * @param errorLabel the label to display validation error messages
     * @return true if validation succeeds, false otherwise
     * @throws NullPointerException if principalField or errorLabel is null
     */
    public boolean validatePrincipalField(TextField principalField, Label errorLabel) {
        Objects.requireNonNull(principalField, "Principal field cannot be null");
        Objects.requireNonNull(errorLabel, "Error label cannot be null");
        
        String principalText = principalField.getText();
        ValidationResult result = validationService.validatePrincipal(principalText);
        
        if (!result.isValid()) {
            applyErrorStyle(principalField);
            errorLabel.setText(result.getErrorMessage());
            principalErrorProperty.set(result.getErrorMessage());
            LOGGER.log(Level.INFO, "Principal validation failed: {0}", result.getErrorMessage());
            return false;
        } else {
            removeErrorStyle(principalField);
            errorLabel.setText("");
            principalErrorProperty.set("");
            LOGGER.log(Level.INFO, "Principal validation passed");
            return true;
        }
    }
    
    /**
     * Validates the loan duration text field and updates UI accordingly.
     *
     * @param durationField the text field for loan duration input
     * @param errorLabel the label to display validation error messages
     * @return true if validation succeeds, false otherwise
     * @throws NullPointerException if durationField or errorLabel is null
     */
    public boolean validateDurationField(TextField durationField, Label errorLabel) {
        Objects.requireNonNull(durationField, "Duration field cannot be null");
        Objects.requireNonNull(errorLabel, "Error label cannot be null");
        
        String durationText = durationField.getText();
        ValidationResult result = validationService.validateDuration(durationText);
        
        if (!result.isValid()) {
            applyErrorStyle(durationField);
            errorLabel.setText(result.getErrorMessage());
            durationErrorProperty.set(result.getErrorMessage());
            LOGGER.log(Level.INFO, "Duration validation failed: {0}", result.getErrorMessage());
            return false;
        } else {
            removeErrorStyle(durationField);
            errorLabel.setText("");
            durationErrorProperty.set("");
            LOGGER.log(Level.INFO, "Duration validation passed");
            return true;
        }
    }
    
    /**
     * Clears validation errors from all fields.
     *
     * @param principalField the text field for principal amount input
     * @param durationField the text field for loan duration input
     * @param errorLabel the label displaying validation error messages
     */
    public void clearValidationErrors(TextField principalField, TextField durationField, Label errorLabel) {
        removeErrorStyle(principalField);
        removeErrorStyle(durationField);
        errorLabel.setText("");
        principalErrorProperty.set("");
        durationErrorProperty.set("");
        LOGGER.log(Level.INFO, "Validation errors cleared");
    }
    
    /**
     * Gets the property containing principal validation error messages.
     *
     * @return the principal error property
     */
    public StringProperty getPrincipalErrorProperty() {
        return principalErrorProperty;
    }
    
    /**
     * Gets the property containing duration validation error messages.
     *
     * @return the duration error property
     */
    public StringProperty getDurationErrorProperty() {
        return durationErrorProperty;
    }
    
    /**
     * Applies error styling to a text field.
     *
     * @param field the text field to style
     */
    private void applyErrorStyle(TextField field) {
        if (!field.getStyleClass().contains(CSS_ERROR_CLASS)) {
            field.getStyleClass().add(CSS_ERROR_CLASS);
        }
    }
    
    /**
     * Removes error styling from a text field.
     *
     * @param field the text field to remove styling from
     */
    private void removeErrorStyle(TextField field) {
        field.getStyleClass().remove(CSS_ERROR_CLASS);
    }
}