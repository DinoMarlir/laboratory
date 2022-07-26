package me.obsilabor.laboratory.platform.impl

import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.SerialName
import me.obsilabor.laboratory.httpClient
import me.obsilabor.laboratory.platform.IPlatform
import me.obsilabor.laboratory.utils.downloadFile
import java.nio.file.Path

object PaperPlatform : IPlatform {
    override val name = "papermc"
    override val jarNamePattern = "paper-\$mcVersion-\$build.jar"

    override suspend fun getBuilds(): List<String> {
        val builds = arrayListOf<String>()
        val projectInfo = httpClient.get("https://api.papermc.io/v2/projects/paper/").body<ProjectResponse>()
        for (version in projectInfo.versions) {
            val versionInfo = httpClient.get("https://api.papermc.io/v2/projects/paper/versions/$version").body<ProjectVersionsResponse>()
            versionInfo.builds.map { it.toString() }.forEach { builds+=it }
        }
        return builds
    }

    override suspend fun getMcVersions(): List<String> {
        val projectInfo = httpClient.get("https://api.papermc.io/v2/projects/paper/").body<ProjectResponse>()
        return projectInfo.versions
    }

    override suspend fun downloadJarFile(path: Path, mcVersion: String, build: String): Boolean {
        val url = "https://api.papermc.io/v2/projects/paper/versions/$mcVersion/builds/$build/downloads/paper-$mcVersion-$build.jar"
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