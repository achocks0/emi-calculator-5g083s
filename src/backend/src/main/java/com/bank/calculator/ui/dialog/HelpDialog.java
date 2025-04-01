package com.bank.calculator.ui.dialog;

import com.bank.calculator.constant.ErrorMessages;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Dialog component that provides contextual help information for the Compound Interest Calculator application.
 * This class displays help content for different aspects of the application, including
 * principal amount input, loan duration, interest rates, and calculation formulas.
 */
public class HelpDialog extends Dialog<Void> {
    private static final Logger LOGGER = Logger.getLogger(HelpDialog.class.getName());
    private static final String FXML_PATH = "/fxml/help-dialog.fxml";
    private static final String HELP_ICON_PATH = "/icons/help-icon.png";
    private static final String PRINCIPAL_TAB_ID = "principalTab";
    private static final String DURATION_TAB_ID = "durationTab";
    private static final String INTEREST_TAB_ID = "interestTab";
    private static final String CALCULATION_TAB_ID = "calculationTab";
    
    @FXML
    private TabPane tabPane;
    
    @FXML
    private Tab principalTab;
    
    @FXML
    private Tab durationTab;
    
    @FXML
    private Tab interestTab;
    
    @FXML
    private Tab calculationTab;
    
    /**
     * Constructs a new HelpDialog instance.
     * Loads the FXML layout and configures the dialog properties.
     */
    public HelpDialog() {
        super();
        setTitle(ErrorMessages.HELP_DIALOG_TITLE);
        
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(FXML_PATH));
            fxmlLoader.setController(this);
            DialogPane dialogPane = fxmlLoader.load();
            setDialogPane(dialogPane);
            getDialogPane().getButtonTypes().add(ButtonType.OK);
            
            // Set dialog icon
            try {
                Image helpIcon = new Image(getClass().getResourceAsStream(HELP_ICON_PATH));
                setGraphic(new ImageView(helpIcon));
            } catch (Exception e) {
                LOGGER.log(Level.WARNING, "Could not load help icon", e);
            }
            
            setResizable(true);
            LOGGER.info("HelpDialog created successfully");
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load help dialog FXML", e);
            throw new RuntimeException("Could not initialize help dialog", e);
        }
    }
    
    /**
     * Initializes the help dialog component after FXML loading.
     * Sets up tab content and styling for the dialog.
     */
    @FXML
    private void initialize() {
        LOGGER.fine("Initializing help dialog");
        
        // Ensure tabs have proper IDs set
        if (principalTab != null) principalTab.setId(PRINCIPAL_TAB_ID);
        if (durationTab != null) durationTab.setId(DURATION_TAB_ID);
        if (interestTab != null) interestTab.setId(INTEREST_TAB_ID);
        if (calculationTab != null) calculationTab.setId(CALCULATION_TAB_ID);
        
        // Set CSS class for styling
        if (tabPane != null) {
            tabPane.getStyleClass().add("help-tab-pane");
        }
        
        LOGGER.info("Help dialog initialized successfully");
    }
    
    /**
     * Shows help information for a specific input field.
     * Selects the appropriate tab based on the field ID and displays the dialog.
     * 
     * @param fieldId the ID of the field to show help for
     */
    public void showHelpForField(String fieldId) {
        LOGGER.fine("Showing help for field: " + fieldId);
        
        if (fieldId == null || fieldId.isEmpty()) {
            showGeneralHelp();
            return;
        }
        
        if (fieldId.contains("principal")) {
            selectTabById(PRINCIPAL_TAB_ID);
        } else if (fieldId.contains("duration")) {
            selectTabById(DURATION_TAB_ID);
        } else if (fieldId.contains("interest")) {
            selectTabById(INTEREST_TAB_ID);
        } else if (fieldId.contains("calculation")) {
            selectTabById(CALCULATION_TAB_ID);
        } else {
            // Default to first tab
            tabPane.getSelectionModel().select(0);
        }
        
        showAndWait();
        LOGGER.fine("Help dialog displayed for field: " + fieldId);
    }
    
    /**
     * Shows general help information about the application.
     * Selects the first tab (overview) and displays the dialog.
     */
    public void showGeneralHelp() {
        LOGGER.fine("Showing general help");
        
        // Select the first tab (overview)
        if (tabPane != null && !tabPane.getTabs().isEmpty()) {
            tabPane.getSelectionModel().select(0);
        }
        
        showAndWait();
        LOGGER.fine("General help dialog displayed");
    }
    
    /**
     * Selects a specific tab in the tab pane by its ID.
     * If the tab with the specified ID is not found, selects the first tab.
     * 
     * @param tabId the ID of the tab to select
     */
    private void selectTabById(String tabId) {
        if (tabPane == null || tabPane.getTabs().isEmpty()) {
            LOGGER.warning("TabPane is null or empty, cannot select tab");
            return;
        }
        
        for (Tab tab : tabPane.getTabs()) {
            if (tab.getId() != null && tab.getId().equals(tabId)) {
                tabPane.getSelectionModel().select(tab);
                return;
            }
        }
        
        LOGGER.warning("Tab with ID '" + tabId + "' not found. Selecting first tab.");
        tabPane.getSelectionModel().select(0);
    }
}