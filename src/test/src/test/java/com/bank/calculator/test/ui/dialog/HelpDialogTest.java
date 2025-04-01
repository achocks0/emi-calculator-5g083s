package com.bank.calculator.test.ui.dialog;

import com.bank.calculator.test.category.UITest;
import com.bank.calculator.test.util.UITestUtils;
import com.bank.calculator.test.fixture.UITestFixture;
import com.bank.calculator.ui.dialog.HelpDialog;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.control.Button;
import javafx.scene.control.TabPane;
import java.util.logging.Logger;
import java.util.logging.Level;

import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Test class for verifying the functionality of the HelpDialog component in the
 * Compound Interest Calculator application. This class tests the dialog's display,
 * content, and interaction with different help topics related to principal amount,
 * loan duration, interest rates, and calculation formulas.
 */
@UITest
@ExtendWith(ApplicationExtension.class)
public class HelpDialogTest {
    private static final Logger LOGGER = Logger.getLogger(HelpDialogTest.class.getName());
    private static final String HELP_DIALOG_TITLE = "Help - Compound Interest Calculator";
    private static final String PRINCIPAL_HELP_BUTTON_ID = "principalHelpButton";
    private static final String DURATION_HELP_BUTTON_ID = "durationHelpButton";
    private static final String GENERAL_HELP_BUTTON_ID = "generalHelpButton";
    private static final String HELP_DIALOG_PANE_ID = "helpDialogPane";
    private static final String HELP_TAB_PANE_ID = "tabPane";
    private static final String PRINCIPAL_TAB_ID = "principalTab";
    private static final String DURATION_TAB_ID = "durationTab";
    private static final String INTEREST_TAB_ID = "interestTab";
    private static final String CALCULATION_TAB_ID = "calculationTab";
    
    private HelpDialog helpDialog;
    private FxRobot robot;
    
    /**
     * Sets up the JavaFX test environment with necessary UI components.
     * 
     * @param stage the primary stage for this test
     */
    @Start
    public void start(Stage stage) {
        LOGGER.info("Setting up test environment for HelpDialogTest");
        
        // Create a root container for our test buttons
        VBox root = new VBox(10); // 10px spacing
        
        // Create buttons for testing help dialog
        Button principalHelpButton = new Button("Principal Help");
        principalHelpButton.setId(PRINCIPAL_HELP_BUTTON_ID);
        
        Button durationHelpButton = new Button("Duration Help");
        durationHelpButton.setId(DURATION_HELP_BUTTON_ID);
        
        Button generalHelpButton = new Button("General Help");
        generalHelpButton.setId(GENERAL_HELP_BUTTON_ID);
        
        // Add buttons to the root container
        root.getChildren().addAll(principalHelpButton, durationHelpButton, generalHelpButton);
        
        // Create and show the scene
        Scene scene = new Scene(root, 400, 300);
        stage.setScene(scene);
        stage.setTitle("Help Dialog Test");
        stage.show();
        
        LOGGER.info("Test environment setup completed");
    }
    
    /**
     * Initializes the test environment before each test.
     */
    @BeforeEach
    public void setUp() {
        LOGGER.info("Setting up test case");
        helpDialog = new HelpDialog();
        UITestUtils.waitForFxEvents();
        LOGGER.info("Test setup completed");
    }
    
    /**
     * Cleans up the test environment after each test.
     */
    @AfterEach
    public void tearDown() {
        LOGGER.info("Tearing down test case");
        // Close the dialog if it's open
        try {
            robot.clickOn("OK");
        } catch (Exception e) {
            LOGGER.info("No dialog to close or close button not found");
        }
        UITestUtils.waitForFxEvents();
        LOGGER.info("Test teardown completed");
    }
    
    /**
     * Tests that the help dialog shows correctly for principal amount field.
     */
    @Test
    @DisplayName("Should show help for principal amount field")
    public void testShowPrincipalHelp() {
        LOGGER.info("Testing showHelpForField for principal amount");
        
        long responseTime = UITestUtils.measureUIResponseTime(() -> {
            helpDialog.showHelpForField(UITestUtils.PRINCIPAL_FIELD_ID);
        });
        
        verifyHelpDialogVisible();
        verifyTabSelected(PRINCIPAL_TAB_ID);
        verifyHelpContent(PRINCIPAL_TAB_ID, "Principal Amount");
        
        UITestUtils.assertUIResponseTime(responseTime);
        LOGGER.info("Principal help test completed successfully");
    }
    
    /**
     * Tests that the help dialog shows correctly for loan duration field.
     */
    @Test
    @DisplayName("Should show help for loan duration field")
    public void testShowDurationHelp() {
        LOGGER.info("Testing showHelpForField for loan duration");
        
        long responseTime = UITestUtils.measureUIResponseTime(() -> {
            helpDialog.showHelpForField(UITestUtils.DURATION_FIELD_ID);
        });
        
        verifyHelpDialogVisible();
        verifyTabSelected(DURATION_TAB_ID);
        verifyHelpContent(DURATION_TAB_ID, "Loan Duration");
        
        UITestUtils.assertUIResponseTime(responseTime);
        LOGGER.info("Duration help test completed successfully");
    }
    
