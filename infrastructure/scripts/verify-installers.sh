#!/bin/bash
#
# verify-installers.sh
#
# This script verifies the integrity and quality of installer packages
# for the Compound Interest Calculator application across multiple platforms.
# It performs checksum validation, size verification, and basic structural checks.
#
# Usage: ./verify-installers.sh [options]
#   --windows [true|false]  Enable/disable Windows installer verification (default: true)
#   --macos [true|false]    Enable/disable macOS installer verification (default: true)
#   --linux [true|false]    Enable/disable Linux installer verification (default: true)
#   --verbose               Enable verbose output
#   --help                  Show this help message
#

# Get script directory for relative paths
SCRIPT_DIR=$(dirname "$0")

# Configuration files
COMMON_PROPS="$SCRIPT_DIR/../jpackage/common.properties"
WINDOWS_PROPS="$SCRIPT_DIR/../jpackage/windows.properties"
MACOS_PROPS="$SCRIPT_DIR/../jpackage/macos.properties"
LINUX_PROPS="$SCRIPT_DIR/../jpackage/linux.properties"

# Default values
APP_NAME="Compound Interest Calculator"
APP_VERSION="1.0.0"
OUTPUT_DIR="$SCRIPT_DIR/../installer"
WINDOWS_DIR="$OUTPUT_DIR/windows"
MACOS_DIR="$OUTPUT_DIR/macos"
LINUX_DIR="$OUTPUT_DIR/linux"
LOG_DIR="$OUTPUT_DIR/logs"
VERIFY_LOG="$LOG_DIR/verification.log"
CHECKSUM_FILE="$OUTPUT_DIR/checksums.sha256"

# Verification flags
WINDOWS_ENABLED=true
MACOS_ENABLED=true
LINUX_ENABLED=true
VERBOSE=false

# Minimum installer sizes in bytes
MIN_SIZE_WINDOWS=50000000  # 50MB
MIN_SIZE_MACOS=50000000    # 50MB
MIN_SIZE_LINUX=50000000    # 50MB

# Print a banner with script information
print_banner() {
    echo "================================================================================"
    echo "                     COMPOUND INTEREST CALCULATOR                               "
    echo "                     INSTALLER VERIFICATION TOOL                                "
    echo "================================================================================"
    echo "Application: $APP_NAME"
    echo "Version: $APP_VERSION"
    echo "Date: $(date)"
    echo "-------------------------------------------------------------------------------"
    echo "Verifying installers for Windows, macOS, and Linux platforms"
    echo "================================================================================"
    echo ""
}

