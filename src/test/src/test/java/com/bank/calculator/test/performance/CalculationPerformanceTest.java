package com.bank.calculator.test.performance;

import org.junit.jupiter.api.Test; // 5.8.2
import org.junit.jupiter.api.BeforeAll; // 5.8.2
import org.junit.jupiter.api.BeforeEach; // 5.8.2
import org.junit.jupiter.api.AfterEach; // 5.8.2
import org.junit.jupiter.api.AfterAll; // 5.8.2
import org.junit.jupiter.api.DisplayName; // 5.8.2
import org.junit.jupiter.api.Assertions; // 5.8.2

import java.math.BigDecimal; // JDK 11
import java.util.List; // JDK 11
import java.util.logging.Logger; // JDK 11
import java.util.logging.Level; // JDK 11

import com.bank.calculator.test.category.PerformanceTest;
import com.bank.calculator.test.util.PerformanceTestUtils;
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.service.impl.CalculationServiceImpl;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.test.fixture.CalculationTestFixture;

/**
 * Test class for verifying the performance of the calculation functionality in the
 * Compound Interest Calculator application.
 */
@PerformanceTest
public class CalculationPerformanceTest {

    private static final Logger LOGGER = Logger.getLogger(CalculationPerformanceTest.class.getName());
    private static final CalculationService calculationService = new CalculationServiceImpl();
    private static final int DEFAULT_ITERATIONS = 20;
    private static final int WARMUP_ITERATIONS = 5;
    private static final BigDecimal STANDARD_PRINCIPAL = new BigDecimal("10000.00");
    private static final BigDecimal LARGE_PRINCIPAL = new BigDecimal("1000000.00");
    private static final int STANDARD_DURATION = 5;
    private static final int LONG_DURATION = 30;
    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("7.5");

    /**
     * Initializes the test class by loading performance thresholds from configuration.
     */
    @BeforeAll
    static void setupClass() {
        LOGGER.log(Level.INFO, "Initializing performance test class");
        PerformanceTestUtils.loadPerformanceThresholds();
        LOGGER.log(Level.INFO, "Performance thresholds loaded");
    }

    /**
     * Prepares the environment before each test by running garbage collection.
     */
    @BeforeEach
    void setupTest() {
        LOGGER.log(Level.INFO, "Starting performance test");
        PerformanceTestUtils.runGarbageCollection();
        LOGGER.log(Level.INFO, "Test environment prepared");
    }

    /**
     * Cleans up after each test by running garbage collection.
     */
    @AfterEach
    void tearDownTest() {
        LOGGER.log(Level.INFO, "Completed performance test");
        PerformanceTestUtils.runGarbageCollection();
        LOGGER.log(Level.INFO, "Test environment cleaned up");
    }

    /**
     * Tests the performance of a standard EMI calculation with typical input values.
     */
    @Test
    @DisplayName("Standard EMI calculation performance test")
    void testStandardCalculationPerformance() {
        LOGGER.log(Level.INFO, "Starting standard calculation performance test");
        
        // Create calculation input with standard values
        CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, STANDARD_DURATION);
        input.setInterestRate(DEFAULT_INTEREST_RATE);
        
        // Perform warmup iterations
        performWarmupIterations(input, WARMUP_ITERATIONS);
        
        // Measure calculation time
        long calculationTimeMs = PerformanceTestUtils.measureCalculationTime(calculationService, input);
        
        // Assert that calculation time is within acceptable threshold
        PerformanceTestUtils.assertCalculationPerformance(calculationTimeMs);
        
