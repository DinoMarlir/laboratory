package me.obsilabor.laboratory.utils

import java.io.File

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