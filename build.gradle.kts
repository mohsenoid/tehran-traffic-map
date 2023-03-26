// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    id("com.android.application") version "7.4.1" apply false
    id("com.android.library") version "7.4.1" apply false
    kotlin("android") version "1.7.0" apply false
    id("org.jlleitschuh.gradle.ktlint") version "10.3.0" apply false
    id("io.gitlab.arturbosch.detekt") version "1.20.0" apply false
    id("com.google.gms.google-services") version "4.3.15" apply false
    id("com.google.firebase.crashlytics") version "2.9.2" apply false
    id("com.google.firebase.appdistribution") version "3.0.1" apply false
    id("androidx.navigation.safeargs") version "2.5.3" apply false
}
