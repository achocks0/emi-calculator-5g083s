package com.bank.calculator.test.ui.component;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;
import org.junit.jupiter.api.Assertions;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import java.math.BigDecimal;
import java.util.logging.Logger;

import com.bank.calculator.ui.component.ResultSection;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.test.util.UITestUtils;
import com.bank.calculator.test.fixture.UITestFixture;

/**
 * Test class for the ResultSection UI component that verifies the display of EMI calculation results
 * in the Compound Interest Calculator application. This class tests the functionality of displaying
 * calculation results, toggling detailed results view, and clearing results.
 */
@ExtendWith(ApplicationExtension.class)
public class ResultSectionTest {

    private static final Logger LOGGER = Logger.getLogger(ResultSectionTest.class.getName());
    
    // IDs of UI elements to be tested
    private static final String EMI_AMOUNT_LABEL_ID = "emiAmountLabel";
    private static final String INFO_LABEL_ID = "infoLabel";
    private static final String DETAILED_RESULTS_PANE_ID = "detailedResultsPane";
    private static final String PRINCIPAL_AMOUNT_LABEL_ID = "principalAmountLabel";
    private static final String INTEREST_AMOUNT_LABEL_ID = "interestAmountLabel";
    private static final String TOTAL_AMOUNT_LABEL_ID = "totalAmountLabel";
    private static final String MONTHLY_INSTALLMENT_LABEL_ID = "monthlyInstallmentLabel";
    private static final String NUMBER_OF_INSTALLMENTS_LABEL_ID = "numberOfInstallmentsLabel";
    private static final String ANNUAL_INTEREST_RATE_LABEL_ID = "annualInterestRateLabel";
    private static final String TOGGLE_DETAILS_BUTTON_ID = "toggleDetailsButton";
    
    // Default display values
    private static final String DEFAULT_EMI_DISPLAY = "--";
    private static final String WAITING_MESSAGE = "Enter valid inputs to calculate EMI";
    
    private ResultSection resultSection;

    @BeforeEach
    void setUp() {
        LOGGER.info("Setting up test environment");
        // Initialize any test resources if needed
    }
    
    @AfterEach
    void tearDown() {
        LOGGER.info("Tearing down test environment");
        // Clean up any test resources if needed
    }
    
    @Start
    void start(Stage stage) {
        LOGGER.info("Initializing JavaFX application for testing");
        
        // Create the ResultSection component to be tested
        resultSection = new ResultSection();
        
        // Set up the stage with the ResultSection component
        Scene scene = new Scene(resultSection);
        stage.setScene(scene);
        stage.show();
        
        // Wait for JavaFX events to complete
        UITestUtils.waitForFxEvents();
    }
    
    @Test
    void testInitialState() {
        LOGGER.info("Testing initial state of ResultSection");
        
        // Verify that the EMI amount label shows the default value
        UITestUtils.verifyLabelText(new FxRobot(), EMI_AMOUNT_LABEL_ID, DEFAULT_EMI_DISPLAY);
        
        // Verify that the info label shows the waiting message
        UITestUtils.verifyLabelText(new FxRobot(), INFO_LABEL_ID, WAITING_MESSAGE);
        
        // Verify that the detailed results pane is not visible
        UITestUtils.verifyNodeNotVisible(new FxRobot(), DETAILED_RESULTS_PANE_ID);
        
        // Verify that the toggle details button is visible but disabled
        UITestUtils.verifyNodeVisible(new FxRobot(), TOGGLE_DETAILS_BUTTON_ID);
        Assertions.assertTrue(
            resultSection.lookup("#" + TOGGLE_DETAILS_BUTTON_ID).queryButton().isDisabled(),
            "Toggle details button should be disabled initially"
        );
        
        LOGGER.info("Initial state test completed successfully");
    }
    
    @Test
    void testDisplayResult() {
        LOGGER.info("Testing display of calculation result");
        
        // Create a mock calculation result
        CalculationResult mockResult = createMockCalculationResult();
        
        // Display the result
        resultSection.displayResult(mockResult);
        
        // Wait for JavaFX events to complete
        UITestUtils.waitForFxEvents();
        
        // Verify that the EMI amount label shows the formatted EMI amount
        UITestUtils.verifyLabelText(new FxRobot(), EMI_AMOUNT_LABEL_ID, mockResult.getFormattedEmiAmount());
        
        // Verify that the info label shows the interest rate information
        UITestUtils.verifyLabelText(new FxRobot(), INFO_LABEL_ID, "Based on an annual interest rate of");
        UITestUtils.verifyLabelText(new FxRobot(), INFO_LABEL_ID, mockResult.getFormattedAnnualInterestRate());
        
        // Verify that the toggle details button is enabled
        Assertions.assertFalse(
            resultSection.lookup("#" + TOGGLE_DETAILS_BUTTON_ID).queryButton().isDisabled(),
            "Toggle details button should be enabled after displaying result"
        );
        
        LOGGER.info("Display result test completed successfully");
    }
    
