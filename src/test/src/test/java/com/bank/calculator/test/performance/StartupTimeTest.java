package com.bank.calculator.test.performance;

import org.junit.jupiter.api.Test; // JUnit 5.8.2
import org.junit.jupiter.api.BeforeEach; // JUnit 5.8.2
import org.junit.jupiter.api.AfterEach; // JUnit 5.8.2
import org.junit.jupiter.api.Assertions; // JUnit 5.8.2
import org.junit.jupiter.api.RepeatedTest; // JUnit 5.8.2
import org.junit.jupiter.api.DisplayName; // JUnit 5.8.2

import java.util.logging.Logger; // JDK 11
import java.util.logging.Level; // JDK 11

import com.bank.calculator.test.category.PerformanceTest;
import com.bank.calculator.test.util.PerformanceTestUtils;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.CompoundInterestCalculatorApp;

/**
 * Test class that measures and verifies the startup time of the Compound Interest Calculator application.
 * This test ensures that the application meets the performance requirement of starting up within the
 * specified time threshold.
 */
@PerformanceTest
public class StartupTimeTest {

    private static final Logger LOGGER = Logger.getLogger(StartupTimeTest.class.getName());
    private static final int TEST_ITERATIONS = 3;
    private static final String STARTUP_TEST_ARGS = "--test-mode";
    private static final long COOLDOWN_PERIOD_MS = 1000;
    
    private long totalStartupTime;
    private int testCount;
    
    /**
     * Default constructor for the StartupTimeTest class
     */
    public StartupTimeTest() {
        totalStartupTime = 0;
        testCount = 0;
    }
    
    /**
     * Prepares the test environment before each test execution
     */
    @BeforeEach
    public void setUp() {
        TestUtils.logTestInfo("Setting up startup time test");
        // Run garbage collection to ensure clean memory state
        System.gc();
        // Wait for a short period to allow system to stabilize
        TestUtils.waitForMillis(100);
    }
    
    /**
     * Cleans up the test environment after each test execution
     */
    @AfterEach
    public void tearDown() {
        TestUtils.logTestInfo("Tearing down startup time test");
        // Run garbage collection to clean up memory
        System.gc();
        // Wait for cooldown period to ensure system returns to normal state
        TestUtils.waitForMillis(COOLDOWN_PERIOD_MS);
    }
    
    /**
     * Tests that the application starts up within the maximum allowed time
     */
    @Test
    @DisplayName("Application should start within the maximum allowed time")
    public void testApplicationStartupTime() {
        TestUtils.logTestInfo("Starting startup time test");
        
        // Measure the startup time
        long startupTime = measureStartupTime();
        
        // Assert that the startup time is within the acceptable limit
        Assertions.assertTrue(startupTime <= PerformanceTestUtils.MAX_STARTUP_TIME_MS,
                "Application startup time (" + startupTime + "ms) exceeds maximum allowed time (" 
                + PerformanceTestUtils.MAX_STARTUP_TIME_MS + "ms)");
        
        TestUtils.logTestInfo("Startup time test completed successfully. Measured time: " + startupTime + "ms");
    }
    
    /**
     * Tests that the average application startup time over multiple iterations is within acceptable limits
     */
    @RepeatedTest(TEST_ITERATIONS)
    @DisplayName("Average application startup time should be within acceptable limits")
    public void testAverageStartupTime() {
        TestUtils.logTestInfo("Starting average startup time test (iteration " + (testCount + 1) + " of " + TEST_ITERATIONS + ")");
        
        // Measure the startup time
        long startupTime = measureStartupTime();
        
        // Add to total for averaging
        totalStartupTime += startupTime;
        testCount++;
        
        // On the last iteration, calculate the average and assert
        if (testCount == TEST_ITERATIONS) {
            long averageStartupTime = totalStartupTime / TEST_ITERATIONS;
            
            Assertions.assertTrue(averageStartupTime <= PerformanceTestUtils.MAX_STARTUP_TIME_MS,
                    "Average application startup time (" + averageStartupTime + "ms) exceeds maximum allowed time (" 
                    + PerformanceTestUtils.MAX_STARTUP_TIME_MS + "ms)");
            
            TestUtils.logTestInfo("Average startup time test completed successfully. Average time: " 
                    + averageStartupTime + "ms over " + TEST_ITERATIONS + " iterations");
            
            // Reset counters for potential future runs
            totalStartupTime = 0;
            testCount = 0;
        }
    }
    
    /**
     * Measures the time it takes for the application to start up
     * 
     * @return The startup time in milliseconds
     */
    private long measureStartupTime() {
        TestUtils.logTestInfo("Measuring application startup time");
        
        // Record start time
        long startTimeNano = System.nanoTime();
        
        try {
            // Start the application in test mode
            CompoundInterestCalculatorApp.main(new String[]{STARTUP_TEST_ARGS});
        } catch (Exception e) {
            TestUtils.logTestError("Error occurred during application startup measurement", e);
            // Return a high value to indicate failure
            return Long.MAX_VALUE;
        }
        
        // Record end time
        long endTimeNano = System.nanoTime();
        
        // Calculate elapsed time
        long elapsedTimeNano = endTimeNano - startTimeNano;
        long elapsedTimeMs = convertNanosToMillis(elapsedTimeNano);
        
        TestUtils.logTestInfo("Application startup time measured: " + elapsedTimeMs + "ms");
        
        return elapsedTimeMs;
    }
    
    /**
     * Converts time in nanoseconds to milliseconds
     * 
     * @param nanos The time in nanoseconds
     * @return The time in milliseconds
     */
    private long convertNanosToMillis(long nanos) {
        return nanos / 1_000_000;
    }
}