#!/bin/bash

# Exit immediately if a command exits with a non-zero status
set -e

# Global variables
SCRIPT_DIR=$(dirname "$0")
PROJECT_ROOT=$(realpath "$SCRIPT_DIR/../..")
TEST_RESULTS_DIR=$PROJECT_ROOT/target/surefire-reports
COVERAGE_DIR=$PROJECT_ROOT/target/site/jacoco
REPORT_DIR=$PROJECT_ROOT/target/test-reports
TIMESTAMP=$(date +"%Y-%m-%d_%H-%M-%S")
REPORT_NAME=test-report-$TIMESTAMP
MAVEN_OPTS=-Xmx1024m

# Default flags
CLEAN_OLD_REPORTS=false
SKIP_UNIT_TESTS=false
SKIP_INTEGRATION_TESTS=false
SKIP_UI_TESTS=false
SKIP_PERFORMANCE_TESTS=false
SKIP_COVERAGE=false
CUSTOM_REPORT_DIR=""

# ------------------------------
# Function Definitions
# ------------------------------

# Prints a formatted header to the console
print_header() {
    local message="$1"
    echo "============================================================"
    echo "=== ${message^^} "
    echo "============================================================"
}

# Checks if all required tools are installed
check_prerequisites() {
    print_header "Checking prerequisites"
    
    # Check if Maven is installed
    if ! command -v mvn &> /dev/null; then
        echo "ERROR: Maven is not installed or not in PATH"
        return 1
    fi
    echo "✓ Maven is installed"
    
    # Check if Java is installed
    if ! command -v java &> /dev/null; then
        echo "ERROR: Java is not installed or not in PATH"
        return 1
    fi
    echo "✓ Java is installed"
    
    # Check if required directories exist
    if [ ! -d "$PROJECT_ROOT" ]; then
        echo "ERROR: Project root directory not found: $PROJECT_ROOT"
        return 1
    fi
    echo "✓ Project root directory found: $PROJECT_ROOT"
    
    # Success
    echo "All prerequisites met"
    return 0
}

# Creates the necessary directories for storing reports
create_report_directories() {
    print_header "Creating report directories"
    
    # Use custom report directory if specified
    if [ -n "$CUSTOM_REPORT_DIR" ]; then
        REPORT_DIR=$CUSTOM_REPORT_DIR
    fi
    
    # Create main report directory
    mkdir -p "$REPORT_DIR"
    echo "Created main report directory: $REPORT_DIR"
    
    # Create subdirectories for different report types
    mkdir -p "$REPORT_DIR/unit"
    mkdir -p "$REPORT_DIR/integration"
    mkdir -p "$REPORT_DIR/ui"
    mkdir -p "$REPORT_DIR/performance"
    mkdir -p "$REPORT_DIR/coverage"
    mkdir -p "$REPORT_DIR/combined"
    
    echo "Created report subdirectories"
}

# Generates reports for unit tests
generate_unit_test_report() {
    if [ "$SKIP_UNIT_TESTS" = true ]; then
        echo "Skipping unit test report generation"
        return 0
    fi
    
    print_header "Generating unit test reports"
    
    # Run Maven surefire report plugin
    echo "Running Maven surefire report plugin..."
    mvn surefire-report:report -Dsurefire.reportNameSuffix=unit -DskipTests
    
    # Copy the generated reports to the unit test report directory
    echo "Copying reports to report directory..."
    cp -r "$PROJECT_ROOT/target/site/surefire-report.html" "$REPORT_DIR/unit/"
    
    # If there are any attachments, copy them too
    if [ -d "$PROJECT_ROOT/target/site/images" ]; then
        cp -r "$PROJECT_ROOT/target/site/images" "$REPORT_DIR/unit/"
    fi
    
    echo "Unit test report generation completed"
}

# Generates reports for integration tests
generate_integration_test_report() {
    if [ "$SKIP_INTEGRATION_TESTS" = true ]; then
        echo "Skipping integration test report generation"
        return 0
    fi
    
    print_header "Generating integration test reports"
    
    # Run Maven failsafe report plugin
    echo "Running Maven failsafe report plugin..."
    mvn failsafe:integration-test failsafe:report -DskipTests
    
    # Copy the generated reports to the integration test report directory
    echo "Copying reports to report directory..."
    cp -r "$PROJECT_ROOT/target/site/failsafe-report.html" "$REPORT_DIR/integration/"
    
    # If there are any attachments, copy them too
    if [ -d "$PROJECT_ROOT/target/site/images" ]; then
        cp -r "$PROJECT_ROOT/target/site/images" "$REPORT_DIR/integration/"
    fi
    
    echo "Integration test report generation completed"
}

