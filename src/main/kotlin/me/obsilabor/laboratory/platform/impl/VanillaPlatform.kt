package me.obsilabor.laboratory.platform.impl

import com.github.ajalt.mordant.rendering.TextColors
import me.obsilabor.laboratory.platform.IPlatform
import me.obsilabor.laboratory.utils.downloadFile
import me.obsilabor.pistonmetakt.PistonMetaClient
import java.nio.file.Path

object VanillaPlatform : IPlatform {
    override val name: String = "vanilla"
    override val jarNamePattern = "vanilla-\$mcVersion.jar"
    override val coloredName = TextColors.brightGreen(name)

    override suspend fun getMcVersions(): List<String> {
        return PistonMetaClient.getLauncherMeta().versions.reversed().map { it.id }
    }

    override suspend fun getBuilds(mcVersion: String): List<String> {
        return listOf("1")
    }

    override suspend fun downloadJarFile(path: Path, mcVersion: String, build: String): Boolean {
        val url = PistonMetaClient.getLauncherMeta().versions.first { it.id == mcVersion }.getDownloads().server.url
        downloadFile(url, path)
        return true
    }
}