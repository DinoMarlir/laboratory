package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.default
import com.github.ajalt.clikt.parameters.types.choice
import com.github.ajalt.mordant.rendering.TextColors
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.arch.Server
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.platform.PlatformResolver
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation
import me.obsilabor.laboratory.terminal.promptYesOrNo
import java.util.Random

class CreateCommand : CliktCommand(
    name = "create",
    help = "Creates a new server"
) {
    private val name by argument(
        "name",
        help = "The name of this server"
    )

    private val software by argument(
        "software",
        help = "The server-software that should be used for the new server"
    ).choice(ignoreCase = true, choices = arrayOf("papermc", "quiltmc", "fabricmc", "graphite"))

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
            val platform = PlatformResolver.resolvePlatform(software)
            var spinner = SpinnerAnimation("Resolving minecraft versions for ${platform.name}")
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
            spinner.stop("Resolved versions")
            terminal.println("We're going with the default server configuration for this server. You can modify it later.")
            val server = Server(
                Random().nextInt(700000), // TODO: lookup database to avoid duplicates
                name,
                true,
                true,
                emptyList(),
                software,
                chosenBuild,
                chosenVersion,
                true,
                1024,
                emptyList(),
                listOf("nogui")
            )
            spinner = SpinnerAnimation("Saving server configuration to database")
            spinner.start()
            JsonDatabase.registerServer(server)
            spinner.stop("Saved server configuration to database")
            if (!terminal.promptYesOrNo("Should the server be automatically started now?", true)) {
                terminal.println(TextColors.brightGreen("Server setup complete"))
            } else {
                server.start()
            }
        }
    }
}