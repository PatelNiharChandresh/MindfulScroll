# Software Requirements Specification (SRS)

## MindfulScroll — Digital Awareness Companion

---

### Document Control

| Field              | Value                                              |
| ------------------ | -------------------------------------------------- |
| **Document Title** | MindfulScroll Software Requirements Specification   |
| **Version**        | 1.0                                                |
| **Status**         | Draft                                              |
| **Created**        | 2026-02-05                                         |
| **Last Updated**   | 2026-02-05                                         |
| **Author**         | Rudy                                               |
| **Reviewers**      | —                                                  |
| **Approved By**    | —                                                  |
| **Parent Document**| [PRD.md](PRD.md) v1.0                              |

### Revision History

| Version | Date       | Author | Changes       |
| ------- | ---------- | ------ | ------------- |
| 1.0     | 2026-02-05 | Rudy   | Initial draft |

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Overall Description](#2-overall-description)
3. [System Architecture Overview](#3-system-architecture-overview)
4. [Functional Requirements](#4-functional-requirements)
5. [Non-Functional Requirements](#5-non-functional-requirements)
6. [External Interface Requirements](#6-external-interface-requirements)
7. [Data Requirements](#7-data-requirements)
8. [Platform & Environment Requirements](#8-platform--environment-requirements)
9. [Permission Requirements](#9-permission-requirements)
10. [State Machine Definitions](#10-state-machine-definitions)
11. [Error Handling & Edge Cases](#11-error-handling--edge-cases)
12. [Acceptance Criteria Summary](#12-acceptance-criteria-summary)
13. [Traceability Matrix](#13-traceability-matrix)
14. [Glossary](#14-glossary)
15. [References](#15-references)

---

## 1. Introduction

### 1.1 Purpose

This Software Requirements Specification defines the complete set of functional and non-functional requirements for MindfulScroll v1.0 (MVP). It translates the product vision, features, and user stories defined in the [Product Requirements Document (PRD.md)](PRD.md) into precise, testable, unambiguous technical requirements.

This document serves as the binding contract between product definition and engineering implementation. Every requirement herein is traceable to a PRD feature or user story, and every requirement has explicit acceptance criteria against which it can be verified.

### 1.2 Scope

This SRS covers all **MVP requirements** (PRD features F-001 through F-015) in full detail. Version 1.1 and future features (F-016 through F-032) are referenced for forward compatibility considerations but are not specified in detail.

### 1.3 Intended Audience

| Audience                    | Usage                                                      |
| --------------------------- | ---------------------------------------------------------- |
| Android developers          | Implementation reference for all feature behavior           |
| QA / Test engineers         | Source of acceptance criteria and testable conditions        |
| UI/UX designers             | Behavioral constraints and interaction specifications       |
| Technical architects        | System boundary and interface definitions                   |
| Project managers            | Scope verification and progress tracking                    |

### 1.4 Definitions & Conventions

**Requirement IDs** follow the pattern `[Category]-[Number]`:

| Prefix | Category                    |
| ------ | --------------------------- |
| FR     | Functional Requirement      |
| NFR    | Non-Functional Requirement  |
| EIR    | External Interface Requirement |
| DR     | Data Requirement            |
| PER    | Platform/Environment Requirement |
| PMR    | Permission Requirement      |

**Priority levels:**

| Priority    | Meaning                                                            |
| ----------- | ------------------------------------------------------------------ |
| **Must**    | Mandatory for MVP. The app cannot ship without this.               |
| **Should**  | Important for MVP quality but the app is functional without it.    |
| **Could**   | Desirable for MVP; implement if time allows.                       |
| **Won't**   | Explicitly deferred to a future version.                           |

**Testability:** Every requirement includes acceptance criteria written as verifiable conditions. A requirement that cannot be tested is not a valid requirement.

### 1.5 Document References

| Document                           | Relationship                                       |
| ---------------------------------- | -------------------------------------------------- |
| [PRD.md](PRD.md)                   | Parent document; defines features and user stories  |
| System Architecture Document (SAD) | Downstream; implements the architecture implied here |
| Technical Design Document (TDD)    | Downstream; provides detailed algorithm design       |
| UI/UX Design Specification         | Downstream; provides visual design for UI requirements |
| Test Plan                          | Downstream; derives test cases from acceptance criteria |

---

## 2. Overall Description

### 2.1 Product Context

MindfulScroll is a standalone native Android application. It operates entirely on-device with no server-side components, no network dependencies, and no user accounts. It interacts with the Android operating system through three primary system APIs:

1. **PackageManager** — to discover installed applications.
2. **UsageStatsManager** — to detect which application is in the foreground.
3. **WindowManager** — to render the floating timer overlay on top of other applications.

### 2.2 User Characteristics

Users range from moderate to high technical comfort (see PRD Section 4.2, Personas). All users are expected to:

- Understand the concept of app permissions (with guidance).
- Be able to navigate to Android system Settings pages when directed.
- Be capable of interacting with a floating overlay (drag, observe).

The onboarding flow must accommodate users with moderate technical comfort (Persona 3: Maria) as the baseline, meaning every step must include plain-language explanations.

### 2.3 Operating Environment

| Parameter               | Value                                              |
| ----------------------- | -------------------------------------------------- |
| Platform                | Android                                            |
| Minimum API level       | 26 (Android 8.0 Oreo)                              |
| Target API level        | 36 (Android 15)                                    |
| Compile SDK             | 36                                                 |
| Language                | Kotlin 2.0+                                        |
| UI framework            | Jetpack Compose with Material Design 3              |
| Supported orientations  | Portrait primary; landscape must not crash or leave ghost overlays |
| Network requirement     | None (fully offline)                                |
| Supported form factors  | Phones (primary); tablets (functional but not optimized) |

### 2.4 Design & Implementation Constraints

Inherited from PRD Sections 13.1–13.3 (Constraints C-01 through C-13). These are binding on the implementation:

- Usage Access and Overlay permissions require manual user grant via system Settings (C-01, C-02).
- Foreground service notification is mandatory and cannot be hidden (C-13).
- All data storage must be local, on-device, using Room (C-11).
- No network calls of any kind (C-12).
- Overlay must pass through touch events outside its bounds (C-09).
- Overlay diameter must not exceed 56dp (C-08).

### 2.5 Assumptions & Dependencies

Inherited from PRD Sections 12 and 14 (Assumptions A-01 through A-10, Dependencies). Key assumptions restated:

- UsageStatsManager polling at 2–3 second intervals provides sufficient accuracy (A-02).
- Users will monitor 1–20 apps on average (A-04).
- Google Play will approve the `PACKAGE_USAGE_STATS` usage given proper policy declaration (A-10).

---

## 3. System Architecture Overview

This section provides the minimal architectural context needed to understand requirement placement. The full architecture is defined in the System Architecture Document (SAD).

### 3.1 High-Level Component Diagram

```
┌─────────────────────────────────────────────────────────────┐
│                      PRESENTATION LAYER                      │
│                                                              │
│  ┌─────────────┐  ┌──────────────┐  ┌───────────────────┐   │
│  │  Onboarding │  │ App Selection │  │ Threshold Config  │   │
│  │   Screens   │  │    Screen     │  │     Screen        │   │
│  └─────────────┘  └──────────────┘  └───────────────────┘   │
│  ┌─────────────┐  ┌──────────────────────────────────────┐   │
│  │    Home     │  │         Settings Screen               │   │
│  │   Screen    │  │                                       │   │
│  └─────────────┘  └──────────────────────────────────────┘   │
├─────────────────────────────────────────────────────────────┤
│                       DOMAIN LAYER                           │
│                                                              │
│  ┌────────────────────┐  ┌─────────────────────────────┐    │
│  │  Use Cases:        │  │  Models:                     │    │
│  │  • GetInstalledApps│  │  • MonitoredApp              │    │
│  │  • UpdateSelection │  │  • UsageSession              │    │
│  │  • GetThresholds   │  │  • ThresholdConfig           │    │
│  │  • EvaluateState   │  │  • SessionState (enum)       │    │
│  └────────────────────┘  │  • TimerVisualState (enum)   │    │
│                          └─────────────────────────────┘    │
├─────────────────────────────────────────────────────────────┤
│                        DATA LAYER                            │
│                                                              │
│  ┌───────────┐  ┌──────────────────┐  ┌─────────────────┐   │
│  │  Room DB  │  │  PackageManager  │  │  UsageStats     │   │
│  │           │  │  Repository      │  │  Repository     │   │
│  └───────────┘  └──────────────────┘  └─────────────────┘   │
├─────────────────────────────────────────────────────────────┤
│                       SERVICE LAYER                          │
│                                                              │
│  ┌──────────────────────────────────────────────────────┐   │
│  │              MonitoringService (Foreground)            │   │
│  │  ┌──────────────┐  ┌────────────┐  ┌──────────────┐  │   │
│  │  │ Polling      │  │  Session   │  │  Overlay     │  │   │
│  │  │ Engine       │  │  Manager   │  │  Controller  │  │   │
│  │  └──────────────┘  └────────────┘  └──────────────┘  │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │           BootReceiver (BroadcastReceiver)             │   │
│  └──────────────────────────────────────────────────────┘   │
│  ┌──────────────────────────────────────────────────────┐   │
│  │          ScreenStateReceiver (BroadcastReceiver)       │   │
│  └──────────────────────────────────────────────────────┘   │
└─────────────────────────────────────────────────────────────┘
```

### 3.2 Key Data Flows

| Flow | Trigger | Path |
| ---- | ------- | ---- |
| **App Discovery** | User opens App Selection screen | PackageManager → Repository → ViewModel → UI |
| **App Selection** | User taps checkbox | UI → ViewModel → Repository → Room DB |
| **Foreground Detection** | Polling timer fires (every ~2–3 sec) | UsageStatsManager → Polling Engine → Session Manager |
| **Overlay Display** | Session duration ≥ 60 seconds | Session Manager → Overlay Controller → WindowManager |
| **Threshold Evaluation** | Session timer tick (every 1 sec) | Session Manager → Threshold Engine → Overlay Controller (color/animation update) |
| **Session End** | Cooldown expires or screen off timeout | Session Manager → Room DB (persist session) → Overlay Controller (remove overlay) |

---

## 4. Functional Requirements

### 4.1 App Discovery (F-001)

---

#### FR-001: Retrieve Installed Applications

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-001, US-001 |
| **Description**   | The system shall query the Android PackageManager to retrieve a list of all applications installed on the device that have a launcher intent (i.e., apps that appear in the user's app drawer). |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-001.AC1 | The returned list contains only apps with `Intent.CATEGORY_LAUNCHER` resolved activities. |
| FR-001.AC2 | System apps without a launcher intent (e.g., "System UI", "Settings Provider") are excluded. |
| FR-001.AC3 | MindfulScroll itself is excluded from the list. |
| FR-001.AC4 | The query completes within 3 seconds on a device with 200+ installed apps. |

---

#### FR-002: Display App Information

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-001, US-001 |
| **Description**   | For each discovered application, the system shall retrieve and display the application's display name (label) and icon (drawable). |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-002.AC1 | Each list item shows the app's icon as resolved by `PackageManager.getApplicationIcon()`. |
| FR-002.AC2 | Each list item shows the app's display name as resolved by `PackageManager.getApplicationLabel()`. |
| FR-002.AC3 | If an app's icon cannot be resolved, a default placeholder icon is displayed. |
| FR-002.AC4 | If an app's label cannot be resolved, the package name is displayed as fallback. |

---

#### FR-003: Sort App List

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-001, F-002 |
| **Description**   | The discovered app list shall be sorted alphabetically by display name (case-insensitive) as the default ordering. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-003.AC1 | Apps are displayed in ascending alphabetical order by display name. |
| FR-003.AC2 | Sorting is case-insensitive (e.g., "facebook" and "Firefox" are sorted correctly). |
| FR-003.AC3 | Currently selected (monitored) apps appear with their selection state visible but are not separated from the sorted list. |

---

### 4.2 App Selection (F-002, F-003)

---

#### FR-004: App Selection Checklist

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-002, US-003, US-004 |
| **Description**   | The App Selection screen shall present each discovered app as a list item with a selectable checkbox. Tapping the checkbox or the list item toggles the app's monitored state. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-004.AC1 | Each app row contains: app icon, app name, and a checkbox. |
| FR-004.AC2 | Tapping the checkbox toggles the selection state (selected ↔ deselected). |
| FR-004.AC3 | Tapping anywhere on the row (not just the checkbox) also toggles selection. |
| FR-004.AC4 | The checkbox visually reflects the current selection state immediately upon tap. |
| FR-004.AC5 | Multiple apps can be selected simultaneously; there is no upper limit enforced in the UI. |

---

#### FR-005: App Search

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-002, US-002 |
| **Description**   | The App Selection screen shall include a search input field at the top. Typing in the field shall filter the visible app list in real time to show only apps whose display name contains the search query (case-insensitive substring match). |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-005.AC1 | A text input field is positioned at the top of the App Selection screen. |
| FR-005.AC2 | As the user types, the list filters in real time (within 100ms of each keystroke). |
| FR-005.AC3 | The filter performs a case-insensitive substring match against the app's display name. |
| FR-005.AC4 | Clearing the search field restores the full app list. |
| FR-005.AC5 | If no apps match the query, a "No apps found" message is displayed. |
| FR-005.AC6 | Selected apps that are filtered out retain their selection state. |

---

#### FR-006: Selection Count Indicator

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Should |
| **Traces To**     | PRD F-002 |
| **Description**   | The App Selection screen shall display a count of currently selected apps (e.g., "5 apps selected") that updates in real time as the user toggles selections. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-006.AC1 | A text label displays the count of selected apps. |
| FR-006.AC2 | The count updates immediately when a selection changes. |
| FR-006.AC3 | When count is 0, the label reads "No apps selected" or equivalent. |

---

#### FR-007: Persist App Selections

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-003, US-005 |
| **Description**   | The system shall persist the user's app selections to a local Room database. Selections shall survive app process termination, app restarts, and device reboots. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-007.AC1 | When the user selects an app, the selection is written to the Room database within 1 second. |
| FR-007.AC2 | When the user deselects an app, the deselection is written to the Room database within 1 second. |
| FR-007.AC3 | On app restart, the App Selection screen reflects the previously persisted selections. |
| FR-007.AC4 | On device reboot, the previously persisted selections are intact. |
| FR-007.AC5 | If a previously selected app has been uninstalled, it is removed from the selection list silently (no error). |

---

#### FR-008: Select All / Deselect All

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Should |
| **Traces To**     | PRD F-002 |
| **Description**   | The App Selection screen shall provide a "Select All" / "Deselect All" toggle action that applies to the currently visible (filtered) list. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-008.AC1 | When no filter is active, "Select All" selects every discovered app. |
| FR-008.AC2 | When a search filter is active, "Select All" selects only the currently visible (filtered) apps. |
| FR-008.AC3 | "Deselect All" deselects all currently visible apps. |
| FR-008.AC4 | The action does not affect apps that are not currently visible due to filtering. |

---

### 4.3 Foreground App Detection (F-004)

---

#### FR-009: Poll Foreground Application

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-004, US-013 |
| **Description**   | The monitoring service shall poll the Android UsageStatsManager at a regular interval to determine which application is currently in the foreground. The polling interval shall be between 2 and 3 seconds. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-009.AC1 | The service queries `UsageStatsManager.queryUsageStats()` or `queryEvents()` to determine the current foreground app. |
| FR-009.AC2 | The polling interval is configurable internally (default: 2 seconds). |
| FR-009.AC3 | The polling mechanism operates on a background thread/coroutine and does not block the main thread. |
| FR-009.AC4 | The detected foreground app's package name is compared against the persisted monitored app list. |
| FR-009.AC5 | If the foreground app is a monitored app, a session start or continuation event is triggered. |
| FR-009.AC6 | If the foreground app is NOT a monitored app, a session cooldown event is triggered (if a session is active). |

---

#### FR-010: Adaptive Polling

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-004, TG-1 (Battery Goal) |
| **Description**   | The polling engine shall adapt its behavior based on device screen state to minimize battery consumption. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-010.AC1 | When the screen turns off (`ACTION_SCREEN_OFF`), polling pauses completely. |
| FR-010.AC2 | When the screen turns on (`ACTION_SCREEN_ON`), polling resumes immediately. |
| FR-010.AC3 | During screen-off, no UsageStatsManager queries are executed. |
| FR-010.AC4 | The transition from screen-off to polling-active occurs within 1 second of `ACTION_SCREEN_ON`. |

---

#### FR-011: Foreground Detection Accuracy

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-004, TG-2 |
| **Description**   | The system shall detect a foreground app transition to a monitored app within 3 seconds of the actual transition event. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-011.AC1 | When a user opens a monitored app from the home screen, MindfulScroll detects it within 3 seconds. |
| FR-011.AC2 | When a user switches from a non-monitored app to a monitored app, detection occurs within 3 seconds. |
| FR-011.AC3 | When a user switches from one monitored app to a different monitored app, the switch is detected within 3 seconds. |

---

### 4.4 Session Tracking (F-005)

---

#### FR-012: Session Start

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-005, US-013 |
| **Description**   | A new usage session shall begin when the polling engine detects that a monitored application is in the foreground and no active session exists for that application. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-012.AC1 | A session record is created in memory with: app package name, session start timestamp, and initial state `ACTIVE`. |
| FR-012.AC2 | The session's elapsed time counter begins incrementing from 0. |
| FR-012.AC3 | Only one session can be in `ACTIVE` state at any given time. |
| FR-012.AC4 | If a session for a different monitored app was in `COOLDOWN` state, it transitions to `ENDED` and a new session starts for the new app. |

---

#### FR-013: Session Continuation

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-005, US-014 |
| **Description**   | If a session is in `COOLDOWN` state and the user returns to the same monitored application before the cooldown window expires, the session shall resume from its previous elapsed time. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-013.AC1 | The session state transitions from `COOLDOWN` back to `ACTIVE`. |
| FR-013.AC2 | The elapsed time counter resumes from the value it held when cooldown began (time during cooldown is NOT counted). |
| FR-013.AC3 | The overlay (if it was visible before cooldown) reappears with the same elapsed time and visual state. |
| FR-013.AC4 | The session start timestamp remains unchanged (it reflects the original session start). |

---

#### FR-014: Session Cooldown

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-005, US-014 |
| **Description**   | When the user navigates away from a monitored application while a session is active, the session shall enter a `COOLDOWN` state for a fixed duration of 45 seconds before ending. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-014.AC1 | When the foreground app changes from the monitored app to any other app, the session transitions to `COOLDOWN`. |
| FR-014.AC2 | The cooldown duration is 45 seconds (fixed for MVP; see PRD OQ-1). |
| FR-014.AC3 | During cooldown, the elapsed time counter pauses (does not increment). |
| FR-014.AC4 | During cooldown, the floating timer (if visible) dims slightly (opacity reduced by 30%) but remains on screen. |
| FR-014.AC5 | If the cooldown timer expires without the user returning, the session transitions to `ENDED`. |

---

#### FR-015: Session End

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-005, F-012 |
| **Description**   | When a session transitions to the `ENDED` state, the system shall persist the session data and remove the floating overlay. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-015.AC1 | The session record is written to the Room database with: app package name, start timestamp, end timestamp, total elapsed duration, and highest threshold stage reached. |
| FR-015.AC2 | The floating timer overlay fades out over 400ms (±100ms). |
| FR-015.AC3 | After fade-out, the overlay view is removed from the WindowManager. |
| FR-015.AC4 | All session-related in-memory state is cleared. |
| FR-015.AC5 | The database write completes successfully even if the app is in the background. |

---

#### FR-016: Screen-Off Session Behavior

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-005, P6 (Graceful Behavior) |
| **Description**   | When the device screen turns off, any active session shall enter cooldown. If the screen remains off for the full cooldown duration, the session ends. If the screen turns back on and the same monitored app is in the foreground, the session resumes. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-016.AC1 | `ACTION_SCREEN_OFF` triggers the active session to enter `COOLDOWN` state. |
| FR-016.AC2 | If the screen turns back on within the cooldown window and the same monitored app is detected in the foreground, the session resumes. |
| FR-016.AC3 | If the cooldown expires while the screen is off, the session ends and is persisted. |
| FR-016.AC4 | The overlay is removed when the screen turns off and restored (if session resumes) when the screen turns on. |

---

#### FR-017: Concurrent Session Prevention

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-005 |
| **Description**   | The system shall maintain at most one active session at any time. If the user switches from one monitored app to another, the first session enters cooldown/end and a new session starts for the second app. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-017.AC1 | Switching from monitored app A to monitored app B causes session A to enter `COOLDOWN`. |
| FR-017.AC2 | A new session for app B starts immediately, regardless of session A's cooldown state. |
| FR-017.AC3 | If session A's cooldown expires, it transitions to `ENDED` and is persisted. |
| FR-017.AC4 | At no point do two sessions have `ACTIVE` state simultaneously. |

---

### 4.5 Floating Timer Overlay (F-006, F-007, F-008, F-012)

---

#### FR-018: Overlay Display Trigger

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-006, US-019 |
| **Description**   | The floating timer overlay shall become visible when a monitored app session's elapsed time reaches 60 seconds of continuous use. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-018.AC1 | The overlay does NOT appear during the first 60 seconds of a session. |
| FR-018.AC2 | At exactly 60 seconds (±2 seconds, accounting for polling), the overlay begins its fade-in animation. |
| FR-018.AC3 | If the user leaves the monitored app before 60 seconds, the overlay never appears for that session segment. |
| FR-018.AC4 | If the session resumes after cooldown and the cumulative time was already ≥60 seconds, the overlay appears immediately upon session resumption. |

---

#### FR-019: Overlay Appearance

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-006, US-020, US-024, C-08 |
| **Description**   | The floating timer shall be a small, round overlay displaying the session's elapsed time in `MM:SS` format. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-019.AC1 | The overlay is circular with a diameter of 48–56dp. |
| FR-019.AC2 | The overlay displays elapsed time in `MM:SS` format (e.g., "01:00", "12:34"). |
| FR-019.AC3 | The time display updates every 1 second. |
| FR-019.AC4 | The elapsed time shown counts from the session start (i.e., includes the first 60 seconds before the overlay appeared). |
| FR-019.AC5 | The overlay is rendered using `WindowManager.addView()` with `TYPE_APPLICATION_OVERLAY` layout type. |
| FR-019.AC6 | The overlay window flags include `FLAG_NOT_FOCUSABLE` to prevent stealing focus from the host app. |
| FR-019.AC7 | Touch events outside the overlay's bounds pass through to the underlying app. |

---

#### FR-020: Overlay Fade-In Animation

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-006, US-024 |
| **Description**   | When the overlay first appears, it shall fade in from fully transparent to its initial calm-state opacity over a duration of 500 milliseconds. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-020.AC1 | The overlay starts at 0% opacity and animates to its calm-state opacity (defined in FR-025). |
| FR-020.AC2 | The fade-in duration is 500ms (±50ms). |
| FR-020.AC3 | The animation uses an ease-in-out interpolation curve. |

---

#### FR-021: Overlay Drag Interaction

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-007, US-021 |
| **Description**   | The user shall be able to touch and drag the floating timer to any position on the screen. The overlay shall follow the user's finger position in real time during the drag gesture. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-021.AC1 | A touch-down event on the overlay initiates a drag operation. |
| FR-021.AC2 | During drag, the overlay position updates in real time to follow the touch point. |
| FR-021.AC3 | The overlay cannot be dragged outside the visible screen bounds. |
| FR-021.AC4 | Short taps (touch duration <150ms with movement <10dp) are NOT interpreted as drags. |
| FR-021.AC5 | The drag gesture does not interfere with the host app's touch events outside the overlay's bounds. |

---

#### FR-022: Edge-Snapping Behavior

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-007, US-022 |
| **Description**   | When the user releases the overlay after dragging, the overlay shall animate to the nearest horizontal screen edge (left or right) while preserving its vertical position. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-022.AC1 | On drag release, the overlay animates horizontally to the nearer screen edge (left or right). |
| FR-022.AC2 | The vertical position (Y-coordinate) is preserved from where the user released it. |
| FR-022.AC3 | The snap animation duration is 200–300ms. |
| FR-022.AC4 | The overlay rests with its edge flush to the screen edge (not off-screen or overlapping the edge by half). |
| FR-022.AC5 | If the overlay is released exactly at the horizontal center, it snaps to the right edge (deterministic default). |

---

#### FR-023: Position Persistence

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-008, US-023 |
| **Description**   | The system shall persist the overlay's last snapped position (edge side and Y-coordinate) and restore it the next time the overlay is displayed. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-023.AC1 | After a drag-and-snap, the final position (edge: left/right, Y-coordinate as percentage of screen height) is saved to local storage. |
| FR-023.AC2 | On the next overlay display, the overlay appears at the saved position. |
| FR-023.AC3 | If no saved position exists (first ever display), the overlay appears at the right edge, vertically centered (50% screen height). |
| FR-023.AC4 | The position is stored globally (same position for all monitored apps in MVP). |

---

#### FR-024: Overlay Auto-Dismiss

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-012, US-025 |
| **Description**   | The floating timer shall be removed from the screen when the session enters `COOLDOWN` that results in `ENDED`, or when the session is explicitly ended. Removal shall be animated. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-024.AC1 | When a session transitions from `COOLDOWN` to `ENDED`, the overlay fades out over 400ms and is then removed. |
| FR-024.AC2 | During the `COOLDOWN` state, the overlay remains visible but dimmed (see FR-014.AC4). |
| FR-024.AC3 | After removal, no ghost overlay remains on screen. |
| FR-024.AC4 | If the session resumes during cooldown, the overlay returns to full opacity. |

---

### 4.6 Threshold Configuration (F-009)

---

#### FR-025: Threshold Preset Selection

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-009, US-028 |
| **Description**   | The Threshold Configuration screen shall offer two preset threshold options and a custom input option. |

**Presets:**

| Preset Name | Threshold 1 (T1) | Threshold 2 (T2) | Threshold 3 (T3) |
| ----------- | ----------------- | ----------------- | ----------------- |
| Light       | 5 minutes         | 10 minutes        | 15 minutes        |
| Moderate    | 10 minutes        | 20 minutes        | 30 minutes        |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-025.AC1 | Two preset options are displayed: "Light (5/10/15 min)" and "Moderate (10/20/30 min)". |
| FR-025.AC2 | Selecting a preset populates all three threshold values. |
| FR-025.AC3 | A "Custom" option allows manual entry of each threshold value. |
| FR-025.AC4 | The default selection (if the user has never configured) is "Moderate (10/20/30 min)". |

---

#### FR-026: Custom Threshold Input

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-009, US-029 |
| **Description**   | When the "Custom" option is selected, the user shall be able to set each of the three threshold values independently using numeric input or sliders. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-026.AC1 | Three input fields (or sliders) are presented, one for each threshold. |
| FR-026.AC2 | Each value must be a positive integer representing minutes. |
| FR-026.AC3 | Minimum allowed value for T1 is 1 minute. |
| FR-026.AC4 | Maximum allowed value for T3 is 120 minutes. |
| FR-026.AC5 | T1 < T2 < T3 must hold. The system shall enforce this constraint and display a validation error if violated. |
| FR-026.AC6 | Validation errors are shown inline, adjacent to the offending field. |

---

#### FR-027: Persist Threshold Configuration

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-009 |
| **Description**   | The selected threshold configuration (preset or custom values) shall be persisted to the local Room database and survive app restarts and device reboots. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-027.AC1 | The threshold config is saved immediately upon user confirmation (button tap). |
| FR-027.AC2 | On app restart, the Threshold Configuration screen reflects the previously saved config. |
| FR-027.AC3 | The monitoring service reads the persisted config on startup and applies it to the threshold engine. |
| FR-027.AC4 | If no config has been saved, the "Moderate (10/20/30 min)" preset is used as the default. |

---

### 4.7 Color Escalation (F-010)

---

#### FR-028: Timer Visual States

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-010, US-030 |
| **Description**   | The floating timer shall have five distinct visual states that correspond to session duration relative to the configured thresholds. |

**Visual State Definitions:**

| State     | Active When                  | Background Color    | Text Color  | Opacity |
| --------- | ---------------------------- | ------------------- | ----------- | ------- |
| **Calm**  | Timer visible, before T1     | Muted gray (#6B7280)| White       | 65%     |
| **Notice**| Elapsed time ≥ T1 and < T2  | Warm amber (#F59E0B)| Dark (#1F2937) | 78%  |
| **Alert** | Elapsed time ≥ T2 and < T3  | Orange (#F97316)    | White       | 88%     |
| **Urgent**| Elapsed time ≥ T3 and < T3+5min | Red/coral (#EF4444) | White   | 95%     |
| **Throb** | Elapsed time ≥ T3 + 5 min   | Pulsing red (#EF4444) | White   | 100%    |

> **Note:** Exact color values are preliminary. Final values will be confirmed in the UI/UX Design Specification. The color names and opacity progression are binding requirements.

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-028.AC1 | The overlay displays in the Calm state from the moment it appears (60 sec) until T1. |
| FR-028.AC2 | At T1, the overlay transitions to the Notice state. |
| FR-028.AC3 | At T2, the overlay transitions to the Alert state. |
| FR-028.AC4 | At T3, the overlay transitions to the Urgent state. |
| FR-028.AC5 | At T3 + 5 minutes, the overlay transitions to the Throb state. |
| FR-028.AC6 | Each state is visually distinct from the previous state. |

---

#### FR-029: Color Transition Animation

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-010, US-031 |
| **Description**   | When the timer transitions between visual states, the color change shall be animated as a smooth gradient transition rather than an abrupt switch. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-029.AC1 | Color transitions between states animate over 2000ms (±200ms). |
| FR-029.AC2 | The transition uses a linear interpolation between the source and target background colors. |
| FR-029.AC3 | Opacity changes are animated over the same duration as the color transition. |
| FR-029.AC4 | The text color transitions smoothly if it differs between states. |
| FR-029.AC5 | The transition does not cause visible flicker, jank, or frame drops. |

---

### 4.8 Throb Animation (F-011)

---

#### FR-030: Throb Trigger

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-011, US-032 |
| **Description**   | The throb animation shall begin exactly 5 minutes after the session elapsed time crosses the third threshold (T3). |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-030.AC1 | The throb begins at elapsed time = T3 + 5 minutes (±2 seconds). |
| FR-030.AC2 | The throb does not begin before T3 + 5 minutes under any condition. |
| FR-030.AC3 | If the session ends (cooldown expires) before T3 + 5 minutes, the throb never activates. |

---

#### FR-031: Throb Animation Specification

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-011, US-032 |
| **Description**   | The throb shall be a repeating scale animation that makes the timer appear to "breathe" — gently expanding and contracting. |

**Animation Parameters:**

| Parameter         | Value                  |
| ----------------- | ---------------------- |
| Scale range       | 1.0x → 1.15x → 1.0x  |
| Cycle duration    | 1500ms per full cycle  |
| Easing            | Ease-in-out (sinusoidal) |
| Repeat mode       | Infinite (until session ends) |
| Glow effect       | Optional subtle shadow expansion synchronized with scale |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-031.AC1 | The overlay scales between 1.0x and 1.15x in a smooth, repeating cycle. |
| FR-031.AC2 | Each full throb cycle (expand + contract) takes 1500ms. |
| FR-031.AC3 | The animation repeats indefinitely until the session enters `COOLDOWN` or `ENDED`. |
| FR-031.AC4 | The animation uses ease-in-out timing (not linear) for a natural breathing feel. |
| FR-031.AC5 | The time text remains legible during the throb (scale does not distort text beyond readability). |
| FR-031.AC6 | The throb animation does not cause dropped frames or jank on mid-range devices. |

---

### 4.9 Permission Onboarding (F-013)

---

#### FR-032: Onboarding Flow Structure

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-013, US-009, US-010, US-011 |
| **Description**   | On first launch (or when required permissions are missing), the app shall present a sequential onboarding flow that guides the user through granting each required permission. |

**Flow Sequence:**

| Step | Permission                           | Screen Content                                                            |
| ---- | ------------------------------------ | ------------------------------------------------------------------------- |
| 1    | Welcome                             | App introduction; explain purpose; [Get Started] button                   |
| 2    | `PACKAGE_USAGE_STATS`               | Explanation + [Grant Permission] → opens system Settings                  |
| 3    | `SYSTEM_ALERT_WINDOW`               | Explanation + [Grant Permission] → opens system Settings                  |
| 4    | Battery Optimization Exemption       | Explanation + [Grant Permission] → system dialog or Settings              |
| 5    | Completion                           | Success message + [Continue to App Selection]                             |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-032.AC1 | The onboarding flow is displayed on first launch when any required permission is missing. |
| FR-032.AC2 | Each permission step includes a plain-language explanation of why the permission is needed (no technical jargon). |
| FR-032.AC3 | Tapping "Grant Permission" opens the correct Android system Settings page for that specific permission. |
| FR-032.AC4 | When the user returns from system Settings, the app checks whether the permission was granted. |
| FR-032.AC5 | If the permission was granted, the UI shows a success indicator and advances to the next step. |
| FR-032.AC6 | If the permission was NOT granted, the UI shows a warning explaining what functionality will be impacted and offers a "Skip" option alongside a "Try Again" option. |
| FR-032.AC7 | The user can skip any individual permission step (except informational steps) with a clear warning. |

---

#### FR-033: Permission Status Check on App Resume

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-013, US-012 |
| **Description**   | Each time the MindfulScroll app is brought to the foreground, the system shall check the status of all required permissions and alert the user if any critical permission has been revoked. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-033.AC1 | On `onResume()`, the app checks `PACKAGE_USAGE_STATS` and `SYSTEM_ALERT_WINDOW` permission status. |
| FR-033.AC2 | If `PACKAGE_USAGE_STATS` is missing, a prominent banner is displayed: "Usage Access permission is required for monitoring. Tap to grant." |
| FR-033.AC3 | If `SYSTEM_ALERT_WINDOW` is missing, a banner warns: "Overlay permission is missing. The floating timer will not appear." |
| FR-033.AC4 | Tapping the banner navigates directly to the relevant system Settings page. |
| FR-033.AC5 | If both critical permissions are granted, no banner is shown. |

---

#### FR-034: Deep-Link to System Settings

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-013, US-010 |
| **Description**   | Permission grant actions shall open the exact system Settings page for the requested permission, not a generic Settings screen. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-034.AC1 | Usage Access: Opens `Settings.ACTION_USAGE_ACCESS_SETTINGS`. |
| FR-034.AC2 | Overlay: Opens `Settings.ACTION_MANAGE_OVERLAY_PERMISSION` with the app's package URI. |
| FR-034.AC3 | Battery Optimization: Uses `ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` with the app's package URI. |
| FR-034.AC4 | If a specific intent cannot be resolved (OEM customization), fall back to `Settings.ACTION_SETTINGS` with a toast instructing the user. |

---

### 4.10 Foreground Service (F-014)

---

#### FR-035: Service Lifecycle

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-014, US-016 |
| **Description**   | The monitoring engine shall run inside a foreground service that starts when the user enables monitoring and persists until the user explicitly disables it or the app is force-stopped. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-035.AC1 | The service starts via `startForegroundService()` when monitoring is activated. |
| FR-035.AC2 | The service calls `startForeground()` within 5 seconds with a valid notification. |
| FR-035.AC3 | The service continues running when the user navigates away from MindfulScroll. |
| FR-035.AC4 | The service continues running when MindfulScroll's activity is destroyed (back press, recents swipe). |
| FR-035.AC5 | The service stops only when: (a) the user explicitly disables monitoring from within the app, or (b) the app is force-stopped. |
| FR-035.AC6 | The service type is declared as `foregroundServiceType="specialUse"` in the manifest (API 34+). |

---

#### FR-036: Foreground Service Notification

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-014, US-016, C-13 |
| **Description**   | The foreground service shall display a persistent notification informing the user that MindfulScroll is actively monitoring. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-036.AC1 | The notification displays the text "MindfulScroll is monitoring" (or equivalent). |
| FR-036.AC2 | The notification includes the MindfulScroll app icon (small icon). |
| FR-036.AC3 | Tapping the notification opens MindfulScroll's main activity. |
| FR-036.AC4 | The notification uses a low-importance notification channel (no sound, no vibration, minimal visual prominence). |
| FR-036.AC5 | The notification is displayed in a dedicated notification channel named "Monitoring Service". |
| FR-036.AC6 | The notification includes a "Stop Monitoring" action button that stops the service. |

---

#### FR-037: Service Self-Recovery

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Should |
| **Traces To**     | PRD F-014, P6 (Graceful Behavior), R-02 |
| **Description**   | If the foreground service is killed by the Android OS (due to memory pressure or OEM battery optimization), the system shall attempt to restart it automatically. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-037.AC1 | The service's `onStartCommand()` returns `START_STICKY`. |
| FR-037.AC2 | On service restart, the service restores its monitoring state from persisted data (monitored apps list, threshold config). |
| FR-037.AC3 | Any in-progress session at the time of the kill is lost (acceptable for MVP; not persisted mid-session). |
| FR-037.AC4 | The foreground notification is re-established upon restart. |

---

### 4.11 Service Auto-Restart on Boot (F-015)

---

#### FR-038: Boot-Completed Receiver

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-015, US-015 |
| **Description**   | The system shall register a `BroadcastReceiver` for the `BOOT_COMPLETED` event. When received, and if monitoring was active before the reboot, the foreground service shall be started. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-038.AC1 | A `BroadcastReceiver` is declared in the manifest with `RECEIVE_BOOT_COMPLETED` intent filter. |
| FR-038.AC2 | On `BOOT_COMPLETED`, the receiver checks a persisted flag indicating whether monitoring was active. |
| FR-038.AC3 | If the flag is true, the receiver starts the foreground service via `startForegroundService()`. |
| FR-038.AC4 | If the flag is false, no service is started. |
| FR-038.AC5 | The "monitoring active" flag is set to `true` when the service starts and `false` when the user explicitly stops monitoring. |
| FR-038.AC6 | The service is started within 10 seconds of the `BOOT_COMPLETED` broadcast. |

---

### 4.12 Home Screen (MVP)

---

#### FR-039: Home Screen Content

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-001 through F-015 (aggregate) |
| **Description**   | The Home screen serves as the main landing screen after onboarding. It displays the current monitoring status and provides access to key actions. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-039.AC1 | The screen displays the monitoring status: "Monitoring Active" with a green indicator, or "Monitoring Inactive" with a gray indicator. |
| FR-039.AC2 | The screen displays the count of currently monitored apps (e.g., "Monitoring 7 apps"). |
| FR-039.AC3 | The screen displays the current threshold configuration in human-readable form (e.g., "Thresholds: 10 / 20 / 30 min"). |
| FR-039.AC4 | A prominent "Start Monitoring" / "Stop Monitoring" toggle or button is available. |
| FR-039.AC5 | Navigation is available to: App Selection screen, Threshold Configuration screen, and Settings/About. |

---

#### FR-040: Start/Stop Monitoring Toggle

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD F-014 |
| **Description**   | The Home screen shall provide a control to start or stop the monitoring service. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-040.AC1 | Tapping "Start Monitoring" starts the foreground service and updates the UI to reflect "Monitoring Active". |
| FR-040.AC2 | Tapping "Stop Monitoring" stops the foreground service, ends any active session, removes any visible overlay, and updates the UI to "Monitoring Inactive". |
| FR-040.AC3 | If required permissions are missing when the user taps "Start Monitoring", the app navigates to the onboarding/permission flow instead of starting the service. |
| FR-040.AC4 | The toggle state is persisted so it survives app restarts. |

---

### 4.13 Navigation

---

#### FR-041: App Navigation Structure

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD Section 10 (User Flows) |
| **Description**   | The app shall use a single-activity architecture with Jetpack Compose navigation. |

**Screen Graph:**

```
Onboarding Flow (conditional, first-launch or missing permissions)
    Welcome → Permission Steps → App Selection → Threshold Config
    │
    ▼
Home Screen (main landing after onboarding)
    ├── App Selection Screen (edit monitored apps)
    ├── Threshold Configuration Screen (edit thresholds)
    └── About Screen (app info, privacy policy link)
```

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| FR-041.AC1 | Navigation uses Jetpack Compose Navigation (NavHost + NavController). |
| FR-041.AC2 | The back button behaves predictably: returns to the previous screen in the navigation stack. |
| FR-041.AC3 | On first launch with missing permissions, the app opens to the onboarding flow. |
| FR-041.AC4 | On subsequent launches with all permissions granted, the app opens to the Home screen. |
| FR-041.AC5 | The onboarding flow is not shown again once all permissions are granted and initial setup is complete. |

---

## 5. Non-Functional Requirements

### 5.1 Performance

---

#### NFR-001: App Startup Time

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | General quality |
| **Description**   | The app shall reach an interactive state (first frame rendered, user can interact) within 2 seconds of a cold start on a mid-range device. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-001.AC1 | Cold start to first interactive frame ≤ 2000ms measured on a device with Snapdragon 600-series or equivalent. |
| NFR-001.AC2 | Warm start (from recents) to interactive frame ≤ 500ms. |

---

#### NFR-002: Battery Consumption

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD TG-1, P5 (Battery Respect) |
| **Description**   | The foreground service, including polling and overlay rendering, shall consume no more than 2% of battery per hour of active monitoring. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-002.AC1 | Battery drain from MindfulScroll's monitoring service ≤ 2% per hour, measured over an 8-hour continuous monitoring session on a Pixel device with a 4000+ mAh battery. |
| NFR-002.AC2 | When the screen is off, battery drain from MindfulScroll approaches 0% (polling is paused). |
| NFR-002.AC3 | MindfulScroll does not appear in the top 5 battery consumers in Android's battery usage statistics during normal operation. |

---

#### NFR-003: Memory Consumption

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | General quality |
| **Description**   | MindfulScroll's total memory footprint (app + service) shall not exceed 80MB during normal operation. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-003.AC1 | Heap allocation ≤ 50MB during normal operation (monitoring active, overlay visible). |
| NFR-003.AC2 | Total PSS (Proportional Set Size) ≤ 80MB as reported by `adb shell dumpsys meminfo`. |
| NFR-003.AC3 | No memory leaks: memory does not grow unboundedly over 8 hours of continuous operation. |

---

#### NFR-004: Overlay Rendering Performance

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD TG-3 |
| **Description**   | The floating timer overlay shall render smoothly without frame drops, jank, or visible lag during all animations (fade-in, color transition, throb, drag). |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-004.AC1 | Overlay animations maintain ≥ 55 FPS on a mid-range device. |
| NFR-004.AC2 | The 1-second time update does not cause a visible frame drop. |
| NFR-004.AC3 | Drag interaction has zero perceptible input lag (overlay tracks finger within the same frame). |

---

#### NFR-005: Database Query Performance

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Should |
| **Traces To**     | General quality |
| **Description**   | All database read operations used during active monitoring (e.g., loading the monitored app list) shall complete within 100ms. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-005.AC1 | Loading the monitored app list from Room ≤ 100ms. |
| NFR-005.AC2 | Loading the threshold configuration from Room ≤ 50ms. |
| NFR-005.AC3 | Writing a session record ≤ 100ms. |
| NFR-005.AC4 | All DB operations run on a background thread; no DB access on the main thread. |

---

### 5.2 Reliability

---

#### NFR-006: Service Uptime

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD TG-4 |
| **Description**   | The foreground service shall remain running for at least 95% of the time monitoring is expected to be active, across major Android OEMs. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-006.AC1 | On Google Pixel devices, service uptime ≥ 99% over a 24-hour period with screen on/off cycles. |
| NFR-006.AC2 | On Samsung (One UI) devices, service uptime ≥ 95% when battery optimization is exempted. |
| NFR-006.AC3 | On Xiaomi (MIUI) devices, service uptime ≥ 90% when battery optimization is exempted and autostart is enabled. |
| NFR-006.AC4 | On all devices, if the service is killed, it restarts within 60 seconds (via `START_STICKY` or alarm). |

---

#### NFR-007: Crash-Free Rate

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | General quality |
| **Description**   | The app shall maintain a crash-free session rate of 99.5% or higher. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-007.AC1 | No unhandled exceptions in the monitoring service. Unexpected errors are caught and logged, not thrown. |
| NFR-007.AC2 | No crashes when permissions are revoked at runtime. |
| NFR-007.AC3 | No crashes when a monitored app is uninstalled while monitoring is active. |

---

#### NFR-008: Data Integrity

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | General quality |
| **Description**   | Persisted data (app selections, threshold configs, session records) shall not be corrupted or lost under normal operating conditions. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-008.AC1 | Room database uses WAL (Write-Ahead Logging) mode to prevent corruption from simultaneous reads/writes. |
| NFR-008.AC2 | Database writes use transactions for multi-row operations. |
| NFR-008.AC3 | A database version migration strategy is defined (even if only one version exists in MVP). |

---

### 5.3 Usability

---

#### NFR-009: Onboarding Completion Rate

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Should |
| **Traces To**     | PRD UG-3, R-03 |
| **Description**   | The onboarding flow shall be designed such that at least 80% of users who begin it complete all permission grants. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-009.AC1 | Each permission step uses plain language (no Android API names or technical terms). |
| NFR-009.AC2 | Each permission step includes a visual illustration or icon showing what the permission enables. |
| NFR-009.AC3 | The flow is completable in under 90 seconds (internal testing benchmark). |

---

#### NFR-010: Accessibility

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Should |
| **Traces To**     | PRD C-10, General quality |
| **Description**   | The app shall meet WCAG 2.1 Level AA contrast requirements for all text displayed in UI screens. The overlay's threshold color states shall be distinguishable without relying solely on color. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-010.AC1 | All in-app text has a contrast ratio of at least 4.5:1 against its background. |
| NFR-010.AC2 | All interactive elements have a minimum touch target size of 48dp × 48dp. |
| NFR-010.AC3 | All UI screens support Android TalkBack (content descriptions on actionable elements). |
| NFR-010.AC4 | The overlay timer text maintains ≥ 3:1 contrast ratio against its background in all visual states. |

---

### 5.4 Security & Privacy

---

#### NFR-011: Data Locality

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD C-11, C-12, P4 (Transparency) |
| **Description**   | All user data shall be stored exclusively on the local device. The app shall make zero network calls. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-011.AC1 | The app declares no `INTERNET` permission in the manifest. |
| NFR-011.AC2 | No network-related libraries (OkHttp, Retrofit, Ktor, etc.) are included in dependencies. |
| NFR-011.AC3 | Static analysis (lint) confirms zero network API usage in the codebase. |

---

#### NFR-012: Data Protection

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD P4 (Transparency) |
| **Description**   | Stored data shall be protected using Android's default app sandbox. No sensitive data shall be written to external storage or shared with other apps. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-012.AC1 | The Room database is stored in the app's private internal storage directory. |
| NFR-012.AC2 | No data is written to external storage (SD card, shared directories). |
| NFR-012.AC3 | No `ContentProvider` is exported. |
| NFR-012.AC4 | No `Activity` is exported except the main launcher activity. |
| NFR-012.AC5 | `android:allowBackup` is set to `false` or backup rules exclude the database. |

---

### 5.5 Maintainability

---

#### NFR-013: Code Architecture

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | General quality |
| **Description**   | The codebase shall follow Clean Architecture with clear separation between presentation, domain, and data layers. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-013.AC1 | The domain layer has no Android framework dependencies (pure Kotlin). |
| NFR-013.AC2 | The presentation layer depends on the domain layer, not directly on the data layer. |
| NFR-013.AC3 | Repository interfaces are defined in the domain layer and implemented in the data layer. |
| NFR-013.AC4 | Dependency injection is used throughout (Hilt or Koin). |

---

#### NFR-014: Test Coverage

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Should |
| **Traces To**     | General quality |
| **Description**   | The domain layer (use cases, session state machine, threshold engine) shall have unit test coverage of at least 80%. |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-014.AC1 | Session state machine transitions are covered by unit tests for all valid transitions. |
| NFR-014.AC2 | Threshold evaluation logic is covered by unit tests for all states (Calm → Notice → Alert → Urgent → Throb). |
| NFR-014.AC3 | Edge cases (cooldown resumption, concurrent session prevention, screen-off behavior) have dedicated test cases. |

---

### 5.6 Compatibility

---

#### NFR-015: Android Version Compatibility

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD Section 2.3 |
| **Description**   | The app shall function correctly on all Android versions from API 26 (8.0) through API 36 (15). |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-015.AC1 | The app compiles and runs without crashes on API 26, 28, 29, 30, 31, 33, 34, 35, and 36. |
| NFR-015.AC2 | Foreground service type declarations are handled conditionally for API 34+ (where `foregroundServiceType` is mandatory). |
| NFR-015.AC3 | Notification channel creation is handled correctly (API 26+). |
| NFR-015.AC4 | Overlay type uses `TYPE_APPLICATION_OVERLAY` (API 26+). |

---

#### NFR-016: OEM Compatibility

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Traces To**     | PRD TG-4, R-02 |
| **Description**   | The app shall be tested and function on the following OEM Android skins, which represent the majority of the Android device market. |

**Target OEMs:**

| OEM      | Skin       | Priority |
| -------- | ---------- | -------- |
| Google   | Stock AOSP | Must     |
| Samsung  | One UI     | Must     |
| Xiaomi   | MIUI / HyperOS | Must |
| OnePlus  | OxygenOS   | Should   |
| Oppo     | ColorOS    | Could    |
| Huawei   | EMUI       | Could    |

**Acceptance Criteria:**

| ID         | Criterion |
| ---------- | --------- |
| NFR-016.AC1 | On all "Must" OEMs, the foreground service remains running for 8+ hours with battery optimization exempted. |
| NFR-016.AC2 | On all "Must" OEMs, the overlay displays correctly over third-party apps. |
| NFR-016.AC3 | On all "Must" OEMs, the boot receiver successfully restarts the service after reboot. |

---

## 6. External Interface Requirements

### 6.1 Android System API Interfaces

---

#### EIR-001: UsageStatsManager Interface

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Description**   | The app interfaces with `android.app.usage.UsageStatsManager` to query foreground application data. |

**Specifications:**

| Parameter            | Value |
| -------------------- | ----- |
| API method           | `queryEvents(beginTime, endTime)` with `MOVE_TO_FOREGROUND` event type |
| Query window         | Last 5 seconds (aligned with polling interval) |
| Fallback method      | `queryUsageStats(INTERVAL_DAILY, ...)` with `lastTimeUsed` comparison |
| Required permission  | `PACKAGE_USAGE_STATS` |

---

#### EIR-002: WindowManager Overlay Interface

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Description**   | The app interfaces with `android.view.WindowManager` to add, update, and remove the floating timer overlay. |

**Specifications:**

| Parameter                    | Value |
| ---------------------------- | ----- |
| Window type                  | `WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY` |
| Flags                        | `FLAG_NOT_FOCUSABLE \| FLAG_NOT_TOUCH_MODAL \| FLAG_LAYOUT_NO_LIMITS` |
| Touch handling               | Overlay consumes touch events only within its bounds (drag handling) |
| Required permission          | `SYSTEM_ALERT_WINDOW` |

---

#### EIR-003: PackageManager Interface

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Must |
| **Description**   | The app interfaces with `android.content.pm.PackageManager` to discover installed applications. |

**Specifications:**

| Parameter            | Value |
| -------------------- | ----- |
| Query method         | `queryIntentActivities()` with `Intent(ACTION_MAIN).addCategory(CATEGORY_LAUNCHER)` |
| Data retrieved       | Package name, application label, application icon |
| Filtering            | Excludes MindfulScroll's own package |

---

### 6.2 User Interface Screens

---

#### EIR-004: Screen Inventory

| Screen                        | Entry Point                    | Purpose                                  |
| ----------------------------- | ------------------------------ | ---------------------------------------- |
| Welcome Screen                | First launch                   | Introduce app, begin onboarding          |
| Permission Step Screens (×3)  | Onboarding flow                | Guide permission grants                  |
| App Selection Screen          | Onboarding or Home navigation  | Select/deselect apps to monitor          |
| Threshold Configuration Screen| Onboarding or Home navigation  | Configure threshold values               |
| Home Screen                   | Post-onboarding default        | Monitoring status, quick actions         |
| About Screen                  | Home navigation                | App version, privacy policy link, credits|
| Floating Timer Overlay        | Service-managed                | Display elapsed time over monitored app  |

---

## 7. Data Requirements

### 7.1 Data Entities

---

#### DR-001: MonitoredApp Entity

| Field             | Type       | Constraints                              | Description                                  |
| ----------------- | ---------- | ---------------------------------------- | -------------------------------------------- |
| `packageName`     | String     | Primary key, not null                    | Android package name (e.g., "com.instagram.android") |
| `displayName`     | String     | Not null                                 | Human-readable app name                      |
| `isActive`        | Boolean    | Not null, default true                   | Whether currently selected for monitoring    |
| `addedAt`         | Long       | Not null                                 | Timestamp when first selected (epoch millis) |

---

#### DR-002: ThresholdConfig Entity

| Field             | Type       | Constraints                              | Description                                  |
| ----------------- | ---------- | ---------------------------------------- | -------------------------------------------- |
| `id`              | Int        | Primary key, auto-generate               | Unique identifier                            |
| `presetName`      | String     | Nullable                                 | "Light", "Moderate", or null (custom)        |
| `threshold1Min`   | Int        | Not null, ≥ 1                            | First threshold in minutes                   |
| `threshold2Min`   | Int        | Not null, > threshold1Min                | Second threshold in minutes                  |
| `threshold3Min`   | Int        | Not null, > threshold2Min, ≤ 120         | Third threshold in minutes                   |
| `isGlobal`        | Boolean    | Not null, default true                   | Whether this is the global default config    |
| `appPackageName`  | String     | Nullable, FK to MonitoredApp             | If non-null, this is a per-app override (v1.1) |

---

#### DR-003: UsageSession Entity

| Field                | Type       | Constraints                              | Description                                  |
| -------------------- | ---------- | ---------------------------------------- | -------------------------------------------- |
| `id`                 | Long       | Primary key, auto-generate               | Unique session identifier                    |
| `appPackageName`     | String     | Not null, FK to MonitoredApp             | Package name of the monitored app            |
| `startTimestamp`     | Long       | Not null                                 | Session start time (epoch millis)            |
| `endTimestamp`       | Long       | Not null                                 | Session end time (epoch millis)              |
| `durationSeconds`    | Int        | Not null, ≥ 0                            | Total active duration in seconds (excluding cooldown pauses) |
| `maxThresholdReached`| Int        | Not null, 0–4                            | Highest visual state reached (0=below timer, 1=Calm, 2=Notice, 3=Alert, 4=Urgent/Throb) |
| `throbActivated`     | Boolean    | Not null, default false                  | Whether the throb animation was triggered    |

---

#### DR-004: UserPreferences Entity

| Field             | Type       | Constraints                              | Description                                  |
| ----------------- | ---------- | ---------------------------------------- | -------------------------------------------- |
| `key`             | String     | Primary key                              | Preference key name                          |
| `value`           | String     | Not null                                 | Preference value (stored as string, parsed by consumer) |

**Required Preference Keys (MVP):**

| Key                          | Type    | Default        | Description                              |
| ---------------------------- | ------- | -------------- | ---------------------------------------- |
| `monitoring_active`          | Boolean | false          | Whether monitoring service should be running |
| `onboarding_completed`       | Boolean | false          | Whether first-launch onboarding is done  |
| `overlay_position_edge`      | String  | "right"        | Last snapped edge ("left" or "right")    |
| `overlay_position_y_percent` | Float   | 0.5            | Last Y position as fraction of screen height |
| `cooldown_duration_seconds`  | Int     | 45             | Cooldown window duration                 |

---

### 7.2 Data Retention

---

#### DR-005: Session Data Retention

| Field             | Value |
| ----------------- | ----- |
| **Priority**      | Should |
| **Description**   | Usage session records shall be retained for 90 days. Records older than 90 days shall be automatically deleted during app startup. |

**Acceptance Criteria:**

| ID        | Criterion |
| --------- | --------- |
| DR-005.AC1 | On each app startup, a cleanup query deletes session records with `endTimestamp` older than 90 days. |
| DR-005.AC2 | The cleanup runs on a background thread and does not delay app startup. |
| DR-005.AC3 | The retention period is defined as a constant, easily changeable for future versions. |

---

### 7.3 Data Volume Estimates

| Entity           | Estimated Records (90 days) | Estimated Size |
| ---------------- | --------------------------- | -------------- |
| MonitoredApp     | 1–30                        | < 5 KB         |
| ThresholdConfig  | 1–30                        | < 3 KB         |
| UsageSession     | 500–5,000                   | 100–500 KB     |
| UserPreferences  | 5–15                        | < 1 KB         |
| **Total**        |                             | **< 600 KB**   |

---

## 8. Platform & Environment Requirements

---

#### PER-001: Build Environment

| Parameter               | Requirement                                        |
| ----------------------- | -------------------------------------------------- |
| Android Gradle Plugin   | 8.13.x or compatible                               |
| Kotlin version          | 2.0.x                                              |
| JVM target              | 11                                                 |
| Gradle version          | 8.13.x                                             |
| Compose BOM             | 2024.09.00 or later                                |
| Min SDK                 | 26                                                 |
| Target SDK              | 36                                                 |
| Compile SDK             | 36                                                 |

---

#### PER-002: Runtime Requirements

| Parameter               | Requirement                                        |
| ----------------------- | -------------------------------------------------- |
| RAM                     | Minimum 2 GB device RAM                            |
| Storage                 | < 20 MB installed app size; < 1 MB data storage    |
| Google Play Services    | NOT required (app is GMS-independent)              |
| Root access             | NOT required                                       |

---

## 9. Permission Requirements

---

#### PMR-001: Permission Manifest

| Permission                              | Android Name                               | Protection Level | Required For              | Grants At      |
| --------------------------------------- | ------------------------------------------ | ---------------- | ------------------------- | -------------- |
| Usage Access                            | `PACKAGE_USAGE_STATS`                      | Signature\|Privileged\|AppOp | FR-009 (Polling)   | System Settings (manual) |
| Display Over Other Apps                 | `SYSTEM_ALERT_WINDOW`                      | Signature\|AppOp | FR-018 (Overlay)          | System Settings (manual) |
| Foreground Service                      | `FOREGROUND_SERVICE`                       | Normal           | FR-035 (Service)          | Auto-granted at install |
| Foreground Service Special Use          | `FOREGROUND_SERVICE_SPECIAL_USE`           | Normal           | FR-035 (Service, API 34+) | Auto-granted at install |
| Receive Boot Completed                  | `RECEIVE_BOOT_COMPLETED`                   | Normal           | FR-038 (Boot restart)     | Auto-granted at install |
| Request Ignore Battery Optimizations    | `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`     | Normal           | NFR-006 (Reliability)     | System dialog (prompted) |
| Post Notifications (API 33+)            | `POST_NOTIFICATIONS`                       | Dangerous        | FR-036 (Notification)     | Runtime prompt (API 33+) |

---

#### PMR-002: Graceful Degradation Matrix

| Permission Missing              | Impact                                              | App Behavior                              |
| ------------------------------- | --------------------------------------------------- | ----------------------------------------- |
| `PACKAGE_USAGE_STATS`           | Cannot detect foreground app                        | Monitoring is non-functional; show banner urging grant |
| `SYSTEM_ALERT_WINDOW`           | Cannot display floating timer                       | Monitoring runs, sessions are tracked, but overlay does not appear; degrade to notification updates |
| Battery optimization exemption  | Service may be killed by OS                         | Monitoring functions but may stop unexpectedly; show informational banner |
| `POST_NOTIFICATIONS`            | Cannot show foreground service notification (API 33+) | Service may not start on API 33+; must request |

---

## 10. State Machine Definitions

### 10.1 Session State Machine

```
                            ┌──────────────┐
                            │   INACTIVE   │
                            │ (No session) │
                            └──────┬───────┘
                                   │
                    Monitored app detected in foreground
                                   │
                                   ▼
                            ┌──────────────┐
                   ┌───────►│    ACTIVE    │◄──────────┐
                   │        │  (Counting)  │           │
                   │        └──────┬───────┘           │
                   │               │                   │
                   │    Foreground app changes OR       │
                   │    screen turns off                │
                   │               │                   │
                   │               ▼                   │
                   │        ┌──────────────┐           │
                   │        │   COOLDOWN   │           │
 Same app returns  │        │  (Waiting)   │    Same app returns
 within cooldown   │        └──────┬───────┘    within cooldown
 window            │               │
                   │               │
                   └───────────────┤
                                   │
                         Cooldown expires OR
                         different monitored app
                         detected
                                   │
                                   ▼
                            ┌──────────────┐
                            │    ENDED     │
                            │  (Persist)   │
                            └──────┬───────┘
                                   │
                            Session saved to DB
                            Overlay removed
                                   │
                                   ▼
                            ┌──────────────┐
                            │   INACTIVE   │
                            └──────────────┘
```

**Transition Rules:**

| From       | To         | Trigger                                                         |
| ---------- | ---------- | --------------------------------------------------------------- |
| INACTIVE   | ACTIVE     | Polling detects a monitored app in the foreground                |
| ACTIVE     | COOLDOWN   | Foreground app changes to a non-monitored app or screen turns off |
| COOLDOWN   | ACTIVE     | Same monitored app detected in foreground before cooldown expires |
| COOLDOWN   | ENDED      | Cooldown timer expires (45 seconds) without the monitored app returning |
| COOLDOWN   | ENDED      | A different monitored app is detected (triggers new ACTIVE for the new app) |
| ACTIVE     | ENDED      | User explicitly stops monitoring from the app                    |
| ENDED      | INACTIVE   | Session data persisted, overlay removed, state cleared           |

---

### 10.2 Timer Visual State Machine

```
    Session starts (overlay not visible yet)
                │
                │  elapsed ≥ 60 sec
                ▼
         ┌──────────┐
         │   CALM   │───── elapsed ≥ T1 ────►┌──────────┐
         │ (Gray)   │                         │  NOTICE  │
         └──────────┘                         │ (Amber)  │
                                              └────┬─────┘
                                                   │
                                          elapsed ≥ T2
                                                   │
                                                   ▼
                                              ┌──────────┐
                                              │  ALERT   │
                                              │ (Orange) │
                                              └────┬─────┘
                                                   │
                                          elapsed ≥ T3
                                                   │
                                                   ▼
                                              ┌──────────┐
                                              │  URGENT  │
                                              │  (Red)   │
                                              └────┬─────┘
                                                   │
                                       elapsed ≥ T3 + 5 min
                                                   │
                                                   ▼
                                              ┌──────────┐
                                              │  THROB   │
                                              │(Pulsing) │
                                              └──────────┘
```

**State Rules:**

- Transitions are **one-directional** — the timer only escalates, never de-escalates within a session.
- If the session resumes after cooldown, the visual state is restored to whatever it was before cooldown.
- Color transitions between states are animated (FR-029).
- The throb state persists indefinitely until the session ends.

---

### 10.3 Monitoring Service State Machine

```
         ┌────────────┐
         │  STOPPED   │
         └─────┬──────┘
               │
      User taps "Start Monitoring"
      (permissions verified)
               │
               ▼
         ┌────────────┐
         │  RUNNING   │◄─── BOOT_COMPLETED (if flag is true)
         │ (Polling)  │◄─── START_STICKY restart
         └─────┬──────┘
               │
      User taps "Stop Monitoring"
      OR app is force-stopped
               │
               ▼
         ┌────────────┐
         │  STOPPED   │
         └────────────┘
```

---

## 11. Error Handling & Edge Cases

### 11.1 Error Handling Requirements

---

#### EH-001: Monitored App Uninstalled

| Scenario          | The user uninstalls an app that is currently in the monitored list. |
| ----------------- | ------------------------------------------------------------------ |
| **Detection**     | On next polling cycle or app selection screen load, the package is no longer resolvable. |
| **Response**      | Remove the app from the monitored list silently. If an active session existed for that app, end it immediately. |
| **User Impact**   | None visible (silent cleanup). The app disappears from the selection list on next visit. |

---

#### EH-002: Permission Revoked at Runtime

| Scenario          | The user revokes Usage Access or Overlay permission while monitoring is active. |
| ----------------- | ------------------------------------------------------------------------------- |
| **Detection**     | Permission check fails on next polling cycle or overlay operation.               |
| **Response**      | For Usage Access: pause monitoring, update notification to indicate degraded state. For Overlay: continue monitoring but do not attempt to show overlay. |
| **User Impact**   | On next app open, a permission banner is shown (FR-033).                         |

---

#### EH-003: Service Killed by OS

| Scenario          | The Android OS or OEM battery management kills the foreground service. |
| ----------------- | --------------------------------------------------------------------- |
| **Detection**     | `START_STICKY` triggers service restart by the system.                 |
| **Response**      | Service restarts, reloads monitoring config from DB, resumes polling. Any in-progress session is lost. |
| **User Impact**   | Brief gap in monitoring (usually 10–60 seconds). No user notification for the gap. |

---

#### EH-004: Overlay Window Failure

| Scenario          | `WindowManager.addView()` throws an exception (e.g., due to permission or window token issues). |
| ----------------- | ------------------------------------------------------------------------------------------------ |
| **Detection**     | Try-catch around `addView()`.                                                                    |
| **Response**      | Log the error. Do not crash. Continue monitoring without the overlay. Retry on the next session. |
| **User Impact**   | Timer does not appear for the current session. Monitoring still functions (sessions are logged). |

---

#### EH-005: UsageStatsManager Returns Empty Data

| Scenario          | `queryEvents()` or `queryUsageStats()` returns no data (can happen if permission was recently granted or on some OEMs). |
| ----------------- | ----------------------------------------------------------------------------------------------------------------------- |
| **Detection**     | Empty result set from the query.                                                                                         |
| **Response**      | Treat as "no monitored app in foreground." Do not start or continue a session. Retry on next polling cycle.              |
| **User Impact**   | Detection may be delayed or missed for one polling interval.                                                             |

---

#### EH-006: Database Write Failure

| Scenario          | Room throws an exception during a write operation (e.g., disk full). |
| ----------------- | -------------------------------------------------------------------- |
| **Detection**     | Exception from Room DAO call.                                        |
| **Response**      | Log the error. Do not crash. In-memory state continues to function. Retry the write on the next opportunity. |
| **User Impact**   | Session data may be lost for the affected write. No visible error to the user. |

---

#### EH-007: Device Rotation During Overlay

| Scenario          | The user rotates the device while the floating timer overlay is visible. |
| ----------------- | ----------------------------------------------------------------------- |
| **Detection**     | Configuration change callback in the service.                           |
| **Response**      | Recalculate the overlay position based on the new screen dimensions. Re-apply the saved Y-percentage to the new screen height. Snap to the saved edge. |
| **User Impact**   | The timer briefly repositions. No interruption to the session. |

---

#### EH-008: Split Screen / Multi-Window

| Scenario          | The user enters split-screen mode with a monitored app in one half. |
| ----------------- | ------------------------------------------------------------------ |
| **Detection**     | UsageStatsManager still reports the focused window's app.          |
| **Response**      | Monitor the app that has input focus. The overlay appears over the full screen (as it uses `TYPE_APPLICATION_OVERLAY`). |
| **User Impact**   | Timer is visible over split-screen. Monitoring works for the focused app. |

---

### 11.2 Edge Case Summary

| ID    | Edge Case                                      | Expected Behavior                                                | Traces To |
| ----- | ---------------------------------------------- | ---------------------------------------------------------------- | --------- |
| EC-01 | User opens monitored app for 59 seconds        | No overlay is shown. Session is tracked internally but not logged (below minimum). | FR-018 |
| EC-02 | User opens monitored app, immediately switches  | Session starts and enters cooldown. If cooldown expires, session is discarded (duration < 60s, not persisted). | FR-012, FR-014 |
| EC-03 | Phone call interrupts monitored app usage       | Session enters cooldown. If user returns within cooldown, session resumes. | FR-014 |
| EC-04 | User has no apps selected but starts monitoring | Service starts but takes no action (no apps to match against). | FR-040 |
| EC-05 | Device clock changes (timezone, manual change)  | Session duration uses `SystemClock.elapsedRealtime()`, immune to wall-clock changes. | FR-012 |
| EC-06 | 100+ apps installed on device                   | App list loads within 3 seconds; list scrolls smoothly. | FR-001 |
| EC-07 | User drags overlay to exact screen center       | Overlay snaps to the right edge (deterministic default). | FR-022 |
| EC-08 | Threshold values: T1=1, T2=2, T3=3 (minimum)   | Timer escalates rapidly. All visual states are shown. Throb at 8 minutes. | FR-026 |
| EC-09 | Threshold values: T1=40, T2=80, T3=120 (maximum)| Timer stays in Calm state for 39 minutes. All states eventually reached. | FR-026 |
| EC-10 | Session spans midnight (date boundary)          | Session is recorded with correct start and end timestamps. No data corruption. | DR-003 |

---

## 12. Acceptance Criteria Summary

### 12.1 MVP Feature Acceptance Matrix

This matrix summarizes the key acceptance criteria for each MVP feature. Detailed criteria are in Section 4.

| PRD Feature | Key Requirement(s) | Critical Acceptance Criteria |
| ----------- | ------------------- | ---------------------------- |
| F-001       | FR-001, FR-002, FR-003 | Launcher apps listed with icons and names; sorted alphabetically; excludes system and self |
| F-002       | FR-004, FR-005, FR-006, FR-008 | Checklist with search; real-time filter; selection count; select all/deselect all |
| F-003       | FR-007              | Selections persist across restart and reboot; handles uninstalled apps |
| F-004       | FR-009, FR-010, FR-011 | Polls every 2–3 sec; adaptive (screen on/off); detects within 3 sec |
| F-005       | FR-012–FR-017       | Session start/continue/cooldown/end; 45-sec cooldown; screen-off handling; no concurrent sessions |
| F-006       | FR-018–FR-020       | Appears at 60 sec; round 48–56dp; MM:SS format; fades in over 500ms |
| F-007       | FR-021, FR-022      | Draggable; edge-snapping; no touch interference with host app |
| F-008       | FR-023              | Position saved and restored; default right-center |
| F-009       | FR-025–FR-027       | Two presets + custom; T1<T2<T3 validation; persisted; default Moderate |
| F-010       | FR-028, FR-029      | Five visual states; smooth 2-sec transitions; opacity progression |
| F-011       | FR-030, FR-031      | Triggers at T3+5min; 1.0x→1.15x scale; 1500ms cycle; infinite repeat |
| F-012       | FR-024              | Fades out on session end; dims during cooldown; no ghost overlays |
| F-013       | FR-032–FR-034       | Sequential flow; plain language; deep-links to Settings; skip option; resume check |
| F-014       | FR-035–FR-037       | Foreground service; persistent notification; START_STICKY; stop action |
| F-015       | FR-038              | BOOT_COMPLETED receiver; checks monitoring flag; starts service within 10 sec |

---

### 12.2 Non-Functional Acceptance Summary

| Category        | Requirement | Key Target                                        |
| --------------- | ----------- | ------------------------------------------------- |
| Performance     | NFR-001     | Cold start ≤ 2 sec                                |
| Battery         | NFR-002     | ≤ 2% drain per hour                               |
| Memory          | NFR-003     | ≤ 80MB total PSS                                  |
| Overlay FPS     | NFR-004     | ≥ 55 FPS during animations                        |
| Reliability     | NFR-006     | ≥ 95% service uptime on major OEMs                |
| Crash-free      | NFR-007     | ≥ 99.5% crash-free session rate                   |
| Privacy         | NFR-011     | Zero network calls; no INTERNET permission         |
| Accessibility   | NFR-010     | WCAG 2.1 AA contrast; 48dp touch targets          |
| Compatibility   | NFR-015     | API 26–36 functional                               |
| Test Coverage   | NFR-014     | ≥ 80% domain layer unit test coverage             |

---

## 13. Traceability Matrix

### 13.1 PRD Feature → SRS Requirement Mapping

| PRD Feature | SRS Requirements                                      |
| ----------- | ----------------------------------------------------- |
| F-001       | FR-001, FR-002, FR-003                                |
| F-002       | FR-004, FR-005, FR-006, FR-008                        |
| F-003       | FR-007                                                |
| F-004       | FR-009, FR-010, FR-011                                |
| F-005       | FR-012, FR-013, FR-014, FR-015, FR-016, FR-017       |
| F-006       | FR-018, FR-019, FR-020                                |
| F-007       | FR-021, FR-022                                        |
| F-008       | FR-023                                                |
| F-009       | FR-025, FR-026, FR-027                                |
| F-010       | FR-028, FR-029                                        |
| F-011       | FR-030, FR-031                                        |
| F-012       | FR-024                                                |
| F-013       | FR-032, FR-033, FR-034                                |
| F-014       | FR-035, FR-036, FR-037                                |
| F-015       | FR-038                                                |
| (Home/Nav)  | FR-039, FR-040, FR-041                                |

### 13.2 PRD User Story → SRS Requirement Mapping

| PRD User Story | SRS Requirements              |
| -------------- | ----------------------------- |
| US-001         | FR-001, FR-002                |
| US-002         | FR-005                        |
| US-003         | FR-004                        |
| US-004         | FR-004                        |
| US-005         | FR-007                        |
| US-009         | FR-032                        |
| US-010         | FR-034                        |
| US-011         | FR-032                        |
| US-012         | FR-033                        |
| US-013         | FR-009, FR-011, FR-012        |
| US-014         | FR-013, FR-014                |
| US-015         | FR-038                        |
| US-016         | FR-036                        |
| US-019         | FR-018                        |
| US-020         | FR-019                        |
| US-021         | FR-021                        |
| US-022         | FR-022                        |
| US-023         | FR-023                        |
| US-024         | FR-020                        |
| US-025         | FR-024                        |
| US-028         | FR-025                        |
| US-029         | FR-026                        |
| US-030         | FR-028                        |
| US-031         | FR-029                        |
| US-032         | FR-030, FR-031                |

### 13.3 PRD Constraint → SRS Requirement Mapping

| PRD Constraint | SRS Requirements                  | How Addressed                              |
| -------------- | --------------------------------- | ------------------------------------------ |
| C-01           | FR-034                            | Deep-link to Usage Access Settings page    |
| C-02           | FR-034                            | Deep-link to Overlay Settings page         |
| C-03           | FR-037, FR-038, NFR-006           | START_STICKY, boot receiver, OEM testing   |
| C-04           | FR-009                            | 2–3 sec polling interval                   |
| C-05           | FR-019 (AC6, AC7)                 | FLAG_NOT_FOCUSABLE; touch pass-through     |
| C-06           | PMR-001                           | Permission justification documented        |
| C-07           | FR-035                            | Overlay displayed from foreground service   |
| C-08           | FR-019 (AC1)                      | 48–56dp diameter constraint                |
| C-09           | FR-019 (AC7)                      | Touch events pass through outside bounds   |
| C-10           | NFR-010                           | WCAG contrast; future accessibility mode   |
| C-11           | NFR-011, DR-001–DR-004            | Room DB, no network, private storage       |
| C-12           | NFR-011                           | No INTERNET permission                     |
| C-13           | FR-036                            | Persistent notification with low importance |

---

## 14. Glossary

All terms from PRD Glossary (Section 17) apply. Additional SRS-specific terms:

| Term                     | Definition                                                                                       |
| ------------------------ | ------------------------------------------------------------------------------------------------ |
| **Acceptance Criteria**  | A set of testable conditions that must be met for a requirement to be considered fulfilled.       |
| **Polling Interval**     | The time between consecutive UsageStatsManager queries (2–3 seconds in this specification).      |
| **Visual State**         | One of five appearance configurations for the floating timer (Calm, Notice, Alert, Urgent, Throb). |
| **Session State Machine**| The formal state model governing usage session lifecycle (INACTIVE → ACTIVE → COOLDOWN → ENDED). |
| **Graceful Degradation** | The app's ability to continue functioning with reduced capability when a non-critical permission or component is unavailable. |
| **Ghost Overlay**        | An overlay view that remains visible on screen after it should have been removed — a defect.     |
| **Edge-Snapping**        | Automatic horizontal animation of the overlay to the nearest screen edge after the user releases a drag gesture. |
| **PSS**                  | Proportional Set Size — an Android memory metric representing the app's share of physical RAM.   |

---

## 15. References

| Reference                                                     | Relevance                                          |
| ------------------------------------------------------------- | -------------------------------------------------- |
| [PRD.md](PRD.md)                                              | Parent document — features, user stories, scope     |
| [Android UsageStatsManager](https://developer.android.com/reference/android/app/usage/UsageStatsManager) | EIR-001 implementation reference |
| [Android WindowManager](https://developer.android.com/reference/android/view/WindowManager) | EIR-002 implementation reference |
| [Android Foreground Services](https://developer.android.com/develop/background-work/services/foreground-services) | FR-035 implementation reference |
| [Android SYSTEM_ALERT_WINDOW](https://developer.android.com/reference/android/Manifest.permission#SYSTEM_ALERT_WINDOW) | PMR-001 permission reference |
| [WCAG 2.1 Level AA](https://www.w3.org/WAI/WCAG21/quickref/) | NFR-010 accessibility standard |
| [Don't Kill My App](https://dontkillmyapp.com/)              | NFR-006, NFR-016 OEM compatibility reference |

---

*End of Document*
