package com.bank.calculator.test.util;

import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.constant.ErrorMessages;

import java.math.BigDecimal; // JDK 11
import java.util.Random; // JDK 11
import java.util.List; // JDK 11
import java.util.ArrayList; // JDK 11
import java.util.Map; // JDK 11
import java.util.HashMap; // JDK 11
import java.util.logging.Logger; // JDK 11

/**
 * Utility class that generates test data for the Compound Interest Calculator application.
 * This class provides methods to create various test inputs with different characteristics
 * for unit, integration, and performance testing.
 */
public class TestDataGenerator {

    private static final Logger LOGGER = Logger.getLogger(TestDataGenerator.class.getName());
    private static final Random RANDOM = new Random();
    private static final BigDecimal DEFAULT_PRINCIPAL = new BigDecimal("10000.00");
    private static final int DEFAULT_DURATION = 5;
    private static final BigDecimal PRINCIPAL_STEP = new BigDecimal("1000.00");

    /**
     * Creates a CalculationInput object with the specified principal amount and loan duration.
     *
     * @param principal The principal amount
     * @param durationYears The loan duration in years
     * @return A CalculationInput object with the specified values
     * @throws IllegalArgumentException if principal is null or durationYears is not positive
     */
    public static CalculationInput createCalculationInput(BigDecimal principal, int durationYears) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal amount cannot be null");
        }
        if (durationYears <= 0) {
            throw new IllegalArgumentException("Loan duration must be greater than zero");
        }
        
        LOGGER.fine("Creating calculation input with principal=" + principal + " and duration=" + durationYears);
        return new CalculationInput(principal, durationYears);
    }

    /**
     * Creates a CalculationInput object with the specified principal amount, loan duration, and interest rate.
     *
     * @param principal The principal amount
     * @param durationYears The loan duration in years
     * @param interestRate The interest rate
     * @return A CalculationInput object with the specified values
     * @throws IllegalArgumentException if any parameter is invalid
     */
    public static CalculationInput createCalculationInput(BigDecimal principal, int durationYears, BigDecimal interestRate) {
        if (principal == null) {
            throw new IllegalArgumentException("Principal amount cannot be null");
        }
        if (durationYears <= 0) {
            throw new IllegalArgumentException("Loan duration must be greater than zero");
        }
        if (interestRate == null) {
            throw new IllegalArgumentException("Interest rate cannot be null");
        }
        
        LOGGER.fine("Creating calculation input with principal=" + principal + 
                   ", duration=" + durationYears + 
                   ", and interestRate=" + interestRate);
        
        CalculationInput input = new CalculationInput(principal, durationYears);
        input.setInterestRate(interestRate);
        return input;
    }

    /**
     * Creates a CalculationInput object with default values for principal amount and loan duration.
     *
     * @return A CalculationInput object with default values
     */
    public static CalculationInput createDefaultCalculationInput() {
        LOGGER.fine("Creating calculation input with default values");
        return new CalculationInput(DEFAULT_PRINCIPAL, DEFAULT_DURATION);
    }

    /**
     * Creates a CalculationInput object with random values within valid ranges.
     *
     * @return A CalculationInput object with random valid values
     */
    public static CalculationInput createRandomCalculationInput() {
        BigDecimal randomPrincipal = createRandomPrincipal();
        int randomDuration = createRandomDuration();
        
        LOGGER.fine("Creating random calculation input with principal=" + 
                   randomPrincipal + " and duration=" + randomDuration);
        
        return new CalculationInput(randomPrincipal, randomDuration);
    }

    /**
     * Generates a random principal amount within the valid range.
     *
     * @return A random principal amount
     */
    public static BigDecimal createRandomPrincipal() {
        BigDecimal min = CalculationConstants.MIN_PRINCIPAL_AMOUNT;
        BigDecimal max = CalculationConstants.MAX_PRINCIPAL_AMOUNT;
        BigDecimal range = max.subtract(min);
        
        // Generate a random value between 0 and range
        BigDecimal randomValue = range.multiply(new BigDecimal(RANDOM.nextDouble()));
        
        // Add the minimum to get a value between min and max
        BigDecimal result = min.add(randomValue);
        
        // Round to currency precision
        result = result.round(CalculationConstants.CURRENCY_MATH_CONTEXT);
        
        LOGGER.fine("Generated random principal: " + result);
        return result;
    }

    /**
     * Generates a random loan duration within the valid range.
     *
     * @return A random loan duration in years
     */
    public static int createRandomDuration() {
        int min = CalculationConstants.MIN_DURATION_YEARS;
        int max = CalculationConstants.MAX_DURATION_YEARS;
        
        // Generate a random value between min and max (inclusive)
        int result = RANDOM.nextInt(max - min + 1) + min;
        
        LOGGER.fine("Generated random duration: " + result);
        return result;
    }

    /**
     * Generates a random interest rate within a reasonable range.
     *
     * @return A random interest rate
     */
    public static BigDecimal createRandomInterestRate() {
        // Generate random interest rate between 1% and 15%
        double randomDouble = 0.01 + (0.15 - 0.01) * RANDOM.nextDouble();
        BigDecimal result = new BigDecimal(String.valueOf(randomDouble)).multiply(CalculationConstants.HUNDRED);
        
        // Round to 4 decimal places
        result = result.setScale(4, CalculationConstants.CURRENCY_MATH_CONTEXT.getRoundingMode());
        
        LOGGER.fine("Generated random interest rate: " + result);
        return result;
    }

    /**
     * Creates a CalculationInput object with an invalid principal amount.
     * Note: This method returns values for testing validation logic rather than creating
     * actual invalid objects (which is prevented by CalculationInput's constructor validation).
     *
     * @param invalidationType The type of invalidation to apply ("NEGATIVE", "ZERO", "BELOW_MIN", "ABOVE_MAX")
     * @return A CalculationInput object for testing invalid principal scenarios
     */
    public static CalculationInput createInvalidPrincipalInput(String invalidationType) {
        BigDecimal principal;
        int duration = DEFAULT_DURATION;
        
        switch (invalidationType) {
            case "NEGATIVE":
                principal = DEFAULT_PRINCIPAL.negate();
                break;
            case "ZERO":
                principal = BigDecimal.ZERO;
                break;
            case "BELOW_MIN":
                principal = CalculationConstants.MIN_PRINCIPAL_AMOUNT.subtract(BigDecimal.ONE);
                break;
            case "ABOVE_MAX":
                principal = CalculationConstants.MAX_PRINCIPAL_AMOUNT.add(BigDecimal.ONE);
                break;
            default:
                principal = DEFAULT_PRINCIPAL.negate();
                break;
        }
        
        LOGGER.fine("Creating invalid principal input of type " + invalidationType +
                   " with value " + principal);
        
        // For testing purposes, we return a valid object but tests should use the invalidationType
        // to simulate the validation logic separately
        return new CalculationInput(principal.abs(), duration);
    }

    /**
     * Creates a CalculationInput object with an invalid loan duration.
     * Note: This method returns values for testing validation logic rather than creating
     * actual invalid objects (which is prevented by CalculationInput's constructor validation).
     *
     * @param invalidationType The type of invalidation to apply ("NEGATIVE", "ZERO", "BELOW_MIN", "ABOVE_MAX")
     * @return A CalculationInput object for testing invalid duration scenarios
     */
    public static CalculationInput createInvalidDurationInput(String invalidationType) {
        BigDecimal principal = DEFAULT_PRINCIPAL;
        int duration;
        
        switch (invalidationType) {
            case "NEGATIVE":
                duration = -1;
                break;
            case "ZERO":
                duration = 0;
                break;
            case "BELOW_MIN":
                duration = CalculationConstants.MIN_DURATION_YEARS - 1;
                if (duration <= 0) duration = 0;
                break;
            case "ABOVE_MAX":
                duration = CalculationConstants.MAX_DURATION_YEARS + 1;
                break;
            default:
                duration = -1;
                break;
        }
        
        LOGGER.fine("Creating invalid duration input of type " + invalidationType +
                   " with value " + duration);
        
        // For testing purposes, we return a valid object but tests should use the invalidationType
        // to simulate the validation logic separately
        return new CalculationInput(principal, Math.max(1, duration));
    }

    /**
     * Creates a ValidationResult object for testing validation logic.
     *
     * @param valid Whether the validation result should be valid
     * @param errorType The type of error to include in the validation result
     * @return A ValidationResult object
     */
    public static ValidationResult createValidationResult(boolean valid, String errorType) {
        if (valid) {
            return ValidationResult.createValid();
        }
        
        String errorMessage;
        switch (errorType) {
            case "PRINCIPAL_REQUIRED":
                errorMessage = ErrorMessages.PRINCIPAL_REQUIRED;
                break;
            case "PRINCIPAL_POSITIVE":
                errorMessage = ErrorMessages.PRINCIPAL_POSITIVE;
                break;
            case "PRINCIPAL_MIN_REQUIRED":
                errorMessage = ErrorMessages.PRINCIPAL_MIN_REQUIRED;
                break;
            case "PRINCIPAL_MAX_EXCEEDED":
                errorMessage = ErrorMessages.PRINCIPAL_MAX_EXCEEDED;
                break;
            case "DURATION_REQUIRED":
                errorMessage = ErrorMessages.DURATION_REQUIRED;
                break;
            case "DURATION_POSITIVE":
                errorMessage = ErrorMessages.DURATION_POSITIVE;
                break;
            case "DURATION_MIN_REQUIRED":
                errorMessage = ErrorMessages.DURATION_MIN_REQUIRED;
                break;
            case "DURATION_MAX_EXCEEDED":
                errorMessage = ErrorMessages.DURATION_MAX_EXCEEDED;
                break;
            default:
                errorMessage = ErrorMessages.INVALID_INPUT;
                break;
        }
        
        LOGGER.fine("Creating invalid validation result with error: " + errorMessage);
        return ValidationResult.createInvalid(errorMessage);
    }

    /**
     * Creates a list of CalculationInput objects for boundary testing.
     *
     * @return A list of CalculationInput objects at boundary values
     */
    public static List<CalculationInput> createBoundaryTestInputs() {
        List<CalculationInput> boundaryInputs = new ArrayList<>();
        
        // Minimum and maximum principal amounts with default duration
        boundaryInputs.add(new CalculationInput(CalculationConstants.MIN_PRINCIPAL_AMOUNT, DEFAULT_DURATION));
        boundaryInputs.add(new CalculationInput(CalculationConstants.MAX_PRINCIPAL_AMOUNT, DEFAULT_DURATION));
        
        // Default principal with minimum and maximum durations
        boundaryInputs.add(new CalculationInput(DEFAULT_PRINCIPAL, CalculationConstants.MIN_DURATION_YEARS));
        boundaryInputs.add(new CalculationInput(DEFAULT_PRINCIPAL, CalculationConstants.MAX_DURATION_YEARS));
        
        // Near-boundary values for principal
        BigDecimal justAboveMin = CalculationConstants.MIN_PRINCIPAL_AMOUNT.add(PRINCIPAL_STEP);
        BigDecimal justBelowMax = CalculationConstants.MAX_PRINCIPAL_AMOUNT.subtract(PRINCIPAL_STEP);
        
        boundaryInputs.add(new CalculationInput(justAboveMin, DEFAULT_DURATION));
        boundaryInputs.add(new CalculationInput(justBelowMax, DEFAULT_DURATION));
        
        // Near-boundary values for duration
        boundaryInputs.add(new CalculationInput(DEFAULT_PRINCIPAL, CalculationConstants.MIN_DURATION_YEARS + 1));
        boundaryInputs.add(new CalculationInput(DEFAULT_PRINCIPAL, CalculationConstants.MAX_DURATION_YEARS - 1));
        
        LOGGER.fine("Created " + boundaryInputs.size() + " boundary test inputs");
        return boundaryInputs;
    }

    /**
     * Creates a list of CalculationInput objects for edge case testing.
     *
     * @return A list of CalculationInput objects with edge case values
     */
    public static List<CalculationInput> createEdgeCaseTestInputs() {
        List<CalculationInput> edgeCaseInputs = new ArrayList<>();
        
        // Zero interest rate edge case
        CalculationInput zeroInterestInput = new CalculationInput(DEFAULT_PRINCIPAL, DEFAULT_DURATION);
        zeroInterestInput.setInterestRate(BigDecimal.ZERO);
        edgeCaseInputs.add(zeroInterestInput);
        
        // Very small principal (just above minimum)
        BigDecimal verySmallPrincipal = CalculationConstants.MIN_PRINCIPAL_AMOUNT.add(new BigDecimal("0.01"));
        edgeCaseInputs.add(new CalculationInput(verySmallPrincipal, DEFAULT_DURATION));
        
        // Very large principal (just below maximum)
        BigDecimal veryLargePrincipal = CalculationConstants.MAX_PRINCIPAL_AMOUNT.subtract(new BigDecimal("0.01"));
        edgeCaseInputs.add(new CalculationInput(veryLargePrincipal, DEFAULT_DURATION));
        
        // Very short duration
        edgeCaseInputs.add(new CalculationInput(DEFAULT_PRINCIPAL, CalculationConstants.MIN_DURATION_YEARS));
        
        // Very long duration
        edgeCaseInputs.add(new CalculationInput(DEFAULT_PRINCIPAL, CalculationConstants.MAX_DURATION_YEARS));
        
        LOGGER.fine("Created " + edgeCaseInputs.size() + " edge case test inputs");
        return edgeCaseInputs;
    }

    /**
     * Creates a list of CalculationInput objects for performance testing.
     *
     * @return A list of CalculationInput objects for performance testing
     */
    public static List<CalculationInput> createPerformanceTestInputs() {
        List<CalculationInput> performanceInputs = new ArrayList<>();
        
        // Default values
        performanceInputs.add(createDefaultCalculationInput());
        
        // Large principal amount
        performanceInputs.add(new CalculationInput(new BigDecimal("500000.00"), DEFAULT_DURATION));
        
        // Maximum duration
        performanceInputs.add(new CalculationInput(DEFAULT_PRINCIPAL, CalculationConstants.MAX_DURATION_YEARS));
        
        // Large principal with maximum duration
        performanceInputs.add(new CalculationInput(new BigDecimal("500000.00"), CalculationConstants.MAX_DURATION_YEARS));
        
        // Add some random inputs for variety
        for (int i = 0; i < 5; i++) {
            performanceInputs.add(createRandomCalculationInput());
        }
        
        LOGGER.fine("Created " + performanceInputs.size() + " performance test inputs");
        return performanceInputs;
    }

    /**
     * Creates a map of test inputs with their expected calculation results.
     * These inputs and expected results can be used for verification testing.
     *
     * @return A map of test inputs to expected EMI amounts
     */
    public static Map<CalculationInput, BigDecimal> createTestInputsWithExpectedResults() {
        Map<CalculationInput, BigDecimal> testCases = new HashMap<>();
        
        // Standard test case: $10,000, 5 years, 7.5% interest rate
        // Expected EMI: $200.38
        CalculationInput standardInput = createDefaultCalculationInput();
        testCases.put(standardInput, new BigDecimal("200.38"));
        
        // $25,000, 3 years, 7.5% interest rate
        // Expected EMI: $777.23
        CalculationInput mediumPrincipalInput = new CalculationInput(new BigDecimal("25000.00"), 3);
        testCases.put(mediumPrincipalInput, new BigDecimal("777.23"));
        
        // $5,000, 1 year, 7.5% interest rate
        // Expected EMI: $430.73
        CalculationInput shortTermInput = new CalculationInput(new BigDecimal("5000.00"), 1);
        testCases.put(shortTermInput, new BigDecimal("430.73"));
        
        // $100,000, 10 years, 7.5% interest rate
        // Expected EMI: $1186.19
        CalculationInput longTermInput = new CalculationInput(new BigDecimal("100000.00"), 10);
        testCases.put(longTermInput, new BigDecimal("1186.19"));
        
        LOGGER.fine("Created " + testCases.size() + " test cases with expected results");
        return testCases;
    }
}