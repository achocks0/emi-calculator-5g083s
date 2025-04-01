package com.bank.calculator.ui;

import javafx.application.Application; // JavaFX 11
import javafx.application.Platform; // JavaFX 11
import javafx.scene.Scene; // JavaFX 11
import javafx.scene.image.Image; // JavaFX 11
import javafx.scene.layout.BorderPane; // JavaFX 11
import javafx.scene.layout.VBox; // JavaFX 11
import javafx.stage.Stage; // JavaFX 11

import java.util.logging.Level; // JDK 11
import java.util.logging.Logger; // JDK 11

import com.bank.calculator.config.AppConfig;
import com.bank.calculator.controller.CalculatorController;
import com.bank.calculator.service.CalculationService;
import com.bank.calculator.service.ValidationService;
import com.bank.calculator.ui.component.ActionSection;
import com.bank.calculator.ui.component.InputSection;
import com.bank.calculator.ui.component.ResultSection;

/**
 * Main UI class for the Compound Interest Calculator application that integrates all UI components
 * and serves as the entry point for the JavaFX application. This class creates and manages the primary
 * stage, scene, and layout containers, and coordinates the interactions between UI components.
 */
public class CalculatorUI extends Application {

    private static final Logger LOGGER = Logger.getLogger(CalculatorUI.class.getName());
    private static final String APPLICATION_ICON_PATH = "/icons/calculator-icon.png";
    private static final double MIN_WIDTH = 600.0;
    private static final double MIN_HEIGHT = 400.0;
    private static final String CSS_PATH = "/css/application.css";

    /**
     * Main entry point for the JavaFX application
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        try {
            // Configure application logging
            AppConfig.configureLogging();
            LOGGER.info("Starting Compound Interest Calculator application");
            
            // Launch the JavaFX application
            launch(args);
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to start application", e);
        }
    }

    /**
     * JavaFX application start method that initializes and displays the primary stage
     *
     * @param primaryStage the primary stage for this application
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            LOGGER.info("Initializing application UI");
            
            // Set up the primary stage with title, icon, and minimum size
            setupPrimaryStage(primaryStage);
            
            // Create service instances using AppConfig
            ValidationService validationService = AppConfig.createValidationService();
            CalculationService calculationService = AppConfig.createCalculationService();
            
            // Create controller instance using AppConfig
            CalculatorController calculatorController = AppConfig.createCalculatorController(
                    validationService, calculationService);
            
            // Create UI components
            InputSection inputSection = new InputSection(validationService);
            ResultSection resultSection = new ResultSection();
            ActionSection actionSection = new ActionSection(calculatorController, inputSection, resultSection);
            
            // Create the main layout container
            BorderPane mainLayout = createMainLayout(inputSection, actionSection, resultSection);
            
            // Create a scene with the layout and apply CSS
            Scene scene = new Scene(mainLayout);
            try {
                String cssPath = getClass().getResource(CSS_PATH).toExternalForm();
                scene.getStylesheets().add(cssPath);
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Failed to load CSS file: " + CSS_PATH, e);
                // Application can continue without CSS
            }
            
            // Set the scene on the primary stage
            primaryStage.setScene(scene);
            
            // Show the primary stage
            primaryStage.show();
            
            LOGGER.info("Application UI initialized successfully");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Error initializing application UI", e);
            // Show error dialog or handle gracefully
            Platform.exit();
        }
    }

    /**
     * JavaFX application stop method that performs cleanup when the application is closing
     *
     * @throws Exception if an error occurs during cleanup
     */
    @Override
    public void stop() throws Exception {
        LOGGER.info("Application shutting down");
        // Perform any necessary cleanup operations
        super.stop();
    }

    /**
     * Creates and configures the main layout container for the application
     *
     * @param inputSection the input section component
     * @param actionSection the action section component
     * @param resultSection the result section component
     * @return the configured main layout container
     */
    private BorderPane createMainLayout(InputSection inputSection, ActionSection actionSection, ResultSection resultSection) {
        // Create a new BorderPane as the main layout container
        BorderPane mainLayout = new BorderPane();
        
        // Create a VBox for the top section containing input and action components
        VBox topSection = new VBox(10); // 10px spacing between children
        topSection.getStyleClass().add("top-section");
        topSection.setPadding(new javafx.geometry.Insets(15));
        
        // Add inputSection to the top VBox
        topSection.getChildren().add(inputSection);
        
        // Add actionSection to the top VBox
        topSection.getChildren().add(actionSection);
        
        // Set the top VBox as the top region of the BorderPane
        mainLayout.setTop(topSection);
        
        // Set resultSection as the center region of the BorderPane
        mainLayout.setCenter(resultSection);
        
        // Apply styling and spacing to the layout
        mainLayout.getStyleClass().add("main-layout");
        
        return mainLayout;
    }

    /**
     * Sets up the primary stage with title, icon, and minimum size
     *
     * @param primaryStage the primary stage to configure
     */
    private void setupPrimaryStage(Stage primaryStage) {
        // Set the stage title to AppConfig.getApplicationName()
        primaryStage.setTitle(AppConfig.getApplicationName());
        
        // Set application icon using APPLICATION_ICON_PATH
        try {
            Image icon = new Image(getClass().getResourceAsStream(APPLICATION_ICON_PATH));
            primaryStage.getIcons().add(icon);
        } catch (Exception e) {
            LOGGER.log(Level.WARNING, "Failed to load application icon: " + APPLICATION_ICON_PATH, e);
            // Application can continue without an icon
        }
        
        // Set minimum width and height for the stage
        primaryStage.setMinWidth(MIN_WIDTH);
        primaryStage.setMinHeight(MIN_HEIGHT);
        
        // Configure stage to exit application on close
        primaryStage.setOnCloseRequest(event -> {
            LOGGER.info("Application close requested by user");
            Platform.exit();
        });
        
        // Make the stage resizable
        primaryStage.setResizable(true);
    }
}