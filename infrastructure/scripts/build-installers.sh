#!/bin/bash
#
# build-installers.sh
#
# Master shell script that orchestrates the building of native installers for the
# Compound Interest Calculator application across multiple platforms (Windows, macOS, 
# and Linux). This script serves as the entry point for the installer build process,
# delegating to platform-specific build scripts and coordinating the overall build 
# workflow.
#
# Author: Banking Division
# Date: 2023
#

# Script directory for relative paths
SCRIPT_DIR=$(dirname "$0")

# Application and build configuration
APP_NAME="Compound Interest Calculator"
APP_VERSION="1.0.0"
COMMON_PROPS="$SCRIPT_DIR/../jpackage/common.properties"
OUTPUT_DIR="$SCRIPT_DIR/../installer"
LOG_DIR="$OUTPUT_DIR/logs"
BUILD_LOG="$LOG_DIR/build.log"

# Build flags (can be overridden via command-line arguments)
WINDOWS_ENABLED=true
MACOS_ENABLED=true
LINUX_ENABLED=true
VERIFY_INSTALLERS=true

# Function to print a banner with script information
print_banner() {
    echo "====================================================================="
    echo "           COMPOUND INTEREST CALCULATOR INSTALLER BUILD               "
    echo "====================================================================="
    echo "Application: $APP_NAME"
    echo "Version: $APP_VERSION"
    echo "Date: $(date)"
    echo "---------------------------------------------------------------------"
    echo "This script builds installers for Windows, macOS, and Linux platforms"
    echo "====================================================================="
    echo ""
}

# Function to load properties from the properties file
load_properties() {
    local ret_val=0
    
    # Check if common properties file exists
    if [ ! -f "$COMMON_PROPS" ]; then
        log_message "Common properties file not found: $COMMON_PROPS" "ERROR"
        return 1
    fi
    
    log_message "Loading properties from: $COMMON_PROPS" "INFO"
    
    # Extract app.name and app.version from properties file
    local prop_app_name=$(grep "^app.name=" "$COMMON_PROPS" | cut -d'=' -f2)
    local prop_app_version=$(grep "^app.version=" "$COMMON_PROPS" | cut -d'=' -f2)
    
    # Update global variables if properties are found
    if [ -n "$prop_app_name" ]; then
        APP_NAME="$prop_app_name"
        log_message "Loaded APP_NAME: $APP_NAME" "INFO"
    else
        log_message "Property app.name not found in $COMMON_PROPS" "WARN"
        ret_val=1
    fi
    
    if [ -n "$prop_app_version" ]; then
        APP_VERSION="$prop_app_version"
        log_message "Loaded APP_VERSION: $APP_VERSION" "INFO"
    else
        log_message "Property app.version not found in $COMMON_PROPS" "WARN"
        ret_val=1
    fi
    
    return $ret_val
}

# Function to parse command-line arguments
parse_arguments() {
    local args=("$@")
    
    # Process command-line arguments
    while [[ $# -gt 0 ]]; do
        case "$1" in
            --windows)
                if [ "$2" = "false" ]; then
                    WINDOWS_ENABLED=false
                elif [ "$2" = "true" ]; then
                    WINDOWS_ENABLED=true
                else
                    log_message "Invalid value for --windows flag: $2. Using default: true" "WARN"
                fi
                shift 2
                ;;
            --macos)
                if [ "$2" = "false" ]; then
                    MACOS_ENABLED=false
                elif [ "$2" = "true" ]; then
                    MACOS_ENABLED=true
                else
                    log_message "Invalid value for --macos flag: $2. Using default: true" "WARN"
                fi
                shift 2
                ;;
            --linux)
                if [ "$2" = "false" ]; then
                    LINUX_ENABLED=false
                elif [ "$2" = "true" ]; then
                    LINUX_ENABLED=true
                else
                    log_message "Invalid value for --linux flag: $2. Using default: true" "WARN"
                fi
                shift 2
                ;;
            --verify)
                if [ "$2" = "false" ]; then
                    VERIFY_INSTALLERS=false
                elif [ "$2" = "true" ]; then
                    VERIFY_INSTALLERS=true
                else
                    log_message "Invalid value for --verify flag: $2. Using default: true" "WARN"
                fi
                shift 2
                ;;
            --help)
                echo "Usage: $(basename "$0") [options]"
                echo "Options:"
                echo "  --windows [true|false]  Enable/disable Windows build (default: true)"
                echo "  --macos [true|false]    Enable/disable macOS build (default: true)"
                echo "  --linux [true|false]    Enable/disable Linux build (default: true)"
                echo "  --verify [true|false]   Enable/disable installer verification (default: true)"
                echo "  --help                  Show this help message"
                exit 0
                ;;
            *)
                log_message "Unknown option: $1" "ERROR"
                echo "Try '$(basename "$0") --help' for more information."
                exit 1
                ;;
        esac
    done
    
    # If all builds are disabled, show a warning
    if [ "$WINDOWS_ENABLED" = "false" ] && [ "$MACOS_ENABLED" = "false" ] && [ "$LINUX_ENABLED" = "false" ]; then
        log_message "Warning: All platform builds are disabled. No installers will be built." "WARN"
    fi
    
    # Log build configuration
    log_message "Build configuration:" "INFO"
    log_message "  Windows build: $WINDOWS_ENABLED" "INFO"
    log_message "  macOS build: $MACOS_ENABLED" "INFO"
    log_message "  Linux build: $LINUX_ENABLED" "INFO"
    log_message "  Installer verification: $VERIFY_INSTALLERS" "INFO"
}

