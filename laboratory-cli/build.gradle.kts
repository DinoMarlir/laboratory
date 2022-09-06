plugins {
    kotlin("jvm") version "1.7.10"
    kotlin("plugin.serialization") version "1.7.10"
    id("application")
}

group = "me.obsilabor"
version = "jvm" // I suck at bash therefore I want the archive name to be laboratory-jvm.zip

application {
    mainClass.set("me.obsilabor.laboratory.ApplicationKt")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:atomicfu-jvm:0.18.3")
    implementation(project(":laboratory-common"))

    implementation("com.github.ajalt.clikt:clikt:3.5.0")
    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta7")

    implementation("io.ktor:ktor-client-core:2.1.1")
    implementation("io.ktor:ktor-client-cio:2.1.1")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.1")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.1")

    implementation("me.obsilabor:piston-meta-kt:1.0.2")
}

tasks {
    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}