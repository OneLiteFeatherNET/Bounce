plugins {
    `java-library`
}

group = "net.theevilreaper"
version = "0.0.1"


dependencies {
    implementation(platform(libs.mycelium.bom))
    implementation(platform(libs.aonyx.bom))
    compileOnly(libs.adventure)
    compileOnly(libs.minestom)
    compileOnly(libs.aves)
    compileOnly(libs.xerus)
}

