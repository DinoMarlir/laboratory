package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.types.choice
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.platform.impl.PaperPlatform
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation
import kotlin.io.path.absolute

class CreateCommand : CliktCommand(
    name = "create",
    help = "Creates a new server"
) {
    private val software by argument(
        "software",
        help = "The server-software that should be used for the new server"
    ).choice(ignoreCase = true, choices = arrayOf("papermc"))

    private val version by argument(
        "version",
        help = "The version of minecraft to use for this server"
    ).default("latest")

    private val build by argument(
        "build",
        help = "The build of your chosen software to use for this server"
    ).default("latest")

    override fun run() {
        mainScope.launch {
            val platform = when(software) {
                "papermc" -> PaperPlatform
                else -> throw RuntimeException("Unknown platform $software")
            }
            val spinner = SpinnerAnimation("Resolving minecraft versions for ${platform.name}")
            spinner.start()
            var chosenVersion = version
            if (chosenVersion == "latest") {
                chosenVersion = platform.getMcVersions().last()
            }
            var chosenBuild = build
            if (chosenBuild == "latest") {
                spinner.update("Resolving ${platform.name} builds")
                chosenBuild = platform.getBuilds(chosenVersion).last()
            }
            spinner.stop()
            val jarFile = Architecture.findOrCreateJar(platform, chosenVersion, chosenBuild)
            terminal.println(jarFile.absolute())
        }
    }
}