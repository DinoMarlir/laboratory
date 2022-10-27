package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.VERSION
import me.obsilabor.laboratory.internal.UpdateManager
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.terminal

class InfoCommand : CliktCommand(
    name = "info",
    help = "Prints the version of laboratory"
) {
    override fun run() {
        terminal.println("Laboratory version: ${TextColors.brightCyan(VERSION)}")
        mainScope.launch {
            runCatching {
                terminal.println("Newest version: ${TextColors.brightGreen(UpdateManager.getLatestVersion())}")
                if (UpdateManager.isUpdateAvailable()) {
                    terminal.println(TextColors.brightRed("Your version doesn't match the newest one. Please update laboratory using ${(TextColors.brightWhite on TextColors.gray)(
                        TextStyles.italic("laboratory update"))}."))
                }
            }.onFailure {
                terminal.println(TextColors.red("Fetching failed: ${it.message}"))
            }
        }
    }
}