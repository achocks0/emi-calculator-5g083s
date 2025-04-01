#!/bin/bash
# ========================================================================
# Main Test Runner Script for Compound Interest Calculator
# ========================================================================
# This script serves as the main entry point for running all test categories
# (unit, integration, UI, performance, and security) for the Compound Interest
# Calculator application. It provides a convenient way to execute tests from 
# the command line on Unix/Linux/macOS systems.
#
# Author: Bank Calculator Team
# Version: 1.0
# ========================================================================

# Exit on error
set -e

# Set default values for global variables
MAVEN_OPTS="-Xmx1024m"
TEST_REPORT_DIR="../target/test-reports"
SCRIPT_DIR=$(dirname "$0")
PROJECT_ROOT=$(realpath "$SCRIPT_DIR/../..")
VERBOSE=false
TEST_CATEGORIES="all"
CUSTOM_JVM_OPTS=""

# Function to display help information
display_help() {
    echo "Usage: $(basename "$0") [OPTIONS]"
    echo ""
    echo "Run tests for the Compound Interest Calculator application."
    echo ""
    echo "Options:"
    echo "  -h, --help                 Display this help message and exit"
    echo "  -v, --verbose              Enable verbose output"
    echo "  -c, --category CATEGORY    Specify test category to run (unit, integration, ui, performance, security, or all)"
    echo "  -r, --report-dir DIR       Specify custom report directory (default: $TEST_REPORT_DIR)"
    echo "  -j, --jvm-opts OPTS        Specify custom JVM options for Maven"
    echo ""
    echo "Examples:"
    echo "  $(basename "$0") -c unit              Run only unit tests"
    echo "  $(basename "$0") -c ui -v             Run UI tests with verbose output"
    echo "  $(basename "$0") -r ./reports         Save reports to ./reports directory"
    echo "  $(basename "$0")                      Run all test categories"
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
            -c|--category)
                if [[ -n "$2" && ! "$2" =~ ^- ]]; then
                    TEST_CATEGORIES="$2"
                    shift 2
                else
                    echo "Error: Option -c|--category requires a category argument."
                    echo "Valid categories: unit, integration, ui, performance, security, all"
                    exit 3
                fi
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

    # Validate test category
    if [[ "$TEST_CATEGORIES" != "all" && \
          "$TEST_CATEGORIES" != "unit" && \
          "$TEST_CATEGORIES" != "integration" && \
          "$TEST_CATEGORIES" != "ui" && \
          "$TEST_CATEGORIES" != "performance" && \
          "$TEST_CATEGORIES" != "security" ]]; then
        echo "Error: Invalid test category: $TEST_CATEGORIES"
        echo "Valid categories: unit, integration, ui, performance, security, all"
        exit 3
    fi
}

# Function to check prerequisites for running tests
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

    # Check if test suite XML files exist
    if [ ! -f "$PROJECT_ROOT/src/test/src/test/resources/test-suites/all-tests.xml" ]; then
        echo "Error: all-tests.xml file not found."
        return 1
    fi

    echo "All prerequisites are met."
    return 0
}

# Function to set up the environment for running tests
setup_environment() {
    echo "Setting up environment..."

    # Set up MAVEN_OPTS
    export MAVEN_OPTS="$MAVEN_OPTS"
    if [ -n "$CUSTOM_JVM_OPTS" ]; then
        export MAVEN_OPTS="$MAVEN_OPTS $CUSTOM_JVM_OPTS"
    fi

    # Create test report directory if it doesn't exist
    mkdir -p "$TEST_REPORT_DIR"

    echo "Environment setup complete."
}

