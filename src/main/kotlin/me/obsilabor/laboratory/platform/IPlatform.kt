package me.obsilabor.laboratory.platform

import java.nio.file.Path

interface IPlatform {
    val name: String
    val jarNamePattern: String
    val coloredName: String
    val isProxy: Boolean
        get() = false

    suspend fun getMcVersions(): List<String>
    suspend fun getBuilds(mcVersion: String): List<String>
    suspend fun downloadJarFile(path: Path, mcVersion: String, build: String): Boolean
    suspend fun installServer(workingDirectory: Path, installerJarFile: Path, mcVersion: String, build: String) = Unit
    suspend fun copyOtherFiles(destinationFolder: Path, mcVersion: String, build: String) = Unit
}