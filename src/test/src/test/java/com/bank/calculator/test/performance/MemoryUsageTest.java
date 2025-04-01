package com.bank.calculator.test.performance;

import org.junit.jupiter.api.Test; // 5.8.2
import org.junit.jupiter.api.BeforeAll; // 5.8.2
import org.junit.jupiter.api.BeforeEach; // 5.8.2
import org.junit.jupiter.api.AfterEach; // 5.8.2
import org.junit.jupiter.api.DisplayName; // 5.8.2
import org.junit.jupiter.api.Assertions; // 5.8.2
import java.math.BigDecimal; // JDK 11
import java.util.logging.Logger; // JDK 11
import java.util.logging.Level; // JDK 11
import java.util.ArrayList; // JDK 11
import java.util.List; // JDK 11

import com.bank.calculator.test.category.PerformanceTest;
import com.bank.calculator.test.util.PerformanceTestUtils;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.service.impl.CalculationServiceImpl;
import com.bank.calculator.model.CalculationInput;

/**
 * Performance test class that verifies the memory usage of the Compound Interest Calculator application.
 * This class tests the memory consumption during various operations to ensure it meets the
 * performance requirements specified in the technical specifications.
 */
@PerformanceTest
public class MemoryUsageTest {

    private static final Logger LOGGER = Logger.getLogger(MemoryUsageTest.class.getName());
    private static final CalculationService calculationService = new CalculationServiceImpl();
    
    // Test data constants
    private static final BigDecimal STANDARD_PRINCIPAL = new BigDecimal("10000.00");
    private static final BigDecimal LARGE_PRINCIPAL = new BigDecimal("1000000.00");
    private static final int STANDARD_DURATION = 5;
    private static final int LONG_DURATION = 30;
    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("7.5");
    private static final int ITERATIONS_FOR_MEMORY_TEST = 1000;
    
    /**
     * Initializes the test class by loading performance thresholds from configuration.
     */
    @BeforeAll
    static void setupClass() {
        TestUtils.logTestInfo("Initializing memory usage test class");
        PerformanceTestUtils.loadPerformanceThresholds();
        TestUtils.logTestInfo("Memory threshold: " + PerformanceTestUtils.MAX_MEMORY_USAGE_MB + " MB");
        TestUtils.logTestInfo("JVM memory configuration - Max: " + PerformanceTestUtils.getMaxMemory() + 
                            " MB, Total: " + PerformanceTestUtils.getTotalMemory() + 
                            " MB, Available: " + PerformanceTestUtils.getAvailableMemory() + " MB");
    }
    
    /**
     * Prepares the environment before each test by running garbage collection.
     */
    @BeforeEach
    void setupTest() {
        TestUtils.logTestInfo("Starting memory usage test");
        PerformanceTestUtils.runGarbageCollection();
        TestUtils.logTestInfo("Available memory before test: " + PerformanceTestUtils.getAvailableMemory() + " MB");
        TestUtils.logTestInfo("Test environment prepared");
    }
    
    /**
     * Cleans up after each test by running garbage collection.
     */
    @AfterEach
    void tearDownTest() {
        TestUtils.logTestInfo("Memory usage test completed");
        PerformanceTestUtils.runGarbageCollection();
        TestUtils.logTestInfo("Available memory after test: " + PerformanceTestUtils.getAvailableMemory() + " MB");
        TestUtils.logTestInfo("Test environment cleaned up");
    }
    
    /**
     * Tests the memory usage during a standard EMI calculation with typical input values.
     */
    @Test
    @DisplayName("Standard EMI calculation memory usage test")
    void testStandardCalculationMemoryUsage() {
        TestUtils.logTestInfo("Starting standard calculation memory usage test");
        
        CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, STANDARD_DURATION);
        input.setInterestRate(DEFAULT_INTEREST_RATE);
        
        long memoryUsage = PerformanceTestUtils.measureMemoryUsage(() -> {
            calculationService.calculateEMI(input);
        });
        
