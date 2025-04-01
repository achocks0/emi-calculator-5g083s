# Compound Interest Calculator - Architecture Documentation

Version: 1.0.0
Date: 2023

## 1. Introduction

This document provides a comprehensive overview of the architecture of the Compound Interest Calculator application. It describes the high-level architecture, component details, data flow, and technical decisions that form the foundation of the application.

### 1.1 Purpose

The purpose of this document is to provide developers, architects, and other stakeholders with a clear understanding of the architectural design of the Compound Interest Calculator application. It serves as a reference for development, maintenance, and future enhancements.

### 1.2 Scope

This document covers the architectural aspects of the Compound Interest Calculator application, including:

- High-level architecture
- Component details and responsibilities
- Data flow and component interactions
- Technical decisions and rationale
- Cross-cutting concerns

It does not cover implementation details, coding standards, or user interface design, which are addressed in separate documentation.

### 1.3 System Overview

The Compound Interest Calculator is a desktop application designed for the banking division to calculate Equated Monthly Installments (EMI) based on principal amount and loan duration. The application follows a layered architecture with Model-View-Controller (MVC) design principles, implemented using Java 11 and JavaFX 11.

The application enables bank officers to quickly provide loan information to customers by calculating accurate EMIs based on standard financial formulas. It features a simple, intuitive user interface for data entry and results presentation, with comprehensive input validation and error handling.

### 1.4 Architecture Diagram

The following diagram illustrates the high-level architecture of the Compound Interest Calculator application:

![Architecture Diagram](diagrams/architecture-diagram.png)

The architecture follows a three-tier layered approach with clear separation of concerns between the presentation layer (UI), control layer (Controller), and business logic layer (Services).

## 2. Architectural Overview

This section provides an overview of the architectural style, principles, and patterns used in the Compound Interest Calculator application.

### 2.1 Architectural Style

The Compound Interest Calculator application follows a layered architecture with Model-View-Controller (MVC) design principles. This architectural approach was selected for its simplicity, maintainability, and appropriateness for desktop applications with straightforward user interactions.

The layered architecture divides the application into distinct layers, each with a specific responsibility:

1. **Presentation Layer (View)**: Responsible for user interface and user interaction
2. **Control Layer (Controller)**: Coordinates between the presentation and business logic layers
3. **Business Logic Layer (Services)**: Implements the core business logic and calculations
4. **Data Model Layer**: Represents the application's data structures

This separation of concerns ensures that each layer has a single responsibility and can be developed, tested, and maintained independently.

### 2.2 Architectural Principles

The architecture of the Compound Interest Calculator application is guided by the following principles:

- **Separation of Concerns**: Each component has a single, well-defined responsibility
- **Single Responsibility Principle**: Each class has a single reason to change
- **Dependency Inversion**: High-level modules depend on abstractions, not concrete implementations
- **Interface Segregation**: Clients depend only on the methods they use
- **Loose Coupling**: Components interact through well-defined interfaces
- **High Cohesion**: Related functionality is grouped together

These principles ensure that the application is maintainable, testable, and extensible.

### 2.3 Design Patterns

The application uses the following design patterns:

- **Model-View-Controller (MVC)**: Separates the application into three interconnected components
- **Dependency Injection**: Services are injected into the Controller
- **Factory Method**: Used in AppConfig to create service instances
- **Strategy**: Different calculation strategies based on input parameters
- **Observer**: JavaFX property binding for UI updates

These patterns help to structure the code, promote reuse, and manage dependencies effectively.

### 2.4 System Boundaries

The Compound Interest Calculator is a standalone desktop application with no external system dependencies. All functionality is self-contained within the application, with no requirements for network connectivity, external APIs, or persistent data storage.

The application's boundary is defined by its user interface, which serves as the sole point of interaction with users. All inputs are provided through the UI, and all outputs are displayed through the UI.

### 2.5 Component Interaction Diagram

The following diagram illustrates the interactions between the major components of the application:

![Component Diagram](diagrams/component-diagram.png)

This diagram shows how the UI components interact with the Controller, which in turn interacts with the Service components to perform validation and calculation operations.

## 3. Component Architecture

This section provides detailed information about the major components of the Compound Interest Calculator application and their responsibilities.

### 3.1 Component Overview

The Compound Interest Calculator application consists of the following major components:

