# Flappy Calculator

Android game combining Flappy Bird mechanics with mental arithmetic.

## Tech Stack
- Kotlin 2.1.x, Jetpack Compose
- Min SDK 26, Target SDK 35
- MVVM + Clean Architecture
- Gradle with Kotlin DSL

## Commands
- `./gradlew build` - Build project
- `./gradlew assembleDebug` - Create debug APK
- `./gradlew installDebug` - Install on device/emulator
- `./gradlew test` - Run unit tests
- `./gradlew connectedAndroidTest` - Run instrumented tests
- `./gradlew lintDebug` - Run Android Lint

## Verification
- All tests pass: `./gradlew test`
- Build succeeds: `./gradlew build`
- Run on emulator and verify game mechanics

## Project Structure
```
app/src/main/java/com/flappycalculator/
├── domain/model/     # Bird, Pipe, MathProblem, GameConfig
├── domain/engine/    # GameEngine, PhysicsSystem, CollisionDetector
├── domain/math/      # MathProblemGenerator, DifficultyTier
├── data/            # ScoreRepository, ScorePreferences
├── presentation/    # Screens, ViewModels, Components
└── util/           # SoundManager, Extensions
```

## Critical Rules

IMPORTANT: Physics constants in `GameConfig.kt` - adjust for playtesting:
- GRAVITY = 450f (pixels/sec^2, very gentle)
- TERMINAL_VELOCITY = 350f (max fall speed)
- FLAP_IMPULSE = -520f (high upward velocity for thinking time)
- PIPE_GAP_MULTIPLIER = 6.0f (gap = bird height x this)
- PIPE_SPACING = 550f (distance between pipes)
- INITIAL_SCROLL_SPEED = 100f (slow start)

IMPORTANT: Math problem constraints:
- All answers must be positive integers
- Division problems must have clean integer results (no remainders)
- Subtraction: first operand always larger than second
- Avoid repeating same operator consecutively

IMPORTANT: Game loop uses `withFrameNanos` for 60fps synchronization

IMPORTANT: Screen layout is 60/40 split:
- Top 60%: Game view (bird, pipes, score, math problem)
- Bottom 40%: Numeric keypad input

## Math Problem Tiers
| Tier | Score | Operations |
|------|-------|------------|
| 1 | 0-19 | Easy Addition (single digits: 1-9) |
| 2 | 20-39 | +/- (2 digits) |
| 3 | 40-59 | + Multiplication (1d x 1d) |
| 4 | 60+ | + Division, harder problems |

## Testing Checklist
- [ ] Bird floats gently (not too fast fall)
- [ ] Correct answer triggers flap
- [ ] Wrong answer clears input, no penalty
- [ ] Score increments on passing pipes
- [ ] High score persists across sessions
- [ ] 60fps on target devices
