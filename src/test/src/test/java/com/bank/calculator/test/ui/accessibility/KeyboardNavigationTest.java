package com.bank.calculator.test.ui.accessibility;

import com.bank.calculator.test.category.UITest;
import com.bank.calculator.test.util.UITestUtils;
import com.bank.calculator.test.fixture.UITestFixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.util.logging.Logger;

/**
 * Test class that verifies keyboard navigation accessibility features of the Compound Interest Calculator application.
 * This class tests that users can navigate through the application using only keyboard inputs, ensuring accessibility
 * for users who cannot use a mouse or other pointing devices.
 */
@UITest
public class KeyboardNavigationTest extends ApplicationTest {

    private static final Logger LOGGER = Logger.getLogger(KeyboardNavigationTest.class.getName());
    private Stage stage;

    /**
     * Starts the JavaFX application for testing.
     *
     * @param stage the primary stage for this application
     */
    @Override
    public void start(Stage stage) {
        this.stage = stage;
        
        // Launch the Compound Interest Calculator application
        // Note: In a real implementation, we would launch the actual application here
        
        // Wait for the application to fully initialize
        UITestUtils.waitForFxEvents();
    }

    /**
     * Sets up the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        LOGGER.info("Setting up test environment");
        
        // Ensure the application window is focused
        if (stage != null) {
            stage.requestFocus();
        }
        
        // Reset the application state by clicking the New Calculation button if needed
        try {
            UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        } catch (Exception e) {
            LOGGER.info("No need to reset application state: " + e.getMessage());
        }
        
        // Wait for UI to stabilize
        UITestUtils.waitForFxEvents();
        
        LOGGER.info("Test setup completed");
    }

    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    public void tearDown() {
        LOGGER.info("Tearing down test environment");
        
        // Reset the application state by clicking the New Calculation button
        UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        
        // Wait for UI to stabilize
        UITestUtils.waitForFxEvents();
        
        LOGGER.info("Test teardown completed");
    }

    /**
     * Tests that the tab order follows a logical flow through the application form.
     */
    @Test
    public void testTabOrderNavigation() {
        LOGGER.info("Starting tab order navigation test");
        
        // Press Tab key to navigate to the Principal Amount field
        press(KeyCode.TAB).release(KeyCode.TAB);
        verifyFocusedElement(UITestUtils.PRINCIPAL_FIELD_ID);
        
        // Press Tab key to navigate to the Loan Duration field
        press(KeyCode.TAB).release(KeyCode.TAB);
        verifyFocusedElement(UITestUtils.DURATION_FIELD_ID);
        
        // Press Tab key to navigate to the Calculate button
        press(KeyCode.TAB).release(KeyCode.TAB);
        verifyFocusedElement(UITestUtils.CALCULATE_BUTTON_ID);
        
        // Press Tab key to navigate to the New Calculation button
        press(KeyCode.TAB).release(KeyCode.TAB);
        verifyFocusedElement(UITestUtils.NEW_CALCULATION_BUTTON_ID);
        
        LOGGER.info("Tab order navigation test completed successfully");
    }

    /**
     * Tests performing a complete calculation using only keyboard inputs.
     */
    @Test
    public void testKeyboardCalculation() {
        LOGGER.info("Starting keyboard calculation test");
        
        // Press Tab key to navigate to the Principal Amount field
        press(KeyCode.TAB).release(KeyCode.TAB);
        
        // Type the standard principal amount
        write(UITestFixture.STANDARD_PRINCIPAL);
        
        // Press Tab key to navigate to the Loan Duration field
        press(KeyCode.TAB).release(KeyCode.TAB);
        
        // Type the standard duration
        write(UITestFixture.STANDARD_DURATION);
        
        // Press Tab key to navigate to the Calculate button
        press(KeyCode.TAB).release(KeyCode.TAB);
        
        // Press Enter key to activate the Calculate button
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        // Wait for calculation to complete
        UITestUtils.waitForFxEvents();
        
        // Verify that the result contains the expected value
        UITestUtils.verifyResultContains(this, UITestFixture.STANDARD_RESULT);
        
        LOGGER.info("Keyboard calculation test completed successfully");
    }

    /**
     * Tests performing a new calculation after completing one, using only keyboard inputs.
     */
    @Test
    public void testKeyboardNewCalculation() {
        LOGGER.info("Starting keyboard new calculation test");
        
        // Perform a calculation using keyboard inputs
        performKeyboardCalculation();
        
        // Press Tab key to navigate to the New Calculation button
        press(KeyCode.TAB).release(KeyCode.TAB);
        
        // Press Enter key to activate the New Calculation button
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        // Wait for form to reset
        UITestUtils.waitForFxEvents();
        
        // Verify that input fields are cleared
        verifyInputFieldsCleared();
        
        LOGGER.info("Keyboard new calculation test completed successfully");
    }

