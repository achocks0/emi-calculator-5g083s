package com.bank.calculator.test.category;

import org.junit.jupiter.api.Tag; // JUnit 5.8.2

/**
 * Marker interface for categorizing security tests in the Compound Interest Calculator application.
 * 
 * This interface allows for selective test execution and reporting of security tests that verify 
 * the application's resistance to security vulnerabilities and adherence to security best practices.
 * 
 * Security tests should focus on:
 * - Input validation security (testing boundary conditions and invalid inputs)
 * - Resource utilization (memory usage during large calculations)
 * - Exception handling (proper handling of all exception paths)
 * 
 * Test classes that implement this interface will automatically be tagged with "security",
 * allowing them to be selectively run using JUnit's tag filtering capabilities.
 */
@Tag("security")
public interface SecurityTest {
    // Marker interface - no methods required
}