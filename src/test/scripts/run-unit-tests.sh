#!/bin/bash
# ========================================================================
# Unit Tests Runner Script for Compound Interest Calculator
# ========================================================================
# This script executes unit tests for the Compound Interest Calculator application
# using the UnitTestsRunner class. It provides a convenient way to run only the 
# unit tests from the command line on Unix/Linux/macOS systems.
#
# Author: Bank Calculator Team
# Version: 1.0
# ========================================================================

# Exit on error
set -e

# Set default values for global variables
MAVEN_OPTS="-Xmx512m"
TEST_REPORT_DIR="../target/test-reports/unit"
SCRIPT_DIR=$(dirname "$0")
PROJECT_ROOT=$(realpath "$SCRIPT_DIR/../..")
VERBOSE=false
CUSTOM_JVM_OPTS=""

# Function to display help information
display_help() {
    echo "Usage: $(basename "$0") [OPTIONS]"
    echo ""
    echo "Run unit tests for the Compound Interest Calculator application."
    echo ""
    echo "Options:"
    echo "  -h, --help               Display this help message and exit"
    echo "  -v, --verbose            Enable verbose output"
    echo "  -r, --report-dir DIR     Specify custom report directory (default: $TEST_REPORT_DIR)"
    echo "  -j, --jvm-opts OPTS      Specify custom JVM options for Maven"
    echo ""
    echo "Examples:"
    echo "  $(basename "$0") -v                  Run tests with verbose output"
    echo "  $(basename "$0") -r ./reports        Save reports to ./reports directory"
    echo "  $(basename "$0") -j \"-Xmx1g -XX:+UseG1GC\"  Run with custom JVM options"
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

# Function to check prerequisites for running unit tests
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

    # Check if unit-tests.xml file exists
    if [ ! -f "$PROJECT_ROOT/src/test/src/test/resources/test-suites/unit-tests.xml" ]; then
        echo "Error: unit-tests.xml file not found."
        return 1
    fi

    echo "All prerequisites are met."
    return 0
}

# Function to set up the environment for running unit tests
setup_environment() {
    echo "Setting up environment..."

    # Set up MAVEN_OPTS
    if [ -n "$CUSTOM_JVM_OPTS" ]; then
        export MAVEN_OPTS="$MAVEN_OPTS $CUSTOM_JVM_OPTS"
    fi

    # Create test report directory if it doesn't exist
    mkdir -p "$TEST_REPORT_DIR"

    echo "Environment setup complete."
}

# Function to execute Maven command to run unit tests
execute_maven_command() {
    echo "Executing unit tests..."

    # Change to project root directory
    cd "$PROJECT_ROOT"

    # Construct Maven command
    local mvn_cmd="mvn test -P unit-tests -Dtest.report.dir=${TEST_REPORT_DIR}"
    
    if [ "$VERBOSE" = true ]; then
        mvn_cmd="$mvn_cmd -X"
    fi

    # Execute Maven command
    if [ "$VERBOSE" = true ]; then
        echo "Running command: $mvn_cmd"
    fi

    eval "$mvn_cmd"
    return $?
}

# Main function that executes unit tests
run_unit_tests() {
    # Display banner
    echo "========================================================================="
    echo "                   Compound Interest Calculator"
    echo "                         Unit Tests Runner"
    echo "========================================================================="
    echo "Starting unit tests execution at $(date)"
    echo ""

    # Check prerequisites
    if ! check_prerequisites; then
        echo "Error: Prerequisites check failed."
        return 2
    fi

    # Set up environment
    setup_environment

    # Execute Maven command
    if execute_maven_command; then
        echo ""
        echo "✓ All unit tests passed successfully!"
        echo "Test reports available at: $TEST_REPORT_DIR"
        return 0
    else
        local exit_code=$?
        echo ""
        echo "✗ One or more unit tests failed."
        echo "Test reports available at: $TEST_REPORT_DIR"
        return $exit_code
    fi
}

# Parse command-line arguments
parse_arguments "$@"

# Run unit tests
run_unit_tests
exit $?