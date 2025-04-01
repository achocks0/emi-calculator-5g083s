package com.bank.calculator.config;

import java.math.BigDecimal; // JDK 11
import java.util.logging.Logger; // JDK 11
import java.util.logging.Level; // JDK 11

import com.bank.calculator.service.CalculationService;
import com.bank.calculator.service.impl.CalculationServiceImpl;
import com.bank.calculator.service.ValidationService;
import com.bank.calculator.service.impl.ValidationServiceImpl;
import com.bank.calculator.controller.CalculatorController;

/**
 * Configuration class that provides centralized configuration for the Compound Interest Calculator application.
 * This class is responsible for creating and configuring service beans, setting up application constants,
 * and providing a single point of access for application-wide configuration settings.
 */
public class AppConfig {

    private static final Logger LOGGER = Logger.getLogger(AppConfig.class.getName());
    
    /**
     * The default annual interest rate used for calculations (7.5%).
     */
    private static final BigDecimal DEFAULT_ANNUAL_INTEREST_RATE = new BigDecimal("7.5");
    
    /**
     * The default compounding frequency used for calculations (12 times per year, or monthly).
     */
    private static final int DEFAULT_COMPOUNDING_FREQUENCY = 12;
    
    /**
     * The application name.
     */
    private static final String APPLICATION_NAME = "Compound Interest Calculator";
    
    /**
     * The application version.
     */
    private static final String APPLICATION_VERSION = "1.0.0";
    
    /**
     * Private constructor to prevent instantiation as this is a utility class with static methods.
     */
    private AppConfig() {
        // Private constructor to prevent instantiation
    }
    
    /**
     * Creates and configures a new instance of the CalculationService.
     *
     * @return A configured instance of CalculationService
     */
    public static CalculationService createCalculationService() {
        LOGGER.info("Creating new instance of CalculationService");
        return new CalculationServiceImpl();
    }
    
    /**
     * Creates and configures a new instance of the ValidationService.
     *
     * @return A configured instance of ValidationService
     */
    public static ValidationService createValidationService() {
        LOGGER.info("Creating new instance of ValidationService");
        return new ValidationServiceImpl();
    }
    
    /**
     * Creates and configures a new instance of the CalculatorController with the provided services.
     *
     * @param validationService The ValidationService instance to use
     * @param calculationService The CalculationService instance to use
     * @return A configured instance of CalculatorController
     */
    public static CalculatorController createCalculatorController(ValidationService validationService, 
                                                                  CalculationService calculationService) {
        LOGGER.info("Creating new instance of CalculatorController");
        return new CalculatorController(validationService, calculationService);
    }
    
    /**
     * Returns the default annual interest rate used for calculations.
     *
     * @return The default annual interest rate as a percentage
     */
    public static BigDecimal getDefaultAnnualInterestRate() {
        return DEFAULT_ANNUAL_INTEREST_RATE;
    }
    
    /**
     * Returns the default compounding frequency used for calculations.
     *
     * @return The default compounding frequency (times per year)
     */
    public static int getDefaultCompoundingFrequency() {
        return DEFAULT_COMPOUNDING_FREQUENCY;
    }
    
    /**
     * Returns the application name.
     *
     * @return The application name
     */
    public static String getApplicationName() {
        return APPLICATION_NAME;
    }
    
    /**
     * Returns the application version.
     *
     * @return The application version
     */
    public static String getApplicationVersion() {
        return APPLICATION_VERSION;
    }
    
    /**
     * Configures the application logging settings.
     * Sets up log levels, formatters, and handlers for consistent logging across the application.
     */
    public static void configureLogging() {
        // Set the root logger level
        Logger rootLogger = Logger.getLogger("");
        rootLogger.setLevel(Level.INFO);
        
        // Configure console handler format if needed
        // This is a basic setup that can be expanded based on requirements
        LOGGER.info("Logging configuration completed");
    }
}