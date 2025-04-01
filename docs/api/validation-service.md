# ValidationService API Documentation

## Overview

The ValidationService component is responsible for validating user inputs in the Compound Interest Calculator application. It ensures that principal amounts and loan durations meet the application's requirements before they are used in calculations.

This API documentation provides comprehensive information about the ValidationService interface, its implementation, validation rules, and usage patterns.

## Interface Definition

`ValidationService` is an interface that defines methods for validating various inputs in the Compound Interest Calculator application:

```java
public interface ValidationService {
    ValidationResult validatePrincipal(String principalStr);
    ValidationResult validateDuration(String durationStr);
    ValidationResult validateAllInputs(String principalStr, String durationStr);
    ValidationResult validateCalculationInput(CalculationInput input);
}
```

The service uses the `ValidationResult` class to encapsulate validation outcomes:

```java
public class ValidationResult {
    private final boolean valid;
    private final String errorMessage;
    
    // Methods
    public boolean isValid();
    public String getErrorMessage();
    public static ValidationResult createValid();
    public static ValidationResult createInvalid(String errorMessage);
}
```

## Method Specifications

### validatePrincipal(String principalStr)

Validates that the principal amount is a positive number with up to 2 decimal places and within allowed range.

**Parameters:**
- `principalStr` - The principal amount as a string

**Returns:**
- `ValidationResult` - An object indicating whether the principal amount is valid

**Validation Rules:**
- Must not be null or empty
- Must be a numeric value with up to 2 decimal places
- Must be greater than zero
- Must be at least $1,000.00
- Must not exceed $1,000,000.00

**Error Messages:**
- `PRINCIPAL_REQUIRED` - Principal amount is required.
- `PRINCIPAL_FORMAT` - Principal amount must be a number with up to 2 decimal places.
- `PRINCIPAL_POSITIVE` - Principal amount must be a positive number.
- `PRINCIPAL_MIN_REQUIRED` - Principal amount must be at least $1,000.00.
- `PRINCIPAL_MAX_EXCEEDED` - Principal amount cannot exceed $1,000,000.00.

### validateDuration(String durationStr)

Validates that the loan duration is a positive integer within allowed range.

**Parameters:**
- `durationStr` - The loan duration as a string

**Returns:**
- `ValidationResult` - An object indicating whether the loan duration is valid

**Validation Rules:**
- Must not be null or empty
- Must be a positive integer
- Must be at least 1 year
- Must not exceed 30 years

**Error Messages:**
- `DURATION_REQUIRED` - Loan duration is required.
- `DURATION_FORMAT` - Loan duration must be a whole number.
- `DURATION_POSITIVE` - Loan duration must be a positive whole number.
- `DURATION_MIN_REQUIRED` - Loan duration must be at least 1 year.
- `DURATION_MAX_EXCEEDED` - Loan duration cannot exceed 30 years.

### validateAllInputs(String principalStr, String durationStr)

Validates all inputs required for calculation.

**Parameters:**
- `principalStr` - The principal amount as a string
- `durationStr` - The loan duration as a string

**Returns:**
- `ValidationResult` - An object indicating whether all inputs are valid

**Implementation:**
This method calls `validatePrincipal` and `validateDuration` sequentially. If any validation fails, it returns the first failure result. If all validations pass, it returns a valid result.

### validateCalculationInput(CalculationInput input)

Validates a CalculationInput object.

**Parameters:**
- `input` - The CalculationInput object to validate

**Returns:**
- `ValidationResult` - An object indicating whether the calculation input is valid

**Validation Rules:**
- Input must not be null
- Principal amount must be within allowed range
- Loan duration must be within allowed range

**Error Messages:**
- `INVALID_INPUT` - Invalid input. Please check your entries and try again.
- `PRINCIPAL_MIN_REQUIRED` - Principal amount must be at least $1,000.00.
- `PRINCIPAL_MAX_EXCEEDED` - Principal amount cannot exceed $1,000,000.00.
- `DURATION_MIN_REQUIRED` - Loan duration must be at least 1 year.
- `DURATION_MAX_EXCEEDED` - Loan duration cannot exceed 30 years.

## Validation Rules

The ValidationService enforces the following validation rules:

- Principal Amount:
  - Must not be null or empty
  - Must be a positive number
  - Must have up to 2 decimal places
  - Must be at least $1,000.00
  - Must not exceed $1,000,000.00

- Loan Duration:
  - Must not be null or empty
  - Must be a positive integer
  - Must be at least 1 year
  - Must not exceed 30 years

## Implementation Details

The primary implementation class `ValidationServiceImpl` utilizes `ValidationUtils` for its core validation logic:

```java
public class ValidationServiceImpl implements ValidationService {
    @Override
    public ValidationResult validatePrincipal(String principalStr) {
        return ValidationUtils.validatePrincipal(principalStr);
    }
    
    @Override
    public ValidationResult validateDuration(String durationStr) {
        return ValidationUtils.validateDuration(durationStr);
    }
    
    // Other methods...
}
```

