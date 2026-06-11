plugins {
    `java-library`
    id("bounce.java-conventions")
}

dependencies {
    implementation(platform(libs.aonyx.bom))
    compileOnly(libs.adventure)
    compileOnly(libs.minestom)
    compileOnly(libs.aves)
    compileOnly(libs.xerus)

    testImplementation(libs.minestom)
    testImplementation(libs.aves)
    testImplementation(libs.cyano)
}
