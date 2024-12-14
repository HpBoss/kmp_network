import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
}

kotlin {
    androidTarget {
        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }

    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Shared"
            isStatic = true
        }
    }

    js(IR) {
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            with(libs) {
                implementation(kotlinx.serialization.json)
                implementation(kotlinx.coroutines.core)
                implementation(bundles.ktor)
                api(bundles.decompose)
                implementation(essenty.lifecycle)
            }
        }

        androidMain.dependencies {
            with(libs) {
                implementation(ktor.client.okhttp)
                implementation(kotlinx.coroutines.android)
            }
        }

        iosMain.dependencies {
            with(libs) {
                implementation(ktor.client.darwin)
            }
        }

        jsMain.dependencies {
            with(compose) {
                implementation(html.core)
            }
            with(libs) {
                implementation(ktor.client.js)
                implementation(ktor.client.json.js)
            }
        }
    }
}

android {
    namespace = "com.jd.library.network.shared"
    compileSdk = libs.versions.android.compileSdk.get().toInt()
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    defaultConfig {
        minSdk = libs.versions.android.minSdk.get().toInt()
    }
}
