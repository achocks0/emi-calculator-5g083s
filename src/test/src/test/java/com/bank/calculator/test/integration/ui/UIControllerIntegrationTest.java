package com.bank.calculator.test.integration.ui;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.extension.ExtendWith;

import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.scene.control.TextField;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.geometry.Insets;

import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.logging.Logger;

import com.bank.calculator.test.category.IntegrationTest;
import com.bank.calculator.test.category.UITest;
import com.bank.calculator.test.util.UITestUtils;
import com.bank.calculator.test.fixture.UITestFixture;
import com.bank.calculator.controller.CalculatorController;
import com.bank.calculator.service.ValidationService;
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.constant.ErrorMessages;
import com.bank.calculator.exception.CalculationException;
import com.bank.calculator.util.CurrencyUtils;

@ExtendWith(ApplicationExtension.class)
@IntegrationTest
@UITest
public class UIControllerIntegrationTest {

    private static final Logger LOGGER = Logger.getLogger(UIControllerIntegrationTest.class.getName());
    
    private FxRobot robot;
    private Stage stage;
    
    @Mock private ValidationService validationService;
    @Mock private CalculationService calculationService;
    
    private CalculatorController controller;
    
    @BeforeEach
    public void setup() {
        LOGGER.info("Setting up test environment");
        
        // Initialize mock services
        MockitoAnnotations.openMocks(this);
        
        // Create a real CalculatorController with mock services
        controller = new CalculatorController(validationService, calculationService);
        
        // Configure mock services behavior for standard test cases
        configureMockServices();
    }
    
    @AfterEach
    public void tearDown() {
        LOGGER.info("Tearing down test environment");
        
        // Reset all mocks
        Mockito.reset(validationService, calculationService);
    }
    
    @Start
    public void start(Stage stage) {
        LOGGER.info("Initializing JavaFX application for UI testing");
        this.stage = stage;
        
        // Create UI components
        TextField principalField = new TextField();
        principalField.setId(UITestUtils.PRINCIPAL_FIELD_ID);
        principalField.setPromptText("Enter principal amount");
        
        TextField durationField = new TextField();
        durationField.setId(UITestUtils.DURATION_FIELD_ID);
        durationField.setPromptText("Enter loan duration in years");
        
        Button calculateButton = new Button("Calculate EMI");
        calculateButton.setId(UITestUtils.CALCULATE_BUTTON_ID);
        
        Label resultLabel = new Label();
        resultLabel.setId(UITestUtils.RESULT_LABEL_ID);
        
        Label principalErrorLabel = new Label();
        principalErrorLabel.setId(UITestUtils.PRINCIPAL_ERROR_LABEL_ID);
        principalErrorLabel.setStyle("-fx-text-fill: red;");
        principalErrorLabel.setVisible(false);
        
        Label durationErrorLabel = new Label();
        durationErrorLabel.setId(UITestUtils.DURATION_ERROR_LABEL_ID);
        durationErrorLabel.setStyle("-fx-text-fill: red;");
        durationErrorLabel.setVisible(false);
        
        // Set up layout
        VBox layout = new VBox(10);
        layout.setPadding(new Insets(20));
        layout.getChildren().addAll(
            new Label("Principal Amount (USD):"),
            principalField,
            principalErrorLabel,
            new Label("Loan Duration (Years):"),
            durationField,
            durationErrorLabel,
            calculateButton,
            new Label("Monthly EMI:"),
            resultLabel
        );
        
        // Set up scene
        Scene scene = new Scene(layout, 400, 500);
        stage.setScene(scene);
        stage.setTitle("Compound Interest Calculator - Test");
        
        // Set up button click handler
        calculateButton.setOnAction(event -> {
            // Clear error labels
            principalErrorLabel.setVisible(false);
            durationErrorLabel.setVisible(false);
            
            // Get input values
            String principal = principalField.getText();
            String duration = durationField.getText();
            
            // Validate inputs using the controller
            ValidationResult validationResult = controller.validateInputs(principal, duration);
            
            if (validationResult.isValid()) {
                try {
                    // Calculate EMI using the controller
                    CalculationResult calculationResult = controller.calculateEMI(principal, duration);
                    
                    // Display result
                    resultLabel.setText(controller.formatResult(calculationResult));
                } catch (Exception e) {
                    // Display calculation error
                    resultLabel.setText("Error: " + e.getMessage());
                }
            } else {
                // Display validation error
                String errorMessage = validationResult.getErrorMessage();
                if (errorMessage.toLowerCase().contains("principal")) {
                    principalErrorLabel.setText(errorMessage);
                    principalErrorLabel.setVisible(true);
                } else {
                    durationErrorLabel.setText(errorMessage);
                    durationErrorLabel.setVisible(true);
                }
            }
        });
        
        // Show the stage
        stage.show();
    }
    
