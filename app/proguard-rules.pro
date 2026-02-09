# MindfulScroll ProGuard / R8 Rules
# TDD Section 17.3

# Room
-keep class * extends androidx.room.RoomDatabase
-keep @androidx.room.Entity class *
-keep @androidx.room.Dao class *

# Hilt
-keep class dagger.hilt.** { *; }
-keep @dagger.hilt.android.lifecycle.HiltViewModel class *

# Domain models (used in Room entity mapping)
-keep class com.rudy.mindfulscroll.domain.model.** { *; }

# Data entities
-keep class com.rudy.mindfulscroll.data.local.entity.** { *; }

# Strip debug logs in release
-assumenosideeffects class android.util.Log {
    public static int d(...);
    public static int v(...);
    public static int i(...);
}