1. **User Interface (UI) Components**: JavaFX-based UI components for user interaction
2. **Controller Component**: Coordinates between UI and services
3. **Validation Service**: Validates user inputs
4. **Calculation Service**: Performs financial calculations
5. **Model Components**: Data structures for inputs and results
6. **Utility Components**: Helper functions for common operations

Each component has a specific responsibility and interacts with other components through well-defined interfaces.

### 3.2 Class Structure

The following diagram illustrates the class structure of the application:

![Class Diagram](diagrams/class-diagram.png)

This diagram shows the classes, interfaces, and their relationships within the application.

### 3.3 User Interface Components

The UI components are responsible for collecting user inputs, displaying results, and providing feedback to the user. The main UI components include:

- **CalculatorUI**: Main UI class that extends JavaFX Application
- **InputSection**: Component for collecting principal amount and loan duration
- **ResultSection**: Component for displaying calculation results
- **ActionSection**: Component with calculation buttons
- **HelpDialog**: Component for displaying help information

The UI components are implemented using JavaFX with a combination of programmatic creation and FXML. They follow a clean, intuitive design with clear input fields, validation feedback, and prominently displayed results.

### 3.4 Controller Component

The Controller component coordinates between the UI and service layers. The main controller class is:

- **CalculatorController**: Handles user actions, validates inputs, and delegates to services

The Controller is responsible for:
1. Validating user inputs using the Validation Service
2. Converting validated inputs to appropriate data types
3. Delegating calculations to the Calculation Service
4. Formatting results for display in the UI
5. Handling errors and providing feedback to the user

The Controller follows the Single Responsibility Principle and depends on service interfaces rather than concrete implementations, adhering to the Dependency Inversion Principle.

### 3.5 Service Components

The Service components implement the business logic of the application. The main service components include:

- **ValidationService**: Interface defining validation operations
- **ValidationServiceImpl**: Implementation of ValidationService
- **CalculationService**: Interface defining calculation operations
- **CalculationServiceImpl**: Implementation of CalculationService

The services are defined as interfaces with concrete implementations, following the Dependency Inversion Principle. This approach allows for easy testing and future extensions.

The ValidationService is responsible for validating user inputs, ensuring they meet the application's requirements before being used in calculations.

The CalculationService is responsible for performing compound interest and EMI calculations using standard financial formulas. It uses BigDecimal for precise financial calculations and handles edge cases such as zero interest rate.

### 3.6 Model Components

The Model components represent the data structures used in the application. The main model classes include:

- **CalculationInput**: Encapsulates input parameters for calculations
- **CalculationResult**: Encapsulates calculation results
- **ValidationResult**: Encapsulates validation results

These models are immutable and provide methods for accessing their properties. They serve as data transfer objects between the different layers of the application.

### 3.7 Utility Components

The Utility components provide helper functions for common operations. The main utility classes include:

- **BigDecimalUtils**: Utilities for working with BigDecimal for precise financial calculations
- **CurrencyUtils**: Utilities for formatting currency values
- **ValidationUtils**: Utilities for common validation operations

These utilities encapsulate common functionality used throughout the application, promoting code reuse and maintainability.

## 4. Data Flow

This section describes how data flows through the Compound Interest Calculator application, from user input to calculation results.

### 4.1 Data Flow Overview

The data flow in the Compound Interest Calculator follows a simple, linear pattern:

1. User inputs (principal amount and loan duration) are collected through the UI layer
2. Input data is passed to the Controller
3. Controller delegates validation to the ValidationService
4. Validated data is converted to appropriate types and passed to the CalculationService
5. CalculationService performs calculations and returns results
6. Controller formats results and updates the UI
7. UI displays formatted results to the user

This flow ensures that data is properly validated and processed at each step, with clear responsibilities for each component.

### 4.2 Sequence Diagram

The following sequence diagram illustrates the interaction flow between components during a complete calculation workflow:

![Sequence Diagram](diagrams/sequence-diagram.png)

This diagram shows the sequence of method calls and data flow between the User, UI, Controller, ValidationService, and CalculationService during a typical calculation operation.

### 4.3 Input Processing

User inputs are processed as follows:

