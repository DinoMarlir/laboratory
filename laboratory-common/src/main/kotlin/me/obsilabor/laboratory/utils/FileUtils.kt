package me.obsilabor.laboratory.utils

import io.ktor.util.*
import io.ktor.util.Identity.decode
import io.ktor.util.Identity.encode
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import me.obsilabor.laboratory.data.ModrinthPack
import me.obsilabor.laboratory.json
import me.obsilabor.laboratory.mainScope
import java.io.File
import java.nio.file.FileSystems
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.util.*
import java.util.zip.ZipFile
import kotlin.io.path.absolutePathString
import kotlin.io.path.copyTo
import kotlin.io.path.isDirectory

fun getDirectory(s: String): File {
    val file = File(s)
    if (!file.exists()) {
        file.mkdir()
    }
    return file
}

fun getDirectory(f: File, s: String): File {
    val file = File(f, s)
    if (!file.exists()) {
        file.mkdir()
    }
    return file
}

fun getFile(s: String): File {
    val file = File(s)
    if (!file.exists()) {
        file.createNewFile()
    }
    return file
}

fun getFile(f: File, s: String): File {
    val file = File(f, s)
    if (!file.exists()) {
        file.createNewFile()
    }
    return file
}

fun copyFolder(source: Path, destination: Path) {
    if (!Files.exists(source)) return
    Files.walk(source).forEach {
        runCatching {
            val dest = destination.resolve(source.relativize(it))
            if (Files.isDirectory(dest)) {
                if (!Files.exists(dest)) {
                    Files.createDirectory(dest)
                }
                return@runCatching
            }
            Files.copy(it, dest, StandardCopyOption.REPLACE_EXISTING)
        }.onFailure {
            it.printStackTrace()
        }
    }
}

fun unzipMrPackFile(mrPack: File, destination: Path): ModrinthPack? {
    runCatching {
        val zipFile = ZipFile(mrPack)
        val modrinthPack = json.decodeFromString<ModrinthPack>(zipFile.getInputStream(zipFile.getEntry("modrinth.index.json")).readAllBytes().toString(Charsets.UTF_8))
        val zip4jFile = net.lingala.zip4j.ZipFile(mrPack)
        zip4jFile.extractAll(destination.absolutePathString())
        zip4jFile.close()
        zipFile.close()
        return modrinthPack
    }.onFailure {
        it.printStackTrace()
    }
    return null
}