package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.mordant.rendering.TextColors
import kotlinx.coroutines.*
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.arch.Server
import me.obsilabor.laboratory.arch.ServerState
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.platform.PlatformResolver
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation
import me.obsilabor.laboratory.terminal.promptYesOrNo
import me.obsilabor.laboratory.utils.copyFolder
import me.obsilabor.laboratory.utils.downloadFile
import me.obsilabor.laboratory.utils.unzipMrPackFile
import java.awt.Desktop
import java.io.File
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import java.nio.file.Files
import java.util.*

class ModrinthCommand : CliktCommand(
    name = "modrinth",
    help = "Import modpacks directly from modrinth"
) {
    private val file by argument(
        "file",
        help = "The path or url pointing to the file"
    )

    override fun run() {
        mainScope.launch {
            if (!file.endsWith(".mrpack")) {
                return@launch
            }
            val dotMrpack: File
            val container: File
            if (file.startsWith("http://") || file.startsWith("https://")) {
                val fileName = file.split("/").last()
                container = File(Architecture.Modrinth, fileName.removeSuffix(".mrpack"))
                if (!container.exists()) {
                    container.mkdir()
                }
                dotMrpack = File(container, fileName)
                downloadFile(file, dotMrpack.toPath())
            } else {
                dotMrpack = File(file)
                if (!dotMrpack.exists()) {
                    terminal.println(TextColors.red("File not found"))
                    return@launch
                } else {
                    val fileName = file.split("/").last()
                    container = File(Architecture.Modrinth, fileName.removeSuffix(".mrpack"))
                    if (!container.exists()) {
                        container.mkdir()
                    }
                    Files.copy(dotMrpack.toPath(), File(container, fileName).toPath())
                }
            }
            val spinner = SpinnerAnimation("Extracting archive")
            spinner.start()
            val modrinthPack = unzipMrPackFile(dotMrpack, container.toPath())
            if (modrinthPack == null) {
                spinner.fail("Extraction failed")
                return@launch
            }
            var loader: String? = null
            var loaderVersion: String? = null
            if (modrinthPack.dependencies.fabricLoader != null) {
                loader = "fabricmc"
                loaderVersion = modrinthPack.dependencies.fabricLoader
            } else if (modrinthPack.dependencies.quiltLoader != null) {
                loader = "quiltmc"
                loaderVersion = modrinthPack.dependencies.quiltLoader
            }else if (modrinthPack.dependencies.forge != null) {
                loader = "forge"
                loaderVersion = modrinthPack.dependencies.forge
            }
            val db = JsonDatabase.db
            val server = Server(
                db.internalCounter,
                modrinthPack.name.replace("%20", "-"),
                true,
                true,
                mutableSetOf(),
                loader ?: return@launch,
                loaderVersion ?: return@launch,
                modrinthPack.dependencies.minecraft,
                false,
                4096,
                mutableSetOf("-Dlog4j2.formatMsgNoLookups=true"),
                if (!Desktop.isDesktopSupported()) mutableSetOf("nogui") else mutableSetOf(),
                25565,
                true,
                true,
                "java",
            )
            db.internalCounter++
            JsonDatabase.writeFile(db)
            spinner.update("Saving server configuration to database")
            val platform = PlatformResolver.resolvePlatform(loader)
            platform.modsFolder?.let {
                val file = File(server.directory, it)
                if (!file.exists()) {
                    file.mkdirs()
                }
            }
            JsonDatabase.registerServer(server)
            var totalCount = modrinthPack.files.size
            var count = 0
            spinner.update("Downloading mods: 0/$totalCount")
            for (file in modrinthPack.files) {
                if (file.path.startsWith("/") || file.path.contains(":/") || file.path.contains(":\\") || file.path.contains("..")) {
                    terminal.println(TextColors.red("Path of file '${file.hashes.sha1}' contains illegal characters, skipping"))
                    totalCount--
                    continue
                }
                val url = URLDecoder.decode(file.downloads.first(), StandardCharsets.UTF_8)
                mainScope.launch {
                    downloadFile(url, server.directory.toPath().resolve(file.path), true) {
                        count ++
                        spinner.update("Downloading mods: $count/$totalCount")
                    }
                }
            }
            while (count != totalCount) {
                buildString {
                    append(count)
                }
                // In case someone wants to know what this while loop is for,
                // I have to wait until all files are downloaded so I added this loop
                // But because something is dumb, the while loop only notices that the condition is false
                // when it gets printed or appended.

                // If you know why this happens or if anyone has a solution, please open a issue or pull request
            }
            spinner.stop("Download complete")
            terminal.println("Checking $count mods")
            for (file in modrinthPack.files) {
                if (file.path.startsWith("/") || file.path.contains(":/") || file.path.contains(":\\") || file.path.contains("..")) {
                    continue
                }
                val path = server.directory.toPath().resolve(file.path)
                if(Files.size(path) != file.fileSize) {
                    terminal.println(TextColors.brightRed("File did not pass the check, downloading it again"))
                    downloadFile(file.downloads.first(), path)
                }
            }
            copyFolder(container.toPath().resolve("overrides"), server.directory.toPath())
            copyFolder(container.toPath().resolve("server-overrides"), server.directory.toPath())
            if (server.state != ServerState.RUNNING) {
                if (!terminal.promptYesOrNo("Should the server be automatically started now?", true)) {
                    terminal.println(TextColors.brightGreen("Server setup complete"))
                } else {
                    server.start()
                }
            }
        }
    }
}