package com.bank.calculator.test.fixture;

import com.bank.calculator.test.util.UITestUtils;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.constant.ErrorMessages;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.JsonNode;

import org.testfx.api.FxRobot;

/**
 * Test fixture class that provides predefined test cases and data for UI testing in the Compound Interest Calculator application.
 * This class creates and manages various UI test scenarios including standard calculations, validation errors, edge cases,
 * and workflow tests to verify the application's user interface functionality.
 */
public class UITestFixture {

    private static final Logger LOGGER = Logger.getLogger(UITestFixture.class.getName());
    private static final String UI_TEST_CASES_FILE = "ui-test-cases.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    
    // Standard test values
    public static final String STANDARD_PRINCIPAL = "10000.00";
    public static final String STANDARD_DURATION = "5";
    public static final String STANDARD_RESULT = "$483.65";
    
    // Edge case test values
    public static final String LARGE_PRINCIPAL = "1000000.00";
    public static final String LARGE_PRINCIPAL_RESULT = "$20,037.80";
    public static final String NEGATIVE_PRINCIPAL = "-1000.00";
    public static final String ZERO_PRINCIPAL = "0.00";
    public static final String INVALID_FORMAT_PRINCIPAL = "abc";
    public static final String NEGATIVE_DURATION = "-5";
    public static final String ZERO_DURATION = "0";
    public static final String DECIMAL_DURATION = "2.5";
    public static final String MAX_DURATION = "30";
    public static final String MIN_DURATION = "1";
    
    // Performance thresholds
    public static final long MAX_UI_RESPONSE_TIME_MS = 100;
    public static final long MAX_CALCULATION_TIME_MS = 500;

