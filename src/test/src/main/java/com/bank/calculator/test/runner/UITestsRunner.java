package com.bank.calculator.test.runner;

import java.util.logging.Logger;
import java.util.logging.Level;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.io.File;

import org.junit.platform.runner.JUnitPlatform; // 4.13.2
import org.junit.runner.RunWith; // 4.13.2
import org.junit.platform.suite.api.SelectPackages; // 1.8.2
import org.junit.platform.suite.api.IncludeTags; // 1.8.2
import org.junit.platform.suite.api.Suite; // 1.8.2
import org.junit.platform.launcher.LauncherDiscoveryRequest; // 1.8.2
import org.junit.platform.launcher.core.LauncherFactory; // 1.8.2
import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder; // 1.8.2
import org.junit.platform.engine.discovery.DiscoverySelectors; // 1.8.2
import org.junit.platform.engine.discovery.TagFilter; // 1.8.2
import org.junit.platform.launcher.TestExecutionSummary; // 1.8.2
import javafx.application.Platform; // JavaFX 11

import com.bank.calculator.test.config.TestConfig;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.util.UITestUtils;
import com.bank.calculator.test.category.UITest;

/**
 * Test runner class that executes UI tests for the Compound Interest Calculator application.
 * This runner provides functionality to run UI tests either individually or as a complete suite,
 * with proper initialization of the JavaFX environment and test configuration.
 */
@RunWith(JUnitPlatform.class)
@Suite
@SelectPackages("com.bank.calculator.test.ui")
@IncludeTags("ui")
public class UITestsRunner {
    
    private static final Logger LOGGER = Logger.getLogger(UITestsRunner.class.getName());
    private static final String UI_TEST_PACKAGE = "com.bank.calculator.test.ui";
    private static final String UI_TEST_TAG = "ui";
    private static final String UI_TEST_SUITE_CONFIG = "test-suites/ui-tests.xml";
    private static final boolean HEADLESS_MODE = Boolean.getBoolean("headless") || true;
    private static final long UI_TEST_TIMEOUT_MS = 30000;
    
    /**
     * Main method to run UI tests programmatically outside of the JUnit runner.
     * 
     * @param args Command line arguments, optionally specifying test classes to run
     */
    public static void main(String[] args) {
        LOGGER.info("Starting UITestsRunner...");
        
        try {
            // Initialize test environment
            TestConfig.initializeTestEnvironment();
            
            // Initialize UI test environment
            initializeUITestEnvironment();
            
            // Parse command line arguments for specific tests to run
            String[] testClasses = parseUITestArguments(args);
            
            // Run the tests
            TestExecutionSummary summary = runUITests(testClasses);
            
            // Log test execution summary
            logTestSummary(summary);
            
            // Clean up environment
            cleanupUITestEnvironment();
            
            LOGGER.info("UITestsRunner completed.");
            
            // Exit with appropriate code
            System.exit(summary.getTestsFailedCount() > 0 ? 1 : 0);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error running UI tests", e);
            TestUtils.logTestError("Failed to run UI tests", e);
            System.exit(1);
        }
    }
    
    /**
     * Initializes the environment specifically for UI testing.
     */
    private static void initializeUITestEnvironment() {
        LOGGER.info("Initializing UI test environment...");
        
        // Set system properties for UI testing
        if (HEADLESS_MODE) {
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
            System.setProperty("java.awt.headless", "true");
            LOGGER.info("Configured for headless testing");
        }
        
        // Configure JavaFX timeout
        System.setProperty("testfx.timeout", String.valueOf(UI_TEST_TIMEOUT_MS));
        
        // Initialize the JavaFX toolkit
        initializeJavaFXToolkit();
        
        LOGGER.info("UI test environment initialized");
    }
    
    /**
     * Initializes the JavaFX toolkit for UI testing.
     */
    private static void initializeJavaFXToolkit() {
        LOGGER.info("Initializing JavaFX toolkit...");
        
        // Check if JavaFX is already initialized
        if (Platform.isFxApplicationThread() || Platform.isImplicitExit()) {
            LOGGER.info("JavaFX toolkit already initialized");
            return;
        }
        
        // Initialize JavaFX toolkit
        final CountDownLatch latch = new CountDownLatch(1);
        
        // Platform.startup() must be called to initialize the JavaFX toolkit when it's not already running
        try {
            Platform.startup(() -> {
                // This will be executed on the JavaFX Application Thread
                LOGGER.info("JavaFX Application Thread started");
                
                // Configure the platform not to exit when the last window is closed
                Platform.setImplicitExit(false);
                
                // Signal that initialization is complete
                latch.countDown();
            });
            
            // Wait for JavaFX initialization to complete
            if (!latch.await(UI_TEST_TIMEOUT_MS, TimeUnit.MILLISECONDS)) {
                LOGGER.warning("Timeout waiting for JavaFX initialization");
            }
            
            LOGGER.info("JavaFX toolkit initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to initialize JavaFX toolkit", e);
            throw new RuntimeException("Failed to initialize JavaFX toolkit", e);
        }
    }
    
    /**
     * Runs UI tests using the JUnit Platform Launcher.
     * 
     * @param testClasses Array of test class names to run, or null to run all UI tests
     * @return Summary of the test execution results
     */
    private static TestExecutionSummary runUITests(String[] testClasses) {
        LOGGER.info("Running UI tests...");
        
        // Create a launcher discovery request for the tests
        LauncherDiscoveryRequest request = createUITestDiscoveryRequest(testClasses);
        
        // Create a launcher and execute the tests
        LOGGER.info("Executing UI tests...");
        TestExecutionSummary summary = LauncherFactory.create()
                .execute(request)
                .getTestExecutionSummary();
        
        return summary;
    }
    
