---
name: test-game
description: Run tests for Flappy Calculator - unit tests, instrumented tests, and lint
allowed-tools:
  - Bash
---

# Test Game

Run tests for Flappy Calculator.

## Run All Unit Tests
```bash
./gradlew test
```

## Run Specific Test Class
```bash
./gradlew test --tests "*.MathProblemGeneratorTest"
./gradlew test --tests "*.PhysicsSystemTest"
./gradlew test --tests "*.CollisionDetectorTest"
./gradlew test --tests "*.GameEngineTest"
```

## Run Instrumented Tests (Requires Device/Emulator)
```bash
./gradlew connectedAndroidTest
```

## Run Lint
```bash
./gradlew lintDebug
```

## Generate Test Report
After running tests, find reports at:
- `app/build/reports/tests/testDebugUnitTest/index.html`

## Test with Coverage
```bash
./gradlew testDebugUnitTestCoverage
```

## Manual Testing Checklist
1. Launch app - title screen shows
2. Tap to start - game begins
3. Bird falls gently (not too fast)
4. Enter correct answer - bird flaps up
5. Enter wrong answer - input clears, no flap
6. Pass pipe - score +1
7. Hit pipe - game over screen
8. Retry - game resets
9. Exit and relaunch - high score persists
