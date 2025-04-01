#!/bin/bash
# ========================================================================
# UI Tests Runner Script for Compound Interest Calculator
# ========================================================================
# This script executes UI tests for the Compound Interest Calculator application
# using the UITestsRunner class. It provides a convenient way to run UI-specific 
# tests from the command line on Unix/Linux/macOS systems, with options for 
# headless mode and custom configurations.
#
# Author: Bank Calculator Team
# Version: 1.0
# ========================================================================

# Exit on error
set -e

# Set default values for global variables
MAVEN_OPTS="-Xmx1024m"
TEST_REPORT_DIR="../target/test-reports/ui"
SCRIPT_DIR=$(dirname "$0")
PROJECT_ROOT=$(realpath "$SCRIPT_DIR/../..")
VERBOSE=false
HEADLESS=true
CUSTOM_JVM_OPTS=""

# Function to display help information
display_help() {
    echo "Usage: $(basename "$0") [OPTIONS]"
    echo ""
    echo "Run UI tests for the Compound Interest Calculator application."
    echo ""
    echo "Options:"
    echo "  -h, --help                Display this help message and exit"
    echo "  -v, --verbose             Enable verbose output"
    echo "  -n, --no-headless         Disable headless mode (show UI during tests)"
    echo "  -r, --report-dir DIR      Specify custom report directory (default: $TEST_REPORT_DIR)"
    echo "  -j, --jvm-opts OPTS       Specify custom JVM options for Maven"
    echo ""
    echo "Examples:"
    echo "  $(basename "$0") -v                       Run with verbose output"
    echo "  $(basename "$0") -n                       Run in non-headless mode (show UI)"
    echo "  $(basename "$0") -r ./ui-reports          Save reports to ./ui-reports directory"
    echo "  $(basename "$0") -j \"-Xmx2g -XX:+UseG1GC\"  Run with custom JVM options"
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
            -n|--no-headless)
                HEADLESS=false
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
            -j|--jvm-opts)
                if [[ -n "$2" && ! "$2" =~ ^- ]]; then
                    CUSTOM_JVM_OPTS="$2"
                    shift 2
                else
                    echo "Error: Option -j|--jvm-opts requires an argument."
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

# Function to check prerequisites for running UI tests
check_prerequisites() {
    echo "Checking prerequisites..."

    # Check if Java is installed
    if ! command -v java &> /dev/null; then
        echo "Error: Java is not installed or not in the PATH."
        return 1
    fi

    # Check if Maven is installed
    if ! command -v mvn &> /dev/null; then
        echo "Error: Maven is not installed or not in the PATH."
        return 1
    fi

    # Check if JavaFX runtime is available
    java_version=$(java -version 2>&1)
    if ! echo "$java_version" | grep -q "version"; then
        echo "Error: Java runtime check failed."
        return 1
    fi

    # Check if UI test suite XML file exists
    if [ ! -f "$PROJECT_ROOT/src/test/src/test/resources/test-suites/ui-tests.xml" ]; then
        echo "Warning: ui-tests.xml file not found at expected location."
        # Not a fatal error as the Maven profile might handle this
    fi

    echo "All prerequisites are met."
    return 0
}

# Function to set up the environment for running UI tests
setup_environment() {
    echo "Setting up environment..."

    # Set up MAVEN_OPTS
    export MAVEN_OPTS="$MAVEN_OPTS"
    if [ -n "$CUSTOM_JVM_OPTS" ]; then
        export MAVEN_OPTS="$MAVEN_OPTS $CUSTOM_JVM_OPTS"
    fi

    # Set JavaFX-specific environment variables for headless mode
    if [ "$HEADLESS" = true ]; then
        export JAVAFX_HEADLESS=true
        export _JAVA_OPTIONS="$_JAVA_OPTIONS -Dtestfx.robot=glass -Dtestfx.headless=true -Dprism.order=sw -Dprism.text=t2k -Djava.awt.headless=true"
        echo "Configured for headless UI testing"
    else
        export JAVAFX_HEADLESS=false
        echo "Configured for non-headless UI testing (UI will be visible)"
    fi

    # Create test report directory if it doesn't exist
    mkdir -p "$TEST_REPORT_DIR"

    echo "Environment setup complete."
}

# Main function to run UI tests
run_ui_tests() {
    # Display banner
    echo "========================================================================="
    echo "                Compound Interest Calculator UI Tests"
    echo "========================================================================="
    echo "Starting UI tests execution at $(date)"
    echo ""

    # Check prerequisites
    if ! check_prerequisites; then
        echo "Error: Prerequisites check failed."
        return 2
    fi

    # Set up environment
    setup_environment

    # Execute Maven command to run the tests
    cd "$PROJECT_ROOT"
    
    local mvn_cmd="mvn test -P ui-tests -Dtest.report.dir=${TEST_REPORT_DIR} -Dheadless=${HEADLESS}"
    
    if [ "$VERBOSE" = true ]; then
        mvn_cmd="$mvn_cmd -X"
        echo "Running command: $mvn_cmd"
    fi
    
    echo "Executing UI tests using Maven..."
    eval "$mvn_cmd"
    local exit_code=$?
    
    # Display results
    echo ""
    if [ $exit_code -eq 0 ]; then
        echo "✓ All UI tests passed successfully!"
    else
        echo "✗ One or more UI tests failed with exit code: $exit_code"
    fi
    echo "Test reports available at: $TEST_REPORT_DIR"
    echo "UI tests completed at $(date)"
    
    return $exit_code
}

# Parse command-line arguments
parse_arguments "$@"

# Execute UI tests
run_ui_tests
exit $?