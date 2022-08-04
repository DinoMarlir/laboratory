package me.obsilabor.laboratory.config

import kotlinx.serialization.Serializable
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.utils.OperatingSystem

@Serializable
data class LaboratoryConfig(
    val folderForAutomaticBackups: String,
    val textEditor: String
) {
    companion object {
        val DEFAULTS = LaboratoryConfig(
            Architecture.Backups.absolutePath,
            if (OperatingSystem.notWindows) "nano" else "notepad.exe",
        )
    }
}
