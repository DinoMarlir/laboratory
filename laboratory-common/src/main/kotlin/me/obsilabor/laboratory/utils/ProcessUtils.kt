package me.obsilabor.laboratory.utils

fun isProcessAlive(pid: Long): Boolean {
    return ProcessHandle.of(pid) != null
}

fun killProcess(pid: Long, forcibly: Boolean) {
    ProcessHandle.of(pid).ifPresent {
        if (forcibly) it.destroyForcibly() else it.destroy()
    }
}