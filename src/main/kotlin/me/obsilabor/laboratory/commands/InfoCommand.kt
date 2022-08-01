package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.mordant.rendering.TextColors
import me.obsilabor.laboratory.VERSION
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation

class InfoCommand : CliktCommand(
    name = "info",
    help = "Prints the version of laboratory"
) {
    override fun run() {
        terminal.println("Laboratory version: ${TextColors.brightCyan(VERSION)}")
        val spinner = SpinnerAnimation("Resolving latest version")
        val latest = 
    }
}