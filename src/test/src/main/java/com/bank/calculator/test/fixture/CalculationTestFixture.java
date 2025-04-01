package com.bank.calculator.test.fixture;

// Internal imports
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.service.impl.CalculationServiceImpl;
import com.bank.calculator.model.CalculationInput;
import com.bank.calculator.model.CalculationResult;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.test.util.TestUtils;
import com.bank.calculator.test.util.TestDataGenerator;

// External imports
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
 * the calculation functionality of the Compound Interest Calculator application.
 * This class creates and manages various test scenarios including standard calculations,
 * edge cases, boundary conditions, and performance test cases.
 */
public class CalculationTestFixture {

    private static final Logger LOGGER = Logger.getLogger(CalculationTestFixture.class.getName());
    private static final String CALCULATION_TEST_CASES_FILE = "calculation-test-cases.json";
    private static final ObjectMapper objectMapper = new ObjectMapper();
    private static final CalculationService calculationService = new CalculationServiceImpl();

    /**
     * Inner class representing a test case for calculation testing.
     */
    public static class TestCase {
        private String id;
        private String name;
        private String description;
        private CalculationInput input;
        private BigDecimal expectedEmiAmount;
        private BigDecimal expectedTotalAmount;
        private BigDecimal expectedInterestAmount;
        
        /**
         * Constructs a new TestCase with the specified parameters.
         *
         * @param id The test case ID
         * @param name The test case name
         * @param description The test case description
         * @param input The calculation input
         * @param expectedEmiAmount The expected EMI amount
         * @param expectedTotalAmount The expected total amount
         * @param expectedInterestAmount The expected interest amount
         */
        public TestCase(String id, String name, String description, CalculationInput input,
                       BigDecimal expectedEmiAmount, BigDecimal expectedTotalAmount, BigDecimal expectedInterestAmount) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.input = input;
            this.expectedEmiAmount = expectedEmiAmount;
            this.expectedTotalAmount = expectedTotalAmount;
            this.expectedInterestAmount = expectedInterestAmount;
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
         * Returns the calculation input for the test case.
         *
         * @return The calculation input
         */
        public CalculationInput getInput() {
            return input;
        }
        
        /**
         * Returns the expected EMI amount for the test case.
         *
         * @return The expected EMI amount
         */
        public BigDecimal getExpectedEmiAmount() {
            return expectedEmiAmount;
        }
        
        /**
         * Returns the expected total amount for the test case.
         *
         * @return The expected total amount
         */
        public BigDecimal getExpectedTotalAmount() {
            return expectedTotalAmount;
        }
        
        /**
         * Returns the expected interest amount for the test case.
         *
         * @return The expected interest amount
         */
        public BigDecimal getExpectedInterestAmount() {
            return expectedInterestAmount;
        }
    }
    
    /**
     * Inner class representing a performance test case for calculation testing.
     */
    public static class PerformanceTestCase {
        private String id;
        private String name;
        private String description;
        private CalculationInput input;
        private BigDecimal expectedEmiAmount;
        private int iterations;
        private long maxExecutionTimeMs;
        
        /**
         * Constructs a new PerformanceTestCase with the specified parameters.
         *
         * @param id The performance test case ID
         * @param name The performance test case name
         * @param description The performance test case description
         * @param input The calculation input
         * @param expectedEmiAmount The expected EMI amount
         * @param iterations The number of iterations to run
         * @param maxExecutionTimeMs The maximum allowed execution time in milliseconds
         */
        public PerformanceTestCase(String id, String name, String description, CalculationInput input,
                                  BigDecimal expectedEmiAmount, int iterations, long maxExecutionTimeMs) {
            this.id = id;
            this.name = name;
            this.description = description;
            this.input = input;
            this.expectedEmiAmount = expectedEmiAmount;
            this.iterations = iterations;
            this.maxExecutionTimeMs = maxExecutionTimeMs;
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
         * Returns the calculation input for the performance test case.
         *
         * @return The calculation input
         */
        public CalculationInput getInput() {
            return input;
        }
        
        /**
         * Returns the expected EMI amount for the performance test case.
         *
         * @return The expected EMI amount
         */
        public BigDecimal getExpectedEmiAmount() {
            return expectedEmiAmount;
        }
        
        /**
         * Returns the number of iterations for the performance test case.
         *
         * @return The number of iterations
         */
        public int getIterations() {
            return iterations;
        }
        
        /**
         * Returns the maximum allowed execution time in milliseconds for the performance test case.
         *
         * @return The maximum allowed execution time in milliseconds
         */
        public long getMaxExecutionTimeMs() {
            return maxExecutionTimeMs;
        }
    }
    
