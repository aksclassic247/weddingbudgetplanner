# What users say about YarnPal, and what StitchBook does about it

This note looks at public reviews of YarnPal (Google Play package `com.yarnpal.ai`, also listed on the App Store as "YarnPal - Crochet & Patterns") and turns the recurring complaints into product decisions.

## How this was gathered, and its limits

- The Google Play page itself could not be opened from the build environment, so the review text below comes from search results and third-party summaries, not from reading every review.
- The store ratings disagree. Google Play and the App Store show roughly 4.6 to 4.7 stars. One aggregator page shows 2.7 stars from about 1,800 reviews. Treat the aggregator figure with caution.
- The two stores list the app under different names, so some reviews may be for different versions.
- Several summaries were written by AI tools. The quotes and themes are worth checking against the store pages before you rely on exact wording.

Sources:
- [Google Play listing](https://play.google.com/store/apps/details?id=com.yarnpal.ai&hl=en)
- [App Store reviews](https://apps.apple.com/us/app/yarnpal-crochet-patterns/id6738537088?see-all=reviews&platform=iphone)
- [Negative review summary (unstar)](https://unstar.app/app/6738537088?platform=ios&country=tr)
- [App Store review summary (AppsHunter)](https://appshunter.io/ios/app/yarnpal-crochet-and-patterns/id6738537088/reviews)
- [Google Play review summary (chrome-stats)](https://chrome-stats.com/d/com.yarnpal.ai/reviews)
- [Google Play pricing and reviews (App Pricing Lab)](https://apppricinglab.com/app/google_play/com.yarnpal.ai)
- [Google Play reviews (Grand Screen)](https://grand-screen.com/apps/yarnpal-crochet-for-beginner/reviews/)
- [AppBrain listing](https://www.appbrain.com/app/yarnpal-crochet-for-beginner/com.yarnpal.ai)

## Recurring complaints and what we do

| What people say | How often it comes up (roughly) | What StitchBook does |
| --- | --- | --- |
| Most patterns sit behind a paid Pro subscription. Free users get only a few. | The most common complaint, across both stores. | Everything in the app is free in this version: all tutorials, all patterns, the counter, and the calculator. No subscription. |
| Ads make it look free, but it is not. | Several reviews. | No ads. |
| A free trial turned into a charge, and a refund was refused. | A few reviews, mostly on Google Play. | No billing in this version, so nothing can be charged. If you add a subscription later, show the price and trial end date before the sign-up button. |
| Patterns are wrong or incomplete: missing yarn details, stitch counts, or assembly steps. | Many reviews. | Each round lists its stitch count where it matters, and each project has a "Check my count" step that tells you whether your count matches, and by how much. Materials and gauge are shown on every pattern. |
| The AI chat gets simple maths wrong and will not back down. | Several reviews. | The shaping calculator does the maths with plain arithmetic, not AI. It gives the same answer every time, with the row written out in plain words. |
| The developer says patterns are human-made, but reviewers say they are AI-generated and the photos do not match the finished item. | Several reviews, with the developer responding. | There is no AI-generated content and no stock photos. Patterns use colored blocks and text, so nothing claims to show a finished item it does not. Sample patterns are labelled as samples. |
| Mostly crochet, with little knitting. | At least one reviewer. | Knitting and crochet get equal space: tutorials, stitch abbreviations, and patterns for both. |
| Video and written instructions do not line up. | A few reviews. | Instructions are written, not video, so there is nothing to fall out of sync. |
| Row counting is one feature among many, and competitors go further. | Competitor comparison, not user reviews. | A counter per project, with an optional target and a progress bar, plus haptic feedback on each tap. Multiple named counters are supported. |

## Requests competitors already meet, and where we stand

Taken from competitor listings (see the sources above):
- Several counters and linked counters: **partly done**. A project can hold several named counters, but they are not linked yet.
- Row reminders: **not done**. This needs notifications permission and is a good next step.
- Automatic increase and decrease calculation: **done**, as the shaping calculator.
- Progress toward a target row: **done**, as the target on each counter.
- Floating counter over a PDF: **not done**. Needs a PDF viewer.
- Sounds and haptics: **haptics done on Android. iOS uses the same counter but has no haptic yet.**

## What changed in the latest round

- Yarn estimate from the label's grams and metres, with a 10 percent margin (both platforms).
- Search and skill-level filters on the Patterns screen (both platforms).
- Every sample pattern has a credit line saying it has not been test-made (both platforms).
- iOS haptic feedback on every counter tap, matching Android.
- App icon placeholder for iOS.

## Status of the requested features

- **Row reminders:** built. A daily reminder per project, with a notification permission request.
- **Your own PDF patterns:** built. Pick a PDF from your files and view it in the app.
- **Photos on projects:** built. Pick photos with the system picker; they stay on the device.
- **Syncing between devices:** partly built. Backup and restore share a file between devices. Automatic sync needs a backend and an account decision.
- **Linked counters:** built as repeat counters. One counter follows another, so a pattern repeat of 8 rows counts repeats automatically.
- **Icons:** placeholder yarn-ball icons on both platforms.
