package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.arch.Server
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.platform.PlatformResolver
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.choose

class StartCommand : CliktCommand(
    name = "start",
    help = "Starts the given server"
) {
    private val query by argument(
        "query",
        help = "The id or name of the server to start"
    ).optional()

    override fun run() {
        mainScope.launch {
            val resolvedServer: Server
            var servers = JsonDatabase.findServer(query ?: "").toMutableSet()
            if (servers.isEmpty()) {
                val id = query?.toIntOrNull()
                if (id == null) {
                    if (JsonDatabase.servers.isEmpty()) {
                        terminal.println(TextColors.brightRed("No server found."))
                        return@launch
                    }
                    servers.addAll(JsonDatabase.servers)
                }
                val server = JsonDatabase.findServer(id ?: 0)
                if (server == null) {
                    if (JsonDatabase.servers.isEmpty()) {
                        terminal.println(TextColors.brightRed("No server found."))
                        return@launch
                    }
                    servers.addAll(JsonDatabase.servers)
                } else {
                    servers = mutableSetOf(server)
                }
            }
            resolvedServer = if (servers.size > 1) {
                terminal.choose("Multiple servers found, which one did you mean?", servers.map {
                    it to it.terminalString
                }) ?: return@launch
            } else {
                servers.first()
            }
            terminal.println(TextStyles.italic("Starting server.."))
            resolvedServer.start()
        }
    }
}