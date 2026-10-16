# Store checklist

Work through this before you submit to Google Play or the App Store.

## Things only you can provide

- [x] **App icon.** Artwork from ChatGPT is in place on both platforms: a 1024 by 1024 opaque icon for iOS and a 432 by 432 transparent foreground for Android (background is coral #E76F5E). Check that your ChatGPT plan's terms allow commercial use of the image.
- [ ] **Bundle IDs.** `com.stitchbook.app` is a placeholder. Register your own ID with Google Play and with Apple Developer, then change it in `android/app/build.gradle.kts` and `ios/project.yml`.
- [ ] **Support email.** Set it in `android/app/src/main/java/com/stitchbook/app/support/Support.kt` and `ios/StitchBook/Support/Support.swift`.
- [ ] **Privacy policy URL.** Publish `docs/privacy-policy.md` at a public web address (for example GitHub Pages) and paste that link into both stores.
- [ ] **Screenshots.** Take them from a phone or emulator running the app, in the sizes each store asks for.
- [ ] **Developer accounts.** Google Play Console (one-off fee) and Apple Developer Program (yearly fee).

## Things already in the repo

- [x] Store description that matches the app: `docs/store-listing.md`
- [x] Privacy policy text: `docs/privacy-policy.md`
- [x] No internet permission and no analytics on Android
- [x] No network code or analytics on iOS
- [x] Sample patterns labelled "Untested draft" until tested: `docs/pattern-testing.md`

## Before you upload

- [ ] Every pattern shown in the store is either tested or clearly marked as an untested draft.
- [ ] A release build runs on a real phone for each platform.
- [ ] Version numbers are set: Android `versionCode` and `versionName`, iOS `CURRENT_PROJECT_VERSION` and `MARKETING_VERSION`.
