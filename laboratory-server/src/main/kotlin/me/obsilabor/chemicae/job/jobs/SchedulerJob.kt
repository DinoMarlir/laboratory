package me.obsilabor.chemicae.job.jobs

import kotlinx.coroutines.*
import me.obsilabor.chemicae.job.IJob
import me.obsilabor.laboratory.db.JsonDatabase

class SchedulerJob : IJob {
    override var job: Job? = null; private set
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())

    override fun start() {
        stop()
        job = scope.launch {
            while (true) {
                for (server in JsonDatabase.servers) {

                }
                delay(1000)
            }
        }
    }

    override fun stop() {
        job?.cancel()
        job = null
    }
}