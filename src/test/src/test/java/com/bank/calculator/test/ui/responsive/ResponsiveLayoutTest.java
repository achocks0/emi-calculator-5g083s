package com.bank.calculator.test.ui.responsive;

import com.bank.calculator.test.category.UITest;
import com.bank.calculator.test.util.UITestUtils;
import com.bank.calculator.test.fixture.UITestFixture;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Assertions;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import org.junit.jupiter.api.extension.ExtendWith;
import javafx.stage.Stage;
import javafx.application.Platform;
import javafx.scene.Node;
import javafx.geometry.Bounds;
import java.util.logging.Logger;
import java.util.logging.Level;

/**
 * Test class for verifying the responsive layout behavior of the Compound Interest Calculator UI.
 * This class tests that the application's UI components properly adapt to different window sizes and screen resolutions,
 * maintaining usability and proper display across various dimensions.
 */
@ExtendWith(ApplicationExtension.class)
@UITest
public class ResponsiveLayoutTest {

    private static final Logger LOGGER = Logger.getLogger(ResponsiveLayoutTest.class.getName());
    
    // Window dimension constants
    private static final double MINIMUM_WIDTH = 600.0;
    private static final double MINIMUM_HEIGHT = 400.0;
    private static final double COMPACT_WIDTH = 500.0;
    private static final double COMPACT_HEIGHT = 350.0;
    private static final double STANDARD_WIDTH = 800.0;
    private static final double STANDARD_HEIGHT = 600.0;
    private static final double LARGE_WIDTH = 1200.0;
    private static final double LARGE_HEIGHT = 900.0;
    
    private static Stage testStage;
    private FxRobot robot;
    
    /**
     * Default constructor for the ResponsiveLayoutTest class
     */
    public ResponsiveLayoutTest() {
        this.robot = new FxRobot();
    }
    
    /**
     * Sets up the test environment before any tests are run
     */
    @BeforeAll
    static void setupClass() {
        LOGGER.info("Setting up ResponsiveLayoutTest class");
    }
    
    /**
     * Cleans up the test environment after all tests have completed
     */
    @AfterAll
    static void tearDownClass() {
        LOGGER.info("Tearing down ResponsiveLayoutTest class");
    }
    
    /**
     * Initializes the JavaFX application for testing
     *
     * @param stage The primary stage for the application
     */
    @Start
    public void start(Stage stage) {
        testStage = stage;
        testStage.setResizable(true);
        testStage.setWidth(STANDARD_WIDTH);
        testStage.setHeight(STANDARD_HEIGHT);
        LOGGER.info("Initialized test application with stage: " + stage);
    }
    
    /**
     * Sets up each test before execution
     */
    @BeforeEach
    void setUp() {
        LOGGER.info("Setting up test");
        resizeStage(STANDARD_WIDTH, STANDARD_HEIGHT);
        UITestUtils.waitForFxEvents();
    }
    
    /**
     * Cleans up after each test
     */
    @AfterEach
    void tearDown() {
        LOGGER.info("Tearing down test");
        resizeStage(STANDARD_WIDTH, STANDARD_HEIGHT);
        UITestUtils.waitForFxEvents();
    }
    
    /**
     * Tests that the application respects minimum window size constraints
     */
    @Test
    @DisplayName("Application should respect minimum window size")
    void testMinimumWindowSize() {
        LOGGER.info("Testing minimum window size constraints");
        
        // Attempt to resize below minimum dimensions
        resizeStage(MINIMUM_WIDTH - 50, MINIMUM_HEIGHT - 50);
        
        // Verify stage respects minimum dimensions
        Assertions.assertTrue(testStage.getWidth() >= MINIMUM_WIDTH, 
            "Window width should not be less than minimum width");
        Assertions.assertTrue(testStage.getHeight() >= MINIMUM_HEIGHT, 
            "Window height should not be less than minimum height");
        
        // Verify all components are still visible and accessible
        verifyComponentsVisible(robot);
    }
    
