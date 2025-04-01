package com.bank.calculator.test.runner;

import java.util.logging.Logger; // JDK 11
import java.util.logging.Level; // JDK 11
import java.util.Arrays; // JDK 11
import java.util.List; // JDK 11
import java.util.ArrayList; // JDK 11

import org.junit.platform.runner.JUnitPlatform; // 1.8.2
import org.junit.runner.RunWith; // 4.13.2
import org.junit.platform.suite.api.SelectPackages; // 1.8.2
import org.junit.platform.suite.api.Suite; // 1.8.2
import org.junit.platform.launcher.LauncherDiscoveryRequest; // 1.8.2
import org.junit.platform.launcher.core.LauncherFactory; // 1.8.2
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder; // 1.8.2
import org.junit.platform.engine.discovery.DiscoverySelectors; // 1.8.2
import org.junit.platform.launcher.TestExecutionSummary; // 1.8.2

import com.bank.calculator.test.config.TestConfig;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.runner.UnitTestsRunner;
import com.bank.calculator.test.runner.IntegrationTestsRunner;
import com.bank.calculator.test.runner.UITestsRunner;
import com.bank.calculator.test.runner.PerformanceTestsRunner;

/**
 * Test runner class that executes all test categories for the Compound Interest Calculator application.
 * This class provides a centralized entry point for running unit, integration, UI, performance,
 * and security tests, either individually or as a complete test suite.
 */
@RunWith(JUnitPlatform.class)
@Suite
@SelectPackages("com.bank.calculator.test")
public class AllTestsRunner {

    private static final Logger LOGGER = Logger.getLogger(AllTestsRunner.class.getName());
    private static final String BASE_PACKAGE = "com.bank.calculator.test";
    private static final String[] TEST_CATEGORIES = {"unit", "integration", "ui", "performance", "security"};

    /**
     * Default constructor
     */
    public AllTestsRunner() {
        LOGGER.info("AllTestsRunner is being instantiated");
    }

    /**
     * Main method to run all tests programmatically outside of the JUnit runner
     * 
     * @param args command line arguments to specify which test categories to run
     */
    public static void main(String[] args) {
        LOGGER.info("Starting AllTestsRunner...");
        TestUtils.logTestInfo("Starting AllTestsRunner...");
        
        try {
            // Initialize the test environment
            initializeRunner();
            
            // Parse command line arguments to determine which test categories to run
            String[] categories = parseArguments(args);
            
            // Run the selected test categories
            List<TestExecutionSummary> summaries;
            if (categories == null) {
                // Run all test categories
                summaries = runAllTestCategories();
            } else {
                // Run selected test categories
                summaries = runSelectedTestCategories(categories);
            }
            
            // Aggregate and report test results
            aggregateTestResults(summaries);
            
            LOGGER.info("AllTestsRunner completed.");
            TestUtils.logTestInfo("AllTestsRunner completed.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error running tests", e);
            TestUtils.logTestError("Failed to run tests", e);
        }
    }

    /**
     * Initializes the test runner environment for all tests
     */
    private static void initializeRunner() {
        LOGGER.info("Initializing all tests runner...");
        TestUtils.logTestInfo("Initializing all tests runner...");
        
        // Set up the test environment
        TestConfig.initializeTestEnvironment();
        
        // Set system properties for test execution
        System.setProperty("spring.profiles.active", "test");
        
        // Configure any additional test-specific settings
        
        LOGGER.info("All tests runner initialization complete");
        TestUtils.logTestInfo("All tests runner initialization complete");
    }

