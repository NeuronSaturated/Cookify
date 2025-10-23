// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false

    // Apply the Google Services plugin
    // The line below was removed as it's a duplicate of the alias above.
    // id("com.android.application") version "8.12.3" apply false
    id("com.google.gms.google-services") version "4.4.4" apply false
}
