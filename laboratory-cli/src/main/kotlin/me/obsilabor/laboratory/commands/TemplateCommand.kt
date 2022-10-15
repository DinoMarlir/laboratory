package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.core.subcommands
import com.github.ajalt.clikt.parameters.arguments.argument
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextColors
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation
import me.obsilabor.laboratory.terminal.promptYesOrNo
import java.io.File

class TemplateCommand : CliktCommand(
    name = "template",
    help = "Manage templates"
) {

    init {
        subcommands(Create(), Delete())
    }

    override fun run() = Unit

    class Create : CliktCommand(
        name = "create",
        help = "Creates a new template"
    ) {
        private val templateName by argument("name", help = "Name of the new template")

        override fun run() {
            val file = File(Architecture.Templates, templateName)
            if (!file.exists()) {
                file.mkdir()
                terminal.println(TextColors.brightGreen("Template $templateName created"))
            } else {
                terminal.println(TextColors.yellow("Template $templateName already exists"))
            }
        }
    }

    class Delete : CliktCommand(
        name = "delete",
        help = "Deletes a template"
    ) {
        private val templateName by argument("name", help = "Name of the template to delete")

        private val yesFlag by option(
            "-y", "--yes",
            help = "When this flag is not, the user will no longer be prompted for any actions"
        ).flag()

        override fun run() {
            val file = File(Architecture.Templates, templateName)
            if (file.exists()) {
                if (terminal.promptYesOrNo(TextColors.brightRed("The template $templateName and all its content will be deleted. Are you sure about that?"), default = false, yesFlag = this.yesFlag)) {
                    mainScope.launch {
                        val spinner = SpinnerAnimation("Deleting template $templateName")
                        file.deleteRecursively()
                        spinner.stop("Deletion complete")
                    }
                } else {
                    terminal.println("Aborting.")
                }
            } else {
                terminal.println(TextColors.brightRed("Template $templateName doesn't exists"))
            }
        }
    }
}