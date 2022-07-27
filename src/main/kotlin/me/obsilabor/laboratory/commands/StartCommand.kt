package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.arch.Server
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation
import me.obsilabor.laboratory.terminal.choose

class StartCommand : CliktCommand(
    name = "start",
    help = "Starts the given server"
) {
    private val query by argument(
        "query",
        help = "The id or name of the server to start"
    )

    override fun run() {
        mainScope.launch {
            val resolvedServer: Server
            var servers = JsonDatabase.findServer(query)
            if (servers.isEmpty()) {
                val id = query.toIntOrNull()
                if (id == null) {
                    terminal.println(TextColors.brightRed("No server found."))
                    return@launch
                }
                val server = JsonDatabase.findServer(id)
                if (server == null) {
                    terminal.println(TextColors.brightRed("No server found."))
                    return@launch
                }
                servers = listOf(server)
            }
            if (servers.size > 1) {
                resolvedServer = terminal.choose("Multiple servers found, which one did you mean?", servers.map { it to "${it.name}-${it.id}" }) ?: return@launch
            } else {
                resolvedServer = servers.first()
            }
            terminal.println(TextStyles.italic("Starting server.."))
            resolvedServer.start()
        }
    }
}