# Load properties from configuration files
load_properties() {
    local ret_val=0

    # Check if common properties file exists
    if [[ -f "$COMMON_PROPS" ]]; then
        # Source common properties file
        # Extract values using grep and sed to handle Java properties format
        APP_NAME=$(grep "^app.name=" "$COMMON_PROPS" | cut -d'=' -f2)
        APP_VERSION=$(grep "^app.version=" "$COMMON_PROPS" | cut -d'=' -f2)
        OUTPUT_DIR=$(grep "^app.output.dir=" "$COMMON_PROPS" | cut -d'=' -f2)
        
        # Update derived paths
        WINDOWS_DIR="$OUTPUT_DIR/windows"
        MACOS_DIR="$OUTPUT_DIR/macos"
        LINUX_DIR="$OUTPUT_DIR/linux"
        LOG_DIR="$OUTPUT_DIR/logs"
        VERIFY_LOG="$LOG_DIR/verification.log"
        CHECKSUM_FILE="$OUTPUT_DIR/checksums.sha256"
        
        log_message "Loaded common properties from $COMMON_PROPS" "INFO"
    else
        log_message "Common properties file not found: $COMMON_PROPS" "ERROR"
        ret_val=1
    fi

    # Load Windows properties if enabled
    if [[ "$WINDOWS_ENABLED" == "true" ]] && [[ -f "$WINDOWS_PROPS" ]]; then
        WIN_INSTALLER_TYPE=$(grep "^win.installer.type=" "$WINDOWS_PROPS" | cut -d'=' -f2)
        log_message "Loaded Windows properties from $WINDOWS_PROPS" "INFO"
    elif [[ "$WINDOWS_ENABLED" == "true" ]]; then
        log_message "Windows properties file not found: $WINDOWS_PROPS" "ERROR"
        ret_val=1
    fi

    # Load macOS properties if enabled
    if [[ "$MACOS_ENABLED" == "true" ]] && [[ -f "$MACOS_PROPS" ]]; then
        MAC_INSTALLER_TYPE=$(grep "^mac.installer.type=" "$MACOS_PROPS" | cut -d'=' -f2)
        log_message "Loaded macOS properties from $MACOS_PROPS" "INFO"
    elif [[ "$MACOS_ENABLED" == "true" ]]; then
        log_message "macOS properties file not found: $MACOS_PROPS" "ERROR"
        ret_val=1
    fi

    # Load Linux properties if enabled
    if [[ "$LINUX_ENABLED" == "true" ]] && [[ -f "$LINUX_PROPS" ]]; then
        LINUX_INSTALLER_TYPES=$(grep "^linux.installer.types=" "$LINUX_PROPS" | cut -d'=' -f2)
        LINUX_PACKAGE_NAME=$(grep "^linux.package.name=" "$LINUX_PROPS" | cut -d'=' -f2)
        log_message "Loaded Linux properties from $LINUX_PROPS" "INFO"
    elif [[ "$LINUX_ENABLED" == "true" ]]; then
        log_message "Linux properties file not found: $LINUX_PROPS" "ERROR"
        ret_val=1
    fi

    # Verify that required properties are set
    if [[ -z "$APP_NAME" ]] || [[ -z "$APP_VERSION" ]] || [[ -z "$OUTPUT_DIR" ]]; then
        log_message "One or more required properties are not set" "ERROR"
        ret_val=1
    fi

    return $ret_val
}

# Parse command-line arguments
parse_arguments() {
    local args=("$@")
    
    # Process command-line arguments
    while [[ $# -gt 0 ]]; do
        case "$1" in
            --windows)
                if [[ "$2" == "false" ]]; then
                    WINDOWS_ENABLED=false
                    log_message "Windows installer verification disabled" "INFO"
                fi
                shift 2
                ;;
            --macos)
                if [[ "$2" == "false" ]]; then
                    MACOS_ENABLED=false
                    log_message "macOS installer verification disabled" "INFO"
                fi
                shift 2
                ;;
            --linux)
                if [[ "$2" == "false" ]]; then
                    LINUX_ENABLED=false
                    log_message "Linux installer verification disabled" "INFO"
                fi
                shift 2
                ;;
            --verbose)
                VERBOSE=true
                log_message "Verbose output enabled" "INFO"
                shift
                ;;
            --help)
                echo "Usage: $0 [options]"
                echo "  --windows [true|false]  Enable/disable Windows installer verification (default: true)"
                echo "  --macos [true|false]    Enable/disable macOS installer verification (default: true)"
                echo "  --linux [true|false]    Enable/disable Linux installer verification (default: true)"
                echo "  --verbose               Enable verbose output"
                echo "  --help                  Show this help message"
                exit 0
                ;;
            *)
                log_message "Unknown option: $1" "ERROR"
                echo "Try '$0 --help' for more information."
                exit 1
                ;;
        esac
    done
    
    # If all platforms are disabled, show a warning
    if [[ "$WINDOWS_ENABLED" == "false" ]] && [[ "$MACOS_ENABLED" == "false" ]] && [[ "$LINUX_ENABLED" == "false" ]]; then
        log_message "Warning: All platforms are disabled for verification." "WARN"
        log_message "No verification will be performed." "WARN"
    fi
}

