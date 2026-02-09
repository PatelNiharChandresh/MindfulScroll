# Risk Assessment

## MindfulScroll — Digital Awareness Companion

---

### Document Control

| Field              | Value                                          |
| ------------------ | ---------------------------------------------- |
| **Document Title** | MindfulScroll Risk Assessment                   |
| **Version**        | 1.0                                            |
| **Status**         | Draft                                          |
| **Created**        | 2026-02-05                                     |
| **Last Updated**   | 2026-02-05                                     |
| **Author**         | Rudy                                           |
| **Reviewers**      | —                                              |
| **Approved By**    | —                                              |

### Revision History

| Version | Date       | Author | Changes       |
| ------- | ---------- | ------ | ------------- |
| 1.0     | 2026-02-05 | Rudy   | Initial draft |

### Parent Documents

| Document                         | Version | Relationship                                        |
| -------------------------------- | ------- | --------------------------------------------------- |
| [PRD.md](PRD.md)                 | 1.0     | Product definition — features, scope, assumptions    |
| [SRS.md](SRS.md)                 | 1.0     | Technical requirements — FR, NFR, edge cases         |
| [ProjectPlan.md](ProjectPlan.md) | 1.0     | Execution plan — phases, WBS, schedule, milestones   |

---

## Table of Contents

