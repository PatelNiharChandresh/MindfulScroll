# MindfulScroll — Database Schema Document

| Field               | Value                                  |
| ------------------- | -------------------------------------- |
| **Document ID**     | DOC-007                                |
| **Version**         | 1.0                                    |
| **Date**            | 2026-02-07                             |
| **Status**          | Draft                                  |
| **Parent Docs**     | PRD.md, SRS.md, SAD.md, TDD.md         |
| **Audience**        | Developers, QA, Maintainers            |

---

## Table of Contents

1. [Overview](#1-overview)
2. [Database Configuration](#2-database-configuration)
3. [Entity-Relationship Diagram](#3-entity-relationship-diagram)
4. [Table Definitions](#4-table-definitions)
   - 4.1 [monitored_apps](#41-monitored_apps)
   - 4.2 [threshold_configs](#42-threshold_configs)
   - 4.3 [usage_sessions](#43-usage_sessions)
   - 4.4 [user_preferences](#44-user_preferences)
5. [Indexes](#5-indexes)
6. [Relationships & Foreign Keys](#6-relationships--foreign-keys)
7. [Data Access Objects (DAOs)](#7-data-access-objects-daos)
   - 7.1 [MonitoredAppDao](#71-monitoredappdao)
   - 7.2 [ThresholdConfigDao](#72-thresholdconfigdao)
   - 7.3 [UsageSessionDao](#73-usagesessiondao)
   - 7.4 [UserPreferencesDao](#74-userpreferencesdao)
8. [Room Database Class](#8-room-database-class)
9. [Entity-to-Domain Mapping](#9-entity-to-domain-mapping)
10. [Repository Layer](#10-repository-layer)
11. [Data Retention Policy](#11-data-retention-policy)
12. [Data Volume Estimates](#12-data-volume-estimates)
13. [Migration Strategy](#13-migration-strategy)
14. [Performance Requirements](#14-performance-requirements)
15. [Data Protection & Privacy](#15-data-protection--privacy)
16. [Seed Data & Defaults](#16-seed-data--defaults)
17. [Error Handling](#17-error-handling)
18. [Traceability Matrix](#18-traceability-matrix)
19. [References](#19-references)

---

## 1. Overview

MindfulScroll uses a single Room (AndroidX) database as the sole persistence layer for all application data, including user preferences. This aligns with architectural decision **ADR-006** (Room for ALL persistence, no DataStore).

The database stores four entity types:

| # | Entity            | Table Name          | Purpose                                      | SRS Ref |
|---|-------------------|---------------------|----------------------------------------------|---------|
| 1 | MonitoredApp      | `monitored_apps`    | Apps selected by the user for monitoring     | DR-001  |
| 2 | ThresholdConfig   | `threshold_configs` | Timer escalation threshold settings          | DR-002  |
| 3 | UsageSession      | `usage_sessions`    | Historical records of completed sessions     | DR-003  |
| 4 | UserPreferences   | `user_preferences`  | Key-value store for app configuration        | DR-004  |

The database is designed to be lightweight (< 600 KB total), fully offline (no INTERNET permission — constraint C-12), and stored exclusively in app-private internal storage (NFR-012).

---

## 2. Database Configuration

| Parameter          | Value                                                                  |
| ------------------ | ---------------------------------------------------------------------- |
| Library            | Room (AndroidX Persistence)                                            |
| Database Name      | `mindfulscroll.db`                                                     |
| Schema Version     | 1 (MVP)                                                                |
| Storage Location   | App private internal storage (`/data/data/com.rudy.mindfulscroll/databases/`) |
| Journal Mode       | WAL (Write-Ahead Logging) — required by NFR-008.AC1                    |
| Export Schema      | Yes, JSON format — for migration tracking (RA-018 mitigation)          |
| Thread Safety      | All DAO operations execute on `Dispatchers.IO` via repository layer    |
| Destructive Migration | **Never** — `fallbackToDestructiveMigration()` is prohibited (RA-018) |

### 2.1 Room Builder Configuration

```kotlin
Room.databaseBuilder(
    context.applicationContext,
    MindfulScrollDatabase::class.java,
    "mindfulscroll.db"
)
.setJournalMode(RoomDatabase.JournalMode.TRUNCATE) // WAL is default; explicit for clarity
.build()
```

> **Note:** Room enables WAL mode by default on API 16+. The explicit `setJournalMode` call documents the architectural intent per NFR-008.

---

## 3. Entity-Relationship Diagram

```
┌────────────────────────────┐
│       monitored_apps       │
├────────────────────────────┤
│ PK  packageName : TEXT     │
│     displayName : TEXT     │
│     isActive    : INTEGER  │
│     addedAt     : INTEGER  │
└──────────┬─────────────────┘
           │ 1
           │
           ├──────────────────── 0..* ─────────────────────┐
           │                                               │
           │ 0..* (v1.1: per-app thresholds)               │ 0..* (session history)
           │                                               │
┌──────────▼─────────────────┐       ┌─────────────────────▼──────┐
│     threshold_configs      │       │       usage_sessions       │
├────────────────────────────┤       ├────────────────────────────┤
│ PK  id : INTEGER (auto)   │       │ PK  id : INTEGER (auto)    │
│     presetName : TEXT      │       │ FK  appPackageName : TEXT   │
│     threshold1Min : INTEGER│       │     startTimestamp : INTEGER│
│     threshold2Min : INTEGER│       │     endTimestamp   : INTEGER│
│     threshold3Min : INTEGER│       │     durationSeconds: INTEGER│
│     isGlobal : INTEGER     │       │     maxThresholdReached:INT │
│ FK  appPackageName : TEXT  │       │     throbActivated : INTEGER│
└────────────────────────────┘       └────────────────────────────┘

┌────────────────────────────┐
│      user_preferences      │
├────────────────────────────┤
│ PK  key   : TEXT           │
│     value : TEXT            │
└────────────────────────────┘
```

**Relationship Summary:**

| From              | To              | Cardinality | FK Column        | Enforcement | Notes                           |
|-------------------|-----------------|-------------|------------------|-------------|---------------------------------|
| threshold_configs | monitored_apps  | N:1         | appPackageName   | Logical     | Nullable; used in v1.1 per-app  |
| usage_sessions    | monitored_apps  | N:1         | appPackageName   | Logical     | Not null; references package    |

> **Design Decision:** Foreign keys are **logical** (not enforced by Room `@ForeignKey` annotation) in v1.0. This avoids CASCADE complications when apps are uninstalled and records are cleaned up asynchronously. Referential integrity is maintained by the repository layer.

---

## 4. Table Definitions

### 4.1 monitored_apps

**SRS Reference:** DR-001

Stores applications selected by the user for screen-time monitoring.

| Column        | SQLite Type | Kotlin Type | Nullable | Default | Constraints      | Description                                     |
|---------------|-------------|-------------|----------|---------|------------------|-------------------------------------------------|
| `packageName` | TEXT        | String      | No       | —       | PRIMARY KEY      | Android package name (e.g., `com.instagram.android`) |
| `displayName` | TEXT        | String      | No       | —       | NOT NULL         | Human-readable app name                          |
| `isActive`    | INTEGER     | Boolean     | No       | 1       | NOT NULL         | Whether currently selected for monitoring (1=true, 0=false) |
| `addedAt`     | INTEGER     | Long        | No       | —       | NOT NULL         | Timestamp when first selected (epoch milliseconds) |

**Room Entity:**

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

**SQL DDL:**

```sql
CREATE TABLE IF NOT EXISTS monitored_apps (
    packageName TEXT NOT NULL PRIMARY KEY,
    displayName TEXT NOT NULL,
    isActive    INTEGER NOT NULL DEFAULT 1,
    addedAt     INTEGER NOT NULL
);
```

---

### 4.2 threshold_configs

**SRS Reference:** DR-002

Stores timer escalation threshold configuration. MVP uses a single global config row; v1.1 adds per-app overrides via `appPackageName`.

| Column           | SQLite Type | Kotlin Type | Nullable | Default | Constraints                     | Description                                     |
|------------------|-------------|-------------|----------|---------|--------------------------------|-------------------------------------------------|
| `id`             | INTEGER     | Int         | No       | auto    | PRIMARY KEY AUTOINCREMENT       | Unique identifier                                |
| `presetName`     | TEXT        | String?     | Yes      | NULL    | —                              | `"Light"`, `"Moderate"`, or null (custom)        |
| `threshold1Min`  | INTEGER     | Int         | No       | —       | NOT NULL, value >= 1           | First threshold in minutes                       |
| `threshold2Min`  | INTEGER     | Int         | No       | —       | NOT NULL, value > threshold1Min | Second threshold in minutes                      |
| `threshold3Min`  | INTEGER     | Int         | No       | —       | NOT NULL, value > threshold2Min, value <= 120 | Third threshold in minutes          |
| `isGlobal`       | INTEGER     | Boolean     | No       | 1       | NOT NULL                        | Whether this is the global default config        |
| `appPackageName` | TEXT        | String?     | Yes      | NULL    | LOGICAL FK → monitored_apps    | If non-null, per-app override (v1.1 forward compat) |

**Validation Rules (enforced at domain/repository layer):**

| Rule | Constraint                       | Error Handling                    |
|------|----------------------------------|-----------------------------------|
| V-01 | `threshold1Min >= 1`            | UI prevents invalid input         |
| V-02 | `threshold2Min > threshold1Min` | UI prevents invalid input         |
| V-03 | `threshold3Min > threshold2Min` | UI prevents invalid input         |
| V-04 | `threshold3Min <= 120`          | UI caps at maximum value          |
| V-05 | Exactly one row where `isGlobal = 1` at all times | Repository enforces on save |

**Room Entity:**

```kotlin
@Entity(tableName = "threshold_configs")
data class ThresholdConfigEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val presetName: String?,        // "Light", "Moderate", or null (custom)
    val threshold1Min: Int,         // >= 1
    val threshold2Min: Int,         // > threshold1Min
    val threshold3Min: Int,         // > threshold2Min, <= 120
    val isGlobal: Boolean = true,
    val appPackageName: String? = null  // FK for v1.1 per-app override
)
```

**SQL DDL:**

```sql
CREATE TABLE IF NOT EXISTS threshold_configs (
    id              INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    presetName      TEXT,
    threshold1Min   INTEGER NOT NULL,
    threshold2Min   INTEGER NOT NULL,
    threshold3Min   INTEGER NOT NULL,
    isGlobal        INTEGER NOT NULL DEFAULT 1,
    appPackageName  TEXT DEFAULT NULL
);
```

**Threshold Presets (application-level constants):**

| Preset     | T1 (min) | T2 (min) | T3 (min) | Description        |
|------------|----------|----------|----------|--------------------|
| Light      | 15       | 30       | 45       | Relaxed thresholds |
| Moderate   | 10       | 20       | 30       | Default (D-004)    |

---

### 4.3 usage_sessions

**SRS Reference:** DR-003

Stores completed usage session records. Sessions are only persisted when they reach the ENDED state and have a duration of at least 60 seconds (overlay visibility threshold per FR-018).

| Column                | SQLite Type | Kotlin Type | Nullable | Default | Constraints                 | Description                                          |
|-----------------------|-------------|-------------|----------|---------|----------------------------|------------------------------------------------------|
| `id`                  | INTEGER     | Long        | No       | auto    | PRIMARY KEY AUTOINCREMENT   | Unique session identifier                             |
| `appPackageName`      | TEXT        | String      | No       | —       | NOT NULL                    | Package name of the monitored app                     |
| `startTimestamp`      | INTEGER     | Long        | No       | —       | NOT NULL                    | Session start time (epoch milliseconds)               |
| `endTimestamp`        | INTEGER     | Long        | No       | —       | NOT NULL                    | Session end time (epoch milliseconds)                 |
| `durationSeconds`     | INTEGER     | Int         | No       | —       | NOT NULL, value >= 0        | Total active duration in seconds (excludes cooldown pauses) |
| `maxThresholdReached` | INTEGER     | Int         | No       | —       | NOT NULL, value IN (0,1,2,3,4) | Highest visual state reached                        |
| `throbActivated`      | INTEGER     | Boolean     | No       | 0       | NOT NULL                    | Whether throb animation was triggered (1=true, 0=false) |

**maxThresholdReached Values:**

| Value | State   | Description                                   |
|-------|---------|-----------------------------------------------|
| 0     | —       | Below timer visibility (session < 60s)        |
| 1     | Calm    | Timer visible, below T1                       |
| 2     | Notice  | Reached T1 (Amber)                            |
| 3     | Alert   | Reached T2 (Orange)                           |
| 4     | Urgent  | Reached T3 (Red) / Throb (T3 + 5min)         |

**Room Entity:**

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
    val maxThresholdReached: Int,   // 0–4 (see table above)
    val throbActivated: Boolean = false
)
```

**SQL DDL:**

```sql
CREATE TABLE IF NOT EXISTS usage_sessions (
    id                  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
    appPackageName      TEXT    NOT NULL,
    startTimestamp      INTEGER NOT NULL,
    endTimestamp        INTEGER NOT NULL,
    durationSeconds     INTEGER NOT NULL,
    maxThresholdReached INTEGER NOT NULL,
    throbActivated      INTEGER NOT NULL DEFAULT 0
);

CREATE INDEX IF NOT EXISTS index_usage_sessions_appPackageName
    ON usage_sessions (appPackageName);

CREATE INDEX IF NOT EXISTS index_usage_sessions_startTimestamp
    ON usage_sessions (startTimestamp);
```

**Timestamp Notes:**
- All timestamps are stored as epoch milliseconds (`System.currentTimeMillis()`).
- Session duration uses `SystemClock.elapsedRealtime()` internally for immunity to wall-clock changes (EC-05), but `durationSeconds` is stored as a derived integer.
- Sessions spanning midnight (EC-10) are stored with correct start/end timestamps — no date-boundary splitting.

---

### 4.4 user_preferences

**SRS Reference:** DR-004

General-purpose key-value store for application preferences. All values are stored as TEXT and parsed by the consumer (repository layer handles type conversion).

| Column  | SQLite Type | Kotlin Type | Nullable | Default | Constraints    | Description                    |
|---------|-------------|-------------|----------|---------|----------------|--------------------------------|
| `key`   | TEXT        | String      | No       | —       | PRIMARY KEY    | Preference key name            |
| `value` | TEXT        | String      | No       | —       | NOT NULL       | Preference value (as string)   |

**Room Entity:**

```kotlin
@Entity(tableName = "user_preferences")
data class UserPreferenceEntity(
    @PrimaryKey
    val key: String,
    val value: String
)
```

**SQL DDL:**

```sql
CREATE TABLE IF NOT EXISTS user_preferences (
    `key`   TEXT NOT NULL PRIMARY KEY,
    value   TEXT NOT NULL
);
```

**Required Preference Keys (MVP):**

| Key                          | Stored Type | Domain Type | Default Value | Description                                 | Decision Ref |
|------------------------------|-------------|-------------|---------------|---------------------------------------------|-------------|
| `monitoring_active`          | `"true"`/`"false"` | Boolean | `"false"` | Whether the monitoring service should run    | —           |
| `onboarding_completed`       | `"true"`/`"false"` | Boolean | `"false"` | Whether first-launch onboarding is complete  | —           |
| `overlay_position_edge`      | `"left"`/`"right"` | String  | `"right"` | Last snapped edge for overlay position       | D-003       |
| `overlay_position_y_percent` | `"0.0"`–`"1.0"`    | Float   | `"0.5"`   | Last Y position as fraction of screen height | —           |
| `cooldown_duration_seconds`  | `"45"`              | Int     | `"45"`    | Cooldown window duration in seconds          | D-001       |

**Type Conversion (handled by UserPreferencesRepositoryImpl):**

| Method             | Conversion                              | Null Handling                 |
|--------------------|-----------------------------------------|-------------------------------|
| `getBoolean()`     | `String.toBooleanStrictOrNull()`        | Returns provided default      |
| `getFloat()`       | `String.toFloatOrNull()`               | Returns provided default      |
| `getInt()`         | `String.toIntOrNull()`                 | Returns provided default      |
| `getString()`      | Direct string return                    | Returns provided default      |

---

## 5. Indexes

| Table              | Index Name                                      | Columns            | Type     | Purpose                                         |
|--------------------|-------------------------------------------------|--------------------|----------|-------------------------------------------------|
| `monitored_apps`   | (primary key index)                              | `packageName`      | Unique   | Primary key lookup                              |
| `threshold_configs`| (primary key index)                              | `id`               | Unique   | Primary key lookup                              |
| `usage_sessions`   | (primary key index)                              | `id`               | Unique   | Primary key lookup                              |
| `usage_sessions`   | `index_usage_sessions_appPackageName`            | `appPackageName`   | Non-unique | Session queries by app (future dashboard)      |
| `usage_sessions`   | `index_usage_sessions_startTimestamp`             | `startTimestamp`   | Non-unique | Retention cleanup (DR-005), recent sessions    |
| `user_preferences` | (primary key index)                              | `key`              | Unique   | Preference key lookup                           |

**Index Justification:**

- `appPackageName` index on `usage_sessions`: Supports per-app session queries for the v1.1 dashboard feature. Even in MVP, this enables efficient `removeUninstalled` cleanup.
- `startTimestamp` index on `usage_sessions`: Supports the 90-day retention cleanup query (`DELETE WHERE endTimestamp < cutoff`) and `ORDER BY startTimestamp DESC` for recent session retrieval.

---

## 6. Relationships & Foreign Keys

### 6.1 Logical Relationships

| Relationship                              | Type | Mandatory | Enforcement Level |
|-------------------------------------------|------|-----------|-------------------|
| `usage_sessions.appPackageName` → `monitored_apps.packageName` | N:1  | Yes       | Application (repository layer) |
| `threshold_configs.appPackageName` → `monitored_apps.packageName` | N:1  | No (nullable) | Application (repository layer) |

### 6.2 Why Not Room @ForeignKey?

Room `@ForeignKey` with `onDelete = CASCADE` was considered and rejected for the following reasons:

1. **App Uninstall Handling (EH-001):** When a user uninstalls a monitored app, `MonitoredApp` records are cleaned up asynchronously via `removeUninstalled()`. With cascading deletes, this would silently destroy historical `UsageSession` data — which should be retained for the full 90-day retention window.
2. **Threshold Safety:** Deleting a `MonitoredApp` should not delete the global `ThresholdConfig` (which has `appPackageName = null`). A future v1.1 per-app config with cascading delete would need careful logic that is better handled in code.
3. **Insertion Order:** During onboarding, apps and sessions may be written in different transactions. Strict FK enforcement would require careful insertion ordering that adds complexity without meaningful benefit at this data scale.

### 6.3 Integrity Enforcement

Referential integrity is maintained through:

| Mechanism                          | Where                            | What It Prevents                        |
|------------------------------------|----------------------------------|-----------------------------------------|
| `removeUninstalled()` DAO query    | `MonitoredAppDao`                | Orphaned app records for uninstalled apps |
| `deleteOlderThan()` DAO query      | `UsageSessionDao`                | Unbounded session data growth           |
| Repository-level validation        | `ThresholdConfigRepositoryImpl`  | Multiple global configs, invalid thresholds |
| Repository-level mapping           | All `*RepositoryImpl` classes    | Type-safe domain ↔ entity conversion    |

---

## 7. Data Access Objects (DAOs)

### 7.1 MonitoredAppDao

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
```

**Query Details:**

| Method                | Return Type                      | Reactive | Thread   | Purpose                                        |
|-----------------------|----------------------------------|----------|----------|-------------------------------------------------|
| `getAll()`            | `Flow<List<MonitoredAppEntity>>` | Yes      | Auto     | UI app list, sorted case-insensitive (FR-003)   |
| `getActive()`         | `Flow<List<MonitoredAppEntity>>` | Yes      | Auto     | Service observes for match-set updates          |
| `getActivePackageNames()` | `List<String>`              | No       | IO       | Polling engine fast-path package lookup         |
| `upsert()`            | `Unit`                           | No       | IO       | Insert or update app record                     |
| `setActive()`         | `Unit`                           | No       | IO       | Toggle monitoring on/off for a single app       |
| `removeUninstalled()` | `Unit`                           | No       | IO       | Cleanup records for uninstalled apps (EH-001)   |

---

### 7.2 ThresholdConfigDao

```kotlin
@Dao
interface ThresholdConfigDao {

    @Query("SELECT * FROM threshold_configs WHERE isGlobal = 1 LIMIT 1")
    suspend fun getGlobal(): ThresholdConfigEntity?

    @Query("SELECT * FROM threshold_configs WHERE isGlobal = 1 LIMIT 1")
    fun observeGlobal(): Flow<ThresholdConfigEntity?>

    @Upsert
    suspend fun upsert(config: ThresholdConfigEntity)
}
```

**Query Details:**

| Method            | Return Type                         | Reactive | Thread | Purpose                                        |
|-------------------|-------------------------------------|----------|--------|-------------------------------------------------|
| `getGlobal()`     | `ThresholdConfigEntity?`            | No       | IO     | One-shot read for current thresholds            |
| `observeGlobal()` | `Flow<ThresholdConfigEntity?>`      | Yes      | Auto   | ThresholdEngine observes for live recalculation |
| `upsert()`        | `Unit`                              | No       | IO     | Save or update threshold config                 |

> **Note:** `getGlobal()` returns `null` when no config exists yet (first launch). The repository layer falls back to `ThresholdConfig.DEFAULT` (Moderate preset: 10/20/30).

---

### 7.3 UsageSessionDao

```kotlin
@Dao
interface UsageSessionDao {

    @Insert
    suspend fun insert(session: UsageSessionEntity): Long

    @Query("SELECT * FROM usage_sessions ORDER BY startTimestamp DESC LIMIT :limit")
    suspend fun getRecent(limit: Int = 100): List<UsageSessionEntity>

    @Query("DELETE FROM usage_sessions WHERE endTimestamp < :cutoffTimestamp")
    suspend fun deleteOlderThan(cutoffTimestamp: Long)
}
```

**Query Details:**

| Method              | Return Type                   | Reactive | Thread | Purpose                                         |
|---------------------|-------------------------------|----------|--------|-------------------------------------------------|
| `insert()`          | `Long` (row ID)               | No       | IO     | Persist completed session (ENDED state only)    |
| `getRecent()`       | `List<UsageSessionEntity>`    | No       | IO     | Retrieve recent sessions for display            |
| `deleteOlderThan()` | `Unit`                        | No       | IO     | 90-day retention cleanup (DR-005)               |

**Insert-Only Pattern:** Sessions use `@Insert` (not `@Upsert`) because sessions are write-once records. A session is only persisted when it transitions to the ENDED state — never updated after insertion (RA-013).

---

### 7.4 UserPreferencesDao

```kotlin
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

**Query Details:**

| Method      | Return Type       | Reactive | Thread | Purpose                                         |
|-------------|-------------------|----------|--------|-------------------------------------------------|
| `get()`     | `String?`         | No       | IO     | One-shot preference read                        |
| `observe()` | `Flow<String?>`   | Yes      | Auto   | Reactive observation (e.g., monitoring toggle)  |
| `set()`     | `Unit`            | No       | IO     | Insert or update a preference                   |

> **Note:** The `key` column name is a SQLite reserved word and must be quoted with backticks in queries.

---

## 8. Room Database Class

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

**Hilt Dependency Injection (DatabaseModule):**

```kotlin
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): MindfulScrollDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            MindfulScrollDatabase::class.java,
            "mindfulscroll.db"
        ).build()

    @Provides
    fun provideMonitoredAppDao(db: MindfulScrollDatabase): MonitoredAppDao =
        db.monitoredAppDao()

    @Provides
    fun provideThresholdConfigDao(db: MindfulScrollDatabase): ThresholdConfigDao =
        db.thresholdConfigDao()

    @Provides
    fun provideUsageSessionDao(db: MindfulScrollDatabase): UsageSessionDao =
        db.usageSessionDao()

    @Provides
    fun provideUserPreferencesDao(db: MindfulScrollDatabase): UserPreferencesDao =
        db.userPreferencesDao()
}
```

---

## 9. Entity-to-Domain Mapping

Room entities are **data-layer** types. The domain layer uses pure Kotlin models with no Room dependencies. Conversion is handled by extension-function mappers in `data/local/entity/Mappers.kt`.

### 9.1 MonitoredApp

```kotlin
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
```

### 9.2 ThresholdConfig

```kotlin
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
```

### 9.3 UsageSession

```kotlin
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

### 9.4 UserPreferences

User preferences do not have a 1:1 domain model mapping. Instead, the `UserPreferencesRepositoryImpl` provides typed get/set methods that parse the string values into appropriate domain types (Boolean, Int, Float, String).

---

## 10. Repository Layer

Repositories bridge the domain layer and data layer, ensuring all database operations flow through `Dispatchers.IO` and return domain types.

### 10.1 Repository Interfaces (domain layer)

| Interface                    | Source File                                 | Key Operations                              |
|------------------------------|---------------------------------------------|---------------------------------------------|
| `MonitoredAppRepository`     | `domain/repository/MonitoredAppRepository.kt` | getAll, getActive, getActivePackageNames, upsert, setActive, removeUninstalled |
| `ThresholdConfigRepository`  | `domain/repository/ThresholdConfigRepository.kt` | getGlobal, observeGlobal, save           |
| `UsageSessionRepository`     | `domain/repository/UsageSessionRepository.kt`  | insert, getRecent, deleteOlderThan       |
| `UserPreferencesRepository`  | `domain/repository/UserPreferencesRepository.kt` | getString/Boolean/Float/Int, setString/Boolean/Float/Int, observeString/Boolean |

### 10.2 Implementation Summary

| Implementation                      | Source File                                         | DAO Dependency        |
|--------------------------------------|-----------------------------------------------------|-----------------------|
| `MonitoredAppRepositoryImpl`         | `data/repository/MonitoredAppRepositoryImpl.kt`     | `MonitoredAppDao`     |
| `ThresholdConfigRepositoryImpl`      | `data/repository/ThresholdConfigRepositoryImpl.kt`  | `ThresholdConfigDao`  |
| `UsageSessionRepositoryImpl`         | `data/repository/UsageSessionRepositoryImpl.kt`     | `UsageSessionDao`     |
| `UserPreferencesRepositoryImpl`      | `data/repository/UserPreferencesRepositoryImpl.kt`  | `UserPreferencesDao`  |

### 10.3 Data Flow

```
UI/ViewModel ──► UseCase ──► Repository (interface) ──► RepositoryImpl ──► DAO ──► Room ──► SQLite
                                │                           │
                         domain layer                  data layer
                       (pure Kotlin)              (Android/Room deps)
```

**Reactive Flows:**

```
Room (SQLite change) ──► DAO (Flow emission) ──► RepositoryImpl (map to domain) ──► UseCase ──► ViewModel ──► UI recompose
```

---

## 11. Data Retention Policy

**SRS Reference:** DR-005

| Parameter             | Value                                                 |
|-----------------------|-------------------------------------------------------|
| Entity                | `usage_sessions` only                                 |
| Retention Period      | 90 days (defined as `AppConstants.RETENTION_DAYS = 90`) |
| Trigger               | App startup (Application.onCreate or first Activity)  |
| Execution Thread      | Background (`Dispatchers.IO`)                         |
| Blocking              | No — cleanup does not delay app startup (DR-005.AC2)  |
| Cleanup Query         | `DELETE FROM usage_sessions WHERE endTimestamp < :cutoffTimestamp` |
| Cutoff Calculation    | `System.currentTimeMillis() - (90 * 24 * 60 * 60 * 1000L)` |
| Configurability       | Retention period is a named constant (DR-005.AC3)     |

**Other Tables:**
- `monitored_apps`: Records are retained indefinitely. Uninstalled apps are cleaned up via `removeUninstalled()` (EH-001).
- `threshold_configs`: Records are retained indefinitely. Only the global config row exists in MVP.
- `user_preferences`: Records are retained indefinitely. No cleanup needed.

---

## 12. Data Volume Estimates

**SRS Reference:** Section 7.3

Based on typical usage patterns (assumption A-09: 100–500 KB/month session data growth):

| Entity            | Est. Records (90 days) | Avg. Row Size | Est. Total Size |
|-------------------|------------------------|---------------|-----------------|
| `monitored_apps`  | 1–30                   | ~150 bytes    | < 5 KB          |
| `threshold_configs` | 1–30                 | ~100 bytes    | < 3 KB          |
| `usage_sessions`  | 500–5,000              | ~60 bytes     | 100–500 KB      |
| `user_preferences`| 5–15                   | ~50 bytes     | < 1 KB          |
| **Total**         |                        |               | **< 600 KB**    |

**Growth Projection:**
- Primary growth driver: `usage_sessions` table.
- With 90-day retention, the table self-prunes and reaches a steady-state size.
- At maximum estimated volume (5,000 records), the database remains well under 1 MB.
- SQLite overhead (indexes, WAL file, journal) adds ~20–50% to raw data size.
- **Worst-case total database size: < 1 MB** (well within PER-002 storage requirement of < 1 MB data storage).

---

## 13. Migration Strategy

**Risk Reference:** RA-018 (Database Migration Failures, score 8)

### 13.1 Guiding Principles

1. **Never use `fallbackToDestructiveMigration()`** — user data must always be preserved.
2. **Export schema JSON** — enables automated comparison between versions.
3. **Manual `Migration` objects** — each migration is hand-written and tested.
4. **Forward compatibility** — v1 schema includes fields needed for v1.1.

### 13.2 Version History

| Version | Release  | Changes                                                           | Migration        |
|---------|----------|-------------------------------------------------------------------|------------------|
| 1       | MVP      | Initial schema (4 tables)                                         | N/A              |
| 2       | v1.1     | Add dashboard summary table; enforce per-app threshold FK         | `Migration(1, 2)` |

### 13.3 Forward Compatibility Measures (v1 → v1.1)

| Measure                                      | Field/Table                 | v1.1 Use Case                              |
|----------------------------------------------|-----------------------------|--------------------------------------------|
| `ThresholdConfig.appPackageName` exists       | `threshold_configs`         | Per-app threshold overrides                |
| `UsageSession` stores all needed fields       | `usage_sessions`            | Dashboard aggregation queries              |
| Schema JSON exported                          | Build output                | Automated diff for migration validation    |

### 13.4 Migration Testing Requirements

| Test                                          | Description                                          |
|-----------------------------------------------|------------------------------------------------------|
| Schema validation                              | Room's `MigrationTestHelper` verifies schema matches |
| Data preservation                              | Pre-migration data survives post-migration           |
| Downgrade prevention                           | No downgrade path — app version only increases       |
| Empty database                                 | Migration handles fresh install (no-op)              |

---

## 14. Performance Requirements

**SRS References:** NFR-005, NFR-008

| Requirement    | Target                  | Measured By                          |
|----------------|-------------------------|--------------------------------------|
| Read latency   | ≤ 100ms per query       | Android Profiler / Room logging      |
| Write latency  | ≤ 100ms per operation   | Android Profiler / Room logging      |
| Thread safety  | All DB ops off main thread | `Dispatchers.IO` in all repositories |
| WAL mode       | Enabled                 | Room default (NFR-008.AC1)           |
| Concurrent reads | Non-blocking          | WAL allows concurrent readers        |

### 14.1 Query Performance Considerations

| Query                           | Expected Rows Scanned | Index Used       | Expected Latency |
|---------------------------------|-----------------------|------------------|------------------|
| `getActivePackageNames()`       | 1–30                  | Full table scan  | < 1ms            |
| `getAll()` (monitored_apps)     | 1–30                  | Full table scan  | < 1ms            |
| `getGlobal()` (threshold)       | 1                     | Full table scan  | < 1ms            |
| `getRecent(100)` (sessions)     | Up to 100             | `startTimestamp`  | < 5ms            |
| `deleteOlderThan()` (retention) | 0–5,000               | `startTimestamp`  | < 50ms           |
| `insert()` (session)            | N/A                   | N/A              | < 5ms            |
| `upsert()` (preference)         | 1                     | Primary key      | < 1ms            |

### 14.2 Caching Strategy

The repository layer does **not** implement an in-memory cache for database queries. This is intentional:
- Data volume is small enough (< 600 KB) that Room's built-in SQLite page cache is sufficient.
- Room's `Flow` emissions provide automatic UI updates without manual cache invalidation.
- The polling engine's `getActivePackageNames()` returns a small list (1–30 items) with sub-millisecond latency.

The monitoring service caches `activePackageNames` in-memory (refreshed on Flow emission) to avoid per-poll database hits — this is handled at the service/engine layer, not the repository layer.

---

## 15. Data Protection & Privacy

**SRS References:** NFR-011, NFR-012

| Measure                        | Implementation                                          | Requirement   |
|-------------------------------|---------------------------------------------------------|---------------|
| Storage location              | App private internal storage only                       | NFR-012       |
| External storage              | Never used — no files written outside app sandbox       | NFR-012       |
| Network access                | No INTERNET permission — zero data transmission         | NFR-011, C-12 |
| Backup                        | `android:allowBackup="false"` in AndroidManifest.xml    | NFR-012       |
| Encryption at rest            | Not required (data is non-sensitive usage timestamps)   | —             |
| Data classification           | All stored data is usage metadata — no PII beyond app package names | —    |
| User data deletion            | Uninstalling the app removes all data (private storage) | NFR-012       |

### 15.1 Data Stored Summary

| Data Type                  | Sensitivity | Contains PII? | Notes                              |
|----------------------------|-------------|---------------|------------------------------------|
| App package names          | Low         | Indirect      | Reveals which apps are installed   |
| Session timestamps         | Low         | Indirect      | Reveals usage patterns             |
| Session durations          | Low         | No            | Aggregate time data                |
| Threshold settings         | None        | No            | User preferences only              |
| Overlay position           | None        | No            | UI state                           |

---

## 16. Seed Data & Defaults

### 16.1 Initial State (Fresh Install)

On first launch, the database is created empty. No seed data is inserted automatically.

| Table                | Initial Rows | Notes                                                |
|----------------------|-------------|------------------------------------------------------|
| `monitored_apps`     | 0           | Populated during onboarding app selection            |
| `threshold_configs`  | 0           | Created on first threshold save; defaults from code  |
| `usage_sessions`     | 0           | Populated as sessions complete                       |
| `user_preferences`   | 0           | Created on first write; defaults from code           |

### 16.2 Application-Level Defaults

When a row does not yet exist, the repository layer returns hardcoded defaults:

| Setting                       | Default Value              | Defined In                     |
|-------------------------------|----------------------------|--------------------------------|
| Global threshold config       | Moderate (10/20/30 min)    | `ThresholdConfig.DEFAULT`      |
| Monitoring active             | `false`                    | `AppConstants.DEFAULT_MONITORING_ACTIVE` |
| Onboarding completed          | `false`                    | `AppConstants.DEFAULT_ONBOARDING_COMPLETED` |
| Overlay position edge         | `"right"`                  | `AppConstants.DEFAULT_OVERLAY_EDGE` |
| Overlay position Y percent    | `0.5`                      | `AppConstants.DEFAULT_OVERLAY_Y_PERCENT` |
| Cooldown duration seconds     | `45`                       | `AppConstants.DEFAULT_COOLDOWN_SECONDS` |

---

## 17. Error Handling

| Error Scenario                 | Detection                        | Response                                                | SRS Ref |
|-------------------------------|----------------------------------|---------------------------------------------------------|---------|
| Database write failure         | Room throws exception            | Log error; in-memory state continues; retry next opportunity | EH-006  |
| Disk full                      | IOException from Room            | Log error; do not crash; degrade gracefully             | EH-006  |
| Corrupted database             | SQLiteException on open          | Room's default corruption handler recreates database    | —       |
| Schema mismatch (no migration) | IllegalStateException            | Prevented by always providing Migration objects         | RA-018  |
| Concurrent write conflict      | WAL mode handles                 | Non-blocking; WAL allows one writer + many readers      | NFR-008 |
| Session data lost on kill      | Service killed mid-session       | Session not persisted (only saved on ENDED state)       | RA-013  |

---

## 18. Traceability Matrix

### 18.1 SRS Data Requirement → Schema Mapping

| SRS Requirement | Schema Element          | Fully Addressed? |
|-----------------|--------------------------|-----------------|
| DR-001          | `monitored_apps` table   | Yes             |
| DR-002          | `threshold_configs` table | Yes            |
| DR-003          | `usage_sessions` table   | Yes             |
| DR-004          | `user_preferences` table | Yes             |
| DR-005          | `deleteOlderThan()` DAO method + AppConstants.RETENTION_DAYS | Yes |

### 18.2 SRS NFR → Schema Design Mapping

| SRS NFR   | Schema Design Element                                          |
|-----------|----------------------------------------------------------------|
| NFR-005   | Indexes on usage_sessions; all queries < 100ms; IO thread      |
| NFR-008   | WAL mode; schema export; manual migrations; transaction support |
| NFR-011   | No INTERNET permission; all data in private storage            |
| NFR-012   | Private internal storage; allowBackup=false; no external files  |

### 18.3 Risk → Schema Mitigation Mapping

| Risk    | Schema Mitigation                                              |
|---------|----------------------------------------------------------------|
| RA-013  | Sessions only persisted on ENDED state (insert-only pattern)   |
| RA-018  | exportSchema=true; no destructive migration; manual Migration objects |
| RA-025  | Epoch millisecond timestamps; no date-boundary splitting       |

### 18.4 PRD Decision → Schema Mapping

| Decision | Schema Impact                                                  |
|----------|----------------------------------------------------------------|
| D-001    | `cooldown_duration_seconds` default = 45 in user_preferences   |
| D-003    | `overlay_position_edge` default = "right" in user_preferences  |
| D-004    | ThresholdConfig.DEFAULT = Moderate (10/20/30)                  |
| D-005    | RETENTION_DAYS = 90; deleteOlderThan() cleanup                 |
| ADR-006  | Room for ALL persistence; user_preferences table (not DataStore) |

### 18.5 ProjectPlan WBS → Schema Coverage

| WBS Task | Description                          | Schema Section        |
|----------|--------------------------------------|-----------------------|
| 2.1      | Room entities for all 4 data types   | Section 4 (all)       |
| 2.2      | MonitoredApp entity + DAO            | Sections 4.1, 7.1     |
| 2.3      | ThresholdConfig entity + DAO         | Sections 4.2, 7.2     |
| 2.4      | UsageSession entity + DAO            | Sections 4.3, 7.3     |
| 2.5      | UserPreferences entity + DAO         | Sections 4.4, 7.4     |
| 2.6      | Database class + DI module           | Section 8             |
| 2.7      | MonitoredAppRepository               | Section 10            |
| 2.8      | ThresholdConfigRepository            | Section 10            |
| 2.9      | UsageSessionRepository               | Section 10            |
| 2.10     | Session retention cleanup            | Section 11            |
| 2.11     | Data layer unit tests                | Section 13.4          |

---

## 19. References

| Document                      | Sections Referenced                            |
|-------------------------------|------------------------------------------------|
| [PRD.md](PRD.md)              | Constraints C-11, C-12; Decision D-001–D-005; Assumption A-09 |
| [SRS.md](SRS.md)              | Section 7 (DR-001–DR-005); NFR-005, NFR-008, NFR-011, NFR-012; EH-001, EH-006; EC-05, EC-10 |
| [SAD.md](SAD.md)              | Section 8 (Data Architecture); ADR-006         |
| [TDD.md](TDD.md)             | Section 4 (Data Layer Design); Section 13 (DI Modules); Section 15 (Performance) |
| [ProjectPlan.md](ProjectPlan.md) | Phase 2 (Data Layer) WBS tasks 2.1–2.11     |
| [RiskAssessment.md](RiskAssessment.md) | RA-013, RA-018, RA-025                 |

---

*End of Document*
