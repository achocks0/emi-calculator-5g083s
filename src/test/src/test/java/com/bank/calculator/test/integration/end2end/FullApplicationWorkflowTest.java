package com.bank.calculator.test.integration.end2end;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer;

import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.application.Platform;
import javafx.stage.Stage;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.bank.calculator.test.fixture.UITestFixture;
import com.bank.calculator.test.util.UITestUtils;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.util.PerformanceTestUtils;
import com.bank.calculator.test.category.IntegrationTest;
import com.bank.calculator.CompoundInterestCalculatorApp;
import com.bank.calculator.ui.CalculatorUI;

/**
 * End-to-end integration test class that verifies the complete workflow of the Compound Interest Calculator application.
 * This test ensures that all components of the application work together correctly in a real-world usage scenario.
 */
@IntegrationTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class FullApplicationWorkflowTest {

    private static final Logger LOGGER = Logger.getLogger(FullApplicationWorkflowTest.class.getName());
    private static final long APPLICATION_STARTUP_TIMEOUT_MS = 10000;
    private static final long UI_OPERATION_TIMEOUT_MS = 5000;
    private static final String[] TEST_ARGS = new String[0];
    
    private FxRobot robot;
    private Stage stage;
    
    /**
     * Sets up the test environment before any tests are run.
     */
    @BeforeAll
    static void setupClass() {
        LOGGER.info("Setting up test environment for FullApplicationWorkflowTest");
        // Configure any test environment settings
        LOGGER.info("Test environment setup complete");
    }
    
    /**
     * Cleans up the test environment after all tests have completed.
     */
    @AfterAll
    static void teardownClass() {
        LOGGER.info("Tearing down test environment for FullApplicationWorkflowTest");
        // Clean up any resources used by the tests
        LOGGER.info("Test environment teardown complete");
    }
    
    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    void setUp() {
        LOGGER.info("Setting up test for FullApplicationWorkflowTest");
        
        // Initialize the application for testing
        robot = new FxRobot();
        launchApplication();
        
        // Wait for the application to fully start
        waitForApplicationStartup();
        
        LOGGER.info("Test setup complete");
    }
    
    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    void tearDown() {
        LOGGER.info("Tearing down test for FullApplicationWorkflowTest");
        
        // Close the application
        closeApplication();
        
        // Clean up any resources used by the test
        robot = null;
        stage = null;
        
        LOGGER.info("Test teardown complete");
    }
    
    /**
     * Tests the standard calculation workflow with valid inputs.
     */
    @Test
    @DisplayName("Test standard calculation workflow with valid inputs")
    @Order(1)
    void testStandardCalculationWorkflow() {
        LOGGER.info("Starting standard calculation workflow test");
        
        // Perform a standard calculation
        UITestFixture.performStandardCalculationTest(robot, 
                UITestFixture.STANDARD_PRINCIPAL, 
                UITestFixture.STANDARD_DURATION, 
                UITestFixture.STANDARD_RESULT);
        
        // Verify UI response time
        long responseTime = PerformanceTestUtils.measureUIResponseTime(() -> {
            UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        });
        UITestUtils.assertUIResponseTime(responseTime);
        
        LOGGER.info("Standard calculation workflow test completed successfully");
    }
    
    /**
     * Tests the calculation workflow with large principal amount.
     */
    @Test
    @DisplayName("Test calculation workflow with large principal amount")
    @Order(2)
    void testLargeValueCalculationWorkflow() {
        LOGGER.info("Starting large value calculation workflow test");
        
        // Perform a calculation with large principal amount
        UITestFixture.performStandardCalculationTest(robot, 
                UITestFixture.LARGE_PRINCIPAL, 
                UITestFixture.STANDARD_DURATION, 
                UITestFixture.LARGE_PRINCIPAL_RESULT);
        
        // Verify UI response time
        long responseTime = PerformanceTestUtils.measureUIResponseTime(() -> {
            UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        });
        UITestUtils.assertUIResponseTime(responseTime);
        
        LOGGER.info("Large value calculation workflow test completed successfully");
    }
    
    /**
     * Tests the validation workflow with negative principal amount.
     */
    @Test
    @DisplayName("Test validation workflow with negative principal amount")
    @Order(3)
    void testNegativePrincipalValidationWorkflow() {
        LOGGER.info("Starting negative principal validation workflow test");
        
        // Perform a calculation with negative principal amount
        UITestFixture.performValidationErrorTest(robot,
                UITestFixture.NEGATIVE_PRINCIPAL,
                UITestFixture.STANDARD_DURATION,
                "principal", // The field expected to have an error
                "Principal amount must be a positive number");
        
        // Verify UI response time
        long responseTime = PerformanceTestUtils.measureUIResponseTime(() -> {
            UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        });
        UITestUtils.assertUIResponseTime(responseTime);
        
        LOGGER.info("Negative principal validation workflow test completed successfully");
    }
    
    /**
     * Tests the validation workflow with zero principal amount.
     */
    @Test
    @DisplayName("Test validation workflow with zero principal amount")
    @Order(4)
    void testZeroPrincipalValidationWorkflow() {
        LOGGER.info("Starting zero principal validation workflow test");
        
        // Perform a calculation with zero principal amount
        UITestFixture.performValidationErrorTest(robot,
                UITestFixture.ZERO_PRINCIPAL,
                UITestFixture.STANDARD_DURATION,
                "principal", // The field expected to have an error
                "Principal amount must be a positive number");
        
        // Verify UI response time
        long responseTime = PerformanceTestUtils.measureUIResponseTime(() -> {
            UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        });
        UITestUtils.assertUIResponseTime(responseTime);
        
        LOGGER.info("Zero principal validation workflow test completed successfully");
    }
    
    /**
     * Tests the validation workflow with negative loan duration.
     */
    @Test
    @DisplayName("Test validation workflow with negative loan duration")
    @Order(5)
    void testNegativeDurationValidationWorkflow() {
        LOGGER.info("Starting negative duration validation workflow test");
        
        // Perform a calculation with negative loan duration
        UITestFixture.performValidationErrorTest(robot,
                UITestFixture.STANDARD_PRINCIPAL,
                UITestFixture.NEGATIVE_DURATION,
                "duration", // The field expected to have an error
                "Loan duration must be a positive whole number");
        
        // Verify UI response time
        long responseTime = PerformanceTestUtils.measureUIResponseTime(() -> {
            UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        });
        UITestUtils.assertUIResponseTime(responseTime);
        
        LOGGER.info("Negative duration validation workflow test completed successfully");
    }
    
    /**
     * Tests the validation workflow with zero loan duration.
     */
    @Test
    @DisplayName("Test validation workflow with zero loan duration")
    @Order(6)
    void testZeroDurationValidationWorkflow() {
        LOGGER.info("Starting zero duration validation workflow test");
        
        // Perform a calculation with zero loan duration
        UITestFixture.performValidationErrorTest(robot,
                UITestFixture.STANDARD_PRINCIPAL,
                UITestFixture.ZERO_DURATION,
                "duration", // The field expected to have an error
                "Loan duration must be a positive whole number");
        
        // Verify UI response time
        long responseTime = PerformanceTestUtils.measureUIResponseTime(() -> {
            UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        });
        UITestUtils.assertUIResponseTime(responseTime);
        
        LOGGER.info("Zero duration validation workflow test completed successfully");
    }
    
    /**
     * Tests the new calculation workflow after a successful calculation.
     */
    @Test
    @DisplayName("Test new calculation workflow after successful calculation")
    @Order(7)
    void testNewCalculationWorkflow() {
        LOGGER.info("Starting new calculation workflow test");
        
        // First perform a standard calculation
        UITestFixture.performStandardCalculationTest(robot, 
                UITestFixture.STANDARD_PRINCIPAL, 
                UITestFixture.STANDARD_DURATION, 
                UITestFixture.STANDARD_RESULT);
        
        // Now click the New Calculation button
        UITestUtils.performNewCalculation(robot);
        
        // Verify that input fields are cleared
        UITestUtils.verifyInputFieldsCleared(robot);
        
        // Verify UI response time
        long responseTime = PerformanceTestUtils.measureUIResponseTime(() -> {
            UITestUtils.clickButton(robot, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        });
        UITestUtils.assertUIResponseTime(responseTime);
        
        LOGGER.info("New calculation workflow test completed successfully");
    }
    
    /**
     * Tests a complete end-to-end workflow with multiple calculations.
     */
    @Test
    @DisplayName("Test complete end-to-end workflow with multiple calculations")
    @Order(8)
    void testCompleteEndToEndWorkflow() {
        LOGGER.info("Starting complete end-to-end workflow test");
        
        // First perform a standard calculation
        UITestFixture.performStandardCalculationTest(robot, 
                UITestFixture.STANDARD_PRINCIPAL, 
                UITestFixture.STANDARD_DURATION, 
                UITestFixture.STANDARD_RESULT);
        
        // Click New Calculation
        UITestUtils.performNewCalculation(robot);
        
        // Verify fields cleared
        UITestUtils.verifyInputFieldsCleared(robot);
        
        // Now perform a calculation with large values
        UITestFixture.performStandardCalculationTest(robot, 
                UITestFixture.LARGE_PRINCIPAL, 
                UITestFixture.STANDARD_DURATION, 
                UITestFixture.LARGE_PRINCIPAL_RESULT);
        
        // Click New Calculation again
        UITestUtils.performNewCalculation(robot);
        
        // Verify fields cleared
        UITestUtils.verifyInputFieldsCleared(robot);
        
        // Now test a validation error case
        UITestFixture.performValidationErrorTest(robot,
                UITestFixture.NEGATIVE_PRINCIPAL,
                UITestFixture.STANDARD_DURATION,
                "principal",
                "Principal amount must be a positive number");
        
        // Verify UI response times for all operations
        long responseTime = PerformanceTestUtils.measureUIResponseTime(() -> {
            UITestUtils.clickButton(robot, UITestUtils.CALCULATE_BUTTON_ID);
        });
        UITestUtils.assertUIResponseTime(responseTime);
        
        LOGGER.info("Complete end-to-end workflow test completed successfully");
    }
    
    /**
     * Launches the application for testing.
     */
    private void launchApplication() {
        LOGGER.info("Launching application for testing");
        
        // Create a latch to wait for application startup
        CountDownLatch latch = new CountDownLatch(1);
        
        // Start the application in a separate thread
        new Thread(() -> {
            try {
                CompoundInterestCalculatorApp.main(TEST_ARGS);
                latch.countDown();
            } catch (Exception e) {
                LOGGER.log(Level.SEVERE, "Error launching application", e);
            }
        }).start();
        
        // Wait for application to start
        try {
            boolean started = latch.await(APPLICATION_STARTUP_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            if (!started) {
                LOGGER.warning("Timed out waiting for application to start");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            LOGGER.log(Level.SEVERE, "Interrupted while waiting for application to start", e);
        }
        
        LOGGER.info("Application launched for testing");
    }
    
    /**
     * Closes the application after testing.
     */
    private void closeApplication() {
        LOGGER.info("Closing application after testing");
        
        // Use Platform.runLater to exit the application
        Platform.runLater(() -> Platform.exit());
        
        // Wait for the application to close
        TestUtils.waitForMillis(UI_OPERATION_TIMEOUT_MS);
        
        LOGGER.info("Application closed after testing");
    }
    
    /**
     * Waits for the application to fully start.
     */
    private void waitForApplicationStartup() {
        LOGGER.info("Waiting for application to fully start");
        
        // Wait for a specified timeout period
        TestUtils.waitForMillis(UI_OPERATION_TIMEOUT_MS);
        
        // Additional verification that application is ready could be added here
        
        LOGGER.info("Application has started successfully");
    }
}