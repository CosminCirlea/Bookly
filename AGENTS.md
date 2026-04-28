# Bookly Agent Guide

## Project Summary

Bookly is a Kotlin Multiplatform mobile app targeting Android and iOS.
Most product code lives in shared Compose Multiplatform modules. The native iOS
host app lives in `iosApp/`, while `composeApp/` is the shared application entry
module exported to iOS and packaged as the Android app.

Core stack in this repo:

- Kotlin Multiplatform, Compose Multiplatform
- Koin for dependency injection
- Ktor-ready service modules and cache-first repository boundaries
- Custom MVI primitives in `core/src/commonMain/kotlin/.../core/mvi`
- Shared design-token system in `design`

## Module Map

- `composeApp`
  Shared app entry points, Android launcher code, iOS framework export, and
  app-level DI startup in `di/AppKoin.kt`.
- `core`
  Shared infrastructure: session/token storage contracts, auth/session use
  cases, shared logging, and the reusable MVI framework.
- `design`
  Shared design tokens and theme definitions. Keep visual tokens centralized here.
- `components`
  Reusable shared UI pieces such as Bookly toolbar, filter chips, book cards,
  and reader cards.
- `services/catalog`
  Catalog domain/data layer: repository interfaces, DTOs, mappers, cache-first
  repository implementation, use cases, and Koin DI.
- `services/profiles`
  Parent profile and auth/session domain/data layer: repository interfaces, use
  cases, session persistence wiring, and Koin DI.
- `features/auth`
  Authentication overlay UI flow and view logic.
- `features/home`
  Home screen UI flow including toolbar, filters, and book list.
- `features/reader`
  Book reader/detail UI flow with the card carousel.
- `features/settings`
  Settings UI flow with signed-in and signed-out variants.
- `iosApp`
  Native Xcode host app and Swift entry points. Touch this only when the task
  requires host-level iOS integration.

## Architecture Conventions

- Default to `commonMain`. Use `androidMain` or `iosMain` only for platform APIs,
  platform UI glue, or native integrations.
- Keep layer boundaries clear:
  - `services/*` own repositories, DTOs, mappers, use cases, and service DI.
  - `features/*` own UI, view contracts, intent processors, state mappers,
    effect producers, and feature DI.
  - `composeApp` owns app composition and top-level dependency startup.
- Shared state handling follows the repo's MVI pattern:
  `*ViewContract.kt` -> `*IntentProcessor.kt` -> `*StateMapper.kt` ->
  `*EffectProducer.kt` -> `*ViewModel.kt`.
- DI is Koin-based. New shared bindings should be added in the relevant
  `.../di/*DiModule.kt` file and included from the app root only when needed.
- Prefer extending service modules before pushing network/data logic into feature
  modules.
- Shared theme/token changes belong in `design/theme/*`, not duplicated inside
  features or components.
- Keep Bookly's cache-first contract in the service layer. Features should ask
  for use cases and not manage persistence or network fallback directly.

## Source Set Rules

- Shared code belongs in `src/commonMain/kotlin`.
- Shared tests belong in `src/commonTest/kotlin`.
- Android-only resources or implementations belong in `src/androidMain`.
- iOS-only implementations belong in `src/iosMain`.
- When changing platform code later in `core` or `components`, keep Android and
  iOS behavior aligned.

## Build And Verification

Use targeted Gradle tasks first. Avoid running the entire repo unless the change
really spans multiple modules.

- Build the Android app:
  `./gradlew :composeApp:assembleDebug`
- Compile the shared app entry after wiring changes:
  `./gradlew :composeApp:compileDebugKotlinAndroid`
- Compile a feature module after editing it:
  `./gradlew :features:reader:compileDebugKotlinAndroid`
- Compile a service module after editing it:
  `./gradlew :services:catalog:compileDebugKotlinAndroid`

If you change shared wiring in `composeApp`, prefer validating with
`./gradlew :composeApp:compileDebugKotlinAndroid`.

## Editing Guidelines

- Respect uncommitted work already in the tree. Do not revert unrelated changes.
- Keep changes scoped to the owning module unless the feature actually requires a
  cross-module contract update.
- If a new screen or flow needs data, add or extend the use case/repository in
  the appropriate `services/*` module and then wire it into the feature DI module.
- When adding reusable UI, prefer `components` over copy/pasting into features.
- When adding shared visual tokens, prefer `design` instead of local per-feature
  constants.
- Do not bypass the session/token abstractions in `core` when working on auth or
  parent profile flows.

## Practical Defaults For Agents

- Read the relevant feature's `di`, `ViewContract`, `ViewModel`, and screen files
  before editing behavior.
- For auth or session work, inspect `core/auth/*`, `core/network/*`, and
  `services/profiles/*` first.
- For catalog or offline-content work, inspect `services/catalog/*` before adding
  logic to `features/home` or `features/reader`.
- For UI styling, start in `design` and `components` before creating new local
  patterns.
- Prefer small, targeted compile/check runs that match the modules you changed.
