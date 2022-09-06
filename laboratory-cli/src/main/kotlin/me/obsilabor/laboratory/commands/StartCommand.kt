package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextStyles
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.chooseServer

class StartCommand : CliktCommand(
    name = "start",
    help = "Starts the given server"
) {
    private val query by argument(
        "query",
        help = "The id or name of the server to start"
    ).optional()

    private val attachFlag by option(
        "-a", "--attach",
        help = "If this flag is set, the player will automatically be attached to the screen"
    ).flag()

    private val experimentalFlag by option(
        "--enableExperimentalWindowsSupport", "-XW",
        help = "If this flag is set, experimental support for windows will be enabled"
    ).flag()

    override fun run() {
        mainScope.launch {
            val resolvedServer = terminal.chooseServer(query ?: "")
            terminal.println(TextStyles.italic("Starting server.."))
            resolvedServer?.start(attach = attachFlag, experimentalWindowsSupport = experimentalFlag)
        }
    }
}