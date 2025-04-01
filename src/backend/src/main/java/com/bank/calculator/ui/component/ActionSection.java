package com.bank.calculator.ui.component;

import javafx.fxml.FXML;  // JavaFX 11
import javafx.fxml.FXMLLoader;  // JavaFX 11
import javafx.scene.control.Button;  // JavaFX 11
import javafx.scene.layout.HBox;  // JavaFX 11
import javafx.scene.control.Alert;  // JavaFX 11
import javafx.scene.control.Alert.AlertType;  // JavaFX 11
import javafx.beans.binding.BooleanBinding;  // JavaFX 11
import javafx.beans.binding.Bindings;  // JavaFX 11

import java.util.Objects;  // JDK 11
import java.util.logging.Logger;  // JDK 11
import java.util.logging.Level;  // JDK 11
import java.io.IOException;  // JDK 11

import com.bank.calculator.controller.CalculatorController;
import com.bank.calculator.ui.component.InputSection;
import com.bank.calculator.ui.component.ResultSection;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.exception.ValidationException;
import com.bank.calculator.exception.CalculationException;

/**
 * UI component class that provides the action buttons section for the Compound Interest Calculator application.
 * This component contains the Calculate EMI and New Calculation buttons and handles their respective actions.
 */
public class ActionSection extends HBox {
    
    private final CalculatorController calculatorController;
    private final InputSection inputSection;
    private final ResultSection resultSection;
    
    private static final Logger LOGGER = Logger.getLogger(ActionSection.class.getName());
    private static final String FXML_PATH = "/fxml/action-section.fxml";
    private static final String ERROR_DIALOG_TITLE = "Calculation Error";
    private static final String GENERIC_ERROR_MESSAGE = "An error occurred during calculation. Please try again.";
    
    @FXML private HBox rootContainer;
    @FXML private Button calculateButton;
    @FXML private Button newCalculationButton;
    
    /**
     * Constructs a new ActionSection with the specified controller and related UI components.
     *
     * @param calculatorController the controller for calculation operations
     * @param inputSection the input section component for getting user inputs
     * @param resultSection the result section component for displaying results
     * @throws NullPointerException if any of the parameters are null
     */
    public ActionSection(CalculatorController calculatorController, InputSection inputSection, ResultSection resultSection) {
        Objects.requireNonNull(calculatorController, "CalculatorController cannot be null");
        Objects.requireNonNull(inputSection, "InputSection cannot be null");
        Objects.requireNonNull(resultSection, "ResultSection cannot be null");
        
        this.calculatorController = calculatorController;
        this.inputSection = inputSection;
        this.resultSection = resultSection;
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_PATH));
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
            LOGGER.log(Level.INFO, "ActionSection component created successfully");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load ActionSection FXML", e);
            throw new RuntimeException("Could not initialize ActionSection component", e);
        }
    }
    
    /**
     * Initializes the component after FXML fields are injected.
     */
    @FXML
    private void initialize() {
        LOGGER.log(Level.INFO, "Initializing ActionSection component");
        
        // Bind calculateButton's disable property to the negation of inputSection's inputsValidProperty
        calculateButton.disableProperty().bind(inputSection.getInputsValidProperty().not());
        
        LOGGER.log(Level.INFO, "ActionSection initialized successfully");
    }
    
    /**
     * Handles the Calculate EMI button click event.
     */
    @FXML
    public void handleCalculateAction() {
        LOGGER.log(Level.INFO, "Calculate button clicked - attempting calculation");
        
        // Validate inputs before proceeding
        if (!inputSection.validateInputs()) {
            LOGGER.log(Level.INFO, "Input validation failed, calculation aborted");
            return;
        }
        
        // Get input values
        String principalAmount = inputSection.getPrincipalAmount();
        String duration = inputSection.getDuration();
        
        try {
            // Perform calculation
            CalculationResult result = calculatorController.calculateEMI(principalAmount, duration);
            
            // Display result
            resultSection.displayResult(result);
            
            LOGGER.log(Level.INFO, "EMI calculation completed successfully: {0}", result.getEmiAmount());
        } catch (ValidationException e) {
            showErrorDialog(e.getMessage());
            LOGGER.log(Level.WARNING, "Validation error during calculation", e);
        } catch (CalculationException e) {
            showErrorDialog(e.getMessage());
            LOGGER.log(Level.WARNING, "Calculation error", e);
        } catch (Exception e) {
            showErrorDialog(GENERIC_ERROR_MESSAGE);
            LOGGER.log(Level.SEVERE, "Unexpected error during calculation", e);
        }
    }
    
    /**
     * Handles the New Calculation button click event.
     */
    @FXML
    public void handleNewCalculationAction() {
        LOGGER.log(Level.INFO, "New Calculation button clicked");
        
        // Clear input fields
        inputSection.clearInputs();
        
        // Clear result display
        resultSection.clearResult();
        
        LOGGER.log(Level.INFO, "Application reset for new calculation");
    }
    
    /**
     * Displays an error dialog with the specified message.
     *
     * @param message the error message to display
     */
    private void showErrorDialog(String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(ERROR_DIALOG_TITLE);
        alert.setContentText(message);
        alert.showAndWait();
        
        LOGGER.log(Level.INFO, "Error dialog displayed: {0}", message);
    }
}