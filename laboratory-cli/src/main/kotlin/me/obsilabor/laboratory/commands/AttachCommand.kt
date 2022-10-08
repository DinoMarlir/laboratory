package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.mordant.rendering.TextColors
import me.obsilabor.laboratory.terminal.chooseServer
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.utils.OperatingSystem

class AttachCommand : CliktCommand(
    name = "attach",
    help = "Attach to the console of a server [unix-only]"
) {
    private val query by argument("query", help = "Name or id of the server to modify").optional()

    override fun run() {
        if (OperatingSystem.notWindows) {
            val server = terminal.chooseServer(query ?: "") ?: return
            val process = ProcessBuilder("screen", "-dr", "${server.name}-${server.id}").inheritIO().start()
            process.waitFor()
        } else {
            terminal.println(TextColors.red("This feature is lacking windows support. Sorry :/"))
        }
    }
}