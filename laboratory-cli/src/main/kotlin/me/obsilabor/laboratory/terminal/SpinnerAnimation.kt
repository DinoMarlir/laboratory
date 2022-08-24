package me.obsilabor.laboratory.terminal

import kotlinx.atomicfu.atomic
import kotlinx.atomicfu.update
import kotlinx.coroutines.*
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.utils.OperatingSystem

/**
 * @author jakobkmar (code from <a href="https://github.com/jakobkmar/pacmc">pacmc</a>)
 */
class SpinnerAnimation(val message: String = "resolving") : CoroutineScope {
    override val coroutineContext = Dispatchers.Default

    private val currentMessage = atomic(message)

    private val spinJob = launch(start = CoroutineStart.LAZY) {
        val symbols = listOf('|', '/', '-', '\\')
        var index = 0
        while (isActive) {
            terminal.print("[${symbols[index]}] ")
            terminal.cursor.move { clearLineAfterCursor() }
            terminal.print(currentMessage.value)
            terminal.cursor.move { startOfLine() }
            index = if (index < symbols.lastIndex) index + 1 else 0
            delay(100)
        }
    }

    fun start() {
        spinJob.start()
    }

    suspend fun stop(message: String = "done") {
        spinJob.cancelAndJoin()
        terminal.cursor.move { startOfLine() }
        terminal.print("[${if (OperatingSystem.notWindows) '✓' else '+'}] ")
        terminal.cursor.move { clearLineAfterCursor() }
        terminal.println(message)
    }

    fun update(message: String) {
        currentMessage.update { message }
    }
}