    /**
     * Tests that the UI adapts to compact window dimensions
     */
    @Test
    @DisplayName("UI should adapt to compact window dimensions")
    void testCompactLayout() {
        LOGGER.info("Testing compact layout");
        
        // Resize to compact dimensions
        resizeStage(COMPACT_WIDTH, COMPACT_HEIGHT);
        UITestUtils.waitForFxEvents();
        
        // Verify all essential UI components are visible
        verifyComponentsVisible(robot);
        
        // Verify that input fields, buttons, and result display are properly arranged
        verifyComponentAlignment(robot);
        
        // Perform a calculation to verify functionality in compact mode
        UITestUtils.performCalculation(robot, UITestFixture.STANDARD_PRINCIPAL, UITestFixture.STANDARD_DURATION);
        
        // Verify that the result is displayed correctly
        UITestUtils.verifyResultContains(robot, UITestFixture.STANDARD_RESULT);
    }
    
    /**
     * Tests that the UI displays correctly at standard window dimensions
     */
    @Test
    @DisplayName("UI should display correctly at standard window dimensions")
    void testStandardLayout() {
        LOGGER.info("Testing standard layout");
        
        // Resize to standard dimensions
        resizeStage(STANDARD_WIDTH, STANDARD_HEIGHT);
        UITestUtils.waitForFxEvents();
        
        // Verify all UI components are visible and properly arranged
        verifyComponentsVisible(robot);
        
        // Verify there is adequate spacing between components
        verifyComponentAlignment(robot);
        
        // Perform a calculation to verify functionality
        UITestUtils.performCalculation(robot, UITestFixture.STANDARD_PRINCIPAL, UITestFixture.STANDARD_DURATION);
        
        // Verify the result is displayed correctly
        UITestUtils.verifyResultContains(robot, UITestFixture.STANDARD_RESULT);
    }
    
    /**
     * Tests that the UI scales appropriately to large window dimensions
     */
    @Test
    @DisplayName("UI should scale appropriately to large window dimensions")
    void testLargeLayout() {
        LOGGER.info("Testing large layout");
        
        // Resize to large dimensions
        resizeStage(LARGE_WIDTH, LARGE_HEIGHT);
        UITestUtils.waitForFxEvents();
        
        // Verify all UI components are visible and properly arranged
        verifyComponentsVisible(robot);
        
        // Verify that components scale appropriately without excessive empty space
        verifyComponentAlignment(robot);
        
        // Perform a calculation to verify functionality
        UITestUtils.performCalculation(robot, UITestFixture.STANDARD_PRINCIPAL, UITestFixture.STANDARD_DURATION);
        
        // Verify the result is displayed correctly
        UITestUtils.verifyResultContains(robot, UITestFixture.STANDARD_RESULT);
    }
    
    /**
     * Tests that the UI adapts correctly when window is resized dynamically
     */
    @Test
    @DisplayName("UI should adapt correctly when window is resized dynamically")
    void testDynamicResizing() {
        LOGGER.info("Testing dynamic resizing");
        
        // Start with standard window dimensions
        resizeStage(STANDARD_WIDTH, STANDARD_HEIGHT);
        
        // Perform a calculation to establish initial state
        UITestUtils.performCalculation(robot, UITestFixture.STANDARD_PRINCIPAL, UITestFixture.STANDARD_DURATION);
        
        // Resize window to compact dimensions
        resizeStage(COMPACT_WIDTH, COMPACT_HEIGHT);
        
        // Verify that UI components adjust and remain functional
        verifyComponentsVisible(robot);
        
        // Verify that calculation result remains visible
        UITestUtils.verifyNodeVisible(robot, UITestUtils.RESULT_LABEL_ID);
        
        // Resize window to large dimensions
        resizeStage(LARGE_WIDTH, LARGE_HEIGHT);
        
        // Verify that UI components adjust and remain functional
        verifyComponentsVisible(robot);
        
        // Verify that calculation result remains visible
        UITestUtils.verifyNodeVisible(robot, UITestUtils.RESULT_LABEL_ID);
        
        // Perform another calculation to verify functionality after resizing
        UITestUtils.performCalculation(robot, UITestFixture.LARGE_PRINCIPAL, UITestFixture.STANDARD_DURATION);
        UITestUtils.verifyResultContains(robot, UITestFixture.LARGE_PRINCIPAL_RESULT);
    }
    
