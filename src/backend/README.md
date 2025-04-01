# Compound Interest Calculator

A desktop application designed for the banking division to calculate Equated Monthly Installments (EMI) based on principal amount and loan duration.

## Overview

This application provides a simple, intuitive interface for bank officers to calculate EMI amounts for loans. It streamlines the loan processing workflow by providing accurate calculations, enabling quick responses to customer inquiries.

## Features

- Input collection for principal amount in USD and loan duration in years
- Compound interest calculation using standard formula
- EMI calculation with proper currency formatting
- Input validation with clear error messages
- Simple, intuitive user interface

## Technology Stack

The application is built using the following technologies:

- Java 11 LTS
- JavaFX 11 for UI components
- Maven 3.8.6 for build automation
- JUnit 5.8.2 for testing
- BigDecimal for precise financial calculations

## Project Structure

The project follows a standard Maven directory structure with the following organization:

```
src/
├── main/
│   ├── java/
│   │   └── com/bank/calculator/
│   │       ├── CompoundInterestCalculatorApp.java (Main application entry point)
│   │       ├── config/
│   │       │   └── AppConfig.java (Application configuration)
│   │       ├── constant/
│   │       │   ├── CalculationConstants.java (Constants for calculations)
│   │       │   └── ErrorMessages.java (Error message constants)
│   │       ├── controller/
│   │       │   └── CalculatorController.java (Controller component)
│   │       ├── exception/
│   │       │   ├── CalculationException.java (Calculation-related exceptions)
│   │       │   └── ValidationException.java (Validation-related exceptions)
│   │       ├── model/
│   │       │   ├── CalculationInput.java (Input data model)
│   │       │   ├── CalculationResult.java (Result data model)
│   │       │   └── ValidationResult.java (Validation result model)
│   │       ├── service/
│   │       │   ├── CalculationService.java (Calculation service interface)
│   │       │   ├── ValidationService.java (Validation service interface)
│   │       │   └── impl/
│   │       │       ├── CalculationServiceImpl.java (Calculation service implementation)
│   │       │       └── ValidationServiceImpl.java (Validation service implementation)
│   │       ├── ui/
│   │       │   ├── CalculatorUI.java (Main UI class)
│   │       │   ├── component/
│   │       │   │   ├── ActionSection.java (Action buttons component)
│   │       │   │   ├── InputSection.java (Input fields component)
│   │       │   │   └── ResultSection.java (Result display component)
│   │       │   ├── dialog/
│   │       │   │   └── HelpDialog.java (Help dialog component)
│   │       │   ├── formatter/
│   │       │   │   └── CurrencyFormatter.java (Currency formatting utility)
│   │       │   └── validator/
│   │       │       └── InputValidator.java (UI input validation)
│   │       └── util/
│   │           ├── BigDecimalUtils.java (BigDecimal utility methods)
│   │           ├── CurrencyUtils.java (Currency utility methods)
│   │           └── ValidationUtils.java (Validation utility methods)
│   └── resources/
│       ├── css/
│       │   ├── application.css (Main application styles)
│       │   ├── input-section.css (Input section styles)
│       │   ├── result-section.css (Result section styles)
│       │   └── action-section.css (Action section styles)
│       ├── fxml/
│       │   ├── calculator-ui.fxml (Main UI layout)
│       │   ├── input-section.fxml (Input section layout)
│       │   ├── result-section.fxml (Result section layout)
│       │   ├── action-section.fxml (Action section layout)
│       │   └── help-dialog.fxml (Help dialog layout)
│       └── icons/
│           ├── calculator-icon.png (Application icon)
│           ├── help-icon.png (Help icon)
│           ├── error-icon.png (Error icon)
│           └── info-icon.png (Information icon)
└── test/
    ├── java/
    │   └── com/bank/calculator/
    │       ├── util/
    │       │   ├── BigDecimalUtilsTest.java
    │       │   └── ValidationUtilsTest.java
    │       ├── service/
    │       │   ├── CalculationServiceTest.java
    │       │   └── ValidationServiceTest.java
    │       ├── controller/
    │       │   └── CalculatorControllerTest.java
    │       └── ui/
    │           └── CalculatorUITest.java
    └── resources/
        └── test-data/
            ├── valid-inputs.json
            ├── invalid-inputs.json
            └── expected-results.json
```

## Prerequisites

To build and run this application, you need the following installed on your system:

- Java Development Kit (JDK) 11 or higher
- Maven 3.8.6 or higher
- Git (for version control)

## Setup and Installation

Follow these steps to set up the project locally:

### 1. Clone the repository

```bash
git clone <repository-url>
cd compound-interest-calculator
```

### 2. Build the project

```bash
mvn clean install
```

### 3. Run the application

```bash
# On Windows
scripts/run.bat

# On macOS/Linux
./scripts/run.sh
```

## Building the Application

The project uses Maven for build automation. Here are the common build commands:

### Compile the code

```bash
mvn compile
```

### Run the tests

```bash
mvn test
```

### Create an executable JAR with dependencies

```bash
mvn package
```

This will create a JAR file in the `target` directory named `compound-interest-calculator-1.0.0-with-dependencies.jar`.

### Create platform-specific installers

```bash
# On Windows
scripts/package.bat

# On macOS/Linux
./scripts/package.sh
```

This will create platform-specific installers in the `target/installer` directory.

## Running the Application

There are several ways to run the application:

### Using Maven

```bash
mvn javafx:run
```

### Using the executable JAR

```bash
java -jar target/compound-interest-calculator-1.0.0-with-dependencies.jar
```

### Using the provided scripts

```bash
# On Windows
scripts/run.bat

# On macOS/Linux
./scripts/run.sh
```

## Testing

The project includes unit tests for all core components. Run the tests using Maven:

```bash
mvn test
```

Test reports are generated in the `target/surefire-reports` directory.

## Application Usage

Once the application is running, follow these steps to calculate EMI:

1. Enter the principal amount in USD in the Principal Amount field
2. Enter the loan duration in years in the Loan Duration field
3. Click the Calculate EMI button
4. View the calculated monthly EMI amount in the result section
5. To perform a new calculation, click the New Calculation button

## Calculation Formulas

The application uses the following formulas for calculations:

### Compound Interest Formula

```
A = P(1 + r/n)^(nt)
```

Where:
- A = Final amount
- P = Principal amount
- r = Annual interest rate (in decimal form)
- n = Number of times interest is compounded per year
- t = Time in years

### EMI Calculation Formula

```
EMI = [P × r × (1 + r)^n]/[(1 + r)^n - 1]
```

Where:
- P = Principal loan amount
- r = Monthly interest rate (annual rate divided by 12 and converted to decimal)
- n = Number of monthly installments (loan duration in years × 12)

## Contributing

Contributions to the project are welcome. Please follow these steps to contribute:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/your-feature-name`)
3. Commit your changes (`git commit -m 'Add some feature'`)
4. Push to the branch (`git push origin feature/your-feature-name`)
5. Create a new Pull Request

## License

This project is licensed under the terms specified in the LICENSE file.