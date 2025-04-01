# 1. INTRODUCTION

This document provides comprehensive design specifications for the Compound Interest Calculator user interface. The UI is designed to be clean, intuitive, and accessible, enabling banking professionals to quickly calculate Equated Monthly Installments (EMI) based on principal amount and loan duration.

The design follows established banking industry standards for financial applications while prioritizing usability, clarity, and efficiency. All UI components are implemented using JavaFX to ensure a consistent, professional appearance across different operating systems.

## 1.1 Purpose and Scope

This document serves as the definitive reference for the UI design of the Compound Interest Calculator application. It covers:

- Design principles and guidelines
- UI component specifications
- Layout and composition
- Interaction patterns
- Visual styling
- Accessibility considerations
- Responsive design approach

This document is intended for developers, designers, and testers working on the application to ensure consistent implementation of the UI design.

## 1.2 Target Users

The primary users of this application are banking division staff who process loans and provide financial advice to customers. These users have the following characteristics:

- Financial professionals with varying levels of technical expertise
- Regular users of banking software and financial calculators
- Need to quickly provide accurate loan information to customers
- May work in different lighting conditions and environments
- May have varying levels of visual acuity and accessibility needs

The UI design accommodates these user characteristics by providing a clear, efficient interface that minimizes cognitive load and maximizes accuracy.

# 2. DESIGN PRINCIPLES

The Compound Interest Calculator UI adheres to the following core design principles:

## 2.1 Simplicity

- Minimal, focused interface with only essential elements
- Clear visual hierarchy that guides users through the calculation workflow
- Elimination of unnecessary decorative elements
- Straightforward, linear task flow from input to result
- Reduced cognitive load through thoughtful information architecture

## 2.2 Clarity

- Clear labels and instructions for all input fields
- Sufficient spacing between elements to reduce visual clutter
- Consistent and predictable placement of UI elements
- Prominent display of calculation results
- Explicit error messages that guide users toward resolution

## 2.3 Feedback

- Immediate validation feedback for user inputs
- Clear visual indication of calculation progress
- Prominent display of calculation results
- Explicit error states with actionable guidance
- Visual confirmation of user actions

## 2.4 Consistency

- Uniform styling of similar UI elements throughout the application
- Consistent spacing, alignment, and sizing
- Predictable behavior for interactive elements
- Standard financial formatting for currency values
- Adherence to platform UI conventions where appropriate

## 2.5 Accessibility

- High contrast between text and background colors
- Support for keyboard navigation
- Screen reader compatibility
- Scalable text and UI elements
- Multiple cues (not just color) to convey information
- Compliance with WCAG 2.1 AA standards

# 3. UI COMPONENTS LEGEND

The following legend defines the notation used in UI mockups throughout this document:

## 3.1 Borders and Containers

```
+-------+ Box/Container border
|       | Vertical border
+-------+ Horizontal border
```

## 3.2 Input Elements

```
[......] Text input field
[Button] Button
( ) Radio button
[ ] Checkbox
[v] Dropdown menu
```

## 3.3 Indicators and Icons

```
[$] Financial/Currency indicator
[!] Warning/Error indicator
[i] Information indicator
[?] Help indicator
[x] Close/Clear indicator
```

## 3.4 Status Elements

```
[=====>] Progress indicator
```

# 4. MAIN APPLICATION WINDOW

The main application window consists of three primary sections: Input Section, Action Section, and Results Section. The layout is designed to guide users through a natural flow from input to action to results.

## 4.1 Window Layout

```
+----------------------------------------------------------------------+
|                     COMPOUND INTEREST CALCULATOR                      |
+----------------------------------------------------------------------+
|                                                                      |
|  Principal Amount (USD): [$] [25000.00........................] [?]  |
|                                                                      |
|  Loan Duration (Years): [...5...........................] [?]        |
|                                                                      |
|                         [    Calculate EMI    ]                      |
|                                                                      |
+----------------------------------------------------------------------+
|  Results                                                             |
+----------------------------------------------------------------------+
|                                                                      |
|  Monthly EMI: $483.65                                                |
|                                                                      |
|  [i] Based on an annual interest rate of 7.5%                        |
|                                                                      |
+----------------------------------------------------------------------+
|                         [    New Calculation    ]                    |
+----------------------------------------------------------------------+
```

