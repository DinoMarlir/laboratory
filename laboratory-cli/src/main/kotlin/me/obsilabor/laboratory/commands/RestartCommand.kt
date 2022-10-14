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

class RestartCommand : CliktCommand(
    name = "restart",
    help = "Restarts the given server"
) {
    private val query by argument(
        "query",
        help = "The id or name of the server to restart"
    ).optional()

    private val forceFlag by option(
        "-f", "--force",
        help = "If this flag is set, the process will be forcibly destroyed"
    ).flag()

    private val attachFlag by option(
        "-a", "--attach",
        help = "If this flag is set, the player will automatically be attached to the screen"
    ).flag()

    override fun run() {
        mainScope.launch {
            withContext(Dispatchers.Default) {
                runBlocking {
                    if (query == "*") {
                        for (server in JsonDatabase.servers) {
                            terminal.println(TextStyles.italic("Stopping server ${server.terminalString}.."))
                            server.stop(forceFlag)
                            server.start()
                        }
                        return@runBlocking
                    }
                    val resolvedServer = terminal.chooseServer(query ?: "") ?: return@runBlocking
                    val spinner = SpinnerAnimation("Stopping server ${resolvedServer.terminalString}..")
                    spinner.start()
                    resolvedServer.stop(forceFlag)
                    spinner.stop("Stopped server ${resolvedServer.terminalString}..")
                    resolvedServer.start(attachFlag)
                }
            }
        }
    }
}