    /**
     * Tests that keyboard shortcuts work correctly for common actions.
     */
    @Test
    public void testKeyboardShortcuts() {
        LOGGER.info("Starting keyboard shortcuts test");
        
        // Press Alt+P to focus the Principal Amount field
        press(KeyCode.ALT, KeyCode.P).release(KeyCode.P).release(KeyCode.ALT);
        verifyFocusedElement(UITestUtils.PRINCIPAL_FIELD_ID);
        
        // Type the standard principal amount
        write(UITestFixture.STANDARD_PRINCIPAL);
        
        // Press Alt+D to focus the Loan Duration field
        press(KeyCode.ALT, KeyCode.D).release(KeyCode.D).release(KeyCode.ALT);
        verifyFocusedElement(UITestUtils.DURATION_FIELD_ID);
        
        // Type the standard duration
        write(UITestFixture.STANDARD_DURATION);
        
        // Press Alt+C to activate the Calculate button
        press(KeyCode.ALT, KeyCode.C).release(KeyCode.C).release(KeyCode.ALT);
        
        // Wait for calculation to complete
        UITestUtils.waitForFxEvents();
        
        // Verify that the result contains the expected value
        UITestUtils.verifyResultContains(this, UITestFixture.STANDARD_RESULT);
        
        // Press Ctrl+N to activate the New Calculation button (as per Appendix F)
        press(KeyCode.CONTROL, KeyCode.N).release(KeyCode.N).release(KeyCode.CONTROL);
        
        // Verify that input fields are cleared
        verifyInputFieldsCleared();
        
        LOGGER.info("Keyboard shortcuts test completed successfully");
    }

    /**
     * Tests that the Escape key closes help dialogs.
     */
    @Test
    public void testEscapeKeyCloseHelp() {
        LOGGER.info("Starting escape key close help test");
        
        // Press F1 to show Help (as per Appendix F)
        press(KeyCode.F1).release(KeyCode.F1);
        
        // Wait for help dialog to appear
        UITestUtils.waitForFxEvents();
        
        // Verify that help dialog is displayed
        // Note: We need to know the ID of the help dialog component
        // This is an assumption - in a real implementation, we would use the actual ID
        String helpDialogId = "helpDialog";
        UITestUtils.verifyNodeVisible(this, helpDialogId);
        
        // Press Escape key to close the help dialog
        press(KeyCode.ESCAPE).release(KeyCode.ESCAPE);
        
        // Wait for help dialog to close
        UITestUtils.waitForFxEvents();
        
        // Verify that help dialog is closed
        UITestUtils.verifyNodeNotVisible(this, helpDialogId);
        
        LOGGER.info("Escape key close help test completed successfully");
    }

    /**
     * Verifies that a specific element has keyboard focus.
     *
     * @param elementId the ID of the element to check for focus
     */
    private void verifyFocusedElement(String elementId) {
        javafx.scene.Node focusedNode = lookup(".focus").query();
        String focusedNodeId = focusedNode.getId();
        Assertions.assertEquals(elementId, focusedNodeId, "Expected focus on element with ID: " + elementId);
        LOGGER.info("Verified focus on element: " + elementId);
    }

    /**
     * Verifies that all input fields are cleared.
     */
    private void verifyInputFieldsCleared() {
        String principalText = UITestUtils.getNodeText(this, UITestUtils.PRINCIPAL_FIELD_ID);
        String durationText = UITestUtils.getNodeText(this, UITestUtils.DURATION_FIELD_ID);
        
        Assertions.assertTrue(principalText.isEmpty(), "Principal field should be empty but contains: " + principalText);
        Assertions.assertTrue(durationText.isEmpty(), "Duration field should be empty but contains: " + durationText);
        
        LOGGER.info("Verified input fields are cleared");
    }

    /**
     * Performs a calculation using only keyboard inputs.
     */
    private void performKeyboardCalculation() {
        // Press Tab key to navigate to the Principal Amount field
        press(KeyCode.TAB).release(KeyCode.TAB);
        
        // Type the standard principal amount
        write(UITestFixture.STANDARD_PRINCIPAL);
        
        // Press Tab key to navigate to the Loan Duration field
        press(KeyCode.TAB).release(KeyCode.TAB);
        
        // Type the standard duration
        write(UITestFixture.STANDARD_DURATION);
        
        // Press Tab key to navigate to the Calculate button
        press(KeyCode.TAB).release(KeyCode.TAB);
        
        // Press Enter key to activate the Calculate button
        press(KeyCode.ENTER).release(KeyCode.ENTER);
        
        // Wait for calculation to complete
        UITestUtils.waitForFxEvents();
    }
}