1. User enters principal amount and loan duration in the InputSection
2. User clicks the Calculate button in the ActionSection
3. InputSection provides the input values to the Controller
4. Controller calls ValidationService to validate the inputs
5. If validation fails, Controller returns error messages to the UI
6. If validation succeeds, Controller converts inputs to appropriate types
7. Controller creates a CalculationInput object with the converted values

Input validation ensures that the principal amount is a positive number with up to 2 decimal places and the loan duration is a positive integer.

### 4.4 Calculation Processing

Calculations are processed as follows:

1. Controller calls CalculationService with the CalculationInput object
2. CalculationService extracts principal, duration, and interest rate from the input
3. CalculationService converts annual interest rate to monthly rate
4. CalculationService converts loan duration from years to months
5. CalculationService calculates EMI using the standard formula
6. CalculationService calculates total amount payable and total interest
7. CalculationService creates a CalculationResult object with all calculated values
8. CalculationService returns the CalculationResult to the Controller

Calculations use BigDecimal for precise financial calculations and handle edge cases such as zero interest rate.

### 4.5 Result Processing

Results are processed as follows:

1. Controller receives CalculationResult from CalculationService
2. Controller formats the EMI amount as a currency string
3. Controller returns the formatted result to the UI
4. ResultSection displays the formatted EMI amount
5. If expanded view is enabled, ResultSection displays additional details

Results are formatted according to USD currency standards with appropriate symbols and decimal places.

### 4.6 Error Handling

Errors are handled as follows:

1. ValidationService returns ValidationResult with error messages for invalid inputs
2. Controller checks ValidationResult and returns error messages to the UI
3. InputSection displays error messages next to the relevant input fields
4. CalculationService throws CalculationException for calculation errors
5. Controller catches exceptions and returns error messages to the UI
6. ResultSection displays error messages in the result area

Error handling ensures that users receive clear, actionable feedback about any issues with their inputs or calculations.

## 5. Technical Decisions

This section explains the key technical decisions made in the architecture of the Compound Interest Calculator application and the rationale behind them.

### 5.1 Architecture Style Decisions

The decision to use a layered architecture with MVC design principles was based on the following considerations:

- **Simplicity**: The application has straightforward requirements that fit well with a layered approach
- **Separation of Concerns**: MVC provides clear separation between UI, control logic, and business logic
- **Maintainability**: Each layer can be developed, tested, and maintained independently
- **Testability**: Service interfaces can be easily mocked for testing
- **Familiarity**: The team is familiar with MVC, reducing the learning curve

Alternative architectural styles considered included:

- **Hexagonal Architecture**: Provides better isolation but adds complexity not justified by the application's requirements
- **Clean Architecture**: Offers more layers of abstraction but would be overengineering for this application
- **MVVM**: Better for more complex UIs with bidirectional data binding

The layered architecture with MVC was chosen as the best balance between structure and simplicity for this application.

### 5.2 Technology Selection Decisions

The following technology decisions were made:

- **Java 11**: Selected for desktop application development as specified in requirements. Java 11 LTS provides long-term support, stability, and cross-platform compatibility.

- **JavaFX 11**: Chosen for UI development because it provides modern, responsive UI components with better styling capabilities than Swing. JavaFX also supports MVC architecture and property binding.

- **BigDecimal**: Selected for financial calculations to avoid floating-point precision errors when handling currency values. This is critical for accurate EMI calculations.

- **Maven**: Chosen as the build system for its robust dependency management, build automation, and wide industry adoption.

- **JUnit 5**: Selected for testing because it's the industry standard for Java unit testing, allowing comprehensive test coverage.

Alternative technologies considered included:

- **Swing**: Older UI framework with less modern styling capabilities
- **SWT**: Platform-specific UI framework with more complex deployment
- **Gradle**: Alternative build system with more flexible but verbose configuration
- **Primitive types (double/float)**: Less precise for financial calculations

The selected technologies provide the best balance of functionality, precision, and ease of development for this application.

### 5.3 Component Design Decisions

The following component design decisions were made:

- **Service Interfaces**: Services are defined as interfaces with concrete implementations to support dependency injection and testability.

- **Immutable Models**: Data models are designed to be immutable to prevent unexpected state changes and ensure thread safety.

- **Controller as Coordinator**: The Controller is designed as a thin coordinator between UI and services, with minimal business logic.

- **Utility Classes**: Common functionality is extracted into utility classes to promote code reuse and maintainability.

