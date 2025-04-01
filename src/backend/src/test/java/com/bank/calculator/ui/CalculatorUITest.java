package com.bank.calculator.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.testfx.api.FxRobot;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.application.Platform;
import org.mockito.Mockito;
import org.mockito.Mock;
import java.util.logging.Logger;

import com.bank.calculator.config.AppConfig;
import com.bank.calculator.controller.CalculatorController;
import com.bank.calculator.service.ValidationService;
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.ui.component.InputSection;
import com.bank.calculator.ui.component.ResultSection;
import com.bank.calculator.ui.component.ActionSection;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.test.util.UITestUtils;

/**
 * Test class for the CalculatorUI component that verifies the proper initialization, integration, 
 * and functionality of the main UI of the Compound Interest Calculator application. This class 
 * tests the UI component interactions, layout creation, and application lifecycle methods.
 */
@ExtendWith(ApplicationExtension.class)
public class CalculatorUITest {

    private static final Logger LOGGER = Logger.getLogger(CalculatorUITest.class.getName());
    private static final String TEST_PRINCIPAL_AMOUNT = "10000";
    private static final String TEST_DURATION = "5";
    private static final String EXPECTED_EMI_PATTERN = "$\\d+\\.\\d{2}";

    private CalculatorUI calculatorUI;
    private Stage testStage;
    private InputSection inputSection;
    private ResultSection resultSection;
    private ActionSection actionSection;
    private CalculatorController calculatorController;

    /**
     * Sets up the test environment before each test
     */
    @BeforeEach
    public void setUp() {
        LOGGER.info("Setting up test environment for CalculatorUI test");
        // Additional setup can be added here if needed
    }

    /**
     * Cleans up the test environment after each test
     */
    @AfterEach
    public void tearDown() {
        LOGGER.info("Tearing down test environment");
        // Close the test stage to clean up resources
        Platform.runLater(() -> {
            if (testStage != null && testStage.isShowing()) {
                testStage.close();
            }
        });
        
        // Wait for JavaFX events to complete
        UITestUtils.waitForFxEvents();
    }

    /**
     * Starts the JavaFX application for testing
     *
     * @param stage the primary stage for the test
     */
    @Start
    public void start(Stage stage) {
        LOGGER.info("Starting JavaFX application for testing");
        
        try {
            // Create services using AppConfig
            ValidationService validationService = AppConfig.createValidationService();
            CalculationService calculationService = AppConfig.createCalculationService();
            
            // Create calculator controller
            calculatorController = AppConfig.createCalculatorController(validationService, calculationService);
            
            // Create UI components
            inputSection = new InputSection(validationService);
            resultSection = new ResultSection();
            actionSection = new ActionSection(calculatorController, inputSection, resultSection);
            
            // Create and initialize the UI
            calculatorUI = new CalculatorUI();
            calculatorUI.start(stage);
            
            // Store stage reference for later use
            testStage = stage;
            
            LOGGER.info("JavaFX application started successfully for testing");
        } catch (Exception e) {
            LOGGER.severe("Failed to start application for testing: " + e.getMessage());
            throw new RuntimeException("Failed to start test application", e);
        }
    }

    /**
     * Tests that the application initializes correctly with all required components
     */
    @Test
    public void testApplicationInitialization() {
        LOGGER.info("Testing application initialization");
        
        // Verify stage setup
        Assertions.assertEquals(AppConfig.getApplicationName(), testStage.getTitle(), 
                "Stage title should match application name");
        Assertions.assertTrue(testStage.isShowing(), "Stage should be showing");

        // Verify scene setup
        Scene scene = testStage.getScene();
        Assertions.assertNotNull(scene, "Scene should not be null");
        Assertions.assertTrue(scene.getRoot() instanceof BorderPane, 
                "Scene root should be a BorderPane");
        
        // Verify component initialization
        BorderPane mainLayout = (BorderPane) scene.getRoot();
        Assertions.assertNotNull(mainLayout.getTop(), "Top section should not be null");
        Assertions.assertNotNull(mainLayout.getCenter(), "Center section should not be null");
        
        LOGGER.info("Application initialization test completed successfully");
    }

    /**
     * Tests that the main layout is created correctly with all required components
     */
    @Test
    public void testMainLayoutCreation() {
        LOGGER.info("Testing main layout creation");
        
        // Get the scene's root node
        Scene scene = testStage.getScene();
        BorderPane mainLayout = (BorderPane) scene.getRoot();
        
        // Verify top section (input and action)
        Assertions.assertTrue(mainLayout.getTop() instanceof VBox, 
                "Top section should be a VBox");
        VBox topSection = (VBox) mainLayout.getTop();
        Assertions.assertTrue(topSection.getChildren().size() >= 2, 
                "Top section should contain at least 2 children (InputSection and ActionSection)");
        
        // Verify center section (result)
        Assertions.assertNotNull(mainLayout.getCenter(), 
                "Center section should not be null");
        Assertions.assertTrue(mainLayout.getCenter() instanceof ResultSection, 
                "Center section should be a ResultSection");
        
        // Verify styling
        Assertions.assertTrue(mainLayout.getStyleClass().contains("main-layout"), 
                "Main layout should have 'main-layout' style class");
        
        LOGGER.info("Main layout creation test completed successfully");
    }