    /**
     * Inner class representing the result of a performance test case execution.
     */
    public static class PerformanceResult {
        private String testCaseId;
        private long totalExecutionTimeMs;
        private double averageExecutionTimeMs;
        private int iterations;
        private boolean passed;
        
        /**
         * Constructs a new PerformanceResult with the specified parameters.
         *
         * @param testCaseId The ID of the test case that was executed
         * @param totalExecutionTimeMs The total execution time in milliseconds
         * @param iterations The number of iterations that were executed
         * @param maxExecutionTimeMs The maximum allowed execution time in milliseconds
         */
        public PerformanceResult(String testCaseId, long totalExecutionTimeMs, int iterations, long maxExecutionTimeMs) {
            this.testCaseId = testCaseId;
            this.totalExecutionTimeMs = totalExecutionTimeMs;
            this.iterations = iterations;
            this.averageExecutionTimeMs = (double) totalExecutionTimeMs / iterations;
            this.passed = averageExecutionTimeMs <= maxExecutionTimeMs;
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
         * Returns the total execution time in milliseconds for all iterations.
         *
         * @return The total execution time in milliseconds
         */
        public long getTotalExecutionTimeMs() {
            return totalExecutionTimeMs;
        }
        
        /**
         * Returns the average execution time in milliseconds per iteration.
         *
         * @return The average execution time in milliseconds
         */
        public double getAverageExecutionTimeMs() {
            return averageExecutionTimeMs;
        }
        
        /**
         * Returns the number of iterations that were executed.
         *
         * @return The number of iterations
         */
        public int getIterations() {
            return iterations;
        }
        
        /**
         * Returns whether the performance test passed (average execution time within limits).
         *
         * @return True if the test passed, false otherwise
         */
        public boolean isPassed() {
            return passed;
        }
    }
    
    /**
     * Creates a list of standard test cases for calculation testing based on predefined test data.
     *
     * @return A list of standard test cases
     */
    public static List<TestCase> createStandardTestCases() {
        TestUtils.logTestInfo("Creating standard test cases");
        List<TestCase> testCases = new ArrayList<>();
        
        try {
            List<JsonNode> jsonNodes = loadTestCasesFromJson("standard");
            for (JsonNode node : jsonNodes) {
                testCases.add(createTestCaseFromJson(node));
            }
        } catch (Exception e) {
            LOGGER.warning("Error creating standard test cases: " + e.getMessage());
        }
        
        return testCases;
    }
    
    /**
     * Creates a list of edge case test cases for calculation testing, including zero interest rate, minimum/maximum values, etc.
     *
     * @return A list of edge case test cases
     */
    public static List<TestCase> createEdgeCaseTestCases() {
        TestUtils.logTestInfo("Creating edge case test cases");
        List<TestCase> testCases = new ArrayList<>();
        
        try {
            List<JsonNode> jsonNodes = loadTestCasesFromJson("edge");
            for (JsonNode node : jsonNodes) {
                testCases.add(createTestCaseFromJson(node));
            }
        } catch (Exception e) {
            LOGGER.warning("Error creating edge case test cases: " + e.getMessage());
        }
        
        return testCases;
    }
    