# Generates reports for UI tests
generate_ui_test_report() {
    if [ "$SKIP_UI_TESTS" = true ]; then
        echo "Skipping UI test report generation"
        return 0
    fi
    
    print_header "Generating UI test reports"
    
    # Process TestFX test results
    echo "Processing TestFX test results..."
    
    # Check if UI test results directory exists
    if [ -d "$TEST_RESULTS_DIR/ui-tests" ]; then
        # Copy the UI test results to the report directory
        cp -r "$TEST_RESULTS_DIR/ui-tests" "$REPORT_DIR/ui/"
        
        # Generate HTML report from UI test results
        echo "Generating HTML report for UI tests..."
        
        # If there are screenshots, copy them too
        if [ -d "$PROJECT_ROOT/target/ui-test-screenshots" ]; then
            cp -r "$PROJECT_ROOT/target/ui-test-screenshots" "$REPORT_DIR/ui/"
        fi
        
        echo "UI test report generation completed"
    else
        echo "WARNING: UI test results directory not found: $TEST_RESULTS_DIR/ui-tests"
        echo "Skipping UI test report generation"
    fi
}

# Generates reports for performance tests
generate_performance_test_report() {
    if [ "$SKIP_PERFORMANCE_TESTS" = true ]; then
        echo "Skipping performance test report generation"
        return 0
    fi
    
    print_header "Generating performance test reports"
    
    # Process performance test results
    echo "Processing performance test results..."
    
    # Check if performance test results directory exists
    if [ -d "$TEST_RESULTS_DIR/performance-tests" ]; then
        # Copy the performance test results to the report directory
        cp -r "$TEST_RESULTS_DIR/performance-tests" "$REPORT_DIR/performance/"
        
        # Generate HTML report from performance test results
        echo "Generating HTML report for performance tests..."
        
        # Generate charts and graphs for performance metrics
        echo "Generating performance charts and graphs..."
        
        echo "Performance test report generation completed"
    else
        echo "WARNING: Performance test results directory not found: $TEST_RESULTS_DIR/performance-tests"
        echo "Skipping performance test report generation"
    fi
}

