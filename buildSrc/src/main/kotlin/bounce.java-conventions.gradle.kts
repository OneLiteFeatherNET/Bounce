import org.gradle.api.tasks.testing.Test

plugins {
    java
}

group = "net.theevilreaper"
version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

dependencies {
    // Gemeinsame JUnit 5 Test-Abhängigkeiten mit explizitem add()
    add("testImplementation", "org.junit.jupiter:junit-jupiter-api")
    add("testImplementation", "org.junit.jupiter:junit-jupiter-params")
    add("testImplementation", "org.junit.platform:junit-platform-launcher")
    add("testRuntimeOnly", "org.junit.jupiter:junit-jupiter-engine")
}

tasks.withType<Test>().configureEach {
    jvmArgs("-Dminestom.inside-test=true")
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
