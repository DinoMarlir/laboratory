plugins {
    kotlin("jvm") version "1.7.20"
    id("com.github.johnrengelman.shadow") version "7.1.2"
}

group = "me.obsilabor"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
}

tasks {
    shadowJar {
        manifest {
            attributes(
                "Main-Class" to "me.obsilabor.laboratory.WinUpdaterKt"
            )
        }
    }
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileJava {
        options.release.set(8)
        options.encoding = "UTF-8"
    }
}