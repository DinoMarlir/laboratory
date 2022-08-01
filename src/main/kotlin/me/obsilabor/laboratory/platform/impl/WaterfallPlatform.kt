package me.obsilabor.laboratory.platform.impl

import com.github.ajalt.mordant.rendering.TextColors
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import me.obsilabor.laboratory.httpClient
import me.obsilabor.laboratory.platform.IPlatform
import me.obsilabor.laboratory.utils.downloadFile
import java.nio.file.Path

object WaterfallPlatform : IPlatform {
    override val name = "waterfall"
    override val jarNamePattern = "waterfall-\$mcVersion-\$build.jar"
    override val coloredName = TextColors.brightRed(name)
    override val isProxy = true

    override suspend fun getBuilds(mcVersion: String): List<String> {
        return httpClient.get("https://api.papermc.io/v2/projects/waterfall/versions/$mcVersion").body<ProjectVersionsResponse>().builds.map { it.toString() }
    }

    override suspend fun getMcVersions(): List<String> {
        return httpClient.get("https://api.papermc.io/v2/projects/waterfall/").body<ProjectResponse>().versions
    }

    override suspend fun downloadJarFile(path: Path, mcVersion: String, build: String): Boolean {
        val url = "https://api.papermc.io/v2/projects/waterfall/versions/$mcVersion/builds/$build/downloads/waterfall-$mcVersion-$build.jar"
        runCatching {
            downloadFile(url, path)
        }.onFailure {
            it.printStackTrace()
            return false
        }
        return true
    }

    @kotlinx.serialization.Serializable
    data class ProjectResponse(
        @SerialName("project_id") val projectId: String,
        @SerialName("project_name") val projectName: String,
        @SerialName("version_groups") val versionGroups: List<String>,
        val versions: List<String>,
    )

    @kotlinx.serialization.Serializable
    data class ProjectVersionsResponse(
        @SerialName("project_id") val projectId: String,
        @SerialName("project_name") val projectName: String,
        val version: String,
        val builds: List<Int>
    )
}