# SafeSandesh

"Check before you trust." / "भरोसा करने से पहले जाँच लें।"

SafeSandesh is a mobile protection application designed to help users identify fraud patterns in digital communications, including KYC threats, fake job offers, lottery notifications, UPI refund scams, courier fees, and digital arrest calls.

## Key Features

- **Instant Check**: Verify suspicious text messages, screenshots, and web links.
- **Clear Verdicts**: Exactly four unambiguous outcomes: Looks Safe, Suspicious, Scam, or Not Sure.
- **Plain Explanations**: Straightforward reasons and practical, numbered action steps.
- **Privacy Protection**: Automated masking of sensitive credential sequences (OTPs, PINs, card numbers) prior to analysis.
- **Bilingual Interface**: Full, seamless support for both English and Hindi across every screen.
- **Offline History**: Stored securely on the local device with one-tap data deletion.
- **Educational Resource**: In-depth guidance articles on major fraud techniques.
- **Emergency Actions**: Direct access to the national cyber fraud helpline (1930) and official reporting portals.

## Technical Architecture

- **Platform**: Android (Native Kotlin + Jetpack Compose)
- **Design System**: Material Design 3 tokens (Navy 900 `#0B2545`, Blue 600 `#1456C4`, flat surfaces, 8dp spacing grid)
- **Architecture**: MVVM with reactive StateFlow state management
- **Engine**: Rule-based heuristic pattern evaluation behind an extensible service interface
- **Accessibility**: Scalable typography with live preview and touch target compliance

## Building and Running

Run the standard build task:
```bash
gradle assembleDebug
```

Execute local JVM and logic unit tests:
```bash
gradle :app:testDebugUnitTest
```