    /**
     * Creates a list of boundary test cases for calculation testing, focusing on values at or near the boundaries of valid input ranges.
     *
     * @return A list of boundary test cases
     */
    public static List<TestCase> createBoundaryTestCases() {
        TestUtils.logTestInfo("Creating boundary test cases");
        List<TestCase> testCases = new ArrayList<>();
        
        try {
            List<JsonNode> jsonNodes = loadTestCasesFromJson("boundary");
            for (JsonNode node : jsonNodes) {
                testCases.add(createTestCaseFromJson(node));
            }
        } catch (Exception e) {
            LOGGER.warning("Error creating boundary test cases: " + e.getMessage());
        }
        
        return testCases;
    }
    
    /**
     * Creates a list of test cases specifically designed for performance testing of the calculation functionality.
     *
     * @return A list of performance test cases
     */
    public static List<PerformanceTestCase> createPerformanceTestCases() {
        TestUtils.logTestInfo("Creating performance test cases");
        List<PerformanceTestCase> testCases = new ArrayList<>();
        
        try {
            List<JsonNode> jsonNodes = loadTestCasesFromJson("performance");
            for (JsonNode node : jsonNodes) {
                testCases.add(createPerformanceTestCaseFromJson(node));
            }
        } catch (Exception e) {
            LOGGER.warning("Error creating performance test cases: " + e.getMessage());
        }
        
        return testCases;
    }
    
    /**
     * Executes a test case by running the calculation and comparing the result with the expected output.
     *
     * @param testCase The test case to execute
     * @return True if the test passes, false otherwise
     */
    public static boolean executeTestCase(TestCase testCase) {
        TestUtils.logTestInfo("Executing test case: " + testCase.getId() + " - " + testCase.getName());
        
        try {
            CalculationInput input = testCase.getInput();
            CalculationResult result = calculationService.calculateEMI(input);
            
            BigDecimal expected = testCase.getExpectedEmiAmount();
            BigDecimal actual = result.getEmiAmount();
            
            // Use TestUtils assertion method which might throw an AssertionError
            try {
                TestUtils.assertBigDecimalEquals(expected, actual);
                TestUtils.logTestInfo("Test passed: " + testCase.getId());
                return true;
            } catch (AssertionError e) {
                TestUtils.logTestWarning("Test failed: " + testCase.getId() + " - " + e.getMessage());
                return false;
            }
        } catch (Exception e) {
            TestUtils.logTestError("Error executing test case: " + testCase.getId(), e);
            return false;
        }
    }
    
    /**
     * Executes a performance test case by running the calculation multiple times and measuring the execution time.
     *
     * @param testCase The performance test case to execute
     * @return The performance test result
     */
    public static PerformanceResult executePerformanceTestCase(PerformanceTestCase testCase) {
        TestUtils.logTestInfo("Executing performance test case: " + testCase.getId() + " - " + testCase.getName());
        
        try {
            CalculationInput input = testCase.getInput();
            int iterations = testCase.getIterations();
            
            long startTime = System.currentTimeMillis();
            
            for (int i = 0; i < iterations; i++) {
                calculationService.calculateEMI(input);
            }
            
            long endTime = System.currentTimeMillis();
            long totalExecutionTime = endTime - startTime;
            
            PerformanceResult result = new PerformanceResult(
                testCase.getId(), totalExecutionTime, iterations, testCase.getMaxExecutionTimeMs());
            
            if (result.isPassed()) {
                TestUtils.logTestInfo("Performance test passed: " + testCase.getId() + 
                                    " - Avg time: " + result.getAverageExecutionTimeMs() + "ms");
            } else {
                TestUtils.logTestWarning("Performance test failed: " + testCase.getId() + 
                                       " - Avg time: " + result.getAverageExecutionTimeMs() + 
                                       "ms (max allowed: " + testCase.getMaxExecutionTimeMs() + "ms)");
            }
            
            return result;
        } catch (Exception e) {
            TestUtils.logTestError("Error executing performance test case: " + testCase.getId(), e);
            
            // Return a failed result
            return new PerformanceResult(
                testCase.getId(), 0, testCase.getIterations(), 0);
        }
    }
    
