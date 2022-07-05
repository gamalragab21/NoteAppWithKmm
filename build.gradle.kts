buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.7.0")
//        classpath("com.android.tools.build:gradle:7.2.1")
        classpath(Deps.Build.kotlin_gradle_plugin)
        classpath(Deps.Build.android_gradle_plugin)
        classpath(Deps.Build.hiltGradlePlugin)
//        classpath("io.realm.kotlin:gradle-plugin:1.0.0")
        classpath(Deps.Build.sqlDelightGradlePlugin)

    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}