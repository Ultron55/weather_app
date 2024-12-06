plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    alias(libs.plugins.google.devtools.ksp)
    alias(libs.plugins.google.dagger.hilt.android)
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "example.weather.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "example.weather.app"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        resourceConfigurations += setOf(
            "ar",  // Arabic
            "bn",  // Bengali
            "bg",  // Bulgarian
            "ca",  // Catalan
            "cs",  // Czech
            "da",  // Danish
            "de",  // German
            "el",  // Greek
            "en",  // English
            "es",  // Spanish
            "fi",  // Finnish
            "fr",  // French
            "he",  // Hebrew
            "hi",  // Hindi
            "hr",  // Croatian
            "hu",  // Hungarian
            "id",  // Indonesian
            "it",  // Italian
            "ja",  // Japanese
            "jv",  // Javanese (необходимо проверить поддержку)
            "ko",  // Korean
            "mr",  // Marathi
            "ms",  // Malay
            "nl",  // Dutch
            "no",  // Norwegian
            "pa",  // Punjabi
            "pl",  // Polish
            "pt",  // Portuguese
            "ro",  // Romanian
            "ru",  // Russian
            "si",  // Sinhala
            "sk",  // Slovak
            "sr",  // Serbian
            "sv",  // Swedish
            "ta",  // Tamil
            "te",  // Telugu
            "th",  // Thai
            "tr",  // Turkish
            "uk",  // Ukrainian
            "ur",  // Urdu
            "vi",  // Vietnamese
            "zh-rCN",  // Simplified Chinese
            "zh-rTW",  // Traditional Chinese (если требуется)
            "zu"   // Zulu
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
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        viewBinding = true
        compose = true
    }

    sourceSets {
        getByName("main").java.srcDirs("build/generated/source/navigation-args")
    }

    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.toString()
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.recyclerview.selection)

    //Kotlin
    implementation(libs.kotlin.stdlib)
    implementation(libs.kotlin.reflect)

    //navigation
    implementation(libs.androidx.navigation.runtime.ktx)
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    //Coroutines and Livecycle
    implementation(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.play.services.location)
    ksp(libs.androidx.lifecycle.compiler)
    implementation(libs.androidx.lifecycle.extensions)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.lifecycle.livedata.ktx)

    //Retrofit
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.logging.interceptor)
    implementation(libs.okhttp.urlconnection)

    debugImplementation(libs.chucker.library)
    releaseImplementation(libs.library.no.op)

    //Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    //Images
    implementation(libs.glide)
    implementation(libs.android.gif.drawable)

    implementation(libs.androidx.preference.ktx)

    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.activity.compose)


}