The `ValidationUtils` class uses regular expressions to validate input formats:

- For principal amounts: `^\\d+(\\.\\d{1,2})?$`
- For loan durations: `^\\d+$`

## Usage Examples

### Basic Validation

```java
// Create ValidationService instance
ValidationService validationService = new ValidationServiceImpl();

// Validate principal amount
ValidationResult principalResult = validationService.validatePrincipal("25000.00");
if (!principalResult.isValid()) {
    // Handle validation error
    String errorMessage = principalResult.getErrorMessage();
    // Display error message to user
}

// Validate loan duration
ValidationResult durationResult = validationService.validateDuration("5");
if (!durationResult.isValid()) {
    // Handle validation error
    String errorMessage = durationResult.getErrorMessage();
    // Display error message to user
}
```

### Validating All Inputs at Once

```java
ValidationResult allInputsResult = validationService.validateAllInputs("25000.00", "5");
if (allInputsResult.isValid()) {
    // Proceed with calculation
} else {
    // Handle validation error
    String errorMessage = allInputsResult.getErrorMessage();
    // Display error message to user
}
```

### Validating a CalculationInput Object

```java
BigDecimal principal = new BigDecimal("25000.00");
int duration = 5;
CalculationInput input = new CalculationInput(principal, duration);

ValidationResult inputResult = validationService.validateCalculationInput(input);
if (inputResult.isValid()) {
    // Proceed with calculation using the input object
} else {
    // Handle validation error
    String errorMessage = inputResult.getErrorMessage();
    // Display error message to user
}
```

## Error Handling

### Using ValidationResult

The ValidationService uses the ValidationResult class to encapsulate validation results. When validation fails, a ValidationResult with valid=false and an appropriate error message is returned.

```java
ValidationResult result = validationService.validatePrincipal("invalid");
if (!result.isValid()) {
    String errorMessage = result.getErrorMessage();
    // Display error message to user
}
```

### Using ValidationException

For more severe validation errors, a ValidationException can be thrown:

```java
try {
    ValidationResult result = validationService.validatePrincipal("-1000");
    if (!result.isValid()) {
        throw new ValidationException(result);
    }
} catch (ValidationException e) {
    // Log the error
    logger.error("Validation error: {} (Error code: {})", 
                e.getErrorMessage(), e.getErrorCode());
    // Display error to user
    displayError(e.getErrorMessage());
}
```

## Integration with UI

The ValidationService is typically used in the Controller component of the MVC pattern to validate user inputs before passing them to the calculation service:

```java
public class Controller {
    private ValidationService validationService;
    private CalculationService calculationService;
    private UIComponent uiComponent;
    
    public void handleCalculateButtonClick() {
        String principalStr = uiComponent.getPrincipalField().getText();
        String durationStr = uiComponent.getDurationField().getText();
        
        ValidationResult result = validationService.validateAllInputs(principalStr, durationStr);
        if (result.isValid()) {
            // Convert inputs to appropriate types
            BigDecimal principal = new BigDecimal(principalStr);
            int duration = Integer.parseInt(durationStr);
            
            // Perform calculation
            BigDecimal emiAmount = calculationService.calculateEMI(principal, duration);
            
            // Display result
            uiComponent.displayResult(formatCurrency(emiAmount));
        } else {
            // Display validation error
            uiComponent.displayError(result.getErrorMessage());
        }
    }
}
```

In a JavaFX implementation, the validation can be performed in real-time as the user types, providing immediate feedback:

```java
principalField.textProperty().addListener((observable, oldValue, newValue) -> {
    ValidationResult result = validationService.validatePrincipal(newValue);
    if (result.isValid()) {
        principalField.setStyle("-fx-border-color: green;");
        errorLabel.setText("");
    } else {
        principalField.setStyle("-fx-border-color: red;");
        errorLabel.setText(result.getErrorMessage());
    }
    updateCalculateButtonState();
});
```

## Thread Safety

The ValidationService implementation is stateless and thread-safe. Multiple threads can safely use the same ValidationService instance without external synchronization.

## Performance Considerations

The validation methods are designed to be lightweight and fast, with O(1) time complexity for most operations. Regular expression validation is used efficiently to ensure quick response times for the UI.

## Best Practices

1. Always validate user inputs before performing calculations
2. Provide immediate feedback to users when validation fails
3. Use the ValidationResult object to access both validation status and error messages
4. For bulk validation, use validateAllInputs to get the first error encountered
5. Consider using ValidationException for programmatic validation errors

## Conclusion

The ValidationService component provides a robust mechanism for validating user inputs in the Compound Interest Calculator application. By using this service consistently throughout the application, you can ensure that invalid inputs are caught early and users receive appropriate feedback, leading to a better user experience and more reliable calculations.