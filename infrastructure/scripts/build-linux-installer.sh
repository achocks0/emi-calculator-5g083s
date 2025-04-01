#!/bin/bash
# ========================================================================
# Linux Installer Build Script for Compound Interest Calculator
# ========================================================================
# 
# This script builds native Linux installers (DEB and RPM packages) for the
# Compound Interest Calculator application using JPackage. It handles
# Linux-specific packaging configurations, creates the necessary directory
# structure, and generates installable packages for Linux distributions.
#
# Version: 1.0.0
# Author: Banking Division IT
# 
# ========================================================================

# Set up script constants
SCRIPT_DIR=$(dirname "$0")
COMMON_PROPS="$SCRIPT_DIR/../jpackage/common.properties"
LINUX_PROPS="$SCRIPT_DIR/../jpackage/linux.properties"
LINUX_CONFIG="$SCRIPT_DIR/../installer/linux/config.properties"
OUTPUT_DIR="$SCRIPT_DIR/../installer/linux"
LOG_DIR="$OUTPUT_DIR/logs"
BUILD_LOG="$LOG_DIR/build.log"
APP_NAME="Compound Interest Calculator"
APP_VERSION="1.0.0"
PACKAGE_TYPES="deb,rpm"
VERBOSE=false

# Function to print a banner with script information
print_banner() {
    echo "======================================================================"
    echo "          COMPOUND INTEREST CALCULATOR - LINUX INSTALLER BUILD         "
    echo "======================================================================"
    echo "                  Version: $APP_VERSION                                "
    echo "                  Date: $(date)                                        "
    echo "                  Building Linux DEB/RPM packages                      "
    echo "======================================================================"
    echo ""
}

# Function to load properties from configuration files
load_properties() {
    local result=0
    
    # Check and load common properties
    if [ ! -f "$COMMON_PROPS" ]; then
        log_message "ERROR: Common properties file not found: $COMMON_PROPS" "ERROR"
        result=1
    else
        log_message "Loading common properties from: $COMMON_PROPS" "INFO"
        # Source the common properties file
        source "$COMMON_PROPS"
    fi
    
    # Check and load Linux properties
    if [ ! -f "$LINUX_PROPS" ]; then
        log_message "ERROR: Linux properties file not found: $LINUX_PROPS" "ERROR"
        result=1
    else
        log_message "Loading Linux properties from: $LINUX_PROPS" "INFO"
        # Source the Linux properties file
        source "$LINUX_PROPS"
    fi
    
    # Check and load Linux config
    if [ ! -f "$LINUX_CONFIG" ]; then
        log_message "ERROR: Linux config file not found: $LINUX_CONFIG" "ERROR"
        result=1
    else
        log_message "Loading Linux config from: $LINUX_CONFIG" "INFO"
        # Source the Linux config file
        source "$LINUX_CONFIG"
    fi
    
    # Verify that required properties are set
    if [ -z "$app_name" ] || [ -z "$app_version" ] || [ -z "$app_main_class" ] || [ -z "$app_main_jar" ]; then
        log_message "ERROR: Required properties are missing" "ERROR"
        result=1
    fi
    
    return $result
}

# Function to parse command-line arguments
parse_arguments() {
    local args=("$@")
    
    # Process command-line arguments
    while getopts ":t:vh" opt; do
        case $opt in
            t)
                PACKAGE_TYPES="$OPTARG"
                ;;
            v)
                VERBOSE=true
                ;;
            h)
                echo "Usage: $(basename "$0") [options]"
                echo "Options:"
                echo "  -t <type>    Package types to build (deb, rpm, or deb,rpm)"
                echo "  -v           Enable verbose output"
                echo "  -h           Display this help message"
                exit 0
                ;;
            \?)
                echo "Invalid option: -$OPTARG" >&2
                echo "Use -h for help"
                exit 1
                ;;
            :)
                echo "Option -$OPTARG requires an argument." >&2
                echo "Use -h for help"
                exit 1
                ;;
        esac
    done
    
    # Validate package types
    if [[ ! "$PACKAGE_TYPES" =~ ^(deb|rpm|deb,rpm|rpm,deb)$ ]]; then
        log_message "WARNING: Invalid package type specified: $PACKAGE_TYPES. Using default: deb,rpm" "WARN"
        PACKAGE_TYPES="deb,rpm"
    fi
    
    # Log the build configuration
    log_message "Build configuration:" "INFO"
    log_message "  Package types: $PACKAGE_TYPES" "INFO"
    log_message "  Verbose mode: $VERBOSE" "INFO"
}