    /**
     * Creates a TestCase object from a JSON node containing test case data.
     *
     * @param jsonNode The JSON node containing test case data
     * @return A TestCase object created from the JSON data
     */
    private static TestCase createTestCaseFromJson(JsonNode jsonNode) {
        String id = jsonNode.get("id").asText();
        String name = jsonNode.get("name").asText();
        String description = jsonNode.get("description").asText();
        
        JsonNode inputNode = jsonNode.get("input");
        BigDecimal principal = new BigDecimal(inputNode.get("principal").asText());
        int duration = inputNode.get("durationYears").asInt();
        BigDecimal interestRate = new BigDecimal(inputNode.get("interestRate").asText());
        
        JsonNode expectedNode = jsonNode.get("expected");
        BigDecimal expectedEmiAmount = new BigDecimal(expectedNode.get("emiAmount").asText());
        BigDecimal expectedTotalAmount = new BigDecimal(expectedNode.get("totalAmount").asText());
        BigDecimal expectedInterestAmount = new BigDecimal(expectedNode.get("interestAmount").asText());
        
        CalculationInput input = new CalculationInput(principal, duration);
        input.setInterestRate(interestRate);
        
        return new TestCase(id, name, description, input, 
                           expectedEmiAmount, expectedTotalAmount, expectedInterestAmount);
    }
    
    /**
     * Creates a PerformanceTestCase object from a JSON node containing performance test case data.
     *
     * @param jsonNode The JSON node containing performance test case data
     * @return A PerformanceTestCase object created from the JSON data
     */
    private static PerformanceTestCase createPerformanceTestCaseFromJson(JsonNode jsonNode) {
        String id = jsonNode.get("id").asText();
        String name = jsonNode.get("name").asText();
        String description = jsonNode.get("description").asText();
        
        JsonNode inputNode = jsonNode.get("input");
        BigDecimal principal = new BigDecimal(inputNode.get("principal").asText());
        int duration = inputNode.get("durationYears").asInt();
        BigDecimal interestRate = new BigDecimal(inputNode.get("interestRate").asText());
        
        JsonNode expectedNode = jsonNode.get("expected");
        BigDecimal expectedEmiAmount = new BigDecimal(expectedNode.get("emiAmount").asText());
        
        JsonNode performanceNode = jsonNode.get("performance");
        int iterations = performanceNode.get("iterations").asInt();
        long maxExecutionTimeMs = performanceNode.get("maxExecutionTimeMs").asLong();
        
        CalculationInput input = new CalculationInput(principal, duration);
        input.setInterestRate(interestRate);
        
        return new PerformanceTestCase(id, name, description, input, 
                                     expectedEmiAmount, iterations, maxExecutionTimeMs);
    }
    
    /**
     * Loads test cases from the JSON test data file based on the specified category.
     *
     * @param category The category of test cases to load
     * @return A list of JSON nodes containing test case data
     */
    private static List<JsonNode> loadTestCasesFromJson(String category) {
        TestUtils.logTestInfo("Loading test cases from JSON for category: " + category);
        List<JsonNode> result = new ArrayList<>();
        
        try {
            String jsonData = TestUtils.loadTestData(CALCULATION_TEST_CASES_FILE);
            if (jsonData == null) {
                LOGGER.warning("Test data file not found: " + CALCULATION_TEST_CASES_FILE);
                return result;
            }
            
            JsonNode rootNode = objectMapper.readTree(jsonData);
            JsonNode testCasesNode = rootNode.get("testCases");
            
            if (testCasesNode != null && testCasesNode.isArray()) {
                for (JsonNode node : testCasesNode) {
                    JsonNode categoryNode = node.get("category");
                    if (categoryNode != null && categoryNode.asText().equals(category)) {
                        result.add(node);
                    }
                }
            }
        } catch (Exception e) {
            LOGGER.warning("Error loading test cases from JSON: " + e.getMessage());
        }
        
        return result;
    }
}