        PerformanceTestUtils.assertMemoryUsage(memoryUsage);
        TestUtils.logTestInfo("Standard calculation memory usage: " + memoryUsage + " MB");
    }
    
    /**
     * Tests the memory usage during an EMI calculation with a large principal amount.
     */
    @Test
    @DisplayName("Large value EMI calculation memory usage test")
    void testLargeValueCalculationMemoryUsage() {
        TestUtils.logTestInfo("Starting large value calculation memory usage test");
        
        CalculationInput input = new CalculationInput(LARGE_PRINCIPAL, STANDARD_DURATION);
        input.setInterestRate(DEFAULT_INTEREST_RATE);
        
        long memoryUsage = PerformanceTestUtils.measureMemoryUsage(() -> {
            calculationService.calculateEMI(input);
        });
        
        PerformanceTestUtils.assertMemoryUsage(memoryUsage);
        TestUtils.logTestInfo("Large value calculation memory usage: " + memoryUsage + " MB");
    }
    
    /**
     * Tests the memory usage during an EMI calculation with a long loan duration.
     */
    @Test
    @DisplayName("Long duration EMI calculation memory usage test")
    void testLongDurationCalculationMemoryUsage() {
        TestUtils.logTestInfo("Starting long duration calculation memory usage test");
        
        CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, LONG_DURATION);
        input.setInterestRate(DEFAULT_INTEREST_RATE);
        
        long memoryUsage = PerformanceTestUtils.measureMemoryUsage(() -> {
            calculationService.calculateEMI(input);
        });
        
        PerformanceTestUtils.assertMemoryUsage(memoryUsage);
        TestUtils.logTestInfo("Long duration calculation memory usage: " + memoryUsage + " MB");
    }
    
    /**
     * Tests the memory usage during multiple consecutive EMI calculations.
     */
    @Test
    @DisplayName("Multiple consecutive EMI calculations memory usage test")
    void testMultipleConsecutiveCalculationsMemoryUsage() {
        TestUtils.logTestInfo("Starting multiple consecutive calculations memory usage test");
        
        CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, STANDARD_DURATION);
        input.setInterestRate(DEFAULT_INTEREST_RATE);
        
        long memoryUsage = PerformanceTestUtils.measureMemoryUsage(() -> {
            performMultipleCalculations(input, 100);
        });
        
        PerformanceTestUtils.assertMemoryUsage(memoryUsage);
        TestUtils.logTestInfo("Multiple consecutive calculations memory usage: " + memoryUsage + " MB");
    }
    
    /**
     * Tests for potential memory leaks by performing repeated calculations and monitoring memory usage.
     */
    @Test
    @DisplayName("Memory leak detection test")
    void testMemoryLeakDetection() {
        TestUtils.logTestInfo("Starting memory leak detection test");
        
        CalculationInput input = new CalculationInput(STANDARD_PRINCIPAL, STANDARD_DURATION);
        input.setInterestRate(DEFAULT_INTEREST_RATE);
        
        // Record initial memory usage
        PerformanceTestUtils.runGarbageCollection();
        long initialMemoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        // Perform a large number of calculations
        performMultipleCalculations(input, ITERATIONS_FOR_MEMORY_TEST);
        
        // Force garbage collection and measure memory again
        PerformanceTestUtils.runGarbageCollection();
        long finalMemoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        // Calculate and check the difference
        long memoryDifferenceBytes = finalMemoryUsage - initialMemoryUsage;
        long memoryDifferenceMb = memoryDifferenceBytes / 1024 / 1024;
        
        // If there's a significant memory increase after GC, it might indicate a memory leak
        TestUtils.logTestInfo("Memory usage before test: " + (initialMemoryUsage / 1024 / 1024) + " MB");
        TestUtils.logTestInfo("Memory usage after test: " + (finalMemoryUsage / 1024 / 1024) + " MB");
        TestUtils.logTestInfo("Memory difference: " + memoryDifferenceMb + " MB");
        
        // Assert that memory difference is within acceptable range
        Assertions.assertTrue(memoryDifferenceMb < 10, 
                "Memory difference after multiple calculations is too large: " + memoryDifferenceMb + " MB");
    }
    
    /**
     * Tests the memory usage when creating and manipulating collections of calculation results.
     */
    @Test
    @DisplayName("Collection memory usage test")
    void testCollectionMemoryUsage() {
        TestUtils.logTestInfo("Starting collection memory usage test");
        
        long memoryUsage = PerformanceTestUtils.measureMemoryUsage(() -> {
            List<BigDecimal> results = createCalculationResultsList(50);
            // Process the results to ensure they aren't optimized away
            BigDecimal total = results.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Assertions.assertTrue(total.compareTo(BigDecimal.ZERO) > 0);
        });
        
        PerformanceTestUtils.assertMemoryUsage(memoryUsage);
        TestUtils.logTestInfo("Collection memory usage: " + memoryUsage + " MB");
    }
    
    /**
     * Tests the peak memory usage during intensive calculation operations.
     */
    @Test
    @DisplayName("Peak memory usage test")
    void testPeakMemoryUsage() {
        TestUtils.logTestInfo("Starting peak memory usage test");
        
        // Create a scenario that generates peak memory usage
        long memoryUsage = PerformanceTestUtils.measureMemoryUsage(() -> {
            List<CalculationInput> inputs = new ArrayList<>();
            
            // Create multiple calculation inputs with varying parameters
            for (int i = 0; i < 100; i++) {
                BigDecimal principal = new BigDecimal(10000 + (i * 1000));
                int duration = 5 + (i % 25);
                CalculationInput input = new CalculationInput(principal, duration);
                input.setInterestRate(DEFAULT_INTEREST_RATE);
                inputs.add(input);
            }
            
            // Perform calculations for all inputs
            List<BigDecimal> results = new ArrayList<>();
            for (CalculationInput input : inputs) {
                results.add(calculationService.calculateEMI(input).getEmiAmount());
            }
            
            // Process the results to ensure they aren't optimized away
            BigDecimal total = results.stream()
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
            Assertions.assertTrue(total.compareTo(BigDecimal.ZERO) > 0);
        });
        
        PerformanceTestUtils.assertMemoryUsage(memoryUsage);
        TestUtils.logTestInfo("Peak memory usage: " + memoryUsage + " MB");
    }
    
    /**
     * Helper method to perform multiple calculations for memory usage testing.
     *
     * @param input The calculation input to use
     * @param iterations The number of calculations to perform
     */
    private void performMultipleCalculations(CalculationInput input, int iterations) {
        TestUtils.logTestInfo("Performing " + iterations + " calculations");
        
        for (int i = 0; i < iterations; i++) {
            calculationService.calculateEMI(input);
        }
        
        TestUtils.logTestInfo("Completed " + iterations + " calculations");
    }
    
    /**
     * Helper method to create a list of calculation results for memory testing.
     *
     * @param size The number of results to create
     * @return A list of calculation results
     */
    private List<BigDecimal> createCalculationResultsList(int size) {
        List<BigDecimal> results = new ArrayList<>(size);
        
        for (int i = 0; i < size; i++) {
            BigDecimal principal = new BigDecimal(5000 + (i * 500));
            int duration = 3 + (i % 10);
            CalculationInput input = new CalculationInput(principal, duration);
            input.setInterestRate(DEFAULT_INTEREST_RATE);
            
            results.add(calculationService.calculateEMI(input).getEmiAmount());
        }
        
        return results;
    }
}