    /**
     * Creates a list of standard UI test cases for testing basic calculation functionality in the UI.
     *
     * @return A list of standard UI test cases
     */
    public static List<UITestCase> createStandardUITestCases() {
        LOGGER.info("Creating standard UI test cases");
        
        try {
            // Load test data from JSON
            List<JsonNode> testCaseNodes = loadUITestCasesFromJson("Standard");
            List<UITestCase> testCases = new ArrayList<>();
            
            // Create test cases from JSON data
            for (JsonNode node : testCaseNodes) {
                testCases.add(createUITestCaseFromJson(node));
            }
            
            return testCases;
        } catch (Exception e) {
            LOGGER.severe("Error creating standard UI test cases: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Creates a list of UI test cases for testing validation error handling in the UI.
     *
     * @return A list of validation error UI test cases
     */
    public static List<UITestCase> createInvalidInputUITestCases() {
        LOGGER.info("Creating validation UI test cases");
        
        try {
            // Load test data from JSON
            List<JsonNode> testCaseNodes = loadUITestCasesFromJson("Validation");
            List<UITestCase> testCases = new ArrayList<>();
            
            // Create test cases from JSON data
            for (JsonNode node : testCaseNodes) {
                testCases.add(createUITestCaseFromJson(node));
            }
            
            return testCases;
        } catch (Exception e) {
            LOGGER.severe("Error creating validation UI test cases: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Creates a list of UI test cases for testing edge cases in the UI.
     *
     * @return A list of edge case UI test cases
     */
    public static List<UITestCase> createEdgeCaseUITestCases() {
        LOGGER.info("Creating edge case UI test cases");
        
        try {
            // Load test data from JSON
            List<JsonNode> testCaseNodes = loadUITestCasesFromJson("Edge Case");
            List<UITestCase> testCases = new ArrayList<>();
            
            // Create test cases from JSON data
            for (JsonNode node : testCaseNodes) {
                testCases.add(createUITestCaseFromJson(node));
            }
            
            return testCases;
        } catch (Exception e) {
            LOGGER.severe("Error creating edge case UI test cases: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Creates a list of UI test cases for testing complete workflows in the UI.
     *
     * @return A list of workflow UI test cases
     */
    public static List<UITestCase> createWorkflowUITestCases() {
        LOGGER.info("Creating workflow UI test cases");
        
        try {
            // Load test data from JSON
            List<JsonNode> testCaseNodes = loadUITestCasesFromJson("Workflow");
            List<UITestCase> testCases = new ArrayList<>();
            
            // Create test cases from JSON data
            for (JsonNode node : testCaseNodes) {
                testCases.add(createUITestCaseFromJson(node));
            }
            
            return testCases;
        } catch (Exception e) {
            LOGGER.severe("Error creating workflow UI test cases: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Creates a list of UI test cases for testing UI performance.
     *
     * @return A list of UI performance test cases
     */
    public static List<UIPerformanceTestCase> createPerformanceUITestCases() {
        LOGGER.info("Creating performance UI test cases");
        
        try {
            // Load test data from JSON
            List<JsonNode> testCaseNodes = loadUITestCasesFromJson("Performance");
            List<UIPerformanceTestCase> testCases = new ArrayList<>();
            
            // Create test cases from JSON data
            for (JsonNode node : testCaseNodes) {
                testCases.add(createUIPerformanceTestCaseFromJson(node));
            }
            
            return testCases;
        } catch (Exception e) {
            LOGGER.severe("Error creating performance UI test cases: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Creates a UITestCase object from a JSON node containing test case data.
     *
     * @param jsonNode The JSON node containing test case data
     * @return A UITestCase object created from the JSON data
     */
    private static UITestCase createUITestCaseFromJson(JsonNode jsonNode) {
        String id = jsonNode.get("id").asText();
        String name = jsonNode.get("name").asText();
        String description = jsonNode.get("description").asText();
        String category = jsonNode.get("category").asText();
        
        // Extract input parameters
        Map<String, String> inputParameters = new HashMap<>();
        JsonNode inputNode = jsonNode.get("input_parameters");
        if (inputNode != null && inputNode.isObject()) {
            inputNode.fields().forEachRemaining(entry -> 
                inputParameters.put(entry.getKey(), entry.getValue().asText()));
        }
        
        // Extract expected results
        Map<String, Object> expectedResults = new HashMap<>();
        JsonNode resultsNode = jsonNode.get("expected_results");
        if (resultsNode != null && resultsNode.isObject()) {
            resultsNode.fields().forEachRemaining(entry -> {
                String key = entry.getKey();
                JsonNode valueNode = entry.getValue();
                if (valueNode.isArray() && key.equals("error_messages")) {
                    List<String> errorMessages = new ArrayList<>();
                    valueNode.forEach(node -> errorMessages.add(node.asText()));
                    expectedResults.put(key, errorMessages);
                } else {
                    expectedResults.put(key, valueNode.asText());
                }
            });
        }
        
        // Extract UI actions
        List<UIAction> uiActions = new ArrayList<>();
        JsonNode actionsNode = jsonNode.get("ui_actions");
        if (actionsNode != null && actionsNode.isArray()) {
            actionsNode.forEach(actionNode -> {
                String action = actionNode.get("action").asText();
                String target = actionNode.get("target").asText();
                String value = actionNode.has("value") ? actionNode.get("value").asText() : "";
                String operation = actionNode.has("operation") ? actionNode.get("operation").asText() : "";
                uiActions.add(new UIAction(action, target, value, operation));
            });
        }
        
        return new UITestCase(id, name, description, category, inputParameters, expectedResults, uiActions);
    }

    /**
     * Creates a UIPerformanceTestCase object from a JSON node containing performance test case data.
     *
     * @param jsonNode The JSON node containing performance test case data
     * @return A UIPerformanceTestCase object created from the JSON data
     */
    private static UIPerformanceTestCase createUIPerformanceTestCaseFromJson(JsonNode jsonNode) {
        String id = jsonNode.get("id").asText();
        String name = jsonNode.get("name").asText();
        String description = jsonNode.get("description").asText();
        
        // Extract input parameters
        Map<String, String> inputParameters = new HashMap<>();
        JsonNode inputNode = jsonNode.get("input_parameters");
        if (inputNode != null && inputNode.isObject()) {
            inputNode.fields().forEachRemaining(entry -> 
                inputParameters.put(entry.getKey(), entry.getValue().asText()));
        }
        
        // Extract performance criteria
        long maxResponseTimeMs = jsonNode.has("max_response_time_ms") ? 
            jsonNode.get("max_response_time_ms").asLong() : MAX_UI_RESPONSE_TIME_MS;
        
        // Extract UI actions
        List<UIAction> uiActions = new ArrayList<>();
        JsonNode actionsNode = jsonNode.get("ui_actions");
        if (actionsNode != null && actionsNode.isArray()) {
            actionsNode.forEach(actionNode -> {
                String action = actionNode.get("action").asText();
                String target = actionNode.get("target").asText();
                String value = actionNode.has("value") ? actionNode.get("value").asText() : "";
                String operation = actionNode.has("operation") ? actionNode.get("operation").asText() : "";
                uiActions.add(new UIAction(action, target, value, operation));
            });
        }
        
        return new UIPerformanceTestCase(id, name, description, inputParameters, maxResponseTimeMs, uiActions);
    }

    /**
     * Executes a UI test case by performing the specified UI actions and verifying the results.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param testCase The UI test case to execute
     * @return True if the test passes, false otherwise
     */
    public static boolean executeUITestCase(FxRobot robot, UITestCase testCase) {
        LOGGER.info("Executing UI test case: " + testCase.getId() + " - " + testCase.getName());
        
        try {
            // Get input parameters and expected results
            String principal = testCase.getPrincipal();
            String duration = testCase.getDuration();
            String expectedResultText = testCase.getExpectedResultText();
            List<String> expectedErrorMessages = testCase.getExpectedErrorMessages();
            
            // Perform UI actions
            for (UIAction action : testCase.getUIActions()) {
                switch (action.getAction()) {
                    case "enter_text":
                        UITestUtils.enterTextInField(robot, action.getTarget(), action.getValue());
                        break;
                    case "click":
                        UITestUtils.clickButton(robot, action.getTarget());
                        break;
                    case "verify_result":
                        UITestUtils.verifyResultContains(robot, action.getValue());
                        break;
                    case "verify_error":
                        UITestUtils.verifyErrorMessageDisplayed(robot, action.getTarget(), action.getValue());
                        break;
                    default:
                        LOGGER.warning("Unknown UI action: " + action.getAction());
                }
            }
            
            // Verify results
            boolean testPassed = true;
            
            // Verify result text if expected
            if (expectedResultText != null && !expectedResultText.isEmpty()) {
                try {
                    UITestUtils.verifyResultContains(robot, expectedResultText);
                } catch (AssertionError e) {
                    LOGGER.warning("Result verification failed: " + e.getMessage());
                    testPassed = false;
                }
            }
            
            // Verify error messages if expected
            if (expectedErrorMessages != null && !expectedErrorMessages.isEmpty()) {
                for (String errorMessage : expectedErrorMessages) {
                    try {
                        // Determine which error label to check based on the error message
                        String errorLabelId = errorMessage.toLowerCase().contains("principal") ? 
                            UITestUtils.PRINCIPAL_ERROR_LABEL_ID : UITestUtils.DURATION_ERROR_LABEL_ID;
                        UITestUtils.verifyErrorMessageDisplayed(robot, errorLabelId, errorMessage);
                    } catch (AssertionError e) {
                        LOGGER.warning("Error message verification failed: " + e.getMessage());
                        testPassed = false;
                    }
                }
            }
            
            LOGGER.info("UI test case " + testCase.getId() + " " + (testPassed ? "PASSED" : "FAILED"));
            return testPassed;
        } catch (Exception e) {
            LOGGER.severe("Error executing UI test case: " + e.getMessage());
            return false;
        }
    }

    /**
     * Executes a UI performance test case by performing the specified UI actions and measuring response times.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param testCase The UI performance test case to execute
     * @return The performance test result
     */
    public static UIPerformanceResult executeUIPerformanceTestCase(FxRobot robot, UIPerformanceTestCase testCase) {
        LOGGER.info("Executing UI performance test case: " + testCase.getId() + " - " + testCase.getName());
        
        Map<String, Long> responseTimesByAction = new HashMap<>();
        
        try {
            // Get input parameters
            String principal = testCase.getPrincipal();
            String duration = testCase.getDuration();
            
            // Perform UI actions and measure response times
            for (UIAction action : testCase.getUIActions()) {
                long responseTime = 0;
                
                switch (action.getAction()) {
                    case "enter_text":
                        responseTime = UITestUtils.enterTextInField(robot, action.getTarget(), action.getValue());
                        break;
                    case "click":
                        responseTime = UITestUtils.clickButton(robot, action.getTarget());
                        break;
                    default:
                        LOGGER.warning("Unknown UI action: " + action.getAction());
                }
                
                // Record response time for this action
                String actionKey = action.getAction() + "_" + action.getTarget();
                responseTimesByAction.put(actionKey, responseTime);
                
                // Assert that the response time is within limits
                UITestUtils.assertUIResponseTime(responseTime);
            }
            
            // Create and return the performance result
            UIPerformanceResult result = new UIPerformanceResult(
                testCase.getId(), responseTimesByAction, testCase.getMaxResponseTimeMs());
            
            LOGGER.info("UI performance test case " + testCase.getId() + " " + 
                       (result.isPassed() ? "PASSED" : "FAILED") + " - Max response time: " + 
                       result.getMaxRecordedResponseTime() + "ms");
                       
            return result;
        } catch (Exception e) {
            LOGGER.severe("Error executing UI performance test case: " + e.getMessage());
            
            // Return a failed result
            return new UIPerformanceResult(testCase.getId(), responseTimesByAction, testCase.getMaxResponseTimeMs());
        }
    }

    /**
     * Performs a standard calculation test in the UI with the specified input values.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param principal The principal amount to enter
     * @param duration The loan duration to enter
     * @param expectedResult The expected result text
     * @return True if the test passes, false otherwise
     */
    public static boolean performStandardCalculationTest(FxRobot robot, String principal, String duration, String expectedResult) {
        LOGGER.info("Performing standard calculation test with principal: " + principal + ", duration: " + duration);
        
        try {
            // Perform the calculation
            UITestUtils.performCalculation(robot, principal, duration);
            
            // Verify the result
            UITestUtils.verifyResultContains(robot, expectedResult);
            
            return true;
        } catch (AssertionError e) {
            LOGGER.warning("Standard calculation test failed: " + e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.severe("Error in standard calculation test: " + e.getMessage());
            return false;
        }
    }

    /**
     * Performs a validation error test in the UI with the specified invalid input.
     *
     * @param robot The FxRobot instance for UI interaction
     * @param principal The principal amount to enter
     * @param duration The loan duration to enter
     * @param expectedErrorField The field expected to have an error (principal or duration)
     * @param expectedErrorMessage The expected error message
     * @return True if the test passes, false otherwise
     */
    public static boolean performValidationErrorTest(FxRobot robot, String principal, String duration, 
                                                   String expectedErrorField, String expectedErrorMessage) {
        LOGGER.info("Performing validation error test with principal: " + principal + ", duration: " + duration);
        
        try {
            // Perform the calculation
            UITestUtils.performCalculation(robot, principal, duration);
            
            // Determine which error label to check
            String errorLabelId = expectedErrorField.equalsIgnoreCase("principal") ? 
                UITestUtils.PRINCIPAL_ERROR_LABEL_ID : UITestUtils.DURATION_ERROR_LABEL_ID;
            
            // Verify the error message
            UITestUtils.verifyErrorMessageDisplayed(robot, errorLabelId, expectedErrorMessage);
            
            return true;
        } catch (AssertionError e) {
            LOGGER.warning("Validation error test failed: " + e.getMessage());
            return false;
        } catch (Exception e) {
            LOGGER.severe("Error in validation error test: " + e.getMessage());
            return false;
        }
    }

    /**
     * Loads UI test cases from the JSON test data file based on the specified category.
     *
     * @param category The category of test cases to load
     * @return A list of JSON nodes containing test case data
     */
    private static List<JsonNode> loadUITestCasesFromJson(String category) {
        LOGGER.info("Loading UI test cases from JSON for category: " + category);
        
        List<JsonNode> result = new ArrayList<>();
        
        try {
            // Load test data from file
            String jsonData = TestUtils.loadTestData(UI_TEST_CASES_FILE);
            if (jsonData == null || jsonData.isEmpty()) {
                LOGGER.warning("UI test cases JSON file is empty or not found");
                return result;
            }
            
            // Parse JSON
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode testCasesNode = rootNode.get("test_cases");
            
            if (testCasesNode != null && testCasesNode.isArray()) {
                // Filter by category
                testCasesNode.forEach(node -> {
                    if (node.has("category") && node.get("category").asText().equals(category)) {
                        result.add(node);
                    }
                });
            }
            
            LOGGER.info("Loaded " + result.size() + " test cases for category: " + category);
        } catch (Exception e) {
            LOGGER.severe("Error loading UI test cases from JSON: " + e.getMessage());
        }
        
        return result;
    }

    /**
     * Inner class representing a test case for UI testing.
     */
    public static class UITestCase {
        private String id;
        private String name;
        private String description;
        private String category;
        private Map<String, String> inputParameters;
        private Map<String, Object> expectedResults;
        private List<UIAction> uiActions;
        
        /**
         * Constructs a new UITestCase with the specified parameters.
         */
        public UITestCase(String id, String name, String description, String category,
                          Map<String, String> inputParameters,
                          Map<String, Object> expectedResults,
                          List<UIAction> uiActions) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.category = category;
            this.inputParameters = inputParameters;
            this.expectedResults = expectedResults;
            this.uiActions = uiActions;
        }
        
        /**
         * Returns the test case ID.
         *
         * @return The test case ID
         */
        public String getId() { 
            return id;
        }
        
        /**
         * Returns the test case name.
         *
         * @return The test case name
         */
        public String getName() { 
            return name;
        }
        
        /**
         * Returns the test case description.
         *
         * @return The test case description
         */
        public String getDescription() { 
            return description;
        }
        
        /**
         * Returns the test case category.
         *
         * @return The test case category
         */
        public String getCategory() { 
            return category;
        }
        
        /**
         * Returns the input parameters for the test case.
         *
         * @return The input parameters
         */
        public Map<String, String> getInputParameters() { 
            return inputParameters;
        }
        
        /**
         * Returns the expected results for the test case.
         *
         * @return The expected results
         */
        public Map<String, Object> getExpectedResults() { 
            return expectedResults;
        }
        
        /**
         * Returns the UI actions for the test case.
         *
         * @return The UI actions
         */
        public List<UIAction> getUIActions() { 
            return uiActions;
        }
        
        /**
         * Returns the principal amount input parameter.
         *
         * @return The principal amount
         */
        public String getPrincipal() { 
            return inputParameters.get("principal");
        }
        
        /**
         * Returns the loan duration input parameter.
         *
         * @return The loan duration
         */
        public String getDuration() { 
            return inputParameters.get("duration_years");
        }
        
        /**
         * Returns the expected result text.
         *
         * @return The expected result text
         */
        public String getExpectedResultText() { 
            return (String) expectedResults.get("result_contains");
        }
        
        /**
         * Returns the expected error messages.
         *
         * @return The expected error messages
         */
        @SuppressWarnings("unchecked")
        public List<String> getExpectedErrorMessages() { 
            return (List<String>) expectedResults.get("error_messages");
        }
    }

    /**
     * Inner class representing a UI action to be performed during a test.
     */
    public static class UIAction {
        private String action;
        private String target;
        private String value;
        private String operation;
        
        /**
         * Constructs a new UIAction with the specified parameters.
         */
        public UIAction(String action, String target, String value, String operation) {
            this.action = action;
            this.target = target;
            this.value = value;
            this.operation = operation;
        }
        
        /**
         * Returns the action type.
         *
         * @return The action type
         */
        public String getAction() { 
            return action;
        }
        
        /**
         * Returns the target element ID.
         *
         * @return The target element ID
         */
        public String getTarget() { 
            return target;
        }
        
        /**
         * Returns the value to be used in the action.
         *
         * @return The action value
         */
        public String getValue() { 
            return value;
        }
        
        /**
         * Returns the operation to be performed.
         *
         * @return The operation
         */
        public String getOperation() { 
            return operation;
        }
    }

    /**
     * Inner class representing a performance test case for UI testing.
     */
    public static class UIPerformanceTestCase {
        private String id;
        private String name;
        private String description;
        private Map<String, String> inputParameters;
        private long maxResponseTimeMs;
        private List<UIAction> uiActions;
        
        /**
         * Constructs a new UIPerformanceTestCase with the specified parameters.
         */
        public UIPerformanceTestCase(String id, String name, String description,
                                   Map<String, String> inputParameters,
                                   long maxResponseTimeMs,
                                   List<UIAction> uiActions) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.inputParameters = inputParameters;
            this.maxResponseTimeMs = maxResponseTimeMs;
            this.uiActions = uiActions;
        }
        
        /**
         * Returns the performance test case ID.
         *
         * @return The performance test case ID
         */
        public String getId() { 
            return id;
        }
        
        /**
         * Returns the performance test case name.
         *
         * @return The performance test case name
         */
        public String getName() { 
            return name;
        }
        
        /**
         * Returns the performance test case description.
         *
         * @return The performance test case description
         */
        public String getDescription() { 
            return description;
        }
        
        /**
         * Returns the input parameters for the performance test case.
         *
         * @return The input parameters
         */
        public Map<String, String> getInputParameters() { 
            return inputParameters;
        }
        
        /**
         * Returns the maximum allowed response time in milliseconds.
         *
         * @return The maximum allowed response time in milliseconds
         */
        public long getMaxResponseTimeMs() { 
            return maxResponseTimeMs;
        }
        
        /**
         * Returns the UI actions for the performance test case.
         *
         * @return The UI actions
         */
        public List<UIAction> getUIActions() { 
            return uiActions;
        }
        
        /**
         * Returns the principal amount input parameter.
         *
         * @return The principal amount
         */
        public String getPrincipal() { 
            return inputParameters.get("principal");
        }
        
        /**
         * Returns the loan duration input parameter.
         *
         * @return The loan duration
         */
        public String getDuration() { 
            return inputParameters.get("duration_years");
        }
    }

    /**
     * Inner class representing the result of a UI performance test case execution.
     */
    public static class UIPerformanceResult {
        private String testCaseId;
        private Map<String, Long> responseTimesByAction;
        private long maxResponseTimeMs;
        private boolean passed;
        
        /**
         * Constructs a new UIPerformanceResult with the specified parameters.
         */
        public UIPerformanceResult(String testCaseId, Map<String, Long> responseTimesByAction, long maxResponseTimeMs) {
            this.testCaseId = testCaseId;
            this.responseTimesByAction = responseTimesByAction;
            this.maxResponseTimeMs = maxResponseTimeMs;
            
            // Check if all response times are within limits
            this.passed = true;
            for (Long time : responseTimesByAction.values()) {
                if (time > maxResponseTimeMs) {
                    this.passed = false;
                    break;
                }
            }
        }
        
        /**
         * Returns the ID of the test case that was executed.
         *
         * @return The test case ID
         */
        public String getTestCaseId() { 
            return testCaseId;
        }
        
        /**
         * Returns the response times for each action.
         *
         * @return The response times by action
         */
        public Map<String, Long> getResponseTimesByAction() { 
            return responseTimesByAction;
        }
        
        /**
         * Returns the maximum allowed response time in milliseconds.
         *
         * @return The maximum allowed response time in milliseconds
         */
        public long getMaxResponseTimeMs() { 
            return maxResponseTimeMs;
        }
        
        /**
         * Returns whether the performance test passed (all response times within limits).
         *
         * @return True if the test passed, false otherwise
         */
        public boolean isPassed() { 
            return passed;
        }
        
        /**
         * Returns the maximum recorded response time across all actions.
         *
         * @return The maximum recorded response time
         */
        public long getMaxRecordedResponseTime() {
            long maxTime = 0;
            for (Long time : responseTimesByAction.values()) {
                if (time > maxTime) {
                    maxTime = time;
                }
            }
            return maxTime;
        }
    }
}