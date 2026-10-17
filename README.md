# KMP Posts Demo

Kotlin Multiplatform + Compose Multiplatform app (Android and iOS) with shared UI and shared business logic.

- **Shared (commonMain):** Compose UI, Ktor networking, SQLDelight cache, offline-first repository, state holder
- **Platform (androidMain / iosMain):** SQLite driver, OkHttp / Darwin Ktor engines, app entry points
- **Tests (commonTest):** Ktor `MockEngine` test for JSON parsing

## Architecture

```
Compose UI (App.kt) -> PostsStore (StateFlow) -> PostsRepository -> SQLDelight (source of truth)
                                                       \-> PostsApi (Ktor) -> JSONPlaceholder
```

The UI only reads from the database. `refresh()` fetches from the network and replaces the cache, so the app works offline.

## Run

**Android:** open in the latest stable Android Studio, sync Gradle, run the `composeApp` configuration.

**iOS (macOS + Xcode required):**
1. Generate the Xcode wrapper at https://kmp.jetbrains.com (Android + iOS, share UI) and copy its `iosApp/` folder into this repo.
2. Make `iosApp/iosApp/ContentView.swift` call `MainViewControllerKt.MainViewController()`.
3. Open `iosApp/iosApp.xcodeproj` and run.

## Test
```
gradle :composeApp:testDebugUnitTest
gradle :composeApp:iosSimulatorArm64Test   # macOS only
```

## Next steps
Koin for DI, pull-to-refresh, a detail screen with navigation, Turbine tests for flows.
