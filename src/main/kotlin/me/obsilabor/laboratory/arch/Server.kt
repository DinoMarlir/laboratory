package me.obsilabor.laboratory.arch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import me.obsilabor.laboratory.platform.PlatformResolver
import me.obsilabor.laboratory.utils.getDirectory
import java.nio.file.Files
import java.nio.file.Path

@Serializable
data class Server(
    val id: Int,
    var name: String,
    var static: Boolean = true,
    var copyTemplates: Boolean = true,
    val templates: List<String> = emptyList(),
    var platform: String,
    var platformBuild: String,
    var mcVersion: String,
    var automaticUpdates: Boolean = true
) {

    val directory by lazy { getDirectory(Architecture.Servers, "$name-$id") }

    suspend fun start() {
        withContext(Dispatchers.IO) {
            if (!static) {
                directory.deleteRecursively()
                directory.mkdir()
            }
            val jar = Architecture.findOrCreateJar(PlatformResolver.resolvePlatform(platform), mcVersion, platformBuild)
            Files.copy(jar, Path.of(directory.absolutePath, "server.jar"))
        }
    }
}
