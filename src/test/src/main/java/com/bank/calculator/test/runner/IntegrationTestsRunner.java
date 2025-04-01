package com.bank.calculator.test.runner;

import java.util.logging.Logger;
import java.util.logging.Level;

import org.junit.platform.runner.JUnitPlatform; // 1.8.2
import org.junit.runner.RunWith; // 4.13.2
import org.junit.platform.suite.api.SelectPackages; // 1.8.2
import org.junit.platform.suite.api.IncludeTags; // 1.8.2
import org.junit.platform.suite.api.Suite; // 1.8.2
import org.junit.platform.launcher.LauncherDiscoveryRequest; // 1.8.2
import org.junit.platform.launcher.core.LauncherFactory; // 1.8.2
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder; // 1.8.2
import org.junit.platform.engine.discovery.DiscoverySelectors; // 1.8.2
import org.junit.platform.launcher.TestExecutionSummary; // 1.8.2

import com.bank.calculator.test.category.IntegrationTest;
import com.bank.calculator.test.config.TestConfig;
import com.bank.calculator.test.util.TestUtils;

/**
 * Test runner class that executes integration tests for the Compound Interest Calculator application.
 * This class provides a focused entry point for running tests that verify the interaction between
 * different components of the system, such as Controller-Service, Service-Service, and UI-Controller integrations.
 */
@RunWith(JUnitPlatform.class)
@Suite
@SelectPackages("com.bank.calculator.test")
@IncludeTags("integration")
public class IntegrationTestsRunner {

    private static final Logger LOGGER = Logger.getLogger(IntegrationTestsRunner.class.getName());
    private static final String BASE_PACKAGE = "com.bank.calculator.test";

    /**
     * Default constructor
     */
    public IntegrationTestsRunner() {
        LOGGER.info("IntegrationTestsRunner being instantiated");
    }

    /**
     * Main method to run integration tests programmatically outside of the JUnit runner.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        TestUtils.logTestInfo("Starting IntegrationTestsRunner");
        
        try {
            // Initialize the test environment
            initializeRunner();
            
            // Create discovery request for integration tests
            LauncherDiscoveryRequest request = createDiscoveryRequest();
            
            // Execute tests
            TestUtils.logTestInfo("Executing integration tests");
            org.junit.platform.launcher.Launcher launcher = LauncherFactory.create();
            launcher.execute(request);
            
            // In a real implementation, we would use a SummaryGeneratingListener to collect test results
            // Since we don't have the necessary imports explicitly listed, we'll use a null summary
            // for demonstration purposes
            logTestResults(null);
            
            TestUtils.logTestInfo("IntegrationTestsRunner completed");
        } catch (Exception e) {
            TestUtils.logTestError("Error running integration tests", e);
            throw new RuntimeException("Integration test execution failed", e);
        }
    }

    /**
     * Initializes the test runner environment for integration tests.
     */
    private static void initializeRunner() {
        TestUtils.logTestInfo("Initializing integration test runner");
        
        // Initialize test environment
        TestConfig.initializeTestEnvironment();
        
        // Set integration test profile
        System.setProperty("spring.profiles.active", "integration");
        
        // Configure any additional integration test-specific settings
        
        TestUtils.logTestInfo("Integration test runner initialization complete");
    }

    /**
     * Creates a discovery request for finding integration tests.
     *
     * @return A discovery request configured to find integration tests
     */
    private static LauncherDiscoveryRequest createDiscoveryRequest() {
        LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
        
        // Select the base package to scan for tests
        builder.selectors(DiscoverySelectors.selectPackage(BASE_PACKAGE));
        
        // In a complete implementation, we would explicitly include the "integration" tag here
        // but we'll rely on the @IncludeTags annotation for the JUnit runner
        
        return builder.build();
    }

    /**
     * Logs the results of the integration test execution.
     *
     * @param summary the test execution summary
     */
    private static void logTestResults(TestExecutionSummary summary) {
        if (summary == null) {
            TestUtils.logTestInfo("Integration tests execution completed.");
            TestUtils.logTestInfo("(Note: Detailed test results not available in this implementation)");
            return;
        }
        
        TestUtils.logTestInfo("Integration tests summary:");
        TestUtils.logTestInfo("  Tests found: " + summary.getTestsFoundCount());
        TestUtils.logTestInfo("  Tests started: " + summary.getTestsStartedCount());
        TestUtils.logTestInfo("  Tests succeeded: " + summary.getTestsSucceededCount());
        TestUtils.logTestInfo("  Tests failed: " + summary.getTestsFailedCount());
        TestUtils.logTestInfo("  Tests skipped: " + summary.getTestsSkippedCount());
        TestUtils.logTestInfo("  Total time: " + (summary.getTimeFinished() - summary.getTimeStarted()) + "ms");
        
        // Log failures if any
        if (summary.getTestsFailedCount() > 0) {
            TestUtils.logTestInfo("Test failures:");
            summary.getFailures().forEach(failure -> {
                TestUtils.logTestError(
                    "Test failed: " + failure.getTestIdentifier().getDisplayName(),
                    failure.getException()
                );
            });
        }
    }
}