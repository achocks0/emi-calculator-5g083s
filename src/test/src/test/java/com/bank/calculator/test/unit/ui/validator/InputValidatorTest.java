package com.bank.calculator.test.unit.ui.validator;

import com.bank.calculator.ui.validator.InputValidator;
import com.bank.calculator.service.ValidationService;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.constant.ErrorMessages;
import com.bank.calculator.test.category.UnitTest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;

import org.mockito.Mockito;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.ArgumentCaptor;

import javafx.scene.control.TextField;
import javafx.scene.control.Label;
import javafx.collections.ObservableList;
import javafx.collections.FXCollections;
import javafx.beans.property.StringProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 * Unit test class for the InputValidator component that validates user inputs 
 * in the Compound Interest Calculator application.
 */
@UnitTest
@DisplayName("Input Validator Tests")
public class InputValidatorTest {

    private static final String CSS_ERROR_CLASS = "error-field";

    private InputValidator inputValidator;
    
    private ValidationService validationService;
    private TextField principalField;
    private TextField durationField;
    private Label errorLabel;
    
    private ObservableList<String> principalStyleClasses;
    private ObservableList<String> durationStyleClasses;

    @BeforeEach
    public void setUp() {
        // Initialize mocks
        validationService = Mockito.mock(ValidationService.class);
        principalField = Mockito.mock(TextField.class);
        durationField = Mockito.mock(TextField.class);
        errorLabel = Mockito.mock(Label.class);
        
        // Setup observable lists for style classes
        principalStyleClasses = FXCollections.observableArrayList();
        durationStyleClasses = FXCollections.observableArrayList();
        
        // Mock behavior for getStyleClass() methods
        Mockito.when(principalField.getStyleClass()).thenReturn(principalStyleClasses);
        Mockito.when(durationField.getStyleClass()).thenReturn(durationStyleClasses);
        
        // Create the validator
        inputValidator = new InputValidator(validationService);
    }

    @AfterEach
    public void tearDown() {
        Mockito.reset(validationService, principalField, durationField, errorLabel);
        principalStyleClasses = null;
        durationStyleClasses = null;
    }

    @Test
    @DisplayName("Should remove error styling when principal input is valid")
    public void testSetupPrincipalValidation_WithValidInput_RemovesErrorStyle() {
        // Setup test data and initial state
        String validPrincipal = "5000";
        principalStyleClasses.add(CSS_ERROR_CLASS);
        
        // Create a real StringProperty to capture the listener
        StringProperty textProperty = new SimpleStringProperty();
        Mockito.when(principalField.textProperty()).thenReturn(textProperty);
        
        // Setup validation response
        Mockito.when(validationService.validatePrincipal(validPrincipal))
               .thenReturn(ValidationResult.createValid());
        
        // Call method to set up the validation
        inputValidator.setupPrincipalValidation(principalField, errorLabel);
        
        // Trigger the change event
        textProperty.set(validPrincipal);
        
        // Verify validation was called
        Mockito.verify(validationService).validatePrincipal(validPrincipal);
        
        // Verify styles and error messages
        Assertions.assertFalse(principalStyleClasses.contains(CSS_ERROR_CLASS));
        Mockito.verify(errorLabel).setText("");
    }

    @Test
    @DisplayName("Should apply error styling when principal input is invalid")
    public void testSetupPrincipalValidation_WithInvalidInput_AppliesErrorStyle() {
        // Setup test data
        String invalidPrincipal = "-5000";
        String errorMessage = ErrorMessages.PRINCIPAL_POSITIVE;
        
        // Create a real StringProperty to capture the listener
        StringProperty textProperty = new SimpleStringProperty();
        Mockito.when(principalField.textProperty()).thenReturn(textProperty);
        
        // Setup validation response
        Mockito.when(validationService.validatePrincipal(invalidPrincipal))
               .thenReturn(ValidationResult.createInvalid(errorMessage));
        
        // Call method to set up the validation
        inputValidator.setupPrincipalValidation(principalField, errorLabel);
        
        // Trigger the change event
        textProperty.set(invalidPrincipal);
        
        // Verify validation was called
        Mockito.verify(validationService).validatePrincipal(invalidPrincipal);
        
        // Verify styles and error messages
        Assertions.assertTrue(principalStyleClasses.contains(CSS_ERROR_CLASS));
        Mockito.verify(errorLabel).setText(errorMessage);
    }

