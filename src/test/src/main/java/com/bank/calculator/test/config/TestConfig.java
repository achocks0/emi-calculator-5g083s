package com.bank.calculator.test.config;

import java.math.BigDecimal; // JDK 11
import java.util.logging.Logger; // JDK 11
import java.util.logging.Level; // JDK 11
import java.io.File; // JDK 11
import java.util.Properties; // JDK 11
import java.io.FileInputStream; // JDK 11
import java.io.IOException; // JDK 11

import com.bank.calculator.service.CalculationService;
import com.bank.calculator.service.impl.CalculationServiceImpl;
import com.bank.calculator.service.ValidationService;
import com.bank.calculator.service.impl.ValidationServiceImpl;
import com.bank.calculator.controller.CalculatorController;
import com.bank.calculator.constant.CalculationConstants;
import com.bank.calculator.test.util.TestUtils;

/**
 * Configuration class for the test environment of the Compound Interest Calculator application.
 * This class provides centralized configuration, test service instances, and environment setup
 * for all test categories including unit, integration, UI, performance, and security tests.
 */
public class TestConfig {

    private static final Logger LOGGER = Logger.getLogger(TestConfig.class.getName());
    private static final String TEST_CONFIG_FILE = "test-config.properties";
    private static final String TEST_ENV_PROPERTY = "test.environment";
    private static final String DEFAULT_TEST_ENV = "development";
    private static final BigDecimal TEST_INTEREST_RATE = new BigDecimal("0.075");
    private static Properties testProperties = new Properties();
    private static boolean initialized = false;

    /**
     * Private constructor to prevent instantiation as this is a utility class with static methods
     */
    private TestConfig() {
        throw new AssertionError("TestConfig class should not be instantiated");
    }

    /**
     * Initializes the test environment by loading configuration properties and setting up required resources.
     */
    public static synchronized void initializeTestEnvironment() {
        if (initialized) {
            LOGGER.info("Test environment already initialized");
            return;
        }

        LOGGER.info("Initializing test environment");
        
        // Load test configuration properties from file
        testProperties = loadTestProperties();
        
        // Configure logging for the test environment
        configureTestLogging();
        
        // Create test data directories if they don't exist
        createTestDataDirectories();
        
        initialized = true;
        LOGGER.info("Test environment initialization complete");
    }
    
    /**
     * Loads test configuration properties from the properties file.
     * 
     * @return The loaded test properties
     */
    private static Properties loadTestProperties() {
        LOGGER.info("Loading test properties from " + TEST_CONFIG_FILE);
        Properties props = new Properties();
        
        try {
            // Try to load from classpath first
            File configFile = new File(TestUtils.TEST_RESOURCES_PATH + TEST_CONFIG_FILE);
            if (configFile.exists()) {
                try (FileInputStream fis = new FileInputStream(configFile)) {
                    props.load(fis);
                    LOGGER.info("Loaded test properties from file: " + configFile.getAbsolutePath());
                }
            } else {
                // If file doesn't exist, create default properties
                LOGGER.warning("Test configuration file not found: " + configFile.getAbsolutePath());
                LOGGER.info("Using default test properties");
                props.setProperty(TEST_ENV_PROPERTY, DEFAULT_TEST_ENV);
            }
        } catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error loading test properties", e);
            // Set default properties if loading fails
            props.setProperty(TEST_ENV_PROPERTY, DEFAULT_TEST_ENV);
        }
        
