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
import java.io.File
import java.io.FileOutputStream
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.time.Instant
import kotlin.system.exitProcess

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

    suspend fun start(attach: Boolean = false, disableIO: Boolean = false, noScreen: Boolean = false) {
        withContext(Dispatchers.IO) {
            state = ServerState.STARTING
            JsonDatabase.editServer(this@Server)
            if (!static) {
                directory.deleteRecursively()
                directory.mkdir()
            }
            val resolvedPlatform = PlatformResolver.resolvePlatform(platform)
            if (initialStart == true || !static) {
                val serverDashIcon = File(Architecture.Meta, "server-icon.png").toPath()
                val serverDotProperties = File(Architecture.Meta, "server.properties").toPath()
                if (!Files.exists(serverDashIcon)) {
                    downloadFile("https://github.com/mooziii/laboratory/raw/${Config.userConfig.updateBranch}/.meta/server-icon.png", Path.of(Architecture.Meta.absolutePath, "server-icon.png"))
                }
                if (!Files.exists(serverDotProperties) && !resolvedPlatform.isProxy) {
                    downloadFile("https://github.com/mooziii/laboratory/raw/${Config.userConfig.updateBranch}/.meta/server.properties", Path.of(Architecture.Meta.absolutePath, "server.properties"))
                }
                val iconPath = Path.of(directory.absolutePath, "server-icon.png")
                if (!Files.exists(iconPath)) {
                    Files.copy(serverDashIcon, iconPath)
                }
                val propertiesPath = Path.of(directory.absolutePath, "server.properties")
                if (!Files.exists(propertiesPath) && !resolvedPlatform.isProxy) {
                    Files.copy(serverDotProperties, propertiesPath)
                }
                val spigotYml = Path.of(directory.absolutePath, "spigot.yml").toFile()
                if (!spigotYml.exists()) {
                    spigotYml.createNewFile()
                }
                spigotYml.writeText("""
                    settings:
                      restart-script: ./restart.sh
                """.trimIndent())
                val restartSh = Path.of(directory.absolutePath,"restart.sh").toFile()
                if (!restartSh.exists()) {
                    restartSh.createNewFile()
                }
                restartSh.writeText("""
                    laboratory start $name-$id -s
                """.trimIndent())
                restartSh.setExecutable(true)
            }
            if (automaticUpdates) {
                update(resolvedPlatform, if(disableIO || noScreen) true else !Config.userConfig.promptOnMajorUpdates)
            }
            if (initialStart == true) {
                initialStart = false
                JsonDatabase.editServer(this@Server)
            }
            if (!static || copyTemplates) {
                templates.forEach {
                    copyFolder(Path.of(Architecture.Templates.absolutePath, it), Path.of(directory.absolutePath))
                }
                if (resolvedPlatform.isProxy) {
                    val pluginsFolder = directory.resolve("plugins")
                    val file = pluginsFolder.resolve("laboratory-proxy-sync.jar")
                    if (!file.exists()) {
                        file.createNewFile()
                    }
                    downloadFileV2("https://github.com/mooziii/laboratory/raw/${Config.userConfig.updateBranch}/.meta/plugins/laboratory-proxy-sync.jar", file.toPath())
                }
            }
            val jar = Architecture.findOrCreateJar(resolvedPlatform, mcVersion, platformBuild)
            Files.copy(jar, Path.of(directory.absolutePath, "server.jar"), StandardCopyOption.REPLACE_EXISTING)
            resolvedPlatform.copyOtherFiles(Path.of(directory.absolutePath), mcVersion, platformBuild, this@Server)
            if (!Config.userConfig.acceptedEULA) {
                if (!terminal.promptYesOrNo("Do you agree the Minecraft EULA? https://www.minecraft.net/en-us/eula") || disableIO || noScreen) {
                    terminal.println(TextColors.red("You need to agree to the Minecraft EULA in order to start a server!"))
                    exitProcess(0)
                } else {
                    val cfg = Config.userConfig
                    cfg.acceptedEULA = true
                    Config.writeUserFile(cfg)
                    if (!resolvedPlatform.isProxy) {
                        val eula = getFile(directory, "eula.txt")
                        eula.writeText("eula=true")
                    }
                }
            } else {
                if (!resolvedPlatform.isProxy) {
                    val eula = getFile(directory, "eula.txt")
                    eula.writeText("eula=true")
                }
            }
        }
        state = ServerState.RUNNING
        JsonDatabase.editServer(this)
        if (!PlatformResolver.resolvePlatform(platform).isProxy) {
            Architecture.ProxySyncPsFile.writeText("REGISTER $name-$id $port")
        }
        if (OperatingSystem.notWindows) {
            val args = buildList {
                if (!noScreen) {
                    add("screen")
                    add("${if(!attach) "-dm" else "-"}S")
                    add("$name-$id")
                }
                add(javaCommand ?: "java")
                add("-Xmx${maxHeapMemory}M")
                add("-Dlaboratory.server=$name-$id")
            }.toMutableList()
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
                if (!noScreen) {
                    val process = processBuilder.start()
                    val pid = process.pid()+2 // don't ask, pid of the process is actually 2 numbers higher
                    this.pid = pid
                    JsonDatabase.editServer(this)
                    terminal.println("Server is now running with PID $pid. Attach using ${(TextColors.brightWhite on TextColors.gray)(TextStyles.italic("screen -dr $name-$id"))}")
                } else {
                    val process = processBuilder.inheritIO().start()
                    val pid = process.pid()+2 // don't ask, pid of the process is actually 2 numbers higher
                    this.pid = pid
                    JsonDatabase.editServer(this)
                    process.waitFor()
                }
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
                        "-Dlaboratory.server=$name-$id"
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
            state = ServerState.STOPPED
            JsonDatabase.editServer(this)
            Architecture.ProxySyncPsFile.writeText("UNREGISTER $name-$id")
            killProcess(pid ?: return, forcibly)
        }
    }


    val isAlive: Boolean
        get() {
            return isProcessAlive(pid ?: return false)
        }

    fun sendCommand(command: String) {
        if (OperatingSystem.notWindows) {
            if (isAlive) {
                ProcessBuilder("screen", "-S", "$name-$id", "-p", "0", "-X", "stuff", "$command\n").start()
                terminal.println("Sent command ${(TextColors.brightWhite on TextColors.gray)(TextStyles.italic("$command"))} to server $terminalString")
            } else {
                terminal.println(TextColors.red("The server $terminalString must be running"))
            }
        } else {
            terminal.println(TextColors.red("This feature is lacking windows support. Sorry :/"))
        }
    }
}
