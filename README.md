# Compound Interest Calculator

![Java 11](https://img.shields.io/badge/Java-11-blue.svg)
![JavaFX](https://img.shields.io/badge/JavaFX-11-green.svg)
![Maven](https://img.shields.io/badge/Maven-3.8.6-orange.svg)

## Overview

The Compound Interest Calculator is a desktop application designed for banking division staff to calculate Equated Monthly Installments (EMI) based on principal amount and loan duration. This tool streamlines the loan processing workflow by providing accurate EMI calculations, enabling bank officers to quickly provide loan information to customers.

By implementing this application, the bank will improve operational efficiency, reduce calculation errors, and enhance customer service through faster response times.

## Features

- ✅ Calculate compound interest and EMI based on principal amount and loan duration in years
- ✅ User-friendly interface with intuitive data entry fields
- ✅ Comprehensive input validation with clear error messages
- ✅ Display of calculated EMI in properly formatted USD
- ✅ Fixed annual interest rate of 7.5% for all calculations
- ✅ Cross-platform compatibility (Windows, macOS, Linux)
- ✅ Standalone application with no external dependencies

## Screenshots

![Main Application Interface](docs/images/main-interface.png)
*Main application interface placeholder*

![Calculation Results](docs/images/calculation-results.png)
*Calculation results display placeholder*

## Installation

### Requirements

- Java Runtime Environment (JRE) 11 or higher
- Minimum 512MB RAM (1GB recommended)
- 100MB free disk space
- Display resolution of 1024x768 or higher

### Installation Instructions

#### Windows

1. Download the latest Windows installer (.msi) from the internal distribution server
2. Double-click the installer file and follow the on-screen instructions
3. Launch the application from the desktop shortcut or Start menu

#### macOS

1. Download the latest macOS package (.dmg) from the internal distribution server
2. Open the .dmg file and drag the application to the Applications folder
3. Launch from the Applications folder or Dock

#### Linux

1. Download the appropriate package (.deb or .rpm) from the internal distribution server
2. Install using your distribution's package manager:
   - For Debian/Ubuntu: `sudo dpkg -i compound-interest-calculator.deb`
   - For RHEL/CentOS: `sudo rpm -i compound-interest-calculator.rpm`
3. Launch from the application menu

#### Manual Installation (JAR file)

1. Ensure Java 11 or higher is installed
2. Download the JAR file from the internal distribution server
3. Run the application using: `java -jar compound-interest-calculator.jar`

## Usage

1. **Enter Principal Amount**
   - Input the loan amount in USD (must be a positive number)
   - Example: 25000.00

2. **Enter Loan Duration**
   - Input the loan duration in years (must be a positive whole number)
   - Example: 5

3. **Calculate EMI**
   - Click the "Calculate EMI" button
   - The monthly EMI amount will be displayed in USD

4. **New Calculation**
   - Click "New Calculation" to clear all fields and start over

### Keyboard Shortcuts

- Enter - Calculate EMI (when focus is in any input field)
- Ctrl+N - New Calculation (clear form)
- Tab - Navigate between input fields
- Alt+P - Focus Principal Amount field
- Alt+D - Focus Duration field
- Alt+C - Click Calculate button
- F1 - Show Help

## Development Setup

### Prerequisites

- JDK 11 (LTS)
- Maven 3.8.6 or higher
- Git
- IDE (recommended: IntelliJ IDEA or Eclipse)

### Setting Up Development Environment

1. Clone the repository:
   ```
   git clone [repository-url]
   cd compound-interest-calculator
   ```

2. Import the project into your IDE:
   - For IntelliJ IDEA: File > Open > Select the project's pom.xml file
   - For Eclipse: File > Import > Maven > Existing Maven Projects

3. Install dependencies:
   ```
   mvn install
   ```

## Build Instructions

### Building JAR File

```
mvn clean package
```
The executable JAR will be created in the `target` directory.

### Building Native Installers

```
mvn clean javafx:jlink jpackage:jpackage
```
The native installers will be created in the `target/dist` directory.

## Testing

### Running Tests

```
mvn test
```

### Test Coverage

```
mvn jacoco:report
```
The coverage report will be available at `target/site/jacoco/index.html`.

## Project Structure

```
compound-interest-calculator/
├── src/
│   ├── main/
│   │   ├── java/        # Java source files
│   │   │   └── com/bank/calculator/
│   │   │       ├── App.java             # Main application class
│   │   │       ├── controller/          # MVC controllers
│   │   │       ├── model/               # Data models
│   │   │       ├── service/             # Business logic services
│   │   │       └── ui/                  # JavaFX UI components
│   │   └── resources/   # Application resources (CSS, FXML, etc.)
│   └── test/
│       └── java/        # Test classes
├── pom.xml              # Maven configuration file
├── README.md            # This file
└── LICENSE              # License information
```

## Technologies

- Java 11 - Core programming language
- JavaFX 11 - UI framework for desktop application
- Maven - Build automation and dependency management
- JUnit 5 - Unit testing framework
- CSS - Styling for the user interface
- JPackage - Creating native installers

## Calculations

The application uses the following formulas:

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

## License

This project is licensed under the [Organization's Internal License] - See the LICENSE file for details.

## Contributors

- Banking Division Technical Team
- Financial Software Development Department

## Support

For support, please contact the IT Support Desk:
- Email: [support-email]
- Internal Extension: [extension-number]