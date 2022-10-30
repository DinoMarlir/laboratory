package me.obsilabor.chemicae.job.jobs

import com.github.ajalt.mordant.rendering.TextColors
import kotlinx.coroutines.*
import me.obsilabor.chemicae.job.IJob
import me.obsilabor.laboratory.config.Config
import me.obsilabor.laboratory.terminal

class ConsoleJob : IJob {
    override var job: Job? = null; private set
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    var runningCmd = false

    override fun start() {
        stop()
        job = scope.launch {
            while (true) {
                val readLine = readLine()
                if (readLine != null && !runningCmd) {
                    runCatching {
                        val process = ProcessBuilder(buildList {
                            add(Config.chemicaeConfig.laboratoryExecutable)
                            addAll(readLine.split(" "))
                        }).inheritIO().start()
                        runningCmd = true
                        scope.launch {
                            process.waitFor()
                            runningCmd = false
                        }
                    }.onFailure {
                        it.printStackTrace()
                        terminal.println(TextColors.red("Could not execute laboratory."))
                    }
                }
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }
}