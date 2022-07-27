package me.obsilabor.laboratory.terminal

import com.github.ajalt.mordant.terminal.Terminal
import me.obsilabor.laboratory.terminal

fun Terminal.promptYesOrNo(question: String, default: Boolean? = null, yesFlag: Boolean = false): Boolean {
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

/**
 * @return memory in megabyte
 */
fun Terminal.awaitMemoryInput(
    message: String,
    default: String = "1024M"
): Long {
    val input = prompt("$message (Default: $default)") ?: default
    var amount = input.replace("M", "").replace("G", "").toLong()
    if (input.endsWith("G")) {
        amount *= 1024L
    }
    return amount
}