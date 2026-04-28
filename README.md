# Bookly

Bookly is a Kotlin Multiplatform mobile app for children up to 5 years old, targeting Android and iOS.
It follows the same modular shared-Compose architecture used in the Storyteller reference project: custom MVI in `core`, centralized design tokens in `design`, reusable UI in `components`, domain/data services in `services/*`, and UI flows in `features/*`.

## Product Direction

Bookly is designed around simple first-learning content:

- Books for animals, birds, food, vegetables, fruits, shapes, colors, and numbers.
- A home screen with a toolbar, active user context, category filters, and a list of books.
- A reader screen with card-style pages and short descriptions for each concept.
- A settings flow that exposes account actions and an auth prompt when the user is not signed in.
- Cache-first catalog loading so content can be reused offline and repeat downloads are minimized.

The current shared services model this cache-first strategy with an in-memory catalog cache and an explicit repository/use-case layer. That structure is intended to be replaced with real persistence and networking without changing feature contracts.

## Module Map

- `composeApp`
  Shared app entry point, Android launcher wiring, iOS framework export, and top-level Koin startup in `di/AppKoin.kt`.
- `core`
  Shared infrastructure: MVI primitives, token/session storage contracts, session use cases, and shared logging.
- `design`
  Shared design tokens and the Bookly theme provider.
- `components`
  Reusable shared UI building blocks such as the toolbar, chips, book cards, and reader cards.
- `services/catalog`
  Catalog domain/data layer with DTOs, mappers, repository contracts, cache-first repository implementation, and use cases.
- `services/profiles`
  Parent profile and authentication/session domain/data layer with repository contracts, use cases, and session persistence wiring.
- `features/auth`
  Auth overlay flow using the shared MVI pattern.
- `features/home`
  Home screen flow: toolbar, filters, and book list.
- `features/reader`
  Book detail and card carousel flow.
- `features/settings`
  Settings flow with authenticated and unauthenticated states.
- `iosApp`
  Native Xcode host app and Swift entry points.

## Architecture Conventions

- Shared state flow follows the repo MVI pattern:
  `*ViewContract.kt` -> `*IntentProcessor.kt` -> `*StateMapper.kt` -> `*EffectProducer.kt` -> `*ViewModel.kt`
- `services/*` own repositories, DTOs, mappers, cache/network boundaries, and use cases.
- `features/*` own UI contracts, intent processing, screen composition, effects, and feature DI modules.
- `composeApp` owns navigation composition and app-level dependency startup only.
- Design tokens live in `design/theme/*`; reusable presentation primitives live in `components/ui/*`.
- Keep new shared business logic in `commonMain` unless platform APIs require `androidMain` or `iosMain`.

## Build And Run

Build the Android app:

```shell
./gradlew :composeApp:assembleDebug
```

Run a targeted shared compile:

```shell
./gradlew :composeApp:compileDebugKotlinAndroid
```

Compile individual shared modules after edits:

```shell
./gradlew :features:home:compileDebugKotlinAndroid
./gradlew :services:catalog:compileDebugKotlinAndroid
```

Open the iOS host app in Xcode from `iosApp/` when running on iOS.