# Set up the verification environment
setup_environment() {
    local ret_val=0
    
    # Check if output directory exists
    if [[ ! -d "$OUTPUT_DIR" ]]; then
        log_message "Output directory does not exist: $OUTPUT_DIR" "ERROR"
        ret_val=1
    fi
    
    # Create logs directory if it doesn't exist
    if [[ ! -d "$LOG_DIR" ]]; then
        mkdir -p "$LOG_DIR"
        if [[ $? -ne 0 ]]; then
            log_message "Failed to create logs directory: $LOG_DIR" "ERROR"
            ret_val=1
        else
            log_message "Created logs directory: $LOG_DIR" "INFO"
        fi
    fi
    
    # Initialize verification log file
    echo "=== Installer Verification Log - $(date) ===" > "$VERIFY_LOG"
    echo "Application: $APP_NAME $APP_VERSION" >> "$VERIFY_LOG"
    echo "==========================================" >> "$VERIFY_LOG"
    
    # Check for required tools
    if ! command -v sha256sum &> /dev/null; then
        # On macOS, use shasum -a 256 as an alternative
        if ! command -v shasum &> /dev/null; then
            log_message "Required tool not found: sha256sum or shasum" "ERROR"
            ret_val=1
        else
            log_message "Using shasum as alternative to sha256sum" "INFO"
        fi
    fi
    
    # Initialize checksums file
    echo "# SHA-256 checksums for $APP_NAME v$APP_VERSION installers" > "$CHECKSUM_FILE"
    echo "# Generated on $(date)" >> "$CHECKSUM_FILE"
    echo "# --------------------------------------------------" >> "$CHECKSUM_FILE"
    
    # Log environment information
    log_message "Verification environment setup complete" "INFO"
    log_message "Output directory: $OUTPUT_DIR" "INFO"
    log_message "Log file: $VERIFY_LOG" "INFO"
    log_message "Checksums file: $CHECKSUM_FILE" "INFO"
    
    return $ret_val
}

# Verify Windows installer
verify_windows_installer() {
    local ret_val=0
    
    if [[ "$WINDOWS_ENABLED" != "true" ]]; then
        log_message "Skipping Windows installer verification" "INFO"
        return 0
    fi
    
    log_message "Verifying Windows installer..." "INFO"
    
    # Determine installer file based on properties
    local installer_type="${WIN_INSTALLER_TYPE:-msi}"
    local installer_file="$WINDOWS_DIR/${APP_NAME// /-}-${APP_VERSION}.$installer_type"
    
    # Check if installer file exists
    if [[ ! -f "$installer_file" ]]; then
        log_message "Windows installer not found: $installer_file" "ERROR"
        return 1
    fi
    
    # Verify file size
    local file_size=$(stat -c%s "$installer_file" 2>/dev/null || stat -f%z "$installer_file" 2>/dev/null)
    if [[ -z "$file_size" ]]; then
        log_message "Failed to determine file size for: $installer_file" "ERROR"
        ret_val=1
    elif [[ $file_size -lt $MIN_SIZE_WINDOWS ]]; then
        log_message "Windows installer file size is too small: $file_size bytes (min: $MIN_SIZE_WINDOWS bytes)" "ERROR"
        ret_val=1
    else
        log_message "Windows installer file size OK: $file_size bytes" "INFO"
    fi
    
    # Generate and store checksum
    local checksum=$(generate_checksum "$installer_file")
    if [[ -n "$checksum" ]]; then
        echo "$checksum *$(basename "$installer_file")" >> "$CHECKSUM_FILE"
        log_message "Windows installer checksum: $checksum" "INFO"
    else
        log_message "Failed to generate checksum for Windows installer" "ERROR"
        ret_val=1
    fi
    
    # Basic structure verification for MSI
    if [[ "$installer_type" == "msi" ]]; then
        if command -v msiinfo &> /dev/null; then
            if ! msiinfo summary "$installer_file" &> /dev/null; then
                log_message "Windows MSI installer failed structure verification" "ERROR"
                ret_val=1
            else
                log_message "Windows MSI installer structure verification passed" "INFO"
            fi
        else
            log_message "msiinfo tool not available, skipping MSI structure verification" "WARN"
        fi
    fi
    
    if [[ $ret_val -eq 0 ]]; then
        log_message "Windows installer verification completed successfully" "INFO"
    else
        log_message "Windows installer verification failed" "ERROR"
    fi
    
    return $ret_val
}

