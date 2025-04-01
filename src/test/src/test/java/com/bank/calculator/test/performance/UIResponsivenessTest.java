package com.bank.calculator.test.performance;

import com.bank.calculator.test.category.PerformanceTest;
import com.bank.calculator.test.util.PerformanceTestUtils;
import com.bank.calculator.test.util.UITestUtils;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.ui.CalculatorUI;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.api.FxRobot;

import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Performance test class that specifically focuses on measuring and validating the UI responsiveness 
 * of the Compound Interest Calculator application. This class verifies that all UI interactions 
 * meet the performance requirements specified in the technical specifications, ensuring that the 
 * application provides a responsive user experience.
 */
public class UIResponsivenessTest extends ApplicationTest implements PerformanceTest {

    private static final Logger LOGGER = Logger.getLogger(UIResponsivenessTest.class.getName());
    private static final String TEST_CLASS_NAME = "UIResponsivenessTest";
    private static final String STANDARD_PRINCIPAL = "10000.00";
    private static final String STANDARD_DURATION = "5";
    private static final int REPEAT_COUNT = 10;
    private static final int WARMUP_COUNT = 3;

    /**
     * Sets up the test class before any tests are run, loading performance thresholds from configuration.
     */
    @BeforeAll
    public static void setupClass() {
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Setting up test class");
        
        // Load performance thresholds from configuration
        PerformanceTestUtils.loadPerformanceThresholds();
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": UI response time threshold: " + 
                            PerformanceTestUtils.MAX_UI_RESPONSE_TIME_MS + " ms");
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Test class setup complete");
    }

    /**
     * Starts the JavaFX application for testing.
     * 
     * @param stage the primary stage for the application
     */
    @Override
    public void start(Stage stage) {
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Starting test application");
        
        try {
            CalculatorUI calculatorUI = new CalculatorUI();
            calculatorUI.start(stage);
            TestUtils.logTestInfo(TEST_CLASS_NAME + ": Test application started successfully");
        } catch (Exception e) {
            TestUtils.logTestError(TEST_CLASS_NAME + ": Failed to start test application", e);
            throw new RuntimeException("Failed to start calculator UI", e);
        }
    }

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Setting up test");
        
        // Wait for the UI to fully initialize
        UITestUtils.waitForFxEvents();
        
        // Ensure the application window is focused
        targetWindow().requestFocus();
        
        // Perform warmup operations to stabilize JVM performance
        performWarmup();
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Test setup complete");
    }

    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    public void tearDown() {
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Tearing down test");
        
        // Click the New Calculation button to reset the form
        UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        
        // Wait for the UI to reset
        UITestUtils.waitForFxEvents();
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Test teardown complete");
    }

    /**
     * Performs warmup operations to stabilize JVM performance before actual tests.
     */
    private void performWarmup() {
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Performing warmup operations");
        
        for (int i = 0; i < WARMUP_COUNT; i++) {
            // Enter principal amount
            UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, STANDARD_PRINCIPAL);
            
            // Enter loan duration
            UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, STANDARD_DURATION);
            
            // Click calculate button
            UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
            
            // Click new calculation button
            UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
            
            // Short wait between iterations
            TestUtils.waitForMillis(100);
        }
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Warmup operations complete");
    }

    /**
     * Tests the response time of entering text in the principal amount field.
     */
    @Test
    @DisplayName("Test principal field response time")
    public void testPrincipalFieldResponseTime() {
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Starting principal field response time test");
        
        long totalTime = 0;
        long maxTime = 0;
        
        for (int i = 0; i < REPEAT_COUNT; i++) {
            long responseTime = UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, STANDARD_PRINCIPAL);
            totalTime += responseTime;
            maxTime = Math.max(maxTime, responseTime);
            
            // Assert that each measurement is within the acceptable threshold
            PerformanceTestUtils.assertUIResponsePerformance(responseTime);
            
            // Clear the field for the next iteration
            UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, "");
        }
        
        // Calculate average response time
        long averageTime = totalTime / REPEAT_COUNT;
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Principal field response time - Max: " + 
                            maxTime + " ms, Average: " + averageTime + " ms");
        
        // Assert that the average response time is within the acceptable threshold
        PerformanceTestUtils.assertUIResponsePerformance(averageTime);
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Principal field response time test completed successfully");
    }

    /**
     * Tests the response time of entering text in the loan duration field.
     */
    @Test
    @DisplayName("Test duration field response time")
    public void testDurationFieldResponseTime() {
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Starting duration field response time test");
        
        long totalTime = 0;
        long maxTime = 0;
        
        for (int i = 0; i < REPEAT_COUNT; i++) {
            long responseTime = UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, STANDARD_DURATION);
            totalTime += responseTime;
            maxTime = Math.max(maxTime, responseTime);
            
            // Assert that each measurement is within the acceptable threshold
            PerformanceTestUtils.assertUIResponsePerformance(responseTime);
            
            // Clear the field for the next iteration
            UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, "");
        }
        
        // Calculate average response time
        long averageTime = totalTime / REPEAT_COUNT;
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Duration field response time - Max: " + 
                            maxTime + " ms, Average: " + averageTime + " ms");
        
        // Assert that the average response time is within the acceptable threshold
        PerformanceTestUtils.assertUIResponsePerformance(averageTime);
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Duration field response time test completed successfully");
    }

    /**
     * Tests the response time of clicking the Calculate button.
     */
    @Test
    @DisplayName("Test calculate button response time")
    public void testCalculateButtonResponseTime() {
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Starting calculate button response time test");
        
        // Enter principal and duration to enable calculate button
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, STANDARD_PRINCIPAL);
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, STANDARD_DURATION);
        
        long totalTime = 0;
        long maxTime = 0;
        
        for (int i = 0; i < REPEAT_COUNT; i++) {
            long responseTime = UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
            totalTime += responseTime;
            maxTime = Math.max(maxTime, responseTime);
            
            // Assert that each measurement is within the acceptable threshold
            PerformanceTestUtils.assertUIResponsePerformance(responseTime);
            
            // Reset form for next iteration
            UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
            
            // Re-enter values for next iteration
            UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, STANDARD_PRINCIPAL);
            UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, STANDARD_DURATION);
        }
        
        // Calculate average response time
        long averageTime = totalTime / REPEAT_COUNT;
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Calculate button response time - Max: " + 
                            maxTime + " ms, Average: " + averageTime + " ms");
        
        // Assert that the average response time is within the acceptable threshold
        PerformanceTestUtils.assertUIResponsePerformance(averageTime);
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Calculate button response time test completed successfully");
    }

    /**
     * Tests the response time of clicking the New Calculation button.
     */
    @Test
    @DisplayName("Test new calculation button response time")
    public void testNewCalculationButtonResponseTime() {
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Starting new calculation button response time test");
        
        // Set up a calculation to be able to reset
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, STANDARD_PRINCIPAL);
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, STANDARD_DURATION);
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        long totalTime = 0;
        long maxTime = 0;
        
        for (int i = 0; i < REPEAT_COUNT; i++) {
            long responseTime = UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
            totalTime += responseTime;
            maxTime = Math.max(maxTime, responseTime);
            
            // Assert that each measurement is within the acceptable threshold
            PerformanceTestUtils.assertUIResponsePerformance(responseTime);
            
            // Set up for next iteration
            UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, STANDARD_PRINCIPAL);
            UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, STANDARD_DURATION);
            UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        }
        
        // Calculate average response time
        long averageTime = totalTime / REPEAT_COUNT;
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": New calculation button response time - Max: " + 
                            maxTime + " ms, Average: " + averageTime + " ms");
        
        // Assert that the average response time is within the acceptable threshold
        PerformanceTestUtils.assertUIResponsePerformance(averageTime);
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": New calculation button response time test completed successfully");
    }

    /**
     * Tests the response time of a complete calculation workflow.
     */
    @Test
    @DisplayName("Test complete workflow response time")
    public void testCompleteWorkflowResponseTime() {
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Starting complete workflow response time test");
        
        long totalTime = 0;
        long maxTime = 0;
        
        for (int i = 0; i < REPEAT_COUNT; i++) {
            long responseTime = measureCompleteWorkflow();
            totalTime += responseTime;
            maxTime = Math.max(maxTime, responseTime);
            
            // Assert that each measurement is within an acceptable threshold for the complete workflow
            // Note: Complete workflow might take longer than individual operations
            Assertions.assertTrue(responseTime <= PerformanceTestUtils.MAX_UI_RESPONSE_TIME_MS * 3,
                            "Complete workflow response time (" + responseTime + 
                            " ms) exceeds the threshold (" + (PerformanceTestUtils.MAX_UI_RESPONSE_TIME_MS * 3) + " ms)");
            
            // Reset for next iteration
            UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        }
        
        // Calculate average response time
        long averageTime = totalTime / REPEAT_COUNT;
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Complete workflow response time - Max: " + 
                            maxTime + " ms, Average: " + averageTime + " ms");
        
        // Assert that the average response time is within an acceptable threshold for the complete workflow
        Assertions.assertTrue(averageTime <= PerformanceTestUtils.MAX_UI_RESPONSE_TIME_MS * 3,
                        "Average complete workflow response time (" + averageTime + 
                        " ms) exceeds the threshold (" + (PerformanceTestUtils.MAX_UI_RESPONSE_TIME_MS * 3) + " ms)");
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Complete workflow response time test completed successfully");
    }

    /**
     * Tests the response time during rapid input changes.
     */
    @Test
    @DisplayName("Test response time during rapid input changes")
    public void testRapidInputResponseTime() {
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Starting rapid input response time test");
        
        long totalTime = 0;
        long maxTime = 0;
        
        for (int i = 0; i < REPEAT_COUNT; i++) {
            long responseTime = measureRapidInputChanges();
            totalTime += responseTime;
            maxTime = Math.max(maxTime, responseTime);
            
            // Assert that each measurement is within the acceptable threshold
            PerformanceTestUtils.assertUIResponsePerformance(responseTime);
            
            // Reset for next iteration
            UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        }
        
        // Calculate average response time
        long averageTime = totalTime / REPEAT_COUNT;
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Rapid input response time - Max: " + 
                            maxTime + " ms, Average: " + averageTime + " ms");
        
        // Assert that the average response time is within the acceptable threshold
        PerformanceTestUtils.assertUIResponsePerformance(averageTime);
        
        TestUtils.logTestInfo(TEST_CLASS_NAME + ": Rapid input response time test completed successfully");
    }

    /**
     * Measures the time to perform a complete calculation workflow.
     * 
     * @return The time taken in milliseconds to perform the complete workflow
     */
    private long measureCompleteWorkflow() {
        Runnable completeWorkflow = () -> {
            // Enter principal amount
            UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, STANDARD_PRINCIPAL);
            
            // Enter loan duration
            UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, STANDARD_DURATION);
            
            // Click calculate button
            UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        };
        
        return PerformanceTestUtils.measureUIResponseTime(completeWorkflow);
    }

    /**
     * Measures the time to handle rapid input changes.
     * 
     * @return The time taken in milliseconds to handle rapid input changes
     */
    private long measureRapidInputChanges() {
        Runnable rapidInputChanges = () -> {
            // Rapidly change the principal amount multiple times
            UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, "1000");
            UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, "5000");
            UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, "10000");
            UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, STANDARD_PRINCIPAL);
        };
        
        return PerformanceTestUtils.measureUIResponseTime(rapidInputChanges);
    }
}