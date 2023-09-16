plugins {
    id("com.android.application")
}

android {
    namespace = "com.example.translatorapp"
    compileSdk = 33

    buildFeatures{
        viewBinding = true;
    }
    defaultConfig {
        applicationId = "com.example.translatorapp"
        minSdk = 26
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    buildFeatures{
        viewBinding = true
    }
}
configurations.all {
    resolutionStrategy {
        eachDependency {
            if ((requested.group == "org.jetbrains.kotlin") && (requested.name.startsWith("kotlin-stdlib"))) {
                useVersion("1.8.0")
            }
        }
    }
}
dependencies {

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

//    sdp
    implementation ("com.intuit.sdp:sdp-android:1.1.0")
//    round image view
    implementation("com.makeramen:roundedimageview:2.3.0")

//    textTranslation:
    implementation ("com.google.mlkit:translate:17.0.1")

//    lottie animation:
    implementation( "com.airbnb.android:lottie:6.1.0")

//    Text recoginition:

    implementation ("com.google.android.gms:play-services-mlkit-text-recognition:19.0.0")
    implementation ("com.github.bumptech.glide:glide:4.16.0")


}