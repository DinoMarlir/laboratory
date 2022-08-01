package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.mordant.rendering.TextColors
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.VERSION
import me.obsilabor.laboratory.httpClient
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation

class InfoCommand : CliktCommand(
    name = "info",
    help = "Prints the version of laboratory"
) {
    override fun run() {
        terminal.println("Laboratory version: ${TextColors.brightCyan(VERSION)}")
        mainScope.launch {
            runCatching {
                val latest = httpClient.get("https://raw.githubusercontent.com/mooziii/laboratory/main/.version").bodyAsText()
                terminal.println("Newest version: ${TextColors.brightGreen(latest)}")
                if (latest != VERSION) {
                    terminal.println(TextColors.brightRed("Your version doesn't match the newest one. Please update laboratory."))
                }
            }.onFailure {}
        }
    }
}