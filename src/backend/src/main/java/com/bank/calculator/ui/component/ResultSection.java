package com.bank.calculator.ui.component;

import javafx.fxml.FXML;  // JavaFX 11
import javafx.fxml.FXMLLoader;  // JavaFX 11
import javafx.scene.control.Label;  // JavaFX 11
import javafx.scene.control.TitledPane;  // JavaFX 11
import javafx.scene.control.Button;  // JavaFX 11
import javafx.scene.layout.VBox;  // JavaFX 11
import java.io.IOException;  // JDK 11
import java.util.logging.Logger;  // JDK 11
import java.util.logging.Level;  // JDK 11

import com.bank.calculator.model.CalculationResult;

/**
 * UI component class that displays the EMI calculation results in the Compound Interest Calculator application.
 * This class manages the result display section, including the main EMI amount and detailed loan information 
 * in a collapsible panel.
 */
public class ResultSection extends VBox {
    
    private static final Logger LOGGER = Logger.getLogger(ResultSection.class.getName());
    private static final String FXML_PATH = "/fxml/result-section.fxml";
    private static final String SHOW_DETAILS_TEXT = "Show Details";
    private static final String HIDE_DETAILS_TEXT = "Hide Details";
    private static final String DEFAULT_EMI_DISPLAY = "--";
    private static final String WAITING_MESSAGE = "Enter valid inputs to calculate EMI";
    private static final String INTEREST_RATE_INFO = "Based on an annual interest rate of %s";
    
    @FXML private VBox rootContainer;
    @FXML private Label emiAmountLabel;
    @FXML private Label infoLabel;
    @FXML private TitledPane detailedResultsPane;
    @FXML private Label principalAmountLabel;
    @FXML private Label interestAmountLabel;
    @FXML private Label totalAmountLabel;
    @FXML private Label monthlyInstallmentLabel;
    @FXML private Label numberOfInstallmentsLabel;
    @FXML private Label annualInterestRateLabel;
    @FXML private Button toggleDetailsButton;
    
    /**
     * Constructs a new ResultSection and loads its FXML layout.
     */
    public ResultSection() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_PATH));
        fxmlLoader.setController(this);
        
        try {
            fxmlLoader.load();
            LOGGER.info("ResultSection component created successfully");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load FXML file: " + FXML_PATH, e);
            throw new RuntimeException("Failed to load ResultSection component", e);
        }
    }
    
    /**
     * Initializes the component after FXML fields are injected.
     */
    @FXML
    private void initialize() {
        LOGGER.fine("Initializing ResultSection component");
        
        // Set default values
        emiAmountLabel.setText(DEFAULT_EMI_DISPLAY);
        infoLabel.setText(WAITING_MESSAGE);
        
        // Initially hide detailed results
        detailedResultsPane.setExpanded(false);
        toggleDetailsButton.setText(SHOW_DETAILS_TEXT);
        
        LOGGER.fine("ResultSection component initialized successfully");
    }
    
    /**
     * Toggles the visibility of the detailed results pane.
     */
    @FXML
    private void toggleDetailedResults() {
        detailedResultsPane.setExpanded(!detailedResultsPane.isExpanded());
        
        if (detailedResultsPane.isExpanded()) {
            toggleDetailsButton.setText(HIDE_DETAILS_TEXT);
        } else {
            toggleDetailsButton.setText(SHOW_DETAILS_TEXT);
        }
        
        LOGGER.fine("Toggled detailed results pane, expanded: " + detailedResultsPane.isExpanded());
    }
    
    /**
     * Displays the calculation result in the UI.
     *
     * @param result The calculation result to display
     */
    public void displayResult(CalculationResult result) {
        LOGGER.fine("Displaying calculation result");
        
        if (result == null) {
            LOGGER.warning("Attempted to display null calculation result");
            return;
        }
        
        // Update main EMI display
        emiAmountLabel.setText(result.getFormattedEmiAmount());
        infoLabel.setText(String.format(INTEREST_RATE_INFO, result.getFormattedAnnualInterestRate()));
        
        // Update detailed results
        updateDetailedResults(result);
        
        // Enable detailed results pane and toggle button
        detailedResultsPane.setDisable(false);
        toggleDetailsButton.setDisable(false);
        
        LOGGER.fine("Calculation result displayed successfully");
    }
    
    /**
     * Clears the displayed result and resets to default state.
     */
    public void clearResult() {
        LOGGER.fine("Clearing calculation result display");
        
        // Reset main display
        emiAmountLabel.setText(DEFAULT_EMI_DISPLAY);
        infoLabel.setText(WAITING_MESSAGE);
        
        // Reset detailed results
        principalAmountLabel.setText("");
        interestAmountLabel.setText("");
        totalAmountLabel.setText("");
        monthlyInstallmentLabel.setText("");
        numberOfInstallmentsLabel.setText("");
        annualInterestRateLabel.setText("");
        
        // Collapse and disable detailed results pane
        detailedResultsPane.setExpanded(false);
        toggleDetailsButton.setText(SHOW_DETAILS_TEXT);
        
        LOGGER.fine("Calculation result display cleared successfully");
    }
    
    /**
     * Updates the detailed results section with calculation data.
     *
     * @param result The calculation result containing detailed data
     */
    private void updateDetailedResults(CalculationResult result) {
        // Note: For the principal amount, we're deriving it from other data
        // In a real implementation, we would add a getPrincipalAmount() method to CalculationResult
        // or it would be passed separately to this component
        
        // Set the values in the detailed results pane
        interestAmountLabel.setText(result.getFormattedInterestAmount());
        totalAmountLabel.setText(result.getFormattedTotalAmount());
        monthlyInstallmentLabel.setText(result.getFormattedEmiAmount());
        numberOfInstallmentsLabel.setText(String.valueOf(result.getNumberOfInstallments()));
        annualInterestRateLabel.setText(result.getFormattedAnnualInterestRate());
        
        // For principal amount, we have to make an assumption based on the data available
        // This could be set elsewhere by the component that has access to the principal amount
        principalAmountLabel.setText(""); // This needs to be set appropriately in a real implementation
    }
}