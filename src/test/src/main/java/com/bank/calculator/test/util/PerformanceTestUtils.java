package com.bank.calculator.test.util;

import org.junit.jupiter.api.Assertions;  // 5.8.2
import java.math.BigDecimal;              // JDK 11
import java.util.logging.Logger;          // JDK 11
import java.util.logging.Level;           // JDK 11
import java.lang.System;                  // JDK 11
import java.lang.Runtime;                 // JDK 11
import java.io.File;                      // JDK 11
import java.io.IOException;               // JDK 11

import com.bank.calculator.service.CalculationService;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.test.util.TestUtils;

/**
 * Utility class that provides methods for measuring and asserting performance metrics 
 * in the Compound Interest Calculator application. This class contains functionality 
 * for timing operations, measuring memory usage, and validating that performance 
 * meets the specified thresholds.
 */
public class PerformanceTestUtils {

    private static final Logger LOGGER = Logger.getLogger(PerformanceTestUtils.class.getName());
    
    // Performance thresholds as per requirements
    public static final long MAX_CALCULATION_TIME_MS = 200;
    public static final long MAX_UI_RESPONSE_TIME_MS = 100;
    public static final long MAX_MEMORY_USAGE_MB = 50;
    public static final long MAX_STARTUP_TIME_MS = 3000;
    
    private static final String PERFORMANCE_THRESHOLDS_FILE = "test-data/performance-thresholds.json";
    
    /**
     * Measures the time taken to calculate EMI using the provided calculation service and input.
     *
     * @param calculationService The calculation service to test
     * @param input The calculation input
     * @return The time taken in milliseconds to perform the calculation
     */
    public static long measureCalculationTime(CalculationService calculationService, CalculationInput input) {
        TestUtils.logTestInfo("Measuring EMI calculation time for principal: " + input.getPrincipal() + 
                            ", duration: " + input.getDurationYears() + " years");
        
        long startTime = System.nanoTime();
        try {
            calculationService.calculateEMI(input.getPrincipal(), input.getDurationYears(), input.getInterestRate());
        } catch (Exception e) {
            TestUtils.logTestError("Error during EMI calculation performance measurement", e);
        }
        long endTime = System.nanoTime();
        
        long elapsedTimeMs = nanosToMillis(endTime - startTime);
        TestUtils.logTestInfo("EMI calculation time: " + elapsedTimeMs + " ms");
        
        return elapsedTimeMs;
    }
    
    /**
     * Measures the time taken to calculate compound interest using the provided calculation service and input.
     *
     * @param calculationService The calculation service to test
     * @param input The calculation input
     * @return The time taken in milliseconds to perform the calculation
     */
    public static long measureCompoundInterestCalculationTime(CalculationService calculationService, CalculationInput input) {
        TestUtils.logTestInfo("Measuring compound interest calculation time for principal: " + input.getPrincipal() + 
                            ", duration: " + input.getDurationYears() + " years");
        
        long startTime = System.nanoTime();
        try {
            calculationService.calculateCompoundInterest(input.getPrincipal(), input.getDurationYears(), input.getInterestRate());
        } catch (Exception e) {
            TestUtils.logTestError("Error during compound interest calculation performance measurement", e);
        }
        long endTime = System.nanoTime();
        
        long elapsedTimeMs = nanosToMillis(endTime - startTime);
        TestUtils.logTestInfo("Compound interest calculation time: " + elapsedTimeMs + " ms");
        
        return elapsedTimeMs;
    }
    
    /**
     * Measures the time taken for a UI operation to complete.
     *
     * @param uiOperation The UI operation to measure
     * @return The time taken in milliseconds to perform the UI operation
     */
    public static long measureUIResponseTime(Runnable uiOperation) {
        TestUtils.logTestInfo("Measuring UI response time");
        
        long startTime = System.nanoTime();
        try {
            uiOperation.run();
        } catch (Exception e) {
            TestUtils.logTestError("Error during UI response time measurement", e);
        }
        long endTime = System.nanoTime();
        
        long elapsedTimeMs = nanosToMillis(endTime - startTime);
        TestUtils.logTestInfo("UI response time: " + elapsedTimeMs + " ms");
        
        return elapsedTimeMs;
    }
    
    /**
     * Measures the memory usage during the execution of a specified operation.
     *
     * @param operation The operation to measure
     * @return The peak memory usage in megabytes during the operation
     */
    public static long measureMemoryUsage(Runnable operation) {
        TestUtils.logTestInfo("Measuring memory usage");
        
        // Run garbage collection to get a clean baseline
        runGarbageCollection();
        
        // Get initial memory usage
        long initialMemoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        // Run the operation
        try {
            operation.run();
        } catch (Exception e) {
            TestUtils.logTestError("Error during memory usage measurement", e);
        }
        
        // Get the final memory usage
        long finalMemoryUsage = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        
        // Calculate the difference
        long memoryUsageBytes = finalMemoryUsage - initialMemoryUsage;
        long memoryUsageMb = bytesToMegabytes(memoryUsageBytes);
        
        TestUtils.logTestInfo("Memory usage: " + memoryUsageMb + " MB");
        
        return memoryUsageMb;
    }
    
    /**
     * Asserts that the calculation time is within the acceptable threshold.
     *
     * @param calculationTimeMs The measured calculation time in milliseconds
     */
    public static void assertCalculationPerformance(long calculationTimeMs) {
        TestUtils.logTestInfo("Asserting calculation time: " + calculationTimeMs + " ms against threshold: " + 
                            MAX_CALCULATION_TIME_MS + " ms");
        
        Assertions.assertTrue(calculationTimeMs <= MAX_CALCULATION_TIME_MS, 
                           "Calculation time (" + calculationTimeMs + " ms) exceeds the threshold (" + 
                           MAX_CALCULATION_TIME_MS + " ms)");
    }
    
