package com.bank.calculator.test.fixture;

import com.bank.calculator.service.ValidationService;
import com.bank.calculator.service.impl.ValidationServiceImpl;
import com.bank.calculator.model.ValidationResult;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.constant.ErrorMessages;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.util.TestDataGenerator;

import java.math.BigDecimal; // JDK 11
import java.util.List; // JDK 11
import java.util.ArrayList; // JDK 11
import java.util.Map; // JDK 11
import java.util.HashMap; // JDK 11
import java.util.logging.Logger; // JDK 11
import com.fasterxml.jackson.databind.ObjectMapper; // 2.13.0
import com.fasterxml.jackson.databind.JsonNode; // 2.13.0

/**
 * A test fixture class that provides predefined test cases and data for testing 
 * the validation functionality of the Compound Interest Calculator application.
 * This class creates and manages various validation test scenarios including
 * valid inputs, invalid inputs, boundary conditions, and edge cases.
 */
public class ValidationTestFixture {
    
    private static final Logger LOGGER = Logger.getLogger(ValidationTestFixture.class.getName());
    private static final String VALIDATION_TEST_CASES_FILE = "validation-test-cases.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final ValidationService validationService = new ValidationServiceImpl();
    
    /**
     * Inner class representing a test case for validation testing.
     */
    public static class ValidationTestCase {
        private String id;
        private String name;
        private String description;
        private String inputValue;
        private String validationType;
        private boolean expectedValidity;
        private String expectedErrorMessage;
        
        /**
         * Constructs a new ValidationTestCase with the specified parameters.
         */
        public ValidationTestCase(String id, String name, String description, 
                                 String inputValue, String validationType,
                                 boolean expectedValidity, String expectedErrorMessage) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.inputValue = inputValue;
            this.validationType = validationType;
            this.expectedValidity = expectedValidity;
            this.expectedErrorMessage = expectedErrorMessage;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getInputValue() { return inputValue; }
        public String getValidationType() { return validationType; }
        public boolean isExpectedValid() { return expectedValidity; }
        public String getExpectedErrorMessage() { return expectedErrorMessage; }
    }

    /**
     * Inner class representing a test case for combined validation testing
     * (principal and duration together).
     */
    public static class CombinedValidationTestCase {
        private String id;
        private String name;
        private String description;
        private String principalValue;
        private String durationValue;
        private boolean expectedValidity;
        private String expectedErrorMessage;
        
        /**
         * Constructs a new CombinedValidationTestCase with the specified parameters.
         */
        public CombinedValidationTestCase(String id, String name, String description, 
                                         String principalValue, String durationValue,
                                         boolean expectedValidity, String expectedErrorMessage) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.principalValue = principalValue;
            this.durationValue = durationValue;
            this.expectedValidity = expectedValidity;
            this.expectedErrorMessage = expectedErrorMessage;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getPrincipalValue() { return principalValue; }
        public String getDurationValue() { return durationValue; }
        public boolean isExpectedValid() { return expectedValidity; }
        public String getExpectedErrorMessage() { return expectedErrorMessage; }
    }

    /**
     * Inner class representing a test case for CalculationInput validation testing.
     */
    public static class CalculationInputValidationTestCase {
        private String id;
        private String name;
        private String description;
        private CalculationInput input;
        private boolean expectedValidity;
        private String expectedErrorMessage;
        
        /**
         * Constructs a new CalculationInputValidationTestCase with the specified parameters.
         */
        public CalculationInputValidationTestCase(String id, String name, String description, 
                                                CalculationInput input, boolean expectedValidity, 
                                                String expectedErrorMessage) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.input = input;
            this.expectedValidity = expectedValidity;
            this.expectedErrorMessage = expectedErrorMessage;
        }
        
        // Getters
        public String getId() { return id; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public CalculationInput getInput() { return input; }
        public boolean isExpectedValid() { return expectedValidity; }
        public String getExpectedErrorMessage() { return expectedErrorMessage; }
    }

