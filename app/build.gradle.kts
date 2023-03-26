val gitSha: String = System.getenv("GITHUB_SHA")?.substring(0, 7) ?: "IDE"

val buildTime: String = "" // LocalDate.now().format(ofPattern("yyyy-MM-dd"))

plugins {
    id("com.android.application")
    kotlin("android")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
    id("com.google.firebase.appdistribution")
}

//val releaseNote = "Commit: ${commitMessage()}\n\n" +
//        "Branch: ${System.getenv('GITHUB_REF')}\n\n" +
//        "Author: ${commitAuthor()}\n\n" +
//        "Hash: ${commitShortHash()}"

val releaseTestersGroup = listOf("qa", "dev")

android {
    namespace = "com.mohsenoid.tehran.traffic"

    compileSdk = 33

    //    signingConfigs {
//        releaseKey {
//            storeFile file(keystoreProperties['storeFile'])
//            storePassword keystoreProperties['storePassword']
//            keyAlias keystoreProperties['keyAlias']
//            keyPassword keystoreProperties['keyPassword']
//        }
//    }

    defaultConfig {
        applicationId = "com.tehran.traffic"

        minSdk = 24
        targetSdk = 33

        versionCode = 33
        versionName = "5.1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")

            //            signingConfig signingConfigs.releaseKey

            //            firebaseAppDistribution {
            //                artifactType = "APK"
            //                releaseNotes = releaseNote
            //                groups = "production"
            //            }

            buildConfigField("String", "GIT_SHA", "\"${gitSha}\"")
            buildConfigField("String", "BUILD_TIME", "\"${buildTime}\"")
        }

        debug {
            applicationIdSuffix = ".debug"
            versionNameSuffix = "_debug"

            isTestCoverageEnabled = true

//            signingConfig signingConfigs.releaseKey

            //            firebaseAppDistribution {
            //                artifactType = "APK"
            //                releaseNotes = releaseNote
            //                groups = "production"
            //            }

            buildConfigField("String", "GIT_SHA", "\"${gitSha}\"")
            buildConfigField("String", "BUILD_TIME", "\"${buildTime}\"")

//            unitTests {
//                includeAndroidResources = true
//            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.1")
    implementation("androidx.activity:activity-compose:1.7.0")
    implementation("androidx.compose.material:material:1.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    val composeBom = platform("androidx.compose:compose-bom:2023.01.00")
    implementation(composeBom)
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.compose.ui:ui-util")
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")
    androidTestImplementation(composeBom)
    androidTestImplementation("androidx.compose.ui:ui-test-junit4")

    implementation("androidx.navigation:navigation-compose:2.5.3")

    val koinVersion = "3.4.0"
    implementation("io.insert-koin:koin-android:$koinVersion")
    implementation("io.insert-koin:koin-androidx-compose:$koinVersion")
    testImplementation("io.insert-koin:koin-test-junit4:$koinVersion")

    implementation("com.squareup.retrofit2:retrofit:2.9.0")

    implementation(platform("com.squareup.okhttp3:okhttp-bom:4.10.0"))
    implementation("com.squareup.okhttp3:okhttp")
    implementation("com.squareup.okhttp3:logging-interceptor")

    implementation("com.jakewharton.timber:timber:5.0.1")

    implementation("org.jbundle.util.osgi.wrapped:org.jbundle.util.osgi.wrapped.org.apache.http.client:4.1.2")

    implementation("io.coil-kt:coil-compose:2.3.0")

    implementation("com.mohsenoid.app-settings:app-settings:1.0.7")
    implementation("com.mohsenoid.navigation-view:navigation-view:1.1.4")
    implementation("com.mohsenoid.android-utils:android-utils:1.0.15")
    implementation("com.google.firebase:firebase-messaging:20.2.4")
    implementation("com.google.firebase:firebase-ads:19.3.0")
}