# Function to set up the build environment
setup_environment() {
    local ret_val=0
    
    # Create output directory if it doesn't exist
    if [ ! -d "$OUTPUT_DIR" ]; then
        mkdir -p "$OUTPUT_DIR"
        log_message "Created output directory: $OUTPUT_DIR" "INFO"
    fi
    
    # Create logs directory if it doesn't exist
    if [ ! -d "$LOG_DIR" ]; then
        mkdir -p "$LOG_DIR"
        log_message "Created logs directory: $LOG_DIR" "INFO"
    fi
    
    # Initialize build log
    echo "===== BUILD LOG: $(date) =====" > "$BUILD_LOG"
    echo "Application: $APP_NAME $APP_VERSION" >> "$BUILD_LOG"
    echo "=================================" >> "$BUILD_LOG"
    
    # Check for required tools
    if ! command -v java &> /dev/null; then
        log_message "Java not found. Make sure Java is installed and in PATH." "ERROR"
        ret_val=1
    else
        local java_version=$(java -version 2>&1 | head -n 1)
        log_message "Found Java: $java_version" "INFO"
    fi
    
    # Check for JPackage availability
    if ! command -v jpackage &> /dev/null; then
        log_message "JPackage not found. Make sure you're using JDK 14+ with jpackage." "ERROR"
        ret_val=1
    else
        local jpackage_version=$(jpackage --version 2>&1)
        log_message "Found JPackage: $jpackage_version" "INFO"
    fi
    
    # Check if all required scripts exist
    if [ ! -f "$SCRIPT_DIR/build-windows-installer.bat" ]; then
        log_message "Windows installer build script not found: $SCRIPT_DIR/build-windows-installer.bat" "ERROR"
        ret_val=1
    fi
    
    if [ ! -f "$SCRIPT_DIR/build-macos-installer.sh" ]; then
        log_message "macOS installer build script not found: $SCRIPT_DIR/build-macos-installer.sh" "ERROR"
        ret_val=1
    fi
    
    if [ ! -f "$SCRIPT_DIR/build-linux-installer.sh" ]; then
        log_message "Linux installer build script not found: $SCRIPT_DIR/build-linux-installer.sh" "ERROR"
        ret_val=1
    fi
    
    if [ ! -f "$SCRIPT_DIR/verify-installers.sh" ]; then
        log_message "Installer verification script not found: $SCRIPT_DIR/verify-installers.sh" "ERROR"
        ret_val=1
    fi
    
    # Log system information
    log_message "Operating System: $(uname -a)" "INFO"
    
    # Log Java information
    if command -v java &> /dev/null; then
        log_message "Java version: $(java -version 2>&1 | head -n 1)" "INFO"
    fi
    
    # Log environment setup complete
    if [ $ret_val -eq 0 ]; then
        log_message "Build environment setup completed successfully" "INFO"
    else
        log_message "Build environment setup completed with errors" "ERROR"
    fi
    
    return $ret_val
}

