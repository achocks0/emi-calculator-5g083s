package com.bank.calculator.test.util;

import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.util.PerformanceTestUtils;

import org.testfx.api.FxRobot;      // TestFX 4.0.16-alpha
import javafx.scene.Node;           // JavaFX 11
import org.junit.jupiter.api.Assertions; // JUnit 5.8.2
import javafx.application.Platform; // JavaFX 11
import java.util.concurrent.CountDownLatch; // JDK 11
import java.util.concurrent.TimeUnit;     // JDK 11
import java.util.logging.Logger;    // JDK 11
import java.util.logging.Level;     // JDK 11

/**
 * Utility class that provides helper methods and constants for UI testing in the Compound Interest Calculator application.
 * This class simplifies interaction with JavaFX UI components, provides common assertions for UI testing,
 * and encapsulates TestFX operations to make UI tests more readable and maintainable.
 */
public class UITestUtils {

    private static final Logger LOGGER = Logger.getLogger(UITestUtils.class.getName());
    
    // Constants for UI element IDs
    public static final String PRINCIPAL_FIELD_ID = "principalField";
    public static final String DURATION_FIELD_ID = "durationField";
    public static final String CALCULATE_BUTTON_ID = "calculateButton";
    public static final String NEW_CALCULATION_BUTTON_ID = "newCalculationButton";
    public static final String RESULT_LABEL_ID = "resultLabel";
    public static final String PRINCIPAL_ERROR_LABEL_ID = "principalErrorLabel";
    public static final String DURATION_ERROR_LABEL_ID = "durationErrorLabel";
    
    // Constants for timeouts and delays
    private static final long WAIT_TIMEOUT_MS = 5000;
    private static final long FX_EVENTS_WAIT_MS = 200;
    
    /**
     * Enters text in a specified input field with UI response time measurement.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param fieldId The ID of the field to enter text into
     * @param text The text to enter
     * @return The time taken in milliseconds to perform the operation
     */
    public static long enterTextInField(FxRobot robot, String fieldId, String text) {
        TestUtils.logTestInfo("Entering text '" + text + "' in field with ID: " + fieldId);
        
        // Clear the field first
        robot.clickOn("#" + fieldId);
        robot.eraseText(robot.lookup("#" + fieldId).queryTextInputControl().getText().length());
        
        // Measure UI response time for entering text
        long responseTime = PerformanceTestUtils.measureUIResponseTime(() -> {
            robot.clickOn("#" + fieldId).write(text);
        });
        
        // Wait for JavaFX events to complete
        waitForFxEvents();
        
        return responseTime;
    }
    
    /**
     * Clicks a button with UI response time measurement.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param buttonId The ID of the button to click
     * @return The time taken in milliseconds to perform the operation
     */
    public static long clickButton(FxRobot robot, String buttonId) {
        TestUtils.logTestInfo("Clicking button with ID: " + buttonId);
        
        // Measure UI response time for clicking the button
        long responseTime = PerformanceTestUtils.measureUIResponseTime(() -> {
            robot.clickOn("#" + buttonId);
        });
        
        // Wait for JavaFX events to complete
        waitForFxEvents();
        
        return responseTime;
    }
    
    /**
     * Verifies that a label contains the expected text.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param labelId The ID of the label to verify
     * @param expectedText The text expected to be in the label
     */
    public static void verifyLabelText(FxRobot robot, String labelId, String expectedText) {
        TestUtils.logTestInfo("Verifying text in label with ID: " + labelId);
        
        // Get the actual text from the label
        String actualText = getNodeText(robot, labelId);
        
        // Assert that the actual text contains the expected text
        Assertions.assertTrue(actualText.contains(expectedText),
                "Expected label to contain '" + expectedText + "' but was '" + actualText + "'");
        
        TestUtils.logTestInfo("Successfully verified label text: " + actualText);
    }
    
    /**
     * Verifies that a node is visible in the UI.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param nodeId The ID of the node to verify
     */
    public static void verifyNodeVisible(FxRobot robot, String nodeId) {
        TestUtils.logTestInfo("Verifying node with ID is visible: " + nodeId);
        
        // Get the node
        Node node = robot.lookup("#" + nodeId).query();
        
        // Assert that the node exists and is visible
        Assertions.assertNotNull(node, "Node with ID '" + nodeId + "' not found");
        Assertions.assertTrue(node.isVisible(), "Node with ID '" + nodeId + "' exists but is not visible");
        
        TestUtils.logTestInfo("Successfully verified node visibility: " + nodeId);
    }
    
