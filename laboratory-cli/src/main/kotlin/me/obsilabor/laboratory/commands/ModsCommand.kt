package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.arguments.optional
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.chooseServer

// TODO change pacmc archive version when server version changes but pacmc doesn't support that yet
/**
 * Waiting for new pacmc release....
 */
class ModsCommand : CliktCommand(
    name = "mods",
    help = """
       Manage mods and plugins via ${TextStyles.hyperlink("https://github.com/jakobkmar/pacmc")("pacmc")}
       For more information about add-on management, ${TextStyles.hyperlink("https://laboratory.obsilabor.me/docs/laboratory-misc/manage-addons")("visit the docs")}.
    """
) {
    init {
        subcommands(CreateArchive())
    }

    override fun run() = Unit

    class CreateArchive : CliktCommand(
        name = "create-archive",
        help = "Create an pacmc-archive for use with pacmc"
    ) {
        private val query by argument("query", help = "Name or id of the server").optional()

        override fun run() {
            terminal.println(TextColors.red("This command doesn't have a function right now. Functionality will be added as soon as a new pacmc release supporting the intended features is out."))
            //val server = terminal.chooseServer(query ?: "") ?: return
        }
    }
}