# Verify macOS installer
verify_macos_installer() {
    local ret_val=0
    
    if [[ "$MACOS_ENABLED" != "true" ]]; then
        log_message "Skipping macOS installer verification" "INFO"
        return 0
    fi
    
    log_message "Verifying macOS installer..." "INFO"
    
    # Determine installer file based on properties
    local installer_type="${MAC_INSTALLER_TYPE:-dmg}"
    local installer_file="$MACOS_DIR/${APP_NAME// /-}-${APP_VERSION}.$installer_type"
    
    # Check if installer file exists
    if [[ ! -f "$installer_file" ]]; then
        log_message "macOS installer not found: $installer_file" "ERROR"
        return 1
    fi
    
    # Verify file size
    local file_size=$(stat -c%s "$installer_file" 2>/dev/null || stat -f%z "$installer_file" 2>/dev/null)
    if [[ -z "$file_size" ]]; then
        log_message "Failed to determine file size for: $installer_file" "ERROR"
        ret_val=1
    elif [[ $file_size -lt $MIN_SIZE_MACOS ]]; then
        log_message "macOS installer file size is too small: $file_size bytes (min: $MIN_SIZE_MACOS bytes)" "ERROR"
        ret_val=1
    else
        log_message "macOS installer file size OK: $file_size bytes" "INFO"
    fi
    
    # Generate and store checksum
    local checksum=$(generate_checksum "$installer_file")
    if [[ -n "$checksum" ]]; then
        echo "$checksum *$(basename "$installer_file")" >> "$CHECKSUM_FILE"
        log_message "macOS installer checksum: $checksum" "INFO"
    else
        log_message "Failed to generate checksum for macOS installer" "ERROR"
        ret_val=1
    fi
    
    # Basic structure verification for DMG
    if [[ "$installer_type" == "dmg" ]]; then
        if command -v hdiutil &> /dev/null; then
            if ! hdiutil verify "$installer_file" &> /dev/null; then
                log_message "macOS DMG installer failed structure verification" "ERROR"
                ret_val=1
            else
                log_message "macOS DMG installer structure verification passed" "INFO"
            fi
        else
            log_message "hdiutil not available, skipping DMG structure verification" "WARN"
        fi
    fi
    
    # Check code signature if on macOS
    if [[ "$(uname)" == "Darwin" ]] && command -v codesign &> /dev/null; then
        if ! codesign -v "$installer_file" &> /dev/null; then
            log_message "macOS installer is not signed or has invalid signature" "WARN"
        else
            log_message "macOS installer signature verification passed" "INFO"
        fi
    fi
    
    if [[ $ret_val -eq 0 ]]; then
        log_message "macOS installer verification completed successfully" "INFO"
    else
        log_message "macOS installer verification failed" "ERROR"
    fi
    
    return $ret_val
}

