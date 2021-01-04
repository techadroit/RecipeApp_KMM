plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("kotlin-parcelize")
}

android {
    compileSdkVersion(30)
    defaultConfig {
        applicationId = "com.recipeapp"
        minSdkVersion(24)
        targetSdkVersion(30)

        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildFeatures {
//        compose = true
        viewBinding = true
    }

//    composeOptions {
//        kotlinCompilerVersion = "1.4.21"
//        kotlinCompilerExtensionVersion = Versions.compose
//    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildToolsVersion = "30.0.3"
}

dependencies {
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("com.google.android.material:material:1.2.1")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")

    implementation("com.airbnb.android:epoxy:4.3.1")
    kapt("com.airbnb.android:epoxy-processor:4.3.1")

    implementation("com.github.haroldadmin:Vector:0.6.3")

    implementation("androidx.constraintlayout:constraintlayout:2.0.4")
    implementation("androidx.arch.core:core-common:2.1.0")
    implementation("androidx.arch.core:core-runtime:2.1.0")
    implementation("androidx.activity:activity-ktx:1.1.0")
    implementation("androidx.fragment:fragment-ktx:1.2.5")
    implementation("io.coil-kt:coil:1.0.0")
    implementation("com.thefinestartist:ytpa:1.2.1")


//    implementation(Compose.ui)
//    implementation(Compose.uiGraphics)
//    implementation(Compose.uiTooling)
//    implementation(Compose.foundationLayout)
//    implementation(Compose.material)
//    implementation(Compose.runtimeLiveData)
//    implementation(Compose.navigation)
//    implementation(Compose.accompanist)
//
//    implementation(Koin.android)
//    implementation(Koin.androidViewModel)

    testImplementation("junit:junit:4.13.1")
    testImplementation("androidx.test:core:1.3.0")
    testImplementation("org.robolectric:robolectric:4.4")
    androidTestImplementation("androidx.test:runner:1.3.0")

    implementation(project(":recipe"))
}

//def epoxy_version = "4.3.1"
//def appcompat_version = "1.1.0"
//def lifecycle_version = "2.1.0"
////Unit Testing
//def robolectric_version = '4.3'
//def junit_version = '4.12'
//def mockito_version = '2.2.0'
//def kluent_version = '1.14'
////Acceptance Testing
//def runner_version = '1.2.0'
//def espresso_version = '3.2.0'
//def okhttp_version = "4.2.1"
//def retrofit_version = "2.6.2"
//def preference_version = "1.1.0"
//def fragment_version = "1.2.0"
//
//implementation 'com.thefinestartist:ytpa:1.2.1'
//implementation fileTree(dir: 'libs', include: ['*.jar'])
//implementation "org.jetbrains.kotlin:kotlin-stdlib-jdk7:$kotlin_version"
//implementation 'androidx.appcompat:appcompat:1.0.2'
//implementation 'androidx.core:core-ktx:1.1.0'
//implementation 'androidx.constraintlayout:constraintlayout:1.1.3'
//implementation 'androidx.arch.core:core-common:2.1.0'
//implementation 'androidx.arch.core:core-runtime:2.1.0'
//implementation 'androidx.activity:activity-ktx:1.0.0'
//implementation "androidx.fragment:fragment-ktx:$fragment_version"
//implementation "androidx.fragment:fragment-testing:$fragment_version"
//implementation 'io.coil-kt:coil:0.8.0'
//implementation "androidx.appcompat:appcompat:$appcompat_version"
//implementation "com.github.haroldadmin:Vector:0.5.5"
//// alternatively - just ViewModel
//implementation "androidx.lifecycle:lifecycle-viewmodel:$lifecycle_version"
//// For Kotlin use lifecycle-viewmodel-ktx
//// alternatively - just LiveData
//implementation "androidx.lifecycle:lifecycle-livedata:$lifecycle_version"
//// alternatively - Lifecycles only (no ViewModel or LiveData). Some UI
////     AndroidX libraries use this lightweight import for Lifecycle
//implementation "androidx.lifecycle:lifecycle-runtime:$lifecycle_version"
//annotationProcessor "androidx.lifecycle:lifecycle-compiler:$lifecycle_version"
//implementation "androidx.preference:preference-ktx:$preference_version"
//implementation 'androidx.lifecycle:lifecycle-extensions:2.1.0'
//implementation 'androidx.transition:transition:1.2.0'
//implementation "com.squareup.okhttp3:okhttp:$okhttp_version"
//implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2'
//implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.4.2'
//implementation 'com.google.android.material:material:1.1.0'
//implementation "com.squareup.okhttp3:logging-interceptor:$okhttp_version"
//implementation "com.squareup.retrofit2:converter-gson:$retrofit_version"
//
//implementation "com.airbnb.android:epoxy:$epoxy_version"
//kapt "com.airbnb.android:epoxy-processor:$epoxy_version"
//implementation 'com.android.support:multidex:1.0.3'
//// Test helpers
////    testImplementation "androidx.room:room-testing:$room_version"