# Function to build the Windows installer
build_windows_installer() {
    local ret_val=0
    
    log_message "Starting Windows installer build..." "INFO"
    
    # Skip if Windows build is disabled
    if [ "$WINDOWS_ENABLED" = "false" ]; then
        log_message "Windows build is disabled. Skipping." "INFO"
        return 0
    fi
    
    # Check if Windows build script exists
    if [ ! -f "$SCRIPT_DIR/build-windows-installer.bat" ]; then
        log_message "Windows installer build script not found: $SCRIPT_DIR/build-windows-installer.bat" "ERROR"
        return 1
    fi
    
    # Check if we're on Windows or cross-platform build is allowed
    local os_name=$(uname -s 2>/dev/null || echo "Unknown")
    if [ "$os_name" != "Windows_NT" ] && [ "$os_name" != "MINGW"* ] && [ "$os_name" != "MSYS"* ]; then
        log_message "Not running on Windows. Cross-platform Windows build may have limitations." "WARN"
    fi
    
    # Create Windows output directory if it doesn't exist
    local windows_output="$OUTPUT_DIR/windows"
    if [ ! -d "$windows_output" ]; then
        mkdir -p "$windows_output"
        log_message "Created Windows output directory: $windows_output" "INFO"
    fi
    
    # Execute Windows build script
    log_message "Executing Windows build script..." "INFO"
    
    # Call the Windows build script with appropriate parameters
    "$SCRIPT_DIR/build-windows-installer.bat" > "$LOG_DIR/windows-build.log" 2>&1
    ret_val=$?
    
    if [ $ret_val -eq 0 ]; then
        log_message "Windows installer build completed successfully" "INFO"
    else
        log_message "Windows installer build failed with code $ret_val" "ERROR"
        log_message "See log file for details: $LOG_DIR/windows-build.log" "ERROR"
    fi
    
    return $ret_val
}

# Function to build the macOS installer
build_macos_installer() {
    local ret_val=0
    
    log_message "Starting macOS installer build..." "INFO"
    
    # Skip if macOS build is disabled
    if [ "$MACOS_ENABLED" = "false" ]; then
        log_message "macOS build is disabled. Skipping." "INFO"
        return 0
    fi
    
    # Check if macOS build script exists
    if [ ! -f "$SCRIPT_DIR/build-macos-installer.sh" ]; then
        log_message "macOS installer build script not found: $SCRIPT_DIR/build-macos-installer.sh" "ERROR"
        return 1
    fi
    
    # Check if we're on macOS or cross-platform build is allowed
    if [ "$(uname)" != "Darwin" ]; then
        log_message "Not running on macOS. Cross-platform macOS build may have limitations." "WARN"
    fi
    
    # Create macOS output directory if it doesn't exist
    local macos_output="$OUTPUT_DIR/macos"
    if [ ! -d "$macos_output" ]; then
        mkdir -p "$macos_output"
        log_message "Created macOS output directory: $macos_output" "INFO"
    fi
    
    # Execute macOS build script
    log_message "Executing macOS build script..." "INFO"
    
    # Make sure the script is executable
    chmod +x "$SCRIPT_DIR/build-macos-installer.sh"
    
    # Call the macOS build script with appropriate parameters
    "$SCRIPT_DIR/build-macos-installer.sh" > "$LOG_DIR/macos-build.log" 2>&1
    ret_val=$?
    
    if [ $ret_val -eq 0 ]; then
        log_message "macOS installer build completed successfully" "INFO"
    else
        log_message "macOS installer build failed with code $ret_val" "ERROR"
        log_message "See log file for details: $LOG_DIR/macos-build.log" "ERROR"
    fi
    
    return $ret_val
}

