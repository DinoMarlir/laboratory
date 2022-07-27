package me.obsilabor.laboratory.arch

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.Serializable
import me.obsilabor.laboratory.platform.PlatformResolver
import me.obsilabor.laboratory.utils.getDirectory
import me.obsilabor.laboratory.utils.getFile
import java.io.File
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
    var automaticUpdates: Boolean = true,
    var maxHeapMemory: Long = 1024
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
            val eula = getFile(directory, "eula.txt")
            eula.writeText("""
                # By using laboratory you automatically agree to the Mojang and Laboratory Terms of Service
                eula=true
            """.trimIndent())
            val process = ProcessBuilder("java -Xmx${maxHeapMemory}M", "-jar", "server.jar").directory(directory).redirectErrorStream(true).redirectOutput(
                ProcessBuilder.Redirect.INHERIT)
            process.start()
        }
    }
}
