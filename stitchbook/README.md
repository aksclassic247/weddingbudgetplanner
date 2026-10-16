# StitchBook

A native Android (Kotlin, Jetpack Compose) and iOS (Swift, SwiftUI) app for learning and tracking knitting and crochet. Everything is free, with no ads and no subscription.

See [docs/review-analysis.md](docs/review-analysis.md) for the user reviews this app responds to and what changed because of them.

## What is in the app

- **Home**: a welcome card, your latest project, quick links, and beginner tutorials.
- **Learn**: step-by-step tutorials for knitting and crochet, and an abbreviations guide.
- **Patterns**: pattern cards filtered by skill level. Start a pattern to make a project.
- **Projects**: a checklist for each round, a "Check my count" tool, row counters with optional targets, and notes. Saved on the device.
- **Tools**: a shaping calculator that works out where to place increases or decreases evenly across a row.

## Layout

```
shared/content.json   Tutorials, stitches and patterns. Both apps read this file.
android/              Android Studio project (Gradle, Kotlin, Compose)
  app/                The app
  core/               Plain Kotlin logic (shaping, counters, stitch check), with unit tests
ios/                  Xcode project, generated from ios/project.yml
  StitchBook/         The app (SwiftUI)
docs/                 Review analysis
```

Logic lives in one place per platform: `android/core` for Android and `ios/StitchBook/Core` for iOS. The two are kept in step by hand, so if you change one, change the other.

## Android

Requirements: Android Studio (Ladybug or newer), JDK 17 (Android Studio bundles it).

1. Open Android Studio and choose **Open**, then select the `android` folder.
2. Wait for Gradle sync to finish.
3. Pick an emulator or a phone with developer mode on, then press **Run**.

Run the logic tests from a terminal:

```bash
cd android
./gradlew :core:test
```

Minimum Android version is 8.0 (API 26).

## iOS

Requirements: a Mac with Xcode 15 or newer. iOS 17 or newer on the device.

```bash
brew install xcodegen
cd ios
xcodegen generate
open StitchBook.xcodeproj
```

Then pick a simulator and press **Run**. To run on your own iPhone, set your Team under Signing & Capabilities.

`project.yml` puts `shared/content.json` into the app bundle, so the iOS app reads the same content as Android.

## Before you publish

- **Icons.** The Android app has a simple launcher icon. The iOS app has no icon yet. Add a 1024 by 1024 icon to an `AppIcon` asset in Xcode before you submit.
- **Bundle IDs.** Both platforms use `com.stitchbook.app`. Change these to an ID you own before publishing.
- **Versions.** Android is 1.0.0 (code 1). iOS is 1.0.0 (build 1).
- **Sample patterns.** The patterns in `shared/content.json` are samples. Check them against your own yarn and tools before you publish them as real instructions.

## Verification status

- `android/core` (shaping, counters, stitch check, progress): 13 unit tests pass.
- The Android app and iOS app were written but could not be compiled in the environment used to build them. Neither the Android SDK nor Xcode was available there. Build them in Android Studio and Xcode before you rely on them.