1. [Introduction](#1-introduction)
2. [Risk Assessment Methodology](#2-risk-assessment-methodology)
3. [Risk Register](#3-risk-register)
4. [Risk Category Analysis](#4-risk-category-analysis)
5. [Assumption Validation Risks](#5-assumption-validation-risks)
6. [Constraint-Derived Risks](#6-constraint-derived-risks)
7. [Phase-Specific Risk Profile](#7-phase-specific-risk-profile)
8. [Risk Interdependencies](#8-risk-interdependencies)
9. [Risk Heat Map](#9-risk-heat-map)
10. [Mitigation Plan Summary](#10-mitigation-plan-summary)
11. [Contingency Budget](#11-contingency-budget)
12. [Monitoring & Review Framework](#12-monitoring--review-framework)
13. [Risk Acceptance Criteria](#13-risk-acceptance-criteria)
14. [Glossary](#14-glossary)
15. [References](#15-references)

---

## 1. Introduction

### 1.1 Purpose

This Risk Assessment provides a comprehensive, structured analysis of all risks that could affect the successful delivery, launch, and operation of MindfulScroll MVP (v1.0). It consolidates risks identified across the PRD (Section 15.1, R-01–R-07), SRS (Section 11, EH-001–EH-008, EC-01–EC-10, NFR targets), and Project Plan (Section 10.1, R-01–R-09), expands upon them with deeper analysis, and introduces new risks uncovered through cross-document review.

This document provides a single source of truth for risk management decisions throughout the project lifecycle.

### 1.2 Scope

This assessment covers risks affecting:

- **Development:** Phases 0–10 as defined in the Project Plan.
- **Launch:** Google Play Store submission and approval.
- **Post-launch:** First 90 days of production operation.

### 1.3 Intended Audience

| Audience            | Usage                                                       |
| ------------------- | ----------------------------------------------------------- |
| Developer(s)        | Technical risk awareness; mitigation implementation          |
| Project owner       | Risk prioritization; go/no-go decisions; budget allocation   |
| QA / Testers        | Risk-focused test planning; regression priority              |
| Reviewers           | Phase gate risk reviews                                      |

### 1.4 Relationship to Other Documents

- **PRD Section 15.1** identified 7 product-level risks (R-01–R-07).
- **SRS Section 11** defined 8 error handling scenarios and 10 edge cases.
- **Project Plan Section 10.1** expanded to 9 project-level risks (R-01–R-09).
- **This document** consolidates all of the above, adds 16 new risks, provides deeper analysis, and establishes a monitoring framework.

---

## 2. Risk Assessment Methodology

### 2.1 Risk Scoring Framework

Each risk is evaluated on two dimensions:

**Probability (P):**

| Level      | Score | Definition                                                     |
| ---------- | ----- | -------------------------------------------------------------- |
| Very Low   | 1     | Unlikely to occur (< 10% chance)                               |
| Low        | 2     | Could occur but improbable (10–25% chance)                     |
| Medium     | 3     | Reasonable chance of occurring (25–50% chance)                  |
| High       | 4     | More likely than not to occur (50–75% chance)                   |
| Very High  | 5     | Almost certain to occur (> 75% chance)                          |

**Impact (I):**

| Level      | Score | Definition                                                     |
| ---------- | ----- | -------------------------------------------------------------- |
| Negligible | 1     | No meaningful effect on project or product                      |
| Minor      | 2     | Slight delay or quality reduction; easily recoverable           |
| Moderate   | 3     | Noticeable delay, feature degradation, or cost increase         |
| Major      | 4     | Significant delay, feature loss, or user experience degradation |
| Critical   | 5     | Project failure, product unusable, or store rejection            |

**Risk Score (R):** `R = P × I`

### 2.2 Risk Priority Levels

| Score Range | Priority     | Action Required                                              |
| ----------- | ------------ | ------------------------------------------------------------ |
| 1–4         | **Low**      | Accept and monitor. No active mitigation needed.              |
| 5–9         | **Medium**   | Mitigate proactively. Monitor at each phase gate.             |
| 10–15       | **High**     | Mitigation plan required. Assign owner. Track weekly.         |
| 16–25       | **Critical** | Immediate mitigation required. May block project proceed.     |

### 2.3 Risk Categories

| Category            | Code | Scope                                                        |
| ------------------- | ---- | ------------------------------------------------------------ |
| **Technical**       | TEC  | Implementation complexity, API behavior, performance          |
| **Platform**        | PLT  | Android OS, OEM variations, API restrictions                  |
| **User Experience** | UXR  | Usability, adoption, perception, onboarding                   |
| **Business**        | BUS  | Store policies, monetization, market positioning               |
| **Operational**     | OPS  | Process, tooling, resources, schedule                         |
| **Security/Privacy**| SEC  | Data protection, permission misuse, policy compliance         |

---

## 3. Risk Register

### 3.1 Consolidated Risk Register

This register consolidates and re-numbers all risks from source documents and adds newly identified risks.

---

#### RA-001: Google Play Rejects App Due to PACKAGE_USAGE_STATS Policy

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | BUS (Business)                                               |
| **Source**         | PRD R-01, ProjectPlan R-01                                   |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 5 (Critical)                                                 |
| **Risk Score**     | **15 (High)**                                                |
| **Phase(s)**       | P10 (Release Preparation)                                    |
| **Traces To**      | PRD A-10, SRS PMR-001, ProjectPlan WBS 10.3                  |

**Description:** Google Play requires a policy declaration for apps using the `PACKAGE_USAGE_STATS` permission. The app may be rejected if Google's review team deems the use of Usage Access inappropriate, the privacy policy insufficient, or the declared use case outside their guidelines. This is an external dependency entirely outside our control.

**Mitigation Strategy:**
1. Write a comprehensive privacy policy early (Phase 0, WBS 0.9) — do not defer.
2. Study approved apps that use `PACKAGE_USAGE_STATS` (ActionDash, StayFree, YourHour) and model the declaration after their approach.
3. Clearly state in the policy declaration that Usage Access is used solely for foreground app detection, that no data leaves the device, and that no individual app usage data is shared with third parties.
4. Include in-app privacy disclosures accessible from the About screen (FR-041).
5. Prepare a detailed appeal letter before submission, anticipating common rejection reasons.

**Contingency:**
- If rejected: File an appeal with additional justification and a video demonstrating the app's behavior.
- If appeal fails: Pivot to sideload distribution (APK via website), F-Droid, or alternative stores while addressing feedback.
- If fundamentally rejected: Explore alternative foreground detection methods (AccessibilityService — but this has its own policy restrictions).

**Early Warning Indicators:**
- Policy changes affecting Usage Access apps announced by Google.
- Competing apps (ActionDash, StayFree) reported being rejected or restricted.

---

#### RA-002: OEM Battery Optimization Kills Foreground Service

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | PLT (Platform)                                               |
| **Source**         | PRD R-02, ProjectPlan R-02, SRS EH-003                       |
| **Probability**    | 4 (High)                                                     |
| **Impact**         | 4 (Major)                                                    |
| **Risk Score**     | **16 (Critical)**                                            |
| **Phase(s)**       | P3, P8, P9                                                   |
| **Traces To**      | SRS NFR-006, FR-037, FR-038, PRD C-03, PRD TG-4              |

**Description:** Android OEMs (Samsung, Xiaomi, Huawei, Oppo, OnePlus, Vivo) implement aggressive battery optimization that goes beyond stock Android's behavior. These customizations can kill foreground services even when battery optimization exemption is granted, particularly on Xiaomi (MIUI/HyperOS), Samsung (One UI), and Huawei (EMUI). This is the single highest-severity risk in the project.

**Detailed OEM Risk Analysis:**

| OEM      | Skin           | Aggressiveness | Specific Mechanisms                                          |
| -------- | -------------- | -------------- | ------------------------------------------------------------ |
| Xiaomi   | MIUI/HyperOS   | Very High      | Autostart restrictions, memory cleanup, battery saver kills services. Must enable "autostart" and lock app in recents. |
| Samsung  | One UI         | High           | Sleeping apps list, adaptive battery, background activity limits. Must add to "never sleeping apps." |
| Huawei   | EMUI           | Very High      | App launch management (auto/manual/no management), power-intensive prompt. |
| OnePlus  | OxygenOS       | Medium         | Battery optimization, app hibernation. Generally more permissive than Xiaomi/Samsung. |
| Oppo     | ColorOS        | High           | Similar to MIUI — background management is aggressive.        |
| Google   | Stock AOSP     | Low            | Standard Android battery optimization; exemption works reliably. |

**Mitigation Strategy:**
1. Implement `START_STICKY` return from `onStartCommand()` (SRS FR-037.AC1).
2. Request `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS` during onboarding (SRS FR-032 Step 4).
3. Implement BootReceiver for `BOOT_COMPLETED` auto-restart (SRS FR-038).
4. Test on Samsung and Xiaomi devices starting in Phase 3 — do not wait until Phase 9.
5. Add a "Troubleshooting" section in the app that links to dontkillmyapp.com instructions per OEM.
6. Implement a "heartbeat" check: the service writes a timestamp to preferences every 5 minutes; if the timestamp is stale when the app is opened, warn the user that monitoring was interrupted.
7. Consider implementing a `WorkManager` periodic check as a backup restart mechanism.

**Contingency:**
- If service uptime drops below targets on a Must OEM: Add OEM-specific onboarding steps that guide users through manufacturer settings (e.g., "Lock app in recents" for Xiaomi).
- If service is fundamentally unreliable: Explore `AlarmManager` with `setExactAndAllowWhileIdle()` as a fallback polling mechanism.

**Early Warning Indicators:**
- Service kills observed during Phase 3 testing on non-Pixel devices.
- Service uptime below 90% on any Must OEM during 8-hour endurance test.

---

#### RA-003: Users Abandon During Onboarding Due to Complex Permission Flow

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | UXR (User Experience)                                        |
| **Source**         | PRD R-03, ProjectPlan R-03                                   |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 4 (Major)                                                    |
| **Risk Score**     | **12 (High)**                                                |
| **Phase(s)**       | P7, P9                                                       |
| **Traces To**      | SRS FR-032, NFR-009, PRD UG-3                                |

**Description:** MindfulScroll requires 3 separate permissions, each requiring manual navigation to System Settings. This multi-step process is significantly more complex than typical app permission flows and may cause users to abandon the app before completing setup.

**Risk Factors:**
- Each permission requires leaving the app, navigating to Settings, toggling a switch, and returning.
- Users may not understand why these permissions are needed despite explanations.
- The round-trip disrupts the onboarding flow and creates opportunities for distraction/abandonment.
- Some OEMs render the Settings pages differently, potentially confusing users.

**Mitigation Strategy:**
1. Use clear, non-technical language for each permission explanation (SRS NFR-009.AC1).
2. Include visual illustrations showing exactly what the user needs to do in Settings (SRS NFR-009.AC2).
3. Target < 90 seconds for complete onboarding (SRS NFR-009.AC3).
4. Allow skipping each permission with a clear warning about impact (SRS FR-032.AC7).
5. Show immediate positive feedback (green checkmark, animation) when a permission is granted.
6. Test onboarding with at least 3 non-technical users before release.
7. Track which permission step has the highest drop-off (via internal testing, not analytics SDK).

**Contingency:**
- If grant rate drops below 80%: Simplify the flow — consider combining the explanation screens or showing a single "set up" overview before launching all permission requests.
- If a specific permission step causes disproportionate abandonment: Add a "Set up later" option that allows the user to enter the app with reduced functionality and be prompted later.

**Early Warning Indicators:**
- Internal testing shows > 30% of testers struggling with any single permission step.
- Average onboarding time exceeds 2 minutes.

---

#### RA-004: Overlay Perceived as Annoying Rather Than Helpful

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | UXR (User Experience)                                        |
| **Source**         | PRD R-04, ProjectPlan R-04                                   |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 4 (Major)                                                    |
| **Risk Score**     | **12 (High)**                                                |
| **Phase(s)**       | P4, P5, P9                                                   |
| **Traces To**      | SRS FR-019, FR-028, PRD P3 (Minimal Intrusion)               |

**Description:** The core value proposition of MindfulScroll depends on the floating timer being perceived as a gentle awareness tool, not an annoying obstruction. If users find it bothersome, they will disable monitoring or uninstall the app, undermining the entire product.

**Risk Factors:**
- The overlay occupies screen real estate on apps the user is actively engaging with.
- Even at 48–56dp and 65% opacity (Calm state), some users may find it distracting.
- The throb animation could cross the line from "noticeable" to "irritating."
- The overlay may obstruct UI elements in certain host apps (navigation bars, action buttons, input fields).
- Users may associate MindfulScroll with guilt or shame, creating a negative emotional response.

**Mitigation Strategy:**
1. Keep overlay size minimal: 48dp default (SRS FR-019.AC1), only scale up during throb (1.15x max).
2. Initial Calm opacity is 65% — the overlay is present but not demanding attention (SRS FR-028).
3. Fade-in animation (500ms) prevents jarring appearance (SRS FR-020).
4. Draggable overlay allows user to move it away from obstructed content (SRS FR-021).
5. Edge-snapping keeps the overlay neatly positioned and out of central content (SRS FR-022).
6. Validate overlay positioning on 10 host apps (ProjectPlan WBS 9.8).
7. Collect qualitative feedback from testers: "Does the timer feel helpful or annoying?"

**Contingency:**
- If feedback is consistently "annoying": Reduce default Calm opacity to 50%. Add a user-configurable opacity setting. Add a temporary dismiss gesture (swipe to hide for current session — currently planned for Future, F-030; may need to be pulled into MVP).
- If specific host apps are problematic: Implement app-specific overlay position overrides.

---

#### RA-005: Battery Drain Complaints Lead to Negative Reviews

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | TEC (Technical)                                              |
| **Source**         | PRD R-05, ProjectPlan R-05                                   |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 3 (Moderate)                                                 |
| **Risk Score**     | **9 (Medium)**                                               |
| **Phase(s)**       | P3, P8, P9                                                   |
| **Traces To**      | SRS NFR-002, FR-010, PRD TG-1, PRD P5                        |

**Description:** A background monitoring service that polls every 2–3 seconds has inherent battery cost. If MindfulScroll appears in Android's battery usage statistics or users perceive faster battery drain, negative reviews will follow. Even 2%/hour over extended periods accumulates.

**Mitigation Strategy:**
1. Implement adaptive polling: no polling when screen is off (SRS FR-010).
2. Profile battery consumption rigorously in Phase 8 (ProjectPlan WBS 8.9).
3. Final verification in Phase 9 (ProjectPlan WBS 9.10).
4. Target ≤ 2% per hour (SRS NFR-002.AC1) and near 0% when screen off (SRS NFR-002.AC2).
5. Ensure MindfulScroll does not appear in top 5 battery consumers (SRS NFR-002.AC3).
6. If borderline: consider increasing polling interval to 3–5 seconds (with detection latency trade-off).

**Contingency:**
- If battery drain exceeds 2%/hour: Increase polling interval to 5 seconds and accept up to 5-second detection latency.
- If still problematic: Implement a "battery saver" mode that pauses monitoring during low battery.

---

#### RA-006: Android OS Updates Break UsageStats or Overlay APIs

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | PLT (Platform)                                               |
| **Source**         | PRD R-06, ProjectPlan R-06                                   |
| **Probability**    | 2 (Low)                                                      |
| **Impact**         | 4 (Major)                                                    |
| **Risk Score**     | **8 (Medium)**                                               |
| **Phase(s)**       | Post-launch                                                  |
| **Traces To**      | SRS NFR-015, EIR-001, EIR-002, PRD C-04                      |

**Description:** Google periodically tightens restrictions on background processes, overlay permissions, and usage stats access. A future Android version could restrict `SYSTEM_ALERT_WINDOW` further, deprecate `UsageStatsManager` query methods, or add new requirements for foreground services.

**Historical Precedent:**
- Android 10: Restricted background activity starts.
- Android 12: Required `foregroundServiceType` declaration.
- Android 14: Made `foregroundServiceType` mandatory with specific types.
- Each major version has introduced new restrictions on background and overlay behavior.

**Mitigation Strategy:**
1. Monitor Android Developer Preview releases starting from Android 16 beta.
2. Maintain a compatibility test suite that can be run on new preview builds.
3. Use API-level conditional code where behavior differs across versions (SRS NFR-015.AC2).
4. Follow Android developer blogs and changelog for early warnings about API deprecations.
5. Maintain a clean separation between platform-dependent code (data layer, service layer) and platform-independent code (domain layer) so that API changes require minimal rework.

**Contingency:**
- If UsageStats API is restricted: Explore `AccessibilityService` as a detection mechanism (has its own policy challenges).
- If overlay API is restricted: Fall back to notification-based time display (SRS PMR-002).
- For any breaking change: Issue an immediate patch release.

---

#### RA-007: UsageStatsManager Returns Inconsistent Data Across OEMs

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | PLT (Platform)                                               |
| **Source**         | ProjectPlan R-07, SRS EH-005                                 |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 3 (Moderate)                                                 |
| **Risk Score**     | **9 (Medium)**                                               |
| **Phase(s)**       | P3, P9                                                       |
| **Traces To**      | SRS FR-009, FR-011, EIR-001                                  |

**Description:** `UsageStatsManager` is known to behave inconsistently across OEMs. Some devices report stale data, return empty event lists despite active foreground apps, or have non-standard event timing. `queryEvents()` may not work reliably on all devices; `queryUsageStats()` with `lastTimeUsed` comparison is a necessary fallback.

**Known OEM-Specific Issues:**
- **Xiaomi (MIUI):** Sometimes delays reporting foreground events by several seconds.
- **Samsung (One UI):** Generally reliable but may batch events.
- **Huawei (EMUI):** May not report events at all in certain battery optimization states.
- **Some Chinese ROMs:** May require additional permissions or have custom UsageStats implementations.

**Mitigation Strategy:**
1. Implement dual-query strategy: primary (`queryEvents` with `MOVE_TO_FOREGROUND`) and fallback (`queryUsageStats` with `lastTimeUsed`) — SRS EIR-001.
2. Test on 3 Must OEMs in Phase 3 before building subsequent phases on top (ProjectPlan Phase 3 Risks note).
3. Handle empty results gracefully: treat as "no monitored app in foreground" and retry next poll (SRS EH-005).
4. Log query results during development to identify OEM-specific patterns.
5. Implement a configurable query window (5 seconds default, expandable to 10 seconds for problematic OEMs).

**Contingency:**
- If a Must OEM consistently returns bad data: Implement OEM detection (`Build.MANUFACTURER`) and apply OEM-specific query adjustments.
- If UsageStats is fundamentally unreliable on an OEM: Document the limitation for users of that OEM and downgrade it from Must to Could.

---

#### RA-008: Phase 3 (Engine) Takes Longer Than Expected

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | OPS (Operational)                                            |
| **Source**         | ProjectPlan R-08                                             |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 3 (Moderate)                                                 |
| **Risk Score**     | **9 (Medium)**                                               |
| **Phase(s)**       | P3                                                           |
| **Traces To**      | ProjectPlan Phase 3, Critical Path (M3)                      |

**Description:** Phase 3 (Core Monitoring Engine) sits on the critical path and involves the most technically uncertain components: UsageStatsManager integration, session state machine, foreground service lifecycle, and OEM compatibility. Any delays here cascade to all subsequent phases.

**Mitigation Strategy:**
1. Allocate extra buffer time for Phase 3 (it is the highest-risk phase).
2. Start OEM testing within Phase 3 rather than deferring all OEM testing to Phase 9.
3. Build the session state machine with exhaustive unit tests (ProjectPlan WBS 3.10) — test-driven development reduces debugging time.
4. Prototype the polling engine early with a simple test harness before integrating it into the full service.

**Contingency:**
- If significantly delayed: Defer OnePlus/Oppo/Huawei OEM testing to post-MVP. Focus on Pixel + Samsung + Xiaomi only.
- If fundamentally blocked by an OEM issue: Ship with documented OEM limitations and address in a patch release.

---

#### RA-009: Release Keystore Lost or Compromised

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | OPS (Operational)                                            |
| **Source**         | ProjectPlan R-09                                             |
| **Probability**    | 1 (Very Low)                                                 |
| **Impact**         | 5 (Critical)                                                 |
| **Risk Score**     | **5 (Medium)**                                               |
| **Phase(s)**       | P10, Post-launch                                             |
| **Traces To**      | ProjectPlan WBS 10.4                                         |

**Description:** The Android release signing keystore is the only credential that allows publishing updates to the app on Google Play. If lost, the app cannot be updated. If compromised, malicious updates could be published.

**Mitigation Strategy:**
1. Create the keystore early (Phase 1, not Phase 10) to allow proper backup procedures.
2. Store the keystore in at least 2 physically separate locations (e.g., local encrypted drive + cloud encrypted storage).
3. Use Google Play App Signing (upload key model) which allows Google to hold the app signing key — this provides a recovery path if the upload key is lost.
4. Document the keystore password in a secure location separate from the keystore file itself.
5. Never commit the keystore or passwords to version control.

**Contingency:**
- If upload key is lost but Google Play App Signing is enabled: Request a new upload key from Google (supported process).
- If keystore is lost without Google Play App Signing: The app must be published as a new listing — all existing users, reviews, and download history are lost. **Prevent this at all costs.**

---

#### RA-010: Overlay Interferes with Host App Touch Events

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | TEC (Technical)                                              |
| **Source**         | New (derived from SRS FR-019, FR-021, PRD C-05, C-09)        |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 4 (Major)                                                    |
| **Risk Score**     | **12 (High)**                                                |
| **Phase(s)**       | P4, P9                                                       |
| **Traces To**      | SRS FR-019.AC6, FR-019.AC7, FR-021.AC5, PRD TG-3             |

**Description:** The floating timer overlay must coexist with host apps without intercepting their touch events. If the overlay blocks scroll gestures, tap targets, or navigation elements in apps like Instagram, TikTok, or YouTube, users will immediately disable monitoring. This is a zero-tolerance issue — even one instance of touch interference is a critical bug.

**Risk Factors:**
- `FLAG_NOT_FOCUSABLE` and `FLAG_NOT_TOUCH_MODAL` must be correctly configured.
- The overlay's touch handler (for drag) must consume only events within its bounds.
- Edge-snapped position may coincide with host app navigation elements.
- Different host apps have different layouts — bottom navigation bars, floating action buttons, story rings, etc.

**Mitigation Strategy:**
1. Use `FLAG_NOT_FOCUSABLE | FLAG_NOT_TOUCH_MODAL | FLAG_LAYOUT_NO_LIMITS` (SRS EIR-002).
2. Ensure drag touch handling only consumes `ACTION_DOWN` events within the overlay's bounds; all other events pass through.
3. Test against all 10 host apps in the compatibility matrix (ProjectPlan WBS 9.8, Section 11.4).
4. Pay special attention to: Instagram stories (top area), TikTok (full-screen video), YouTube (controls overlay), Reddit (comment input at bottom).
5. Default position (right edge, 50% height) should avoid common host app UI elements.

**Contingency:**
- If interference is detected on specific apps: Implement per-app position offsets or "safe zones" that avoid known UI element locations.
- If fundamental touch issues: Consider making the overlay completely non-touchable during normal display, and enabling drag mode only via a long-press.

---

#### RA-011: Ghost Overlay Left on Screen After Session End

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | TEC (Technical)                                              |
| **Source**         | New (derived from SRS FR-024.AC3, SRS Glossary "Ghost Overlay") |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 4 (Major)                                                    |
| **Risk Score**     | **12 (High)**                                                |
| **Phase(s)**       | P4, P8                                                       |
| **Traces To**      | SRS FR-024.AC3, FR-015.AC3, EH-004, EH-007                   |

**Description:** A "ghost overlay" occurs when the floating timer view remains visible on screen after the session has ended, the service has stopped, or the app has been force-stopped. This is a high-severity defect because: (a) the user cannot remove it without rebooting, (b) it creates the perception of a broken or malicious app, and (c) it may block touch events on the area it covers.

**Scenarios That Can Cause Ghost Overlays:**
- Service is killed by OS without proper `onDestroy()` callback.
- Activity crash during overlay removal.
- Exception thrown in `WindowManager.removeView()`.
- Race condition between session end and overlay fade-out animation.
- Device rotation during overlay removal.
- Split-screen mode transition during overlay removal.

**Mitigation Strategy:**
1. Always remove the overlay in a `finally` block or `onDestroy()` of the service.
2. Maintain a reference to the overlay view and check `isAttachedToWindow()` before removal.
3. Implement a cleanup routine on service startup that removes any leftover overlays.
4. Use a global overlay tracking variable — if the service starts and an overlay reference exists, remove it.
5. Wrap all `WindowManager.removeView()` calls in try-catch (SRS EH-004).
6. Test ghost overlay scenarios specifically: force-stop app, kill service via adb, crash during animation.

**Contingency:**
- If ghost overlays persist: Implement a `BroadcastReceiver` that listens for `ACTION_SCREEN_ON` and cleans up stale overlays.
- Provide a "Reset Overlay" option in the app's notification that force-removes any overlay views.

---

#### RA-012: Session State Machine Logic Errors

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | TEC (Technical)                                              |
| **Source**         | New (derived from SRS Section 10.1, FR-012–FR-017)            |
| **Probability**    | 2 (Low)                                                      |
| **Impact**         | 4 (Major)                                                    |
| **Risk Score**     | **8 (Medium)**                                               |
| **Phase(s)**       | P3, P8                                                       |
| **Traces To**      | SRS FR-012–FR-017, SRS 10.1, EC-02, EC-03, EC-05             |

**Description:** The session state machine (INACTIVE → ACTIVE → COOLDOWN → ENDED) orchestrates the core behavior of the app. Logic errors in state transitions could result in sessions never starting, never ending, counting time incorrectly, or allowing concurrent sessions — all of which would make the app unreliable.

**Critical Transition Scenarios:**
- Rapid app switching (A → B → A within seconds).
- Screen off during active session.
- Screen on after cooldown expired.
- Monitored app opened exactly at cooldown expiry boundary.
- Different monitored app opened during cooldown of first app.
- User manually stops monitoring during active session.
- Clock changes (timezone, manual adjustment) — mitigated by `SystemClock.elapsedRealtime()` (EC-05).

**Mitigation Strategy:**
1. Implement the state machine as a formally testable component with clear transition rules.
2. 100% unit test coverage for the state machine (ProjectPlan Section 11.2).
3. Test all 7 defined transition rules (SRS Section 10.1 Transition Rules table).
4. Test all relevant edge cases: EC-02, EC-03, EC-05.
5. Use `SystemClock.elapsedRealtime()` for all timing to be immune to wall-clock changes (SRS EC-05).
6. Add assertions/guards in the state machine that prevent invalid transitions.

**Contingency:**
- If logic errors are discovered late: The state machine is a pure domain component with no Android dependencies, so it can be rapidly debugged and fixed with unit tests.

---

#### RA-013: Data Loss on Service Kill (In-Progress Session)

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | TEC (Technical)                                              |
| **Source**         | New (derived from SRS FR-037.AC3, EH-003)                     |
| **Probability**    | 4 (High)                                                     |
| **Impact**         | 2 (Minor)                                                    |
| **Risk Score**     | **8 (Medium)**                                               |
| **Phase(s)**       | P3, Post-launch                                              |
| **Traces To**      | SRS FR-037.AC3, EH-003, FR-015                               |

**Description:** When the OS kills the foreground service, any in-progress session data is lost because sessions are only persisted to the database on `ENDED` state transition (SRS FR-015). This is documented as acceptable for MVP (SRS FR-037.AC3) but represents data loss.

**Impact Assessment:**
- For MVP, sessions are not surfaced to the user (no dashboard yet). The primary impact is that the timer resets if the user returns to the monitored app after a service kill.
- For v1.1 (when dashboard is introduced), lost sessions create gaps in usage data.

**Mitigation Strategy:**
1. Accept for MVP as documented (SRS FR-037.AC3).
2. Track service kill frequency during testing to assess the real-world impact.
3. For v1.1: Implement periodic session checkpointing — write partial session data to the database every 5 minutes so that at most 5 minutes of data is lost on a service kill.

**Contingency:**
- If service kills are frequent (>2 per day): Implement immediate session checkpointing (write to DB every 60 seconds while a session is active).

---

#### RA-014: Overlay Rendering Jank or Frame Drops

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | TEC (Technical)                                              |
| **Source**         | New (derived from SRS NFR-004, FR-029, FR-031)                |
| **Probability**    | 2 (Low)                                                      |
| **Impact**         | 3 (Moderate)                                                 |
| **Risk Score**     | **6 (Medium)**                                               |
| **Phase(s)**       | P4, P5, P8                                                   |
| **Traces To**      | SRS NFR-004, FR-029.AC5, FR-031.AC6                          |

**Description:** The overlay is rendered using WindowManager (View-based, not Compose) and includes multiple animations: fade-in, color transitions (2000ms lerp), opacity changes, and the throb animation (1.15x scale, 1500ms cycle). These animations must maintain ≥55 FPS without impacting the host app's performance.

**Risk Factors:**
- WindowManager overlay rendering is outside Compose's render pipeline.
- The 1-second timer text update coinciding with a color transition could cause a frame spike.
- The throb animation is infinite-repeat and must not accumulate memory or processing overhead.
- Mid-range and low-end devices may struggle with concurrent animations.

**Mitigation Strategy:**
1. Use `ObjectAnimator` with hardware acceleration for all overlay animations.
2. Profile overlay rendering FPS using Android GPU Profiler in Phase 8 (ProjectPlan WBS 8.11).
3. Test on a mid-range device (not just flagship) to catch performance issues early.
4. Keep the overlay view hierarchy flat — a single custom `View` drawing a circle, text, and color.
5. Avoid `invalidate()` calls more frequently than necessary — update only when state changes.

**Contingency:**
- If FPS drops below 55: Simplify animations. Reduce color transition smoothness. Disable the glow effect on the throb.
- If fundamentally janky on low-end devices: Add a "reduced animations" option.

---

#### RA-015: User Monitors 50+ Apps Causing Performance Issues

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | TEC (Technical)                                              |
| **Source**         | PRD R-07, SRS EC-06                                          |
| **Probability**    | 1 (Very Low)                                                 |
| **Impact**         | 2 (Minor)                                                    |
| **Risk Score**     | **2 (Low)**                                                  |
| **Phase(s)**       | P3                                                           |
| **Traces To**      | SRS FR-009, PRD A-04                                         |

**Description:** While the assumption (PRD A-04) is that users will monitor 1–20 apps, there is no upper limit enforced in the UI (SRS FR-004.AC5). A user monitoring 100+ apps does not increase polling cost (single UsageStatsManager query) but increases the match-check cost and database load.

**Mitigation Strategy:**
1. The polling cost is per-poll, not per-app (single query returns the foreground app).
2. The match check is a set lookup (O(1) with a HashSet of package names).
3. No mitigation needed beyond the existing design. Accept this risk.

**Contingency:**
- If performance issues are observed at scale: Cache the monitored app set in memory (likely already planned). Add a soft warning if the user selects more than 50 apps.

---

#### RA-016: Privacy Policy Insufficient for Store or User Trust

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | SEC (Security/Privacy)                                       |
| **Source**         | New (derived from PRD P4, PRD A-10, SRS NFR-011)              |
| **Probability**    | 2 (Low)                                                      |
| **Impact**         | 4 (Major)                                                    |
| **Risk Score**     | **8 (Medium)**                                               |
| **Phase(s)**       | P0, P10                                                      |
| **Traces To**      | ProjectPlan WBS 0.9, WBS 10.1, PRD UG-4, SRS NFR-011, NFR-012 |

**Description:** The privacy policy must satisfy two audiences: (1) Google Play's review team, who require specific disclosures for `PACKAGE_USAGE_STATS` apps, and (2) end users, who may be concerned about an app that knows which apps they use and for how long.

**Mitigation Strategy:**
1. Create the Privacy & Data Handling document early (ProjectPlan WBS 0.9).
2. Clearly state: no data collection, no network calls, no analytics, no third-party SDKs, no INTERNET permission.
3. Describe exactly what data is stored (app names, session timestamps, durations) and why.
4. Explain that all data remains on-device and is deleted when the app is uninstalled.
5. Reference the no-INTERNET-permission guarantee as a technical proof of data locality.
6. Review privacy policies of competitor apps (ActionDash, StayFree) for format and language guidance.

**Contingency:**
- If Google requests policy changes: Revise the policy promptly and resubmit.
- If users express privacy concerns: Add an in-app data viewer showing exactly what is stored and a "delete all data" button.

---

#### RA-017: Notification Permission Denial on Android 13+ (API 33+)

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | PLT (Platform)                                               |
| **Source**         | New (derived from SRS PMR-001, PMR-002)                       |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 3 (Moderate)                                                 |
| **Risk Score**     | **9 (Medium)**                                               |
| **Phase(s)**       | P7                                                           |
| **Traces To**      | SRS PMR-001 (POST_NOTIFICATIONS), PMR-002, FR-036             |

**Description:** Starting with Android 13 (API 33), the `POST_NOTIFICATIONS` permission is classified as a dangerous runtime permission. If the user denies it, the foreground service cannot display its required persistent notification, which may prevent the service from starting or cause the OS to kill it.

**Mitigation Strategy:**
1. Include notification permission request in the onboarding flow for API 33+ devices.
2. Explain that the notification is required for the monitoring service to operate.
3. Handle the denial case: show a banner explaining that monitoring cannot work without notification permission (SRS PMR-002).
4. On API 32 and below, this is not an issue — `POST_NOTIFICATIONS` is not required.

**Contingency:**
- If denial rates are high: Explore whether `startForeground()` can use a silent/minimal notification channel that doesn't require explicit user permission (behavior may vary by API level).

---

#### RA-018: Database Migration Failures in Future Updates

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | TEC (Technical)                                              |
| **Source**         | New (derived from SRS NFR-008.AC3, DR-001–DR-004)             |
| **Probability**    | 2 (Low)                                                      |
| **Impact**         | 4 (Major)                                                    |
| **Risk Score**     | **8 (Medium)**                                               |
| **Phase(s)**       | Post-launch                                                  |
| **Traces To**      | SRS NFR-008, DR-001–DR-004                                   |

**Description:** While MVP ships with schema version 1, future versions (v1.1: per-app thresholds, dashboard) will require schema changes. If database migrations are not planned correctly from the start, users upgrading from MVP could lose all their data (Room's default behavior on migration failure is to destructively recreate the database).

**Mitigation Strategy:**
1. Define a migration strategy in MVP even though only version 1 exists (SRS NFR-008.AC3).
2. Never use `fallbackToDestructiveMigration()` in production builds.
3. Design the v1 schema with forward compatibility in mind — the ThresholdConfig entity already includes `appPackageName` for future per-app overrides (SRS DR-002).
4. Write migration tests before any schema change is released.
5. Export Room schema JSON to the project for version tracking.

**Contingency:**
- If a migration is botched in a released update: Issue an immediate hotfix. If data is already lost, provide clear user communication.

---

#### RA-019: App Discovery Returns Incomplete or Incorrect App List

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | TEC (Technical)                                              |
| **Source**         | New (derived from SRS FR-001, EIR-003)                        |
| **Probability**    | 2 (Low)                                                      |
| **Impact**         | 2 (Minor)                                                    |
| **Risk Score**     | **4 (Low)**                                                  |
| **Phase(s)**       | P6                                                           |
| **Traces To**      | SRS FR-001, FR-002, EIR-003                                  |

**Description:** `PackageManager.queryIntentActivities()` with `CATEGORY_LAUNCHER` may not return all user-expected apps. Some apps (especially from alternative stores or sideloaded) may not declare a launcher category. Conversely, some system apps or helper apps may appear in the list when users don't expect them.

**Mitigation Strategy:**
1. The query approach (launcher intent + `CATEGORY_LAUNCHER`) is standard and covers >99% of user-facing apps.
2. Exclude MindfulScroll itself (SRS FR-001.AC3).
3. Provide the option to show system apps (PRD F-032, Future — but consider adding to MVP if users report missing apps).
4. Show package name as fallback if label is unavailable (SRS FR-002.AC4).

**Contingency:**
- If users report missing apps: Add a "Show All Apps" toggle (currently F-032, Future). Consider pulling into MVP.

---

#### RA-020: Cooldown Duration (45 sec) Proves Suboptimal

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | UXR (User Experience)                                        |
| **Source**         | New (derived from PRD OQ-1, SRS FR-014.AC2, Decision D-001)   |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 2 (Minor)                                                    |
| **Risk Score**     | **6 (Medium)**                                               |
| **Phase(s)**       | P9, Post-launch                                              |
| **Traces To**      | SRS FR-014.AC2, DR-004 (cooldown_duration_seconds), PRD OQ-1  |

**Description:** The cooldown duration was fixed at 45 seconds for MVP (Decision D-001). If this proves too short, sessions will fragment (brief interruptions like answering a text cause a new session). If too long, the timer lingers awkwardly after the user has clearly moved on.

**Mitigation Strategy:**
1. The cooldown value is stored as a preference constant (SRS DR-004: `cooldown_duration_seconds`), making it easy to adjust.
2. Test with real-world usage patterns: reply to a text message, check a notification, take a quick phone call.
3. 45 seconds was chosen as a middle ground between the PRD's original 30–60 second range.

**Contingency:**
- If too short: Increase to 60 seconds in a patch release.
- If too long: Decrease to 30 seconds.
- For v1.1: Make cooldown user-configurable (this was the original OQ-1 consideration).

---

#### RA-021: Foreground Service Type Declaration Rejected (API 34+)

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | PLT (Platform)                                               |
| **Source**         | New (derived from SRS FR-035.AC6, NFR-015.AC2)                |
| **Probability**    | 2 (Low)                                                      |
| **Impact**         | 4 (Major)                                                    |
| **Risk Score**     | **8 (Medium)**                                               |
| **Phase(s)**       | P3, P10                                                      |
| **Traces To**      | SRS FR-035.AC6, NFR-015.AC2, PMR-001                         |

**Description:** Android 14 (API 34) requires all foreground services to declare a `foregroundServiceType`. MindfulScroll's monitoring use case doesn't fit neatly into standard types like `location`, `camera`, or `mediaPlayback`. The `specialUse` type is available but requires a written justification during Play Store review, which may be challenged.

**Mitigation Strategy:**
1. Declare `foregroundServiceType="specialUse"` in the manifest (SRS FR-035.AC6).
2. Prepare a clear justification: "The foreground service continuously monitors which application is in the foreground to provide real-time usage awareness through a floating overlay timer."
3. Research how similar apps (ActionDash, StayFree) declare their service type.
4. Keep documentation ready for any appeal process.

**Contingency:**
- If `specialUse` is rejected: Explore whether `dataSync` or another type could apply.
- If no type is accepted: This would be a critical blocker requiring architectural changes or an appeal to Google.

---

#### RA-022: Memory Leak in Long-Running Service or Overlay

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | TEC (Technical)                                              |
| **Source**         | New (derived from SRS NFR-003.AC3)                            |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 3 (Moderate)                                                 |
| **Risk Score**     | **9 (Medium)**                                               |
| **Phase(s)**       | P3, P4, P8                                                   |
| **Traces To**      | SRS NFR-003, ProjectPlan WBS 8.10, WBS 9.9                   |

**Description:** The foreground service runs continuously for hours or days. The overlay is created and destroyed multiple times per day. Any memory leak — even a small one — will compound over time, eventually causing the service to be killed by the OS due to excessive memory consumption.

**Common Leak Sources:**
- Animation listeners not removed on overlay destroy.
- Context/View references held by closures or callbacks after the overlay is removed.
- Coroutine scopes not properly canceled on service stop.
- BroadcastReceivers registered but never unregistered.
- Room database connections not properly managed.

**Mitigation Strategy:**
1. Profile memory during 8-hour endurance test (ProjectPlan WBS 9.9).
2. Use LeakCanary during development (debug builds only).
3. Ensure all coroutine scopes are tied to the service lifecycle and canceled in `onDestroy()`.
4. Ensure all registered receivers are unregistered in `onDestroy()`.
5. Verify overlay view is fully detached from window and all animation listeners are cleared on removal.
6. Monitor heap size over time — it should stabilize, not grow.

**Contingency:**
- If a leak is detected: Use Android Profiler's heap dump and allocation tracking to identify the source. Leaks in the overlay or animation system are the most likely culprits.

---

#### RA-023: Threshold Color Values Fail Accessibility Standards

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | UXR (User Experience)                                        |
| **Source**         | New (derived from SRS NFR-010, FR-028, PRD C-10)              |
| **Probability**    | 3 (Medium)                                                   |
| **Impact**         | 2 (Minor)                                                    |
| **Risk Score**     | **6 (Medium)**                                               |
| **Phase(s)**       | P5, P8                                                       |
| **Traces To**      | SRS NFR-010, FR-028, PRD C-10                                |

**Description:** The 5 visual states use specific color values (SRS FR-028): gray, amber, orange, red, pulsing red. Users with common forms of color blindness (deuteranopia, protanopia) may not distinguish between amber/orange/red states, reducing the escalation effectiveness for ~8% of male users.

**Mitigation Strategy:**
1. The opacity progression (65% → 78% → 88% → 95% → 100%) provides a secondary visual cue independent of color.
2. The throb animation at the final state provides a motion-based cue that is color-independent.
3. Verify text contrast ratio ≥ 3:1 in all visual states (SRS NFR-010.AC4).
4. Test with Android's built-in color blindness simulation modes.
5. Accessibility Mode (F-031, Future) will address this comprehensively with pattern/shape changes.

**Contingency:**
- If accessibility concerns are raised: Pull the accessibility mode (F-031) from Future into v1.1 priority. Add haptic feedback as a supplementary cue (related to PRD OQ-4).

---

#### RA-024: App Competes with Digital Wellbeing for Usage Access Data

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | PLT (Platform)                                               |
| **Source**         | New                                                          |
| **Probability**    | 1 (Very Low)                                                 |
| **Impact**         | 3 (Moderate)                                                 |
| **Risk Score**     | **3 (Low)**                                                  |
| **Phase(s)**       | Post-launch                                                  |
| **Traces To**      | SRS EIR-001, FR-009                                          |

**Description:** Android's built-in Digital Wellbeing uses the same `UsageStatsManager` API. There is a theoretical risk that the OS could restrict third-party access to usage stats in favor of its own first-party tool, or that having both active could cause reporting inconsistencies.

**Mitigation Strategy:**
1. This risk is theoretical — currently, UsageStats access is available to any app with the permission.
2. No active mitigation needed. Monitor Android developer announcements.

**Contingency:**
- If access is restricted: This would affect the entire category of usage-tracking apps. Advocacy and industry-wide response would be needed.

---

#### RA-025: Session Spanning Midnight Causes Data Anomalies

| Field              | Value                                                        |
| ------------------ | ------------------------------------------------------------ |
| **Category**       | TEC (Technical)                                              |
| **Source**         | SRS EC-10                                                    |
| **Probability**    | 2 (Low)                                                      |
| **Impact**         | 1 (Negligible)                                               |
| **Risk Score**     | **2 (Low)**                                                  |
| **Phase(s)**       | P3                                                           |
| **Traces To**      | SRS EC-10, DR-003                                            |

**Description:** A session that starts at 11:45 PM and ends at 12:15 AM crosses a date boundary. This could theoretically cause issues in future daily usage calculations (v1.1 dashboard).

**Mitigation Strategy:**
1. Sessions are stored with epoch millisecond timestamps (SRS DR-003) — not date strings.
2. Duration is calculated from elapsed time, not from timestamp difference.
3. `SystemClock.elapsedRealtime()` is used for duration (immune to clock changes, SRS EC-05).
4. No mitigation needed for MVP. For v1.1, the dashboard query will handle date-boundary sessions by apportioning time to the day the session ended (or started, depending on the chosen convention).

---

## 4. Risk Category Analysis

### 4.1 Summary by Category

| Category            | Code | Count | Avg Score | Highest Risk                               | Highest Score |
| ------------------- | ---- | ----- | --------- | ------------------------------------------ | ------------- |
| Technical           | TEC  | 9     | 7.6       | RA-010 (Touch Interference)                | 12            |
| Platform            | PLT  | 5     | 8.6       | RA-002 (OEM Battery Kill)                  | 16            |
| User Experience     | UXR  | 4     | 9.0       | RA-003 (Onboarding Abandonment)            | 12            |
| Business            | BUS  | 1     | 15.0      | RA-001 (Play Store Rejection)              | 15            |
| Operational         | OPS  | 2     | 7.0       | RA-008 (Phase 3 Delay)                     | 9             |
| Security/Privacy    | SEC  | 1     | 8.0       | RA-016 (Privacy Policy)                    | 8             |

### 4.2 Key Insights

1. **Platform risks dominate the critical tier.** RA-002 (OEM battery kills) is the single highest-scored risk (16). This is because Android OEM behavior is completely outside our control and directly threatens the core product function.

2. **Business risk is concentrated in a single point of failure.** RA-001 (Play Store rejection) is the only way the product fails entirely without a technical defect. All other risks degrade the product; this one prevents it from reaching users.

3. **User experience risks threaten adoption, not functionality.** RA-003 and RA-004 don't cause the app to malfunction, but they prevent the product from achieving its goals (PRD UG-1 through UG-4).

4. **Technical risks are numerous but individually manageable.** No single technical risk is critical, but their aggregate volume means disciplined engineering and testing are essential.

---

## 5. Assumption Validation Risks

Each assumption from PRD Section 12 (A-01 through A-10) carries an implicit risk if the assumption proves false.

| Assumption | Description | Risk If Invalid | Probability Invalid | Impact | Validation Method | Validate By |
| ---------- | ----------- | --------------- | ------------------- | ------ | ----------------- | ----------- |
| A-01 | Users willing to grant Usage Access and Overlay permissions | App is non-functional | Low (with good UX) | Critical | Onboarding grant rate in internal testing | Phase 7 |
| A-02 | 2–3 sec polling provides sufficient detection accuracy | Delayed detection annoys users | Very Low | Moderate | Manual testing of detection latency | Phase 3 |
| A-03 | Foreground service runs reliably with battery exemption | Monitoring gaps, lost sessions | See RA-002 | See RA-002 | 8-hour endurance test on 3 OEMs | Phase 9 |
| A-04 | Users monitor 1–20 apps (not 100+) | Performance degradation (unlikely) | Very Low | Minor | See RA-015 | Phase 3 |
| A-05 | Primary use case is social/entertainment apps | Overlay may obstruct productivity app UIs differently | Low | Minor | Host app compatibility testing | Phase 9 |
| A-06 | Users have Android 8.0+ (API 26+) | Excludes users on older devices | Very Low | Negligible | Market data confirms >97% coverage | N/A (validated) |
| A-07 | Users configure thresholds once, rarely change | Settings complexity is acceptable | Low | Minor | User feedback post-launch | Post-launch |
| A-08 | No remote server or API needed | If analytics/crash reporting is needed, architecture must change | Low | Moderate | Decision on OQ-5 (crash reporting SDK) | Phase 0 |
| A-09 | Session data growth ~100–500 KB/month | Storage pressure on low-end devices | Very Low | Negligible | 90-day retention cleanup handles this | Phase 2 |
| A-10 | Google Play approves PACKAGE_USAGE_STATS usage | See RA-001 | See RA-001 | See RA-001 | Play Store submission | Phase 10 |

### 5.1 Highest-Risk Assumptions

1. **A-03 (Service Reliability):** Directly relates to RA-002. Must be validated on real OEM hardware, not just emulators. The assumption is optimistic — many developers have been surprised by aggressive OEM battery management.

2. **A-10 (Play Store Approval):** Directly relates to RA-001. Cannot be validated until Phase 10 submission. All mitigation must be proactive.

3. **A-01 (Permission Willingness):** Directly relates to RA-003. Three manual permission grants is unusual. The onboarding UX must be exceptional to make this assumption hold.

---

## 6. Constraint-Derived Risks

Each constraint from PRD Section 13 (C-01 through C-13) creates a boundary condition that, if stressed, generates risk.

| Constraint | Description | Derived Risk | Traces To |
| ---------- | ----------- | ------------ | --------- |
| C-01 | `PACKAGE_USAGE_STATS` requires manual grant | Users may fail to complete the grant process | RA-003 |
| C-02 | `SYSTEM_ALERT_WINDOW` requires manual grant | Additional friction in onboarding | RA-003 |
| C-03 | OEMs kill background/foreground services aggressively | Service unreliability | RA-002 |
| C-04 | UsageStatsManager is polling-only (no push events) | Battery cost and detection latency | RA-005, RA-007 |
| C-05 | Overlay must not intercept host app touches | Touch handling must be precisely implemented | RA-010 |
| C-06 | Play Store requires policy declaration for Usage Access | Declaration may be insufficient | RA-001 |
| C-07 | Android 10+ restricts background activity starts | Must display overlay from foreground service only | Architecture constraint (mitigated by design) |
| C-08 | Timer must not exceed ~56dp | Limited space for visual information | RA-004 (overlay perception) |
| C-09 | Overlay must pass through touches outside its bounds | Implementation complexity for drag handling | RA-010 |
| C-10 | Color escalation must be accessible | Accessibility challenges with color-only states | RA-023 |
| C-11 | All data must be local (Room, no network) | Cannot add analytics, crash reporting, or cloud backup | RA-016 (privacy), OQ-5 |
| C-12 | Must function fully offline | No dependency issues | None (positive constraint) |
| C-13 | Foreground service notification cannot be hidden | Users may find notification annoying | Minor UX concern; accepted |

---

## 7. Phase-Specific Risk Profile

### 7.1 Risk Exposure by Phase

```
Phase   Risks Active                                    Max Risk Score  Risk Density
──────  ──────────────────────────────────────────────  ──────────────  ───────────
P0      RA-001, RA-016                                  15              Low
P1      RA-009                                          5               Low
P2      RA-018, RA-025                                  8               Low
P3      RA-002, RA-005, RA-007, RA-008, RA-012,        16              ★ HIGHEST
        RA-013, RA-015, RA-021, RA-022
P4      RA-004, RA-010, RA-011, RA-014, RA-022         12              High
P5      RA-014, RA-023                                  6               Medium
P6      RA-019                                          4               Low
P7      RA-003, RA-017                                  12              Medium
P8      RA-005, RA-010, RA-011, RA-012, RA-014,        12              High
        RA-022, RA-023
P9      RA-002, RA-003, RA-004, RA-005, RA-007,        16              High
        RA-020
P10     RA-001, RA-009, RA-016, RA-021                  15              High
```

### 7.2 Critical Observation

**Phase 3 has the highest risk density.** It sits on the critical path, involves the most technically uncertain components (UsageStatsManager, foreground service, session state machine), and hosts the project's single Critical-priority risk (RA-002). Phase 3 should be approached with:

- Extra time allocation.
- Early OEM device testing (not just emulator).
- Rigorous unit testing of the state machine before integration.
- A decision gate after Phase 3 to assess OEM viability before investing in overlay and escalation work.

---

## 8. Risk Interdependencies

### 8.1 Risk Dependency Map

Some risks are interconnected — one risk materializing can trigger or amplify another.

```
RA-002 (OEM Battery Kill)
    │
    ├──► RA-013 (Session Data Loss) — service kills cause lost sessions
    ├──► RA-011 (Ghost Overlay) — unclean service termination may leave overlay
    └──► RA-008 (Phase 3 Delay) — OEM issues extend Phase 3 debugging

RA-001 (Play Store Rejection)
    │
    ├──► RA-016 (Privacy Policy) — insufficient policy is a rejection reason
    └──► RA-021 (Service Type) — incorrect foregroundServiceType is a rejection reason

RA-003 (Onboarding Abandonment)
    │
    └──► RA-017 (Notification Permission) — one more permission to request on API 33+

RA-010 (Touch Interference)
    │
    └──► RA-004 (Overlay Annoying) — if overlay blocks content, it's worse than annoying

RA-007 (UsageStats Inconsistency)
    │
    ├──► RA-012 (State Machine Errors) — bad input data could trigger wrong transitions
    └──► RA-008 (Phase 3 Delay) — diagnosing OEM-specific data issues takes time
```

### 8.2 Risk Cascades

**Cascade 1 (OEM Service Kill → Ghost Overlay → User Distrust):**
If the OS kills the service without `onDestroy()` → overlay remains on screen → user cannot remove it → user thinks the app is broken/malicious → uninstall.
*Break the cascade:* Implement overlay cleanup on service startup (RA-011 mitigation).

**Cascade 2 (Play Store Rejection → No Distribution → Project Failure):**
If the privacy policy is weak (RA-016) AND the service type justification fails (RA-021) → rejection with multiple issues → difficult appeal.
*Break the cascade:* Address RA-016 and RA-021 proactively in Phase 0, not Phase 10.

**Cascade 3 (Onboarding Complexity → Low Adoption → Negative Reviews):**
Complex permissions (RA-003) + notification permission (RA-017) → users frustrated → low setup completion → negative Play Store reviews → reduced downloads.
*Break the cascade:* Invest heavily in onboarding UX. Test with non-technical users.

---

## 9. Risk Heat Map

### 9.1 Visual Heat Map

```
Impact →    Negligible(1)  Minor(2)     Moderate(3)   Major(4)      Critical(5)
                  │              │              │             │              │
Probability ▼     │              │              │             │              │
                  │              │              │             │              │
Very High (5)     │              │              │             │              │
                  │              │              │             │              │
High (4)          │        RA-013│        RA-022│     RA-002  │              │
                  │          (8) │          (12)│      (16)   │              │
Medium (3)        │  RA-020,     │  RA-005,     │  RA-003,    │  RA-001      │
                  │  RA-023      │  RA-007,     │  RA-004,    │   (15)       │
                  │   (6)        │  RA-008,     │  RA-010,    │              │
                  │              │  RA-017      │  RA-011     │              │
                  │              │   (9)        │   (12)      │              │
Low (2)           │              │  RA-019      │  RA-006,    │  RA-009      │
                  │              │    (4)       │  RA-012,    │    (5)       │
                  │              │              │  RA-016,    │              │
                  │              │              │  RA-018,    │              │
                  │              │              │  RA-021     │              │
                  │              │              │   (8)       │              │
Very Low (1)      │  RA-025      │  RA-015      │  RA-024     │              │
                  │    (2)       │    (2)       │    (3)      │              │
```

### 9.2 Distribution Summary

| Priority    | Count | Risks                                               |
| ----------- | ----- | --------------------------------------------------- |
| Critical    | 1     | RA-002                                              |
| High        | 4     | RA-001, RA-003, RA-004, RA-010, RA-011              |
| Medium      | 13    | RA-005, RA-006, RA-007, RA-008, RA-009, RA-012, RA-013, RA-014, RA-016, RA-017, RA-020, RA-021, RA-022, RA-023 |
| Low         | 4     | RA-015, RA-019, RA-024, RA-025                      |

---

## 10. Mitigation Plan Summary

### 10.1 Critical and High-Priority Mitigation Actions

These actions must be completed. They are ordered by the phase in which they should be executed.

| # | Action | Mitigates | Phase | Owner |
| - | ------ | --------- | ----- | ----- |
| 1 | Write comprehensive privacy policy | RA-001, RA-016 | P0 | Project Owner |
| 2 | Research competing apps' Play Store declarations | RA-001, RA-021 | P0 | Developer |
| 3 | Create keystore and configure Google Play App Signing | RA-009 | P1 | Developer |
| 4 | Implement `START_STICKY` and boot receiver | RA-002 | P3 | Developer |
| 5 | Test service uptime on Samsung and Xiaomi devices in Phase 3 | RA-002 | P3 | Developer |
| 6 | Implement dual-query strategy for UsageStatsManager | RA-007 | P3 | Developer |
| 7 | 100% unit test coverage for session state machine | RA-012 | P3 | Developer |
| 8 | Implement precise touch handling with `FLAG_NOT_FOCUSABLE` | RA-010 | P4 | Developer |
| 9 | Implement overlay cleanup on service start (ghost overlay prevention) | RA-011 | P4 | Developer |
| 10 | Test overlay on all 10 host apps in compatibility matrix | RA-010, RA-004 | P4/P9 | Developer |
| 11 | Design onboarding with plain language, illustrations, skip options | RA-003 | P7 | Developer |
| 12 | Handle `POST_NOTIFICATIONS` permission for API 33+ | RA-017 | P7 | Developer |
| 13 | 8-hour endurance test on 3 Must OEMs | RA-002, RA-022 | P9 | Developer |
| 14 | Memory profiling: verify no memory growth over 8 hours | RA-022 | P9 | Developer |
| 15 | Prepare Play Store declaration form with detailed justification | RA-001, RA-021 | P10 | Project Owner |

### 10.2 Medium-Priority Monitoring Actions

These should be tracked and addressed if they begin to materialize.

| Action | Mitigates | Monitor At |
| ------ | --------- | ---------- |
| Profile battery consumption | RA-005 | Phase 8, Phase 9 |
| Monitor Android developer previews for API changes | RA-006 | Ongoing post-launch |
| Validate 45-second cooldown with real-world usage | RA-020 | Phase 9 |
| Verify color contrast in all visual states | RA-023 | Phase 5 |
| Test Room schema export for migration readiness | RA-018 | Phase 2 |
| Profile overlay FPS on mid-range device | RA-014 | Phase 4, Phase 8 |
| Validate app list completeness on 3 OEMs | RA-019 | Phase 6 |

---

## 11. Contingency Budget

### 11.1 Risk Contingency Allocation

Certain risks, if they materialize, will require significant unplanned work. The following time contingencies should be reserved.

| Risk | Contingency Scenario | Estimated Additional Effort | Trigger |
| ---- | -------------------- | --------------------------- | ------- |
| RA-002 | OEM-specific onboarding steps + troubleshooting guide | +20% of Phase 3 | Service uptime <90% on any Must OEM |
| RA-001 | Appeal process + policy revision | +100% of Phase 10 | Play Store rejection |
| RA-010 | Per-app position offsets or safe zones | +30% of Phase 4 | Touch interference on 3+ host apps |
| RA-011 | Broadcast-based cleanup + notification reset button | +15% of Phase 4 | Ghost overlays in Phase 4 testing |
| RA-003 | Onboarding flow redesign + "set up later" option | +30% of Phase 7 | <80% grant rate in testing |
| RA-008 | Reduced OEM scope (Pixel + Samsung only for MVP) | -20% of Phase 9 | Phase 3 exceeds 2x estimated duration |

### 11.2 Total Contingency

A general contingency buffer of **15–20% of total project effort** should be reserved for the combined effect of risk materialization. Phase 3 and Phase 10 are the most likely consumers of contingency.

---

## 12. Monitoring & Review Framework

### 12.1 Risk Review Schedule

| Review Point | Trigger | Actions |
| ------------ | ------- | ------- |
| **Phase Gate (M0–M10)** | Each milestone completion | Review all active risks. Update probability/impact scores. Retire resolved risks. Identify new risks. |
| **Phase 3 Decision Gate** | End of Phase 3 | Special review: assess OEM viability. Decide if all Must OEMs are achievable or scope must be reduced. |
| **Pre-Submission Review** | Before Phase 10.8 | Verify all store-related risks (RA-001, RA-016, RA-021) are fully mitigated. |
| **Post-Launch Review** | 30 days after launch | Review real-world risk materializations. Update register with production data. |
| **Quarterly Review** | Every 90 days post-launch | Assess platform risks (RA-006) against Android release cycle. |

### 12.2 Risk Metrics

| Metric | Target | Measured At |
| ------ | ------ | ----------- |
| Open Critical risks | 0 at release | Phase 9 exit |
| Open High risks with no mitigation | 0 at release | Phase 8 exit |
| Service uptime (Pixel) | ≥99% | Phase 9 endurance test |
| Service uptime (Samsung) | ≥95% | Phase 9 endurance test |
| Service uptime (Xiaomi) | ≥90% | Phase 9 endurance test |
| Onboarding completion rate | ≥80% | Phase 7 testing |
| Battery drain per hour | ≤2% | Phase 8 profiling |
| Ghost overlay incidents | 0 in testing | Phase 4+ testing |
| Touch interference incidents | 0 across 10 host apps | Phase 9 compatibility test |

### 12.3 Risk Escalation Protocol

| Severity | Action | Escalate To |
| -------- | ------ | ----------- |
| A risk score increases to Critical (16+) | Immediate stop-and-assess. Cannot proceed to next phase. | Project Owner |
| A new Critical risk is discovered | Emergency review. Update mitigation plan. | Project Owner |
| A mitigation action fails | Activate contingency. Reassess risk score. | Project Owner |
| Two or more High risks materialize simultaneously | Reassess project scope and timeline. | Project Owner |

---

## 13. Risk Acceptance Criteria

### 13.1 MVP Release Risk Gate

The MVP can be released when ALL of the following are true:

1. **Zero Critical-priority risks are open** (RA-002 must be mitigated to the defined uptime targets).
2. **All High-priority risks have active mitigations in place** (not necessarily eliminated, but managed).
3. **No High-priority risk is actively materializing** at the time of release.
4. **All assumption validations for Phase 0–9 are complete** (see Section 5).
5. **All mandatory mitigation actions** (Section 10.1, items 1–15) are complete.
6. **Service uptime targets met** on all Must OEMs (Pixel ≥99%, Samsung ≥95%, Xiaomi ≥90%).
7. **Battery drain confirmed** ≤2% per hour.
8. **Zero ghost overlay incidents** in Phase 9 testing.
9. **Zero touch interference incidents** across the 10-app compatibility matrix.

### 13.2 Accepted Risks at MVP

The following risks are explicitly accepted for MVP and will be addressed in subsequent versions:

| Risk | Why Accepted | Plan for Resolution |
| ---- | ------------ | ------------------- |
| RA-013 (Session data loss on service kill) | Impact is Minor for MVP (no dashboard). Documented in SRS FR-037.AC3. | v1.1: Implement periodic session checkpointing. |
| RA-020 (Cooldown duration may be suboptimal) | Fixed at 45 seconds per Decision D-001. Easy to adjust in a patch. | Monitor user feedback. Adjust or make configurable in v1.1. |
| RA-023 (Color accessibility) | Opacity and throb animation provide secondary non-color cues. | v1.1 or v1.2: Implement Accessibility Mode (PRD F-031). |
| RA-015 (50+ monitored apps) | Architecturally handled by O(1) set lookup. No real performance impact. | No action needed unless performance data says otherwise. |
| RA-025 (Session spanning midnight) | Stored as epoch timestamps. No display implications for MVP. | v1.1: Handle in dashboard date queries. |

---

## 14. Glossary

All terms from PRD Glossary (Section 17), SRS Glossary (Section 14), and Project Plan Glossary (Section 17) apply. Additional risk assessment terms:

| Term                    | Definition                                                                                        |
| ----------------------- | ------------------------------------------------------------------------------------------------- |
| **Risk Score**          | The product of Probability × Impact, used to prioritize risks (scale 1–25).                       |
| **Mitigation**          | An action taken to reduce the probability or impact of a risk before it occurs.                    |
| **Contingency**         | A pre-planned response to be executed if a risk materializes despite mitigation.                   |
| **Risk Register**       | A comprehensive, structured list of all identified risks with their scores and mitigations.        |
| **Risk Heat Map**       | A visual representation of risks plotted on a Probability × Impact grid.                          |
| **Risk Cascade**        | A sequence where one risk materializing triggers or amplifies subsequent risks.                     |
| **Risk Gate**           | A set of criteria that must be met (regarding risk status) before a project phase can proceed.      |
| **Assumption Validation**| The process of testing whether a stated assumption holds true in practice.                         |
| **Graceful Degradation**| The app's ability to continue functioning with reduced capability when a component fails.           |
| **Early Warning Indicator** | An observable signal that a risk may be materializing, enabling proactive response.            |
| **Contingency Budget**  | Reserved time or resources allocated to handle risk materialization.                               |

---

## 15. References

| Reference                                                     | Relevance                                          |
| ------------------------------------------------------------- | -------------------------------------------------- |
| [PRD.md](PRD.md)                                              | Source: risks R-01–R-07, assumptions A-01–A-10, constraints C-01–C-13 |
| [SRS.md](SRS.md)                                              | Source: error handling EH-001–EH-008, edge cases EC-01–EC-10, NFR targets |
| [ProjectPlan.md](ProjectPlan.md)                              | Source: risk register R-01–R-09, phase details, critical path |
| [Don't Kill My App](https://dontkillmyapp.com/)              | OEM-specific battery optimization behavior (RA-002) |
| [Google Play Usage Access Policy](https://support.google.com/googleplay/android-developer/answer/9888170) | Store policy for PACKAGE_USAGE_STATS (RA-001) |
| [Android Foreground Service Types](https://developer.android.com/develop/background-work/services/foreground-services) | Service type requirements for API 34+ (RA-021) |
| [WCAG 2.1 Level AA](https://www.w3.org/WAI/WCAG21/quickref/) | Accessibility contrast standards (RA-023) |

---

*End of Document*
