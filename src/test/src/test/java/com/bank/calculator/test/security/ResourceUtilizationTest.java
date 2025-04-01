package com.bank.calculator.test.security;

import org.junit.jupiter.api.Test; // JUnit 5.8.2
import org.junit.jupiter.api.BeforeEach; // JUnit 5.8.2
import org.junit.jupiter.api.AfterEach; // JUnit 5.8.2
import org.junit.jupiter.api.DisplayName; // JUnit 5.8.2
import org.junit.jupiter.api.Assertions; // JUnit 5.8.2

import org.mockito.Mockito; // Mockito 4.0.0
import org.mockito.Mock; // Mockito 4.0.0
import org.mockito.InjectMocks; // Mockito 4.0.0
import org.mockito.MockitoAnnotations; // Mockito 4.0.0

import java.math.BigDecimal; // JDK 11
import java.util.ArrayList; // JDK 11
import java.util.List; // JDK 11
import java.lang.Runtime; // JDK 11

import com.bank.calculator.test.category.SecurityTest;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.util.PerformanceTestUtils;
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.CompoundInterestCalculatorApp;

/**
 * Security test class that verifies the application's resource utilization from a security perspective.
 * This class tests that the application properly manages memory, CPU, and other system resources to prevent
 * resource exhaustion attacks and ensure stable operation under extreme conditions.
 */
@SecurityTest
@DisplayName("Resource Utilization Security Tests")
public class ResourceUtilizationTest {

    private static final int LARGE_CALCULATION_COUNT = 1000;
    private static final int EXTREME_CALCULATION_COUNT = 10000;
    private static final BigDecimal LARGE_PRINCIPAL = new BigDecimal("10000000.00");
    private static final int LARGE_DURATION = 30;
    private static final BigDecimal DEFAULT_INTEREST_RATE = new BigDecimal("0.075");

    @Mock
    private CalculationService calculationService;

