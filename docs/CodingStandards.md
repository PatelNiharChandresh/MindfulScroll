# MindfulScroll — Coding Standards Document

| Field             | Value                                                        |
| ----------------- | ------------------------------------------------------------ |
| **Project**       | MindfulScroll — Digital Awareness Companion                  |
| **Document**      | CodingStandards.md (D-08)                                    |
| **Version**       | 1.0                                                          |
| **Date**          | 2026-02-07                                                   |
| **Status**        | Phase 0 — Final                                              |
| **WBS Reference** | WBS 0.8                                                      |

### Source Documents

| Document                              | Version | Relevance                                                     |
| ------------------------------------- | ------- | ------------------------------------------------------------- |
| [PRD.md](PRD.md)                      | 1.0     | Product principles, constraints, features                     |
| [SRS.md](SRS.md)                      | 1.0     | Functional/non-functional requirements, data model            |
| [ProjectPlan.md](ProjectPlan.md)      | 1.0     | Phase plan, WBS, technology stack                             |
| [RiskAssessment.md](RiskAssessment.md)| 1.0     | Risk mitigations affecting code patterns                      |
| [SAD.md](SAD.md)                      | 1.0     | Architecture layers, patterns, package structure, ADRs        |
| [TDD.md](TDD.md)                     | 1.0     | Implementation designs, conventions, algorithms               |
| [DbSchema.md](DbSchema.md)           | 1.0     | Database schema, entity design, repository patterns           |
| [UIUXspec.md](UIUXspec.md)           | 1.0     | Design system, Compose patterns, animation specs              |
| [Privacy.md](Privacy.md)             | 1.0     | Privacy-by-design, zero-network, prohibited dependencies      |

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Project Configuration Standards](#2-project-configuration-standards)
3. [Architecture Standards](#3-architecture-standards)
4. [Package Structure Standards](#4-package-structure-standards)
5. [Kotlin Language Conventions](#5-kotlin-language-conventions)
6. [Naming Conventions](#6-naming-conventions)
7. [Error Handling Standards](#7-error-handling-standards)
8. [Coroutine & Flow Standards](#8-coroutine--flow-standards)
9. [Dependency Injection Standards](#9-dependency-injection-standards)
10. [Data Layer Standards](#10-data-layer-standards)
11. [Domain Layer Standards](#11-domain-layer-standards)
12. [Presentation Layer Standards](#12-presentation-layer-standards)
13. [Service Layer Standards](#13-service-layer-standards)
14. [Testing Standards](#14-testing-standards)
15. [Privacy & Security Standards](#15-privacy--security-standards)
16. [Performance Standards](#16-performance-standards)
17. [Logging Standards](#17-logging-standards)
18. [Build & Release Standards](#18-build--release-standards)
19. [Version Control Standards](#19-version-control-standards)
20. [Code Review Standards](#20-code-review-standards)
21. [Traceability Matrix](#21-traceability-matrix)
22. [Glossary](#22-glossary)
23. [References](#23-references)

---

## 1. Introduction

### 1.1 Purpose

This document establishes mandatory coding standards, conventions, and quality criteria for all MindfulScroll source code. Every developer, reviewer, and contributor must follow these standards to ensure consistency, maintainability, and alignment with the architecture defined in [SAD.md](SAD.md) and the technical designs in [TDD.md](TDD.md).

This document is the authoritative reference for:

- How code must be organized and structured
- Which patterns must be used (and which are prohibited)
- Quality gates that code must pass before merge

### 1.2 Scope

These standards apply to **all Kotlin source code** in the `com.rudy.mindfulscroll` package, including production code, test code, build configuration, and manifest declarations. XML layout files (overlay View) and Compose UI code are also covered.

### 1.3 Intended Audience

| Audience           | Usage                                                        |
| ------------------ | ------------------------------------------------------------ |
| Android developers | Primary reference during implementation                      |
| Code reviewers     | Checklist for pull request reviews                           |
| QA / Test engineers| Test structure and naming requirements                       |
| CI pipeline        | Automated enforcement rules                                  |

### 1.4 Enforcement

| Level       | Mechanism                                                    |
| ----------- | ------------------------------------------------------------ |
| Automated   | Android Lint, ktlint, Detekt (integrated in build pipeline) |
| Manual      | Code review against this document                            |
| Architectural | Module dependency checks (no cross-layer violations)       |

### 1.5 Conventions Used in This Document

- **MUST** / **MUST NOT**: Absolute requirement or prohibition.
- **SHOULD** / **SHOULD NOT**: Recommended practice with documented exceptions allowed.
- **MAY**: Optional practice left to developer judgment.

---

## 2. Project Configuration Standards

### 2.1 Build Environment

All developers MUST use the following versions. *Source: PER-001, ProjectPlan Section 5.*

| Component              | Version / Value                  |
| ---------------------- | -------------------------------- |
| Kotlin                 | 2.0.x (currently 2.0.21)        |
| Android Gradle Plugin  | 8.13.x                          |
| JVM Target             | 11                               |
| Compose BOM            | 2024.09.00                       |
| Min SDK                | 26 (Android 8.0 Oreo)           |
| Target SDK             | 36                               |
| Compile SDK            | 36                               |
| Gradle                 | Latest compatible with AGP 8.13  |

### 2.2 Dependency Rules

| Rule ID | Rule                                                         | Source       |
| ------- | ------------------------------------------------------------ | ------------ |
| DEP-01  | MUST NOT add any dependency that requires `android.permission.INTERNET` | NFR-011, ADR-009, Privacy.md |
| DEP-02  | MUST NOT add analytics, crash reporting, or telemetry SDKs   | ADR-009, C-01 |
| DEP-03  | MUST NOT add network libraries (OkHttp, Retrofit, Ktor, Volley) | Privacy.md Section 6 |
| DEP-04  | MUST NOT add advertising or tracking libraries               | Privacy.md Section 6 |
| DEP-05  | New dependencies MUST be reviewed against the prohibited dependencies list in [Privacy.md](Privacy.md) Section 6.3 | Privacy.md |
| DEP-06  | SHOULD prefer AndroidX / first-party libraries over third-party alternatives | ProjectPlan |
| DEP-07  | Google Mobile Services (GMS) MUST NOT be required at runtime  | PER-002      |

### 2.3 Approved Dependencies

| Category      | Library                          | Purpose                       |
| ------------- | -------------------------------- | ----------------------------- |
| UI            | Jetpack Compose (BOM)            | Declarative UI                |
| UI            | Material Design 3                | Design system components      |
| Navigation    | Jetpack Navigation Compose       | Screen navigation             |
| DI            | Hilt                             | Dependency injection          |
| Database      | Room                             | Local persistence             |
| Async         | Kotlin Coroutines + Flow         | Concurrency and reactive streams |
| Testing       | JUnit, MockK, Turbine            | Unit and ViewModel testing    |
| Testing       | AndroidX Test, Compose Test      | Instrumented and UI testing   |
| Lifecycle     | Jetpack Lifecycle                | ViewModel, lifecycle-aware components |

---

## 3. Architecture Standards

### 3.1 Architectural Pattern

MindfulScroll follows **Clean Architecture** with an additional Service layer. *Source: SAD Section 3, ADR-001.*

```
┌──────────────────────────────┐
│     Presentation Layer       │  Compose screens, ViewModels
├──────────────────────────────┤
│        Domain Layer          │  Models, repository interfaces, use cases
├──────────────────────────────┤
│         Data Layer           │  Room entities, DAOs, repository implementations
├──────────────────────────────┤
│       Service Layer          │  Foreground service, overlay, polling engine
└──────────────────────────────┘
```

### 3.2 Mandatory Architectural Rules

| Rule ID | Rule                                                         | Source                   |
| ------- | ------------------------------------------------------------ | ------------------------ |
| ARC-01  | **Domain layer MUST have zero Android framework dependencies** (pure Kotlin). The sole exception is the `Any?`-typed icon field in `InstalledApp`. | NFR-013.AC1, ADR-001     |
| ARC-02  | **Presentation layer MUST NOT access the Data layer directly.** All data access goes through Domain layer interfaces. | NFR-013.AC2, SAD R4      |
| ARC-03  | **Data layer MUST NOT depend on Presentation layer.**         | SAD R5                   |
| ARC-04  | **All repository access MUST go through Domain interfaces.** Concrete implementations reside in the Data layer. | NFR-013.AC3, ADR-001     |
| ARC-05  | **Dependency injection MUST be used throughout.** No manual instantiation of repositories, use cases, or ViewModels. | NFR-013.AC4, ADR-002     |
| ARC-06  | **Service layer MAY depend on Domain layer** but MUST NOT depend on Presentation layer. | SAD R6                   |
| ARC-07  | **Single-Activity architecture.** All screens are Compose destinations within `MainActivity`. | SAD Section 3, ADR-001   |
| ARC-08  | **MVVM pattern** for all Presentation components. ViewModels expose state; Compose screens observe and render. | SAD Section 3             |

### 3.3 Dependency Direction

```
Presentation  ──→  Domain  ←──  Data
                     ↑
               Service Layer
```

Dependencies MUST point inward toward the Domain layer. The Domain layer defines interfaces; outer layers provide implementations.

### 3.4 Supplementary Patterns

| Pattern              | Usage                                              | Source       |
| -------------------- | -------------------------------------------------- | ------------ |
| Repository           | Abstract data sources behind domain interfaces     | ARC-04       |
| State Machine        | Session lifecycle, timer visual state, service state | SRS Section 10, TDD Section 6 |
| Observer (Flow)      | Reactive data streams from DB to UI                | ADR-005      |
| Strategy             | Threshold presets (Light, Moderate, Strict, Custom) | TDD Section 7 |
| Singleton (DI)       | Database instance, system service wrappers         | SAD Section 13 |

---

## 4. Package Structure Standards

### 4.1 Top-Level Package

All source code resides under `com.rudy.mindfulscroll`. *Source: SAD Section 6.*

```
com.rudy.mindfulscroll/
├── di/                          # Hilt modules
│   ├── DatabaseModule.kt
│   ├── RepositoryModule.kt
│   ├── SystemModule.kt
│   └── UseCaseModule.kt
├── domain/                      # Pure Kotlin domain layer
│   ├── model/                   # Data classes, enums, sealed classes
│   ├── repository/              # Repository interfaces
│   └── usecase/                 # Use case classes
├── data/                        # Android-dependent data layer
│   ├── local/
│   │   ├── dao/                 # Room DAO interfaces
│   │   ├── entity/              # Room @Entity classes
│   │   └── MindfulScrollDatabase.kt
│   ├── repository/              # Repository implementations
│   └── system/                  # System API wrappers
│       ├── PackageManagerWrapper.kt
│       ├── UsageStatsWrapper.kt
│       └── PermissionChecker.kt
├── presentation/                # Compose UI + ViewModels
│   ├── MainActivity.kt
│   ├── home/
│   ├── appselection/
│   ├── threshold/
│   ├── onboarding/
│   ├── about/
│   ├── navigation/
│   ├── components/              # Shared Compose components
│   └── theme/                   # Material 3 theme
├── service/                     # Background service + overlay
│   ├── monitoring/
│   │   ├── MonitoringService.kt
│   │   ├── PollingEngine.kt
│   │   └── SessionManager.kt
│   ├── threshold/
│   │   └── ThresholdEngine.kt
│   ├── overlay/
│   │   ├── OverlayController.kt
│   │   ├── OverlayTimerView.kt
│   │   ├── OverlayAnimationManager.kt
│   │   ├── VisualStateProperties.kt
│   │   └── VisualStateConfig.kt
│   └── receiver/
│       ├── ScreenStateReceiver.kt
│       └── BootReceiver.kt
├── util/
│   ├── constants/
│   │   └── AppConstants.kt
│   └── extensions/              # Extension functions
│       └── TimeFormatExtensions.kt
└── MindfulScrollApp.kt          # Hilt @HiltAndroidApp
```

### 4.2 Package Dependency Rules

| Rule ID | Allowed Dependency                        | Prohibited Dependency                  |
| ------- | ----------------------------------------- | -------------------------------------- |
| PKG-01  | `presentation` → `domain`                | `presentation` → `data` (NEVER)       |
| PKG-02  | `data` → `domain`                        | `data` → `presentation` (NEVER)       |
| PKG-03  | `service` → `domain`                     | `service` → `presentation` (NEVER)    |
| PKG-04  | `service` → `data` (system wrappers only)| `domain` → `data` (NEVER)             |
| PKG-05  | `di` → all layers (wiring only)          | `domain` → `presentation` (NEVER)     |
| PKG-06  | `util` → no layer dependencies           | `domain` → `service` (NEVER)          |

### 4.3 File Organization Rules

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| FIL-01  | One top-level class per file. File name MUST match class name. |
| FIL-02  | Small closely related classes (e.g., `UiState` + `ViewModel`) MAY share a file if the state class is ≤15 lines. |
| FIL-03  | Extension functions MUST reside in `util/extensions/` and be named `<Type>Extensions.kt`. |
| FIL-04  | All constants MUST reside in `util/constants/AppConstants.kt`. No magic numbers in code. |
| FIL-05  | Enum classes used across layers MUST reside in `domain/model/`. |

---

## 5. Kotlin Language Conventions

### 5.1 General Kotlin Style

| Rule ID | Rule                                                         | Source       |
| ------- | ------------------------------------------------------------ | ------------ |
| KT-01   | Follow the [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) as the baseline. | TDD Section 2.1 |
| KT-02   | Use `val` over `var` wherever possible. Prefer immutability.  | TDD Section 2.1 |
| KT-03   | Domain models MUST be `data class` with `val` properties. Use `copy()` for modifications. | TDD Section 2.1 |
| KT-04   | Avoid nullable types in domain models. Nullable types are permitted only at system boundaries (data entities, system API wrappers). | TDD Section 2.1 |
| KT-05   | Use `when` expressions instead of `if-else` chains for 3+ branches. | — |
| KT-06   | Use expression body (`= expr`) for single-expression functions. | — |
| KT-07   | Use scope functions (`let`, `apply`, `also`, `run`, `with`) appropriately. Do not nest more than 2 levels of scope functions. | — |
| KT-08   | Use string templates (`"$var"`, `"${expr}"`) instead of concatenation. | — |
| KT-09   | Prefer `listOf()`, `mapOf()`, `setOf()` for immutable collections. Use mutable variants only when mutation is required. | — |
| KT-10   | Use trailing commas in multi-line declarations (parameter lists, enum entries, collection literals). | — |
| KT-11   | Maximum line length: **120 characters**. Break at a logical point. | — |
| KT-12   | Use 4-space indentation. No tabs.                            | — |

### 5.2 Type Safety

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| TS-01   | MUST NOT use `!!` (non-null assertion) in production code. Use safe calls (`?.`), `let`, or `requireNotNull` with a meaningful message. |
| TS-02   | Use `require()` for precondition validation (argument checks). Use `check()` for state validation. |
| TS-03   | Use sealed classes or sealed interfaces for representing finite state sets. |
| TS-04   | Use `@Suppress("DEPRECATION")` with a comment explaining the API version context when calling deprecated APIs needed for backward compatibility. |

### 5.3 Kotlin-Specific Idioms

| Pattern               | Preferred                                     | Avoid                               |
| --------------------- | --------------------------------------------- | ----------------------------------- |
| Null check + action   | `value?.let { use(it) }`                      | `if (value != null) { use(value) }` |
| Default value         | `value ?: default`                            | `if (value == null) default else value` |
| Type check + cast     | `value as? Type`                              | `if (value is Type) value as Type`  |
| Collection transform  | `.map { }`, `.filter { }`, `.associate { }`   | Manual for-loop with mutable list   |
| Empty check           | `.isEmpty()` / `.isNotEmpty()`                | `.size == 0` / `.size > 0`         |
| String check          | `.isNullOrEmpty()` / `.isNullOrBlank()`       | `str == null \|\| str == ""`       |

---

## 6. Naming Conventions

### 6.1 General Naming Rules

| Element              | Convention                  | Example                                |
| -------------------- | --------------------------- | -------------------------------------- |
| Classes / Interfaces | `PascalCase`                | `SessionManager`, `MonitoredAppRepository` |
| Functions            | `camelCase`                 | `onForegroundAppChanged()`, `getActive()` |
| Properties / Variables | `camelCase`               | `elapsedSeconds`, `isMonitoringActive` |
| Constants            | `SCREAMING_SNAKE_CASE`      | `POLL_INTERVAL_MS`, `DEFAULT_T1`       |
| Enum values          | `SCREAMING_SNAKE_CASE`      | `SessionState.ACTIVE`, `TimerVisualState.CALM` |
| Packages             | `lowercase`, no underscores | `com.rudy.mindfulscroll.domain.model`  |
| Type parameters      | Single uppercase letter     | `<T>`, `<R>`                           |
| Backing properties   | Prefix with `_`             | `_uiState` (private), `uiState` (public) |

### 6.2 Component-Specific Naming

| Component         | Naming Pattern                    | Example                              |
| ----------------- | --------------------------------- | ------------------------------------ |
| ViewModel         | `<Feature>ViewModel`              | `HomeViewModel`, `AppSelectionViewModel` |
| UI State          | `<Feature>UiState`                | `HomeUiState`, `AppSelectionUiState` |
| Compose Screen    | `<Feature>Screen`                 | `HomeScreen`, `ThresholdConfigScreen` |
| Repository (interface) | `<Entity>Repository`         | `MonitoredAppRepository`             |
| Repository (impl) | `<Entity>RepositoryImpl`          | `MonitoredAppRepositoryImpl`         |
| Use Case          | `<Verb><Noun>UseCase`             | `GetInstalledAppsUseCase`, `UpdateAppSelectionUseCase` |
| DAO               | `<Entity>Dao`                     | `MonitoredAppDao`, `UsageSessionDao` |
| Room Entity       | `<Name>Entity`                    | `MonitoredAppEntity`, `UsageSessionEntity` |
| Domain Model      | Plain name (no suffix)            | `MonitoredApp`, `UsageSession`       |
| Hilt Module       | `<Category>Module`                | `DatabaseModule`, `RepositoryModule` |
| Navigation Route  | `<Flow>/<screen_name>` (lowercase)| `"main/home"`, `"onboarding/welcome"` |
| Mapper extension  | `toDomain()` / `toEntity()`      | `MonitoredAppEntity.toDomain()`      |

### 6.3 Boolean Naming

Boolean properties and functions MUST use a clear affirmative prefix:

| Prefix   | Usage                    | Example                          |
| -------- | ------------------------ | -------------------------------- |
| `is`     | State query              | `isActive`, `isOverlayAttached`  |
| `has`    | Presence query           | `hasPermission`, `hasApps`       |
| `should` | Conditional logic        | `shouldShowOverlay()`            |
| `can`    | Capability query         | `canDrawOverlays()`              |

### 6.4 Test Naming

Test methods MUST use backtick-quoted descriptive names. *Source: TDD Section 16.*

```kotlin
// Pattern: `<condition or action> <expected result>`
@Test fun `monitored app detected from INACTIVE starts new session`()
@Test fun `600 seconds returns NOTICE`()
@Test fun `cooldown expiry ends session`()
```

---

## 7. Error Handling Standards

### 7.1 General Error Handling Rules

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| ERR-01  | **Service layer MUST never crash.** Wrap all system API calls in try-catch. | TDD Section 2.2    |
| ERR-02  | On non-fatal errors, **log at WARN level and continue operation.** | TDD Section 2.2    |
| ERR-03  | Use `Result<T>` or sealed class for operations that can fail at domain boundaries. | TDD Section 2.2    |
| ERR-04  | Use `CoroutineExceptionHandler` on service scope.            | TDD Section 2.2, ADR-010 |
| ERR-05  | Use `SupervisorJob` for child coroutine isolation — one child's failure MUST NOT cancel siblings. | ADR-010            |
| ERR-06  | MUST NOT use generic `catch (e: Exception)` without logging. Always log the exception. | —                  |
| ERR-07  | MUST NOT swallow exceptions silently. At minimum, log at WARN level. | —                  |
| ERR-08  | Try-catch blocks SHOULD be as narrow as possible. Do not wrap entire function bodies. | —                  |

### 7.2 Error Handling Matrix

All error scenarios defined in SRS (EH-001 through EH-008) MUST be handled per the following pattern. *Source: TDD Section 14.1.*

| Scenario           | Detection                         | Response                              |
| ------------------ | --------------------------------- | ------------------------------------- |
| App uninstalled    | `NameNotFoundException`           | Remove from DB silently               |
| Permission revoked | `PermissionChecker.checkAll()`    | Pause monitoring, show banner         |
| Service killed     | `START_STICKY` auto-restart       | Reload config from DB, resume         |
| Overlay failure    | Try-catch `WindowManager.addView` | Log error, continue without overlay   |
| UsageStats empty   | Null/empty result                 | Return null, no session action        |
| DB write failure   | Exception from Room DAO           | Log error, continue in-memory         |
| Device rotation    | Configuration change              | Recalculate position from Y-percent   |
| Split-screen       | UsageStats reports focused app    | Monitor focused app normally          |

### 7.3 Exception Patterns by Layer

| Layer        | Pattern                                                      |
| ------------ | ------------------------------------------------------------ |
| Domain       | Functions return `Result<T>` or throw `IllegalArgumentException` for invalid input. |
| Data         | Catch all `Exception` from Room/system APIs. Map to domain-friendly errors. |
| Service      | `try-catch` every system API call. Log and degrade gracefully. |
| Presentation | ViewModels catch and map exceptions to error UI states.      |

---

## 8. Coroutine & Flow Standards

### 8.1 Coroutine Rules

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| COR-01  | **Never use `GlobalScope`.** Always use structured concurrency (`viewModelScope`, `lifecycleScope`, or a service-owned scope). | TDD Section 2.1    |
| COR-02  | Use `Dispatchers.IO` for database and system API calls.      | —                  |
| COR-03  | Use `Dispatchers.Main` for UI updates (default for `viewModelScope`). | —                  |
| COR-04  | Use `Dispatchers.Default` for CPU-intensive computation only. | —                  |
| COR-05  | Every coroutine scope created in a component MUST be canceled in that component's `onDestroy()` / `onCleared()`. | RA-022             |
| COR-06  | Use `withContext()` to switch dispatchers within a coroutine rather than launching a new coroutine. | —                  |
| COR-07  | The sole exception to COR-01 is `BootReceiver.onReceive()` where a manual `CoroutineScope(Dispatchers.IO)` is used with `goAsync()`. | TDD Section 8.3    |

### 8.2 Flow Rules

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| FLW-01  | Use cold `Flow` for database streams (Room DAO return type). | TDD Section 2.1, ADR-005 |
| FLW-02  | Use `StateFlow` for mutable state exposed to observers.      | ADR-005            |
| FLW-03  | Use `MutableStateFlow` as private backing field; expose as `StateFlow` via `.asStateFlow()`. | TDD Section 10.1   |
| FLW-04  | ViewModels MUST expose a single `StateFlow<UiState>` per screen. | TDD Section 10.1   |
| FLW-05  | Use `StateFlow.update { }` for thread-safe state mutations from multiple coroutines. | —                  |
| FLW-06  | Collect flows using `collectAsStateWithLifecycle()` in Compose screens. | —                  |
| FLW-07  | **Never** use `LiveData`. StateFlow is the standard. | ADR-005            |

### 8.3 Scope Ownership

| Component          | Scope Source                     | Job Type          | Cancellation        |
| ------------------ | -------------------------------- | ----------------- | ------------------- |
| ViewModel          | `viewModelScope`                 | Regular Job       | Auto on `onCleared()` |
| MonitoringService  | Manual `CoroutineScope`          | `SupervisorJob`   | `scope.cancel()` in `onDestroy()` |
| BootReceiver       | Manual `CoroutineScope`          | Regular Job       | `pendingResult.finish()` |
| Compose screens    | `lifecycleScope` / `LaunchedEffect` | Regular Job    | Auto on lifecycle   |

---

## 9. Dependency Injection Standards

### 9.1 Framework

Hilt is the mandatory DI framework. *Source: ADR-002.*

### 9.2 Module Organization

| Module              | Install In              | Binding Style                | Purpose                          |
| ------------------- | ----------------------- | ---------------------------- | -------------------------------- |
| `DatabaseModule`    | `SingletonComponent`    | `@Provides`                  | Room database instance and DAOs  |
| `RepositoryModule`  | `SingletonComponent`    | `@Binds` (abstract)          | Repository interface → impl      |
| `SystemModule`      | `SingletonComponent`    | `@Provides`                  | Android system services          |
| `UseCaseModule`     | `SingletonComponent`    | `@Provides`                  | Use case instances               |

### 9.3 Scope Rules

| Scope               | Hilt Annotation         | Usage                                |
| -------------------- | ----------------------- | ------------------------------------ |
| Singleton            | `@Singleton`            | Database, system service wrappers    |
| ViewModel-scoped     | `@ViewModelScoped`      | Not currently used (reserved)        |
| Unscoped             | (none)                  | Use cases, repository implementations — new instance per injection |

### 9.4 DI Rules

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| DI-01   | `@HiltAndroidApp` on the `Application` class (`MindfulScrollApp`). |
| DI-02   | `@AndroidEntryPoint` on `MainActivity`.                       |
| DI-03   | `@HiltViewModel` + `@Inject constructor` on all ViewModels.  |
| DI-04   | Use `@Binds` (abstract module) for interface-to-implementation bindings. |
| DI-05   | Use `@Provides` (object module) for concrete instance creation. |
| DI-06   | MUST NOT use `@Inject` field injection in Kotlin (use constructor injection). |
| DI-07   | Service components that cannot use constructor injection MUST use `@AndroidEntryPoint` or manual Hilt entry points. |

---

## 10. Data Layer Standards

### 10.1 Room Database Rules

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| DB-01   | Single database class: `MindfulScrollDatabase` with all DAOs. | DbSchema.md        |
| DB-02   | Database name: `"mindfulscroll.db"`.                         | TDD Section 13.1   |
| DB-03   | Enable **WAL (Write-Ahead Logging)** mode for concurrent read/write. | NFR-008.AC1        |
| DB-04   | All multi-step write operations MUST use `@Transaction`.      | NFR-008.AC2        |
| DB-05   | **Never use `fallbackToDestructiveMigration()`.** All schema changes require proper migrations. | NFR-008.AC3, RA-018 |
| DB-06   | Export schemas for migration testing: `exportSchema = true` in `@Database`. | DbSchema.md Section 13 |
| DB-07   | Database instance MUST be `@Singleton` in Hilt.               | TDD Section 13.1   |

### 10.2 Entity Standards

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| ENT-01  | Entity classes reside in `data/local/entity/` and are suffixed with `Entity`. |
| ENT-02  | Entity classes MUST NOT be used outside the Data layer. Map to domain models at the repository boundary. |
| ENT-03  | Use `@PrimaryKey(autoGenerate = true)` for auto-incrementing IDs. |
| ENT-04  | Define indices in `@Entity(indices = [...])` for columns used in WHERE clauses. |
| ENT-05  | Nullable fields in entities are allowed (system boundary). Map to non-null domain defaults in mappers. |

### 10.3 DAO Standards

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| DAO-01  | DAO interfaces reside in `data/local/dao/`.                  |
| DAO-02  | Query methods returning reactive data MUST return `Flow<T>`. |
| DAO-03  | One-shot queries MUST be `suspend fun`.                      |
| DAO-04  | Use `@Insert(onConflict = OnConflictStrategy.REPLACE)` for upsert operations. |
| DAO-05  | Complex queries with multiple operations MUST be annotated with `@Transaction`. |

### 10.4 Entity-Domain Mapping

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| MAP-01  | Define `toDomain()` extension function on Entity classes.     |
| MAP-02  | Define `toEntity()` extension function on Domain model classes. |
| MAP-03  | Mappers reside in `data/repository/` alongside the repository implementation or in a dedicated `data/mapper/` file. |
| MAP-04  | Mapping MUST be bidirectional and lossless for all persisted fields. |

### 10.5 Repository Implementation Standards

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| REP-01  | Repository implementations reside in `data/repository/`.      |
| REP-02  | Repository implementations are annotated with `@Inject constructor`. |
| REP-03  | All database access MUST go through `Dispatchers.IO`.         |
| REP-04  | Repository methods MUST map entities to domain models before returning. |
| REP-05  | System API wrappers (`PackageManagerWrapper`, `UsageStatsWrapper`) implement domain repository interfaces. |

---

## 11. Domain Layer Standards

### 11.1 Domain Model Rules

| Rule ID | Rule                                                         | Source            |
| ------- | ------------------------------------------------------------ | ----------------- |
| DOM-01  | Domain models reside in `domain/model/` as `data class` with `val` properties. | TDD Section 3.1   |
| DOM-02  | Domain models MUST have zero Android framework imports.       | ARC-01            |
| DOM-03  | Use `copy()` for creating modified instances. MUST NOT mutate state. | TDD Section 2.1   |
| DOM-04  | Computed properties (e.g., `threshold1Seconds`) are allowed as `get()` accessors. | TDD Section 3.1.3 |
| DOM-05  | Companion objects with preset constants are allowed (e.g., `ThresholdConfig.MODERATE`). | TDD Section 3.1.3 |

### 11.2 Repository Interface Rules

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| INT-01  | Repository interfaces reside in `domain/repository/`.         |
| INT-02  | Interface methods use domain model types only (never entities). |
| INT-03  | Reactive methods return `Flow<T>`. One-shot methods are `suspend fun`. |

### 11.3 Use Case Rules

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| UC-01   | Use cases reside in `domain/usecase/`.                        |
| UC-02   | Each use case has a **single public function**: `operator fun invoke(...)` or `suspend operator fun invoke(...)`. |
| UC-03   | Use cases orchestrate calls to one or more repositories. They contain business logic but no Android framework code. |
| UC-04   | Use cases are named `<Verb><Noun>UseCase` (e.g., `GetInstalledAppsUseCase`). |
| UC-05   | Use cases are unscoped in Hilt (new instance per injection).  |

### 11.4 State Enums

All state enums reside in `domain/model/` and follow this pattern:

```kotlin
// domain/model/SessionState.kt
enum class SessionState {
    INACTIVE, ACTIVE, COOLDOWN, ENDED
}

// domain/model/TimerVisualState.kt
enum class TimerVisualState {
    CALM, NOTICE, ALERT, URGENT, THROB
}
```

---

## 12. Presentation Layer Standards

### 12.1 ViewModel Standards

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| VM-01   | All ViewModels MUST use `@HiltViewModel` + `@Inject constructor`. | DI-03              |
| VM-02   | Each ViewModel MUST expose a single `StateFlow<UiState>`.     | FLW-04, TDD Section 10.1 |
| VM-03   | UiState MUST be a `data class` with default values for all fields. | TDD Section 10.1   |
| VM-04   | UI events are handled through public functions on the ViewModel (not through event channels). | —                  |
| VM-05   | ViewModels MUST NOT hold references to `Context`, `Activity`, or `View`. | —                  |
| VM-06   | When `Context` is needed (e.g., starting a service), pass it as a function parameter. | TDD Section 10.1.1 |

### 12.2 Compose Standards

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| CMP-01  | Screen-level composables are named `<Feature>Screen` and take a ViewModel as parameter (retrieved via `hiltViewModel()`). |
| CMP-02  | Screen composables SHOULD be stateless: receive state as parameter, emit events as lambdas. |
| CMP-03  | Use `collectAsStateWithLifecycle()` to observe StateFlow in Compose. |
| CMP-04  | Use `@Preview` annotations for all reusable components with sample data. |
| CMP-05  | Shared UI components reside in `presentation/components/`.    |
| CMP-06  | Use Material 3 components from the design system. Custom styling through theme overrides, not hardcoded values. |
| CMP-07  | All user-visible strings MUST be in `res/values/strings.xml` (no hardcoded strings in Compose). |
| CMP-08  | Follow the design system defined in [UIUXspec.md](UIUXspec.md): typography scale, color tokens, spacing system, elevation levels. |

### 12.3 Navigation Standards

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| NAV-01  | All routes are defined in the `Route` sealed class in `presentation/navigation/Routes.kt`. | TDD Section 12.1   |
| NAV-02  | Navigation is handled through Jetpack Navigation Compose in a single `AppNavHost`. | TDD Section 12.2   |
| NAV-03  | Conditional start destination is determined by `determineStartDestination()`. | TDD Section 12.3   |
| NAV-04  | When navigating after onboarding, use `popUpTo(inclusive = true)` to clear the back stack. | TDD Section 12.2   |
| NAV-05  | Screen composables receive navigation callbacks as lambda parameters (e.g., `onBack`, `onNavigateTo`). | —                  |

### 12.4 Accessibility Standards

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| ACC-01  | All interactive elements MUST have content descriptions.      | UIUXspec.md Section 14 |
| ACC-02  | Support minimum touch target size of 48dp.                    | UIUXspec.md Section 14 |
| ACC-03  | Maintain a minimum contrast ratio of 4.5:1 for text.          | UIUXspec.md Section 14 |
| ACC-04  | Support dynamic font scaling (do not use fixed `sp` values that break at large scales). | UIUXspec.md Section 14 |

---

## 13. Service Layer Standards

### 13.1 Foreground Service Standards

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| SVC-01  | `MonitoringService` MUST be a foreground service with `startForeground()` called within 5 seconds of `onCreate()`. | FR-035, FR-036     |
| SVC-02  | Use `START_STICKY` return from `onStartCommand()` for automatic restart after kill. | FR-037, RA-002     |
| SVC-03  | Foreground service type MUST be `specialUse` with a declaration justification in the manifest. | FR-035.AC6, RA-021 |
| SVC-04  | Service MUST clean up all resources in `onDestroy()`: cancel scope, remove overlay, unregister receivers. | RA-022, RA-011     |
| SVC-05  | Service MUST NOT perform heavy initialization on the main thread. Use coroutines for loading config from DB. | —                  |
| SVC-06  | Notification channel MUST use `IMPORTANCE_LOW` with no badge and no sound. | TDD Section 8.1    |

### 13.2 Overlay Standards

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| OVL-01  | Overlay MUST use the Android View system (not Compose). `WindowManager` requires `View` objects. | ADR-003            |
| OVL-02  | Overlay MUST use `TYPE_APPLICATION_OVERLAY` window type.       | FR-019             |
| OVL-03  | Overlay MUST use `FLAG_NOT_FOCUSABLE` to avoid stealing input from host app. | FR-019, RA-010     |
| OVL-04  | Check `Settings.canDrawOverlays()` before every `addView()` call. | TDD Section 9.1    |
| OVL-05  | Call `cleanupStaleOverlay()` on service startup to remove ghost overlays. | RA-011             |
| OVL-06  | Use a flat view hierarchy — single `OverlayTimerView` with `onDraw()` for minimal render cost. | NFR-004, TDD Section 15.2 |
| OVL-07  | Invalidate only on time update (1/second) or visual state change. | NFR-004.AC2        |
| OVL-08  | All `Paint` objects MUST be created once and reused. Do not allocate in `onDraw()`. | NFR-004            |
| OVL-09  | Use `setLayerType(LAYER_TYPE_HARDWARE, null)` for hardware acceleration. | NFR-004.AC1        |

### 13.3 Receiver Standards

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| RCV-01  | `ScreenStateReceiver` is registered/unregistered dynamically by the service (not in manifest). |
| RCV-02  | `BootReceiver` MUST use `goAsync()` for coroutine work within the 10-second `onReceive()` window. |
| RCV-03  | `BootReceiver` MUST check the `monitoring_active` preference before starting the service. |

---

## 14. Testing Standards

### 14.1 Coverage Targets

| Component                  | Test Type      | Coverage Target | Source       |
| -------------------------- | -------------- | --------------- | ------------ |
| Domain layer (total)       | Unit           | ≥ 80%          | NFR-014      |
| SessionManager (state machine) | Unit       | 100% of transitions | NFR-014.AC1 |
| ThresholdEngine            | Unit           | 100% of states + boundaries | NFR-014.AC2 |
| Use cases                  | Unit           | All validation paths | NFR-014  |
| Repository implementations | Instrumented   | All CRUD operations | —         |
| ViewModels                 | Unit           | Key state transitions | —       |
| Compose screens            | UI             | Navigation + key interactions | — |
| Overlay                    | Manual         | Visual verification on 10 host apps | — |
| OEM compatibility          | Manual         | 8-hour endurance on Pixel, Samsung, Xiaomi | RA-002 |

### 14.2 Test Organization

```
src/
├── test/                        # Unit tests (JVM)
│   └── com/rudy/mindfulscroll/
│       ├── domain/usecase/
│       ├── service/monitoring/   # SessionManagerTest, etc.
│       ├── service/threshold/    # ThresholdEngineTest
│       └── presentation/        # ViewModel tests
└── androidTest/                 # Instrumented tests (device/emulator)
    └── com/rudy/mindfulscroll/
        ├── data/local/dao/      # DAO tests with in-memory DB
        └── presentation/        # Compose UI tests
```

### 14.3 Test Naming

All tests MUST use backtick-quoted descriptive names (*source: TDD Section 16*):

```kotlin
@Test fun `monitored app detected from INACTIVE starts new session`()
@Test fun `600 seconds returns NOTICE`()
@Test fun `cooldown expiry ends session`()
```

### 14.4 Test Patterns

| Pattern                | Usage                                             |
| ---------------------- | ------------------------------------------------- |
| **Arrange-Act-Assert** | Standard structure for all unit tests.            |
| **MockK**              | Mock dependencies in unit tests.                  |
| **Turbine**            | Test `StateFlow` and `Flow` emissions in ViewModels. |
| **Room in-memory DB**  | Test DAOs without touching disk.                  |
| **Compose Test Rule**  | Test Compose UI with `createComposeRule()`.       |

### 14.5 Test Rules

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| TST-01  | Every new feature branch MUST include tests for new logic.    |
| TST-02  | Tests MUST be deterministic — no reliance on timing, network, or device state. |
| TST-03  | Use `TestCoroutineDispatcher` / `runTest` for coroutine testing. |
| TST-04  | Mock all external dependencies. Tests MUST NOT call real system APIs. |
| TST-05  | State machine tests MUST cover every transition in the state table (T1–T9). |
| TST-06  | Threshold engine tests MUST cover all boundary values (exact threshold, one below, one above). |
| TST-07  | Test class names MUST match the class under test with `Test` suffix (e.g., `SessionManagerTest`). |

---

## 15. Privacy & Security Standards

### 15.1 Zero-Network Architecture

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| PRI-01  | `android.permission.INTERNET` MUST NEVER appear in the manifest. | NFR-011.AC1, C-01  |
| PRI-02  | No network library MUST be present in the dependency graph.   | NFR-011.AC2, Privacy.md |
| PRI-03  | CI pipeline SHOULD include a static analysis step to verify no INTERNET permission or network library is added. | NFR-011.AC3        |
| PRI-04  | No analytics, crash reporting, or telemetry SDK.              | ADR-009            |

### 15.2 Data Protection

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| SEC-01  | All data stored in app-private internal storage only.         | NFR-012.AC1        |
| SEC-02  | MUST NOT write to external storage or shared directories.     | NFR-012.AC2        |
| SEC-03  | MUST NOT export ContentProvider.                              | NFR-012.AC3        |
| SEC-04  | Only `MainActivity` (launcher) is exported. All other components: `exported="false"`. | NFR-012.AC4        |
| SEC-05  | `android:allowBackup="false"` in manifest.                    | NFR-012.AC5        |
| SEC-06  | Usage session data MUST be retained for 90 days maximum. Implement retention cleanup. | D-005, DR-005      |
| SEC-07  | Store only package names (not app content, screenshots, or keystrokes). | Privacy.md Section 3 |

### 15.3 Permission Handling

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| PRM-01  | Declare only the minimum required permissions (see manifest in TDD Section 17.1). |
| PRM-02  | Never request permissions silently. Always explain the purpose to the user first (onboarding flow). |
| PRM-03  | Handle permission revocation gracefully — pause monitoring, show recovery UI. |
| PRM-04  | Check permissions on every `Activity.onResume()`.             |

### 15.4 Prohibited Dependencies Checklist

Before adding any new dependency, verify it is **not** on this list. *Source: [Privacy.md](Privacy.md) Section 6.3.*

| Category             | Prohibited Examples                                     |
| -------------------- | ------------------------------------------------------- |
| Network              | OkHttp, Retrofit, Ktor, Volley, Fuel                   |
| Analytics            | Firebase Analytics, Mixpanel, Amplitude                 |
| Crash Reporting      | Firebase Crashlytics, Sentry, Bugsnag                   |
| Advertising          | Google AdMob, Facebook Ads SDK, Unity Ads               |
| Tracking             | Adjust, AppsFlyer, Branch                               |
| Cloud Storage        | Firebase Storage, AWS SDK                               |
| Remote Config        | Firebase Remote Config                                  |
| Any SDK requiring INTERNET | (check transitive dependencies with `./gradlew dependencies`) |

---

## 16. Performance Standards

### 16.1 Startup Performance

| Metric                  | Target                | Source       |
| ----------------------- | --------------------- | ------------ |
| Cold start to interactive | < 2 seconds          | NFR-001      |
| App size (APK)          | < 20 MB               | PER-002      |
| Database size           | < 1 MB (typical use)  | PER-002      |

### 16.2 Runtime Performance

| Metric                  | Target                | Source       |
| ----------------------- | --------------------- | ------------ |
| Polling cycle cost      | < 10ms per cycle      | TDD Section 15.1 |
| Overlay render          | < 2ms per frame       | NFR-004      |
| Memory footprint        | Minimize; no leaks    | NFR-003, RA-022 |
| Battery impact          | < 3% per hour         | NFR-002      |

### 16.3 Performance Rules

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| PRF-01  | Monitored app set MUST be cached in a `HashSet<String>` for O(1) lookup in the polling hot path. |
| PRF-02  | Overlay uses a single flat `View` with `onDraw()` — no nested `ViewGroup`. |
| PRF-03  | Overlay MUST invalidate at most once per second during normal operation. |
| PRF-04  | Reuse all `Paint`, `Rect`, and other drawing objects — never allocate in `onDraw()`. |
| PRF-05  | Use `elapsedRealtime()` for timing (not `System.currentTimeMillis()`), as it survives deep sleep. |
| PRF-06  | Polling skips queries when screen is off (battery optimization). |
| PRF-07  | Database operations MUST always run on `Dispatchers.IO`. Never block main thread. |

---

## 17. Logging Standards

### 17.1 Log Levels

| Level   | Usage                                                        | Stripped in Release |
| ------- | ------------------------------------------------------------ | ------------------- |
| `Log.v` | Extremely verbose output (flow tracing, per-poll data)       | Yes                 |
| `Log.d` | Development debugging (state transitions, config loads)      | Yes                 |
| `Log.i` | Informational milestones (service start/stop, session end)   | Yes                 |
| `Log.w` | Recoverable errors (failed overlay add, empty UsageStats)    | No                  |
| `Log.e` | Unexpected errors (DB failure, uncaught exceptions)          | No                  |

### 17.2 Logging Rules

| Rule ID | Rule                                                         | Source             |
| ------- | ------------------------------------------------------------ | ------------------ |
| LOG-01  | Use a `TAG` constant (`private const val TAG = "<ClassName>"`) in every class that logs. | SAD Section 19.1   |
| LOG-02  | Debug-level and below (`d`, `v`, `i`) are stripped in release builds via ProGuard rules. | TDD Section 17.3   |
| LOG-03  | MUST NOT log user-identifiable information, app names, or session durations at WARN or ERROR level. | Privacy.md         |
| LOG-04  | MUST NOT log full exception stack traces at INFO or below in production scenarios. | —                  |
| LOG-05  | Service-layer classes SHOULD log state transitions at DEBUG level for troubleshooting. | —                  |

### 17.3 ProGuard Log Stripping

The following rule in `proguard-rules.pro` strips debug/verbose/info logs from release builds:

```proguard
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
    public static int i(...);
}
```

---

## 18. Build & Release Standards

### 18.1 Build Variants

| Variant   | `minifyEnabled` | `debuggable` | Signing          | ProGuard | Logs            |
| --------- | --------------- | ------------ | ---------------- | -------- | --------------- |
| `debug`   | false           | true         | Debug keystore   | Off      | Full (d/v/i/w/e)|
| `release` | true            | false        | Release keystore | R8       | Critical (w/e)  |

### 18.2 Manifest Standards

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| MAN-01  | Declare all permissions at the top of the manifest with comments grouping by purpose. |
| MAN-02  | Include `<!-- NEVER: android.permission.INTERNET -->` comment as a reminder. |
| MAN-03  | `android:allowBackup="false"` on the `<application>` tag.    |
| MAN-04  | `android:exported="true"` ONLY on `MainActivity` and `BootReceiver`. All others `exported="false"`. |
| MAN-05  | Foreground service type: `android:foregroundServiceType="specialUse"` with property justification. |
| MAN-06  | Use `tools:ignore="ProtectedPermissions"` on `PACKAGE_USAGE_STATS`. |

### 18.3 ProGuard / R8 Standards

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| PGD-01  | Keep all Room entities, DAOs, and database classes.           |
| PGD-02  | Keep all Hilt-related classes.                                |
| PGD-03  | Keep domain models (used in Room entity mapping).             |
| PGD-04  | Strip debug logs (see Section 17.3).                          |
| PGD-05  | Test release builds with minification enabled before every release. |

### 18.4 Resource Standards

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| RES-01  | All user-visible strings in `res/values/strings.xml`.         |
| RES-02  | Use dimension resources (`res/values/dimens.xml`) for reusable dimensions. |
| RES-03  | Use color tokens from Material 3 theme. Hardcoded color values allowed only in `VisualStateConfig` (overlay colors not tied to theme). |
| RES-04  | Vector drawables preferred over bitmap assets for icons.      |

---

## 19. Version Control Standards

### 19.1 Branch Strategy

| Branch              | Purpose                              | Protected |
| ------------------- | ------------------------------------ | --------- |
| `master`            | Production-ready code                | Yes       |
| `develop`           | Integration branch                   | Yes       |
| `feature/<name>`    | Feature development                  | No        |
| `bugfix/<name>`     | Bug fixes                            | No        |
| `release/<version>` | Release preparation                  | No        |

### 19.2 Commit Standards

| Rule ID | Rule                                                         |
| ------- | ------------------------------------------------------------ |
| GIT-01  | Use conventional commits: `type(scope): description`. Types: `feat`, `fix`, `refactor`, `test`, `docs`, `chore`, `style`, `perf`. |
| GIT-02  | Commit messages MUST be in imperative mood (e.g., "Add session state machine", not "Added" or "Adds"). |
| GIT-03  | Commits SHOULD be atomic — one logical change per commit.    |
| GIT-04  | MUST NOT commit generated files, IDE-specific files (except shared run configs), or `local.properties`. |
| GIT-05  | MUST NOT commit secrets, API keys, or signing keystores.     |

### 19.3 .gitignore Requirements

The `.gitignore` MUST include at minimum:

```
*.iml
.gradle/
local.properties
.idea/caches/
.idea/libraries/
.idea/workspace.xml
.idea/navEditor.xml
build/
captures/
.externalNativeBuild/
.cxx/
*.apk
*.aab
*.jks
*.keystore
```

---

## 20. Code Review Standards

### 20.1 Review Checklist

Every pull request MUST be checked against the following criteria:

| Category         | Check                                                        |
| ---------------- | ------------------------------------------------------------ |
| Architecture     | No cross-layer dependency violations (ARC-01 through ARC-08) |
| Architecture     | New code placed in the correct package per Section 4         |
| Naming           | Follows naming conventions in Section 6                      |
| Error Handling   | Service layer code wrapped in try-catch (ERR-01)             |
| Error Handling   | No silent exception swallowing (ERR-07)                      |
| Coroutines       | No `GlobalScope` usage (COR-01)                              |
| Coroutines       | Scopes properly canceled (COR-05)                            |
| Flow             | No `LiveData` (FLW-07)                                       |
| Privacy          | No INTERNET permission or network dependency added (PRI-01, PRI-02) |
| Privacy          | No logging of user data at WARN/ERROR (LOG-03)               |
| Performance      | No allocations in `onDraw()` (PRF-04)                        |
| Performance      | DB operations on IO dispatcher (PRF-07)                      |
| Testing          | New logic has corresponding tests (TST-01)                   |
| Type Safety      | No `!!` in production code (TS-01)                           |
| Constants        | No magic numbers — values in `AppConstants` (FIL-04)         |
| Strings          | No hardcoded user-visible strings (CMP-07)                   |

### 20.2 Review Process

| Step | Action                                                       |
| ---- | ------------------------------------------------------------ |
| 1    | Developer creates PR against `develop` branch.               |
| 2    | CI pipeline runs lint, tests, and build checks.              |
| 3    | At least one reviewer reviews against the checklist above.   |
| 4    | All comments must be resolved before merge.                  |
| 5    | Squash merge preferred for feature branches.                 |

---

## 21. Traceability Matrix

### 21.1 SRS Requirements → Coding Standards

| SRS Requirement | Coding Standard Sections      | Key Rules                           |
| --------------- | ----------------------------- | ----------------------------------- |
| NFR-008         | 10 (Data Layer)               | DB-03, DB-04, DB-05                 |
| NFR-011         | 15 (Privacy)                  | PRI-01, PRI-02, PRI-03             |
| NFR-012         | 15 (Privacy)                  | SEC-01 through SEC-07               |
| NFR-013         | 3 (Architecture), 4 (Package) | ARC-01 through ARC-08, PKG-01 through PKG-06 |
| NFR-014         | 14 (Testing)                  | Coverage targets, TST-01 through TST-07 |
| NFR-004         | 13.2 (Overlay), 16 (Performance) | OVL-06 through OVL-09, PRF-02 through PRF-04 |
| PER-001         | 2 (Configuration)             | Build environment table              |
| PER-002         | 2 (Configuration), 16 (Performance) | DEP-07, startup/size targets   |

### 21.2 ADRs → Coding Standards

| ADR     | Decision                               | Enforced In         |
| ------- | -------------------------------------- | ------------------- |
| ADR-001 | Clean Architecture                     | ARC-01 through ARC-08 |
| ADR-002 | Hilt for DI                            | Section 9 (all DI rules) |
| ADR-003 | View-based overlay (not Compose)       | OVL-01              |
| ADR-004 | Coroutine-based polling                | COR-01 through COR-07 |
| ADR-005 | StateFlow over LiveData                | FLW-07              |
| ADR-006 | Room for all persistence               | Section 10 (all DB rules) |
| ADR-007 | `elapsedRealtime()` for timing         | PRF-05              |
| ADR-008 | Single notification channel            | SVC-06              |
| ADR-009 | No crash reporting SDK                 | PRI-04, DEP-02      |
| ADR-010 | SupervisorJob in service               | ERR-05              |

### 21.3 Risks → Coding Standards

| Risk    | Description                    | Mitigating Rules                     |
| ------- | ------------------------------ | ------------------------------------ |
| RA-002  | OEM battery optimization kill  | SVC-02, SVC-04, RCV-02, RCV-03      |
| RA-010  | Touch interference             | OVL-03                               |
| RA-011  | Ghost overlay                  | OVL-05, SVC-04                       |
| RA-012  | State machine errors           | TST-05                               |
| RA-014  | Overlay jank                   | OVL-06, OVL-07, PRF-02, PRF-04      |
| RA-018  | Migration failures             | DB-05, DB-06                         |
| RA-022  | Memory leak                    | COR-05, SVC-04                       |

---

## 22. Glossary

All terms from PRD (Section 17), SRS (Section 14), SAD (Section 23), and TDD (Section 19) apply. Additional terms specific to this document:

| Term                        | Definition                                                    |
| --------------------------- | ------------------------------------------------------------- |
| **Coding Standard Rule**    | A numbered rule (e.g., ARC-01) that is enforceable during code review or by automated tooling. |
| **MUST / MUST NOT**         | Absolute requirement or prohibition per RFC 2119.             |
| **SHOULD / SHOULD NOT**     | Recommended practice; exceptions require documented justification. |
| **MAY**                     | Optional practice left to developer judgment.                 |
| **Cross-layer violation**   | Code in one architectural layer directly referencing types from a layer it should not depend on. |
| **Hot path**                | Code executed every polling cycle (~2 seconds). Must be optimized for minimal CPU/memory. |
| **Ghost overlay**           | A stale overlay view left attached to WindowManager by a previous service instance. |
| **Backing property**        | A private `MutableStateFlow` prefixed with `_` that backs a public read-only `StateFlow`. |

---

## 23. References

| Reference                                 | Relevance                                          |
| ----------------------------------------- | -------------------------------------------------- |
| [PRD.md](PRD.md)                          | Product constraints and principles                 |
| [SRS.md](SRS.md)                          | Functional and non-functional requirements         |
| [ProjectPlan.md](ProjectPlan.md)          | Build environment, phase plan                      |
| [SAD.md](SAD.md)                          | Architecture, package structure, ADRs              |
| [TDD.md](TDD.md)                         | Implementation conventions, error handling, testing |
| [DbSchema.md](DbSchema.md)               | Database schema, entity design                     |
| [UIUXspec.md](UIUXspec.md)               | Design system, accessibility standards             |
| [Privacy.md](Privacy.md)                  | Privacy rules, prohibited dependencies             |
| [RiskAssessment.md](RiskAssessment.md)    | Risk mitigations affecting code patterns           |
| [Kotlin Coding Conventions](https://kotlinlang.org/docs/coding-conventions.html) | Baseline Kotlin style guide |
| [Android Kotlin Style Guide](https://developer.android.com/kotlin/style-guide) | Android-specific Kotlin style |
| [Material Design 3 Guidelines](https://m3.material.io/) | Design system reference |

---

*End of Document*
