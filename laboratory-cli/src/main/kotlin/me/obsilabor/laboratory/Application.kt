package me.obsilabor.laboratory

import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import com.github.ajalt.mordant.terminal.Terminal
import kotlinx.coroutines.coroutineScope
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.commands.LaboratoryCommand
import me.obsilabor.laboratory.utils.OperatingSystem
import java.awt.Desktop

val terminal = Terminal()

suspend fun main(args: Array<String>) {
    if (System.getProperty("user.name").equals("root", true)) {
        terminal.println(TextColors.brightRed("Do NOT run laboratory as root!"))
        return
    }
    if (!OperatingSystem.notWindows && !Desktop.isDesktopSupported()) {
        terminal.println(TextStyles.bold(TextColors.brightRed("Do NOT run laboratory on a headless windows environment")))
        return
    }
    coroutineScope {
        mainScope = this
        Architecture.setupArchitecture()
        LaboratoryCommand().main(args)
    }
}