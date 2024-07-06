import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    java
    kotlin("jvm") version "1.9.21"
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("org.openjfx.javafxplugin") version "0.1.0"
    id("org.beryx.jlink") version "2.25.0"
}

group = "ru.squad10"
version = "1.0-SNAPSHOT"

val currentOS = DefaultNativePlatform.getCurrentOperatingSystem()

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgumentProviders.add(CommandLineArgumentProvider {
        listOf(
            "--patch-module",
            "my.module=${sourceSets["main"].output.asPath}"
        )
    })
}

java {
    targetCompatibility = JavaVersion.VERSION_17
    sourceCompatibility = JavaVersion.VERSION_17
    modularity.inferModulePath = true
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.brunomnsilva:smartgraph:2.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
}

javafx {
    version = "17.0.8"
    modules("javafx.base", "javafx.controls")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainModule = "algoapp"
    mainClass = "ru.squad10.AlgoApp"
}

jlink {
    imageZip = project.file("${buildDir}/distributions/app-${javafx.platform.classifier}.zip")
    options = listOf("--strip-debug", "--compress", "2", "--no-header-files", "--no-man-pages")
    launcher {
        name = "app"
    }
}

tasks {
    jlinkZip {
        group = "distribution"
    }
}