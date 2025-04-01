#!/bin/bash
#
# Shell script to run performance tests for the Compound Interest Calculator application.
# This script provides a convenient way to execute performance tests from the command line
# on Unix/Linux/macOS systems to verify that the application meets performance requirements
# such as calculation speed, UI responsiveness, memory usage, and startup time.

# Exit on error
set -e

# Script variables
SCRIPT_DIR=$(dirname "$0")
PROJECT_ROOT=$(realpath "$SCRIPT_DIR/../..")
MAVEN_OPTS="-Xmx1024m"
TEST_REPORT_DIR="../target/test-reports/performance"
PERFORMANCE_TEST_PACKAGE="com.bank.calculator.test.performance"
PERFORMANCE_TAG="performance"
VERBOSE=false
SPECIFIC_TEST=""
CUSTOM_JVM_OPTS=""

# Function to display a banner
display_banner() {
    echo "========================================================"
    echo "  Compound Interest Calculator - Performance Tests"
    echo "========================================================"
    echo
}

# Function to display usage information
display_help() {
    echo "Usage: $0 [options]"
    echo
    echo "Run performance tests for the Compound Interest Calculator application."
    echo "These tests verify the application meets performance requirements for"
    echo "calculation speed, UI responsiveness, memory usage, and startup time."
    echo
    echo "Options:"
    echo "  -h, --help                  Display this help message"
    echo "  -v, --verbose               Enable verbose output"
    echo "  -r, --report-dir DIR        Specify custom report directory (default: $TEST_REPORT_DIR)"
    echo "  -t, --test TEST_NAME        Run specific performance test class or method"
    echo "  -j, --jvm-opts OPTIONS      Specify custom JVM options for Maven"
    echo
    echo "Examples:"
    echo "  $0 -v                       Run all performance tests with verbose output"
    echo "  $0 -t CalculationPerformanceTest  Run only calculation performance tests"
    echo "  $0 -j \"-Xmx2048m\"          Run tests with 2GB heap size"
}

# Parse command-line arguments
parse_arguments() {
    while [[ $# -gt 0 ]]; do
        case $1 in
            -h|--help)
                display_banner
                display_help
                exit 0
                ;;
            -v|--verbose)
                VERBOSE=true
                shift
                ;;
            -r|--report-dir)
                TEST_REPORT_DIR="$2"
                shift 2
                ;;
            -t|--test)
                SPECIFIC_TEST="$2"
                shift 2
                ;;
            -j|--jvm-opts)
                CUSTOM_JVM_OPTS="$2"
                shift 2
                ;;
            *)
                echo "Error: Unknown option $1"
                display_help
                exit 1
                ;;
        esac
    done
}

# Check prerequisites
check_prerequisites() {
    echo "Checking prerequisites..."
    
    # Check for Java
    if ! command -v java &> /dev/null; then
        echo "Error: Java is not installed or not in PATH"
        exit 1
    fi
    
    # Check for Maven
    if ! command -v mvn &> /dev/null; then
        echo "Error: Maven is not installed or not in PATH"
        exit 1
    fi
    
    # Check for performance test XML
    PERFORMANCE_XML="$PROJECT_ROOT/src/test/src/test/resources/test-suites/performance-tests.xml"
    if [ ! -f "$PERFORMANCE_XML" ]; then
        echo "Error: Performance test suite XML not found at:"
        echo "$PERFORMANCE_XML"
        exit 1
    fi
    
    # Check for performance thresholds file
    THRESHOLD_FILE="$PROJECT_ROOT/src/test/src/test/resources/test-data/performance-thresholds.json"
    if [ ! -f "$THRESHOLD_FILE" ]; then
        echo "Warning: Performance thresholds file not found at:"
        echo "$THRESHOLD_FILE"
        echo "Will use default threshold values."
    fi
    
    echo "Prerequisites check passed!"
    return 0
}

