plugins {
    id("com.android.application")
}
android {
    namespace = "com.example.ewallet"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.ewallet"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    buildToolsVersion = "34.0.0"
}

dependencies {

    // add CCP
    implementation("com.hbb20:ccp:2.5.4")

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.11.0")
//    implementation ("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment:2.7.7")
    implementation("androidx.navigation:navigation-ui:2.7.7")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    implementation ("androidx.viewpager2:viewpager2:1.0.0")
//    implementation ("com.google.android.material:material:1.3.0-alpha03")
//    implementation ("com.google.android.material:material:1.11.0")

// Qrcode dependencies
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0")
    coreLibraryDesugaring ("com.android.tools:desugar_jdk_libs:2.0.4")


    implementation ("androidx.multidex:multidex:2.0.1")



    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")


    // QR scan
    implementation ("com.journeyapps:zxing-android-embedded:4.3.0@aar")
    implementation ("com.google.zxing:core:3.4.1")
    // van tay sinh trac hoc
    implementation ("com.google.android.gms:play-services-vision:20.1.3")
    implementation ("androidx.biometric:biometric:1.1.0")


    implementation("com.github.bumptech.glide:glide:4.14.2")


}