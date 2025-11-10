plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    // Agregamos los plugins de Google Services
    //id("com.android.application")
    //id("com.android.application")
    //id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
}

android {
    namespace = "cl.goodhealthy.cookify"
    compileSdk = 36

    defaultConfig {
        applicationId = "cl.goodhealthy.cookify"
        minSdk = 27
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    // Dependencias base de AndroidX y Compose
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose) // Se declara una sola vez

    // 2. IMPORTAMOS LA BOM DE COMPOSE.
    // Esto asegura que todas las dependencias de Compose de abajo sean compatibles.
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)// Se declara una sola vez y sin versión
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Compose base (seguro ya están)
    implementation("androidx.activity:activity-compose:1.11.0")
    implementation("androidx.compose.material3:material3:1.4.0")

    // Navigation para Compose (para ir de Home -> Detalle)
    implementation("androidx.navigation:navigation-compose:2.9.5")

    // Cargar imágenes por URL (Coil)
    implementation("io.coil-kt:coil-compose:2.7.0")

    // Iconos extendidos de Material
    implementation("androidx.compose.material:material-icons-extended:1.7.8")

    // ViewModel (lo usa AuthViewModel)
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")

    // DataStore (para el tema oscuro)
    implementation("androidx.datastore:datastore-preferences:1.1.1")


    // JSON (para leer recipes_gourmet.json)
    implementation("com.google.code.gson:gson:2.13.2")



    // --- Firebase SDKs ---
    // Importa la BoM de Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.5.1"))

    // Dependencias de Firebase SDK
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("com.google.firebase:firebase-firestore")
    implementation("com.google.firebase:firebase-analytics")

    // (Opcional si después usas Google Sign-In)
    // implementation("com.google.android.gms:play-services-auth:21.2.0")
}