    @Test
    @DisplayName("Should remove error styling when duration input is valid")
    public void testSetupDurationValidation_WithValidInput_RemovesErrorStyle() {
        // Setup test data and initial state
        String validDuration = "5";
        durationStyleClasses.add(CSS_ERROR_CLASS);
        
        // Create a real StringProperty to capture the listener
        StringProperty textProperty = new SimpleStringProperty();
        Mockito.when(durationField.textProperty()).thenReturn(textProperty);
        
        // Setup validation response
        Mockito.when(validationService.validateDuration(validDuration))
               .thenReturn(ValidationResult.createValid());
        
        // Call method to set up the validation
        inputValidator.setupDurationValidation(durationField, errorLabel);
        
        // Trigger the change event
        textProperty.set(validDuration);
        
        // Verify validation was called
        Mockito.verify(validationService).validateDuration(validDuration);
        
        // Verify styles and error messages
        Assertions.assertFalse(durationStyleClasses.contains(CSS_ERROR_CLASS));
        Mockito.verify(errorLabel).setText("");
    }

    @Test
    @DisplayName("Should apply error styling when duration input is invalid")
    public void testSetupDurationValidation_WithInvalidInput_AppliesErrorStyle() {
        // Setup test data
        String invalidDuration = "-5";
        String errorMessage = ErrorMessages.DURATION_POSITIVE;
        
        // Create a real StringProperty to capture the listener
        StringProperty textProperty = new SimpleStringProperty();
        Mockito.when(durationField.textProperty()).thenReturn(textProperty);
        
        // Setup validation response
        Mockito.when(validationService.validateDuration(invalidDuration))
               .thenReturn(ValidationResult.createInvalid(errorMessage));
        
        // Call method to set up the validation
        inputValidator.setupDurationValidation(durationField, errorLabel);
        
        // Trigger the change event
        textProperty.set(invalidDuration);
        
        // Verify validation was called
        Mockito.verify(validationService).validateDuration(invalidDuration);
        
        // Verify styles and error messages
        Assertions.assertTrue(durationStyleClasses.contains(CSS_ERROR_CLASS));
        Mockito.verify(errorLabel).setText(errorMessage);
    }

    @Test
    @DisplayName("Should return true when principal input is valid")
    public void testValidatePrincipalField_WithValidInput_ReturnsTrue() {
        // Setup test data
        String validPrincipal = "5000";
        
        // Setup initial state with error styling
        principalStyleClasses.add(CSS_ERROR_CLASS);
        
        // Mock TextField getText() behavior
        Mockito.when(principalField.getText()).thenReturn(validPrincipal);
        
        // Mock ValidationService behavior
        Mockito.when(validationService.validatePrincipal(validPrincipal))
               .thenReturn(ValidationResult.createValid());
        
        // Call method under test
        boolean result = inputValidator.validatePrincipalField(principalField, errorLabel);
        
        // Verify result
        Assertions.assertTrue(result);
        
        // Verify error style was removed
        Assertions.assertFalse(principalStyleClasses.contains(CSS_ERROR_CLASS));
        
        // Verify error label was cleared
        Mockito.verify(errorLabel).setText("");
    }

    @Test
    @DisplayName("Should return false when principal input is invalid")
    public void testValidatePrincipalField_WithInvalidInput_ReturnsFalse() {
        // Setup test data
        String invalidPrincipal = "-5000";
        String errorMessage = ErrorMessages.PRINCIPAL_POSITIVE;
        
        // Mock TextField getText() behavior
        Mockito.when(principalField.getText()).thenReturn(invalidPrincipal);
        
        // Mock ValidationService behavior
        Mockito.when(validationService.validatePrincipal(invalidPrincipal))
               .thenReturn(ValidationResult.createInvalid(errorMessage));
        
        // Call method under test
        boolean result = inputValidator.validatePrincipalField(principalField, errorLabel);
        
        // Verify result
        Assertions.assertFalse(result);
        
        // Verify error style was applied
        Assertions.assertTrue(principalStyleClasses.contains(CSS_ERROR_CLASS));
        
        // Verify error message was set
        Mockito.verify(errorLabel).setText(errorMessage);
    }

