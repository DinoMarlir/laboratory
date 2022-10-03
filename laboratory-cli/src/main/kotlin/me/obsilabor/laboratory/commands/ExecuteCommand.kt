package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextStyles
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation
import me.obsilabor.laboratory.terminal.chooseServer

class ExecuteCommand : CliktCommand(
    name = "execute",
    help = "Executes a command on the given server [unix-only]"
) {
    private val query by argument(
        "query",
        help = "The id or name of the server to restart"
    ).optional()

    private val command by argument(
        "command",
        help = "The command to execute"
    )

    override fun run() {
        mainScope.launch {
            withContext(Dispatchers.Default) {
                runBlocking {
                    if (query == "*") {
                        for (server in JsonDatabase.servers) {
                            server.sendCommand(command)
                        }
                        return@runBlocking
                    }
                    val resolvedServer = terminal.chooseServer(query ?: "") ?: return@runBlocking
                    resolvedServer.sendCommand(command)
                }
            }
        }
    }
}