package com.bank.calculator;

import java.util.logging.Logger; // JDK 11
import java.util.logging.Level; // JDK 11

import com.bank.calculator.ui.CalculatorUI;
import com.bank.calculator.config.AppConfig;

/**
 * Main application class that serves as the entry point for the Compound Interest Calculator desktop application.
 * This class initializes the application, configures the environment, and launches the JavaFX UI.
 */
public class CompoundInterestCalculatorApp {

    private static final Logger LOGGER = Logger.getLogger(CompoundInterestCalculatorApp.class.getName());
    private static final String APPLICATION_STARTUP_MESSAGE = "Starting Compound Interest Calculator Application";
    private static final String APPLICATION_VERSION_MESSAGE = "Version: %s";

    /**
     * Private constructor to prevent instantiation as this is a utility class with static methods
     */
    private CompoundInterestCalculatorApp() {
        // Prevent instantiation
    }

    /**
     * Main entry point for the application that initializes the environment and launches the UI
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            // Configure application logging
            AppConfig.configureLogging();
            
            // Log application startup information
            displayStartupInfo();
            
            // Launch the JavaFX UI
            CalculatorUI.main(args);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to start application", e);
        }
    }

    /**
     * Displays and logs application startup information
     */
    private static void displayStartupInfo() {
        String appName = AppConfig.getApplicationName();
        String appVersion = AppConfig.getApplicationVersion();
        
        LOGGER.info(appName);
        LOGGER.info(APPLICATION_STARTUP_MESSAGE);
        LOGGER.info(String.format(APPLICATION_VERSION_MESSAGE, appVersion));
    }
}