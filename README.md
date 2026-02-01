# Flappy Calculator

A math-powered twist on Flappy Bird for Android.

## Concept

Flappy Calculator combines the side-scrolling obstacle-dodging gameplay of Flappy Bird with real-time mental arithmetic. Instead of tapping to flap, solve a math problem and submit the correct answer at the right moment.

**"Flappy Bird, but your brain is the button."**

## How to Play

1. A math problem appears on screen (e.g., "24 + 13 = ?")
2. Type your answer on the numeric keypad
3. Submit the correct answer to make the bird flap
4. Time your answers to navigate through pipe gaps
5. Wrong answers have no penalty - just try again!

## Features

- Gentle, forgiving physics (softer than original Flappy Bird)
- Progressive difficulty through math tiers:
  - Score 0-9: Addition
  - Score 10-24: Addition & Subtraction
  - Score 25-49: + Multiplication
  - Score 50+: + Division
- Local high score persistence
- Simple, colorful pixel art style

## Tech Stack

- Kotlin
- Jetpack Compose
- MVVM + Clean Architecture
- Min SDK 26 (Android 8.0)

## Build & Run

### Prerequisites
- Android Studio (latest)
- Android SDK 35
- Java 17

### Build
```bash
./gradlew build
```

### Install on Device/Emulator
```bash
./gradlew installDebug
```

### Run Tests
```bash
./gradlew test
```

## Project Structure

```
app/src/main/java/com/flappycalculator/
├── domain/          # Game logic, models, engine
├── data/            # Score persistence
├── presentation/    # UI, screens, components
└── util/            # Sound manager, utilities
```

## Development

See [CLAUDE.md](CLAUDE.md) for development guidelines and [docs/STATUS.md](docs/STATUS.md) for current progress.

## License

Free to play. No ads. No monetization.