    @Test
    @DisplayName("Should return true when duration input is valid")
    public void testValidateDurationField_WithValidInput_ReturnsTrue() {
        // Setup test data
        String validDuration = "5";
        
        // Setup initial state with error styling
        durationStyleClasses.add(CSS_ERROR_CLASS);
        
        // Mock TextField getText() behavior
        Mockito.when(durationField.getText()).thenReturn(validDuration);
        
        // Mock ValidationService behavior
        Mockito.when(validationService.validateDuration(validDuration))
               .thenReturn(ValidationResult.createValid());
        
        // Call method under test
        boolean result = inputValidator.validateDurationField(durationField, errorLabel);
        
        // Verify result
        Assertions.assertTrue(result);
        
        // Verify error style was removed
        Assertions.assertFalse(durationStyleClasses.contains(CSS_ERROR_CLASS));
        
        // Verify error label was cleared
        Mockito.verify(errorLabel).setText("");
    }

    @Test
    @DisplayName("Should return false when duration input is invalid")
    public void testValidateDurationField_WithInvalidInput_ReturnsFalse() {
        // Setup test data
        String invalidDuration = "-5";
        String errorMessage = ErrorMessages.DURATION_POSITIVE;
        
        // Mock TextField getText() behavior
        Mockito.when(durationField.getText()).thenReturn(invalidDuration);
        
        // Mock ValidationService behavior
        Mockito.when(validationService.validateDuration(invalidDuration))
               .thenReturn(ValidationResult.createInvalid(errorMessage));
        
        // Call method under test
        boolean result = inputValidator.validateDurationField(durationField, errorLabel);
        
        // Verify result
        Assertions.assertFalse(result);
        
        // Verify error style was applied
        Assertions.assertTrue(durationStyleClasses.contains(CSS_ERROR_CLASS));
        
        // Verify error message was set
        Mockito.verify(errorLabel).setText(errorMessage);
    }

    @Test
    @DisplayName("Should remove all error styling when clearing validation errors")
    public void testClearValidationErrors_RemovesAllErrorStyles() {
        // Setup initial state with error styling
        principalStyleClasses.add(CSS_ERROR_CLASS);
        durationStyleClasses.add(CSS_ERROR_CLASS);
        
        // Call method under test
        inputValidator.clearValidationErrors(principalField, durationField, errorLabel);
        
        // Verify error styles were removed
        Assertions.assertFalse(principalStyleClasses.contains(CSS_ERROR_CLASS));
        Assertions.assertFalse(durationStyleClasses.contains(CSS_ERROR_CLASS));
        
        // Verify error label was cleared
        Mockito.verify(errorLabel).setText("");
    }

    @Test
    @DisplayName("Should throw NullPointerException when parameters are null")
    public void testValidatePrincipalField_WithNullParameters_ThrowsException() {
        // Verify NullPointerException is thrown for null parameters
        Assertions.assertThrows(NullPointerException.class, () -> {
            inputValidator.validatePrincipalField(null, errorLabel);
        });
        
        Assertions.assertThrows(NullPointerException.class, () -> {
            inputValidator.validatePrincipalField(principalField, null);
        });
    }

    @Test
    @DisplayName("Should throw NullPointerException when parameters are null")
    public void testValidateDurationField_WithNullParameters_ThrowsException() {
        // Verify NullPointerException is thrown for null parameters
        Assertions.assertThrows(NullPointerException.class, () -> {
            inputValidator.validateDurationField(null, errorLabel);
        });
        
        Assertions.assertThrows(NullPointerException.class, () -> {
            inputValidator.validateDurationField(durationField, null);
        });
    }
}