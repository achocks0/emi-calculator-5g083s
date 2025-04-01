#!/bin/bash
# package.sh - Script for packaging the Compound Interest Calculator application
# into distributable formats for Unix-based systems (Linux, macOS).

# Exit on error
set -e

# Global variables
SCRIPT_DIR=$(dirname "$0")
APP_NAME="Compound Interest Calculator"
APP_VERSION="1.0.0"
MAIN_CLASS="com.bank.calculator.CompoundInterestCalculatorApp"
TARGET_DIR="$SCRIPT_DIR/../target"
JAR_FILE="$TARGET_DIR/compound-interest-calculator.jar"
DIST_DIR="$SCRIPT_DIR/../dist"
ICON_PATH="$SCRIPT_DIR/../src/main/resources/icons/calculator-icon.png"

# Check prerequisites
check_prerequisites() {
    echo "Checking prerequisites..."
    
    # Check Java version
    if ! command -v java &> /dev/null; then
        echo "Error: Java is not installed or not in the PATH"
        return 1
    fi
    
    # Get Java version
    java_version=$(java -version 2>&1 | awk -F '"' '/version/ {print $2}')
    echo "Found Java version: $java_version"
    
    # Extract major version
    java_major=$(echo "$java_version" | cut -d'.' -f1)
    if [[ "$java_major" =~ ^[0-9]+$ ]]; then
        # It's a numeric version (Java 9+)
        java_major_version=$java_major
    else
        # It's in 1.X format (Java 8 or earlier)
        java_major_version=$(echo "$java_version" | cut -d'.' -f2)
    fi
    
    # Check for Java 11 or higher
    if [[ $java_major_version -lt 11 ]]; then
        echo "Error: Java 11 or higher is required. Found: $java_version"
        return 1
    fi
    
    # Check Maven
    if ! command -v mvn &> /dev/null; then
        echo "Error: Maven is not installed or not in the PATH"
        return 1
    fi
    
    mvn_version=$(mvn --version | head -1)
    echo "Found Maven: $mvn_version"
    
    # Check for jpackage availability
    jpackage_available=false
    
    # First check for standalone jpackage command
    if command -v jpackage &> /dev/null; then
        jpackage_available=true
        echo "Found jpackage command"
    # Then check if Java 14+ is available (jpackage included in JDK)
    elif [[ $java_major_version -ge 14 ]]; then
        # Check if jpackage is in JAVA_HOME/bin
        if [ -n "$JAVA_HOME" ] && [ -f "$JAVA_HOME/bin/jpackage" ]; then
            jpackage_available=true
            echo "Found jpackage in JAVA_HOME: $JAVA_HOME/bin/jpackage"
        # It could be on the path but not as a separate command
        elif command -v "$JAVA_HOME/bin/jpackage" &> /dev/null; then
            jpackage_available=true
            echo "Found jpackage via JAVA_HOME path"
        else
            echo "Warning: Java $java_major_version should include jpackage, but it could not be found"
        fi
    else
        echo "Warning: jpackage not available. Java 14+ or standalone jpackage tool is required for native installers."
        echo "Will create JAR package only."
    fi
    
    echo "Prerequisites check completed."
    
    if $jpackage_available; then
        return 0
    else
        return 2
    fi
}

# Build the JAR file using Maven
build_jar() {
    echo "Building JAR file..."
    
    # Change to project root directory
    cd "$SCRIPT_DIR/.."
    
    # Build with Maven
    mvn clean package
    
    # Check if JAR file was created
    if [ ! -f "$JAR_FILE" ]; then
        echo "Error: JAR file was not created by Maven build"
        return 1
    fi
    
    echo "JAR file built successfully: $JAR_FILE"
    return 0
}

# Helper function to find jpackage command
find_jpackage() {
    if command -v jpackage &> /dev/null; then
        echo "jpackage"
        return 0
    elif [ -n "$JAVA_HOME" ] && [ -f "$JAVA_HOME/bin/jpackage" ]; then
        echo "$JAVA_HOME/bin/jpackage"
        return 0
    else
        echo ""
        return 1
    fi
}

# Create macOS DMG installer
create_dmg() {
    echo "Creating macOS DMG package..."
    
    # Check if we're on macOS
    if [[ "$(uname)" != "Darwin" ]]; then
        echo "Warning: Not on macOS, skipping DMG creation"
        return 0
    fi
    
    # Find jpackage command
    local jpackage_cmd=$(find_jpackage)
    if [ -z "$jpackage_cmd" ]; then
        echo "Error: jpackage command not found"
        return 1
    fi
    
    # Create temporary directory for packaging
    local pkg_dir="$TARGET_DIR/macos-package"
    mkdir -p "$pkg_dir"
    
    # Create native installer using jpackage
    $jpackage_cmd \
        --input "$TARGET_DIR" \
        --name "$APP_NAME" \
        --main-jar "$(basename "$JAR_FILE")" \
        --main-class "$MAIN_CLASS" \
        --app-version "$APP_VERSION" \
        --icon "$ICON_PATH" \
        --dest "$pkg_dir" \
        --type dmg \
        --vendor "Banking Division" \
        --copyright "Copyright $(date +%Y)" \
        --mac-package-name "$APP_NAME" \
        --description "Calculator for compound interest and EMI"
    
    # Move the created DMG to the distribution directory
    find "$pkg_dir" -name "*.dmg" -exec mv {} "$DIST_DIR/" \;
    
    echo "macOS DMG package created successfully"
    return 0
}

