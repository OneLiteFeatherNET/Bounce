plugins {
    id("bounce.java-conventions")
}

// Minestom extension that bridges CloudNet permission checks to LuckPerms. It is packaged as a
// standalone extension jar (dropped into a CloudNet service's extensions/ folder next to the
// CloudNet bridge) and never bundled into a fat jar. Everything it compiles against is provided at
// runtime: the CloudNet driver by the CloudNet wrapper, the bridge by the CloudNet_Bridge
// extension, Minestom and Adventure by the application classloader.
dependencies {
    compileOnly(platform(libs.aonyx.bom))
    compileOnly(libs.minestom)
    compileOnly(libs.adventure)
    compileOnly(platform(libs.minestom.extensions.bom))
    compileOnly(libs.minestom.extensions)
    compileOnly(libs.minestom.extensions.processor)
    annotationProcessor(platform(libs.minestom.extensions.bom))
    annotationProcessor(libs.minestom.extensions.processor)

    compileOnly(platform(libs.cloudnet.bom))
    compileOnly(libs.cloudnet.driver.api)
    compileOnly(libs.cloudnet.bridge)
    compileOnly(libs.cloudnet.bridge.impl)
}

// The annotation processor generates extension.json but cannot know the project version. Subprojects
// do not inherit the root version, so read it from the root project explicitly.
tasks.compileJava {
    options.compilerArgs.add("-Aminestom.extension.version=${rootProject.version}")
}
