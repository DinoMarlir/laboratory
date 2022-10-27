plugins {
    kotlin("jvm") version "1.7.20"
    kotlin("plugin.serialization") version "1.7.20"
    id("application")
    id("maven-publish")
    id("signing")
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

    implementation("io.ktor:ktor-client-core:2.1.2")
    implementation("io.ktor:ktor-client-cio:2.1.2")
    implementation("io.ktor:ktor-client-content-negotiation:2.1.2")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.1.2")

    implementation("me.obsilabor:piston-meta-kt:1.0.2")
    implementation("net.lingala.zip4j:zip4j:2.11.2")
}

signing {
    sign(publishing.publications)
}

val publishVersion: String
    get() = File("../.meta/version").readText()

tasks {
    compileJava {
        options.release.set(17)
        options.encoding = "UTF-8"
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "17"
    }
}

publishing {
    kotlin.runCatching {
        repositories {
            maven("https://repo.obsilabor.me/snapshots") {
                name = "obsilaborRepoSnapshots"
                credentials(PasswordCredentials::class) {
                    username = (property("obsilaborRepoUsername") ?: return@credentials) as String
                    password = (property("obsilaborRepoPassword") ?: return@credentials) as String
                }
                authentication {
                    create<BasicAuthentication>("basic")
                }
            }
        }
    }.onFailure {
        println("Unable to add publishing repositories: ${it.message}")
    }

    publications {
        create<MavenPublication>(project.name) {
            from(components["java"])
            artifact(tasks.distZip)

            this.groupId = project.group.toString()
            this.artifactId = project.name.toLowerCase()
            this.version = publishVersion
        }
    }
}