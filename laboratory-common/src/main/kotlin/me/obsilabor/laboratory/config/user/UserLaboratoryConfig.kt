package me.obsilabor.laboratory.config.user

import kotlinx.serialization.Serializable
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.utils.OperatingSystem

@Serializable
data class UserLaboratoryConfig(
    val folderForAutomaticBackups: String,
    val textEditor: String,
    val promptOnMajorUpdates: Boolean,
    val updateBranch: String = "dev/chemicae"
) {
    companion object {
        val DEFAULTS = UserLaboratoryConfig(
            Architecture.Backups.absolutePath,
            if (OperatingSystem.notWindows) "nano" else "notepad.exe",
            true,
            "dev/chemicae"
        )
    }
}
