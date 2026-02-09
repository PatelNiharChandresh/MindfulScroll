# Product Requirements Document (PRD)

## MindfulScroll — Digital Awareness Companion

---

### Document Control

| Field              | Value                                      |
| ------------------ | ------------------------------------------ |
| **Document Title** | MindfulScroll Product Requirements Document |
| **Version**        | 1.0                                        |
| **Status**         | Draft                                      |
| **Created**        | 2026-02-05                                 |
| **Last Updated**   | 2026-02-05                                 |
| **Author**         | Rudy                                       |
| **Reviewers**      | —                                          |
| **Approved By**    | —                                          |

### Revision History

| Version | Date       | Author | Changes         |
| ------- | ---------- | ------ | --------------- |
| 1.0     | 2026-02-05 | Rudy   | Initial draft   |

---

## Table of Contents

1. [Executive Summary](#1-executive-summary)
2. [Product Vision](#2-product-vision)
3. [Problem Statement](#3-problem-statement)
4. [Target Audience & User Personas](#4-target-audience--user-personas)
5. [Goals & Success Metrics](#5-goals--success-metrics)
6. [Competitive Analysis](#6-competitive-analysis)
7. [Product Principles](#7-product-principles)
8. [Feature Inventory](#8-feature-inventory)
9. [User Stories](#9-user-stories)
10. [User Flows](#10-user-flows)
11. [Scope & Boundaries](#11-scope--boundaries)
12. [Assumptions](#12-assumptions)
13. [Constraints](#13-constraints)
14. [Dependencies](#14-dependencies)
15. [Risks & Open Questions](#15-risks--open-questions)
16. [Release Strategy](#16-release-strategy)
17. [Glossary](#17-glossary)
18. [References](#18-references)

---

## 1. Executive Summary

MindfulScroll is a native Android application designed to help users develop awareness of their screen time habits on specific applications. Unlike conventional screen-time management tools that enforce hard limits or block access, MindfulScroll takes a mindfulness-first approach: it makes usage visible through a non-intrusive floating timer overlay, empowering users to make conscious decisions about their time.

The app monitors user-selected applications in the background, detects when those applications are actively in use, and after a brief grace period, displays a small, draggable, floating timer on screen. As usage duration crosses user-configured time thresholds, the timer's visual appearance escalates progressively — shifting from calm, muted colors to brighter, more attention-grabbing hues and ultimately a pulsing animation — serving as a gentle, persistent reminder of time spent.

MindfulScroll does not restrict, block, or punish. It informs.

---

## 2. Product Vision

**Vision Statement:** To be the most respectful and effective digital awareness tool on Android — one that trusts its users, respects their autonomy, and helps them build a healthier relationship with technology through awareness rather than restriction.

**Long-Term Aspiration:** MindfulScroll becomes a daily companion that users genuinely appreciate rather than resent. Over time, users develop an internalized sense of time awareness when using their devices, reducing their dependence on the tool itself. The ultimate success of MindfulScroll is a user who no longer needs it.

---

## 3. Problem Statement

### 3.1 The Problem

People routinely underestimate how much time they spend on their phones, particularly on social media, entertainment, and short-form video applications. Studies consistently show that users perceive their usage at 30–50% below actual figures. This perception gap leads to unintentional overconsumption of screen time, contributing to reduced productivity, sleep disruption, and diminished well-being.

### 3.2 Why Existing Solutions Fall Short

Current screen-time tools (Android's Digital Wellbeing, third-party blockers) primarily operate on a **restriction model**:

- They set hard daily limits and lock users out when time is up.
- They rely on willpower at the moment of the lock screen (users frequently override).
- They create an adversarial relationship — the tool is the obstacle, and the user wants to defeat it.
- They provide feedback only *after the fact* (end-of-day reports), when the behavior has already occurred.

### 3.3 The Opportunity

There is a gap in the market for a tool that provides **real-time, in-context awareness** without restriction. A tool that:

- Shows the user how long they've been scrolling *while they're scrolling*.
- Escalates visual urgency gradually, not with a sudden wall.
- Lets the user decide what to do with that information.
- Treats the user as a capable adult, not an addict to be controlled.

MindfulScroll fills this gap.

---

## 4. Target Audience & User Personas

### 4.1 Primary Audience

Adults (18–45) who are self-aware enough to recognize they spend too much time on certain apps but struggle with real-time awareness of how much time is passing during a session.

### 4.2 User Personas

#### Persona 1: The Conscious Scroller — "Aisha"

| Attribute        | Detail                                                                 |
| ---------------- | ---------------------------------------------------------------------- |
| **Age**          | 27                                                                     |
| **Occupation**   | Marketing coordinator                                                  |
| **Behavior**     | Opens Instagram "for 5 minutes" during lunch, looks up 40 minutes later |
| **Frustration**  | "I don't want to delete Instagram, I just want to know when I've been on it too long" |
| **Goal**         | A gentle nudge that keeps her aware without making her feel guilty      |
| **Tech Comfort** | Comfortable with app permissions, uses phone 4–6 hours/day             |

#### Persona 2: The Productivity Protector — "Daniel"

| Attribute        | Detail                                                                 |
| ---------------- | ---------------------------------------------------------------------- |
| **Age**          | 34                                                                     |
| **Occupation**   | Freelance software developer                                           |
| **Behavior**     | Falls into Reddit/YouTube rabbit holes between work tasks               |
| **Frustration**  | "Blockers don't work because I just override them. I need to see the time ticking" |
| **Goal**         | Real-time visibility into procrastination time so he can self-correct   |
| **Tech Comfort** | Power user, will configure thresholds per app                          |

#### Persona 3: The Intentional Parent — "Maria"

| Attribute        | Detail                                                                 |
| ---------------- | ---------------------------------------------------------------------- |
| **Age**          | 41                                                                     |
| **Occupation**   | Teacher                                                                |
| **Behavior**     | Wants to model healthy phone behavior for her children                  |
| **Frustration**  | "I tell my kids to put their phones down, but I'm glued to mine"        |
| **Goal**         | A visible accountability tool that she can see and her family can see   |
| **Tech Comfort** | Moderate; needs a smooth onboarding experience                         |

#### Persona 4: The Digital Minimalist — "Kenji"

| Attribute        | Detail                                                                 |
| ---------------- | ---------------------------------------------------------------------- |
| **Age**          | 23                                                                     |
| **Occupation**   | University student                                                     |
| **Behavior**     | Actively trying to reduce screen time; uses grayscale mode and app timers |
| **Frustration**  | "Existing tools either do too much or too little. I want a simple timer, not a life coach" |
| **Goal**         | A minimal, distraction-free timer that stays out of the way until needed |
| **Tech Comfort** | High; appreciates clean, minimal UI                                    |

### 4.3 Anti-Personas (Who This App Is NOT For)

- **Parents seeking parental control software.** MindfulScroll is a self-directed tool, not a surveillance or enforcement tool.
- **Users wanting hard app-blocking capabilities.** We do not and will not block access to any application.
- **Enterprise IT administrators.** This is not an MDM or device management tool.
- **Users seeking social/competitive screen-time features.** No leaderboards, no sharing, no public accountability.

---

## 5. Goals & Success Metrics

### 5.1 Business Goals

| ID   | Goal                                                        | Timeframe     |
| ---- | ----------------------------------------------------------- | ------------- |
| BG-1 | Launch on Google Play Store with full policy compliance      | MVP release   |
| BG-2 | Achieve 1,000 organic downloads within 90 days of launch    | Post-launch   |
| BG-3 | Maintain a 4.0+ star rating on Play Store                   | Ongoing       |
| BG-4 | Achieve 30-day user retention rate of 40% or higher         | Post-launch   |

### 5.2 User Goals

| ID   | Goal                                                        | Measurement                           |
| ---- | ----------------------------------------------------------- | ------------------------------------- |
| UG-1 | Users become aware of their usage during a session          | Timer is visible on screen and not dismissed prematurely in >70% of sessions |
| UG-2 | Users reduce average session length on monitored apps       | Dashboard data shows downward trend over 4 weeks |
| UG-3 | Users do not find the app intrusive or annoying             | Uninstall rate below 50% within first 7 days |
| UG-4 | Users trust the app with their usage data                   | No negative reviews citing privacy concerns |

### 5.3 Technical Goals

| ID   | Goal                                                        | Target                                |
| ---- | ----------------------------------------------------------- | ------------------------------------- |
| TG-1 | Battery consumption is negligible                           | Less than 2% battery drain per hour of active monitoring |
| TG-2 | Foreground app detection is near-instant                    | Detection within 3 seconds of app switch |
| TG-3 | Overlay does not interfere with host app functionality      | Zero reports of overlay blocking taps or content |
| TG-4 | Service reliability across major OEMs                       | Service survives background for 95%+ of active monitoring time on Samsung, Xiaomi, Pixel, OnePlus |

---

## 6. Competitive Analysis

### 6.1 Competitive Landscape

| App                  | Approach          | Real-Time Overlay | Non-Restrictive | Threshold Escalation | Free |
| -------------------- | ----------------- | ----------------- | --------------- | -------------------- | ---- |
| **Digital Wellbeing** (Google) | Timer + blocker | No               | No (locks out)  | No                   | Yes  |
| **Screen Time** (Apple) | Timer + blocker | No               | No (locks out)  | No                   | Yes  |
| **ActionDash**       | Statistics        | No                | Yes             | No                   | Freemium |
| **StayFree**         | Timer + blocker   | Partial (notification) | Optional   | No                   | Freemium |
| **YourHour**         | Statistics + blocker | No             | Optional        | No                   | Freemium |
| **Forest**           | Gamification      | No                | No (punitive)   | No                   | Paid |
| **MindfulScroll**    | **Real-time awareness** | **Yes (floating timer)** | **Yes (never blocks)** | **Yes (color + throb)** | **Yes** |

### 6.2 Key Differentiators

1. **Real-time floating overlay.** No competitor provides an always-visible, in-context timer overlaying the monitored app itself. Most provide only notifications or after-the-fact reports.
2. **Progressive visual escalation.** The graduated color shift and eventual throb animation is a novel UX pattern that communicates urgency without interruption.
3. **Purely non-restrictive.** MindfulScroll never blocks, locks, or restricts. This is a philosophical differentiator, not just a feature gap.
4. **Simplicity of purpose.** The app does one thing and does it well: makes time visible. No gamification, no social features, no bloat.

---

## 7. Product Principles

These principles guide every design and implementation decision. When in doubt, refer here.

### P1: Awareness Over Restriction

We never block, lock, or restrict access to any application. We make time visible. The user decides what to do with that information. If a feature ever feels punitive, it violates this principle.

### P2: Respect User Autonomy

The user is in full control at all times. They choose which apps to monitor. They choose their thresholds. They can dismiss the timer. They can pause monitoring. They can uninstall the app. We never guilt, shame, or manipulate.

### P3: Minimal Intrusion

The overlay must be small, draggable, and semi-transparent. It should be noticeable enough to serve its purpose but never so prominent that it degrades the experience of the host application. If a user describes the timer as "annoying," we have failed.

### P4: Transparency

We access sensitive data (app usage patterns). We must be transparent about exactly what we access, why, and how it's stored. No data leaves the device. No hidden analytics. No dark patterns.

### P5: Battery Respect

A monitoring app that drains the battery defeats its own purpose. Performance and battery efficiency are first-class requirements, not afterthoughts.

### P6: Graceful Behavior

The app must handle edge cases gracefully — permission revocation, service termination by the OS, device reboots, split-screen mode. It should recover silently or inform the user clearly. It should never crash, hang, or leave ghost overlays on screen.

---

## 8. Feature Inventory

Features are organized by priority tier. Each feature has a unique identifier for traceability across documents.

### 8.1 MVP (Minimum Viable Product) — Must Ship

| ID    | Feature                        | Description                                                                                              |
| ----- | ------------------------------ | -------------------------------------------------------------------------------------------------------- |
| F-001 | App Discovery                  | Scan and list all user-installed applications (excluding system apps by default) with their names and icons. |
| F-002 | App Selection                  | Provide a scrollable, searchable checklist where the user can select or deselect apps to monitor.         |
| F-003 | Selection Persistence          | Store selected apps locally so the selection survives app restarts and device reboots.                    |
| F-004 | Foreground App Detection       | Continuously detect which application is currently in the foreground using Android's UsageStatsManager.   |
| F-005 | Session Tracking               | Track continuous usage sessions for monitored apps, including handling brief interruptions (cooldown window). |
| F-006 | Floating Timer Overlay         | Display a small, round, floating timer on screen after 1 minute of continuous usage of a monitored app.   |
| F-007 | Timer Drag & Reposition        | Allow the user to drag the floating timer anywhere on screen, with edge-snapping behavior on release.     |
| F-008 | Timer Position Memory           | Remember the last position the user dragged the timer to and restore it on next appearance.               |
| F-009 | Threshold Configuration        | Allow the user to configure 3 time thresholds (in minutes) with preset options (5/10/15 and 10/20/30) plus custom input. |
| F-010 | Color Escalation               | Progressively change the timer's color at each threshold crossing, from muted/calm to bright/urgent.      |
| F-011 | Throb Animation                | 5 minutes after the final threshold is crossed, the timer begins a gentle pulsing/breathing animation.    |
| F-012 | Timer Auto-Dismiss             | The floating timer disappears (fades out) when the user navigates away from the monitored app.            |
| F-013 | Permission Onboarding          | Guided onboarding flow that explains and requests the required permissions (Usage Access, Overlay, Battery Optimization). |
| F-014 | Foreground Service             | A persistent foreground service with a notification that powers the monitoring engine.                     |
| F-015 | Service Auto-Restart           | Automatically restart the monitoring service after device reboot.                                         |

### 8.2 Version 1.1 — Should Ship Soon After MVP

| ID    | Feature                        | Description                                                                                              |
| ----- | ------------------------------ | -------------------------------------------------------------------------------------------------------- |
| F-016 | Usage Dashboard                | A screen showing daily, weekly, and monthly usage statistics for each monitored app.                      |
| F-017 | Session History                | A log of all usage sessions with start time, duration, and highest threshold reached.                     |
| F-018 | Per-App Thresholds             | Allow different threshold configurations for different monitored apps (override global defaults).          |
| F-019 | Pause Monitoring               | Allow the user to temporarily pause all monitoring for a specified duration (15 min, 30 min, 1 hour, until re-enabled). |
| F-020 | App Category Grouping          | Group discovered apps by category (Social, Entertainment, Games, Productivity, etc.) on the selection screen. |
| F-021 | Quick-Select Presets           | Offer pre-built selection groups ("All Social Media", "All Video Apps") for fast onboarding.               |
| F-022 | Timer Tap Interaction          | Single tap on the floating timer briefly expands it to show the app name and current threshold stage.      |
| F-023 | Monitoring Schedule            | Allow the user to define active monitoring hours (e.g., 8 AM – 11 PM) to avoid unnecessary overnight monitoring. |

### 8.3 Future Versions — Nice to Have

| ID    | Feature                        | Description                                                                                              |
| ----- | ------------------------------ | -------------------------------------------------------------------------------------------------------- |
| F-024 | Trend Analysis                 | Show usage trends over time (increasing/decreasing) with simple visual indicators on the dashboard.       |
| F-025 | Daily Summary Notification     | An optional end-of-day notification summarizing total monitored time and threshold breaches.               |
| F-026 | Widget                         | A home screen widget showing today's total monitored time at a glance.                                    |
| F-027 | New App Detection              | Detect when a new app is installed and suggest adding it to the monitored list if it matches common categories. |
| F-028 | Data Export                    | Allow users to export their usage data as CSV for personal analysis.                                      |
| F-029 | Theme Customization            | Let users choose the color palette for threshold escalation (for accessibility or personal preference).    |
| F-030 | Timer Dismiss Gesture          | Allow a swipe gesture on the floating timer to temporarily dismiss it for the current session.             |
| F-031 | Accessibility Mode             | Alternative escalation signals for color-blind users (pattern changes, shape changes, haptic feedback).    |
| F-032 | Show System Apps Toggle        | Optional toggle on the app selection screen to include system apps in the list.                            |

---

## 9. User Stories

User stories are grouped by feature area and written in standard format. Each story maps to one or more features from Section 8.

### 9.1 App Discovery & Selection

| ID     | Story | Feature(s) | Priority |
| ------ | ----- | ---------- | -------- |
| US-001 | As a user, I want to see a list of all apps installed on my device so that I can choose which ones to monitor. | F-001 | MVP |
| US-002 | As a user, I want to search the app list by name so that I can quickly find a specific app without scrolling through the entire list. | F-002 | MVP |
| US-003 | As a user, I want to select multiple apps from the list by tapping checkboxes so that I can monitor more than one app at a time. | F-002 | MVP |
| US-004 | As a user, I want to deselect a previously selected app so that MindfulScroll stops monitoring it. | F-002 | MVP |
| US-005 | As a user, I want my app selections to be remembered after I close and reopen MindfulScroll so that I don't have to reconfigure every time. | F-003 | MVP |
| US-006 | As a user, I want apps grouped by category (Social, Games, Entertainment, etc.) so that I can quickly select an entire group. | F-020 | v1.1 |
| US-007 | As a user, I want a "Select All Social Media" preset so that I can quickly enable monitoring for common social apps without selecting them individually. | F-021 | v1.1 |
| US-008 | As a user, I want to be notified when I install a new app that fits a monitored category so that I can decide whether to add it. | F-027 | Future |

### 9.2 Permissions & Onboarding

| ID     | Story | Feature(s) | Priority |
| ------ | ----- | ---------- | -------- |
| US-009 | As a new user, I want a clear explanation of why MindfulScroll needs Usage Access permission so that I feel confident granting it. | F-013 | MVP |
| US-010 | As a new user, I want to be taken directly to the correct Settings page when granting permissions so that I don't have to navigate there manually. | F-013 | MVP |
| US-011 | As a new user, I want to understand what "Draw Over Other Apps" means in simple language so that the permission request doesn't feel suspicious. | F-013 | MVP |
| US-012 | As a user, I want MindfulScroll to tell me if a required permission has been revoked so that I understand why monitoring has stopped. | F-013 | MVP |

### 9.3 Monitoring & Detection

| ID     | Story | Feature(s) | Priority |
| ------ | ----- | ---------- | -------- |
| US-013 | As a user, I want MindfulScroll to detect when I open a monitored app within a few seconds so that tracking starts promptly. | F-004 | MVP |
| US-014 | As a user, I want my usage session to continue if I briefly switch away (e.g., to reply to a text) and return within a short time, so that the timer isn't reset by minor interruptions. | F-005 | MVP |
| US-015 | As a user, I want monitoring to survive a device reboot so that I don't have to reopen MindfulScroll every time I restart my phone. | F-015 | MVP |
| US-016 | As a user, I want to see a persistent notification confirming that MindfulScroll is actively monitoring so that I know it's running. | F-014 | MVP |
| US-017 | As a user, I want to temporarily pause all monitoring so that I can use monitored apps without the timer when I choose to (e.g., during intentional leisure time). | F-019 | v1.1 |
| US-018 | As a user, I want to set a schedule for when monitoring is active so that it doesn't run overnight or during work hours when I'm not using social apps. | F-023 | v1.1 |

### 9.4 Floating Timer

| ID     | Story | Feature(s) | Priority |
| ------ | ----- | ---------- | -------- |
| US-019 | As a user, I want a floating timer to appear on my screen after I've been on a monitored app for 1 minute so that I become aware of the time passing. | F-006 | MVP |
| US-020 | As a user, I want the timer to show elapsed time in MM:SS format so that I can see exactly how long I've been on the app. | F-006 | MVP |
| US-021 | As a user, I want to drag the floating timer to any position on my screen so that it doesn't block content I'm trying to see. | F-007 | MVP |
| US-022 | As a user, I want the timer to snap to the nearest screen edge when I release it so that it stays neatly positioned. | F-007 | MVP |
| US-023 | As a user, I want the timer to remember where I last placed it so that it appears in my preferred position next time. | F-008 | MVP |
| US-024 | As a user, I want the timer to fade in gently rather than appearing abruptly so that it doesn't startle me while I'm reading or watching content. | F-006 | MVP |
| US-025 | As a user, I want the timer to disappear when I leave the monitored app so that it's not visible when I'm doing other things. | F-012 | MVP |
| US-026 | As a user, I want to tap the timer to see which app is being tracked and what threshold stage I'm at so that I have context at a glance. | F-022 | v1.1 |
| US-027 | As a user, I want to swipe the timer away to dismiss it for the current session so that I can consciously choose to continue without the reminder. | F-030 | Future |

### 9.5 Thresholds & Escalation

| ID     | Story | Feature(s) | Priority |
| ------ | ----- | ---------- | -------- |
| US-028 | As a user, I want to choose from preset threshold options (5/10/15 min or 10/20/30 min) so that I can quickly configure without thinking about exact numbers. | F-009 | MVP |
| US-029 | As a user, I want to set custom threshold values so that I can tailor the experience to my personal tolerance. | F-009 | MVP |
| US-030 | As a user, I want the timer's color to change when I cross a threshold so that I have a visual cue that I've been on the app longer than intended. | F-010 | MVP |
| US-031 | As a user, I want the color changes to be gradual transitions rather than sudden shifts so that the experience feels calm, not alarming. | F-010 | MVP |
| US-032 | As a user, I want the timer to start pulsing/throbbing 5 minutes after the last threshold so that I get a stronger signal that I've been on the app well beyond my intended limits. | F-011 | MVP |
| US-033 | As a user, I want to set different thresholds for different apps so that I can be more lenient with some apps and stricter with others. | F-018 | v1.1 |

### 9.6 Dashboard & Statistics

| ID     | Story | Feature(s) | Priority |
| ------ | ----- | ---------- | -------- |
| US-034 | As a user, I want to see how much time I spent on each monitored app today so that I can reflect on my usage. | F-016 | v1.1 |
| US-035 | As a user, I want to see weekly and monthly summaries so that I can identify patterns in my behavior. | F-016 | v1.1 |
| US-036 | As a user, I want to see how many times I crossed each threshold per app so that I can understand how often I overuse specific apps. | F-017 | v1.1 |
| US-037 | As a user, I want to see whether my usage is trending up or down over time so that I can gauge my progress. | F-024 | Future |

---

## 10. User Flows

### 10.1 First Launch / Onboarding Flow

```
App Launch (first time)
    │
    ├─► Welcome Screen
    │       "MindfulScroll helps you stay aware of your screen time"
    │       [Get Started]
    │
    ├─► Permission 1: Usage Access
    │       Explanation: "To detect which app you're using, we need Usage Access permission"
    │       [Grant Permission] ──► Opens System Settings (Usage Access page)
    │       ◄── User returns to app
    │       Permission granted? ──► Yes: Continue
    │                           └─► No: Explain impact, allow skip with warning
    │
    ├─► Permission 2: Display Over Other Apps
    │       Explanation: "To show the floating timer, we need permission to display over other apps"
    │       [Grant Permission] ──► Opens System Settings (Overlay page)
    │       ◄── User returns to app
    │       Permission granted? ──► Yes: Continue
    │                           └─► No: Explain impact, allow skip with warning
    │
    ├─► Permission 3: Battery Optimization Exemption
    │       Explanation: "To keep monitoring reliably, we need to be excluded from battery optimization"
    │       [Grant Permission] ──► System dialog or Settings page
    │       ◄── User returns to app
    │       Permission granted? ──► Yes: Continue
    │                           └─► No: Explain impact, allow skip with warning
    │
    ├─► App Selection Screen
    │       Display installed apps with checkboxes
    │       Search bar at top
    │       Preset buttons: [Social Media] [Video] [Games]
    │       User selects apps ──► [Continue]
    │
    ├─► Threshold Configuration
    │       Preset options: [5 / 10 / 15 min] [10 / 20 / 30 min] [Custom]
    │       User selects or customizes ──► [Start Monitoring]
    │
    └─► Home Screen / Dashboard
            "MindfulScroll is now active. You're monitoring X apps."
            Foreground service starts
            Monitoring begins
```

### 10.2 Active Monitoring Flow

```
User opens a monitored app (e.g., Instagram)
    │
    ├─► MindfulScroll detects foreground change (within ~3 seconds)
    │       Session timer starts internally (not visible yet)
    │
    ├─► 1 minute of continuous use reached
    │       Floating timer fades in (500ms fade)
    │       Color: Calm state (muted gray/green)
    │       Format: MM:SS
    │       Position: Last remembered position (or default top-right)
    │
    ├─► User continues using the app
    │       Timer increments every second
    │       │
    │       ├─► Threshold 1 crossed (e.g., 5 min)
    │       │       Color transitions to Amber/Yellow (over 2–3 seconds)
    │       │       Opacity increases
    │       │
    │       ├─► Threshold 2 crossed (e.g., 10 min)
    │       │       Color transitions to Orange (over 2–3 seconds)
    │       │       Opacity increases further
    │       │
    │       ├─► Threshold 3 crossed (e.g., 15 min)
    │       │       Color transitions to Red/Coral (over 2–3 seconds)
    │       │       Opacity near maximum
    │       │
    │       └─► Threshold 3 + 5 minutes (e.g., 20 min)
    │               Timer begins throb animation (gentle pulse)
    │               Continues indefinitely until user leaves app
    │
    ├─► User switches away from monitored app
    │       │
    │       ├─► Cooldown period begins (~30–60 seconds)
    │       │       Timer pauses (still visible, slightly dimmed)
    │       │       │
    │       │       ├─► User returns to monitored app within cooldown
    │       │       │       Session resumes, timer continues from where it was
    │       │       │
    │       │       └─► Cooldown expires, user did not return
    │       │               Timer fades out (300–500ms)
    │       │               Session ends
    │       │               Session data logged to database
    │       │
    │       └─► User opens a different monitored app
    │               Previous session enters cooldown
    │               New session begins for the new app
    │               Timer resets for new app
    │
    └─► User locks screen or turns off display
            Monitoring pauses (no polling)
            Session enters cooldown
            When screen turns back on, monitoring resumes
```

### 10.3 Timer Interaction Flow

```
Floating timer is visible on screen
    │
    ├─► User touches and drags the timer
    │       Timer follows finger position in real time
    │       On release: timer snaps to nearest horizontal screen edge
    │       New position saved to storage
    │
    ├─► User single-taps the timer (v1.1)
    │       Timer briefly expands to show:
    │           App name: "Instagram"
    │           Stage: "Threshold 2 of 3"
    │       Collapses back after 3 seconds
    │
    └─► User long-presses the timer (v1.1)
            Menu appears:
                [Pause for 15 min]
                [Pause for 30 min]
                [Open MindfulScroll]
```

---

## 11. Scope & Boundaries

### 11.1 In Scope

- Native Android application (Kotlin, Jetpack Compose)
- Monitoring of user-selected installed applications
- Foreground app detection via UsageStatsManager
- Floating system overlay timer with drag capability
- Configurable time thresholds with visual escalation
- Local-only data storage (Room database)
- Foreground service with persistent notification
- Boot-completed service restart
- Material Design 3 UI
- Support for Android 8.0 (API 26) through Android 15 (API 36)

### 11.2 Explicitly Out of Scope

| Item | Reason |
| ---- | ------ |
| App blocking or locking | Violates Product Principle P1 |
| Cloud sync or backup of usage data | Privacy-first design; all data stays on device |
| User accounts or authentication | No need; single-user, single-device tool |
| Social features (sharing, leaderboards) | Violates Product Principle P2; creates social pressure |
| iOS version | Android-only for MVP and foreseeable roadmap |
| Tablet-optimized layouts | Phone-first; tablet support is a future consideration |
| Wear OS companion | Out of scope for all planned versions |
| Monetization features (ads, subscriptions) | The app is free; monetization strategy TBD separately |
| Website usage monitoring | Android app monitoring only; browser content is out of scope |
| Notification monitoring | We monitor foreground app usage, not notification content |
| Screen content analysis | We never read, capture, or analyze what the user sees on screen |

---

## 12. Assumptions

| ID   | Assumption                                                                                         |
| ---- | -------------------------------------------------------------------------------------------------- |
| A-01 | Users are willing to grant Usage Access and Overlay permissions when given clear explanations.       |
| A-02 | UsageStatsManager with a 2–3 second polling interval provides sufficient detection accuracy.         |
| A-03 | The foreground service can run reliably on mainstream Android OEMs with appropriate battery optimization exclusion. |
| A-04 | Users will monitor between 1 and 20 apps on average (not 100+).                                    |
| A-05 | The primary use case is social media and entertainment apps, not productivity tools.                 |
| A-06 | Users have Android 8.0 or higher (API 26+), which covers ~95%+ of active Android devices.           |
| A-07 | Users will interact with the threshold configuration once and rarely change it.                      |
| A-08 | The app will not need to communicate with any remote server or API.                                  |
| A-09 | Session data growth (estimated ~100–500 KB/month per user) will not create storage pressure.         |
| A-10 | Google Play will approve the app's use of `PACKAGE_USAGE_STATS` given a clear privacy policy and declared use case. |

---

## 13. Constraints

### 13.1 Platform Constraints

| ID   | Constraint                                                                                          |
| ---- | --------------------------------------------------------------------------------------------------- |
| C-01 | `PACKAGE_USAGE_STATS` permission cannot be granted programmatically; user must navigate to System Settings manually. |
| C-02 | `SYSTEM_ALERT_WINDOW` permission requires manual user grant on Android 6.0+ via Settings.            |
| C-03 | Android OEMs (Samsung, Xiaomi, Huawei, Oppo, OnePlus) may kill background/foreground services aggressively despite exemptions. |
| C-04 | UsageStatsManager does not provide push-based events; polling is required, which has inherent latency. |
| C-05 | Overlay windows cannot intercept touch events intended for the app beneath them without explicitly consuming them (must use `FLAG_NOT_FOCUSABLE`). |
| C-06 | Google Play requires a policy declaration form for apps using Usage Access permission.                |
| C-07 | Android 10+ restricts background activity starts; overlay display from a foreground service is the compliant approach. |

### 13.2 Design Constraints

| ID   | Constraint                                                                                          |
| ---- | --------------------------------------------------------------------------------------------------- |
| C-08 | The floating timer must not exceed ~56dp diameter to avoid obstructing host app content.              |
| C-09 | The overlay must pass through all touch events outside its bounds to avoid interfering with the host app. |
| C-10 | Color escalation must be distinguishable for users with common forms of color blindness (addressed in accessibility roadmap). |

### 13.3 Technical Constraints

| ID   | Constraint                                                                                          |
| ---- | --------------------------------------------------------------------------------------------------- |
| C-11 | All data must be stored locally on-device using Room. No network calls, no external storage.          |
| C-12 | The app must function fully offline (no network dependency whatsoever).                               |
| C-13 | The foreground service notification cannot be hidden; Android requires it for all foreground services. |

---

## 14. Dependencies

### 14.1 Platform Dependencies

| Dependency                        | Required For                              | Risk If Unavailable                     |
| --------------------------------- | ----------------------------------------- | --------------------------------------- |
| Android UsageStatsManager API     | Foreground app detection (F-004)          | App cannot function; core feature fails |
| Android WindowManager (TYPE_APPLICATION_OVERLAY) | Floating timer (F-006)       | Timer cannot be displayed; core feature fails |
| Android PackageManager API        | App discovery (F-001)                     | App list cannot be populated            |
| Room Persistence Library          | Data storage (F-003, F-005, F-009)        | No persistence; could fall back to DataStore |
| Jetpack Compose                   | All UI screens                            | Would require XML layout rewrite        |

### 14.2 Permission Dependencies

| Permission                                | Features Dependent On It                  | Fallback If Denied                      |
| ----------------------------------------- | ----------------------------------------- | --------------------------------------- |
| `PACKAGE_USAGE_STATS`                     | F-004, F-005, F-006, F-010, F-011, F-012 | App is non-functional; must guide user to grant |
| `SYSTEM_ALERT_WINDOW`                     | F-006, F-007, F-010, F-011               | Monitoring works but timer is invisible; degrade to notification-based alerts |
| `FOREGROUND_SERVICE`                      | F-014                                     | Cannot run persistent monitoring        |
| `RECEIVE_BOOT_COMPLETED`                  | F-015                                     | Service won't auto-restart after reboot |
| `REQUEST_IGNORE_BATTERY_OPTIMIZATIONS`    | F-014 reliability                         | Service may be killed by OEM; reduced reliability |

### 14.3 Document Dependencies

| This Document (PRD) | Is Required By                                                      |
| -------------------- | ------------------------------------------------------------------- |
| Section 8 (Features) | Software Requirements Specification (SRS) — functional requirements |
| Section 5 (Goals)    | Test Plan — success criteria define test targets                    |
| Section 7 (Principles) | UI/UX Design Specification — design decisions reference principles |
| Section 11 (Scope)   | System Architecture Document — defines system boundaries            |
| Section 13 (Constraints) | Technical Design Document — implementation must respect constraints |

---

## 15. Risks & Open Questions

### 15.1 Risks

| ID   | Risk                                                      | Probability | Impact   | Mitigation                                                                 |
| ---- | --------------------------------------------------------- | ----------- | -------- | -------------------------------------------------------------------------- |
| R-01 | Google Play rejects the app due to `PACKAGE_USAGE_STATS` policy | Medium      | Critical | Prepare thorough policy declaration; privacy policy must clearly justify usage. |
| R-02 | OEM battery optimization kills the foreground service      | High        | High     | Guide users through OEM-specific battery settings; implement service restart mechanisms. |
| R-03 | Users abandon during onboarding due to complex permission flow | Medium      | High     | Clear, simple language; explain "why" before "how"; allow partial functionality on denial. |
| R-04 | Overlay is perceived as annoying rather than helpful       | Medium      | High     | Extensive UX testing; ensure timer is small, subtle, and easy to reposition. |
| R-05 | Battery drain complaints lead to negative reviews          | Medium      | Medium   | Adaptive polling; screen on/off awareness; performance profiling before launch. |
| R-06 | Android OS updates change UsageStats or overlay behavior   | Low         | High     | Monitor Android developer previews; maintain compatibility test suite.       |
| R-07 | Users monitor too many apps, causing performance issues    | Low         | Medium   | The monitoring cost is per-poll, not per-app (single UsageStats query); but validate at scale. |

### 15.2 Open Questions

| ID   | Question                                                                                        | Impact On         | Decision Needed By |
| ---- | ----------------------------------------------------------------------------------------------- | ----------------- | ------------------ |
| OQ-1 | Should the cooldown window duration (currently proposed 30–60 sec) be user-configurable or fixed? | F-005, SRS        | Before SRS         |
| OQ-2 | Should the timer show MM:SS from session start, or MM:SS from the moment it appeared (1 min offset)? | F-006, UX Design  | Before UX Design   |
| OQ-3 | What exact color values should be used for each threshold state? Are we defining them or deferring to a designer? | F-010, UX Design  | Before UX Design   |
| OQ-4 | Should the throb animation include haptic feedback or only visual pulsing?                        | F-011, UX Design  | Before UX Design   |
| OQ-5 | Should we include a minimal analytics/crash reporting SDK (e.g., Firebase Crashlytics) for post-launch stability, or go fully offline? | Architecture, Privacy | Before SAD |
| OQ-6 | What is the monetization strategy (if any)? Free forever? Freemium? Donation-based?              | Business, PRD     | Before launch       |
| OQ-7 | Should the foreground service notification show the current session timer or just a static "Monitoring active" message? | F-014, UX Design  | Before UX Design   |

---

## 16. Release Strategy

### 16.1 Phased Release Plan

| Phase   | Contents                                                               | Goal                                            |
| ------- | ---------------------------------------------------------------------- | ----------------------------------------------- |
| **MVP** | F-001 through F-015 (Core monitoring, overlay, thresholds, onboarding) | Functional product; validates core value proposition |
| **v1.1** | F-016 through F-023 (Dashboard, per-app thresholds, pause, scheduling) | Enriches the experience; supports retention      |
| **v1.2+** | F-024 through F-032 (Trends, widget, export, accessibility mode)     | Polish and reach; addresses broader audience needs |

### 16.2 MVP Release Criteria

The MVP is ready for release when **all** of the following are true:

1. All F-001 through F-015 features are implemented and tested.
2. The app passes Google Play's policy review, including the `PACKAGE_USAGE_STATS` declaration.
3. The foreground service runs reliably for 8+ continuous hours on Pixel, Samsung, and Xiaomi devices.
4. Battery consumption is confirmed below 2% per hour of active monitoring.
5. The overlay does not interfere with touch events on at least 10 tested host applications (Instagram, TikTok, YouTube, Reddit, X, Facebook, Snapchat, Chrome, Netflix, WhatsApp).
6. The onboarding flow achieves >80% permission grant rate in internal testing.
7. No critical or high-severity bugs remain open.
8. Privacy policy is published and accessible from within the app and on the Play Store listing.

---

## 17. Glossary

| Term                    | Definition                                                                                        |
| ----------------------- | ------------------------------------------------------------------------------------------------- |
| **Monitored App**       | An application that the user has selected for MindfulScroll to track.                             |
| **Session**             | A continuous period of usage of a single monitored app, from foreground detection to session end (after cooldown). |
| **Cooldown Window**     | A brief grace period (30–60 seconds) after the user leaves a monitored app, during which the session remains active in case the user returns. |
| **Floating Timer**      | The small, round, draggable overlay that displays elapsed session time on top of the monitored app. |
| **Threshold**           | A user-configured time boundary (in minutes) at which the timer changes visual appearance.         |
| **Escalation**          | The progressive visual change in the timer as thresholds are crossed (color shift, opacity increase). |
| **Throb**               | A gentle pulsing animation that begins 5 minutes after the final threshold is crossed.             |
| **Edge-Snapping**       | Behavior where the floating timer, when released after dragging, automatically moves to the nearest horizontal screen edge. |
| **Foreground Service**  | An Android service type that runs with a persistent notification, used to maintain background operations. |
| **UsageStatsManager**   | An Android system API that provides access to device usage history and current foreground app information. |
| **Overlay Permission**  | The `SYSTEM_ALERT_WINDOW` Android permission that allows an app to draw on top of other applications. |
| **Usage Access**        | The `PACKAGE_USAGE_STATS` Android permission that allows an app to query device usage statistics.  |
| **Adaptive Polling**    | A strategy where the monitoring frequency adjusts based on screen state (faster when on, paused when off). |

---

## 18. References

| Reference                                                     | Relevance                                          |
| ------------------------------------------------------------- | -------------------------------------------------- |
| [Android UsageStatsManager Documentation](https://developer.android.com/reference/android/app/usage/UsageStatsManager) | Core API for foreground detection |
| [Android System Alert Window](https://developer.android.com/reference/android/Manifest.permission#SYSTEM_ALERT_WINDOW) | Overlay permission reference |
| [Android Foreground Services Guide](https://developer.android.com/develop/background-work/services/foreground-services) | Service implementation guidance |
| [Google Play Usage Access Policy](https://support.google.com/googleplay/android-developer/answer/9888170) | Policy compliance for `PACKAGE_USAGE_STATS` |
| [Material Design 3 Guidelines](https://m3.material.io/) | UI design system reference |
| [Don't Kill My App (dontkillmyapp.com)](https://dontkillmyapp.com/) | OEM-specific background service restrictions |

---

*End of Document*