    /**
     * Verifies that a node is not visible in the UI.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param nodeId The ID of the node to verify
     */
    public static void verifyNodeNotVisible(FxRobot robot, String nodeId) {
        TestUtils.logTestInfo("Verifying node with ID is not visible: " + nodeId);
        
        try {
            // Try to get the node
            Node node = robot.lookup("#" + nodeId).query();
            
            // If the node exists, it should not be visible
            Assertions.assertFalse(node.isVisible(), "Node with ID '" + nodeId + "' is visible but should not be");
            TestUtils.logTestInfo("Node exists but is not visible as expected: " + nodeId);
        } catch (Exception e) {
            // If the node doesn't exist, that's also acceptable
            TestUtils.logTestInfo("Node not found, which is acceptable for this verification: " + nodeId);
        }
        
        TestUtils.logTestInfo("Successfully verified node is not visible: " + nodeId);
    }
    
    /**
     * Verifies that an error message is displayed for a specific field.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param errorLabelId The ID of the error label to verify
     * @param expectedErrorMessage The expected error message
     */
    public static void verifyErrorMessageDisplayed(FxRobot robot, String errorLabelId, String expectedErrorMessage) {
        TestUtils.logTestInfo("Verifying error message displayed for: " + errorLabelId);
        
        // Verify the error label is visible
        verifyNodeVisible(robot, errorLabelId);
        
        // Verify the error message text
        verifyLabelText(robot, errorLabelId, expectedErrorMessage);
        
        TestUtils.logTestInfo("Successfully verified error message: " + expectedErrorMessage);
    }
    
    /**
     * Verifies that no error message is displayed for a specific field.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param errorLabelId The ID of the error label to verify
     */
    public static void verifyNoErrorMessageDisplayed(FxRobot robot, String errorLabelId) {
        TestUtils.logTestInfo("Verifying no error message is displayed for: " + errorLabelId);
        
        // Verify the error label is not visible
        verifyNodeNotVisible(robot, errorLabelId);
        
        TestUtils.logTestInfo("Successfully verified no error message displayed");
    }
    
    /**
     * Verifies that a calculation result is displayed.
     *
     * @param robot The FxRobot instance for UI interaction
     * @return The displayed result text
     */
    public static String verifyResultDisplayed(FxRobot robot) {
        TestUtils.logTestInfo("Verifying result is displayed");
        
        // Verify the result label is visible
        verifyNodeVisible(robot, RESULT_LABEL_ID);
        
        // Get the result text
        String resultText = getNodeText(robot, RESULT_LABEL_ID);
        
        // Verify the result text is not empty
        Assertions.assertFalse(resultText.isEmpty(), "Result text should not be empty");
        
        TestUtils.logTestInfo("Successfully verified result displayed: " + resultText);
        
        return resultText;
    }
    
    /**
     * Verifies that the calculation result contains expected text.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param expectedText The text expected to be in the result
     */
    public static void verifyResultContains(FxRobot robot, String expectedText) {
        TestUtils.logTestInfo("Verifying result contains: " + expectedText);
        
        // Get the result text
        String resultText = verifyResultDisplayed(robot);
        
        // Assert that the result text contains the expected text
        Assertions.assertTrue(resultText.contains(expectedText),
                "Expected result to contain '" + expectedText + "' but was '" + resultText + "'");
        
        TestUtils.logTestInfo("Successfully verified result contains expected text");
    }
    
    /**
     * Performs a complete calculation with the given inputs.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param principal The principal amount to enter
     * @param duration The loan duration to enter
     * @return The calculation result text
     */
    public static String performCalculation(FxRobot robot, String principal, String duration) {
        TestUtils.logTestInfo("Performing calculation with principal: " + principal + ", duration: " + duration);
        
        // Enter principal amount
        enterTextInField(robot, PRINCIPAL_FIELD_ID, principal);
        
        // Enter loan duration
        enterTextInField(robot, DURATION_FIELD_ID, duration);
        
        // Click the calculate button
        clickButton(robot, CALCULATE_BUTTON_ID);
        
        // Get and return the result
        return verifyResultDisplayed(robot);
    }
    
    /**
     * Clicks the New Calculation button to reset the form.
     *
     * @param robot The FxRobot instance for UI interaction
     */
    public static void performNewCalculation(FxRobot robot) {
        TestUtils.logTestInfo("Performing new calculation (resetting form)");
        
        // Click the new calculation button
        clickButton(robot, NEW_CALCULATION_BUTTON_ID);
        
        TestUtils.logTestInfo("Successfully reset form for new calculation");
    }
    