# Run the performance tests
run_performance_tests() {
    display_banner
    echo "Starting performance tests..."
    
    # Set environment variables
    if [ -n "$CUSTOM_JVM_OPTS" ]; then
        export MAVEN_OPTS="$CUSTOM_JVM_OPTS"
    else
        export MAVEN_OPTS="$MAVEN_OPTS"
    fi
    
    # Create report directory if it doesn't exist
    mkdir -p "$TEST_REPORT_DIR"
    
    # Change to project root directory
    cd "$PROJECT_ROOT"
    
    # Construct Maven command to run PerformanceTestsRunner
    MAVEN_CMD="mvn exec:java"
    MAVEN_CMD="$MAVEN_CMD -Dexec.mainClass=com.bank.calculator.test.runner.PerformanceTestsRunner"
    
    # Add system properties for configuration
    # Set the report directory
    MAVEN_CMD="$MAVEN_CMD -Dperformance.report.dir=$TEST_REPORT_DIR"
    
    # Add test package filter
    MAVEN_CMD="$MAVEN_CMD -Dperformance.test.package=$PERFORMANCE_TEST_PACKAGE"
    
    # Add specific test if provided
    if [ -n "$SPECIFIC_TEST" ]; then
        MAVEN_CMD="$MAVEN_CMD -Dperformance.test.name=$SPECIFIC_TEST"
    fi
    
    # Add verbose flag if enabled
    if [ "$VERBOSE" = true ]; then
        MAVEN_CMD="$MAVEN_CMD -Dperformance.verbose=true"
        MAVEN_CMD="$MAVEN_CMD -e"
    fi
    
    # Run Maven command
    if [ "$VERBOSE" = true ]; then
        echo "Executing: $MAVEN_CMD"
    fi
    
    # Execute Maven and capture the exit code
    eval $MAVEN_CMD
    MAVEN_EXIT_CODE=$?
    
    # Check if tests were successful
    if [ $MAVEN_EXIT_CODE -eq 0 ]; then
        echo "========================================================"
        echo "  Performance Tests Completed Successfully"
        echo "========================================================"
    else
        echo "========================================================"
        echo "  Performance Tests Failed with Exit Code $MAVEN_EXIT_CODE"
        echo "========================================================"
    fi
    
    return $MAVEN_EXIT_CODE
}

# Generate a report from the test results
generate_report() {
    echo "Generating performance test report..."
    
    # Check if report directory exists
    if [ ! -d "$TEST_REPORT_DIR" ]; then
        echo "Error: Report directory does not exist: $TEST_REPORT_DIR"
        return 1
    fi
    
    # Generate a summary of performance metrics
    echo "Generating performance metrics summary..."
    SUMMARY_FILE="$TEST_REPORT_DIR/performance-summary.txt"
    
    echo "Performance Metrics Summary" > "$SUMMARY_FILE"
    echo "===============================" >> "$SUMMARY_FILE"
    echo "Test executed: $(date)" >> "$SUMMARY_FILE"
    echo >> "$SUMMARY_FILE"
    
    # In a real implementation, this would parse the test results and extract performance metrics
    echo "Calculation Performance:" >> "$SUMMARY_FILE"
    echo "  Required: < 200ms" >> "$SUMMARY_FILE"
    echo "  Actual: [Extracted from test results]" >> "$SUMMARY_FILE"
    echo >> "$SUMMARY_FILE"
    
    echo "UI Responsiveness:" >> "$SUMMARY_FILE"
    echo "  Required: < 100ms" >> "$SUMMARY_FILE"
    echo "  Actual: [Extracted from test results]" >> "$SUMMARY_FILE"
    echo >> "$SUMMARY_FILE"
    
    echo "Memory Usage:" >> "$SUMMARY_FILE"
    echo "  Required: < 100MB" >> "$SUMMARY_FILE"
    echo "  Actual: [Extracted from test results]" >> "$SUMMARY_FILE"
    echo >> "$SUMMARY_FILE"
    
    echo "Startup Time:" >> "$SUMMARY_FILE"
    echo "  Required: < 3000ms" >> "$SUMMARY_FILE"
    echo "  Actual: [Extracted from test results]" >> "$SUMMARY_FILE"
    
    echo
    echo "Performance test report generated at: $TEST_REPORT_DIR"
    echo "Performance metrics summary: $SUMMARY_FILE"
}

# Main script execution
main() {
    # Parse command-line arguments
    parse_arguments "$@"
    
    # Check prerequisites
    check_prerequisites
    
    # Run the performance tests
    run_performance_tests
    TEST_EXIT_CODE=$?
    
    # Generate report if tests were successful
    if [ $TEST_EXIT_CODE -eq 0 ]; then
        generate_report
    fi
    
    echo
    if [ $TEST_EXIT_CODE -eq 0 ]; then
        echo "Performance tests completed successfully!"
    else
        echo "Performance tests failed with exit code: $TEST_EXIT_CODE"
    fi
    
    # Return the test exit code
    return $TEST_EXIT_CODE
}

# Call the main function with all script arguments
main "$@"