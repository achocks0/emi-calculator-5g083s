# Compound Interest Calculator - Test Suite

Comprehensive test suite for the Compound Interest Calculator desktop application, providing test coverage for all application components including calculation logic, validation, UI, and performance aspects.

## Test Organization

The test suite is organized into multiple categories to ensure comprehensive coverage of all application aspects:

### Unit Tests
Tests for individual components in isolation, including services, utilities, and models.

### Integration Tests
Tests for interactions between components, such as service-to-service and controller-to-service interactions.

### UI Tests
Tests for the user interface components and interactions using TestFX.

### Performance Tests
Tests for application performance metrics including calculation speed, memory usage, and UI responsiveness.

### Security Tests
Tests for input validation security, exception handling, and resource utilization.

## Test Directory Structure

The test module is organized with the following directory structure:

### src/main/java
Contains test infrastructure code including test categories, fixtures, utilities, and runners.

### src/main/resources
Contains test data files and configuration resources.

### src/test/java
Contains the actual test classes organized by test category and application component.

### src/test/resources
Contains test-specific resources such as test suites and test data.

### scripts
Contains shell and batch scripts for running tests from the command line.

## Prerequisites

The following prerequisites are required to run the tests:

- Java Development Kit (JDK) 11 or higher
- Apache Maven 3.8.6 or higher
- Git (for source code management)
- Sufficient system resources (minimum 1GB RAM recommended)

## Setup Instructions

Follow these steps to set up the test environment:

1. Clone the repository: `git clone [repository-url]`
2. Navigate to the project root directory: `cd compound-interest-calculator`
3. Build the project: `mvn clean install -DskipTests`
4. Navigate to the test directory: `cd src/test`

## Running Tests

There are several ways to run the tests:

### Using Maven

Run tests using Maven commands:

```bash
# Run all tests
mvn test -P all-tests

# Run only unit tests
mvn test -P unit-tests

# Run only integration tests
mvn verify -P integration-tests

# Run only UI tests
mvn test -P ui-tests

# Run only performance tests
mvn test -P performance-tests

# Run only security tests
mvn test -P security-tests
```

### Using Scripts

Run tests using the provided scripts:

```bash
# On Unix/Linux/macOS
# Run all tests
./scripts/run-tests.sh

# Run specific test category
./scripts/run-tests.sh -c unit

# On Windows
# Run all tests
scripts\run-tests.bat

# Run specific test category
scripts\run-tests.bat -c unit
```

### Using IDE

Run tests directly from your IDE by running the appropriate test runner class:

- AllTestsRunner - Runs all test categories
- UnitTestsRunner - Runs only unit tests
- IntegrationTestsRunner - Runs only integration tests
- UITestsRunner - Runs only UI tests
- PerformanceTestsRunner - Runs only performance tests
- SecurityTestsRunner - Runs only security tests

## Test Reports

After running tests, reports are generated in the following locations:

- JUnit XML reports: `target/surefire-reports/` and `target/failsafe-reports/`
- HTML test reports: `target/site/surefire-report.html` and `target/site/failsafe-report.html`
- Code coverage reports: `target/site/jacoco/`

### Generating Reports

Generate comprehensive HTML reports using:

```bash
# Generate test reports
mvn site

# Or use the provided script
./scripts/generate-test-report.sh
```

## Test Data

Test data is organized in JSON files located in the `src/main/resources/test-data/` and `src/test/resources/test-data/` directories:

- calculation-test-cases.json - Test cases for calculation logic
- validation-test-cases.json - Test cases for input validation
- ui-test-cases.json - Test cases for UI testing
- performance-test-cases.json - Test cases for performance testing
- edge-cases.json - Edge case test scenarios
- performance-thresholds.json - Performance acceptance thresholds

## Test Categories

Tests are categorized using JUnit 5 tags to allow selective execution:

- @Tag("unit") - Unit tests
- @Tag("integration") - Integration tests
- @Tag("ui") - UI tests
- @Tag("performance") - Performance tests
- @Tag("security") - Security tests

## Test Fixtures

Test fixtures provide predefined test data and scenarios:

- CalculationTestFixture - Test cases for calculation logic
- ValidationTestFixture - Test cases for input validation
- UITestFixture - Test cases for UI testing

## Continuous Integration

Tests are automatically executed in the CI pipeline configured in `.github/workflows/`:

- build.yml - Builds the application and runs unit tests
- test.yml - Runs all test categories
- release.yml - Runs tests before creating a release

## Troubleshooting

Common issues and solutions:

- UI tests failing: Ensure you have a display server running or use headless mode
- Performance tests failing: Check system resources and ensure no resource-intensive processes are running
- Test execution hanging: Check for deadlocks or infinite loops in test code
- Out of memory errors: Increase Maven heap size using MAVEN_OPTS environment variable

## Contributing

When adding new tests, follow these guidelines:

- Place unit tests in the appropriate component package under src/test/java
- Add appropriate @Tag annotations to categorize tests
- Follow the naming convention: methodName_testCondition_expectedResult
- Include test data in the appropriate test data file
- Ensure tests are independent and can run in any order
- Add comments explaining the purpose and expectations of each test

## Contact

For questions or issues related to the test suite, contact the development team.