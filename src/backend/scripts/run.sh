#!/bin/bash

# Script to run the Compound Interest Calculator application on Unix-based systems
# This script checks for Java availability, verifies the JAR file exists,
# and executes the application with appropriate JVM arguments.

# Get the script directory
SCRIPT_DIR=$(dirname "$0")

# Application details
APP_NAME="Compound Interest Calculator"
JAR_FILE="$SCRIPT_DIR/../target/compound-interest-calculator.jar"
JAVA_OPTS="-Xms256m -Xmx512m"

# Check if Java is installed and available in the PATH
check_java() {
    if ! command -v java >/dev/null 2>&1; then
        echo "Error: Java is not installed or not in the PATH"
        echo "Please install Java 11 or higher to run this application"
        return 1
    fi
    
    # Check Java version to ensure it's at least Java 11
    java_version=$(java -version 2>&1 | grep -i version | cut -d'"' -f2)
    
    # Extract major version number, handling both legacy (1.x) and new (x) version formats
    if [[ $java_version == 1.* ]]; then
        # Legacy version format (1.8.0_XXX)
        major_version=$(echo "$java_version" | awk -F '.' '{print $2}')
    else
        # New version format (11.0.X)
        major_version=$(echo "$java_version" | awk -F '.' '{print $1}')
    fi
    
    # Check if major version is at least 11
    if [[ $major_version -lt 11 ]]; then
        echo "Error: Java version $java_version is not supported"
        echo "Please install Java 11 or higher to run this application"
        return 1
    fi
    
    echo "Java version $java_version found"
    return 0
}

# Check if the application JAR file exists
check_jar() {
    if [ ! -f "$JAR_FILE" ]; then
        echo "Error: Application JAR file not found at $JAR_FILE"
        echo "Please build the application first using Maven:"
        echo "  cd $SCRIPT_DIR/.. && mvn clean package"
        return 1
    fi
    
    echo "Application JAR file found at $JAR_FILE"
    return 0
}

# Run the Compound Interest Calculator application
run_application() {
    echo "Starting $APP_NAME..."
    java $JAVA_OPTS -jar "$JAR_FILE"
    return $?
}

# Main function that orchestrates the script execution
main() {
    echo "======================================================="
    echo "  $APP_NAME"
    echo "======================================================="
    
    # Check Java availability
    if ! check_java; then
        exit 1
    fi
    
    # Check JAR file exists
    if ! check_jar; then
        exit 1
    fi
    
    # Run the application
    run_application
    exit_code=$?
    
    if [ $exit_code -ne 0 ]; then
        echo "Application exited with code $exit_code"
    fi
    
    return $exit_code
}

# Execute main function
main