# Function to run a specific test category
run_specific_category() {
    local category="$1"
    echo "Running $category tests..."

    # Construct appropriate command based on category
    case "$category" in
        unit)
            # Use the run-unit-tests.sh script
            "$SCRIPT_DIR/run-unit-tests.sh" ${VERBOSE:+--verbose} --report-dir "$TEST_REPORT_DIR/unit" ${CUSTOM_JVM_OPTS:+--jvm-opts "$CUSTOM_JVM_OPTS"}
            return $?
            ;;
        integration)
            # Run integration tests using Maven
            cd "$PROJECT_ROOT"
            local mvn_cmd="mvn verify -P integration-tests -Dtest.report.dir=${TEST_REPORT_DIR}/integration"
            if [ "$VERBOSE" = true ]; then
                mvn_cmd="$mvn_cmd -X"
            fi
            eval "$mvn_cmd"
            return $?
            ;;
        ui)
            # Run UI tests using Maven
            cd "$PROJECT_ROOT"
            local mvn_cmd="mvn test -P ui-tests -Dtest.report.dir=${TEST_REPORT_DIR}/ui"
            if [ "$VERBOSE" = true ]; then
                mvn_cmd="$mvn_cmd -X"
            fi
            eval "$mvn_cmd"
            return $?
            ;;
        performance)
            # Run performance tests using Maven
            cd "$PROJECT_ROOT"
            local mvn_cmd="mvn test -P performance-tests -Dtest.report.dir=${TEST_REPORT_DIR}/performance"
            if [ "$VERBOSE" = true ]; then
                mvn_cmd="$mvn_cmd -X"
            fi
            eval "$mvn_cmd"
            return $?
            ;;
        security)
            # Run security tests using Maven
            cd "$PROJECT_ROOT"
            local mvn_cmd="mvn test -P security-tests -Dtest.report.dir=${TEST_REPORT_DIR}/security"
            if [ "$VERBOSE" = true ]; then
                mvn_cmd="$mvn_cmd -X"
            fi
            eval "$mvn_cmd"
            return $?
            ;;
        *)
            echo "Error: Unknown test category: $category"
            return 1
            ;;
    esac
}

# Function to run all test categories
run_all_categories() {
    echo "Running all test categories..."

    # Execute Maven command with all-tests profile
    cd "$PROJECT_ROOT"
    local mvn_cmd="mvn test -P all-tests -Dtest.report.dir=${TEST_REPORT_DIR}"
    
    if [ "$VERBOSE" = true ]; then
        mvn_cmd="$mvn_cmd -X"
    fi

    eval "$mvn_cmd"
    return $?
}

# Function to generate comprehensive test report
generate_report() {
    echo "Generating comprehensive test report..."

    # Generate HTML report using Maven site plugin
    cd "$PROJECT_ROOT"
    mvn site:site -DgenerateReports=true -DoutputDirectory="$TEST_REPORT_DIR"

    echo "Test report generated at: $TEST_REPORT_DIR"
}

# Main function that executes all or selected test categories
run_tests() {
    # Display banner
    echo "========================================================================="
    echo "               Compound Interest Calculator Test Runner"
    echo "========================================================================="
    echo "Starting test execution at $(date)"
    echo ""

    # Check prerequisites
    if ! check_prerequisites; then
        echo "Error: Prerequisites check failed."
        return 2
    fi

    # Set up environment
    setup_environment

    # Run tests based on selected category
    local exit_code=0
    if [ "$TEST_CATEGORIES" = "all" ]; then
        # Run all test categories
        run_all_categories
        exit_code=$?
    else
        # Run specific test category
        run_specific_category "$TEST_CATEGORIES"
        exit_code=$?
    fi

    # Generate comprehensive report
    if [ $exit_code -eq 0 ]; then
        generate_report
    fi

    # Display summary
    echo ""
    if [ $exit_code -eq 0 ]; then
        echo "✓ All tests completed successfully!"
    else
        echo "✗ One or more tests failed with exit code: $exit_code"
    fi
    echo "Test reports available at: $TEST_REPORT_DIR"
    
    return $exit_code
}

# Parse command-line arguments
parse_arguments "$@"

# Run tests
run_tests
exit $?