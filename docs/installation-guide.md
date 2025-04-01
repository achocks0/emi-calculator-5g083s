# Compound Interest Calculator - Installation Guide

## Introduction

The Compound Interest Calculator is a desktop application designed for banking staff to calculate Equated Monthly Installments (EMI) based on principal amount and loan duration. This installation guide provides detailed instructions for installing and setting up the application on Windows, macOS, and Linux operating systems.

This guide is intended for:
- Individual bank staff who need to install the application on their workstation
- IT administrators deploying the application across multiple workstations
- Technical support personnel who assist with application installation and troubleshooting

## System Requirements

Before installing the Compound Interest Calculator, ensure your system meets the following requirements:

### Hardware Requirements

| Component | Minimum Requirement | Recommended |
|-----------|---------------------|-------------|
| Processor | 1 GHz or faster | 2 GHz or faster |
| Memory | 512 MB RAM | 1 GB RAM |
| Disk Space | 100 MB free space | 200 MB free space |
| Display | 1024×768 resolution | 1920×1080 resolution |

### Software Requirements

| Component | Requirement |
|-----------|-------------|
| Operating System | • Windows 10/11<br>• macOS 10.14 (Mojave) or later<br>• Ubuntu 18.04 LTS or later<br>• Red Hat Enterprise Linux 8 or later<br>• CentOS 8 or later |
| Java Runtime Environment | JRE 11 or higher (bundled with installers) |

> **Note:** If you're using the cross-platform JAR package rather than the platform-specific installers, you will need to install JRE 11+ separately.

## Pre-Installation Checklist

### Verifying System Compatibility

Before proceeding with installation, verify that your system meets the requirements:

1. **Check your operating system version**:
   - Windows: Right-click on "This PC" or "My Computer" and select "Properties"
   - macOS: Click the Apple menu > About This Mac
   - Linux: Open a terminal and type `lsb_release -a` or `cat /etc/os-release`

2. **Check available disk space**:
   - Windows: Right-click on the C: drive, select "Properties"
   - macOS: Apple menu > About This Mac > Storage
   - Linux: Open a terminal and type `df -h`

3. **Check system memory**:
   - Windows: Open Task Manager (Ctrl+Shift+Esc) > Performance tab
   - macOS: Apple menu > About This Mac > Memory
   - Linux: Open a terminal and type `free -h`

### Java Runtime Environment

If you're using the platform-specific installers (MSI, DMG, DEB, or RPM), the Java Runtime Environment is bundled with the application, and you can skip this step.

If you're using the JAR file for installation, verify that JRE 11 or higher is installed:

1. **Check your current Java version**:
   - Open a command prompt (Windows) or terminal (macOS/Linux)
   - Type `java -version` and press Enter
   - Verify that the version is 11 or higher