    /**
     * Tests that the general help dialog shows correctly.
     */
    @Test
    @DisplayName("Should show general help information")
    public void testShowGeneralHelp() {
        LOGGER.info("Testing showGeneralHelp");
        
        long responseTime = UITestUtils.measureUIResponseTime(() -> {
            helpDialog.showGeneralHelp();
        });
        
        verifyHelpDialogVisible();
        // Verify that the first tab is selected for general help
        TabPane tabPane = robot.lookup("#" + HELP_TAB_PANE_ID).queryAs(TabPane.class);
        Assertions.assertEquals(0, tabPane.getSelectionModel().getSelectedIndex(),
                              "First tab should be selected for general help");
        
        UITestUtils.assertUIResponseTime(responseTime);
        LOGGER.info("General help test completed successfully");
    }
    
    /**
     * Tests navigation between tabs in the help dialog.
     */
    @Test
    @DisplayName("Should allow navigation between help tabs")
    public void testHelpDialogTabNavigation() {
        LOGGER.info("Testing tab navigation in help dialog");
        
        // Show the help dialog
        helpDialog.showGeneralHelp();
        verifyHelpDialogVisible();
        
        // Navigate to each tab and verify it becomes selected
        robot.clickOn("#" + PRINCIPAL_TAB_ID);
        verifyTabSelected(PRINCIPAL_TAB_ID);
        
        robot.clickOn("#" + DURATION_TAB_ID);
        verifyTabSelected(DURATION_TAB_ID);
        
        robot.clickOn("#" + INTEREST_TAB_ID);
        verifyTabSelected(INTEREST_TAB_ID);
        
        robot.clickOn("#" + CALCULATION_TAB_ID);
        verifyTabSelected(CALCULATION_TAB_ID);
        
        LOGGER.info("Tab navigation test completed successfully");
    }
    
    /**
     * Tests that the help dialog closes when the close button is clicked.
     */
    @Test
    @DisplayName("Should close help dialog when close button is clicked")
    public void testHelpDialogCloseButton() {
        LOGGER.info("Testing help dialog close button");
        
        // Show the help dialog
        helpDialog.showGeneralHelp();
        verifyHelpDialogVisible();
        
        // Click the close button
        robot.clickOn("OK");
        
        // Verify that the help dialog is no longer visible
        try {
            UITestUtils.verifyNodeNotVisible(robot, HELP_DIALOG_PANE_ID);
            LOGGER.info("Help dialog closed successfully");
        } catch (Exception e) {
            Assertions.fail("Help dialog should be closed after clicking OK button: " + e.getMessage());
        }
        
        LOGGER.info("Close button test completed successfully");
    }
    
    /**
     * Tests the responsiveness of the help dialog UI.
     */
    @Test
    @DisplayName("Should respond to user interactions within performance limits")
    public void testHelpDialogResponsiveness() {
        LOGGER.info("Testing help dialog responsiveness");
        
        // Show the help dialog
        helpDialog.showGeneralHelp();
        
        // Measure tab selection response time
        long tabSelectionTime = UITestUtils.measureUIResponseTime(() -> {
            robot.clickOn("#" + DURATION_TAB_ID);
        });
        
        // Measure dialog closing response time
        long dialogCloseTime = UITestUtils.measureUIResponseTime(() -> {
            robot.clickOn("OK");
        });
        
        // Assert that all response times are within acceptable limits
        UITestUtils.assertUIResponseTime(tabSelectionTime);
        UITestUtils.assertUIResponseTime(dialogCloseTime);
        
        LOGGER.info("Responsiveness test completed successfully");
    }
    
    /**
     * Verifies that the help dialog is visible.
     */
    private void verifyHelpDialogVisible() {
        UITestUtils.verifyNodeVisible(robot, HELP_DIALOG_PANE_ID);
    }
    
    /**
     * Verifies that a specific tab is selected in the help dialog.
     * 
     * @param tabId the ID of the tab to verify
     */
    private void verifyTabSelected(String tabId) {
        TabPane tabPane = robot.lookup("#" + HELP_TAB_PANE_ID).queryAs(TabPane.class);
        Assertions.assertEquals(tabId, tabPane.getSelectionModel().getSelectedItem().getId(),
                            "Expected tab should be selected");
        LOGGER.info("Verified tab '" + tabId + "' is selected");
    }
    
    /**
     * Verifies that the help dialog contains expected content.
     * 
     * @param tabId the ID of the tab to check
     * @param expectedContent the content that should be present
     */
    private void verifyHelpContent(String tabId, String expectedContent) {
        String contentText = UITestUtils.getNodeText(robot, tabId + "Content");
        Assertions.assertTrue(contentText.contains(expectedContent),
                           "Help content should contain expected text");
        LOGGER.info("Verified help content contains '" + expectedContent + "'");
    }
}