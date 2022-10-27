package me.obsilabor.laboratory

import java.io.File
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption

fun main() {
    val laboratoryDir = File(System.getenv("LOCALAPPDATA"), "laboratory")
    val tempDir = File(laboratoryDir, "Temp")
    copyFolder(tempDir.resolve("laboratory-cli-jvm\\bin").toPath(), laboratoryDir.resolve("bin").toPath())
    copyFolder(tempDir.resolve("laboratory-cli-jvm\\lib").toPath(), laboratoryDir.resolve("lib").toPath())
    tempDir.deleteRecursively()
}

private fun copyFolder(source: Path, destination: Path) {
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