    @Test
    @DisplayName("Should calculate EMI correctly for standard input")
    public void testStandardCalculation() {
        LOGGER.info("Executing standard calculation test");
        
        // Enter standard principal amount in the principal field
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        
        // Enter standard duration in the duration field
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Click the calculate button
        UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify that the controller's validateInputs method was called with correct parameters
        Mockito.verify(validationService).validateAllInputs(
            UITestFixture.STANDARD_PRINCIPAL,
            UITestFixture.STANDARD_DURATION
        );
        
        // Verify that the controller's calculateEMI method was called with correct parameters
        BigDecimal expectedPrincipal = CurrencyUtils.parseCurrencyValue(UITestFixture.STANDARD_PRINCIPAL);
        int expectedDuration = Integer.parseInt(UITestFixture.STANDARD_DURATION);
        Mockito.verify(calculationService).calculateEMI(
            Mockito.eq(expectedPrincipal),
            Mockito.eq(expectedDuration),
            Mockito.any(BigDecimal.class)
        );
        
        // Verify that the result displayed in the UI contains the expected standard result
        UITestUtils.verifyResultContains(robot, UITestFixture.STANDARD_RESULT);
        
        // Verify UI response times are within acceptable limits
        UITestUtils.assertUIResponseTime(100);
    }
    
    @Test
    @DisplayName("Should calculate EMI correctly for large principal amount")
    public void testLargePrincipalCalculation() {
        LOGGER.info("Executing large principal calculation test");
        
        // Enter large principal amount in the principal field
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.LARGE_PRINCIPAL);
        
        // Enter standard duration in the duration field
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Click the calculate button
        UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify that the controller's validateInputs method was called with correct parameters
        Mockito.verify(validationService).validateAllInputs(
            UITestFixture.LARGE_PRINCIPAL,
            UITestFixture.STANDARD_DURATION
        );
        
        // Verify that the controller's calculateEMI method was called with correct parameters
        BigDecimal expectedPrincipal = CurrencyUtils.parseCurrencyValue(UITestFixture.LARGE_PRINCIPAL);
        int expectedDuration = Integer.parseInt(UITestFixture.STANDARD_DURATION);
        Mockito.verify(calculationService).calculateEMI(
            Mockito.eq(expectedPrincipal),
            Mockito.eq(expectedDuration),
            Mockito.any(BigDecimal.class)
        );
        
        // Verify that the result displayed in the UI contains the expected large principal result
        UITestUtils.verifyResultContains(robot, UITestFixture.LARGE_PRINCIPAL_RESULT);
        
