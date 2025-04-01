# Compound Interest Calculator - User Guide

![Compound Interest Calculator Logo](images/calculator-logo.png)

**Version:** 1.0.0  
**Last Updated:** 2023-10-01  
**Target Audience:** Banking Division Staff  

## Table of Contents

- [Introduction](#introduction)
  - [Purpose](#purpose)
  - [Intended Audience](#intended-audience)
  - [System Requirements](#system-requirements)
- [Getting Started](#getting-started)
  - [Installation](#installation)
  - [Launching the Application](#launching-the-application)
  - [Application Interface Overview](#application-interface-overview)
- [Using the Calculator](#using-the-calculator)
  - [Entering Principal Amount](#entering-principal-amount)
  - [Entering Loan Duration](#entering-loan-duration)
  - [Calculating EMI](#calculating-emi)
  - [Viewing Detailed Results](#viewing-detailed-results)
  - [Starting a New Calculation](#starting-a-new-calculation)
- [Understanding the Results](#understanding-the-results)
  - [Monthly EMI](#monthly-emi)
  - [Total Interest](#total-interest)
  - [Total Amount Payable](#total-amount-payable)
  - [Interest Rate Information](#interest-rate-information)
- [Help and Support](#help-and-support)
  - [Using the Help Feature](#using-the-help-feature)
  - [Understanding Error Messages](#understanding-error-messages)
  - [Keyboard Shortcuts](#keyboard-shortcuts)
  - [Getting Technical Support](#getting-technical-support)
- [Calculation Formulas](#calculation-formulas)
  - [Compound Interest Formula](#compound-interest-formula)
  - [EMI Calculation Formula](#emi-calculation-formula)
  - [Sample Calculations](#sample-calculations)
- [Troubleshooting](#troubleshooting)
  - [Application Won't Start](#application-wont-start)
  - [Calculation Errors](#calculation-errors)
  - [Display Issues](#display-issues)
  - [Performance Problems](#performance-problems)
- [Glossary](#glossary)
  - [Financial Terms](#financial-terms)
  - [Technical Terms](#technical-terms)
- [Appendix](#appendix)
  - [Version History](#version-history)
  - [Legal Information](#legal-information)

## Introduction

### Purpose

The Compound Interest Calculator is a desktop application designed to streamline the loan processing workflow by providing accurate Equated Monthly Installment (EMI) calculations. This tool enables you, as a banking professional, to quickly calculate and provide loan payment information to customers based on the principal amount and loan duration.

Key benefits include:
- Rapid EMI calculations with guaranteed accuracy
- Elimination of manual calculation errors
- Consistent results across all bank branches
- Enhanced customer service through faster response times
- Simplified loan consultation process

### Intended Audience

This application is specifically designed for:
- Banking division staff who process loan applications
- Financial advisors providing loan information to customers
- Branch officers calculating payment schedules for various loan products
- Loan officers needing to quickly generate EMI figures during customer meetings

No specialized technical knowledge is required to use this calculator.

### System Requirements

The Compound Interest Calculator has minimal system requirements:

| Component | Minimum Requirement |
|-----------|---------------------|
| Operating System | Windows 10/11, macOS 10.14+, or Linux with GUI |
| Processor | 1 GHz or faster |
| Memory | 512 MB RAM (1 GB recommended) |
| Disk Space | 100 MB free space |
| Display | 1024×768 resolution or higher |
| Additional Software | Java Runtime Environment (JRE) 11 or higher |

## Getting Started

### Installation

#### Windows Installation

1. Locate the installation file `CompoundInterestCalculator-Setup.msi` in your download folder
2. Double-click the file to start the installation wizard
3. Click "Next" to proceed through the welcome screen
4. Read and accept the license agreement
5. Choose the installation location (default is recommended) and click "Next"
6. Click "Install" to begin the installation
7. When prompted by Windows security, click "Yes" to allow the installation
8. Click "Finish" once the installation is complete

![Windows Installation Wizard](images/windows-installation.png)

#### macOS Installation

1. Open the downloaded `CompoundInterestCalculator.dmg` file
2. Drag the Compound Interest Calculator icon to the Applications folder
3. If you see a security warning when first launching the application, right-click (or Control+click) on the application and select "Open"
4. Click "Open" in the confirmation dialog

![macOS Installation](images/macos-installation.png)

#### Linux Installation

**Debian/Ubuntu:**
```
sudo dpkg -i compound-interest-calculator_1.0.0_amd64.deb
```

**Red Hat/CentOS:**
```
sudo rpm -i compound-interest-calculator-1.0.0.x86_64.rpm
```

### Launching the Application

#### Windows
- Double-click the "Compound Interest Calculator" icon on your desktop, or
- Find and click "Compound Interest Calculator" in the Start menu

#### macOS
- Open the Applications folder and double-click "Compound Interest Calculator", or
- Click "Compound Interest Calculator" in the Launchpad

#### Linux
- Find "Compound Interest Calculator" in your applications menu, or
- Run `compound-interest-calculator` in the terminal

### Application Interface Overview

Upon launching the application, you will see the main calculator window:

![Main Application Window](images/main-window.png)

The interface consists of the following components:

1. **Title Bar** - Displays the application name
2. **Input Section** - Contains fields for entering loan information:
   - Principal Amount (USD) field
   - Loan Duration (Years) field
3. **Action Section** - Contains the Calculate EMI button
4. **Results Section** - Displays the calculated monthly EMI amount
5. **Details Section** - Can be expanded to show additional loan information
6. **New Calculation Button** - Clears the form for a new calculation

## Using the Calculator

### Entering Principal Amount

The Principal Amount is the initial loan amount in US Dollars (USD).

To enter the principal amount:
1. Click on the "Principal Amount (USD)" field
2. Type the loan amount (for example, 25000.00)

**Validation Rules:**
- Must be a positive number
- Can include up to 2 decimal places
- Does not require the dollar sign ($) as it will be automatically formatted

![Principal Amount Entry](images/principal-entry.png)

> **Note:** If you enter an invalid amount, an error message will appear when you attempt to calculate.

### Entering Loan Duration

The Loan Duration is the period (in years) over which the loan will be repaid.

To enter the loan duration:
1. Click on the "Loan Duration (Years)" field
2. Type the number of years (for example, 5)

**Validation Rules:**
- Must be a positive whole number
- Decimal values are not accepted (use whole years only)

![Loan Duration Entry](images/duration-entry.png)

> **Note:** The calculator uses a fixed annual interest rate of 7.5% for all calculations.

### Calculating EMI

Once you have entered both the Principal Amount and Loan Duration:

1. Click the "Calculate EMI" button
2. The system will validate your inputs
3. If valid, the monthly EMI amount will display in the Results section
4. If any input is invalid, an error message will appear indicating the issue

![Calculate EMI Button](images/calculate-button.png)

You can also press the Enter key after entering values to trigger the calculation.

### Viewing Detailed Results

After calculating the EMI, you can view detailed information about the loan:

1. Click the "Show" button in the Results section
2. The Results section will expand to show additional information:
   - Principal Amount
   - Total Interest
   - Total Amount Payable
   - Monthly Installment
   - Number of Installments
   - Annual Interest Rate

![Detailed Results View](images/detailed-results.png)

To hide the detailed view, click the "Hide" button.

### Starting a New Calculation

To perform another calculation:

1. Click the "New Calculation" button at the bottom of the window
2. All input fields will be cleared
3. The results section will be reset
4. You can now enter new values

![New Calculation Button](images/new-calculation.png)

Alternatively, you can press Ctrl+N on your keyboard to start a new calculation.

## Understanding the Results

### Monthly EMI

The Monthly EMI (Equated Monthly Installment) is the fixed amount that the borrower will need to pay each month until the loan is fully repaid.

For example, if the calculated EMI is $483.65, the borrower will pay this exact amount every month for the entire loan duration.

![Monthly EMI Result](images/monthly-emi.png)

### Total Interest

The Total Interest is the total amount of interest that will be paid over the entire loan period.

For example, for a $25,000 loan for 5 years at 7.5% interest rate, the total interest would be $4,019.00.

This figure helps customers understand the actual cost of borrowing beyond the principal amount.

### Total Amount Payable

The Total Amount Payable is the sum of the principal amount and the total interest.

For example, if the principal is $25,000 and the total interest is $4,019.00, the total amount payable would be $29,019.00.

This represents the complete financial commitment of the loan, showing customers the full amount they will pay by the end of the loan term.

### Interest Rate Information

The application uses a fixed annual interest rate of 7.5% for all calculations. This is displayed in the detailed results view and is used as the basis for all EMI and interest calculations.

The interest is compounded monthly, meaning that interest is calculated on a monthly basis on the remaining principal amount.

## Help and Support

### Using the Help Feature

The application includes context-sensitive help for each input field:

1. Click the question mark icon [?] next to any field to view help about that specific input
2. A help tooltip will appear with information about the expected values and format
3. Click anywhere outside the tooltip to close it

![Help Tooltip](images/help-tooltip.png)

### Understanding Error Messages

Common error messages and their solutions:

| Error Message | Meaning | Solution |
|---------------|---------|----------|
| "Principal amount must be a positive number" | The principal amount entered is zero, negative, or non-numeric | Enter a positive number with up to 2 decimal places |
| "Loan duration must be a positive whole number" | The duration entered is zero, negative, non-numeric, or contains decimals | Enter a positive integer (whole number) |
| "An error occurred during calculation" | The application encountered an unexpected error during calculation | Try restarting the application or contact technical support |

![Error Message Example](images/error-message.png)

### Keyboard Shortcuts

For more efficient use of the application, you can use the following keyboard shortcuts:

| Shortcut | Action |
|----------|--------|
| Enter | Calculate EMI (when focus is in any input field) |
| Ctrl+N | New Calculation (clear form) |
| Tab | Navigate between input fields |
| Alt+P | Focus Principal Amount field |
| Alt+D | Focus Duration field |
| Alt+C | Click Calculate button |
| F1 | Show Help |

### Getting Technical Support

If you encounter any issues not covered in this guide:

1. Contact the IT Help Desk at extension 1234 or email [helpdesk@bankname.com](mailto:helpdesk@bankname.com)
2. Provide the following information:
   - Your name and branch location
   - Application version (found in Help > About)
   - Detailed description of the issue
   - Any error messages displayed
   - Steps to reproduce the issue

Support hours: Monday to Friday, 9:00 AM to 5:00 PM EST

## Calculation Formulas

### Compound Interest Formula

The application uses the standard compound interest formula:

**A = P(1 + r/n)^(nt)**

Where:
- A = Final amount (principal + interest)
- P = Principal amount
- r = Annual interest rate (in decimal form)
- n = Number of times interest is compounded per year (12 for monthly)
- t = Time in years

For the bank's calculations, interest is compounded monthly (n=12) at an annual rate of 7.5% (r=0.075).

### EMI Calculation Formula

The EMI is calculated using the following formula:

**EMI = [P × r × (1 + r)^n]/[(1 + r)^n - 1]**

Where:
- P = Principal loan amount
- r = Monthly interest rate (annual rate ÷ 12 ÷ 100)
- n = Number of monthly installments (loan duration in years × 12)

For example, with an annual interest rate of 7.5%:
- Monthly interest rate (r) = 7.5% ÷ 12 ÷ 100 = 0.00625

### Sample Calculations

**Example 1:**
- Principal Amount: $10,000
- Loan Duration: 5 years (60 months)
- Annual Interest Rate: 7.5%

**Calculation Steps:**
1. Monthly interest rate = 7.5% ÷ 12 ÷ 100 = 0.00625
2. Number of installments = 5 years × 12 = 60 months
3. EMI = [$10,000 × 0.00625 × (1 + 0.00625)^60] ÷ [(1 + 0.00625)^60 - 1]
4. EMI = $200.38

**Example 2:**
- Principal Amount: $25,000
- Loan Duration: 3 years (36 months)
- Annual Interest Rate: 7.5%

**Calculation Steps:**
1. Monthly interest rate = 7.5% ÷ 12 ÷ 100 = 0.00625
2. Number of installments = 3 years × 12 = 36 months
3. EMI = [$25,000 × 0.00625 × (1 + 0.00625)^36] ÷ [(1 + 0.00625)^36 - 1]
4. EMI = $777.23

## Troubleshooting

### Application Won't Start

If the application fails to start:

1. **Check Java Installation**
   - Ensure Java Runtime Environment (JRE) 11 or higher is installed
   - Open a command prompt or terminal and type: `java -version`
   - If Java is not installed or the version is below 11, download and install the latest JRE from [java.com](https://java.com)

2. **Verify Installation**
   - For Windows, try reinstalling the application
   - For macOS, check if the application is in the quarantine by running: `xattr -d com.apple.quarantine /Applications/CompoundInterestCalculator.app`
   - For Linux, check application permissions with: `ls -la /usr/bin/compound-interest-calculator`

3. **Check System Resources**
   - Ensure your computer meets the minimum system requirements
   - Close other applications to free up memory

If the issue persists, contact technical support.

### Calculation Errors

If you receive incorrect calculation results or errors during calculation:

1. **Verify Inputs**
   - Ensure all input values are within acceptable ranges
   - Check for typos in principal amount or loan duration

2. **Application Version**
   - Check that you're using the latest version of the application
   - The current version can be found in Help > About

3. **Calculation Limits**
   - Very large principal amounts (over $10 million) may cause precision issues
   - Loan durations are limited to 30 years maximum

4. **Restart the Application**
   - Close and restart the application to reset the calculation engine

### Display Issues

If the application interface doesn't display correctly:

1. **Screen Resolution**
   - Ensure your display resolution meets the minimum requirement (1024×768)
   - Try adjusting your display scaling settings

2. **Font Scaling**
   - If text appears too small, adjust system font size in your operating system settings
   - Windows: Settings > Ease of Access > Display > Make text bigger
   - macOS: System Preferences > Displays > Scaled

3. **Window Size**
   - If the window appears cut off, try resizing it by dragging the corners
   - You can also maximize the window for optimal viewing

4. **Graphics Drivers**
   - Ensure your graphics drivers are up to date

### Performance Problems

If the application feels slow or unresponsive:

1. **Check System Resources**
   - Close other applications to free up memory
   - Check for resource-intensive processes in Task Manager (Windows), Activity Monitor (macOS), or System Monitor (Linux)

2. **Application Restart**
   - Close and restart the application to clear any memory issues

3. **System Restart**
   - Restart your computer to free up system resources

4. **Temporary Files**
   - Clearing temporary files might help:
     - Windows: `%TEMP%` folder
     - macOS: `/tmp` folder
     - Linux: `/tmp` folder

## Glossary

### Financial Terms

| Term | Definition |
|------|------------|
| EMI | Equated Monthly Installment; a fixed payment amount made by a borrower to a lender at a specified date each month. |
| Principal Amount | The original sum of money borrowed in a loan, or put into an investment. |
| Interest Rate | The amount charged by a lender to a borrower for the use of assets, expressed as a percentage of the principal. |
| Compound Interest | Interest calculated on the initial principal and also on the accumulated interest of previous periods. |
| Loan Duration | The period over which a loan is to be repaid, typically expressed in years or months. |
| Amortization | The process of spreading out a loan into a series of fixed payments over time. |
| Total Interest | The total amount of interest paid over the entire loan period. |
| Total Amount Payable | The sum of the principal amount and the total interest. |

### Technical Terms

| Term | Definition |
|------|------------|
| JRE | Java Runtime Environment; software required to run Java applications. |
| GUI | Graphical User Interface; the visual components of the application that users interact with. |
| Validation | The process of checking that user inputs meet the required format and rules. |
| Desktop Application | Software that runs locally on a computer's operating system rather than in a web browser. |
| Input Field | A UI element where users can enter data. |
| Tooltip | A small pop-up element that provides helpful information when hovering over or clicking a UI element. |
| Dialog | A small window that appears to provide information or request input from the user. |

## Appendix

### Version History

| Version | Release Date | Changes |
|---------|--------------|---------|
| 1.0.0 | 2023-10-01 | Initial release of the Compound Interest Calculator |
| 1.0.1 | [Future] | [Future enhancement placeholder] |

### Legal Information

© 2023 Bank Name. All rights reserved.

This software is proprietary to Bank Name and is provided for the exclusive use of Bank Name employees. Unauthorized distribution, modification, or use is strictly prohibited.

The Compound Interest Calculator is provided "as is" without warranty of any kind, either express or implied, including but not limited to the implied warranties of merchantability and fitness for a particular purpose.

For questions regarding this software, please contact the Legal Department at [legal@bankname.com](mailto:legal@bankname.com).