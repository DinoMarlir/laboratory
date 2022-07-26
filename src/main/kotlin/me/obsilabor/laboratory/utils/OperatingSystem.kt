package me.obsilabor.laboratory.utils

enum class OperatingSystem(
    val prefix: String,
    val displayName: String,
    val mojangName: String = prefix,
) {
    LINUX("linux", "Linux"),
    MACOS("mac", "macOS", "osx"),
    WINDOWS("windows", "Microsoft Windows");

    companion object {
        val current = System.getProperty("os.name").lowercase().let { osName ->
            values().singleOrNull { osName.startsWith(it.prefix) }
        }
        val notWindows get() = current != WINDOWS
    }
}