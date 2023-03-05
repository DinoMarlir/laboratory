package me.obsilabor.laboratory.platform.impl

import com.github.ajalt.mordant.rendering.TextColors
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.arch.Server
import me.obsilabor.laboratory.httpClient
import me.obsilabor.laboratory.platform.IPlatform
import me.obsilabor.laboratory.terminal.SpinnerAnimation
import me.obsilabor.laboratory.utils.copyFolder
import me.obsilabor.laboratory.utils.downloadFile
import me.obsilabor.pistonmetakt.MinecraftVersions
import me.obsilabor.pistonmetakt.PistonMetaClient
import me.obsilabor.pistonmetakt.utils.compareTo
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.regex.Pattern
import kotlin.io.path.absolutePathString

object ForgePlatform : IPlatform {
    override val name = "forge"
    override val jarNamePattern = "forgelauncher-\$mcVersion-\$build.jar"
    override val coloredName = TextColors.white(name)
    override val modsFolder = "mods"

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
        return set.toList().reversed()
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
        return set.toList().reversed()
    }

    override suspend fun downloadJarFile(path: Path, mcVersion: String, build: String): Boolean {
        val dir = File(path.toFile().parentFile.absolutePath + "/$mcVersion-$build")
        if (!dir.exists()) {
            dir.mkdir()
        }
        downloadFile("https://maven.minecraftforge.net/net/minecraftforge/forge/$mcVersion-$build/forge-$mcVersion-$build-installer.jar", Path.of(path.toFile().parentFile.absolutePath + "/$mcVersion-$build", "forge-installer-$mcVersion-$build.jar"))
        downloadFile("https://github.com/mooziii/forgelauncher/releases/download/1.0.1/forgelauncher-1.0.1-all.jar", Path.of(path.toFile().parentFile.absolutePath + "/$mcVersion-$build", "forgelauncher-$mcVersion-$build.jar"))
        installServer(
            Path.of(path.toFile().parentFile.absolutePath),
            Path.of(path.toFile().parentFile.absolutePath + "/$mcVersion-$build", "forge-installer-$mcVersion-$build.jar"),
            mcVersion,
            build
        )
        return true
    }

    override suspend fun installServer(workingDirectory: Path, installerJarFile: Path, mcVersion: String, build: String) {
        withContext(Dispatchers.IO) {
            val processBuilder = ProcessBuilder(
                "java",
                "-jar",
                installerJarFile.toFile().name,
                "--installServer"
            ).directory(workingDirectory.resolve(Path.of("$mcVersion-$build")).toFile())
            val spinner = SpinnerAnimation("Installing Forge")
            runBlocking {
                spinner.start()
                processBuilder.redirectOutput(ProcessBuilder.Redirect.INHERIT).redirectInput(ProcessBuilder.Redirect.INHERIT).start().waitFor()
                Files.copy(Path.of(workingDirectory.absolutePathString() + "/$mcVersion-$build", "/forgelauncher-$mcVersion-$build.jar"), Path.of(Architecture.Platforms.absolutePath, "forge/forgelauncher-$mcVersion-$build.jar"), StandardCopyOption.REPLACE_EXISTING)
                spinner.stop("Forge installed")
            }
        }
    }

    override suspend fun copyOtherFiles(destinationFolder: Path, mcVersion: String, build: String, server: Server) {
        runCatching {
            Files.copy(Path.of(Architecture.Platforms.absolutePath, "vanilla/vanilla-$mcVersion.jar"), Path.of(destinationFolder.absolutePathString(), "minecraft_server.$mcVersion.jar"), StandardCopyOption.REPLACE_EXISTING)
        }.onFailure {
            VanillaPlatform.downloadJarFile(Path.of(Architecture.Platforms.absolutePath, "vanilla/vanilla-$mcVersion.jar"), mcVersion, build)
            Files.copy(Path.of(Architecture.Platforms.absolutePath, "vanilla/vanilla-$mcVersion.jar"), Path.of(destinationFolder.absolutePathString(), "minecraft_server.$mcVersion.jar"), StandardCopyOption.REPLACE_EXISTING)
        }
        Files.copy(Path.of(Architecture.Platforms.absolutePath, "forge/$mcVersion-$build/forgelauncher-$mcVersion-$build.jar"), Path.of(destinationFolder.absolutePathString(), "forgelauncher-$mcVersion-$build.jar"), StandardCopyOption.REPLACE_EXISTING)
        copyFolder(Path.of(Architecture.Platforms.absolutePath, "forge/$mcVersion-$build/libraries"), Path.of(destinationFolder.absolutePathString(), "libraries"))
        val file = File(Path.of(destinationFolder.absolutePathString(), "forgelauncher.txt").absolutePathString())
        if (!file.exists()) {
            file.createNewFile()
        }
        val list = buildList {
            for (jvmArgument in server.jvmArguments) {
                for (s in jvmArgument.split(" ")) {
                    add(s)
                }
            }
        }
        file.writeText("""
            $mcVersion-$build
            ${server.javaCommand ?: "java"}
            ${list.joinToString(" ")}
        """.trimIndent())
        val launcherMetaVersion = PistonMetaClient.getLauncherMeta().versions.first { it.id == mcVersion }
        if (launcherMetaVersion < MinecraftVersions.RELEASE_1_17) {
            runCatching {
                Files.copy(Path.of(Architecture.Platforms.resolve("forge").absolutePath, "/$mcVersion-$build/forge-$mcVersion-$build.jar"), Path.of(destinationFolder.absolutePathString(), "forge-$mcVersion-$build.jar"), StandardCopyOption.REPLACE_EXISTING)
            }
            runCatching {
                Files.copy(Path.of(Architecture.Platforms.resolve("forge").absolutePath, "/$mcVersion-$build/forge-$mcVersion-$build-$mcVersion.jar"), Path.of(destinationFolder.absolutePathString(), "forge-$mcVersion-$build.jar"), StandardCopyOption.REPLACE_EXISTING)
            }
            runCatching {
                Files.copy(Path.of(Architecture.Platforms.resolve("forge").absolutePath, "/$mcVersion-$build/forge-$mcVersion-$build-$mcVersion-universal.jar"), Path.of(destinationFolder.absolutePathString(), "forge-$mcVersion-$build.jar"), StandardCopyOption.REPLACE_EXISTING)
            }
            runCatching {
                Files.copy(Path.of(Architecture.Platforms.resolve("forge").absolutePath, "/$mcVersion-$build/forge-$mcVersion-$build-universal.jar"), Path.of(destinationFolder.absolutePathString(), "forge-$mcVersion-$build.jar"), StandardCopyOption.REPLACE_EXISTING)
            }
        }
    }
}