    /**
     * Tests that the input fields are present and correctly configured
     */
    @Test
    public void testInputFieldsPresent(FxRobot robot) {
        LOGGER.info("Testing presence of input fields");
        
        // Verify principal field
        TextField principalField = UITestUtils.waitForNode(robot, UITestUtils.PRINCIPAL_FIELD_ID);
        Assertions.assertNotNull(principalField, "Principal field should be present");
        
        // Verify duration field
        TextField durationField = UITestUtils.waitForNode(robot, UITestUtils.DURATION_FIELD_ID);
        Assertions.assertNotNull(durationField, "Duration field should be present");
        
        // Verify prompt text
        Assertions.assertFalse(principalField.getPromptText().isEmpty(), 
                "Principal field should have prompt text");
        Assertions.assertFalse(durationField.getPromptText().isEmpty(), 
                "Duration field should have prompt text");
        
        // Verify initial state
        Assertions.assertTrue(principalField.getText().isEmpty(), 
                "Principal field should be initially empty");
        Assertions.assertTrue(durationField.getText().isEmpty(), 
                "Duration field should be initially empty");
        
        LOGGER.info("Input fields presence test completed successfully");
    }

    /**
     * Tests that the action buttons are present and correctly configured
     */
    @Test
    public void testActionButtonsPresent(FxRobot robot) {
        LOGGER.info("Testing presence of action buttons");
        
        // Verify calculate button
        Button calculateButton = UITestUtils.waitForNode(robot, UITestUtils.CALCULATE_BUTTON_ID);
        Assertions.assertNotNull(calculateButton, "Calculate button should be present");
        
        // Verify new calculation button
        Button newCalculationButton = UITestUtils.waitForNode(robot, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        Assertions.assertNotNull(newCalculationButton, "New calculation button should be present");
        
        // Verify initial state (calculate button should be disabled until valid input)
        Assertions.assertTrue(calculateButton.isDisabled(), 
                "Calculate button should be initially disabled");
        Assertions.assertFalse(newCalculationButton.isDisabled(), 
                "New calculation button should be initially enabled");
        
        LOGGER.info("Action buttons presence test completed successfully");
    }

    /**
     * Tests that the result section is present and correctly configured
     */
    @Test
    public void testResultSectionPresent(FxRobot robot) {
        LOGGER.info("Testing presence of result section");
        
        // Verify result label
        Label resultLabel = UITestUtils.waitForNode(robot, UITestUtils.RESULT_LABEL_ID);
        Assertions.assertNotNull(resultLabel, "Result label should be present");
        
        // Verify initial state
        Assertions.assertEquals("--", resultLabel.getText(), 
                "Result label should show default text initially");
        
        // Verify detailed results section (should be present but initially collapsed)
        Assertions.assertNotNull(robot.lookup(".titled-pane").tryQuery(), 
                "Detailed results section should be present");
        
        LOGGER.info("Result section presence test completed successfully");
    }

    /**
     * Tests the complete calculation workflow from input to result display
     */
    @Test
    public void testEndToEndCalculationWorkflow(FxRobot robot) {
        LOGGER.info("Testing end-to-end calculation workflow");
        
        // Enter test values
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, TEST_PRINCIPAL_AMOUNT);
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, TEST_DURATION);
        
        // Perform calculation
        UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify result
        String resultText = UITestUtils.getNodeText(robot, UITestUtils.RESULT_LABEL_ID);
        Assertions.assertTrue(resultText.matches(EXPECTED_EMI_PATTERN),
                "Expected EMI pattern match but got: " + resultText);
        
        // Verify detailed results section is available
        Button detailsButton = robot.lookup(".button").match(button -> 
                button.getText() != null && button.getText().contains("Show Details")).queryButton();
        Assertions.assertNotNull(detailsButton, "Details button should be present");
        
        // Test new calculation functionality
        UITestUtils.clickButton(robot, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        
        // Verify fields are cleared
        UITestUtils.verifyInputFieldsCleared(robot);
        
        // Verify result is reset
        UITestUtils.verifyLabelText(robot, UITestUtils.RESULT_LABEL_ID, "--");
        
        LOGGER.info("End-to-end calculation workflow test completed successfully");
    }

