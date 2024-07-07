import org.gradle.nativeplatform.platform.internal.DefaultNativePlatform

plugins {
    java
    kotlin("jvm") version "1.9.21"
    application
    id("org.javamodularity.moduleplugin") version "1.8.12"
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "ru.squad10"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.brunomnsilva:smartgraph:2.0.0")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

javafx {
    version = "17.0.8"
    modules("javafx.controls")
}

kotlin {
    jvmToolchain(17)
}

application {
    mainModule = "algoapp"
    mainClass = "ru.squad10.Main"
}