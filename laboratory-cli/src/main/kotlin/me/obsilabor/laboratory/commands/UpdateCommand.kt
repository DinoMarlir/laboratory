package me.obsilabor.laboratory.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.internal.UpdateManager
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.promptYesOrNo
import me.obsilabor.laboratory.utils.OperatingSystem
import kotlin.system.exitProcess

class UpdateCommand : CliktCommand(
    name = "update",
    help = "Updates laboratory to the newest version"
) {
    private val yesFlag by option(
        "-y", "--yes",
        help = "When this flag is not, the user will no longer be prompted for any actions"
    ).flag()

    override fun run() {
        mainScope.launch {
            runCatching {
                if (UpdateManager.isUpdateAvailable()) {
                    terminal.println(TextColors.yellow("Note: If you installed laboratory through a package-manager, DO NOT RUN this command!"))
                    terminal.println("To learn more about installation and updating, ${TextStyles.hyperlink("https://laboratory.obsilabor.me/docs/intro")("visit the docs")}.")
                    if (!terminal.promptYesOrNo("Laboratory found some updates. Do you want to update now?", default = true, yesFlag = yesFlag)) {
                        terminal.println(TextColors.brightRed("Aborting!"))
                        exitProcess(0)
                    } else {
                        if (OperatingSystem.notWindows) {
                            terminal.println(" ")
                            terminal.println("To update laboratory, you have to enter your root password.")
                            terminal.println("We won't do anything bad with it, pinky promise \uD83E\uDD1E")
                            val sudoPassword = terminal.prompt(
                                "Please enter your sudo password",
                                showDefault = false,
                                showChoices = false,
                                hideInput = true
                            ) ?: return@launch
                            UpdateManager.updateOnLinux(sudoPassword)
                        } else {
                            UpdateManager.updateOnWindows()
                            terminal.println("Update completed.")
                        }
                    }
                }
            }.onFailure {
                terminal.println(TextColors.red("Fetching failed: ${it.message}"))
            }
        }
    }
}