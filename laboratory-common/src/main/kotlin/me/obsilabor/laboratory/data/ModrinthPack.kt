package me.obsilabor.laboratory.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ModrinthPack(
    val formatVersion: Int,
    val game: String,
    val versionId: String,
    val name: String,
    val summary: String? = null,
    val files: List<File>,
    val dependencies: Dependencies
)

@Serializable
data class File(
    val path: String,
    val hashes: Hashes,
    val downloads: List<String>,
    val fileSize: Long
)

@Serializable
data class Hashes(
    val sha1: String,
    val sha512: String
)

@Serializable
data class Dependencies(
    val minecraft: String,
    val forge: String? = null,
    @SerialName("fabric-loader") val fabricLoader: String? = null,
    @SerialName("quilt-loader") val quiltLoader: String? = null
)