# Function to set up the build environment
setup_environment() {
    local result=0
    
    # Create output directory if it doesn't exist
    if [ ! -d "$OUTPUT_DIR" ]; then
        log_message "Creating output directory: $OUTPUT_DIR" "INFO"
        mkdir -p "$OUTPUT_DIR"
    fi
    
    # Create logs directory if it doesn't exist
    if [ ! -d "$LOG_DIR" ]; then
        log_message "Creating logs directory: $LOG_DIR" "INFO"
        mkdir -p "$LOG_DIR"
    fi
    
    # Initialize build log
    echo "======== BUILD LOG: $(date) ========" > "$BUILD_LOG"
    
    # Check for required tools
    if ! command -v java &> /dev/null; then
        log_message "ERROR: Java not found" "ERROR"
        result=1
    else
        log_message "Java version: $(java -version 2>&1 | head -n 1)" "INFO"
    fi
    
    if ! command -v jpackage &> /dev/null; then
        log_message "ERROR: jpackage not found. JDK 14+ is required." "ERROR"
        result=1
    else
        log_message "jpackage version: $(jpackage --version 2>&1)" "INFO"
    fi
    
    # Check for Linux-specific packaging tools
    if [[ "$PACKAGE_TYPES" == *"deb"* ]] && ! command -v dpkg &> /dev/null; then
        log_message "WARNING: dpkg not found. DEB package creation may fail." "WARN"
    fi
    
    if [[ "$PACKAGE_TYPES" == *"rpm"* ]] && ! command -v rpm &> /dev/null; then
        log_message "WARNING: rpm not found. RPM package creation may fail." "WARN"
    fi
    
    # Log system information
    log_message "Operating System: $(uname -a)" "INFO"
    if [ -f /etc/os-release ]; then
        log_message "Linux Distribution: $(cat /etc/os-release | grep PRETTY_NAME | cut -d= -f2 | tr -d '"')" "INFO"
    fi
    
    return $result
}

