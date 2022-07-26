package me.obsilabor.laboratory

import kotlinx.coroutines.coroutineScope
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.commands.LaboratoryCommand

suspend fun main(args: Array<String>) {
    coroutineScope {
        mainScope = this
        Architecture.setupArchitecture()
        LaboratoryCommand().main(args)
    }
}