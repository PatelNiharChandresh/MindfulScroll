# System Architecture Document (SAD)

## MindfulScroll — Digital Awareness Companion

---

### Document Control

| Field              | Value                                           |
| ------------------ | ----------------------------------------------- |
| **Document Title** | MindfulScroll System Architecture Document       |
| **Version**        | 1.0                                             |
| **Status**         | Draft                                           |
| **Created**        | 2026-02-05                                      |
| **Last Updated**   | 2026-02-05                                      |
| **Author**         | Rudy                                            |
| **Reviewers**      | —                                               |
| **Approved By**    | —                                               |

### Revision History

| Version | Date       | Author | Changes       |
| ------- | ---------- | ------ | ------------- |
| 1.0     | 2026-02-05 | Rudy   | Initial draft |

### Parent Documents

| Document                              | Version | Relationship                                             |
| ------------------------------------- | ------- | -------------------------------------------------------- |
| [PRD.md](PRD.md)                      | 1.0     | Product definition — features, principles, constraints    |
| [SRS.md](SRS.md)                      | 1.0     | Technical requirements — FR, NFR, data model, interfaces  |
| [ProjectPlan.md](ProjectPlan.md)      | 1.0     | Execution plan — phases, technology stack, WBS            |
| [RiskAssessment.md](RiskAssessment.md)| 1.0     | Risk register — architectural risk mitigations            |

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Architectural Goals & Constraints](#2-architectural-goals--constraints)
3. [Architectural Patterns & Decisions](#3-architectural-patterns--decisions)
4. [System Context](#4-system-context)
5. [Layer Architecture](#5-layer-architecture)
6. [Package Structure](#6-package-structure)
7. [Component Architecture](#7-component-architecture)
8. [Data Architecture](#8-data-architecture)
9. [Service Architecture](#9-service-architecture)
10. [Overlay Architecture](#10-overlay-architecture)
11. [State Management Architecture](#11-state-management-architecture)
12. [Navigation Architecture](#12-navigation-architecture)
13. [Dependency Injection Architecture](#13-dependency-injection-architecture)
14. [Concurrency & Threading Model](#14-concurrency--threading-model)
15. [Security & Privacy Architecture](#15-security--privacy-architecture)
16. [Performance Architecture](#16-performance-architecture)
17. [Error Handling Architecture](#17-error-handling-architecture)
18. [Testability Architecture](#18-testability-architecture)
19. [Cross-Cutting Concerns](#19-cross-cutting-concerns)
20. [Future Extensibility](#20-future-extensibility)
21. [Architectural Decision Record](#21-architectural-decision-record)
22. [Traceability Matrix](#22-traceability-matrix)
23. [Glossary](#23-glossary)
24. [References](#24-references)

---

## 1. Introduction

### 1.1 Purpose

This System Architecture Document defines the high-level and component-level architecture of MindfulScroll MVP (v1.0). It translates the requirements defined in the [SRS](SRS.md) into a concrete software architecture that guides all implementation decisions. This document establishes the structural foundation — layers, components, interfaces, data flows, and cross-cutting concerns — on which the Technical Design Document (TDD) will build detailed algorithm and implementation designs.

### 1.2 Scope

This document covers the complete architecture of MindfulScroll MVP, including:

- Application layer structure (presentation, domain, data, service)
- All major components and their responsibilities
- Data persistence architecture
- Background service and monitoring engine design
- Floating overlay system design
- State management approach
- Navigation and UI architecture
- Dependency injection strategy
- Threading and concurrency model
- Security, privacy, performance, and error handling strategies

Post-MVP features (v1.1, v1.2+) are referenced only where they influence architectural decisions that must be made now for forward compatibility.

### 1.3 Intended Audience

| Audience              | Usage                                                          |
| --------------------- | -------------------------------------------------------------- |
| Android developers    | Primary implementation reference for all structural decisions   |
| Technical architects  | Architectural review and validation                             |
| QA / Test engineers   | Understanding component boundaries for test strategy            |
| Project managers      | Assessing technical scope and dependency clarity                 |

### 1.4 Key Architectural Drivers

The architecture of MindfulScroll is shaped by the following primary drivers, in priority order:

| Priority | Driver                    | Source                              | Architectural Implication                           |
| -------- | ------------------------- | ----------------------------------- | --------------------------------------------------- |
| 1        | Battery efficiency        | PRD P5, SRS NFR-002                 | Adaptive polling, minimal wake-locks, efficient rendering |
| 2        | Service reliability       | PRD TG-4, SRS NFR-006, RA-002      | Robust foreground service with self-recovery         |
| 3        | Overlay non-interference  | PRD P3, SRS FR-019, RA-010         | Precise touch handling, minimal footprint            |
| 4        | Data privacy              | PRD P4, SRS NFR-011                 | Zero network, local-only storage, no analytics       |
| 5        | Testability               | SRS NFR-014, ProjectPlan QA Strategy| Clean Architecture, dependency injection, pure domain |
| 6        | OEM compatibility         | SRS NFR-016, RA-002, RA-007        | Defensive coding, fallback strategies, OEM detection |

---

## 2. Architectural Goals & Constraints

### 2.1 Architectural Goals

| ID    | Goal                                         | Traces To              | Verification Method                           |
| ----- | -------------------------------------------- | ---------------------- | --------------------------------------------- |
| AG-01 | Clear layer separation with unidirectional dependency flow | SRS NFR-013 | Domain layer has zero Android imports          |
| AG-02 | All business logic is unit-testable without Android framework | SRS NFR-014 | ≥80% domain layer unit test coverage     |
| AG-03 | Background monitoring survives activity lifecycle | SRS FR-035 | Service runs independently of any Activity      |
| AG-04 | Overlay system operates independently of Compose UI | SRS EIR-002 | Overlay uses View system via WindowManager   |
| AG-05 | All data access is repository-abstracted     | SRS NFR-013.AC3        | No DAO calls outside repository implementations |
| AG-06 | State changes flow through observable streams | —                    | ViewModels expose StateFlow; Service uses Flow   |
| AG-07 | Forward-compatible schema and component design | RA-018               | v1.1 features require no architectural rework    |
| AG-08 | Minimal dependency footprint                 | SRS NFR-011, PRD C-12  | No network libraries; minimal third-party deps   |

### 2.2 Architectural Constraints

Inherited from PRD (C-01–C-13), SRS (Section 2.4), and Risk Assessment.

| ID    | Constraint                                            | Source         | Architectural Impact                               |
| ----- | ----------------------------------------------------- | -------------- | -------------------------------------------------- |
| AC-01 | No network calls of any kind                          | PRD C-12, SRS NFR-011 | No HTTP clients, analytics, or crash reporting SDKs |
| AC-02 | No `INTERNET` permission in manifest                  | SRS NFR-011.AC1 | Compile-time enforcement via lint                  |
| AC-03 | All persistence via Room on private internal storage   | PRD C-11, SRS NFR-012 | Single Room database, no external storage          |
| AC-04 | Overlay must use `TYPE_APPLICATION_OVERLAY` (API 26+)  | SRS EIR-002    | View-based overlay, not Compose                    |
| AC-05 | Overlay must not intercept host app touches            | PRD C-05, C-09 | `FLAG_NOT_FOCUSABLE`, touch pass-through           |
| AC-06 | Foreground service is mandatory for monitoring         | PRD C-13, SRS FR-035 | Service architecture with persistent notification  |
| AC-07 | Single-activity architecture with Compose Navigation   | SRS FR-041     | One Activity, NavHost for all screens              |
| AC-08 | Min SDK 26, Target/Compile SDK 36                      | SRS PER-001    | API-level conditional code where needed            |
| AC-09 | `foregroundServiceType="specialUse"` for API 34+       | SRS FR-035.AC6 | Manifest conditional or single declaration         |
| AC-10 | No Google Play Services dependency                     | SRS PER-002    | GMS-independent; no Firebase, no GMS APIs          |

---

## 3. Architectural Patterns & Decisions

### 3.1 Primary Patterns

#### 3.1.1 Clean Architecture

MindfulScroll follows **Clean Architecture** with three concentric layers:

```
┌───────────────────────────────────────────────┐
│             PRESENTATION LAYER                 │
│   (Compose UI, ViewModels, Navigation)         │
│                                                │
│   Depends on: Domain Layer                     │
│   Android-dependent: Yes                       │
├───────────────────────────────────────────────┤
│               DOMAIN LAYER                     │
│   (Use Cases, Models, Repository Interfaces,   │
│    State Machines, Business Logic)              │
│                                                │
│   Depends on: Nothing (pure Kotlin)            │
│   Android-dependent: NO                        │
├───────────────────────────────────────────────┤
│                DATA LAYER                      │
│   (Room DB, DAOs, Repository Implementations,  │
│    System API Wrappers)                        │
│                                                │
│   Depends on: Domain Layer (interfaces)        │
│   Android-dependent: Yes                       │
└───────────────────────────────────────────────┘
```

**Dependency Rule:** Dependencies point inward only. The domain layer has zero dependencies on the presentation or data layers. The presentation layer never directly accesses the data layer.

**Rationale:** Enables unit testing of all business logic without Android instrumentation (AG-02). Isolates platform-dependent code to the outer layers where it can be substituted or mocked. Satisfies SRS NFR-013.

#### 3.1.2 MVVM (Model-View-ViewModel)

The presentation layer follows the **MVVM** pattern:

```
┌──────────┐     observes     ┌──────────────┐     calls     ┌──────────┐
│  Compose  │ ◄───────────── │  ViewModel    │ ──────────► │ Use Case │
│   Screen  │   StateFlow    │              │              │          │
│  (View)   │ ──────────────►│              │              │          │
│           │   user events  │              │              │          │
└──────────┘                 └──────────────┘              └──────────┘
```

- **View (Compose):** Stateless composable functions that render UI state and emit user events.
- **ViewModel:** Holds UI state as `StateFlow`, processes user events by calling use cases, survives configuration changes.
- **Model:** Domain models and UI state data classes.

**Rationale:** Industry-standard pattern for Android. StateFlow provides lifecycle-aware state observation. ViewModels survive rotation without data loss.

#### 3.1.3 Single-Activity Architecture

The entire app runs in a single `MainActivity`. All screens are Compose destinations managed by Jetpack Navigation.

**Rationale:** Simplifies navigation, reduces the Activity lifecycle surface area, and is the recommended approach for Jetpack Compose apps (SRS FR-041).

### 3.2 Supplementary Patterns

| Pattern | Applied To | Purpose |
| ------- | ---------- | ------- |
| **Repository** | All data access | Abstracts data sources behind domain-layer interfaces (SRS NFR-013.AC3) |
| **State Machine** | Session lifecycle, Timer visual state, Service state | Formal, testable state management (SRS Section 10) |
| **Observer** | UI ↔ ViewModel, Service ↔ Repository | Reactive data flow via Kotlin Flow/StateFlow |
| **Strategy** | UsageStats query (primary/fallback) | OEM-adaptive query selection (RA-007) |
| **Singleton** | Room database, DI-provided services | Single instances managed by DI container |

---

## 4. System Context

### 4.1 System Context Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                          ANDROID DEVICE                              │
│                                                                      │
│  ┌──────────────┐    queries    ┌───────────────────────────────┐   │
│  │ UsageStats   │ ◄──────────── │                               │   │
│  │ Manager      │               │                               │   │
│  └──────────────┘               │                               │   │
│                                 │       MINDFULSCROLL APP        │   │
│  ┌──────────────┐    queries    │                               │   │
│  │ Package      │ ◄──────────── │   ┌─────────┐  ┌──────────┐  │   │
│  │ Manager      │               │   │ Single  │  │Monitoring│  │   │
│  └──────────────┘               │   │Activity │  │ Service  │  │   │
│                                 │   │(Compose)│  │(FG Svc)  │  │   │
│  ┌──────────────┐   add/remove  │   └─────────┘  └────┬─────┘  │   │
│  │ Window       │ ◄──────────── │                     │        │   │
│  │ Manager      │               │              ┌──────┴──────┐ │   │
│  └──────────────┘               │              │  Overlay    │ │   │
│                                 │              │  (View)     │ │   │
│  ┌──────────────┐   read/write  │              └─────────────┘ │   │
│  │ Room DB      │ ◄──────────── │                               │   │
│  │ (SQLite)     │               │                               │   │
│  └──────────────┘               └───────────────────────────────┘   │
│                                                                      │
│  ┌──────────────┐     boot      ┌───────────────────────────────┐   │
│  │ Android OS   │ ──────────── │     BootReceiver               │   │
│  │ (System)     │   broadcast   │     (starts service)          │   │
│  └──────────────┘               └───────────────────────────────┘   │
│                                                                      │
│  ┌──────────────┐   screen      ┌───────────────────────────────┐   │
│  │ Screen State │ ──────────── │   ScreenStateReceiver          │   │
│  │ (on/off)     │  broadcast    │   (pauses/resumes polling)    │   │
│  └──────────────┘               └───────────────────────────────┘   │
│                                                                      │
│  ═══════════════════════════════════════════════════════════════════  │
│  NO NETWORK CONNECTION — FULLY OFFLINE                               │
│  ═══════════════════════════════════════════════════════════════════  │
└─────────────────────────────────────────────────────────────────────┘
```

### 4.2 External System Interfaces

| Interface | Android API | Direction | Purpose | SRS Trace |
| --------- | ----------- | --------- | ------- | --------- |
| UsageStatsManager | `android.app.usage.UsageStatsManager` | Read | Query foreground app | EIR-001 |
| WindowManager | `android.view.WindowManager` | Read/Write | Add/update/remove overlay | EIR-002 |
| PackageManager | `android.content.pm.PackageManager` | Read | Discover installed apps | EIR-003 |
| NotificationManager | `android.app.NotificationManager` | Write | Foreground service notification | FR-036 |
| AlarmManager | `android.app.AlarmManager` | Write | Backup service restart (contingency) | RA-002 |

### 4.3 System Boundaries

| Boundary | Inside | Outside |
| -------- | ------ | ------- |
| **Network** | All data, logic, storage | No network communication whatsoever |
| **Storage** | App private internal storage only | No external storage, no shared directories |
| **IPC** | Boot broadcast receiver only | No exported Activities, ContentProviders, or Services |
| **Permissions** | Declared permissions in manifest | No undeclared permission usage |

---

## 5. Layer Architecture

### 5.1 Layer Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                     PRESENTATION LAYER                           │
│                                                                  │
│  ┌─────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌───────┐ │
│  │Onboard- │ │   App    │ │Threshold │ │  Home    │ │ About │ │
│  │  ing    │ │Selection │ │  Config  │ │ Screen   │ │Screen │ │
│  │ Screens │ │  Screen  │ │  Screen  │ │          │ │       │ │
│  └────┬────┘ └────┬─────┘ └────┬─────┘ └────┬─────┘ └───┬───┘ │
│       │           │            │             │           │      │
│  ┌────▼───────────▼────────────▼─────────────▼───────────▼───┐  │
│  │                      ViewModels                            │  │
│  │  OnboardingVM │ AppSelectionVM │ ThresholdVM │ HomeVM      │  │
│  └───────────────────────────┬────────────────────────────────┘  │
│                              │ calls                             │
├──────────────────────────────┼───────────────────────────────────┤
│                     DOMAIN LAYER (Pure Kotlin)                   │
│                              │                                   │
│  ┌───────────────────────────▼────────────────────────────────┐  │
│  │                      Use Cases                              │  │
│  │  GetInstalledApps │ UpdateAppSelection │ GetThresholds      │  │
│  │  SaveThresholds   │ GetMonitoringState │ ToggleMonitoring   │  │
│  │  CheckPermissions │ GetUserPreferences │ SaveUserPreference │  │
│  └───────────────────────────┬────────────────────────────────┘  │
│                              │                                   │
│  ┌───────────────────────────▼────────────────────────────────┐  │
│  │                   Domain Models                             │  │
│  │  MonitoredApp │ ThresholdConfig │ UsageSession              │  │
│  │  SessionState │ TimerVisualState │ ServiceState             │  │
│  └────────────────────────────────────────────────────────────┘  │
│                                                                  │
│  ┌────────────────────────────────────────────────────────────┐  │
│  │              Repository Interfaces                          │  │
│  │  MonitoredAppRepository │ ThresholdConfigRepository         │  │
│  │  UsageSessionRepository │ UserPreferencesRepository         │  │
│  │  InstalledAppRepository │ UsageStatsRepository              │  │
│  └───────────────────────────┬────────────────────────────────┘  │
│                              │ implements                        │
├──────────────────────────────┼───────────────────────────────────┤
│                       DATA LAYER                                 │
│                              │                                   │
│  ┌───────────────────────────▼────────────────────────────────┐  │
│  │              Repository Implementations                     │  │
│  │  MonitoredAppRepositoryImpl │ ThresholdConfigRepositoryImpl │  │
│  │  UsageSessionRepositoryImpl │ UserPreferencesRepositoryImpl │  │
│  │  InstalledAppRepositoryImpl │ UsageStatsRepositoryImpl      │  │
│  └────────────┬───────────────────────────────┬───────────────┘  │
│               │                               │                  │
│  ┌────────────▼──────────┐       ┌────────────▼──────────────┐  │
│  │      Room Database    │       │   System API Wrappers      │  │
│  │  ┌─────────────────┐  │       │  ┌─────────────────────┐  │  │
│  │  │ MonitoredAppDao │  │       │  │ PackageManagerWrapper│  │  │
│  │  │ ThresholdDao    │  │       │  │ UsageStatsWrapper    │  │  │
│  │  │ SessionDao      │  │       │  │ PermissionChecker    │  │  │
│  │  │ PreferencesDao  │  │       │  └─────────────────────┘  │  │
│  │  └─────────────────┘  │       └───────────────────────────┘  │
│  └───────────────────────┘                                       │
├──────────────────────────────────────────────────────────────────┤
│                      SERVICE LAYER                                │
│                                                                  │
│  ┌──────────────────────────────────────────────────────────┐    │
│  │              MonitoringService (Foreground)                │    │
│  │  ┌──────────────┐  ┌────────────┐  ┌──────────────────┐  │    │
│  │  │ Polling      │  │  Session   │  │   Overlay        │  │    │
│  │  │ Engine       │  │  Manager   │  │   Controller     │  │    │
│  │  └──────────────┘  └────────────┘  └──────────────────┘  │    │
│  │  ┌──────────────┐  ┌────────────────────────────────────┐ │    │
│  │  │ Threshold    │  │  Notification Manager              │ │    │
│  │  │ Engine       │  │                                    │ │    │
│  │  └──────────────┘  └────────────────────────────────────┘ │    │
│  └──────────────────────────────────────────────────────────┘    │
│  ┌────────────────────┐  ┌────────────────────────┐              │
│  │   BootReceiver     │  │  ScreenStateReceiver   │              │
│  └────────────────────┘  └────────────────────────┘              │
└──────────────────────────────────────────────────────────────────┘
```

### 5.2 Layer Responsibilities

| Layer | Responsibility | Dependencies | Android Framework? |
| ----- | -------------- | ------------ | ------------------ |
| **Presentation** | Render UI, handle user input, manage navigation, observe state | Domain | Yes (Compose, Lifecycle) |
| **Domain** | Business logic, state machines, use case orchestration, model definitions, repository contracts | None | **No** (pure Kotlin) |
| **Data** | Implement repository contracts, manage Room DB, wrap system APIs | Domain (interfaces) | Yes (Room, PackageManager, UsageStats) |
| **Service** | Run foreground service, orchestrate polling/session/overlay lifecycle | Domain, Data | Yes (Service, WindowManager, BroadcastReceiver) |

### 5.3 Layer Communication Rules

| Rule | Description |
| ---- | ----------- |
| **R1** | Presentation → Domain: via use case function calls |
| **R2** | Domain → Data: via repository interfaces defined in domain, implemented in data |
| **R3** | Service → Domain: via use case calls and domain model types |
| **R4** | Service → Data: via repository interfaces (same as R2) |
| **R5** | Data → Domain: NEVER (data implements domain interfaces but does not call domain logic) |
| **R6** | Presentation → Data: NEVER (no direct DAO or repository-impl access) |
| **R7** | Presentation → Service: via Android Intent/binding mechanisms only |
| **R8** | All reactive data flows use Kotlin Flow (cold) or StateFlow (hot) |

---

## 6. Package Structure

### 6.1 Top-Level Package

```
com.rudy.mindfulscroll/
├── di/                          # Dependency injection modules
├── domain/                      # Domain layer (pure Kotlin)
│   ├── model/                   #   Domain models and enums
│   ├── repository/              #   Repository interfaces
│   └── usecase/                 #   Use case classes
├── data/                        # Data layer
│   ├── local/                   #   Room database components
│   │   ├── dao/                 #     DAO interfaces
│   │   ├── entity/              #     Room entity classes
│   │   └── converter/           #     Type converters
│   ├── repository/              #   Repository implementations
│   └── system/                  #   System API wrappers
├── presentation/                # Presentation layer
│   ├── navigation/              #   NavHost, routes, navigation logic
│   ├── theme/                   #   Compose theme (colors, typography, shapes)
│   ├── common/                  #   Shared composables and UI utilities
│   ├── onboarding/              #   Onboarding screens + ViewModel
│   ├── appselection/            #   App Selection screen + ViewModel
│   ├── threshold/               #   Threshold Configuration screen + ViewModel
│   ├── home/                    #   Home screen + ViewModel
│   └── about/                   #   About screen
├── service/                     # Service layer
│   ├── monitoring/              #   MonitoringService, PollingEngine, SessionManager
│   ├── overlay/                 #   OverlayController, OverlayView, animations
│   ├── threshold/               #   ThresholdEngine
│   └── receiver/                #   BootReceiver, ScreenStateReceiver
├── util/                        # Shared utilities
│   ├── extensions/              #   Kotlin extension functions
│   └── constants/               #   App-wide constants
└── MindfulScrollApp.kt          # Application class (DI entry point)
```

### 6.2 Package Dependency Rules

```
di/ ──────────► ALL packages (provides dependencies)

presentation/ ─► domain/ (use cases, models)
              ─► di/     (injection)
              ✗  data/   (NEVER)
              ✗  service/(NEVER directly; communicates via Intent/binding)

domain/ ──────► nothing  (ZERO external dependencies)

data/ ────────► domain/  (implements repository interfaces)
              ─► di/     (injection)
              ✗  presentation/ (NEVER)
              ✗  service/      (NEVER)

service/ ─────► domain/  (use cases, models, repository interfaces)
              ─► data/   (via DI — repository implementations injected)
              ─► di/     (injection)
              ✗  presentation/ (NEVER)
```

### 6.3 Domain Layer Package Detail

The domain layer is the most critical package to keep pure. It must have **zero** Android framework imports.

```
domain/
├── model/
│   ├── MonitoredApp.kt              # Data class: packageName, displayName, isActive, addedAt
│   ├── ThresholdConfig.kt           # Data class: preset, t1, t2, t3, isGlobal, appPackage
│   ├── UsageSession.kt              # Data class: id, app, start, end, duration, maxThreshold, throb
│   ├── InstalledApp.kt              # Data class: packageName, displayName, icon (Drawable abstracted)
│   ├── SessionState.kt              # Enum: INACTIVE, ACTIVE, COOLDOWN, ENDED
│   ├── TimerVisualState.kt          # Enum: CALM, NOTICE, ALERT, URGENT, THROB
│   ├── ServiceState.kt              # Enum: STOPPED, RUNNING
│   └── PermissionStatus.kt          # Data class: usageAccess, overlay, batteryOpt, notifications
│
├── repository/
│   ├── MonitoredAppRepository.kt    # Interface: getAll, getActive, setActive, removeUninstalled
│   ├── ThresholdConfigRepository.kt # Interface: getGlobal, save, getForApp
│   ├── UsageSessionRepository.kt    # Interface: insert, getRecent, deleteOlderThan
│   ├── UserPreferencesRepository.kt # Interface: get, set, getMonitoringActive, getOverlayPosition
│   ├── InstalledAppRepository.kt    # Interface: getInstalledApps
│   └── UsageStatsRepository.kt      # Interface: getCurrentForegroundApp
│
└── usecase/
    ├── GetInstalledAppsUseCase.kt   # Queries installed apps, excludes self
    ├── UpdateAppSelectionUseCase.kt  # Toggles monitored state for an app
    ├── GetMonitoredAppsUseCase.kt   # Returns currently monitored app list
    ├── GetThresholdConfigUseCase.kt # Returns current threshold configuration
    ├── SaveThresholdConfigUseCase.kt# Validates and saves threshold config
    ├── ToggleMonitoringUseCase.kt   # Start/stop monitoring service
    ├── CheckPermissionsUseCase.kt   # Checks all required permission states
    ├── GetUserPreferenceUseCase.kt  # Reads a user preference
    ├── SaveUserPreferenceUseCase.kt # Writes a user preference
    └── CleanupOldSessionsUseCase.kt # Deletes sessions older than 90 days
```

---

## 7. Component Architecture

### 7.1 Component Overview

```
┌──────────────────────────────────────────────────────────────────────┐
│                          MINDFULSCROLL                                │
│                                                                      │
│  ┌──────────────────────────────────────────┐                        │
│  │              MainActivity                 │                        │
│  │  ┌──────────────────────────────────┐    │                        │
│  │  │           NavHost                 │    │                        │
│  │  │  ┌──────┐ ┌─────┐ ┌──────────┐  │    │                        │
│  │  │  │Onbrd │ │Home │ │AppSelect │  │    │                        │
│  │  │  └──────┘ └─────┘ └──────────┘  │    │                        │
│  │  │  ┌──────────┐ ┌───────┐         │    │                        │
│  │  │  │Threshold │ │About  │         │    │                        │
│  │  │  └──────────┘ └───────┘         │    │                        │
│  │  └──────────────────────────────────┘    │                        │
│  └──────────────────────────────────────────┘                        │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐    │
│  │                   MonitoringService                           │    │
│  │                                                               │    │
│  │  ┌──────────────┐     ┌────────────────┐                     │    │
│  │  │PollingEngine │────►│ SessionManager │                     │    │
│  │  │              │     │                │                     │    │
│  │  │ queries      │     │  state machine │                     │    │
│  │  │ UsageStats   │     │  cooldown mgmt │                     │    │
│  │  │ every 2 sec  │     │  session CRUD  │                     │    │
│  │  └──────────────┘     └───────┬────────┘                     │    │
│  │                               │                               │    │
│  │                    ┌──────────┼──────────┐                    │    │
│  │                    │          │          │                    │    │
│  │              ┌─────▼───┐ ┌───▼─────┐ ┌──▼────────────┐      │    │
│  │              │Threshold│ │ Overlay │ │ Notification   │      │    │
│  │              │ Engine  │ │Controller│ │ Manager       │      │    │
│  │              │         │ │         │ │               │      │    │
│  │              │evaluates│ │manages  │ │updates svc    │      │    │
│  │              │visual   │ │WindowMgr│ │notification   │      │    │
│  │              │states   │ │overlay  │ │               │      │    │
│  │              └─────────┘ └─────────┘ └───────────────┘      │    │
│  └──────────────────────────────────────────────────────────────┘    │
│                                                                      │
│  ┌──────────────────┐  ┌──────────────────────┐                      │
│  │  BootReceiver    │  │ ScreenStateReceiver  │                      │
│  │  (BOOT_COMPLETED)│  │ (SCREEN_ON/OFF)      │                      │
│  └──────────────────┘  └──────────────────────┘                      │
│                                                                      │
│  ┌──────────────────────────────────────────────────────────────┐    │
│  │                     Room Database                             │    │
│  │  MonitoredApp │ ThresholdConfig │ UsageSession │ Preferences  │    │
│  └──────────────────────────────────────────────────────────────┘    │
└──────────────────────────────────────────────────────────────────────┘
```

### 7.2 Component Responsibilities

#### 7.2.1 MainActivity

| Aspect | Detail |
| ------ | ------ |
| **Responsibility** | Hosts the Compose NavHost. Entry point for the application UI. |
| **Lifecycle** | Standard Activity lifecycle. Service runs independently. |
| **Inputs** | User interactions (via Compose), notification tap intent |
| **Outputs** | Navigation events, service start/stop intents |
| **SRS Trace** | FR-041 |

#### 7.2.2 MonitoringService

| Aspect | Detail |
| ------ | ------ |
| **Responsibility** | Foreground service hosting the monitoring engine. Orchestrates polling, session management, overlay display, and threshold evaluation. |
| **Type** | `Service` with `startForeground()`, returns `START_STICKY` |
| **Lifecycle** | Starts on user action or boot. Runs until explicitly stopped or force-killed. Self-recovers via `START_STICKY`. |
| **Owned Components** | PollingEngine, SessionManager, ThresholdEngine, OverlayController |
| **Inputs** | Start/stop intents, screen state broadcasts, UsageStats data |
| **Outputs** | Overlay view changes, session records to database, notification updates |
| **SRS Trace** | FR-035, FR-036, FR-037 |
| **Risk Trace** | RA-002, RA-013, RA-022 |

#### 7.2.3 PollingEngine

| Aspect | Detail |
| ------ | ------ |
| **Responsibility** | Periodically queries UsageStatsManager to determine the current foreground app. Implements adaptive polling (pause when screen off). |
| **Polling Interval** | 2 seconds (configurable internally) |
| **Query Strategy** | Primary: `queryEvents()` with `MOVE_TO_FOREGROUND`. Fallback: `queryUsageStats()` with `lastTimeUsed`. |
| **Thread** | Runs on a dedicated coroutine in the service's `CoroutineScope` |
| **Inputs** | Screen state (on/off), polling interval configuration |
| **Outputs** | Foreground app package name (emitted via Flow) |
| **SRS Trace** | FR-009, FR-010, FR-011, EIR-001 |
| **Risk Trace** | RA-005, RA-007 |

#### 7.2.4 SessionManager

| Aspect | Detail |
| ------ | ------ |
| **Responsibility** | Implements the session state machine (INACTIVE → ACTIVE → COOLDOWN → ENDED). Manages cooldown timers, session elapsed time, and session persistence. |
| **State Machine** | As defined in SRS Section 10.1 |
| **Timing** | Uses `SystemClock.elapsedRealtime()` for all duration calculations (immune to clock changes) |
| **Inputs** | Foreground app events from PollingEngine, screen state events |
| **Outputs** | Session state changes (emitted via StateFlow), session records to repository |
| **SRS Trace** | FR-012 – FR-017 |
| **Risk Trace** | RA-012, RA-013 |

#### 7.2.5 ThresholdEngine

| Aspect | Detail |
| ------ | ------ |
| **Responsibility** | Evaluates the current session elapsed time against the configured thresholds and determines the current timer visual state (Calm → Notice → Alert → Urgent → Throb). |
| **Domain Logic** | Pure function: `evaluate(elapsedSeconds: Int, config: ThresholdConfig) -> TimerVisualState` |
| **Inputs** | Elapsed session time, threshold configuration |
| **Outputs** | Current `TimerVisualState` |
| **SRS Trace** | FR-028, FR-030 |

#### 7.2.6 OverlayController

| Aspect | Detail |
| ------ | ------ |
| **Responsibility** | Manages the floating timer overlay's lifecycle: creation, display, position, animations, visual state updates, and removal via WindowManager. |
| **View System** | Uses Android View (not Compose) — `TYPE_APPLICATION_OVERLAY` requires View-based rendering |
| **Inputs** | Session state changes, timer visual state changes, elapsed time ticks |
| **Outputs** | Overlay view on screen, position persistence via repository |
| **SRS Trace** | FR-018 – FR-024, FR-028 – FR-031, EIR-002 |
| **Risk Trace** | RA-004, RA-010, RA-011, RA-014 |

#### 7.2.7 BootReceiver

| Aspect | Detail |
| ------ | ------ |
| **Responsibility** | Receives `BOOT_COMPLETED` broadcast. Checks the persisted monitoring-active flag. If true, starts the MonitoringService. |
| **SRS Trace** | FR-038 |

#### 7.2.8 ScreenStateReceiver

| Aspect | Detail |
| ------ | ------ |
| **Responsibility** | Receives `ACTION_SCREEN_ON` and `ACTION_SCREEN_OFF` broadcasts. Notifies the PollingEngine and SessionManager to adapt behavior. |
| **Registration** | Registered dynamically in MonitoringService (not in manifest) |
| **SRS Trace** | FR-010, FR-016 |

---

## 8. Data Architecture

### 8.1 Database Overview

| Parameter | Value |
| --------- | ----- |
| Database Library | Room (AndroidX) |
| Database Name | `mindfulscroll.db` |
| Schema Version | 1 (MVP) |
| Location | App private internal storage |
| WAL Mode | Enabled (SRS NFR-008.AC1) |
| Export Schema | Yes (JSON, for migration tracking — RA-018 mitigation) |

### 8.2 Entity-Relationship Diagram

```
┌────────────────────────┐
│     MonitoredApp       │
├────────────────────────┤
│ PK packageName: String │
│    displayName: String │
│    isActive: Boolean   │
│    addedAt: Long       │
└───────────┬────────────┘
            │ 1
            │
            │ 0..* (v1.1: per-app thresholds)
            │
┌───────────▼────────────┐       ┌─────────────────────────┐
│    ThresholdConfig     │       │     UserPreferences      │
├────────────────────────┤       ├─────────────────────────┤
│ PK id: Int (auto)      │       │ PK key: String           │
│    presetName: String? │       │    value: String         │
│    threshold1Min: Int  │       └─────────────────────────┘
│    threshold2Min: Int  │
│    threshold3Min: Int  │
│    isGlobal: Boolean   │
│ FK appPackageName: Str?│──── (nullable FK to MonitoredApp, for v1.1)
└────────────────────────┘

┌──────────────────────────┐
│      UsageSession        │
├──────────────────────────┤
│ PK id: Long (auto)       │
│ FK appPackageName: String│──── (references MonitoredApp.packageName)
│    startTimestamp: Long  │
│    endTimestamp: Long    │
│    durationSeconds: Int  │
│    maxThresholdReached: Int│
│    throbActivated: Boolean│
└──────────────────────────┘
```

### 8.3 Entity Specifications

#### 8.3.1 MonitoredApp (DR-001)

```kotlin
@Entity(tableName = "monitored_apps")
data class MonitoredAppEntity(
    @PrimaryKey
    val packageName: String,
    val displayName: String,
    val isActive: Boolean = true,
    val addedAt: Long       // epoch millis
)
```

#### 8.3.2 ThresholdConfig (DR-002)

```kotlin
@Entity(tableName = "threshold_configs")
data class ThresholdConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val presetName: String?,        // "Light", "Moderate", or null (custom)
    val threshold1Min: Int,         // ≥ 1
    val threshold2Min: Int,         // > threshold1Min
    val threshold3Min: Int,         // > threshold2Min, ≤ 120
    val isGlobal: Boolean = true,
    val appPackageName: String? = null  // FK for v1.1 per-app override
)
```

#### 8.3.3 UsageSession (DR-003)

```kotlin
@Entity(
    tableName = "usage_sessions",
    indices = [Index("appPackageName"), Index("startTimestamp")]
)
data class UsageSessionEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val appPackageName: String,
    val startTimestamp: Long,       // epoch millis
    val endTimestamp: Long,         // epoch millis
    val durationSeconds: Int,       // active time only (excludes cooldown)
    val maxThresholdReached: Int,   // 0=below timer, 1=Calm, 2=Notice, 3=Alert, 4=Urgent/Throb
    val throbActivated: Boolean = false
)
```

#### 8.3.4 UserPreferences (DR-004)

```kotlin
@Entity(tableName = "user_preferences")
data class UserPreferenceEntity(
    @PrimaryKey
    val key: String,
    val value: String
)
```

**Required Keys (MVP):**

| Key | Type | Default | Purpose |
| --- | ---- | ------- | ------- |
| `monitoring_active` | Boolean | false | Whether service should run |
| `onboarding_completed` | Boolean | false | First-launch gate |
| `overlay_position_edge` | String | "right" | Last snapped edge |
| `overlay_position_y_percent` | Float | 0.5 | Last Y position (fraction) |
| `cooldown_duration_seconds` | Int | 45 | Cooldown window duration |

### 8.4 DAO Interfaces

```kotlin
@Dao
interface MonitoredAppDao {
    @Query("SELECT * FROM monitored_apps ORDER BY displayName COLLATE NOCASE")
    fun getAll(): Flow<List<MonitoredAppEntity>>

    @Query("SELECT * FROM monitored_apps WHERE isActive = 1")
    fun getActive(): Flow<List<MonitoredAppEntity>>

    @Query("SELECT packageName FROM monitored_apps WHERE isActive = 1")
    suspend fun getActivePackageNames(): List<String>

    @Upsert
    suspend fun upsert(app: MonitoredAppEntity)

    @Query("UPDATE monitored_apps SET isActive = :isActive WHERE packageName = :packageName")
    suspend fun setActive(packageName: String, isActive: Boolean)

    @Query("DELETE FROM monitored_apps WHERE packageName NOT IN (:installedPackages)")
    suspend fun removeUninstalled(installedPackages: List<String>)
}

@Dao
interface ThresholdConfigDao {
    @Query("SELECT * FROM threshold_configs WHERE isGlobal = 1 LIMIT 1")
    suspend fun getGlobal(): ThresholdConfigEntity?

    @Query("SELECT * FROM threshold_configs WHERE isGlobal = 1 LIMIT 1")
    fun observeGlobal(): Flow<ThresholdConfigEntity?>

    @Upsert
    suspend fun upsert(config: ThresholdConfigEntity)
}

@Dao
interface UsageSessionDao {
    @Insert
    suspend fun insert(session: UsageSessionEntity): Long

    @Query("SELECT * FROM usage_sessions ORDER BY startTimestamp DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = 100): List<UsageSessionEntity>

    @Query("DELETE FROM usage_sessions WHERE endTimestamp < :cutoffTimestamp")
    suspend fun deleteOlderThan(cutoffTimestamp: Long)
}

@Dao
interface UserPreferencesDao {
    @Query("SELECT value FROM user_preferences WHERE `key` = :key")
    suspend fun get(key: String): String?

    @Query("SELECT value FROM user_preferences WHERE `key` = :key")
    fun observe(key: String): Flow<String?>

    @Upsert
    suspend fun set(preference: UserPreferenceEntity)
}
```

### 8.5 Database Class

```kotlin
@Database(
    entities = [
        MonitoredAppEntity::class,
        ThresholdConfigEntity::class,
        UsageSessionEntity::class,
        UserPreferenceEntity::class
    ],
    version = 1,
    exportSchema = true  // RA-018: schema export for migration tracking
)
abstract class MindfulScrollDatabase : RoomDatabase() {
    abstract fun monitoredAppDao(): MonitoredAppDao
    abstract fun thresholdConfigDao(): ThresholdConfigDao
    abstract fun usageSessionDao(): UsageSessionDao
    abstract fun userPreferencesDao(): UserPreferencesDao
}
```

### 8.6 Data Flow Diagram

```
User Action                    Database                      Service
───────────                    ────────                      ───────
Select app ────► Repository ──► Room INSERT/UPDATE
                                      │
                                      ▼ (Flow emission)
                               Service observes ──► PollingEngine updates
                               active app list       match set

Session ends ◄── SessionManager ──► Repository ──► Room INSERT session record

Threshold change ──► Repository ──► Room UPSERT
                                      │
                                      ▼ (Flow emission)
                               ThresholdEngine ──► recalculates states

App startup ────► CleanupUseCase ──► Repository ──► Room DELETE (>90 days)
```

### 8.7 Migration Strategy

| Version | Changes | Migration |
| ------- | ------- | --------- |
| 1 (MVP) | Initial schema | N/A |
| 2 (v1.1 planned) | Add dashboard summary table, add per-app threshold FK enforcement | Manual `Migration(1, 2)` — never use `fallbackToDestructiveMigration()` |

**Forward Compatibility Measures:**
- `ThresholdConfig.appPackageName` already exists for v1.1 per-app overrides.
- `UsageSession` stores all data needed for the v1.1 dashboard.
- Schema JSON is exported for version comparison.

---

## 9. Service Architecture

### 9.1 Service Lifecycle

```
                         ┌──────────────────────┐
           App Launch    │                      │
           or Boot  ─────►  Check persisted     │
                         │  monitoring_active    │
                         │  flag                 │
                         └──────────┬───────────┘
                                    │
                          ┌─────────┼──────────┐
                          │ true               │ false
                          ▼                    ▼
               ┌────────────────┐    ┌───────────────┐
               │ startForeground│    │   Do nothing   │
               │ Service()      │    │                │
               └───────┬────────┘    └───────────────┘
                       │
                       ▼
              ┌─────────────────┐
              │  onCreate()     │
              │  • Create       │
              │    notification │
              │    channel      │
              │  • Inject deps  │
              └────────┬────────┘
                       │
                       ▼
              ┌─────────────────┐
              │ onStartCommand()│
              │  • startFore-   │
              │    ground()     │
              │  • Return       │
              │    START_STICKY │
              │  • Start polling│
              │  • Register     │
              │    screen rcvr  │
              │  • Cleanup stale│
              │    overlays     │──── RA-011 mitigation
              └────────┬────────┘
                       │
                       ▼
              ┌─────────────────┐
              │  RUNNING STATE  │◄──── Polling active
              │  (normal ops)   │      Session management active
              │                 │      Overlay managed
              └────────┬────────┘
                       │
          ┌────────────┼──────────────┐
          │ User stops              │ OS kills
          ▼                         ▼
┌─────────────────┐      ┌──────────────────┐
│  onDestroy()    │      │  Process killed  │
│  • Stop polling │      │  (no callback)   │
│  • End sessions │      │                  │
│  • Remove       │      │  START_STICKY    │
│    overlay      │      │  ──► system      │
│  • Unregister   │      │     restarts     │
│    receivers    │      │     service      │
│  • Clear flag   │      │                  │
└─────────────────┘      └──────────────────┘
```

### 9.2 Polling Engine Architecture

```
┌──────────────────────────────────────────────┐
│              PollingEngine                     │
│                                               │
│  ┌─────────────────────────────────────────┐  │
│  │          Coroutine Loop                  │  │
│  │                                          │  │
│  │  while (isActive) {                      │  │
│  │      if (screenOn) {                     │  │
│  │          val pkg = queryForegroundApp()  │  │
│  │          emit(pkg)                       │  │
│  │      }                                   │  │
│  │      delay(POLL_INTERVAL_MS)             │  │
│  │  }                                       │  │
│  └─────────────────────────────────────────┘  │
│                                               │
│  ┌─────────────────────────────────────────┐  │
│  │    queryForegroundApp()                  │  │
│  │                                          │  │
│  │  1. Try queryEvents(MOVE_TO_FOREGROUND)  │  │
│  │     ↓ success? return packageName        │  │
│  │     ↓ empty/error?                       │  │
│  │  2. Fallback: queryUsageStats()          │  │
│  │     compare lastTimeUsed                 │  │
│  │     ↓ success? return packageName        │  │
│  │     ↓ empty?                             │  │
│  │  3. Return null (no foreground detected) │  │
│  └─────────────────────────────────────────┘  │
│                                               │
│  Constants:                                   │
│    POLL_INTERVAL_MS = 2000                    │
│    QUERY_WINDOW_MS  = 5000                    │
└──────────────────────────────────────────────┘
```

### 9.3 Session Management Flow

```
PollingEngine                    SessionManager                    OverlayController
     │                                │                                  │
     │ foregroundApp = "com.instagram" │                                  │
     │ ──────────────────────────────► │                                  │
     │                                │ Is it monitored?                 │
     │                                │ ── Yes ──►                       │
     │                                │                                  │
     │                                │ Current state = INACTIVE         │
     │                                │ ──► Transition to ACTIVE         │
     │                                │     Start elapsed timer          │
     │                                │                                  │
     │                                │ elapsed = 60 sec                 │
     │                                │ ──────────────────────────────► │
     │                                │                  Show overlay    │
     │                                │                  (fade-in)       │
     │                                │                                  │
     │                                │ elapsed = T1                     │
     │                                │ ──► ThresholdEngine: NOTICE      │
     │                                │ ──────────────────────────────► │
     │                                │               Update color       │
     │                                │               (animate 2000ms)   │
     │                                │                                  │
     │ foregroundApp = "com.launcher"  │                                  │
     │ ──────────────────────────────► │                                  │
     │                                │ Monitored app left               │
     │                                │ ──► Transition to COOLDOWN       │
     │                                │     Start 45-sec timer           │
     │                                │ ──────────────────────────────► │
     │                                │                  Dim overlay     │
     │                                │                  (30% opacity)   │
     │                                │                                  │
     │                                │ Cooldown expires (45 sec)        │
     │                                │ ──► Transition to ENDED          │
     │                                │     Persist session to DB        │
     │                                │ ──────────────────────────────► │
     │                                │                  Fade-out 400ms  │
     │                                │                  Remove overlay  │
     │                                │                                  │
     │                                │ ──► Transition to INACTIVE       │
```

### 9.4 Service Notification

| Parameter | Value |
| --------- | ----- |
| Channel ID | `mindfulscroll_monitoring` |
| Channel Name | "Monitoring Service" |
| Importance | `IMPORTANCE_LOW` (no sound, no vibration) |
| Content Title | "MindfulScroll is monitoring" |
| Content Text | Static for MVP (see OQ-7 for future enhancement) |
| Small Icon | App icon |
| Content Intent | Opens MainActivity |
| Action Button | "Stop Monitoring" — stops the service |
| Ongoing | Yes (foreground service notification) |

---

## 10. Overlay Architecture

### 10.1 Overlay System Design

The overlay is a **View-based** component, not a Compose component. This is because `WindowManager.addView()` with `TYPE_APPLICATION_OVERLAY` requires Android View objects. The overlay system is entirely managed by the `OverlayController` within the `MonitoringService`.

### 10.2 Overlay Component Diagram

```
┌──────────────────────────────────────────────────────────────────┐
│                      OverlayController                            │
│                                                                   │
│  ┌───────────────┐   ┌──────────────────┐   ┌─────────────────┐  │
│  │ OverlayView   │   │ WindowManager    │   │ PositionManager │  │
│  │               │   │ Operations       │   │                 │  │
│  │ • Custom View │   │ • addView()      │   │ • Load saved    │  │
│  │ • Draws       │   │ • updateLayout() │   │   position      │  │
│  │   circle +    │   │ • removeView()   │   │ • Save position │  │
│  │   text        │   │                  │   │ • Calculate     │  │
│  │ • Handles     │   │                  │   │   snap target   │  │
│  │   touch/drag  │   │                  │   │ • Handle rotate │  │
│  └───────┬───────┘   └────────┬─────────┘   └────────┬────────┘  │
│          │                    │                       │           │
│  ┌───────▼────────────────────▼───────────────────────▼────────┐  │
│  │                    AnimationManager                          │  │
│  │                                                              │  │
│  │  • Fade-in   (500ms, ease-in-out, 0% → Calm opacity)       │  │
│  │  • Fade-out  (400ms, ease-in-out, current → 0%)            │  │
│  │  • Color     (2000ms, linear interpolation, bg + text)      │  │
│  │  • Opacity   (2000ms, synchronized with color)              │  │
│  │  • Snap      (250ms, decelerate, horizontal translation)    │  │
│  │  • Throb     (1500ms, ease-in-out, scale 1.0x ↔ 1.15x)    │  │
│  │  • Dim       (300ms, reduce opacity by 30%)                 │  │
│  │  • Undim     (300ms, restore to full state opacity)         │  │
│  └──────────────────────────────────────────────────────────────┘  │
└───────────────────────────────────────────────────────────────────┘
```

### 10.3 OverlayView Specification

```
┌─────────────────────────────┐
│                             │
│     ┌───────────────┐       │
│     │               │       │
│     │   Background  │       │   Diameter: 48-56dp
│     │   (Circle)    │       │   Shape: Circle
│     │               │       │   Background: Solid color (visual state dependent)
│     │    MM:SS      │       │   Text: White or dark (visual state dependent)
│     │  (centered)   │       │   Font: Monospace, ~12sp
│     │               │       │
│     └───────────────┘       │
│                             │
└─────────────────────────────┘
```

### 10.4 WindowManager Layout Parameters

```kotlin
val params = WindowManager.LayoutParams(
    overlaySize,                          // Width: 48-56dp in pixels
    overlaySize,                          // Height: 48-56dp in pixels
    WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
    WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
        or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
    PixelFormat.TRANSLUCENT
).apply {
    gravity = Gravity.TOP or Gravity.START
    x = calculatedX                       // From PositionManager
    y = calculatedY                       // From PositionManager
}
```

**Flag Explanation:**

| Flag | Purpose | SRS Trace |
| ---- | ------- | --------- |
| `FLAG_NOT_FOCUSABLE` | Prevents overlay from stealing focus from host app | FR-019.AC6 |
| `FLAG_NOT_TOUCH_MODAL` | Allows touches outside overlay bounds to pass through | FR-019.AC7 |
| `FLAG_LAYOUT_NO_LIMITS` | Allows overlay to be positioned at screen edges during drag | FR-021.AC3 |

### 10.5 Touch Handling Strategy

```
User touches screen
        │
        ▼
Is touch within overlay bounds?
        │
    ┌───┴───┐
    │ YES   │ NO ──► Touch passes through to host app
    │       │       (FLAG_NOT_FOCUSABLE handles this automatically)
    ▼
Is touch duration < 150ms AND movement < 10dp?
    │
┌───┴───┐
│ YES   │ NO ──► Drag operation
│       │       • ACTION_MOVE: update overlay position in real-time
│       │       • ACTION_UP: edge-snap animation
│       │                    save position to repository
│       │
▼
Short tap ──► Consumed (no action in MVP; tap-to-expand in v1.1)
```

### 10.6 Edge-Snapping Algorithm

```
On ACTION_UP (drag release):

1. Get overlay center X position
2. Get screen width
3. If center X ≤ screen width / 2:
       snapEdge = LEFT
       targetX = 0  (flush left)
   Else if center X > screen width / 2:
       snapEdge = RIGHT
       targetX = screenWidth - overlayWidth  (flush right)
   Else (exactly center):
       snapEdge = RIGHT  (deterministic default, Decision D-003)

4. Animate overlay X from current to targetX over 250ms (decelerate interpolator)
5. Preserve Y position (no vertical snapping)
6. Save (snapEdge, Y as percentage of screenHeight) to UserPreferences repository
```

### 10.7 Visual State Color Mapping

| State | Background Color | Text Color | Opacity | Trigger |
| ----- | --------------- | ---------- | ------- | ------- |
| CALM | `#6B7280` (Muted gray) | White `#FFFFFF` | 65% | Overlay appears (60 sec) |
| NOTICE | `#F59E0B` (Warm amber) | Dark `#1F2937` | 78% | Elapsed ≥ T1 |
| ALERT | `#F97316` (Orange) | White `#FFFFFF` | 88% | Elapsed ≥ T2 |
| URGENT | `#EF4444` (Red/coral) | White `#FFFFFF` | 95% | Elapsed ≥ T3 |
| THROB | `#EF4444` (Pulsing red) | White `#FFFFFF` | 100% | Elapsed ≥ T3 + 5 min |

> **Note:** Colors are preliminary per SRS FR-028. Final values pending UI/UX Specification.

### 10.8 Ghost Overlay Prevention (RA-011)

| Mechanism | Implementation |
| --------- | -------------- |
| Service `onDestroy()` cleanup | Always remove overlay in `finally` block of `onDestroy()` |
| Service startup cleanup | On `onStartCommand()`, check for and remove any existing overlay |
| Try-catch on removal | Wrap all `WindowManager.removeView()` in try-catch |
| View attachment check | Call `isAttachedToWindow()` before `removeView()` |
| Null-safe reference | Clear overlay reference after successful removal |

---

## 11. State Management Architecture

### 11.1 State Machine: Session State

As defined in SRS Section 10.1. This is implemented as a pure Kotlin class in the domain layer.

```kotlin
// domain/model/SessionState.kt
enum class SessionState {
    INACTIVE,   // No active session
    ACTIVE,     // Monitored app in foreground, timer counting
    COOLDOWN,   // User left monitored app, waiting to see if they return
    ENDED       // Session finished, data being persisted
}
```

**Transition Matrix:**

| Current State | Event | Next State | Action |
| ------------- | ----- | ---------- | ------ |
| INACTIVE | Monitored app detected | ACTIVE | Create session, start elapsed timer |
| ACTIVE | Foreground app changes to non-monitored | COOLDOWN | Pause elapsed timer, start cooldown timer, dim overlay |
| ACTIVE | Screen turns off | COOLDOWN | Same as above |
| ACTIVE | User stops monitoring | ENDED | End session, persist data |
| COOLDOWN | Same monitored app returns | ACTIVE | Resume elapsed timer, undim overlay |
| COOLDOWN | 45 seconds expire | ENDED | End session, persist data, remove overlay |
| COOLDOWN | Different monitored app detected | ENDED + new ACTIVE | End first session, start new session |
| ENDED | Session persisted | INACTIVE | Clear state, remove overlay |

### 11.2 State Machine: Timer Visual State

As defined in SRS Section 10.2. Implemented as a pure function in the ThresholdEngine.

```kotlin
// domain/model/TimerVisualState.kt
enum class TimerVisualState {
    CALM,       // Overlay visible, before T1
    NOTICE,     // Elapsed ≥ T1 and < T2
    ALERT,      // Elapsed ≥ T2 and < T3
    URGENT,     // Elapsed ≥ T3 and < T3+5min
    THROB       // Elapsed ≥ T3+5min (adds scale animation)
}
```

**Evaluation Function:**

```kotlin
fun evaluate(elapsedSeconds: Int, config: ThresholdConfig): TimerVisualState {
    val t1Seconds = config.threshold1Min * 60
    val t2Seconds = config.threshold2Min * 60
    val t3Seconds = config.threshold3Min * 60
    val throbSeconds = t3Seconds + (5 * 60)

    return when {
        elapsedSeconds >= throbSeconds -> TimerVisualState.THROB
        elapsedSeconds >= t3Seconds   -> TimerVisualState.URGENT
        elapsedSeconds >= t2Seconds   -> TimerVisualState.ALERT
        elapsedSeconds >= t1Seconds   -> TimerVisualState.NOTICE
        else                          -> TimerVisualState.CALM
    }
}
```

### 11.3 State Machine: Monitoring Service

As defined in SRS Section 10.3.

```kotlin
// domain/model/ServiceState.kt
enum class ServiceState {
    STOPPED,    // Service not running
    RUNNING     // Service active and polling
}
```

### 11.4 UI State Management

Each screen uses a sealed interface or data class for its UI state, held in the corresponding ViewModel as `StateFlow`.

```kotlin
// Example: Home Screen
data class HomeUiState(
    val isMonitoringActive: Boolean = false,
    val monitoredAppCount: Int = 0,
    val thresholdDisplay: String = "",
    val permissionStatus: PermissionStatus = PermissionStatus(),
    val isLoading: Boolean = true
)

class HomeViewModel @Inject constructor(
    private val getMonitoredApps: GetMonitoredAppsUseCase,
    private val getThresholdConfig: GetThresholdConfigUseCase,
    private val toggleMonitoring: ToggleMonitoringUseCase,
    private val checkPermissions: CheckPermissionsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    // ... collects data from use cases, updates _uiState
}
```

---

## 12. Navigation Architecture

### 12.1 Navigation Graph

```
┌─────────────────────────────────────────────────────────────────┐
│                      NavHost (MainActivity)                      │
│                                                                  │
│  ┌──────────────────────────────────────────────┐                │
│  │            ONBOARDING NESTED GRAPH            │                │
│  │  (conditional: shown if !onboarding_completed │                │
│  │   OR missing critical permissions)            │                │
│  │                                               │                │
│  │  Welcome ──► UsageAccess ──► Overlay ──►      │                │
│  │  Battery ──► AppSelection ──► Threshold ──►   │                │
│  │  Completion ──► [navigate to Home, clear backstack]│           │
│  └──────────────────────────────────────────────┘                │
│                        │                                         │
│                        ▼                                         │
│  ┌──────────────────────────────────────────────┐                │
│  │              MAIN NESTED GRAPH                │                │
│  │                                               │                │
│  │  Home (start) ──► AppSelection                │                │
│  │       │       ──► ThresholdConfig             │                │
│  │       │       ──► About                       │                │
│  │       │                                       │                │
│  │       └── [Permission banner: tap navigates   │                │
│  │           back to onboarding permission step] │                │
│  └──────────────────────────────────────────────┘                │
└─────────────────────────────────────────────────────────────────┘
```

### 12.2 Route Definitions

```kotlin
sealed class Route(val route: String) {
    // Onboarding
    object Welcome           : Route("onboarding/welcome")
    object PermUsageAccess   : Route("onboarding/perm_usage_access")
    object PermOverlay       : Route("onboarding/perm_overlay")
    object PermBattery       : Route("onboarding/perm_battery")
    object OnboardAppSelect  : Route("onboarding/app_selection")
    object OnboardThreshold  : Route("onboarding/threshold")
    object OnboardComplete   : Route("onboarding/complete")

    // Main
    object Home              : Route("main/home")
    object AppSelection      : Route("main/app_selection")
    object ThresholdConfig   : Route("main/threshold_config")
    object About             : Route("main/about")
}
```

### 12.3 Navigation Logic

```
On app launch:
  │
  ├─ Check onboarding_completed flag
  │   │
  │   ├─ false ──► Navigate to Onboarding graph (Welcome)
  │   │
  │   └─ true ──► Check critical permissions (Usage Access)
  │       │
  │       ├─ All granted ──► Navigate to Main graph (Home)
  │       │
  │       └─ Missing ──► Navigate to Onboarding graph
  │                      (skip to the missing permission step)
```

---

## 13. Dependency Injection Architecture

### 13.1 DI Framework

| Parameter | Value |
| --------- | ----- |
| Framework | **Hilt** (recommended) |
| Rationale | First-party Jetpack support, compile-time verification, ViewModel integration, WorkManager support for future |
| Alternative | Koin (if Hilt's annotation processing overhead is undesirable) |

### 13.2 Module Structure

```kotlin
// di/DatabaseModule.kt
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MindfulScrollDatabase {
        return Room.databaseBuilder(
            context,
            MindfulScrollDatabase::class.java,
            "mindfulscroll.db"
        ).build()
    }

    @Provides fun provideMonitoredAppDao(db: MindfulScrollDatabase) = db.monitoredAppDao()
    @Provides fun provideThresholdConfigDao(db: MindfulScrollDatabase) = db.thresholdConfigDao()
    @Provides fun provideUsageSessionDao(db: MindfulScrollDatabase) = db.usageSessionDao()
    @Provides fun provideUserPreferencesDao(db: MindfulScrollDatabase) = db.userPreferencesDao()
}

// di/RepositoryModule.kt
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds abstract fun bindMonitoredAppRepo(impl: MonitoredAppRepositoryImpl): MonitoredAppRepository
    @Binds abstract fun bindThresholdConfigRepo(impl: ThresholdConfigRepositoryImpl): ThresholdConfigRepository
    @Binds abstract fun bindUsageSessionRepo(impl: UsageSessionRepositoryImpl): UsageSessionRepository
    @Binds abstract fun bindUserPreferencesRepo(impl: UserPreferencesRepositoryImpl): UserPreferencesRepository
    @Binds abstract fun bindInstalledAppRepo(impl: InstalledAppRepositoryImpl): InstalledAppRepository
    @Binds abstract fun bindUsageStatsRepo(impl: UsageStatsRepositoryImpl): UsageStatsRepository
}

// di/SystemModule.kt
@Module
@InstallIn(SingletonComponent::class)
object SystemModule {
    @Provides
    fun provideUsageStatsManager(@ApplicationContext context: Context): UsageStatsManager {
        return context.getSystemService(Context.USAGE_STATS_SERVICE) as UsageStatsManager
    }

    @Provides
    fun providePackageManager(@ApplicationContext context: Context): PackageManager {
        return context.packageManager
    }
}
```

### 13.3 Scope Strategy

| Scope | Lifecycle | Components |
| ----- | --------- | ---------- |
| `@Singleton` | Application lifetime | Database, DAOs, Repositories, System API wrappers |
| `@ViewModelScoped` | ViewModel lifetime | Use Cases (if stateful), UI state holders |
| Unscoped | Created per injection | Use Cases (if stateless), mappers, validators |

### 13.4 Service Injection

Hilt supports `@AndroidEntryPoint` for Services. The MonitoringService receives its dependencies via field injection:

```kotlin
@AndroidEntryPoint
class MonitoringService : Service() {
    @Inject lateinit var usageStatsRepository: UsageStatsRepository
    @Inject lateinit var monitoredAppRepository: MonitoredAppRepository
    @Inject lateinit var thresholdConfigRepository: ThresholdConfigRepository
    @Inject lateinit var usageSessionRepository: UsageSessionRepository
    @Inject lateinit var userPreferencesRepository: UserPreferencesRepository
    // ...
}
```

---

## 14. Concurrency & Threading Model

### 14.1 Thread Architecture

```
┌─────────────────────────────────────────────────────────────┐
│                        MAIN THREAD                           │
│                                                              │
│  • Compose UI rendering                                      │
│  • StateFlow collection in composables                       │
│  • Navigation events                                         │
│  • OverlayView animation callbacks                           │
│  • Touch event handling                                      │
│                                                              │
│  Rule: NO database access, NO network calls, NO long ops     │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                   Dispatchers.IO                              │
│                                                              │
│  • Room database operations                                  │
│  • UsageStatsManager queries                                 │
│  • PackageManager queries                                    │
│  • File I/O (if any)                                         │
│                                                              │
│  Rule: All suspend functions that touch I/O run here         │
└─────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────┐
│                  Dispatchers.Default                          │
│                                                              │
│  • Threshold evaluation (CPU-bound computation)              │
│  • App list filtering/sorting                                │
│  • Any heavy computation                                     │
│                                                              │
│  Rule: CPU-intensive work that doesn't involve I/O           │
└─────────────────────────────────────────────────────────────┘
```

### 14.2 Coroutine Scope Strategy

| Component | Scope | Dispatcher | Lifecycle |
| --------- | ----- | ---------- | --------- |
| ViewModel | `viewModelScope` | Main (default) | Cleared when ViewModel is cleared |
| MonitoringService | `CoroutineScope(SupervisorJob() + Dispatchers.Main)` | Main + IO for child coroutines | Canceled in `onDestroy()` |
| PollingEngine | Child of service scope | IO | Canceled when service stops |
| SessionManager | Child of service scope | Default + IO for DB writes | Canceled when service stops |
| Repository operations | Caller's scope | IO (via `withContext`) | Bound to caller's lifecycle |

### 14.3 Critical Concurrency Rules

| Rule | Reason |
| ---- | ------ |
| **No database on main thread** | Room enforces this. All DAO operations are `suspend` or return `Flow`. |
| **Service scope uses SupervisorJob** | Failure in one child coroutine (e.g., a DB write) does not cancel the polling engine. |
| **Overlay updates on main thread** | `WindowManager.updateViewLayout()` must be called from the thread that created the view (main thread). |
| **StateFlow for UI state** | `StateFlow` is conflated — only the latest state is delivered, preventing UI from processing stale states. |
| **Flow for database observations** | Room returns `Flow` for reactive queries. These are cold and only execute when collected. |
| **Cancel scopes in onDestroy** | Prevents coroutine leaks if the service is destroyed (RA-022 mitigation). |

### 14.4 Timer Tick Mechanism

The session elapsed time updates every second. This is implemented via a coroutine, not a `Handler` or `Timer`.

```kotlin
// Inside SessionManager
private fun startElapsedTimer() {
    elapsedTimerJob = serviceScope.launch {
        while (isActive) {
            delay(1000L)
            if (currentState == SessionState.ACTIVE) {
                elapsedSeconds++
                _elapsedTime.value = elapsedSeconds
            }
        }
    }
}
```

The `_elapsedTime` is a `StateFlow<Int>` observed by both the OverlayController (for display) and the ThresholdEngine (for state evaluation).

---

## 15. Security & Privacy Architecture

### 15.1 Threat Model

| Threat | Mitigation | SRS Trace |
| ------ | ---------- | --------- |
| Data exfiltration via network | No `INTERNET` permission. Zero network libraries. Lint enforcement. | NFR-011 |
| Data exfiltration via storage | All data in app-private internal storage. No external storage. | NFR-012.AC1, AC2 |
| Data exfiltration via IPC | No exported components except main launcher Activity. | NFR-012.AC3, AC4 |
| Data exfiltration via backup | `android:allowBackup="false"` or backup rules exclude database. | NFR-012.AC5 |
| Unauthorized access to usage data | Android sandbox isolates app data. No ContentProvider exported. | NFR-012 |
| Overlay clickjacking | Overlay uses `FLAG_NOT_FOCUSABLE` — cannot receive input intended for other apps. | FR-019.AC6 |

### 15.2 Data Privacy Enforcement

```
┌─────────────────────────────────────────────────────┐
│              PRIVACY ENFORCEMENT LAYERS               │
│                                                      │
│  1. MANIFEST ──── No INTERNET permission declared    │
│                                                      │
│  2. DEPENDENCIES No OkHttp, Retrofit, Ktor, or any  │
│                  network library in build.gradle      │
│                                                      │
│  3. LINT ─────── Custom lint rule (or manual check)  │
│                  verifies zero java.net / okhttp      │
│                  imports in codebase                  │
│                                                      │
│  4. STORAGE ──── Room DB in Context.getDatabasePath()│
│                  (app-private internal directory)     │
│                                                      │
│  5. IPC ──────── Only launcher Activity exported     │
│                  No ContentProviders                  │
│                  No exported Services                 │
│                  BootReceiver: exported=true but      │
│                  only receives BOOT_COMPLETED         │
│                                                      │
│  6. BACKUP ───── android:allowBackup="false" or      │
│                  backup rules exclude database        │
└─────────────────────────────────────────────────────┘
```

### 15.3 Permission Architecture

```
AndroidManifest.xml permissions:

<!-- Auto-granted at install -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE" />
<uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
<uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />

<!-- Requires manual user grant via System Settings -->
<uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"
    tools:ignore="ProtectedPermissions" />
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />

<!-- Runtime permission (API 33+) -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

<!-- EXPLICITLY NOT DECLARED -->
<!-- android.permission.INTERNET — NEVER ADD THIS -->
```

---

## 16. Performance Architecture

### 16.1 Performance Budget

| Metric | Target | Source | Enforcement |
| ------ | ------ | ------ | ----------- |
| Cold start | ≤ 2000ms | NFR-001.AC1 | Profile in Phase 8 |
| Warm start | ≤ 500ms | NFR-001.AC2 | Profile in Phase 8 |
| Battery drain | ≤ 2%/hr monitoring | NFR-002.AC1 | 8-hour endurance test |
| Battery (screen off) | ≈ 0% | NFR-002.AC2 | Adaptive polling verification |
| Heap memory | ≤ 50MB | NFR-003.AC1 | Profiler in Phase 8 |
| Total PSS | ≤ 80MB | NFR-003.AC2 | `adb shell dumpsys meminfo` |
| Memory growth (8hr) | 0 (no leaks) | NFR-003.AC3 | Endurance test + LeakCanary |
| Overlay FPS | ≥ 55 FPS | NFR-004.AC1 | GPU Profiler in Phase 8 |
| DB reads (hot path) | ≤ 100ms | NFR-005.AC1 | Benchmarked in Phase 2 |
| DB writes | ≤ 100ms | NFR-005.AC3 | Benchmarked in Phase 2 |
| App list load (200+ apps) | ≤ 3000ms | FR-001.AC4 | Tested in Phase 6 |

### 16.2 Performance Strategies

| Strategy | Applies To | Implementation |
| -------- | ---------- | -------------- |
| **Adaptive polling** | Battery | Pause polling when `ACTION_SCREEN_OFF`; resume on `ACTION_SCREEN_ON` |
| **In-memory caching** | Monitored app set | Cache active package names in a `HashSet` for O(1) lookup per poll |
| **Lazy loading** | App list with icons | Load icons asynchronously; display list structure immediately |
| **Flow conflation** | UI state updates | `StateFlow` conflates — UI always gets latest state, never processes stale |
| **Background DB writes** | Session persistence | All Room operations on `Dispatchers.IO` |
| **Flat view hierarchy** | Overlay rendering | Single custom View with `onDraw()` — no nested views |
| **Hardware acceleration** | Overlay animations | `setLayerType(LAYER_TYPE_HARDWARE)` for smooth animations |
| **Coroutine-based timer** | Elapsed time tick | `delay()` in coroutine — no `Handler`, no `Timer` thread |

### 16.3 Startup Optimization

```
App cold start timeline:

0ms    Application.onCreate()
       └── Hilt dependency graph initialization
           (Singleton scope: DB, Repos, System wrappers)

~200ms MainActivity.onCreate()
       └── setContent { NavHost(...) }

~400ms Compose first frame
       └── Determine start destination (onboarding vs. home)
       └── Read onboarding_completed flag (suspend, fast)
       └── Read permission states (sync, fast)

~600ms First interactive frame
       └── User can interact

Target: ≤ 2000ms (NFR-001.AC1)
Budget: ~1400ms headroom for device variance
```

---

## 17. Error Handling Architecture

### 17.1 Error Handling Strategy

```
┌──────────────────────────────────────────────────────────────────┐
│                    ERROR HANDLING LAYERS                           │
│                                                                   │
│  LAYER 1: PREVENTION                                              │
│  ────────────────────                                             │
│  • State machine guards prevent invalid transitions               │
│  • Input validation in use cases (threshold T1 < T2 < T3)        │
│  • Permission checks before operations                            │
│  • Null-safe Kotlin types throughout                              │
│                                                                   │
│  LAYER 2: GRACEFUL HANDLING                                       │
│  ─────────────────────────                                        │
│  • Try-catch around system API calls (WindowManager, UsageStats)  │
│  • Empty-result handling for queries                              │
│  • Fallback strategies for OEM-specific failures                  │
│                                                                   │
│  LAYER 3: RECOVERY                                                │
│  ────────────────                                                 │
│  • START_STICKY for service restart                               │
│  • BootReceiver for post-reboot recovery                          │
│  • Overlay cleanup on service restart                             │
│  • Session data recovery (future: checkpointing)                  │
│                                                                   │
│  LAYER 4: USER COMMUNICATION                                      │
│  ────────────────────────────                                     │
│  • Permission status banners on Home screen                       │
│  • Onboarding skip warnings                                      │
│  • Silent degradation for non-critical failures                   │
│                                                                   │
│  LAYER 5: NEVER CRASH                                             │
│  ─────────────────────                                            │
│  • Global uncaught exception handler logs (but does not prevent)  │
│  • No unhandled exceptions in service (SRS NFR-007.AC1)           │
│  • All error paths lead to a defined state, never to a crash      │
└──────────────────────────────────────────────────────────────────┘
```

### 17.2 Error Scenario Mapping

| Error Scenario | SRS Trace | Component Responsible | Response |
| -------------- | --------- | --------------------- | -------- |
| Monitored app uninstalled | EH-001 | MonitoredAppRepository | Remove from list silently |
| Permission revoked at runtime | EH-002 | MonitoringService + HomeViewModel | Pause monitoring; show banner |
| Service killed by OS | EH-003 | Android OS + START_STICKY | Auto-restart; reload config from DB |
| Overlay window failure | EH-004 | OverlayController | Log error; continue without overlay |
| UsageStats returns empty | EH-005 | PollingEngine | Treat as no monitored app; retry next poll |
| Database write failure | EH-006 | Repository implementations | Log error; continue in-memory; retry later |
| Device rotation during overlay | EH-007 | OverlayController + PositionManager | Recalculate position from Y-percentage |
| Split-screen mode | EH-008 | PollingEngine | Monitor focused app; overlay covers full screen |

---

## 18. Testability Architecture

### 18.1 Testability by Layer

| Layer | Testability Approach | Framework | Coverage Target |
| ----- | -------------------- | --------- | --------------- |
| **Domain** | Pure unit tests; no Android deps; mock repositories | JUnit 4 + MockK | ≥80% (NFR-014) |
| **Data (Room)** | Instrumented tests with in-memory DB | AndroidX Test + Room Testing | 90% of DAOs |
| **Data (System)** | Mock system APIs via repository interface | MockK | Via domain layer tests |
| **Presentation** | Compose UI tests; mock ViewModels | Compose Test | Key flows |
| **Service** | Integration tests; mock system APIs | AndroidX Test | Core flows |
| **Overlay** | Manual testing on real devices | — | Visual verification |

### 18.2 Test Architecture Diagram

```
┌───────────────────────────────────────────────────────────────┐
│                      TEST PYRAMID                              │
│                                                                │
│                         ╱╲                                     │
│                        ╱  ╲         Manual Tests               │
│                       ╱    ╲        • Overlay interaction      │
│                      ╱ Manual╲      • OEM compatibility        │
│                     ╱────────╲      • Endurance (8 hr)         │
│                    ╱          ╲                                 │
│                   ╱ Integration╲    Integration Tests           │
│                  ╱──────────────╲   • Service + Polling         │
│                 ╱                ╲  • Navigation flows          │
│                ╱   UI / Compose   ╲ • Compose screen tests     │
│               ╱────────────────────╲                           │
│              ╱                      ╲  Unit Tests              │
│             ╱      Unit Tests        ╲ • Session state machine │
│            ╱──────────────────────────╲• Threshold engine      │
│           ╱                            ╲• Repositories (Room)  │
│          ╱        LARGEST LAYER         ╲• Use cases           │
│         ╱────────────────────────────────╲• Validation logic   │
│                                                                │
└───────────────────────────────────────────────────────────────┘
```

### 18.3 Key Testability Enablers

| Enabler | How It Helps | Architectural Impact |
| ------- | ------------ | -------------------- |
| Repository interfaces in domain | System APIs can be mocked for unit tests | Domain layer stays pure Kotlin |
| Constructor injection via Hilt | Dependencies are mockable | All classes receive deps via constructor/field injection |
| StateFlow for state | State is observable and assertable in tests | ViewModels expose testable state |
| Pure functions in ThresholdEngine | Deterministic, no side effects | Can test all state transitions with simple assertions |
| `SystemClock.elapsedRealtime()` abstracted | Timing can be controlled in tests | Inject a clock abstraction for deterministic time tests |

### 18.4 Critical Test Scenarios

| Scenario | Type | Priority | Covers |
| -------- | ---- | -------- | ------ |
| Session state: all 7 transitions | Unit | Must | RA-012 |
| Threshold engine: all 5 states + boundaries | Unit | Must | FR-028 |
| Cooldown: exact 45-second expiry | Unit | Must | FR-014 |
| Concurrent session prevention | Unit | Must | FR-017 |
| Screen-off triggers cooldown | Integration | Must | FR-016 |
| Overlay appears at exactly 60 seconds | Integration | Must | FR-018 |
| Overlay touch pass-through | Manual | Must | RA-010 |
| Ghost overlay after force-stop | Manual | Must | RA-011 |
| Service survives 8 hours on Pixel | Manual | Must | RA-002 |
| Service survives 8 hours on Samsung | Manual | Must | RA-002 |
| Service survives 8 hours on Xiaomi | Manual | Must | RA-002 |

---

## 19. Cross-Cutting Concerns

### 19.1 Logging

| Environment | Strategy |
| ----------- | -------- |
| **Debug builds** | Use `android.util.Log` with a tagged wrapper. Log all state transitions, polling results, and errors. |
| **Release builds** | Strip all debug logs via ProGuard/R8 rules. Only log critical errors (caught exceptions). |
| **No crash reporting SDK** | Per AC-01 (no network) and OQ-5 (unresolved). Logs are local only. |

### 19.2 Configuration Constants

All configurable values are centralized in a constants file:

```kotlin
object AppConstants {
    const val POLL_INTERVAL_MS = 2000L
    const val QUERY_WINDOW_MS = 5000L
    const val COOLDOWN_DURATION_SECONDS = 45
    const val OVERLAY_APPEAR_THRESHOLD_SECONDS = 60
    const val THROB_DELAY_AFTER_T3_SECONDS = 300  // 5 minutes
    const val SESSION_RETENTION_DAYS = 90

    // Overlay
    const val OVERLAY_SIZE_DP = 52
    const val OVERLAY_FADE_IN_MS = 500L
    const val OVERLAY_FADE_OUT_MS = 400L
    const val COLOR_TRANSITION_MS = 2000L
    const val SNAP_ANIMATION_MS = 250L
    const val THROB_CYCLE_MS = 1500L
    const val THROB_SCALE_MAX = 1.15f
    const val COOLDOWN_DIM_ALPHA_REDUCTION = 0.30f

    // Drag thresholds
    const val TAP_TIMEOUT_MS = 150L
    const val TAP_SLOP_DP = 10
}
```

### 19.3 ProGuard / R8 Rules

```proguard
# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *

# Hilt
-keep class dagger.hilt.** { *; }

# Keep domain models (used in Room entities and serialization)
-keep class com.rudy.mindfulscroll.domain.model.** { *; }

# Strip debug logs in release
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
    public static int i(...);
}
```

### 19.4 Build Variants

| Variant | Signing | ProGuard | Logs | Purpose |
| ------- | ------- | -------- | ---- | ------- |
| `debug` | Debug keystore | Off | Full | Development and testing |
| `release` | Release keystore | On (R8) | Critical only | Production distribution |

---

## 20. Future Extensibility

### 20.1 v1.1 Extensibility Points

The architecture is designed to support v1.1 features without structural changes:

| v1.1 Feature | Extensibility Point | Architectural Readiness |
| ------------ | ------------------- | ----------------------- |
| F-016: Usage Dashboard | Add new screen + ViewModel. Query `UsageSession` via existing repository. | `UsageSessionRepository` and `UsageSessionDao` already support historical queries. |
| F-017: Session History | Add new screen. Use existing `UsageSession` entity. | Entity already stores all required fields. |
| F-018: Per-App Thresholds | `ThresholdConfig.appPackageName` field already exists. Add query by app. | Schema is forward-compatible. ThresholdEngine accepts config parameter. |
| F-019: Pause Monitoring | Add a `PAUSED` state to `ServiceState`. Pause polling. | ServiceState enum is easily extensible. |
| F-022: Timer Tap Interaction | Add `onTapListener` to OverlayView. Show expanded info view. | OverlayView touch handler already distinguishes taps from drags. |
| F-023: Monitoring Schedule | Add schedule data to `UserPreferences`. PollingEngine checks schedule before polling. | PollingEngine's polling loop can be conditionally gated. |

### 20.2 Extension Points

| Extension Point | Mechanism | Purpose |
| --------------- | --------- | ------- |
| New screens | Add Route + Composable + ViewModel | Standard Compose Navigation extension |
| New entities | Add Room entity + DAO + migration | Schema version increment with migration |
| New use cases | Add class to `domain/usecase/` | DI auto-provides via constructor injection |
| OEM-specific behavior | Strategy pattern in PollingEngine | OEM detection via `Build.MANUFACTURER` |
| Alternative data sources | New repository implementation | Swap via DI module |
| New visual states | Extend `TimerVisualState` enum | ThresholdEngine evaluation function handles new states |

---

## 21. Architectural Decision Record

### 21.1 Decisions Made

| ADR ID | Decision | Alternatives Considered | Rationale | Date |
| ------ | -------- | ----------------------- | --------- | ---- |
| ADR-001 | Clean Architecture with 3 layers | 2-layer (UI+Data), Android Architecture Components only | Enables pure Kotlin domain layer for testability (AG-02). Required by NFR-013. | 2026-02-05 |
| ADR-002 | Hilt for dependency injection | Koin, Manual DI | Compile-time verification. First-party Jetpack support. Service injection support. | 2026-02-05 |
| ADR-003 | View-based overlay (not Compose) | Compose with `TYPE_APPLICATION_OVERLAY` | WindowManager requires View objects. Compose cannot render into `TYPE_APPLICATION_OVERLAY` windows. No alternative. | 2026-02-05 |
| ADR-004 | Coroutine-based polling (not Handler/Timer) | `Handler.postDelayed`, `java.util.Timer`, `AlarmManager` | Coroutines integrate with structured concurrency, are cancelable, and work cleanly with `Dispatchers.IO`. | 2026-02-05 |
| ADR-005 | StateFlow for UI state (not LiveData) | `LiveData`, `MutableState` | StateFlow is Kotlin-native, works in non-UI contexts (service), and integrates with Compose's `collectAsState()`. | 2026-02-05 |
| ADR-006 | Room for all persistence (not DataStore) | DataStore, SharedPreferences, SQLite directly | Room provides type-safe queries, reactive Flow, and migration support. Even preferences are stored in Room for consistency. | 2026-02-05 |
| ADR-007 | `SystemClock.elapsedRealtime()` for timing | `System.currentTimeMillis()`, `Instant.now()` | Immune to wall-clock changes (timezone, manual adjustment). Monotonic. Required by SRS EC-05. | 2026-02-05 |
| ADR-008 | Single notification channel (low importance) | Multiple channels, high importance | Monitoring notification should be minimally intrusive. One channel simplifies management. | 2026-02-05 |
| ADR-009 | No crash reporting SDK | Firebase Crashlytics, Sentry | No network constraint (AC-01). OQ-5 unresolved but architecture defaults to no-network. | 2026-02-05 |
| ADR-010 | SupervisorJob in service scope | Regular Job | Child coroutine failure (DB write) should not cancel polling engine. Fault isolation. | 2026-02-05 |

### 21.2 Decisions Pending

| ID | Decision Needed | Options | Depends On | Decide By |
| -- | --------------- | ------- | ---------- | --------- |
| ADR-P01 | Throb animation: include haptic feedback? | Yes (vibrate pattern) / No (visual only) | UX testing | Before Phase 5 (OQ-4) |
| ADR-P02 | Service notification: show timer or static text? | Timer in notification / Static "Monitoring active" | UX decision | Before Phase 3 (OQ-7) |
| ADR-P03 | Include crash reporting (affects AC-01)? | No SDK / Firebase Crashlytics / Local-only logging | Privacy decision | Before Phase 1 (OQ-5) |

---

## 22. Traceability Matrix

### 22.1 SRS Requirement → Architecture Component

| SRS Requirement | Architecture Component | Package |
| --------------- | ---------------------- | ------- |
| FR-001 – FR-003 | InstalledAppRepository, PackageManagerWrapper | `data/system/`, `data/repository/` |
| FR-004 – FR-008 | MonitoredAppRepository, AppSelectionViewModel | `data/repository/`, `presentation/appselection/` |
| FR-009 – FR-011 | PollingEngine, UsageStatsRepository | `service/monitoring/`, `data/system/` |
| FR-012 – FR-017 | SessionManager (state machine) | `service/monitoring/` |
| FR-018 – FR-024 | OverlayController, OverlayView, PositionManager, AnimationManager | `service/overlay/` |
| FR-025 – FR-027 | ThresholdConfigRepository, ThresholdViewModel | `data/repository/`, `presentation/threshold/` |
| FR-028 – FR-029 | ThresholdEngine, OverlayController (color transitions) | `service/threshold/`, `service/overlay/` |
| FR-030 – FR-031 | ThresholdEngine (trigger), AnimationManager (throb) | `service/threshold/`, `service/overlay/` |
| FR-032 – FR-034 | OnboardingViewModel, PermissionChecker | `presentation/onboarding/`, `data/system/` |
| FR-035 – FR-037 | MonitoringService | `service/monitoring/` |
| FR-038 | BootReceiver | `service/receiver/` |
| FR-039 – FR-041 | HomeViewModel, Navigation graph | `presentation/home/`, `presentation/navigation/` |
| NFR-001 | Startup optimization, lazy init | Cross-cutting |
| NFR-002 | Adaptive polling, screen state handling | `service/monitoring/` |
| NFR-003 | Memory-conscious design, coroutine lifecycle | Cross-cutting |
| NFR-004 | Hardware-accelerated overlay, flat view | `service/overlay/` |
| NFR-006 | START_STICKY, BootReceiver, battery exemption | `service/monitoring/`, `service/receiver/` |
| NFR-008 | Room WAL mode, transactions, migration strategy | `data/local/` |
| NFR-011 | No INTERNET permission, no network libs | Manifest, `build.gradle.kts` |
| NFR-012 | Private storage, no exports, no backup | Manifest, `data/local/` |
| NFR-013 | Clean Architecture, 3 layers, DI | All packages |
| NFR-014 | Domain layer testability, mock-friendly interfaces | `domain/` |
| NFR-016 | OEM-adaptive polling, fallback queries | `service/monitoring/`, `data/system/` |

### 22.2 Risk → Architecture Mitigation

| Risk | Architecture Mitigation | Component |
| ---- | ----------------------- | --------- |
| RA-002 (OEM battery kill) | START_STICKY, BootReceiver, SupervisorJob, battery exemption | MonitoringService, BootReceiver |
| RA-007 (UsageStats inconsistency) | Dual-query strategy (primary + fallback) | PollingEngine, UsageStatsRepository |
| RA-010 (Touch interference) | FLAG_NOT_FOCUSABLE, FLAG_NOT_TOUCH_MODAL, precise touch bounds | OverlayController |
| RA-011 (Ghost overlay) | Cleanup on startup, try-catch on removal, null-safe references | OverlayController |
| RA-012 (State machine errors) | Pure Kotlin state machine, exhaustive transitions, 100% unit tests | SessionManager (domain model) |
| RA-013 (Session data loss) | Accepted for MVP; future: periodic checkpointing | SessionManager |
| RA-014 (Overlay jank) | Flat view hierarchy, hardware acceleration, coroutine-based timer | OverlayView |
| RA-018 (Migration failures) | Schema export, never destructive migration, forward-compatible schema | MindfulScrollDatabase |
| RA-022 (Memory leak) | Scoped coroutines, receiver unregistration, animation cleanup in onDestroy | MonitoringService, OverlayController |

### 22.3 Project Plan Phase → Architecture Component

| Phase | Primary Architecture Components Built |
| ----- | ------------------------------------- |
| P1: Foundation | Package structure, DI modules, manifest, theme, navigation shell |
| P2: Data Layer | Room database, entities, DAOs, repositories, UserPreferences |
| P3: Engine | PollingEngine, SessionManager (state machine), MonitoringService, BootReceiver, ScreenStateReceiver |
| P4: Overlay | OverlayController, OverlayView, PositionManager, AnimationManager (fade, drag, snap) |
| P5: Escalation | ThresholdEngine, color transitions, throb animation |
| P6: UI | All Compose screens, ViewModels, navigation connections |
| P7: Onboarding | Onboarding screens, PermissionChecker, conditional routing |
| P8: Integration | Error handlers, edge case hardening, performance profiling |

---

## 23. Glossary

All terms from PRD Glossary (Section 17), SRS Glossary (Section 14), Project Plan Glossary (Section 17), and Risk Assessment Glossary (Section 14) apply. Additional architecture-specific terms:

| Term | Definition |
| ---- | ---------- |
| **Clean Architecture** | A software architecture pattern that separates code into concentric layers with dependencies pointing inward, isolating business logic from framework concerns. |
| **MVVM** | Model-View-ViewModel pattern where the ViewModel holds UI state and the View observes it reactively. |
| **Repository Pattern** | An abstraction that provides a clean API for data access, hiding the details of data sources (database, API, cache) behind an interface. |
| **StateFlow** | A Kotlin coroutine primitive that holds a single value and emits updates to collectors. Used for observable state. |
| **Flow** | A Kotlin coroutine primitive for asynchronous stream processing. Cold — only executes when collected. |
| **Dependency Injection (DI)** | A design pattern where dependencies are provided to a class rather than created by it, enabling testability and loose coupling. |
| **Hilt** | A Jetpack dependency injection library built on Dagger that provides compile-time DI for Android. |
| **SupervisorJob** | A coroutine Job variant where failure of one child does not cancel sibling children. Used for fault isolation. |
| **NavHost** | The Jetpack Compose Navigation container that hosts navigation destinations. |
| **WAL Mode** | Write-Ahead Logging mode in SQLite/Room that allows concurrent reads and writes for improved performance. |
| **TYPE_APPLICATION_OVERLAY** | An Android window type that allows an app to draw on top of other applications. Requires `SYSTEM_ALERT_WINDOW` permission. |
| **FLAG_NOT_FOCUSABLE** | A window flag that prevents the overlay from receiving input focus, allowing the underlying app to receive key and touch events. |
| **START_STICKY** | A service return value indicating the system should recreate the service after it is killed, calling `onStartCommand()` with a null intent. |
| **CoroutineScope** | A structured concurrency scope that manages the lifecycle of coroutines launched within it. |
| **ProGuard/R8** | Android code shrinking and obfuscation tools that reduce APK size and protect code. |

---

## 24. References

| Reference | Relevance |
| --------- | --------- |
| [PRD.md](PRD.md) | Product features, principles, constraints, scope |
| [SRS.md](SRS.md) | Functional and non-functional requirements, data model, state machines |
| [ProjectPlan.md](ProjectPlan.md) | Phase breakdown, technology stack, WBS dependencies |
| [RiskAssessment.md](RiskAssessment.md) | Risk register, architectural risk mitigations |
| [Android Clean Architecture Guide](https://developer.android.com/topic/architecture) | Architecture pattern reference |
| [Jetpack Compose Navigation](https://developer.android.com/develop/ui/compose/navigation) | Navigation architecture reference |
| [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android) | DI framework reference |
| [Room Persistence Library](https://developer.android.com/training/data-storage/room) | Database architecture reference |
| [Android Foreground Services](https://developer.android.com/develop/background-work/services/foreground-services) | Service architecture reference |
| [Android WindowManager](https://developer.android.com/reference/android/view/WindowManager) | Overlay system reference |
| [Kotlin Coroutines Guide](https://kotlinlang.org/docs/coroutines-guide.html) | Concurrency model reference |
| [Don't Kill My App](https://dontkillmyapp.com/) | OEM service reliability reference |

---

*End of Document*
