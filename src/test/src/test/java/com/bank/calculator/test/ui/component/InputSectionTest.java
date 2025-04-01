package com.bank.calculator.test.ui.component;

import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.service.ValidationService;
import com.bank.calculator.ui.component.InputSection;
import com.bank.calculator.constant.ErrorMessages;
import com.bank.calculator.test.category.UITest;
import com.bank.calculator.test.util.UITestUtils;
import com.bank.calculator.test.fixture.UITestFixture;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.api.FxRobot;
import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javafx.scene.layout.VBox;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Test class for the InputSection component of the Compound Interest Calculator application.
 * This class tests the functionality of the input section UI component, including input field validation,
 * error message display, help functionality, and input clearing.
 */
@ExtendWith(ApplicationExtension.class)
@ExtendWith(MockitoExtension.class)
@UITest
public class InputSectionTest {

    private static final Logger LOGGER = Logger.getLogger(InputSectionTest.class.getName());
    
    @Mock
    private ValidationService validationService;
    
    private InputSection inputSection;
    
    private Stage stage;
    private Scene scene;
    
    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        LOGGER.log(Level.INFO, "Setting up test environment for InputSectionTest");
        
        // Initialize mock ValidationService
        Mockito.reset(validationService);
        
        // Create InputSection with mock ValidationService
        Platform.runLater(() -> {
            inputSection = new InputSection(validationService);
            scene = new Scene(inputSection, 800, 600);
            stage = new Stage();
            stage.setScene(scene);
            stage.show();
        });
        
        // Wait for JavaFX events to complete
        UITestUtils.waitForFxEvents();
        
