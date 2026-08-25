plugins {
    java
    `maven-publish`
    application
    alias(libs.plugins.shadow)
}

group = "net.theevilreaper"
version = "0.6.0" // x-release-please-version
description = "Bounce"

dependencies {
    implementation(project(":common"))
    implementation(platform(libs.aonyx.bom))
    implementation(platform(libs.falco.bom))
    implementation(project(":block"))
    implementation(libs.adventure)
    implementation(libs.falco.anvil)
    implementation(libs.pvp)
    implementation(libs.minestom)
    implementation(libs.aves)
    implementation(libs.slf4j.api)
    // SLF4J needs a binding at runtime; without one it falls back to NOP and the
    // server logs nothing at all.
    runtimeOnly(libs.slf4j.simple)
    implementation(libs.xerus)

    implementation(platform(libs.minestom.extensions.bom))
    implementation(libs.minestom.extensions)

    // Guava used to arrive transitively through CloudNet; bundle it explicitly now that CloudNet
    // is no longer a direct dependency of this module (it loads as an extension at runtime instead).
    implementation(libs.guava)
    compileOnly(libs.luckperms.api)
    runtimeOnly(libs.luckperms.minestom.loader)

    testImplementation(libs.minestom)
    testImplementation(libs.aves)
    testImplementation(libs.cyano)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testImplementation(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.engine)
}

// Keeps the loader off the test class path, which is what makes LuckPermsSupport report absent and
// every permission check answer TRUE during tests.
configurations.testRuntimeClasspath {
    exclude(group = "net.luckperms", module = "minestom-loader")
}

application {
    mainClass.set("net.theevilreaper.bounce.BounceServer")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks {
    jar {
        dependsOn("shadowJar")
    }

    test {
        jvmArgs("-Dminestom.inside-test=true")
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}

