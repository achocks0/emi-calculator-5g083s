package com.bank.calculator.ui.component;

import com.bank.calculator.service.ValidationService;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.ui.validator.InputValidator;
import com.bank.calculator.ui.dialog.HelpDialog;

import javafx.fxml.FXMLLoader;
import javafx.fxml.FXML;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.scene.input.MouseEvent;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

import java.util.Objects;
import java.util.logging.Logger;
import java.util.logging.Level;
import java.io.IOException;

/**
 * JavaFX UI component that provides the input section for the Compound Interest Calculator application.
 * This component collects and validates user inputs for principal amount and loan duration,
 * providing real-time validation feedback and help functionality.
 */
public class InputSection extends VBox {
    
    private final ValidationService validationService;
    private InputValidator inputValidator;
    private BooleanProperty inputsValidProperty;
    
    private static final Logger LOGGER = Logger.getLogger(InputSection.class.getName());
    private static final String FXML_PATH = "/fxml/input-section.fxml";
    
    @FXML private TextField principalField;
    @FXML private TextField durationField;
    @FXML private Label principalErrorLabel;
    @FXML private Label durationErrorLabel;
    @FXML private Text principalHelpIcon;
    @FXML private Text durationHelpIcon;
    
    /**
     * Constructs a new InputSection with the specified validation service.
     *
     * @param validationService the validation service to use for input validation
     * @throws NullPointerException if validationService is null
     */
    public InputSection(ValidationService validationService) {
        Objects.requireNonNull(validationService, "ValidationService cannot be null");
        this.validationService = validationService;
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_PATH));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
            LOGGER.log(Level.INFO, "InputSection component created successfully");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load InputSection FXML", e);
            throw new RuntimeException("Could not initialize InputSection component", e);
        }
    }
    
    /**
     * Initializes the input section component after FXML loading.
     */
    @FXML
    private void initialize() {
        LOGGER.log(Level.INFO, "Initializing InputSection component");
        
        // Initialize properties
        inputsValidProperty = new SimpleBooleanProperty(false);
        
        // Set up input validator
        inputValidator = new InputValidator(validationService);
        
        // Set up validation for input fields
        inputValidator.setupPrincipalValidation(principalField, principalErrorLabel);
        inputValidator.setupDurationValidation(durationField, durationErrorLabel);
        
        // Add focus listeners for improved user experience
        principalField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                inputValidator.validatePrincipalField(principalField, principalErrorLabel);
            }
        });
        
        durationField.focusedProperty().addListener((obs, wasFocused, isNowFocused) -> {
            if (!isNowFocused) {
                inputValidator.validateDurationField(durationField, durationErrorLabel);
            }
        });
        
        LOGGER.log(Level.INFO, "InputSection initialized successfully");
    }
    
    /**
     * Handles click events on the principal amount help icon.
     *
     * @param event the mouse event
     */
    @FXML
    private void handlePrincipalHelpClick(MouseEvent event) {
        LOGGER.log(Level.INFO, "Principal help icon clicked");
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.showHelpForField("principal");
        event.consume();
    }
    
    /**
     * Handles click events on the loan duration help icon.
     *
     * @param event the mouse event
     */
    @FXML
    private void handleDurationHelpClick(MouseEvent event) {
        LOGGER.log(Level.INFO, "Duration help icon clicked");
        HelpDialog helpDialog = new HelpDialog();
        helpDialog.showHelpForField("duration");
        event.consume();
    }
    
    /**
     * Gets the principal amount entered by the user.
     *
     * @return the principal amount as a string
     */
    public String getPrincipalAmount() {
        return principalField.getText();
    }
    
    /**
     * Gets the loan duration entered by the user.
     *
     * @return the loan duration as a string
     */
    public String getDuration() {
        return durationField.getText();
    }
    
    /**
     * Clears all input fields and error messages.
     */
    public void clearInputs() {
        principalField.clear();
        durationField.clear();
        inputValidator.clearValidationErrors(principalField, durationField, principalErrorLabel);
        inputsValidProperty.set(false);
        LOGGER.log(Level.INFO, "Input fields cleared");
    }
    
    /**
     * Validates all input fields and updates UI accordingly.
     *
     * @return true if all inputs are valid, false otherwise
     */
    public boolean validateInputs() {
        LOGGER.log(Level.INFO, "Validating input fields");
        
        boolean principalValid = inputValidator.validatePrincipalField(principalField, principalErrorLabel);
        boolean durationValid = inputValidator.validateDurationField(durationField, durationErrorLabel);
        
        boolean allValid = principalValid && durationValid;
        inputsValidProperty.set(allValid);
        
        LOGGER.log(Level.INFO, "Input validation result: {0}", allValid);
        return allValid;
    }
    
    /**
     * Gets the property that tracks whether all inputs are valid.
     *
     * @return the inputs valid property
     */
    public BooleanProperty getInputsValidProperty() {
        return inputsValidProperty;
    }
    
    /**
     * Checks if all inputs are currently valid.
     *
     * @return true if all inputs are valid, false otherwise
     */
    public boolean areInputsValid() {
        return inputsValidProperty.get();
    }
}