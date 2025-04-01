# Calculation Service API Documentation

## Table of Contents
- [Service Overview](#service-overview)
- [Method Reference](#method-reference)
  - [Compound Interest Calculation](#compound-interest-calculation)
  - [EMI Calculation](#emi-calculation)
- [Input Parameters](#input-parameters)
- [Result Structure](#result-structure)
- [Calculation Formulas](#calculation-formulas)
- [Usage Examples](#usage-examples)
- [Error Handling](#error-handling)
- [Performance Considerations](#performance-considerations)

## Service Overview

The Calculation Service component provides core financial calculation functionality for the Compound Interest Calculator application. It implements precise calculation of compound interest and Equated Monthly Installments (EMI) based on principal amount, loan duration, and interest rate.

The service provides high precision financial calculations using the BigDecimal class to avoid floating-point errors, which is critical for monetary calculations. All calculations adhere to standard banking formulas for compound interest and EMI calculations.

**Key Features:**
- Calculation of compound interest with configurable compounding frequency
- Calculation of monthly EMI amounts based on loan parameters
- High precision financial calculations
- Comprehensive validation of input parameters
- Detailed calculation results including principal, interest, and total amounts

## Method Reference

The `CalculationService` interface defines the contract for compound interest and EMI calculations. All methods in this interface are designed to ensure calculation accuracy while maintaining good performance characteristics.

### Compound Interest Calculation

#### `BigDecimal calculateCompoundInterest(CalculationInput input)`

Calculates the compound interest based on the parameters in the `CalculationInput` object.

**Parameters:**
- `input` - A `CalculationInput` object containing principal amount, loan duration, and interest rate

**Returns:**
- A `BigDecimal` value representing the final amount after applying compound interest

**Exceptions:**
- `CalculationException` - If there is an error during calculation
- `NullPointerException` - If input is null

#### `BigDecimal calculateCompoundInterest(BigDecimal principal, int durationYears, BigDecimal interestRate)`

Calculates the compound interest based on individual parameters.

**Parameters:**
- `principal` - The principal amount for the loan (in USD)
- `durationYears` - The loan duration in years
- `interestRate` - The annual interest rate (as a percentage, e.g., 7.5 for 7.5%)

**Returns:**
- A `BigDecimal` value representing the final amount after applying compound interest

**Exceptions:**
- `CalculationException` - If there is an error during calculation
- `NullPointerException` - If principal or interestRate is null
- `IllegalArgumentException` - If durationYears is less than or equal to zero

### EMI Calculation

#### `CalculationResult calculateEMI(CalculationInput input)`

Calculates the monthly EMI based on the parameters in the `CalculationInput` object.

**Parameters:**
- `input` - A `CalculationInput` object containing principal amount, loan duration, and interest rate

**Returns:**
- A `CalculationResult` object containing the EMI amount and additional calculation details

**Exceptions:**
- `CalculationException` - If there is an error during calculation
- `NullPointerException` - If input is null

#### `CalculationResult calculateEMI(BigDecimal principal, int durationYears, BigDecimal interestRate)`

Calculates the monthly EMI based on individual parameters.

**Parameters:**
- `principal` - The principal amount for the loan (in USD)
- `durationYears` - The loan duration in years
- `interestRate` - The annual interest rate (as a percentage, e.g., 7.5 for 7.5%)

**Returns:**
- A `CalculationResult` object containing the EMI amount and additional calculation details

**Exceptions:**
- `CalculationException` - If there is an error during calculation
- `NullPointerException` - If principal or interestRate is null
- `IllegalArgumentException` - If durationYears is less than or equal to zero

## Input Parameters

The `CalculationInput` class encapsulates the input parameters required for financial calculations:

```java
public class CalculationInput {
    private BigDecimal principal;
    private int durationYears;
    private BigDecimal interestRate;
    
    // Constructors and methods...
}
```

### Properties

| Property | Type | Description | Constraints |
|----------|------|-------------|------------|
| `principal` | BigDecimal | The principal amount for the loan | Must be positive, typically between $1,000 and $1,000,000 |
| `durationYears` | int | The loan duration in years | Must be positive, typically between 1 and 30 years |
| `interestRate` | BigDecimal | The annual interest rate as a percentage | Defaults to 7.5% (can be modified) |

### Creating Input Parameters

```java
// Create with default interest rate (7.5%)
CalculationInput input = new CalculationInput(
    new BigDecimal("25000.00"),  // Principal amount
    5                            // Duration in years
);

// Create and set custom interest rate
CalculationInput input = new CalculationInput(
    new BigDecimal("25000.00"),  // Principal amount
    5                            // Duration in years
);
input.setInterestRate(new BigDecimal("8.25")); // Custom interest rate
```

### Validation

The `CalculationInput` class performs validation when creating instances:
- Throws `NullPointerException` if principal is null
- Throws `IllegalArgumentException` if durationYears is zero or negative
- Throws `NullPointerException` if setting a null interest rate

## Result Structure

The `CalculationResult` class encapsulates the results of EMI calculations:

```java
public class CalculationResult {
    private final BigDecimal emiAmount;
    private final BigDecimal totalAmount;
    private final BigDecimal interestAmount;
    private final BigDecimal annualInterestRate;
    private final int numberOfInstallments;
    
    // Constructors and methods...
}
```

### Properties

| Property | Type | Description |
|----------|------|-------------|
| `emiAmount` | BigDecimal | The monthly EMI amount |
| `totalAmount` | BigDecimal | The total amount payable over the loan duration |
| `interestAmount` | BigDecimal | The total interest amount payable |
| `annualInterestRate` | BigDecimal | The annual interest rate used for the calculation |
| `numberOfInstallments` | int | The number of monthly installments |

### Formatted Results

The `CalculationResult` class provides methods to access formatted results:

| Method | Return Type | Description |
|--------|-------------|-------------|
| `getFormattedEmiAmount()` | String | Returns the EMI amount formatted as USD currency (e.g., "$483.65") |
| `getFormattedTotalAmount()` | String | Returns the total amount formatted as USD currency |
| `getFormattedInterestAmount()` | String | Returns the interest amount formatted as USD currency |
| `getFormattedAnnualInterestRate()` | String | Returns the interest rate as a percentage string (e.g., "7.5%") |

### Accessing Results

```java
CalculationResult result = calculationService.calculateEMI(input);

// Access raw values (BigDecimal)
BigDecimal monthlyEMI = result.getEmiAmount();
BigDecimal totalRepayment = result.getTotalAmount();
BigDecimal interestPaid = result.getInterestAmount();
int numberOfPayments = result.getNumberOfInstallments();

// Access formatted values (String)
String formattedEMI = result.getFormattedEmiAmount();        // "$483.65"
String formattedTotal = result.getFormattedTotalAmount();    // "$29,019.00"
String formattedInterest = result.getFormattedInterestAmount(); // "$4,019.00"
String formattedRate = result.getFormattedAnnualInterestRate(); // "7.5%"
```

## Calculation Formulas

### Compound Interest Formula

The compound interest calculation uses the standard formula:

**A = P(1 + r/n)^(nt)**

Where:
- **A** = Final amount (principal + interest)
- **P** = Principal amount
- **r** = Annual interest rate (in decimal form, e.g., 0.075 for 7.5%)
- **n** = Number of times interest is compounded per year (default: 12 for monthly compounding)
- **t** = Time in years

#### Implementation Details

The implementation handles interest rates as percentages and converts them to decimal form internally:
- Interest rate of 7.5% is converted to 0.075
- For monthly compounding, the rate is further divided by 12

### EMI Formula

The EMI calculation uses the standard formula:

**EMI = [P × r × (1 + r)^n]/[(1 + r)^n - 1]**

Where:
- **EMI** = Equated Monthly Installment
- **P** = Principal loan amount
- **r** = Monthly interest rate (annual rate ÷ 12 and converted to decimal)
- **n** = Number of monthly installments (loan duration in years × 12)

#### Special Cases

- **Zero Interest Rate**: When the interest rate is zero, the formula simplifies to:
  ```
  EMI = Principal ÷ Number of Installments
  ```

#### Example Calculation

For a loan with:
- Principal: $25,000
- Duration: 5 years (60 months)
- Annual Interest Rate: 7.5%

**Step 1**: Convert annual rate to monthly decimal rate
- Monthly Rate = 7.5% ÷ 12 ÷ 100 = 0.00625

**Step 2**: Calculate EMI
- EMI = [25000 × 0.00625 × (1 + 0.00625)^60] ÷ [(1 + 0.00625)^60 - 1]
- EMI = [25000 × 0.00625 × 1.448676] ÷ [1.448676 - 1]
- EMI = [25000 × 0.00625 × 1.448676] ÷ 0.448676
- EMI = 226.355 ÷ 0.448676
- EMI = $483.65

## Usage Examples

### Example 1: Calculating EMI with Default Interest Rate

```java
import java.math.BigDecimal;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.service.CalculationService;

public class EMICalculationExample {
    
    private CalculationService calculationService;
    
    public void calculateAndDisplayEMI() {
        try {
            // Create input with principal: $25,000 and duration: 5 years
            CalculationInput input = new CalculationInput(
                new BigDecimal("25000.00"),
                5
            );
            
            // Calculate EMI
            CalculationResult result = calculationService.calculateEMI(input);
            
            // Display results
            System.out.println("Monthly EMI: " + result.getFormattedEmiAmount());
            System.out.println("Total Repayment: " + result.getFormattedTotalAmount());
            System.out.println("Total Interest: " + result.getFormattedInterestAmount());
            System.out.println("Number of Installments: " + result.getNumberOfInstallments());
        } catch (Exception e) {
            System.err.println("Error calculating EMI: " + e.getMessage());
        }
    }
}
```

### Example 2: Using Individual Parameters with Custom Interest Rate

```java
import java.math.BigDecimal;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.service.CalculationService;

public class CustomRateEMIExample {
    
    private CalculationService calculationService;
    
    public void calculateWithCustomRate() {
        try {
            BigDecimal principal = new BigDecimal("50000.00");
            int durationYears = 3;
            BigDecimal interestRate = new BigDecimal("6.5");
            
            CalculationResult result = calculationService.calculateEMI(
                principal, durationYears, interestRate
            );
            
            System.out.println("Monthly EMI: " + result.getFormattedEmiAmount());
        } catch (Exception e) {
            System.err.println("Error calculating EMI: " + e.getMessage());
        }
    }
}
```

### Example 3: Calculating Just the Compound Interest

```java
import java.math.BigDecimal;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.service.CalculationService;

public class CompoundInterestExample {
    
    private CalculationService calculationService;
    
    public void calculateCompoundInterest() {
        try {
            CalculationInput input = new CalculationInput(
                new BigDecimal("10000.00"),
                5
            );
            
            BigDecimal finalAmount = calculationService.calculateCompoundInterest(input);
            
            System.out.println("Principal Amount: $10,000.00");
            System.out.println("Final Amount after 5 years: $" + finalAmount);
            System.out.println("Total Interest Earned: $" + 
                finalAmount.subtract(input.getPrincipal()));
        } catch (Exception e) {
            System.err.println("Error calculating compound interest: " + e.getMessage());
        }
    }
}
```

## Error Handling

The Calculation Service employs a robust error handling strategy to ensure that calculation errors are properly identified and reported.

### Common Exceptions

| Exception Type | Conditions | Error Code |
|----------------|------------|------------|
| `NullPointerException` | When required parameters are null | - |
| `IllegalArgumentException` | When parameter values are invalid | - |
| `CalculationException` | When a calculation error occurs | E003 |

### CalculationException

The `CalculationException` class is a custom exception for calculation-specific errors:

```java
public class CalculationException extends RuntimeException {
    private String errorCode;
    private String errorMessage;
    
    // Constructors and methods...
}
```

The exception includes:
- An error code (e.g., "E003")
- A descriptive error message
- Optional cause (nested exception)

### Error Scenarios

| Scenario | Exception | Error Message |
|----------|-----------|---------------|
| Null principal amount | `NullPointerException` | "Principal amount cannot be null" |
| Negative duration | `IllegalArgumentException` | "Loan duration must be greater than zero" |
| Zero interest rate | `CalculationException` | "Interest rate cannot be zero for compound interest calculation" |
| Numeric overflow | `CalculationException` | "Numeric overflow occurred during calculation. Try smaller values." |
| Division by zero | `CalculationException` | "Cannot divide by zero during calculation." |

### Handling Errors

Best practices for handling errors when using the Calculation Service:

```java
try {
    CalculationResult result = calculationService.calculateEMI(input);
    // Process successful result...
} catch (NullPointerException e) {
    // Handle null input parameters
    System.err.println("Missing required input: " + e.getMessage());
} catch (IllegalArgumentException e) {
    // Handle invalid input values
    System.err.println("Invalid input value: " + e.getMessage());
} catch (CalculationException e) {
    // Handle calculation-specific errors
    System.err.println("Calculation error " + e.getErrorCode() + ": " + e.getErrorMessage());
} catch (Exception e) {
    // Handle unexpected errors
    System.err.println("Unexpected error: " + e.getMessage());
}
```

## Performance Considerations

The Calculation Service is optimized for performance while maintaining the accuracy required for financial calculations.

### BigDecimal Optimization

The service employs several optimizations for BigDecimal operations:

1. **Precision Management**: Uses appropriate precision for different calculation stages
   - `CALCULATION_PRECISION`: 10 decimal places for internal calculations
   - `CURRENCY_PRECISION`: 2 decimal places for final results

2. **Rounding Strategy**: Uses HALF_UP rounding mode for financial calculations

3. **Minimizing BigDecimal Operations**: Reduces the number of BigDecimal operations by:
   - Caching commonly used values (e.g., 0, 1, 100)
   - Optimizing power operations for small exponents
   - Using specialized multiply/divide operations for integer values

### Performance Metrics

The Calculation Service is designed to meet the following performance requirements:

| Operation | Target Performance | Notes |
|-----------|-------------------|-------|
| EMI Calculation | < 200ms | As specified in requirements |
| Compound Interest | < 200ms | As specified in requirements |

### Performance Tips

For optimal performance when using the Calculation Service:

1. **Reuse Input Objects**: Create `CalculationInput` objects once and reuse them for multiple calculations with the same parameters

2. **Batch Processing**: When performing multiple calculations, batch them together to minimize overhead

3. **Thread Safety**: The service interface is stateless and thread-safe, allowing concurrent calculations in multi-threaded environments

4. **Input Validation**: Validate inputs before calling calculation methods to avoid unnecessary exception handling

5. **Result Caching**: Consider caching calculation results for frequently used input combinations

### Memory Considerations

The service implementation minimizes memory usage by:
- Avoiding unnecessary object creation
- Using appropriate precision to prevent excessive memory consumption
- Properly handling large values to prevent out-of-memory errors

---

*This document describes version 1.0 of the Calculation Service API.*