    /**
     * Asserts that the calculation time is within the specified threshold.
     *
     * @param calculationTimeMs The measured calculation time in milliseconds
     * @param thresholdMs The threshold in milliseconds
     */
    public static void assertCalculationPerformance(long calculationTimeMs, long thresholdMs) {
        TestUtils.logTestInfo("Asserting calculation time: " + calculationTimeMs + " ms against custom threshold: " + 
                            thresholdMs + " ms");
        
        Assertions.assertTrue(calculationTimeMs <= thresholdMs, 
                           "Calculation time (" + calculationTimeMs + " ms) exceeds the threshold (" + 
                           thresholdMs + " ms)");
    }
    
    /**
     * Asserts that the UI response time is within the acceptable threshold.
     *
     * @param responseTimeMs The measured UI response time in milliseconds
     */
    public static void assertUIResponsePerformance(long responseTimeMs) {
        TestUtils.logTestInfo("Asserting UI response time: " + responseTimeMs + " ms against threshold: " + 
                            MAX_UI_RESPONSE_TIME_MS + " ms");
        
        Assertions.assertTrue(responseTimeMs <= MAX_UI_RESPONSE_TIME_MS, 
                           "UI response time (" + responseTimeMs + " ms) exceeds the threshold (" + 
                           MAX_UI_RESPONSE_TIME_MS + " ms)");
    }
    
    /**
     * Asserts that the memory usage is within the acceptable threshold.
     *
     * @param memoryUsageMb The measured memory usage in megabytes
     */
    public static void assertMemoryUsage(long memoryUsageMb) {
        TestUtils.logTestInfo("Asserting memory usage: " + memoryUsageMb + " MB against threshold: " + 
                            MAX_MEMORY_USAGE_MB + " MB");
        
        Assertions.assertTrue(memoryUsageMb <= MAX_MEMORY_USAGE_MB, 
                           "Memory usage (" + memoryUsageMb + " MB) exceeds the threshold (" + 
                           MAX_MEMORY_USAGE_MB + " MB)");
    }
    
    /**
     * Runs garbage collection to clean up memory before or after performance tests.
     */
    public static void runGarbageCollection() {
        TestUtils.logTestInfo("Requesting garbage collection");
        
        // Request garbage collection
        System.gc();
        Runtime.getRuntime().gc();
        
        // Sleep a bit to allow GC to complete
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            TestUtils.logTestError("Interrupted while waiting for garbage collection", e);
        }
        
        TestUtils.logTestInfo("Garbage collection completed");
    }
    
    /**
     * Gets the amount of available memory in the JVM.
     *
     * @return The available memory in megabytes
     */
    public static long getAvailableMemory() {
        long availableBytes = Runtime.getRuntime().freeMemory();
        return bytesToMegabytes(availableBytes);
    }
    
    /**
     * Gets the total memory allocated to the JVM.
     *
     * @return The total memory in megabytes
     */
    public static long getTotalMemory() {
        long totalBytes = Runtime.getRuntime().totalMemory();
        return bytesToMegabytes(totalBytes);
    }
    
    /**
     * Gets the maximum memory that can be allocated to the JVM.
     *
     * @return The maximum memory in megabytes
     */
    public static long getMaxMemory() {
        long maxBytes = Runtime.getRuntime().maxMemory();
        return bytesToMegabytes(maxBytes);
    }
    
    /**
     * Loads performance thresholds from the configuration file.
     */
    public static void loadPerformanceThresholds() {
        TestUtils.logTestInfo("Loading performance thresholds from: " + PERFORMANCE_THRESHOLDS_FILE);
        
        try {
            File thresholdsFile = new File(PERFORMANCE_THRESHOLDS_FILE);
            if (thresholdsFile.exists()) {
                // In a real implementation, this would parse a JSON file and update the threshold constants
                // For example using Jackson or Gson library to parse JSON
                // Since we don't have those dependencies in the spec, just logging that we would do it
                
                TestUtils.logTestInfo("Performance thresholds file found - would parse and update thresholds");
                
                // Example of how this would work with pseudocode:
                // JsonObject json = parseJsonFile(thresholdsFile);
                // MAX_CALCULATION_TIME_MS = json.getLong("maxCalculationTimeMs");
                // MAX_UI_RESPONSE_TIME_MS = json.getLong("maxUiResponseTimeMs");
                // MAX_MEMORY_USAGE_MB = json.getLong("maxMemoryUsageMb");
                // MAX_STARTUP_TIME_MS = json.getLong("maxStartupTimeMs");
            } else {
                TestUtils.logTestInfo("Performance thresholds file not found, using default values");
            }
        } catch (Exception e) {
            TestUtils.logTestError("Failed to load performance thresholds", e);
        }
        
        // Log the current thresholds (whether defaults or loaded from file)
        TestUtils.logTestInfo("Performance thresholds: " +
                            "Calculation=" + MAX_CALCULATION_TIME_MS + "ms, " +
                            "UI=" + MAX_UI_RESPONSE_TIME_MS + "ms, " +
                            "Memory=" + MAX_MEMORY_USAGE_MB + "MB, " +
                            "Startup=" + MAX_STARTUP_TIME_MS + "ms");
    }
    
    /**
     * Converts bytes to megabytes.
     *
     * @param bytes The value in bytes
     * @return The equivalent value in megabytes
     */
    private static long bytesToMegabytes(long bytes) {
        return bytes / 1024 / 1024;
    }
    
    /**
     * Converts nanoseconds to milliseconds.
     *
     * @param nanos The value in nanoseconds
     * @return The equivalent value in milliseconds
     */
    private static long nanosToMillis(long nanos) {
        return nanos / 1_000_000;
    }
}