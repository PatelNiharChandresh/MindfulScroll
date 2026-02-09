# Test Plan

## MindfulScroll — Digital Awareness Companion

---

### Document Control

| Field              | Value                                     |
| ------------------ | ----------------------------------------- |
| **Document Title** | MindfulScroll Test Plan                    |
| **Version**        | 1.0                                       |
| **Status**         | Draft                                     |
| **Created**        | 2026-02-07                                |
| **Last Updated**   | 2026-02-07                                |
| **Author**         | Rudy                                      |
| **Reviewers**      | —                                         |
| **Approved By**    | —                                         |

### Revision History

| Version | Date       | Author | Changes       |
| ------- | ---------- | ------ | ------------- |
| 1.0     | 2026-02-07 | Rudy   | Initial draft |

### Parent Documents

| Document                              | Version | Relationship                                           |
| ------------------------------------- | ------- | ------------------------------------------------------ |
| [PRD.md](PRD.md)                      | 1.0     | Product definition — features F-001 to F-015, user stories |
| [SRS.md](SRS.md)                      | 1.0     | Technical requirements — FR-001 to FR-041, NFR-001 to NFR-016, acceptance criteria |
| [SAD.md](SAD.md)                      | 1.0     | Architecture — component design, state machines, concurrency model |
| [TDD.md](TDD.md)                     | 1.0     | Technical design — implementation details, algorithms, test strategy |
| [DbSchema.md](DbSchema.md)           | 1.0     | Database schema — entities, DAOs, migrations           |
| [UIUXspec.md](UIUXspec.md)           | 1.0     | UI/UX design — screens, overlay visual states, animations |
| [ProjectPlan.md](ProjectPlan.md)     | 1.0     | Project plan — phases, QA strategy, Definition of Done |
| [RiskAssessment.md](RiskAssessment.md)| 1.0    | Risk register — 25 risks, mitigation strategies        |
| [CodingStandards.md](CodingStandards.md)| 1.0  | Coding conventions — testing rules TST-01 to TST-07    |
| [Privacy.md](Privacy.md)             | 1.0     | Privacy architecture — data inventory, threat model    |

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Test Strategy](#2-test-strategy)
3. [Test Environment](#3-test-environment)
4. [Test Levels & Types](#4-test-levels--types)
5. [Unit Test Plan](#5-unit-test-plan)
6. [Integration Test Plan](#6-integration-test-plan)
7. [UI Test Plan](#7-ui-test-plan)
8. [System Test Plan — Functional](#8-system-test-plan--functional)
9. [System Test Plan — Non-Functional](#9-system-test-plan--non-functional)
10. [OEM Compatibility Test Plan](#10-oem-compatibility-test-plan)
11. [Host App Compatibility Test Plan](#11-host-app-compatibility-test-plan)
12. [Edge Case & Error Handling Test Plan](#12-edge-case--error-handling-test-plan)
13. [Security & Privacy Test Plan](#13-security--privacy-test-plan)
14. [Accessibility Test Plan](#14-accessibility-test-plan)
15. [Endurance & Stress Test Plan](#15-endurance--stress-test-plan)
16. [Regression Test Plan](#16-regression-test-plan)
17. [Test Coverage Targets](#17-test-coverage-targets)
18. [Bug Severity Classification](#18-bug-severity-classification)
19. [Entry & Exit Criteria](#19-entry--exit-criteria)
20. [Test Schedule & Phase Mapping](#20-test-schedule--phase-mapping)
21. [Risk-Based Test Prioritization](#21-risk-based-test-prioritization)
22. [Traceability Matrix](#22-traceability-matrix)
23. [Glossary](#23-glossary)
24. [References](#24-references)

---

## 1. Introduction

### 1.1 Purpose

This Test Plan defines the complete testing strategy, scope, approach, schedule, and criteria for verifying and validating MindfulScroll MVP (v1.0). It derives test cases from the acceptance criteria specified in the [SRS](SRS.md), the architectural components defined in the [SAD](SAD.md), the implementation details in the [TDD](TDD.md), the visual specifications in the [UIUXspec](UIUXspec.md), and the risks identified in the [RiskAssessment](RiskAssessment.md).

Every test case in this plan is traceable to at least one SRS requirement or risk mitigation action.

### 1.2 Scope

This plan covers testing for the **MVP release** (PRD features F-001 through F-015, SRS requirements FR-001 through FR-041, NFR-001 through NFR-016). Post-MVP features (v1.1+) are out of scope.

### 1.3 Intended Audience

| Audience            | Usage                                                        |
| ------------------- | ------------------------------------------------------------ |
| Developer(s)        | Test implementation guide; unit/integration test writing      |
| QA / Testers        | Manual test execution; exploratory testing reference          |
| Project owner       | Quality gate verification; release readiness assessment       |
| Reviewers           | Phase gate quality verification                               |

### 1.4 Testing Principles

1. **Requirements-Driven:** Every test case traces to an SRS acceptance criterion.
2. **Risk-Focused:** High-risk areas (RA-002, RA-010, RA-011, RA-012) receive proportionally more test coverage.
3. **Automated First:** Prefer automated tests where feasible; reserve manual testing for physical device interactions.
4. **Shift Left:** Unit and integration tests are written alongside implementation, not deferred.
5. **Privacy Respected:** Test data uses only local, synthetic data. No real user data is collected during testing.

---

## 2. Test Strategy

### 2.1 Test Pyramid

```
            ┌──────────┐
            │  Manual   │  ← OEM, Endurance, Exploratory
            │  Tests    │
           ┌┴──────────┴┐
           │  UI Tests   │  ← Compose Test, Espresso
           │ (Automated) │
          ┌┴────────────┴┐
          │  Integration  │  ← Service + DB + Engine pipeline
          │    Tests      │
         ┌┴──────────────┴┐
         │   Unit Tests    │  ← Domain logic, state machines, threshold engine
         │  (Foundation)   │
         └────────────────┘
```

| Level            | Proportion | Automated | Framework                       |
| ---------------- | ---------- | --------- | ------------------------------- |
| Unit Tests       | ~50%       | Yes       | JUnit 4, MockK, Turbine        |
| Integration Tests| ~20%       | Yes       | AndroidX Test, Room In-Memory   |
| UI Tests         | ~15%       | Yes       | Compose Test, Espresso          |
| Manual Tests     | ~15%       | No        | Physical devices, checklists    |

### 2.2 Test Data Strategy

- All test data is synthetic and local. No network calls, no real user data.
- Unit tests use hardcoded test fixtures and MockK-generated stubs.
- Integration tests use Room's in-memory database builder.
- UI tests use fake repositories injected via Hilt test modules.
- Manual tests use the tester's own device with personally installed apps.

### 2.3 Defect Management

Defects discovered during testing are classified by severity (Section 18) and tracked as issues. All Critical and High severity defects must be resolved before MVP release. Medium severity defects are resolved if time allows. Low severity defects are deferred to v1.1.

---

## 3. Test Environment

### 3.1 Development / CI Environment

| Parameter               | Value                                     |
| ----------------------- | ----------------------------------------- |
| IDE                     | Android Studio (latest stable)            |
| Build system            | Gradle 8.13.x (Kotlin DSL)               |
| AGP                     | 8.13.x                                    |
| Kotlin                  | 2.0.x                                     |
| JVM target              | 11                                        |
| Test runner             | JUnit 4                                   |
| Mocking framework       | MockK                                     |
| Flow testing            | Turbine                                   |
| Compose testing         | Compose Test (from BOM 2024.09.00)        |
| Instrumented testing    | AndroidX Test + Espresso                  |

### 3.2 Physical Test Devices

| Device                        | OS Version   | OEM Skin        | Priority | Purpose                       |
| ----------------------------- | ------------ | --------------- | -------- | ----------------------------- |
| Google Pixel 7/8              | Android 14/15| Stock AOSP      | Must     | Baseline reference             |
| Samsung Galaxy S23/S24        | Android 14   | One UI 6.x      | Must     | OEM compatibility (Samsung)    |
| Xiaomi Redmi Note 12/13       | Android 13/14| MIUI / HyperOS  | Must     | OEM compatibility (Xiaomi)     |
| OnePlus Nord / OnePlus 12     | Android 14   | OxygenOS 14     | Should   | OEM compatibility (OnePlus)    |
| Low-end device (2GB RAM)      | Android 8.0  | Any             | Should   | Min SDK / low-spec testing     |

### 3.3 Emulator Configuration

| Emulator                      | API Level | Purpose                              |
| ----------------------------- | --------- | ------------------------------------ |
| Pixel 7 API 26                | 26        | Min SDK boundary testing              |
| Pixel 7 API 33                | 33        | POST_NOTIFICATIONS runtime permission |
| Pixel 7 API 34                | 34        | foregroundServiceType enforcement     |
| Pixel 7 API 36                | 36        | Target SDK testing                    |
| Small screen (5")             | 34        | Overlay positioning on small screens  |
| Large screen (6.7")           | 34        | Overlay positioning on large screens  |

---

## 4. Test Levels & Types

### 4.1 Summary

| Test Level       | Test Type            | Scope                                          | Automated | Phase  |
| ---------------- | -------------------- | ---------------------------------------------- | --------- | ------ |
| Unit             | Functional           | Domain models, state machines, threshold engine | Yes       | P2–P5  |
| Unit             | Functional           | Repositories, mappers, DAOs                    | Yes       | P2     |
| Unit             | Functional           | ViewModels                                      | Yes       | P6     |
| Integration      | Functional           | Service + Polling + Session + DB pipeline       | Partial   | P3, P8 |
| Integration      | Functional           | Navigation flow                                 | Yes       | P6     |
| UI               | Functional           | Screen rendering, user interactions             | Yes       | P6     |
| System           | Functional           | End-to-end feature validation                   | No        | P8, P9 |
| System           | Performance          | Battery, memory, startup, FPS                   | Partial   | P8     |
| System           | Reliability          | Service uptime, crash-free rate                 | No        | P9     |
| System           | Compatibility        | OEM and Android version testing                 | No        | P9     |
| System           | Security / Privacy   | Data locality, permission model, manifest audit | Partial   | P8     |
| System           | Accessibility        | Contrast, touch targets, TalkBack               | Partial   | P8     |
| System           | Endurance            | 8-hour continuous monitoring                     | No        | P9     |
| Acceptance       | User Acceptance      | MVP Definition of Done (8 criteria)             | No        | P9     |

---

## 5. Unit Test Plan

Unit tests form the foundation of the test pyramid. They cover pure domain logic with no Android framework dependencies.

### 5.1 Session State Machine Tests

**Component:** `SessionManager` (TDD Section 6)
**Traces To:** FR-012 through FR-017, NFR-014

| Test ID   | Test Case                                            | Input / Precondition                                   | Expected Result                                          | SRS Ref   |
| --------- | ---------------------------------------------------- | ------------------------------------------------------ | -------------------------------------------------------- | --------- |
| UT-SM-001 | Start new session when monitored app detected        | State = INACTIVE, foreground = monitored app            | State → ACTIVE, elapsed timer starts from 0              | FR-012    |
| UT-SM-002 | Only one active session at a time                    | State = ACTIVE for App A                               | No second ACTIVE session can coexist                     | FR-017    |
| UT-SM-003 | Transition to COOLDOWN when app leaves foreground    | State = ACTIVE, foreground = non-monitored app          | State → COOLDOWN, elapsed timer pauses                   | FR-014    |
| UT-SM-004 | Resume session from COOLDOWN                         | State = COOLDOWN, same app returns within 45 sec        | State → ACTIVE, elapsed resumes from paused value        | FR-013    |
| UT-SM-005 | End session when cooldown expires                    | State = COOLDOWN, 45 seconds elapse                     | State → ENDED, session persisted to DB                   | FR-014, FR-015 |
| UT-SM-006 | Screen-off triggers cooldown                         | State = ACTIVE, screen off event received               | State → COOLDOWN                                         | FR-016    |
| UT-SM-007 | Screen-on resumes session if same app in foreground  | State = COOLDOWN (screen-off), screen on, same app      | State → ACTIVE, elapsed resumes                          | FR-016    |
| UT-SM-008 | Session ends if screen stays off through cooldown    | State = COOLDOWN (screen-off), 45 sec expire            | State → ENDED                                            | FR-016    |
| UT-SM-009 | Switch between monitored apps ends first session     | State = ACTIVE for App A, foreground = App B (monitored)| App A → COOLDOWN, new ACTIVE session for App B           | FR-017    |
| UT-SM-010 | Cooldown duration is exactly 45 seconds              | State = COOLDOWN                                        | ENDED transition at 45s (±1s)                            | FR-014    |
| UT-SM-011 | Elapsed time excludes cooldown pauses                | Session: 30s active → cooldown → resume → 30s active    | Total elapsed = 60s (not 60 + cooldown)                  | FR-013    |
| UT-SM-012 | Session start timestamp is preserved on resume       | Session resumes from cooldown                           | startTimestamp unchanged from original start              | FR-013    |

### 5.2 Threshold Engine Tests

**Component:** `ThresholdEngine` (TDD Section 7)
**Traces To:** FR-028 through FR-031, NFR-014

| Test ID   | Test Case                                            | Input                                    | Expected Result                     | SRS Ref   |
| --------- | ---------------------------------------------------- | ---------------------------------------- | ----------------------------------- | --------- |
| UT-TE-001 | CALM state when elapsed < T1                         | elapsed=5min, T1=10, T2=20, T3=30       | TimerVisualState.CALM               | FR-028    |
| UT-TE-002 | NOTICE state when elapsed >= T1 and < T2             | elapsed=10min, T1=10, T2=20, T3=30      | TimerVisualState.NOTICE             | FR-028    |
| UT-TE-003 | ALERT state when elapsed >= T2 and < T3              | elapsed=20min, T1=10, T2=20, T3=30      | TimerVisualState.ALERT              | FR-028    |
| UT-TE-004 | URGENT state when elapsed >= T3 and < T3+5min        | elapsed=30min, T1=10, T2=20, T3=30      | TimerVisualState.URGENT             | FR-028    |
| UT-TE-005 | THROB state when elapsed >= T3 + 5min                | elapsed=35min, T1=10, T2=20, T3=30      | TimerVisualState.THROB              | FR-028, FR-030 |
| UT-TE-006 | Boundary: exactly at T1                              | elapsed=600s (10min), T1=10              | TimerVisualState.NOTICE             | FR-028    |
| UT-TE-007 | Boundary: exactly at T2                              | elapsed=1200s (20min), T2=20             | TimerVisualState.ALERT              | FR-028    |
| UT-TE-008 | Boundary: exactly at T3                              | elapsed=1800s (30min), T3=30             | TimerVisualState.URGENT             | FR-028    |
| UT-TE-009 | Boundary: exactly at T3 + 5min                       | elapsed=2100s (35min), T3=30             | TimerVisualState.THROB              | FR-030    |
| UT-TE-010 | Minimum thresholds (T1=1, T2=2, T3=3)               | elapsed=8min                             | TimerVisualState.THROB              | FR-026    |
| UT-TE-011 | Maximum thresholds (T1=40, T2=80, T3=120)            | elapsed=39min                            | TimerVisualState.CALM               | FR-026    |
| UT-TE-012 | shouldShowOverlay returns false below 60s             | elapsed=59s                              | false                               | FR-018    |
| UT-TE-013 | shouldShowOverlay returns true at 60s                 | elapsed=60s                              | true                                | FR-018    |

### 5.3 Threshold Validation Tests

**Component:** `ThresholdConfig` validation (TDD Section 3.1.3)
**Traces To:** FR-025, FR-026

| Test ID   | Test Case                                   | Input               | Expected Result                    | SRS Ref   |
| --------- | ------------------------------------------- | -------------------- | ---------------------------------- | --------- |
| UT-TV-001 | Valid Light preset                           | T1=5, T2=10, T3=15  | Valid                              | FR-025    |
| UT-TV-002 | Valid Moderate preset                        | T1=10, T2=20, T3=30 | Valid                              | FR-025    |
| UT-TV-003 | Valid custom: T1 < T2 < T3                   | T1=7, T2=15, T3=45  | Valid                              | FR-026    |
| UT-TV-004 | Invalid: T1 >= T2                            | T1=10, T2=10, T3=30 | Validation error                   | FR-026    |
| UT-TV-005 | Invalid: T2 >= T3                            | T1=5, T2=30, T3=30  | Validation error                   | FR-026    |
| UT-TV-006 | Invalid: T1 < 1 minute                       | T1=0, T2=10, T3=20  | Validation error                   | FR-026    |
| UT-TV-007 | Invalid: T3 > 120 minutes                    | T1=5, T2=10, T3=121 | Validation error                   | FR-026    |
| UT-TV-008 | Default config is Moderate when none saved    | No saved config      | T1=10, T2=20, T3=30               | FR-027    |

### 5.4 ViewModel Tests

**Components:** `HomeViewModel`, `AppSelectionViewModel` (TDD Section 10)
**Traces To:** FR-039, FR-040, FR-004 through FR-008

| Test ID   | Test Case                                          | Expected Behavior                                      | SRS Ref   |
| --------- | -------------------------------------------------- | ------------------------------------------------------ | --------- |
| UT-VM-001 | HomeViewModel exposes monitoring status             | StateFlow emits ACTIVE/INACTIVE matching service state | FR-039    |
| UT-VM-002 | HomeViewModel exposes monitored app count           | Count matches number of active MonitoredApp records    | FR-039    |
| UT-VM-003 | HomeViewModel start monitoring checks permissions   | If permissions missing, navigates to onboarding        | FR-040    |
| UT-VM-004 | AppSelectionViewModel loads installed apps           | Apps listed alphabetically, self excluded              | FR-001, FR-003 |
| UT-VM-005 | AppSelectionViewModel toggles selection             | Selection persisted to repository                      | FR-004, FR-007 |
| UT-VM-006 | AppSelectionViewModel search filters list           | Case-insensitive substring match, < 100ms              | FR-005    |
| UT-VM-007 | AppSelectionViewModel select all (no filter)        | All visible apps selected                              | FR-008    |
| UT-VM-008 | AppSelectionViewModel select all (with filter)      | Only filtered apps selected                            | FR-008    |
| UT-VM-009 | AppSelectionViewModel deselect all                  | All visible apps deselected                            | FR-008    |
| UT-VM-010 | Filtered-out apps retain selection on clear         | Selection state unchanged after filter removal         | FR-005    |

### 5.5 Mapper & Utility Tests

**Components:** Entity-Domain mappers (TDD Section 4.1), PermissionChecker (TDD Section 4.4)

| Test ID   | Test Case                                    | Expected Behavior                            | SRS Ref   |
| --------- | -------------------------------------------- | -------------------------------------------- | --------- |
| UT-MP-001 | MonitoredAppEntity → MonitoredApp mapping    | All fields correctly mapped                  | DR-001    |
| UT-MP-002 | MonitoredApp → MonitoredAppEntity mapping    | All fields correctly mapped                  | DR-001    |
| UT-MP-003 | ThresholdConfigEntity → ThresholdConfig      | Minutes correctly converted                  | DR-002    |
| UT-MP-004 | UsageSession → UsageSessionEntity            | Timestamps and durations preserved           | DR-003    |
| UT-MP-005 | MM:SS format for 1 minute 0 seconds          | "01:00"                                      | FR-019    |
| UT-MP-006 | MM:SS format for 59 minutes 59 seconds       | "59:59"                                      | FR-019    |
| UT-MP-007 | MM:SS format for 100+ minutes                | "100:00" (or overflow handling)              | FR-019    |

---

## 6. Integration Test Plan

Integration tests verify the interaction between multiple components, particularly the data layer and the service pipeline.

### 6.1 Repository + Room DAO Tests

**Traces To:** FR-007, FR-027, DR-001 through DR-005, NFR-005, NFR-008

| Test ID   | Test Case                                          | Expected Behavior                                   | SRS Ref   |
| --------- | -------------------------------------------------- | --------------------------------------------------- | --------- |
| IT-DB-001 | Insert and retrieve MonitoredApp                   | Record persisted and retrievable                     | FR-007    |
| IT-DB-002 | Toggle MonitoredApp isActive flag                  | Update reflected on next query                       | FR-007    |
| IT-DB-003 | Delete MonitoredApp by package name                | Record removed, no orphaned data                     | EH-001    |
| IT-DB-004 | Insert and retrieve ThresholdConfig                | Preset name and values persisted                     | FR-027    |
| IT-DB-005 | Update ThresholdConfig with custom values          | New values override previous                         | FR-027    |
| IT-DB-006 | Insert UsageSession on session end                 | All fields (timestamps, duration, maxThreshold) saved| FR-015    |
| IT-DB-007 | Query sessions older than 90 days                  | Retention cleanup deletes old records                | DR-005    |
| IT-DB-008 | UserPreferences key-value persistence              | monitoring_active flag persists across access        | FR-038, FR-040 |
| IT-DB-009 | Concurrent read/write with WAL mode                | No corruption; reads succeed during writes           | NFR-008   |
| IT-DB-010 | All queries complete within 100ms                  | Measured on in-memory DB with 5000 session records   | NFR-005   |
| IT-DB-011 | Select all active MonitoredApps                    | Only isActive=true records returned                  | FR-009    |
| IT-DB-012 | Overlay position preferences persist               | edge and y_percent values survive read/write cycle   | FR-023    |

### 6.2 Service Pipeline Integration Tests

**Traces To:** FR-009 through FR-017, FR-035 through FR-037

| Test ID   | Test Case                                              | Expected Behavior                                    | SRS Ref   |
| --------- | ------------------------------------------------------ | ---------------------------------------------------- | --------- |
| IT-SV-001 | Polling engine detects monitored app in foreground      | SessionManager.handleMonitoredAppDetected() called   | FR-009    |
| IT-SV-002 | Polling engine detects non-monitored app                | SessionManager.handleMonitoredAppLeft() called       | FR-009    |
| IT-SV-003 | Session start triggers overlay display after 60s        | OverlayController.updateOverlay() called at 60s      | FR-018    |
| IT-SV-004 | Threshold engine evaluates visual state on each tick    | Correct visual state passed to overlay controller    | FR-028    |
| IT-SV-005 | Session end persists to database                        | UsageSession record written to Room                  | FR-015    |
| IT-SV-006 | Service stops cleanly on stopMonitoring                 | Coroutine scope canceled, overlay removed, state cleared | FR-040  |
| IT-SV-007 | ScreenStateReceiver pauses/resumes polling              | Polling stops on SCREEN_OFF, resumes on SCREEN_ON    | FR-010    |

### 6.3 Navigation Integration Tests

**Traces To:** FR-041

| Test ID   | Test Case                                          | Expected Behavior                               | SRS Ref   |
| --------- | -------------------------------------------------- | ----------------------------------------------- | --------- |
| IT-NV-001 | First launch navigates to onboarding               | Welcome screen displayed, not Home              | FR-041    |
| IT-NV-002 | All permissions granted skips onboarding            | Home screen displayed directly                  | FR-041    |
| IT-NV-003 | Back button returns to previous screen              | Navigation stack pops correctly                 | FR-041    |
| IT-NV-004 | Onboarding not shown after initial completion       | Subsequent launches go to Home                  | FR-041    |
| IT-NV-005 | Home → App Selection → back returns to Home        | Standard back navigation works                  | FR-041    |

---

## 7. UI Test Plan

UI tests verify screen rendering and user interactions using Compose Test and Espresso.

### 7.1 Onboarding Screens

**Traces To:** FR-032 through FR-034, NFR-009

| Test ID   | Test Case                                          | Expected Behavior                                 | SRS Ref   |
| --------- | -------------------------------------------------- | ------------------------------------------------- | --------- |
| UI-OB-001 | Welcome screen renders with Get Started button     | Title, description, and CTA button visible        | FR-032    |
| UI-OB-002 | Usage Access screen explains permission plainly    | No technical jargon; Grant Permission button shown | FR-032    |
| UI-OB-003 | Overlay permission screen renders correctly         | Explanation + Grant Permission button visible      | FR-032    |
| UI-OB-004 | Battery optimization screen renders correctly       | Explanation + Grant Permission button visible      | FR-032    |
| UI-OB-005 | Skip option visible on each permission step        | "Skip" and "Try Again" both accessible             | FR-032    |
| UI-OB-006 | Completion screen shows success and Continue button | Navigation to App Selection available              | FR-032    |
| UI-OB-007 | Permission granted shows success indicator         | Visual confirmation when returning from Settings   | FR-032    |
| UI-OB-008 | Permission denied shows warning message            | Clear warning about impacted functionality         | FR-032    |

### 7.2 Home Screen

**Traces To:** FR-039, FR-040

| Test ID   | Test Case                                          | Expected Behavior                                 | SRS Ref   |
| --------- | -------------------------------------------------- | ------------------------------------------------- | --------- |
| UI-HM-001 | Monitoring status indicator displayed               | Green "Active" or gray "Inactive" indicator       | FR-039    |
| UI-HM-002 | Monitored app count displayed                       | "Monitoring X apps" text visible                  | FR-039    |
| UI-HM-003 | Current threshold config displayed                  | "Thresholds: T1 / T2 / T3 min" readable          | FR-039    |
| UI-HM-004 | Start Monitoring button visible when inactive       | Button text = "Start Monitoring"                  | FR-040    |
| UI-HM-005 | Stop Monitoring button visible when active          | Button text = "Stop Monitoring"                   | FR-040    |
| UI-HM-006 | Navigation to App Selection works                   | Tap navigates to App Selection screen             | FR-039    |
| UI-HM-007 | Navigation to Threshold Configuration works         | Tap navigates to Threshold screen                 | FR-039    |
| UI-HM-008 | Permission missing banner displayed                 | Banner shown when Usage Access revoked            | FR-033    |
| UI-HM-009 | Permission banner tap opens Settings                | Correct Settings page opened                      | FR-033    |

### 7.3 App Selection Screen

**Traces To:** FR-001 through FR-008

| Test ID   | Test Case                                          | Expected Behavior                                 | SRS Ref   |
| --------- | -------------------------------------------------- | ------------------------------------------------- | --------- |
| UI-AS-001 | App list renders with icons and names              | Each row: icon + name + checkbox                  | FR-002, FR-004 |
| UI-AS-002 | Apps sorted alphabetically (case-insensitive)      | "facebook" before "Firefox" before "Gmail"        | FR-003    |
| UI-AS-003 | MindfulScroll excluded from list                   | Own package not in list                           | FR-001    |
| UI-AS-004 | Checkbox toggles on tap                            | Visual state changes immediately                  | FR-004    |
| UI-AS-005 | Row tap toggles selection (not just checkbox)       | Full row is clickable                             | FR-004    |
| UI-AS-006 | Search field present at top                        | Text input field visible                          | FR-005    |
| UI-AS-007 | Search filters list in real time                   | Results update as user types                      | FR-005    |
| UI-AS-008 | Empty search shows "No apps found"                 | Message displayed when no matches                 | FR-005    |
| UI-AS-009 | Selection count updates on toggle                  | "X apps selected" text updates                    | FR-006    |
| UI-AS-010 | Select All selects all visible apps                | All checkboxes checked                            | FR-008    |
| UI-AS-011 | Deselect All deselects all visible apps            | All checkboxes unchecked                          | FR-008    |

### 7.4 Threshold Configuration Screen

**Traces To:** FR-025 through FR-027

| Test ID   | Test Case                                          | Expected Behavior                                 | SRS Ref   |
| --------- | -------------------------------------------------- | ------------------------------------------------- | --------- |
| UI-TC-001 | Light preset option displayed                      | "Light (5/10/15 min)" selectable                  | FR-025    |
| UI-TC-002 | Moderate preset option displayed                   | "Moderate (10/20/30 min)" selectable              | FR-025    |
| UI-TC-003 | Custom option enables manual input                 | Three input fields/sliders visible                | FR-025    |
| UI-TC-004 | Preset selection populates all three values         | Selecting Light sets T1=5, T2=10, T3=15           | FR-025    |
| UI-TC-005 | Validation error for T1 >= T2                      | Inline error displayed                            | FR-026    |
| UI-TC-006 | Validation error for T3 > 120                      | Inline error displayed                            | FR-026    |
| UI-TC-007 | Save button persists configuration                 | Config written to DB on tap                       | FR-027    |
| UI-TC-008 | Previously saved config loaded on screen open      | Correct preset or custom values pre-populated     | FR-027    |

---

## 8. System Test Plan — Functional

System-level functional tests validate end-to-end feature behavior on real or emulated devices.

### 8.1 App Discovery & Selection (F-001, F-002, F-003)

| Test ID   | Test Case                                          | Steps                                               | Expected Result                                   | SRS Ref        |
| --------- | -------------------------------------------------- | --------------------------------------------------- | ------------------------------------------------- | -------------- |
| ST-AD-001 | Discover launcher apps only                        | Open App Selection                                  | Only apps with launcher intent shown              | FR-001         |
| ST-AD-002 | System apps excluded                               | Open App Selection                                  | No "System UI" or "Settings Provider" entries     | FR-001         |
| ST-AD-003 | Self-exclusion                                     | Open App Selection                                  | MindfulScroll not in list                         | FR-001         |
| ST-AD-004 | App list loads within 3s (200+ apps)               | Device with 200+ apps, open selection               | List renders within 3 seconds                     | FR-001         |
| ST-AD-005 | Placeholder icon for unresolvable icon             | App with missing icon                               | Default placeholder displayed                     | FR-002         |
| ST-AD-006 | Package name fallback for missing label            | App with missing label                              | Package name shown instead                        | FR-002         |
| ST-AD-007 | Selections persist across app restart              | Select apps → force close → reopen                  | Previous selections intact                        | FR-007         |
| ST-AD-008 | Selections persist across reboot                   | Select apps → reboot → reopen                       | Previous selections intact                        | FR-007         |
| ST-AD-009 | Uninstalled app removed from selection             | Select app → uninstall it → reopen selection        | App silently removed from list                    | FR-007, EH-001 |

### 8.2 Foreground Detection (F-004)

| Test ID   | Test Case                                          | Steps                                               | Expected Result                                   | SRS Ref        |
| --------- | -------------------------------------------------- | --------------------------------------------------- | ------------------------------------------------- | -------------- |
| ST-FD-001 | Detects monitored app within 3 seconds             | Start monitoring → open monitored app               | Detection within 3 sec (use stopwatch)            | FR-011         |
| ST-FD-002 | Detects switch from non-monitored to monitored     | Open non-monitored app → switch to monitored        | Detection within 3 sec                            | FR-011         |
| ST-FD-003 | Detects switch between two monitored apps          | Open App A → switch to App B (both monitored)       | Switch detected, sessions transition correctly    | FR-011, FR-017 |
| ST-FD-004 | Polling pauses on screen off                       | Start monitoring → turn screen off → observe logs   | No UsageStats queries during screen off           | FR-010         |
| ST-FD-005 | Polling resumes within 1s on screen on             | Screen off → screen on → open monitored app         | Detection resumes within 1 second                 | FR-010         |

### 8.3 Session Tracking (F-005)

| Test ID   | Test Case                                          | Steps                                               | Expected Result                                   | SRS Ref        |
| --------- | -------------------------------------------------- | --------------------------------------------------- | ------------------------------------------------- | -------------- |
| ST-SS-001 | Session starts on monitored app detection          | Open monitored app with monitoring active            | Session state = ACTIVE, timer counting            | FR-012         |
| ST-SS-002 | Session enters cooldown on leaving app             | Open monitored app → go to Home screen              | 45-sec cooldown begins, timer pauses              | FR-014         |
| ST-SS-003 | Session resumes if returning within cooldown       | Leave app → return within 45 sec                    | Timer resumes from previous value                 | FR-013         |
| ST-SS-004 | Session ends after cooldown expires                | Leave app → wait 45+ sec                            | Session saved to DB, overlay removed              | FR-014, FR-015 |
| ST-SS-005 | Screen off triggers cooldown                       | Using monitored app → turn screen off               | Session enters cooldown                           | FR-016         |
| ST-SS-006 | Session resumes after screen on (same app)         | Screen off → screen on within cooldown → same app   | Session resumes with correct elapsed time         | FR-016         |
| ST-SS-007 | No concurrent sessions                             | Open App A → switch to App B (both monitored)       | App A in cooldown, App B in new ACTIVE session    | FR-017         |
| ST-SS-008 | Session data persisted correctly                   | Complete a session → check DB                       | All fields present: package, start, end, duration, maxThreshold | FR-015 |

### 8.4 Floating Timer Overlay (F-006, F-007, F-008, F-012)

| Test ID   | Test Case                                          | Steps                                               | Expected Result                                   | SRS Ref        |
| --------- | -------------------------------------------------- | --------------------------------------------------- | ------------------------------------------------- | -------------- |
| ST-OV-001 | Overlay appears at 60 seconds                      | Open monitored app, wait 60 seconds                 | Overlay fades in at ~60s                          | FR-018         |
| ST-OV-002 | Overlay does not appear before 60s                 | Open monitored app, observe at 59s                  | No overlay visible                                | FR-018         |
| ST-OV-003 | Overlay is circular, 48-56dp                       | Observe overlay appearance                          | Round shape, correct size                         | FR-019         |
| ST-OV-004 | Overlay shows MM:SS format                         | Observe overlay text                                | Format like "01:00", updates every second         | FR-019         |
| ST-OV-005 | Overlay time counts from session start             | Wait 60s for overlay → check time shown             | Shows "01:00" (not "00:00")                       | FR-019         |
| ST-OV-006 | Overlay fade-in animation (500ms)                  | Observe overlay first appearance                    | Smooth fade from transparent to calm opacity      | FR-020         |
| ST-OV-007 | Overlay is draggable                               | Touch and drag overlay                              | Overlay follows finger                            | FR-021         |
| ST-OV-008 | Overlay snaps to nearest edge on release            | Drag to middle-left area → release                  | Snaps to left edge                                | FR-022         |
| ST-OV-009 | Overlay snaps to right edge when centered           | Drag to exact center → release                      | Snaps to right edge (deterministic default)       | FR-022         |
| ST-OV-010 | Vertical position preserved on snap                | Drag to top-left → release                          | Snaps to left edge, Y position maintained         | FR-022         |
| ST-OV-011 | Position persists across sessions                  | Drag to left side → end session → start new         | Overlay appears at saved left-side position       | FR-023         |
| ST-OV-012 | Default position: right edge, vertically centered  | First ever overlay display                          | Right edge, 50% screen height                     | FR-023         |
| ST-OV-013 | Overlay fades out on session end (400ms)           | Leave monitored app → cooldown expires              | Overlay fades out smoothly                        | FR-024         |
| ST-OV-014 | Overlay dims during cooldown                       | Leave monitored app (session still in cooldown)     | Overlay opacity reduced by ~30%                   | FR-014, FR-024 |
| ST-OV-015 | Overlay restored to full opacity on resume         | Return to app during cooldown                       | Overlay back to normal opacity                    | FR-024         |
| ST-OV-016 | No ghost overlay after session end                 | End session → navigate around device                | No orphaned overlay remains                       | FR-024, RA-011 |
| ST-OV-017 | Short tap (<150ms, <10dp) is not a drag            | Tap overlay quickly                                 | No drag behavior triggered                        | FR-021         |
| ST-OV-018 | Touch pass-through outside overlay                 | Tap content behind overlay                          | Host app receives the touch event                 | FR-019         |

### 8.5 Threshold Configuration & Color Escalation (F-009, F-010)

| Test ID   | Test Case                                          | Steps                                               | Expected Result                                   | SRS Ref        |
| --------- | -------------------------------------------------- | --------------------------------------------------- | ------------------------------------------------- | -------------- |
| ST-TH-001 | Default threshold is Moderate                      | Fresh install → check config                        | T1=10, T2=20, T3=30                              | FR-027         |
| ST-TH-002 | Color escalation: CALM → NOTICE at T1              | Use monitored app for T1 minutes                    | Overlay color transitions gray → amber            | FR-028         |
| ST-TH-003 | Color escalation: NOTICE → ALERT at T2             | Continue for T2 minutes                             | Overlay color transitions amber → orange          | FR-028         |
| ST-TH-004 | Color escalation: ALERT → URGENT at T3             | Continue for T3 minutes                             | Overlay color transitions orange → red            | FR-028         |
| ST-TH-005 | Throb activates at T3 + 5min                       | Continue for T3+5 minutes                           | Pulsing scale animation begins                    | FR-030         |
| ST-TH-006 | Color transitions are smooth (2000ms)              | Observe transition moment                           | Gradual color change, not abrupt                  | FR-029         |
| ST-TH-007 | Throb animation: 1.0x → 1.15x, 1500ms cycle       | Observe throb state                                 | Breathing animation, smooth, repeating            | FR-031         |
| ST-TH-008 | Throb continues until session ends                 | Observe throb → leave app → cooldown expires        | Throb stops only when session ends                | FR-031         |
| ST-TH-009 | Saved thresholds applied by monitoring service     | Save custom thresholds → start monitoring           | Service uses new threshold values                 | FR-027         |

### 8.6 Permission Onboarding (F-013)

| Test ID   | Test Case                                          | Steps                                               | Expected Result                                   | SRS Ref        |
| --------- | -------------------------------------------------- | --------------------------------------------------- | ------------------------------------------------- | -------------- |
| ST-PO-001 | Onboarding shown on first launch                   | Fresh install → launch                              | Welcome screen displayed                          | FR-032         |
| ST-PO-002 | Usage Access deep-link opens correct Settings      | Tap "Grant Permission" for Usage Access             | `ACTION_USAGE_ACCESS_SETTINGS` opened             | FR-034         |
| ST-PO-003 | Overlay deep-link opens correct Settings           | Tap "Grant Permission" for Overlay                  | `ACTION_MANAGE_OVERLAY_PERMISSION` with package URI| FR-034        |
| ST-PO-004 | Battery optimization deep-link works               | Tap "Grant Permission" for Battery                  | `ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`     | FR-034         |
| ST-PO-005 | Permission check on return from Settings           | Grant permission → return to app                    | Success indicator shown, auto-advance             | FR-032         |
| ST-PO-006 | Skip option works for each step                    | Tap "Skip" on Usage Access step                     | Warning shown, advances to next step              | FR-032         |
| ST-PO-007 | Onboarding completable in under 90 seconds         | Time the full onboarding flow                       | < 90 seconds for skilled user                     | NFR-009        |
| ST-PO-008 | Permission revoked: banner on app resume            | Grant all → revoke Usage Access → reopen app        | Banner: "Usage Access permission is required..."  | FR-033         |
| ST-PO-009 | Banner tap navigates to correct Settings page      | Tap permission banner                               | Correct Settings page opened                      | FR-033         |
| ST-PO-010 | Deep-link fallback for unsupported OEM intent      | OEM that doesn't resolve specific intent            | Falls back to generic Settings + toast            | FR-034         |

### 8.7 Foreground Service & Boot (F-014, F-015)

| Test ID   | Test Case                                          | Steps                                               | Expected Result                                   | SRS Ref        |
| --------- | -------------------------------------------------- | --------------------------------------------------- | ------------------------------------------------- | -------------- |
| ST-FS-001 | Service starts on "Start Monitoring"               | Tap Start Monitoring with all permissions            | Foreground service running, notification shown    | FR-035         |
| ST-FS-002 | Service persists after leaving app                 | Start monitoring → press Home                       | Service continues running                         | FR-035         |
| ST-FS-003 | Service persists after swiping from recents        | Start monitoring → swipe app from recents           | Service continues running                         | FR-035         |
| ST-FS-004 | Service stops on "Stop Monitoring"                 | Tap Stop Monitoring                                 | Service stops, notification removed               | FR-035         |
| ST-FS-005 | Notification text: "MindfulScroll is monitoring"   | Start monitoring → check notification               | Correct text and icon                             | FR-036         |
| ST-FS-006 | Notification tap opens app                         | Tap the persistent notification                     | MindfulScroll main activity opened                | FR-036         |
| ST-FS-007 | "Stop Monitoring" notification action works        | Tap "Stop Monitoring" in notification               | Service stops, overlay removed                    | FR-036         |
| ST-FS-008 | Notification uses low importance                   | Check notification channel                          | No sound, no vibration                            | FR-036         |
| ST-FS-009 | START_STICKY restarts after OS kill                | Use ADB to kill service → observe restart           | Service restarts, monitoring resumes              | FR-037         |
| ST-FS-010 | Boot receiver restarts service after reboot        | Start monitoring → reboot device                    | Service auto-starts after boot                    | FR-038         |
| ST-FS-011 | Boot receiver respects monitoring_active flag      | Stop monitoring → reboot device                     | Service does NOT start after boot                 | FR-038         |
| ST-FS-012 | Service starts within 10s of BOOT_COMPLETED        | Reboot with monitoring active → time to start       | Service running within 10 seconds                 | FR-038         |
| ST-FS-013 | Missing permissions blocks Start Monitoring        | Revoke Usage Access → tap Start Monitoring          | Navigates to permission flow, not service start   | FR-040         |

### 8.8 Home Screen & Navigation

| Test ID   | Test Case                                          | Steps                                               | Expected Result                                   | SRS Ref        |
| --------- | -------------------------------------------------- | --------------------------------------------------- | ------------------------------------------------- | -------------- |
| ST-HN-001 | Home shows correct monitoring status               | Start monitoring → check Home                       | Green "Monitoring Active" indicator               | FR-039         |
| ST-HN-002 | Home shows "Monitoring Inactive" when stopped      | Stop monitoring → check Home                        | Gray "Monitoring Inactive" indicator              | FR-039         |
| ST-HN-003 | Toggle state persists across restart               | Start monitoring → kill app → reopen                | Home shows "Monitoring Active"                    | FR-040         |
| ST-HN-004 | About screen accessible from Home                  | Navigate to About                                   | App version and privacy policy link shown         | FR-041         |

---

## 9. System Test Plan — Non-Functional

### 9.1 Performance Tests

**Traces To:** NFR-001 through NFR-005

| Test ID   | Test Case                            | Method                                          | Pass Criteria                          | SRS Ref   |
| --------- | ------------------------------------ | ----------------------------------------------- | -------------------------------------- | --------- |
| NF-PF-001 | Cold start time                      | Measure with `adb shell am start` + logcat timestamps | First interactive frame ≤ 2000ms      | NFR-001   |
| NF-PF-002 | Warm start time                      | Switch to app from recents                      | Interactive frame ≤ 500ms              | NFR-001   |
| NF-PF-003 | Battery consumption (8-hour test)    | `adb shell dumpsys batterystats` after 8 hours  | ≤ 2% per hour                          | NFR-002   |
| NF-PF-004 | Battery near zero when screen off    | Monitor battery during 1 hour screen off        | MindfulScroll consumption ≈ 0%         | NFR-002   |
| NF-PF-005 | Memory footprint (PSS)              | `adb shell dumpsys meminfo com.rudy.mindfulscroll` | ≤ 80MB PSS                           | NFR-003   |
| NF-PF-006 | Heap allocation                      | Same as above, check heap column                | ≤ 50MB heap during active monitoring   | NFR-003   |
| NF-PF-007 | No memory leaks over 8 hours         | Profile heap at 0h, 2h, 4h, 8h                 | No unbounded growth                    | NFR-003   |
| NF-PF-008 | Overlay animation FPS                | Android GPU profiling / systrace                | ≥ 55 FPS during all animations         | NFR-004   |
| NF-PF-009 | Overlay drag input lag               | Subjective evaluation + frame timing            | Zero perceptible lag                   | NFR-004   |
| NF-PF-010 | DB query: load monitored apps        | Measure query time with Room test utilities     | ≤ 100ms                               | NFR-005   |
| NF-PF-011 | DB query: load threshold config      | Measure query time                              | ≤ 50ms                                | NFR-005   |
| NF-PF-012 | DB write: session record             | Measure write time                              | ≤ 100ms                               | NFR-005   |
| NF-PF-013 | App list loads within 3s (200+ apps) | Device with many apps, time list render         | ≤ 3 seconds                            | FR-001    |

### 9.2 Reliability Tests

**Traces To:** NFR-006, NFR-007, NFR-008

| Test ID   | Test Case                                    | Method                                          | Pass Criteria                          | SRS Ref   |
| --------- | -------------------------------------------- | ----------------------------------------------- | -------------------------------------- | --------- |
| NF-RL-001 | Service uptime: 24h on Pixel                 | Run monitoring for 24h with screen cycles       | Uptime ≥ 99%                           | NFR-006   |
| NF-RL-002 | Service uptime: 24h on Samsung               | Same as above on Samsung device                 | Uptime ≥ 95% (with battery exemption)  | NFR-006   |
| NF-RL-003 | Service uptime: 24h on Xiaomi                | Same as above on Xiaomi device                  | Uptime ≥ 90% (with battery exemption)  | NFR-006   |
| NF-RL-004 | Service recovery after OS kill               | Kill service via ADB → measure restart time     | Restarts within 60 seconds             | NFR-006   |
| NF-RL-005 | No crashes during normal use (1h session)    | Use app normally for 1 hour                     | Zero crashes                           | NFR-007   |
| NF-RL-006 | No crash on runtime permission revocation    | Revoke permissions while monitoring             | Graceful degradation, no crash         | NFR-007   |
| NF-RL-007 | No crash on monitored app uninstall          | Uninstall monitored app during active session   | Session ends gracefully, no crash      | NFR-007   |
| NF-RL-008 | Room WAL prevents corruption                 | Concurrent read/write operations                | Data intact, no SQLite errors          | NFR-008   |

---

## 10. OEM Compatibility Test Plan

**Traces To:** NFR-016, RA-002, RA-007

Each "Must" OEM must pass all tests below. "Should" OEMs pass with minor deviations accepted. "Could" OEMs are tested best-effort.

| OEM      | Skin          | Priority | Devices to Test                  |
| -------- | ------------- | -------- | -------------------------------- |
| Google   | Stock AOSP    | Must     | Pixel 7 or Pixel 8              |
| Samsung  | One UI        | Must     | Galaxy S23 or Galaxy S24        |
| Xiaomi   | MIUI/HyperOS  | Must     | Redmi Note 12 or Redmi Note 13 |
| OnePlus  | OxygenOS      | Should   | Nord or OnePlus 12              |
| Oppo     | ColorOS       | Could    | Any recent Oppo device          |
| Huawei   | EMUI          | Could    | Any recent Huawei device        |

### 10.1 OEM Test Cases

| Test ID    | Test Case                                         | Pass Criteria                                       | SRS Ref   |
| ---------- | ------------------------------------------------- | --------------------------------------------------- | --------- |
| OEM-001    | Foreground service runs for 8+ hours              | Service alive after 8h (battery optimization exempt)| NFR-016   |
| OEM-002    | Overlay displays over third-party apps             | Timer visible over Instagram, YouTube, etc.         | NFR-016   |
| OEM-003    | Boot receiver restarts service after reboot        | Service running within 10s of boot completion       | NFR-016   |
| OEM-004    | UsageStatsManager returns valid data               | Foreground app correctly detected                   | RA-007    |
| OEM-005    | Dual-query fallback works if queryEvents fails     | queryUsageStats fallback returns correct result     | RA-007    |
| OEM-006    | Battery optimization exemption request works       | System dialog/Settings page opens correctly         | FR-034    |
| OEM-007    | Overlay drag and snap work correctly               | No touch issues specific to OEM skin               | RA-010    |
| OEM-008    | No ghost overlays after service restart            | Clean overlay state after START_STICKY restart      | RA-011    |

### 10.2 OEM-Specific Known Issues Checklist

| OEM     | Known Issue                                        | Test Action                                         |
| ------- | -------------------------------------------------- | --------------------------------------------------- |
| Xiaomi  | Autostart permission required                      | Verify guidance shown to user; test with autostart enabled |
| Xiaomi  | Battery saver kills background services            | Test with battery saver ON and OFF                  |
| Samsung | Sleeping apps feature kills services               | Verify app is in "Never sleeping" list              |
| Samsung | One UI "Adaptive battery" throttling               | Test with app exempted from adaptive battery        |
| Huawei  | Aggressive battery management                      | Test with app protected from battery optimization   |
| OnePlus | "Deep optimization" kills services                 | Verify battery optimization exemption works         |

---

## 11. Host App Compatibility Test Plan

**Traces To:** PRD Section 16.2 #5, ProjectPlan Section 11.4

The floating timer overlay must be tested on the following 10 host applications to verify it does not interfere with the host app's functionality.

| #  | Host App     | Category        |
| -- | ------------ | --------------- |
| 1  | Instagram    | Social Media    |
| 2  | TikTok       | Short Video     |
| 3  | YouTube      | Video           |
| 4  | Reddit       | Social / Forum  |
| 5  | X (Twitter)  | Social Media    |
| 6  | Facebook     | Social Media    |
| 7  | Snapchat     | Social Media    |
| 8  | Chrome       | Browser         |
| 9  | Netflix      | Streaming       |
| 10 | WhatsApp     | Messaging       |

### 11.1 Per-App Test Cases

For **each** host app above, execute all of the following:

| Test ID    | Test Case                                    | Pass Criteria                                         |
| ---------- | -------------------------------------------- | ----------------------------------------------------- |
| HA-001     | Timer overlay appears after 60s              | Overlay visible and showing correct time              |
| HA-002     | Overlay does not block content               | Host app content scrollable and readable behind overlay|
| HA-003     | Overlay is draggable                         | Drag works smoothly, no interference with host scroll |
| HA-004     | Edge-snapping works                          | Overlay snaps correctly after drag release            |
| HA-005     | Host app scroll/tap not blocked by overlay   | Touch events pass through outside overlay bounds      |
| HA-006     | Color transitions visible                    | Visual state changes are observable                   |
| HA-007     | Overlay fades in/out correctly               | Smooth fade-in on appear, fade-out on session end     |
| HA-008     | No ghost overlay when leaving host app       | No orphaned overlay after navigating away             |

---

## 12. Edge Case & Error Handling Test Plan

**Traces To:** SRS Section 11 (EH-001 through EH-008, EC-01 through EC-10)

### 12.1 Error Handling Tests

| Test ID   | Test Case                                        | Steps                                                   | Expected Behavior                                    | SRS Ref   |
| --------- | ------------------------------------------------ | ------------------------------------------------------- | ---------------------------------------------------- | --------- |
| EH-TC-001 | Monitored app uninstalled during session         | Start session with App A → uninstall App A              | Session ends, app removed from list silently         | EH-001    |
| EH-TC-002 | Usage Access permission revoked at runtime       | Start monitoring → revoke Usage Access in Settings      | Monitoring pauses, banner shown on next app open     | EH-002    |
| EH-TC-003 | Overlay permission revoked at runtime            | Start monitoring → revoke Overlay in Settings           | Monitoring continues, overlay not shown, no crash    | EH-002    |
| EH-TC-004 | Service killed by OS (simulate via ADB)          | `adb shell am stopservice` or `kill`                    | Service restarts via START_STICKY, monitoring resumes| EH-003    |
| EH-TC-005 | WindowManager.addView throws exception           | Simulate by revoking overlay mid-session                | Error caught, no crash, monitoring continues         | EH-004    |
| EH-TC-006 | UsageStatsManager returns empty data             | Grant permission, immediately query                     | Treated as "no monitored app", retry on next poll    | EH-005    |
| EH-TC-007 | Database write fails (simulate disk full)        | Fill storage → trigger session end                      | Error logged, no crash, in-memory state continues    | EH-006    |
| EH-TC-008 | Device rotation during active overlay            | Show overlay → rotate device                            | Overlay repositioned correctly, session continues    | EH-007    |
| EH-TC-009 | Split screen with monitored app                  | Enter split-screen with monitored app                   | Overlay visible, monitoring works for focused app    | EH-008    |

### 12.2 Edge Case Tests

| Test ID   | Test Case                                        | Steps                                                   | Expected Behavior                                    | SRS Ref   |
| --------- | ------------------------------------------------ | ------------------------------------------------------- | ---------------------------------------------------- | --------- |
| EC-TC-001 | Monitored app used for exactly 59 seconds        | Open monitored app → leave at 59s                       | No overlay shown, session not persisted (below min)  | EC-01     |
| EC-TC-002 | Open monitored app then immediately switch       | Open for <5s → switch away                              | Session starts, enters cooldown, discarded if < 60s  | EC-02     |
| EC-TC-003 | Phone call interrupts monitored app usage        | Use monitored app → receive phone call → return         | Session enters cooldown → resumes on return          | EC-03     |
| EC-TC-004 | No apps selected but monitoring started          | Start monitoring with 0 selected apps                   | Service runs but takes no action                     | EC-04     |
| EC-TC-005 | Device clock/timezone change during session      | Change timezone mid-session                             | Elapsed time unaffected (uses elapsedRealtime)       | EC-05     |
| EC-TC-006 | 100+ apps installed on device                    | Device with 100+ apps → open App Selection              | List loads within 3s, scrolls smoothly               | EC-06     |
| EC-TC-007 | Overlay dragged to exact screen center           | Drag overlay to horizontal center → release             | Snaps to right edge (deterministic default)          | EC-07     |
| EC-TC-008 | Minimum threshold values (T1=1, T2=2, T3=3)     | Set custom: 1/2/3 → monitor for 8 min                  | All 5 visual states shown, throb at 8 min            | EC-08     |
| EC-TC-009 | Maximum threshold values (T1=40, T2=80, T3=120)  | Set custom: 40/80/120 → monitor for 39 min             | Stays in CALM state for 39 minutes                   | EC-09     |
| EC-TC-010 | Session spans midnight boundary                  | Start session before midnight → end after midnight      | Correct timestamps, no data corruption               | EC-10     |
| EC-TC-011 | Overlay reappears on resume from cooldown (≥60s)  | Session at 90s → cooldown → resume                     | Overlay reappears immediately with 90s+ time         | FR-018    |
| EC-TC-012 | Rapid app switching (stress test)                | Switch between 3+ monitored apps every 5 seconds       | No crashes, sessions transition correctly            | FR-017    |

---

## 13. Security & Privacy Test Plan

**Traces To:** NFR-011, NFR-012, Privacy.md

### 13.1 Data Locality Tests

| Test ID   | Test Case                                    | Method                                          | Pass Criteria                          | SRS Ref   |
| --------- | -------------------------------------------- | ----------------------------------------------- | -------------------------------------- | --------- |
| SP-DL-001 | No INTERNET permission in manifest           | Inspect AndroidManifest.xml                     | `INTERNET` not declared                | NFR-011   |
| SP-DL-002 | No network library in dependencies           | Inspect build.gradle dependencies               | No OkHttp, Retrofit, Ktor, etc.       | NFR-011   |
| SP-DL-003 | Zero network calls (static analysis)         | Run lint check for network API usage            | Zero findings                          | NFR-011   |
| SP-DL-004 | Zero network calls (runtime verification)    | Monitor network via `adb shell tcpdump` for 1h  | No outbound connections               | NFR-011   |

### 13.2 Data Protection Tests

| Test ID   | Test Case                                    | Method                                          | Pass Criteria                          | SRS Ref   |
| --------- | -------------------------------------------- | ----------------------------------------------- | -------------------------------------- | --------- |
| SP-DP-001 | Database in private internal storage         | Check file path of Room DB                      | Located in app's private directory     | NFR-012   |
| SP-DP-002 | No data on external storage                  | Check for files on SD card / shared dirs        | No MindfulScroll files found           | NFR-012   |
| SP-DP-003 | No exported ContentProvider                  | Inspect manifest for exported providers         | None declared or all `exported=false`  | NFR-012   |
| SP-DP-004 | Only launcher Activity exported              | Inspect manifest for exported components        | Only main Activity is exported        | NFR-012   |
| SP-DP-005 | allowBackup disabled or DB excluded          | Inspect manifest and backup rules               | DB not included in backup              | NFR-012   |
| SP-DP-006 | Uninstall removes all data                   | Install → use → uninstall → check data dirs     | All app data removed                  | Privacy   |

### 13.3 Permission Model Tests

| Test ID   | Test Case                                    | Method                                          | Pass Criteria                          | SRS Ref   |
| --------- | -------------------------------------------- | ----------------------------------------------- | -------------------------------------- | --------- |
| SP-PM-001 | Only required permissions declared            | Inspect manifest                                | Exactly 7 permissions per PMR-001     | PMR-001   |
| SP-PM-002 | Graceful degradation: no Usage Access        | Deny Usage Access → start monitoring            | Non-functional monitoring + banner    | PMR-002   |
| SP-PM-003 | Graceful degradation: no Overlay             | Deny Overlay → start monitoring                 | Monitoring works, no overlay shown    | PMR-002   |
| SP-PM-004 | Graceful degradation: no battery exemption   | Deny battery exemption                          | Monitoring works but may be killed    | PMR-002   |
| SP-PM-005 | POST_NOTIFICATIONS on API 33+               | Test on API 33+ device                          | Runtime permission requested          | PMR-001   |

---

## 14. Accessibility Test Plan

**Traces To:** NFR-010, UIUXspec.md

| Test ID   | Test Case                                    | Method                                          | Pass Criteria                          | SRS Ref   |
| --------- | -------------------------------------------- | ----------------------------------------------- | -------------------------------------- | --------- |
| AC-001    | Text contrast ratio (in-app screens)         | Use Accessibility Scanner or manual check       | ≥ 4.5:1 against background            | NFR-010   |
| AC-002    | Overlay text contrast in CALM state          | Check white text on #6B7280 at 65% opacity      | ≥ 3:1 contrast ratio                  | NFR-010   |
| AC-003    | Overlay text contrast in all visual states   | Check each of 5 states                          | ≥ 3:1 contrast ratio per state        | NFR-010   |
| AC-004    | Touch target size ≥ 48dp                     | Measure all interactive elements                | All buttons/checkboxes ≥ 48dp × 48dp  | NFR-010   |
| AC-005    | TalkBack support: Home screen                | Enable TalkBack → navigate Home                 | All elements have content descriptions | NFR-010   |
| AC-006    | TalkBack support: App Selection              | Enable TalkBack → navigate App Selection        | App names and checkboxes announced     | NFR-010   |
| AC-007    | TalkBack support: Onboarding                 | Enable TalkBack → navigate onboarding           | All steps navigable with TalkBack     | NFR-010   |
| AC-008    | Color-blind distinguishability               | Simulate deuteranopia/protanopia                | States distinguishable (opacity helps) | NFR-010   |

---

## 15. Endurance & Stress Test Plan

**Traces To:** NFR-002, NFR-003, NFR-006, RA-022

| Test ID   | Test Case                                    | Duration | Method                                          | Pass Criteria                          |
| --------- | -------------------------------------------- | -------- | ----------------------------------------------- | -------------------------------------- |
| EN-001    | 8-hour continuous monitoring (Pixel)         | 8 hours  | Start monitoring → use phone normally            | Service alive, battery ≤ 2%/hr, no leaks |
| EN-002    | 8-hour continuous monitoring (Samsung)       | 8 hours  | Same as above on Samsung device                  | Service alive with ≥ 95% uptime       |
| EN-003    | 8-hour continuous monitoring (Xiaomi)        | 8 hours  | Same as above on Xiaomi device                   | Service alive with ≥ 90% uptime       |
| EN-004    | Memory stability over 8 hours                | 8 hours  | Profile memory at 0h, 2h, 4h, 8h                | No unbounded memory growth             |
| EN-005    | Rapid session cycling (1000 sessions)        | 2 hours  | Switch apps every 60s to trigger many sessions   | No crashes, DB handles volume          |
| EN-006    | Overlay continuously visible for 2 hours     | 2 hours  | Use monitored app continuously for 2h            | Overlay stable, animations smooth      |
| EN-007    | Multiple service restarts                    | 1 hour   | Kill service via ADB every 5 minutes             | Service recovers each time cleanly     |

---

## 16. Regression Test Plan

### 16.1 Core Regression Suite

These tests must pass before any release. They form the minimum regression set run after each significant code change.

| Priority | Tests Included                                                                                     |
| -------- | -------------------------------------------------------------------------------------------------- |
| P0       | UT-SM-001 to UT-SM-012 (Session state machine)                                                    |
| P0       | UT-TE-001 to UT-TE-013 (Threshold engine)                                                         |
| P0       | IT-DB-001 to IT-DB-012 (Database operations)                                                       |
| P1       | ST-OV-001, ST-OV-007, ST-OV-013, ST-OV-016 (Overlay core: appear, drag, dismiss, no ghost)       |
| P1       | ST-FS-001, ST-FS-004, ST-FS-010 (Service start, stop, boot restart)                               |
| P1       | ST-FD-001, ST-FD-004 (Detection and adaptive polling)                                              |
| P2       | UI-HM-001 to UI-HM-005 (Home screen basics)                                                       |
| P2       | UI-AS-001, UI-AS-004, UI-AS-007 (App selection basics)                                             |
| P2       | SP-DL-001 to SP-DL-003 (Security: no internet, no network libs)                                   |

### 16.2 Regression Trigger Criteria

Run the regression suite when:
- Any change to the `domain` package (models, use cases, state machines)
- Any change to the `service` package (MonitoringService, PollingEngine, OverlayController)
- Any change to the `data` package (entities, DAOs, repositories)
- Any dependency version update
- Before each milestone gate (M0–M10)
- Before release candidate builds

---

## 17. Test Coverage Targets

**Traces To:** NFR-014, CodingStandards TST-01 through TST-07

| Module / Component         | Target Coverage | Type                    | Phase Achieved |
| -------------------------- | --------------- | ----------------------- | -------------- |
| Session State Machine      | 100%            | Unit (JUnit + MockK)    | P3             |
| Threshold Engine           | 100%            | Unit (JUnit)            | P5             |
| Cooldown Logic             | 100%            | Unit (JUnit + MockK)    | P3             |
| Domain Use Cases           | ≥ 80%           | Unit (JUnit + MockK)    | P5             |
| Repository Implementations | ≥ 70%           | Integration (Room test) | P2             |
| ViewModels                 | ≥ 60%           | Unit (Turbine + MockK)  | P6             |
| Overall Domain Layer       | ≥ 80%           | Combined                | P9             |

---

## 18. Bug Severity Classification

**Traces To:** ProjectPlan Section 11.3

| Severity     | Definition                                                                | Action                | Release Blocker? |
| ------------ | ------------------------------------------------------------------------- | --------------------- | ---------------- |
| **Critical** | App crashes, data loss, security vulnerability, service non-functional    | Fix immediately       | Yes              |
| **High**     | Feature non-functional, major UX defect, ghost overlay, wrong timing      | Fix before release    | Yes              |
| **Medium**   | Minor UX defect, edge case not graceful, cosmetic issue                   | Fix if time allows    | No               |
| **Low**      | Cosmetic, enhancement, "nice to fix"                                      | Defer to v1.1         | No               |

### 18.1 Examples per Severity

| Severity | Example                                                                          |
| -------- | -------------------------------------------------------------------------------- |
| Critical | App crashes when revoking Usage Access during active session                     |
| Critical | Session data corrupted after device reboot                                       |
| High     | Ghost overlay remains after session end on Samsung devices                        |
| High     | Throb animation triggers at wrong time (T3 instead of T3+5min)                  |
| High     | Touch events blocked by overlay on host app                                      |
| Medium   | Snap animation slightly jittery on low-end device                                |
| Medium   | Search filter takes >200ms on 200+ app list                                      |
| Low      | Overlay shadow slightly misaligned during throb                                  |
| Low      | "No apps found" message could be more descriptive                                |

---

## 19. Entry & Exit Criteria

### 19.1 Test Phase Entry Criteria

| Criterion                                              | Applies To           |
| ------------------------------------------------------ | -------------------- |
| Code compiles without errors                           | All test levels      |
| Relevant SRS requirements are finalized                | All test levels      |
| Test environment set up and verified                   | Integration, UI, System |
| Unit tests written and passing for changed modules     | Integration and above |
| Previous phase exit criteria met                       | Each successive phase |

### 19.2 Test Phase Exit Criteria

| Criterion                                              | Applies To           |
| ------------------------------------------------------ | -------------------- |
| All planned test cases executed                        | All test levels      |
| All Critical and High severity bugs resolved           | Release readiness    |
| Test coverage targets met (Section 17)                 | Unit, Integration    |
| Regression suite passes 100%                           | Release readiness    |
| OEM compatibility verified on all "Must" devices       | Release readiness    |
| Host app compatibility verified on all 10 apps         | Release readiness    |
| Endurance tests pass on at least 2 devices             | Release readiness    |
| No INTERNET permission in final build                  | Release readiness    |

### 19.3 MVP Release Gate Criteria

From ProjectPlan Section 12.3, the MVP is release-ready when:

1. All F-001 through F-015 features implemented and tested (all ST-* tests pass).
2. Google Play policy review passed.
3. Foreground service reliable for 8+ hours on Pixel, Samsung, Xiaomi (EN-001, EN-002, EN-003).
4. Battery ≤ 2%/hr confirmed (NF-PF-003).
5. Overlay tested on 10 host apps without interference (all HA-* tests pass).
6. Onboarding achieves >80% permission grant rate in internal testing.
7. Zero Critical or High severity open bugs.
8. Privacy policy published and accessible.

---

## 20. Test Schedule & Phase Mapping

**Traces To:** ProjectPlan Section 4 (Phases P0–P10)

| Project Phase | Phase Name                   | Testing Activities                                              |
| ------------- | ---------------------------- | --------------------------------------------------------------- |
| P0            | Documentation                | Test plan creation (this document)                              |
| P1            | Project Setup                | Test environment setup, emulator configuration                  |
| P2            | Data Layer                   | UT-MP-*, IT-DB-* (Repository + DAO tests)                      |
| P3            | Engine Layer                 | UT-SM-* (Session state machine), IT-SV-* (Service pipeline)    |
| P4            | Overlay System               | ST-OV-* (Manual overlay tests), UT-TE-* (Threshold engine)     |
| P5            | Visual States & Animations   | ST-TH-* (Color escalation), UT-TE-* (Throb timing)             |
| P6            | Presentation Layer           | UT-VM-*, UI-* (ViewModel tests, Compose UI tests), IT-NV-*     |
| P7            | Onboarding Flow              | ST-PO-* (Permission onboarding tests)                           |
| P8            | Integration & Polish         | NF-PF-* (Performance), SP-* (Security/Privacy), AC-* (Accessibility) |
| P9            | Testing & OEM Validation     | OEM-*, HA-*, EN-* (OEM compat, Host app compat, Endurance)     |
| P10           | Release Preparation          | Full regression suite, MVP release gate verification            |

---

## 21. Risk-Based Test Prioritization

Tests are prioritized based on the risk scores from [RiskAssessment.md](RiskAssessment.md). Higher-risk areas receive more test cases and earlier testing.

| Risk ID | Risk Description                    | Score | Priority Tests                                                | Phase |
| ------- | ----------------------------------- | ----- | ------------------------------------------------------------- | ----- |
| RA-002  | OEM battery kill (Critical, 16)     | 16    | OEM-001, EN-001/002/003, NF-RL-001/002/003, ST-FS-009/010    | P3, P9|
| RA-001  | Play Store rejection (High, 15)     | 15    | SP-DL-001/002/003, SP-PM-001, SP-DP-005                      | P8    |
| RA-003  | Onboarding abandonment (High, 12)   | 12    | ST-PO-001 through ST-PO-010, UI-OB-*, NFR-009                | P7    |
| RA-004  | Overlay perceived as annoying (12)  | 12    | ST-OV-*, HA-*, AC-008                                         | P4, P5|
| RA-010  | Touch interference (High, 12)       | 12    | ST-OV-017/018, HA-005, OEM-007                                | P4, P9|
| RA-011  | Ghost overlay (High, 12)            | 12    | ST-OV-016, HA-008, OEM-008, EH-TC-005                        | P4, P9|
| RA-012  | State machine errors (12)           | 12    | UT-SM-001 to UT-SM-012 (100% coverage required)              | P3    |
| RA-007  | UsageStats inconsistency (9)        | 9     | OEM-004/005, EH-TC-006, ST-FD-001/002                        | P3, P9|
| RA-014  | Overlay jank (9)                    | 9     | NF-PF-008/009, ST-TH-006/007                                 | P5, P8|
| RA-022  | Memory leak (8)                     | 8     | NF-PF-005/006/007, EN-004                                    | P8, P9|
| RA-018  | Migration failures (6)             | 6     | IT-DB-009, NF-RL-008                                         | P2    |

---

## 22. Traceability Matrix

### 22.1 SRS Functional Requirement → Test Case Mapping

| SRS Requirement | Test Cases                                                         |
| --------------- | ------------------------------------------------------------------ |
| FR-001          | UT-VM-004, ST-AD-001, ST-AD-002, ST-AD-003, ST-AD-004             |
| FR-002          | ST-AD-005, ST-AD-006, UI-AS-001                                   |
| FR-003          | UT-VM-004, UI-AS-002                                               |
| FR-004          | UT-VM-005, UI-AS-004, UI-AS-005                                   |
| FR-005          | UT-VM-006, UI-AS-007, UI-AS-008, UT-VM-010                        |
| FR-006          | UI-AS-009                                                          |
| FR-007          | UT-VM-005, IT-DB-001, IT-DB-002, ST-AD-007, ST-AD-008, ST-AD-009 |
| FR-008          | UT-VM-007, UT-VM-008, UT-VM-009, UI-AS-010, UI-AS-011            |
| FR-009          | IT-SV-001, IT-SV-002, ST-FD-001                                   |
| FR-010          | IT-SV-007, ST-FD-004, ST-FD-005                                   |
| FR-011          | ST-FD-001, ST-FD-002, ST-FD-003                                   |
| FR-012          | UT-SM-001, ST-SS-001                                               |
| FR-013          | UT-SM-004, UT-SM-011, UT-SM-012, ST-SS-003                        |
| FR-014          | UT-SM-003, UT-SM-005, UT-SM-010, ST-SS-002, ST-SS-004             |
| FR-015          | UT-SM-005, IT-SV-005, ST-SS-004, ST-SS-008                        |
| FR-016          | UT-SM-006, UT-SM-007, UT-SM-008, ST-SS-005, ST-SS-006             |
| FR-017          | UT-SM-002, UT-SM-009, ST-SS-007, EC-TC-012                        |
| FR-018          | UT-TE-012, UT-TE-013, IT-SV-003, ST-OV-001, ST-OV-002            |
| FR-019          | UT-MP-005, UT-MP-006, ST-OV-003, ST-OV-004, ST-OV-005, ST-OV-018 |
| FR-020          | ST-OV-006                                                          |
| FR-021          | ST-OV-007, ST-OV-017                                               |
| FR-022          | ST-OV-008, ST-OV-009, ST-OV-010                                   |
| FR-023          | IT-DB-012, ST-OV-011, ST-OV-012                                   |
| FR-024          | ST-OV-013, ST-OV-014, ST-OV-015, ST-OV-016                       |
| FR-025          | UT-TV-001, UT-TV-002, UI-TC-001, UI-TC-002, UI-TC-004             |
| FR-026          | UT-TV-003 through UT-TV-007, UI-TC-003, UI-TC-005, UI-TC-006      |
| FR-027          | UT-TV-008, IT-DB-004, IT-DB-005, UI-TC-007, UI-TC-008, ST-TH-001, ST-TH-009 |
| FR-028          | UT-TE-001 through UT-TE-009, IT-SV-004, ST-TH-002 through ST-TH-004 |
| FR-029          | ST-TH-006                                                          |
| FR-030          | UT-TE-005, UT-TE-009, ST-TH-005                                   |
| FR-031          | ST-TH-007, ST-TH-008                                               |
| FR-032          | UI-OB-001 through UI-OB-008, ST-PO-001, ST-PO-005, ST-PO-006     |
| FR-033          | UI-HM-008, UI-HM-009, ST-PO-008, ST-PO-009                       |
| FR-034          | ST-PO-002, ST-PO-003, ST-PO-004, ST-PO-010                       |
| FR-035          | ST-FS-001, ST-FS-002, ST-FS-003, ST-FS-004                        |
| FR-036          | ST-FS-005, ST-FS-006, ST-FS-007, ST-FS-008                        |
| FR-037          | ST-FS-009, NF-RL-004                                               |
| FR-038          | IT-DB-008, ST-FS-010, ST-FS-011, ST-FS-012                        |
| FR-039          | UT-VM-001, UT-VM-002, UI-HM-001 through UI-HM-007, ST-HN-001, ST-HN-002 |
| FR-040          | UT-VM-003, UI-HM-004, UI-HM-005, ST-FS-001, ST-FS-004, ST-FS-013, ST-HN-003 |
| FR-041          | IT-NV-001 through IT-NV-005, ST-HN-004                            |

### 22.2 SRS Non-Functional Requirement → Test Case Mapping

| SRS Requirement | Test Cases                                                |
| --------------- | --------------------------------------------------------- |
| NFR-001         | NF-PF-001, NF-PF-002                                     |
| NFR-002         | NF-PF-003, NF-PF-004, EN-001, EN-002, EN-003             |
| NFR-003         | NF-PF-005, NF-PF-006, NF-PF-007, EN-004                  |
| NFR-004         | NF-PF-008, NF-PF-009                                     |
| NFR-005         | NF-PF-010, NF-PF-011, NF-PF-012, IT-DB-010               |
| NFR-006         | NF-RL-001, NF-RL-002, NF-RL-003, NF-RL-004, OEM-001     |
| NFR-007         | NF-RL-005, NF-RL-006, NF-RL-007                          |
| NFR-008         | NF-RL-008, IT-DB-009                                      |
| NFR-009         | UI-OB-001 through UI-OB-008, ST-PO-007                   |
| NFR-010         | AC-001 through AC-008                                     |
| NFR-011         | SP-DL-001, SP-DL-002, SP-DL-003, SP-DL-004               |
| NFR-012         | SP-DP-001 through SP-DP-006                               |
| NFR-013         | (Architecture review — not testable via test cases)       |
| NFR-014         | Unit test coverage metrics (Section 17)                   |
| NFR-015         | Emulator tests across API 26, 28, 29, 30, 31, 33, 34, 35, 36 |
| NFR-016         | OEM-001 through OEM-008                                   |

### 22.3 Risk → Test Case Mapping

| Risk ID | Risk                              | Mitigating Test Cases                                              |
| ------- | --------------------------------- | ------------------------------------------------------------------ |
| RA-001  | Play Store rejection              | SP-DL-001/002/003, SP-PM-001, SP-DP-005                           |
| RA-002  | OEM battery kill                  | OEM-001, EN-001/002/003, NF-RL-001/002/003/004, ST-FS-009/010     |
| RA-003  | Onboarding abandonment            | UI-OB-*, ST-PO-*, AC-005/006/007                                  |
| RA-004  | Overlay annoying                  | ST-OV-*, HA-*, AC-008                                             |
| RA-007  | UsageStats inconsistency          | OEM-004/005, EH-TC-006, ST-FD-*                                   |
| RA-010  | Touch interference                | ST-OV-017/018, HA-005, OEM-007                                    |
| RA-011  | Ghost overlay                     | ST-OV-016, HA-008, OEM-008, EH-TC-005                             |
| RA-012  | State machine errors              | UT-SM-001 through UT-SM-012                                       |
| RA-014  | Overlay jank                      | NF-PF-008/009, ST-TH-006/007                                      |
| RA-018  | Migration failures                | IT-DB-009, NF-RL-008                                               |
| RA-022  | Memory leak                       | NF-PF-005/006/007, EN-004                                         |

### 22.4 PRD Feature → Test Case Mapping

| PRD Feature | Description                  | Test Cases                                          |
| ----------- | ---------------------------- | --------------------------------------------------- |
| F-001       | App Discovery                | UT-VM-004, ST-AD-001 to ST-AD-006, NF-PF-013       |
| F-002       | App Selection                | UT-VM-005 to UT-VM-010, UI-AS-001 to UI-AS-011     |
| F-003       | Persist Selections           | IT-DB-001/002, ST-AD-007 to ST-AD-009               |
| F-004       | Foreground Detection         | IT-SV-001/002/007, ST-FD-001 to ST-FD-005           |
| F-005       | Session Tracking             | UT-SM-001 to UT-SM-012, ST-SS-001 to ST-SS-008     |
| F-006       | Floating Timer Overlay       | ST-OV-001 to ST-OV-006, ST-OV-018                  |
| F-007       | Overlay Drag & Snap          | ST-OV-007 to ST-OV-010, ST-OV-017                  |
| F-008       | Position Persistence         | IT-DB-012, ST-OV-011, ST-OV-012                    |
| F-009       | Threshold Configuration      | UT-TV-001 to UT-TV-008, UI-TC-001 to UI-TC-008, ST-TH-001/009 |
| F-010       | Color Escalation             | UT-TE-001 to UT-TE-009, ST-TH-002 to ST-TH-006    |
| F-011       | Throb Animation              | UT-TE-005/009, ST-TH-005/007/008                   |
| F-012       | Overlay Lifecycle            | ST-OV-013 to ST-OV-016                             |
| F-013       | Permission Onboarding        | UI-OB-001 to UI-OB-008, ST-PO-001 to ST-PO-010    |
| F-014       | Foreground Service           | ST-FS-001 to ST-FS-009, ST-FS-013                  |
| F-015       | Boot Auto-Restart            | ST-FS-010, ST-FS-011, ST-FS-012                    |

---

## 23. Glossary

All terms from PRD (Section 17), SRS (Section 14), SAD (Section 23), and TDD (Section 19) apply. Additional test-specific terms:

| Term                        | Definition                                                                                      |
| --------------------------- | ----------------------------------------------------------------------------------------------- |
| **Test Case**               | A set of preconditions, inputs, execution steps, and expected results for verifying a requirement. |
| **Test Suite**              | A collection of test cases grouped by test level or feature area.                               |
| **Regression Suite**        | The set of test cases executed after each significant change to verify no existing behavior broke. |
| **Endurance Test**          | A test that runs the system under normal load for an extended duration (8+ hours) to detect degradation. |
| **Ghost Overlay**           | An overlay view that remains visible after it should have been removed — a high-severity defect. |
| **PSS**                     | Proportional Set Size — an Android memory metric for the app's share of physical RAM.           |
| **Host App**                | A third-party application over which MindfulScroll's floating timer overlay is displayed.       |
| **Edge-Snapping**           | Automatic animation of the overlay to the nearest screen edge after a drag release.             |
| **Cooldown**                | The 45-second grace period after a user leaves a monitored app before the session ends.         |
| **Visual State**            | One of five overlay appearance configurations: Calm, Notice, Alert, Urgent, Throb.              |
| **Dual-Query Strategy**     | Trying `queryEvents()` first and falling back to `queryUsageStats()` if the primary query fails.|

---

## 24. References

| Reference                                                                                      | Relevance                                              |
| ---------------------------------------------------------------------------------------------- | ------------------------------------------------------ |
| [PRD.md](PRD.md)                                                                               | Features, user stories, acceptance criteria scope       |
| [SRS.md](SRS.md)                                                                               | All FR, NFR, acceptance criteria, state machines        |
| [SAD.md](SAD.md)                                                                               | Architecture, components, testability design            |
| [TDD.md](TDD.md)                                                                               | Implementation details, test strategy per component    |
| [DbSchema.md](DbSchema.md)                                                                     | Database entities, DAOs, migration strategy             |
| [UIUXspec.md](UIUXspec.md)                                                                     | Visual states, animation specs, screen inventory        |
| [ProjectPlan.md](ProjectPlan.md)                                                               | Phase schedule, QA strategy, Definition of Done         |
| [RiskAssessment.md](RiskAssessment.md)                                                         | Risk register, mitigation priorities                    |
| [CodingStandards.md](CodingStandards.md)                                                       | Testing conventions TST-01 through TST-07               |
| [Privacy.md](Privacy.md)                                                                       | Data inventory, threat model, compliance requirements   |
| [JUnit 4](https://junit.org/junit4/)                                                           | Unit test framework                                     |
| [MockK](https://mockk.io/)                                                                     | Kotlin mocking library                                  |
| [Turbine](https://github.com/cashapp/turbine)                                                  | Kotlin Flow testing library                             |
| [Compose Testing](https://developer.android.com/develop/ui/compose/testing)                    | Jetpack Compose test framework                          |
| [Espresso](https://developer.android.com/training/testing/espresso)                            | Android UI testing framework                            |
| [Don't Kill My App](https://dontkillmyapp.com/)                                               | OEM battery management reference                        |

---

*End of Document*
