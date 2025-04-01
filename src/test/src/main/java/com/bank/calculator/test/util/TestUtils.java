package com.bank.calculator.test.util;

import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.model.ValidationResult;

import org.junit.jupiter.api.Assertions; // 5.8.2
import java.util.logging.Logger; // JDK 11
import java.util.logging.Level; // JDK 11
import java.io.File; // JDK 11
import java.io.InputStream; // JDK 11
import java.io.IOException; // JDK 11
import java.math.BigDecimal; // JDK 11
import java.math.MathContext; // JDK 11
import java.util.List; // JDK 11
import java.util.ArrayList; // JDK 11
import java.util.Map; // JDK 11
import java.util.HashMap; // JDK 11

/**
 * A utility class that provides common testing functionality for the Compound Interest Calculator application.
 * This class contains methods for test logging, resource loading, test data manipulation, and common assertions
 * used across different test categories.
 */
public class TestUtils {

    private static final Logger LOGGER = Logger.getLogger(TestUtils.class.getName());
    
    public static final String TEST_RESOURCES_PATH = "src/test/resources/";
    public static final String TEST_DATA_PATH = TEST_RESOURCES_PATH + "test-data/";
    public static final MathContext TEST_MATH_CONTEXT = new MathContext(10);
    public static final BigDecimal DELTA = new BigDecimal("0.01");
    
    /**
     * Logs an informational message for test execution.
     *
     * @param message the message to log
     */
    public static void logTestInfo(String message) {
        String threadName = Thread.currentThread().getName();
        long timestamp = System.currentTimeMillis();
        LOGGER.log(Level.INFO, "[{0}][{1}] {2}", new Object[]{threadName, timestamp, message});
    }
    
    /**
     * Logs a warning message for test execution.
     *
     * @param message the message to log
     */
    public static void logTestWarning(String message) {
        String threadName = Thread.currentThread().getName();
        long timestamp = System.currentTimeMillis();
        LOGGER.log(Level.WARNING, "[{0}][{1}] {2}", new Object[]{threadName, timestamp, message});
    }
    
    /**
     * Logs an error message for test execution.
     *
     * @param message the message to log
     * @param throwable the exception to log
     */
    public static void logTestError(String message, Throwable throwable) {
        String threadName = Thread.currentThread().getName();
        long timestamp = System.currentTimeMillis();
        LOGGER.log(Level.SEVERE, "[{0}][{1}] {2}: {3}", 
                new Object[]{threadName, timestamp, message, throwable.getMessage()});
        LOGGER.log(Level.SEVERE, "Exception details:", throwable);
    }
    
    /**
     * Loads a test resource file as an InputStream.
     *
     * @param resourcePath the path to the resource
     * @return the input stream for the requested resource, or null if not found
     */
    public static InputStream loadTestResource(String resourcePath) {
        logTestInfo("Loading test resource: " + resourcePath);
        
        // Try to load as a classpath resource
        InputStream is = TestUtils.class.getClassLoader().getResourceAsStream(resourcePath);
        
        // If not found in classpath, try as a file
        if (is == null) {
            try {
                File file = new File(resourcePath);
                if (file.exists()) {
                    is = file.toURI().toURL().openStream();
                }
            } catch (IOException e) {
                logTestError("Failed to load resource from file system: " + resourcePath, e);
            }
        }
        
        if (is == null) {
            logTestWarning("Resource not found: " + resourcePath);
        }
        
        return is;
    }
    
    /**
     * Loads test data from a JSON file in the test-data directory.
     *
     * @param fileName the name of the test data file
     * @return the content of the test data file as a string, or null if loading failed
     */
    public static String loadTestData(String fileName) {
        String filePath = TEST_DATA_PATH + fileName;
        try (InputStream is = loadTestResource(filePath)) {
            if (is == null) {
                return null;
            }
            
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            return new String(buffer);
        } catch (IOException e) {
            logTestError("Failed to load test data: " + fileName, e);
            return null;
        }
    }
    
