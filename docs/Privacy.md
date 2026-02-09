# Privacy & Data Handling Document

## MindfulScroll — Digital Awareness Companion

---

### Document Control

| Field              | Value                                              |
| ------------------ | -------------------------------------------------- |
| **Document ID**    | DOC-009                                            |
| **Version**        | 1.0                                                |
| **Date**           | 2026-02-07                                         |
| **Status**         | Draft                                              |
| **Parent Docs**    | PRD.md, SRS.md, SAD.md, TDD.md, DbSchema.md, UIUXspec.md, RiskAssessment.md, ProjectPlan.md |
| **Audience**       | Developers, QA, Project Owner, Legal, Google Play Reviewers |

### Revision History

| Version | Date       | Author | Changes       |
| ------- | ---------- | ------ | ------------- |
| 1.0     | 2026-02-07 | Rudy   | Initial draft |

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Privacy Philosophy](#2-privacy-philosophy)
3. [Data Inventory](#3-data-inventory)
4. [Data Collection & Processing](#4-data-collection--processing)
5. [Data Storage](#5-data-storage)
6. [Data Retention & Deletion](#6-data-retention--deletion)
7. [Network & Data Transmission](#7-network--data-transmission)
8. [Permissions](#8-permissions)
9. [Data Protection Architecture](#9-data-protection-architecture)
10. [Threat Model](#10-threat-model)
11. [Data Flow Diagrams](#11-data-flow-diagrams)
12. [User Rights & Controls](#12-user-rights--controls)
13. [Third-Party Dependencies](#13-third-party-dependencies)
14. [Google Play Policy Compliance](#14-google-play-policy-compliance)
15. [Children's Privacy](#15-childrens-privacy)
16. [Privacy by Design Verification](#16-privacy-by-design-verification)
17. [User-Facing Privacy Policy (Draft)](#17-user-facing-privacy-policy-draft)
18. [Traceability Matrix](#18-traceability-matrix)
19. [References](#19-references)

---

## 1. Introduction

### 1.1 Purpose

This document defines the complete privacy and data handling strategy for MindfulScroll MVP (v1.0). It specifies what data is collected, how it is processed, where it is stored, how long it is retained, and what protections are in place. It serves three audiences:

1. **Developers** — Ensuring every line of code respects the privacy constraints.
2. **Google Play Reviewers** — Demonstrating that the use of `PACKAGE_USAGE_STATS` is justified, proportionate, and transparent.
3. **End Users** — Providing clear, honest information about how their data is handled (Section 17).

### 1.2 Scope

This document covers all data handling in MindfulScroll MVP (v1.0), including:

- Data collected through `UsageStatsManager` (foreground app detection)
- Data persisted in the local Room database
- Data displayed in the floating overlay and in-app screens
- Data lifecycle from creation through deletion

### 1.3 Core Privacy Commitment

**MindfulScroll never transmits any data off the device. It has no network capability whatsoever. The `INTERNET` permission is explicitly absent from the manifest.** This is not a policy choice — it is a technical enforcement built into the architecture (SRS NFR-011, SAD ADR-009).

---

## 2. Privacy Philosophy

### 2.1 Guiding Principles

MindfulScroll's privacy stance is derived from PRD Product Principle P4 (Transparency) and reinforced across all design documents.

| # | Principle                           | Implementation                                                              |
|---|-------------------------------------|-----------------------------------------------------------------------------|
| 1 | **No data leaves the device**       | No `INTERNET` permission. No network libraries. Zero outbound connections.  |
| 2 | **No analytics or tracking**        | No Firebase, no Crashlytics, no Sentry, no custom analytics (ADR-009).      |
| 3 | **No cloud sync or backup**         | `android:allowBackup="false"`. No server-side components (PRD Section 11.2). |
| 4 | **Minimal data collection**         | Only data necessary for the core function (foreground detection + session timing). |
| 5 | **User controls all data**          | User chooses which apps to monitor. User can stop monitoring at any time.   |
| 6 | **Transparent about access**        | Plain-language explanations during onboarding for every permission requested. |
| 7 | **Data has an expiration**          | Session records are automatically deleted after 90 days (DR-005).           |
| 8 | **Uninstall removes everything**    | All data is in app-private storage and is deleted when the app is uninstalled. |

### 2.2 What MindfulScroll Does NOT Do

| Action                                     | Status           | Enforcement                               |
|--------------------------------------------|------------------|--------------------------------------------|
| Send data to any server                    | **Never**        | No INTERNET permission in manifest         |
| Track browsing history or web activity     | **Never**        | Only foreground package name is read       |
| Read notification content                  | **Never**        | No notification listener permission        |
| Capture screen content                     | **Never**        | No screenshot or accessibility APIs        |
| Record keystrokes or text input            | **Never**        | No accessibility or input method services  |
| Share data with third parties              | **Never**        | No SDKs, no exported providers             |
| Use advertising identifiers               | **Never**        | No ad libraries, no Google Advertising ID  |
| Create user profiles or accounts           | **Never**        | No authentication, no user identity        |
| Access contacts, photos, or files          | **Never**        | No storage or contacts permissions         |
| Access location data                       | **Never**        | No location permissions                    |
| Monitor app content or in-app behavior     | **Never**        | Only the package name of the foreground app is read |

### 2.3 Architectural Decision References

| Decision | Description                              | Privacy Impact                                           |
|----------|------------------------------------------|----------------------------------------------------------|
| ADR-006  | Room for ALL persistence (not DataStore) | Single storage mechanism, easy to audit and verify       |
| ADR-009  | No crash reporting SDK                   | No network dependency, no data exfiltration risk         |
| C-11     | All data stored locally using Room       | No external storage, no shared directories               |
| C-12     | Must function fully offline              | No network dependency whatsoever                         |

---

## 3. Data Inventory

### 3.1 Complete Data Inventory

MindfulScroll stores four categories of data, all in a single Room database (`mindfulscroll.db`) in app-private internal storage.

#### 3.1.1 Monitored App Records

| Field          | Data Type | Sensitivity | Contains PII? | Purpose                                |
|----------------|-----------|-------------|---------------|----------------------------------------|
| `packageName`  | String    | Low         | Indirect      | Identifies which app to monitor        |
| `displayName`  | String    | Low         | No            | Human-readable name for display        |
| `isActive`     | Boolean   | None        | No            | Whether currently selected             |
| `addedAt`      | Long      | Low         | No            | When the user first selected this app  |

**SRS Reference:** DR-001

**Privacy Note:** Package names reveal which apps are installed on the device. This is considered indirect PII because it could theoretically infer user interests. However, this data never leaves the device and is only used for the core monitoring function.

#### 3.1.2 Threshold Configuration

| Field            | Data Type | Sensitivity | Contains PII? | Purpose                                |
|------------------|-----------|-------------|---------------|----------------------------------------|
| `id`             | Int       | None        | No            | Internal identifier                    |
| `presetName`     | String?   | None        | No            | "Light", "Moderate", or null           |
| `threshold1Min`  | Int       | None        | No            | First threshold in minutes             |
| `threshold2Min`  | Int       | None        | No            | Second threshold in minutes            |
| `threshold3Min`  | Int       | None        | No            | Third threshold in minutes             |
| `isGlobal`       | Boolean   | None        | No            | Global vs. per-app config              |
| `appPackageName` | String?   | None        | No            | Per-app override (v1.1 forward compat) |

**SRS Reference:** DR-002

**Privacy Note:** Threshold settings contain no personal information. They are purely user-defined preferences.

#### 3.1.3 Usage Session Records

| Field                 | Data Type | Sensitivity | Contains PII? | Purpose                                |
|-----------------------|-----------|-------------|---------------|----------------------------------------|
| `id`                  | Long      | None        | No            | Internal identifier                    |
| `appPackageName`      | String    | Low         | Indirect      | Which app was used                     |
| `startTimestamp`      | Long      | Low         | Indirect      | When the session started               |
| `endTimestamp`        | Long      | Low         | Indirect      | When the session ended                 |
| `durationSeconds`     | Int       | Low         | No            | How long the session lasted            |
| `maxThresholdReached` | Int       | None        | No            | Highest visual escalation state        |
| `throbActivated`      | Boolean   | None        | No            | Whether throb animation triggered      |

**SRS Reference:** DR-003

**Privacy Note:** Session records contain indirect PII: a combination of app package name + timestamps reveals a user's app usage patterns (e.g., "User opened Instagram at 9:15 PM and used it for 25 minutes"). This data is retained for 90 days and automatically deleted afterward (DR-005). It never leaves the device.

#### 3.1.4 User Preferences

| Key                            | Value Type | Sensitivity | Contains PII? | Purpose                          |
|--------------------------------|------------|-------------|---------------|----------------------------------|
| `monitoring_active`            | Boolean    | None        | No            | Whether service is running       |
| `onboarding_completed`        | Boolean    | None        | No            | Whether onboarding is done       |
| `overlay_position_edge`        | String     | None        | No            | "left" or "right" snap edge      |
| `overlay_position_y_percent`   | Float      | None        | No            | Vertical position (0.0–1.0)      |
| `cooldown_duration_seconds`    | Int        | None        | No            | Cooldown window (45 seconds)     |

**SRS Reference:** DR-004

**Privacy Note:** User preferences contain no personal information. They are UI state values.

### 3.2 Data Sensitivity Summary

| Category           | Record Count (est.) | Storage Size (est.) | Contains PII? | Retention       |
|--------------------|---------------------|---------------------|---------------|-----------------|
| Monitored Apps     | 1–30                | < 5 KB              | Indirect      | Indefinite      |
| Threshold Configs  | 1–30                | < 3 KB              | No            | Indefinite      |
| Usage Sessions     | 500–5,000           | 100–500 KB          | Indirect      | 90 days         |
| User Preferences   | 5–15                | < 1 KB              | No            | Indefinite      |
| **Total**          |                     | **< 600 KB**        |               |                 |

### 3.3 Data NOT Collected

The following data categories are explicitly never accessed, collected, or stored:

| Data Category             | API That Would Access It           | MindfulScroll Uses It? |
|---------------------------|------------------------------------|------------------------|
| App content (text, images)| AccessibilityService, MediaProjection | **No**              |
| Notification content      | NotificationListenerService        | **No**                 |
| Browsing history          | ContentProvider (Browser)          | **No**                 |
| Contacts                  | ContactsContract                   | **No**                 |
| Location                  | LocationManager, FusedLocation     | **No**                 |
| Camera or microphone      | Camera2, MediaRecorder             | **No**                 |
| Files or photos           | MediaStore, SAF                    | **No**                 |
| SMS or call logs          | Telephony, CallLog                 | **No**                 |
| Device identifiers        | TelephonyManager, Settings.Secure  | **No**                 |
| Advertising ID            | AdvertisingIdClient                | **No**                 |
| Keystroke data            | InputMethodService                 | **No**                 |
| Clipboard content         | ClipboardManager                   | **No**                 |
| Sensor data               | SensorManager                      | **No**                 |

---

## 4. Data Collection & Processing

### 4.1 How Data Is Collected

| Data                   | Collection Method                        | Trigger                           | SRS Ref |
|------------------------|------------------------------------------|-----------------------------------|---------|
| Foreground app name    | `UsageStatsManager.queryEvents()`        | Every 2 seconds while screen is on | FR-009  |
| App selection          | User taps checkbox in App Selection      | Direct user action                | FR-004  |
| Threshold settings     | User enters values in Threshold Config   | Direct user action                | FR-025  |
| Session records        | System generates when session ends       | Session state machine → ENDED     | FR-015  |
| Overlay position       | System captures after drag-and-snap      | Direct user action (drag)         | FR-023  |

### 4.2 UsageStatsManager Data Access

`UsageStatsManager` is the most sensitive API used by MindfulScroll. Here is a precise description of how it is used:

**What is queried:**

```
usageStatsManager.queryEvents(now - 5000ms, now)
```

This returns a list of `UsageEvents.Event` objects within the last 5 seconds. MindfulScroll reads only:

- `event.eventType` — checking for `MOVE_TO_FOREGROUND` (value = 1)
- `event.packageName` — the package name of the app that moved to foreground

**What is NOT accessed:**

| UsageStatsManager Capability       | Used by MindfulScroll? |
|------------------------------------|------------------------|
| `queryEvents()` — event type       | Yes (MOVE_TO_FOREGROUND only) |
| `queryEvents()` — package name     | Yes                    |
| `queryEvents()` — other event types| No (ignored)           |
| `queryUsageStats()` — daily stats  | Fallback only          |
| `queryUsageStats()` — weekly stats | No                     |
| `queryUsageStats()` — monthly stats| No                     |
| `queryUsageStats()` — yearly stats | No                     |
| `queryConfigurations()`            | No                     |
| `queryEventStats()`               | No                     |
| App usage duration from OS         | No (calculated internally) |

**Data lifetime in memory:**

- The foreground package name is held in a `StateFlow<String?>` variable.
- It is overwritten every 2 seconds by the next poll.
- It is never persisted directly — only the `appPackageName` is stored in the `usage_sessions` table when a session ends.
- When the screen is off, polling stops and the in-memory value is cleared to `null`.

### 4.3 Fallback Query

If `queryEvents()` returns no results (known to occur on some OEM devices — RA-007), MindfulScroll falls back to:

```
usageStatsManager.queryUsageStats(INTERVAL_DAILY, now - 10000ms, now)
```

This returns aggregated usage statistics. MindfulScroll reads only the `lastTimeUsed` field to identify the most recently used app. No aggregated duration, launch count, or other statistical data is read or stored.

### 4.4 Data Processing

MindfulScroll performs the following processing on collected data:

| Processing                        | Input                     | Output                   | Stored? |
|-----------------------------------|---------------------------|--------------------------|---------|
| Foreground detection              | UsageStats event          | Package name (String)    | No (in-memory only) |
| Monitored app matching            | Package name + active set | Boolean (match/no match) | No      |
| Session timing                    | `SystemClock.elapsedRealtime()` | Elapsed seconds (Int) | Yes (on session end) |
| Threshold evaluation              | Elapsed seconds + config  | Visual state (enum)      | Yes (max state, on session end) |
| Timer formatting                  | Elapsed seconds           | "MM:SS" string           | No (display only) |
| Position calculation              | Touch coordinates         | Edge + Y-percent         | Yes (user preference) |

No machine learning, profiling, fingerprinting, or behavioral analysis is performed on any data.

---

## 5. Data Storage

### 5.1 Storage Location

| Parameter         | Value                                                                    |
|-------------------|--------------------------------------------------------------------------|
| **Storage type**  | Android app-private internal storage                                     |
| **Path**          | `/data/data/com.rudy.mindfulscroll/databases/mindfulscroll.db`           |
| **Library**       | Room (AndroidX Persistence)                                              |
| **Format**        | SQLite database with WAL (Write-Ahead Logging) journal mode              |
| **Encryption**    | Not encrypted at the application level (Android OS encrypts at rest on devices with FDE/FBE) |
| **Access**        | Only accessible to MindfulScroll's own process (Android sandbox)         |

### 5.2 Storage Isolation

| Isolation Layer          | Mechanism                                                             | SRS Ref      |
|--------------------------|-----------------------------------------------------------------------|--------------|
| Process isolation        | Android application sandbox (each app runs in its own Linux process)  | OS-enforced  |
| File system isolation    | App-private internal storage (`/data/data/<package>/`)                | NFR-012.AC1  |
| No external storage      | Zero writes to SD card, shared directories, or Downloads              | NFR-012.AC2  |
| No content providers     | No exported `ContentProvider` that could expose data                  | NFR-012.AC3  |
| No exported components   | Only the main launcher `Activity` is exported                         | NFR-012.AC4  |
| No backup                | `android:allowBackup="false"` in AndroidManifest.xml                  | NFR-012.AC5  |

### 5.3 What Happens to Data on Uninstall

When the user uninstalls MindfulScroll:

1. Android OS deletes the entire `/data/data/com.rudy.mindfulscroll/` directory.
2. This includes the `mindfulscroll.db` database and all WAL/journal files.
3. No data survives uninstallation because:
   - `android:allowBackup="false"` prevents cloud backup to Google Drive.
   - No data is written to external storage or shared directories.
   - No data is stored on any server.
4. **Result: Complete data deletion with zero residual.**

### 5.4 Encryption Considerations

MindfulScroll does not implement application-level database encryption. Rationale:

| Factor                        | Assessment                                                         |
|-------------------------------|--------------------------------------------------------------------|
| Data sensitivity              | Low — usage timestamps and app package names, no passwords or financial data |
| Device encryption             | Android provides Full Disk Encryption (FDE) or File-Based Encryption (FBE) on all supported devices (API 26+) |
| Performance impact            | SQLCipher would add ~5–10% database operation overhead             |
| APK size impact               | SQLCipher adds ~3–4 MB to the APK                                 |
| Risk of data exposure         | Minimal — data is in app-private storage, no network, no exports   |
| Decision                      | OS-level encryption is sufficient for the data sensitivity level   |

If a future version stores higher-sensitivity data, application-level encryption should be reconsidered.

---

## 6. Data Retention & Deletion

### 6.1 Retention Policy

| Data Category       | Retention Period | Deletion Trigger                      | SRS Ref |
|---------------------|------------------|---------------------------------------|---------|
| Monitored apps      | Indefinite       | User deselects or app is uninstalled  | EH-001  |
| Threshold configs   | Indefinite       | User modifies (overwritten via UPSERT)| —       |
| Usage sessions      | **90 days**      | Automatic cleanup on app startup      | DR-005  |
| User preferences    | Indefinite       | Overwritten on change                 | —       |
| All data            | Until uninstall  | Android OS deletes on uninstall       | —       |

### 6.2 Session Cleanup Implementation

```
On each app startup:
1. Calculate cutoff = now - (90 days in milliseconds)
2. Execute: DELETE FROM usage_sessions WHERE endTimestamp < cutoff
3. This runs on a background thread (Dispatchers.IO)
4. It does not delay app startup (DR-005.AC2)
5. The retention period (90 days) is defined as a named constant (DR-005.AC3)
```

**Constant:** `AppConstants.SESSION_RETENTION_DAYS = 90`

### 6.3 User-Initiated Data Deletion

In MVP, the user can delete all MindfulScroll data by uninstalling the app. The following additional deletion mechanisms are planned:

| Version | Feature                              | Description                                |
|---------|--------------------------------------|--------------------------------------------|
| MVP     | Uninstall                            | Removes all data (OS-enforced)             |
| v1.1    | "Clear session history" button       | Deletes all `usage_sessions` records       |
| v1.1    | "Reset all data" button              | Clears all 4 tables, resets to fresh state |

### 6.4 Uninstalled App Cleanup

When a monitored app is uninstalled from the device:

1. On the next polling cycle or App Selection screen load, the app's package is no longer resolvable via `PackageManager`.
2. The `removeUninstalled()` DAO method removes the `MonitoredApp` record (EH-001).
3. Historical `UsageSession` records for that app are **retained** (they contain useful historical data and will be auto-deleted after 90 days).
4. No user notification is shown — the cleanup is silent.

---

## 7. Network & Data Transmission

### 7.1 Zero-Network Architecture

MindfulScroll has **zero network capability**. This is enforced at multiple architectural layers:

| Layer           | Enforcement                                                            | SRS Ref     |
|-----------------|------------------------------------------------------------------------|-------------|
| **Manifest**    | `android.permission.INTERNET` is **not declared**                      | NFR-011.AC1 |
| **Dependencies**| No OkHttp, Retrofit, Ktor, Volley, or any HTTP client in `build.gradle.kts` | NFR-011.AC2 |
| **Code**        | Zero usage of `java.net.*`, `javax.net.*`, `okhttp3.*`, or any network APIs | NFR-011.AC3 |
| **Verification**| Static analysis (lint) confirms zero network API usage                  | NFR-011.AC3 |

### 7.2 What "No Internet" Means in Practice

| Scenario                                | Behavior                                                    |
|-----------------------------------------|-------------------------------------------------------------|
| User opens MindfulScroll with no WiFi   | App works identically — no network needed                   |
| User opens MindfulScroll in airplane mode| App works identically — no network needed                  |
| A network call is attempted in code     | **Impossible** — no INTERNET permission means the OS blocks any socket attempt |
| An analytics SDK is added by a developer| **Build fails** — no INTERNET permission in manifest        |
| A crash occurs                          | Logged to `logcat` only — no remote reporting               |

### 7.3 No Third-Party Data Sharing

MindfulScroll does not share data with any third party. There are no:

- Analytics SDKs (Google Analytics, Firebase, Mixpanel, Amplitude, etc.)
- Crash reporting SDKs (Crashlytics, Sentry, Bugsnag, etc.)
- Advertising SDKs (AdMob, Unity Ads, Meta Audience Network, etc.)
- Attribution SDKs (Adjust, AppsFlyer, Branch, etc.)
- Social SDKs (Facebook SDK, Google Sign-In, etc.)
- Any other SDK that transmits data off-device

---

## 8. Permissions

### 8.1 Permission Inventory

| Permission                               | Android Name                               | Level        | Why Needed                                    | Data Accessed                      |
|------------------------------------------|--------------------------------------------|-------------|-----------------------------------------------|-------------------------------------|
| Usage Access                             | `PACKAGE_USAGE_STATS`                      | AppOp       | Detect which app is in the foreground          | Foreground app package name only    |
| Display Over Other Apps                  | `SYSTEM_ALERT_WINDOW`                      | AppOp       | Show the floating timer overlay                | None — displays UI only             |
| Foreground Service                       | `FOREGROUND_SERVICE`                       | Normal      | Run the monitoring service continuously        | None — service lifecycle only       |
| Foreground Service (Special Use)         | `FOREGROUND_SERVICE_SPECIAL_USE`           | Normal      | Declare service type for API 34+ compliance    | None — manifest declaration only    |
| Boot Completed                           | `RECEIVE_BOOT_COMPLETED`                   | Normal      | Auto-restart monitoring after device reboot    | None — receives broadcast only      |
| Battery Optimization Exemption           | `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`     | Normal      | Prevent OS from killing the monitoring service | None — system setting only          |
| Post Notifications (API 33+)             | `POST_NOTIFICATIONS`                       | Dangerous   | Display the required foreground service notification | None — displays notification only |

### 8.2 Permission NOT Requested

The following permissions are explicitly NOT declared and will never be added:

| Permission              | Why NOT Declared                                                   |
|-------------------------|--------------------------------------------------------------------|
| `INTERNET`              | Core privacy commitment — no data transmission                     |
| `ACCESS_FINE_LOCATION`  | Location is irrelevant to screen-time awareness                    |
| `ACCESS_COARSE_LOCATION`| Location is irrelevant to screen-time awareness                    |
| `READ_CONTACTS`         | Contact data is irrelevant                                         |
| `CAMERA`                | Camera access is irrelevant                                        |
| `RECORD_AUDIO`          | Audio recording is irrelevant                                      |
| `READ_EXTERNAL_STORAGE` | No need to access files                                            |
| `WRITE_EXTERNAL_STORAGE`| All data stays in app-private internal storage                     |
| `READ_PHONE_STATE`      | Telephony data is irrelevant                                       |
| `READ_SMS`              | SMS content is irrelevant                                          |
| `ACCESS_WIFI_STATE`     | Network state is irrelevant (fully offline)                        |
| `BIND_ACCESSIBILITY_SERVICE` | Never reads screen content                                    |
| `BIND_NOTIFICATION_LISTENER_SERVICE` | Never reads notification content                      |

### 8.3 PACKAGE_USAGE_STATS Justification

`PACKAGE_USAGE_STATS` is the most sensitive permission requested by MindfulScroll. This section provides the detailed justification required for Google Play's policy declaration.

**Why this permission is needed:**

MindfulScroll's core function is to show a floating timer when the user is actively using a monitored app. To determine which app is in the foreground, the app must query `UsageStatsManager`. There is no alternative Android API that provides this information without `PACKAGE_USAGE_STATS`.

**How the permission is used:**

1. The app calls `usageStatsManager.queryEvents(now - 5000ms, now)` every 2 seconds.
2. It reads only the `MOVE_TO_FOREGROUND` event type and the corresponding `packageName`.
3. It compares the package name against the user's selected list of monitored apps.
4. If the foreground app is monitored, a session timer begins.
5. The foreground package name is held transiently in memory and overwritten every 2 seconds.
6. Only the monitored app's package name is persisted — and only when a session ends.

**What the permission is NOT used for:**

- It is NOT used to collect or aggregate usage statistics for any purpose.
- It is NOT used to monitor app usage patterns for analytics or behavioral profiling.
- It is NOT used to report usage data to any server, service, or third party.
- It is NOT used to identify, fingerprint, or track the user.
- It is NOT used to access any data beyond the foreground app's package name.

**Privacy safeguards in place:**

1. No `INTERNET` permission — data cannot leave the device.
2. No third-party SDKs — no code path exists for data exfiltration.
3. No exported components — no other app can query MindfulScroll's data.
4. 90-day automatic data retention limit on session records.
5. User chooses which apps to monitor — only those apps' usage is tracked.
6. User can stop monitoring at any time with a single tap.

### 8.4 Permission Transparency in the App

Each permission is explained to the user during onboarding with plain, non-technical language (SRS NFR-009):

| Permission                | User-Facing Explanation                                                |
|---------------------------|------------------------------------------------------------------------|
| Usage Access              | "To detect which app you're using, MindfulScroll needs access to usage data. This data stays on your device. We never send it anywhere." |
| Display Over Other Apps   | "To show the floating timer while you use other apps, MindfulScroll needs permission to draw over other apps." |
| Battery Optimization      | "To keep monitoring reliably in the background, MindfulScroll needs to be excluded from battery optimization." |

Each explanation includes a "What this means" card that clarifies:
- What the app CAN see with this permission
- What the app CANNOT see
- That no data leaves the phone

Users can skip any permission with a warning about the impact (SRS FR-032.AC7).

---

## 9. Data Protection Architecture

### 9.1 Defense-in-Depth Model

MindfulScroll implements a 6-layer data protection architecture (SAD Section 15.2):

```
Layer 1: MANIFEST
    └── No INTERNET permission declared
         └── OS-level enforcement: all socket operations are blocked

Layer 2: DEPENDENCIES
    └── No network libraries in build.gradle.kts
         └── No OkHttp, Retrofit, Ktor, Volley, or HTTP clients

Layer 3: CODE VERIFICATION
    └── Zero usage of java.net.*, javax.net.*, okhttp3.*
         └── Verified via static analysis (lint)

Layer 4: STORAGE ISOLATION
    └── Room DB in Context.getDatabasePath()
         └── App-private internal directory only

Layer 5: IPC ISOLATION
    └── No exported ContentProviders
    └── No exported Services
    └── Only launcher Activity exported
    └── BootReceiver: exported=true but receives only BOOT_COMPLETED

Layer 6: BACKUP PREVENTION
    └── android:allowBackup="false"
         └── No cloud backup to Google Drive
```

### 9.2 AndroidManifest.xml Privacy Configuration

```xml
<manifest xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:tools="http://schemas.android.com/tools"
    package="com.rudy.mindfulscroll">

    <!-- Declared permissions (see Section 8.1 for justification) -->
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
    <uses-permission android:name="android.permission.FOREGROUND_SERVICE_SPECIAL_USE" />
    <uses-permission android:name="android.permission.RECEIVE_BOOT_COMPLETED" />
    <uses-permission android:name="android.permission.REQUEST_IGNORE_BATTERY_OPTIMIZATIONS" />
    <uses-permission android:name="android.permission.PACKAGE_USAGE_STATS"
        tools:ignore="ProtectedPermissions" />
    <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" />

    <!-- EXPLICITLY NOT DECLARED — NEVER ADD -->
    <!-- android.permission.INTERNET -->

    <application
        android:allowBackup="false">

        <!-- Only the main Activity is exported -->
        <activity
            android:name=".presentation.MainActivity"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.MAIN" />
                <category android:name="android.intent.category.LAUNCHER" />
            </intent-filter>
        </activity>

        <!-- Service is NOT exported -->
        <service
            android:name=".service.monitoring.MonitoringService"
            android:exported="false"
            android:foregroundServiceType="specialUse" />

        <!-- Receiver: exported for BOOT_COMPLETED only -->
        <receiver
            android:name=".service.receiver.BootReceiver"
            android:exported="true">
            <intent-filter>
                <action android:name="android.intent.action.BOOT_COMPLETED" />
            </intent-filter>
        </receiver>
    </application>
</manifest>
```

---

## 10. Threat Model

### 10.1 Threat Analysis

| # | Threat                                   | Attack Vector                          | Mitigation                                                    | Residual Risk |
|---|------------------------------------------|----------------------------------------|---------------------------------------------------------------|---------------|
| 1 | Data exfiltration via network            | Network socket call                    | No INTERNET permission — OS blocks all sockets                | None          |
| 2 | Data exfiltration via external storage   | Write to SD card / Downloads           | No external storage permissions or writes                     | None          |
| 3 | Data exfiltration via IPC                | Exported ContentProvider or Service    | No exported providers; only launcher Activity exported        | None          |
| 4 | Data exfiltration via backup             | Android backup to Google Drive         | `android:allowBackup="false"`                                 | None          |
| 5 | Unauthorized access by other apps        | Read from MindfulScroll's storage      | Android sandbox isolates app data directories                 | None          |
| 6 | Overlay clickjacking                     | Overlay captures taps intended for host| `FLAG_NOT_FOCUSABLE` — overlay cannot receive focus           | None          |
| 7 | Data exposure via rooted device          | Root access reads app-private storage  | Out of scope — rooted devices bypass all app-level protections | Accepted      |
| 8 | Data exposure via device theft           | Physical access to unlocked device     | OS-level encryption (FDE/FBE) protects at rest                | Low           |
| 9 | Supply chain attack via dependency       | Malicious library included             | Minimal dependencies; no network libraries; code review       | Low           |
| 10| Malicious fork of MindfulScroll         | Modified version adds INTERNET         | Out of scope — user responsibility for app source             | Accepted      |

### 10.2 Accepted Risks

| Risk                              | Why Accepted                                                      |
|-----------------------------------|-------------------------------------------------------------------|
| Rooted device data exposure       | App-level protections cannot defend against root access. This is an OS-level concern. |
| Modified/forked version           | MindfulScroll is responsible only for its own codebase, not forks. |

---

## 11. Data Flow Diagrams

### 11.1 Data Collection Flow

```
User opens monitored app
        │
        ▼
UsageStatsManager.queryEvents()
        │
        ▼
Package name extracted (in-memory only)
        │
        ▼
Compared against monitored apps set
        │
        ├── Not monitored → Discarded (no persistence)
        │
        └── Monitored → Session state machine activates
                │
                ├── Timer counts seconds (in-memory)
                │
                ├── Overlay displays MM:SS (screen only)
                │
                └── Session ends → Persisted to Room DB
                        │
                        ▼
                    usage_sessions table
                    (local, private, 90-day retention)
```

### 11.2 Data Storage Flow

```
┌─────────────────────────────────────────────────┐
│                   User Actions                   │
├─────────────────────────────────────────────────┤
│ Select apps │ Set thresholds │ Drag overlay      │
└──────┬──────┴───────┬────────┴────────┬──────────┘
       │              │                 │
       ▼              ▼                 ▼
┌──────────────┐ ┌──────────────┐ ┌──────────────┐
│ monitored_   │ │ threshold_   │ │ user_        │
│ apps         │ │ configs      │ │ preferences  │
└──────────────┘ └──────────────┘ └──────────────┘

┌─────────────────────────────────────────────────┐
│                 System Events                    │
├─────────────────────────────────────────────────┤
│ Session ends → usage_sessions table              │
│ App startup  → DELETE sessions older than 90 days│
└─────────────────────────────────────────────────┘

All tables stored in: mindfulscroll.db
Location: /data/data/com.rudy.mindfulscroll/databases/
Accessible by: MindfulScroll process only
Network access: NONE
```

### 11.3 Data Exit Points

```
Data created → Stored in Room DB → Displayed on screen → That's it.

There are NO other exit points:
  ✗ No network transmission
  ✗ No file export (MVP)
  ✗ No clipboard copy
  ✗ No share intent
  ✗ No broadcast to other apps
  ✗ No content provider queries
  ✗ No backup to cloud
```

---

## 12. User Rights & Controls

### 12.1 User Control Summary

| Right                        | How Exercised                                           | Availability |
|------------------------------|--------------------------------------------------------|--------------|
| **Right to know**            | This privacy policy and in-app "About" screen          | MVP          |
| **Right to control**         | User selects which apps to monitor                     | MVP          |
| **Right to stop**            | One-tap "Stop" button on Home screen                   | MVP          |
| **Right to delete (partial)**| Deselect apps; sessions auto-expire after 90 days      | MVP          |
| **Right to delete (full)**   | Uninstall the app — removes all data                   | MVP          |
| **Right to revoke access**   | Revoke any permission via System Settings at any time  | MVP          |
| **Right to skip permissions**| Skip any onboarding permission step                    | MVP          |
| **Right to export**          | CSV data export                                        | v1.2 (F-028) |
| **Right to clear history**   | "Clear session history" button                         | v1.1         |
| **Right to reset**           | "Reset all data" button                                | v1.1         |

### 12.2 Permission Revocation

Users can revoke any permission at any time via Android System Settings. MindfulScroll handles revocation gracefully:

| Permission Revoked        | App Behavior                                                      | User Communication              |
|---------------------------|-------------------------------------------------------------------|---------------------------------|
| Usage Access              | Monitoring pauses; polling returns null                           | Banner on Home: "Usage Access not granted. Tap to fix." |
| Overlay                   | Monitoring continues; sessions tracked; no timer shown            | Banner on Home: "Overlay permission not granted. Timer won't appear." |
| Battery Optimization      | Service may be killed by OS                                       | Informational banner            |
| Notifications (API 33+)   | Service may not start                                             | Banner: "Notification permission needed." |

### 12.3 Data Portability

MVP does not include data export functionality. This is planned for v1.2 (PRD F-028: CSV export). When implemented, the export will include:

- Session records: app name, start time, end time, duration, max threshold reached
- No data beyond what is already stored locally

---

## 13. Third-Party Dependencies

### 13.1 Dependency Audit

All dependencies used by MindfulScroll are listed below with their privacy implications:

| Dependency               | Source            | Network Capability | Data Collection | Privacy Impact |
|--------------------------|-------------------|--------------------|-----------------|----------------|
| Kotlin stdlib            | JetBrains         | None               | None            | None           |
| Jetpack Compose          | Google (AndroidX) | None               | None            | None           |
| Compose Navigation       | Google (AndroidX) | None               | None            | None           |
| Room (Persistence)       | Google (AndroidX) | None               | None            | None           |
| Hilt (DI)                | Google (Dagger)   | None               | None            | None           |
| Lifecycle Runtime KTX    | Google (AndroidX) | None               | None            | None           |
| Material Design 3        | Google (AndroidX) | None               | None            | None           |
| Coroutines               | JetBrains         | None               | None            | None           |

**No dependency has network capability.** All dependencies are standard AndroidX/Jetpack libraries that operate entirely locally.

### 13.2 Prohibited Dependencies

The following categories of dependencies must never be added to MindfulScroll:

| Category                   | Examples                                   | Reason                          |
|----------------------------|--------------------------------------------|---------------------------------|
| HTTP clients               | OkHttp, Retrofit, Ktor, Volley             | No INTERNET permission          |
| Analytics SDKs             | Firebase Analytics, Google Analytics       | No data collection              |
| Crash reporting SDKs       | Crashlytics, Sentry, Bugsnag              | No network (ADR-009)            |
| Advertising SDKs           | AdMob, Facebook Ads                        | No ads, no data sharing         |
| Social login SDKs          | Google Sign-In, Facebook SDK               | No user accounts                |
| Attribution SDKs           | Adjust, AppsFlyer, Branch                  | No tracking                     |
| Remote config SDKs         | Firebase Remote Config                     | No network                      |
| Push notification SDKs     | Firebase Cloud Messaging                   | No network                      |

**Enforcement:** Any pull request that adds a dependency with network capability must be rejected.

---

## 14. Google Play Policy Compliance

### 14.1 Usage Access Policy Declaration

Google Play requires apps using `PACKAGE_USAGE_STATS` to submit a policy declaration form. The following information should be used in the declaration:

**Core Use Case:**

> MindfulScroll uses Usage Access solely to detect which app is currently in the foreground. When the foreground app matches one of the user's selected apps, a small floating timer overlay is displayed showing elapsed time. This timer changes color as the user spends more time, providing real-time awareness of screen time. No usage data is collected for analytics, shared with third parties, or transmitted off the device.

**Data Handling Declaration:**

> - All data is stored exclusively on the user's device in app-private internal storage.
> - The app declares no INTERNET permission and has no network capability.
> - No third-party SDKs that collect or transmit data are included.
> - Usage session records are automatically deleted after 90 days.
> - Uninstalling the app removes all data permanently.

### 14.2 Privacy Policy Requirements

Google Play requires a privacy policy for apps using sensitive permissions. The user-facing privacy policy draft is in Section 17.

**Distribution:**

| Location                        | Requirement                                    | Status   |
|---------------------------------|------------------------------------------------|----------|
| Google Play Store listing       | Link to privacy policy (required)              | Phase 10 |
| In-app About screen             | "Privacy Policy" link (WBS 10.2)               | Phase 6  |
| First-launch onboarding         | Referenced in permission explanations           | Phase 7  |

### 14.3 Data Safety Section (Google Play)

Google Play's Data Safety section requires disclosure of data collection, sharing, and security practices. MindfulScroll's responses:

| Question                                      | Answer                                    |
|-----------------------------------------------|-------------------------------------------|
| Does your app collect or share user data?      | Collects — does not share                 |
| Data types collected                           | App usage data (which apps are used and for how long) |
| Is data collected processed ephemerally?       | Foreground detection: yes (overwritten every 2 seconds). Session records: no (stored for 90 days). |
| Is data encrypted in transit?                  | Not applicable — no data is transmitted   |
| Can users request data deletion?               | Yes — uninstalling the app deletes all data. In-app deletion planned for v1.1. |
| Does the app share data with third parties?    | No                                        |
| Does the app follow Google's Families policy?  | Not a children's app                      |

### 14.4 Foreground Service Type Declaration (API 34+)

Android 14+ requires `foregroundServiceType` in the manifest. MindfulScroll declares `specialUse` with the following justification (RA-021):

> The foreground service continuously monitors which application is in the foreground to provide real-time usage awareness through a floating overlay timer. This requires persistent operation while the user actively uses their device.

---

## 15. Children's Privacy

MindfulScroll is designed for adult users (PRD Section 4.1: target audience is 18–45). It is not directed at children under 13 and does not knowingly collect data from children.

| Compliance Area                      | Status                                      |
|--------------------------------------|---------------------------------------------|
| COPPA (US)                           | Not applicable — not directed at children   |
| Google Play Families Policy          | Not applicable — not a family/kids app      |
| Content rating                       | Will be determined via Play Store questionnaire (WBS 10.7) |
| Age gate                             | Not required — app has no age-restricted content |

---

## 16. Privacy by Design Verification

### 16.1 Verification Checklist

This checklist should be verified at each phase gate (M0–M10) and before release.

| # | Verification Item                                         | Method                        | Phase   | Status   |
|---|----------------------------------------------------------|-------------------------------|---------|----------|
| 1 | No `INTERNET` permission in merged manifest              | `aapt dump permissions`       | P1+     | Pending  |
| 2 | No network libraries in dependency tree                  | `./gradlew dependencies`      | P1+     | Pending  |
| 3 | Zero network API usage in codebase                       | Lint/grep for `java.net.*`    | P1+     | Pending  |
| 4 | Database stored in app-private internal storage          | Device file inspector         | P2      | Pending  |
| 5 | No writes to external storage                            | Code review + storage audit   | P2      | Pending  |
| 6 | No exported ContentProvider in manifest                  | Manifest review               | P1      | Pending  |
| 7 | Only launcher Activity is exported                       | Manifest review               | P1      | Pending  |
| 8 | `android:allowBackup="false"` in manifest                | Manifest review               | P1      | Pending  |
| 9 | 90-day session retention cleanup works                   | Unit test                     | P2      | Pending  |
| 10| UsageStatsManager reads only MOVE_TO_FOREGROUND events   | Code review                   | P3      | Pending  |
| 11| Foreground package name is not logged in release builds  | ProGuard/R8 rules review      | P1      | Pending  |
| 12| Permission explanations are clear and accurate           | UX review                     | P7      | Pending  |
| 13| Privacy policy is accessible from About screen           | Manual test                   | P6      | Pending  |
| 14| Privacy policy is accurate and complete                  | Legal review                  | P10     | Pending  |
| 15| Data Safety section matches actual behavior              | Cross-reference with code     | P10     | Pending  |
| 16| No debug logging of sensitive data in release builds     | ProGuard/R8 rules review      | P8      | Pending  |
| 17| All data deleted on uninstall                            | Manual test on device         | P9      | Pending  |

### 16.2 Automated Verification

The following checks should be automated in the CI/CD pipeline or build process:

| Check                                      | Tool / Command                                     |
|--------------------------------------------|----------------------------------------------------|
| No INTERNET permission                     | `aapt dump permissions app.apk \| grep -c INTERNET` (should be 0) |
| No network dependencies                   | Custom Gradle task scanning dependency tree         |
| No network API imports                     | Lint rule or grep for `java.net.`, `okhttp3.`, `retrofit2.` |
| `allowBackup` is false                     | Lint check on AndroidManifest.xml                   |
| No exported components (except launcher)   | Custom lint rule                                    |

---

## 17. User-Facing Privacy Policy (Draft)

The following is a draft of the user-facing privacy policy to be published on the Google Play Store listing and accessible from the in-app About screen.

---

### MindfulScroll Privacy Policy

**Last updated: [Date]**

#### Summary

MindfulScroll is a fully offline Android app. It stores all data on your device only. It has no internet access and never sends any data anywhere.

#### What Data We Collect

MindfulScroll collects the following data, stored only on your device:

- **Which apps you select for monitoring** — the app name and package identifier of apps you choose to monitor.
- **Usage sessions** — when you use a monitored app, we record the start time, end time, and duration of each session. We also record the highest visual alert level reached.
- **Your settings** — your threshold configuration, overlay position preference, and monitoring status.

#### How We Use Your Data

Your data is used solely to:

- Detect when you are using a monitored app
- Display the floating timer overlay
- Change the timer's appearance based on your configured thresholds
- Remember your settings between app launches

#### What We Do NOT Collect

We do not collect, access, or store:

- The content of any app (what you see, type, or do inside apps)
- Your contacts, messages, photos, or files
- Your location
- Your device identifier or advertising ID
- Your browsing history
- Your notification content

#### Data Storage

All data is stored locally on your device in a private database that only MindfulScroll can access. No other app can read this data.

#### Data Retention

Usage session records are automatically deleted after 90 days. All other data (your app selections and settings) is kept until you modify it or uninstall the app.

#### Data Sharing

We do not share your data with anyone. MindfulScroll has no internet permission and is physically incapable of transmitting data off your device.

#### Data Deletion

You can delete all MindfulScroll data by uninstalling the app. When you uninstall, all data is permanently removed from your device.

#### Permissions

MindfulScroll requests the following permissions:

- **Usage Access** — to detect which app is in the foreground. We only read the name of the current app, nothing else.
- **Display Over Other Apps** — to show the floating timer while you use other apps.
- **Battery Optimization Exemption** — to keep monitoring running reliably.

You can revoke any permission at any time in your device's Settings.

#### Children's Privacy

MindfulScroll is not designed for children under 13 and does not knowingly collect data from children.

#### Changes to This Policy

If we update this privacy policy, the updated version will be available in the app and on our store listing.

#### Contact

If you have questions about this privacy policy, please contact: [Contact information to be added before release]

---

## 18. Traceability Matrix

### 18.1 SRS Requirement → Privacy Section Mapping

| SRS Requirement | Description                              | Privacy Section          |
|-----------------|------------------------------------------|--------------------------|
| NFR-011         | Data locality (no network)               | Sections 7, 9            |
| NFR-011.AC1     | No INTERNET permission                   | Sections 7.1, 9.2        |
| NFR-011.AC2     | No network libraries                     | Sections 7.1, 13         |
| NFR-011.AC3     | Static analysis verification             | Section 16.2             |
| NFR-012         | Data protection                          | Sections 5, 9            |
| NFR-012.AC1     | App-private internal storage             | Section 5.1              |
| NFR-012.AC2     | No external storage                      | Section 5.2              |
| NFR-012.AC3     | No exported ContentProvider              | Section 5.2, 9.2         |
| NFR-012.AC4     | Only launcher Activity exported          | Section 9.2              |
| NFR-012.AC5     | allowBackup=false                        | Section 5.2, 9.2         |
| DR-001          | MonitoredApp entity                      | Section 3.1.1            |
| DR-002          | ThresholdConfig entity                   | Section 3.1.2            |
| DR-003          | UsageSession entity                      | Section 3.1.3            |
| DR-004          | UserPreferences entity                   | Section 3.1.4            |
| DR-005          | 90-day session retention                 | Section 6                |
| PMR-001         | Permission manifest                      | Section 8.1              |
| PMR-002         | Graceful degradation                     | Section 12.2             |
| FR-032          | Onboarding permission flow               | Section 8.4              |
| FR-033          | Permission status banner                 | Section 12.2             |
| FR-034          | Deep-link to Settings                    | Section 8.4              |

### 18.2 PRD Principle → Privacy Implementation Mapping

| PRD Principle | Description                    | Privacy Implementation                                   |
|---------------|--------------------------------|----------------------------------------------------------|
| P1            | Awareness over restriction     | Informational timer only — no blocking, no data leverage |
| P2            | Respect user autonomy          | All permissions skippable; all monitoring stoppable       |
| P4            | Transparency                   | Plain-language explanations; in-app privacy policy        |
| P5            | Battery respect                | Screen-off pauses polling — reduces data collection       |
| P6            | Graceful behavior              | Silent error recovery; no crash reports sent              |

### 18.3 Risk → Privacy Mitigation Mapping

| Risk    | Description                              | Privacy Mitigation                                     |
|---------|------------------------------------------|--------------------------------------------------------|
| RA-001  | Google Play rejects PACKAGE_USAGE_STATS  | Comprehensive privacy policy; policy declaration form   |
| RA-016  | Privacy policy insufficient              | Detailed policy (Section 17); early preparation (P0)    |
| RA-021  | Foreground service type rejected         | Clear specialUse justification (Section 14.4)           |

### 18.4 ProjectPlan WBS → Privacy Document Mapping

| WBS Task | Description                                   | Privacy Section          |
|----------|-----------------------------------------------|--------------------------|
| 0.9      | Create Privacy & Data Handling Document        | This entire document     |
| 10.1     | Write user-facing privacy policy               | Section 17               |
| 10.2     | Integrate privacy policy link in About screen  | Section 14.2             |
| 10.3     | Prepare PACKAGE_USAGE_STATS declaration form   | Section 14.1             |

---

## 19. References

| Document                                | Sections Referenced                                           |
|-----------------------------------------|---------------------------------------------------------------|
| [PRD.md](PRD.md)                        | Product Principles (P1, P2, P4), Constraints (C-11, C-12), Scope (Section 11.2) |
| [SRS.md](SRS.md)                        | NFR-011, NFR-012, DR-001–DR-005, PMR-001–PMR-002, FR-032–FR-034, EH-001–EH-006 |
| [SAD.md](SAD.md)                        | Section 8 (Data Architecture), Section 15 (Security & Privacy), ADR-006, ADR-009 |
| [TDD.md](TDD.md)                        | Section 2.3 (Constants), Section 4 (Data Layer), Section 8 (Monitoring Service) |
| [DbSchema.md](DbSchema.md)              | Section 15 (Data Protection & Privacy), Section 11 (Retention), Section 12 (Volume) |
| [UIUXspec.md](UIUXspec.md)              | Section 6 (Onboarding), Section 10 (Permission Banner), Section 7.4 (About Screen) |
| [ProjectPlan.md](ProjectPlan.md)        | WBS 0.9, 10.1, 10.2, 10.3                                    |
| [RiskAssessment.md](RiskAssessment.md)  | RA-001, RA-016, RA-021                                        |
| [Google Play Usage Access Policy](https://support.google.com/googleplay/android-developer/answer/9888170) | Policy compliance |
| [Google Play Data Safety](https://support.google.com/googleplay/android-developer/answer/10787469) | Data safety section |

---

*End of Document*