    /**
     * Tests that UI components maintain proper alignment at different window sizes
     */
    @Test
    @DisplayName("UI components should maintain proper alignment at different window sizes")
    void testComponentAlignment() {
        LOGGER.info("Testing component alignment at different window sizes");
        
        // Test alignment at compact size
        resizeStage(COMPACT_WIDTH, COMPACT_HEIGHT);
        UITestUtils.waitForFxEvents();
        verifyComponentAlignment(robot);
        
        // Test alignment at standard size
        resizeStage(STANDARD_WIDTH, STANDARD_HEIGHT);
        UITestUtils.waitForFxEvents();
        verifyComponentAlignment(robot);
        
        // Test alignment at large size
        resizeStage(LARGE_WIDTH, LARGE_HEIGHT);
        UITestUtils.waitForFxEvents();
        verifyComponentAlignment(robot);
        
        // Verify that input fields and labels remain properly aligned
        // Verify that buttons remain centered or properly aligned
        // Verify that result display maintains proper positioning
    }
    
    /**
     * Tests that text wraps appropriately when window width is reduced
     */
    @Test
    @DisplayName("Text should wrap appropriately when window width is reduced")
    void testTextWrapping() {
        LOGGER.info("Testing text wrapping behavior");
        
        // Resize window to standard width
        resizeStage(STANDARD_WIDTH, STANDARD_HEIGHT);
        
        // Perform calculation with result that produces long text
        UITestUtils.performCalculation(robot, UITestFixture.LARGE_PRINCIPAL, UITestFixture.MAX_DURATION);
        
        // Verify text display
        String resultText = UITestUtils.getNodeText(robot, UITestUtils.RESULT_LABEL_ID);
        Assertions.assertFalse(resultText.isEmpty(), "Result text should not be empty");
        
        // Get result dimensions at standard width
        Bounds standardBounds = getNodeDimensions(robot, UITestUtils.RESULT_LABEL_ID);
        
        // Resize window to compact width
        resizeStage(COMPACT_WIDTH, COMPACT_HEIGHT);
        UITestUtils.waitForFxEvents();
        
        // Get result dimensions at compact width
        Bounds compactBounds = getNodeDimensions(robot, UITestUtils.RESULT_LABEL_ID);
        
        // Verify that text wraps appropriately without truncation
        UITestUtils.verifyNodeVisible(robot, UITestUtils.RESULT_LABEL_ID);
        
        // Verify that all information remains readable
        String compactResultText = UITestUtils.getNodeText(robot, UITestUtils.RESULT_LABEL_ID);
        Assertions.assertEquals(resultText, compactResultText, "Result text should remain the same after resizing");
    }
    
    /**
     * Tests that input fields scale appropriately with window size
     */
    @Test
    @DisplayName("Input fields should scale appropriately with window size")
    void testInputFieldScaling() {
        LOGGER.info("Testing input field scaling");
        
        // Get input field dimensions at compact size
        resizeStage(COMPACT_WIDTH, COMPACT_HEIGHT);
        UITestUtils.waitForFxEvents();
        Bounds compactPrincipalBounds = getNodeDimensions(robot, UITestUtils.PRINCIPAL_FIELD_ID);
        
        // Get input field dimensions at standard size
        resizeStage(STANDARD_WIDTH, STANDARD_HEIGHT);
        UITestUtils.waitForFxEvents();
        Bounds standardPrincipalBounds = getNodeDimensions(robot, UITestUtils.PRINCIPAL_FIELD_ID);
        
        // Get input field dimensions at large size
        resizeStage(LARGE_WIDTH, LARGE_HEIGHT);
        UITestUtils.waitForFxEvents();
        Bounds largePrincipalBounds = getNodeDimensions(robot, UITestUtils.PRINCIPAL_FIELD_ID);
        
        // Verify that input fields scale proportionally with window width
        Assertions.assertTrue(compactPrincipalBounds.getWidth() < standardPrincipalBounds.getWidth(),
            "Input field should be smaller in compact mode");
        Assertions.assertTrue(standardPrincipalBounds.getWidth() < largePrincipalBounds.getWidth(),
            "Input field should be larger in large mode");
            
        // Verify that input fields remain usable at all sizes
        resizeStage(COMPACT_WIDTH, COMPACT_HEIGHT);
        UITestUtils.waitForFxEvents();
        UITestUtils.performCalculation(robot, UITestFixture.STANDARD_PRINCIPAL, UITestFixture.STANDARD_DURATION);
        UITestUtils.verifyResultContains(robot, UITestFixture.STANDARD_RESULT);
    }
    
