plugins {
    kotlin("multiplatform")
    id("com.android.library")
//    id("kotlinx-serialization")
    kotlin("plugin.serialization") version "1.4.10"
}

val ktorVersion = "1.4.3"
val ktorSerialization = "1.4.3"

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}


repositories {
    gradlePluginPortal()
    google()
    jcenter()
    mavenCentral()
    maven {
        url = uri("https://dl.bintray.com/kotlin/kotlin-eap")
    }
}
kotlin {
    android()
//    ios {
//        binaries {
//            framework {
//                baseName = "recipe"
//            }
//        }
//    }
    sourceSets {
        val commonMain by getting {
            dependencies{
//                implementation("org.jetbrains.kotlin:kotlin-stdlib")
//                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.4.2")
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-runtime:0.4.1")
//                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.0.1")
//                implementation("io.ktor:ktor-client-core:$ktorVersion")
//                implementation("io.ktor:ktor-serialization:$ktorSerialization")
//                implementation("io.ktor:ktor-client-cio:$ktorVersion")
                implementation("ch.qos.logback:logback-classic:1.2.1")
                // Coroutines
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.kotlinCoroutines}")

                implementation(Ktor.clientCore)
                implementation(Ktor.clientJson)
                implementation(Ktor.clientLogging)
                implementation(Ktor.clientSerialization)
                implementation(Ktor.clientCio)

                // Serialize
                implementation(Serialization.core)

                // Kodein-DB
                api("org.kodein.db:kodein-db:${Versions.kodein_db}")
                api("org.kodein.db:kodein-db-serializer-kotlinx:${Versions.kodein_db}")


            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("androidx.core:core-ktx:1.2.0")
                implementation(Ktor.clientAndroid)
            }
        }
        val androidTest by getting {
            dependencies {
                implementation(kotlin("test-junit"))
                implementation("junit:junit:4.12")
            }
        }
//        val iosMain by getting
//        val iosTest by getting
    }
}
android {
    compileSdkVersion(29)
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdkVersion(24)
        targetSdkVersion(29)
        versionCode = 1
        versionName = "1.0"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

}