        LOGGER.log(Level.INFO, "Test environment setup completed");
    }
    
    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    public void tearDown() {
        LOGGER.log(Level.INFO, "Tearing down test environment for InputSectionTest");
        
        // Close the Stage
        Platform.runLater(() -> {
            if (stage != null) {
                stage.close();
            }
        });
        
        // Wait for JavaFX events to complete
        UITestUtils.waitForFxEvents();
        
        // Reset all mocks
        Mockito.reset(validationService);
        
        LOGGER.log(Level.INFO, "Test environment teardown completed");
    }
    
    /**
     * Tests that the InputSection initializes correctly.
     */
    @Test
    @DisplayName("Should initialize InputSection correctly")
    public void testInitialization() {
        LOGGER.log(Level.INFO, "Testing InputSection initialization");
        
        // Verify that InputSection is not null
        Assertions.assertNotNull(inputSection, "InputSection should not be null");
        
        // Verify that principal field is empty
        Assertions.assertEquals("", inputSection.getPrincipalAmount(), "Principal field should be empty initially");
        
        // Verify that duration field is empty
        Assertions.assertEquals("", inputSection.getDuration(), "Duration field should be empty initially");
        
        // Verify that inputs are not valid initially
        Assertions.assertFalse(inputSection.areInputsValid(), "Inputs should not be valid initially");
        
        LOGGER.log(Level.INFO, "InputSection initialization test completed");
    }
    
    /**
     * Tests validation of the principal amount field.
     */
    @Test
    @DisplayName("Should validate principal amount field correctly")
    public void testPrincipalFieldValidation(FxRobot robot) {
        LOGGER.log(Level.INFO, "Testing principal field validation");
        
        // Set up mock ValidationService to return valid result for standard principal
        Mockito.when(validationService.validatePrincipal(UITestFixture.STANDARD_PRINCIPAL))
               .thenReturn(ValidationResult.createValid());
        
        // Set up mock ValidationService to return invalid result for negative principal
        Mockito.when(validationService.validatePrincipal(UITestFixture.NEGATIVE_PRINCIPAL))
               .thenReturn(ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_POSITIVE));
        
        // Enter standard principal amount in the field
        long responseTime = UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        
        // Verify no error message is displayed
        UITestUtils.verifyNoErrorMessageDisplayed(robot, UITestUtils.PRINCIPAL_ERROR_LABEL_ID);
        
        // Enter negative principal amount in the field
        responseTime = UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.NEGATIVE_PRINCIPAL);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(robot, UITestUtils.PRINCIPAL_ERROR_LABEL_ID, ErrorMessages.PRINCIPAL_POSITIVE);
        
        // Verify UI response time is within acceptable limits
        UITestUtils.assertUIResponseTime(responseTime);
        
        LOGGER.log(Level.INFO, "Principal field validation test completed");
    }
    
    /**
     * Tests validation of the loan duration field.
     */
    @Test
    @DisplayName("Should validate loan duration field correctly")
    public void testDurationFieldValidation(FxRobot robot) {
        LOGGER.log(Level.INFO, "Testing duration field validation");
        
        // Set up mock ValidationService to return valid result for standard duration
        Mockito.when(validationService.validateDuration(UITestFixture.STANDARD_DURATION))
               .thenReturn(ValidationResult.createValid());
        
        // Set up mock ValidationService to return invalid result for negative duration
        Mockito.when(validationService.validateDuration(UITestFixture.NEGATIVE_DURATION))
               .thenReturn(ValidationResult.createInvalid(ErrorMessages.DURATION_POSITIVE));
        
        // Enter standard duration in the field
        long responseTime = UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Verify no error message is displayed
        UITestUtils.verifyNoErrorMessageDisplayed(robot, UITestUtils.DURATION_ERROR_LABEL_ID);
        
        // Enter negative duration in the field
        responseTime = UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, UITestFixture.NEGATIVE_DURATION);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(robot, UITestUtils.DURATION_ERROR_LABEL_ID, ErrorMessages.DURATION_POSITIVE);
        
        // Verify UI response time is within acceptable limits
        UITestUtils.assertUIResponseTime(responseTime);
        
        LOGGER.log(Level.INFO, "Duration field validation test completed");
    }
    
    /**
     * Tests the clearInputs method.
     */
    @Test
    @DisplayName("Should clear all inputs correctly")
    public void testClearInputs(FxRobot robot) {
        LOGGER.log(Level.INFO, "Testing clearInputs method");
        
        // Enter standard principal amount in the field
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        
        // Enter standard duration in the field
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Call clearInputs method
        Platform.runLater(() -> inputSection.clearInputs());
        UITestUtils.waitForFxEvents();
        
        // Verify principal field is empty
        Assertions.assertEquals("", inputSection.getPrincipalAmount(), "Principal field should be empty after clearing");
        
        // Verify duration field is empty
        Assertions.assertEquals("", inputSection.getDuration(), "Duration field should be empty after clearing");
        
        // Verify inputs are not valid after clearing
        Assertions.assertFalse(inputSection.areInputsValid(), "Inputs should not be valid after clearing");
        
        LOGGER.log(Level.INFO, "ClearInputs test completed");
    }
    
    /**
     * Tests the validateInputs method.
     */
    @Test
    @DisplayName("Should validate all inputs correctly")
    public void testValidateInputs(FxRobot robot) {
        LOGGER.log(Level.INFO, "Testing validateInputs method");
        
        // Enter standard principal amount in the field
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        
        // Enter standard duration in the field
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Set up mock ValidationService to return valid result for all inputs
        Mockito.when(validationService.validatePrincipal(UITestFixture.STANDARD_PRINCIPAL))
               .thenReturn(ValidationResult.createValid());
        Mockito.when(validationService.validateDuration(UITestFixture.STANDARD_DURATION))
               .thenReturn(ValidationResult.createValid());
        
        // Call validateInputs method and get result
        final boolean[] result = new boolean[1];
        Platform.runLater(() -> result[0] = inputSection.validateInputs());
        UITestUtils.waitForFxEvents();
        
        // Verify result is true
        Assertions.assertTrue(result[0], "validateInputs should return true for valid inputs");
        
        // Verify areInputsValid returns true
        Assertions.assertTrue(inputSection.areInputsValid(), "areInputsValid should return true after validation");
        
        // Set up mock ValidationService to return invalid result for all inputs
        Mockito.when(validationService.validatePrincipal(UITestFixture.STANDARD_PRINCIPAL))
               .thenReturn(ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_POSITIVE));
        
        // Call validateInputs method and get result
        Platform.runLater(() -> result[0] = inputSection.validateInputs());
        UITestUtils.waitForFxEvents();
        
        // Verify result is false
        Assertions.assertFalse(result[0], "validateInputs should return false for invalid inputs");
        
        // Verify areInputsValid returns false
        Assertions.assertFalse(inputSection.areInputsValid(), "areInputsValid should return false after failed validation");
        
        LOGGER.log(Level.INFO, "ValidateInputs test completed");
    }
    
    /**
     * Tests validation of principal amount format.
     */
    @Test
    @DisplayName("Should validate principal amount format correctly")
    public void testPrincipalFormatValidation(FxRobot robot) {
        LOGGER.log(Level.INFO, "Testing principal format validation");
        
        // Set up mock ValidationService to return invalid result for invalid format principal
        Mockito.when(validationService.validatePrincipal(UITestFixture.INVALID_FORMAT_PRINCIPAL))
               .thenReturn(ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_FORMAT));
        
        // Enter invalid format principal in the field
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.INVALID_FORMAT_PRINCIPAL);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(robot, UITestUtils.PRINCIPAL_ERROR_LABEL_ID, ErrorMessages.PRINCIPAL_FORMAT);
        
        LOGGER.log(Level.INFO, "Principal format validation test completed");
    }
    
    /**
     * Tests validation of loan duration format.
     */
    @Test
    @DisplayName("Should validate loan duration format correctly")
    public void testDurationFormatValidation(FxRobot robot) {
        LOGGER.log(Level.INFO, "Testing duration format validation");
        
        // Set up mock ValidationService to return invalid result for decimal duration
        Mockito.when(validationService.validateDuration(UITestFixture.DECIMAL_DURATION))
               .thenReturn(ValidationResult.createInvalid(ErrorMessages.DURATION_FORMAT));
        
        // Enter decimal duration in the field
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, UITestFixture.DECIMAL_DURATION);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(robot, UITestUtils.DURATION_ERROR_LABEL_ID, ErrorMessages.DURATION_FORMAT);
        
        LOGGER.log(Level.INFO, "Duration format validation test completed");
    }
    
    /**
     * Tests validation of zero values.
     */
    @Test
    @DisplayName("Should validate zero values correctly")
    public void testZeroValueValidation(FxRobot robot) {
        LOGGER.log(Level.INFO, "Testing zero value validation");
        
        // Set up mock ValidationService to return invalid result for zero principal
        Mockito.when(validationService.validatePrincipal(UITestFixture.ZERO_PRINCIPAL))
               .thenReturn(ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_POSITIVE));
        
        // Enter zero principal in the field
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.ZERO_PRINCIPAL);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(robot, UITestUtils.PRINCIPAL_ERROR_LABEL_ID, ErrorMessages.PRINCIPAL_POSITIVE);
        
        // Set up mock ValidationService to return invalid result for zero duration
        Mockito.when(validationService.validateDuration(UITestFixture.ZERO_DURATION))
               .thenReturn(ValidationResult.createInvalid(ErrorMessages.DURATION_POSITIVE));
        
        // Enter zero duration in the field
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, UITestFixture.ZERO_DURATION);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(robot, UITestUtils.DURATION_ERROR_LABEL_ID, ErrorMessages.DURATION_POSITIVE);
        
        LOGGER.log(Level.INFO, "Zero value validation test completed");
    }
    
    /**
     * Tests validation of empty fields.
     */
    @Test
    @DisplayName("Should validate empty fields correctly")
    public void testEmptyFieldValidation(FxRobot robot) {
        LOGGER.log(Level.INFO, "Testing empty field validation");
        
        // Set up mock ValidationService to return invalid result for empty principal
        Mockito.when(validationService.validatePrincipal(""))
               .thenReturn(ValidationResult.createInvalid(ErrorMessages.PRINCIPAL_REQUIRED));
        
        // Clear principal field
        UITestUtils.enterTextInField(robot, UITestUtils.PRINCIPAL_FIELD_ID, "");
        
        // Call validateInputs method
        Platform.runLater(() -> inputSection.validateInputs());
        UITestUtils.waitForFxEvents();
        
        // Verify error message is displayed for principal field
        UITestUtils.verifyErrorMessageDisplayed(robot, UITestUtils.PRINCIPAL_ERROR_LABEL_ID, ErrorMessages.PRINCIPAL_REQUIRED);
        
        // Set up mock ValidationService to return invalid result for empty duration
        Mockito.when(validationService.validateDuration(""))
               .thenReturn(ValidationResult.createInvalid(ErrorMessages.DURATION_REQUIRED));
        
        // Clear duration field
        UITestUtils.enterTextInField(robot, UITestUtils.DURATION_FIELD_ID, "");
        
        // Call validateInputs method
        Platform.runLater(() -> inputSection.validateInputs());
        UITestUtils.waitForFxEvents();
        
        // Verify error message is displayed for duration field
        UITestUtils.verifyErrorMessageDisplayed(robot, UITestUtils.DURATION_ERROR_LABEL_ID, ErrorMessages.DURATION_REQUIRED);
        
        LOGGER.log(Level.INFO, "Empty field validation test completed");
    }
}