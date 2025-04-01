package com.bank.calculator.test.ui;

import com.bank.calculator.test.category.UITest;
import com.bank.calculator.test.util.UITestUtils;
import com.bank.calculator.test.fixture.UITestFixture;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.util.PerformanceTestUtils;
import com.bank.calculator.ui.CalculatorUI;
import com.bank.calculator.constant.ErrorMessages;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.api.Assertions;

import org.testfx.framework.junit5.ApplicationTest;
import org.testfx.api.FxRobot;

import javafx.stage.Stage;
import javafx.application.Platform;

import java.util.List;
import java.util.ArrayList;
import java.util.logging.Logger;
import java.util.logging.Level;

public class CalculatorUITest extends ApplicationTest implements UITest {

    private static final Logger LOGGER = Logger.getLogger(CalculatorUITest.class.getName());
    private static final String TEST_CLASS_NAME = "CalculatorUITest";
    
    @Override
    public void start(Stage stage) {
        LOGGER.info("Starting test application");
        CalculatorUI calculator = new CalculatorUI();
        calculator.start(stage);
        LOGGER.info("Test application started successfully");
    }
    
    @BeforeEach
    public void setUp() {
        LOGGER.info("Setting up test");
        TestUtils.waitForMillis(500); // Allow UI to fully initialize
        // Ensure the application window is focused
        clickOn(lookup(".root").query());
        LOGGER.info("Test setup complete");
    }
    
    @AfterEach
    public void tearDown() {
        LOGGER.info("Tearing down test");
        UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        TestUtils.waitForMillis(200); // Allow UI to reset
        LOGGER.info("Test teardown complete");
    }
    
    @Test
    @DisplayName("Test standard EMI calculation with valid inputs")
    public void testStandardCalculation() {
        LOGGER.info("Starting standard calculation test");
        
        // Enter principal amount
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        
        // Enter loan duration
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify result is displayed
        UITestUtils.verifyResultDisplayed(this);
        
        // Verify result contains expected text
        UITestUtils.verifyResultContains(this, UITestFixture.STANDARD_RESULT);
        
        LOGGER.info("Standard calculation test completed successfully");
    }
    
    @Test
    @DisplayName("Test EMI calculation with large principal amount")
    public void testLargePrincipalCalculation() {
        LOGGER.info("Starting large principal calculation test");
        
        // Enter large principal amount
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.LARGE_PRINCIPAL);
        
        // Enter loan duration
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify result is displayed
        UITestUtils.verifyResultDisplayed(this);
        
        // Verify result contains expected text
        UITestUtils.verifyResultContains(this, UITestFixture.LARGE_PRINCIPAL_RESULT);
        
