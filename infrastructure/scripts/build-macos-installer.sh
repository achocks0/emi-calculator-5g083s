#!/bin/bash
#
# build-macos-installer.sh
#
# Purpose: Build a native macOS installer (DMG) for the Compound Interest Calculator
#          using JPackage, with code signing and notarization.
#
# Author: Banking Division
# Date: 2023
#
# Requirements:
# - JDK 14+ (for jpackage)
# - Xcode Command Line Tools (for codesign and xcrun)
# - Apple Developer ID (for code signing and notarization)
#
# Usage: ./build-macos-installer.sh
#

# Exit on error, undefined variable reference, and error in piped commands
set -euo pipefail

# Script directory
SCRIPT_DIR=$(dirname "$0")

# Property files
COMMON_PROPS="$SCRIPT_DIR/../jpackage/common.properties"
MACOS_PROPS="$SCRIPT_DIR/../jpackage/macos.properties"

# Default values (will be overridden by properties)
APP_NAME="Compound Interest Calculator"
APP_VERSION="1.0.0"

# Output directories
OUTPUT_DIR="$SCRIPT_DIR/../installer/macos"
LOG_DIR="$OUTPUT_DIR/logs"
BUILD_LOG="$LOG_DIR/macos-build.log"
TEMP_DIR="$SCRIPT_DIR/../temp/macos"

# Output DMG file
DMG_FILE="$OUTPUT_DIR/${APP_NAME}-${APP_VERSION}.dmg"

#
# Function to print a banner
#
print_banner() {
    echo "============================================================="
    echo "          Compound Interest Calculator - macOS Build         "
    echo "============================================================="
    echo "Build Date: $(date)"
    echo "Purpose: Building macOS DMG installer"
    echo "============================================================="
    echo ""
}

