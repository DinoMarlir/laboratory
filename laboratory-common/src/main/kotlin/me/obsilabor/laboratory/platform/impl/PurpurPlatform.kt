package me.obsilabor.laboratory.platform.impl

import com.github.ajalt.mordant.rendering.TextColors
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.Serializable
import me.obsilabor.laboratory.httpClient
import me.obsilabor.laboratory.platform.IPlatform
import me.obsilabor.laboratory.utils.downloadFile
import java.nio.file.Path

object PurpurPlatform : IPlatform {
    override val name = "purpurmc"
    override val jarNamePattern = "purpur-\$mcVersion-\$build.jar"
    override val coloredName = TextColors.magenta(name)
    override val configurationFiles = mapOf(
        "config/paper-global.yml" to "Paper config for versions above 1.19",
        "paper.yml" to "Paper config for versions below 1.19",
        "purpur.yml" to "Purpur config",
        "pufferfish.yml" to "Pufferfish config",
        "spigot.yml" to "Spigot config",
        "bukkit.yml" to "Bukkit config",
        "server.properties" to "Vanilla config"
    )
    override val modsFolder = "plugins"
    override val spigotBased = true

    override suspend fun getMcVersions(): List<String> {
        return httpClient.get("https://api.purpurmc.org/v2/purpur/").body<PurpurVersionsResponse>().versions
    }

    override suspend fun getBuilds(mcVersion: String): List<String> {
        return httpClient.get("https://api.purpurmc.org/v2/purpur/$mcVersion/").body<PurpurBuildsResponse>().builds.all
    }

    override suspend fun downloadJarFile(path: Path, mcVersion: String, build: String): Boolean {
        val url = "https://api.purpurmc.org/v2/purpur/$mcVersion/$build/download"
        runCatching {
            downloadFile(url, path)
        }.onFailure {
            it.printStackTrace()
            return false
        }
        return true
    }

    @Serializable
    data class PurpurVersionsResponse(
        val project: String,
        val versions: List<String>
    )

    @Serializable
    data class PurpurBuildsResponse(
        val builds: PurpurBuilds,
        val project: String,
        val version: String
    )

    @Serializable
    data class PurpurBuilds(
        val all: List<String>,
        val latest: String
    )
}
