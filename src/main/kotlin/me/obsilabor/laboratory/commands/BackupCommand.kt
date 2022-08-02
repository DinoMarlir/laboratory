package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.clikt.parameters.options.default
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextColors
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.platform.PlatformResolver
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.chooseServer
import java.nio.file.Path

class BackupCommand : CliktCommand(
    name = "backup",
    help = "Manage your backups"
) {
    private val query by argument("query", help = "Name or id of the server to delete").optional()

    private val worldsOnlyFlag by option(
        "-w", "--worlds-only",
        help = "If this flag is set, only the worlds will be backed-up"
    ).flag()

    private val outputFlag by option(
        "-o", "--output",
        help = "This option defines the output directory."
    ).default(Architecture.Backups.absolutePath)

    override fun run() {
        val server = terminal.chooseServer(query ?: "") ?: return
        if (!server.static) {
            terminal.println(TextColors.brightRed("You cannot backup a non-static server."))
            return
        }
        val platform = PlatformResolver.resolvePlatform(server.platform)
        if (platform.isProxy) {
            terminal.println(TextColors.brightRed("You cannot backup a proxy server."))
            return
        }
        mainScope.launch {
            server.backup(Path.of(outputFlag), worldsOnlyFlag, platform)
        }
    }
}