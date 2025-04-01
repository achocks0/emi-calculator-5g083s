package com.bank.calculator.test.category;

import org.junit.jupiter.api.Tag; // JUnit 5.8.2

/**
 * Marker interface for categorizing unit tests in the application.
 * Tests that verify individual components in isolation, such as services, utilities, and models
 * should be annotated with this interface.
 * 
 * This interface enables:
 * 1. Selective test execution - run only unit tests during development or CI
 * 2. Consistent reporting - identify and track unit test coverage separately
 * 3. Organization - clear categorization of test types in the test suite
 * 
 * Usage example:
 * 
 * <pre>
 * {@code
 * class ValidationServiceTest implements UnitTest {
 *     // test methods for ValidationService
 * }
 * }
 * </pre>
 */
@Tag("unit")
public interface UnitTest {
    // This is a marker interface with no methods
    // It uses the JUnit 5 @Tag annotation to categorize tests
}