        return props;
    }
    
    /**
     * Configures logging for the test environment.
     */
    private static void configureTestLogging() {
        String testEnv = getTestEnvironment();
        
        // Set the root logger level based on test environment
        Level logLevel;
        if ("development".equals(testEnv)) {
            logLevel = Level.INFO;
        } else if ("ci".equals(testEnv)) {
            logLevel = Level.WARNING;
        } else {
            logLevel = Level.INFO; // Default log level
        }
        
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(logLevel);
        
        LOGGER.info("Logging configured for test environment: " + testEnv + " with level: " + logLevel);
    }
    
    /**
     * Creates directories for test data if they don't exist.
     */
    private static void createTestDataDirectories() {
        // Create the main test resources directory if it doesn't exist
        File testResourcesDir = new File(TestUtils.TEST_RESOURCES_PATH);
        if (!testResourcesDir.exists() && testResourcesDir.mkdirs()) {
            LOGGER.info("Created test resources directory: " + testResourcesDir.getAbsolutePath());
        }
        
        // Create the test data directory if it doesn't exist
        File testDataDir = new File(TestUtils.TEST_DATA_PATH);
        if (!testDataDir.exists() && testDataDir.mkdirs()) {
            LOGGER.info("Created test data directory: " + testDataDir.getAbsolutePath());
        }
        
        // Create subdirectories for different test categories
        String[] testDirs = {"unit", "integration", "performance", "security"};
        for (String dir : testDirs) {
            File categoryDir = new File(TestUtils.TEST_DATA_PATH + dir);
            if (!categoryDir.exists() && categoryDir.mkdirs()) {
                LOGGER.info("Created test category directory: " + categoryDir.getAbsolutePath());
            }
        }
    }
    
    /**
     * Gets the current test environment (development, CI, production).
     * 
     * @return The current test environment
     */
    public static String getTestEnvironment() {
        ensureInitialized();
        
        // Check system property first (for CI environment overrides)
        String env = System.getProperty(TEST_ENV_PROPERTY);
        if (env == null || env.isEmpty()) {
            // If not set as system property, get from test properties
            env = testProperties.getProperty(TEST_ENV_PROPERTY, DEFAULT_TEST_ENV);
        }
        
        return env;
    }
    
    /**
     * Creates and configures a new instance of the CalculationService for testing.
     * 
     * @return A configured instance of CalculationService for testing
     */
    public static CalculationService createCalculationService() {
        ensureInitialized();
        LOGGER.info("Creating test CalculationService instance");
        return new CalculationServiceImpl();
    }
    
    /**
     * Creates and configures a new instance of the ValidationService for testing.
     * 
     * @return A configured instance of ValidationService for testing
     */
    public static ValidationService createValidationService() {
        ensureInitialized();
        LOGGER.info("Creating test ValidationService instance");
        return new ValidationServiceImpl();
    }
    
    /**
     * Creates and configures a new instance of the CalculatorController for testing.
     * 
     * @param validationService The validation service to use in the controller
     * @param calculationService The calculation service to use in the controller
     * @return A configured instance of CalculatorController for testing
     */
    public static CalculatorController createCalculatorController(
            ValidationService validationService, 
            CalculationService calculationService) {
        ensureInitialized();
        LOGGER.info("Creating test CalculatorController instance");
        return new CalculatorController(validationService, calculationService);
    }
    
    /**
     * Gets the interest rate to use for tests.
     * 
     * @return The test interest rate
     */
    public static BigDecimal getTestInterestRate() {
        return TEST_INTEREST_RATE;
    }
    
    /**
     * Gets a test configuration property value.
     * 
     * @param propertyName The name of the property to get
     * @return The property value or null if not found
     */
    public static String getTestProperty(String propertyName) {
        ensureInitialized();
        return testProperties.getProperty(propertyName);
    }
    
    /**
     * Gets a test configuration property value with a default value if not found.
     * 
     * @param propertyName The name of the property to get
     * @param defaultValue The default value to return if the property is not found
     * @return The property value or the default value if not found
     */
    public static String getTestPropertyWithDefault(String propertyName, String defaultValue) {
        ensureInitialized();
        return testProperties.getProperty(propertyName, defaultValue);
    }
    
    /**
     * Resets the test environment for a clean state.
     */
    public static synchronized void resetTestEnvironment() {
        LOGGER.info("Resetting test environment");
        
        // Clear any cached test data
        testProperties.clear();
        
        initialized = false;
        LOGGER.info("Test environment has been reset");
    }
    
    /**
     * Ensures that the test environment is initialized before use.
     */
    private static void ensureInitialized() {
        if (!initialized) {
            initializeTestEnvironment();
        }
    }
}