- **Exception Hierarchy**: Custom exceptions are defined for different error categories to provide clear error handling.

These design decisions promote clean separation of concerns, code reuse, and maintainability.

### 5.4 Data Management Decisions

The following data management decisions were made:

- **In-Memory Processing**: All data is processed in-memory without persistent storage, as the application performs calculations on-demand without needing to save historical data.

- **Data Transfer Objects**: CalculationInput and CalculationResult serve as DTOs between layers, encapsulating related data.

- **BigDecimal for Currency**: All financial calculations use BigDecimal with appropriate scale and rounding to ensure precision.

- **Formatted Output**: Currency values are formatted according to USD standards for display.

These decisions ensure accurate calculations and appropriate data handling for a financial application.

## 6. Cross-Cutting Concerns

This section describes how cross-cutting concerns are addressed in the architecture of the Compound Interest Calculator application.

### 6.1 Error Handling

The application implements a comprehensive error handling strategy focused on user input validation and calculation error management:

- **Input Validation**: All user inputs are validated before processing with clear error messages
- **Validation Results**: ValidationResult objects encapsulate validation status and error messages
- **Custom Exceptions**: CalculationException and ValidationException provide specific error types
- **Exception Handling**: Structured exception handling with try-catch blocks at appropriate levels
- **User Feedback**: Clear, actionable error messages displayed in the UI
- **Logging**: All exceptions are logged with appropriate context

This approach ensures that errors are caught early, handled appropriately, and communicated clearly to the user.

### 6.2 Logging

The application uses Java's built-in logging framework with the following approach:

- **Log Levels**: Appropriate log levels (INFO, WARNING, SEVERE) for different types of events
- **Context Information**: Log messages include relevant context for troubleshooting
- **Exception Logging**: All exceptions are logged with stack traces
- **Configuration**: Logging is configured in the AppConfig class
- **Log Storage**: Logs are written to a local log file with rotation

This logging strategy provides visibility into application behavior for debugging and troubleshooting while avoiding excessive logging in performance-critical code.

### 6.3 Performance Considerations

The application addresses performance considerations as follows:

- **Efficient Algorithms**: Calculation algorithms are optimized for performance
- **BigDecimal Optimization**: BigDecimal operations are optimized to minimize performance impact
- **UI Responsiveness**: UI operations are designed to be responsive and non-blocking
- **Resource Management**: Resources are properly managed to avoid leaks
- **Minimal Object Creation**: Object creation is minimized in performance-critical paths

These considerations ensure that the application performs well even with large input values and remains responsive during calculations.

### 6.4 Security Considerations

Although the application has minimal security requirements as a standalone desktop tool, the following security considerations are addressed:

- **Input Validation**: All user inputs are validated to prevent unexpected behavior
- **Error Handling**: Errors are handled gracefully without exposing system details
- **No Sensitive Data**: The application does not store sensitive customer data
- **Resource Protection**: The application uses resources responsibly to prevent denial of service

These basic security practices ensure that the application operates safely and reliably.

### 6.5 Testability

The architecture is designed for testability with the following features:

- **Interface-based Design**: Services are defined as interfaces that can be mocked
- **Dependency Injection**: Dependencies are injected, allowing them to be replaced with mocks
- **Single Responsibility**: Components have clear responsibilities that can be tested in isolation
- **Pure Functions**: Calculation methods are designed as pure functions for deterministic testing
- **Separation of Concerns**: UI, control logic, and business logic are separated for easier testing

This approach enables comprehensive unit testing, integration testing, and UI testing to ensure application quality.

### 6.6 Internationalization

While the current version of the application is designed for USD currency and English language, the architecture supports future internationalization with the following features:

- **Separated UI Strings**: UI text is separated from business logic
- **Currency Formatting**: Currency formatting is handled by dedicated utility methods
- **Locale-aware Formatting**: Number and currency formatting can be extended to support different locales

These features provide a foundation for adding full internationalization support in future versions if required.

## 7. Deployment Architecture

This section describes the deployment architecture of the Compound Interest Calculator application.

### 7.1 Deployment Overview

The Compound Interest Calculator is deployed as a standalone desktop application that runs on bank staff workstations. The application does not require server-side components, cloud resources, or complex deployment infrastructure.

The application is packaged and distributed in the following formats:

