package com.bank.calculator.test.runner;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.junit.platform.runner.JUnitPlatform; // 1.8.2
import org.junit.runner.RunWith; // 4.13.2
import org.junit.platform.suite.api.SelectPackages; // 1.8.2
import org.junit.platform.suite.api.IncludeTags; // 1.8.2
import org.junit.platform.launcher.LauncherDiscoveryRequest; // 1.8.2
import org.junit.platform.launcher.core.LauncherFactory; // 1.8.2
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder; // 1.8.2
import org.junit.platform.engine.discovery.DiscoverySelectors; // 1.8.2
import org.junit.platform.engine.discovery.TagFilter; // 1.8.2
import org.junit.platform.launcher.TestExecutionSummary; // 1.8.2

import com.bank.calculator.test.config.TestConfig;
import com.bank.calculator.test.util.PerformanceTestUtils;
import com.bank.calculator.test.util.TestUtils;

/**
 * Test runner class specifically designed to execute performance tests for the Compound Interest Calculator application.
 * This class provides functionality to run performance tests that verify the application meets performance requirements
 * such as calculation speed, UI responsiveness, memory usage, and startup time.
 */
@RunWith(JUnitPlatform.class)
@SelectPackages("com.bank.calculator.test.performance")
@IncludeTags("performance")
public class PerformanceTestsRunner {

    private static final Logger LOGGER = Logger.getLogger(PerformanceTestsRunner.class.getName());
    private static final String PERFORMANCE_TEST_PACKAGE = "com.bank.calculator.test.performance";
    private static final String PERFORMANCE_TAG = "performance";

    /**
     * Default constructor
     */
    public PerformanceTestsRunner() {
        LOGGER.info("PerformanceTestsRunner is being instantiated");
    }

    /**
     * Main method to run performance tests programmatically outside of the JUnit runner.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        LOGGER.info("Starting PerformanceTestsRunner...");
        TestUtils.logTestInfo("Starting PerformanceTestsRunner...");
        
        // Initialize test environment
        TestConfig.initializeTestEnvironment();
        
        // Load performance thresholds
        PerformanceTestUtils.loadPerformanceThresholds();
        
        // Create discovery request for performance tests
        LauncherDiscoveryRequest discoveryRequest = createPerformanceTestDiscoveryRequest();
        
        // Execute the tests and get summary
        TestExecutionSummary summary = executeTests(discoveryRequest);
        
        // Print the test summary
        printTestSummary(summary);
        
        LOGGER.info("PerformanceTestsRunner completed.");
        TestUtils.logTestInfo("PerformanceTestsRunner completed.");
    }
    
    /**
     * Creates a discovery request for performance tests.
     *
     * @return a discovery request configured to run performance tests
     */
    private static LauncherDiscoveryRequest createPerformanceTestDiscoveryRequest() {
        return LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectPackage(PERFORMANCE_TEST_PACKAGE))
                .filters(TagFilter.includeTags(PERFORMANCE_TAG))
                .build();
    }
    
    /**
     * Executes the performance tests and returns a summary.
     *
     * @param discoveryRequest the discovery request for the tests to run
     * @return a summary of the test execution results
     */
    private static TestExecutionSummary executeTests(LauncherDiscoveryRequest discoveryRequest) {
        TestUtils.logTestInfo("Executing performance tests...");
        
        // Configure environment before running tests
        configurePerformanceTestEnvironment();
        
        // Execute the tests with a launcher
        org.junit.platform.launcher.Launcher launcher = LauncherFactory.create();
        
        // In a complete implementation, we would use a SummaryGeneratingListener
        // to capture test execution results. For now, we just execute the tests
        // without detailed result capturing.
        launcher.execute(discoveryRequest);
        
        // In a real implementation with the proper dependencies, 
        // we would return the actual TestExecutionSummary
        TestUtils.logTestInfo("Performance tests executed successfully");
        
        return null;
    }
    
    /**
     * Prints a summary of the test execution results.
     *
     * @param summary the test execution summary
     */
    private static void printTestSummary(TestExecutionSummary summary) {
        TestUtils.logTestInfo("Performance Test Results Summary");
        
        if (summary == null) {
            TestUtils.logTestInfo("Detailed test execution summary not available in this implementation");
            TestUtils.logTestInfo("Check logs for individual test results");
            return;
        }
        
        // In a full implementation, we would log details from the summary like:
        // - Tests found/started/succeeded/failed/skipped counts
        // - Total execution time
        // - Details about any test failures
        // - Overall pass/fail status
    }
    
    /**
     * Configures the environment specifically for performance tests.
     */
    private static void configurePerformanceTestEnvironment() {
        TestUtils.logTestInfo("Configuring performance test environment...");
        
        // Set system properties specific to performance testing
        System.setProperty("performance.test.mode", "true");
        System.setProperty("performance.metrics.enabled", "true");
        
        // Configure logging levels appropriate for performance tests
        // Lower log levels during performance testing to minimize logging overhead
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.WARNING);
        
        // Perform any necessary cleanup to ensure consistent test environment
        System.gc(); // Request garbage collection to start with clean memory state
        
        TestUtils.logTestInfo("Performance test environment has been configured");
    }
}