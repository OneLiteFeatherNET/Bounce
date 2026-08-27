rootProject.name = "Bounce"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://central.sonatype.com/repository/maven-snapshots/")
        maven("https://repository.derklaro.dev/snapshots/")
        maven {
            name = "OneLiteFeatherReleases"
            url = uri("https://repo.onelitefeather.dev/releases")
        }
        maven {
            name = "OneLiteFeatherRepository"
            url = uri("https://repo.onelitefeather.dev/onelitefeather")
            if (System.getenv("CI") != null) {
                credentials {
                    username = System.getenv("ONELITEFEATHER_MAVEN_USERNAME")
                    password = System.getenv("ONELITEFEATHER_MAVEN_PASSWORD")
                }
            } else {
                credentials(PasswordCredentials::class)
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }

    versionCatalogs {
        create("libs") {
            version("shadow", "9.6.1")
            version("aonyx", "0.8.4")
            version("pvp", "2026.05.30-26.1.1")
            version("cloudnet", "4.0.0-RC18-SNAPSHOT")
            version("slf4j", "2.0.18")
            version("falco", "2.1.0")
            version("luckperms", "5.5")
            version("luckperms-minestom-loader", "6.0.0")
            version("minestom-extensions", "2.2.0")
            version("guava", "33.7.1-jre")
            version("pica", "0.1.2")

            library("aonyx.bom", "net.onelitefeather", "aonyx-bom").versionRef("aonyx")

            library("falco.bom", "net.onelitefeather", "falco-bom").versionRef("falco")
            library("falco.anvil", "net.onelitefeather", "falco-anvil").withoutVersion()

            library("slf4j.api", "org.slf4j", "slf4j-api").versionRef("slf4j")
            library("slf4j.simple", "org.slf4j", "slf4j-simple").versionRef("slf4j")
            library("pvp", "io.github.togar2", "MinestomPvP").versionRef("pvp")
            library("minestom", "net.minestom", "minestom").withoutVersion()
            library("adventure", "net.kyori", "adventure-text-minimessage").withoutVersion()
            library("cyano", "net.onelitefeather", "cyano").withoutVersion()
            library("guira", "net.onelitefeather", "guira").withoutVersion()
            library("junit.api", "org.junit.jupiter", "junit-jupiter-api").withoutVersion()
            library("junit.engine", "org.junit.jupiter", "junit-jupiter-engine").withoutVersion()
            library("junit.platform.launcher", "org.junit.platform", "junit-platform-launcher").withoutVersion()
            library("junit.params", "org.junit.jupiter", "junit-jupiter-params").withoutVersion()
            library("aves", "net.theevilreaper", "aves").withoutVersion()
            library("xerus", "net.theevilreaper", "xerus").withoutVersion()
            library("luckperms.api", "net.luckperms", "api").versionRef("luckperms")
            library("luckperms.minestom.loader", "net.luckperms", "minestom-loader").versionRef("luckperms-minestom-loader")
            library("guava", "com.google.guava", "guava").versionRef("guava")

            library("minestom-extensions-bom", "net.onelitefeather", "minestom-extensions-bom").versionRef("minestom-extensions")
            library("minestom-extensions", "net.onelitefeather", "minestom-extensions").withoutVersion()
            library("minestom-extensions-processor", "net.onelitefeather", "minestom-extensions-processor").withoutVersion()
            library("pica", "net.onelitefeather", "pica").versionRef("pica")

            library("cloudnet-bom", "eu.cloudnetservice.cloudnet", "bom").versionRef("cloudnet")
            library("cloudnet-driver-api", "eu.cloudnetservice.cloudnet", "driver-api").withoutVersion()
            library("cloudnet-bridge", "eu.cloudnetservice.cloudnet", "bridge-api").withoutVersion()
            library("cloudnet-bridge-impl", "eu.cloudnetservice.cloudnet", "bridge-impl").withoutVersion()
            library("cloudnet-driver-impl", "eu.cloudnetservice.cloudnet", "driver-impl").withoutVersion()
            library("cloudnet-platform-inject", "eu.cloudnetservice.cloudnet", "platform-inject-api").withoutVersion()
            library("cloudnet-jvm-wrapper", "eu.cloudnetservice.cloudnet", "wrapper-jvm-api").withoutVersion()

            plugin("shadow", "com.gradleup.shadow").versionRef("shadow")
        }
    }
}

include("common")
include("setup")
include("block")
include("bridge")