package me.obsilabor.chemicae.job.jobs

import kotlinx.coroutines.*
import me.obsilabor.chemicae.job.IJob
import me.obsilabor.laboratory.db.JsonDatabase
import java.util.Calendar
import me.obsilabor.laboratory.terminal

class SchedulerJob : IJob {
    override var job: Job? = null; private set
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun start() {
        stop()
        job = scope.launch {
            while (true) {
                val calendar = Calendar.getInstance()
                val hour = calendar.get(Calendar.HOUR_OF_DAY)
                val minute = calendar.get(Calendar.MINUTE)
                for (server in JsonDatabase.servers) {
                    server.scheduledTasks.filter { it.hour == hour && it.minute == minute }.forEach {
                        terminal.println("Running task \"${it.action.displayName}\" for ${server.terminalString}")
                        it.action.executor.invoke(server)
                    }
                }
                delay(60000)
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }
}