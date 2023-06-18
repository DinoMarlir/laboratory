package tech.marlonr.laboratory.ingamecontrol.velocity.commands

import com.velocitypowered.api.command.SimpleCommand
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.commands.LaboratoryCommand
import me.obsilabor.laboratory.mainScope

class LaboratoryCommand: SimpleCommand {

    override fun execute(invocation: SimpleCommand.Invocation?) {

        if (invocation != null) {
            val source = invocation.source()
            if (source.hasPermission("laboratory.use")) {
                CoroutineScope(Dispatchers.IO).launch {
                    mainScope = this
                    LaboratoryCommand().main(arrayOf("info"))
                }
            }
        }
    }
}