    /**
     * Creates a list of test cases for principal amount validation testing
     * based on predefined test data.
     *
     * @return A list of principal validation test cases
     */
    public static List<ValidationTestCase> createPrincipalValidationTestCases() {
        LOGGER.info("Creating principal validation test cases");
        String jsonData = TestUtils.loadTestData(VALIDATION_TEST_CASES_FILE);
        List<ValidationTestCase> testCases = new ArrayList<>();
        
        try {
            List<JsonNode> principalTestNodes = loadTestCasesFromJson("principal");
            for (JsonNode node : principalTestNodes) {
                testCases.add(createValidationTestCaseFromJson(node));
            }
        } catch (Exception e) {
            LOGGER.severe("Error creating principal validation test cases: " + e.getMessage());
        }
        
        return testCases;
    }

    /**
     * Creates a list of test cases for loan duration validation testing
     * based on predefined test data.
     *
     * @return A list of duration validation test cases
     */
    public static List<ValidationTestCase> createDurationValidationTestCases() {
        LOGGER.info("Creating duration validation test cases");
        String jsonData = TestUtils.loadTestData(VALIDATION_TEST_CASES_FILE);
        List<ValidationTestCase> testCases = new ArrayList<>();
        
        try {
            List<JsonNode> durationTestNodes = loadTestCasesFromJson("duration");
            for (JsonNode node : durationTestNodes) {
                testCases.add(createValidationTestCaseFromJson(node));
            }
        } catch (Exception e) {
            LOGGER.severe("Error creating duration validation test cases: " + e.getMessage());
        }
        
        return testCases;
    }

    /**
     * Creates a list of test cases for combined validation testing
     * (principal and duration together) based on predefined test data.
     *
     * @return A list of combined validation test cases
     */
    public static List<CombinedValidationTestCase> createCombinedValidationTestCases() {
        LOGGER.info("Creating combined validation test cases");
        String jsonData = TestUtils.loadTestData(VALIDATION_TEST_CASES_FILE);
        List<CombinedValidationTestCase> testCases = new ArrayList<>();
        
        try {
            List<JsonNode> combinedTestNodes = loadTestCasesFromJson("combined");
            for (JsonNode node : combinedTestNodes) {
                testCases.add(createCombinedValidationTestCaseFromJson(node));
            }
        } catch (Exception e) {
            LOGGER.severe("Error creating combined validation test cases: " + e.getMessage());
        }
        
        return testCases;
    }

    /**
     * Creates a list of test cases for CalculationInput validation testing
     * based on predefined test data.
     *
     * @return A list of CalculationInput validation test cases
     */
    public static List<CalculationInputValidationTestCase> createCalculationInputValidationTestCases() {
        LOGGER.info("Creating CalculationInput validation test cases");
        String jsonData = TestUtils.loadTestData(VALIDATION_TEST_CASES_FILE);
        List<CalculationInputValidationTestCase> testCases = new ArrayList<>();
        
        try {
            List<JsonNode> calcInputTestNodes = loadTestCasesFromJson("calculationInput");
            for (JsonNode node : calcInputTestNodes) {
                testCases.add(createCalculationInputValidationTestCaseFromJson(node));
            }
        } catch (Exception e) {
            LOGGER.severe("Error creating CalculationInput validation test cases: " + e.getMessage());
        }
        
        return testCases;
    }
    
