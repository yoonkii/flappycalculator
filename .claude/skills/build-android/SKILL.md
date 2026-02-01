---
name: build-android
description: Build and deploy Flappy Calculator to Android device or emulator
allowed-tools:
  - Bash
---

# Build Android

Build and deploy the Flappy Calculator app.

## Build Debug APK
```bash
./gradlew assembleDebug
```

## Install on Connected Device/Emulator
```bash
./gradlew installDebug
```

## Build and Install (Combined)
```bash
./gradlew installDebug
```

## Build Release APK
```bash
./gradlew assembleRelease
```

## Clean Build
```bash
./gradlew clean build
```

## Common Issues

### SDK Not Found
Check `local.properties` has correct `sdk.dir` path.

### Device Not Connected
```bash
adb devices
```
Should show your device/emulator.

### Build Cache Issues
```bash
./gradlew clean
rm -rf ~/.gradle/caches/
./gradlew build
```
