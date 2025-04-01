package com.bank.calculator.test.runner;

import java.util.logging.Logger; // JDK 11
import java.util.logging.Level; // JDK 11

import org.junit.platform.runner.JUnitPlatform; // 1.8.2
import org.junit.runner.RunWith; // 4.13.2
import org.junit.platform.suite.api.SelectPackages; // 1.8.2
import org.junit.platform.suite.api.IncludeTags; // 1.8.2
import org.junit.platform.launcher.LauncherDiscoveryRequest; // 1.8.2
import org.junit.platform.launcher.core.LauncherFactory; // 1.8.2
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder; // 1.8.2
import org.junit.platform.engine.discovery.DiscoverySelectors; // 1.8.2
import org.junit.platform.launcher.TestExecutionSummary; // 1.8.2
import org.junit.platform.launcher.Launcher; // 1.8.2
import org.junit.platform.launcher.listeners.SummaryGeneratingListener; // 1.8.2
import org.junit.platform.engine.discovery.TagFilter; // 1.8.2

import com.bank.calculator.test.config.TestConfig;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.category.UnitTest;

/**
 * Test runner class that executes unit tests for the Compound Interest Calculator application.
 * This class provides a programmatic way to run all unit tests, either as part of the complete
 * test suite or independently.
 */
@RunWith(JUnitPlatform.class)
@SelectPackages("com.bank.calculator.test.unit")
@IncludeTags("unit")
public class UnitTestsRunner {

    private static final Logger LOGGER = Logger.getLogger(UnitTestsRunner.class.getName());
    private static final String UNIT_TEST_PACKAGE = "com.bank.calculator.test.unit";
    private static final String UNIT_TEST_TAG = "unit";

    /**
     * Default constructor
     */
    public UnitTestsRunner() {
        LOGGER.info("UnitTestsRunner is being instantiated");
    }

    /**
     * Main method to run unit tests programmatically outside of the JUnit runner
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        TestUtils.logTestInfo("Starting UnitTestsRunner...");
        
        // Initialize the test environment
        initializeRunner();
        
        // Create a discovery request for unit tests
        LauncherDiscoveryRequest discoveryRequest = createDiscoveryRequest();
        
        // Execute the tests
        TestExecutionSummary summary = executeTests(discoveryRequest);
        
        // Log the test summary
        logTestSummary(summary);
        
        TestUtils.logTestInfo("UnitTestsRunner completed");
    }

    /**
     * Initializes the test runner environment for unit tests
     */
    private static void initializeRunner() {
        TestUtils.logTestInfo("Initializing unit tests runner...");
        
        // Set up the test environment
        TestConfig.initializeTestEnvironment();
        
        // Set system properties for test execution
        System.setProperty("spring.profiles.active", "test");
        
        // Any additional unit test-specific settings can be configured here
        
        TestUtils.logTestInfo("Unit tests runner initialization complete");
    }

    /**
     * Creates a discovery request for finding unit tests
     * 
     * @return the discovery request for unit tests
     */
    private static LauncherDiscoveryRequest createDiscoveryRequest() {
        return LauncherDiscoveryRequestBuilder.request()
                .selectors(DiscoverySelectors.selectPackage(UNIT_TEST_PACKAGE))
                .filters(TagFilter.includeTags(UNIT_TEST_TAG))
                .build();
    }

    /**
     * Executes the unit tests using the JUnit platform
     * 
     * @param discoveryRequest the discovery request for finding tests
     * @return a summary of the test execution
     */
    private static TestExecutionSummary executeTests(LauncherDiscoveryRequest discoveryRequest) {
        // Create the launcher
        Launcher launcher = LauncherFactory.create();
        
        // Create a listener for collecting test results
        SummaryGeneratingListener listener = new SummaryGeneratingListener();
        
        // Register the listener with the launcher
        launcher.registerTestExecutionListeners(listener);
        
        // Execute the tests
        launcher.execute(discoveryRequest);
        
        // Return the test execution summary
        return listener.getSummary();
    }

    /**
     * Logs a summary of the unit test execution results
     * 
     * @param summary the test execution summary
     */
    private static void logTestSummary(TestExecutionSummary summary) {
        TestUtils.logTestInfo("Unit Test Results:");
        TestUtils.logTestInfo("Tests found: " + summary.getTestsFoundCount());
        TestUtils.logTestInfo("Tests started: " + summary.getTestsStartedCount());
        TestUtils.logTestInfo("Tests succeeded: " + summary.getTestsSucceededCount());
        TestUtils.logTestInfo("Tests failed: " + summary.getTestsFailedCount());
        TestUtils.logTestInfo("Tests skipped: " + summary.getTestsSkippedCount());
        TestUtils.logTestInfo("Total time: " + summary.getTimeFinished() + "ms");
        
        // Log details of failures, if any
        if (summary.getTestsFailedCount() > 0) {
            TestUtils.logTestInfo("Failed tests:");
            summary.getFailures().forEach(failure -> {
                TestUtils.logTestError("Test failed: " + failure.getTestIdentifier().getDisplayName(), failure.getException());
            });
        }
        
        // Log overall success or failure
        if (summary.getTestsFailedCount() == 0) {
            TestUtils.logTestInfo("All unit tests PASSED");
        } else {
            TestUtils.logTestInfo("Some unit tests FAILED");
        }
    }
}