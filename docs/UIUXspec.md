# UI/UX Design Specification

## MindfulScroll — Digital Awareness Companion

---

### Document Control

| Field              | Value                                            |
| ------------------ | ------------------------------------------------ |
| **Document ID**    | DOC-006                                          |
| **Version**        | 1.0                                              |
| **Date**           | 2026-02-07                                       |
| **Status**         | Draft                                            |
| **Parent Docs**    | PRD.md, SRS.md, SAD.md, TDD.md, DbSchema.md     |
| **Audience**       | Developers, Designers, QA, Project Owner         |

---

## Table of Contents

1. [Overview](#1-overview)
2. [Design Principles](#2-design-principles)
3. [Design System](#3-design-system)
   - 3.1 [Color Palette](#31-color-palette)
   - 3.2 [Typography](#32-typography)
   - 3.3 [Spacing & Layout Grid](#33-spacing--layout-grid)
   - 3.4 [Elevation & Shapes](#34-elevation--shapes)
   - 3.5 [Iconography](#35-iconography)
4. [Screen Inventory](#4-screen-inventory)
5. [Navigation Architecture](#5-navigation-architecture)
6. [Onboarding Flow](#6-onboarding-flow)
   - 6.1 [Welcome Screen](#61-welcome-screen)
   - 6.2 [Permission: Usage Access](#62-permission-usage-access)
   - 6.3 [Permission: Display Over Other Apps](#63-permission-display-over-other-apps)
   - 6.4 [Permission: Battery Optimization](#64-permission-battery-optimization)
   - 6.5 [Onboarding Completion Screen](#65-onboarding-completion-screen)
7. [Main Screens](#7-main-screens)
   - 7.1 [Home Screen](#71-home-screen)
   - 7.2 [App Selection Screen](#72-app-selection-screen)
   - 7.3 [Threshold Configuration Screen](#73-threshold-configuration-screen)
   - 7.4 [About Screen](#74-about-screen)
8. [Floating Timer Overlay](#8-floating-timer-overlay)
   - 8.1 [Overlay Appearance](#81-overlay-appearance)
   - 8.2 [Visual States & Color Escalation](#82-visual-states--color-escalation)
   - 8.3 [Overlay Animations](#83-overlay-animations)
   - 8.4 [Drag & Edge-Snapping](#84-drag--edge-snapping)
   - 8.5 [Cooldown Behavior](#85-cooldown-behavior)
   - 8.6 [Throb Animation](#86-throb-animation)
9. [Notification Design](#9-notification-design)
10. [Permission Banner](#10-permission-banner)
11. [Animation Specification](#11-animation-specification)
12. [Accessibility](#12-accessibility)
13. [Responsive & Edge Cases](#13-responsive--edge-cases)
14. [Interaction Patterns](#14-interaction-patterns)
15. [Empty States & Loading](#15-empty-states--loading)
16. [Error States](#16-error-states)
17. [Traceability Matrix](#17-traceability-matrix)
18. [References](#18-references)

---

## 1. Overview

This document specifies the complete visual design, interaction patterns, and user experience for MindfulScroll MVP (v1.0). It translates the product principles from the PRD, the functional requirements from the SRS, and the architectural decisions from the SAD/TDD into concrete screen designs, component specifications, and animation definitions.

**Design Framework:** Material Design 3 (Material You) with Jetpack Compose.

**Architecture Pattern:** Single-Activity with Compose Navigation. All in-app screens are Compose composables. The floating timer overlay uses the Android View system (not Compose) due to `WindowManager` constraints (ADR-003).

**Key Design Goals:**

| Goal                        | Source       | How Addressed                                                   |
|-----------------------------|-------------|------------------------------------------------------------------|
| Minimal intrusion           | PRD P3      | Small overlay (48-56dp), subtle initial opacity, edge-snapping   |
| Awareness over restriction  | PRD P1      | Informational timer only, no blocking, no guilt messaging        |
| Respect user autonomy       | PRD P2      | All permissions skippable, all settings user-configurable        |
| Transparency                | PRD P4      | Plain-language permission explanations, privacy-forward About    |
| Graceful behavior           | PRD P6      | Smooth animations, no ghost overlays, silent error recovery      |

---

## 2. Design Principles

### 2.1 UX Principles

| # | Principle                  | Guideline                                                                       |
|---|----------------------------|---------------------------------------------------------------------------------|
| 1 | **Calm by default**        | The app's visual tone should be neutral and calming. No alarm colors, no warning language on first launch. |
| 2 | **Progressive disclosure** | Show essential information first. Details on demand (threshold config, about).   |
| 3 | **Minimal taps to value**  | User should reach active monitoring in under 90 seconds from first launch (NFR-009.AC3). |
| 4 | **No guilt, no shame**     | Never use language like "You've wasted X minutes." Timer is purely informational. |
| 5 | **Feedback on every action** | Toggle states, permission grants, app selections — all give immediate visual feedback. |
| 6 | **Safe to explore**        | Every action is reversible. Deselecting an app is one tap. Stopping monitoring is one tap. |

### 2.2 Visual Design Principles

| # | Principle                     | Guideline                                                          |
|---|-------------------------------|---------------------------------------------------------------------|
| 1 | **Clean & spacious**          | Generous padding, no visual clutter. Content breathes.              |
| 2 | **Consistent hierarchy**      | Primary actions are prominent. Secondary actions are subtle.        |
| 3 | **Motion with purpose**       | Every animation communicates state change. No decorative animation. |
| 4 | **Accessible by default**     | WCAG 2.1 AA contrast ratios. 48dp minimum touch targets.           |
| 5 | **Native feel**               | Follow Material Design 3 conventions. No custom paradigms.          |

---

## 3. Design System

### 3.1 Color Palette

#### 3.1.1 App Theme Colors (Material Design 3)

| Role                   | Light Mode           | Dark Mode            | Usage                              |
|------------------------|----------------------|----------------------|------------------------------------|
| **Primary**            | `#2563EB` (Blue 600) | `#60A5FA` (Blue 400) | Toggle, primary buttons, FAB       |
| **On Primary**         | `#FFFFFF`            | `#1E3A5F`            | Text/icons on primary              |
| **Primary Container**  | `#DBEAFE` (Blue 100) | `#1E3A5F` (Blue 900) | Cards, chips, selected states      |
| **Secondary**          | `#7C3AED` (Violet)   | `#A78BFA`            | Accent, threshold indicators       |
| **Surface**            | `#FFFFFF`            | `#1C1B1F`            | Screen backgrounds                 |
| **Surface Variant**    | `#F3F4F6`            | `#2D2D30`            | Card backgrounds, dividers         |
| **On Surface**         | `#1F2937` (Gray 800) | `#E5E7EB`            | Primary text                       |
| **On Surface Variant** | `#6B7280` (Gray 500) | `#9CA3AF`            | Secondary text, captions           |
| **Error**              | `#EF4444` (Red 500)  | `#F87171`            | Error states, missing permissions  |
| **Outline**            | `#D1D5DB` (Gray 300) | `#404040`            | Borders, dividers                  |

> **Note:** Dark mode support is recommended for v1.1. MVP launches with light mode only.

#### 3.1.2 Overlay Visual State Colors

These are the floating timer overlay colors, separate from the app theme. They use the View system (not Compose) and are defined as ARGB hex values.

| State    | Background         | Text Color         | Opacity | Hex (BG)    | Hex (Text)  |
|----------|--------------------|--------------------|---------|-------------|-------------|
| CALM     | Muted Gray         | White              | 65%     | `#6B7280`   | `#FFFFFF`   |
| NOTICE   | Warm Amber         | Dark               | 78%     | `#F59E0B`   | `#1F2937`   |
| ALERT    | Orange             | White              | 88%     | `#F97316`   | `#FFFFFF`   |
| URGENT   | Red / Coral        | White              | 95%     | `#EF4444`   | `#FFFFFF`   |
| THROB    | Pulsing Red        | White              | 100%    | `#EF4444`   | `#FFFFFF`   |

**Contrast Ratios (text on background):**

| State    | Ratio  | WCAG AA (≥3:1 for large text) | Pass? |
|----------|--------|-------------------------------|-------|
| CALM     | 4.9:1  | Yes                           | Yes   |
| NOTICE   | 3.4:1  | Yes (dark on amber)           | Yes   |
| ALERT    | 4.1:1  | Yes                           | Yes   |
| URGENT   | 5.7:1  | Yes                           | Yes   |
| THROB    | 5.7:1  | Yes                           | Yes   |

**SRS Trace:** FR-028 (visual states), NFR-010.AC4 (contrast ≥ 3:1).

### 3.2 Typography

| Role            | Font               | Weight    | Size  | Line Height | Usage                           |
|-----------------|--------------------|-----------| ------|-------------|----------------------------------|
| Display Large   | System Default     | Regular   | 34sp  | 40sp        | Welcome screen headline          |
| Headline Medium | System Default     | Medium    | 28sp  | 34sp        | Screen titles                    |
| Title Large     | System Default     | Medium    | 22sp  | 28sp        | Section headers                  |
| Title Medium    | System Default     | Medium    | 16sp  | 24sp        | Card titles, list item primary   |
| Body Large      | System Default     | Regular   | 16sp  | 24sp        | Body text, descriptions          |
| Body Medium     | System Default     | Regular   | 14sp  | 20sp        | Secondary text, captions         |
| Body Small      | System Default     | Regular   | 12sp  | 16sp        | Hints, helper text               |
| Label Large     | System Default     | Medium    | 14sp  | 20sp        | Buttons, chips                   |
| Label Small     | System Default     | Medium    | 11sp  | 16sp        | Badge text, counters             |
| **Overlay Timer** | **Monospace**    | Regular   | 12sp  | —           | Floating timer MM:SS text        |

> **Note:** The overlay timer uses `Typeface.MONOSPACE` to ensure digits don't shift width as the counter increments (TDD Section 9.2).

### 3.3 Spacing & Layout Grid

| Token       | Value   | Usage                                        |
|-------------|---------|----------------------------------------------|
| `space-xs`  | 4dp     | Inline element spacing                       |
| `space-sm`  | 8dp     | Tight internal padding                       |
| `space-md`  | 16dp    | Standard padding, card internal margin       |
| `space-lg`  | 24dp    | Section spacing, screen horizontal padding   |
| `space-xl`  | 32dp    | Between major sections                       |
| `space-xxl` | 48dp    | Screen top/bottom breathing room             |

**Screen Margins:** 24dp horizontal padding on all screens (standard Material guideline).

**List Item Height:** Minimum 56dp for app list items (48dp content + 8dp padding) to meet the 48dp touch target requirement (NFR-010.AC3).

### 3.4 Elevation & Shapes

| Component          | Elevation | Corner Radius | Usage                              |
|--------------------|-----------|---------------|------------------------------------|
| Screen surface     | 0dp       | —             | Background                         |
| Card               | 1dp       | 12dp          | Home screen info cards             |
| Floating overlay   | 6dp       | 50% (circle)  | Timer overlay                      |
| Bottom sheet       | 3dp       | 28dp top      | Future: tap-to-expand info         |
| Dialog              | 6dp       | 28dp          | Confirmation dialogs               |
| Button (filled)    | 0dp       | 20dp          | Primary action buttons             |
| Button (outlined)  | 0dp       | 20dp          | Secondary action buttons           |
| Search bar         | 0dp       | 28dp          | App selection search               |
| Chip               | 0dp       | 8dp           | Threshold presets                  |

### 3.5 Iconography

| Icon                   | Context                        | Style                    |
|------------------------|--------------------------------|--------------------------|
| App icons              | App selection list             | Loaded from PackageManager, 40dp |
| Checkmark              | Permission granted indicator   | Material Icon, Filled, Green |
| Warning                | Permission denied indicator    | Material Icon, Filled, Amber |
| Shield                 | Usage Access permission        | Material Icon, Outlined  |
| Layers                 | Overlay permission             | Material Icon, Outlined  |
| Battery                | Battery optimization           | Material Icon, Outlined  |
| Play/Stop              | Monitoring toggle              | Material Icon, Filled    |
| Search                 | App search bar                 | Material Icon, Outlined  |
| Arrow back             | Top app bar navigation         | Material Icon, Outlined  |
| Info                   | About screen navigation        | Material Icon, Outlined  |
| Notification small     | Service notification           | Custom, monochrome       |

---

## 4. Screen Inventory

| #  | Screen                             | Route                         | Entry Point                      | Phase |
|----|-------------------------------------|-------------------------------|----------------------------------|-------|
| 1  | Welcome Screen                      | `onboarding/welcome`          | First launch                     | P7    |
| 2  | Permission: Usage Access            | `onboarding/perm_usage_access`| Onboarding flow / permission fix | P7    |
| 3  | Permission: Display Over Other Apps | `onboarding/perm_overlay`     | Onboarding flow / permission fix | P7    |
| 4  | Permission: Battery Optimization    | `onboarding/perm_battery`     | Onboarding flow / permission fix | P7    |
| 5  | Onboarding Completion               | `onboarding/complete`         | After last permission step       | P7    |
| 6  | Home Screen                         | `main/home`                   | Post-onboarding default          | P6    |
| 7  | App Selection Screen                | `main/app_selection`          | Home navigation / onboarding     | P6    |
| 8  | Threshold Configuration Screen      | `main/threshold_config`       | Home navigation / onboarding     | P6    |
| 9  | About Screen                        | `main/about`                  | Home navigation                  | P6    |
| 10 | Floating Timer Overlay              | (WindowManager, not route)    | Service-managed                  | P4    |

**SRS Trace:** EIR-004 (screen inventory).

---

## 5. Navigation Architecture

### 5.1 Navigation Graph

```
App Launch
    │
    ├─ onboarding_completed == false?
    │       │
    │       └──► ONBOARDING GRAPH
    │            Welcome ──► UsageAccess ──► Overlay ──► Battery
    │                                                        │
    │                                              OnboardComplete
    │                                                        │
    │                                              (clear backstack)
    │                                                        │
    │                                                        ▼
    └─ onboarding_completed == true?
            │
            ├─ Usage Access granted? ──► No ──► Onboarding (skip to permission)
            │
            └─ Yes ──► MAIN GRAPH
                        Home (start destination)
                         ├──► App Selection ──► (back)
                         ├──► Threshold Config ──► (back)
                         └──► About ──► (back)
```

### 5.2 Navigation Rules

| Rule                              | Implementation                                                         | SRS Ref   |
|-----------------------------------|------------------------------------------------------------------------|-----------|
| Onboarding gate                   | Check `onboarding_completed` pref on launch                           | FR-041.AC3 |
| Permission re-check               | If critical permission revoked, redirect to onboarding step            | FR-041.AC4 |
| Onboarding clears backstack       | `popUpTo(Welcome) { inclusive = true }` when navigating to Home        | FR-041.AC5 |
| Back from main screens            | `navController.popBackStack()` returns to Home                        | Standard   |
| Back from Home                    | System back exits app                                                  | Standard   |
| Deep-link from notification       | Opens MainActivity → resolves to Home                                  | FR-036     |

### 5.3 Start Destination Logic

```
1. Read onboarding_completed from UserPreferences
2. If false → Route.Welcome
3. If true → check PermissionChecker.isUsageAccessGranted()
4. If not granted → Route.PermUsageAccess
5. If granted → Route.Home
```

**SRS Trace:** FR-041 (navigation), FR-033 (permission re-check), TDD Section 12.3.

---

## 6. Onboarding Flow

### 6.1 Welcome Screen

**Route:** `onboarding/welcome`
**SRS Trace:** FR-032 Step 1

```
┌────────────────────────────────┐
│                                │
│         (App Icon)             │
│                                │
│       MindfulScroll            │   ← Display Large
│                                │
│   Your digital awareness       │
│       companion                │   ← Body Large, On Surface Variant
│                                │
│   MindfulScroll helps you      │
│   stay aware of how much       │   ← Body Medium
│   time you spend on your       │
│   favorite apps — gently,      │
│   without blocking or          │
│   restricting anything.        │
│                                │
│                                │
│   ┌──────────────────────┐     │
│   │    Get Started        │     │   ← Filled Button, Primary
│   └──────────────────────┘     │
│                                │
└────────────────────────────────┘
```

**Behavior:**
- No back button (first screen)
- "Get Started" navigates to Permission: Usage Access
- Centered layout, vertically aligned content
- App icon at top (branded, not system icon)

---

### 6.2 Permission: Usage Access

**Route:** `onboarding/perm_usage_access`
**SRS Trace:** FR-032 Step 2, FR-034.AC1

```
┌────────────────────────────────┐
│  ← Back                       │
│                                │
│         🛡 (Shield icon)       │   ← 64dp, Primary color
│                                │
│    Usage Access Permission     │   ← Headline Medium
│                                │
│   To detect which app you're   │
│   using, MindfulScroll needs   │   ← Body Large
│   access to usage data.        │
│                                │
│   This data stays on your      │
│   device. We never send it     │   ← Body Medium, On Surface Variant
│   anywhere.                    │
│                                │
│   ┌──────────────────────────┐ │
│   │   What this means:       │ │   ← Card, Surface Variant
│   │                          │ │
│   │   • We can see which     │ │
│   │     app is in the        │ │
│   │     foreground            │ │
│   │   • We CANNOT see what   │ │
│   │     you do inside apps   │ │
│   │   • No data leaves your  │ │
│   │     phone                │ │
│   └──────────────────────────┘ │
│                                │
│   ┌──────────────────────┐     │
│   │  Grant Permission     │     │   ← Filled Button, Primary
│   └──────────────────────┘     │
│                                │
│       Skip for now             │   ← Text Button, On Surface Variant
│                                │
│   ● ○ ○                       │   ← Progress indicators (3 steps)
│                                │
└────────────────────────────────┘
```

**Behavior:**
- "Grant Permission" → launches `Settings.ACTION_USAGE_ACCESS_SETTINGS` (FR-034.AC1)
- On return from Settings, check if permission is now granted
  - If granted: Show green checkmark animation, auto-advance after 1 second
  - If not granted: Stay on screen, button text unchanged
- "Skip for now" → shows warning dialog, then advances to next permission
- Progress dots indicate 3 permission steps (1 of 3)
- If deep-link intent doesn't resolve → fallback to generic Settings (FR-034.AC4)

**Skip Warning Dialog:**

```
┌──────────────────────────────┐
│                              │
│   Skip Usage Access?         │  ← Title Medium
│                              │
│   Without this permission,   │
│   MindfulScroll cannot       │  ← Body Medium
│   detect which app you're    │
│   using. Monitoring will     │
│   not work.                  │
│                              │
│        Cancel    Skip        │  ← Text Buttons
│                              │
└──────────────────────────────┘
```

---

### 6.3 Permission: Display Over Other Apps

**Route:** `onboarding/perm_overlay`
**SRS Trace:** FR-032 Step 3, FR-034.AC2

Same layout as Section 6.2 with the following changes:

| Element           | Value                                                                        |
|-------------------|------------------------------------------------------------------------------|
| Icon              | Layers icon, 64dp, Primary color                                            |
| Title             | "Display Over Other Apps"                                                    |
| Description       | "To show the floating timer while you use other apps, MindfulScroll needs permission to draw over other apps." |
| What this means   | "A small, draggable timer appears on screen after 1 minute of app usage. You can move it anywhere. It never blocks your taps." |
| Intent            | `Settings.ACTION_MANAGE_OVERLAY_PERMISSION` with `package:` URI              |
| Progress          | ○ ● ○ (2 of 3)                                                              |

**Skip Warning:** "Without this permission, the floating timer will not appear. Monitoring will still track your sessions, but you won't see a visual reminder."

---

### 6.4 Permission: Battery Optimization

**Route:** `onboarding/perm_battery`
**SRS Trace:** FR-032 Step 4, FR-034.AC3

Same layout as Section 6.2 with the following changes:

| Element           | Value                                                                        |
|-------------------|------------------------------------------------------------------------------|
| Icon              | Battery icon, 64dp, Primary color                                            |
| Title             | "Battery Optimization"                                                       |
| Description       | "To keep monitoring reliably in the background, MindfulScroll needs to be excluded from battery optimization." |
| What this means   | "Some phones stop background apps to save battery. This ensures MindfulScroll keeps running when you need it." |
| Intent            | `Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` with `package:` URI   |
| Progress          | ○ ○ ● (3 of 3)                                                              |

**Skip Warning:** "Without this exclusion, your phone may stop MindfulScroll in the background. Monitoring could stop unexpectedly."

**API 33+ Addition:** If `POST_NOTIFICATIONS` permission is not yet granted and device is API 33+, show a notification permission request after battery optimization (as an inline prompt, not a separate screen).

---

### 6.5 Onboarding Completion Screen

**Route:** `onboarding/complete`
**SRS Trace:** FR-032 Step 5

```
┌────────────────────────────────┐
│                                │
│                                │
│         ✓ (Checkmark)          │   ← 72dp, animated, Primary color
│                                │
│       You're all set!          │   ← Headline Medium
│                                │
│   MindfulScroll is ready to    │
│   help you stay aware of       │   ← Body Large, centered
│   your screen time.            │
│                                │
│   ┌──────────────────────────┐ │
│   │ ✓ Usage Access     granted│ │
│   │ ✓ Overlay          granted│ │   ← Permission summary
│   │ ⚠ Battery Opt.     skipped│ │      (green check or amber warning)
│   └──────────────────────────┘ │
│                                │
│   ┌──────────────────────┐     │
│   │   Start Exploring     │     │   ← Filled Button, Primary
│   └──────────────────────┘     │
│                                │
└────────────────────────────────┘
```

**Behavior:**
- Shows summary of permission grant status
- Checkmark icon animates on entry (scale from 0 to 1, overshoot interpolator)
- "Start Exploring" sets `onboarding_completed = true` and navigates to Home with backstack cleared
- Granted permissions show green checkmark
- Skipped/denied permissions show amber warning icon

---

## 7. Main Screens

### 7.1 Home Screen

**Route:** `main/home`
**SRS Trace:** FR-039, FR-040, FR-033

```
┌────────────────────────────────┐
│  MindfulScroll          ℹ️     │   ← Top App Bar (ℹ️ = About)
│                                │
│ ┌──────────────────────────┐   │
│ │ ⚠ Usage Access not       │   │   ← Permission Banner (conditional)
│ │   granted. Tap to fix.   │   │      Error container color
│ └──────────────────────────┘   │
│                                │
│ ┌──────────────────────────┐   │
│ │                          │   │   ← Monitoring Status Card
│ │   Monitoring is OFF      │   │      Title Medium
│ │                          │   │
│ │      ┌──────────┐        │   │
│ │      │  Start   │        │   │   ← Large Filled Button, Primary
│ │      └──────────┘        │   │
│ │                          │   │
│ └──────────────────────────┘   │
│                                │
│ ┌──────────────────────────┐   │
│ │  📱 Monitored Apps    ▶  │   │   ← Clickable Card → App Selection
│ │     5 apps selected      │   │      Body Medium, On Surface Variant
│ └──────────────────────────┘   │
│                                │
│ ┌──────────────────────────┐   │
│ │  ⏱ Thresholds         ▶  │   │   ← Clickable Card → Threshold Config
│ │     10 / 20 / 30 min    │   │      Body Medium, On Surface Variant
│ └──────────────────────────┘   │
│                                │
└────────────────────────────────┘
```

**Monitoring Status Card States:**

| State | Title                    | Button      | Card Color         |
|-------|--------------------------|-------------|--------------------|
| OFF   | "Monitoring is OFF"      | "Start"     | Surface Variant    |
| ON    | "Monitoring is active"   | "Stop"      | Primary Container  |

**Button Behavior:**
- "Start" → checks permissions first via `PermissionChecker.checkAll()`
  - If all critical permissions granted → starts `MonitoringService`, updates preference
  - If missing → navigates to onboarding permission step
- "Stop" → stops `MonitoringService`, updates preference

**Permission Banner:**
- Only shown when a critical permission (Usage Access or Overlay) is revoked after onboarding
- Tapping the banner navigates to the relevant onboarding permission step
- Uses `Error` container color with `On Error` text
- Dismissed automatically when permission is re-granted (checked on `onResume`)

**Cards:**
- "Monitored Apps" card shows count of active apps; taps navigate to App Selection
- "Thresholds" card shows current T1/T2/T3 values; taps navigate to Threshold Config
- If no apps selected: "No apps selected" in amber text
- If no threshold config: Shows default "10 / 20 / 30 min (Moderate)"

**SRS Trace:** FR-039 (content), FR-040 (start/stop), FR-033 (permission banner).

---

### 7.2 App Selection Screen

**Route:** `main/app_selection`
**SRS Trace:** FR-001–FR-008, EIR-003

```
┌────────────────────────────────┐
│  ← Select Apps      3 selected │   ← Top App Bar with count
│                                │
│ ┌──────────────────────────┐   │
│ │ 🔍 Search apps...       │   │   ← Search TextField, 28dp corners
│ └──────────────────────────┘   │
│                                │
│  Select All    Deselect All    │   ← Text Buttons, aligned right
│                                │
│ ┌──────────────────────────┐   │
│ │ 🔲 Chrome                │   │   ← App list item
│ │    [icon]  Chrome         │   │     40dp icon, Title Medium name
│ ├──────────────────────────┤   │
│ │ ☑ Facebook               │   │   ← Selected item
│ │    [icon]  Facebook       │   │     Checkbox filled, Primary color
│ ├──────────────────────────┤   │
│ │ ☑ Instagram              │   │
│ │    [icon]  Instagram      │   │
│ ├──────────────────────────┤   │
│ │ 🔲 Netflix               │   │
│ │    [icon]  Netflix        │   │
│ ├──────────────────────────┤   │
│ │ ☑ Reddit                 │   │
│ │    [icon]  Reddit         │   │
│ ├──────────────────────────┤   │
│ │  ...                     │   │   ← LazyColumn, scrollable
│ └──────────────────────────┘   │
│                                │
└────────────────────────────────┘
```

**List Item Specification:**

| Element        | Size/Position                          | Style                     |
|----------------|----------------------------------------|---------------------------|
| App icon       | 40dp × 40dp, 16dp from start          | Loaded from PackageManager |
| App name       | Start after icon + 16dp spacing        | Title Medium, On Surface  |
| Checkbox       | 24dp, 16dp from end                    | Material Checkbox, Primary |
| Item height    | 56dp minimum                           | Includes 8dp vertical padding |
| Divider        | Full width, 1dp                        | Outline color             |

**Search Behavior:**
- Filters app list in real-time as user types (FR-005)
- Case-insensitive substring match on `displayName`
- Empty results: show "No apps found" centered text
- Clear button (X) appears when text is entered

**Select All / Deselect All:**
- Operates on the currently visible (filtered) set of apps (FR-008)
- "Select All" selects all visible apps
- "Deselect All" deselects all visible apps
- Each selection change immediately persists to the database

**App List:**
- Sorted alphabetically by display name, case-insensitive (FR-003)
- Excludes MindfulScroll itself (FR-001.AC3)
- Uses `LazyColumn` for efficient scrolling (EC-06: 100+ apps)
- Loads within 3 seconds even with 100+ apps (SRS EC-06)

**Selection Count:**
- Displayed in the top app bar title area
- Shows total selected count (not filtered count)
- Format: "N selected" (FR-006)

---

### 7.3 Threshold Configuration Screen

**Route:** `main/threshold_config`
**SRS Trace:** FR-025, FR-026, FR-027

```
┌────────────────────────────────┐
│  ← Thresholds                  │   ← Top App Bar
│                                │
│   Set when the timer changes   │
│   color to remind you of       │   ← Body Large, On Surface Variant
│   time passing.                │
│                                │
│   Presets                      │   ← Title Medium
│                                │
│   ┌──────────┐  ┌──────────┐  │
│   │  Light   │  │ Moderate │  │   ← Chips (selectable)
│   │ 15/30/45 │  │ 10/20/30 │  │     Selected = Primary Container
│   └──────────┘  └──────────┘  │     Unselected = Surface Variant
│                                │
│   Custom                       │   ← Title Medium
│                                │
│   First threshold              │
│   ┌──────────────────────────┐ │
│   │          10         min  │ │   ← OutlinedTextField, numeric
│   └──────────────────────────┘ │
│                                │
│   Second threshold             │
│   ┌──────────────────────────┐ │
│   │          20         min  │ │
│   └──────────────────────────┘ │
│                                │
│   Third threshold              │
│   ┌──────────────────────────┐ │
│   │          30         min  │ │
│   └──────────────────────────┘ │
│                                │
│   Timer changes color at       │
│   each threshold. 5 min after  │   ← Body Small, helper text
│   the third, it starts         │
│   pulsing gently.              │
│                                │
│   ┌──────────────────────┐     │
│   │       Save            │     │   ← Filled Button, Primary
│   └──────────────────────┘     │
│                                │
└────────────────────────────────┘
```

**Preset Chips:**

| Preset     | T1  | T2  | T3  | Selected Style           |
|------------|-----|-----|-----|--------------------------|
| Light      | 15  | 30  | 45  | Primary Container fill   |
| Moderate   | 10  | 20  | 30  | Primary Container fill (default, D-004) |

- Selecting a preset auto-fills the custom fields
- Editing custom fields deselects any active preset
- Custom values are validated before save

**Validation Rules (FR-026.AC5):**

| Rule | Constraint               | Error Message                          |
|------|--------------------------|----------------------------------------|
| V-01 | T1 ≥ 1 min              | "First threshold must be at least 1 minute" |
| V-02 | T2 > T1                 | "Second threshold must be greater than first" |
| V-03 | T3 > T2                 | "Third threshold must be greater than second" |
| V-04 | T3 ≤ 120 min            | "Third threshold cannot exceed 120 minutes" |

- Validation errors appear as red helper text below the affected field
- "Save" button is disabled when validation fails
- On successful save, navigate back to Home with a brief confirmation (Snackbar: "Thresholds saved")

---

### 7.4 About Screen

**Route:** `main/about`

```
┌────────────────────────────────┐
│  ← About                      │   ← Top App Bar
│                                │
│         (App Icon)             │   ← 72dp, centered
│                                │
│       MindfulScroll            │   ← Headline Medium
│       Version 1.0.0            │   ← Body Medium, On Surface Variant
│                                │
│ ┌──────────────────────────┐   │
│ │                          │   │
│ │  MindfulScroll is a      │   │
│ │  digital awareness tool  │   │   ← Body Large
│ │  that helps you stay     │   │
│ │  mindful of your screen  │   │
│ │  time — without blocking │   │
│ │  or restricting anything.│   │
│ │                          │   │
│ └──────────────────────────┘   │
│                                │
│ ┌──────────────────────────┐   │
│ │  Privacy Policy        ▶ │   │   ← Clickable list item
│ ├──────────────────────────┤   │
│ │  Open Source Licenses  ▶ │   │   ← Clickable list item
│ └──────────────────────────┘   │
│                                │
│   Made with care.              │   ← Body Small, centered, On Surface Variant
│                                │
└────────────────────────────────┘
```

**Behavior:**
- "Privacy Policy" opens the privacy policy (in-app or external link)
- "Open Source Licenses" shows OSS license information
- Version number is dynamically read from `BuildConfig.VERSION_NAME`

---

## 8. Floating Timer Overlay

The floating timer overlay is the defining UI element of MindfulScroll. It is a View-based component (not Compose) rendered via `WindowManager` with `TYPE_APPLICATION_OVERLAY`.

### 8.1 Overlay Appearance

```
         ┌────────┐
         │        │
         │ 05:23  │        Diameter: 52dp (constant in AppConstants)
         │        │        Shape: Perfect circle
         └────────┘        Background: Solid color (state-dependent)
                           Text: Centered, monospace, 12sp
                           Time format: MM:SS (from session start, D-002)
                           Shadow: Subtle drop shadow (elevation 6dp)
```

| Property              | Value                                | SRS Ref    |
|-----------------------|--------------------------------------|------------|
| Diameter              | 52dp (48-56dp range)                 | FR-019.AC1 |
| Shape                 | Circle                               | FR-019.AC2 |
| Text format           | `MM:SS`                              | FR-019.AC3 |
| Text typeface         | Monospace                            | TDD 9.2    |
| Text size             | 12sp                                 | TDD 2.3    |
| Update frequency      | Every 1 second                       | FR-019.AC4 |
| Time reference        | From session start (includes first 60s) | D-002   |
| Window type           | `TYPE_APPLICATION_OVERLAY`           | EIR-002    |
| Window flags          | `FLAG_NOT_FOCUSABLE \| FLAG_NOT_TOUCH_MODAL \| FLAG_LAYOUT_NO_LIMITS` | EIR-002 |
| Default position      | Right edge, 50% screen height        | D-003      |

### 8.2 Visual States & Color Escalation

The overlay progresses through 5 visual states as session time increases. Transitions are **one-directional** — the timer only escalates, never de-escalates within a session.

| State   | Trigger                  | Background   | Text    | Opacity | Meaning                  |
|---------|--------------------------|-------------|---------|---------|--------------------------|
| CALM    | Overlay appears (60s)    | `#6B7280`   | White   | 65%     | Awareness begins         |
| NOTICE  | Elapsed ≥ T1             | `#F59E0B`   | Dark    | 78%     | First threshold crossed  |
| ALERT   | Elapsed ≥ T2             | `#F97316`   | White   | 88%     | Growing awareness        |
| URGENT  | Elapsed ≥ T3             | `#EF4444`   | White   | 95%     | Time to consider leaving |
| THROB   | Elapsed ≥ T3 + 5 min    | `#EF4444`   | White   | 100%    | Well beyond limit        |

**Visual Escalation Diagram:**

```
Session Start                    60s         T1          T2          T3      T3+5min
    │                             │           │           │           │          │
    ├─── (no overlay) ───────────►│── CALM ──►│── NOTICE ►│── ALERT ─►│─ URGENT ►│── THROB ──►
    │                             │           │           │           │          │
    │         Timer hidden        │  Gray     │  Amber    │  Orange   │  Red     │  Red+Pulse
    │                             │  65%      │  78%      │  88%      │  95%     │  100%
```

**With default Moderate thresholds (10/20/30):**
- 0:00–0:59 → No overlay
- 1:00–9:59 → CALM (gray, 65%)
- 10:00–19:59 → NOTICE (amber, 78%)
- 20:00–29:59 → ALERT (orange, 88%)
- 30:00–34:59 → URGENT (red, 95%)
- 35:00+ → THROB (red, pulsing, 100%)

### 8.3 Overlay Animations

| Animation          | Duration | Interpolator               | Details                                    | SRS Ref |
|--------------------|----------|----------------------------|--------------------------------------------|---------|
| Fade-in            | 500ms    | Ease-in-out (Accelerate-Decelerate) | Alpha 0% → state opacity (e.g., 65%) | FR-020  |
| Fade-out           | 400ms    | Ease-in-out                | Current alpha → 0%, then remove view       | FR-024  |
| Color transition   | 2000ms   | Linear                     | ARGB lerp for background + text color      | FR-029  |
| Opacity transition | 2000ms   | Linear (sync with color)   | Float lerp for overall opacity             | FR-029  |
| Edge-snap          | 250ms    | Decelerate                 | Horizontal translate to nearest edge        | FR-022  |
| Dim (cooldown)     | 300ms    | Linear                     | Reduce alpha by 30%                         | FR-014.AC4 |
| Undim (resume)     | 300ms    | Linear                     | Restore alpha to full state value           | —       |
| Throb              | 1500ms   | Ease-in-out, infinite      | Scale 1.0x ↔ 1.15x, reverse repeat         | FR-031  |

### 8.4 Drag & Edge-Snapping

**Drag Behavior (FR-021):**
1. Touch within overlay bounds → captures touch
2. If movement > 10dp (tap slop) → enters drag mode
3. Overlay follows finger position in real-time via `WindowManager.updateViewLayout()`
4. Touch outside overlay bounds → passes through to host app automatically

**Edge-Snapping (FR-022):**
1. On finger release, calculate overlay's center X position
2. If center X ≤ screen width / 2 → snap to left edge (X = 0)
3. If center X > screen width / 2 → snap to right edge (X = screenWidth - overlayWidth)
4. If exactly centered → snap to **right** (deterministic default, D-003)
5. Animate horizontal position over 250ms with decelerate interpolator
6. Y position is preserved (no vertical snapping), clamped to screen bounds

**Position Persistence (FR-023):**
- On each snap, save `(edge: "left"|"right", yPercent: 0.0-1.0)` to `user_preferences` table
- On next overlay appearance, restore from saved position
- Default: right edge, 50% Y (D-003)

### 8.5 Cooldown Behavior

When the user leaves the monitored app and enters the cooldown period (45 seconds, D-001):

1. **Overlay dims** by reducing alpha by 30% over 300ms (FR-014.AC4)
2. **Timer pauses** — the MM:SS text stops incrementing
3. If user returns to the monitored app within 45 seconds:
   - **Overlay undims** (alpha restored) over 300ms
   - **Timer resumes** from where it paused
   - Visual state is preserved (no de-escalation)
4. If cooldown expires:
   - **Overlay fades out** over 400ms
   - **Overlay is removed** from WindowManager
   - Session ends and is persisted to database

### 8.6 Throb Animation

**Trigger:** Elapsed time ≥ T3 + 5 minutes (300 seconds after final threshold)

| Property            | Value                               | SRS Ref   |
|---------------------|--------------------------------------|-----------|
| Scale range         | 1.0x → 1.15x                        | FR-031.AC2 |
| Cycle duration      | 1500ms (750ms expand, 750ms contract)| FR-031.AC3 |
| Repeat mode         | Reverse (breathing effect)           | FR-031.AC4 |
| Repeat count        | Infinite                             | FR-031.AC5 |
| Interpolator        | Ease-in-out                          | FR-031.AC6 |
| Co-existing         | Runs alongside the 1-second timer update | — |

The throb animation continues indefinitely until the session ends. It provides a motion-based cue that is perceivable regardless of color vision capability.

---

## 9. Notification Design

The foreground service requires a persistent notification (Android requirement, C-13).

| Property            | Value                                                   |
|---------------------|---------------------------------------------------------|
| Channel ID          | `mindfulscroll_monitoring`                              |
| Channel Name        | "Monitoring Service"                                    |
| Importance          | `IMPORTANCE_LOW` (no sound, no vibration, no heads-up)  |
| Content Title       | "MindfulScroll is monitoring"                           |
| Content Text        | "Tap to open"                                           |
| Small Icon          | Custom monochrome app icon                              |
| Content Intent      | Opens `MainActivity` (→ Home screen)                     |
| Action Button       | "Stop Monitoring" → stops the service                   |
| Ongoing             | Yes (cannot be swiped away)                             |
| Badge               | Disabled (`setShowBadge(false)`)                        |
| Sound               | None (`setSound(null, null)`)                           |

**SRS Trace:** FR-036 (notification), FR-036.AC5 (IMPORTANCE_LOW), FR-037 (stop action).

---

## 10. Permission Banner

Displayed on the Home Screen when a critical permission is revoked post-onboarding.

```
┌──────────────────────────────────┐
│ ⚠  Usage Access not granted.     │   ← Error Container color
│    Tap to fix.                   │     On Error text
└──────────────────────────────────┘
```

| Property       | Value                                         | SRS Ref |
|----------------|-----------------------------------------------|---------|
| Visibility     | Only when critical permission is revoked       | FR-033  |
| Background     | Error Container color                          | —       |
| Icon           | Warning (⚠), On Error color                    | —       |
| Text           | Describes which permission is missing          | —       |
| Action         | Tap navigates to relevant onboarding step      | FR-033  |
| Check timing   | On every `onResume` of Home screen             | FR-033.AC1 |
| Dismissal      | Auto-hides when permission is re-granted       | —       |

**Banner Messages:**

| Missing Permission       | Message                                        |
|--------------------------|------------------------------------------------|
| Usage Access             | "Usage Access not granted. Tap to fix."        |
| Overlay                  | "Overlay permission not granted. Timer won't appear." |
| Both                     | "Permissions needed. Tap to set up."           |

---

## 11. Animation Specification

### 11.1 Screen Transitions

| Transition                     | Animation                         | Duration |
|--------------------------------|-----------------------------------|----------|
| Navigate forward (push)        | Slide in from right               | 300ms    |
| Navigate back (pop)            | Slide out to right                | 250ms    |
| Onboarding step forward        | Slide in from right               | 300ms    |
| Onboarding to Home             | Fade transition (backstack clear) | 400ms    |

### 11.2 In-Screen Animations

| Element                        | Animation                              | Duration | Trigger                     |
|--------------------------------|----------------------------------------|----------|-----------------------------|
| Permission checkmark           | Scale 0 → 1 with overshoot            | 400ms    | Permission detected granted |
| Monitoring toggle              | Smooth state change                    | 200ms    | User taps Start/Stop        |
| App selection checkbox         | Material checkbox animation            | 150ms    | User taps list item         |
| Search clear button            | Fade in/out                            | 150ms    | Text entered/cleared        |
| Threshold chip selection       | Background color change                | 200ms    | User taps chip              |
| Permission banner              | Slide down from top                    | 300ms    | Permission revoked detected |
| Snackbar                       | Slide up from bottom                   | 250ms    | Save confirmation           |
| Completion checkmark           | Scale 0 → 1.2 → 1.0 (overshoot)      | 600ms    | Screen entry                |

### 11.3 Overlay Animations

See [Section 8.3](#83-overlay-animations) for the complete overlay animation specification.

---

## 12. Accessibility

### 12.1 Requirements (NFR-010)

| Requirement                   | Target                           | Verification                     |
|-------------------------------|----------------------------------|----------------------------------|
| Text contrast ratio           | ≥ 4.5:1 (normal), ≥ 3:1 (large) | Color contrast analyzer          |
| Overlay contrast              | ≥ 3:1 (large text on circle bg) | NFR-010.AC4                      |
| Touch target size             | ≥ 48dp × 48dp                   | All interactive elements         |
| TalkBack labels               | All interactive elements labeled | Screen reader testing            |
| Focus order                   | Logical reading order            | Tab/D-pad navigation             |
| Text scaling                  | Supports up to 200% font scale  | Test with large font settings    |
| Motion sensitivity            | Respect "reduce motion" setting  | Reduce/skip overlay animations   |

### 12.2 Content Descriptions

| Element                    | Content Description                                      |
|----------------------------|----------------------------------------------------------|
| Monitoring Start button    | "Start monitoring selected apps"                         |
| Monitoring Stop button     | "Stop monitoring"                                        |
| App checkbox (unchecked)   | "{app name}, not selected for monitoring"                |
| App checkbox (checked)     | "{app name}, selected for monitoring"                    |
| Search field               | "Search apps"                                            |
| Select All button          | "Select all visible apps for monitoring"                 |
| Deselect All button        | "Deselect all visible apps"                              |
| Threshold field            | "Threshold {n} in minutes"                               |
| Permission grant button    | "Grant {permission name} permission"                     |
| Skip button                | "Skip {permission name} for now"                         |
| Back button                | "Navigate back"                                          |
| About button               | "About MindfulScroll"                                    |
| Floating overlay           | "Usage timer showing {MM:SS}"                            |

### 12.3 Color-Blind Considerations

The overlay's 5 visual states rely on color progression (gray → amber → orange → red). For users with color vision deficiency:

| Cue Type            | Behavior                                                    |
|---------------------|-------------------------------------------------------------|
| **Opacity**         | Progresses 65% → 78% → 88% → 95% → 100% (independent of color) |
| **Motion (throb)**  | Final state adds a pulsing animation (color-independent)    |
| **Time text**       | Always shows exact elapsed time                             |

**Future (v1.1/v1.2):** Accessibility Mode (PRD F-031) will add shape changes, pattern overlays, and optional haptic feedback as color-independent escalation cues.

---

## 13. Responsive & Edge Cases

### 13.1 Screen Size Adaptation

| Screen Size         | Adaptation                                                 |
|---------------------|------------------------------------------------------------|
| Phone (< 600dp)    | Standard single-column layout (primary target)             |
| Phone landscape     | Scrollable content; overlay repositions via Y-percent      |
| Tablet (≥ 600dp)   | Out of scope for MVP (PRD Section 11.2); layout stretches  |

### 13.2 Device Rotation (EH-007)

- **Overlay:** Recalculates position from saved Y-percent and snapped edge
- **In-app screens:** Compose handles recomposition automatically; ViewModel survives
- **Session data:** Not affected (ViewModel + StateFlow pattern)

### 13.3 Split-Screen Mode (EH-008)

- Overlay uses `TYPE_APPLICATION_OVERLAY` → appears over the full screen, not just one split
- Monitoring tracks the focused window's app
- No special UI adaptation needed

### 13.4 Keyboard Interaction

- Threshold configuration fields: numeric soft keyboard
- Search field: text soft keyboard with clear action
- Fields should not be obscured by keyboard (use `imePadding()` modifier)

---

## 14. Interaction Patterns

### 14.1 Monitoring Toggle

```
User taps "Start"
    │
    ├─ Check permissions
    │   ├─ Missing → Navigate to onboarding
    │   └─ OK → Start service
    │           Button changes to "Stop"
    │           Card color → Primary Container
    │           Label → "Monitoring is active"
    │
User taps "Stop"
    │
    └─ Stop service
       Button changes to "Start"
       Card color → Surface Variant
       Label → "Monitoring is OFF"
```

### 14.2 App Selection Toggle

```
User taps app row
    │
    ├─ If unchecked → Check, persist upsert, increment counter
    └─ If checked → Uncheck, persist setActive(false), decrement counter
```

- Immediate visual feedback (checkbox state toggles)
- Count in toolbar updates immediately
- Database write happens on background thread

### 14.3 Threshold Save

```
User edits fields
    │
    ├─ Validation runs on each change
    │   ├─ Invalid → Show error, disable Save
    │   └─ Valid → Enable Save
    │
User taps "Save"
    │
    ├─ Persist to database
    ├─ Show Snackbar: "Thresholds saved"
    └─ Navigate back to Home
```

---

## 15. Empty States & Loading

### 15.1 Empty States

| Screen              | Condition                    | Display                                          |
|---------------------|------------------------------|--------------------------------------------------|
| App Selection       | No installed apps found      | "No apps found on your device." (centered text)  |
| App Selection       | Search returns no results    | "No apps match your search." (centered text)     |
| Home (apps card)    | No apps selected             | "No apps selected" (amber text, card subtitle)   |

### 15.2 Loading States

| Screen              | Loading Indicator                    | Duration Target         |
|---------------------|--------------------------------------|-------------------------|
| App Selection       | Circular progress indicator (center) | < 3 seconds (EC-06)    |
| Home                | Shimmer on cards or skeleton         | < 500ms typically       |
| Threshold Config    | None (data loads instantly from DB)  | < 100ms (NFR-005)      |
| Onboarding          | None (static content)               | —                       |

---

## 16. Error States

### 16.1 User-Visible Errors

| Error                          | Display                                       | Recovery Action             |
|--------------------------------|-----------------------------------------------|-----------------------------|
| Permission not granted         | Permission banner on Home screen              | Tap → onboarding step       |
| Service not starting (API 33+) | Banner: "Notification permission needed"     | Tap → system permission     |
| No apps selected + Start       | Snackbar: "Select at least one app first"    | Navigate to App Selection   |
| Threshold validation error     | Inline error text below affected field        | User corrects input         |

### 16.2 Silent Errors (No UI)

These errors are handled by the service/data layer with no user-visible indication:

| Error                          | Handling                                       | SRS Ref |
|--------------------------------|------------------------------------------------|---------|
| Overlay add failure            | Log, continue monitoring without overlay       | EH-004  |
| UsageStats empty data          | Treat as no foreground app, retry next poll     | EH-005  |
| Database write failure         | Log, continue in-memory                        | EH-006  |
| App uninstalled                | Silently remove from monitored list            | EH-001  |
| Service killed by OS           | `START_STICKY` auto-restart                    | EH-003  |

---

## 17. Traceability Matrix

### 17.1 SRS Requirement → UI Element Mapping

| SRS Requirement | UI Element                                          | Section   |
|-----------------|-----------------------------------------------------|-----------|
| FR-001–FR-003   | App Selection list (discovery, display, sort)       | 7.2       |
| FR-004–FR-006   | App Selection (checklist, search, count)             | 7.2       |
| FR-007          | Persistence (DB-backed, survives restart)            | 7.2       |
| FR-008          | Select All / Deselect All buttons                    | 7.2       |
| FR-018          | Overlay appear trigger (60 seconds)                  | 8.1       |
| FR-019          | Overlay appearance spec (48-56dp, circle, MM:SS)     | 8.1       |
| FR-020          | Fade-in animation (500ms)                            | 8.3       |
| FR-021          | Drag behavior                                        | 8.4       |
| FR-022          | Edge-snapping                                        | 8.4       |
| FR-023          | Position persistence                                 | 8.4       |
| FR-024          | Fade-out / auto-dismiss                              | 8.3       |
| FR-025–FR-027   | Threshold Configuration screen                       | 7.3       |
| FR-028          | 5 visual states with colors/opacity                  | 8.2       |
| FR-029          | Color transition animation (2000ms)                  | 8.3       |
| FR-030          | Throb trigger logic                                  | 8.6       |
| FR-031          | Throb animation spec                                 | 8.6       |
| FR-032          | Onboarding flow (5 steps)                            | 6.1–6.5  |
| FR-033          | Permission status banner                             | 10        |
| FR-034          | Deep-link to Settings                                | 6.2–6.4  |
| FR-036          | Service notification                                 | 9         |
| FR-039          | Home screen content                                  | 7.1       |
| FR-040          | Start/Stop monitoring toggle                         | 7.1       |
| FR-041          | Navigation structure                                 | 5         |

### 17.2 NFR → UI Design Mapping

| SRS NFR  | UI Design Element                                            | Section  |
|----------|--------------------------------------------------------------|----------|
| NFR-001  | Cold start ≤ 2 sec (minimal splash, fast navigation)        | 15.2     |
| NFR-004  | Overlay ≥ 55 FPS (flat view hierarchy, hardware accel)       | 8.1      |
| NFR-009  | Onboarding < 90 sec (streamlined flow)                       | 6        |
| NFR-010  | Accessibility (contrast, touch targets, TalkBack)            | 12       |

### 17.3 Risk → UI Mitigation Mapping

| Risk     | UI Mitigation                                                 | Section  |
|----------|---------------------------------------------------------------|----------|
| RA-003   | Plain language, skip option, progress indicators             | 6.2      |
| RA-004   | Small size (52dp), subtle initial opacity (65%), edge-snap   | 8.1, 8.2 |
| RA-010   | `FLAG_NOT_FOCUSABLE`, touch pass-through, drag detection     | 8.4      |
| RA-011   | Ghost overlay cleanup on service start, fade-out before remove | 8.3     |
| RA-023   | Opacity progression + throb animation (color-independent cues) | 12.3   |

### 17.4 PRD Feature → Screen Mapping

| PRD Feature | Screen(s)                                           |
|-------------|-----------------------------------------------------|
| F-001       | App Selection Screen                                |
| F-002       | App Selection Screen                                |
| F-003       | App Selection Screen (DB persistence)               |
| F-004       | (Service layer — no UI)                             |
| F-005       | (Service layer — no UI)                             |
| F-006       | Floating Timer Overlay                              |
| F-007       | Floating Timer Overlay (drag + snap)                |
| F-008       | Floating Timer Overlay (position persistence)       |
| F-009       | Threshold Configuration Screen                      |
| F-010       | Floating Timer Overlay (color escalation)           |
| F-011       | Floating Timer Overlay (throb animation)            |
| F-012       | Floating Timer Overlay (auto-dismiss)               |
| F-013       | Onboarding Flow (Welcome + 3 permissions + Complete) |
| F-014       | Notification + Home Screen toggle                   |
| F-015       | (Service layer — no UI)                             |

### 17.5 ProjectPlan WBS → UI Section Mapping

| WBS Task  | Description                          | UIUXspec Section  |
|-----------|--------------------------------------|-------------------|
| 4.1–4.12  | Phase 4: Floating Timer Overlay      | Section 8         |
| 5.1–5.9   | Phase 5: Threshold & Escalation      | Section 8.2, 8.6  |
| 6.1–6.11  | Phase 6: UI Screens                  | Section 7         |
| 7.1–7.11  | Phase 7: Permission Onboarding       | Section 6         |
| 8.13      | Accessibility pass                   | Section 12        |

---

## 18. References

| Document                         | Sections Referenced                                         |
|----------------------------------|-------------------------------------------------------------|
| [PRD.md](PRD.md)                 | Product Principles (P1–P6), User Flows (10.1–10.3), Features (F-001–F-015) |
| [SRS.md](SRS.md)                 | FR-001–FR-041, NFR-001/004/009/010, EIR-002–EIR-004, EH-001–EH-008 |
| [SAD.md](SAD.md)                 | Section 10 (Overlay Architecture), Section 11 (State Management), Section 12 (Navigation) |
| [TDD.md](TDD.md)                | Section 9 (Overlay System), Section 10 (Presentation Layer), Section 12 (Navigation) |
| [DbSchema.md](DbSchema.md)      | Section 4.4 (user_preferences keys for overlay position, monitoring state) |
| [ProjectPlan.md](ProjectPlan.md) | Phase 4 (Overlay), Phase 5 (Escalation), Phase 6 (UI), Phase 7 (Onboarding) |
| [RiskAssessment.md](RiskAssessment.md) | RA-003, RA-004, RA-010, RA-011, RA-023                |
| [Material Design 3](https://m3.material.io/) | Design system reference                          |
| [WCAG 2.1 Level AA](https://www.w3.org/WAI/WCAG21/quickref/) | Accessibility standard         |

---

*End of Document*
