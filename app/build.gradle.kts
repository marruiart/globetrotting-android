plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("androidx.navigation.safeargs.kotlin")
    id("org.jetbrains.kotlin.kapt")
    id("com.google.dagger.hilt.android")
    id("kotlin-parcelize")
    id("com.google.gms.google-services")
    id("org.jetbrains.dokka")
}

android {
    namespace = "com.marina.ruiz.globetrotting"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.marina.ruiz.globetrotting"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
    }

    tasks.dokkaHtml.configure {
        dokkaSourceSets {
            configureEach {
                outputDirectory.set(layout.buildDirectory.dir("./documentation/html"))
                suppressObviousFunctions.set(true)
                suppressInheritedMembers.set(true)
                documentedVisibilities.set(
                    listOf(org.jetbrains.dokka.DokkaConfiguration.Visibility.PRIVATE)
                            + documentedVisibilities.get()
                )
            }
        }
    }
}

dependencies {
    implementation("androidx.activity:activity-ktx:1.9.0")
    val activityVersion = "1.9.0"
    val fragmentVersion = "1.8.0"
    val navVersion = "2.7.7"
    val retrofitVersion = "2.9.0"
    val roomVersion = "2.6.1"
    val hiltVersion = "2.48.1"
    val glideVersion = "4.15.1"
    val sharedPreferences = "1.1.0-alpha06"
    val gif = "1.2.17"

    dokkaPlugin("org.jetbrains.dokka:android-documentation-plugin:1.9.20")
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.appcompat:appcompat:1.7.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.annotation:annotation:1.8.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.2")
    implementation("androidx.activity:activity-ktx:1.9.0")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    // ACTIVITIES & FRAGMENTS
    implementation("androidx.activity:activity-ktx:${activityVersion}")
    implementation("androidx.fragment:fragment-ktx:${fragmentVersion}")

    // NAVIGATION
    implementation("androidx.navigation:navigation-fragment-ktx:${navVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${navVersion}")

    // RETROFIT
    implementation("com.squareup.retrofit2:retrofit:${retrofitVersion}")
    implementation("com.squareup.retrofit2:converter-gson:${retrofitVersion}")

    // ROOM
    kapt("androidx.room:room-compiler:${roomVersion}")
    implementation("androidx.room:room-runtime:${roomVersion}")
    // para que funcione con observables
    implementation("androidx.room:room-ktx:${roomVersion}")

    // COIL
    implementation("io.coil-kt:coil:2.5.0")

    // HILT
    kapt("com.google.dagger:hilt-compiler:${hiltVersion}")
    implementation("com.google.dagger:hilt-android:${hiltVersion}")

    // GLIDE
    implementation("com.github.bumptech.glide:glide:${glideVersion}")
    annotationProcessor("com.github.bumptech.glide:compiler:${glideVersion}")

    // FIREBASE
    implementation(platform("com.google.firebase:firebase-bom:33.1.0"))
    implementation("com.google.firebase:firebase-storage")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore")

    // LOTTIES
    /*    val lottieVersion = "6.4.0"
        implementation("om.airbnb.android:lottie:$lottieVersion")*/

    // SECURED SHARED PREFERENCES
    implementation("androidx.security:security-crypto:${sharedPreferences}")
}