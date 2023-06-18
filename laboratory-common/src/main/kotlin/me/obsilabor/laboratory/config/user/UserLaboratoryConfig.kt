package me.obsilabor.laboratory.config.user

import kotlinx.serialization.Serializable
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.utils.OperatingSystem

@Serializable
data class UserLaboratoryConfig(
    val folderForAutomaticBackups: String,
    val textEditor: String,
    val promptOnMajorUpdates: Boolean,
    val updateBranch: String = "dev/reborn",
    var acceptedEULA: Boolean = false
) {
    companion object {
        val DEFAULTS = UserLaboratoryConfig(
            Architecture.Backups.absolutePath,
            if (OperatingSystem.notWindows) "vi" else "notepad.exe",
            true,
            "dev/chemicae",
            false
        )
    }
}
