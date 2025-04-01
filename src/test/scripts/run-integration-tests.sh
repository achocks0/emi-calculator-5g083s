#!/bin/bash
# ========================================================================
# Integration Tests Runner Script for Compound Interest Calculator
# ========================================================================
# This script executes integration tests for the Compound Interest Calculator
# application. It provides a convenient way to run integration tests from the
# command line on Unix/Linux/macOS systems, focusing on component interactions
# and end-to-end workflows.
#
# Author: Bank Calculator Team
# Version: 1.0
# ========================================================================

# Exit on error
set -e

# Source common functions from the main test script
SCRIPT_DIR=$(dirname "$0")
if [ -f "$SCRIPT_DIR/run-tests.sh" ]; then
    source "$SCRIPT_DIR/run-tests.sh"
else
    echo "Error: Common test functions not found. Make sure run-tests.sh exists."
    exit 1
fi

# Set default values for global variables
MAVEN_OPTS="-Xmx1024m"
TEST_REPORT_DIR="../target/integration-test-reports"
PROJECT_ROOT=$(realpath "$SCRIPT_DIR/../..")
VERBOSE=false

# Function to display help information
display_help() {
    echo "Usage: $(basename "$0") [OPTIONS]"
    echo ""
    echo "Run integration tests for the Compound Interest Calculator application."
    echo ""
    echo "Options:"
    echo "  -h, --help                Display this help message and exit"
    echo "  -v, --verbose             Enable verbose output"
    echo "  -r, --report-dir DIR      Specify custom report directory (default: $TEST_REPORT_DIR)"
    echo ""
    echo "Examples:"
    echo "  $(basename "$0") -v                 Run tests with verbose output"
    echo "  $(basename "$0") -r ./reports       Save reports to ./reports directory"
    echo ""
    echo "Exit Codes:"
    echo "  0  All tests passed"
    echo "  1  One or more tests failed"
    echo "  2  Prerequisites check failed"
    echo "  3  Invalid command-line arguments"
}

# Function to parse command-line arguments
parse_arguments() {
    while [[ $# -gt 0 ]]; do
        case "$1" in
            -h|--help)
                display_help
                exit 0
                ;;
            -v|--verbose)
                VERBOSE=true
                shift
                ;;
            -r|--report-dir)
                if [[ -n "$2" && ! "$2" =~ ^- ]]; then
                    TEST_REPORT_DIR="$2"
                    shift 2
                else
                    echo "Error: Option -r|--report-dir requires a directory argument."
                    exit 3
                fi
                ;;
            *)
                echo "Error: Unknown option: $1"
                echo "Try '$(basename "$0") --help' for more information."
                exit 3
                ;;
        esac
    done
}

# Function to run integration tests
run_integration_tests() {
    # Display banner
    echo "========================================================================="
    echo "             Compound Interest Calculator Integration Tests"
    echo "========================================================================="
    echo "Starting integration tests execution at $(date)"
    echo ""

    # Set environment variables
    export MAVEN_OPTS

    # Create test report directory if it doesn't exist
    mkdir -p "$TEST_REPORT_DIR"

    # Change to project root directory
    cd "$PROJECT_ROOT"

    # Construct Maven command
    local mvn_cmd="mvn verify -P integration-tests -Dtest.report.dir=${TEST_REPORT_DIR}"
    
    if [ "$VERBOSE" = true ]; then
        mvn_cmd="$mvn_cmd -X"
        echo "Running command: $mvn_cmd"
    fi

    # Execute Maven command
    echo "Executing integration tests..."
    if eval "$mvn_cmd"; then
        echo ""
        echo "✓ All integration tests passed successfully!"
        echo "Test reports available at: $TEST_REPORT_DIR"
        # Generate detailed report
        generate_report
        return 0
    else
        local exit_code=$?
        echo ""
        echo "✗ One or more integration tests failed."
        echo "Test reports available at: $TEST_REPORT_DIR"
        return $exit_code
    fi
}

# Function to generate an integration test report
generate_report() {
    echo "Generating integration test report..."
    
    # Create report directory if it doesn't exist
    mkdir -p "$TEST_REPORT_DIR"
    
    # Generate HTML report using Maven site plugin
    mvn site:site -DgenerateReports=true -Dtest.category=integration -DoutputDirectory="$TEST_REPORT_DIR" -quiet
    
    echo "Integration test report generated at: $TEST_REPORT_DIR/index.html"
}

# Parse command-line arguments
parse_arguments "$@"

# Check prerequisites
if ! check_prerequisites; then
    echo "Error: Prerequisites check failed."
    exit 2
fi

# Run integration tests
run_integration_tests
exit $?