# Create Linux DEB installer
create_deb() {
    echo "Creating Linux DEB package..."
    
    # Check if we're on Linux
    if [[ "$(uname)" != "Linux" ]]; then
        echo "Warning: Not on Linux, skipping DEB creation"
        return 0
    fi
    
    # Find jpackage command
    local jpackage_cmd=$(find_jpackage)
    if [ -z "$jpackage_cmd" ]; then
        echo "Error: jpackage command not found"
        return 1
    fi
    
    # Create temporary directory for packaging
    local pkg_dir="$TARGET_DIR/linux-deb-package"
    mkdir -p "$pkg_dir"
    
    # Create native installer using jpackage
    $jpackage_cmd \
        --input "$TARGET_DIR" \
        --name "$APP_NAME" \
        --main-jar "$(basename "$JAR_FILE")" \
        --main-class "$MAIN_CLASS" \
        --app-version "$APP_VERSION" \
        --icon "$ICON_PATH" \
        --dest "$pkg_dir" \
        --type deb \
        --vendor "Banking Division" \
        --copyright "Copyright $(date +%Y)" \
        --description "Calculator for compound interest and EMI" \
        --linux-menu-group "Finance" \
        --linux-app-category "Finance" \
        --linux-shortcut
    
    # Move the created DEB to the distribution directory
    find "$pkg_dir" -name "*.deb" -exec mv {} "$DIST_DIR/" \;
    
    echo "Linux DEB package created successfully"
    return 0
}

# Create Linux RPM installer
create_rpm() {
    echo "Creating Linux RPM package..."
    
    # Check if we're on Linux
    if [[ "$(uname)" != "Linux" ]]; then
        echo "Warning: Not on Linux, skipping RPM creation"
        return 0
    fi
    
    # Check if rpm tools are available
    if ! command -v rpmbuild &> /dev/null; then
        echo "Warning: rpmbuild not found, skipping RPM creation"
        return 0
    fi
    
    # Find jpackage command
    local jpackage_cmd=$(find_jpackage)
    if [ -z "$jpackage_cmd" ]; then
        echo "Error: jpackage command not found"
        return 1
    fi
    
    # Create temporary directory for packaging
    local pkg_dir="$TARGET_DIR/linux-rpm-package"
    mkdir -p "$pkg_dir"
    
    # Create native installer using jpackage
    $jpackage_cmd \
        --input "$TARGET_DIR" \
        --name "$APP_NAME" \
        --main-jar "$(basename "$JAR_FILE")" \
        --main-class "$MAIN_CLASS" \
        --app-version "$APP_VERSION" \
        --icon "$ICON_PATH" \
        --dest "$pkg_dir" \
        --type rpm \
        --vendor "Banking Division" \
        --copyright "Copyright $(date +%Y)" \
        --description "Calculator for compound interest and EMI" \
        --linux-menu-group "Finance" \
        --linux-app-category "Finance" \
        --linux-shortcut
    
    # Move the created RPM to the distribution directory
    find "$pkg_dir" -name "*.rpm" -exec mv {} "$DIST_DIR/" \;
    
    echo "Linux RPM package created successfully"
    return 0
}

# Create runnable JAR with dependencies
create_jar_with_dependencies() {
    echo "Creating runnable JAR package..."
    
    # Create jar directory
    local jar_dir="$DIST_DIR/jar"
    mkdir -p "$jar_dir"
    
    # Copy the built JAR to the distribution directory
    cp "$JAR_FILE" "$jar_dir/"
    
    # Create a simple shell script to run the application
    cat > "$jar_dir/run.sh" << EOF
#!/bin/bash
# Script to run the Compound Interest Calculator application

# Get the directory of this script
SCRIPT_DIR=\$(dirname "\$(readlink -f "\$0")")

# Run the application
java -jar "\$SCRIPT_DIR/$(basename "$JAR_FILE")"
EOF
    
    # Make the script executable
    chmod +x "$jar_dir/run.sh"
    
    echo "Runnable JAR package created successfully"
    return 0
}

# Main function
main() {
    echo "=========================================================="
    echo "Packaging $APP_NAME v$APP_VERSION"
    echo "=========================================================="
    
    # Check prerequisites
    check_prerequisites
    local prereq_status=$?
    
    if [ $prereq_status -eq 1 ]; then
        echo "Prerequisites check failed. Exiting."
        exit 1
    fi
    
    # Build the JAR file
    build_jar || { echo "JAR build failed. Exiting."; exit 1; }
    
    # Create distribution directory if it doesn't exist
    mkdir -p "$DIST_DIR"
    
    # Create platform-specific packages if jpackage is available
    if [ $prereq_status -eq 0 ]; then
        if [ "$(uname)" == "Darwin" ]; then
            # macOS
            create_dmg || echo "Warning: Failed to create DMG package"
        elif [ "$(uname)" == "Linux" ]; then
            # Linux
            create_deb || echo "Warning: Failed to create DEB package"
            create_rpm || echo "Warning: Failed to create RPM package"
        fi
    fi
    
    # Create JAR package (works on any platform)
    create_jar_with_dependencies || echo "Warning: Failed to create JAR package"
    
    # Check if any packages were created
    if [ "$(find "$DIST_DIR" -type f | wc -l)" -eq 0 ]; then
        echo "No packages were created. Please check the errors above."
        exit 1
    fi
    
    echo "=========================================================="
    echo "Packaging completed successfully!"
    echo "Packages are available in: $DIST_DIR"
    echo "Created packages:"
    find "$DIST_DIR" -type f | sort
    echo "=========================================================="
    
    return 0
}

# Execute main function
main