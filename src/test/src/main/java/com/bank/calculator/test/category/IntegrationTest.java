package com.bank.calculator.test.category;

import org.junit.jupiter.api.Tag; // JUnit 5.8.2

/**
 * Marker interface for categorizing integration tests in the application.
 * Tests that verify the interaction between different components such as 
 * Controller-Service, Service-Service, or UI-Controller integrations should 
 * be annotated with this interface.
 * 
 * This interface allows for selective test execution and reporting of 
 * integration tests that verify the interaction between different components 
 * of the system.
 * 
 * Usage:
 * <pre>
 * {@code
 * @IntegrationTest
 * class CalculationControllerIntegrationTest {
 *     // Integration tests that verify controller and service interactions
 * }
 * }
 * </pre>
 * 
 * This enables selective execution with Maven or Gradle by targeting the "integration" tag:
 * <pre>
 * {@code
 * mvn test -Dgroups="integration"
 * }
 * </pre>
 */
@Tag("integration")
public interface IntegrationTest {
    // This is a marker interface with no methods
    // It is used solely for categorizing tests as integration tests
}