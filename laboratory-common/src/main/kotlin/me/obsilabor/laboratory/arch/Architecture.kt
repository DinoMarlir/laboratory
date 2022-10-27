package me.obsilabor.laboratory.arch

import com.github.ajalt.mordant.rendering.TextColors
import me.obsilabor.laboratory.platform.IPlatform
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.utils.getDirectory
import java.io.File
import java.nio.file.Path
import kotlin.system.exitProcess

object Architecture {

    fun setupArchitecture() {
        Root
        Live
        Servers
        Containers
        Platforms
        Vanilla
        Templates
        Meta
        Backups

        me.obsilabor.laboratory.config.Config
    }

    val Root by lazy { getDirectory(System.getProperty("user.home") + "/laboratory") }
    val Database by lazy { File(Root, "db.json") }
    val Live by lazy { getDirectory(Root, "live") }
    val Servers by lazy { getDirectory(Live, "servers") }
    val Containers by lazy { getDirectory(Root, "containers") }
    val Platforms by lazy { getDirectory(Containers, "platforms") }
    val Vanilla by lazy { getDirectory(Platforms, "vanilla") }
    val Modrinth by lazy { getDirectory(Platforms, "modrinth") }
    val Templates by lazy { getDirectory(Containers, "templates") }
    val Meta by lazy { getDirectory(Containers, "meta") }
    val Backups by lazy { getDirectory(Containers, "backups") }
    val UserConfig by lazy { File(Root, "config.json") }
    val ChemicaeConfig by lazy { File(Root, "chemicae-config.json") }
    val ProxySyncPsFile by lazy { File(Live, "proxysync.ps") }

    suspend fun findOrCreateJar(platform: IPlatform, version: String, build: String): Path {
        val platformFolder = getDirectory(Platforms, platform.name)
        val file = File(platformFolder, platform.jarNamePattern.replace("\$mcVersion", version).replace("\$build", build))
        if (!file.exists()) {
            if (!platform.downloadJarFile(file.toPath(), version, build)) {
                terminal.println(TextColors.red("Download of ${platform.name} failed"))
                exitProcess(0)
            }
        }
        return file.toPath()
    }
}