    /**
     * Creates a discovery request for UI tests.
     * 
     * @param testClasses Array of test class names to run, or null to run all UI tests
     * @return The discovery request for UI tests
     */
    private static LauncherDiscoveryRequest createUITestDiscoveryRequest(String[] testClasses) {
        LauncherDiscoveryRequestBuilder builder = LauncherDiscoveryRequestBuilder.request();
        
        // If specific test classes are provided, add class selectors for them
        if (testClasses != null && testClasses.length > 0) {
            LOGGER.info("Adding selectors for " + testClasses.length + " specific test classes");
            for (String className : testClasses) {
                LOGGER.info("Adding selector for class: " + className);
                builder.selectors(DiscoverySelectors.selectClass(className));
            }
        } else {
            // Otherwise, select all tests in the UI test package
            LOGGER.info("Selecting all tests in package: " + UI_TEST_PACKAGE);
            builder.selectors(DiscoverySelectors.selectPackage(UI_TEST_PACKAGE));
            
            // Check if test suite configuration file exists
            File suiteConfig = new File(UI_TEST_SUITE_CONFIG);
            if (suiteConfig.exists()) {
                LOGGER.info("Using test suite configuration: " + UI_TEST_SUITE_CONFIG);
                builder.configurationParameter("junit.jupiter.extensions.autodetection.enabled", "true");
                // In a real implementation, we would also add configuration to load the suite file
            }
        }
        
        // Add a filter to include only UI tests
        builder.filters(TagFilter.includeTags(UI_TEST_TAG));
        
        return builder.build();
    }
    
    /**
     * Parses command line arguments to determine which UI tests to run.
     * 
     * @param args Command line arguments
     * @return Array of test class names to run, or null to run all UI tests
     */
    private static String[] parseUITestArguments(String[] args) {
        if (args == null || args.length == 0) {
            LOGGER.info("No specific test classes specified, will run all UI tests");
            return null;
        }
        
        // Create a list to store valid class names
        java.util.List<String> classList = new java.util.ArrayList<>();
        
        // Process each argument
        for (String arg : args) {
            if (arg.startsWith("class:")) {
                // Extract the class name
                String className = arg.substring("class:".length());
                LOGGER.info("Adding test class: " + className);
                classList.add(className);
            } else if (arg.contains(".") && Character.isUpperCase(arg.charAt(arg.lastIndexOf('.') + 1))) {
                // Assume it's a fully qualified class name
                LOGGER.info("Adding test class: " + arg);
                classList.add(arg);
            } else {
                LOGGER.warning("Unrecognized argument or class name: " + arg);
            }
        }
        
        // If no valid classes were found, run all UI tests
        if (classList.isEmpty()) {
            LOGGER.info("No valid test classes specified, will run all UI tests");
            return null;
        }
        
        // Convert list to array and return
        return classList.toArray(new String[0]);
    }
    
    /**
     * Logs a summary of UI test execution results.
     * 
     * @param summary The test execution summary
     */
    private static void logTestSummary(TestExecutionSummary summary) {
        LOGGER.info("==== UI Test Execution Summary ====");
        LOGGER.info("Tests found: " + summary.getTestsFoundCount());
        LOGGER.info("Tests started: " + summary.getTestsStartedCount());
        LOGGER.info("Tests succeeded: " + summary.getTestsSucceededCount());
        LOGGER.info("Tests failed: " + summary.getTestsFailedCount());
        LOGGER.info("Tests skipped: " + summary.getTestsSkippedCount());
        LOGGER.info("Total time: " + summary.getTimeFinished() + "ms");
        
        // Log failures if any
        if (summary.getTestsFailedCount() > 0) {
            LOGGER.warning("==== Failed Tests ====");
            summary.getFailures().forEach(failure -> {
                LOGGER.warning("Test failed: " + failure.getTestIdentifier().getDisplayName());
                LOGGER.warning("Reason: " + failure.getException().getMessage());
                TestUtils.logTestError("Test failure: " + failure.getTestIdentifier().getDisplayName(), 
                                      failure.getException());
            });
        }
        
        // Log overall success or failure
        if (summary.getTestsFailedCount() == 0) {
            LOGGER.info("UI TEST EXECUTION SUCCESSFUL");
        } else {
            LOGGER.warning("UI TEST EXECUTION FAILED: " + summary.getTestsFailedCount() + " tests failed");
        }
    }
    
    /**
     * Cleans up the UI test environment after tests complete.
     */
    private static void cleanupUITestEnvironment() {
        LOGGER.info("Cleaning up UI test environment...");
        
        // Ensure all JavaFX events are processed
        UITestUtils.waitForFxEvents();
        
        // Shutdown JavaFX platform if running in non-headless mode
        if (!HEADLESS_MODE) {
            try {
                Platform.runLater(() -> {
                    if (Platform.isImplicitExit()) {
                        Platform.setImplicitExit(true);
                        Platform.exit();
                        LOGGER.info("JavaFX platform exited");
                    }
                });
                
                // Wait for the platform exit to complete
                TestUtils.waitForMillis(500);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Error shutting down JavaFX platform", e);
            }
        }
        
        // Reset system properties that were set for UI testing
        if (HEADLESS_MODE) {
            System.clearProperty("testfx.robot");
            System.clearProperty("testfx.headless");
            System.clearProperty("prism.order");
            System.clearProperty("prism.text");
            System.clearProperty("java.awt.headless");
        }
        
        System.clearProperty("testfx.timeout");
        
        LOGGER.info("UI test environment cleanup completed");
    }
}