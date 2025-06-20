plugins {
    java
    `maven-publish`
    application
    alias(libs.plugins.shadow)
}

group = "net.theevilreaper"
version = "0.0.1"
description = "Bounce"

dependencies {
    implementation(platform(libs.mycelium.bom))
    implementation(platform(libs.aonyx.bom))
    implementation(project(":common"))
    implementation(libs.adventure)
    implementation(libs.pvp)
    implementation(libs.minestom)
    implementation(libs.aves)
    implementation(libs.xerus)

    testImplementation(libs.minestom)
    testImplementation(libs.aves)
    testImplementation(libs.cyano)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testImplementation(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.engine)
}

application {
    mainClass.set("net.theevilreaper.bounce.BounceServer")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
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

