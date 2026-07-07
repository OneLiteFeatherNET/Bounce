import org.gradle.api.tasks.testing.Test

plugins {
    java
}

version = "0.0.1"

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

tasks.withType<Test>().configureEach {
    jvmArgs("-Dminestom.inside-test=true")
    useJUnitPlatform()
    testLogging {
        events("passed", "skipped", "failed")
    }
}
