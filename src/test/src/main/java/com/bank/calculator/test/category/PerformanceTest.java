package com.bank.calculator.test.category;

import org.junit.jupiter.api.Tag; // JUnit 5.8.2

/**
 * Marker interface for categorizing performance tests in the Compound Interest Calculator application.
 * Tests that verify performance requirements such as calculation speed, UI responsiveness, 
 * memory usage, and startup time should be annotated with this interface.
 * 
 * This interface allows for selective test execution and reporting of performance tests
 * that verify the application meets the following performance requirements:
 * - UI Responsiveness: < 100ms for all interactions
 * - Calculation Performance: < 500ms for standard calculations
 * - Memory Usage: < 100MB during normal operation
 * - Startup Time: < 3 seconds on standard hardware
 * 
 * Usage example:
 * <pre>
 * public class CalculationServicePerformanceTest implements PerformanceTest {
 *     @Test
 *     void testCalculationSpeed() {
 *         // Test implementation
 *     }
 * }
 * </pre>
 * 
 * In CI/CD pipelines, performance tests can be executed separately using:
 * <pre>
 * mvn test -Dgroups="performance"
 * </pre>
 */
@Tag("performance")
public interface PerformanceTest {
    // Marker interface - no methods required
}