    @Test
    void testToggleDetailedResults() {
        LOGGER.info("Testing toggle of detailed results");
        
        // Create a mock calculation result
        CalculationResult mockResult = createMockCalculationResult();
        
        // Display the result
        resultSection.displayResult(mockResult);
        UITestUtils.waitForFxEvents();
        
        // Verify that detailed results pane is not visible initially
        UITestUtils.verifyNodeNotVisible(new FxRobot(), DETAILED_RESULTS_PANE_ID);
        
        // Click the toggle details button
        UITestUtils.clickButton(new FxRobot(), TOGGLE_DETAILS_BUTTON_ID);
        
        // Verify that detailed results pane is now visible
        UITestUtils.verifyNodeVisible(new FxRobot(), DETAILED_RESULTS_PANE_ID);
        
        // Verify the content of the detailed results
        UITestUtils.verifyLabelText(new FxRobot(), INTEREST_AMOUNT_LABEL_ID, mockResult.getFormattedInterestAmount());
        UITestUtils.verifyLabelText(new FxRobot(), TOTAL_AMOUNT_LABEL_ID, mockResult.getFormattedTotalAmount());
        UITestUtils.verifyLabelText(new FxRobot(), MONTHLY_INSTALLMENT_LABEL_ID, mockResult.getFormattedEmiAmount());
        UITestUtils.verifyLabelText(new FxRobot(), NUMBER_OF_INSTALLMENTS_LABEL_ID, 
                                    String.valueOf(mockResult.getNumberOfInstallments()));
        UITestUtils.verifyLabelText(new FxRobot(), ANNUAL_INTEREST_RATE_LABEL_ID, 
                                    mockResult.getFormattedAnnualInterestRate());
        
        // Click the toggle details button again
        UITestUtils.clickButton(new FxRobot(), TOGGLE_DETAILS_BUTTON_ID);
        
        // Verify that detailed results pane is not visible again
        UITestUtils.verifyNodeNotVisible(new FxRobot(), DETAILED_RESULTS_PANE_ID);
        
        LOGGER.info("Toggle detailed results test completed successfully");
    }
    
    @Test
    void testClearResult() {
        LOGGER.info("Testing clearing of calculation result");
        
        // Create a mock calculation result
        CalculationResult mockResult = createMockCalculationResult();
        
        // Display the result
        resultSection.displayResult(mockResult);
        UITestUtils.waitForFxEvents();
        
        // Clear the result
        resultSection.clearResult();
        UITestUtils.waitForFxEvents();
        
        // Verify that the EMI amount label shows the default value
        UITestUtils.verifyLabelText(new FxRobot(), EMI_AMOUNT_LABEL_ID, DEFAULT_EMI_DISPLAY);
        
        // Verify that the info label shows the waiting message
        UITestUtils.verifyLabelText(new FxRobot(), INFO_LABEL_ID, WAITING_MESSAGE);
        
        // Verify that the detailed results pane is not visible
        UITestUtils.verifyNodeNotVisible(new FxRobot(), DETAILED_RESULTS_PANE_ID);
        
        // Verify that the toggle details button is disabled
        Assertions.assertTrue(
            resultSection.lookup("#" + TOGGLE_DETAILS_BUTTON_ID).queryButton().isDisabled(),
            "Toggle details button should be disabled after clearing result"
        );
        
        LOGGER.info("Clear result test completed successfully");
    }
    
    @Test
    void testDetailedResultsContent() {
        LOGGER.info("Testing content of detailed results");
        
        // Create a mock calculation result
        CalculationResult mockResult = createMockCalculationResult();
        
        // Display the result
        resultSection.displayResult(mockResult);
        UITestUtils.waitForFxEvents();
        
        // Click the toggle details button to show detailed results
        UITestUtils.clickButton(new FxRobot(), TOGGLE_DETAILS_BUTTON_ID);
        
        // Wait for JavaFX events to complete
        UITestUtils.waitForFxEvents();
        
        // Verify that the detailed results show correct values
        UITestUtils.verifyLabelText(new FxRobot(), INTEREST_AMOUNT_LABEL_ID, mockResult.getFormattedInterestAmount());
        UITestUtils.verifyLabelText(new FxRobot(), TOTAL_AMOUNT_LABEL_ID, mockResult.getFormattedTotalAmount());
        UITestUtils.verifyLabelText(new FxRobot(), MONTHLY_INSTALLMENT_LABEL_ID, mockResult.getFormattedEmiAmount());
        UITestUtils.verifyLabelText(new FxRobot(), NUMBER_OF_INSTALLMENTS_LABEL_ID, 
                                   String.valueOf(mockResult.getNumberOfInstallments()));
        UITestUtils.verifyLabelText(new FxRobot(), ANNUAL_INTEREST_RATE_LABEL_ID, 
                                   mockResult.getFormattedAnnualInterestRate());
        
        LOGGER.info("Detailed results content test completed successfully");
    }
    
    /**
     * Creates a mock CalculationResult for testing.
     *
     * @return A mock calculation result
     */
    private CalculationResult createMockCalculationResult() {
        // Create values that match UITestFixture.STANDARD_RESULT ($483.65)
        BigDecimal emiAmount = new BigDecimal("483.65");
        BigDecimal totalAmount = new BigDecimal("29019.00");
        BigDecimal interestAmount = new BigDecimal("4019.00");
        BigDecimal annualInterestRate = new BigDecimal("0.075");  // 7.5%
        int numberOfInstallments = 60;  // 5 years * 12 months
        
        return new CalculationResult(
                emiAmount, 
                totalAmount, 
                interestAmount, 
                annualInterestRate, 
                numberOfInstallments);
    }
}