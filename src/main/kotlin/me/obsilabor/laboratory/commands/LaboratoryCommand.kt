package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands

class LaboratoryCommand : CliktCommand(
    name = "laboratory",
    help = "The root command of laboratory"
) {
    init {
        subcommands(
            CreateCommand(),
            ClearCommand(),
            StartCommand(),
            ListCommand(),
            TemplateCommand(),
            ServerCommand()
        )
    }

    override fun run() = Unit
}