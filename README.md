# Offline-First Android XML Template

![Android](https://img.shields.io/badge/Android-XML-green)
![Kotlin](https://img.shields.io/badge/Kotlin-2.3.0-blueviolet)
![Architecture](https://img.shields.io/badge/Architecture-MVVM%20%7C%20Clean-brightgreen)
![Offline](https://img.shields.io/badge/Offline--First-Yes-success)
![License](https://img.shields.io/badge/License-MIT-yellow)

Offline-First • XML • MVVM • Clean Architecture • Single Source of Truth (SSOT)

A **production-grade Android application template**
designed with **offline-first principles**, **robust authentication**, and **clean architecture**.

---

## Author

**Jayant Sharma**  
Senior Software Engineer 

---

## What This Template Solves

This template is built to avoid the most common production issues:

- Scattered authentication logic
- SharedPreferences-based session flags
- Token refresh race conditions
- Logout navigation bugs
- Back stack inconsistencies
- UI-driven authentication decisions
- Online-only assumptions
---

## Core Architectural Principles

### Single Source of Truth (SSOT)

Room database is the **only authority** for authentication and user state.

- User row exists → authenticated
- User table empty → unauthenticated

No auth booleans  
No token checks in Activities  
No session flags driving navigation

---

### Offline First

- UI always renders from Room
- Network is used only for syncing data
- App behaves predictably without internet

---

### Unidirectional Data Flow

```
Activity / XML
   ↓
ViewModel (UI State)
   ↓
Repository
   ↓
Local (Room)  ↔  Remote (API)
```

UI never talks directly to Retrofit, DataStore, or interceptors.

---

## Project Structure

```
com.example.myandroidtemplate
│
├── MainApplication.kt          # App-level auth observer
│
├── ui
│   ├── splash                 # Auth router
│   ├── login                  # Login flow
│   ├── signup                 # Signup flow
│   ├── home                   # Home screen
│   └── common                 # UiState, UiError, FormError
│
├── domain
│   └── model                  # Domain models
│
├── data
│   ├── local
│   │   ├── entity             # Room entities
│   │   ├── db                 # Database + DAO
│   │   └── datasource         # Local data source
│   │
│   ├── remote
│   │   ├── api                # Retrofit APIs
│   │   ├── request            # Request DTOs
│   │   ├── dto                # Response DTOs
│   │   ├── datasource         # Remote data source
│   │   ├── mock               # Mock API interceptor
│   │   └── AuthInterceptor    # Auth + refresh logic
│   │
│   ├── repository             # Repository implementations
│   └── auth
│       └── TokenRefreshCoordinator
│
├── di
│   ├── NetworkModule           # Retrofit / OkHttp
│   ├── DatabaseModule          # Room
│   ├── RepositoryModule        # Bindings
│   └── Qualifiers              # Custom qualifiers
│
├── worker
│   └── SyncWorker              # Background sync
│
└── utils
    ├── SessionManager          # Token storage only
    ├── ErrorMapper             # API → UI errors
    ├── FormValidator           # Input validation
    └── Extensions              # UI helpers
```

---

## Authentication & Session Management

### Token Handling

- Access token injected via OkHttp interceptor
- 401 triggers refresh flow
- Refresh is mutex-protected
- Refresh failure clears Room → global logout

### TokenRefreshCoordinator

Ensures:
- Only one refresh call at a time
- Prevents refresh storms
- Waiting requests reuse refreshed token

---

## Global Auto Logout

### How It Works

1. User logs out OR token refresh fails
2. Room user table is cleared
3. Application observes auth state
4. App task restarts
5. SplashActivity routes to Login

This works from **any screen**, foreground or background.

---

## SplashActivity

SplashActivity is a **pure router**:

- No UI
- No business logic
- Observes Room only
- Routes to Login or Home

---

## UI State Management

All screens use a unified state model:

```
Idle
Loading
Success
ValidationError
Error
```

Guarantees:
- Consistent loaders
- Field-level validation errors
- User-friendly messages
- No silent failures

---

## Mock API Support

- MockApiInterceptor allows UI development without backend
- Easily removable when backend is ready

---

## Background Work

- WorkManager used for background sync
- Safe for offline usage
- Retry-capable

---

## Tech Stack

- Language: Kotlin
- UI: XML + ViewBinding
- Architecture: MVVM + Clean Architecture
- Dependency Injection: Hilt
- Database: Room
- Networking: Retrofit + OkHttp
- Async: Coroutines + Flow
- Background: WorkManager
- Lifecycle: ProcessLifecycleOwner
- Build: Kotlin DSL + Version Catalog

---

## What This Template Avoids

- UI-driven authentication
- SharedPreferences session flags
- Multiple Retrofit instances
- Navigation from repositories
- Token checks in Activities
- Lifecycle race conditions

---

## License

This project is licensed under the **MIT License**.

See the [LICENSE](LICENSE) file for full details.