2. **If Java is not installed or the version is below 11**:
   - Download JRE 11 or higher from [AdoptOpenJDK](https://adoptopenjdk.net/) or [Oracle](https://www.oracle.com/java/technologies/javase-jre11-downloads.html)
   - Follow the installation instructions for your operating system

## Windows Installation

### Using the MSI Installer

The Windows MSI installer provides the simplest installation method for Windows users.

1. Download the `CompoundInterestCalculator-1.0.0.msi` installer from the distribution server or portal
2. Double-click the MSI file to start the installation wizard
3. If a User Account Control prompt appears, click "Yes" to allow the installation
4. Click "Next" on the welcome screen
5. Review and accept the license agreement, then click "Next"
6. Choose the installation directory or use the default (`C:\Program Files\Compound Interest Calculator`), then click "Next"
7. Select shortcut options (Desktop and/or Start Menu), then click "Next"
8. Click "Install" to begin the installation process
9. Wait for the installation to complete
10. Click "Finish" to exit the installer

![Windows Installation Process](images/windows-installation.png)

### Manual Installation

If you prefer not to use the MSI installer, you can manually install using the JAR file:

1. Ensure Java 11 or higher is installed on your system (see [Java Runtime Environment](#java-runtime-environment))
2. Download the `compound-interest-calculator-1.0.0-jar-with-dependencies.jar` file
3. Create a folder where you want to install the application (e.g., `C:\Program Files\Compound Interest Calculator`)
4. Copy the JAR file to this folder
5. Right-click in the folder and create a new text file
6. Enter the following content:
   ```
   javaw -jar "compound-interest-calculator-1.0.0-jar-with-dependencies.jar"
   ```
7. Save the file as `CompoundInterestCalculator.bat`
8. Right-click on `CompoundInterestCalculator.bat` and select "Create shortcut"
9. Move the shortcut to your Desktop or Start Menu

### Silent Installation

For automated deployment or silent installation:

1. Open a command prompt with administrator privileges
2. Navigate to the folder containing the MSI file
3. Run the following command:
   ```
   msiexec /i CompoundInterestCalculator-1.0.0.msi /quiet
   ```

Additional command-line options:
- `/qb`: Basic UI with no user interaction
- `INSTALLDIR="C:\Custom\Path"`: Specify a custom installation directory
- `DESKTOP_SHORTCUT=0`: Disable desktop shortcut creation (1 to enable)
- `STARTMENU_SHORTCUT=0`: Disable Start Menu shortcut creation (1 to enable)

## macOS Installation

### Using the DMG Installer

1. Download the `CompoundInterestCalculator-1.0.0.dmg` disk image
2. Double-click the DMG file to mount it
3. A Finder window will open showing the application icon and an Applications folder shortcut
4. Drag the Compound Interest Calculator icon to the Applications folder
5. Eject the disk image by right-clicking on it in the sidebar and selecting "Eject"
6. Open the Applications folder and double-click Compound Interest Calculator to launch

> **Note:** When launching for the first time, macOS might show a security warning. If this happens, right-click (or Control+click) on the application and select "Open", then click "Open" in the confirmation dialog.

![macOS Installation Process](images/macos-installation.png)

### Manual Installation

For manual installation using the JAR file:

1. Ensure Java 11 or higher is installed on your system (see [Java Runtime Environment](#java-runtime-environment))
2. Download the `compound-interest-calculator-1.0.0-jar-with-dependencies.jar` file
3. Create a folder in your Applications directory (e.g., `/Applications/Compound Interest Calculator`)
4. Copy the JAR file to this folder
5. Create a new file named `CompoundInterestCalculator.command` with the following content:
   ```bash
   #!/bin/bash
   cd "$(dirname "$0")"
   java -jar compound-interest-calculator-1.0.0-jar-with-dependencies.jar
   ```
6. Make the file executable by opening Terminal and running:
   ```bash
   chmod +x /Applications/Compound\ Interest\ Calculator/CompoundInterestCalculator.command
   ```
7. Create an alias of the command file on your desktop or dock

## Linux Installation

### Debian/Ubuntu Installation

For Debian-based distributions (Ubuntu, Mint, etc.):

1. Download the `compound-interest-calculator_1.0.0_amd64.deb` package
2. Open a terminal window
3. Navigate to the download directory:
   ```bash
   cd ~/Downloads
   ```
4. Run the installation command:
   ```bash
   sudo apt install ./compound-interest-calculator_1.0.0_amd64.deb
   ```
5. Enter your password when prompted
6. The application will be installed in `/opt/Compound Interest Calculator`
7. Launch from the Applications menu or by running `compound-interest-calculator` in the terminal

### RHEL/CentOS Installation

For Red Hat-based distributions (RHEL, CentOS, Fedora):

1. Download the `compound-interest-calculator-1.0.0.x86_64.rpm` package
2. Open a terminal window
3. Navigate to the download directory:
   ```bash
   cd ~/Downloads
   ```
4. Run the installation command:
   ```bash
   sudo dnf install ./compound-interest-calculator-1.0.0.x86_64.rpm
   ```
   (On older systems, use `sudo yum install` instead)
5. Enter your password when prompted
6. The application will be installed in `/opt/Compound Interest Calculator`
7. Launch from the Applications menu or by running `compound-interest-calculator` in the terminal

### Manual Installation

For manual installation or distributions without DEB/RPM support:

1. Ensure Java 11 or higher is installed on your system (see [Java Runtime Environment](#java-runtime-environment))
2. Download the `compound-interest-calculator-1.0.0-jar-with-dependencies.jar` file
3. Create a directory for the application:
   ```bash
   sudo mkdir -p /opt/compound-interest-calculator
   ```
4. Copy the JAR file to this directory:
   ```bash
   sudo cp compound-interest-calculator-1.0.0-jar-with-dependencies.jar /opt/compound-interest-calculator/
   ```
5. Create a launcher script:
   ```bash
   sudo nano /usr/local/bin/compound-interest-calculator
   ```
6. Add the following content:
   ```bash
   #!/bin/bash
   java -jar /opt/compound-interest-calculator/compound-interest-calculator-1.0.0-jar-with-dependencies.jar
   ```
7. Make the script executable:
   ```bash
   sudo chmod +x /usr/local/bin/compound-interest-calculator
   ```
8. Create a desktop entry:
   ```bash
   sudo nano /usr/share/applications/compound-interest-calculator.desktop
   ```
9. Add the following content:
   ```
   [Desktop Entry]
   Name=Compound Interest Calculator
   Comment=Calculator for EMI based on principal amount and loan duration
   Exec=compound-interest-calculator
   Icon=/opt/compound-interest-calculator/icon.png
   Terminal=false
   Type=Application
   Categories=Office;Finance;
   ```

## Enterprise Deployment

### Software Distribution Tools

For large-scale deployments across multiple workstations, consider using enterprise software distribution systems:

#### Windows Deployment

**Microsoft System Center Configuration Manager (SCCM)**:

1. Add the MSI package to the SCCM application catalog:
   - Open SCCM Console > Software Library > Application Management > Applications
   - Click "Create Application" and select "Automatically detect information about this application from installation files"
   - Browse to the MSI file and complete the wizard

2. Create a deployment:
   - Right-click the application and select "Deploy"
   - Select the target collection (devices or users)
   - Configure deployment settings (required vs. available, schedule, etc.)
   - Complete the deployment wizard

MSI Properties for customization:
```
INSTALLDIR="C:\Custom\Path"
DESKTOP_SHORTCUT=0
STARTMENU_SHORTCUT=1
```

### Group Policy Deployment (Windows)

For Active Directory environments:

1. Create a network share to store the MSI file
2. Create a Group Policy Object (GPO):
   - Open Group Policy Management Console
   - Right-click on the OU containing target computers and select "Create a GPO in this domain, and Link it here"
   - Name the GPO (e.g., "Compound Interest Calculator Deployment")

3. Edit the GPO:
   - Right-click the GPO and select "Edit"
   - Navigate to Computer Configuration > Policies > Software Settings > Software Installation
   - Right-click and select "New > Package"
   - Browse to the MSI file using the UNC path (\\server\share\CompoundInterestCalculator-1.0.0.msi)
   - Select "Assigned" deployment method
   - Configure additional options as needed

4. The application will install automatically when computers restart

### Package Management Systems (Linux)

For enterprise Linux deployments:

#### Setting up an internal APT repository (Debian/Ubuntu):

1. Create a repository structure:
   ```bash
   mkdir -p /var/www/html/apt-repo/pool/main
   ```

2. Copy the DEB package to the pool:
   ```bash
   cp compound-interest-calculator_1.0.0_amd64.deb /var/www/html/apt-repo/pool/main/
   ```

3. Generate the repository metadata:
   ```bash
   cd /var/www/html/apt-repo
   dpkg-scanpackages pool /dev/null | gzip -9c > Packages.gz
   ```

4. Configure client systems to use the repository:
   ```bash
   echo "deb http://your-server/apt-repo ./" | sudo tee /etc/apt/sources.list.d/internal-repo.list
   ```

5. Deploy using standard package management tools:
   ```bash
   sudo apt update
   sudo apt install compound-interest-calculator
   ```

#### Setting up an internal YUM repository (RHEL/CentOS):

1. Create a repository structure:
   ```bash
   mkdir -p /var/www/html/yum-repo/
   ```

2. Copy the RPM package to the repository:
   ```bash
   cp compound-interest-calculator-1.0.0.x86_64.rpm /var/www/html/yum-repo/
   ```

3. Generate the repository metadata:
   ```bash
   createrepo /var/www/html/yum-repo/
   ```

4. Configure client systems to use the repository:
   ```bash
   sudo nano /etc/yum.repos.d/internal.repo
   ```
   Add:
   ```
   [internal]
   name=Internal Repository
   baseurl=http://your-server/yum-repo/
   enabled=1
   gpgcheck=0
   ```

5. Deploy using standard package management tools:
   ```bash
   sudo yum install compound-interest-calculator
   ```

#### Deploying via Ansible:

For automated deployment across multiple Linux systems:

```yaml
---
- name: Deploy Compound Interest Calculator
  hosts: all
  tasks:
    - name: Install Compound Interest Calculator (Debian/Ubuntu)
      apt:
        deb: http://your-server/path/to/compound-interest-calculator_1.0.0_amd64.deb
      when: ansible_os_family == "Debian"

    - name: Install Compound Interest Calculator (RHEL/CentOS)
      yum:
        name: http://your-server/path/to/compound-interest-calculator-1.0.0.x86_64.rpm
        state: present
      when: ansible_os_family == "RedHat"
```

## Verifying Installation

### Launching the Application

After installation, you can launch the application using the following methods:

**Windows:**
- Double-click the "Compound Interest Calculator" icon on your desktop
- Click Start > Compound Interest Calculator
- Run from the command line: `"C:\Program Files\Compound Interest Calculator\CompoundInterestCalculator.exe"`

**macOS:**
- Open the Applications folder and double-click "Compound Interest Calculator"
- Click on the application icon in the Dock (if pinned)
- From Terminal: `open /Applications/Compound\ Interest\ Calculator.app`

**Linux:**
- Click on the application icon in the Applications menu (usually under Office or Finance category)
- Run from terminal: `compound-interest-calculator`

If the application launches successfully, you will see the main calculator window:

![Main Application Window](images/main-window.png)

### Troubleshooting Installation Issues

If you encounter issues during installation, try the following solutions:

#### Application fails to start

**Possible Causes:**
- Missing Java Runtime
- Corrupted installation
- Insufficient permissions

**Solutions:**
1. Verify Java installation by running `java -version` in command line/terminal
2. Reinstall the application using administrator/root privileges
3. Check for error logs:
   - Windows: `%TEMP%\Compound Interest Calculator_install.log`
   - macOS: `/var/log/install.log`
   - Linux: `/var/log/dpkg.log` or `/var/log/yum.log`

#### Installation fails

**Possible Causes:**
- Insufficient permissions
- Disk space issues
- Pre-existing installation

**Solutions:**
1. Run installer as administrator/root
2. Free up disk space (at least 100MB required)
3. Uninstall any previous versions before installing

#### Java version conflict

**Possible Causes:**
- Multiple Java versions installed
- System using incorrect Java version

**Solutions:**
1. Use the bundled JRE included with platform-specific installers
2. Set JAVA_HOME environment variable to JRE 11 or higher
3. Configure system to use the correct Java version:
   - Windows: Update the PATH environment variable
   - macOS: Use `java_home -V` to view available versions
   - Linux: Use `update-alternatives --config java`

## Updating the Application

### Windows Updates

To update to a newer version on Windows:

1. Download the new MSI installer file
2. Double-click the installer to run it
3. Follow the on-screen instructions
4. The installer will automatically update the existing installation

For silent updates:
```
msiexec /i CompoundInterestCalculator-NEW_VERSION.msi /quiet
```

### macOS Updates

To update on macOS:

1. Download the new DMG file
2. Mount the DMG by double-clicking it
3. Drag the application icon to the Applications folder
4. If prompted about replacing an existing item, click "Replace"
5. Eject the disk image

### Linux Updates

For Debian/Ubuntu systems:
```bash
sudo apt install ./compound-interest-calculator_NEW_VERSION_amd64.deb
```

For RHEL/CentOS systems:
```bash
sudo dnf update ./compound-interest-calculator-NEW_VERSION.x86_64.rpm
```

For JAR installations, simply replace the old JAR file with the new one.

## Uninstallation

### Windows Uninstallation

Method 1: Using Control Panel
1. Open Control Panel > Programs > Programs and Features
2. Find "Compound Interest Calculator" in the list
3. Click on it and select "Uninstall"
4. Follow the on-screen instructions

Method 2: Using the command line (for silent uninstallation)
```
msiexec /x {ProductCode} /quiet
```
Replace `{ProductCode}` with the product code, which can be found by:
1. Open registry editor (regedit)
2. Navigate to `HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall`
3. Look for Compound Interest Calculator entry and find the "UninstallString" value

### macOS Uninstallation

1. Open the Applications folder in Finder
2. Find "Compound Interest Calculator"
3. Drag the application to the Trash
4. Empty the Trash

For a complete uninstallation including preferences:
1. Delete the application preferences:
   ```bash
   rm ~/Library/Preferences/com.bankname.compoundinterestcalculator.plist
   ```
2. Delete any application support files:
   ```bash
   rm -rf ~/Library/Application\ Support/Compound\ Interest\ Calculator/
   ```

### Linux Uninstallation

For Debian/Ubuntu systems:
```bash
sudo apt remove compound-interest-calculator
```

To remove all configuration files:
```bash
sudo apt purge compound-interest-calculator
```

For RHEL/CentOS systems:
```bash
sudo dnf remove compound-interest-calculator
```

For manual installations:
```bash
sudo rm /usr/local/bin/compound-interest-calculator
sudo rm /usr/share/applications/compound-interest-calculator.desktop
sudo rm -rf /opt/compound-interest-calculator
```

## Appendix

### Installation Log Files

When troubleshooting installation issues, check the following log file locations:

**Windows:**
- Installation logs: `%TEMP%\Compound Interest Calculator_install.log`
- Application logs: `%USERPROFILE%\AppData\Local\Compound Interest Calculator\logs\`

**macOS:**
- Installation logs: `/var/log/install.log`
- Application logs: `~/Library/Logs/Compound Interest Calculator/`

**Linux:**
- Debian/Ubuntu installation logs: `/var/log/dpkg.log`
- RHEL/CentOS installation logs: `/var/log/yum.log` or `/var/log/dnf.log`
- Application logs: `~/.local/share/Compound Interest Calculator/logs/`

### Command-Line Installation Options

#### Windows MSI Command-Line Options

```
msiexec /i CompoundInterestCalculator-1.0.0.msi [OPTIONS]
```

Common options:
- `/quiet`: Silent installation with no user interface
- `/qb`: Basic UI with no user interaction
- `/log log.txt`: Create a detailed log file
- `INSTALLDIR="C:\Custom\Path"`: Specify installation directory
- `DESKTOP_SHORTCUT=0`: Disable desktop shortcut (1 to enable)
- `STARTMENU_SHORTCUT=0`: Disable Start Menu shortcut (1 to enable)

#### Debian/Ubuntu DEB Package Options

```bash
sudo apt install ./compound-interest-calculator_1.0.0_amd64.deb [OPTIONS]
```

Common options:
- `-y`: Automatically answer yes to prompts
- `--no-install-recommends`: Don't install recommended packages
- `--reinstall`: Reinstall if already installed

#### RHEL/CentOS RPM Package Options

```bash
sudo dnf install ./compound-interest-calculator-1.0.0.x86_64.rpm [OPTIONS]
```

Common options:
- `-y`: Automatically answer yes to prompts
- `--nodeps`: Don't check for dependencies
- `--reinstall`: Reinstall if already installed

### Frequently Asked Questions

**Q: Do I need to uninstall the previous version before installing an update?**  
A: No, the installer will automatically update the existing installation.

**Q: Can I install the application without administrator/root privileges?**  
A: The platform-specific installers (MSI, DMG, DEB, RPM) typically require administrator privileges. For non-admin installation, use the JAR file and install it in a location where you have write permissions.

**Q: Can I install the application on a shared network drive for multiple users?**  
A: Yes, you can install the JAR version on a network share. Create a batch file or script that launches the application from the network location.

**Q: Will the application work on older operating systems?**  
A: The application requires Windows 10/11, macOS 10.14+, or modern Linux distributions. It may work on older systems if they have Java 11 or higher installed, but this is not officially supported.

**Q: Does the application require internet connectivity to install or run?**  
A: No, the application is designed to work completely offline once installed.

**Q: Where can I find the latest version of the application?**  
A: Contact your IT department or check the internal distribution portal for the latest version.

**Q: How much disk space does the application require?**  
A: The application requires approximately 100MB of free disk space for installation.

**Q: Can I run multiple versions of the application side by side?**  
A: This is not recommended as it may cause conflicts. Always update to the latest version.