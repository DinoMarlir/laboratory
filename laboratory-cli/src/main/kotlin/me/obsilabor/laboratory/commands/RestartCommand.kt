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
        help = "If this flag is set, the process will be destroyed"
    ).flag()

    private val attachFlag by option(
        "-a", "--attach",
        help = "If this flag is set, the player will automatically be attached to the screen"
    ).flag()

    override fun run() {
        mainScope.launch {
            withContext(Dispatchers.Default) {
                runBlocking {
                    val resolvedServer = terminal.chooseServer(query ?: "")
                    val spinner = SpinnerAnimation("Stopping server")
                    spinner.start()
                    resolvedServer?.stop(forceFlag)
                    spinner.stop("Stopped server")
                    resolvedServer?.start(attachFlag)
                }
            }
        }
    }
}