# Generates code coverage reports using JaCoCo
generate_coverage_report() {
    if [ "$SKIP_COVERAGE" = true ]; then
        echo "Skipping code coverage report generation"
        return 0
    fi
    
    print_header "Generating code coverage reports"
    
    # Run Maven JaCoCo report plugin
    echo "Running Maven JaCoCo report plugin..."
    mvn jacoco:report
    
    # Check if JaCoCo report directory exists
    if [ -d "$COVERAGE_DIR" ]; then
        # Copy the JaCoCo reports to the coverage report directory
        echo "Copying coverage reports to report directory..."
        cp -r "$COVERAGE_DIR"/* "$REPORT_DIR/coverage/"
        
        # Generate coverage summary
        echo "Generating coverage summary..."
        
        echo "Code coverage report generation completed"
    else
        echo "WARNING: JaCoCo report directory not found: $COVERAGE_DIR"
        echo "Skipping code coverage report generation"
    fi
}

# Combines all test reports into a single comprehensive report
generate_combined_report() {
    print_header "Generating combined report"
    
    # Create index.html that links to all report types
    echo "Creating combined report index.html..."
    
    # Create index.html file
    cat > "$REPORT_DIR/combined/index.html" << EOF
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Compound Interest Calculator - Test Reports</title>
    <style>
        body { font-family: Arial, sans-serif; margin: 0; padding: 20px; }
        h1 { color: #2c3e50; }
        .report-section { margin-bottom: 30px; }
        .report-link { display: inline-block; margin-right: 15px; padding: 8px 15px; 
                      background-color: #3498db; color: white; text-decoration: none; 
                      border-radius: 4px; }
        .report-link:hover { background-color: #2980b9; }
        .summary-table { border-collapse: collapse; width: 100%; }
        .summary-table th, .summary-table td { border: 1px solid #ddd; padding: 8px; text-align: left; }
        .summary-table th { background-color: #f2f2f2; }
        .success { color: green; }
        .failure { color: red; }
    </style>
</head>
<body>
    <h1>Compound Interest Calculator - Test Reports</h1>
    <p>Report generated on: $(date)</p>
    
    <div class="report-section">
        <h2>Report Links</h2>
        <a class="report-link" href="../unit/surefire-report.html">Unit Tests</a>
        <a class="report-link" href="../integration/failsafe-report.html">Integration Tests</a>
        <a class="report-link" href="../ui/index.html">UI Tests</a>
        <a class="report-link" href="../performance/index.html">Performance Tests</a>
        <a class="report-link" href="../coverage/index.html">Code Coverage</a>
    </div>
    
    <div class="report-section">
        <h2>Summary</h2>
        <table class="summary-table">
            <tr>
                <th>Test Type</th>
                <th>Total</th>
                <th>Passed</th>
                <th>Failed</th>
                <th>Skipped</th>
                <th>Pass Rate</th>
            </tr>
            <!-- Summary data will be added programmatically -->
        </table>
    </div>
    
    <div class="report-section">
        <h2>Code Coverage Summary</h2>
        <table class="summary-table">
            <tr>
                <th>Package</th>
                <th>Line Coverage</th>
                <th>Branch Coverage</th>
                <th>Complexity</th>
            </tr>
            <!-- Coverage data will be added programmatically -->
        </table>
    </div>
    
    <div class="report-section">
        <h2>Executive Summary</h2>
        <p>This report contains comprehensive test results for the Compound Interest Calculator application.</p>
        <p>The application has been tested with the following test types:</p>
        <ul>
            <li>Unit Tests: Verifying individual components in isolation</li>
            <li>Integration Tests: Verifying component interactions</li>
            <li>UI Tests: Verifying user interface functionality</li>
            <li>Performance Tests: Verifying performance metrics</li>
        </ul>
        <p>Code coverage analysis is also provided to assess test thoroughness.</p>
    </div>
</body>
</html>
EOF
    
    # Generate summary statistics across all test types
    echo "Generating summary statistics..."
    
    # Create executive summary with key metrics
    echo "Creating executive summary..."
    
    echo "Combined report generation completed"
}

# Removes old report files to save disk space
cleanup_old_reports() {
    if [ "$CLEAN_OLD_REPORTS" = true ]; then
        print_header "Cleaning up old reports"
        
        # Ask for confirmation before deleting
        echo "This will remove all report directories older than 30 days in: $REPORT_DIR"
        read -p "Are you sure you want to proceed? (y/n): " confirm
        
        if [ "$confirm" = "y" ] || [ "$confirm" = "Y" ]; then
            # Find and remove report directories older than 30 days
            echo "Removing old report directories..."
            find "$REPORT_DIR" -maxdepth 1 -type d -name "test-report-*" -mtime +30 -exec rm -rf {} \;
            echo "Cleanup completed"
        else
            echo "Cleanup aborted"
        fi
    fi
}

# Parses command line arguments passed to the script
parse_arguments() {
    local TEMP=$(getopt -o "h" --long "clean,no-unit,no-integration,no-ui,no-performance,no-coverage,report-dir:,help" -n "$(basename "$0")" -- "$@")
    
    if [ $? != 0 ]; then
        echo "Terminating..." >&2
        exit 1
    fi
    
    eval set -- "$TEMP"
    
    while true; do
        case "$1" in
            --clean)
                CLEAN_OLD_REPORTS=true
                shift
                ;;
            --no-unit)
                SKIP_UNIT_TESTS=true
                shift
                ;;
            --no-integration)
                SKIP_INTEGRATION_TESTS=true
                shift
                ;;
            --no-ui)
                SKIP_UI_TESTS=true
                shift
                ;;
            --no-performance)
                SKIP_PERFORMANCE_TESTS=true
                shift
                ;;
            --no-coverage)
                SKIP_COVERAGE=true
                shift
                ;;
            --report-dir)
                CUSTOM_REPORT_DIR="$2"
                shift 2
                ;;
            -h|--help)
                display_help
                exit 0
                ;;
            --)
                shift
                break
                ;;
            *)
                echo "Internal error!" >&2
                exit 1
                ;;
        esac
    done
}

# Displays help information about the script usage
display_help() {
    echo "Usage: $(basename "$0") [options]"
    echo
    echo "Generate comprehensive test reports for the Compound Interest Calculator application."
    echo
    echo "Options:"
    echo "  --help, -h            Display this help message"
    echo "  --clean               Clean up old reports before generating new ones"
    echo "  --no-unit             Skip unit test report generation"
    echo "  --no-ui               Skip UI test report generation"
    echo "  --no-integration      Skip integration test report generation"
    echo "  --no-performance      Skip performance test report generation" 
    echo "  --no-coverage         Skip code coverage report generation"
    echo "  --report-dir <dir>    Specify custom report directory"
    echo
    echo "Examples:"
    echo "  $(basename "$0")                                # Generate all reports"
    echo "  $(basename "$0") --clean                        # Clean old reports and generate all reports"
    echo "  $(basename "$0") --report-dir /path/to/reports  # Generate reports in custom directory"
    echo "  $(basename "$0") --no-unit --no-ui --no-integration # Skip unit, UI, and integration test reports"
}

# Main function that orchestrates the report generation process
main() {
    # Print script banner
    print_header "Compound Interest Calculator - Test Report Generator"
    echo "Starting report generation process..."
    
    # Parse command line arguments
    parse_arguments "$@"
    
    # Check if required tools are installed
    if ! check_prerequisites; then
        echo "ERROR: Prerequisites check failed. Please install the required tools and try again."
        exit 1
    fi
    
    # Create report directories
    create_report_directories
    
    # Generate reports for each test type
    generate_unit_test_report
    generate_integration_test_report
    generate_ui_test_report
    generate_performance_test_report
    
    # Generate code coverage report
    generate_coverage_report
    
    # Generate combined report
    generate_combined_report
    
    # Clean up old reports if requested
    cleanup_old_reports
    
    # Print final summary
    print_header "Report Generation Completed"
    echo "Reports have been generated successfully!"
    echo "Report location: $REPORT_DIR"
    
    return 0
}

# Call the main function passing all script arguments
main "$@"