# Verify Linux installers
verify_linux_installers() {
    local ret_val=0
    
    if [[ "$LINUX_ENABLED" != "true" ]]; then
        log_message "Skipping Linux installer verification" "INFO"
        return 0
    fi
    
    log_message "Verifying Linux installers..." "INFO"
    
    # Default values if not set in properties
    local installer_types="${LINUX_INSTALLER_TYPES:-deb,rpm}"
    local package_name="${LINUX_PACKAGE_NAME:-compound-interest-calculator}"
    
    # Convert comma-separated list to array
    IFS=',' read -r -a types_array <<< "$installer_types"
    
    for type in "${types_array[@]}"; do
        local installer_file="$LINUX_DIR/${package_name}_${APP_VERSION}-1_amd64.$type"
        
        # For RPM, use different naming convention
        if [[ "$type" == "rpm" ]]; then
            installer_file="$LINUX_DIR/${package_name}-${APP_VERSION}-1.x86_64.$type"
        fi
        
        # Check if installer file exists
        if [[ ! -f "$installer_file" ]]; then
            log_message "Linux $type installer not found: $installer_file" "ERROR"
            ret_val=1
            continue
        fi
        
        # Verify file size
        local file_size=$(stat -c%s "$installer_file" 2>/dev/null || stat -f%z "$installer_file" 2>/dev/null)
        if [[ -z "$file_size" ]]; then
            log_message "Failed to determine file size for: $installer_file" "ERROR"
            ret_val=1
        elif [[ $file_size -lt $MIN_SIZE_LINUX ]]; then
            log_message "Linux $type installer file size is too small: $file_size bytes (min: $MIN_SIZE_LINUX bytes)" "ERROR"
            ret_val=1
        else
            log_message "Linux $type installer file size OK: $file_size bytes" "INFO"
        fi
        
        # Generate and store checksum
        local checksum=$(generate_checksum "$installer_file")
        if [[ -n "$checksum" ]]; then
            echo "$checksum *$(basename "$installer_file")" >> "$CHECKSUM_FILE"
            log_message "Linux $type installer checksum: $checksum" "INFO"
        else
            log_message "Failed to generate checksum for Linux $type installer" "ERROR"
            ret_val=1
        fi
        
        # Type-specific verification
        if [[ "$type" == "deb" ]] && command -v dpkg &> /dev/null; then
            if ! dpkg -I "$installer_file" &> /dev/null; then
                log_message "Linux DEB installer failed structure verification" "ERROR"
                ret_val=1
            else
                local pkg_info=$(dpkg -I "$installer_file" 2>/dev/null)
                log_message "Linux DEB installer structure verification passed" "INFO"
                if [[ "$VERBOSE" == "true" ]]; then
                    log_message "DEB package info: $pkg_info" "DEBUG"
                fi
            fi
        elif [[ "$type" == "rpm" ]] && command -v rpm &> /dev/null; then
            if ! rpm -qip "$installer_file" &> /dev/null; then
                log_message "Linux RPM installer failed structure verification" "ERROR"
                ret_val=1
            else
                local pkg_info=$(rpm -qip "$installer_file" 2>/dev/null)
                log_message "Linux RPM installer structure verification passed" "INFO"
                if [[ "$VERBOSE" == "true" ]]; then
                    log_message "RPM package info: $pkg_info" "DEBUG"
                fi
            fi
        else
            log_message "Skipping structure verification for Linux $type installer (tools not available)" "WARN"
        fi
    done
    
    if [[ $ret_val -eq 0 ]]; then
        log_message "Linux installer verification completed successfully" "INFO"
    else
        log_message "Linux installer verification failed" "ERROR"
    fi
    
    return $ret_val
}

