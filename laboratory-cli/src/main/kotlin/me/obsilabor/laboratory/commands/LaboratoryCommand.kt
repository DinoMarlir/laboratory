package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.mordant.rendering.TextStyles

class LaboratoryCommand : CliktCommand(
    name = "laboratory",
    help = """
        Laboratory is the easiest and most powerful solution for managing minecraft-servers.
        
        ${(TextStyles.bold + TextStyles.underline)("Resources")}${TextStyles.bold(":")}
        ${TextStyles.hyperlink("https://laboratory.obsilabor.me/")("Website")}
        ${TextStyles.hyperlink("https://laboratory.obsilabor.me/docs/intro")("Documentation")}
        ${TextStyles.hyperlink("https://laboratory.obsilabor.me/blog")("Blog")}
        ${TextStyles.hyperlink("https://github.com/mooziii/laboratory")("Source")}
    """
) {
    init {
        subcommands(
            CreateCommand(),
            ClearCommand(),
            StartCommand(),
            ListCommand(),
            TemplateCommand(),
            ServerCommand(),
            InfoCommand(),
            BackupCommand(),
            StopCommand(),
            RestartCommand(),
            ModrinthCommand(),
            ExecuteCommand(),
            AttachCommand(),
            UpdateCommand(),
            ModsCommand()
        )
    }

    override fun aliases(): Map<String, List<String>> {
        return mapOf(
            "rm" to listOf("server", "delete"),
            "del" to listOf("server", "delete"),
            "modify" to listOf("server", "modify"),
            "ls" to listOf("list"),
            "mr" to listOf("modrinth"),
        )
    }

    override fun run() = Unit
}