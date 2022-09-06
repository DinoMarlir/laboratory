package me.obsilabor.chemicae.job.jobs

import kotlinx.coroutines.*
import me.obsilabor.chemicae.job.IJob
import me.obsilabor.laboratory.arch.Server
import me.obsilabor.laboratory.arch.ServerState
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.terminal

class ServerLifecycleJob : IJob {
    override var job: Job? = null; private set
    private val scope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val preValues = hashMapOf<Server, Boolean>()

    override fun start() {
        stop()
        job = scope.launch {
            while (true) {
                for (server in JsonDatabase.servers) {
                    val now = server.isAlive
                    if (!now && server.state == ServerState.RUNNING) {
                        server.state = ServerState.STOPPED
                        JsonDatabase.editServer(server)
                        terminal.println(server.terminalString + " stopped")
                        if (server.automaticRestarts == true) {
                            terminal.println(server.terminalString + " is starting")
                            server.start(disableIO = true, experimentalWindowsSupport = true)
                        }
                    }
                    preValues[server] = now
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