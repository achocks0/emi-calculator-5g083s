# Compound Interest Calculator - Developer Guide

**Version:** 1.0.0  
**Date:** 2023

## Table of Contents

- [Introduction](#introduction)
  - [Purpose](#purpose)
  - [Audience](#audience)
  - [Project Overview](#project-overview)
- [Development Environment Setup](#development-environment-setup)
  - [Prerequisites](#prerequisites)
  - [Repository Setup](#repository-setup)
  - [IDE Configuration](#ide-configuration)
  - [Building the Project](#building-the-project)
  - [Running the Application](#running-the-application)
- [Project Structure](#project-structure)
  - [Directory Structure](#directory-structure)
  - [Key Components](#key-components)
  - [Configuration Files](#configuration-files)
- [Architecture Overview](#architecture-overview)
  - [Architectural Layers](#architectural-layers)
  - [Component Interactions](#component-interactions)
  - [Key Design Patterns](#key-design-patterns)
- [Core Components](#core-components)
  - [User Interface (UI)](#user-interface-ui)
  - [Controller](#controller)
  - [Services](#services)
  - [Models](#models)
  - [Utilities](#utilities)
- [Development Guidelines](#development-guidelines)
  - [Coding Standards](#coding-standards)
  - [Error Handling](#error-handling)
  - [Logging](#logging)
  - [Testing](#testing)
- [Implementation Details](#implementation-details)
  - [Compound Interest Calculation](#compound-interest-calculation)
  - [EMI Calculation](#emi-calculation)
  - [Input Validation](#input-validation)
  - [UI Implementation](#ui-implementation)
- [Build and Deployment](#build-and-deployment)
  - [Build Process](#build-process)
  - [Packaging](#packaging)
  - [Deployment](#deployment)
  - [Continuous Integration](#continuous-integration)
- [Extending the Application](#extending-the-application)
  - [Adding New Calculation Types](#adding-new-calculation-types)
  - [Customizing the UI](#customizing-the-ui)
  - [Adding New Input Fields](#adding-new-input-fields)
  - [Internationalization](#internationalization)
- [Troubleshooting](#troubleshooting)
  - [Common Build Issues](#common-build-issues)
  - [Runtime Issues](#runtime-issues)
  - [Debugging Tips](#debugging-tips)
- [API Reference](#api-reference)
  - [CalculationService API](#calculationservice-api)
  - [ValidationService API](#validationservice-api)
  - [Model Classes](#model-classes)
  - [Utility Classes](#utility-classes)
- [Appendix](#appendix)
  - [Glossary](#glossary)
  - [References](#references)
  - [Calculation Formulas](#calculation-formulas)

## Introduction

This developer guide provides comprehensive documentation for developers working on the Compound Interest Calculator application. It covers development environment setup, architecture overview, coding standards, and guidelines for extending and maintaining the application.

### Purpose

The purpose of this guide is to provide developers with the necessary information to understand, develop, and maintain the Compound Interest Calculator application.

### Audience

This guide is intended for software developers, testers, and technical leads who are involved in the development and maintenance of the Compound Interest Calculator application.

### Project Overview

The Compound Interest Calculator is a desktop application designed for the banking division to calculate Equated Monthly Installments (EMI) based on principal amount and loan duration. The application is built using Java 11 and JavaFX 11, following a layered architecture with MVC design principles.

## Development Environment Setup

This section provides instructions for setting up the development environment for the Compound Interest Calculator application.

### Prerequisites

Before setting up the development environment, ensure you have the following prerequisites installed:

- JDK 11 or higher
- Maven 3.8.6 or higher
- Git
- IDE (Eclipse or IntelliJ IDEA recommended)

### Repository Setup

1. Clone the repository from the bank's Git server:
   ```bash
   git clone https://git.bank.com/calculator/compound-interest-calculator.git
   ```
2. Navigate to the project directory:
   ```bash
   cd compound-interest-calculator
   ```

### IDE Configuration

#### Eclipse Setup
1. Import the project as a Maven project
2. Ensure Eclipse is configured to use JDK 11
3. Install the e(fx)clipse plugin for JavaFX support

#### IntelliJ IDEA Setup
1. Import the project as a Maven project
2. Ensure IntelliJ is configured to use JDK 11
3. Install the JavaFX plugin if not already included

### Building the Project

To build the project, run the following Maven command:
```bash
mvn clean install
```

This will compile the code, run the tests, and package the application.

### Running the Application

To run the application from the development environment:

#### Using Maven
```bash
mvn javafx:run
```

#### Using IDE
Run the `com.bank.calculator.CompoundInterestCalculatorApp` class as a Java application.

## Project Structure

This section describes the structure of the project and the purpose of each directory and key file.

### Directory Structure

```
src/
├── main/
│   ├── java/
│   │   └── com/
│   │       └── bank/
│   │           └── calculator/
│   │               ├── CompoundInterestCalculatorApp.java
│   │               ├── config/
│   │               ├── constant/
│   │               ├── controller/
│   │               ├── exception/
│   │               ├── model/
│   │               ├── service/
│   │               │   ├── impl/
│   │               │   ├── CalculationService.java
│   │               │   └── ValidationService.java
│   │               ├── ui/
│   │               │   ├── component/
│   │               │   ├── dialog/
│   │               │   ├── formatter/
│   │               │   ├── validator/
│   │               │   └── CalculatorUI.java
│   │               └── util/
│   └── resources/
│       ├── css/
│       ├── fxml/
│       └── icons/
└── test/
    ├── java/
    │   └── com/
    │       └── bank/
    │           └── calculator/
    │               ├── controller/
    │               ├── service/
    │               ├── ui/
    │               └── util/
    └── resources/
        └── test-data/
```

### Key Components

- **CompoundInterestCalculatorApp.java**: Main application entry point
- **config/**: Application configuration classes
- **constant/**: Constant values used throughout the application
- **controller/**: Controller classes that coordinate between UI and services
- **exception/**: Custom exception classes
- **model/**: Data model classes
- **service/**: Service interfaces and implementations for business logic
- **ui/**: User interface components and JavaFX classes
- **util/**: Utility classes for common operations

### Configuration Files

- **pom.xml**: Maven project configuration
- **src/main/resources/css/**: CSS files for styling the UI
- **src/main/resources/fxml/**: FXML files for UI layout
- **src/main/resources/icons/**: Icon files for the application

## Architecture Overview

The Compound Interest Calculator follows a layered architecture with Model-View-Controller (MVC) design principles. This section provides an overview of the architecture and its components.

### Architectural Layers

The application is divided into the following layers:

1. **Presentation Layer (View)**: JavaFX UI components
2. **Controller Layer**: Coordinates between UI and services
3. **Service Layer**: Business logic and calculations
4. **Model Layer**: Data structures and domain objects

For a detailed architecture description, refer to the [Architecture Documentation](architecture.md).

### Component Interactions

The components interact as follows:

1. User interacts with the UI components
2. UI components notify the Controller of user actions
3. Controller validates inputs using the Validation Service
4. Controller delegates calculations to the Calculation Service
5. Calculation Service performs calculations and returns results
6. Controller formats results and updates the UI

This separation of concerns ensures that each component has a single responsibility and can be developed, tested, and maintained independently.

### Key Design Patterns

The application uses the following design patterns:

- **Model-View-Controller (MVC)**: Separates the application into three interconnected components
- **Factory Method**: Used in AppConfig to create service instances
- **Strategy**: Different calculation strategies based on input parameters
- **Observer**: JavaFX property binding for UI updates

## Core Components

This section provides detailed information about the core components of the application and their responsibilities.

### User Interface (UI)

The UI is implemented using JavaFX and consists of the following main components:

- **CalculatorUI**: Main UI class that extends JavaFX Application
- **InputSection**: Component for collecting principal amount and loan duration
- **ActionSection**: Component with calculation buttons
- **ResultSection**: Component for displaying calculation results

The UI is designed to be simple, intuitive, and responsive, following the design principles specified in the requirements.

### Controller

The Controller coordinates between the UI and service layers:

- **CalculatorController**: Main controller class that handles user actions, validates inputs, and delegates to services

The Controller is responsible for:
1. Validating user inputs using the Validation Service
2. Converting validated inputs to appropriate data types
3. Delegating calculations to the Calculation Service
4. Formatting results for display in the UI
5. Handling errors and providing feedback to the user

### Services

The application has two main services:

- **ValidationService**: Validates user inputs before processing
- **CalculationService**: Performs compound interest and EMI calculations

These services are defined as interfaces with concrete implementations in the `impl` package, following the dependency inversion principle.

### Models

The application uses the following model classes:

- **CalculationInput**: Encapsulates input parameters for calculations
- **CalculationResult**: Encapsulates calculation results
- **ValidationResult**: Encapsulates validation results

These models are immutable and provide methods for accessing their properties.

### Utilities

The application includes several utility classes:

- **BigDecimalUtils**: Utilities for working with BigDecimal for precise financial calculations
- **CurrencyUtils**: Utilities for formatting currency values
- **ValidationUtils**: Utilities for common validation operations

## Development Guidelines

This section provides guidelines for developing and extending the Compound Interest Calculator application.

### Coding Standards

All code should follow these standards:

- Use Java code conventions for naming and formatting
- Follow the Google Java Style Guide
- Use meaningful variable and method names
- Include JavaDoc comments for all public classes and methods
- Keep methods small and focused on a single responsibility
- Write unit tests for all new code

### Error Handling

The application uses a structured approach to error handling:

- Use custom exception classes for specific error scenarios
- Validate all inputs before processing
- Provide clear error messages for validation failures
- Log all exceptions with appropriate context
- Handle exceptions at the appropriate level
- Never expose internal exceptions to the user interface

### Logging

The application uses Java's built-in logging framework:

- Use appropriate log levels (INFO, WARNING, SEVERE)
- Include context information in log messages
- Log all exceptions with stack traces
- Configure logging in the AppConfig class
- Avoid excessive logging in performance-critical code

### Testing

All code should be thoroughly tested:

- Write unit tests for all services and utilities
- Use JUnit 5 for testing
- Use Mockito for mocking dependencies
- Aim for at least 85% code coverage
- Include tests for edge cases and error scenarios
- Use parameterized tests for testing multiple input scenarios

## Implementation Details

This section provides detailed information about the implementation of key features in the application.

### Compound Interest Calculation

The compound interest calculation is implemented in the `CalculationServiceImpl` class using the formula:

```java
// A = P(1 + r/n)^(nt)
BigDecimal rate = interestRate.divide(new BigDecimal("100"), MathContext.DECIMAL128);
BigDecimal compoundingFrequency = new BigDecimal("12"); // Monthly compounding
BigDecimal ratePerPeriod = rate.divide(compoundingFrequency, MathContext.DECIMAL128);
BigDecimal totalPeriods = compoundingFrequency.multiply(new BigDecimal(durationYears));
BigDecimal compoundFactor = BigDecimal.ONE.add(ratePerPeriod).pow(totalPeriods.intValue(), MathContext.DECIMAL128);
BigDecimal finalAmount = principal.multiply(compoundFactor, MathContext.DECIMAL128);
```

The calculation uses `BigDecimal` for precise financial calculations and handles edge cases such as zero interest rate.

### EMI Calculation

The EMI calculation is implemented in the `CalculationServiceImpl` class using the formula:

```java
// EMI = [P × r × (1 + r)^n]/[(1 + r)^n - 1]
BigDecimal monthlyRate = interestRate.divide(new BigDecimal("1200"), MathContext.DECIMAL128);
int totalMonths = durationYears * 12;

if (monthlyRate.compareTo(BigDecimal.ZERO) == 0) {
    // Special case: zero interest rate
    emiAmount = principal.divide(new BigDecimal(totalMonths), MathContext.DECIMAL128);
} else {
    // Standard EMI formula
    BigDecimal rateFactorPower = BigDecimal.ONE.add(monthlyRate).pow(totalMonths, MathContext.DECIMAL128);
    BigDecimal numerator = principal.multiply(monthlyRate).multiply(rateFactorPower, MathContext.DECIMAL128);
    BigDecimal denominator = rateFactorPower.subtract(BigDecimal.ONE);
    emiAmount = numerator.divide(denominator, MathContext.DECIMAL128);
}
```

The calculation handles the special case of zero interest rate and uses `BigDecimal` for precise financial calculations.

### Input Validation

Input validation is implemented in the `ValidationServiceImpl` class:

```java
@Override
public ValidationResult validatePrincipal(String principalStr) {
    if (principalStr == null || principalStr.trim().isEmpty()) {
        return ValidationResult.createError("Principal amount is required");
    }
    
    if (!principalStr.matches("^\\d+(\\.\\d{1,2})?$")) {
        return ValidationResult.createError("Principal must be a positive number with up to 2 decimal places");
    }
    
    try {
        BigDecimal principal = new BigDecimal(principalStr);
        if (principal.compareTo(BigDecimal.ZERO) <= 0) {
            return ValidationResult.createError("Principal amount must be greater than zero");
        }
    } catch (NumberFormatException e) {
        return ValidationResult.createError("Invalid principal amount format");
    }
    
    return ValidationResult.createValid();
}
```

The validation includes checks for required fields, format validation, and business rule validation.

### UI Implementation

The UI is implemented using JavaFX with a combination of programmatic creation and FXML:

```java
private BorderPane createMainLayout(InputSection inputSection, ActionSection actionSection, ResultSection resultSection) {
    BorderPane mainLayout = new BorderPane();
    
    VBox topSection = new VBox(10);
    topSection.setPadding(new Insets(20));
    topSection.getChildren().add(inputSection);
    topSection.getChildren().add(actionSection);
    
    mainLayout.setTop(topSection);
    mainLayout.setCenter(resultSection);
    
    return mainLayout;
}
```

The UI components are organized in a BorderPane layout with the input and action sections in the top region and the result section in the center region.

## Build and Deployment

This section provides information about building and deploying the Compound Interest Calculator application.

### Build Process

The application is built using Maven with the following goals:

- **mvn clean**: Cleans the target directory
- **mvn compile**: Compiles the source code
- **mvn test**: Runs the unit tests
- **mvn package**: Creates the JAR file
- **mvn install**: Installs the JAR file in the local Maven repository

The build process is configured in the `pom.xml` file.

### Packaging

The application can be packaged in different formats:

- **JAR with dependencies**: Created using the Maven Assembly Plugin
- **Native installers**: Created using the JPackage Maven Plugin

To create a JAR with dependencies:
```bash
mvn clean package
```

To create native installers:
```bash
mvn clean package -P package
```

The packaging configuration is defined in the `pom.xml` file under the `package` profile.

### Deployment

The application can be deployed in different ways:

- **JAR file**: Can be run using `java -jar` command
- **Native installers**: Can be installed using platform-specific installers

For detailed deployment instructions, refer to the [Installation Guide](installation-guide.md).

### Continuous Integration

The project uses GitHub Actions for continuous integration:

- **Build workflow**: Builds the application and runs tests on every push
- **Test workflow**: Runs comprehensive tests including unit, integration, and UI tests
- **Release workflow**: Creates release artifacts when a new version is tagged

The CI configuration is defined in the `.github/workflows/` directory.

## Extending the Application

This section provides guidelines for extending the Compound Interest Calculator application with new features.

### Adding New Calculation Types

To add a new calculation type:

1. Define the calculation method in the `CalculationService` interface
2. Implement the calculation in the `CalculationServiceImpl` class
3. Add appropriate validation in the `ValidationService`
4. Update the UI to include the new calculation option
5. Update the controller to handle the new calculation type
6. Add tests for the new calculation

### Customizing the UI

To customize the UI:

1. Modify the CSS files in the `src/main/resources/css/` directory
2. Update the FXML files in the `src/main/resources/fxml/` directory
3. Modify the UI component classes in the `ui/component/` package
4. Update the `CalculatorUI` class to incorporate the changes

### Adding New Input Fields

To add new input fields:

1. Update the `InputSection` class to include the new field
2. Add validation for the new field in the `ValidationService`
3. Update the `CalculationInput` model to include the new field
4. Update the `CalculatorController` to handle the new input
5. Update the calculation logic to use the new input

### Internationalization

To add internationalization support:

1. Create resource bundles for different languages
2. Use resource bundle keys instead of hardcoded strings
3. Add language selection in the UI
4. Update the `AppConfig` to load the appropriate resource bundle
5. Test the application with different languages

## Troubleshooting

This section provides guidance for troubleshooting common issues during development.

### Common Build Issues

- **Compilation errors**: Ensure JDK 11 is properly configured
- **Test failures**: Check test logs for details
- **Dependency issues**: Verify Maven settings and repository access
- **JavaFX issues**: Ensure JavaFX modules are properly configured

### Runtime Issues

- **Application won't start**: Check Java version and JavaFX installation
- **UI rendering problems**: Verify CSS and FXML files
- **Calculation errors**: Enable debug logging and check calculation logic
- **Performance issues**: Profile the application to identify bottlenecks

### Debugging Tips

- Use logging to track application flow
- Enable debug logging by setting the log level in `AppConfig`
- Use breakpoints in your IDE to step through code
- Check exception stack traces for error details
- Use JavaFX Scene Builder to debug UI layout issues

## API Reference

This section provides reference documentation for the key APIs in the application.

### CalculationService API

```java
public interface CalculationService {
    /**
     * Calculates the compound interest based on the provided calculation input parameters.
     *
     * @param input The calculation input containing principal, duration, and interest rate
     * @return The final amount after applying compound interest
     * @throws CalculationException if calculation fails
     */
    BigDecimal calculateCompoundInterest(CalculationInput input) throws CalculationException;
    
    /**
     * Calculates the compound interest based on the provided principal amount, duration, and interest rate.
     *
     * @param principal The principal amount
     * @param durationYears The loan duration in years
     * @param interestRate The annual interest rate as a percentage
     * @return The final amount after applying compound interest
     * @throws CalculationException if calculation fails
     */
    BigDecimal calculateCompoundInterest(BigDecimal principal, int durationYears, BigDecimal interestRate) throws CalculationException;
    
    /**
     * Calculates the Equated Monthly Installment (EMI) based on the provided calculation input parameters.
     *
     * @param input The calculation input containing principal, duration, and interest rate
     * @return The calculation result containing EMI amount and other details
     * @throws CalculationException if calculation fails
     */
    CalculationResult calculateEMI(CalculationInput input) throws CalculationException;
    
    /**
     * Calculates the Equated Monthly Installment (EMI) based on the provided principal amount, duration, and interest rate.
     *
     * @param principal The principal amount
     * @param durationYears The loan duration in years
     * @param interestRate The annual interest rate as a percentage
     * @return The calculation result containing EMI amount and other details
     * @throws CalculationException if calculation fails
     */
    CalculationResult calculateEMI(BigDecimal principal, int durationYears, BigDecimal interestRate) throws CalculationException;
}
```

### ValidationService API

```java
public interface ValidationService {
    /**
     * Validates the principal amount string input.
     *
     * @param principal The principal amount as a string
     * @return A ValidationResult indicating whether the input is valid
     */
    ValidationResult validatePrincipal(String principal);
    
    /**
     * Validates the loan duration string input.
     *
     * @param duration The loan duration as a string
     * @return A ValidationResult indicating whether the input is valid
     */
    ValidationResult validateDuration(String duration);
    
    /**
     * Validates all inputs required for calculation.
     *
     * @param principal The principal amount as a string
     * @param duration The loan duration as a string
     * @return A ValidationResult indicating whether all inputs are valid
     */
    ValidationResult validateAllInputs(String principal, String duration);
}
```

### Model Classes

```java
public class CalculationInput {
    private final BigDecimal principal;
    private final int durationYears;
    private final BigDecimal interestRate;
    
    // Constructor, getters, and validation methods
}

public class CalculationResult {
    private final BigDecimal emiAmount;
    private final BigDecimal totalAmount;
    private final BigDecimal interestAmount;
    private final BigDecimal annualInterestRate;
    private final int numberOfInstallments;
    
    // Constructor, getters, and formatting methods
}

public class ValidationResult {
    private final boolean valid;
    private final String errorMessage;
    
    // Constructor, getters, and factory methods
}
```

### Utility Classes

```java
public class BigDecimalUtils {
    // Utility methods for BigDecimal operations
    public static BigDecimal roundForCurrency(BigDecimal value);
    public static BigDecimal add(BigDecimal a, BigDecimal b);
    public static BigDecimal subtract(BigDecimal a, BigDecimal b);
    public static BigDecimal multiply(BigDecimal a, BigDecimal b);
    public static BigDecimal divide(BigDecimal a, BigDecimal b);
    public static BigDecimal pow(BigDecimal base, int exponent);
    public static boolean isZero(BigDecimal value);
    public static BigDecimal decimalToPercentage(BigDecimal decimal);
}

public class CurrencyUtils {
    // Utility methods for currency formatting
    public static String formatAsCurrency(BigDecimal amount);
    public static String formatAsPercentage(BigDecimal value);
}

public class ValidationUtils {
    // Utility methods for validation
    public static boolean isPositiveNumber(String value);
    public static boolean isPositiveInteger(String value);
    public static boolean isValidDecimalFormat(String value, int maxDecimalPlaces);
}
```

## Appendix

This section provides additional reference information.

### Glossary

- **EMI**: Equated Monthly Installment; a fixed payment amount made by a borrower to a lender at a specified date each month.
- **Compound Interest**: Interest calculated on the initial principal and also on the accumulated interest of previous periods.
- **Principal Amount**: The original sum of money borrowed in a loan, or put into an investment.
- **Interest Rate**: The amount charged by a lender to a borrower for the use of assets, expressed as a percentage of the principal.
- **Loan Duration**: The period over which a loan is to be repaid, typically expressed in years or months.

### References

- [Java 11 Documentation](https://docs.oracle.com/en/java/javase/11/docs/api/index.html)
- [JavaFX 11 Documentation](https://openjfx.io/javadoc/11/)
- [Maven Documentation](https://maven.apache.org/guides/index.html)
- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)

### Calculation Formulas

#### Compound Interest Formula
```
A = P(1 + r/n)^(nt)
```
Where:
- A = Final amount
- P = Principal amount
- r = Annual interest rate (in decimal form)
- n = Number of times interest is compounded per year
- t = Time in years

#### EMI Calculation Formula
```
EMI = [P × r × (1 + r)^n]/[(1 + r)^n - 1]
```
Where:
- P = Principal loan amount
- r = Monthly interest rate (annual rate divided by 12 and converted to decimal)
- n = Number of monthly installments (loan duration in years × 12)