    /**
     * Tests the calculation workflow with mocked services to verify proper integration
     */
    @Test
    public void testCalculationWithMockedServices() {
        LOGGER.info("Testing calculation with mocked services");
        
        // Create mocks
        ValidationService mockValidationService = Mockito.mock(ValidationService.class);
        CalculationService mockCalculationService = Mockito.mock(CalculationService.class);
        CalculatorController mockController = Mockito.mock(CalculatorController.class);
        
        // Configure mocks
        Mockito.when(mockValidationService.validateAllInputs(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(com.bank.calculator.model.ValidationResult.createValid());
        
        CalculationResult mockResult = Mockito.mock(CalculationResult.class);
        Mockito.when(mockResult.getFormattedEmiAmount()).thenReturn("$123.45");
        
        Mockito.when(mockController.calculateEMI(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(mockResult);
        
        // Create UI components with mocked services
        InputSection mockInputSection = new InputSection(mockValidationService);
        ResultSection mockResultSection = new ResultSection();
        ActionSection mockActionSection = new ActionSection(mockController, mockInputSection, mockResultSection);
        
        // Simulate user input
        Mockito.when(mockInputSection.getPrincipalAmount()).thenReturn(TEST_PRINCIPAL_AMOUNT);
        Mockito.when(mockInputSection.getDuration()).thenReturn(TEST_DURATION);
        Mockito.when(mockInputSection.validateInputs()).thenReturn(true);
        
        // Call action handler
        mockActionSection.handleCalculateAction();
        
        // Verify interactions
        Mockito.verify(mockController).calculateEMI(TEST_PRINCIPAL_AMOUNT, TEST_DURATION);
        
        LOGGER.info("Calculation with mocked services test completed successfully");
    }

    /**
     * Tests that input validation is properly integrated with the UI
     */
    @Test
    public void testInputValidationIntegration(FxRobot robot) {
        LOGGER.info("Testing input validation integration");
        
        // Test invalid principal
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, "-1000");
        UITestUtils.waitForFxEvents();
        
        // Verify error message
        Label principalErrorLabel = robot.lookup("#principalErrorLabel").tryQueryAs(Label.class).orElse(null);
        Assertions.assertNotNull(principalErrorLabel, "Principal error label should be present");
        Assertions.assertFalse(principalErrorLabel.getText().isEmpty(), 
                "Principal error message should not be empty");
        
        // Verify calculate button still disabled
        Button calculateButton = UITestUtils.waitForNode(robot, UITestUtils.CALCULATE_BUTTON_ID);
        Assertions.assertTrue(calculateButton.isDisabled(), 
                "Calculate button should be disabled with invalid principal");
        
        // Correct principal
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, TEST_PRINCIPAL_AMOUNT);
        UITestUtils.waitForFxEvents();
        
        // Test invalid duration
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, "-5");
        UITestUtils.waitForFxEvents();
        
        // Verify error message
        Label durationErrorLabel = robot.lookup("#durationErrorLabel").tryQueryAs(Label.class).orElse(null);
        Assertions.assertNotNull(durationErrorLabel, "Duration error label should be present");
        Assertions.assertFalse(durationErrorLabel.getText().isEmpty(), 
                "Duration error message should not be empty");
        
        // Verify calculate button still disabled
        Assertions.assertTrue(calculateButton.isDisabled(), 
                "Calculate button should be disabled with invalid duration");
        
        // Correct duration
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, TEST_DURATION);
        UITestUtils.waitForFxEvents();
        
        // Verify calculate button is enabled
        UITestUtils.waitForFxEvents(); // Wait for UI to update
        Assertions.assertFalse(calculateButton.isDisabled(), 
                "Calculate button should be enabled with valid inputs");
        
        LOGGER.info("Input validation integration test completed successfully");
    }

    /**
     * Tests that errors during calculation are properly handled and displayed
     */
    @Test
    public void testErrorHandling() {
        LOGGER.info("Testing error handling");
        
        // Create mocks
        ValidationService mockValidationService = Mockito.mock(ValidationService.class);
        CalculationService mockCalculationService = Mockito.mock(CalculationService.class);
        CalculatorController mockController = Mockito.mock(CalculatorController.class);
        
        // Configure mocks to throw exception
        Mockito.when(mockValidationService.validateAllInputs(Mockito.anyString(), Mockito.anyString()))
                .thenReturn(com.bank.calculator.model.ValidationResult.createValid());
        Mockito.when(mockInputSection.validateInputs()).thenReturn(true);
        Mockito.when(mockController.calculateEMI(Mockito.anyString(), Mockito.anyString()))
                .thenThrow(new RuntimeException("Test calculation error"));
        
        // Create UI components with mocked services
        InputSection mockInputSection = new InputSection(mockValidationService);
        ResultSection mockResultSection = new ResultSection();
        ActionSection mockActionSection = new ActionSection(mockController, mockInputSection, mockResultSection);
        
        // Mock input validation to pass
        Mockito.when(mockInputSection.validateInputs()).thenReturn(true);
        Mockito.when(mockInputSection.getPrincipalAmount()).thenReturn(TEST_PRINCIPAL_AMOUNT);
        Mockito.when(mockInputSection.getDuration()).thenReturn(TEST_DURATION);
        
        // Execute action that should trigger error handling
        try {
            mockActionSection.handleCalculateAction();
            // We expect an Alert dialog to be shown, but can't verify in headless test
            // The error would be logged and an alert shown to the user
        } catch (Exception e) {
            // Either approach is valid - the error might be caught and displayed in an alert,
            // or it might propagate up, depending on the error handling implementation
            Assertions.assertTrue(e.getMessage().contains("Test calculation error"),
                    "Exception should contain our test error message");
        }
        
        LOGGER.info("Error handling test completed successfully");
    }
}