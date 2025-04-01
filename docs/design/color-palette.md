# Color Palette Documentation

## 1. INTRODUCTION

This document defines the official color palette for the Compound Interest Calculator application. The color scheme has been carefully selected to create a professional, banking-appropriate interface that maintains high readability, clear visual hierarchy, and meets accessibility standards.

Consistent use of this color palette throughout the application ensures visual coherence, reinforces the application's identity, and helps users quickly understand the interface through consistent visual cues.

## 2. PRIMARY COLORS

The primary colors establish the application's visual identity and are used for key interactive elements, important information, and primary actions.

### 2.1 Primary Blue

| Color Name | Hex Value | RGB Value | Sample |
| ---------- | --------- | --------- | ------ |
| Primary Blue | #0066CC | rgb(0, 102, 204) | ![Primary Blue](../images/colors/primary-blue.png) |

**Usage Guidelines:**
- Primary action buttons (Calculate EMI)
- Interactive elements (links, selectable items)
- Focus indicators
- Primary icons

**Accessibility Note:** Provides sufficient contrast (4.5:1) against white backgrounds for text and interactive elements.

### 2.2 Dark Blue

| Color Name | Hex Value | RGB Value | Sample |
| ---------- | --------- | --------- | ------ |
| Dark Blue | #1A365D | rgb(26, 54, 93) | ![Dark Blue](../images/colors/dark-blue.png) |

**Usage Guidelines:**
- Application title
- Section headings
- Important text (calculation results)
- Primary data values

**Accessibility Note:** Provides excellent contrast (10:1) against white backgrounds for headings and important text.

### 2.3 Light Blue

| Color Name | Hex Value | RGB Value | Sample |
| ---------- | --------- | --------- | ------ |
| Light Blue | #4D94FF | rgb(77, 148, 255) | ![Light Blue](../images/colors/light-blue.png) |

**Usage Guidelines:**
- Highlights
- Secondary interactive elements
- Selection indicators
- Progress indicators

**Accessibility Note:** Should be used sparingly for large text or non-text elements due to lower contrast against white backgrounds.

### 2.4 Primary Blue Variants

| Color Name | Hex Value | RGB Value | Sample | Usage |
| ---------- | --------- | --------- | ------ | ----- |
| Primary Blue Hover | #0055AA | rgb(0, 85, 170) | ![Primary Blue Hover](../images/colors/primary-blue-hover.png) | Button hover states |
| Primary Blue Pressed | #004488 | rgb(0, 68, 136) | ![Primary Blue Pressed](../images/colors/primary-blue-pressed.png) | Button pressed states |
| Primary Blue Light | #E6F0FF | rgb(230, 240, 255) | ![Primary Blue Light](../images/colors/primary-blue-light.png) | Selected item backgrounds, information panels |

## 3. NEUTRAL COLORS

Neutral colors form the foundation of the interface, providing backgrounds, text colors, and subtle visual elements that support the primary colors without competing for attention.

### 3.1 Background Colors

| Color Name | Hex Value | RGB Value | Sample |
| ---------- | --------- | --------- | ------ |
| Background Light Gray | #F5F5F5 | rgb(245, 245, 245) | ![Background Light Gray](../images/colors/background-light-gray.png) |
| White | #FFFFFF | rgb(255, 255, 255) | ![White](../images/colors/white.png) |

