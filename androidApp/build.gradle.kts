plugins {
    id(Plugins.androidApp)
    kotlin(Plugins.androidPlugin)
    kotlin(Plugins.kapt)

}

android {
    compileSdk = Versions.compile_sdk
    defaultConfig {
        applicationId = Versions.appId
        minSdk = Versions.min_sdk
        targetSdk = Versions.target_sdk
        versionCode = Versions.version_code
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion =
            Versions.compose_compiler_version
    }
}

dependencies {
    implementation(project(":shared"))
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")
    //2
    with(Deps.Google) {
        implementation(material)
//        implementation(material3)
//        implementation(material3_window_size)
    }
    //Compose
    with(Deps.Compose) {
        implementation(compiler)
        implementation(runtime)
        implementation(runtime_livedata)
        implementation(ui)
        implementation(tooling)
        implementation(foundation)
        implementation(foundationLayout)
        implementation(material)
        implementation(material_icons)
        implementation(activity)
        implementation(uiUtil)
        implementation(constraintLayout)
        implementation(navigation)
        implementation(coil)
    }
//    with(Deps.Hilt){
//        implementation(hiltAndroid)
//        implementation(hiltNavigation)
//        kapt(hiltCompiler)
//    }
    with(Deps.AndroidX) {
        implementation(appCompat)
    }
    with(Deps.Other) {
        implementation(datetime)
        debugImplementation(leakCanary)
        implementation(napier)

    }
//    with(Deps.Ktor){
//        implementation(android)
//    }
    with(Deps.Coroutines){
        implementation(android)
    }

    with(Deps.Koin) {
        implementation(android)
        implementation(compose)
    }
}