        // Verify UI response times are within acceptable limits
        UITestUtils.assertUIResponseTime(100);
    }
    
    @Test
    @DisplayName("Should display error message for invalid principal amount")
    public void testInvalidPrincipalValidation() {
        LOGGER.info("Executing invalid principal validation test");
        
        // Configure mock validation service to return invalid result for principal
        ValidationResult invalidPrincipalResult = ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_POSITIVE);
        Mockito.when(validationService.validateAllInputs(
            UITestFixture.NEGATIVE_PRINCIPAL,
            UITestFixture.STANDARD_DURATION
        )).thenReturn(invalidPrincipalResult);
        
        // Enter invalid principal amount in the principal field
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.NEGATIVE_PRINCIPAL);
        
        // Enter standard duration in the duration field
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Click the calculate button
        UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify that the controller's validateInputs method was called with correct parameters
        Mockito.verify(validationService).validateAllInputs(
            UITestFixture.NEGATIVE_PRINCIPAL,
            UITestFixture.STANDARD_DURATION
        );
        
        // Verify that the controller's calculateEMI method was not called
        Mockito.verify(calculationService, Mockito.never()).calculateEMI(
            Mockito.any(BigDecimal.class),
            Mockito.anyInt(),
            Mockito.any(BigDecimal.class)
        );
        
        // Verify that the appropriate error message is displayed in the UI
        UITestUtils.verifyErrorMessageDisplayed(robot, UITestUtils.PRINCIPAL_ERROR_LABEL_ID, ErrorMessages.PRINCIPAL_POSITIVE);
        
        // Verify UI response times are within acceptable limits
        UITestUtils.assertUIResponseTime(100);
    }
    
    @Test
    @DisplayName("Should display error message for invalid loan duration")
    public void testInvalidDurationValidation() {
        LOGGER.info("Executing invalid duration validation test");
        
        // Configure mock validation service to return invalid result for duration
        ValidationResult invalidDurationResult = ValidationResult.createInvalid(ErrorMessages.DURATION_POSITIVE);
        Mockito.when(validationService.validateAllInputs(
            UITestFixture.STANDARD_PRINCIPAL,
            UITestFixture.ZERO_DURATION
        )).thenReturn(invalidDurationResult);
        
        // Enter standard principal amount in the principal field
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        
        // Enter invalid duration in the duration field
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, UITestFixture.ZERO_DURATION);
        
        // Click the calculate button
        UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify that the controller's validateInputs method was called with correct parameters
        Mockito.verify(validationService).validateAllInputs(
            UITestFixture.STANDARD_PRINCIPAL,
            UITestFixture.ZERO_DURATION
        );
        
        // Verify that the controller's calculateEMI method was not called
        Mockito.verify(calculationService, Mockito.never()).calculateEMI(
            Mockito.any(BigDecimal.class),
            Mockito.anyInt(),
            Mockito.any(BigDecimal.class)
        );
        
        // Verify that the appropriate error message is displayed in the UI
        UITestUtils.verifyErrorMessageDisplayed(robot, UITestUtils.DURATION_ERROR_LABEL_ID, ErrorMessages.DURATION_POSITIVE);
        
        // Verify UI response times are within acceptable limits
        UITestUtils.assertUIResponseTime(100);
    }
    
    @Test
    @DisplayName("Should handle calculation errors gracefully")
    public void testCalculationErrorHandling() {
        LOGGER.info("Executing calculation error handling test");
        
        // Configure mock validation service to return valid result
        ValidationResult validResult = ValidationResult.createValid();
        Mockito.when(validationService.validateAllInputs(
            UITestFixture.STANDARD_PRINCIPAL,
            UITestFixture.STANDARD_DURATION
        )).thenReturn(validResult);
        
        // Configure mock calculation service to throw CalculationException
        BigDecimal principal = CurrencyUtils.parseCurrencyValue(UITestFixture.STANDARD_PRINCIPAL);
        int duration = Integer.parseInt(UITestFixture.STANDARD_DURATION);
        Mockito.when(calculationService.calculateEMI(
            Mockito.eq(principal),
            Mockito.eq(duration),
            Mockito.any(BigDecimal.class)
        )).thenThrow(new CalculationException(ErrorMessages.CALCULATION_ERROR));
        
        // Enter standard principal amount in the principal field
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        
        // Enter standard duration in the duration field
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Click the calculate button
        UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify that the controller's validateInputs method was called with correct parameters
        Mockito.verify(validationService).validateAllInputs(
            UITestFixture.STANDARD_PRINCIPAL,
            UITestFixture.STANDARD_DURATION
        );
        
        // Verify that the controller's calculateEMI method was called with correct parameters
        Mockito.verify(calculationService).calculateEMI(
            Mockito.eq(principal),
            Mockito.eq(duration),
            Mockito.any(BigDecimal.class)
        );
        
        // Verify that the appropriate error message is displayed in the UI
        UITestUtils.verifyResultContains(robot, "Error: " + ErrorMessages.CALCULATION_ERROR);
        
        // Verify UI response times are within acceptable limits
        UITestUtils.assertUIResponseTime(100);
    }
    
    @Test
    @DisplayName("Should handle multiple calculations correctly")
    public void testMultipleCalculations() {
        LOGGER.info("Executing multiple calculations test");
        
        // Perform first calculation with standard inputs
        String firstResult = UITestUtils.performCalculation(robot, UITestFixture.STANDARD_PRINCIPAL, UITestFixture.STANDARD_DURATION);
        
        // Verify first calculation result
        Assertions.assertTrue(firstResult.contains(UITestFixture.STANDARD_RESULT), 
                "First calculation result should contain " + UITestFixture.STANDARD_RESULT);
        
        // Perform second calculation with different inputs
        String secondResult = UITestUtils.performCalculation(robot, UITestFixture.LARGE_PRINCIPAL, UITestFixture.STANDARD_DURATION);
        
        // Verify second calculation result
        Assertions.assertTrue(secondResult.contains(UITestFixture.LARGE_PRINCIPAL_RESULT), 
                "Second calculation result should contain " + UITestFixture.LARGE_PRINCIPAL_RESULT);
        
        // Verify that controller methods were called correct number of times
        Mockito.verify(validationService, Mockito.times(2)).validateAllInputs(Mockito.anyString(), Mockito.anyString());
        
        // Verify UI response times are within acceptable limits
        UITestUtils.assertUIResponseTime(100);
    }
    
    @Test
    @DisplayName("Should execute all standard UI test cases successfully")
    public void testUITestCasesExecution() {
        LOGGER.info("Executing standard UI test cases");
        
        // Get standard UI test cases from UITestFixture
        List<UITestFixture.UITestCase> testCases = UITestFixture.createStandardUITestCases();
        
        // Execute each test case using UITestFixture.executeUITestCase
        for (UITestFixture.UITestCase testCase : testCases) {
            boolean passed = UITestFixture.executeUITestCase(robot, testCase);
            Assertions.assertTrue(passed, "Test case " + testCase.getId() + " should pass");
        }
        
        // Verify UI response times are within acceptable limits
        UITestUtils.assertUIResponseTime(100);
    }
    
    /**
     * Configures the mock services with standard behavior for testing.
     */
    private void configureMockServices() {
        // Configure validation service
        ValidationResult validResult = ValidationResult.createValid();
        Mockito.when(validationService.validateAllInputs(
            UITestFixture.STANDARD_PRINCIPAL,
            UITestFixture.STANDARD_DURATION
        )).thenReturn(validResult);
        
        Mockito.when(validationService.validateAllInputs(
            UITestFixture.LARGE_PRINCIPAL,
            UITestFixture.STANDARD_DURATION
        )).thenReturn(validResult);
        
        // Configure calculation service
        BigDecimal standardPrincipal = CurrencyUtils.parseCurrencyValue(UITestFixture.STANDARD_PRINCIPAL);
        int standardDuration = Integer.parseInt(UITestFixture.STANDARD_DURATION);
        
        CalculationResult standardResult = Mockito.mock(CalculationResult.class);
        Mockito.when(standardResult.getEmiAmount()).thenReturn(new BigDecimal("483.65"));
        Mockito.when(standardResult.getFormattedEmiAmount()).thenReturn(UITestFixture.STANDARD_RESULT);
        
        Mockito.when(calculationService.calculateEMI(
            Mockito.eq(standardPrincipal),
            Mockito.eq(standardDuration),
            Mockito.any(BigDecimal.class)
        )).thenReturn(standardResult);
        
        BigDecimal largePrincipal = CurrencyUtils.parseCurrencyValue(UITestFixture.LARGE_PRINCIPAL);
        
        CalculationResult largeResult = Mockito.mock(CalculationResult.class);
        Mockito.when(largeResult.getEmiAmount()).thenReturn(new BigDecimal("20037.80"));
        Mockito.when(largeResult.getFormattedEmiAmount()).thenReturn(UITestFixture.LARGE_PRINCIPAL_RESULT);
        
        Mockito.when(calculationService.calculateEMI(
            Mockito.eq(largePrincipal),
            Mockito.eq(standardDuration),
            Mockito.any(BigDecimal.class)
        )).thenReturn(largeResult);
    }
    
    /**
     * Verifies that the controller was called with the expected parameters.
     */
    private void verifyControllerInteractions(String principal, String duration) {
        // Verify that controller.validateInputs was called with the correct principal and duration
        Mockito.verify(validationService).validateAllInputs(principal, duration);
        
        // Verify that controller.calculateEMI was called with the correct principal and duration
        ValidationResult validationResult = validationService.validateAllInputs(principal, duration);
        if (validationResult.isValid()) {
            BigDecimal principalAmount = CurrencyUtils.parseCurrencyValue(principal);
            int durationYears = Integer.parseInt(duration);
            
            Mockito.verify(calculationService).calculateEMI(
                Mockito.eq(principalAmount),
                Mockito.eq(durationYears),
                Mockito.any(BigDecimal.class)
            );
        }
    }
}