package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextStyles
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.arch.ServerState
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.chooseServer

class StopCommand : CliktCommand(
    name = "stop",
    help = "Stops the given server"
) {
    private val query by argument(
        "query",
        help = "The id or name of the server to stop"
    ).optional()

    private val forceFlag by option(
        "-f", "--force",
        help = "If this flag is set, the process will be destroyed"
    ).flag()

    override fun run() {
        mainScope.launch {
            val resolvedServer = terminal.chooseServer(query ?: "") ?: return@launch
            terminal.println(TextStyles.italic("Stopping server.."))
            resolvedServer.stop(forceFlag)
            resolvedServer.state = ServerState.OFFLINE
            JsonDatabase.editServer(resolvedServer)
        }
    }
}