## 4.2 Window Properties

- **Title**: "Compound Interest Calculator"
- **Minimum Size**: 600px width × 400px height
- **Default Size**: 800px width × 600px height
- **Resizable**: Yes, with responsive layout
- **Icon**: Calculator icon (calculator-icon.png)
- **Background Color**: Light Gray (#F5F5F5)

# 5. INPUT SECTION

The Input Section collects the necessary parameters for EMI calculation: principal amount and loan duration.

## 5.1 Principal Amount Field

- **Label**: "Principal Amount (USD):"
- **Input Type**: Text field with currency indicator
- **Default Value**: Empty
- **Validation**: Positive number with up to 2 decimal places
- **Error Message**: "Principal amount must be a positive number"
- **Help Icon**: [?] - Shows tooltip with input requirements
- **Keyboard Shortcut**: Alt+P (focuses field)

## 5.2 Loan Duration Field

- **Label**: "Loan Duration (Years):"
- **Input Type**: Text field
- **Default Value**: Empty
- **Validation**: Positive integer
- **Error Message**: "Loan duration must be a positive whole number"
- **Help Icon**: [?] - Shows tooltip with input requirements
- **Keyboard Shortcut**: Alt+D (focuses field)

## 5.3 Input Validation States

**Error State - Invalid Principal Amount**:
```
+----------------------------------------------------------------------+
|                     COMPOUND INTEREST CALCULATOR                      |
+----------------------------------------------------------------------+
|                                                                      |
|  Principal Amount (USD): [$] [-1000...........................] [?]  |
|  [!] Principal amount must be a positive number                      |
|                                                                      |
|  Loan Duration (Years): [...5...........................] [?]        |
|                                                                      |
|                         [    Calculate EMI    ]                      |
|                                                                      |
+----------------------------------------------------------------------+
|  Results                                                             |
+----------------------------------------------------------------------+
|                                                                      |
|  Monthly EMI: --                                                     |
|                                                                      |
|  [i] Enter valid inputs to calculate EMI                             |
|                                                                      |
+----------------------------------------------------------------------+
|                         [    New Calculation    ]                    |
+----------------------------------------------------------------------+
```

**Error State - Invalid Loan Duration**:
```
+----------------------------------------------------------------------+
|                     COMPOUND INTEREST CALCULATOR                      |
+----------------------------------------------------------------------+
|                                                                      |
|  Principal Amount (USD): [$] [25000.00........................] [?]  |
|                                                                      |
|  Loan Duration (Years): [..0............................] [?]        |
|  [!] Loan duration must be a positive whole number                   |
|                                                                      |
|                         [    Calculate EMI    ]                      |
|                                                                      |
+----------------------------------------------------------------------+
|  Results                                                             |
+----------------------------------------------------------------------+
|                                                                      |
|  Monthly EMI: --                                                     |
|                                                                      |
|  [i] Enter valid inputs to calculate EMI                             |
|                                                                      |
+----------------------------------------------------------------------+
|                         [    New Calculation    ]                    |
+----------------------------------------------------------------------+
```

## 5.4 Help Tooltips

**Principal Amount Help**:
```
+----------------------------------------------------------------------+
|                     COMPOUND INTEREST CALCULATOR                      |
+----------------------------------------------------------------------+
|                                                                      |
|  Principal Amount (USD): [$] [25000.00........................] [?]  |
|  +--------------------------------------------------+                |
|  | Enter the loan amount in USD.                    |                |
|  | - Must be a positive number                      |                |
|  | - Can include up to 2 decimal places             |                |
|  | - Example: 25000.00                              |                |
|  +--------------------------------------------------+                |
|                                                                      |
|  Loan Duration (Years): [...5...........................] [?]        |
|                                                                      |
|                         [    Calculate EMI    ]                      |
|                                                                      |
+----------------------------------------------------------------------+
|  Results                                                             |
+----------------------------------------------------------------------+
|                                                                      |
|  Monthly EMI: --                                                     |
|                                                                      |
|  [i] Enter valid inputs to calculate EMI                             |
|                                                                      |
+----------------------------------------------------------------------+
|                         [    New Calculation    ]                    |
+----------------------------------------------------------------------+
```

**Loan Duration Help**:
```
+----------------------------------------------------------------------+
|                     COMPOUND INTEREST CALCULATOR                      |
+----------------------------------------------------------------------+
|                                                                      |
|  Principal Amount (USD): [$] [25000.00........................] [?]  |
|                                                                      |
|  Loan Duration (Years): [...5...........................] [?]        |
|  +--------------------------------------------------+                |
|  | Enter the loan duration in years.               |                |
|  | - Must be a positive whole number               |                |
|  | - Maximum value: 30 years                       |                |
|  | - Example: 5                                    |                |
|  +--------------------------------------------------+                |
|                                                                      |
|                         [    Calculate EMI    ]                      |
|                                                                      |
+----------------------------------------------------------------------+
|  Results                                                             |
+----------------------------------------------------------------------+
|                                                                      |
|  Monthly EMI: --                                                     |
|                                                                      |
|  [i] Enter valid inputs to calculate EMI                             |
|                                                                      |
+----------------------------------------------------------------------+
|                         [    New Calculation    ]                    |
+----------------------------------------------------------------------+
```

# 6. ACTION SECTION

The Action Section contains buttons that trigger the main operations of the application.

## 6.1 Calculate EMI Button

- **Label**: "Calculate EMI"
- **Type**: Primary button
- **State Rules**: Enabled when both input fields have content, disabled otherwise
- **Action**: Triggers validation and calculation process
- **Visual Style**: Blue background (#0066CC) with white text
- **Hover State**: Darker blue (#0055AA)
- **Pressed State**: Darkest blue (#004488)
- **Disabled State**: Gray (#CCCCCC) with darker gray text
- **Keyboard Shortcut**: Alt+C or Enter (when focus is in any input field)

## 6.2 New Calculation Button

- **Label**: "New Calculation"
- **Type**: Secondary button
- **State Rules**: Always enabled
- **Action**: Clears all input fields and results
- **Visual Style**: Light gray background (#F0F0F0) with dark text
- **Hover State**: Slightly darker gray (#E8E8E8)
- **Pressed State**: Darker gray (#DDDDDD)
- **Keyboard Shortcut**: Ctrl+N

## 6.3 Calculation In Progress State

```
+----------------------------------------------------------------------+
|                     COMPOUND INTEREST CALCULATOR                      |
+----------------------------------------------------------------------+
|                                                                      |
|  Principal Amount (USD): [$] [25000.00........................] [?]  |
|                                                                      |
|  Loan Duration (Years): [...5...........................] [?]        |
|                                                                      |
|                         [    Calculating...    ]                     |
|                                                                      |
+----------------------------------------------------------------------+
|  Results                                                             |
+----------------------------------------------------------------------+
|                                                                      |
|  [=======>                                                  ]        |
|                                                                      |
|  [i] Calculating EMI, please wait...                                 |
|                                                                      |
+----------------------------------------------------------------------+
|                         [    New Calculation    ]                    |
+----------------------------------------------------------------------+
```

# 7. RESULTS SECTION

The Results Section displays the calculated EMI and provides additional information about the calculation.

## 7.1 Basic Results Display

- **Label**: "Monthly EMI:"
- **Value Format**: Currency (USD) with 2 decimal places
- **Default Value**: "--" (before calculation)
- **Font Style**: Bold, larger than standard text
- **Color**: Dark Blue (#1A365D)
- **Information Text**: "Based on an annual interest rate of 7.5%"
- **Waiting Message**: "Enter valid inputs to calculate EMI"

## 7.2 Detailed Results View

```
+----------------------------------------------------------------------+
|                     COMPOUND INTEREST CALCULATOR                      |
+----------------------------------------------------------------------+
|                                                                      |
|  Principal Amount (USD): [$] [25000.00........................] [?]  |
|                                                                      |
|  Loan Duration (Years): [...5...........................] [?]        |
|                                                                      |
|                         [    Calculate EMI    ]                      |
|                                                                      |
+----------------------------------------------------------------------+
|  Results                                                   [+] Show  |
+----------------------------------------------------------------------+
|                                                                      |
|  Monthly EMI: $483.65                                                |
|                                                                      |
|  Loan Summary:                                                       |
|  +--------------------------------------------------+                |
|  | Principal Amount:         $25,000.00             |                |
|  | Total Interest:           $4,019.00              |                |
|  | Total Amount Payable:     $29,019.00             |                |
|  | Monthly Installment:      $483.65                |                |
|  | Number of Installments:   60                     |                |
|  | Annual Interest Rate:     7.5%                   |                |
|  +--------------------------------------------------+                |
|                                                                      |
+----------------------------------------------------------------------+
|                         [    New Calculation    ]                    |
+----------------------------------------------------------------------+
```

## 7.3 Error States

**Calculation Error**:
- Display error dialog with title "Calculation Error"
- Error message: "An error occurred during calculation. Please try again."
- OK button to dismiss dialog
- Focus returns to input fields after dismissal

**System Error**:
- Display error dialog with title "System Error"
- Error message: "A system error occurred. Please restart the application."
- OK button to dismiss dialog

# 8. RESPONSIVE LAYOUTS

The application UI adapts to different window sizes to maintain usability across various display configurations.

## 8.1 Compact Window Layout

For window widths below 800px:

```
+------------------------------------------+
|        COMPOUND INTEREST CALCULATOR      |
+------------------------------------------+
|                                          |
|  Principal Amount (USD):                 |
|  [$] [25000.00..................] [?]    |
|                                          |
|  Loan Duration (Years):                  |
|  [...5.....................] [?]         |
|                                          |
|         [    Calculate EMI    ]          |
|                                          |
+------------------------------------------+
|  Results                                 |
+------------------------------------------+
|                                          |
|  Monthly EMI: $483.65                    |
|                                          |
|  [i] Based on 7.5% annual interest       |
|                                          |
+------------------------------------------+
|         [    New Calculation    ]        |
+------------------------------------------+
```

- Font sizes reduced by 1pt
- Field widths reduced proportionally
- Vertical layout with labels above fields
- Padding reduced to maintain spacing
- Detailed results view scrollable if needed

## 8.2 Responsive Design Rules

- All dimensions use relative units (%, em) rather than fixed pixels
- Text fields expand/contract to fill available space
- Minimum sizes enforced for all interactive elements
- Layout switches from horizontal to vertical at narrow widths
- Scrolling enabled for content that doesn't fit
- Touch targets maintain minimum 44×44px size for touch devices

# 9. INTERACTION FLOW

The application follows a simple, linear interaction flow from input to calculation to result.

## 9.1 Primary User Journey

1. User launches application
2. User enters principal amount
3. User enters loan duration
4. User clicks Calculate EMI button (or presses Enter)
5. System validates inputs
   - If invalid, display error messages
   - If valid, proceed to calculation
6. System calculates EMI
7. System displays EMI result
8. User reviews result
9. User can:
   - Start a new calculation (clears form)
   - Modify inputs and recalculate
   - Close the application

## 9.2 Input Validation Flow

1. User enters value in input field
2. When field loses focus or user presses Enter:
   - System validates input
   - If invalid, display error message below field
   - If valid, clear any previous error messages
3. Calculate button is enabled only when all inputs are valid
4. User can click help icon at any time to view input requirements

## 9.3 Error Handling Flow

1. Input Validation Errors:
   - Display specific error message below the relevant field
   - Highlight the field with error styling (red border)
   - Disable Calculate button until errors are corrected

2. Calculation Errors:
   - Display error dialog with specific message
   - Provide guidance on how to resolve the issue
   - Allow user to dismiss dialog and correct inputs

3. System Errors:
   - Display error dialog with generic message
   - Log detailed error information
   - Provide option to restart application if needed

## 9.4 Keyboard Navigation

1. Tab order follows logical flow:
   - Principal Amount field
   - Loan Duration field
   - Calculate EMI button
   - New Calculation button

2. Keyboard shortcuts:
   - Alt+P: Focus Principal Amount field
   - Alt+D: Focus Loan Duration field
   - Alt+C: Activate Calculate EMI button
   - Ctrl+N: Activate New Calculation button
   - Enter: Activate Calculate EMI button (when inputs have focus)
   - Esc: Clear focused field or dismiss dialog

# 10. UI COMPONENT SPECIFICATIONS

Detailed specifications for all UI components used in the application.

## 10.1 Input Fields

| Component | Type | Validation | Format | Default Value |
| --------- | ---- | ---------- | ------ | ------------- |
| Principal Amount | TextField | Positive number with up to 2 decimal places | Currency (USD) | Empty |
| Loan Duration | TextField | Positive integer | Whole number | Empty |

## 10.2 Buttons

| Component | Type | State Rules | Action |
| --------- | ---- | ----------- | ------ |
| Calculate EMI | Button | Enabled when both fields have content | Triggers validation and calculation |
| New Calculation | Button | Always enabled | Clears form and results |

## 10.3 Display Elements

| Component | Type | Format | Update Trigger |
| --------- | ---- | ------ | -------------- |
| Monthly EMI | Label | Currency (USD) with 2 decimal places | After successful calculation |
| Error Messages | Label | Red text with warning icon | After validation failure |
| Help Tooltips | Popup | Text with border | On help icon click |
| Loan Summary | Panel | Tabular data with currency formatting | On "Show" button click |

## 10.4 Icons

| Icon | Purpose | Size | Color | Interaction |
| ---- | ------- | ---- | ----- | ----------- |
| Help (?) | Provide field-specific help | 14pt | Primary Blue (#0066CC) | Clickable, shows help tooltip |
| Warning (!) | Indicate validation error | 14pt | Error Red (#CC0000) | Static, appears with error message |
| Info (i) | Provide additional information | 14pt | Medium Gray (#666666) | Static, appears with informational text |

# 11. STYLING GUIDELINES

Styling guidelines ensure visual consistency throughout the application.

## 11.1 Typography

| Element | Font | Size | Weight | Color | Alignment |
| ------- | ---- | ---- | ------ | ----- | --------- |
| Window Title | "Segoe UI", Arial, sans-serif | 16pt | Bold | Dark Blue (#1A365D) | Center |
| Field Labels | "Segoe UI", Arial, sans-serif | 12pt | Regular | Dark Gray (#333333) | Left |
| Input Fields | "Segoe UI", Arial, sans-serif | 12pt | Regular | Black (#000000) | Left |
| Buttons | "Segoe UI", Arial, sans-serif | 12pt | Bold | White (#FFFFFF) on Blue (#0066CC) | Center |
| Results | "Segoe UI", Arial, sans-serif | 14pt | Bold | Dark Blue (#1A365D) | Left |
| Error Messages | "Segoe UI", Arial, sans-serif | 11pt | Regular | Red (#CC0000) | Left |
| Information Text | "Segoe UI", Arial, sans-serif | 11pt | Italic | Gray (#666666) | Left |

## 11.2 Color Usage

The application uses a consistent color palette defined below. Key color usage includes:

- **Primary Blue (#0066CC)**: Used for primary buttons, interactive elements, and focus indicators
- **Dark Blue (#1A365D)**: Used for headings and important text like results
- **Dark Gray (#333333)**: Used for standard text and labels
- **Light Gray (#F5F5F5)**: Used for application background
- **White (#FFFFFF)**: Used for input field backgrounds and button text
- **Error Red (#CC0000)**: Used exclusively for error messages and indicators
- **Border Gray (#DDDDDD)**: Used for borders and separators

A comprehensive color scheme definition includes these colors plus additional shades and variants for different UI states and components.

## 11.3 Spacing and Layout

- **Container Padding**: 20px around all container edges
- **Element Spacing**: 15px between related elements
- **Section Spacing**: 25px between major sections
- **Field Label Width**: 150px (minimum) for horizontal layouts
- **Input Field Width**: 200px (minimum), expands with container
- **Button Padding**: 10px vertical, 20px horizontal
- **Button Spacing**: 10px between adjacent buttons
- **Error Message Spacing**: 3px below the related field

## 11.4 Visual Hierarchy

- **Primary Elements**: Largest size, boldest weight, most prominent color
- **Secondary Elements**: Medium size, regular weight, standard colors
- **Tertiary Elements**: Smallest size, lightest weight, subdued colors

The visual hierarchy guides users through the application flow:
1. Input fields (primary focus)
2. Action buttons (next step)
3. Results display (outcome)
4. Supporting information (context)

# 12. ACCESSIBILITY CONSIDERATIONS

The application is designed to be accessible to all users, including those with disabilities.

## 12.1 Keyboard Navigation

- All interactive elements are accessible via keyboard
- Tab order follows a logical flow through the interface
- Focus indicators are clearly visible
- Keyboard shortcuts are provided for common actions
- No keyboard traps or inaccessible elements

## 12.2 Screen Reader Support

- All UI elements have appropriate ARIA labels
- Images and icons have alt text
- Form fields have explicit labels
- Error messages are associated with their fields
- Dynamic content changes are announced
- Semantic HTML structure for proper navigation

## 12.3 Color and Contrast

- All text meets WCAG AA contrast requirements (4.5:1 for normal text, 3:1 for large text)
- Color is never used as the only means of conveying information
- Error states use both color and icons/text
- Focus states have visible indicators beyond color
- Application supports high contrast mode

## 12.4 Text and Scaling

- Text uses relative units (em, %) to support browser text scaling
- UI remains functional when text is enlarged up to 200%
- No fixed-width containers that could truncate enlarged text
- Sufficient line height and letter spacing for readability

## 12.5 Input Assistance

- Clear labels for all input fields
- Explicit instructions for expected input format
- Immediate validation feedback
- Helpful error messages that explain how to correct issues
- Context-sensitive help available for all inputs

# 13. IMPLEMENTATION NOTES

Technical guidance for implementing the UI design.

## 13.1 JavaFX Implementation

- Use JavaFX Scene Builder for layout design
- Implement MVC pattern with clear separation of UI and logic
- Use FXML for UI structure with separate controller classes
- Apply CSS for styling to maintain consistency
- Use property binding for real-time updates
- Implement validation with immediate feedback
- Ensure responsive layout that adapts to window resizing

## 13.2 CSS Structure

- Main stylesheet: application.css (global styles)
- Component-specific stylesheets:
  - input-section.css
  - result-section.css
  - action-section.css
- Use CSS variables for colors and common values
- Apply consistent naming conventions for CSS classes
- Include media queries for responsive design
- Maintain separation of structure and presentation

## 13.3 Component Architecture

- Main application class: CalculatorUI.java
- UI components:
  - InputSection.java
  - ActionSection.java
  - ResultSection.java
  - HelpDialog.java
- Supporting classes:
  - CurrencyFormatter.java
  - InputValidator.java
- Each component should be self-contained with clear interfaces
- Use dependency injection for service references
- Implement event-based communication between components

## 13.4 Testing Considerations

- Implement UI tests using TestFX
- Test all validation scenarios
- Verify keyboard navigation
- Test screen reader compatibility
- Validate responsive behavior at different sizes
- Test with system accessibility settings enabled
- Verify color contrast meets requirements
- Test with different system font sizes

# 14. APPENDICES

Additional reference information for UI implementation.

## 14.1 Keyboard Shortcuts

| Shortcut | Action |
| -------- | ------ |
| Enter | Calculate EMI (when focus is in any input field) |
| Ctrl+N | New Calculation (clear form) |
| Tab | Navigate between input fields |
| Alt+P | Focus Principal Amount field |
| Alt+D | Focus Loan Duration field |
| Alt+C | Click Calculate button |
| F1 | Show Help |
| Esc | Dismiss dialog or clear focused field |

## 14.2 Error Messages

| Error Scenario | Message |
| -------------- | ------- |
| Empty Principal | "Principal amount is required" |
| Negative Principal | "Principal amount must be a positive number" |
| Non-numeric Principal | "Principal must be a number with up to 2 decimal places" |
| Empty Duration | "Loan duration is required" |
| Negative/Zero Duration | "Loan duration must be a positive whole number" |
| Non-integer Duration | "Duration must be a whole number of years" |
| Calculation Error | "An error occurred during calculation. Please try again." |
| System Error | "System error. Please restart the application." |

## 14.3 Help Content

**Principal Amount Help**:
- Enter the loan amount in USD
- Must be a positive number
- Can include up to 2 decimal places
- Example: 25000.00

**Loan Duration Help**:
- Enter the loan duration in years
- Must be a positive whole number
- Maximum value: 30 years
- Example: 5

**Interest Rate Information**:
- The application uses a fixed annual interest rate of 7.5%
- Interest is compounded monthly
- The rate is used to calculate the EMI based on principal and duration

## 14.4 UI Component Hierarchy

```
CalculatorUI (main application class)
├── InputSection
│   ├── Principal Amount Field
│   ├── Principal Error Label
│   ├── Principal Help Icon
│   ├── Loan Duration Field
│   ├── Duration Error Label
│   └── Duration Help Icon
├── ActionSection
│   ├── Calculate EMI Button
│   └── New Calculation Button
├── ResultSection
│   ├── EMI Amount Label
│   ├── Info Label
│   └── Detailed Results Pane
│       ├── Principal Amount Label
│       ├── Total Interest Label
│       ├── Total Amount Label
│       ├── Monthly Installment Label
│       ├── Number of Installments Label
│       └── Annual Interest Rate Label
└── HelpDialog
    ├── Principal Tab
    ├── Duration Tab
    ├── Interest Tab
    └── Calculation Tab
```

## 14.5 Color Scheme Details

| Color Name | Hex Value | RGB Value | Purpose |
| ---------- | --------- | --------- | ------- |
| Primary Blue | #0066CC | rgb(0, 102, 204) | Primary buttons, interactive elements |
| Dark Blue | #1A365D | rgb(26, 54, 93) | Headings, important text |
| Light Blue | #4D94FF | rgb(77, 148, 255) | Hover states, highlights |
| Dark Gray | #333333 | rgb(51, 51, 51) | Standard text, labels |
| Medium Gray | #666666 | rgb(102, 102, 102) | Secondary text, disabled states |
| Light Gray | #F5F5F5 | rgb(245, 245, 245) | Backgrounds, containers |
| Border Gray | #DDDDDD | rgb(221, 221, 221) | Borders, separators |
| White | #FFFFFF | rgb(255, 255, 255) | Input backgrounds, button text |
| Error Red | #CC0000 | rgb(204, 0, 0) | Error messages, warnings |
| Success Green | #008800 | rgb(0, 136, 0) | Success messages, confirmations |