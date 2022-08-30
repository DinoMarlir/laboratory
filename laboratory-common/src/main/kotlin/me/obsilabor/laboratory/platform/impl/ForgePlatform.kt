package me.obsilabor.laboratory.platform.impl

import com.github.ajalt.mordant.rendering.TextColors
import io.ktor.client.call.*
import io.ktor.client.request.*
import me.obsilabor.laboratory.httpClient
import me.obsilabor.laboratory.platform.IPlatform
import me.obsilabor.laboratory.utils.downloadFile
import java.nio.file.Path
import java.util.regex.Pattern

object ForgePlatform : IPlatform {
    override val name = "forge"
    override val jarNamePattern = "forgelauncher-"
    override val coloredName = TextColors.white(LegacyFabricPlatform.name)

    override suspend fun getMcVersions(): List<String> {
        val newestVersionPattern = Pattern.compile("<li class=\"elem-active\">(.*?)</li>", Pattern.DOTALL)
        val allVersionsPattern = Pattern.compile("href=\"(.*?)\"", Pattern.DOTALL)
        val url = "https://files.minecraftforge.net/net/minecraftforge/forge/"
        val html = httpClient.get(url) {
            header("Accept", "*/*")
            header("Accept-Language", "*")
            header("Accept-Encoding", "*")
        }
        val matcherAllVersions = allVersionsPattern.matcher(html.body<String>())
        val matcherNewestVersion = newestVersionPattern.matcher(html.body<String>())
        val set = mutableSetOf<String>()
        while (matcherNewestVersion.find()) {
            val possibleVersion = matcherNewestVersion.group(1)
            set.add(possibleVersion)
        }
        while (matcherAllVersions.find()) {
            val possibleVersion = matcherAllVersions.group(1)
            if (possibleVersion.startsWith("index_") && possibleVersion.endsWith(".html")) {
                set.add(possibleVersion.removePrefix("index_").removeSuffix(".html"))
            }
        }
        return set.toList()
    }

    override suspend fun getBuilds(mcVersion: String): List<String> {
        val set = mutableSetOf<String>()
        val url = "https://files.minecraftforge.net/net/minecraftforge/forge/index_${mcVersion}.html"
        val html = httpClient.get(url) {
            header("Accept", "*/*")
            header("Accept-Language", "*")
            header("Accept-Encoding", "*")
        }
        val matcher = SpongePlatform.hrefPattern.matcher(html.body<String>())
        while (matcher.find()) {
            val installerUrl = matcher.group(1)
            val fileName = installerUrl.split("/").last()
            if (fileName.endsWith("-installer.jar")) {
                val versionName = fileName.removePrefix("forge-${mcVersion}-").removeSuffix("-installer.jar")
                set.add(versionName)
            }
        }
        return set.toList()
    }

    // https://adfoc.us/serve/sitelinks/?id=271228&url= <--- remove this prefix
    override suspend fun downloadJarFile(path: Path, mcVersion: String, build: String): Boolean {
        downloadFile("https://maven.minecraftforge.net/net/minecraftforge/forge/$mcVersion-$build/forge-$mcVersion-$build-installer.jar", Path.of(path.toFile().parentFile.absolutePath, "forge-installer.jar"))
        return true
    }

    override suspend fun copyOtherFiles(destinationFolder: Path, mcVersion: String, build: String) {

    }
}


