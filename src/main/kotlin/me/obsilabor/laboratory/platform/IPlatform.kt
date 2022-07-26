package me.obsilabor.laboratory.platform

import java.nio.file.Path

interface IPlatform {
    val name: String
    val jarNamePattern: String

    suspend fun getMcVersions(): List<String>
    suspend fun getBuilds(mcVersion: String): List<String>
    suspend fun downloadJarFile(path: Path, mcVersion: String, build: String): Boolean
}