# Room & Compose Dependencies Guide

## Current Room Dependencies in Your Project

```kotlin
// Room - Core Dependencies (from build.gradle.kts)
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")
```

## Complete Room Dependency Suite (All Components)

```kotlin
// Core Room
implementation("androidx.room:room-runtime:2.6.1")
implementation("androidx.room:room-ktx:2.6.1")
ksp("androidx.room:room-compiler:2.6.1")

// Optional: Room Paging Support (for pagination)
implementation("androidx.room:room-paging:2.6.1")

// Optional: Room Testing
testImplementation("androidx.room:room-testing:2.6.1")

// Optional: Room RxJava Support
implementation("androidx.room:room-rxjava2:2.6.1") // or room-rxjava3

// Optional: Room Guava Support
implementation("androidx.room:room-guava:2.6.1")
```

## Compose Dependencies (Compatible with Room)

```kotlin
// Compose UI Framework
val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
implementation(composeBom)
implementation("androidx.compose.ui:ui")
implementation("androidx.compose.material3:material3")
implementation("androidx.compose.ui:ui-graphics")
implementation("androidx.compose.ui:ui-tooling-preview")

// Compose Activity Integration
implementation("androidx.activity:activity-compose:1.8.1")

// Compose Navigation
implementation("androidx.navigation:navigation-compose:2.7.7")

// Compose Lifecycle
implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")

// Compose ViewModel Integration
implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")
```

## Key Points About Room + Compose Compatibility

✅ **Room FULLY Works with Compose** - They are 100% compatible!

- Room is a **database abstraction layer** (backend data layer)
- Compose is a **UI framework** (frontend presentation layer)
- They operate at different layers and work seamlessly together

### How They Work Together:

1. **Room** stores and retrieves data from SQLite database
2. **ViewModel** holds and processes the data using Room
3. **Compose UI** observes the data via StateFlow/LiveData from ViewModel
4. User interactions update data through ViewModel, which Room persists

### Recommended Architecture Flow:

```
Database (Room) 
    ↓
DAO (Data Access)
    ↓
Repository
    ↓
ViewModel
    ↓
Compose UI (observes data)
```

## Complete Updated build.gradle.kts Example

```kotlin
plugins {
    alias(libs.plugins.android.application)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.laddu.studentmanagerapp"

    defaultConfig {
        applicationId = "com.laddu.studentmanagerapp"
        minSdk = 30
        compileSdk = 36
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.11"
    }
}

dependencies {
    // Compose BOM
    val composeBom = platform("androidx.compose:compose-bom:2024.02.00")
    implementation(composeBom)
    androidTestImplementation(composeBom)

    // Room Database
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    ksp("androidx.room:room-compiler:2.6.1")

    // Compose
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    
    // Compose Activity Integration
    implementation("androidx.activity:activity-compose:1.8.1")
    
    // Compose Navigation
    implementation("androidx.navigation:navigation-compose:2.7.7")
    
    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:2.7.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // Core
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)

    // Testing
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
}
```

## Version Reference (As of March 2026)

| Component | Latest Version | Compatible |
|-----------|---|---|
| Room | 2.6.1+ | ✅ Yes |
| Compose | 2024.02.00+ | ✅ Yes |
| Kotlin | 1.9.20+ | ✅ Yes |
| Lifecycle | 2.7.0+ | ✅ Yes |

## Best Practices

1. **Use StateFlow** (from room-ktx) for reactive data updates in Compose
2. **Use ViewModel** to hold and manage Room data
3. **Use collectAsState()** to observe StateFlow in Compose
4. **Don't** directly pass database instances to Composables - use ViewModels
5. **Create a Repository** pattern between Room DAO and ViewModel

## Example Usage Pattern

```kotlin
// ViewModel with Room data
class StudentViewModel(private val repository: StudentRepository) : ViewModel() {
    val students: StateFlow<List<Student>> = repository.getAllStudents()
}

// Compose UI
@Composable
fun StudentListScreen(viewModel: StudentViewModel = hiltViewModel()) {
    val students by viewModel.students.collectAsState()
    
    LazyColumn {
        items(students) { student ->
            StudentCard(student)
        }
    }
}
```

## Summary

✅ **All Room dependencies work perfectly with Compose**
✅ **No additional compatibility layers needed**
✅ **Room handles data persistence, Compose handles UI rendering**
✅ **Use ViewModel and StateFlow as bridge between Room and Compose**