# Generate a verification report
generate_verification_report() {
    local windows_result=$1
    local macos_result=$2
    local linux_result=$3
    local report_file="$OUTPUT_DIR/verification_report.txt"
    
    log_message "Generating verification report..." "INFO"
    
    # Create the report file
    echo "============================================================" > "$report_file"
    echo "            INSTALLER VERIFICATION REPORT                   " >> "$report_file"
    echo "============================================================" >> "$report_file"
    echo "Application: $APP_NAME" >> "$report_file"
    echo "Version: $APP_VERSION" >> "$report_file"
    echo "Date: $(date)" >> "$report_file"
    echo "============================================================" >> "$report_file"
    echo "" >> "$report_file"
    
    # Windows verification results
    echo "WINDOWS INSTALLER VERIFICATION" >> "$report_file"
    echo "------------------------------------------------------------" >> "$report_file"
    if [[ "$WINDOWS_ENABLED" == "true" ]]; then
        if [[ $windows_result -eq 0 ]]; then
            echo "Status: SUCCESS" >> "$report_file"
        else
            echo "Status: FAILED" >> "$report_file"
        fi
        
        local installer_type="${WIN_INSTALLER_TYPE:-msi}"
        local installer_file="$WINDOWS_DIR/${APP_NAME// /-}-${APP_VERSION}.$installer_type"
        
        if [[ -f "$installer_file" ]]; then
            local file_size=$(stat -c%s "$installer_file" 2>/dev/null || stat -f%z "$installer_file" 2>/dev/null)
            local checksum=$(generate_checksum "$installer_file")
            
            echo "File: $(basename "$installer_file")" >> "$report_file"
            echo "Size: $file_size bytes" >> "$report_file"
            echo "Checksum (SHA-256): $checksum" >> "$report_file"
        else
            echo "File: Not found" >> "$report_file"
        fi
    else
        echo "Status: SKIPPED" >> "$report_file"
    fi
    echo "" >> "$report_file"
    
    # macOS verification results
    echo "MACOS INSTALLER VERIFICATION" >> "$report_file"
    echo "------------------------------------------------------------" >> "$report_file"
    if [[ "$MACOS_ENABLED" == "true" ]]; then
        if [[ $macos_result -eq 0 ]]; then
            echo "Status: SUCCESS" >> "$report_file"
        else
            echo "Status: FAILED" >> "$report_file"
        fi
        
        local installer_type="${MAC_INSTALLER_TYPE:-dmg}"
        local installer_file="$MACOS_DIR/${APP_NAME// /-}-${APP_VERSION}.$installer_type"
        
        if [[ -f "$installer_file" ]]; then
            local file_size=$(stat -c%s "$installer_file" 2>/dev/null || stat -f%z "$installer_file" 2>/dev/null)
            local checksum=$(generate_checksum "$installer_file")
            
            echo "File: $(basename "$installer_file")" >> "$report_file"
            echo "Size: $file_size bytes" >> "$report_file"
            echo "Checksum (SHA-256): $checksum" >> "$report_file"
        else
            echo "File: Not found" >> "$report_file"
        fi
    else
        echo "Status: SKIPPED" >> "$report_file"
    fi
    echo "" >> "$report_file"
    
    # Linux verification results
    echo "LINUX INSTALLER VERIFICATION" >> "$report_file"
    echo "------------------------------------------------------------" >> "$report_file"
    if [[ "$LINUX_ENABLED" == "true" ]]; then
        if [[ $linux_result -eq 0 ]]; then
            echo "Status: SUCCESS" >> "$report_file"
        else
            echo "Status: FAILED" >> "$report_file"
        fi
        
        local installer_types="${LINUX_INSTALLER_TYPES:-deb,rpm}"
        local package_name="${LINUX_PACKAGE_NAME:-compound-interest-calculator}"
        
        # Convert comma-separated list to array
        IFS=',' read -r -a types_array <<< "$installer_types"
        
        for type in "${types_array[@]}"; do
            local installer_file="$LINUX_DIR/${package_name}_${APP_VERSION}-1_amd64.$type"
            
            # For RPM, use different naming convention
            if [[ "$type" == "rpm" ]]; then
                installer_file="$LINUX_DIR/${package_name}-${APP_VERSION}-1.x86_64.$type"
            fi
            
            echo "Type: $type" >> "$report_file"
            if [[ -f "$installer_file" ]]; then
                local file_size=$(stat -c%s "$installer_file" 2>/dev/null || stat -f%z "$installer_file" 2>/dev/null)
                local checksum=$(generate_checksum "$installer_file")
                
                echo "  File: $(basename "$installer_file")" >> "$report_file"
                echo "  Size: $file_size bytes" >> "$report_file"
                echo "  Checksum (SHA-256): $checksum" >> "$report_file"
            else
                echo "  File: Not found" >> "$report_file"
            fi
            echo "" >> "$report_file"
        done
    else
        echo "Status: SKIPPED" >> "$report_file"
    fi
    echo "" >> "$report_file"
    
    # Overall verification status
    echo "OVERALL VERIFICATION STATUS" >> "$report_file"
    echo "------------------------------------------------------------" >> "$report_file"
    local overall_success=true
    
    if [[ "$WINDOWS_ENABLED" == "true" ]] && [[ $windows_result -ne 0 ]]; then
        overall_success=false
    fi
    
    if [[ "$MACOS_ENABLED" == "true" ]] && [[ $macos_result -ne 0 ]]; then
        overall_success=false
    fi
    
    if [[ "$LINUX_ENABLED" == "true" ]] && [[ $linux_result -ne 0 ]]; then
        overall_success=false
    fi
    
    if [[ "$overall_success" == "true" ]]; then
        echo "Status: SUCCESS - All enabled verifications passed" >> "$report_file"
    else
        echo "Status: FAILED - One or more verifications failed" >> "$report_file"
    fi
    echo "" >> "$report_file"
    
    # Timestamps and locations
    echo "VERIFICATION DETAILS" >> "$report_file"
    echo "------------------------------------------------------------" >> "$report_file"
    echo "Verification started: $(head -n 1 "$VERIFY_LOG" | cut -d'-' -f2-)" >> "$report_file"
    echo "Verification completed: $(date)" >> "$report_file"
    echo "Log file: $VERIFY_LOG" >> "$report_file"
    echo "Checksums file: $CHECKSUM_FILE" >> "$report_file"
    echo "" >> "$report_file"
    echo "============================================================" >> "$report_file"
    
    log_message "Verification report generated: $report_file" "INFO"
}