#
# Function to load properties from property files
#
load_properties() {
    log_message "Loading properties..." "INFO"
    
    # Check if common properties file exists
    if [ ! -f "$COMMON_PROPS" ]; then
        log_message "Common properties file not found: $COMMON_PROPS" "ERROR"
        return 1
    fi
    
    # Source common properties
    while IFS='=' read -r key value || [ -n "$key" ]; do
        # Skip comments and empty lines
        if [[ $key =~ ^[[:space:]]*# ]] || [[ -z "$key" ]]; then
            continue
        fi
        
        # Trim leading/trailing whitespace from key and value
        key=$(echo "$key" | xargs)
        value=$(echo "$value" | xargs)
        
        # Convert property name format (e.g., app.name to app_name)
        varname=$(echo "$key" | tr '.' '_')
        
        # Export the variable
        export "$varname"="$value"
    done < "$COMMON_PROPS"
    
    # Check if macOS properties file exists
    if [ ! -f "$MACOS_PROPS" ]; then
        log_message "macOS properties file not found: $MACOS_PROPS" "ERROR"
        return 1
    fi
    
    # Source macOS properties
    while IFS='=' read -r key value || [ -n "$key" ]; do
        # Skip comments and empty lines
        if [[ $key =~ ^[[:space:]]*# ]] || [[ -z "$key" ]]; then
            continue
        fi
        
        # Trim leading/trailing whitespace from key and value
        key=$(echo "$key" | xargs)
        value=$(echo "$value" | xargs)
        
        # Convert property name format (e.g., mac.bundle.identifier to mac_bundle_identifier)
        varname=$(echo "$key" | tr '.' '_')
        
        # Export the variable
        export "$varname"="$value"
    done < "$MACOS_PROPS"
    
    # Update global variables with loaded properties
    APP_NAME="$app_name"
    APP_VERSION="$app_version"
    DMG_FILE="$OUTPUT_DIR/${APP_NAME}-${APP_VERSION}.dmg"
    
    # Verify required properties
    if [ -z "${app_name:-}" ] || [ -z "${app_version:-}" ] || [ -z "${app_main_class:-}" ] || [ -z "${app_main_jar:-}" ]; then
        log_message "Missing required properties in $COMMON_PROPS" "ERROR"
        return 1
    fi
    
    if [ -z "${mac_bundle_identifier:-}" ] || [ -z "${mac_installer_type:-}" ]; then
        log_message "Missing required properties in $MACOS_PROPS" "ERROR"
        return 1
    fi
    
    log_message "Properties loaded successfully." "INFO"
    log_message "Application name: $APP_NAME" "INFO"
    log_message "Application version: $APP_VERSION" "INFO"
    log_message "Main class: $app_main_class" "INFO"
    log_message "Bundle identifier: $mac_bundle_identifier" "INFO"
    
    return 0
}

#
# Function to set up the build environment
#
setup_environment() {
    log_message "Setting up build environment..." "INFO"
    
    # Create output directory if it doesn't exist
    mkdir -p "$OUTPUT_DIR"
    
    # Create logs directory if it doesn't exist
    mkdir -p "$LOG_DIR"
    
    # Create temp directory if it doesn't exist
    mkdir -p "$TEMP_DIR"
    
    # Initialize build log
    echo "=== macOS Build Log - $(date) ===" > "$BUILD_LOG"
    
    # Log environment information
    log_message "macOS Version: $(sw_vers -productVersion)" "INFO"
    log_message "Java Version: $(java -version 2>&1 | head -n 1)" "INFO"
    
    log_message "Build environment set up successfully." "INFO"
    return 0
}

#
# Function to check if all prerequisites are met
#
check_prerequisites() {
    log_message "Checking prerequisites..." "INFO"
    
    # Check if running on macOS
    if [ "$(uname)" != "Darwin" ]; then
        log_message "This script must be run on macOS" "ERROR"
        return 1
    fi
    
    # Check if Java is installed
    if ! command -v java &> /dev/null; then
        log_message "Java is not installed" "ERROR"
        return 1
    fi
    
    # Check Java version (need 14+ for jpackage)
    JAVA_VERSION=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}' | awk -F '.' '{print $1}')
    if [ "$JAVA_VERSION" -lt 14 ]; then
        log_message "Java 14+ is required for jpackage (found version $JAVA_VERSION)" "ERROR"
        return 1
    fi
    
    # Check if jpackage is available
    if ! command -v jpackage &> /dev/null; then
        log_message "jpackage tool not found. Ensure JDK 14+ is installed and jpackage is in your PATH" "ERROR"
        return 1
    fi
    
    # Check if Xcode command line tools are installed
    if ! command -v codesign &> /dev/null || ! command -v xcrun &> /dev/null; then
        log_message "Xcode command line tools are not installed" "ERROR"
        log_message "Install with: xcode-select --install" "INFO"
        return 1
    fi
    
    # Check if input JAR exists
    if [ ! -f "${app_input_dir}/${app_main_jar}" ]; then
        log_message "Input JAR file not found: ${app_input_dir}/${app_main_jar}" "ERROR"
        return 1
    fi
    
    # Check if icon file exists
    if [ ! -f "${mac_icon_path}" ]; then
        log_message "Icon file not found: ${mac_icon_path}" "ERROR"
        return 1
    fi
    
    # Check if signing is enabled and signing identity is available
    if [ "${mac_signing_enabled:-false}" = "true" ]; then
        if ! security find-identity -v -p codesigning | grep -q "${mac_signing_key}"; then
            log_message "Signing identity not found: ${mac_signing_key}" "WARNING"
            log_message "Code signing will be skipped." "WARNING"
            export mac_signing_enabled="false"
        else
            log_message "Signing identity found: ${mac_signing_key}" "INFO"
        fi
    fi
    
    # Check if notarization is enabled and credentials are available
    if [ "${mac_notarization_enabled:-false}" = "true" ]; then
        if [ -z "${mac_notarization_apple_id:-}" ] || [ -z "${mac_notarization_team_id:-}" ]; then
            log_message "Notarization credentials missing. Notarization will be skipped." "WARNING"
            export mac_notarization_enabled="false"
        else
            log_message "Notarization credentials found" "INFO"
        fi
    fi
    
    log_message "All prerequisites met." "INFO"
    return 0
}

#
# Function to prepare resources for the installer
#
prepare_resources() {
    log_message "Preparing resources..." "INFO"
    
    # Create resources directory in temp
    mkdir -p "$TEMP_DIR/resources"
    
    # Copy icon to temp directory
    log_message "Copying icon: ${mac_icon_path}" "INFO"
    cp "${mac_icon_path}" "$TEMP_DIR/resources/"
    
    # Copy and process Info.plist template if it exists
    if [ -f "${mac_plist_template:-}" ]; then
        log_message "Processing Info.plist template: ${mac_plist_template}" "INFO"
        
        # Create a processed plist file with variables replaced
        sed -e "s/\${app.name}/$app_name/g" \
            -e "s/\${app.version}/$app_version/g" \
            -e "s/\${mac.bundle.identifier}/$mac_bundle_identifier/g" \
            -e "s/\${app.main.class}/$app_main_class/g" \
            -e "s/\${mac.category}/$mac_category/g" \
            "${mac_plist_template}" > "$TEMP_DIR/resources/Info.plist"
            
        log_message "Info.plist template processed successfully" "INFO"
    else
        log_message "No Info.plist template specified. Using default plist." "INFO"
    fi
    
    # Copy other macOS-specific resources if needed (license, etc.)
    if [ -f "${mac_license_path:-}" ]; then
        log_message "Copying license: ${mac_license_path}" "INFO"
        cp "${mac_license_path}" "$TEMP_DIR/resources/"
    fi
    
    # If a DMG background image is specified, copy it
    if [ -f "${mac_background_image:-}" ]; then
        log_message "Copying DMG background: ${mac_background_image}" "INFO"
        cp "${mac_background_image}" "$TEMP_DIR/resources/"
    fi
    
    log_message "Resources prepared successfully." "INFO"
    return 0
}

#
# Function to build the application image
#
build_app_image() {
    log_message "Building application image..." "INFO"
    
    # Create app image directory if it doesn't exist
    mkdir -p "$TEMP_DIR/app-image"
    
    # Build the base jpackage command
    JPACKAGE_CMD=(
        "jpackage" "--type" "app-image"
        "--name" "$app_name"
        "--app-version" "$app_version"
        "--vendor" "$app_vendor"
        "--input" "${app_input_dir}"
        "--main-jar" "${app_main_jar}"
        "--main-class" "${app_main_class}"
        "--dest" "$TEMP_DIR/app-image"
    )
    
    # Add description if provided
    if [ -n "${app_description:-}" ]; then
        JPACKAGE_CMD+=("--description" "$app_description")
    fi
    
    # Add copyright if provided
    if [ -n "${app_copyright:-}" ]; then
        JPACKAGE_CMD+=("--copyright" "$app_copyright")
    fi
    
    # Add macOS-specific options
    JPACKAGE_CMD+=(
        "--mac-package-name" "$app_name"
        "--mac-bundle-identifier" "$mac_bundle_identifier"
        "--icon" "${mac_icon_path}"
    )
    
    # Add resource-dir if it exists
    if [ -d "$TEMP_DIR/resources" ]; then
        JPACKAGE_CMD+=("--resource-dir" "$TEMP_DIR/resources")
    fi
    
    # If a custom Info.plist was created, use it
    if [ -f "$TEMP_DIR/resources/Info.plist" ]; then
        JPACKAGE_CMD+=("--mac-info-plist" "$TEMP_DIR/resources/Info.plist")
    fi
    
    # Add Java runtime options if provided
    if [ -n "${app_java_options:-}" ]; then
        JPACKAGE_CMD+=("--java-options" "$app_java_options")
    fi
    
    # Execute the command
    log_message "Executing jpackage to create app image..." "INFO"
    log_message "Command: ${JPACKAGE_CMD[*]}" "DEBUG"
    
    "${JPACKAGE_CMD[@]}" >> "$BUILD_LOG" 2>&1
    
    # Check if app image was created
    if [ ! -d "$TEMP_DIR/app-image/$app_name.app" ]; then
        log_message "Failed to create app image. Check build log for details: $BUILD_LOG" "ERROR"
        return 1
    fi
    
    log_message "Application image built successfully: $TEMP_DIR/app-image/$app_name.app" "INFO"
    return 0
}

#
# Function to sign the application bundle
#
sign_app_bundle() {
    # Check if signing is enabled
    if [ "${mac_signing_enabled:-false}" != "true" ]; then
        log_message "Code signing is disabled. Skipping..." "INFO"
        return 0
    fi
    
    log_message "Signing application bundle..." "INFO"
    
    # Path to the app bundle
    APP_BUNDLE="$TEMP_DIR/app-image/$app_name.app"
    
    # Sign the app bundle
    log_message "Signing with identity: $mac_signing_key" "INFO"
    codesign --force --options runtime --timestamp \
        --sign "$mac_signing_key" \
        --deep "$APP_BUNDLE" >> "$BUILD_LOG" 2>&1
    
    if [ $? -ne 0 ]; then
        log_message "Code signing failed. Check build log for details: $BUILD_LOG" "ERROR"
        return 1
    fi
    
    # Verify the signature
    log_message "Verifying signature..." "INFO"
    codesign --verify --verbose=2 "$APP_BUNDLE" >> "$BUILD_LOG" 2>&1
    
    if [ $? -ne 0 ]; then
        log_message "Code signing verification failed. Check build log for details: $BUILD_LOG" "ERROR"
        return 1
    fi
    
    log_message "Application bundle signed successfully." "INFO"
    return 0
}

#
# Function to create a DMG installer
#
create_dmg() {
    log_message "Creating DMG installer..." "INFO"
    
    # Build the jpackage command for DMG creation
    JPACKAGE_CMD=(
        "jpackage" "--type" "dmg"
        "--name" "$app_name"
        "--app-version" "$app_version"
        "--app-image" "$TEMP_DIR/app-image/$app_name.app"
        "--dest" "$OUTPUT_DIR"
        "--mac-package-identifier" "$mac_bundle_identifier"
    )
    
    # Add DMG volume name if specified
    if [ -n "${mac_dmg_volume_name:-}" ]; then
        JPACKAGE_CMD+=("--mac-dmg-volume-name" "$mac_dmg_volume_name")
    else
        JPACKAGE_CMD+=("--mac-dmg-volume-name" "$app_name")
    fi
    
    # Add background image if specified
    if [ -n "${mac_background_image:-}" ] && [ -f "${mac_background_image}" ]; then
        JPACKAGE_CMD+=("--mac-dmg-background" "${mac_background_image}")
    fi
    
    # Add resource-dir if it exists
    if [ -d "$TEMP_DIR/resources" ]; then
        JPACKAGE_CMD+=("--resource-dir" "$TEMP_DIR/resources")
    fi
    
    # Execute the command
    log_message "Executing jpackage to create DMG..." "INFO"
    log_message "Command: ${JPACKAGE_CMD[*]}" "DEBUG"
    
    "${JPACKAGE_CMD[@]}" >> "$BUILD_LOG" 2>&1
    
    # Check if DMG was created
    if [ ! -f "$DMG_FILE" ]; then
        log_message "Failed to create DMG installer. Check build log for details: $BUILD_LOG" "ERROR"
        return 1
    fi
    
    log_message "DMG installer created successfully: $DMG_FILE" "INFO"
    return 0
}

#
# Function to notarize the DMG with Apple
#
notarize_dmg() {
    # Check if notarization is enabled
    if [ "${mac_notarization_enabled:-false}" != "true" ]; then
        log_message "Notarization is disabled. Skipping..." "INFO"
        return 0
    fi
    
    log_message "Notarizing DMG installer..." "INFO"
    
    # Check if required notarization credentials are available
    if [ -z "${mac_notarization_apple_id:-}" ] || [ -z "${mac_notarization_team_id:-}" ]; then
        log_message "Missing notarization credentials. Notarization will be skipped." "WARNING"
        return 0
    fi
    
    # Create a temporary file for the notarization request UUID
    UUID_FILE="$TEMP_DIR/notarization_uuid.txt"
    
    # Submit the DMG for notarization
    log_message "Uploading DMG to Apple notarization service..." "INFO"
    
    # Store password in keychain if not already there
    # Note: In a real environment, use a more secure approach for handling credentials
    # This is a simplified example that assumes you've set up keychain access
    
    log_message "Using Apple ID: $mac_notarization_apple_id and Team ID: $mac_notarization_team_id" "INFO"
    
    xcrun notarytool submit "$DMG_FILE" \
        --apple-id "$mac_notarization_apple_id" \
        --team-id "$mac_notarization_team_id" \
        --wait \
        --keychain-profile "notary_profile" \
        2>&1 | tee -a "$BUILD_LOG" > "$TEMP_DIR/notarization_result.txt"
    
    # Check if submission was successful
    if ! grep -q "success" "$TEMP_DIR/notarization_result.txt"; then
        log_message "Failed to submit for notarization. Check build log for details: $BUILD_LOG" "ERROR"
        return 1
    fi
    
    log_message "Notarization successful." "INFO"
    
    # Staple the notarization ticket to the DMG
    log_message "Stapling notarization ticket to DMG..." "INFO"
    xcrun stapler staple "$DMG_FILE" >> "$BUILD_LOG" 2>&1
    
    if [ $? -ne 0 ]; then
        log_message "Failed to staple notarization ticket. Check build log for details: $BUILD_LOG" "ERROR"
        return 1
    fi
    
    # Verify stapling
    xcrun stapler validate "$DMG_FILE" >> "$BUILD_LOG" 2>&1
    
    if [ $? -ne 0 ]; then
        log_message "Stapling verification failed. Check build log for details: $BUILD_LOG" "ERROR"
        return 1
    }
    
    log_message "DMG installer notarized and stapled successfully." "INFO"
    return 0
}

#
# Function to clean up temporary files
#
cleanup() {
    log_message "Cleaning up temporary files..." "INFO"
    
    # Make cleanup behavior configurable
    if [ "${SKIP_CLEANUP:-false}" = "true" ]; then
        log_message "Cleanup skipped. Temporary files kept at: $TEMP_DIR" "INFO"
        return 0
    fi
    
    # Remove temporary files
    rm -rf "$TEMP_DIR"
    log_message "Temporary files removed." "INFO"
    
    return 0
}

#
# Function to log messages to both console and log file
#
log_message() {
    local message="$1"
    local level="${2:-INFO}"
    local timestamp=$(date "+%Y-%m-%d %H:%M:%S")
    
    echo "[$timestamp] [$level] $message"
    
    # Ensure log directory exists
    if [ ! -d "$LOG_DIR" ]; then
        mkdir -p "$LOG_DIR"
    fi
    
    # Ensure log file exists
    if [ ! -f "$BUILD_LOG" ]; then
        touch "$BUILD_LOG"
    fi
    
    echo "[$timestamp] [$level] $message" >> "$BUILD_LOG"
}

#
# Main function
#
main() {
    local exit_code=0
    
    # Print banner
    print_banner
    
    # Setup environment
    if ! setup_environment; then
        log_message "Failed to set up build environment" "ERROR"
        return 1
    fi
    
    # Load properties
    if ! load_properties; then
        log_message "Failed to load properties" "ERROR"
        return 1
    fi
    
    # Check prerequisites
    if ! check_prerequisites; then
        log_message "Prerequisites check failed" "ERROR"
        return 1
    fi
    
    # Prepare resources
    if ! prepare_resources; then
        log_message "Failed to prepare resources" "ERROR"
        return 1
    fi
    
    # Build app image
    if ! build_app_image; then
        log_message "Failed to build application image" "ERROR"
        return 1
    fi
    
    # Sign app bundle
    if ! sign_app_bundle; then
        log_message "Failed to sign application bundle" "ERROR"
        return 1
    fi
    
    # Create DMG
    if ! create_dmg; then
        log_message "Failed to create DMG installer" "ERROR"
        return 1
    fi
    
    # Notarize DMG
    if ! notarize_dmg; then
        log_message "Failed to notarize DMG installer" "ERROR"
        return 1
    fi
    
    # Clean up
    cleanup
    
    log_message "Build completed successfully!" "SUCCESS"
    log_message "DMG installer is available at: $DMG_FILE" "INFO"
    
    return 0
}

# Call main function
main "$@"
exit $?