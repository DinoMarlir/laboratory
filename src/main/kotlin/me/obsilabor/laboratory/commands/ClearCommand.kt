package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextColors
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.utils.askYesOrNo

class ClearCommand : CliktCommand(
    name = "clear",
    help = "Deletes all cached files and exits"
) {
    private val yesFlag by option(
        "-y", "--yes",
        help = "When this flag is not, the user will no longer be prompted for any actions"
    ).flag()

    override fun run() {
        if (!terminal.askYesOrNo("This action will delete every cached file. Proceed?", default = true, yesFlag = yesFlag)) {
            terminal.println(TextColors.brightRed("Aborting!"))
            return
        }
        Architecture.Platforms.deleteRecursively()
        terminal.println(TextColors.brightGreen("Cache has been cleared!"))
    }
}