# Verify a file against its expected checksum
verify_checksum() {
    local file_path=$1
    local expected_checksum=$2
    
    if [[ ! -f "$file_path" ]]; then
        log_message "File not found for checksum verification: $file_path" "ERROR"
        return 1
    fi
    
    local actual_checksum=$(generate_checksum "$file_path")
    
    if [[ "$actual_checksum" == "$expected_checksum" ]]; then
        log_message "Checksum verification successful for: $(basename "$file_path")" "INFO"
        return 0
    else
        log_message "Checksum verification failed for: $(basename "$file_path")" "ERROR"
        log_message "Expected: $expected_checksum" "ERROR"
        log_message "Actual: $actual_checksum" "ERROR"
        return 1
    fi
}

# Generate a SHA-256 checksum for a file
generate_checksum() {
    local file_path=$1
    
    if [[ ! -f "$file_path" ]]; then
        log_message "File not found for checksum generation: $file_path" "ERROR"
        return 1
    fi
    
    local checksum=""
    
    # Try using sha256sum (Linux, Windows with cygwin/msys)
    if command -v sha256sum &> /dev/null; then
        checksum=$(sha256sum "$file_path" | cut -d' ' -f1)
    # Fall back to shasum (macOS)
    elif command -v shasum &> /dev/null; then
        checksum=$(shasum -a 256 "$file_path" | cut -d' ' -f1)
    else
        log_message "No checksum tool available (sha256sum or shasum)" "ERROR"
        return 1
    fi
    
    echo "$checksum"
}

# Log a message to both console and log file
log_message() {
    local message=$1
    local level=${2:-INFO}
    local timestamp=$(date "+%Y-%m-%d %H:%M:%S")
    
    # Format the message
    local formatted_message="[$timestamp] [$level] $message"
    
    # Print to console with color based on level
    if [[ "$level" == "ERROR" ]]; then
        echo -e "\033[31m$formatted_message\033[0m"  # Red
    elif [[ "$level" == "WARN" ]]; then
        echo -e "\033[33m$formatted_message\033[0m"  # Yellow
    elif [[ "$level" == "INFO" ]]; then
        echo -e "\033[36m$formatted_message\033[0m"  # Cyan
    elif [[ "$level" == "DEBUG" ]]; then
        # Only print debug messages if verbose mode is enabled
        if [[ "$VERBOSE" == "true" ]]; then
            echo -e "\033[90m$formatted_message\033[0m"  # Gray
        fi
    else
        echo "$formatted_message"
    fi
    
    # Write to log file
    if [[ -d "$LOG_DIR" ]]; then
        echo "$formatted_message" >> "$VERIFY_LOG"
    fi
}