    /**
     * Verifies that all input fields are cleared.
     *
     * @param robot The FxRobot instance for UI interaction
     */
    public static void verifyInputFieldsCleared(FxRobot robot) {
        TestUtils.logTestInfo("Verifying input fields are cleared");
        
        // Get the principal field text
        String principalText = getNodeText(robot, PRINCIPAL_FIELD_ID);
        
        // Get the duration field text
        String durationText = getNodeText(robot, DURATION_FIELD_ID);
        
        // Assert that both fields are empty
        Assertions.assertTrue(principalText.isEmpty(), "Principal field should be empty but contains: " + principalText);
        Assertions.assertTrue(durationText.isEmpty(), "Duration field should be empty but contains: " + durationText);
        
        TestUtils.logTestInfo("Successfully verified input fields are cleared");
    }
    
    /**
     * Asserts that a UI operation completes within the acceptable response time.
     *
     * @param responseTimeMs The UI operation response time in milliseconds
     */
    public static void assertUIResponseTime(long responseTimeMs) {
        TestUtils.logTestInfo("Asserting UI response time: " + responseTimeMs + "ms");
        
        // Assert that the response time is within the acceptable limit
        PerformanceTestUtils.assertUIResponsePerformance(responseTimeMs);
        
        TestUtils.logTestInfo("UI response time is within acceptable limits");
    }
    
    /**
     * Waits for all pending JavaFX events to be processed.
     */
    public static void waitForFxEvents() {
        TestUtils.logTestInfo("Waiting for JavaFX events to complete");
        
        // Create a latch to wait for
        CountDownLatch latch = new CountDownLatch(1);
        
        // Run an empty task on the JavaFX application thread, which will execute after all previous events
        Platform.runLater(() -> latch.countDown());
        
        try {
            // Wait for the latch to be counted down
            boolean completed = latch.await(WAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            
            if (!completed) {
                TestUtils.logTestWarning("Timed out waiting for JavaFX events to complete");
            }
            
            // Sleep a short additional time to ensure events are fully processed
            TestUtils.waitForMillis(FX_EVENTS_WAIT_MS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            TestUtils.logTestError("Interrupted while waiting for JavaFX events", e);
        }
        
        TestUtils.logTestInfo("JavaFX events processing completed");
    }
    
    /**
     * Runs a task on the JavaFX application thread and waits for completion.
     *
     * @param task The task to run on the JavaFX thread
     */
    public static void runOnFxThread(Runnable task) {
        TestUtils.logTestInfo("Running task on JavaFX application thread");
        
        // Create a latch to wait for
        CountDownLatch latch = new CountDownLatch(1);
        
        // Run the task on the JavaFX application thread
        Platform.runLater(() -> {
            try {
                task.run();
            } finally {
                latch.countDown();
            }
        });
        
        try {
            // Wait for the latch to be counted down
            boolean completed = latch.await(WAIT_TIMEOUT_MS, TimeUnit.MILLISECONDS);
            
            if (!completed) {
                TestUtils.logTestWarning("Timed out waiting for JavaFX task to complete");
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            TestUtils.logTestError("Interrupted while waiting for JavaFX task", e);
        }
        
        TestUtils.logTestInfo("JavaFX task completed");
    }
    
    /**
     * Gets the text from a JavaFX node.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param nodeId The ID of the node to get text from
     * @return The text from the node, or empty string if not found or if node has no text
     */
    public static String getNodeText(FxRobot robot, String nodeId) {
        TestUtils.logTestInfo("Getting text from node with ID: " + nodeId);
        
        try {
            // Look up the node
            Node node = robot.lookup("#" + nodeId).query();
            
            // Extract text based on node type
            String text = "";
            
            if (node instanceof javafx.scene.control.Labeled) {
                text = ((javafx.scene.control.Labeled) node).getText();
            } else if (node instanceof javafx.scene.control.TextInputControl) {
                text = ((javafx.scene.control.TextInputControl) node).getText();
            } else {
                // Try toString() as fallback
                text = node.toString();
            }
            
            TestUtils.logTestInfo("Got text from node: " + text);
            return text;
        } catch (Exception e) {
            TestUtils.logTestError("Error getting text from node with ID: " + nodeId, e);
            return "";
        }
    }
}