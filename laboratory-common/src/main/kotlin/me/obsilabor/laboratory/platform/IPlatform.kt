package me.obsilabor.laboratory.platform

import me.obsilabor.laboratory.arch.Server
import java.nio.file.Path

interface IPlatform {
    val name: String
    val jarNamePattern: String
    val coloredName: String
    val isProxy: Boolean
        get() = false
    val configurationFiles: Map<String, String>
        get() = mapOf("server.properties" to "Vanilla config")
    val modsFolder: String?
        get() = null

    suspend fun getMcVersions(): List<String>
    suspend fun getBuilds(mcVersion: String): List<String>
    suspend fun downloadJarFile(path: Path, mcVersion: String, build: String): Boolean
    suspend fun installServer(workingDirectory: Path, installerJarFile: Path, mcVersion: String, build: String) = Unit
    suspend fun copyOtherFiles(destinationFolder: Path, mcVersion: String, build: String, server: Server) = Unit
}