# Print a summary of the verification results
print_summary() {
    local windows_result=$1
    local macos_result=$2
    local linux_result=$3
    
    echo ""
    echo "================================================================================"
    echo "                     VERIFICATION SUMMARY                                       "
    echo "================================================================================"
    
    # Windows summary
    echo -n "Windows Installer: "
    if [[ "$WINDOWS_ENABLED" != "true" ]]; then
        echo "SKIPPED"
    elif [[ $windows_result -eq 0 ]]; then
        echo -e "\033[32mSUCCESS\033[0m"  # Green
    else
        echo -e "\033[31mFAILED\033[0m"   # Red
    fi
    
    # macOS summary
    echo -n "macOS Installer:   "
    if [[ "$MACOS_ENABLED" != "true" ]]; then
        echo "SKIPPED"
    elif [[ $macos_result -eq 0 ]]; then
        echo -e "\033[32mSUCCESS\033[0m"  # Green
    else
        echo -e "\033[31mFAILED\033[0m"   # Red
    fi
    
    # Linux summary
    echo -n "Linux Installer:   "
    if [[ "$LINUX_ENABLED" != "true" ]]; then
        echo "SKIPPED"
    elif [[ $linux_result -eq 0 ]]; then
        echo -e "\033[32mSUCCESS\033[0m"  # Green
    else
        echo -e "\033[31mFAILED\033[0m"   # Red
    fi
    
    echo "--------------------------------------------------------------------------------"
    
    # Overall status
    local overall_success=true
    
    if [[ "$WINDOWS_ENABLED" == "true" ]] && [[ $windows_result -ne 0 ]]; then
        overall_success=false
    fi
    
    if [[ "$MACOS_ENABLED" == "true" ]] && [[ $macos_result -ne 0 ]]; then
        overall_success=false
    fi
    
    if [[ "$LINUX_ENABLED" == "true" ]] && [[ $linux_result -ne 0 ]]; then
        overall_success=false
    fi
    
    echo -n "Overall Status:    "
    if [[ "$overall_success" == "true" ]]; then
        echo -e "\033[32mSUCCESS - All enabled verifications passed\033[0m"  # Green
    else
        echo -e "\033[31mFAILED - One or more verifications failed\033[0m"   # Red
    fi
    
    echo "--------------------------------------------------------------------------------"
    echo "Verification Report: $OUTPUT_DIR/verification_report.txt"
    echo "Checksums File:      $CHECKSUM_FILE"
    echo "Log File:            $VERIFY_LOG"
    echo "================================================================================"
}

# Main function
main() {
    local args=("$@")
    local windows_result=0
    local macos_result=0
    local linux_result=0
    local overall_result=0
    
    # Print banner
    print_banner
    
    # Parse command-line arguments
    parse_arguments "${args[@]}"
    
    # Set up environment
    setup_environment
    if [[ $? -ne 0 ]]; then
        log_message "Failed to set up verification environment" "ERROR"
        return 1
    fi
    
    # Load properties
    load_properties
    if [[ $? -ne 0 ]]; then
        log_message "Failed to load properties" "ERROR"
        return 1
    fi
    
    # Verify Windows installer
    if [[ "$WINDOWS_ENABLED" == "true" ]]; then
        verify_windows_installer
        windows_result=$?
    fi
    
    # Verify macOS installer
    if [[ "$MACOS_ENABLED" == "true" ]]; then
        verify_macos_installer
        macos_result=$?
    fi
    
    # Verify Linux installers
    if [[ "$LINUX_ENABLED" == "true" ]]; then
        verify_linux_installers
        linux_result=$?
    fi
    
    # Generate verification report
    generate_verification_report $windows_result $macos_result $linux_result
    
    # Print summary
    print_summary $windows_result $macos_result $linux_result
    
    # Determine overall result
    if [[ "$WINDOWS_ENABLED" == "true" ]] && [[ $windows_result -ne 0 ]]; then
        overall_result=1
    fi
    
    if [[ "$MACOS_ENABLED" == "true" ]] && [[ $macos_result -ne 0 ]]; then
        overall_result=1
    fi
    
    if [[ "$LINUX_ENABLED" == "true" ]] && [[ $linux_result -ne 0 ]]; then
        overall_result=1
    fi
    
    return $overall_result
}

# Execute main function with all script arguments
main "$@"
exit $?