    /**
     * Asserts that two BigDecimal values are equal within a small delta.
     *
     * @param expected the expected value
     * @param actual the actual value
     */
    public static void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual) {
        assertBigDecimalEquals(expected, actual, DELTA);
    }
    
    /**
     * Asserts that two BigDecimal values are equal within a specified delta.
     *
     * @param expected the expected value
     * @param actual the actual value
     * @param delta the maximum allowed difference
     */
    public static void assertBigDecimalEquals(BigDecimal expected, BigDecimal actual, BigDecimal delta) {
        if (expected == null && actual == null) {
            return;
        }
        
        Assertions.assertNotNull(expected, "Expected value should not be null");
        Assertions.assertNotNull(actual, "Actual value should not be null");
        
        BigDecimal difference = expected.subtract(actual).abs();
        Assertions.assertTrue(difference.compareTo(delta) <= 0,
                String.format("Expected %s to be equal to %s within delta %s, but difference was %s",
                        expected, actual, delta, difference));
    }
    
    /**
     * Asserts that a ValidationResult has the expected validity and error message.
     *
     * @param result the validation result to check
     * @param expectedValidity the expected validity
     * @param expectedErrorMessage the expected error message (or part of it)
     */
    public static void assertValidationResult(ValidationResult result, boolean expectedValidity, String expectedErrorMessage) {
        Assertions.assertNotNull(result, "ValidationResult should not be null");
        Assertions.assertEquals(expectedValidity, result.isValid(), 
                "ValidationResult validity does not match expected");
        
        if (!expectedValidity && expectedErrorMessage != null) {
            Assertions.assertTrue(result.getErrorMessage() != null && 
                    result.getErrorMessage().contains(expectedErrorMessage),
                    String.format("Expected error message to contain '%s' but was '%s'", 
                            expectedErrorMessage, result.getErrorMessage()));
        }
    }
    
    /**
     * Asserts that two CalculationInput objects have equal principal and duration values.
     *
     * @param expected the expected calculation input
     * @param actual the actual calculation input
     */
    public static void assertCalculationInputEquals(CalculationInput expected, CalculationInput actual) {
        Assertions.assertNotNull(expected, "Expected CalculationInput should not be null");
        Assertions.assertNotNull(actual, "Actual CalculationInput should not be null");
        
        assertBigDecimalEquals(expected.getPrincipal(), actual.getPrincipal());
        Assertions.assertEquals(expected.getDurationYears(), actual.getDurationYears(),
                "Duration in years does not match expected");
    }
    
    /**
     * Formats a BigDecimal value as a USD currency string for test output.
     *
     * @param amount the amount to format
     * @return the formatted currency string
     */
    public static String formatCurrency(BigDecimal amount) {
        if (amount == null) {
            return "N/A";
        }
        
        BigDecimal rounded = amount.setScale(2, BigDecimal.ROUND_HALF_UP);
        return "$" + rounded.toPlainString();
    }
    
    /**
     * Creates a unique test identifier based on the test class and method name.
     *
     * @param testClass the test class
     * @param testMethodName the test method name
     * @return a unique test identifier
     */
    public static String createTestId(Class<?> testClass, String testMethodName) {
        return testClass.getSimpleName() + "." + testMethodName + "." + System.currentTimeMillis();
    }
    
    /**
     * Gets the name of the current test method from the stack trace.
     *
     * @return the name of the current test method, or "unknown" if not found
     */
    public static String getTestMethodName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            String className = element.getClassName();
            String methodName = element.getMethodName();
            if (className.contains("Test") && methodName.startsWith("test")) {
                return methodName;
            }
        }
        return "unknown";
    }
    
    /**
     * Gets the name of the current test class from the stack trace.
     *
     * @return the name of the current test class, or "unknown" if not found
     */
    public static String getTestClassName() {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        for (StackTraceElement element : stackTrace) {
            String className = element.getClassName();
            if (className.contains("Test")) {
                return className.substring(className.lastIndexOf('.') + 1);
            }
        }
        return "unknown";
    }
    
    /**
     * Pauses the current thread for the specified number of milliseconds.
     *
     * @param millis the time to wait in milliseconds
     */
    public static void waitForMillis(long millis) {
        logTestInfo("Waiting for " + millis + " milliseconds");
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            logTestWarning("Thread sleep interrupted: " + e.getMessage());
            Thread.currentThread().interrupt();
        }
    }
}