    /**
     * Creates a ValidationTestCase object from a JSON node containing test case data.
     *
     * @param jsonNode The JSON node containing test case data
     * @return A ValidationTestCase object created from the JSON data
     */
    private static ValidationTestCase createValidationTestCaseFromJson(JsonNode jsonNode) {
        String id = jsonNode.has("id") ? jsonNode.get("id").asText() : "unknown";
        String name = jsonNode.has("name") ? jsonNode.get("name").asText() : "Unnamed Test Case";
        String description = jsonNode.has("description") ? jsonNode.get("description").asText() : "";
        String inputValue = jsonNode.has("inputValue") ? jsonNode.get("inputValue").asText() : "";
        String validationType = jsonNode.has("validationType") ? jsonNode.get("validationType").asText() : "";
        boolean expectedValidity = jsonNode.has("expectedValid") ? jsonNode.get("expectedValid").asBoolean() : false;
        String expectedErrorMessage = jsonNode.has("expectedErrorMessage") ? jsonNode.get("expectedErrorMessage").asText() : "";
        
        return new ValidationTestCase(
            id, name, description, inputValue, validationType, expectedValidity, expectedErrorMessage
        );
    }

    /**
     * Creates a CombinedValidationTestCase object from a JSON node containing
     * combined validation test case data.
     *
     * @param jsonNode The JSON node containing combined test case data
     * @return A CombinedValidationTestCase object created from the JSON data
     */
    private static CombinedValidationTestCase createCombinedValidationTestCaseFromJson(JsonNode jsonNode) {
        String id = jsonNode.has("id") ? jsonNode.get("id").asText() : "unknown";
        String name = jsonNode.has("name") ? jsonNode.get("name").asText() : "Unnamed Test Case";
        String description = jsonNode.has("description") ? jsonNode.get("description").asText() : "";
        String principalValue = jsonNode.has("principalValue") ? jsonNode.get("principalValue").asText() : "";
        String durationValue = jsonNode.has("durationValue") ? jsonNode.get("durationValue").asText() : "";
        boolean expectedValidity = jsonNode.has("expectedValid") ? jsonNode.get("expectedValid").asBoolean() : false;
        String expectedErrorMessage = jsonNode.has("expectedErrorMessage") ? jsonNode.get("expectedErrorMessage").asText() : "";
        
        return new CombinedValidationTestCase(
            id, name, description, principalValue, durationValue, expectedValidity, expectedErrorMessage
        );
    }

    /**
     * Creates a CalculationInputValidationTestCase object from a JSON node containing
     * CalculationInput validation test case data.
     *
     * @param jsonNode The JSON node containing CalculationInput test case data
     * @return A CalculationInputValidationTestCase object created from the JSON data
     */
    private static CalculationInputValidationTestCase createCalculationInputValidationTestCaseFromJson(JsonNode jsonNode) {
        String id = jsonNode.has("id") ? jsonNode.get("id").asText() : "unknown";
        String name = jsonNode.has("name") ? jsonNode.get("name").asText() : "Unnamed Test Case";
        String description = jsonNode.has("description") ? jsonNode.get("description").asText() : "";
        
        // Extract principal and duration
        BigDecimal principal = jsonNode.has("principal") ? 
            new BigDecimal(jsonNode.get("principal").asText()) : 
            CalculationConstants.MIN_PRINCIPAL_AMOUNT;
        
        int duration = jsonNode.has("duration") ? 
            jsonNode.get("duration").asInt() : 
            CalculationConstants.MIN_DURATION_YEARS;
        
        // Create CalculationInput object
        CalculationInput input = new CalculationInput(principal, duration);
        
        boolean expectedValidity = jsonNode.has("expectedValid") ? jsonNode.get("expectedValid").asBoolean() : false;
        String expectedErrorMessage = jsonNode.has("expectedErrorMessage") ? jsonNode.get("expectedErrorMessage").asText() : "";
        
        return new CalculationInputValidationTestCase(
            id, name, description, input, expectedValidity, expectedErrorMessage
        );
    }

