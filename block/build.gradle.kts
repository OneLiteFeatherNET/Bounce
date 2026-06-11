plugins {
    `java-library`
    id("bounce.java-conventions")
}

dependencies {
    implementation(platform(libs.aonyx.bom))
    compileOnly(libs.minestom)

    testImplementation(libs.minestom)
    testImplementation(libs.cyano)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testImplementation(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.engine)
}
