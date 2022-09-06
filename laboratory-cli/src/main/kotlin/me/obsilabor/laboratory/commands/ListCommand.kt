package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.mordant.rendering.TextColors
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.terminal

class ListCommand : CliktCommand(
    name = "list",
    help = "Lists all servers and exits"
) {
    override fun run() {
        if (JsonDatabase.servers.isEmpty()) {
            terminal.println(TextColors.brightRed("No servers registered, create one using ${TextColors.white("laboratory create")}"))
        } else {
            terminal.println("All registered servers:")
            JsonDatabase.servers.forEach {
                terminal.println("  ${it.terminalString}")
                terminal.println("      ${it.state.terminalString}")
            }
        }
    }
}