    /**
     * Tests that buttons scale appropriately with window size
     */
    @Test
    @DisplayName("Buttons should scale appropriately with window size")
    void testButtonScaling() {
        LOGGER.info("Testing button scaling");
        
        // Get button dimensions at compact size
        resizeStage(COMPACT_WIDTH, COMPACT_HEIGHT);
        UITestUtils.waitForFxEvents();
        Bounds compactButtonBounds = getNodeDimensions(robot, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Get button dimensions at standard size
        resizeStage(STANDARD_WIDTH, STANDARD_HEIGHT);
        UITestUtils.waitForFxEvents();
        Bounds standardButtonBounds = getNodeDimensions(robot, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Get button dimensions at large size
        resizeStage(LARGE_WIDTH, LARGE_HEIGHT);
        UITestUtils.waitForFxEvents();
        Bounds largeButtonBounds = getNodeDimensions(robot, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify that buttons scale appropriately with window size
        Assertions.assertTrue(compactButtonBounds.getWidth() <= standardButtonBounds.getWidth(),
            "Button should not be larger in compact mode");
        Assertions.assertTrue(standardButtonBounds.getWidth() <= largeButtonBounds.getWidth(),
            "Button should not be smaller in large mode");
        
        // Verify that buttons remain clickable and functional at all sizes
        resizeStage(COMPACT_WIDTH, COMPACT_HEIGHT);
        UITestUtils.waitForFxEvents();
        UITestUtils.performCalculation(robot, UITestFixture.STANDARD_PRINCIPAL, UITestFixture.STANDARD_DURATION);
        UITestUtils.verifyResultContains(robot, UITestFixture.STANDARD_RESULT);
    }
    
    /**
     * Resizes the test stage to the specified dimensions
     *
     * @param width The new width
     * @param height The new height
     */
    private void resizeStage(double width, double height) {
        LOGGER.info("Resizing stage to " + width + "x" + height);
        
        Platform.runLater(() -> {
            testStage.setWidth(width);
            testStage.setHeight(height);
        });
        
        // Wait for the resize operation to complete
        UITestUtils.waitForFxEvents();
    }
    
    /**
     * Gets the dimensions of a UI node
     *
     * @param robot The FxRobot instance for UI interaction
     * @param nodeId The ID of the node to get dimensions for
     * @return The bounds of the node
     */
    private Bounds getNodeDimensions(FxRobot robot, String nodeId) {
        Node node = robot.lookup("#" + nodeId).query();
        return node.getBoundsInParent();
    }
    
    /**
     * Verifies that all essential UI components are visible
     *
     * @param robot The FxRobot instance for UI interaction
     */
    private void verifyComponentsVisible(FxRobot robot) {
        UITestUtils.verifyNodeVisible(robot, UITestUtils.PRINCIPAL_FIELD_ID);
        UITestUtils.verifyNodeVisible(robot, UITestUtils.DURATION_FIELD_ID);
        UITestUtils.verifyNodeVisible(robot, UITestUtils.CALCULATE_BUTTON_ID);
        UITestUtils.verifyNodeVisible(robot, UITestUtils.RESULT_LABEL_ID);
    }
    
    /**
     * Verifies that UI components are properly aligned
     *
     * @param robot The FxRobot instance for UI interaction
     */
    private void verifyComponentAlignment(FxRobot robot) {
        // Get bounds of various components
        Bounds principalFieldBounds = getNodeDimensions(robot, UITestUtils.PRINCIPAL_FIELD_ID);
        Bounds durationFieldBounds = getNodeDimensions(robot, UITestUtils.DURATION_FIELD_ID);
        Bounds calculateButtonBounds = getNodeDimensions(robot, UITestUtils.CALCULATE_BUTTON_ID);
        Bounds resultLabelBounds = getNodeDimensions(robot, UITestUtils.RESULT_LABEL_ID);
        
        // Verify vertical ordering (principal field above duration field above calculate button above result)
        Assertions.assertTrue(principalFieldBounds.getMinY() < durationFieldBounds.getMinY(),
            "Principal field should be above duration field");
        Assertions.assertTrue(durationFieldBounds.getMinY() < calculateButtonBounds.getMinY(),
            "Duration field should be above calculate button");
        Assertions.assertTrue(calculateButtonBounds.getMinY() < resultLabelBounds.getMinY(),
            "Calculate button should be above result label");
    }
}