**Usage Guidelines:**
- Application background: Light Gray (#F5F5F5)
- Input field backgrounds: White (#FFFFFF)
- Content containers: White (#FFFFFF)
- Dialog backgrounds: White (#FFFFFF)

### 3.2 Text Colors

| Color Name | Hex Value | RGB Value | Sample |
| ---------- | --------- | --------- | ------ |
| Dark Gray (Primary Text) | #333333 | rgb(51, 51, 51) | ![Dark Gray](../images/colors/dark-gray.png) |
| Medium Gray (Secondary Text) | #666666 | rgb(102, 102, 102) | ![Medium Gray](../images/colors/medium-gray.png) |
| Light Medium Gray (Tertiary Text) | #999999 | rgb(153, 153, 153) | ![Light Medium Gray](../images/colors/light-medium-gray.png) |

**Usage Guidelines:**
- Primary text: Dark Gray (#333333)
- Secondary text and labels: Medium Gray (#666666)
- Placeholder text and disabled text: Light Medium Gray (#999999)

**Accessibility Note:** All text colors provide sufficient contrast against their intended backgrounds according to WCAG AA standards.

### 3.3 Border and Separator Colors

| Color Name | Hex Value | RGB Value | Sample |
| ---------- | --------- | --------- | ------ |
| Border Gray | #DDDDDD | rgb(221, 221, 221) | ![Border Gray](../images/colors/border-gray.png) |
| Light Border Gray | #EEEEEE | rgb(238, 238, 238) | ![Light Border Gray](../images/colors/light-border-gray.png) |

**Usage Guidelines:**
- Standard borders: Border Gray (#DDDDDD)
- Subtle separators: Light Border Gray (#EEEEEE)
- Input field borders: Border Gray (#DDDDDD)
- Section dividers: Border Gray (#DDDDDD)

### 3.4 Interactive Element Colors

| Color Name | Hex Value | RGB Value | Sample |
| ---------- | --------- | --------- | ------ |
| Button Gray | #F0F0F0 | rgb(240, 240, 240) | ![Button Gray](../images/colors/button-gray.png) |
| Button Gray Hover | #E8E8E8 | rgb(232, 232, 232) | ![Button Gray Hover](../images/colors/button-gray-hover.png) |
| Button Gray Pressed | #DDDDDD | rgb(221, 221, 221) | ![Button Gray Pressed](../images/colors/button-gray-pressed.png) |

**Usage Guidelines:**
- Secondary buttons: Button Gray (#F0F0F0)
- Secondary button hover: Button Gray Hover (#E8E8E8)
- Secondary button pressed: Button Gray Pressed (#DDDDDD)

## 4. FEEDBACK COLORS

Feedback colors communicate status, errors, warnings, and success states to users. These colors have specific meanings and should be used consistently throughout the application.

### 4.1 Error Colors

| Color Name | Hex Value | RGB Value | Sample |
| ---------- | --------- | --------- | ------ |
| Error Red | #CC0000 | rgb(204, 0, 0) | ![Error Red](../images/colors/error-red.png) |
| Error Red Light | #FFEEEE | rgb(255, 238, 238) | ![Error Red Light](../images/colors/error-red-light.png) |

**Usage Guidelines:**
- Error messages: Error Red (#CC0000)
- Error icons: Error Red (#CC0000)
- Error field borders: Error Red (#CC0000)
- Error backgrounds: Error Red Light (#FFEEEE)

**Accessibility Note:** Error states should always be indicated by both color and another visual cue (icon, text, border) to ensure accessibility for users with color vision deficiencies.

### 4.2 Success Colors

| Color Name | Hex Value | RGB Value | Sample |
| ---------- | --------- | --------- | ------ |
| Success Green | #008800 | rgb(0, 136, 0) | ![Success Green](../images/colors/success-green.png) |
| Success Green Light | #EEFFEE | rgb(238, 255, 238) | ![Success Green Light](../images/colors/success-green-light.png) |

**Usage Guidelines:**
- Success messages: Success Green (#008800)
- Success icons: Success Green (#008800)
- Success indicators: Success Green (#008800)
- Success backgrounds: Success Green Light (#EEFFEE)

### 4.3 Warning Colors

| Color Name | Hex Value | RGB Value | Sample |
| ---------- | --------- | --------- | ------ |
| Warning Orange | #FF8800 | rgb(255, 136, 0) | ![Warning Orange](../images/colors/warning-orange.png) |
| Warning Orange Light | #FFFFEE | rgb(255, 255, 238) | ![Warning Orange Light](../images/colors/warning-orange-light.png) |

**Usage Guidelines:**
- Warning messages: Warning Orange (#FF8800)
- Warning icons: Warning Orange (#FF8800)
- Warning indicators: Warning Orange (#FF8800)
- Warning backgrounds: Warning Orange Light (#FFFFEE)

## 5. ACCESSIBILITY CONSIDERATIONS

The color palette has been designed with accessibility in mind to ensure the application is usable by people with various visual abilities, including those with color vision deficiencies.

### 5.1 Contrast Ratios

All text colors in the application meet or exceed the WCAG 2.1 AA standards for contrast:

| Text Type | Minimum Contrast Ratio | Our Implementation |
| --------- | ---------------------- | ----------------- |
| Normal Text (< 18pt) | 4.5:1 | Dark Gray on White: 12.6:1 |
| | | Dark Gray on Light Gray: 10.1:1 |
| | | Medium Gray on White: 5.7:1 |
| | | Primary Blue on White: 4.6:1 |
| Large Text (≥ 18pt) | 3:1 | All large text exceeds 4.5:1 |

Interactive elements (buttons, links, form controls) maintain a minimum contrast ratio of 3:1 against adjacent colors to ensure they are distinguishable.

### 5.2 Color Independence

Color is never used as the only visual means of conveying information. Additional cues are always provided:

- Error states use both red color and error icons/messages
- Required fields are indicated by both color and an asterisk (*)
- Interactive elements have visual cues beyond color (underlines, borders, icons)
- Focus states have visible outlines or background changes

This ensures that users with color vision deficiencies can still use the application effectively.

### 5.3 High Contrast Mode

The application supports operating system high contrast modes by:

- Using system colors when high contrast mode is detected
- Maintaining clear borders and focus indicators
- Ensuring text remains readable
- Not relying on background images for critical information

This allows users who need high contrast settings to use the application comfortably.

## 6. IMPLEMENTATION GUIDELINES

To ensure consistent application of the color palette across the application, follow these implementation guidelines:

### 6.1 CSS Variables

All colors should be implemented as CSS variables in the root stylesheet:

```css
:root {
    /* Primary Colors */
    --color-primary-blue: #0066CC;
    --color-primary-blue-hover: #0055AA;
    --color-primary-blue-pressed: #004488;
    --color-primary-blue-light: #E6F0FF;
    --color-dark-blue: #1A365D;
    --color-light-blue: #4D94FF;
    
    /* Neutral Colors */
    --color-background: #F5F5F5;
    --color-white: #FFFFFF;
    --color-text-primary: #333333;
    --color-text-secondary: #666666;
    --color-text-tertiary: #999999;
    --color-border: #DDDDDD;
    --color-border-light: #EEEEEE;
    --color-button-gray: #F0F0F0;
    --color-button-gray-hover: #E8E8E8;
    --color-button-gray-pressed: #DDDDDD;
    
    /* Feedback Colors */
    --color-error: #CC0000;
    --color-error-light: #FFEEEE;
    --color-success: #008800;
    --color-success-light: #EEFFEE;
    --color-warning: #FF8800;
    --color-warning-light: #FFFFEE;
}
```

Always reference these variables instead of hardcoding color values to ensure consistency and make future updates easier.

### 6.2 JavaFX Implementation

For JavaFX implementation, define color constants in a central location:

```java
public class ColorConstants {
    // Primary Colors
    public static final Color PRIMARY_BLUE = Color.web("#0066CC");
    public static final Color PRIMARY_BLUE_HOVER = Color.web("#0055AA");
    public static final Color PRIMARY_BLUE_PRESSED = Color.web("#004488");
    public static final Color PRIMARY_BLUE_LIGHT = Color.web("#E6F0FF");
    public static final Color DARK_BLUE = Color.web("#1A365D");
    public static final Color LIGHT_BLUE = Color.web("#4D94FF");
    
    // Neutral Colors
    public static final Color BACKGROUND = Color.web("#F5F5F5");
    public static final Color WHITE = Color.web("#FFFFFF");
    public static final Color TEXT_PRIMARY = Color.web("#333333");
    public static final Color TEXT_SECONDARY = Color.web("#666666");
    public static final Color TEXT_TERTIARY = Color.web("#999999");
    public static final Color BORDER = Color.web("#DDDDDD");
    public static final Color BORDER_LIGHT = Color.web("#EEEEEE");
    public static final Color BUTTON_GRAY = Color.web("#F0F0F0");
    public static final Color BUTTON_GRAY_HOVER = Color.web("#E8E8E8");
    public static final Color BUTTON_GRAY_PRESSED = Color.web("#DDDDDD");
    
    // Feedback Colors
    public static final Color ERROR = Color.web("#CC0000");
    public static final Color ERROR_LIGHT = Color.web("#FFEEEE");
    public static final Color SUCCESS = Color.web("#008800");
    public static final Color SUCCESS_LIGHT = Color.web("#EEFFEE");
    public static final Color WARNING = Color.web("#FF8800");
    public static final Color WARNING_LIGHT = Color.web("#FFFFEE");
}
```

### 6.3 Color Usage Mapping

| UI Element | Color Variable |
| ---------- | ------------- |
| Application background | --color-background |
| Content containers | --color-white |
| Primary text | --color-text-primary |
| Secondary text | --color-text-secondary |
| Field labels | --color-text-primary |
| Primary buttons | --color-primary-blue |
| Primary button text | --color-white |
| Secondary buttons | --color-button-gray |
| Secondary button text | --color-text-primary |
| Input field borders | --color-border |
| Section separators | --color-border-light |
| Error messages | --color-error |
| Success messages | --color-success |
| Warning messages | --color-warning |
| Headings | --color-dark-blue |
| Links | --color-primary-blue |
| Focus indicators | --color-primary-blue |

## 7. COLOR PALETTE EVOLUTION

The color palette is a living standard that may evolve over time. Changes to the color palette should be carefully considered and documented.

### 7.1 Change Process

1. Proposed color changes must be evaluated for:
   - Accessibility impact (contrast ratios)
   - Visual coherence with existing palette
   - Backward compatibility
   - Implementation effort

2. All changes must be approved by the design team and product owner

3. Changes should be documented in this document with:
   - Previous color values
   - New color values
   - Rationale for change
   - Date of implementation

### 7.2 Version History

| Version | Date | Changes | Rationale |
| ------- | ---- | ------- | --------- |
| 1.0 | 2023-06-01 | Initial color palette | Established baseline palette for application |
| 1.1 | 2023-08-15 | Added light variants for feedback colors | Improved visual feedback for error, warning, and success states |

## 8. APPENDIX

Additional resources and references related to the color palette.

### 8.1 Color Accessibility Tools

The following tools were used to verify the accessibility of the color palette:

- [WebAIM Contrast Checker](https://webaim.org/resources/contrastchecker/)
- [Colour Contrast Analyser](https://www.tpgi.com/color-contrast-checker/)
- [Adobe Color Accessibility Tools](https://color.adobe.com/create/color-accessibility)
- [Stark Contrast Checker](https://www.getstark.co/)

### 8.2 References

- [WCAG 2.1 Color Contrast Guidelines](https://www.w3.org/WAI/WCAG21/Understanding/contrast-minimum.html)
- [Material Design Color System](https://material.io/design/color/the-color-system.html)
- [IBM Carbon Design System Colors](https://www.carbondesignsystem.com/guidelines/color/overview/)

### 8.3 Color Palette Files

The following files contain the complete color palette in various formats:

- Adobe Swatch Exchange (ASE): [compound-calculator-palette.ase](../resources/compound-calculator-palette.ase)
- Sketch Palette: [compound-calculator-palette.sketchpalette](../resources/compound-calculator-palette.sketchpalette)
- CSS Variables: [colors.css](../resources/colors.css)