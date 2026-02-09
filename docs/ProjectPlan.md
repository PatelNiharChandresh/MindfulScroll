# Project Plan

## MindfulScroll — Digital Awareness Companion

---

### Document Control

| Field              | Value                                     |
| ------------------ | ----------------------------------------- |
| **Document Title** | MindfulScroll Project Plan                 |
| **Version**        | 1.0                                       |
| **Status**         | Draft                                     |
| **Created**        | 2026-02-05                                |
| **Last Updated**   | 2026-02-05                                |
| **Author**         | Rudy                                      |
| **Reviewers**      | —                                         |
| **Approved By**    | —                                         |

### Revision History

| Version | Date       | Author | Changes       |
| ------- | ---------- | ------ | ------------- |
| 1.0     | 2026-02-05 | Rudy   | Initial draft |

### Parent Documents

| Document                    | Version | Relationship                               |
| --------------------------- | ------- | ------------------------------------------ |
| [PRD.md](PRD.md)            | 1.0     | Product definition — features, scope, goals |
| [SRS.md](SRS.md)            | 1.0     | Technical requirements — FR, NFR, data model |

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Project Overview](#2-project-overview)
3. [Scope Summary](#3-scope-summary)
4. [Project Phases & Work Breakdown Structure](#4-project-phases--work-breakdown-structure)
5. [Phase Details](#5-phase-details)
6. [Milestone Schedule](#6-milestone-schedule)
7. [Dependency Graph](#7-dependency-graph)
8. [Resource Requirements](#8-resource-requirements)
9. [Technology Stack & Tooling](#9-technology-stack--tooling)
10. [Risk Management Plan](#10-risk-management-plan)
11. [Quality Assurance Strategy](#11-quality-assurance-strategy)
12. [Definition of Done](#12-definition-of-done)
13. [Communication & Reporting](#13-communication--reporting)
14. [Change Management](#14-change-management)
15. [Open Questions & Decision Log](#15-open-questions--decision-log)
16. [Document Roadmap](#16-document-roadmap)
17. [Glossary](#17-glossary)

---

## 1. Introduction

### 1.1 Purpose

This Project Plan defines the execution strategy for MindfulScroll MVP (v1.0). It breaks the work into phases, establishes milestones, identifies dependencies between work items, and defines the criteria that must be met before each phase is considered complete. It provides the blueprint for how we move from documented requirements to a shipped, production-quality Android application on the Google Play Store.

### 1.2 Scope of This Plan

This plan covers the **MVP release** (PRD features F-001 through F-015, SRS requirements FR-001 through FR-041, NFR-001 through NFR-016). Post-MVP versions (v1.1, v1.2+) are referenced for forward planning but are not scheduled in detail.

### 1.3 Intended Audience

| Audience            | Usage                                                       |
| ------------------- | ----------------------------------------------------------- |
| Developer(s)        | Implementation sequence, task breakdown, dependency awareness |
| Project owner       | Progress tracking, milestone verification, scope management  |
| QA / Testers        | Testing phase entry criteria, test scope per phase           |
| Reviewers           | Phase gate approval criteria                                 |

---

## 2. Project Overview

### 2.1 Project Summary

| Parameter               | Value                                                 |
| ----------------------- | ----------------------------------------------------- |
| **Product**             | MindfulScroll — Digital Awareness Companion            |
| **Platform**            | Native Android (Kotlin, Jetpack Compose)               |
| **Target Release**      | MVP (v1.0) on Google Play Store                        |
| **Architecture**        | Clean Architecture, MVVM, Single-Activity              |
| **Key Technical Challenges** | Foreground service reliability, system overlay, UsageStats polling, OEM compatibility |
| **Data Strategy**       | 100% local, on-device, Room database                   |
| **Network**             | None (fully offline)                                   |

### 2.2 Success Criteria (from PRD Section 5)

The project is successful when:

1. All 15 MVP features (F-001–F-015) are implemented and pass acceptance criteria.
2. The app passes Google Play's `PACKAGE_USAGE_STATS` policy review.
3. The foreground service runs reliably for 8+ hours on Pixel, Samsung, and Xiaomi.
4. Battery consumption is confirmed below 2% per hour of active monitoring.
5. The overlay does not interfere with touch events on 10+ tested host apps.
6. No critical or high-severity bugs remain open.
7. Privacy policy is published and accessible.

---

## 3. Scope Summary

### 3.1 In Scope (MVP)

| Area                     | Features                                                    | SRS Requirements          |
| ------------------------ | ----------------------------------------------------------- | ------------------------- |
| App Discovery            | Scan, list, display installed apps                          | FR-001 – FR-003           |
| App Selection            | Checklist, search, select/deselect, persist                 | FR-004 – FR-008           |
| Foreground Detection     | UsageStats polling, adaptive behavior, detection accuracy   | FR-009 – FR-011           |
| Session Tracking         | Start, continue, cooldown, end, screen-off, concurrency     | FR-012 – FR-017           |
| Floating Timer Overlay   | Display, appearance, fade-in, drag, snap, position memory   | FR-018 – FR-024           |
| Threshold Configuration  | Presets, custom input, validation, persistence              | FR-025 – FR-027           |
| Color Escalation         | Five visual states, animated transitions                    | FR-028 – FR-029           |
| Throb Animation          | Trigger logic, breathing animation                          | FR-030 – FR-031           |
| Permission Onboarding    | Flow structure, status checks, deep-links                   | FR-032 – FR-034           |
| Foreground Service       | Lifecycle, notification, self-recovery                      | FR-035 – FR-037           |
| Boot Auto-Restart        | BOOT_COMPLETED receiver                                     | FR-038                    |
| Home Screen & Navigation | Status display, start/stop toggle, navigation graph         | FR-039 – FR-041           |

### 3.2 Out of Scope (MVP)

Dashboard (F-016), session history (F-017), per-app thresholds (F-018), pause monitoring (F-019), app categories (F-020), quick-select presets (F-021), timer tap interaction (F-022), monitoring schedule (F-023), and all future features (F-024–F-032). See PRD Section 11.2 for the full exclusion list.

---

## 4. Project Phases & Work Breakdown Structure

### 4.1 Phase Overview

```
Phase 0: Documentation & Design ◄── YOU ARE HERE
    │
    ▼
Phase 1: Project Foundation
    │
    ▼
Phase 2: Data Layer
    │
    ▼
Phase 3: Core Monitoring Engine
    │
    ▼
Phase 4: Floating Timer Overlay
    │
    ▼
Phase 5: Threshold & Escalation System
    │
    ▼
Phase 6: UI Screens
    │
    ▼
Phase 7: Permission Onboarding
    │
    ▼
Phase 8: Integration & Polish
    │
    ▼
Phase 9: Testing & OEM Validation
    │
    ▼
Phase 10: Release Preparation
```

### 4.2 Work Breakdown Structure (WBS)

| WBS ID  | Phase | Task                                          | SRS Trace              | Depends On        |
| ------- | ----- | --------------------------------------------- | ---------------------- | ----------------- |
| **0.0** | **Phase 0: Documentation & Design**           |                        |                   |
| 0.1     | 0     | Create PRD                                    | —                      | —                 |
| 0.2     | 0     | Create SRS                                    | —                      | 0.1               |
| 0.3     | 0     | Create Project Plan                           | —                      | 0.1, 0.2          |
| 0.4     | 0     | Create System Architecture Document           | —                      | 0.2               |
| 0.5     | 0     | Create Technical Design Document              | —                      | 0.4               |
| 0.6     | 0     | Create UI/UX Design Specification             | —                      | 0.2               |
| 0.7     | 0     | Create Database Schema Document               | —                      | 0.2               |
| 0.8     | 0     | Create Coding Standards & Conventions         | —                      | 0.4               |
| 0.9     | 0     | Create Privacy & Data Handling Document        | —                      | 0.2               |
| 0.10    | 0     | Resolve all PRD Open Questions (OQ-1 – OQ-7)  | —                      | 0.1               |
| **1.0** | **Phase 1: Project Foundation**               |                        |                   |
| 1.1     | 1     | Configure Clean Architecture package structure | NFR-013                | 0.4               |
| 1.2     | 1     | Set up dependency injection (Hilt or Koin)     | NFR-013.AC4            | 1.1               |
| 1.3     | 1     | Add Room, Navigation, Lifecycle dependencies   | PER-001                | 1.1               |
| 1.4     | 1     | Configure build variants (debug, release)      | PER-001                | 1.1               |
| 1.5     | 1     | Configure ProGuard/R8 rules                    | PER-001                | 1.4               |
| 1.6     | 1     | Set up notification channel for service        | FR-036.AC5             | 1.3               |
| 1.7     | 1     | Configure manifest: permissions, service, receiver declarations | PMR-001 | 1.3               |
| 1.8     | 1     | Set up Compose theme, colors, typography       | —                      | 1.3               |
| 1.9     | 1     | Set up navigation graph (NavHost + routes)     | FR-041                 | 1.3, 1.8          |
| 1.10    | 1     | Set up unit test framework and first test       | NFR-014                | 1.2               |
| **2.0** | **Phase 2: Data Layer**                       |                        |                   |
| 2.1     | 2     | Define Room entities: MonitoredApp             | DR-001                 | 1.3               |
| 2.2     | 2     | Define Room entities: ThresholdConfig          | DR-002                 | 1.3               |
| 2.3     | 2     | Define Room entities: UsageSession             | DR-003                 | 1.3               |
| 2.4     | 2     | Define Room entities: UserPreferences          | DR-004                 | 1.3               |
| 2.5     | 2     | Create Room database class with all DAOs       | NFR-008                | 2.1–2.4           |
| 2.6     | 2     | Implement MonitoredAppRepository               | FR-007                 | 2.5               |
| 2.7     | 2     | Implement ThresholdConfigRepository            | FR-027                 | 2.5               |
| 2.8     | 2     | Implement UsageSessionRepository               | FR-015                 | 2.5               |
| 2.9     | 2     | Implement UserPreferencesRepository            | DR-004                 | 2.5               |
| 2.10    | 2     | Implement session data retention cleanup       | DR-005                 | 2.8               |
| 2.11    | 2     | Unit test all repositories                     | NFR-014                | 2.6–2.10          |
| **3.0** | **Phase 3: Core Monitoring Engine**           |                        |                   |
| 3.1     | 3     | Define domain models: SessionState enum         | SRS 10.1               | 1.1               |
| 3.2     | 3     | Define domain models: TimerVisualState enum     | SRS 10.2               | 1.1               |
| 3.3     | 3     | Implement session state machine                 | FR-012 – FR-017        | 3.1, 2.8          |
| 3.4     | 3     | Implement PollingEngine (UsageStatsManager queries) | FR-009, FR-010, FR-011 | 1.7, 2.6      |
| 3.5     | 3     | Implement ScreenStateReceiver (screen on/off)   | FR-010, FR-016         | 1.7               |
| 3.6     | 3     | Implement SessionManager (orchestrates state machine + polling) | FR-012 – FR-017 | 3.3, 3.4, 3.5 |
| 3.7     | 3     | Implement MonitoringService (foreground service) | FR-035, FR-036, FR-037 | 3.6, 1.6      |
| 3.8     | 3     | Implement BootReceiver                          | FR-038                 | 3.7, 2.9          |
| 3.9     | 3     | Implement service start/stop logic              | FR-040                 | 3.7               |
| 3.10    | 3     | Unit test session state machine (all transitions) | NFR-014.AC1          | 3.3               |
| 3.11    | 3     | Unit test polling engine                        | NFR-014                | 3.4               |
| 3.12    | 3     | Integration test: service + polling + session    | —                      | 3.7               |
| **4.0** | **Phase 4: Floating Timer Overlay**           |                        |                   |
| 4.1     | 4     | Implement OverlayController (WindowManager add/update/remove) | FR-019, EIR-002 | 3.7          |
| 4.2     | 4     | Implement overlay layout (circular, 48–56dp, MM:SS text) | FR-019          | 4.1               |
| 4.3     | 4     | Implement fade-in animation (500ms, ease-in-out) | FR-020               | 4.2               |
| 4.4     | 4     | Implement fade-out animation (400ms)            | FR-024                 | 4.2               |
| 4.5     | 4     | Implement drag touch handling                   | FR-021                 | 4.2               |
| 4.6     | 4     | Implement edge-snapping behavior                | FR-022                 | 4.5               |
| 4.7     | 4     | Implement position persistence                  | FR-023                 | 4.6, 2.9          |
| 4.8     | 4     | Implement overlay display trigger (60-sec threshold) | FR-018            | 4.3, 3.6          |
| 4.9     | 4     | Implement overlay auto-dismiss on session end    | FR-024                | 4.4, 3.6          |
| 4.10    | 4     | Implement cooldown dimming behavior             | FR-014.AC4, FR-024.AC2 | 4.2, 3.6          |
| 4.11    | 4     | Implement overlay rotation handling             | EH-007                 | 4.7               |
| 4.12    | 4     | Manual test: drag, snap, fade, rotation across 5+ apps | —              | 4.1–4.11          |
| **5.0** | **Phase 5: Threshold & Escalation System**    |                        |                   |
| 5.1     | 5     | Implement ThresholdEngine (evaluates elapsed time vs. thresholds) | FR-028 | 3.2, 2.7    |
| 5.2     | 5     | Implement color escalation (5 visual states)    | FR-028                 | 5.1, 4.2          |
| 5.3     | 5     | Implement color transition animations (2000ms lerp) | FR-029             | 5.2               |
| 5.4     | 5     | Implement throb trigger logic (T3 + 5 min)      | FR-030                 | 5.1               |
| 5.5     | 5     | Implement throb animation (scale 1.0x–1.15x, 1500ms cycle) | FR-031     | 5.4, 4.2          |
| 5.6     | 5     | Connect ThresholdEngine to SessionManager and OverlayController | —      | 5.1–5.5, 3.6, 4.1 |
| 5.7     | 5     | Unit test threshold evaluation (all 5 states)    | NFR-014.AC2           | 5.1               |
| 5.8     | 5     | Unit test throb trigger conditions               | NFR-014.AC3           | 5.4               |
| 5.9     | 5     | Manual test: full escalation flow with real timer  | —                   | 5.6               |
| **6.0** | **Phase 6: UI Screens**                       |                        |                   |
| 6.1     | 6     | Implement Home Screen (status, app count, thresholds, toggle) | FR-039, FR-040 | 1.9, 3.9, 2.6, 2.7 |
| 6.2     | 6     | Implement App Selection Screen (list, icons, checkboxes) | FR-004, FR-002, EIR-003 | 1.9, 2.6  |
| 6.3     | 6     | Implement app search/filter functionality        | FR-005                 | 6.2               |
| 6.4     | 6     | Implement selection count indicator              | FR-006                 | 6.2               |
| 6.5     | 6     | Implement select all / deselect all              | FR-008                 | 6.2               |
| 6.6     | 6     | Implement Threshold Configuration Screen         | FR-025, FR-026         | 1.9, 2.7          |
| 6.7     | 6     | Implement threshold validation (T1 < T2 < T3)   | FR-026.AC5             | 6.6               |
| 6.8     | 6     | Implement About Screen                           | —                      | 1.9               |
| 6.9     | 6     | Connect all screens to navigation graph          | FR-041                 | 6.1–6.8           |
| 6.10    | 6     | Implement permission status banner on Home       | FR-033                 | 6.1               |
| 6.11    | 6     | UI test: complete navigation flow                | FR-041                 | 6.9               |
| **7.0** | **Phase 7: Permission Onboarding**            |                        |                   |
| 7.1     | 7     | Implement Welcome screen                         | FR-032 Step 1          | 1.9               |
| 7.2     | 7     | Implement Usage Access permission screen         | FR-032 Step 2, FR-034.AC1 | 7.1           |
| 7.3     | 7     | Implement Overlay permission screen              | FR-032 Step 3, FR-034.AC2 | 7.2           |
| 7.4     | 7     | Implement Battery Optimization permission screen | FR-032 Step 4, FR-034.AC3 | 7.3           |
| 7.5     | 7     | Implement Completion screen                      | FR-032 Step 5          | 7.4               |
| 7.6     | 7     | Implement permission grant verification on return | FR-032.AC4, AC5, AC6  | 7.2–7.4           |
| 7.7     | 7     | Implement skip logic with warning                | FR-032.AC7             | 7.6               |
| 7.8     | 7     | Implement onboarding-completed flag persistence  | DR-004 (onboarding_completed) | 7.5, 2.9   |
| 7.9     | 7     | Implement conditional onboarding vs. home routing | FR-041.AC3, AC4, AC5 | 7.8, 6.1          |
| 7.10    | 7     | Implement deep-link fallback for unresolvable intents | FR-034.AC4       | 7.2–7.4           |
| 7.11    | 7     | Manual test: full onboarding on Pixel, Samsung, Xiaomi | NFR-009         | 7.1–7.10          |
| **8.0** | **Phase 8: Integration & Polish**             |                        |                   |
| 8.1     | 8     | End-to-end integration: onboarding → selection → start monitoring → overlay appears | — | All P1–P7 |
| 8.2     | 8     | Implement uninstalled-app cleanup logic          | EH-001, FR-007.AC5     | 2.6, 3.4          |
| 8.3     | 8     | Implement permission revocation handling          | EH-002, FR-033        | 3.7, 6.10         |
| 8.4     | 8     | Implement overlay window failure handling (try-catch) | EH-004            | 4.1               |
| 8.5     | 8     | Implement empty UsageStats data handling          | EH-005                | 3.4               |
| 8.6     | 8     | Implement database write failure handling         | EH-006                | 2.5               |
| 8.7     | 8     | Implement split-screen/multi-window behavior      | EH-008                | 3.4               |
| 8.8     | 8     | Apply edge-case hardening (EC-01 through EC-10)   | SRS 11.2              | 3.6, 4.8, 5.1     |
| 8.9     | 8     | Performance profiling: battery consumption         | NFR-002               | 3.7               |
| 8.10    | 8     | Performance profiling: memory consumption          | NFR-003               | 3.7, 4.1          |
| 8.11    | 8     | Performance profiling: overlay render FPS           | NFR-004               | 4.2, 5.5          |
| 8.12    | 8     | Performance profiling: startup time                | NFR-001               | 6.1               |
| 8.13    | 8     | Accessibility pass: contrast, touch targets, TalkBack | NFR-010           | 6.1–6.8, 7.1–7.5  |
| 8.14    | 8     | Code cleanup and linting                           | NFR-013               | All                |
| **9.0** | **Phase 9: Testing & OEM Validation**         |                        |                   |
| 9.1     | 9     | Execute full unit test suite                       | NFR-014               | 3.10, 3.11, 5.7, 5.8, 2.11 |
| 9.2     | 9     | Execute integration test suite                     | —                     | 3.12, 8.1         |
| 9.3     | 9     | Manual test: complete user flow end-to-end         | PRD 16.2              | 8.1               |
| 9.4     | 9     | OEM test: Google Pixel (Stock Android)             | NFR-016 (Must)        | 8.1               |
| 9.5     | 9     | OEM test: Samsung (One UI)                         | NFR-016 (Must)        | 8.1               |
| 9.6     | 9     | OEM test: Xiaomi (MIUI / HyperOS)                 | NFR-016 (Must)        | 8.1               |
| 9.7     | 9     | OEM test: OnePlus (OxygenOS)                       | NFR-016 (Should)      | 8.1               |
| 9.8     | 9     | Overlay host-app compatibility test (10 apps)      | PRD 16.2 #5           | 8.1               |
| 9.9     | 9     | 8-hour continuous monitoring endurance test         | PRD 16.2 #3, NFR-006  | 8.1               |
| 9.10    | 9     | Battery profiling verification (final)             | NFR-002               | 8.9               |
| 9.11    | 9     | Boot-restart verification on all Must OEMs         | FR-038, NFR-016.AC3   | 9.4–9.6           |
| 9.12    | 9     | Bug triage and fix cycle                           | —                     | 9.1–9.11          |
| 9.13    | 9     | Regression test after bug fixes                    | —                     | 9.12              |
| **10.0** | **Phase 10: Release Preparation**            |                        |                   |
| 10.1    | 10    | Write user-facing privacy policy                   | PRD 16.2 #8           | 0.9               |
| 10.2    | 10    | Integrate privacy policy link in About screen      | —                     | 10.1, 6.8         |
| 10.3    | 10    | Prepare Google Play `PACKAGE_USAGE_STATS` declaration form | PRD R-01    | 10.1              |
| 10.4    | 10    | Configure release signing (keystore)               | —                     | 1.4               |
| 10.5    | 10    | Generate release APK / AAB                         | —                     | 10.4, 9.13        |
| 10.6    | 10    | Prepare Play Store listing: title, description, screenshots | —           | 9.13              |
| 10.7    | 10    | Prepare Play Store listing: content rating questionnaire | —              | 10.6              |
| 10.8    | 10    | Submit to Google Play for review                   | PRD BG-1              | 10.3, 10.5–10.7   |
| 10.9    | 10    | Address Google Play review feedback (if any)       | —                     | 10.8              |
| 10.10   | 10    | Staged rollout: 20% → 50% → 100%                  | —                     | 10.9              |

---

## 5. Phase Details

### Phase 0: Documentation & Design

**Objective:** Produce all design and planning documents before writing any production code. Resolve all open questions. Establish shared understanding of what will be built and how.

**Entry Criteria:** PRD approved.

**Deliverables:**

| ID   | Deliverable                             | Status      |
| ---- | --------------------------------------- | ----------- |
| 0.1  | Product Requirements Document (PRD.md)  | Complete    |
| 0.2  | Software Requirements Specification (SRS.md) | Complete |
| 0.3  | Project Plan (ProjectPlan.md)           | Complete    |
| 0.4  | System Architecture Document (SAD.md)   | Complete    |
| 0.5  | Technical Design Document (TDD.md)      | Pending     |
| 0.6  | UI/UX Design Specification (UISpec.md)  | Pending     |
| 0.7  | Database Schema Document (DBSchema.md)  | Pending     |
| 0.8  | Coding Standards (CodingStandards.md)   | Pending     |
| 0.9  | Privacy & Data Handling (Privacy.md)    | Pending     |
| 0.10 | Open Questions resolved                 | Pending     |

**Exit Criteria:** All documents created, reviewed, and approved. All 7 open questions (OQ-1 through OQ-7) resolved with decisions recorded.

---

### Phase 1: Project Foundation

**Objective:** Set up the project scaffolding, architecture skeleton, dependency injection, navigation graph, and build configuration so that all subsequent phases have a clean foundation to build on.

**Entry Criteria:** Phase 0 complete. SAD and Coding Standards documents approved.

**Deliverables:**

- Clean Architecture package structure established
- Dependency injection configured and verified
- All required libraries added to `build.gradle.kts`
- Manifest configured with all required permissions, service, and receiver declarations
- Navigation graph with placeholder screens for all routes
- Compose theme (colors, typography, shapes) configured per UI spec
- Notification channel created
- Debug and release build variants configured
- ProGuard/R8 basic rules in place
- First unit test executing successfully

**Exit Criteria:** Project compiles, runs on a device, navigates between placeholder screens, and executes at least one unit test. All declared permissions are present in the manifest.

**SRS Requirements Addressed:** NFR-013, FR-041, PMR-001, PER-001, FR-036.AC5

---

### Phase 2: Data Layer

**Objective:** Implement the complete persistence layer — Room database, all entities, DAOs, and repositories.

**Entry Criteria:** Phase 1 complete. Database Schema Document approved.

**Deliverables:**

- Room database class with version 1 schema
- All 4 entities (MonitoredApp, ThresholdConfig, UsageSession, UserPreferences) implemented
- All DAOs with CRUD operations
- Repository interfaces (domain layer) and implementations (data layer)
- Session data retention cleanup (90-day rule)
- Unit tests for all repository operations

**Exit Criteria:** All DB operations (insert, query, update, delete) pass unit tests. Data survives app process kill and relaunch (verified on device).

**SRS Requirements Addressed:** DR-001 – DR-005, FR-007, FR-027, FR-015, NFR-008

---

### Phase 3: Core Monitoring Engine

**Objective:** Implement the heart of the application — the foreground service, polling engine, session state machine, and boot receiver. After this phase, the app can detect foreground app usage and track sessions internally (no overlay yet).

**Entry Criteria:** Phase 2 complete.

**Deliverables:**

- SessionState enum and state machine implementation
- TimerVisualState enum
- PollingEngine with UsageStatsManager integration
- ScreenStateReceiver (screen on/off handling)
- SessionManager (orchestration of state machine, polling, cooldown)
- MonitoringService (foreground service with notification)
- BootReceiver (BOOT_COMPLETED handling)
- Service start/stop logic tied to persisted monitoring flag
- Unit tests for session state machine (all transitions)
- Unit tests for polling engine
- Integration test for service + polling + session

**Exit Criteria:** When a monitored app is opened on a test device, the service detects it within 3 seconds. Session state machine transitions correctly through ACTIVE → COOLDOWN → ENDED. Service survives activity destruction. Service restarts after device reboot (with monitoring flag set).

**SRS Requirements Addressed:** FR-009 – FR-017, FR-035 – FR-038, FR-040, EIR-001, NFR-006

**Risks:** This phase has the highest technical risk. UsageStatsManager behavior varies across OEMs. Validate on at least one non-Pixel device before proceeding.

---

### Phase 4: Floating Timer Overlay

**Objective:** Implement the floating overlay system — rendering, animations, drag, snap, position persistence, and lifecycle management tied to session state.

**Entry Criteria:** Phase 3 complete.

**Deliverables:**

- OverlayController managing WindowManager add/update/remove
- Circular overlay view (48–56dp, MM:SS text, 1-second update)
- Fade-in animation (500ms)
- Fade-out animation (400ms)
- Drag touch handler
- Edge-snapping behavior
- Position persistence (edge + Y-percent)
- Display trigger at 60-second session mark
- Auto-dismiss on session end
- Cooldown dimming behavior
- Rotation handling
- Manual test across 5+ host apps

**Exit Criteria:** Timer appears at 60 seconds, is draggable, snaps to edges, remembers position, dims during cooldown, fades out on session end. No ghost overlays. No touch interference with host apps.

**SRS Requirements Addressed:** FR-018 – FR-024, EIR-002, EH-004, EH-007, NFR-004

---

### Phase 5: Threshold & Escalation System

**Objective:** Implement the visual escalation system — threshold evaluation, color transitions, and throb animation. After this phase, the complete monitoring-to-visual-feedback pipeline is operational.

**Entry Criteria:** Phase 4 complete.

**Deliverables:**

- ThresholdEngine with state evaluation logic
- 5 visual states implemented (Calm, Notice, Alert, Urgent, Throb)
- Color transition animations (2000ms lerp)
- Throb animation (1.0x–1.15x scale, 1500ms cycle, infinite repeat)
- ThresholdEngine connected to SessionManager and OverlayController
- Unit tests for threshold evaluation (all 5 states, boundary conditions)
- Unit tests for throb trigger conditions
- Manual test: full escalation flow on a real device

**Exit Criteria:** Starting from Calm, the timer transitions through all 5 states at the correct times. Color transitions are smooth (no jank). Throb activates exactly at T3 + 5 min and continues until session ends.

**SRS Requirements Addressed:** FR-028 – FR-031, NFR-004

---

### Phase 6: UI Screens

**Objective:** Implement all user-facing in-app screens: Home, App Selection, Threshold Configuration, and About.

**Entry Criteria:** Phase 2 complete. Phase 3 complete (for Home Screen start/stop toggle). UI/UX Design Specification approved.

**Deliverables:**

- Home Screen with monitoring status, app count, threshold display, and start/stop toggle
- App Selection Screen with app list, icons, checkboxes, search, select all/deselect all, count indicator
- Threshold Configuration Screen with presets, custom input, validation
- About Screen with app info and privacy policy link
- Permission status banner on Home Screen
- All screens connected to navigation graph
- UI test for complete navigation flow

**Exit Criteria:** All screens render correctly, are navigable, and are connected to their respective data sources (repositories/ViewModels). Search filters work. Threshold validation prevents T1 ≥ T2 ≥ T3. Start/stop toggle controls the service.

**SRS Requirements Addressed:** FR-001 – FR-008, FR-025 – FR-027, FR-033, FR-039 – FR-041, EIR-003, EIR-004

---

### Phase 7: Permission Onboarding

**Objective:** Implement the first-launch onboarding flow that guides users through granting all required permissions.

**Entry Criteria:** Phase 6 complete (Home Screen exists as the post-onboarding destination).

**Deliverables:**

- Welcome screen
- 3 permission request screens (Usage Access, Overlay, Battery Optimization)
- Completion screen
- Permission grant verification on return from Settings
- Skip logic with warning per step
- Onboarding-completed flag persistence
- Conditional routing: onboarding vs. home on app launch
- Deep-link fallback for unresolvable intents
- Manual test on Pixel, Samsung, Xiaomi

**Exit Criteria:** A fresh install walks the user through all 5 onboarding steps. Granting permissions is detected on return. Skipping shows a warning. After completion, subsequent launches go directly to Home. On Pixel, Samsung, and Xiaomi, all deep-links resolve to the correct Settings page.

**SRS Requirements Addressed:** FR-032 – FR-034, FR-041.AC3–AC5, NFR-009

---

### Phase 8: Integration & Polish

**Objective:** Connect all components end-to-end, implement error handling for all defined scenarios, harden edge cases, profile performance, and ensure accessibility compliance.

**Entry Criteria:** Phases 3–7 complete.

**Deliverables:**

- End-to-end flow verified: onboarding → selection → monitoring → overlay → escalation → session end
- All 8 error handlers implemented (EH-001 – EH-008)
- All 10 edge cases hardened (EC-01 – EC-10)
- Performance profiled: battery, memory, FPS, startup time
- Accessibility pass: contrast ratios, touch targets, TalkBack labels
- Code cleanup, linting, dead code removal

**Exit Criteria:** The complete user journey works without errors. No crashes under error conditions. Battery ≤ 2%/hr. Memory ≤ 80MB. Startup ≤ 2 sec. Overlay ≥ 55 FPS. All accessibility checks pass.

**SRS Requirements Addressed:** SRS Section 11 (all EH and EC), NFR-001 – NFR-004, NFR-010, NFR-011, NFR-012

---

### Phase 9: Testing & OEM Validation

**Objective:** Execute the complete test plan — unit, integration, manual, OEM compatibility, endurance, and performance verification. Triage and fix all bugs.

**Entry Criteria:** Phase 8 complete.

**Deliverables:**

- Full unit test suite passing (≥80% domain layer coverage)
- Integration test suite passing
- Manual test: complete user flow verified
- OEM testing completed on Pixel, Samsung, Xiaomi (must), OnePlus (should)
- Overlay compatibility verified on 10 host apps
- 8-hour endurance test passed on 3 OEMs
- Battery profiling final report (confirming ≤ 2%/hr)
- Boot-restart verified on all Must OEMs
- Bug list triaged: zero critical/high bugs remain
- Regression tests passed after all bug fixes

**Exit Criteria:** All MVP acceptance criteria (PRD Section 16.2) are met. Zero critical or high bugs. All Must-priority OEM tests pass.

**SRS Requirements Addressed:** NFR-006, NFR-007, NFR-014 – NFR-016, PRD 16.2

---

### Phase 10: Release Preparation

**Objective:** Prepare all assets and submissions for Google Play Store launch.

**Entry Criteria:** Phase 9 complete. All MVP acceptance criteria met.

**Deliverables:**

- User-facing privacy policy (web-hosted or in-app)
- Privacy policy integrated in About screen
- Google Play `PACKAGE_USAGE_STATS` declaration form submitted
- Release keystore configured and secured
- Signed release AAB generated
- Play Store listing: title, short description, full description, screenshots (phone), feature graphic
- Content rating questionnaire completed
- App submitted to Google Play review
- Review feedback addressed (if any)
- Staged rollout executed: 20% → 50% → 100%

**Exit Criteria:** App is live on Google Play Store and available for download.

---

## 6. Milestone Schedule

### 6.1 Milestone Definitions

| Milestone | Name                        | Completion Criteria                                                      | Phase Gate |
| --------- | --------------------------- | ------------------------------------------------------------------------ | ---------- |
| **M0**    | Documentation Complete      | All design documents created and approved. All open questions resolved.   | End of P0  |
| **M1**    | Foundation Ready            | Project compiles, navigates, DI works, first test passes.                | End of P1  |
| **M2**    | Data Layer Complete         | All entities, DAOs, repos implemented and unit tested.                   | End of P2  |
| **M3**    | Engine Operational          | Service detects foreground app, tracks sessions, survives reboot.        | End of P3  |
| **M4**    | Overlay Functional          | Timer appears, drags, snaps, fades, auto-dismisses correctly.            | End of P4  |
| **M5**    | Escalation Complete         | All 5 visual states + throb work end-to-end with correct timing.         | End of P5  |
| **M6**    | UI Complete                 | All screens implemented, navigable, connected to data.                   | End of P6  |
| **M7**    | Onboarding Complete         | Full permission flow works on 3 OEMs.                                    | End of P7  |
| **M8**    | Integration & Polish Done   | E2E flow works. Errors handled. Performance targets met. Accessible.     | End of P8  |
| **M9**    | Testing Complete            | All tests pass. Zero critical/high bugs. OEM validation done.            | End of P9  |
| **M10**   | Released                    | App live on Google Play Store.                                           | End of P10 |

### 6.2 Critical Path

The critical path flows through the components that have the most sequential dependencies and the highest technical risk:

```
M0 → M1 → M2 → M3 → M4 → M5 → M8 → M9 → M10
         Documentation  Foundation  Data  Engine  Overlay  Escalation  Polish  Test  Release
```

**M6 (UI) and M7 (Onboarding)** can proceed partially in parallel with M4 and M5 since UI screens depend on the data layer (M2) and navigation (M1) but not on the overlay or escalation systems. However, M8 (Integration) cannot begin until all of M3–M7 are complete.

### 6.3 Parallel Work Opportunities

| Work Stream A                    | Work Stream B                     | Can Parallelize After |
| -------------------------------- | --------------------------------- | --------------------- |
| Phase 3 (Engine)                 | Phase 6 (UI Screens — partial)    | Phase 2 complete      |
| Phase 4 (Overlay)                | Phase 6 (UI Screens — remaining)  | Phase 3 complete      |
| Phase 5 (Escalation)            | Phase 7 (Onboarding)              | Phase 4 + Phase 6     |
| Phase 9 (OEM Testing)           | Phase 10 (Privacy Policy writing) | Phase 8 complete      |

---

## 7. Dependency Graph

### 7.1 Phase Dependencies

```
P0: Documentation
 │
 ├──► P1: Foundation
 │     │
 │     ├──► P2: Data Layer
 │     │     │
 │     │     ├──► P3: Engine ─────────────────────┐
 │     │     │     │                               │
 │     │     │     ├──► P4: Overlay                │
 │     │     │     │     │                         │
 │     │     │     │     └──► P5: Escalation       │
 │     │     │     │                               │
 │     │     ├──► P6: UI Screens ◄────────────────┘
 │     │     │     │                   (P3 needed for Home toggle)
 │     │     │     │
 │     │     │     └──► P7: Onboarding
 │     │     │
 │     │     └──────────────────────────────────────┐
 │     │                                            │
 │     └────────────────────────────────────────────┤
 │                                                  │
 │                                  P8: Integration & Polish
 │                                  (requires P3, P4, P5, P6, P7)
 │                                          │
 │                                          ▼
 │                                  P9: Testing & OEM Validation
 │                                          │
 │                                          ▼
 └─────────────────────────────►   P10: Release Preparation
                                   (P0.9 Privacy Doc needed for P10.1)
```

### 7.2 Critical External Dependencies

| Dependency                              | Required By    | Risk If Delayed                              |
| --------------------------------------- | -------------- | -------------------------------------------- |
| Test devices available (Pixel, Samsung, Xiaomi) | Phase 3+ | Cannot validate engine or overlay on real hardware |
| Google Play Developer account active     | Phase 10       | Cannot submit the app                        |
| Release keystore created and secured     | Phase 10       | Cannot produce signed release build          |
| Privacy policy hosted (URL)              | Phase 10       | Cannot submit Play Store listing             |

---

## 8. Resource Requirements

### 8.1 Hardware

| Resource                        | Purpose                                       | Required By  |
| ------------------------------- | --------------------------------------------- | ------------ |
| Google Pixel device (or emulator) | Primary development and testing target        | Phase 1      |
| Samsung device (One UI)          | OEM compatibility testing                     | Phase 3      |
| Xiaomi device (MIUI/HyperOS)    | OEM compatibility testing                     | Phase 3      |
| OnePlus device (OxygenOS)        | OEM compatibility testing (Should priority)   | Phase 9      |

### 8.2 Software & Accounts

| Resource                        | Purpose                                       | Required By  |
| ------------------------------- | --------------------------------------------- | ------------ |
| Android Studio (latest stable)  | IDE for development                           | Phase 1      |
| Google Play Developer Account   | App distribution                              | Phase 10     |
| Git repository (GitHub/GitLab)  | Version control                               | Phase 1      |
| Keystore (release signing)      | Signing the release AAB                       | Phase 10     |

### 8.3 Skills Required

| Skill                                    | Phases         |
| ---------------------------------------- | -------------- |
| Kotlin (advanced)                        | All            |
| Jetpack Compose (intermediate+)          | P1, P6, P7     |
| Android Services (foreground, lifecycle) | P3, P8         |
| WindowManager / System overlays          | P4             |
| UsageStatsManager API                    | P3             |
| Room (database, migrations)              | P2             |
| Android permissions model                | P7             |
| Animation (Android View + Property)      | P4, P5         |
| Android testing (JUnit, Espresso, Compose test) | P2–P9   |

---

## 9. Technology Stack & Tooling

### 9.1 Core Stack (from SRS PER-001)

| Component             | Technology                       | Version        |
| --------------------- | -------------------------------- | -------------- |
| Language              | Kotlin                           | 2.0.x          |
| Build system          | Gradle (Kotlin DSL)              | 8.13.x         |
| Android Gradle Plugin | AGP                              | 8.13.x         |
| UI framework          | Jetpack Compose                  | BOM 2024.09.00 |
| Design system         | Material Design 3                | Latest via BOM |
| Navigation            | Compose Navigation               | Latest stable  |
| Persistence           | Room                             | Latest stable  |
| DI                    | Hilt (recommended) or Koin       | Latest stable  |
| Lifecycle             | Lifecycle Runtime KTX            | Latest stable  |
| Min SDK               | 26                               | —              |
| Target / Compile SDK  | 36                               | —              |

### 9.2 Testing Stack

| Component             | Technology                       |
| --------------------- | -------------------------------- |
| Unit testing          | JUnit 4 + Kotlin coroutines test |
| Mocking               | MockK                            |
| Compose UI testing    | Compose Test (from BOM)          |
| Instrumented testing  | AndroidX Test + Espresso         |

### 9.3 Build & Quality Tools

| Tool                  | Purpose                          |
| --------------------- | -------------------------------- |
| Android Lint          | Static analysis                  |
| ktlint or detekt      | Kotlin code style enforcement    |
| R8 / ProGuard         | Code shrinking and obfuscation   |
| Git                   | Version control                  |

---

## 10. Risk Management Plan

### 10.1 Risk Register

Risks inherited from PRD Section 15.1 with project-level mitigations added.

| ID   | Risk                                                       | Prob.  | Impact   | Phase(s) Affected | Mitigation                                                                 | Contingency                                            |
| ---- | ---------------------------------------------------------- | ------ | -------- | ------------------ | -------------------------------------------------------------------------- | ------------------------------------------------------ |
| R-01 | Google Play rejects app due to `PACKAGE_USAGE_STATS` policy | Medium | Critical | P10                | Prepare thorough policy declaration in Phase 0. Write privacy policy early. | Appeal with detailed justification. Consider sideload distribution. |
| R-02 | OEM battery optimization kills foreground service           | High   | High     | P3, P9             | Implement START_STICKY, boot receiver, battery exemption request. Test on 3+ OEMs early (Phase 3). | Add user-facing guide for OEM-specific battery settings (link to dontkillmyapp.com). |
| R-03 | Users abandon during onboarding (complex permissions)       | Medium | High     | P7                 | Plain language, clear illustrations, skip option with warning. Test onboarding with 3 non-technical users. | Simplify flow; combine steps; add "Set up later" option. |
| R-04 | Overlay is perceived as annoying                            | Medium | High     | P4, P5             | Small size (48–56dp), subtle initial opacity (65%), smooth animations. Validate with user feedback before release. | Add user-configurable opacity. Add dismiss gesture (future). |
| R-05 | Battery drain complaints                                    | Medium | Medium   | P3, P8             | Adaptive polling (screen off = no polling). Profile in Phase 8. Target < 2%/hr. | Reduce polling frequency. Add battery-saver mode. |
| R-06 | Android OS updates break UsageStats or overlay APIs          | Low    | High     | P9+                | Monitor Android developer previews. Maintain compatibility test suite. | API-level conditional code. Immediate patch release. |
| R-07 | UsageStatsManager returns inconsistent data on some OEMs     | Medium | Medium   | P3                 | Implement fallback query method (queryUsageStats if queryEvents fails). Test on 3 OEMs in Phase 3. | Dual-query strategy with OEM-specific workarounds. |
| R-08 | Phase 3 (Engine) takes longer than expected due to OEM quirks | Medium | Medium  | P3, schedule       | Allocate buffer time. Start OEM testing early. Document OEM-specific findings. | Defer OnePlus/Oppo/Huawei testing to post-MVP. |
| R-09 | Release keystore lost or compromised                         | Low    | Critical | P10                | Create keystore early (Phase 1). Back up securely. Document recovery procedure. | Cannot recover; would require new listing. Prevent at all costs. |

### 10.2 Risk Review Schedule

Risks should be reviewed at every milestone gate (M0 through M10). Any new risks discovered during implementation should be added to this register.

---

## 11. Quality Assurance Strategy

### 11.1 Testing Levels

| Level                | Scope                                                        | Automated? | Phase    |
| -------------------- | ------------------------------------------------------------ | ---------- | -------- |
| **Unit Tests**       | Domain logic: state machines, threshold engine, cooldown logic | Yes        | P2–P5    |
| **Repository Tests** | Room DAO operations, data persistence                        | Yes        | P2       |
| **Integration Tests**| Service + Polling + Session + Database pipeline               | Partial    | P3, P8   |
| **UI Tests**         | Compose screen rendering, navigation                          | Yes        | P6       |
| **Manual Tests**     | Overlay interaction, drag, snap, animations                   | No         | P4, P5   |
| **OEM Tests**        | Full flow on Pixel, Samsung, Xiaomi, OnePlus                  | No         | P9       |
| **Endurance Tests**  | 8-hour continuous monitoring                                  | No         | P9       |
| **Performance Tests**| Battery, memory, FPS, startup time                            | Partial    | P8       |
| **Accessibility**    | Contrast, touch targets, TalkBack                             | Partial    | P8       |

### 11.2 Coverage Targets

| Module                    | Target Coverage | Achieved In |
| ------------------------- | --------------- | ----------- |
| Session State Machine     | 100%            | P3          |
| Threshold Engine          | 100%            | P5          |
| Cooldown Logic            | 100%            | P3          |
| Repositories              | 90%             | P2          |
| Overall Domain Layer      | ≥ 80%           | P9          |

### 11.3 Bug Severity Classification

| Severity     | Definition                                                           | Action              |
| ------------ | -------------------------------------------------------------------- | ------------------- |
| **Critical** | App crashes, data loss, security vulnerability, service non-functional | Fix immediately. Blocks release. |
| **High**     | Feature non-functional, major UX defect, overlay ghost, wrong timing  | Fix before release. |
| **Medium**   | Minor UX defect, edge case not handled gracefully, cosmetic issue     | Fix if time allows. |
| **Low**      | Cosmetic, enhancement, "nice to fix"                                  | Defer to v1.1.     |

### 11.4 Host App Compatibility Test Matrix

The overlay must be tested on the following apps (per PRD 16.2 #5):

| #  | App          | Category        |
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

**Test criteria per app:** Timer appears, is draggable, does not block content, does not interfere with scroll or tap gestures, snaps correctly, fades in/out correctly.

---

## 12. Definition of Done

### 12.1 Per-Task Definition of Done

A single task (WBS item) is "done" when:

1. The code implementing the task compiles without errors or warnings.
2. The code follows the project's Coding Standards (document 0.8).
3. All acceptance criteria from the corresponding SRS requirement are met.
4. Unit tests (where applicable) are written and passing.
5. The code does not introduce regressions in existing tests.
6. The code has been self-reviewed for obvious issues.

### 12.2 Per-Phase Definition of Done

A phase is "done" when:

1. All tasks in the phase are done per 12.1.
2. The phase's exit criteria (defined in Section 5) are met.
3. All deliverables for the phase are produced.
4. Any bugs discovered during the phase are either fixed or logged with severity.
5. The phase milestone (Section 6.1) is formally marked complete.

### 12.3 MVP Definition of Done

The MVP is "done" when all 8 criteria from PRD Section 16.2 are met:

1. All F-001 through F-015 features implemented and tested.
2. Google Play policy review passed.
3. Foreground service reliable for 8+ hours on Pixel, Samsung, Xiaomi.
4. Battery ≤ 2%/hr confirmed.
5. Overlay tested on 10 host apps without interference.
6. Onboarding achieves >80% permission grant rate in internal testing.
7. Zero critical or high-severity bugs.
8. Privacy policy published and accessible.

---

## 13. Communication & Reporting

### 13.1 Progress Tracking

Progress is tracked at the **milestone level**. At each milestone gate:

1. Review all exit criteria for the completing phase.
2. Update the milestone status (Pending → In Progress → Complete).
3. Record any deferred items or known issues.
4. Assess risks (Section 10) against current project state.
5. Decide whether to proceed to the next phase or address blockers.

### 13.2 Status Reporting Format

| Field                         | Content                                            |
| ----------------------------- | -------------------------------------------------- |
| **Current Phase**             | Phase number and name                              |
| **Current Milestone Target**  | Next milestone ID and name                         |
| **Tasks Completed This Period** | List of WBS IDs completed                        |
| **Tasks In Progress**         | List of WBS IDs currently underway                 |
| **Blockers**                  | Any issues preventing progress                     |
| **Risks Updated**             | Any new or changed risks                           |
| **Decisions Needed**          | Any open questions requiring resolution             |

### 13.3 Version Control Practices

| Practice                       | Convention                                         |
| ------------------------------ | -------------------------------------------------- |
| **Branching strategy**         | Feature branches off `develop`, merged via PR       |
| **Branch naming**              | `feature/wbs-X.X-short-description`                |
| **Commit messages**            | `[WBS X.X] Imperative description of change`       |
| **Main branches**              | `main` (release-ready), `develop` (integration)    |
| **Tagging**                    | `vX.Y.Z` on each release build                     |

---

## 14. Change Management

### 14.1 Change Request Process

Any change to scope, requirements, or architecture after Phase 0 completion must follow this process:

1. **Document the change:** Describe what is being changed and why.
2. **Impact assessment:** Identify which phases, tasks, documents, and timelines are affected.
3. **Approval:** Change must be approved by the project owner before implementation.
4. **Update documents:** Affected documents (PRD, SRS, this Project Plan) must be updated to reflect the change.
5. **Update revision history:** Document the change in the revision history of each affected document.

### 14.2 Scope Change Categories

| Category       | Examples                                                   | Approval Required |
| -------------- | ---------------------------------------------------------- | ----------------- |
| **Cosmetic**   | Color value change, text wording adjustment                | Verbal approval   |
| **Minor**      | Add a UI element, adjust animation timing, change a default | Written note      |
| **Significant**| Add a new feature, change a state machine, modify a data entity | Formal change request |
| **Major**      | Add a new phase, change the architecture, add new permissions | Full impact assessment + approval |

---

## 15. Open Questions & Decision Log

### 15.1 Unresolved (from PRD OQ-1 through OQ-7)

These must be resolved before the phase that depends on them.

| ID   | Question                                                          | Decision Needed By | Decided? | Decision |
| ---- | ----------------------------------------------------------------- | ------------------ | -------- | -------- |
| OQ-1 | Should cooldown duration be user-configurable or fixed?            | Before Phase 3     | Yes      | Fixed at 45 seconds for MVP (resolved in SRS FR-014.AC2) |
| OQ-2 | Timer shows MM:SS from session start or from overlay appearance?   | Before Phase 4     | Yes      | From session start (resolved in SRS FR-019.AC4) |
| OQ-3 | What exact color values for each threshold state?                  | Before Phase 5     | Partial  | Preliminary values in SRS FR-028; final values pending UI/UX Spec |
| OQ-4 | Should throb include haptic feedback?                              | Before Phase 5     | No       | — |
| OQ-5 | Include crash reporting SDK (Firebase Crashlytics)?                | Before Phase 1     | No       | — |
| OQ-6 | Monetization strategy?                                             | Before Phase 10    | No       | — |
| OQ-7 | Should service notification show session timer or static text?     | Before Phase 3     | No       | — |

### 15.2 Decisions Made During Planning

| Decision ID | Date       | Decision                                                          | Rationale                                      |
| ----------- | ---------- | ----------------------------------------------------------------- | ---------------------------------------------- |
| D-001       | 2026-02-05 | Cooldown duration fixed at 45 seconds for MVP                     | Simplicity; avoids UI complexity for MVP        |
| D-002       | 2026-02-05 | Timer shows elapsed time from session start (includes first 60s)  | Users should see total time, not time since timer appeared |
| D-003       | 2026-02-05 | Edge-snap defaults to right edge on tie                           | Deterministic; most users are right-handed      |
| D-004       | 2026-02-05 | Default threshold preset is "Moderate (10/20/30)"                 | Reasonable middle ground for first-time users   |
| D-005       | 2026-02-05 | Session data retained for 90 days                                 | Balances storage with useful history for future dashboard |

---

## 16. Document Roadmap

### 16.1 Planned Documentation

All documents will be stored in the `docs/` directory.

| ID   | Document                              | Filename               | Status      | Required Before | Created By |
| ---- | ------------------------------------- | ---------------------- | ----------- | --------------- | ---------- |
| D-01 | Product Requirements Document         | `PRD.md`               | Complete    | Phase 0 exit    | Done       |
| D-02 | Software Requirements Specification   | `SRS.md`               | Complete    | Phase 0 exit    | Done       |
| D-03 | Project Plan                          | `ProjectPlan.md`       | Complete    | Phase 0 exit    | This doc   |
| D-04 | System Architecture Document          | `SAD.md`               | Complete    | Phase 1 start   | Done       |
| D-05 | Technical Design Document             | `TDD.md`               | Pending     | Phase 3 start   | After D-04 |
| D-06 | UI/UX Design Specification            | `UISpec.md`            | Pending     | Phase 6 start   | After D-02 |
| D-07 | Database Schema Document              | `DBSchema.md`          | Pending     | Phase 2 start   | After D-02 |
| D-08 | Coding Standards & Conventions        | `CodingStandards.md`   | Pending     | Phase 1 start   | After D-04 |
| D-09 | Privacy & Data Handling Document      | `Privacy.md`           | Pending     | Phase 10 start  | After D-02 |
| D-10 | Risk Assessment (standalone)          | `RiskAssessment.md`    | Complete    | Phase 1 start   | Done       |
| D-11 | Test Plan & Strategy                  | `TestPlan.md`          | Pending     | Phase 9 start   | After D-02 |
| D-12 | Build & Release Plan                  | `ReleasePlan.md`       | Pending     | Phase 10 start  | After D-03 |

### 16.2 Recommended Document Creation Order

```
 PRD (Done) → SRS (Done) → Project Plan (Done)
     │
     ├──► SAD ──► TDD ──► Coding Standards
     │
     ├──► DB Schema
     │
     ├──► UI/UX Spec
     │
     ├──► Privacy Doc
     │
     ├──► Risk Assessment
     │
     └──► Test Plan ──► Release Plan
```

**Next document to create: Technical Design Document (TDD.md)**

---

## 17. Glossary

All terms from PRD Glossary (Section 17) and SRS Glossary (Section 14) apply. Additional project-level terms:

| Term                        | Definition                                                                                |
| --------------------------- | ----------------------------------------------------------------------------------------- |
| **WBS**                     | Work Breakdown Structure — a hierarchical decomposition of project work into tasks.        |
| **Phase Gate**              | A review point at the end of a phase where exit criteria are verified before proceeding.   |
| **Critical Path**           | The longest sequence of dependent tasks that determines the minimum project duration.       |
| **Definition of Done (DoD)**| A checklist of criteria that must be met before a task, phase, or release is considered complete. |
| **Milestone**               | A significant checkpoint in the project, marking the completion of a phase or deliverable.  |
| **Staged Rollout**          | Releasing the app to a progressively larger percentage of users on Google Play.             |
| **Endurance Test**          | A test verifying that the app functions correctly over an extended period (e.g., 8 hours). |
| **Regression Test**         | Re-running previously passing tests after changes to verify no existing functionality is broken. |

---

*End of Document*
