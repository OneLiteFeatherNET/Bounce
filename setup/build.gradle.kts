plugins {
    `maven-publish`
    application
    alias(libs.plugins.shadow)
    id("bounce.java-conventions")
}

description = "Bounce Setup Server"

dependencies {
    implementation(project(":common"))
    implementation(project(":block"))
    implementation(platform(libs.aonyx.bom))
    implementation(platform(libs.falco.bom))
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
    implementation(libs.guira)

    implementation(platform(libs.minestom.extensions.bom))
    implementation(libs.minestom.extensions)

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
    mainClass.set("net.theevilreaper.bounce.BounceSetupServer")
}

tasks {
    jar {
        dependsOn("shadowJar")
    }
}