# Function to build the Linux installer
build_linux_installer() {
    local ret_val=0
    
    log_message "Starting Linux installer build..." "INFO"
    
    # Skip if Linux build is disabled
    if [ "$LINUX_ENABLED" = "false" ]; then
        log_message "Linux build is disabled. Skipping." "INFO"
        return 0
    fi
    
    # Check if Linux build script exists
    if [ ! -f "$SCRIPT_DIR/build-linux-installer.sh" ]; then
        log_message "Linux installer build script not found: $SCRIPT_DIR/build-linux-installer.sh" "ERROR"
        return 1
    fi
    
    # Check if we're on Linux or cross-platform build is allowed
    if [ "$(uname)" != "Linux" ]; then
        log_message "Not running on Linux. Cross-platform Linux build may have limitations." "WARN"
    fi
    
    # Create Linux output directory if it doesn't exist
    local linux_output="$OUTPUT_DIR/linux"
    if [ ! -d "$linux_output" ]; then
        mkdir -p "$linux_output"
        log_message "Created Linux output directory: $linux_output" "INFO"
    fi
    
    # Execute Linux build script
    log_message "Executing Linux build script..." "INFO"
    
    # Make sure the script is executable
    chmod +x "$SCRIPT_DIR/build-linux-installer.sh"
    
    # Call the Linux build script with appropriate parameters
    "$SCRIPT_DIR/build-linux-installer.sh" > "$LOG_DIR/linux-build.log" 2>&1
    ret_val=$?
    
    if [ $ret_val -eq 0 ]; then
        log_message "Linux installer build completed successfully" "INFO"
    else
        log_message "Linux installer build failed with code $ret_val" "ERROR"
        log_message "See log file for details: $LOG_DIR/linux-build.log" "ERROR"
    fi
    
    return $ret_val
}

# Function to verify the installers
verify_installers() {
    local ret_val=0
    
    # Skip if verification is disabled
    if [ "$VERIFY_INSTALLERS" = "false" ]; then
        log_message "Installer verification is disabled. Skipping." "INFO"
        return 0
    fi
    
    # Check if verification script exists
    if [ ! -f "$SCRIPT_DIR/verify-installers.sh" ]; then
        log_message "Installer verification script not found: $SCRIPT_DIR/verify-installers.sh" "ERROR"
        return 1
    fi
    
    log_message "Starting installer verification..." "INFO"
    
    # Make sure the verification script is executable
    chmod +x "$SCRIPT_DIR/verify-installers.sh"
    
    # Build verification command with appropriate flags
    local verify_cmd="$SCRIPT_DIR/verify-installers.sh"
    
    # Add platform flags based on what was built
    if [ "$WINDOWS_ENABLED" = "false" ]; then
        verify_cmd="$verify_cmd --windows false"
    fi
    
    if [ "$MACOS_ENABLED" = "false" ]; then
        verify_cmd="$verify_cmd --macos false"
    fi
    
    if [ "$LINUX_ENABLED" = "false" ]; then
        verify_cmd="$verify_cmd --linux false"
    fi
    
    # Execute verification script
    log_message "Executing verification command: $verify_cmd" "INFO"
    $verify_cmd > "$LOG_DIR/verification.log" 2>&1
    ret_val=$?
    
    if [ $ret_val -eq 0 ]; then
        log_message "Installer verification completed successfully" "INFO"
    else
        log_message "Installer verification failed with code $ret_val" "ERROR"
        log_message "See log file for details: $LOG_DIR/verification.log" "ERROR"
    fi
    
    return $ret_val
}

# Function to log a message to both console and log file
log_message() {
    local message="$1"
    local level="${2:-INFO}"
    local timestamp=$(date +"%Y-%m-%d %H:%M:%S")
    
    # Format the message
    local formatted_message="[$timestamp] [$level] $message"
    
    # Print to console with color based on level
    if [ "$level" = "ERROR" ]; then
        echo -e "\033[31m$formatted_message\033[0m"  # Red
    elif [ "$level" = "WARN" ]; then
        echo -e "\033[33m$formatted_message\033[0m"  # Yellow
    elif [ "$level" = "INFO" ]; then
        echo -e "\033[36m$formatted_message\033[0m"  # Cyan
    else
        echo "$formatted_message"
    fi
    
    # Write to log file
    if [ -d "$LOG_DIR" ]; then
        echo "$formatted_message" >> "$BUILD_LOG"
    fi
}

