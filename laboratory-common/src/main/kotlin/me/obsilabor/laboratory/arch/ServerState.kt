package me.obsilabor.laboratory.arch

import com.github.ajalt.colormath.model.RGBInt
import com.github.ajalt.mordant.rendering.AnsiLevel
import com.github.ajalt.mordant.rendering.TextColors
import com.github.ajalt.mordant.rendering.TextStyles
import me.obsilabor.laboratory.utils.OperatingSystem

enum class ServerState(val color: RGBInt, val capitalized: String) {
    STARTING(RGBInt((16710583).toUInt()), "Starting"),
    RUNNING(RGBInt((3079042).toUInt()), "Running"),
    STOPPED(RGBInt((13247306).toUInt()), "Stopped");

    val terminalString: String
        get() {
            val color = TextColors.color(color, AnsiLevel.TRUECOLOR)
            return "${color(if (OperatingSystem.notWindows) "●" else TextStyles.bold("${name.first()}"))} ${TextColors.brightWhite(capitalized)}"
        }
}