    /**
     * Runs all test categories in sequence
     * 
     * @return List of test execution summaries for each category
     */
    private static List<TestExecutionSummary> runAllTestCategories() {
        List<TestExecutionSummary> summaries = new ArrayList<>();
        
        LOGGER.info("Running all test categories...");
        TestUtils.logTestInfo("Running all test categories...");
        
        // Run unit tests
        LOGGER.info("Running unit tests...");
        UnitTestsRunner.main(new String[0]);
        
        // Run integration tests
        LOGGER.info("Running integration tests...");
        IntegrationTestsRunner.main(new String[0]);
        
        // Run UI tests
        LOGGER.info("Running UI tests...");
        UITestsRunner.main(new String[0]);
        
        // Run performance tests
        LOGGER.info("Running performance tests...");
        PerformanceTestsRunner.main(new String[0]);
        
        // Check if security tests are available
        try {
            Class<?> securityRunnerClass = Class.forName("com.bank.calculator.test.runner.SecurityTestsRunner");
            LOGGER.info("Running security tests...");
            // If available, call the main method via reflection
            securityRunnerClass.getMethod("main", String[].class).invoke(null, (Object) new String[0]);
        } catch (ClassNotFoundException e) {
            LOGGER.info("Security tests runner not available, skipping security tests");
            TestUtils.logTestInfo("Security tests runner not available, skipping security tests");
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Error running security tests", e);
            TestUtils.logTestError("Error running security tests", e);
        }
        
        return summaries;
    }

    /**
     * Runs selected test categories based on command line arguments
     * 
     * @param categories Array of test categories to run
     * @return List of test execution summaries for selected categories
     */
    private static List<TestExecutionSummary> runSelectedTestCategories(String[] categories) {
        List<TestExecutionSummary> summaries = new ArrayList<>();
        
        LOGGER.info("Running selected test categories: " + Arrays.toString(categories));
        TestUtils.logTestInfo("Running selected test categories: " + Arrays.toString(categories));
        
        for (String category : categories) {
            if ("unit".equalsIgnoreCase(category)) {
                LOGGER.info("Running unit tests...");
                UnitTestsRunner.main(new String[0]);
            } else if ("integration".equalsIgnoreCase(category)) {
                LOGGER.info("Running integration tests...");
                IntegrationTestsRunner.main(new String[0]);
            } else if ("ui".equalsIgnoreCase(category)) {
                LOGGER.info("Running UI tests...");
                UITestsRunner.main(new String[0]);
            } else if ("performance".equalsIgnoreCase(category)) {
                LOGGER.info("Running performance tests...");
                PerformanceTestsRunner.main(new String[0]);
            } else if ("security".equalsIgnoreCase(category)) {
                try {
                    Class<?> securityRunnerClass = Class.forName("com.bank.calculator.test.runner.SecurityTestsRunner");
                    LOGGER.info("Running security tests...");
                    // If available, call the main method via reflection
                    securityRunnerClass.getMethod("main", String[].class).invoke(null, (Object) new String[0]);
                } catch (ClassNotFoundException e) {
                    LOGGER.info("Security tests runner not available, skipping security tests");
                    TestUtils.logTestInfo("Security tests runner not available, skipping security tests");
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING, "Error running security tests", e);
                    TestUtils.logTestError("Error running security tests", e);
                }
            } else {
                LOGGER.warning("Unknown test category: " + category);
                TestUtils.logTestInfo("WARNING: Unknown test category: " + category);
            }
        }
        
