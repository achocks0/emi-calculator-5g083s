package com.bank.calculator.test.category;

import org.junit.jupiter.api.Tag; // JUnit 5.8.2

/**
 * Marker interface for categorizing UI tests in the Compound Interest Calculator application.
 * 
 * Tests that verify the user interface functionality, including input validation, calculation
 * workflow, and result display should implement this interface to be identified as UI tests.
 * This allows for selective test execution and reporting of UI tests.
 * 
 * This interface is used in conjunction with TestFX for testing JavaFX UI components and
 * event handling in the calculator application. Test scenarios typically include:
 * - Input field interaction
 * - Calculate button behavior
 * - Error message display
 * - Field highlighting
 * - Result formatting
 * 
 * Usage:
 * <pre>
 * {@code
 * class MyUITest implements UITest {
 *     // UI test methods...
 * }
 * }
 * </pre>
 */
@Tag("ui")
public interface UITest {
    // This is a marker interface - no methods required
}