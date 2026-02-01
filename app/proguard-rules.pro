# Flappy Calculator ProGuard Rules

# Keep Kotlin metadata for reflection
-keepattributes *Annotation*
-keepattributes Signature
-keepattributes InnerClasses

# Keep Compose runtime
-keep class androidx.compose.** { *; }

# Keep game models
-keep class com.flappycalculator.domain.model.** { *; }