# Function to prepare resources for the installer
prepare_resources() {
    local result=0
    local temp_dir="$OUTPUT_DIR/temp"
    
    log_message "Preparing resources for Linux installer" "INFO"
    
    # Create temp directory if it doesn't exist
    if [ ! -d "$temp_dir" ]; then
        mkdir -p "$temp_dir"
    else
        # Clean temp directory if it exists
        rm -rf "$temp_dir"/*
    fi
    
    # Check if the jar file exists
    local jar_path="${app_input_dir}/${app_main_jar}"
    if [ ! -f "$jar_path" ]; then
        log_message "ERROR: Application JAR file not found: $jar_path" "ERROR"
        return 1
    fi
    
    # Copy JAR file to temp directory
    log_message "Copying application JAR: $jar_path" "INFO"
    cp "$jar_path" "$temp_dir/"
    
    # Check and copy Linux icon if exists
    if [ -n "$linux_icon_path" ] && [ -f "$linux_icon_path" ]; then
        log_message "Copying Linux icon: $linux_icon_path" "INFO"
        cp "$linux_icon_path" "$temp_dir/"
    else
        log_message "WARNING: Linux icon not found: $linux_icon_path" "WARN"
    fi
    
    # Check and copy license file if exists
    if [ -n "$linux_license_path" ] && [ -f "$linux_license_path" ]; then
        log_message "Copying license file: $linux_license_path" "INFO"
        cp "$linux_license_path" "$temp_dir/"
    else
        log_message "WARNING: License file not found: $linux_license_path" "WARN"
    fi
    
    # Check and copy post-install script if specified
    if [ -n "$post_install_script" ]; then
        local script_path="$OUTPUT_DIR/$post_install_script"
        if [ -f "$script_path" ]; then
            log_message "Copying post-install script: $script_path" "INFO"
            cp "$script_path" "$temp_dir/"
            chmod +x "$temp_dir/$post_install_script"
        else
            log_message "WARNING: Post-install script not found: $script_path" "WARN"
        fi
    fi
    
    # Check and copy pre-uninstall script if specified
    if [ -n "$pre_uninstall_script" ]; then
        local script_path="$OUTPUT_DIR/$pre_uninstall_script"
        if [ -f "$script_path" ]; then
            log_message "Copying pre-uninstall script: $script_path" "INFO"
            cp "$script_path" "$temp_dir/"
            chmod +x "$temp_dir/$pre_uninstall_script"
        else
            log_message "WARNING: Pre-uninstall script not found: $script_path" "WARN"
        fi
    fi
    
    log_message "Resources prepared successfully" "INFO"
    return $result
}

# Function to build DEB package
build_deb_package() {
    local result=0
    local temp_dir="$OUTPUT_DIR/temp"
    local output_path="$OUTPUT_DIR/deb"
    
    # Create output directory if it doesn't exist
    if [ ! -d "$output_path" ]; then
        mkdir -p "$output_path"
    fi
    
    log_message "Building DEB package for $app_name $app_version" "INFO"
    
    # Construct jpackage command for DEB
    local cmd=(
        "jpackage"
        "--type" "deb"
        "--name" "${linux_package_name:-$app_name}"
        "--app-version" "$app_version"
        "--vendor" "$app_vendor"
        "--description" "$app_description"
        "--copyright" "$app_copyright"
        "--input" "$temp_dir"
        "--dest" "$output_path"
        "--main-jar" "$app_main_jar"
        "--main-class" "$app_main_class"
        "--linux-package-name" "${linux_package_name:-compound-interest-calculator}"
        "--linux-menu-group" "$linux_menu_group"
        "--linux-shortcut" "true"
        "--linux-app-category" "$linux_package_category"
        "--install-dir" "${linux_dir_installLocation:-/opt/Compound Interest Calculator}"
    )
    
    # Add icon if available
    if [ -f "$temp_dir/$(basename "$linux_icon_path")" ]; then
        cmd+=("--icon" "$temp_dir/$(basename "$linux_icon_path")")
    fi
    
    # Add license if available
    if [ -f "$temp_dir/$(basename "$linux_license_path")" ]; then
        cmd+=("--license-file" "$temp_dir/$(basename "$linux_license_path")")
    fi
    
    # Add maintainer and email if available
    if [ -n "$linux_package_maintainer" ]; then
        cmd+=("--linux-deb-maintainer" "$linux_package_maintainer")
    fi
    
    if [ -n "$linux_package_email" ]; then
        cmd+=("--linux-deb-maintainer-email" "$linux_package_email")
    fi
    
    # Add post-install script if available
    if [ -f "$temp_dir/$post_install_script" ]; then
        cmd+=("--linux-deb-maintainer-scripts" "$temp_dir")
    fi
    
    # Add verbose flag if enabled
    if [ "$VERBOSE" = true ]; then
        cmd+=("--verbose")
    fi
    
    # Execute jpackage command
    log_message "Executing: ${cmd[*]}" "INFO"
    if "${cmd[@]}" >> "$BUILD_LOG" 2>&1; then
        log_message "DEB package created successfully" "INFO"
    else
        log_message "ERROR: Failed to create DEB package" "ERROR"
        result=1
    fi
    
    # Check if package was created
    if [ $result -eq 0 ]; then
        local package_file="${output_path}/${linux_package_name:-compound-interest-calculator}_${app_version}-1_amd64.deb"
        if [ -f "$package_file" ]; then
            log_message "DEB package available at: $package_file" "INFO"
            # Generate checksum
            (cd "$output_path" && sha256sum "$(basename "$package_file")" > "$(basename "$package_file").sha256")
        else
            log_message "WARNING: DEB package file not found at expected location" "WARN"
            # Try to locate the package file
            local found_pkg=$(find "$output_path" -name "*.deb" -type f | head -n 1)
            if [ -n "$found_pkg" ]; then
                log_message "Found DEB package at: $found_pkg" "INFO"
                # Generate checksum
                (cd "$(dirname "$found_pkg")" && sha256sum "$(basename "$found_pkg")" > "$(basename "$found_pkg").sha256")
            else
                log_message "ERROR: No DEB package found in output directory" "ERROR"
                result=1
            fi
        fi
    fi
    
    return $result
}

# Function to build RPM package
build_rpm_package() {
    local result=0
    local temp_dir="$OUTPUT_DIR/temp"
    local output_path="$OUTPUT_DIR/rpm"
    
    # Create output directory if it doesn't exist
    if [ ! -d "$output_path" ]; then
        mkdir -p "$output_path"
    fi
    
    log_message "Building RPM package for $app_name $app_version" "INFO"
    
    # Construct jpackage command for RPM
    local cmd=(
        "jpackage"
        "--type" "rpm"
        "--name" "${linux_package_name:-$app_name}"
        "--app-version" "$app_version"
        "--vendor" "$app_vendor"
        "--description" "$app_description"
        "--copyright" "$app_copyright"
        "--input" "$temp_dir"
        "--dest" "$output_path"
        "--main-jar" "$app_main_jar"
        "--main-class" "$app_main_class"
        "--linux-package-name" "${linux_package_name:-compound-interest-calculator}"
        "--linux-menu-group" "$linux_menu_group"
        "--linux-shortcut" "true"
        "--linux-app-category" "$linux_package_category"
        "--install-dir" "${linux_dir_installLocation:-/opt/Compound Interest Calculator}"
    )
    
    # Add icon if available
    if [ -f "$temp_dir/$(basename "$linux_icon_path")" ]; then
        cmd+=("--icon" "$temp_dir/$(basename "$linux_icon_path")")
    fi
    
    # Add license if available
    if [ -f "$temp_dir/$(basename "$linux_license_path")" ]; then
        cmd+=("--license-file" "$temp_dir/$(basename "$linux_license_path")")
    fi
    
    # Add maintainer if available
    if [ -n "$linux_package_maintainer" ]; then
        cmd+=("--linux-rpm-license" "${linux_package_license:-Proprietary}")
    fi
    
    # Add post-install script if available
    if [ -f "$temp_dir/$post_install_script" ]; then
        cmd+=("--linux-rpm-maintainer-scripts" "$temp_dir")
    fi
    
    # Add verbose flag if enabled
    if [ "$VERBOSE" = true ]; then
        cmd+=("--verbose")
    fi
    
    # Execute jpackage command
    log_message "Executing: ${cmd[*]}" "INFO"
    if "${cmd[@]}" >> "$BUILD_LOG" 2>&1; then
        log_message "RPM package created successfully" "INFO"
    else
        log_message "ERROR: Failed to create RPM package" "ERROR"
        result=1
    fi
    
    # Check if package was created
    if [ $result -eq 0 ]; then
        local package_file="${output_path}/${linux_package_name:-compound-interest-calculator}-${app_version}-1.x86_64.rpm"
        if [ -f "$package_file" ]; then
            log_message "RPM package available at: $package_file" "INFO"
            # Generate checksum
            (cd "$output_path" && sha256sum "$(basename "$package_file")" > "$(basename "$package_file").sha256")
        else
            log_message "WARNING: RPM package file not found at expected location" "WARN"
            # Try to locate the package file
            local found_pkg=$(find "$output_path" -name "*.rpm" -type f | head -n 1)
            if [ -n "$found_pkg" ]; then
                log_message "Found RPM package at: $found_pkg" "INFO"
                # Generate checksum
                (cd "$(dirname "$found_pkg")" && sha256sum "$(basename "$found_pkg")" > "$(basename "$found_pkg").sha256")
            else
                log_message "ERROR: No RPM package found in output directory" "ERROR"
                result=1
            fi
        fi
    fi
    
    return $result
}

# Function to clean up temporary files
cleanup() {
    log_message "Cleaning up after build" "INFO"
    
    # Remove temp directory if it exists
    if [ -d "$OUTPUT_DIR/temp" ]; then
        rm -rf "$OUTPUT_DIR/temp"
    fi
    
    log_message "Cleanup completed" "INFO"
    
    # Copy build log to output root for easy access
    cp "$BUILD_LOG" "$OUTPUT_DIR/build-$(date +%Y%m%d-%H%M%S).log"
    
    log_message "Build log saved to: $OUTPUT_DIR/build-$(date +%Y%m%d-%H%M%S).log" "INFO"
}

# Function to log messages to console and log file
log_message() {
    local message="$1"
    local level="${2:-INFO}"
    local timestamp=$(date +"%Y-%m-%d %H:%M:%S")
    
    # Format the message
    local formatted_message="[$timestamp] [$level] $message"
    
    # Print to console
    echo "$formatted_message"
    
    # Append to log file if it exists
    if [ -f "$BUILD_LOG" ]; then
        echo "$formatted_message" >> "$BUILD_LOG"
    fi
}

# Main function
main() {
    local exit_code=0
    local deb_result=0
    local rpm_result=0
    local deb_enabled=false
    local rpm_enabled=false
    
    # Print banner
    print_banner
    
    # Parse command line arguments
    parse_arguments "$@"
    
    # Set up environment
    if ! setup_environment; then
        log_message "ERROR: Failed to set up build environment" "ERROR"
        exit 1
    fi
    
    # Load properties
    if ! load_properties; then
        log_message "ERROR: Failed to load properties" "ERROR"
        exit 1
    fi
    
    # Prepare resources
    if ! prepare_resources; then
        log_message "ERROR: Failed to prepare resources" "ERROR"
        exit 1
    fi
    
    # Determine which package types to build
    if [[ "$PACKAGE_TYPES" == *"deb"* ]]; then
        deb_enabled=true
    fi
    
    if [[ "$PACKAGE_TYPES" == *"rpm"* ]]; then
        rpm_enabled=true
    fi
    
    # Build DEB package if enabled
    if [ "$deb_enabled" = true ]; then
        if ! build_deb_package; then
            deb_result=1
            exit_code=1
        fi
    fi
    
    # Build RPM package if enabled
    if [ "$rpm_enabled" = true ]; then
        if ! build_rpm_package; then
            rpm_result=1
            exit_code=1
        fi
    fi
    
    # Clean up
    cleanup
    
    # Print summary
    echo ""
    echo "======================================================================"
    echo "                  BUILD SUMMARY                                       "
    echo "======================================================================"
    if [ "$deb_enabled" = true ]; then
        if [ $deb_result -eq 0 ]; then
            echo "  DEB Package: SUCCESS"
        else
            echo "  DEB Package: FAILED"
        fi
    else
        echo "  DEB Package: SKIPPED"
    fi
    
    if [ "$rpm_enabled" = true ]; then
        if [ $rpm_result -eq 0 ]; then
            echo "  RPM Package: SUCCESS"
        else
            echo "  RPM Package: FAILED"
        fi
    else
        echo "  RPM Package: SKIPPED"
    fi
    echo "======================================================================"
    
    if [ $exit_code -eq 0 ]; then
        log_message "Build completed successfully" "INFO"
    else
        log_message "Build completed with errors" "ERROR"
    fi
    
    return $exit_code
}

# Execute main function with all script arguments
main "$@"