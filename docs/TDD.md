# Technical Design Document (TDD)

## MindfulScroll — Digital Awareness Companion

---

### Document Control

| Field              | Value                                            |
| ------------------ | ------------------------------------------------ |
| **Document Title** | MindfulScroll Technical Design Document           |
| **Version**        | 1.0                                              |
| **Status**         | Draft                                            |
| **Created**        | 2026-02-07                                       |
| **Last Updated**   | 2026-02-07                                       |
| **Author**         | Rudy                                             |
| **Reviewers**      | —                                                |
| **Approved By**    | —                                                |

### Revision History

| Version | Date       | Author | Changes       |
| ------- | ---------- | ------ | ------------- |
| 1.0     | 2026-02-07 | Rudy   | Initial draft |

### Parent Documents

| Document                              | Version | Relationship                                              |
| ------------------------------------- | ------- | --------------------------------------------------------- |
| [PRD.md](PRD.md)                      | 1.0     | Product definition — features, principles, scope          |
| [SRS.md](SRS.md)                      | 1.0     | Technical requirements — FR, NFR, data model, interfaces  |
| [ProjectPlan.md](ProjectPlan.md)      | 1.0     | Execution plan — phases, WBS, schedule, milestones        |
| [SAD.md](SAD.md)                      | 1.0     | System architecture — layers, components, patterns        |
| [RiskAssessment.md](RiskAssessment.md)| 1.0     | Risk register — architectural risk mitigations            |

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Design Principles & Conventions](#2-design-principles--conventions)
3. [Domain Model Design](#3-domain-model-design)
4. [Data Layer Design](#4-data-layer-design)
5. [Polling Engine Design](#5-polling-engine-design)
6. [Session State Machine Design](#6-session-state-machine-design)
7. [Threshold Engine Design](#7-threshold-engine-design)
8. [Monitoring Service Design](#8-monitoring-service-design)
9. [Overlay System Design](#9-overlay-system-design)
10. [Presentation Layer Design](#10-presentation-layer-design)
11. [Permission Management Design](#11-permission-management-design)
12. [Navigation Design](#12-navigation-design)
13. [Dependency Injection Design](#13-dependency-injection-design)
14. [Error Handling Design](#14-error-handling-design)
15. [Performance Design](#15-performance-design)
16. [Testing Design](#16-testing-design)
17. [Build Configuration Design](#17-build-configuration-design)
18. [Traceability Matrix](#18-traceability-matrix)
19. [Glossary](#19-glossary)
20. [References](#20-references)

---

## 1. Introduction

### 1.1 Purpose

This Technical Design Document translates the high-level architecture defined in the [SAD](SAD.md) into implementation-level designs. While the SAD establishes *what* components exist and *how they relate*, this TDD specifies *how each component is implemented*: class designs, method signatures, algorithms, data flows, error handling patterns, and implementation sequences.

This document is the primary reference for developers writing production code. Every design in this document traces back to SRS requirements and SAD architecture decisions.

### 1.2 Scope

This document covers implementation designs for all MVP (v1.0) components across all four layers (Presentation, Domain, Data, Service). It provides:

- Class-level designs with method signatures and contracts
- Algorithm specifications for core logic (polling, session management, threshold evaluation)
- Detailed overlay rendering and animation implementations
- Repository and DAO implementation patterns
- ViewModel and Compose screen patterns
- Error handling implementations for all SRS EH scenarios
- Testing strategies with concrete patterns

### 1.3 Intended Audience

| Audience             | Usage                                                       |
| -------------------- | ----------------------------------------------------------- |
| Android developers   | Primary implementation reference — class designs and algorithms |
| QA / Test engineers  | Test design reference — testable interfaces, mock strategies  |
| Code reviewers       | Verification that implementation matches design               |

### 1.4 Key Reference Summary

| Design Aspect            | Source Document | Key Sections                          |
| ------------------------ | --------------- | ------------------------------------- |
| Functional behavior      | SRS             | FR-001–FR-041, EH-001–EH-008         |
| Performance targets      | SRS             | NFR-001–NFR-016                       |
| Architecture components  | SAD             | Sections 5–14                         |
| Package structure        | SAD             | Section 6                             |
| State machines           | SRS/SAD         | SRS Section 10, SAD Section 11        |
| Data entities            | SRS/SAD         | SRS DR-001–DR-005, SAD Section 8      |
| Risk mitigations         | RiskAssessment  | RA-001–RA-025                         |
| Implementation phases    | ProjectPlan     | WBS 1.0–10.10                         |

---

## 2. Design Principles & Conventions

### 2.1 Kotlin Coding Conventions

| Convention | Rule | Example |
| ---------- | ---- | ------- |
| Naming | Classes: `PascalCase`, functions/properties: `camelCase`, constants: `SCREAMING_SNAKE_CASE` | `SessionManager`, `elapsedSeconds`, `POLL_INTERVAL_MS` |
| Nullability | Avoid nullable types in domain models. Nullable only at system boundary. | Domain: `val name: String`; Data: `val label: String?` |
| Coroutines | Use structured concurrency. Never use `GlobalScope`. | `viewModelScope.launch { }` |
| Flow | Cold `Flow` for database streams. Hot `StateFlow` for mutable state. | `fun getAll(): Flow<List<T>>` vs `val state: StateFlow<T>` |
| Immutability | Domain models are `data class` with `val` properties. Use `copy()` for modifications. | `session.copy(state = COOLDOWN)` |
| Extension functions | Use for utility operations on platform types. Place in `util/extensions/`. | `fun Long.toFormattedTime(): String` |

### 2.2 Error Handling Conventions

| Convention | Rule |
| ---------- | ---- |
| Never crash in service | Wrap all system API calls in try-catch within the service layer |
| Log then continue | On non-fatal errors, log at WARN level and continue operation |
| Result types for use cases | Use Kotlin `Result<T>` or sealed class for operations that can fail at the domain boundary |
| Coroutine exception handling | Use `CoroutineExceptionHandler` on service scope; `SupervisorJob` for child isolation |

### 2.3 Constants Organization

All magic numbers and configurable values are centralized in a single object.

```kotlin
// util/constants/AppConstants.kt


object AppConstants {
    // Polling
    const val POLL_INTERVAL_MS = 2000L
    const val QUERY_WINDOW_MS = 5000L
    const val FALLBACK_QUERY_WINDOW_MS = 10000L

    // Session
    const val COOLDOWN_DURATION_SECONDS = 45
    const val OVERLAY_APPEAR_THRESHOLD_SECONDS = 60
    const val THROB_DELAY_AFTER_T3_SECONDS = 300  // 5 minutes
    const val SESSION_RETENTION_DAYS = 90L

    // Overlay Dimensions
    const val OVERLAY_SIZE_DP = 52
    const val OVERLAY_TEXT_SIZE_SP = 12f

    // Overlay Animations
    const val FADE_IN_DURATION_MS = 500L
    const val FADE_OUT_DURATION_MS = 400L
    const val COLOR_TRANSITION_DURATION_MS = 2000L
    const val SNAP_ANIMATION_DURATION_MS = 250L
    const val THROB_CYCLE_DURATION_MS = 1500L
    const val THROB_SCALE_MAX = 1.15f
    const val COOLDOWN_DIM_ALPHA_REDUCTION = 0.30f
    const val DIM_UNDIM_DURATION_MS = 300L

    // Drag Detection
    const val TAP_TIMEOUT_MS = 150L
    const val TAP_SLOP_DP = 10

    // Defaults
    const val DEFAULT_OVERLAY_EDGE = "right"
    const val DEFAULT_OVERLAY_Y_PERCENT = 0.5f
    const val DEFAULT_THRESHOLD_PRESET = "Moderate"
    const val DEFAULT_T1 = 10
    const val DEFAULT_T2 = 20
    const val DEFAULT_T3 = 30

    // Notification
    const val NOTIFICATION_CHANNEL_ID = "mindfulscroll_monitoring"
    const val NOTIFICATION_CHANNEL_NAME = "Monitoring Service"
    const val NOTIFICATION_ID = 1001

    // Preference Keys
    const val PREF_MONITORING_ACTIVE = "monitoring_active"
    const val PREF_ONBOARDING_COMPLETED = "onboarding_completed"
    const val PREF_OVERLAY_POSITION_EDGE = "overlay_position_edge"
    const val PREF_OVERLAY_POSITION_Y_PERCENT = "overlay_position_y_percent"
    const val PREF_COOLDOWN_DURATION_SECONDS = "cooldown_duration_seconds"
}
```

**SRS Trace:** SAD Section 19.2, FR-014.AC2 (45-sec cooldown), FR-018.AC2 (60-sec overlay trigger), FR-030.AC1 (T3+5min throb), DR-005 (90-day retention).

---

## 3. Domain Model Design

### 3.1 Domain Models

All domain models reside in `domain/model/` and are pure Kotlin data classes with no Android dependencies.

#### 3.1.1 MonitoredApp

```kotlin
// domain/model/MonitoredApp.kt
data class MonitoredApp(
    val packageName: String,
    val displayName: String,
    val isActive: Boolean,
    val addedAt: Long
)
```

#### 3.1.2 InstalledApp

```kotlin
// domain/model/InstalledApp.kt

// to keep domain pure, we abstract the icon as Any? and cast at the presentation layer.
// Alternative: Use a wrapper interface.

data class InstalledApp(
    val packageName: String,
    val displayName: String,
    val icon: Any?  // Drawable reference, opaque to domain logic
)
```

> **Design Note:** The `icon` field is typed as `Any?` rather than `Drawable` to avoid an Android framework import in the domain layer (AG-01). The presentation layer casts it to `Drawable` for display. If stronger typing is desired, introduce an `AppIcon` interface in the domain layer with a `Drawable`-backed implementation in the data layer.

#### 3.1.3 ThresholdConfig

```kotlin
// domain/model/ThresholdConfig.kt
data class ThresholdConfig(
    val id: Int = 0,
    val presetName: String?,
    val threshold1Min: Int,
    val threshold2Min: Int,
    val threshold3Min: Int,
    val isGlobal: Boolean = true,
    val appPackageName: String? = null
) {
    val threshold1Seconds: Int get() = threshold1Min * 60
    val threshold2Seconds: Int get() = threshold2Min * 60
    val threshold3Seconds: Int get() = threshold3Min * 60
    val throbTriggerSeconds: Int get() = threshold3Seconds + (5 * 60)

    companion object {
        val LIGHT = ThresholdConfig(
            presetName = "Light",
            threshold1Min = 5, threshold2Min = 10, threshold3Min = 15
        )
        val MODERATE = ThresholdConfig(
            presetName = "Moderate",
            threshold1Min = 10, threshold2Min = 20, threshold3Min = 30
        )
        val DEFAULT = MODERATE
    }
}
```

**SRS Trace:** DR-002, FR-025 (presets), FR-026 (custom), FR-030.AC1 (throb at T3+5min).

#### 3.1.4 UsageSession

```kotlin
// domain/model/UsageSession.kt
data class UsageSession(
    val id: Long = 0,
    val appPackageName: String,
    val startTimestamp: Long,
    val endTimestamp: Long = 0,
    val durationSeconds: Int = 0,
    val maxThresholdReached: Int = 0,
    val throbActivated: Boolean = false
)
```

#### 3.1.5 State Enums

```kotlin
// domain/model/SessionState.kt
enum class SessionState {
    INACTIVE,
    ACTIVE,
    COOLDOWN,
    ENDED
}

// domain/model/TimerVisualState.kt
enum class TimerVisualState {
    CALM,
    NOTICE,
    ALERT,
    URGENT,
    THROB
}

// domain/model/ServiceState.kt
enum class ServiceState {
    STOPPED,
    RUNNING
}
```

#### 3.1.6 PermissionStatus

```kotlin
// domain/model/PermissionStatus.kt
data class PermissionStatus(
    val usageAccessGranted: Boolean = false,
    val overlayGranted: Boolean = false,
    val batteryOptimizationExempted: Boolean = false,
    val notificationGranted: Boolean = false
) {
    val allCriticalGranted: Boolean
        get() = usageAccessGranted && overlayGranted

    val allGranted: Boolean
        get() = usageAccessGranted && overlayGranted &&
                batteryOptimizationExempted && notificationGranted
}
```

### 3.2 Repository Interfaces

All repository interfaces reside in `domain/repository/`. They define the contract; implementations in `data/repository/` fulfill it.

```kotlin
// domain/repository/MonitoredAppRepository.kt
interface MonitoredAppRepository {
    fun getAll(): Flow<List<MonitoredApp>>
    fun getActive(): Flow<List<MonitoredApp>>
    suspend fun getActivePackageNames(): Set<String>
    suspend fun upsert(app: MonitoredApp)
    suspend fun setActive(packageName: String, isActive: Boolean)
    suspend fun removeUninstalled(installedPackages: List<String>)
}

// domain/repository/ThresholdConfigRepository.kt
interface ThresholdConfigRepository {
    suspend fun getGlobal(): ThresholdConfig
    fun observeGlobal(): Flow<ThresholdConfig>
    suspend fun save(config: ThresholdConfig)
}

// domain/repository/UsageSessionRepository.kt
interface UsageSessionRepository {
    suspend fun insert(session: UsageSession): Long
    suspend fun getRecent(limit: Int = 100): List<UsageSession>
    suspend fun deleteOlderThan(cutoffTimestamp: Long)
}

// domain/repository/UserPreferencesRepository.kt
interface UserPreferencesRepository {
    suspend fun getString(key: String, default: String = ""): String
    suspend fun getBoolean(key: String, default: Boolean = false): Boolean
    suspend fun getFloat(key: String, default: Float = 0f): Float
    suspend fun getInt(key: String, default: Int = 0): Int
    suspend fun setString(key: String, value: String)
    suspend fun setBoolean(key: String, value: Boolean)
    suspend fun setFloat(key: String, value: Float)
    suspend fun setInt(key: String, value: Int)
    fun observeString(key: String): Flow<String?>
    fun observeBoolean(key: String): Flow<Boolean>
}

// domain/repository/InstalledAppRepository.kt
interface InstalledAppRepository {
    suspend fun getInstalledApps(): List<InstalledApp>
}

// domain/repository/UsageStatsRepository.kt
interface UsageStatsRepository {
    suspend fun getCurrentForegroundPackage(): String?
}
```

### 3.3 Use Case Design

Use cases follow a single-responsibility pattern. Each use case has one `operator fun invoke()` or `suspend operator fun invoke()`.

```kotlin
// domain/usecase/GetInstalledAppsUseCase.kt
class GetInstalledAppsUseCase(
    private val installedAppRepository: InstalledAppRepository
) {
    suspend operator fun invoke(): List<InstalledApp> {
        return installedAppRepository.getInstalledApps()
    }
}

// domain/usecase/UpdateAppSelectionUseCase.kt
class UpdateAppSelectionUseCase(
    private val monitoredAppRepository: MonitoredAppRepository
) {
    suspend operator fun invoke(packageName: String, displayName: String, isActive: Boolean) {
        monitoredAppRepository.upsert(
            MonitoredApp(
                packageName = packageName,
                displayName = displayName,
                isActive = isActive,
                addedAt = System.currentTimeMillis()
            )
        )
    }
}

// domain/usecase/GetThresholdConfigUseCase.kt
class GetThresholdConfigUseCase(
    private val thresholdConfigRepository: ThresholdConfigRepository
) {
    suspend operator fun invoke(): ThresholdConfig {
        return thresholdConfigRepository.getGlobal()
    }
}

// domain/usecase/SaveThresholdConfigUseCase.kt
class SaveThresholdConfigUseCase(
    private val thresholdConfigRepository: ThresholdConfigRepository
) {
    suspend operator fun invoke(config: ThresholdConfig): Result<Unit> {
        // Validate T1 < T2 < T3
        if (config.threshold1Min >= config.threshold2Min) {
            return Result.failure(
                IllegalArgumentException("T1 must be less than T2")
            )
        }
        if (config.threshold2Min >= config.threshold3Min) {
            return Result.failure(
                IllegalArgumentException("T2 must be less than T3")
            )
        }
        if (config.threshold1Min < 1) {
            return Result.failure(
                IllegalArgumentException("T1 must be at least 1 minute")
            )
        }
        if (config.threshold3Min > 120) {
            return Result.failure(
                IllegalArgumentException("T3 must not exceed 120 minutes")
            )
        }
        thresholdConfigRepository.save(config)
        return Result.success(Unit)
    }
}

// domain/usecase/CleanupOldSessionsUseCase.kt
class CleanupOldSessionsUseCase(
    private val usageSessionRepository: UsageSessionRepository
) {
    suspend operator fun invoke() {
        val cutoff = System.currentTimeMillis() -
            (AppConstants.SESSION_RETENTION_DAYS * 24 * 60 * 60 * 1000)
        usageSessionRepository.deleteOlderThan(cutoff)
    }
}

// domain/usecase/CheckPermissionsUseCase.kt
class CheckPermissionsUseCase(
    private val permissionChecker: PermissionChecker
) {
    operator fun invoke(): PermissionStatus {
        return permissionChecker.checkAll()
    }
}
```

**SRS Trace:** FR-026.AC5 (validation), DR-005 (90-day cleanup), FR-033 (permission check).

---

## 4. Data Layer Design

### 4.1 Entity-to-Domain Mapping

Each Room entity has a corresponding mapper to/from the domain model. Mappers are extension functions on the entity.

```kotlin
// data/local/entity/Mappers.kt
fun MonitoredAppEntity.toDomain() = MonitoredApp(
    packageName = packageName,
    displayName = displayName,
    isActive = isActive,
    addedAt = addedAt
)

fun MonitoredApp.toEntity() = MonitoredAppEntity(
    packageName = packageName,
    displayName = displayName,
    isActive = isActive,
    addedAt = addedAt
)

fun ThresholdConfigEntity.toDomain() = ThresholdConfig(
    id = id,
    presetName = presetName,
    threshold1Min = threshold1Min,
    threshold2Min = threshold2Min,
    threshold3Min = threshold3Min,
    isGlobal = isGlobal,
    appPackageName = appPackageName
)

fun ThresholdConfig.toEntity() = ThresholdConfigEntity(
    id = id,
    presetName = presetName,
    threshold1Min = threshold1Min,
    threshold2Min = threshold2Min,
    threshold3Min = threshold3Min,
    isGlobal = isGlobal,
    appPackageName = appPackageName
)

fun UsageSessionEntity.toDomain() = UsageSession(
    id = id,
    appPackageName = appPackageName,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp,
    durationSeconds = durationSeconds,
    maxThresholdReached = maxThresholdReached,
    throbActivated = throbActivated
)

fun UsageSession.toEntity() = UsageSessionEntity(
    id = id,
    appPackageName = appPackageName,
    startTimestamp = startTimestamp,
    endTimestamp = endTimestamp,
    durationSeconds = durationSeconds,
    maxThresholdReached = maxThresholdReached,
    throbActivated = throbActivated
)
```

### 4.2 Repository Implementations

Repository implementations reside in `data/repository/`. They delegate to DAOs and system wrappers, mapping between entity and domain types.

```kotlin
// data/repository/MonitoredAppRepositoryImpl.kt
class MonitoredAppRepositoryImpl @Inject constructor(
    private val dao: MonitoredAppDao
) : MonitoredAppRepository {

    override fun getAll(): Flow<List<MonitoredApp>> =
        dao.getAll().map { list -> list.map { it.toDomain() } }

    override fun getActive(): Flow<List<MonitoredApp>> =
        dao.getActive().map { list -> list.map { it.toDomain() } }

    override suspend fun getActivePackageNames(): Set<String> =
        withContext(Dispatchers.IO) {
            dao.getActivePackageNames().toSet()
        }

    override suspend fun upsert(app: MonitoredApp) =
        withContext(Dispatchers.IO) {
            dao.upsert(app.toEntity())
        }

    override suspend fun setActive(packageName: String, isActive: Boolean) =
        withContext(Dispatchers.IO) {
            dao.setActive(packageName, isActive)
        }

    override suspend fun removeUninstalled(installedPackages: List<String>) =
        withContext(Dispatchers.IO) {
            dao.removeUninstalled(installedPackages)
        }
}
```

```kotlin
// data/repository/ThresholdConfigRepositoryImpl.kt
class ThresholdConfigRepositoryImpl @Inject constructor(
    private val dao: ThresholdConfigDao
) : ThresholdConfigRepository {

    override suspend fun getGlobal(): ThresholdConfig =
        withContext(Dispatchers.IO) {
            dao.getGlobal()?.toDomain() ?: ThresholdConfig.DEFAULT
        }

    override fun observeGlobal(): Flow<ThresholdConfig> =
        dao.observeGlobal().map { entity ->
            entity?.toDomain() ?: ThresholdConfig.DEFAULT
        }

    override suspend fun save(config: ThresholdConfig) =
        withContext(Dispatchers.IO) {
            dao.upsert(config.toEntity())
        }
}
```

```kotlin
// data/repository/UserPreferencesRepositoryImpl.kt
class UserPreferencesRepositoryImpl @Inject constructor(
    private val dao: UserPreferencesDao
) : UserPreferencesRepository {

    override suspend fun getString(key: String, default: String): String =
        withContext(Dispatchers.IO) { dao.get(key) ?: default }

    override suspend fun getBoolean(key: String, default: Boolean): Boolean =
        withContext(Dispatchers.IO) { dao.get(key)?.toBooleanStrictOrNull() ?: default }

    override suspend fun getFloat(key: String, default: Float): Float =
        withContext(Dispatchers.IO) { dao.get(key)?.toFloatOrNull() ?: default }

    override suspend fun getInt(key: String, default: Int): Int =
        withContext(Dispatchers.IO) { dao.get(key)?.toIntOrNull() ?: default }

    override suspend fun setString(key: String, value: String) =
        withContext(Dispatchers.IO) { dao.set(UserPreferenceEntity(key, value)) }

    override suspend fun setBoolean(key: String, value: Boolean) =
        withContext(Dispatchers.IO) { dao.set(UserPreferenceEntity(key, value.toString())) }

    override suspend fun setFloat(key: String, value: Float) =
        withContext(Dispatchers.IO) { dao.set(UserPreferenceEntity(key, value.toString())) }

    override suspend fun setInt(key: String, value: Int) =
        withContext(Dispatchers.IO) { dao.set(UserPreferenceEntity(key, value.toString())) }

    override fun observeString(key: String): Flow<String?> = dao.observe(key)

    override fun observeBoolean(key: String): Flow<Boolean> =
        dao.observe(key).map { it?.toBooleanStrictOrNull() ?: false }
}
```

### 4.3 System API Wrappers

System wrappers abstract Android APIs behind testable interfaces.

```kotlin
// data/system/PackageManagerWrapper.kt
class PackageManagerWrapper @Inject constructor(
    private val packageManager: PackageManager,
    @ApplicationContext private val context: Context
) : InstalledAppRepository {

    override suspend fun getInstalledApps(): List<InstalledApp> =
        withContext(Dispatchers.IO) {
            val intent = Intent(Intent.ACTION_MAIN).apply {
                addCategory(Intent.CATEGORY_LAUNCHER)
            }
            val resolveInfos = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.queryIntentActivities(
                    intent,
                    PackageManager.ResolveInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.queryIntentActivities(intent, 0)
            }

            val selfPackage = context.packageName

            resolveInfos
                .asSequence()
                .map { it.activityInfo }
                .filter { it.packageName != selfPackage }
                .distinctBy { it.packageName }
                .map { activityInfo ->
                    InstalledApp(
                        packageName = activityInfo.packageName,
                        displayName = activityInfo.loadLabel(packageManager).toString(),
                        icon = try {
                            activityInfo.loadIcon(packageManager)
                        } catch (e: Exception) { null }
                    )
                }
                .sortedBy { it.displayName.lowercase() }
                .toList()
        }
}
```

**SRS Trace:** FR-001 (retrieve installed apps), FR-001.AC3 (exclude self), FR-003 (sorted alphabetically).

```kotlin
// data/system/UsageStatsWrapper.kt
class UsageStatsWrapper @Inject constructor(
    private val usageStatsManager: UsageStatsManager
) : UsageStatsRepository {

    override suspend fun getCurrentForegroundPackage(): String? =
        withContext(Dispatchers.IO) {
            try {
                // Primary strategy: queryEvents with MOVE_TO_FOREGROUND
                val endTime = System.currentTimeMillis()
                val beginTime = endTime - AppConstants.QUERY_WINDOW_MS
                val events = usageStatsManager.queryEvents(beginTime, endTime)
                val event = UsageEvents.Event()
                var lastForegroundPackage: String? = null

                while (events.hasNextEvent()) {
                    events.getNextEvent(event)
                    if (event.eventType == UsageEvents.Event.MOVE_TO_FOREGROUND) {
                        lastForegroundPackage = event.packageName
                    }
                }

                if (lastForegroundPackage != null) {
                    return@withContext lastForegroundPackage
                }

                // Fallback strategy: queryUsageStats with lastTimeUsed
                val fallbackBegin = endTime - AppConstants.FALLBACK_QUERY_WINDOW_MS
                val stats = usageStatsManager.queryUsageStats(
                    UsageStatsManager.INTERVAL_DAILY,
                    fallbackBegin,
                    endTime
                )
                stats?.maxByOrNull { it.lastTimeUsed }?.packageName
            } catch (e: SecurityException) {
                // Permission revoked
                null
            } catch (e: Exception) {
                // OEM-specific failures
                null
            }
        }
}
```

**SRS Trace:** EIR-001 (dual query strategy), FR-009.AC1, EH-005 (empty data), RA-007 (OEM inconsistency).

### 4.4 Permission Checker

```kotlin
// data/system/PermissionChecker.kt
class PermissionChecker @Inject constructor(
    @ApplicationContext private val context: Context
) {
    fun checkAll(): PermissionStatus {
        return PermissionStatus(
            usageAccessGranted = isUsageAccessGranted(),
            overlayGranted = Settings.canDrawOverlays(context),
            batteryOptimizationExempted = isBatteryOptExempted(),
            notificationGranted = isNotificationGranted()
        )
    }

    fun isUsageAccessGranted(): Boolean {
        val appOps = context.getSystemService(Context.APP_OPS_SERVICE) as AppOpsManager
        val mode = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            appOps.unsafeCheckOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        } else {
            @Suppress("DEPRECATION")
            appOps.checkOpNoThrow(
                AppOpsManager.OPSTR_GET_USAGE_STATS,
                android.os.Process.myUid(),
                context.packageName
            )
        }
        return mode == AppOpsManager.MODE_ALLOWED
    }

    private fun isBatteryOptExempted(): Boolean {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return pm.isIgnoringBatteryOptimizations(context.packageName)
    }

    private fun isNotificationGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) ==
                PackageManager.PERMISSION_GRANTED
        } else {
            true // Not required before API 33
        }
    }
}
```

**SRS Trace:** FR-033 (permission check on resume), FR-032 (onboarding flow), PMR-001–PMR-002.

---

## 5. Polling Engine Design

### 5.1 Class Design

```kotlin
// service/monitoring/PollingEngine.kt
class PollingEngine(
    private val usageStatsRepository: UsageStatsRepository,
    private val scope: CoroutineScope
) {
    private val _foregroundApp = MutableStateFlow<String?>(null)
    val foregroundApp: StateFlow<String?> = _foregroundApp.asStateFlow()

    private var pollingJob: Job? = null
    private val _isScreenOn = MutableStateFlow(true)

    fun start() {
        pollingJob?.cancel()
        pollingJob = scope.launch(Dispatchers.IO) {
            while (isActive) {
                if (_isScreenOn.value) {
                    val pkg = usageStatsRepository.getCurrentForegroundPackage()
                    _foregroundApp.value = pkg
                }
                delay(AppConstants.POLL_INTERVAL_MS)
            }
        }
    }

    fun stop() {
        pollingJob?.cancel()
        pollingJob = null
        _foregroundApp.value = null
    }

    fun onScreenOn() {
        _isScreenOn.value = true
    }

    fun onScreenOff() {
        _isScreenOn.value = false
        _foregroundApp.value = null  // Clear to trigger cooldown in SessionManager
    }
}
```

### 5.2 Polling Algorithm

```
ALGORITHM: Foreground App Polling

Input: None (reads from UsageStatsManager)
Output: foregroundApp StateFlow emits package name or null

LOOP (every POLL_INTERVAL_MS = 2000ms):
  1. IF screen is OFF:
       - Skip query (battery optimization, FR-010.AC3)
       - CONTINUE

  2. Try PRIMARY query:
       a. Call queryEvents(now - 5000ms, now)
       b. Iterate events, find last MOVE_TO_FOREGROUND event
       c. IF found: foregroundApp = event.packageName, CONTINUE

  3. Try FALLBACK query:
       a. Call queryUsageStats(INTERVAL_DAILY, now - 10000ms, now)
       b. Find entry with max lastTimeUsed
       c. IF found: foregroundApp = entry.packageName, CONTINUE

  4. IF both queries return nothing:
       foregroundApp = null
       (SessionManager treats this as "no monitored app in foreground")
```

**SRS Trace:** FR-009 (poll interval), FR-010 (adaptive), FR-011 (3-sec detection), EIR-001 (dual query), EH-005 (empty data).

---

## 6. Session State Machine Design

### 6.1 Class Design

```kotlin
// service/monitoring/SessionManager.kt
class SessionManager(
    private val monitoredAppRepository: MonitoredAppRepository,
    private val usageSessionRepository: UsageSessionRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val scope: CoroutineScope
) {
    // Public state
    private val _sessionState = MutableStateFlow(SessionState.INACTIVE)
    val sessionState: StateFlow<SessionState> = _sessionState.asStateFlow()

    private val _elapsedSeconds = MutableStateFlow(0)
    val elapsedSeconds: StateFlow<Int> = _elapsedSeconds.asStateFlow()

    private val _currentAppPackage = MutableStateFlow<String?>(null)
    val currentAppPackage: StateFlow<String?> = _currentAppPackage.asStateFlow()

    // Internal state
    private var monitoredPackages: Set<String> = emptySet()
    private var cooldownJob: Job? = null
    private var elapsedTimerJob: Job? = null
    private var sessionStartRealtime: Long = 0L
    private var accumulatedSeconds: Int = 0
    private var maxThresholdReached: Int = 0
    private var throbActivated: Boolean = false
    private var sessionStartTimestamp: Long = 0L

    suspend fun initialize() {
        monitoredPackages = monitoredAppRepository.getActivePackageNames()

        // Observe changes to monitored apps
        scope.launch {
            monitoredAppRepository.getActive().collect { apps ->
                monitoredPackages = apps.map { it.packageName }.toSet()
            }
        }
    }

    fun onForegroundAppChanged(packageName: String?) {
        when {
            // Case 1: A monitored app is in foreground
            packageName != null && packageName in monitoredPackages -> {
                handleMonitoredAppDetected(packageName)
            }
            // Case 2: Non-monitored app or null (screen off / no app)
            else -> {
                handleMonitoredAppLeft()
            }
        }
    }

    fun onScreenOff() {
        if (_sessionState.value == SessionState.ACTIVE) {
            transitionToCooldown()
        }
    }

    fun stopMonitoring() {
        if (_sessionState.value == SessionState.ACTIVE ||
            _sessionState.value == SessionState.COOLDOWN) {
            endSession()
        }
    }
    // ... (see Section 6.2 for transition implementations)
}
```

### 6.2 State Transition Implementations

```kotlin
// Inside SessionManager

private fun handleMonitoredAppDetected(packageName: String) {
    when (_sessionState.value) {
        SessionState.INACTIVE -> {
            startNewSession(packageName)
        }
        SessionState.ACTIVE -> {
            if (_currentAppPackage.value != packageName) {
                // Different monitored app — end current, start new
                endSession()
                startNewSession(packageName)
            }
            // Same app — no action needed, session continues
        }
        SessionState.COOLDOWN -> {
            if (_currentAppPackage.value == packageName) {
                // Same app returned during cooldown — resume
                resumeSession()
            } else {
                // Different monitored app during cooldown — end + start new
                endSession()
                startNewSession(packageName)
            }
        }
        SessionState.ENDED -> {
            // Should not remain in ENDED; auto-transitions to INACTIVE
            startNewSession(packageName)
        }
    }
}

private fun handleMonitoredAppLeft() {
    if (_sessionState.value == SessionState.ACTIVE) {
        transitionToCooldown()
    }
    // If already in COOLDOWN, do nothing (cooldown timer is running)
}

private fun startNewSession(packageName: String) {
    _currentAppPackage.value = packageName
    _sessionState.value = SessionState.ACTIVE
    sessionStartRealtime = SystemClock.elapsedRealtime()
    sessionStartTimestamp = System.currentTimeMillis()
    accumulatedSeconds = 0
    maxThresholdReached = 0
    throbActivated = false
    _elapsedSeconds.value = 0

    startElapsedTimer()
}

private fun resumeSession() {
    cooldownJob?.cancel()
    cooldownJob = null
    _sessionState.value = SessionState.ACTIVE
    sessionStartRealtime = SystemClock.elapsedRealtime()

    startElapsedTimer()
}

private fun transitionToCooldown() {
    _sessionState.value = SessionState.COOLDOWN
    pauseElapsedTimer()

    cooldownJob = scope.launch {
        delay(AppConstants.COOLDOWN_DURATION_SECONDS * 1000L)
        if (_sessionState.value == SessionState.COOLDOWN) {
            endSession()
        }
    }
}

private fun endSession() {
    cooldownJob?.cancel()
    cooldownJob = null
    pauseElapsedTimer()

    _sessionState.value = SessionState.ENDED

    // Persist session to database
    val session = UsageSession(
        appPackageName = _currentAppPackage.value ?: "",
        startTimestamp = sessionStartTimestamp,
        endTimestamp = System.currentTimeMillis(),
        durationSeconds = _elapsedSeconds.value,
        maxThresholdReached = maxThresholdReached,
        throbActivated = throbActivated
    )

    scope.launch(Dispatchers.IO) {
        try {
            if (session.durationSeconds > 0) {
                usageSessionRepository.insert(session)
            }
        } catch (e: Exception) {
            // EH-006: Log and continue
        }
    }

    // Transition to INACTIVE
    _sessionState.value = SessionState.INACTIVE
    _currentAppPackage.value = null
    _elapsedSeconds.value = 0
}

private fun startElapsedTimer() {
    elapsedTimerJob?.cancel()
    elapsedTimerJob = scope.launch {
        while (isActive) {
            delay(1000L)
            if (_sessionState.value == SessionState.ACTIVE) {
                accumulatedSeconds++
                _elapsedSeconds.value = accumulatedSeconds
            }
        }
    }
}

private fun pauseElapsedTimer() {
    elapsedTimerJob?.cancel()
    elapsedTimerJob = null
}

fun updateThresholdState(visualState: TimerVisualState) {
    val stateOrdinal = visualState.ordinal + 1  // CALM=1, NOTICE=2, etc.
    if (stateOrdinal > maxThresholdReached) {
        maxThresholdReached = stateOrdinal
    }
    if (visualState == TimerVisualState.THROB) {
        throbActivated = true
    }
}
```

### 6.3 State Transition Diagram Summary

| # | From | Event | To | Actions |
| - | ---- | ----- | -- | ------- |
| T1 | INACTIVE | Monitored app detected | ACTIVE | Create session, start timer |
| T2 | ACTIVE | Non-monitored app detected | COOLDOWN | Pause timer, start 45-sec countdown |
| T3 | ACTIVE | Screen off | COOLDOWN | Same as T2 |
| T4 | ACTIVE | Different monitored app | ENDED + ACTIVE | End current, start new session |
| T5 | ACTIVE | User stops monitoring | ENDED | Persist session, clear state |
| T6 | COOLDOWN | Same app returns | ACTIVE | Resume timer |
| T7 | COOLDOWN | 45 seconds expire | ENDED | Persist session, clear state |
| T8 | COOLDOWN | Different monitored app | ENDED + ACTIVE | End current, start new for new app |
| T9 | ENDED | (auto) | INACTIVE | State cleanup |

**SRS Trace:** SRS Section 10.1 (all transition rules), FR-012–FR-017, EC-02, EC-03, EC-05.

---

## 7. Threshold Engine Design

### 7.1 Pure Function Design

The threshold engine is a stateless, pure function with no side effects. This enables 100% unit test coverage.

```kotlin
// service/threshold/ThresholdEngine.kt
object ThresholdEngine {

    /**
     * Evaluates the current visual state based on elapsed seconds and threshold config.
     *
     * @param elapsedSeconds Total active session time in seconds
     * @param config Current threshold configuration
     * @return The TimerVisualState for the given elapsed time
     */
    fun evaluate(elapsedSeconds: Int, config: ThresholdConfig): TimerVisualState {
        return when {
            elapsedSeconds >= config.throbTriggerSeconds -> TimerVisualState.THROB
            elapsedSeconds >= config.threshold3Seconds   -> TimerVisualState.URGENT
            elapsedSeconds >= config.threshold2Seconds   -> TimerVisualState.ALERT
            elapsedSeconds >= config.threshold1Seconds   -> TimerVisualState.NOTICE
            else                                         -> TimerVisualState.CALM
        }
    }

    /**
     * Determines if the overlay should be visible based on elapsed seconds.
     *
     * @param elapsedSeconds Total active session time in seconds
     * @return true if overlay should be displayed
     */
    fun shouldShowOverlay(elapsedSeconds: Int): Boolean {
        return elapsedSeconds >= AppConstants.OVERLAY_APPEAR_THRESHOLD_SECONDS
    }
}
```

### 7.2 Visual State Properties

```kotlin
// service/overlay/VisualStateProperties.kt
data class VisualStateProperties(
    val backgroundColor: Int,     // Color int (ARGB)
    val textColor: Int,           // Color int (ARGB)
    val opacity: Float            // 0.0 – 1.0
)

object VisualStateConfig {
    private val CALM_PROPS = VisualStateProperties(
        backgroundColor = 0xFF6B7280.toInt(),   // Muted gray
        textColor = 0xFFFFFFFF.toInt(),          // White
        opacity = 0.65f
    )
    private val NOTICE_PROPS = VisualStateProperties(
        backgroundColor = 0xFFF59E0B.toInt(),   // Warm amber
        textColor = 0xFF1F2937.toInt(),          // Dark
        opacity = 0.78f
    )
    private val ALERT_PROPS = VisualStateProperties(
        backgroundColor = 0xFFF97316.toInt(),   // Orange
        textColor = 0xFFFFFFFF.toInt(),          // White
        opacity = 0.88f
    )
    private val URGENT_PROPS = VisualStateProperties(
        backgroundColor = 0xFFEF4444.toInt(),   // Red/coral
        textColor = 0xFFFFFFFF.toInt(),          // White
        opacity = 0.95f
    )
    private val THROB_PROPS = VisualStateProperties(
        backgroundColor = 0xFFEF4444.toInt(),   // Pulsing red
        textColor = 0xFFFFFFFF.toInt(),          // White
        opacity = 1.0f
    )

    fun getProperties(state: TimerVisualState): VisualStateProperties {
        return when (state) {
            TimerVisualState.CALM   -> CALM_PROPS
            TimerVisualState.NOTICE -> NOTICE_PROPS
            TimerVisualState.ALERT  -> ALERT_PROPS
            TimerVisualState.URGENT -> URGENT_PROPS
            TimerVisualState.THROB  -> THROB_PROPS
        }
    }
}
```

**SRS Trace:** FR-028 (visual states with exact colors/opacity), FR-030 (throb trigger at T3+5min).

---

## 8. Monitoring Service Design

### 8.1 Service Class Design

```kotlin
// service/monitoring/MonitoringService.kt
@AndroidEntryPoint
class MonitoringService : Service() {

    @Inject lateinit var usageStatsRepository: UsageStatsRepository
    @Inject lateinit var monitoredAppRepository: MonitoredAppRepository
    @Inject lateinit var thresholdConfigRepository: ThresholdConfigRepository
    @Inject lateinit var usageSessionRepository: UsageSessionRepository
    @Inject lateinit var userPreferencesRepository: UserPreferencesRepository
    @Inject lateinit var permissionChecker: PermissionChecker

    private lateinit var pollingEngine: PollingEngine
    private lateinit var sessionManager: SessionManager
    private lateinit var overlayController: OverlayController
    private lateinit var screenStateReceiver: ScreenStateReceiver

    private val serviceScope = CoroutineScope(
        SupervisorJob() + Dispatchers.Main + CoroutineExceptionHandler { _, throwable ->
            // Log but never crash the service
            Log.e(TAG, "Uncaught exception in service scope", throwable)
        }
    )

    override fun onCreate() {
        super.onCreate()

        pollingEngine = PollingEngine(usageStatsRepository, serviceScope)
        sessionManager = SessionManager(
            monitoredAppRepository, usageSessionRepository,
            userPreferencesRepository, serviceScope
        )
        overlayController = OverlayController(this, userPreferencesRepository, serviceScope)

        screenStateReceiver = ScreenStateReceiver(
            onScreenOn = {
                pollingEngine.onScreenOn()
            },
            onScreenOff = {
                pollingEngine.onScreenOff()
                sessionManager.onScreenOff()
                overlayController.onScreenOff()
            }
        )
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        createNotificationChannel()
        startForeground(AppConstants.NOTIFICATION_ID, buildNotification())

        // RA-011: Cleanup any stale overlay from a previous service instance
        overlayController.cleanupStaleOverlay()

        serviceScope.launch {
            sessionManager.initialize()
            startMonitoringPipeline()
        }

        registerScreenStateReceiver()

        return START_STICKY  // RA-002: Auto-restart on kill
    }

    override fun onDestroy() {
        super.onDestroy()
        sessionManager.stopMonitoring()
        pollingEngine.stop()
        overlayController.removeOverlay()
        unregisterScreenStateReceiver()
        serviceScope.cancel()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    // --- Pipeline ---

    private fun startMonitoringPipeline() {
        pollingEngine.start()

        // Observe foreground app changes → SessionManager
        serviceScope.launch {
            pollingEngine.foregroundApp.collect { pkg ->
                sessionManager.onForegroundAppChanged(pkg)
            }
        }

        // Observe session state → OverlayController
        serviceScope.launch {
            sessionManager.sessionState.collect { state ->
                handleSessionStateChange(state)
            }
        }

        // Observe elapsed seconds → Threshold evaluation + Overlay update
        serviceScope.launch {
            val config = thresholdConfigRepository.getGlobal()
            sessionManager.elapsedSeconds.collect { elapsed ->
                if (sessionManager.sessionState.value == SessionState.ACTIVE) {
                    val visualState = ThresholdEngine.evaluate(elapsed, config)
                    sessionManager.updateThresholdState(visualState)

                    if (ThresholdEngine.shouldShowOverlay(elapsed)) {
                        overlayController.updateOverlay(elapsed, visualState)
                    }
                }
            }
        }

        // Observe threshold config changes
        serviceScope.launch {
            thresholdConfigRepository.observeGlobal().collect { config ->
                // Re-evaluate with new config when thresholds change
            }
        }
    }

    private fun handleSessionStateChange(state: SessionState) {
        when (state) {
            SessionState.ACTIVE -> {
                // Overlay is shown/updated via elapsed seconds collector
            }
            SessionState.COOLDOWN -> {
                overlayController.dimOverlay()
            }
            SessionState.ENDED, SessionState.INACTIVE -> {
                overlayController.removeOverlayWithAnimation()
            }
        }
    }

    // --- Notification ---

    private fun createNotificationChannel() {
        val channel = NotificationChannel(
            AppConstants.NOTIFICATION_CHANNEL_ID,
            AppConstants.NOTIFICATION_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_LOW
        ).apply {
            setShowBadge(false)
            setSound(null, null)
        }
        val nm = getSystemService(NotificationManager::class.java)
        nm.createNotificationChannel(channel)
    }

    private fun buildNotification(): Notification {
        val openIntent = packageManager.getLaunchIntentForPackage(packageName)
        val openPendingIntent = PendingIntent.getActivity(
            this, 0, openIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        val stopIntent = Intent(this, MonitoringService::class.java).apply {
            action = ACTION_STOP
        }
        val stopPendingIntent = PendingIntent.getService(
            this, 1, stopIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        return NotificationCompat.Builder(this, AppConstants.NOTIFICATION_CHANNEL_ID)
            .setContentTitle("MindfulScroll is monitoring")
            .setContentText("Tap to open")
            .setSmallIcon(R.drawable.ic_notification)
            .setOngoing(true)
            .setContentIntent(openPendingIntent)
            .addAction(R.drawable.ic_stop, "Stop Monitoring", stopPendingIntent)
            .build()
    }

    private fun registerScreenStateReceiver() {
        val filter = IntentFilter().apply {
            addAction(Intent.ACTION_SCREEN_ON)
            addAction(Intent.ACTION_SCREEN_OFF)
        }
        registerReceiver(screenStateReceiver, filter)
    }

    private fun unregisterScreenStateReceiver() {
        try { unregisterReceiver(screenStateReceiver) } catch (_: Exception) {}
    }

    companion object {
        private const val TAG = "MonitoringService"
        const val ACTION_STOP = "com.rudy.mindfulscroll.STOP_MONITORING"
    }
}
```

**SRS Trace:** FR-035 (lifecycle), FR-036 (notification), FR-037 (START_STICKY), EH-002 (permission revocation), EH-003 (service killed), RA-002 (OEM kill recovery), RA-011 (ghost overlay cleanup), RA-022 (memory leak prevention via scope cancel).

### 8.2 ScreenStateReceiver

```kotlin
// service/receiver/ScreenStateReceiver.kt
class ScreenStateReceiver(
    private val onScreenOn: () -> Unit,
    private val onScreenOff: () -> Unit
) : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        when (intent.action) {
            Intent.ACTION_SCREEN_ON -> onScreenOn()
            Intent.ACTION_SCREEN_OFF -> onScreenOff()
        }
    }
}
```

### 8.3 BootReceiver

```kotlin
// service/receiver/BootReceiver.kt
class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action != Intent.ACTION_BOOT_COMPLETED) return

        // Check monitoring_active flag synchronously from DB
        // Since onReceive has a limited time window (~10 sec),
        // use a goAsync() pending result with a coroutine.
        val pendingResult = goAsync()

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val db = Room.databaseBuilder(
                    context.applicationContext,
                    MindfulScrollDatabase::class.java,
                    "mindfulscroll.db"
                ).build()

                val value = db.userPreferencesDao()
                    .get(AppConstants.PREF_MONITORING_ACTIVE)

                if (value?.toBooleanStrictOrNull() == true) {
                    val serviceIntent = Intent(context, MonitoringService::class.java)
                    context.startForegroundService(serviceIntent)
                }
            } catch (e: Exception) {
                Log.e("BootReceiver", "Failed to start service on boot", e)
            } finally {
                pendingResult.finish()
            }
        }
    }
}
```

**SRS Trace:** FR-038 (boot restart), FR-038.AC2 (check flag), FR-038.AC6 (within 10 seconds).

---

## 9. Overlay System Design

### 9.1 OverlayController

The OverlayController manages the lifecycle of the overlay view via WindowManager. It is created by MonitoringService and operates on the main thread for all view operations.

```kotlin
// service/overlay/OverlayController.kt
class OverlayController(
    private val context: Context,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val scope: CoroutineScope
) {
    private val windowManager: WindowManager =
        context.getSystemService(Context.WINDOW_SERVICE) as WindowManager

    private var overlayView: OverlayTimerView? = null
    private var layoutParams: WindowManager.LayoutParams? = null
    private var animationManager: OverlayAnimationManager? = null
    private var isOverlayAttached = false
    private var currentVisualState: TimerVisualState? = null

    // --- Public API ---

    fun updateOverlay(elapsedSeconds: Int, visualState: TimerVisualState) {
        if (!Settings.canDrawOverlays(context)) return

        if (!isOverlayAttached) {
            showOverlay(elapsedSeconds, visualState)
        } else {
            // Update time text
            overlayView?.setTime(elapsedSeconds)

            // Update visual state if changed
            if (visualState != currentVisualState) {
                val fromProps = currentVisualState?.let { VisualStateConfig.getProperties(it) }
                val toProps = VisualStateConfig.getProperties(visualState)

                if (fromProps != null) {
                    animationManager?.animateColorTransition(fromProps, toProps)
                } else {
                    overlayView?.applyVisualState(toProps)
                }

                // Start throb if entering THROB state
                if (visualState == TimerVisualState.THROB &&
                    currentVisualState != TimerVisualState.THROB) {
                    animationManager?.startThrob()
                }

                currentVisualState = visualState
            }
        }
    }

    fun dimOverlay() {
        animationManager?.animateDim()
    }

    fun undimOverlay() {
        animationManager?.animateUndim()
    }

    fun removeOverlayWithAnimation() {
        if (!isOverlayAttached) return
        animationManager?.animateFadeOut {
            removeOverlay()
        }
    }

    fun removeOverlay() {
        try {
            if (overlayView != null && overlayView!!.isAttachedToWindow) {
                windowManager.removeView(overlayView)
            }
        } catch (e: Exception) {
            Log.w(TAG, "Failed to remove overlay", e)
        } finally {
            animationManager?.cancelAll()
            overlayView = null
            layoutParams = null
            animationManager = null
            isOverlayAttached = false
            currentVisualState = null
        }
    }

    fun cleanupStaleOverlay() {
        // RA-011: Called on service startup to remove any ghost overlay
        removeOverlay()
    }

    fun onScreenOff() {
        removeOverlay()
    }

    // --- Private Implementation ---

    private fun showOverlay(elapsedSeconds: Int, visualState: TimerVisualState) {
        try {
            val displayMetrics = context.resources.displayMetrics
            val overlaySizePx = (AppConstants.OVERLAY_SIZE_DP * displayMetrics.density).toInt()

            overlayView = OverlayTimerView(context).apply {
                setTime(elapsedSeconds)
                applyVisualState(VisualStateConfig.getProperties(visualState))
            }

            layoutParams = createLayoutParams(overlaySizePx)
            applyPersistedPosition(layoutParams!!, displayMetrics)

            // Set up drag handling
            overlayView!!.setDragListener(object : OverlayTimerView.DragListener {
                override fun onDragStart() {}
                override fun onDrag(dx: Float, dy: Float) {
                    layoutParams?.let { params ->
                        params.x += dx.toInt()
                        params.y += dy.toInt()
                        try {
                            windowManager.updateViewLayout(overlayView, params)
                        } catch (_: Exception) {}
                    }
                }
                override fun onDragEnd() {
                    snapToEdge()
                }
            })

            windowManager.addView(overlayView, layoutParams)
            isOverlayAttached = true
            currentVisualState = visualState

            // Create animation manager and fade in
            animationManager = OverlayAnimationManager(overlayView!!)
            animationManager?.animateFadeIn()

        } catch (e: Exception) {
            // EH-004: Overlay window failure — continue without overlay
            Log.e(TAG, "Failed to show overlay", e)
            isOverlayAttached = false
        }
    }

    private fun createLayoutParams(size: Int): WindowManager.LayoutParams {
        return WindowManager.LayoutParams(
            size, size,
            WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                or WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                or WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            PixelFormat.TRANSLUCENT
        ).apply {
            gravity = Gravity.TOP or Gravity.START
        }
    }

    private fun applyPersistedPosition(
        params: WindowManager.LayoutParams,
        metrics: DisplayMetrics
    ) {
        scope.launch {
            val edge = userPreferencesRepository.getString(
                AppConstants.PREF_OVERLAY_POSITION_EDGE,
                AppConstants.DEFAULT_OVERLAY_EDGE
            )
            val yPercent = userPreferencesRepository.getFloat(
                AppConstants.PREF_OVERLAY_POSITION_Y_PERCENT,
                AppConstants.DEFAULT_OVERLAY_Y_PERCENT
            )

            val overlaySizePx = (AppConstants.OVERLAY_SIZE_DP * metrics.density).toInt()
            val screenWidth = metrics.widthPixels
            val screenHeight = metrics.heightPixels

            withContext(Dispatchers.Main) {
                params.x = if (edge == "left") 0 else screenWidth - overlaySizePx
                params.y = ((screenHeight - overlaySizePx) * yPercent).toInt()
                try {
                    if (isOverlayAttached) {
                        windowManager.updateViewLayout(overlayView, params)
                    }
                } catch (_: Exception) {}
            }
        }
    }

    private fun snapToEdge() {
        val params = layoutParams ?: return
        val view = overlayView ?: return
        val metrics = context.resources.displayMetrics
        val overlaySizePx = (AppConstants.OVERLAY_SIZE_DP * metrics.density).toInt()
        val screenWidth = metrics.widthPixels
        val screenHeight = metrics.heightPixels

        val centerX = params.x + overlaySizePx / 2
        val snapToRight = centerX > screenWidth / 2  // D-003: right on tie
        val targetX = if (snapToRight) screenWidth - overlaySizePx else 0

        // Clamp Y
        val clampedY = params.y.coerceIn(0, screenHeight - overlaySizePx)
        val yPercent = clampedY.toFloat() / (screenHeight - overlaySizePx).coerceAtLeast(1)

        animationManager?.animateSnap(params.x, targetX) { currentX ->
            params.x = currentX
            try {
                windowManager.updateViewLayout(view, params)
            } catch (_: Exception) {}
        }

        params.y = clampedY

        // Persist position
        val edge = if (snapToRight) "right" else "left"
        scope.launch(Dispatchers.IO) {
            userPreferencesRepository.setString(AppConstants.PREF_OVERLAY_POSITION_EDGE, edge)
            userPreferencesRepository.setFloat(AppConstants.PREF_OVERLAY_POSITION_Y_PERCENT, yPercent)
        }
    }

    companion object {
        private const val TAG = "OverlayController"
    }
}
```

**SRS Trace:** FR-018 (display trigger), FR-019 (appearance), FR-020 (fade-in), FR-021 (drag), FR-022 (edge-snap), FR-023 (position persistence), FR-024 (auto-dismiss), FR-028–FR-031 (visual states and throb), EIR-002 (WindowManager interface), EH-004 (window failure), EH-007 (rotation), RA-011 (ghost overlay).

### 9.2 OverlayTimerView

```kotlin
// service/overlay/OverlayTimerView.kt
class OverlayTimerView(context: Context) : View(context) {

    interface DragListener {
        fun onDragStart()
        fun onDrag(dx: Float, dy: Float)
        fun onDragEnd()
    }

    private val circlePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
    }

    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textAlign = Paint.Align.CENTER
        typeface = Typeface.MONOSPACE
        textSize = AppConstants.OVERLAY_TEXT_SIZE_SP *
            resources.displayMetrics.scaledDensity
    }

    private var timeText: String = "00:00"
    private var dragListener: DragListener? = null
    private var touchStartX = 0f
    private var touchStartY = 0f
    private var touchStartTime = 0L
    private var isDragging = false
    private val tapSlopPx = (AppConstants.TAP_SLOP_DP * resources.displayMetrics.density)

    fun setTime(elapsedSeconds: Int) {
        val minutes = elapsedSeconds / 60
        val seconds = elapsedSeconds % 60
        timeText = String.format("%02d:%02d", minutes, seconds)
        invalidate()
    }

    fun applyVisualState(props: VisualStateProperties) {
        circlePaint.color = props.backgroundColor
        textPaint.color = props.textColor
        alpha = props.opacity
        invalidate()
    }

    fun setDragListener(listener: DragListener) {
        dragListener = listener
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val cx = width / 2f
        val cy = height / 2f
        val radius = (width.coerceAtMost(height)) / 2f

        // Draw circle
        canvas.drawCircle(cx, cy, radius, circlePaint)

        // Draw time text centered
        val textY = cy - (textPaint.descent() + textPaint.ascent()) / 2
        canvas.drawText(timeText, cx, textY, textPaint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                touchStartX = event.rawX
                touchStartY = event.rawY
                touchStartTime = SystemClock.elapsedRealtime()
                isDragging = false
                return true
            }
            MotionEvent.ACTION_MOVE -> {
                val dx = event.rawX - touchStartX
                val dy = event.rawY - touchStartY
                val distance = kotlin.math.sqrt(dx * dx + dy * dy)

                if (!isDragging && distance > tapSlopPx) {
                    isDragging = true
                    dragListener?.onDragStart()
                }

                if (isDragging) {
                    dragListener?.onDrag(
                        event.rawX - touchStartX,
                        event.rawY - touchStartY
                    )
                    touchStartX = event.rawX
                    touchStartY = event.rawY
                }
                return true
            }
            MotionEvent.ACTION_UP -> {
                if (isDragging) {
                    dragListener?.onDragEnd()
                } else {
                    val duration = SystemClock.elapsedRealtime() - touchStartTime
                    if (duration < AppConstants.TAP_TIMEOUT_MS) {
                        // Short tap — no action in MVP (FR-022 in v1.1)
                        performClick()
                    }
                }
                return true
            }
        }
        return super.onTouchEvent(event)
    }

    override fun performClick(): Boolean {
        super.performClick()
        return true
    }
}
```

**SRS Trace:** FR-019 (circular 48-56dp, MM:SS, FLAG_NOT_FOCUSABLE), FR-021 (drag with tap/slop detection), SAD Section 10.3 (OverlayView spec), SAD Section 10.5 (touch handling strategy).

### 9.3 OverlayAnimationManager

```kotlin
// service/overlay/OverlayAnimationManager.kt
class OverlayAnimationManager(
    private val view: OverlayTimerView
) {
    private var fadeAnimator: ValueAnimator? = null
    private var colorAnimator: ValueAnimator? = null
    private var snapAnimator: ValueAnimator? = null
    private var throbAnimator: ValueAnimator? = null
    private var dimAnimator: ValueAnimator? = null

    fun animateFadeIn() {
        fadeAnimator?.cancel()
        fadeAnimator = ValueAnimator.ofFloat(0f, view.alpha).apply {
            duration = AppConstants.FADE_IN_DURATION_MS
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { view.alpha = it.animatedValue as Float }
            start()
        }
    }

    fun animateFadeOut(onComplete: () -> Unit) {
        fadeAnimator?.cancel()
        fadeAnimator = ValueAnimator.ofFloat(view.alpha, 0f).apply {
            duration = AppConstants.FADE_OUT_DURATION_MS
            interpolator = AccelerateDecelerateInterpolator()
            addUpdateListener { view.alpha = it.animatedValue as Float }
            addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    onComplete()
                }
            })
            start()
        }
    }

    fun animateColorTransition(
        from: VisualStateProperties,
        to: VisualStateProperties
    ) {
        colorAnimator?.cancel()
        colorAnimator = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = AppConstants.COLOR_TRANSITION_DURATION_MS
            interpolator = LinearInterpolator()
            addUpdateListener { animator ->
                val fraction = animator.animatedValue as Float
                val bgColor = ArgbEvaluator().evaluate(
                    fraction, from.backgroundColor, to.backgroundColor
                ) as Int
                val txtColor = ArgbEvaluator().evaluate(
                    fraction, from.textColor, to.textColor
                ) as Int
                val opacity = from.opacity + (to.opacity - from.opacity) * fraction

                view.applyVisualState(
                    VisualStateProperties(bgColor, txtColor, opacity)
                )
            }
            start()
        }
    }

    fun animateSnap(fromX: Int, toX: Int, onUpdate: (Int) -> Unit) {
        snapAnimator?.cancel()
        snapAnimator = ValueAnimator.ofInt(fromX, toX).apply {
            duration = AppConstants.SNAP_ANIMATION_DURATION_MS
            interpolator = DecelerateInterpolator()
            addUpdateListener { onUpdate(it.animatedValue as Int) }
            start()
        }
    }

    fun startThrob() {
        throbAnimator?.cancel()
        throbAnimator = ValueAnimator.ofFloat(1.0f, AppConstants.THROB_SCALE_MAX).apply {
            duration = AppConstants.THROB_CYCLE_DURATION_MS / 2
            interpolator = AccelerateDecelerateInterpolator()
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.REVERSE
            addUpdateListener {
                val scale = it.animatedValue as Float
                view.scaleX = scale
                view.scaleY = scale
            }
            start()
        }
    }

    fun animateDim() {
        dimAnimator?.cancel()
        val targetAlpha = (view.alpha * (1 - AppConstants.COOLDOWN_DIM_ALPHA_REDUCTION))
            .coerceAtLeast(0f)
        dimAnimator = ValueAnimator.ofFloat(view.alpha, targetAlpha).apply {
            duration = AppConstants.DIM_UNDIM_DURATION_MS
            addUpdateListener { view.alpha = it.animatedValue as Float }
            start()
        }
    }

    fun animateUndim() {
        val currentState = view.alpha
        val targetAlpha = currentState / (1 - AppConstants.COOLDOWN_DIM_ALPHA_REDUCTION)
        dimAnimator?.cancel()
        dimAnimator = ValueAnimator.ofFloat(view.alpha, targetAlpha.coerceAtMost(1f)).apply {
            duration = AppConstants.DIM_UNDIM_DURATION_MS
            addUpdateListener { view.alpha = it.animatedValue as Float }
            start()
        }
    }

    fun cancelAll() {
        fadeAnimator?.cancel()
        colorAnimator?.cancel()
        snapAnimator?.cancel()
        throbAnimator?.cancel()
        dimAnimator?.cancel()
    }
}
```

**SRS Trace:** FR-020 (fade-in 500ms), FR-024 (fade-out 400ms), FR-029 (color transition 2000ms), FR-022 (snap 250ms), FR-031 (throb 1500ms cycle, 1.15x scale), FR-014.AC4 (cooldown dim 30%).

---

## 10. Presentation Layer Design

### 10.1 ViewModel Patterns

All ViewModels follow a consistent pattern: expose a single `StateFlow<UiState>` and accept events through public functions.

#### 10.1.1 HomeViewModel

```kotlin
// presentation/home/HomeViewModel.kt
data class HomeUiState(
    val isMonitoringActive: Boolean = false,
    val monitoredAppCount: Int = 0,
    val thresholdDisplay: String = "",
    val permissionStatus: PermissionStatus = PermissionStatus(),
    val isLoading: Boolean = true
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val monitoredAppRepository: MonitoredAppRepository,
    private val thresholdConfigRepository: ThresholdConfigRepository,
    private val userPreferencesRepository: UserPreferencesRepository,
    private val permissionChecker: PermissionChecker
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            // Observe monitoring status
            userPreferencesRepository.observeBoolean(AppConstants.PREF_MONITORING_ACTIVE)
                .collect { active ->
                    _uiState.update { it.copy(isMonitoringActive = active) }
                }
        }
        viewModelScope.launch {
            monitoredAppRepository.getActive().collect { apps ->
                _uiState.update {
                    it.copy(monitoredAppCount = apps.size, isLoading = false)
                }
            }
        }
        viewModelScope.launch {
            thresholdConfigRepository.observeGlobal().collect { config ->
                val display = "${config.threshold1Min} / ${config.threshold2Min} / ${config.threshold3Min} min"
                _uiState.update { it.copy(thresholdDisplay = display) }
            }
        }
    }

    fun refreshPermissions() {
        _uiState.update { it.copy(permissionStatus = permissionChecker.checkAll()) }
    }

    fun toggleMonitoring(context: Context) {
        viewModelScope.launch {
            val current = _uiState.value.isMonitoringActive
            if (current) {
                // Stop monitoring
                context.stopService(Intent(context, MonitoringService::class.java))
                userPreferencesRepository.setBoolean(AppConstants.PREF_MONITORING_ACTIVE, false)
            } else {
                // Check permissions first
                val perms = permissionChecker.checkAll()
                if (!perms.allCriticalGranted) {
                    _uiState.update { it.copy(permissionStatus = perms) }
                    return@launch  // Navigate to onboarding instead
                }
                // Start monitoring
                val intent = Intent(context, MonitoringService::class.java)
                context.startForegroundService(intent)
                userPreferencesRepository.setBoolean(AppConstants.PREF_MONITORING_ACTIVE, true)
            }
        }
    }
}
```

**SRS Trace:** FR-039 (home screen content), FR-040 (start/stop toggle), FR-033 (permission check on resume).

#### 10.1.2 AppSelectionViewModel

```kotlin
// presentation/appselection/AppSelectionViewModel.kt
data class AppSelectionUiState(
    val apps: List<AppSelectionItem> = emptyList(),
    val searchQuery: String = "",
    val selectedCount: Int = 0,
    val isLoading: Boolean = true
)

data class AppSelectionItem(
    val packageName: String,
    val displayName: String,
    val icon: Any?,
    val isSelected: Boolean
)

@HiltViewModel
class AppSelectionViewModel @Inject constructor(
    private val getInstalledApps: GetInstalledAppsUseCase,
    private val monitoredAppRepository: MonitoredAppRepository,
    private val updateAppSelection: UpdateAppSelectionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AppSelectionUiState())
    val uiState: StateFlow<AppSelectionUiState> = _uiState.asStateFlow()

    private var allApps: List<AppSelectionItem> = emptyList()

    init {
        loadApps()
    }

    private fun loadApps() {
        viewModelScope.launch {
            val installed = getInstalledApps()
            val monitored = monitoredAppRepository.getActivePackageNames()

            allApps = installed.map { app ->
                AppSelectionItem(
                    packageName = app.packageName,
                    displayName = app.displayName,
                    icon = app.icon,
                    isSelected = app.packageName in monitored
                )
            }

            applyFilter()
            _uiState.update { it.copy(isLoading = false) }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
        applyFilter()
    }

    fun onToggleApp(packageName: String) {
        viewModelScope.launch {
            val app = allApps.find { it.packageName == packageName } ?: return@launch
            val newSelection = !app.isSelected

            updateAppSelection(packageName, app.displayName, newSelection)

            allApps = allApps.map {
                if (it.packageName == packageName) it.copy(isSelected = newSelection)
                else it
            }
            applyFilter()
        }
    }

    fun onSelectAll() {
        val filtered = getFilteredApps()
        viewModelScope.launch {
            filtered.filter { !it.isSelected }.forEach { app ->
                updateAppSelection(app.packageName, app.displayName, true)
            }
            allApps = allApps.map { app ->
                if (filtered.any { it.packageName == app.packageName }) {
                    app.copy(isSelected = true)
                } else app
            }
            applyFilter()
        }
    }

    fun onDeselectAll() {
        val filtered = getFilteredApps()
        viewModelScope.launch {
            filtered.filter { it.isSelected }.forEach { app ->
                updateAppSelection(app.packageName, app.displayName, false)
            }
            allApps = allApps.map { app ->
                if (filtered.any { it.packageName == app.packageName }) {
                    app.copy(isSelected = false)
                } else app
            }
            applyFilter()
        }
    }

    private fun getFilteredApps(): List<AppSelectionItem> {
        val query = _uiState.value.searchQuery
        return if (query.isBlank()) allApps
        else allApps.filter {
            it.displayName.contains(query, ignoreCase = true)
        }
    }

    private fun applyFilter() {
        val filtered = getFilteredApps()
        _uiState.update {
            it.copy(
                apps = filtered,
                selectedCount = allApps.count { app -> app.isSelected }
            )
        }
    }
}
```

**SRS Trace:** FR-004 (checklist), FR-005 (search), FR-006 (count), FR-008 (select/deselect all).

### 10.2 Compose Screen Pattern

Screens are stateless composable functions that observe the ViewModel's StateFlow.

```kotlin
// presentation/home/HomeScreen.kt
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel(),
    onNavigateToAppSelection: () -> Unit,
    onNavigateToThreshold: () -> Unit,
    onNavigateToAbout: () -> Unit,
    onNavigateToOnboarding: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.refreshPermissions()
    }

    HomeScreenContent(
        uiState = uiState,
        onToggleMonitoring = {
            if (!uiState.permissionStatus.allCriticalGranted) {
                onNavigateToOnboarding()
            } else {
                viewModel.toggleMonitoring(context)
            }
        },
        onNavigateToAppSelection = onNavigateToAppSelection,
        onNavigateToThreshold = onNavigateToThreshold,
        onNavigateToAbout = onNavigateToAbout,
        onPermissionBannerTap = onNavigateToOnboarding
    )
}
```

---

## 11. Permission Management Design

### 11.1 Deep-Link Intent Creation

```kotlin
// presentation/onboarding/PermissionIntents.kt
object PermissionIntents {

    fun usageAccessSettings(): Intent {
        return Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS)
    }

    fun overlaySettings(packageName: String): Intent {
        return Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:$packageName")
        )
    }

    fun batteryOptimization(packageName: String): Intent {
        return Intent(
            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
            Uri.parse("package:$packageName")
        )
    }

    fun fallbackSettings(): Intent {
        return Intent(Settings.ACTION_SETTINGS)
    }

    /**
     * Safely launch an intent with fallback to generic Settings.
     * Returns true if the primary intent resolved, false if fallback was used.
     */
    fun safeLaunch(context: Context, intent: Intent): Boolean {
        return try {
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(intent)
                true
            } else {
                context.startActivity(fallbackSettings())
                Toast.makeText(
                    context,
                    "Please navigate to the setting manually",
                    Toast.LENGTH_LONG
                ).show()
                false
            }
        } catch (e: Exception) {
            context.startActivity(fallbackSettings())
            false
        }
    }
}
```

**SRS Trace:** FR-034 (deep-links), FR-034.AC1–AC4 (specific intents with fallback).

---

## 12. Navigation Design

### 12.1 Route Definitions

```kotlin
// presentation/navigation/Routes.kt
sealed class Route(val route: String) {
    // Onboarding
    data object Welcome          : Route("onboarding/welcome")
    data object PermUsageAccess  : Route("onboarding/perm_usage_access")
    data object PermOverlay      : Route("onboarding/perm_overlay")
    data object PermBattery      : Route("onboarding/perm_battery")
    data object OnboardComplete  : Route("onboarding/complete")

    // Main
    data object Home             : Route("main/home")
    data object AppSelection     : Route("main/app_selection")
    data object ThresholdConfig  : Route("main/threshold_config")
    data object About            : Route("main/about")
}
```

### 12.2 NavHost Setup

```kotlin
// presentation/navigation/AppNavHost.kt
@Composable
fun AppNavHost(
    navController: NavHostController,
    startDestination: String
) {
    NavHost(navController = navController, startDestination = startDestination) {
        // Onboarding flow
        composable(Route.Welcome.route) {
            WelcomeScreen(onContinue = {
                navController.navigate(Route.PermUsageAccess.route)
            })
        }
        composable(Route.PermUsageAccess.route) {
            PermissionScreen(
                permissionType = PermissionType.USAGE_ACCESS,
                onContinue = { navController.navigate(Route.PermOverlay.route) },
                onSkip = { navController.navigate(Route.PermOverlay.route) }
            )
        }
        composable(Route.PermOverlay.route) {
            PermissionScreen(
                permissionType = PermissionType.OVERLAY,
                onContinue = { navController.navigate(Route.PermBattery.route) },
                onSkip = { navController.navigate(Route.PermBattery.route) }
            )
        }
        composable(Route.PermBattery.route) {
            PermissionScreen(
                permissionType = PermissionType.BATTERY_OPTIMIZATION,
                onContinue = { navController.navigate(Route.OnboardComplete.route) },
                onSkip = { navController.navigate(Route.OnboardComplete.route) }
            )
        }
        composable(Route.OnboardComplete.route) {
            OnboardingCompleteScreen(onFinish = {
                navController.navigate(Route.Home.route) {
                    popUpTo(Route.Welcome.route) { inclusive = true }
                }
            })
        }

        // Main flow
        composable(Route.Home.route) {
            HomeScreen(
                onNavigateToAppSelection = {
                    navController.navigate(Route.AppSelection.route)
                },
                onNavigateToThreshold = {
                    navController.navigate(Route.ThresholdConfig.route)
                },
                onNavigateToAbout = {
                    navController.navigate(Route.About.route)
                },
                onNavigateToOnboarding = {
                    navController.navigate(Route.PermUsageAccess.route)
                }
            )
        }
        composable(Route.AppSelection.route) {
            AppSelectionScreen(onBack = { navController.popBackStack() })
        }
        composable(Route.ThresholdConfig.route) {
            ThresholdConfigScreen(onBack = { navController.popBackStack() })
        }
        composable(Route.About.route) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
    }
}
```

### 12.3 Start Destination Logic

```kotlin
// presentation/navigation/StartDestination.kt
suspend fun determineStartDestination(
    userPreferencesRepository: UserPreferencesRepository,
    permissionChecker: PermissionChecker
): String {
    val onboardingCompleted = userPreferencesRepository.getBoolean(
        AppConstants.PREF_ONBOARDING_COMPLETED, false
    )

    if (!onboardingCompleted) {
        return Route.Welcome.route
    }

    val permissions = permissionChecker.checkAll()
    if (!permissions.usageAccessGranted) {
        return Route.PermUsageAccess.route
    }

    return Route.Home.route
}
```

**SRS Trace:** FR-041 (navigation structure), FR-041.AC3–AC5 (conditional routing).

---

## 13. Dependency Injection Design

### 13.1 Module Structure

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
    @Binds abstract fun bindInstalledAppRepo(impl: PackageManagerWrapper): InstalledAppRepository
    @Binds abstract fun bindUsageStatsRepo(impl: UsageStatsWrapper): UsageStatsRepository
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

// di/UseCaseModule.kt
@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Provides
    fun provideGetInstalledApps(repo: InstalledAppRepository) = GetInstalledAppsUseCase(repo)

    @Provides
    fun provideUpdateAppSelection(repo: MonitoredAppRepository) = UpdateAppSelectionUseCase(repo)

    @Provides
    fun provideGetThresholdConfig(repo: ThresholdConfigRepository) = GetThresholdConfigUseCase(repo)

    @Provides
    fun provideSaveThresholdConfig(repo: ThresholdConfigRepository) = SaveThresholdConfigUseCase(repo)

    @Provides
    fun provideCleanupSessions(repo: UsageSessionRepository) = CleanupOldSessionsUseCase(repo)

    @Provides
    fun provideCheckPermissions(checker: PermissionChecker) = CheckPermissionsUseCase(checker)
}
```

**SRS Trace:** SAD Section 13 (DI architecture), NFR-013.AC4 (DI used throughout), ADR-002 (Hilt chosen).

---

## 14. Error Handling Design

### 14.1 Error Handling Matrix

| SRS Ref | Scenario | Detection | Component | Response | User Impact |
| ------- | -------- | --------- | --------- | -------- | ----------- |
| EH-001 | App uninstalled | `PackageManager.getApplicationInfo()` throws `NameNotFoundException` | MonitoredAppRepository | Remove from DB silently | None visible |
| EH-002 | Permission revoked | `PermissionChecker.checkAll()` returns false | MonitoringService + HomeVM | Pause monitoring, show banner | Banner on Home screen |
| EH-003 | Service killed | `START_STICKY` → Android restarts | OS + MonitoringService | Reload config from DB, resume | Brief gap in monitoring |
| EH-004 | Overlay failure | Try-catch around `WindowManager.addView()` | OverlayController | Log error, continue without overlay | No timer shown |
| EH-005 | UsageStats empty | Null/empty result from query | UsageStatsWrapper | Return null → no session action | Detection delayed by 1 poll |
| EH-006 | DB write failure | Exception from Room DAO | Repository impl | Log error, continue in-memory | Session data may be lost |
| EH-007 | Device rotation | Configuration change callback | OverlayController | Recalculate position from Y-percent | Timer briefly repositions |
| EH-008 | Split-screen mode | UsageStats reports focused app | PollingEngine | Monitor focused app normally | Works as expected |

### 14.2 Uninstalled App Cleanup

```kotlin
// data/repository/MonitoredAppRepositoryImpl.kt (additional method)
suspend fun cleanupUninstalledApps(packageManager: PackageManager) {
    withContext(Dispatchers.IO) {
        val installedPackages = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getInstalledApplications(
                    PackageManager.ApplicationInfoFlags.of(0)
                )
            } else {
                @Suppress("DEPRECATION")
                packageManager.getInstalledApplications(0)
            }.map { it.packageName }
        } catch (e: Exception) {
            return@withContext
        }
        dao.removeUninstalled(installedPackages)
    }
}
```

---

## 15. Performance Design

### 15.1 In-Memory Caching Strategy

The polling engine's hot path (every 2 seconds) must be extremely efficient. The monitored app set is cached in a `HashSet<String>` for O(1) lookup.

```
Polling hot path:
1. Query UsageStatsManager → O(1) per call (~5ms)
2. Check if result is in monitoredPackages HashSet → O(1)
3. Emit to StateFlow → O(1)

Total per-poll cost: ~5-10ms
```

### 15.2 Overlay Rendering Strategy

The overlay uses a flat view hierarchy (single custom `View`) with `onDraw()` to minimize rendering cost.

| Strategy | Implementation | SRS Trace |
| -------- | -------------- | --------- |
| Flat hierarchy | Single `OverlayTimerView` with `onDraw()` | NFR-004 |
| Hardware acceleration | `setLayerType(LAYER_TYPE_HARDWARE)` | NFR-004.AC1 |
| Minimal invalidation | Only `invalidate()` on time update (1/sec) or state change | NFR-004.AC2 |
| Efficient text rendering | Pre-formatted string, `Paint` objects reused | NFR-004 |

### 15.3 Database Access Pattern

| Operation | Thread | Flow Type | Caching |
| --------- | ------ | --------- | ------- |
| Read monitored app list | IO | Cold `Flow` (reactive) | In-memory HashSet in SessionManager |
| Read threshold config | IO | Cold `Flow` (reactive) | Held in service scope |
| Write session record | IO | Suspend (fire-and-forget) | None needed |
| Read/write preferences | IO | Suspend | None; fast key-value lookups |

---

## 16. Testing Design

### 16.1 Test Strategy per Component

| Component | Test Type | Framework | Coverage Target |
| --------- | --------- | --------- | --------------- |
| SessionManager (state machine) | Unit | JUnit + MockK | 100% of transitions |
| ThresholdEngine | Unit | JUnit | 100% of states + boundaries |
| Use cases | Unit | JUnit + MockK | All validation paths |
| Repository implementations | Instrumented | AndroidX Test + Room in-memory DB | All CRUD operations |
| ViewModels | Unit | JUnit + MockK + Turbine | Key state transitions |
| Compose screens | UI | Compose Test | Navigation + key interactions |
| Overlay | Manual | Real devices | Visual verification on 10 host apps |
| Service integration | Manual + Instrumented | AndroidX Test | Core monitoring pipeline |
| OEM compatibility | Manual | Real devices (Pixel, Samsung, Xiaomi) | 8-hour endurance |

### 16.2 Session State Machine Test Cases

```kotlin
// test/service/monitoring/SessionManagerTest.kt
class SessionManagerTest {

    // T1: INACTIVE → ACTIVE when monitored app detected
    @Test fun `monitored app detected from INACTIVE starts new session`()

    // T2: ACTIVE → COOLDOWN when non-monitored app detected
    @Test fun `non-monitored app detected from ACTIVE enters cooldown`()

    // T3: ACTIVE → COOLDOWN when screen off
    @Test fun `screen off from ACTIVE enters cooldown`()

    // T4: ACTIVE → ENDED + ACTIVE when different monitored app
    @Test fun `different monitored app ends current and starts new`()

    // T5: ACTIVE → ENDED when user stops
    @Test fun `stop monitoring from ACTIVE ends session`()

    // T6: COOLDOWN → ACTIVE when same app returns
    @Test fun `same app returns during cooldown resumes session`()

    // T7: COOLDOWN → ENDED when timeout
    @Test fun `cooldown expiry ends session`()

    // T8: COOLDOWN → ENDED + ACTIVE when different monitored app
    @Test fun `different monitored app during cooldown ends and starts new`()

    // Edge cases
    @Test fun `elapsed time does not increment during cooldown`()
    @Test fun `only one active session at a time`()
    @Test fun `rapid app switching handles correctly`()
    @Test fun `session under 60 seconds does not trigger overlay`()
}
```

### 16.3 ThresholdEngine Test Cases

```kotlin
// test/service/threshold/ThresholdEngineTest.kt
class ThresholdEngineTest {
    private val config = ThresholdConfig.MODERATE  // 10/20/30 min

    @Test fun `0 seconds returns CALM`()
    @Test fun `599 seconds returns CALM`()             // Just below T1
    @Test fun `600 seconds returns NOTICE`()            // Exactly T1
    @Test fun `1199 seconds returns NOTICE`()           // Just below T2
    @Test fun `1200 seconds returns ALERT`()            // Exactly T2
    @Test fun `1799 seconds returns ALERT`()            // Just below T3
    @Test fun `1800 seconds returns URGENT`()           // Exactly T3
    @Test fun `2099 seconds returns URGENT`()           // Just below T3+5min
    @Test fun `2100 seconds returns THROB`()            // Exactly T3+5min
    @Test fun `10000 seconds returns THROB`()           // Well past T3+5min

    // Boundary: custom config T1=1, T2=2, T3=3 (EC-08)
    @Test fun `minimum thresholds escalate rapidly`()

    // Boundary: custom config T1=40, T2=80, T3=120 (EC-09)
    @Test fun `maximum thresholds stay calm for extended period`()

    // shouldShowOverlay
    @Test fun `59 seconds returns false`()
    @Test fun `60 seconds returns true`()
}
```

**SRS Trace:** NFR-014 (test coverage), ProjectPlan Section 11.2 (coverage targets).

---

## 17. Build Configuration Design

### 17.1 Manifest Configuration

```xml
<!-- AndroidManifest.xml (key declarations) -->
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools">

    <!-- Permissions -->
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
    <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
    <!-- NEVER: android.permission.INTERNET -->

    <application
        android:name=".MindfulScrollApp"
        android:allowBackup="false"
        android:label="@string/app_name"
        android:supportsRtl="true"
        android:theme="@style/Theme.MindfulScroll">

        <activity
            android:name=".presentation.MainActivity"
            android:exported="true"
            android:launchMode="singleTask">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <service
            android:name=".service.monitoring.MonitoringService"
            android:exported="false"
            android:foregroundServiceType="specialUse">
            <property
                android:name="android.app.PROPERTY_SPECIAL_USE_FGS_SUBTYPE"
                android:value="Monitors foreground app usage to display a real-time awareness timer overlay" />
        </service>

        <receiver
            android:name=".service.receiver.BootReceiver"
            android:enabled="true"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>

    </application>
</manifest>
```

**SRS Trace:** PMR-001 (permission manifest), FR-035.AC6 (foregroundServiceType), FR-038.AC1 (BOOT_COMPLETED receiver), NFR-011.AC1 (no INTERNET), NFR-012.AC5 (allowBackup=false), RA-021 (specialUse justification).

### 17.2 Build Variants

| Variant | `minifyEnabled` | `debuggable` | Signing | ProGuard | Logs |
| ------- | --------------- | ------------ | ------- | -------- | ---- |
| `debug` | false | true | Debug keystore | Off | Full (d/v/i/w/e) |
| `release` | true | false | Release keystore | R8 with rules | Critical only (w/e) |

### 17.3 ProGuard / R8 Rules

```proguard
# proguard-rules.pro

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# Hilt
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class *

# Domain models (used in Room entities)
-keep class com.rudy.mindfulscroll.domain.model.** { *; }

# Data entities
-keep class com.rudy.mindfulscroll.data.local.entity.** { *; }

# Strip debug logs in release
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
    public static int i(...);
}
```

---

## 18. Traceability Matrix

### 18.1 SRS Requirement → TDD Section

| SRS Requirement | TDD Section | Component Designed |
| --------------- | ----------- | ------------------ |
| FR-001–FR-003 | 4.3 (PackageManagerWrapper) | App discovery and listing |
| FR-004–FR-008 | 10.1.2 (AppSelectionViewModel) | App selection UI logic |
| FR-009–FR-011 | 5 (Polling Engine) | Foreground detection |
| FR-012–FR-017 | 6 (Session State Machine) | Session lifecycle |
| FR-018–FR-024 | 9 (Overlay System) | Floating timer overlay |
| FR-025–FR-027 | 3.1.3 (ThresholdConfig), 10 | Threshold configuration |
| FR-028–FR-029 | 7 (Threshold Engine), 9.3 (Animations) | Visual escalation |
| FR-030–FR-031 | 7 (ThresholdEngine), 9.3 (Throb) | Throb animation |
| FR-032–FR-034 | 11 (Permission Management), 12 (Navigation) | Onboarding flow |
| FR-035–FR-037 | 8 (Monitoring Service) | Foreground service |
| FR-038 | 8.3 (BootReceiver) | Boot auto-restart |
| FR-039–FR-041 | 10.1.1 (HomeViewModel), 12 (Navigation) | Home screen and navigation |
| NFR-001 | 15 (Performance) | Startup optimization |
| NFR-002 | 5 (Adaptive polling), 15 | Battery efficiency |
| NFR-003 | 15 (Memory strategy) | Memory management |
| NFR-004 | 9.2 (OverlayTimerView), 15.2 | Overlay rendering |
| NFR-006 | 8 (START_STICKY, BootReceiver) | Service reliability |
| NFR-008 | 4.1 (Room entities, WAL) | Data integrity |
| NFR-011–012 | 17.1 (Manifest), 4 (Data Layer) | Privacy and security |
| NFR-013 | 2–4, 13 (DI) | Clean Architecture |
| NFR-014 | 16 (Testing) | Test coverage |
| NFR-016 | 5.1 (dual query), 14 (Error Handling) | OEM compatibility |

### 18.2 Risk → TDD Mitigation

| Risk | TDD Mitigation | Section |
| ---- | -------------- | ------- |
| RA-002 (OEM battery kill) | START_STICKY, BootReceiver, SupervisorJob | 8.1, 8.3 |
| RA-007 (UsageStats inconsistency) | Dual-query strategy in UsageStatsWrapper | 4.3, 5 |
| RA-010 (Touch interference) | FLAG_NOT_FOCUSABLE, tap/drag discrimination | 9.1, 9.2 |
| RA-011 (Ghost overlay) | cleanupStaleOverlay(), try-catch removal, null-safe refs | 9.1 |
| RA-012 (State machine errors) | Pure Kotlin state machine, 100% test coverage | 6, 16.2 |
| RA-014 (Overlay jank) | Flat view, hardware accel, 1/sec invalidation | 9.2, 15.2 |
| RA-018 (Migration failures) | Schema export, no destructive migration | 4 (Room config) |
| RA-022 (Memory leak) | Scoped coroutines, cancelAll() in animations, scope.cancel() | 8.1, 9.3 |

---

## 19. Glossary

All terms from PRD (Section 17), SRS (Section 14), SAD (Section 23), and RiskAssessment (Section 14) apply. Additional TDD-specific terms:

| Term | Definition |
| ---- | ---------- |
| **Mapper** | An extension function that converts between data layer entities and domain models. |
| **Hot Path** | The code executed on every polling cycle (~every 2 seconds). Must be optimized for minimal CPU and memory. |
| **Tap Slop** | The minimum movement distance (in dp) before a touch is classified as a drag rather than a tap. |
| **Edge-Snap Target** | The calculated X position to which the overlay animates after a drag release (0 for left edge, screenWidth - overlayWidth for right edge). |
| **Visual State Properties** | A data class holding the background color, text color, and opacity for a specific `TimerVisualState`. |
| **Service Scope** | The `CoroutineScope` owned by `MonitoringService` with `SupervisorJob`, canceled in `onDestroy()`. |
| **Dual-Query Strategy** | The approach of trying `queryEvents()` first and falling back to `queryUsageStats()` if the primary query returns empty results. |
| **Stale Overlay** | An overlay view that was added by a previous service instance and never removed (synonym for ghost overlay). |

---

## 20. References

| Reference | Relevance |
| --------- | --------- |
| [PRD.md](PRD.md) | Product features, principles, constraints |
| [SRS.md](SRS.md) | Functional and non-functional requirements, data model, state machines |
| [SAD.md](SAD.md) | Architecture patterns, component design, package structure |
| [ProjectPlan.md](ProjectPlan.md) | Phase breakdown, WBS, technology stack |
| [RiskAssessment.md](RiskAssessment.md) | Risk register, mitigation strategies |
| [Android UsageStatsManager](https://developer.android.com/reference/android/app/usage/UsageStatsManager) | Polling engine API reference |
| [Android WindowManager](https://developer.android.com/reference/android/view/WindowManager) | Overlay system API reference |
| [Android Foreground Services](https://developer.android.com/develop/background-work/services/foreground-services) | Service design reference |
| [Hilt Dependency Injection](https://developer.android.com/training/dependency-injection/hilt-android) | DI framework reference |
| [Room Persistence Library](https://developer.android.com/training/data-storage/room) | Database implementation reference |
| [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-guide.html) | Concurrency model reference |
| [Android Property Animation](https://developer.android.com/develop/ui/views/animations/prop-animation) | Overlay animation reference |

---

*End of Document*
