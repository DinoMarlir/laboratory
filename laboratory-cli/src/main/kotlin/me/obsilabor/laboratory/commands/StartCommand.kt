package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextStyles
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.chooseServer
import me.obsilabor.laboratory.terminal.chooseServerHeadless

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
        help = "If this flag is set, the user will automatically be attached to the screen"
    ).flag()

    private val shellFlag by option(
        "-s", "--shell",
        help = "If this flag is set, the command will be launched in a non-interactive mode"
    ).flag()

    override fun run() {
        mainScope.launch {
            if (query == "*") {
                for (server in JsonDatabase.servers) {
                    terminal.println(TextStyles.italic("Starting server ${server.terminalString}.."))
                    server.start(attach = attachFlag, noScreen = shellFlag)
                }
                return@launch
            }
            val resolvedServer = if (!shellFlag) {
                terminal.chooseServer(query ?: "") ?: return@launch
            } else {
                terminal.chooseServerHeadless(query ?: "") ?: return@launch
            }
            if (!shellFlag) {
                terminal.println(TextStyles.italic("Starting server ${resolvedServer.terminalString}.."))
            }
            resolvedServer.start(attach = attachFlag, disableIO = shellFlag)
        }
    }
}