import com.android.build.gradle.internal.cxx.configure.gradleLocalProperties
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.jnu.togetherpet"
    compileSdk = 34

    defaultConfig {
        ndk {
            abiFilters.add("arm64-v8a")
            abiFilters.add("armeabi-v7a")
            abiFilters.add("x86")
            abiFilters.add("x86_64")
        }
        applicationId = "com.jnu.togetherpet"
        minSdk = 26
        targetSdk = 34
        versionCode = 2
        versionName = "1.01"

        testInstrumentationRunner = "com.example.togetherpet.CustomTestRunner"

        buildConfigField(
            "String",
            "KAKAO_NATIVE_APP_KEY",
            gradleLocalProperties(rootDir, providers).getProperty("KAKAO_NATIVE_APP_KEY")
        )
        manifestPlaceholders["KAKAO_NATIVE_APP_KEY"] =
            gradleLocalProperties(rootDir, providers).getProperty("KAKAO_NATIVE_APP_KEY")

        buildConfigField(
            "String",
            "BASE_URL",
            gradleLocalProperties(rootDir, providers).getProperty("BASE_URL")
        )
        buildConfigField(
            "String",
            "KAKAO_BASE_URL",
            gradleLocalProperties(rootDir, providers).getProperty("KAKAO_BASE_URL")
        )
        buildConfigField(
            "String",
            "KAKAO_LOCAL_API_KEY",
            gradleLocalProperties(rootDir, providers).getProperty("KAKAO_LOCAL_API_KEY")
        )
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
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
        dataBinding = true
    }
    packaging { resources.excludes.add("META-INF/*") }

}
dependencies {
    val roomVersion = "2.6.1"
    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.activity:activity-ktx:1.8.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation(libs.androidx.activity)
    implementation("com.kakao.sdk:v2-user:2.20.6")
    implementation("com.kakao.maps.open:android:2.11.9")
    implementation("com.google.android.gms:play-services-location:21.3.0")
    implementation("com.google.dagger:hilt-android:2.51.1")
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.1")
    implementation("androidx.test:core-ktx:1.6.1")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test:runner:1.6.2")
    androidTestImplementation("androidx.test:rules:1.6.1")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-intents:3.5.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")
    implementation("androidx.room:room-ktx:$roomVersion")
    implementation("androidx.activity:activity-ktx:1.9.2")
    implementation("androidx.fragment:fragment-ktx:1.8.5")
    implementation("androidx.room:room-runtime:$roomVersion")
    kapt("androidx.room:room-compiler:$roomVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("com.github.bumptech.glide:glide:4.16.0")    //Glide : url -> 이미지 로딩
    implementation("de.hdodenhof:circleimageview:3.1.0")
    implementation("androidx.core:core-splashscreen:1.0.1")
    implementation("com.google.dagger:hilt-android-testing:2.51.1")
    testImplementation("androidx.arch.core:core-testing:2.1.0")
    testImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.3.9")
    testAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.51.1")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.51.1")
    androidTestAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.51.1")
    kaptTest("com.google.dagger:hilt-android-compiler:2.51.1")
    kaptAndroidTest("com.google.dagger:hilt-compiler:2.51.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.51.1")
    androidTestImplementation("androidx.fragment:fragment-testing:1.8.5")
    androidTestImplementation("androidx.navigation:navigation-testing:2.8.1")
    implementation("io.mockk:mockk:1.13.11")
    testImplementation("io.mockk:mockk-android:1.13.11")
    testImplementation("io.mockk:mockk-agent:1.13.11")
}