# Function to print a summary of build results
print_summary() {
    local windows_result=$1
    local macos_result=$2
    local linux_result=$3
    local verification_result=$4
    
    echo ""
    echo "====================================================================="
    echo "                      BUILD SUMMARY                                  "
    echo "====================================================================="
    echo "Application: $APP_NAME"
    echo "Version: $APP_VERSION"
    echo "Build Date: $(date)"
    echo "---------------------------------------------------------------------"
    
    # Windows result
    echo -n "Windows Installer: "
    if [ "$WINDOWS_ENABLED" = "false" ]; then
        echo "SKIPPED"
    elif [ $windows_result -eq 0 ]; then
        echo -e "\033[32mSUCCESS\033[0m"  # Green
    else
        echo -e "\033[31mFAILED\033[0m"   # Red
    fi
    
    # macOS result
    echo -n "macOS Installer:   "
    if [ "$MACOS_ENABLED" = "false" ]; then
        echo "SKIPPED"
    elif [ $macos_result -eq 0 ]; then
        echo -e "\033[32mSUCCESS\033[0m"  # Green
    else
        echo -e "\033[31mFAILED\033[0m"   # Red
    fi
    
    # Linux result
    echo -n "Linux Installer:   "
    if [ "$LINUX_ENABLED" = "false" ]; then
        echo "SKIPPED"
    elif [ $linux_result -eq 0 ]; then
        echo -e "\033[32mSUCCESS\033[0m"  # Green
    else
        echo -e "\033[31mFAILED\033[0m"   # Red
    fi
    
    # Verification result
    echo -n "Verification:      "
    if [ "$VERIFY_INSTALLERS" = "false" ]; then
        echo "SKIPPED"
    elif [ $verification_result -eq 0 ]; then
        echo -e "\033[32mSUCCESS\033[0m"  # Green
    else
        echo -e "\033[31mFAILED\033[0m"   # Red
    fi
    
    echo "---------------------------------------------------------------------"
    
    # Overall status
    local overall_success=true
    
    if [ "$WINDOWS_ENABLED" = "true" ] && [ $windows_result -ne 0 ]; then
        overall_success=false
    fi
    
    if [ "$MACOS_ENABLED" = "true" ] && [ $macos_result -ne 0 ]; then
        overall_success=false
    fi
    
    if [ "$LINUX_ENABLED" = "true" ] && [ $linux_result -ne 0 ]; then
        overall_success=false
    fi
    
    if [ "$VERIFY_INSTALLERS" = "true" ] && [ $verification_result -ne 0 ]; then
        overall_success=false
    fi
    
    echo -n "Overall Status:    "
    if [ "$overall_success" = "true" ]; then
        echo -e "\033[32mSUCCESS\033[0m"  # Green
    else
        echo -e "\033[31mFAILED\033[0m"   # Red
    fi
    
    echo "---------------------------------------------------------------------"
    echo "Installer Location:  $OUTPUT_DIR"
    echo "Build Log:           $BUILD_LOG"
    echo "====================================================================="
}

# Main function that coordinates the build process
main() {
    local args=("$@")
    local windows_result=0
    local macos_result=0
    local linux_result=0
    local verification_result=0
    local overall_result=0
    
    # Print banner
    print_banner
    
    # Parse command-line arguments
    parse_arguments "${args[@]}"
    
    # Set up environment
    if ! setup_environment; then
        log_message "Failed to set up build environment" "ERROR"
        exit 1
    fi
    
    # Load properties
    if ! load_properties; then
        log_message "Failed to load properties" "ERROR"
        # Continue with defaults if properties fail to load
    fi
    
    # Build Windows installer if enabled
    if [ "$WINDOWS_ENABLED" = "true" ]; then
        build_windows_installer
        windows_result=$?
        if [ $windows_result -ne 0 ]; then
            overall_result=1
        fi
    fi
    
    # Build macOS installer if enabled
    if [ "$MACOS_ENABLED" = "true" ]; then
        build_macos_installer
        macos_result=$?
        if [ $macos_result -ne 0 ]; then
            overall_result=1
        fi
    fi
    
    # Build Linux installer if enabled
    if [ "$LINUX_ENABLED" = "true" ]; then
        build_linux_installer
        linux_result=$?
        if [ $linux_result -ne 0 ]; then
            overall_result=1
        fi
    fi
    
    # Verify installers if enabled
    if [ "$VERIFY_INSTALLERS" = "true" ]; then
        verify_installers
        verification_result=$?
        if [ $verification_result -ne 0 ]; then
            overall_result=1
        fi
    fi
    
    # Print summary
    print_summary $windows_result $macos_result $linux_result $verification_result
    
    # Return overall result
    return $overall_result
}

# Call main function with all script arguments
main "$@"
exit $?