    /**
     * Executes a principal validation test case by validating the input and
     * comparing the result with the expected output.
     *
     * @param testCase The test case to execute
     * @return True if the test passes, false otherwise
     */
    public static boolean executePrincipalValidationTestCase(ValidationTestCase testCase) {
        LOGGER.info("Executing principal validation test case: " + testCase.getName());
        
        // Get input value and execute validation
        String inputValue = testCase.getInputValue();
        ValidationResult result = validationService.validatePrincipal(inputValue);
        
        // Compare result with expected outcome
        boolean valid = result.isValid() == testCase.isExpectedValid();
        
        // If not valid as expected, check if error message matches
        if (!testCase.isExpectedValid() && valid) {
            valid = result.getErrorMessage() != null &&
                    result.getErrorMessage().contains(testCase.getExpectedErrorMessage());
        }
        
        LOGGER.info("Test result: " + (valid ? "PASS" : "FAIL"));
        return valid;
    }

    /**
     * Executes a duration validation test case by validating the input and
     * comparing the result with the expected output.
     *
     * @param testCase The test case to execute
     * @return True if the test passes, false otherwise
     */
    public static boolean executeDurationValidationTestCase(ValidationTestCase testCase) {
        LOGGER.info("Executing duration validation test case: " + testCase.getName());
        
        // Get input value and execute validation
        String inputValue = testCase.getInputValue();
        ValidationResult result = validationService.validateDuration(inputValue);
        
        // Compare result with expected outcome
        boolean valid = result.isValid() == testCase.isExpectedValid();
        
        // If not valid as expected, check if error message matches
        if (!testCase.isExpectedValid() && valid) {
            valid = result.getErrorMessage() != null &&
                    result.getErrorMessage().contains(testCase.getExpectedErrorMessage());
        }
        
        LOGGER.info("Test result: " + (valid ? "PASS" : "FAIL"));
        return valid;
    }

    /**
     * Executes a combined validation test case by validating both principal and
     * duration inputs and comparing the result with the expected output.
     *
     * @param testCase The test case to execute
     * @return True if the test passes, false otherwise
     */
    public static boolean executeCombinedValidationTestCase(CombinedValidationTestCase testCase) {
        LOGGER.info("Executing combined validation test case: " + testCase.getName());
        
        // Get input values and execute validation
        String principalValue = testCase.getPrincipalValue();
        String durationValue = testCase.getDurationValue();
        ValidationResult result = validationService.validateAllInputs(principalValue, durationValue);
        
        // Compare result with expected outcome
        boolean valid = result.isValid() == testCase.isExpectedValid();
        
        // If not valid as expected, check if error message matches
        if (!testCase.isExpectedValid() && valid) {
            valid = result.getErrorMessage() != null &&
                    result.getErrorMessage().contains(testCase.getExpectedErrorMessage());
        }
        
        LOGGER.info("Test result: " + (valid ? "PASS" : "FAIL"));
        return valid;
    }

    /**
     * Executes a CalculationInput validation test case by validating the CalculationInput
     * object and comparing the result with the expected output.
     *
     * @param testCase The test case to execute
     * @return True if the test passes, false otherwise
     */
    public static boolean executeCalculationInputValidationTestCase(CalculationInputValidationTestCase testCase) {
        LOGGER.info("Executing CalculationInput validation test case: " + testCase.getName());
        
        // Get input object and execute validation
        CalculationInput input = testCase.getInput();
        ValidationResult result = validationService.validateCalculationInput(input);
        
        // Compare result with expected outcome
        boolean valid = result.isValid() == testCase.isExpectedValid();
        
        // If not valid as expected, check if error message matches
        if (!testCase.isExpectedValid() && valid) {
            valid = result.getErrorMessage() != null &&
                    result.getErrorMessage().contains(testCase.getExpectedErrorMessage());
        }
        
        LOGGER.info("Test result: " + (valid ? "PASS" : "FAIL"));
        return valid;
    }

