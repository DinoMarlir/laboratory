package me.obsilabor.laboratory.arch

import me.obsilabor.laboratory.mainScope
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.platform.PlatformResolver
import java.nio.file.Path
import me.obsilabor.laboratory.config.Config

enum class SchedulableAction(val displayName: String, val executor: (Server) -> Unit) {
    BACKUP("Backup", {
        mainScope.launch {
            it.backup(Path.of(Config.userConfig.folderForAutomaticBackups), false, PlatformResolver.resolvePlatform(it.platform))
        }
    }),
    BACKUP_WORLDS("Backup worlds", {
        mainScope.launch {
            it.backup(Path.of(Config.userConfig.folderForAutomaticBackups), true, PlatformResolver.resolvePlatform(it.platform))
        }
    }),
    RESTART("Restart", {
        mainScope.launch {
            it.stop(false)
            it.start()
        }
    })
}