        return summaries;
    }

    /**
     * Aggregates test results from multiple test categories
     * 
     * @param summaries List of test execution summaries
     */
    private static void aggregateTestResults(List<TestExecutionSummary> summaries) {
        LOGGER.info("Aggregating test results...");
        TestUtils.logTestInfo("Aggregating test results...");
        
        if (summaries == null || summaries.isEmpty()) {
            LOGGER.info("No test summaries available for aggregation");
            TestUtils.logTestInfo("No test summaries available for aggregation");
            return;
        }
        
        long totalTests = 0;
        long totalSucceeded = 0;
        long totalFailed = 0;
        long totalSkipped = 0;
        long totalTime = 0;
        
        for (TestExecutionSummary summary : summaries) {
            if (summary != null) {
                totalTests += summary.getTestsFoundCount();
                totalSucceeded += summary.getTestsSucceededCount();
                totalFailed += summary.getTestsFailedCount();
                totalSkipped += summary.getTestsSkippedCount();
                totalTime += summary.getTimeFinished() - summary.getTimeStarted();
            }
        }
        
        LOGGER.info("=== Aggregated Test Results ===");
        LOGGER.info("Total tests: " + totalTests);
        LOGGER.info("Succeeded: " + totalSucceeded);
        LOGGER.info("Failed: " + totalFailed);
        LOGGER.info("Skipped: " + totalSkipped);
        LOGGER.info("Total execution time: " + totalTime + " ms");
        
        TestUtils.logTestInfo("=== Aggregated Test Results ===");
        TestUtils.logTestInfo("Total tests: " + totalTests);
        TestUtils.logTestInfo("Succeeded: " + totalSucceeded);
        TestUtils.logTestInfo("Failed: " + totalFailed);
        TestUtils.logTestInfo("Skipped: " + totalSkipped);
        TestUtils.logTestInfo("Total execution time: " + totalTime + " ms");
        
        if (totalFailed > 0) {
            LOGGER.warning("Some tests failed! Review individual test logs for details.");
            TestUtils.logTestInfo("WARNING: Some tests failed! Review individual test logs for details.");
        } else {
            LOGGER.info("All tests passed successfully!");
            TestUtils.logTestInfo("All tests passed successfully!");
        }
    }

    /**
     * Parses command line arguments to determine which test categories to run
     * 
     * @param args Command line arguments
     * @return Array of test categories to run, or null to run all categories
     */
    private static String[] parseArguments(String[] args) {
        if (args == null || args.length == 0) {
            return null; // Run all categories
        }
        
        List<String> validCategories = new ArrayList<>();
        
        for (String arg : args) {
            String category = arg.toLowerCase();
            if (isValidCategory(category)) {
                validCategories.add(category);
            } else {
                LOGGER.warning("Unknown test category: " + arg);
                TestUtils.logTestInfo("WARNING: Unknown test category: " + arg);
            }
        }
        
        if (validCategories.isEmpty()) {
            return null; // Run all categories
        }
        
        return validCategories.toArray(new String[0]);
    }

    /**
     * Checks if a given string is a valid test category
     * 
     * @param category The category to check
     * @return true if valid, false otherwise
     */
    private static boolean isValidCategory(String category) {
        if (category == null || category.isEmpty()) {
            return false;
        }
        
        for (String validCategory : TEST_CATEGORIES) {
            if (validCategory.equalsIgnoreCase(category)) {
                return true;
            }
        }
        
        return false;
    }

    /**
     * Logs a summary of test execution results
     * 
     * @param summary The test execution summary
     * @param categoryName The name of the test category
     */
    private static void logTestSummary(TestExecutionSummary summary, String categoryName) {
        if (summary == null) {
            LOGGER.info(categoryName + " tests execution completed, but no summary available");
            TestUtils.logTestInfo(categoryName + " tests execution completed, but no summary available");
            return;
        }
        
        LOGGER.info("=== " + categoryName + " Test Results ===");
        LOGGER.info("Tests found: " + summary.getTestsFoundCount());
        LOGGER.info("Tests started: " + summary.getTestsStartedCount());
        LOGGER.info("Tests succeeded: " + summary.getTestsSucceededCount());
        LOGGER.info("Tests failed: " + summary.getTestsFailedCount());
        LOGGER.info("Tests skipped: " + summary.getTestsSkippedCount());
        LOGGER.info("Total time: " + summary.getTimeFinished() + "ms");
        
        // Log details of failures, if any
        if (summary.getTestsFailedCount() > 0) {
            LOGGER.info("Failed tests:");
            summary.getFailures().forEach(failure -> {
                TestUtils.logTestError("Test failed: " + failure.getTestIdentifier().getDisplayName(), failure.getException());
            });
        }
    }
}