    /**
     * Loads test cases from the JSON test data file based on the specified category.
     *
     * @param category The category of test cases to load
     * @return A list of JSON nodes containing test case data
     */
    private static List<JsonNode> loadTestCasesFromJson(String category) {
        LOGGER.info("Loading test cases from JSON for category: " + category);
        List<JsonNode> testCases = new ArrayList<>();
        String jsonData = TestUtils.loadTestData(VALIDATION_TEST_CASES_FILE);
        
        if (jsonData == null) {
            LOGGER.warning("Test data file not found: " + VALIDATION_TEST_CASES_FILE);
            return testCases;
        }
        
        try {
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode testCasesNode = rootNode.get("testCases");
            
            if (testCasesNode != null && testCasesNode.isArray()) {
                for (JsonNode testCase : testCasesNode) {
                    if (testCase.has("category") && 
                        testCase.get("category").asText().equals(category)) {
                        testCases.add(testCase);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.severe("Error parsing test case JSON: " + e.getMessage());
        }
        
        return testCases;
    }

    /**
     * Creates a set of predefined validation test cases for common validation scenarios
     * without relying on JSON data.
     *
     * @return A map of validation types to lists of test cases
     */
    public static Map<String, List<ValidationTestCase>> createPredefinedValidationTestCases() {
        Map<String, List<ValidationTestCase>> testCases = new HashMap<>();
        List<ValidationTestCase> principalTestCases = new ArrayList<>();
        List<ValidationTestCase> durationTestCases = new ArrayList<>();
        
        // Principal test cases
        principalTestCases.add(new ValidationTestCase(
            "P001", "Valid Principal", "A valid principal amount",
            "5000", "principal", true, null
        ));
        
        principalTestCases.add(new ValidationTestCase(
            "P002", "Negative Principal", "A negative principal amount",
            "-1000", "principal", false, ErrorMessages.PRINCIPAL_POSITIVE
        ));
        
        principalTestCases.add(new ValidationTestCase(
            "P003", "Zero Principal", "A zero principal amount",
            "0", "principal", false, ErrorMessages.PRINCIPAL_POSITIVE
        ));
        
        principalTestCases.add(new ValidationTestCase(
            "P004", "Below Minimum Principal", "A principal amount below the minimum threshold",
            "500", "principal", false, ErrorMessages.PRINCIPAL_MIN_REQUIRED
        ));
        
        principalTestCases.add(new ValidationTestCase(
            "P005", "Above Maximum Principal", "A principal amount above the maximum threshold",
            "2000000", "principal", false, ErrorMessages.PRINCIPAL_MAX_EXCEEDED
        ));
        
        principalTestCases.add(new ValidationTestCase(
            "P006", "Non-numeric Principal", "A non-numeric principal amount",
            "abc", "principal", false, ErrorMessages.PRINCIPAL_FORMAT
        ));
        
        // Duration test cases
        durationTestCases.add(new ValidationTestCase(
            "D001", "Valid Duration", "A valid loan duration",
            "5", "duration", true, null
        ));
        
        durationTestCases.add(new ValidationTestCase(
            "D002", "Negative Duration", "A negative loan duration",
            "-2", "duration", false, ErrorMessages.DURATION_FORMAT
        ));
        
        durationTestCases.add(new ValidationTestCase(
            "D003", "Zero Duration", "A zero loan duration",
            "0", "duration", false, ErrorMessages.DURATION_POSITIVE
        ));
        
        durationTestCases.add(new ValidationTestCase(
            "D004", "Below Minimum Duration", "A loan duration below the minimum threshold",
            "0", "duration", false, ErrorMessages.DURATION_POSITIVE
        ));
        
        durationTestCases.add(new ValidationTestCase(
            "D005", "Above Maximum Duration", "A loan duration above the maximum threshold",
            "31", "duration", false, ErrorMessages.DURATION_MAX_EXCEEDED
        ));
        
        durationTestCases.add(new ValidationTestCase(
            "D006", "Non-integer Duration", "A non-integer loan duration",
            "2.5", "duration", false, ErrorMessages.DURATION_FORMAT
        ));
        
        testCases.put("principal", principalTestCases);
        testCases.put("duration", durationTestCases);
        
        return testCases;
    }
}