plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("application")
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.obsilabor"
version = "jvm"

application {
    mainClass.set("me.obsilabor.chemicae.ChemicaeServerAppKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.slf4j:slf4j-simple:2.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation(project(":laboratory-common"))
    implementation("com.github.ajalt.clikt:clikt:3.5.0")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta7")
    implementation("io.ktor:ktor-server-core:2.1.2")
    implementation("io.ktor:ktor-server-netty:2.1.2")
    implementation("io.ktor:ktor-server-websockets:2.1.2")

    implementation("io.ktor:ktor-client-core:2.1.2")
    implementation("io.ktor:ktor-client-cio:2.1.2")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.2")

    implementation("me.obsilabor:piston-meta-kt:1.0.2")
}

tasks {
    shadowJar {
        manifest {
            attributes(
                "Main-Class" to "me.obsilabor.chemicae.ChemicaeServerAppKt"
            )
        }
    }
    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}