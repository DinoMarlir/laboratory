package me.obsilabor.laboratory

import com.github.ajalt.mordant.rendering.TextColors
import kotlinx.coroutines.coroutineScope
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.commands.LaboratoryCommand

suspend fun main(args: Array<String>) {
    if (System.getProperty("user.name").equals("root", true)) {
        terminal.println(TextColors.brightRed("Do NOT run laboratory as root!"))
        return
    }
    coroutineScope {
        mainScope = this
        Architecture.setupArchitecture()
        LaboratoryCommand().main(args)
    }
}