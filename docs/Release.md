# Release Plan

## MindfulScroll — Digital Awareness Companion

---

### Document Control

| Field              | Value                                     |
| ------------------ | ----------------------------------------- |
| **Document Title** | MindfulScroll Release Plan                 |
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

| Document                                  | Version | Relationship                                                |
| ----------------------------------------- | ------- | ----------------------------------------------------------- |
| [PRD.md](PRD.md)                          | 1.0     | Product vision, features, release strategy, success metrics |
| [SRS.md](SRS.md)                          | 1.0     | Functional and non-functional requirements, acceptance criteria |
| [SAD.md](SAD.md)                          | 1.0     | Architecture, build configuration, security model           |
| [TDD.md](TDD.md)                         | 1.0     | Implementation design, build variants, ProGuard rules       |
| [DbSchema.md](DbSchema.md)               | 1.0     | Database schema, migration strategy, data retention         |
| [UIUXspec.md](UIUXspec.md)               | 1.0     | Screen inventory, visual design, accessibility specs        |
| [ProjectPlan.md](ProjectPlan.md)         | 1.0     | Phases, milestones, WBS, Definition of Done                 |
| [RiskAssessment.md](RiskAssessment.md)   | 1.0     | Risk register, mitigation strategies, release gate risks    |
| [CodingStandards.md](CodingStandards.md) | 1.0     | Build configuration, testing rules, git conventions         |
| [Privacy.md](Privacy.md)                 | 1.0     | Privacy policy, Google Play compliance, Data Safety section |
| [TestPlan.md](TestPlan.md)               | 1.0     | Test strategy, exit criteria, regression suite              |

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Release Overview](#2-release-overview)
3. [Release Scope — MVP (v1.0)](#3-release-scope--mvp-v10)
4. [Release Readiness Criteria](#4-release-readiness-criteria)
5. [Pre-Release Checklist](#5-pre-release-checklist)
6. [Build & Signing Configuration](#6-build--signing-configuration)
7. [Google Play Store Submission](#7-google-play-store-submission)
8. [Privacy & Policy Compliance](#8-privacy--policy-compliance)
9. [Staged Rollout Strategy](#9-staged-rollout-strategy)
10. [Release Verification Testing](#10-release-verification-testing)
11. [Post-Release Monitoring](#11-post-release-monitoring)
12. [Rollback & Hotfix Procedures](#12-rollback--hotfix-procedures)
13. [Release Risk Assessment](#13-release-risk-assessment)
14. [Post-MVP Release Roadmap](#14-post-mvp-release-roadmap)
15. [Communication Plan](#15-communication-plan)
16. [Release Artifact Inventory](#16-release-artifact-inventory)
17. [Roles & Responsibilities](#17-roles--responsibilities)
18. [Release Timeline](#18-release-timeline)
19. [Traceability Matrix](#19-traceability-matrix)
20. [Glossary](#20-glossary)
21. [References](#21-references)

---

## 1. Introduction

### 1.1 Purpose

This Release Plan defines the strategy, process, criteria, and procedures for releasing MindfulScroll MVP (v1.0) to the Google Play Store. It covers the activities required from the completion of development and testing (end of Phase 9) through a successful, publicly available release (Phase 10). It also outlines the post-release monitoring plan, rollback procedures, and the roadmap for subsequent releases.

### 1.2 Scope

This plan covers:

- **Primary release:** MindfulScroll MVP (v1.0) on Google Play Store.
- **Post-release:** First 90 days of production monitoring and hotfix procedures.
- **Roadmap:** High-level plans for v1.1 and v1.2+ releases.

### 1.3 Intended Audience

| Audience           | Usage                                                         |
| ------------------ | ------------------------------------------------------------- |
| Developer(s)       | Build, signing, submission process, hotfix procedures         |
| Project owner      | Release readiness decisions, go/no-go authority               |
| QA / Testers       | Release verification testing, post-release monitoring         |
| Reviewers          | Release gate validation                                       |

---

## 2. Release Overview

### 2.1 Product Summary

| Parameter               | Value                                                 |
| ----------------------- | ----------------------------------------------------- |
| **Product**             | MindfulScroll — Digital Awareness Companion            |
| **Platform**            | Native Android (Kotlin, Jetpack Compose)               |
| **Release Version**     | 1.0.0 (MVP)                                           |
| **Version Code**        | 1                                                     |
| **Min SDK**             | 26 (Android 8.0 Oreo)                                 |
| **Target SDK**          | 36 (Android 15)                                       |
| **Compile SDK**         | 36                                                    |
| **Architecture**        | Clean Architecture, MVVM, Single-Activity              |
| **Data Strategy**       | 100% local, on-device, Room database                   |
| **Network**             | None (fully offline — no INTERNET permission)          |
| **Distribution**        | Google Play Store                                      |
| **Pricing**             | Free                                                  |

### 2.2 Release Goals

From PRD Section 5.1 (Business Goals) and Section 16.2 (MVP Release Criteria):

| ID   | Goal                                                        | Measurement                     |
| ---- | ----------------------------------------------------------- | ------------------------------- |
| BG-1 | Launch on Google Play Store with full policy compliance      | App approved and live           |
| BG-2 | Achieve 1,000 organic downloads within 90 days of launch    | Play Console metrics            |
| BG-3 | Maintain a 4.0+ star rating on Play Store                   | Play Console reviews            |
| BG-4 | Achieve 30-day user retention rate of 40% or higher         | Play Console retention data     |

### 2.3 Key Stakeholders

| Stakeholder          | Role                                                       |
| -------------------- | ---------------------------------------------------------- |
| Rudy                 | Project owner, developer, release authority                |
| Google Play Review   | External — approves or rejects the submission              |

---

## 3. Release Scope — MVP (v1.0)

### 3.1 Features Included

All 15 MVP features (PRD F-001 through F-015) are included:

| PRD Feature | Description                      | SRS Requirements          | Status   |
| ----------- | -------------------------------- | ------------------------- | -------- |
| F-001       | App Discovery                    | FR-001 – FR-003           | Pending  |
| F-002       | App Selection                    | FR-004 – FR-008           | Pending  |
| F-003       | Persist App Selections           | FR-007                    | Pending  |
| F-004       | Foreground App Detection         | FR-009 – FR-011           | Pending  |
| F-005       | Session Tracking                 | FR-012 – FR-017           | Pending  |
| F-006       | Floating Timer Overlay           | FR-018 – FR-020           | Pending  |
| F-007       | Overlay Drag & Snap              | FR-021 – FR-022           | Pending  |
| F-008       | Overlay Position Persistence     | FR-023                    | Pending  |
| F-009       | Threshold Configuration          | FR-025 – FR-027           | Pending  |
| F-010       | Color Escalation                 | FR-028 – FR-029           | Pending  |
| F-011       | Throb Animation                  | FR-030 – FR-031           | Pending  |
| F-012       | Overlay Lifecycle                | FR-024                    | Pending  |
| F-013       | Permission Onboarding            | FR-032 – FR-034           | Pending  |
| F-014       | Foreground Service               | FR-035 – FR-037           | Pending  |
| F-015       | Boot Auto-Restart                | FR-038                    | Pending  |

Plus Home Screen & Navigation (FR-039 – FR-041).

### 3.2 Features Excluded (Deferred to Future Releases)

| Feature     | Description                      | Deferred To |
| ----------- | -------------------------------- | ----------- |
| F-016       | Usage Dashboard                  | v1.1        |
| F-017       | Session History List             | v1.1        |
| F-018       | Per-App Thresholds               | v1.1        |
| F-019       | Pause/Resume Monitoring          | v1.1        |
| F-020       | App Categories                   | v1.1        |
| F-021       | Quick-Select Presets             | v1.1        |
| F-022       | Timer Tap Interaction            | v1.1        |
| F-023       | Monitoring Schedule              | v1.1        |
| F-024–F-032 | Advanced features (trends, widget, export, accessibility mode, etc.) | v1.2+ |

### 3.3 Non-Functional Requirements Included

| Category        | Requirement | Target                                    |
| --------------- | ----------- | ----------------------------------------- |
| Performance     | NFR-001     | Cold start ≤ 2 sec                        |
| Battery         | NFR-002     | ≤ 2% drain per hour                       |
| Memory          | NFR-003     | ≤ 80MB total PSS                          |
| Overlay FPS     | NFR-004     | ≥ 55 FPS during animations                |
| DB Performance  | NFR-005     | All queries ≤ 100ms                        |
| Reliability     | NFR-006     | ≥ 95% service uptime on major OEMs        |
| Crash-free      | NFR-007     | ≥ 99.5% crash-free session rate            |
| Data Integrity  | NFR-008     | WAL mode, transaction safety               |
| Usability       | NFR-009     | Onboarding completable in 90 sec           |
| Accessibility   | NFR-010     | WCAG 2.1 AA contrast, 48dp touch targets   |
| Privacy         | NFR-011     | Zero network calls, no INTERNET permission |
| Data Protection | NFR-012     | Private storage only, no exported components|
| Architecture    | NFR-013     | Clean Architecture, Hilt DI                |
| Test Coverage   | NFR-014     | ≥ 80% domain layer coverage                |
| API Compat      | NFR-015     | API 26–36 functional                       |
| OEM Compat      | NFR-016     | Google, Samsung, Xiaomi (Must)             |

---

## 4. Release Readiness Criteria

### 4.1 MVP Release Gate (Go/No-Go)

From PRD Section 16.2 and ProjectPlan Section 12.3, the MVP is release-ready when **all** of the following are true:

| #  | Criterion                                                                 | Verification Method                      | Status   |
| -- | ------------------------------------------------------------------------- | ---------------------------------------- | -------- |
| 1  | All F-001 through F-015 features implemented and tested                  | TestPlan system tests (ST-*) all pass    | Pending  |
| 2  | Google Play policy review passed (PACKAGE_USAGE_STATS declaration)       | Play Console approval                    | Pending  |
| 3  | Foreground service reliable for 8+ hours on Pixel, Samsung, Xiaomi       | TestPlan endurance tests (EN-001/002/003)| Pending  |
| 4  | Battery consumption confirmed ≤ 2% per hour                              | TestPlan NF-PF-003                       | Pending  |
| 5  | Overlay tested on 10 host apps without touch interference                | TestPlan host app tests (HA-*)           | Pending  |
| 6  | Onboarding achieves >80% permission grant rate in internal testing       | Manual testing with 3+ testers           | Pending  |
| 7  | Zero Critical or High severity open bugs                                 | Bug tracker review                       | Pending  |
| 8  | Privacy policy published and accessible from app and Play Store          | Manual verification                      | Pending  |

### 4.2 Technical Readiness Criteria

| #  | Criterion                                                  | Verification Method                        | Status   |
| -- | ---------------------------------------------------------- | ------------------------------------------ | -------- |
| 1  | Release build compiles without errors or warnings          | `./gradlew assembleRelease`                | Pending  |
| 2  | All unit tests pass                                        | `./gradlew testReleaseUnitTest`            | Pending  |
| 3  | Domain layer test coverage ≥ 80%                           | JaCoCo report                              | Pending  |
| 4  | No INTERNET permission in merged manifest                  | `aapt dump permissions` returns 0 matches  | Pending  |
| 5  | No network libraries in dependency tree                    | `./gradlew dependencies` audit             | Pending  |
| 6  | ProGuard/R8 rules verified (no crashes in release build)   | Release build smoke test on device         | Pending  |
| 7  | Debug logging stripped from release build                  | ProGuard rules strip `Log.d`, `Log.v`, `Log.i` | Pending |
| 8  | Release keystore configured and backed up                  | Keystore file verified in 2 locations      | Pending  |
| 9  | Version code = 1, version name = "1.0.0"                  | `build.gradle.kts` verified                | Pending  |
| 10 | `allowBackup="false"` in manifest                          | Manifest inspection                        | Pending  |
| 11 | Only launcher Activity is exported                         | Manifest inspection                        | Pending  |
| 12 | `foregroundServiceType="specialUse"` declared              | Manifest inspection                        | Pending  |
| 13 | Regression test suite passes 100%                          | TestPlan Section 16                        | Pending  |

### 4.3 Documentation Readiness Criteria

| #  | Criterion                                    | Status   |
| -- | -------------------------------------------- | -------- |
| 1  | User-facing privacy policy finalized         | Pending  |
| 2  | Play Store listing copy written              | Pending  |
| 3  | Play Store screenshots captured              | Pending  |
| 4  | Feature graphic designed                     | Pending  |
| 5  | PACKAGE_USAGE_STATS declaration form drafted | Pending  |
| 6  | Content rating questionnaire answers prepared| Pending  |
| 7  | All project documents up to date             | Pending  |

---

## 5. Pre-Release Checklist

This checklist must be completed sequentially before submission to Google Play. Each item requires sign-off.

### 5.1 Code Freeze

| #  | Action                                              | Owner | Sign-off |
| -- | --------------------------------------------------- | ----- | -------- |
| 1  | Feature freeze — no new features after this point   | Rudy  | [ ]      |
| 2  | All bug fixes committed and tested                  | Rudy  | [ ]      |
| 3  | Final code review completed                         | Rudy  | [ ]      |
| 4  | Final lint pass (zero errors, minimal warnings)     | Rudy  | [ ]      |
| 5  | All TODO/FIXME comments resolved or documented      | Rudy  | [ ]      |
| 6  | Version code and version name set correctly         | Rudy  | [ ]      |

### 5.2 Build Verification

| #  | Action                                              | Owner | Sign-off |
| -- | --------------------------------------------------- | ----- | -------- |
| 1  | Generate signed release AAB                         | Rudy  | [ ]      |
| 2  | Install release APK on Pixel device — smoke test    | Rudy  | [ ]      |
| 3  | Install release APK on Samsung device — smoke test  | Rudy  | [ ]      |
| 4  | Install release APK on Xiaomi device — smoke test   | Rudy  | [ ]      |
| 5  | Verify ProGuard/R8 doesn't break runtime behavior   | Rudy  | [ ]      |
| 6  | Verify app size is reasonable (< 20MB installed)    | Rudy  | [ ]      |
| 7  | Verify no debug logging in release build (logcat)   | Rudy  | [ ]      |

### 5.3 Privacy & Security Verification

| #  | Action                                                 | Owner | Sign-off |
| -- | ------------------------------------------------------ | ----- | -------- |
| 1  | No INTERNET permission in merged manifest              | Rudy  | [ ]      |
| 2  | No network libraries in dependency tree                | Rudy  | [ ]      |
| 3  | No exported components except launcher Activity        | Rudy  | [ ]      |
| 4  | `allowBackup="false"` confirmed                        | Rudy  | [ ]      |
| 5  | Database in app-private internal storage               | Rudy  | [ ]      |
| 6  | Privacy policy accessible from About screen            | Rudy  | [ ]      |
| 7  | Privacy policy content matches actual app behavior     | Rudy  | [ ]      |
| 8  | Data Safety section answers prepared                   | Rudy  | [ ]      |

### 5.4 Store Listing Preparation

| #  | Action                                              | Owner | Sign-off |
| -- | --------------------------------------------------- | ----- | -------- |
| 1  | App title finalized                                 | Rudy  | [ ]      |
| 2  | Short description written (80 chars max)            | Rudy  | [ ]      |
| 3  | Full description written (4,000 chars max)          | Rudy  | [ ]      |
| 4  | Phone screenshots captured (min 2, max 8)           | Rudy  | [ ]      |
| 5  | Feature graphic designed (1024 × 500 px)            | Rudy  | [ ]      |
| 6  | App icon verified (512 × 512 px, hi-res)            | Rudy  | [ ]      |
| 7  | App category selected (Tools or Health & Fitness)   | Rudy  | [ ]      |
| 8  | Content rating questionnaire completed              | Rudy  | [ ]      |
| 9  | Target audience and content declarations completed  | Rudy  | [ ]      |

---

## 6. Build & Signing Configuration

### 6.1 Build Variants

From TDD Section 17.2:

| Variant   | `minifyEnabled` | `debuggable` | Signing          | ProGuard | Logs              |
| --------- | --------------- | ------------ | ---------------- | -------- | ----------------- |
| `debug`   | false           | true         | Debug keystore   | Off      | Full (d/v/i/w/e)  |
| `release` | true            | false        | Release keystore | R8       | Critical only (w/e)|

### 6.2 Release Signing

| Parameter              | Value                                           |
| ---------------------- | ----------------------------------------------- |
| Keystore format        | JKS or PKCS12                                   |
| Key algorithm          | RSA 2048-bit (minimum)                          |
| Validity period        | 25+ years (Google Play requirement)             |
| Google Play App Signing| Enabled (recommended for key recovery)          |
| Upload key             | Separate from app signing key                   |
| Backup locations       | (1) Local encrypted drive, (2) Cloud encrypted storage |

**Critical:** The release keystore must be created during Phase 1 (not deferred to Phase 10) and backed up in at least 2 physically separate locations (RA-009 mitigation).

### 6.3 Release Build Steps

```
1. Verify version code and version name in build.gradle.kts
2. Run full unit test suite:       ./gradlew testReleaseUnitTest
3. Run lint:                       ./gradlew lintRelease
4. Generate signed AAB:            ./gradlew bundleRelease
5. Verify AAB size:                bundletool get-size total --bundle=app-release.aab
6. Generate APK for testing:       bundletool build-apks --bundle=app-release.aab
7. Install and smoke test on 3 OEMs
8. Verify merged manifest:         aapt dump permissions app-release.apk
```

### 6.4 ProGuard / R8 Rules

From TDD Section 17.3, the release build applies R8 with rules that:
- Keep Room entities, DAOs, and database classes
- Keep Hilt-injected ViewModels
- Keep domain models used in Room entities
- Strip debug logs (`Log.d`, `Log.v`, `Log.i`)

### 6.5 Version Strategy

| Release    | Version Name | Version Code | Notes                          |
| ---------- | ------------ | ------------ | ------------------------------ |
| MVP        | 1.0.0        | 1            | Initial release                |
| Hotfix 1   | 1.0.1        | 2            | Bug fixes only                 |
| Hotfix 2   | 1.0.2        | 3            | Bug fixes only                 |
| v1.1       | 1.1.0        | 10           | Dashboard + per-app thresholds |
| v1.2       | 1.2.0        | 20           | Trends + widget + export       |

Version code increments by 1 for hotfixes, by larger jumps for minor releases to leave room for patches.

---

## 7. Google Play Store Submission

### 7.1 Play Store Listing

#### 7.1.1 App Identity

| Field              | Value                                                  |
| ------------------ | ------------------------------------------------------ |
| **App Name**       | MindfulScroll — Screen Time Awareness                  |
| **Package Name**   | com.rudy.mindfulscroll                                 |
| **Category**       | Tools (primary) or Health & Fitness (alternative)      |
| **Content Rating** | Determined via questionnaire (expected: Everyone)      |
| **Pricing**        | Free                                                   |
| **Countries**      | All countries (no restrictions)                        |

#### 7.1.2 Short Description (≤ 80 characters)

> See how long you scroll. A gentle floating timer — no blocking, no limits.

#### 7.1.3 Full Description (Draft)

> **Know your scroll. Own your time.**
>
> MindfulScroll is a lightweight screen time awareness tool that shows you how long you've been using specific apps — in real time, while you're using them.
>
> Unlike screen time blockers that lock you out, MindfulScroll trusts you. It places a small, floating timer on your screen that gently changes color the longer you scroll. You decide what to do with that awareness.
>
> **How it works:**
> - Choose which apps to monitor (social media, video, games — you pick)
> - A small floating timer appears after 60 seconds of use
> - The timer gradually shifts color as you spend more time
> - Drag the timer anywhere on screen, or let it snap to the edge
> - That's it. No blocking. No lectures. Just awareness.
>
> **Key features:**
> - Floating timer overlay with drag and edge-snap
> - 5-stage color escalation (calm → notice → alert → urgent → throb)
> - Customizable time thresholds (presets or your own values)
> - Works with any app — Instagram, TikTok, YouTube, Reddit, and more
> - Runs quietly in the background with minimal battery impact
> - Survives reboots — pick up right where you left off
>
> **Your privacy matters:**
> - 100% offline — no internet permission, no data ever leaves your device
> - No accounts, no analytics, no tracking
> - All data stored locally and deleted when you uninstall
> - Open and transparent about every permission we need
>
> **Permissions explained:**
> - Usage Access: To detect which app is in the foreground (we only see the app name, nothing else)
> - Display Over Other Apps: To show the floating timer
> - Battery Optimization Exemption: To keep monitoring running reliably
>
> MindfulScroll helps you build awareness, not guilt. Start scrolling mindfully today.

#### 7.1.4 Screenshots Required

| #  | Screen                     | Content Shown                                  |
| -- | -------------------------- | ---------------------------------------------- |
| 1  | Home Screen (Active)       | Monitoring active, green indicator, app count  |
| 2  | App Selection              | List of apps with checkboxes, search bar       |
| 3  | Threshold Configuration    | Presets and custom input                       |
| 4  | Overlay on Instagram       | Floating timer (CALM state) over Instagram     |
| 5  | Overlay on YouTube         | Floating timer (NOTICE/ALERT state) over YouTube|
| 6  | Overlay Throb State        | Pulsing timer (THROB state) over a host app    |
| 7  | Onboarding (Welcome)       | Welcome screen with Get Started button         |
| 8  | Permission Explanation     | Usage Access permission screen                 |

**Requirements:** Phone screenshots must be 16:9 aspect ratio, minimum 1080px wide, PNG or JPEG.

#### 7.1.5 Feature Graphic

| Parameter      | Requirement            |
| -------------- | ---------------------- |
| Dimensions     | 1024 × 500 px         |
| Format         | PNG or JPEG            |
| Content        | App name + tagline + visual of floating timer overlay |

### 7.2 PACKAGE_USAGE_STATS Declaration

Google Play requires a policy declaration form for apps using the `PACKAGE_USAGE_STATS` permission. From Privacy.md Section 14.1:

**Core Use Case Declaration:**

> MindfulScroll uses Usage Access solely to detect which app is currently in the foreground. When the foreground app matches one of the user's selected apps, a small floating timer overlay is displayed showing elapsed time. This timer changes color as the user spends more time, providing real-time awareness of screen time. No usage data is collected for analytics, shared with third parties, or transmitted off the device.

**Data Handling Declaration:**

> - All data is stored exclusively on the user's device in app-private internal storage.
> - The app declares no INTERNET permission and has no network capability.
> - No third-party SDKs that collect or transmit data are included.
> - Usage session records are automatically deleted after 90 days.
> - Uninstalling the app removes all data permanently.

**Supporting Evidence to Include:**

| Evidence                               | Purpose                                      |
| -------------------------------------- | -------------------------------------------- |
| AndroidManifest.xml (no INTERNET)      | Proves no network capability                 |
| Dependency list (no network libs)      | Proves no hidden network access              |
| Privacy policy URL                     | Required for policy compliance               |
| Demo video of app behavior             | Shows legitimate use of UsageStats           |

### 7.3 Foreground Service Type Declaration (API 34+)

From Privacy.md Section 14.4:

**Service Type:** `specialUse`

**Justification:**

> The foreground service continuously monitors which application is in the foreground to provide real-time usage awareness through a floating overlay timer. This requires persistent operation while the user actively uses their device.

### 7.4 Data Safety Section

From Privacy.md Section 14.3:

| Question                                      | Answer                                    |
| --------------------------------------------- | ----------------------------------------- |
| Does your app collect or share user data?      | Collects — does not share                 |
| Data types collected                           | App usage data (which apps are used and for how long) |
| Is data collected processed ephemerally?       | Foreground detection: yes. Session records: no (stored for 90 days). |
| Is data encrypted in transit?                  | Not applicable — no data is transmitted   |
| Can users request data deletion?               | Yes — uninstalling the app deletes all data |
| Does the app share data with third parties?    | No                                        |
| Does the app follow Google's Families policy?  | Not a children's app                      |

### 7.5 Content Rating Questionnaire

| Question Area             | Expected Answer                              |
| ------------------------- | -------------------------------------------- |
| Violence                  | None                                         |
| Sexual content            | None                                         |
| Language                  | None                                         |
| Controlled substances     | None                                         |
| User-generated content    | None                                         |
| Data sharing              | No data shared                               |
| Location data             | Not collected                                |
| **Expected Rating**       | **Everyone / PEGI 3 / USK 0**                |

---

## 8. Privacy & Policy Compliance

### 8.1 Privacy Policy

The user-facing privacy policy (full draft in Privacy.md Section 17) must be:

| Requirement                              | Location                           | Status   |
| ---------------------------------------- | ---------------------------------- | -------- |
| Accessible from Google Play Store listing| Play Console → Store listing → URL | Pending  |
| Accessible from in-app About screen      | About Screen → "Privacy Policy" link| Pending |
| Referenced during onboarding             | Permission explanation screens     | Pending  |

**Key Privacy Commitments (from Privacy.md Section 2.1):**

1. No data leaves the device
2. No analytics or tracking
3. No cloud sync or backup
4. Minimal data collection
5. User controls all data
6. Transparent about access
7. Data has an expiration (90 days)
8. Uninstall removes everything

### 8.2 Privacy by Design Verification

From Privacy.md Section 16.1, the following must be verified before release:

| # | Verification Item                                    | Method                        | Status   |
|---|------------------------------------------------------|-------------------------------|----------|
| 1 | No INTERNET permission in merged manifest            | `aapt dump permissions`       | Pending  |
| 2 | No network libraries in dependency tree              | `./gradlew dependencies`      | Pending  |
| 3 | Zero network API usage in codebase                   | Lint/grep for `java.net.*`    | Pending  |
| 4 | Database stored in app-private internal storage      | Device file inspector         | Pending  |
| 5 | No writes to external storage                        | Code review + storage audit   | Pending  |
| 6 | No exported ContentProvider in manifest              | Manifest review               | Pending  |
| 7 | Only launcher Activity is exported                   | Manifest review               | Pending  |
| 8 | `allowBackup="false"` in manifest                    | Manifest review               | Pending  |
| 9 | 90-day session retention cleanup works               | Unit test                     | Pending  |
| 10| UsageStatsManager reads only MOVE_TO_FOREGROUND events| Code review                  | Pending  |
| 11| Package names not logged in release builds           | ProGuard rules review         | Pending  |
| 12| Permission explanations are clear and accurate       | UX review                     | Pending  |
| 13| Privacy policy accessible from About screen          | Manual test                   | Pending  |
| 14| Privacy policy content is accurate and complete      | Legal review                  | Pending  |
| 15| Data Safety section matches actual behavior          | Cross-reference with code     | Pending  |
| 16| No debug logging of sensitive data in release builds | ProGuard rules review         | Pending  |
| 17| All data deleted on uninstall                        | Manual test on device         | Pending  |

### 8.3 Permissions in Final Build

From SRS PMR-001, exactly these 7 permissions must be declared:

| Permission                              | Type             | Granted At                |
| --------------------------------------- | ---------------- | ------------------------- |
| `PACKAGE_USAGE_STATS`                   | Signature/AppOp  | System Settings (manual)  |
| `SYSTEM_ALERT_WINDOW`                   | Signature/AppOp  | System Settings (manual)  |
| `FOREGROUND_SERVICE`                    | Normal           | Auto-granted at install   |
| `FOREGROUND_SERVICE_SPECIAL_USE`        | Normal           | Auto-granted at install   |
| `RECEIVE_BOOT_COMPLETED`               | Normal           | Auto-granted at install   |
| `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`  | Normal           | System dialog (prompted)  |
| `POST_NOTIFICATIONS` (API 33+)         | Dangerous        | Runtime prompt            |

**No additional permissions may be present.** The `INTERNET` permission must explicitly NOT be declared.

---

## 9. Staged Rollout Strategy

### 9.1 Rollout Phases

From ProjectPlan WBS 10.10:

| Phase        | Rollout % | Duration  | Purpose                                      | Exit Criteria                      |
| ------------ | --------- | --------- | -------------------------------------------- | ---------------------------------- |
| **Phase A**  | 20%       | 3–5 days  | Early adopter feedback, critical bug detection| No critical crashes, no 1-star spike|
| **Phase B**  | 50%       | 3–5 days  | Broader validation, performance at scale      | Crash-free rate ≥ 99%, no regressions|
| **Phase C**  | 100%      | Ongoing   | Full availability                            | Sustained stability                |

### 9.2 Rollout Decision Criteria

| Decision Point      | Go Criteria                                    | No-Go Criteria                              |
| ------------------- | ---------------------------------------------- | ------------------------------------------- |
| Phase A → Phase B   | Crash-free ≥ 99.5%, no critical bugs reported, rating ≥ 3.5 | Any critical bug, crash rate > 1%, rating < 3.0 |
| Phase B → Phase C   | Crash-free ≥ 99.5%, no new critical/high bugs, no ANR spike | Any unresolved critical bug, ANR rate > 0.5% |

### 9.3 Rollout Pause Triggers

The rollout must be paused immediately if any of the following occur:

| Trigger                                   | Action                                     |
| ----------------------------------------- | ------------------------------------------ |
| Crash-free rate drops below 99%           | Halt rollout, investigate, prepare hotfix   |
| ANR rate exceeds 0.5%                     | Halt rollout, investigate                   |
| Multiple 1-star reviews citing same issue | Halt rollout, assess severity              |
| Google Play policy warning received       | Halt rollout, address policy issue          |
| Ghost overlay reported by multiple users  | Halt rollout, prepare immediate hotfix      |
| Touch interference reported               | Halt rollout, prepare immediate hotfix      |

---

## 10. Release Verification Testing

### 10.1 Release Candidate Smoke Test

Before submission, the signed release build must pass this smoke test on all 3 Must-priority OEM devices:

| #  | Test                                               | Pixel | Samsung | Xiaomi |
| -- | -------------------------------------------------- | ----- | ------- | ------ |
| 1  | Fresh install and launch                           | [ ]   | [ ]     | [ ]    |
| 2  | Complete onboarding (all permissions granted)      | [ ]   | [ ]     | [ ]    |
| 3  | Select 3 apps for monitoring                       | [ ]   | [ ]     | [ ]    |
| 4  | Start monitoring — service starts, notification shown | [ ] | [ ]     | [ ]    |
| 5  | Open monitored app — detection within 3 sec        | [ ]   | [ ]     | [ ]    |
| 6  | Timer appears at 60 sec with fade-in               | [ ]   | [ ]     | [ ]    |
| 7  | Timer is draggable, snaps to edge                  | [ ]   | [ ]     | [ ]    |
| 8  | Timer does not block host app touches              | [ ]   | [ ]     | [ ]    |
| 9  | Leave app — timer dims during cooldown             | [ ]   | [ ]     | [ ]    |
| 10 | Return within cooldown — session resumes           | [ ]   | [ ]     | [ ]    |
| 11 | Wait for cooldown expiry — timer fades out         | [ ]   | [ ]     | [ ]    |
| 12 | No ghost overlay after session end                 | [ ]   | [ ]     | [ ]    |
| 13 | Stop monitoring — service stops                    | [ ]   | [ ]     | [ ]    |
| 14 | Reboot device — service auto-restarts              | [ ]   | [ ]     | [ ]    |
| 15 | Notification tap opens app                         | [ ]   | [ ]     | [ ]    |
| 16 | App survives activity destruction (back press)     | [ ]   | [ ]     | [ ]    |
| 17 | Privacy policy accessible from About screen        | [ ]   | [ ]     | [ ]    |

### 10.2 Release Build Specific Checks

| #  | Check                                                   | Pass  |
| -- | ------------------------------------------------------- | ----- |
| 1  | No debug toast messages appear                          | [ ]   |
| 2  | No verbose logging visible in logcat                    | [ ]   |
| 3  | ProGuard/R8 hasn't broken any runtime behavior          | [ ]   |
| 4  | App icon displays correctly on home screen              | [ ]   |
| 5  | App name displays correctly in launcher                 | [ ]   |
| 6  | Notification icon displays correctly                    | [ ]   |
| 7  | AAB file size is reasonable (< 15 MB bundle)            | [ ]   |
| 8  | Installed app size is reasonable (< 20 MB)              | [ ]   |

---

## 11. Post-Release Monitoring

### 11.1 Monitoring Dashboard (Google Play Console)

After release, monitor the following metrics daily during the first 2 weeks, then weekly:

| Metric                        | Target                     | Alert Threshold           | Source             |
| ----------------------------- | -------------------------- | ------------------------- | ------------------ |
| Crash-free rate               | ≥ 99.5%                   | < 99%                     | Android Vitals     |
| ANR rate                      | < 0.2%                    | > 0.5%                    | Android Vitals     |
| Star rating                   | ≥ 4.0                     | < 3.5                     | Play Console       |
| Install count (7-day)         | Growing trend              | Sustained decline         | Play Console       |
| Uninstall rate (7-day)        | < 50%                     | > 60%                     | Play Console       |
| 1-star review count (daily)   | < 3 per day               | > 5 per day same issue    | Play Console       |

### 11.2 Review Monitoring

All Play Store reviews must be read daily during the first 2 weeks. Watch for patterns indicating:

| Pattern                               | Likely Root Cause                     | Action                          |
| ------------------------------------- | ------------------------------------- | ------------------------------- |
| "App crashes when..."                 | Runtime exception not caught          | Hotfix release                  |
| "Timer won't go away"                 | Ghost overlay (RA-011)                | Hotfix release (Critical)       |
| "Can't scroll/tap with timer"         | Touch interference (RA-010)           | Hotfix release (Critical)       |
| "Drains my battery"                   | Battery consumption too high (RA-005) | Investigate, potential hotfix   |
| "Monitoring keeps stopping"           | OEM battery kill (RA-002)             | Add OEM-specific guidance       |
| "Too many permissions"                | Onboarding UX (RA-003)               | UX improvement in v1.1          |
| "Timer is annoying"                   | Overlay perception (RA-004)           | Consider opacity settings       |
| "Doesn't detect [app]"               | UsageStats inconsistency (RA-007)    | Investigate OEM-specific issue  |

### 11.3 Success Metrics Tracking

From PRD Section 5 (Goals & Success Metrics):

| Metric                                    | Target                     | Measurement Window | Source          |
| ----------------------------------------- | -------------------------- | ------------------ | --------------- |
| Organic downloads                         | 1,000 in 90 days          | 90 days post-launch| Play Console    |
| Star rating                               | ≥ 4.0                     | Ongoing            | Play Console    |
| 30-day retention rate                     | ≥ 40%                     | 30 days post-launch| Play Console    |
| 7-day uninstall rate                      | < 50%                     | Ongoing            | Play Console    |
| No negative reviews citing privacy        | 0 privacy-related 1-stars | Ongoing            | Manual review   |

---

## 12. Rollback & Hotfix Procedures

### 12.1 Rollback Procedure

If a critical issue is discovered post-release that cannot be fixed quickly:

| Step | Action                                                           |
| ---- | ---------------------------------------------------------------- |
| 1    | Halt staged rollout in Play Console (if not at 100%)            |
| 2    | Assess severity and impact scope                                |
| 3    | If rollback warranted: use Play Console to roll back to the previous version (if one exists) |
| 4    | For MVP (no previous version): halt rollout and prepare hotfix   |
| 5    | Communicate status via Play Store listing update (if needed)    |

**Note:** MVP is the first release — there is no previous version to roll back to. The only option is to halt the staged rollout and prepare a hotfix.

### 12.2 Hotfix Procedure

| Step | Action                                                           | Timeline      |
| ---- | ---------------------------------------------------------------- | ------------- |
| 1    | Identify and reproduce the issue                                | Within 4 hours|
| 2    | Create a hotfix branch: `hotfix/1.0.1-description`             | Immediate     |
| 3    | Fix the issue with minimal code change                          | ASAP          |
| 4    | Run the core regression suite (TestPlan Section 16.1, P0 tests) | Before merge  |
| 5    | Run the release candidate smoke test (Section 10.1)             | Before build  |
| 6    | Increment version: code +1, name "1.0.1"                       | Before build  |
| 7    | Generate signed release AAB                                     | After test    |
| 8    | Submit to Play Store with expedited review request              | Immediate     |
| 9    | Resume staged rollout once hotfix is verified                   | After approval|

### 12.3 Hotfix Severity Classification

| Severity     | Response Time       | Examples                                          |
| ------------ | ------------------- | ------------------------------------------------- |
| **P0**       | Fix within 24 hours | App crash on launch, ghost overlay, data loss     |
| **P1**       | Fix within 72 hours | Touch interference, service won't start, wrong timing |
| **P2**       | Fix in next release  | Minor UX defect, cosmetic issue                   |

### 12.4 Version Control for Hotfixes

From ProjectPlan Section 13.3 and CodingStandards:

```
main (release-ready)
  ├── hotfix/1.0.1-ghost-overlay-fix
  ├── hotfix/1.0.2-touch-interference-fix
  └── ...

develop (integration)
  ├── feature/v1.1-dashboard
  └── ...
```

- Hotfixes branch from `main`, are merged back into both `main` and `develop`.
- Hotfix commits use conventional format: `fix: [description]`
- Tag format: `v1.0.1`, `v1.0.2`

---

## 13. Release Risk Assessment

### 13.1 Release-Specific Risks

These are risks that specifically affect the release process (beyond development risks covered in RiskAssessment.md):

| Risk ID   | Risk                                               | P | I | Score | Mitigation                                              |
| --------- | -------------------------------------------------- | - | - | ----- | ------------------------------------------------------- |
| RR-001    | Google Play rejects PACKAGE_USAGE_STATS declaration| 3 | 5 | **15**| Thorough declaration (Section 7.2), privacy policy, appeal letter prepared |
| RR-002    | Google Play rejects foreground service specialUse  | 2 | 4 | **8** | Clear justification (Section 7.3), competitor research  |
| RR-003    | Release keystore lost or compromised               | 1 | 5 | **5** | Create early, backup in 2 locations, use Play App Signing (Section 6.2) |
| RR-004    | ProGuard/R8 breaks runtime behavior in release build| 2 | 4 | **8** | Smoke test on 3 devices (Section 10.1), keep rules updated |
| RR-005    | Staged rollout reveals critical bug missed in testing| 2 | 4 | **8** | Phased rollout (20% → 50% → 100%), pause triggers defined |
| RR-006    | Privacy policy deemed insufficient by Google       | 2 | 4 | **8** | Comprehensive policy (Section 8.1), model after approved apps |
| RR-007    | High 1-star review rate in first week              | 3 | 3 | **9** | Monitor daily (Section 11.2), respond quickly, prepare hotfix |
| RR-008    | OEM-specific issue surfaces at scale               | 3 | 3 | **9** | OEM testing done pre-release, add troubleshooting guidance post-launch |

### 13.2 Mitigation Actions Before Submission

| Action                                              | Mitigates Risk | Status   |
| --------------------------------------------------- | -------------- | -------- |
| Prepare detailed PACKAGE_USAGE_STATS declaration    | RR-001         | Pending  |
| Prepare appeal letter for potential rejection       | RR-001         | Pending  |
| Research competitor service type declarations       | RR-002         | Pending  |
| Create and backup release keystore in Phase 1       | RR-003         | Pending  |
| Enable Google Play App Signing                      | RR-003         | Pending  |
| Smoke test release build on 3 OEM devices           | RR-004         | Pending  |
| Define staged rollout with pause triggers           | RR-005         | Complete |
| Finalize and host privacy policy                    | RR-006         | Pending  |
| Set up Play Console monitoring alerts               | RR-007         | Pending  |
| Include OEM troubleshooting in About screen         | RR-008         | Pending  |

### 13.3 Release Gate Risk Status

From RiskAssessment.md Section 13, these criteria must be met:

| Gate Criterion                                           | Risk Addressed | Status   |
| -------------------------------------------------------- | -------------- | -------- |
| Service uptime ≥ 95% on Must OEMs (8-hour test)         | RA-002         | Pending  |
| Overlay tested on 10 host apps without interference      | RA-010         | Pending  |
| Zero ghost overlays in all test scenarios                | RA-011         | Pending  |
| State machine 100% unit test coverage                    | RA-012         | Pending  |
| No memory leaks over 8-hour endurance test               | RA-022         | Pending  |
| Battery consumption ≤ 2%/hr confirmed                    | RA-005         | Pending  |
| Privacy policy compliant with Play Store requirements    | RA-001, RA-016 | Pending  |

---

## 14. Post-MVP Release Roadmap

### 14.1 Phased Release Plan

From PRD Section 16.1:

| Release   | Features                                                          | Goal                                         |
| --------- | ----------------------------------------------------------------- | -------------------------------------------- |
| **v1.0**  | F-001–F-015 (Core monitoring, overlay, thresholds, onboarding)   | Functional product; validates core value prop |
| **v1.1**  | F-016–F-023 (Dashboard, history, per-app thresholds, pause, schedule) | Enriches experience; supports retention |
| **v1.2+** | F-024–F-032 (Trends, widget, export, accessibility mode)         | Polish and reach; broader audience           |

### 14.2 v1.1 Feature Details

| Feature   | Description                    | Dependencies                      |
| --------- | ------------------------------ | --------------------------------- |
| F-016     | Usage Dashboard                | UsageSession data (already collected in MVP) |
| F-017     | Session History List           | UsageSession data                 |
| F-018     | Per-App Thresholds             | ThresholdConfig.appPackageName (schema ready) |
| F-019     | Pause/Resume Monitoring        | Service lifecycle extension       |
| F-020     | App Categories                 | UI enhancement                    |
| F-021     | Quick-Select Presets           | App selection UI enhancement      |
| F-022     | Timer Tap Interaction          | Overlay touch handler extension   |
| F-023     | Monitoring Schedule            | Time-based service control        |

**Database Migration:** v1.1 will require a Room schema migration from version 1 to version 2. The v1 schema was designed with forward compatibility (ThresholdConfig already has `appPackageName` field for per-app overrides). Migration must be non-destructive (RA-018).

### 14.3 v1.2+ Feature Details

| Feature   | Description                    |
| --------- | ------------------------------ |
| F-024     | Weekly/Monthly Usage Trends    |
| F-025     | Notification-Based Alerts      |
| F-026     | Home Screen Widget             |
| F-027     | Dark Mode Support              |
| F-028     | CSV Data Export                 |
| F-029     | Accessibility Mode             |
| F-030     | Overlay Dismiss Gesture        |
| F-031     | Focus Mode Integration         |
| F-032     | Show All Apps (including system)|

### 14.4 Update Release Process

For all post-MVP releases:

| Step | Action                                                    |
| ---- | --------------------------------------------------------- |
| 1    | Feature development on feature branches off `develop`     |
| 2    | Integration testing on `develop` branch                   |
| 3    | Release candidate branch: `release/1.1.0`                |
| 4    | Full regression suite + new feature tests                |
| 5    | Database migration tests (if schema changed)             |
| 6    | Release candidate smoke test on 3 Must OEM devices       |
| 7    | Increment version code and version name                  |
| 8    | Generate signed AAB, submit to Play Store                |
| 9    | Staged rollout: 20% → 50% → 100%                        |
| 10   | Merge release branch back to `main` and `develop`        |

---

## 15. Communication Plan

### 15.1 Pre-Release Communication

| Timing           | Audience          | Channel           | Content                           |
| ---------------- | ----------------- | ------------------ | --------------------------------- |
| Before submission| Internal          | Project log        | Release readiness confirmation    |

### 15.2 Release Day Communication

| Action                                          | Responsibility |
| ----------------------------------------------- | -------------- |
| Verify app appears on Play Store                | Rudy           |
| Test download and install from Play Store       | Rudy           |
| Verify Play Store listing renders correctly     | Rudy           |
| Verify privacy policy link works in listing     | Rudy           |

### 15.3 Post-Release Communication

| Timing           | Audience          | Channel            | Content                          |
| ---------------- | ----------------- | -------------------- | -------------------------------- |
| Daily (week 1–2) | Internal          | Project log         | Metrics review, review monitoring|
| Weekly (weeks 3–12)| Internal        | Project log         | Performance summary              |
| As needed        | Users (Play Store)| Play Store responses| Respond to user reviews          |
| As needed        | Users             | Play Store listing  | Update description for clarity   |

### 15.4 Review Response Guidelines

| Review Type                        | Response Approach                                   |
| ---------------------------------- | --------------------------------------------------- |
| Positive (4-5 stars)               | Thank the user; acknowledge specific feedback       |
| Feature request                    | Acknowledge; mention if planned for future release   |
| Bug report                         | Acknowledge; ask for device details; log internally  |
| Privacy concern                    | Clearly explain no-internet architecture; link privacy policy |
| OEM service killed                 | Provide OEM-specific battery settings guidance; link dontkillmyapp.com |
| Negative (1-2 stars, vague)        | Ask for specifics to help improve                    |

---

## 16. Release Artifact Inventory

### 16.1 Required Artifacts

| Artifact                           | Format      | Location                           | Status   |
| ---------------------------------- | ----------- | ---------------------------------- | -------- |
| Signed release AAB                 | .aab        | Build output                       | Pending  |
| Release keystore                   | .jks/.pkcs12| Encrypted local + cloud backup     | Pending  |
| Keystore password (documented)     | Secure note | Separate from keystore file        | Pending  |
| Privacy policy (web-hosted)        | HTML/URL    | To be determined                   | Pending  |
| Play Store short description       | Text        | Section 7.1.2                      | Draft    |
| Play Store full description        | Text        | Section 7.1.3                      | Draft    |
| Phone screenshots (min 2)          | PNG/JPEG    | To be captured                     | Pending  |
| Feature graphic                    | PNG/JPEG    | To be designed                     | Pending  |
| App icon (512 × 512 hi-res)       | PNG         | Project resources                  | Pending  |
| PACKAGE_USAGE_STATS declaration    | Text        | Section 7.2                        | Draft    |
| specialUse service justification   | Text        | Section 7.3                        | Draft    |
| Data Safety section answers        | Text        | Section 7.4                        | Draft    |
| Content rating questionnaire       | Text        | Section 7.5                        | Draft    |
| Appeal letter (contingency)        | Text        | To be prepared                     | Pending  |

### 16.2 Source Control Tags

| Tag        | Content                        | Created When             |
| ---------- | ------------------------------ | ------------------------ |
| `v1.0.0`   | MVP release commit             | After final release build|
| `v1.0.1`   | First hotfix (if needed)       | After hotfix build       |

---

## 17. Roles & Responsibilities

| Role                   | Person | Responsibilities                                          |
| ---------------------- | ------ | --------------------------------------------------------- |
| Release Manager        | Rudy   | Build, sign, submit, monitor rollout, go/no-go decisions  |
| Developer              | Rudy   | Bug fixes, hotfixes, code freeze compliance               |
| QA                     | Rudy   | Release verification testing, smoke tests, regression     |
| Content Author         | Rudy   | Play Store listing copy, screenshots, privacy policy      |
| Reviewer               | —      | External code review (if available)                       |

---

## 18. Release Timeline

### 18.1 Phase 10 Task Breakdown

From ProjectPlan WBS 10.1–10.10:

| WBS ID | Task                                              | Depends On       | Status   |
| ------ | ------------------------------------------------- | ---------------- | -------- |
| 10.1   | Write user-facing privacy policy                  | WBS 0.9          | Pending  |
| 10.2   | Integrate privacy policy link in About screen     | 10.1, WBS 6.8    | Pending  |
| 10.3   | Prepare PACKAGE_USAGE_STATS declaration form      | 10.1             | Pending  |
| 10.4   | Configure release signing (keystore)              | WBS 1.4          | Pending  |
| 10.5   | Generate signed release AAB                       | 10.4, WBS 9.13   | Pending  |
| 10.6   | Prepare Play Store listing (title, desc, screenshots) | WBS 9.13    | Pending  |
| 10.7   | Complete content rating questionnaire             | 10.6             | Pending  |
| 10.8   | Submit to Google Play for review                  | 10.3, 10.5–10.7  | Pending  |
| 10.9   | Address Google Play review feedback (if any)      | 10.8             | Pending  |
| 10.10  | Staged rollout: 20% → 50% → 100%                 | 10.9             | Pending  |

### 18.2 Milestone Dependencies

```
M9 (Testing Complete)
  │
  ├──► 10.1 Privacy Policy
  │      └──► 10.2 Integrate in App
  │      └──► 10.3 PACKAGE_USAGE_STATS Declaration
  │
  ├──► 10.4 Release Signing (should be done in P1)
  │      └──► 10.5 Generate Signed AAB
  │
  ├──► 10.6 Store Listing
  │      └──► 10.7 Content Rating
  │
  └──► 10.8 Submit to Google Play
         └──► 10.9 Address Feedback
               └──► 10.10 Staged Rollout
                     │
                     ▼
               M10 (Released)
```

### 18.3 Estimated Timeline

| Activity                              | Estimated Duration |
| ------------------------------------- | ------------------ |
| Privacy policy finalization           | 1 day              |
| Privacy policy integration in app     | 0.5 day            |
| Store listing preparation             | 1 day              |
| Screenshot capture                    | 0.5 day            |
| Release build generation              | 0.5 day            |
| Release verification testing          | 1 day              |
| Submission to Google Play             | 0.5 day            |
| Google Play review                    | 1–7 days (external)|
| Address feedback (if any)             | 1–3 days           |
| Staged rollout (20% → 50% → 100%)    | 6–10 days          |
| **Total Phase 10**                    | **~2–3 weeks**     |

---

## 19. Traceability Matrix

### 19.1 PRD Release Criteria → Release Plan Section

| PRD Release Criterion (Section 16.2)                    | Release Plan Section                    |
| ------------------------------------------------------- | --------------------------------------- |
| 1. All F-001–F-015 implemented and tested               | Section 4.1 (#1), Section 3.1          |
| 2. Google Play policy review passed                     | Section 7.2, Section 7.3, Section 8    |
| 3. Service reliable 8+ hours on Pixel, Samsung, Xiaomi  | Section 4.1 (#3), Section 10.1 (#14)   |
| 4. Battery ≤ 2%/hr confirmed                            | Section 4.1 (#4)                       |
| 5. Overlay tested on 10 host apps                       | Section 4.1 (#5)                       |
| 6. Onboarding >80% grant rate                           | Section 4.1 (#6)                       |
| 7. Zero critical/high bugs                              | Section 4.1 (#7)                       |
| 8. Privacy policy published and accessible              | Section 4.1 (#8), Section 8.1          |

### 19.2 ProjectPlan WBS → Release Plan Section

| WBS Task | Description                                   | Release Plan Section        |
| -------- | --------------------------------------------- | --------------------------- |
| 10.1     | Write user-facing privacy policy              | Section 8.1                 |
| 10.2     | Integrate privacy policy in About screen      | Section 8.1                 |
| 10.3     | Prepare PACKAGE_USAGE_STATS declaration       | Section 7.2                 |
| 10.4     | Configure release signing                     | Section 6.2                 |
| 10.5     | Generate release AAB                          | Section 6.3                 |
| 10.6     | Prepare Play Store listing                    | Section 7.1                 |
| 10.7     | Content rating questionnaire                  | Section 7.5                 |
| 10.8     | Submit to Google Play                         | Section 7, Section 9        |
| 10.9     | Address review feedback                       | Section 12.1                |
| 10.10    | Staged rollout                                | Section 9                   |

### 19.3 Risk → Release Plan Mitigation

| Risk ID | Risk Description                              | Release Plan Mitigation Section      |
| ------- | --------------------------------------------- | ------------------------------------ |
| RA-001  | Google Play rejects PACKAGE_USAGE_STATS       | Section 7.2 (declaration), Section 8 (privacy) |
| RA-002  | OEM battery kill                              | Section 10.1 (smoke test #14), Section 11.2 |
| RA-004  | Overlay annoying                              | Section 11.2 (review monitoring)     |
| RA-005  | Battery drain complaints                      | Section 11.2 (review monitoring)     |
| RA-009  | Keystore lost or compromised                  | Section 6.2 (signing strategy)       |
| RA-010  | Touch interference                            | Section 9.3 (rollout pause trigger)  |
| RA-011  | Ghost overlay                                 | Section 9.3 (rollout pause trigger), Section 12.2 |
| RA-016  | Privacy policy insufficient                   | Section 8.1, Section 8.2             |
| RA-021  | Foreground service type rejected              | Section 7.3 (justification)          |

### 19.4 TestPlan → Release Plan Mapping

| TestPlan Section                   | Release Plan Section             |
| ---------------------------------- | -------------------------------- |
| Section 16 (Regression Suite)      | Section 4.2 (#13)               |
| Section 19.3 (MVP Release Gate)    | Section 4.1                     |
| Section 15 (Endurance Tests)       | Section 4.1 (#3)                |
| Section 13 (Security/Privacy)      | Section 8.2                     |
| Section 10 (OEM Compatibility)     | Section 10.1 (smoke test)       |

---

## 20. Glossary

All terms from PRD (Section 17), SRS (Section 14), and other project documents apply. Additional release-specific terms:

| Term                        | Definition                                                                                      |
| --------------------------- | ----------------------------------------------------------------------------------------------- |
| **AAB**                     | Android App Bundle — the publishing format for Google Play Store distribution.                   |
| **APK**                     | Android Package — the installable format used for testing; generated from AAB via bundletool.    |
| **Staged Rollout**          | A phased release strategy where the app is made available to an increasing percentage of users.  |
| **Code Freeze**             | A point after which no new features are added; only bug fixes are permitted.                    |
| **Release Candidate (RC)**  | A build that is considered potentially ready for release, pending final verification.            |
| **Hotfix**                  | A minimal, urgent release that addresses a critical bug in the production build.                |
| **Go/No-Go**               | A decision point where the release authority decides whether to proceed with or halt the release.|
| **ANR**                     | Application Not Responding — an Android system dialog triggered when the main thread is blocked. |
| **Google Play App Signing** | A service where Google holds the app signing key, allowing recovery if the upload key is lost.   |
| **Upload Key**              | The key used to sign the AAB before uploading to Play Console; separate from the app signing key.|
| **Play Console**            | Google's web portal for managing app listings, releases, and analytics on Google Play.          |
| **Data Safety**             | A Google Play Store section where developers declare their data collection and sharing practices.|
| **Content Rating**          | An age-based rating assigned to the app based on a questionnaire (e.g., Everyone, Teen, Mature).|
| **Feature Graphic**         | A 1024×500 px image displayed at the top of the Play Store listing.                             |

---

## 21. References

| Reference                                                                                      | Relevance                                              |
| ---------------------------------------------------------------------------------------------- | ------------------------------------------------------ |
| [PRD.md](PRD.md)                                                                               | Product vision, release strategy, success metrics       |
| [SRS.md](SRS.md)                                                                               | Functional/non-functional requirements, acceptance criteria |
| [SAD.md](SAD.md)                                                                               | Architecture, build variants, security model            |
| [TDD.md](TDD.md)                                                                               | Build configuration, ProGuard rules, version strategy  |
| [DbSchema.md](DbSchema.md)                                                                     | Database schema, migration strategy                    |
| [UIUXspec.md](UIUXspec.md)                                                                     | Screen inventory for screenshots, visual design        |
| [ProjectPlan.md](ProjectPlan.md)                                                               | Phase 10 WBS, milestones, Definition of Done           |
| [RiskAssessment.md](RiskAssessment.md)                                                         | Release-affecting risks (RA-001, RA-009, RA-016, RA-021)|
| [CodingStandards.md](CodingStandards.md)                                                       | Build config, git conventions, testing rules            |
| [Privacy.md](Privacy.md)                                                                       | Privacy policy, Google Play compliance, Data Safety     |
| [TestPlan.md](TestPlan.md)                                                                     | Release verification, regression suite, exit criteria  |
| [Google Play Console Help](https://support.google.com/googleplay/android-developer)             | Store submission process                               |
| [Google Play Usage Access Policy](https://support.google.com/googleplay/android-developer/answer/9888170) | PACKAGE_USAGE_STATS declaration requirements |
| [Google Play Data Safety](https://support.google.com/googleplay/android-developer/answer/10787469) | Data Safety section guidance                       |
| [Google Play App Signing](https://developer.android.com/studio/publish/app-signing)             | Keystore and signing best practices                    |
| [Android App Bundle](https://developer.android.com/guide/app-bundle)                            | AAB format and bundletool                              |
| [Don't Kill My App](https://dontkillmyapp.com/)                                                | OEM battery management reference for post-release support |

---

*End of Document*
