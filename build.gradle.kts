plugins {
    java
    `maven-publish`
}

group = "com.theevilreaper"
version = "0.0.1"
description = "Bounce"

dependencies {
    compileOnly(libs.org.spigotmc.spigot.api)
    compileOnly(libs.net.titan.cloudspigotapi)
    compileOnly(libs.de.icevizion.icecore)
}

java.sourceCompatibility = JavaVersion.VERSION_1_8