        LOGGER.log(Level.INFO, "Standard calculation completed in {0}ms", calculationTimeMs);
    }

    /**
     * Tests the performance of an EMI calculation with a large principal amount.
     */
    @Test
    @DisplayName("Large value EMI calculation performance test")
    void testLargeValueCalculationPerformance() {
        LOGGER.log(Level.INFO, "Starting large value calculation performance test");
        
        // Create calculation input with large principal amount
        CalculationInput input = new CalculationInput(LARGE_PRINCIPAL, STANDARD_DURATION);
        input.setInterestRate(DEFAULT_INTEREST_RATE);
        
        // Perform warmup iterations
        performWarmupIterations(input, WARMUP_ITERATIONS);
        
        // Measure calculation time
        long calculationTimeMs = PerformanceTestUtils.measureCalculationTime(calculationService, input);
        
        // Assert that calculation time is within acceptable threshold
        PerformanceTestUtils.assertCalculationPerformance(calculationTimeMs);
        
        LOGGER.log(Level.INFO, "Large value calculation completed in {0}ms", calculationTimeMs);
    }

    /**
     * Tests the performance of an EMI calculation with a long loan duration.
     */
    @Test
    @DisplayName("Long duration EMI calculation performance test")
    void testLongDurationCalculationPerformance() {
        LOGGER.log(Level.INFO, "Starting long duration calculation performance test");
        
        // Create calculation input with long duration
        CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, LONG_DURATION);
        input.setInterestRate(DEFAULT_INTEREST_RATE);
        
        // Perform warmup iterations
        performWarmupIterations(input, WARMUP_ITERATIONS);
        
        // Measure calculation time
        long calculationTimeMs = PerformanceTestUtils.measureCalculationTime(calculationService, input);
        
        // Assert that calculation time is within acceptable threshold
        PerformanceTestUtils.assertCalculationPerformance(calculationTimeMs);
        
        LOGGER.log(Level.INFO, "Long duration calculation completed in {0}ms", calculationTimeMs);
    }

    /**
     * Tests the performance of a compound interest calculation.
     */
    @Test
    @DisplayName("Compound interest calculation performance test")
    void testCompoundInterestCalculationPerformance() {
        LOGGER.log(Level.INFO, "Starting compound interest calculation performance test");
        
        // Create calculation input with standard values
        CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, STANDARD_DURATION);
        input.setInterestRate(DEFAULT_INTEREST_RATE);
        
        // Perform warmup iterations
        performWarmupIterations(input, WARMUP_ITERATIONS);
        
        // Measure compound interest calculation time
        long calculationTimeMs = PerformanceTestUtils.measureCompoundInterestCalculationTime(calculationService, input);
        
        // Assert that calculation time is within acceptable threshold
        PerformanceTestUtils.assertCalculationPerformance(calculationTimeMs);
        
        LOGGER.log(Level.INFO, "Compound interest calculation completed in {0}ms", calculationTimeMs);
    }

    /**
     * Tests the performance of multiple consecutive EMI calculations.
     */
    @Test
    @DisplayName("Multiple consecutive EMI calculations performance test")
    void testMultipleConsecutiveCalculations() {
        LOGGER.log(Level.INFO, "Starting multiple consecutive calculations performance test");
        
        // Create calculation input with standard values
        CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, STANDARD_DURATION);
        input.setInterestRate(DEFAULT_INTEREST_RATE);
        
        // Perform warmup iterations
        performWarmupIterations(input, WARMUP_ITERATIONS);
        
        // Start measuring time
        long startTime = System.currentTimeMillis();
        
        // Perform multiple calculations
        for (int i = 0; i < DEFAULT_ITERATIONS; i++) {
            calculationService.calculateEMI(input.getPrincipal(), input.getDurationYears(), input.getInterestRate());
        }
        
        // Calculate total time and average
        long endTime = System.currentTimeMillis();
        long totalTime = endTime - startTime;
        double averageTime = (double) totalTime / DEFAULT_ITERATIONS;
        
        // Assert that average calculation time is within acceptable threshold
        Assertions.assertTrue(averageTime <= PerformanceTestUtils.MAX_CALCULATION_TIME_MS,
                "Average calculation time (" + averageTime + "ms) exceeds the threshold (" + 
                PerformanceTestUtils.MAX_CALCULATION_TIME_MS + "ms)");
        
        LOGGER.log(Level.INFO, "Multiple calculations completed in {0}ms, average: {1}ms per calculation", 
                new Object[]{totalTime, averageTime});
    }

    /**
     * Tests the performance of calculations using predefined test cases from the test fixture.
     */
    @Test
    @DisplayName("Predefined performance test cases")
    void testPredefinedPerformanceTestCases() {
        LOGGER.log(Level.INFO, "Starting predefined performance test cases test");
        
        // Load predefined performance test cases
        List<CalculationTestFixture.PerformanceTestCase> testCases = CalculationTestFixture.createPerformanceTestCases();
        
        // Execute each test case
        for (CalculationTestFixture.PerformanceTestCase testCase : testCases) {
            LOGGER.log(Level.INFO, "Executing performance test case: {0}", testCase.getName());
            
            // Execute the performance test case and get the result
            CalculationTestFixture.PerformanceResult result = 
                    CalculationTestFixture.executePerformanceTestCase(testCase);
            
            // Assert that the test case passed
            Assertions.assertTrue(result.isPassed(), 
                    "Performance test case " + testCase.getId() + " failed: " + 
                    "Average execution time (" + result.getAverageExecutionTimeMs() + "ms) exceeds " + 
                    "maximum allowed time (" + testCase.getMaxExecutionTimeMs() + "ms)");
            
            LOGGER.log(Level.INFO, "Test case {0} passed with average execution time: {1}ms", 
                    new Object[]{testCase.getId(), result.getAverageExecutionTimeMs()});
        }
    }

    /**
     * Tests the memory usage during EMI calculations.
     */
    @Test
    @DisplayName("EMI calculation memory usage test")
    void testCalculationMemoryUsage() {
        LOGGER.log(Level.INFO, "Starting calculation memory usage test");
        
        // Create calculation input with standard values
        final CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, STANDARD_DURATION);
        input.setInterestRate(DEFAULT_INTEREST_RATE);
        
        // Measure memory usage during calculation
        long memoryUsageMb = PerformanceTestUtils.measureMemoryUsage(() -> {
            for (int i = 0; i < DEFAULT_ITERATIONS; i++) {
                calculationService.calculateEMI(input.getPrincipal(), input.getDurationYears(), input.getInterestRate());
            }
        });
        
        // Assert that memory usage is within acceptable threshold
        PerformanceTestUtils.assertMemoryUsage(memoryUsageMb);
        
        LOGGER.log(Level.INFO, "Calculation memory usage: {0}MB", memoryUsageMb);
    }

    /**
     * Performs warmup iterations to stabilize JVM performance before measuring.
     * 
     * @param input The calculation input to use
     * @param iterations The number of warmup iterations to perform
     */
    private void performWarmupIterations(CalculationInput input, int iterations) {
        LOGGER.log(Level.INFO, "Performing {0} warmup iterations", iterations);
        
        for (int i = 0; i < iterations; i++) {
            calculationService.calculateEMI(input.getPrincipal(), input.getDurationYears(), input.getInterestRate());
        }
        
        LOGGER.log(Level.INFO, "Warmup iterations completed");
    }
}