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
    implementation(libs.adventure)
    implementation(libs.pvp)
    implementation(libs.minestom)
    implementation(libs.aves)
    // https://mvnrepository.com/artifact/org.slf4j/slf4j-api
    implementation("org.slf4j:slf4j-simple:2.0.18")
    implementation(libs.xerus)
    implementation(libs.guira)

    testImplementation(libs.minestom)
    testImplementation(libs.aves)
    testImplementation(libs.cyano)
}

application {
    mainClass.set("net.theevilreaper.bounce.BounceSetupServer")
}

tasks {
    jar {
        dependsOn("shadowJar")
    }
}