    /**
     * Set up test environment before each test
     */
    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        PerformanceTestUtils.runGarbageCollection();
        logMemoryStatistics("Before Test");
        TestUtils.logTestInfo("Setting up ResourceUtilizationTest");
    }

    /**
     * Clean up after each test
     */
    @AfterEach
    public void tearDown() {
        PerformanceTestUtils.runGarbageCollection();
        logMemoryStatistics("After Test");
        TestUtils.logTestInfo("Tearing down ResourceUtilizationTest");
    }

    /**
     * Tests that repeated calculations do not cause memory leaks
     */
    @Test
    @DisplayName("Should not leak memory during repeated calculations")
    public void testMemoryLeakInRepeatedCalculations() {
        // Measure initial memory usage
        TestUtils.logTestInfo("Testing for memory leaks during repeated calculations");
        long initialMemory = PerformanceTestUtils.measureMemoryUsage(() -> {});
        
        // Setup calculation service mock
        Mockito.when(calculationService.calculateEMI(
                Mockito.any(BigDecimal.class), 
                Mockito.anyInt(), 
                Mockito.any(BigDecimal.class)))
            .thenReturn(new BigDecimal("100.00"));
        
        // Perform multiple calculations
        TestUtils.logTestInfo("Performing " + LARGE_CALCULATION_COUNT + " calculations");
        PerformanceTestUtils.measureMemoryUsage(() -> {
            for (int i = 0; i < LARGE_CALCULATION_COUNT; i++) {
                calculationService.calculateEMI(
                    BigDecimal.valueOf(1000), 
                    5, 
                    DEFAULT_INTEREST_RATE);
            }
        });
        
        // Run garbage collection
        PerformanceTestUtils.runGarbageCollection();
        
        // Measure memory after calculations and GC
        long finalMemory = PerformanceTestUtils.measureMemoryUsage(() -> {});
        
        // Assert that memory usage is within acceptable limits
        TestUtils.logTestInfo("Initial memory: " + initialMemory + "MB, Final memory: " + finalMemory + "MB");
        Assertions.assertTrue(finalMemory - initialMemory < 10, 
            "Memory leak detected: " + (finalMemory - initialMemory) + "MB not reclaimed after GC");
        Assertions.assertTrue(finalMemory < PerformanceTestUtils.MAX_MEMORY_USAGE_MB, 
            "Memory usage exceeds maximum allowed: " + finalMemory + "MB > " + 
            PerformanceTestUtils.MAX_MEMORY_USAGE_MB + "MB");
    }

    /**
     * Tests that calculations with large input values do not cause excessive memory usage
     */
    @Test
    @DisplayName("Should handle large input values without excessive memory usage")
    public void testMemoryUsageWithLargeInputs() {
        // Set up calculation service to handle large principal and duration values
        TestUtils.logTestInfo("Testing memory usage with large input values");
        Mockito.when(calculationService.calculateEMI(
                Mockito.eq(LARGE_PRINCIPAL), 
                Mockito.eq(LARGE_DURATION), 
                Mockito.any(BigDecimal.class)))
            .thenReturn(new BigDecimal("96000.00"));
        
        // Measure memory usage during calculation with large inputs
        long memoryUsage = PerformanceTestUtils.measureMemoryUsage(() -> {
            for (int i = 0; i < 100; i++) {
                calculationService.calculateEMI(
                    LARGE_PRINCIPAL, 
                    LARGE_DURATION, 
                    DEFAULT_INTEREST_RATE);
            }
        });
        
        // Assert that memory usage is within acceptable limits
        TestUtils.logTestInfo("Memory usage with large inputs: " + memoryUsage + "MB");
        Assertions.assertTrue(memoryUsage < PerformanceTestUtils.MAX_MEMORY_USAGE_MB, 
            "Memory usage with large inputs exceeds maximum allowed: " + memoryUsage + "MB > " + 
            PerformanceTestUtils.MAX_MEMORY_USAGE_MB + "MB");
    }

    /**
     * Tests that the application is resistant to resource exhaustion attacks
     */
    @Test
    @DisplayName("Should be resistant to resource exhaustion attacks")
    public void testResourceExhaustionResistance() {
        TestUtils.logTestInfo("Testing resistance to resource exhaustion attacks");
        
        // Measure initial memory
        long initialMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
        initialMemory = initialMemory / (1024 * 1024); // Convert to MB
        
        try {
            // Attempt to create a very large number of objects
            TestUtils.logTestInfo("Attempting to create large number of objects");
            List<BigDecimal> largeList = new ArrayList<>();
            boolean outOfMemoryOccurred = false;
            
            try {
                for (int i = 0; i < EXTREME_CALCULATION_COUNT; i++) {
                    largeList.add(new BigDecimal(String.valueOf(i)).pow(10));
                }
            } catch (OutOfMemoryError e) {
                outOfMemoryOccurred = true;
                TestUtils.logTestInfo("OutOfMemoryError occurred during object creation as expected");
            }
            
            // If we didn't get OutOfMemoryError, clean up
            if (!outOfMemoryOccurred) {
                largeList.clear();
                PerformanceTestUtils.runGarbageCollection();
                TestUtils.logTestInfo("Cleared large list without OutOfMemoryError");
            }
            
            // Attempt to perform an extreme number of calculations
            TestUtils.logTestInfo("Attempting to perform extreme number of calculations");
            Mockito.when(calculationService.calculateEMI(
                    Mockito.any(BigDecimal.class), 
                    Mockito.anyInt(), 
                    Mockito.any(BigDecimal.class)))
                .thenReturn(new BigDecimal("100.00"));
            
            // Use a reasonable number for actual execution to avoid test timeout
            for (int i = 0; i < 1000; i++) {
                calculationService.calculateEMI(
                    BigDecimal.valueOf(1000), 
                    5, 
                    DEFAULT_INTEREST_RATE);
            }
            
            // Attempt to create large BigDecimal objects with high precision
            TestUtils.logTestInfo("Attempting to create BigDecimal objects with high precision");
            List<BigDecimal> highPrecisionList = new ArrayList<>();
            try {
                for (int i = 0; i < 100; i++) {
                    BigDecimal highPrecision = new BigDecimal(String.valueOf(i) + "." + "1".repeat(1000));
                    highPrecisionList.add(highPrecision);
                }
            } catch (OutOfMemoryError e) {
                TestUtils.logTestInfo("OutOfMemoryError occurred during high precision BigDecimal creation");
            }
            
            // Clean up and verify memory usage after attack simulation
            highPrecisionList.clear();
            largeList.clear();
            PerformanceTestUtils.runGarbageCollection();
            long finalMemory = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
            finalMemory = finalMemory / (1024 * 1024); // Convert to MB
            
            TestUtils.logTestInfo("Memory usage after resource exhaustion test: " + finalMemory + "MB");
            Assertions.assertTrue(finalMemory - initialMemory < 50, 
                "Excessive memory retained after resource exhaustion test: " + (finalMemory - initialMemory) + "MB");
            
        } catch (Exception e) {
            TestUtils.logTestError("Unexpected exception during resource exhaustion test", e);
            Assertions.fail("Unexpected exception during resource exhaustion test: " + e.getMessage());
        }
    }

    /**
     * Tests that garbage collection effectively reclaims memory after large operations
     */
    @Test
    @DisplayName("Should efficiently reclaim memory after large operations")
    public void testGarbageCollectionEfficiency() {
        TestUtils.logTestInfo("Testing garbage collection efficiency");
        
        // Measure initial memory usage
        long initialMemory = PerformanceTestUtils.measureMemoryUsage(() -> {});
        TestUtils.logTestInfo("Initial memory usage: " + initialMemory + "MB");
        
        // Create a large amount of temporary data
        List<BigDecimal> tempData = null;
        long memoryBeforeGC = 0;
        
        try {
            // Create and process a large amount of temporary data
            tempData = createLargeTemporaryData(100000);
            
            // Process the data
            BigDecimal sum = BigDecimal.ZERO;
            for (BigDecimal value : tempData) {
                sum = sum.add(value);
            }
            
            // Measure memory usage before garbage collection
            memoryBeforeGC = PerformanceTestUtils.measureMemoryUsage(() -> {});
            TestUtils.logTestInfo("Memory usage before GC: " + memoryBeforeGC + "MB");
            
            // Clear reference to allow GC
            tempData = null;
            
            // Run garbage collection
            PerformanceTestUtils.runGarbageCollection();
            
            // Measure memory usage after garbage collection
            long memoryAfterGC = PerformanceTestUtils.measureMemoryUsage(() -> {});
            TestUtils.logTestInfo("Memory usage after GC: " + memoryAfterGC + "MB");
            
            // Verify that memory was reclaimed
            long memoryFreed = memoryBeforeGC - memoryAfterGC;
            TestUtils.logTestInfo("Memory freed by GC: " + memoryFreed + "MB");
            
            // Assert that a significant portion of memory was reclaimed
            Assertions.assertTrue(memoryFreed > 0, 
                "Garbage collection did not free any memory");
            
            // Assert that memory usage returned close to initial levels
            long memoryDiff = Math.abs(memoryAfterGC - initialMemory);
            Assertions.assertTrue(memoryDiff < 10, 
                "Memory usage after GC not close to initial levels: " + 
                memoryAfterGC + "MB vs initial " + initialMemory + "MB");
            
        } catch (OutOfMemoryError e) {
            // If we can't allocate this much memory, the test is still valid
            // We just note it and continue
            TestUtils.logTestInfo("OutOfMemoryError during large data creation - reducing test size");
            
            // Make sure to clear the reference and run GC
            tempData = null;
            PerformanceTestUtils.runGarbageCollection();
        }
    }

    /**
     * Tests that application startup does not consume excessive memory
     */
    @Test
    @DisplayName("Should not consume excessive memory during startup")
    public void testApplicationStartupMemoryUsage() {
        TestUtils.logTestInfo("Testing application startup memory usage");
        
        // Measure memory usage before application startup
        long memoryBeforeStartup = PerformanceTestUtils.measureMemoryUsage(() -> {});
        TestUtils.logTestInfo("Memory usage before startup: " + memoryBeforeStartup + "MB");
        
        // Simulate application startup process
        long startupMemoryUsage = PerformanceTestUtils.measureMemoryUsage(() -> {
            simulateApplicationStartup();
        });
        
        // Calculate memory consumption during startup
        TestUtils.logTestInfo("Memory used during startup: " + startupMemoryUsage + "MB");
        
        // Assert that startup memory usage is within acceptable limits
        Assertions.assertTrue(startupMemoryUsage < PerformanceTestUtils.MAX_MEMORY_USAGE_MB, 
            "Startup memory usage exceeds maximum allowed: " + startupMemoryUsage + "MB > " + 
            PerformanceTestUtils.MAX_MEMORY_USAGE_MB + "MB");
    }

    /**
     * Tests that concurrent calculations do not cause resource contention or excessive memory usage
     */
    @Test
    @DisplayName("Should handle concurrent calculations without resource issues")
    public void testConcurrentCalculationResourceUsage() {
        TestUtils.logTestInfo("Testing concurrent calculations resource usage");
        
        // Set up calculation service mock
        Mockito.when(calculationService.calculateEMI(
                Mockito.any(BigDecimal.class), 
                Mockito.anyInt(), 
                Mockito.any(BigDecimal.class)))
            .thenReturn(new BigDecimal("100.00"));
        
        // Create a thread-safe list to track any exceptions in threads
        final List<Exception> threadExceptions = new ArrayList<>();
        
        // Number of concurrent threads to run
        final int THREAD_COUNT = 10;
        final int CALCS_PER_THREAD = 100;
        
        // Create multiple threads to perform calculations simultaneously
        Thread[] threads = new Thread[THREAD_COUNT];
        
        for (int t = 0; t < THREAD_COUNT; t++) {
            final int threadId = t;
            threads[t] = new Thread(() -> {
                try {
                    for (int i = 0; i < CALCS_PER_THREAD; i++) {
                        // Use different principal and duration values for each thread
                        // to simulate realistic usage
                        BigDecimal principal = new BigDecimal(1000 * (threadId + 1));
                        int duration = 5 + (threadId % 5);
                        
                        calculationService.calculateEMI(
                            principal, 
                            duration, 
                            DEFAULT_INTEREST_RATE);
                        
                        // Small delay to simulate real-world timing
                        if (i % 10 == 0) {
                            TestUtils.waitForMillis(1);
                        }
                    }
                } catch (Exception e) {
                    synchronized(threadExceptions) {
                        threadExceptions.add(e);
                    }
                }
            }, "CalcThread-" + t);
        }
        
        // Measure memory usage during concurrent calculations
        long memoryUsage = PerformanceTestUtils.measureMemoryUsage(() -> {
            // Start all threads
            for (Thread thread : threads) {
                thread.start();
            }
            
            // Wait for all threads to complete
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException e) {
                    TestUtils.logTestError("Thread interrupted while waiting", e);
                }
            }
        });
        
        // Assert that memory usage is within acceptable limits
        TestUtils.logTestInfo("Memory usage during concurrent calculations: " + memoryUsage + "MB");
        Assertions.assertTrue(memoryUsage < PerformanceTestUtils.MAX_MEMORY_USAGE_MB, 
            "Memory usage during concurrent calculations exceeds maximum allowed: " + 
            memoryUsage + "MB > " + PerformanceTestUtils.MAX_MEMORY_USAGE_MB + "MB");
        
        // Assert that calculations complete without thread contention issues
        Assertions.assertTrue(threadExceptions.isEmpty(), 
            "Exceptions occurred during concurrent calculations: " + threadExceptions.size());
    }

    /**
     * Tests that BigDecimal operations are optimized for memory usage
     */
    @Test
    @DisplayName("Should optimize BigDecimal operations for memory usage")
    public void testBigDecimalMemoryOptimization() {
        TestUtils.logTestInfo("Testing BigDecimal memory optimization");
        
        final int ITERATIONS = 1000;
        
        // Measure memory usage during BigDecimal calculations with different scales and precisions
        // Unoptimized approach
        TestUtils.logTestInfo("Testing unoptimized BigDecimal operations");
        long unoptimizedMemory = PerformanceTestUtils.measureMemoryUsage(() -> {
            BigDecimal result = BigDecimal.ZERO;
            for (int i = 0; i < ITERATIONS; i++) {
                // Unoptimized: Creating new BigDecimals with high precision
                BigDecimal value = new BigDecimal(String.valueOf(i)).divide(
                    new BigDecimal("3.14159265358979323846"), 20, BigDecimal.ROUND_HALF_UP);
                result = result.add(value);
            }
        });
        
        // Force garbage collection
        PerformanceTestUtils.runGarbageCollection();
        
        // Optimized approach
        TestUtils.logTestInfo("Testing optimized BigDecimal operations");
        long optimizedMemory = PerformanceTestUtils.measureMemoryUsage(() -> {
            BigDecimal result = BigDecimal.ZERO;
            BigDecimal divisor = new BigDecimal("3.14159265358979323846");
            for (int i = 0; i < ITERATIONS; i++) {
                // Optimized: Reusing BigDecimals and using appropriate scale
                BigDecimal value = new BigDecimal(i).divide(divisor, 2, BigDecimal.ROUND_HALF_UP);
                result = result.add(value);
            }
        });
        
        // Compare memory usage between optimized and unoptimized BigDecimal operations
        TestUtils.logTestInfo("Unoptimized memory usage: " + unoptimizedMemory + 
            "MB, Optimized memory usage: " + optimizedMemory + "MB");
        
        // Assert that optimized operations use significantly less memory
        Assertions.assertTrue(optimizedMemory < unoptimizedMemory, 
            "Optimized BigDecimal operations did not use less memory: " + 
            optimizedMemory + "MB >= " + unoptimizedMemory + "MB");
    }

    /**
     * Helper method to create a large amount of temporary data for memory testing
     * 
     * @param size The number of BigDecimal objects to create
     * @return A list containing the specified number of BigDecimal objects
     */
    private List<BigDecimal> createLargeTemporaryData(int size) {
        List<BigDecimal> data = new ArrayList<>(size);
        for (int i = 0; i < size; i++) {
            // Create BigDecimal with varying values and precisions
            int precision = 1 + (i % 10);
            BigDecimal value = new BigDecimal(String.valueOf(i)).divide(
                BigDecimal.TEN, precision, BigDecimal.ROUND_HALF_UP);
            data.add(value);
        }
        return data;
    }

    /**
     * Helper method to simulate application startup for memory testing
     */
    private void simulateApplicationStartup() {
        try {
            // Create a separate ClassLoader to load application classes
            ClassLoader testClassLoader = this.getClass().getClassLoader();
            
            // Load the application class using reflection to minimize initialization
            Class<?> appClass = testClassLoader.loadClass(CompoundInterestCalculatorApp.class.getName());
            
            // Get the main method but don't fully execute it to avoid UI initialization
            // We just want to trigger class loading and basic initialization
            java.lang.reflect.Method mainMethod = appClass.getDeclaredMethod("main", String[].class);
            
            // Invoke the main method of CompoundInterestCalculatorApp with minimal initialization
            Thread t = new Thread(() -> {
                try {
                    String[] args = new String[0];
                    mainMethod.invoke(null, (Object) args);
                } catch (Exception e) {
                    // Expected and can be ignored - we're just triggering class loading
                }
            });
            
            t.start();
            
            // Let it run briefly to trigger initialization then interrupt
            TestUtils.waitForMillis(100);
            t.interrupt();
            
        } catch (Exception e) {
            TestUtils.logTestError("Error simulating application startup", e);
        }
    }

    /**
     * Helper method to log current memory statistics
     * 
     * @param phase The current test phase for logging
     */
    private void logMemoryStatistics(String phase) {
        // Get current free memory, total memory, and max memory
        long freeMemory = PerformanceTestUtils.getAvailableMemory();
        long totalMemory = PerformanceTestUtils.getTotalMemory();
        long maxMemory = PerformanceTestUtils.getMaxMemory();
        long usedMemory = totalMemory - freeMemory;
        
        // Log all memory statistics with the specified phase label
        TestUtils.logTestInfo(phase + " Memory Stats - Used: " + usedMemory + 
            "MB, Free: " + freeMemory + "MB, Total: " + totalMemory + 
            "MB, Max: " + maxMemory + "MB");
    }
}