        LOGGER.info("Large principal calculation test completed successfully");
    }
    
    @Test
    @DisplayName("Test validation for negative principal amount")
    public void testNegativePrincipalValidation() {
        LOGGER.info("Starting negative principal validation test");
        
        // Enter negative principal amount
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.NEGATIVE_PRINCIPAL);
        
        // Enter loan duration
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(this, UITestUtils.PRINCIPAL_ERROR_LABEL_ID, ErrorMessages.PRINCIPAL_POSITIVE);
        
        LOGGER.info("Negative principal validation test completed successfully");
    }
    
    @Test
    @DisplayName("Test validation for zero principal amount")
    public void testZeroPrincipalValidation() {
        LOGGER.info("Starting zero principal validation test");
        
        // Enter zero principal amount
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.ZERO_PRINCIPAL);
        
        // Enter loan duration
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(this, UITestUtils.PRINCIPAL_ERROR_LABEL_ID, ErrorMessages.PRINCIPAL_POSITIVE);
        
        LOGGER.info("Zero principal validation test completed successfully");
    }
    
    @Test
    @DisplayName("Test validation for empty principal amount")
    public void testEmptyPrincipalValidation() {
        LOGGER.info("Starting empty principal validation test");
        
        // Leave principal field empty
        
        // Enter loan duration
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(this, UITestUtils.PRINCIPAL_ERROR_LABEL_ID, ErrorMessages.PRINCIPAL_REQUIRED);
        
        LOGGER.info("Empty principal validation test completed successfully");
    }
    
    @Test
    @DisplayName("Test validation for invalid format principal amount")
    public void testInvalidFormatPrincipalValidation() {
        LOGGER.info("Starting invalid format principal validation test");
        
        // Enter invalid principal amount
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, "abc");
        
        // Enter loan duration
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(this, UITestUtils.PRINCIPAL_ERROR_LABEL_ID, ErrorMessages.PRINCIPAL_FORMAT);
        
        LOGGER.info("Invalid format principal validation test completed successfully");
    }
    
    @Test
    @DisplayName("Test validation for negative loan duration")
    public void testNegativeDurationValidation() {
        LOGGER.info("Starting negative duration validation test");
        
        // Enter principal amount
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        
        // Enter negative duration
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.NEGATIVE_DURATION);
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(this, UITestUtils.DURATION_ERROR_LABEL_ID, ErrorMessages.DURATION_POSITIVE);
        
        LOGGER.info("Negative duration validation test completed successfully");
    }
    
    @Test
    @DisplayName("Test validation for zero loan duration")
    public void testZeroDurationValidation() {
        LOGGER.info("Starting zero duration validation test");
        
        // Enter principal amount
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        
        // Enter zero duration
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.ZERO_DURATION);
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(this, UITestUtils.DURATION_ERROR_LABEL_ID, ErrorMessages.DURATION_POSITIVE);
        
        LOGGER.info("Zero duration validation test completed successfully");
    }
    
    @Test
    @DisplayName("Test validation for empty loan duration")
    public void testEmptyDurationValidation() {
        LOGGER.info("Starting empty duration validation test");
        
        // Enter principal amount
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        
        // Leave duration field empty
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(this, UITestUtils.DURATION_ERROR_LABEL_ID, ErrorMessages.DURATION_REQUIRED);
        
        LOGGER.info("Empty duration validation test completed successfully");
    }
    
    @Test
    @DisplayName("Test validation for decimal loan duration")
    public void testDecimalDurationValidation() {
        LOGGER.info("Starting decimal duration validation test");
        
        // Enter principal amount
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        
        // Enter decimal duration
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.DECIMAL_DURATION);
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify error message is displayed
        UITestUtils.verifyErrorMessageDisplayed(this, UITestUtils.DURATION_ERROR_LABEL_ID, ErrorMessages.DURATION_FORMAT);
        
        LOGGER.info("Decimal duration validation test completed successfully");
    }
    
    @Test
    @DisplayName("Test New Calculation button clears the form")
    public void testNewCalculationButton() {
        LOGGER.info("Starting new calculation button test");
        
        // Perform a calculation first
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify result is displayed
        UITestUtils.verifyResultDisplayed(this);
        
        // Click New Calculation button
        UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        
        // Verify input fields are cleared
        UITestUtils.verifyInputFieldsCleared(this);
        
        // Verify result is no longer displayed
        UITestUtils.verifyNodeNotVisible(this, UITestUtils.RESULT_LABEL_ID);
        
        LOGGER.info("New calculation button test completed successfully");
    }
    
    @Test
    @DisplayName("Test complete workflow from input to calculation to new calculation")
    public void testCompleteWorkflow() {
        LOGGER.info("Starting complete workflow test");
        
        // Perform first calculation
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify result of first calculation
        UITestUtils.verifyResultDisplayed(this);
        UITestUtils.verifyResultContains(this, UITestFixture.STANDARD_RESULT);
        
        // Clear and perform second calculation
        UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.LARGE_PRINCIPAL);
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify result of second calculation
        UITestUtils.verifyResultDisplayed(this);
        UITestUtils.verifyResultContains(this, UITestFixture.LARGE_PRINCIPAL_RESULT);
        
        LOGGER.info("Complete workflow test completed successfully");
    }
    
    @Test
    @DisplayName("Test UI response time for operations")
    public void testUIResponseTime() {
        LOGGER.info("Starting UI response time test");
        
        // Measure time to enter text in principal field
        long principalEnterTime = UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, UITestFixture.STANDARD_PRINCIPAL);
        UITestUtils.assertUIResponseTime(principalEnterTime);
        
        // Measure time to enter text in duration field
        long durationEnterTime = UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, UITestFixture.STANDARD_DURATION);
        UITestUtils.assertUIResponseTime(durationEnterTime);
        
        // Measure time to click calculate button
        long calculateClickTime = UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        UITestUtils.assertUIResponseTime(calculateClickTime);
        
        // Measure time to click new calculation button
        long newCalcClickTime = UITestUtils.clickButton(this, UITestUtils.NEW_CALCULATION_BUTTON_ID);
        UITestUtils.assertUIResponseTime(newCalcClickTime);
        
        LOGGER.info("UI response time test completed successfully");
    }
    
    @ParameterizedTest
    @MethodSource("calculationTestCases")
    @DisplayName("Test parameterized calculations with different inputs")
    public void testParameterizedCalculations(String principal, String duration, String expectedResult) {
        LOGGER.info("Starting parameterized calculation test with principal: " + principal + ", duration: " + duration);
        
        // Enter principal amount
        UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, principal);
        
        // Enter loan duration
        UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, duration);
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify result is displayed
        UITestUtils.verifyResultDisplayed(this);
        
        // Verify result contains expected text
        UITestUtils.verifyResultContains(this, expectedResult);
        
        LOGGER.info("Parameterized calculation test completed successfully");
    }
    
    public static List<Object[]> calculationTestCases() {
        List<Object[]> testCases = new ArrayList<>();
        
        // Add test case for standard calculation
        testCases.add(new Object[]{"10000.00", "5", "$483.65"});
        // Add test case for small principal
        testCases.add(new Object[]{"1000.00", "1", "$87.92"});
        // Add test case for large principal
        testCases.add(new Object[]{"100000.00", "10", "$1,210.04"});
        // Add test case for long duration
        testCases.add(new Object[]{"50000.00", "30", "$349.05"});
        // Add test case for short duration
        testCases.add(new Object[]{"5000.00", "1", "$439.58"});
        
        return testCases;
    }
    
    @ParameterizedTest
    @MethodSource("validationErrorTestCases")
    @DisplayName("Test parameterized validation errors with different inputs")
    public void testParameterizedValidationErrors(String principal, String duration, String errorField, String expectedErrorMessage) {
        LOGGER.info("Starting parameterized validation error test with principal: " + principal + ", duration: " + duration);
        
        // Enter principal amount
        if (principal != null && !principal.isEmpty()) {
            UITestUtils.enterTextInField(this, UITestUtils.PRINCIPAL_FIELD_ID, principal);
        }
        
        // Enter loan duration
        if (duration != null && !duration.isEmpty()) {
            UITestUtils.enterTextInField(this, UITestUtils.DURATION_FIELD_ID, duration);
        }
        
        // Click calculate button
        UITestUtils.clickButton(this, UITestUtils.CALCULATE_BUTTON_ID);
        
        // Verify error message is displayed for the appropriate field
        if ("principal".equals(errorField)) {
            UITestUtils.verifyErrorMessageDisplayed(this, UITestUtils.PRINCIPAL_ERROR_LABEL_ID, expectedErrorMessage);
        } else if ("duration".equals(errorField)) {
            UITestUtils.verifyErrorMessageDisplayed(this, UITestUtils.DURATION_ERROR_LABEL_ID, expectedErrorMessage);
        }
        
        LOGGER.info("Parameterized validation error test completed successfully");
    }
    
    public static List<Object[]> validationErrorTestCases() {
        List<Object[]> testCases = new ArrayList<>();
        
        // Add test case for negative principal
        testCases.add(new Object[]{"-1000.00", "5", "principal", ErrorMessages.PRINCIPAL_POSITIVE});
        // Add test case for zero principal
        testCases.add(new Object[]{"0.00", "5", "principal", ErrorMessages.PRINCIPAL_POSITIVE});
        // Add test case for empty principal
        testCases.add(new Object[]{"", "5", "principal", ErrorMessages.PRINCIPAL_REQUIRED});
        // Add test case for invalid format principal
        testCases.add(new Object[]{"abc", "5", "principal", ErrorMessages.PRINCIPAL_FORMAT});
        // Add test case for negative duration
        testCases.add(new Object[]{"10000.00", "-5", "duration", ErrorMessages.DURATION_POSITIVE});
        // Add test case for zero duration
        testCases.add(new Object[]{"10000.00", "0", "duration", ErrorMessages.DURATION_POSITIVE});
        // Add test case for empty duration
        testCases.add(new Object[]{"10000.00", "", "duration", ErrorMessages.DURATION_REQUIRED});
        // Add test case for decimal duration
        testCases.add(new Object[]{"10000.00", "2.5", "duration", ErrorMessages.DURATION_FORMAT});
        
        return testCases;
    }
    
    @Test
    @DisplayName("Test using predefined test cases from UITestFixture")
    public void testFixtureBasedTests() {
        LOGGER.info("Starting fixture-based tests");
        
        // Get standard UI test cases from UITestFixture
        List<UITestFixture.UITestCase> standardTestCases = UITestFixture.createStandardUITestCases();
        for (UITestFixture.UITestCase testCase : standardTestCases) {
            boolean success = UITestFixture.executeUITestCase(this, testCase);
            Assertions.assertTrue(success, "Test case should succeed: " + testCase.getName());
            
            // Clear for next test
            UITestUtils.performNewCalculation(this);
        }
        
        // Get invalid input UI test cases from UITestFixture
        List<UITestFixture.UITestCase> invalidInputTestCases = UITestFixture.createInvalidInputUITestCases();
        for (UITestFixture.UITestCase testCase : invalidInputTestCases) {
            boolean success = UITestFixture.executeUITestCase(this, testCase);
            Assertions.assertTrue(success, "Test case should succeed: " + testCase.getName());
            
            // Clear for next test
            UITestUtils.performNewCalculation(this);
        }
        
        // Get edge case UI test cases from UITestFixture
        List<UITestFixture.UITestCase> edgeCaseTestCases = UITestFixture.createEdgeCaseUITestCases();
        for (UITestFixture.UITestCase testCase : edgeCaseTestCases) {
            boolean success = UITestFixture.executeUITestCase(this, testCase);
            Assertions.assertTrue(success, "Test case should succeed: " + testCase.getName());
            
            // Clear for next test
            UITestUtils.performNewCalculation(this);
        }
        
        LOGGER.info("Fixture-based tests completed successfully");
    }
}