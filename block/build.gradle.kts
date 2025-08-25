plugins {
    `java-library`
}

group = "net.theevilreaper"
version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation(platform(libs.mycelium.bom))
    compileOnly(libs.minestom)

    testImplementation(libs.minestom)
    testImplementation(libs.cyano)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testImplementation(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.engine)
}

tasks {
    test {
        jvmArgs("-Dminestom.inside-test=true")
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }
}