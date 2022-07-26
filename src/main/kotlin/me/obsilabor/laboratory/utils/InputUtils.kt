package me.obsilabor.laboratory.utils

import com.github.ajalt.mordant.terminal.Terminal

fun Terminal.askYesOrNo(question: String, default: Boolean? = null, yesFlag: Boolean = false): Boolean {
    val keyString = if (default == null) "(y/n)" else (if (default) "(Y/n)" else "(y/N)")
    while (true) {
        print("$question $keyString ")
        if (yesFlag) {
            println("yes")
            return true
        }
        return when (readlnOrNull()?.trim()?.lowercase()) {
            "n", "no" -> false
            "y", "yes" -> true
            "" -> {
                if (default == null) continue else {
                    cursor.move { up(1) }
                    println("$question $keyString ${if (default) "yes" else "no"}")
                    default
                }
            }
            null -> {
                println()
                false
            }
            else -> continue
        }
    }
}