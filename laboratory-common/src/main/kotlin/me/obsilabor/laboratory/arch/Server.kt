package me.obsilabor.laboratory.arch

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import kotlinx.coroutines.*
import kotlinx.serialization.Serializable
import me.obsilabor.laboratory.DATE_FORMAT
import me.obsilabor.laboratory.TIME_FORMAT
import me.obsilabor.laboratory.config.Config
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.platform.IPlatform
import me.obsilabor.laboratory.platform.PlatformResolver
import me.obsilabor.laboratory.platform.impl.PaperPlatform
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation
import me.obsilabor.laboratory.terminal.promptYesOrNo
import me.obsilabor.laboratory.utils.*
import java.awt.Dimension
import java.awt.event.WindowAdapter
import java.awt.event.WindowEvent
import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.Instant
import javax.swing.JFrame
import javax.swing.JLabel
import javax.swing.WindowConstants

@Serializable
data class Server(
    val id: Int,
    var name: String,
    var static: Boolean,
    var copyTemplates: Boolean,
    val templates: MutableSet<String>,
    var platform: String,
    var platformBuild: String,
    var mcVersion: String,
    var automaticUpdates: Boolean,
    var maxHeapMemory: Long,
    var jvmArguments: MutableSet<String>,
    var processArguments: MutableSet<String>,
    var port: Int? = 25565,
    var initialStart: Boolean? = true,
    var backupOnUpdate: Boolean? = true,
    var javaCommand: String? = "java",
    var pid: Long? = null,
    var automaticRestarts: Boolean? = true,
    var state: ServerState = ServerState.STOPPED,
    var scheduledTasks: List<ScheduledTask> = emptyList()
) {
    val terminalString: String
        get() = "${TextStyles.bold(PlatformResolver.resolvePlatform(platform).coloredName)}${TextColors.white("/")}${TextStyles.bold("${TextColors.brightWhite("$name-$id ")}${TextColors.green("$mcVersion-$platformBuild")}")}"

    val directory by lazy { getDirectory(Architecture.Servers, "$name-$id") }

    suspend fun start(attach: Boolean = false, disableIO: Boolean = false) {
        withContext(Dispatchers.IO) {
            state = ServerState.STARTING
            JsonDatabase.editServer(this@Server)
            if (!static) {
                directory.deleteRecursively()
                directory.mkdir()
            }
            if (initialStart == true || !static) {
                val serverDashIcon = File(Architecture.Meta, "server-icon.png").toPath()
                val serverDotProperties = File(Architecture.Meta, "server.properties").toPath()
                if (!Files.exists(serverDashIcon)) {
                    downloadFile("https://github.com/mooziii/laboratory/raw/main/.meta/server-icon.png", Path.of(Architecture.Meta.absolutePath, "server-icon.png"))
                }
                if (!Files.exists(serverDotProperties)) {
                    downloadFile("https://github.com/mooziii/laboratory/raw/main/.meta/server.properties", Path.of(Architecture.Meta.absolutePath, "server.properties"))
                }
                val iconPath = Path.of(directory.absolutePath, "server-icon.png")
                if (!Files.exists(iconPath)) {
                    Files.copy(serverDashIcon, iconPath)
                }
                val propertiesPath = Path.of(directory.absolutePath, "server.properties")
                if (!Files.exists(propertiesPath)) {
                    Files.copy(serverDotProperties, propertiesPath)
                }
            }
            val resolvedPlatform = PlatformResolver.resolvePlatform(platform)
            if (automaticUpdates) {
                update(resolvedPlatform, !Config.userConfig.promptOnMajorUpdates)
            }
            if (initialStart == true) {
                initialStart = false
                JsonDatabase.editServer(this@Server)
            }
            if (!static || copyTemplates) {
                templates.forEach {
                    copyFolder(Path.of(Architecture.Templates.absolutePath, it), Path.of(directory.absolutePath))
                }
            }
            val jar = Architecture.findOrCreateJar(resolvedPlatform, mcVersion, platformBuild)
            Files.copy(jar, Path.of(directory.absolutePath, "server.jar"), StandardCopyOption.REPLACE_EXISTING)
            resolvedPlatform.copyOtherFiles(Path.of(directory.absolutePath), mcVersion, platformBuild, this@Server)
            val spinner = SpinnerAnimation("Accepting mojang EULA")
            spinner.start()
            val eula = getFile(directory, "eula.txt")
            eula.writeText("""
                # By using laboratory you automatically agree to the Mojang and Laboratory Terms of Service
                eula=true
            """.trimIndent())
            spinner.stop()
        }
        state = ServerState.RUNNING
        JsonDatabase.editServer(this)
        if (OperatingSystem.notWindows) {
            val args = arrayListOf(
                "screen",
                "${if(!attach) "-dm" else "-"}S",
                "$name-$id",
                javaCommand,
                "-Xmx${maxHeapMemory}M",
            )
            for (jvmArgument in jvmArguments) {
                args.addAll(jvmArgument.split(" "))
            }
            args.add("-jar")
            args.add("server.jar")
            args.add("--port")
            args.add("$port")
            args.addAll(processArguments)
            val processBuilder = ProcessBuilder(args).directory(directory)
            if (!attach) {
                val process = processBuilder.start()
                val pid = process.pid()+2 // don't ask, pid of the process is actually 2 numbers higher
                terminal.println("Server is now running with PID $pid. Attach using ${TextStyles.dim(TextColors.brightWhite("screen -dr $name-$id"))}")
                this.pid = pid
                JsonDatabase.editServer(this)
            } else {
                withContext(Dispatchers.Default) {
                    runBlocking {
                        val process = processBuilder.redirectErrorStream(true).redirectError(ProcessBuilder.Redirect.INHERIT).redirectInput(
                            ProcessBuilder.Redirect.INHERIT).redirectOutput(ProcessBuilder.Redirect.INHERIT).start()
                        this@Server.pid = process.pid()+2
                        JsonDatabase.editServer(this@Server)
                        process.waitFor()
                    }
                }
            }
        } else {
            withContext(Dispatchers.Default) {
                runBlocking {
                    val args = arrayListOf(
                        javaCommand,
                        "-Xmx${maxHeapMemory}M",
                    )
                    args.addAll(jvmArguments)
                    args.add("-jar")
                    args.add("server.jar")
                    args.add("--port")
                    args.add("$port")
                    args.addAll(processArguments)
                    val processBuilder = ProcessBuilder(args)
                        .directory(directory)
                    if (!disableIO) {
                        processBuilder.redirectErrorStream(true).redirectOutput(ProcessBuilder.Redirect.INHERIT).redirectInput(ProcessBuilder.Redirect.INHERIT)
                    }
                    val process = processBuilder.start()
                    this@Server.pid = process.pid()+2
                    JsonDatabase.editServer(this@Server)
                    process.waitFor()
                }
            }
        }
    }

    suspend fun update(platform: IPlatform, noConfirm: Boolean = false) {
        val spinner = SpinnerAnimation("Resolving latest ${platform.name} build")
        spinner.start()
        val newestMcVersion = platform.getMcVersions().last()
        spinner.stop("resolved")
        var updated = false
        if (mcVersion != newestMcVersion) {
            if (terminal.promptYesOrNo("Updating the server will update to a new minecraft version. Is this okay?", true, yesFlag = noConfirm)) {
                mcVersion = newestMcVersion
                updated = true
            }
        }
        val newestPlatformBuild = platform.getBuilds(mcVersion).last()
        if (newestPlatformBuild != platformBuild) {
            updated = true
            platformBuild = newestPlatformBuild
        }
        if (updated && backupOnUpdate == true && static) {
            backup(Path.of(Config.userConfig.folderForAutomaticBackups), !platform.isProxy, platform)
        }
        if (updated) {
            spinner.update("Updating..")
            spinner.start()
            JsonDatabase.editServer(this@Server)
            spinner.stop("Updated your server to ${platform.name}-$mcVersion-$platformBuild")
        } else {
            terminal.println("Server is up to date!")
        }
    }

    suspend fun backup(output: Path, worldsOnly: Boolean, platform: IPlatform) {
        if (initialStart == true) return
        val spinner = SpinnerAnimation("Creating a backup of $terminalString")
        spinner.start()
        var outputFolder = output.resolve("$name-$mcVersion-$platformBuild-${DATE_FORMAT.format(Instant.now()).split("+")[0]}")
        if (!Files.exists(outputFolder)) {
            Files.createDirectory(outputFolder)
        }
        outputFolder = outputFolder.resolve(TIME_FORMAT.format(Instant.now()).replace(":", "-").split(".")[0])
        if (!Files.exists(outputFolder)) {
            Files.createDirectory(outputFolder)
        }
        runCatching {
            if (worldsOnly) {
                copyFolder(directory.toPath().resolve("world"), outputFolder.resolve("world"))
                if (platform == PaperPlatform) {
                    copyFolder(directory.toPath().resolve("world_nether"), outputFolder.resolve("world_nether"))
                    copyFolder(directory.toPath().resolve("world_the_end"), outputFolder.resolve("world_the_end"))
                }
            } else {
                copyFolder(directory.toPath(), outputFolder)
            }
            spinner.stop("Backup completed")
        }.onFailure {
            spinner.stop(TextColors.brightRed("Backup failed"))
        }
    }

    fun stop(forcibly: Boolean) {
        if (isAlive) {
            state = ServerState.RUNNING
            JsonDatabase.editServer(this)
            killProcess(pid ?: return, forcibly)
        }
    }


    val isAlive: Boolean
        get() {
            return isProcessAlive(pid ?: return false)
        }
}
