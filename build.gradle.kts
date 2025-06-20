plugins {
    java
    `maven-publish`
}

group = "net.theevilreaper"
version = "0.0.1"
description = "Bounce"

dependencies {
    implementation(platform(libs.mycelium.bom))
    implementation(platform(libs.aonyx.bom))
    implementation(libs.adventure)

    compileOnly(libs.minestom)
    compileOnly(libs.aves)
    compileOnly(libs.xerus)

    testImplementation(libs.minestom)
    testImplementation(libs.aves)
    testImplementation(libs.cyano)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testImplementation(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.engine)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