- **JAR with dependencies**: Cross-platform Java archive that requires JRE 11+
- **Native installers**: Platform-specific installers for Windows, macOS, and Linux

These packaging options provide flexibility for different deployment scenarios while maintaining the standalone nature of the application.

### 7.2 System Requirements

The application has the following system requirements:

- **Operating System**: Windows 10/11, macOS 10.14+, or Linux with GUI
- **Java Runtime**: JRE 11 or higher (bundled with native installers)
- **Memory**: 512 MB RAM minimum, 1 GB recommended
- **Disk Space**: 100 MB free space
- **Display**: 1024x768 resolution or higher

These minimal requirements ensure that the application can run on standard bank workstations without special hardware or software.

### 7.3 Installation Process

The application is installed as follows:

- **JAR Distribution**: Copy the JAR file to the desired location and run with `java -jar`
- **Windows**: Run the MSI installer and follow the installation wizard
- **macOS**: Mount the DMG, drag the application to Applications folder
- **Linux**: Install using the DEB or RPM package manager

The installation process is simple and can be performed by IT staff or bank officers with standard user privileges.

### 7.4 Updates and Maintenance

The application is updated as follows:

- **Version Checking**: The application displays its version in the About dialog
- **Update Distribution**: New versions are distributed through the bank's internal software distribution system
- **Update Installation**: Updates are installed by replacing the existing application
- **Rollback**: Previous versions are kept available for rollback if needed

This simple update process ensures that bank staff always have access to the latest version of the application.

## 8. Future Considerations

This section outlines potential future enhancements to the architecture of the Compound Interest Calculator application.

### 8.1 Additional Calculation Types

The architecture can be extended to support additional calculation types:

- **Variable Interest Rates**: Support for changing interest rates over the loan period
- **Different Compounding Frequencies**: Support for daily, quarterly, or annual compounding
- **Loan Comparison**: Ability to compare multiple loan scenarios
- **Amortization Schedule**: Generation of detailed payment schedules

These enhancements would require extensions to the CalculationService interface and implementation, with corresponding UI changes.

### 8.2 Integration Capabilities

The architecture could be extended to support integration with other banking systems:

- **API Layer**: Addition of an API layer for integration with other applications
- **Data Import/Export**: Support for importing and exporting calculation data
- **Customer Database Integration**: Integration with customer information systems
- **Loan Processing System Integration**: Integration with loan origination and processing systems

These integrations would require additional architectural components such as an integration layer and data access layer.

### 8.3 Multi-platform Support

The architecture could be extended to support additional platforms:

- **Web Application**: Conversion to a web-based application using JavaFX Web or a web framework
- **Mobile Application**: Development of companion mobile applications for iOS and Android
- **Cloud Deployment**: Deployment as a cloud-based service with multi-user support

These extensions would require significant architectural changes, potentially moving to a client-server or microservices architecture.

### 8.4 Advanced Features

The architecture could be extended to support advanced features:

- **User Profiles**: Support for user-specific settings and preferences
- **Calculation History**: Storage and retrieval of previous calculations
- **Reporting**: Generation of detailed reports and charts
- **Workflow Integration**: Integration with loan approval workflows

These features would require additions to the data model and potentially a persistence layer for data storage.

## 9. Conclusion

The architecture of the Compound Interest Calculator application is designed to meet the specific requirements of a desktop application for calculating EMIs in a banking environment. The layered architecture with MVC design principles provides a clean separation of concerns, making the application maintainable, testable, and extensible.

The application's components are organized into clear layers with well-defined responsibilities and interfaces. The data flow is straightforward, with proper validation and error handling at each step. The technical decisions are based on a balance of simplicity, functionality, and performance appropriate for the application's requirements.

This architecture provides a solid foundation for the current requirements while allowing for future enhancements and extensions as needed. It ensures that the application will be reliable, accurate, and user-friendly, meeting the needs of bank officers for quick and accurate EMI calculations.

## 10. References

- Java 11 Documentation: https://docs.oracle.com/en/java/javase/11/docs/api/index.html
- JavaFX 11 Documentation: https://openjfx.io/javadoc/11/
- Maven Documentation: https://maven.apache.org/guides/index.html
- Compound Interest Formula: A = P(1 + r/n)^(nt)
- EMI Formula: EMI = [P × r × (1 + r)^n]/[(1 + r)^n - 1]