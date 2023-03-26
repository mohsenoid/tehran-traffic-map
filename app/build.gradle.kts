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
    namespace = "com.tehran.traffic"

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

        minSdk = 19
        targetSdk = 33

        versionCode = 33
        versionName = "5.1.0"

        multiDexEnabled = true

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
    packagingOptions {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {
//    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.8.10")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("com.android.support:support-annotations:28.0.0")
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("com.google.android.material:material:1.8.0")

    testImplementation("junit:junit:4.13.2")

    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.jakewharton.timber:timber:4.7.1")

    implementation("org.jbundle.util.osgi.wrapped:org.jbundle.util.osgi.wrapped.org.apache.http.client:4.1.2")

    implementation("com.mohsenoid.app-settings:app-settings:1.0.7")
    implementation("com.mohsenoid.navigation-view:navigation-view:1.1.3")
    implementation("com.mohsenoid.android-utils:android-utils:1.0.14")
    implementation("com.google.firebase:firebase-messaging:20.2.4")
    implementation("com.google.firebase:firebase-ads:19.3.0")
}
