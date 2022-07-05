plugins {
    kotlin(Plugins.serialization) version Versions.kotlin
    id(Plugins.androidLib)
    kotlin(Plugins.multiplatform)
//    id(Plugins.realm_db)
    id(Plugins.sqlDelightPlugin)

}
version = "1.0"

kotlin {
    android()
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "shared"
        }
    }

    sourceSets {
        val commonMain by getting{
            dependencies {
                implementation(Deps.Other.datetime)
//                implementation(Deps.RealmDB.common)
                implementation(Deps.Coroutines.common)
                implementation(Deps.Koin.common)
                implementation(Deps.SQLDelight.runTime)
                implementation(Deps.Other.napier)
                implementation("dev.gitlive:firebase-storage:1.6.1")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting{
            dependencies {
                implementation(Deps.SQLDelight.androidDriver)

            }
        }
        val androidTest by getting
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
            dependencies {
                implementation(Deps.SQLDelight.nativeDriver)

            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }
    }
}

android {
    compileSdk = Versions.compile_sdk
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = Versions.min_sdk
        targetSdk = Versions.target_sdk
    }
}

sqldelight {
    database("NoteDatabase") {
        packageName = "com.example.noteappwithkmm.dataSource.cache"
        sourceFolders = listOf("sqldelight")
        version = 2
    }
}