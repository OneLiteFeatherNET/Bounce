plugins {
    `java-library`
    id("bounce.java-conventions")
}

dependencies {
    implementation(platform(libs.aonyx.bom))
    implementation(libs.slf4j.api)
    compileOnly(libs.adventure)
    compileOnly(libs.minestom)
    compileOnly(libs.aves)
    compileOnly(libs.xerus)
    compileOnly(libs.luckperms.api)
    // Only to compile LuckPermsSupport.bootstrap(). The artifact is shipped by the game and setup
    // modules as runtimeOnly - common must not put it on any runtime class path, because its
    // absence is exactly what LuckPermsSupport detects.
    compileOnly(libs.luckperms.minestom.loader)

    testImplementation(libs.minestom)
    testImplementation(libs.luckperms.api)
    testImplementation(libs.aves)
    testImplementation(libs.cyano)
    testImplementation(libs.junit.api)
    testImplementation(libs.junit.params)
    testImplementation(libs.junit.platform.launcher)
    testRuntimeOnly(libs.junit.engine)
}
