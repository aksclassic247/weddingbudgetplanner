# StitchBook

A mobile app for learning and tracking knitting and crochet projects. It runs on Android and iOS from one codebase, built with Expo and React Native.

## Features

- **Home**: quick start actions, a "continue your project" card, and beginner tutorials.
- **Learn**: step-by-step tutorials for knitting and crochet, plus a stitch abbreviation guide.
- **Patterns**: inspiration cards for every skill level, filterable by level. Start any pattern as a project.
- **Projects**: track each round or step with a checklist and progress bar. Projects are saved on the device.

## Run it

Requirements: Node 20 or newer, and either Expo Go on your phone or an Android/iOS emulator.

```bash
cd stitchbook
npm install
npx expo start
```

Scan the QR code with Expo Go (Android or iOS), or press `a` for Android or `i` for iOS.

## Project layout

```
src/app/            Screens and navigation (Expo Router, file-based routes)
src/components/     Shared UI pieces (buttons, chips, progress bar, badges)
src/context/        Project state, saved with AsyncStorage
src/data/content.ts Tutorials, stitch abbreviations, and patterns
src/theme/          Colors and spacing
```

## Adding content

Tutorials, abbreviations, and patterns all live in `src/data/content.ts`. Add an entry to `TUTORIALS`, `STITCHES`, or `PATTERNS` and it appears in the app.

The sample patterns are for demonstration. Check every pattern against your own yarn and tools before you start a real project.

## Building for the stores

Use EAS Build for production builds:

```bash
npx eas-cli@latest build --platform android
npx eas-cli@latest build --platform ios
```

You need an Expo account. iOS builds can run in the cloud, so you do not need a Mac for them.

## Notes

- `.npmrc` sets `legacy-peer-deps=true`. Some Expo web-tooling packages declare peer ranges that npm rejects by default, and this setting lets `npm install` finish.
