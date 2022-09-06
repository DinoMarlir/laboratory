package me.obsilabor.laboratory.utils

import kotlin.jvm.optionals.getOrNull

@OptIn(ExperimentalStdlibApi::class)
fun isProcessAlive(pid: Long): Boolean {
    return ProcessHandle.of(pid)?.getOrNull()?.isAlive ?: false
}

fun killProcess(pid: Long, forcibly: Boolean) {
    ProcessHandle.of(pid).ifPresent {
        if (forcibly) it.destroyForcibly() else it.destroy()
    }
}