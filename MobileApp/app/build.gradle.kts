plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    kotlin("plugin.serialization") version "2.0.21"
}
//https://supabase.com/docs/reference/kotlin/installing
android {
    namespace = "com.example.tesy2"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.tesy2"
        //minSdk = 24
        minSdk=26
        targetSdk = 35
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.6.0"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation(platform("io.github.jan-tennert.supabase:bom:3.1.3"))
    implementation("io.github.jan-tennert.supabase:postgrest-kt")
    implementation("io.ktor:ktor-client-android:3.1.1") //karen
    implementation("io.github.jan-tennert.supabase:auth-kt")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.7")
    implementation("io.coil-kt.coil3:coil-compose:3.1.0")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.1.0")
    implementation("io.github.jan-tennert.supabase:storage-kt")

//    implementation("io.github.jan-tennert.supabase:supabase-kt-android:1.3.1")



    // CameraX core library
    implementation ("androidx.camera:camera-core:1.4.2")
    // CameraX Camera2 implementation
    implementation ("androidx.camera:camera-camera2:1.4.2")
    // CameraX Lifecycle library
    implementation ("androidx.camera:camera-lifecycle:1.4.2")
    // CameraX View library
    implementation ("androidx.camera:camera-view:1.4.2")
    implementation ("androidx.compose.material:material-icons-extended:1.4.0")
    implementation ("io.coil-kt:coil-compose:2.1.0")

    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation ("com.squareup.okhttp3:okhttp:4.9.0")

    implementation("androidx.compose.ui:ui:1.6.0")
    implementation("androidx.compose.material3:material3:1.2.0")

    // Foundation (for LazyVerticalGrid)
    implementation("androidx.compose.foundation:foundation:1.6.0")

    implementation ("io.github.vanpra.compose-material-dialogs:datetime:0.9.0")

    //implementation("io.ktor:ktor-client-okhttp:2.3.9")
    //implementation("io.ktor:ktor-serialization-gson:2.3.9") // adapte la version à ton projet
//    implementation("io.ktor:ktor-client-core:3.1.1")
//    implementation("io.ktor:ktor-client-okhttp:3.1.1")
//    implementation("io.ktor:ktor-client-content-negotiation:3.1.1")
//    implementation("io.ktor:ktor-serialization-gson:3.1.1")
//    implementation("io.ktor:ktor-client-plugins:3.1.1")




















}