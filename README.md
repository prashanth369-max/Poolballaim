# Pool Aim Assistant

Transparent Android overlay assistant for mobile billiards games.

## Highlights
- Kotlin Android application (minSdk 26)
- TYPE_APPLICATION_OVERLAY floating system
- Canvas-based drawing pipeline
- JNI bridge to native C++ geometry engine
- Marker manipulation + table calibration
- Reflection trajectory simulation skeleton

## Build
```bash
./gradlew assembleDebug
```

## Native Geometry
Native sources live in `app/src/main/cpp` and are built with CMake via the Android NDK.
