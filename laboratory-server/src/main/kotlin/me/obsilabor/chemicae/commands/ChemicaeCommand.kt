package me.obsilabor.chemicae.commands

import com.github.ajalt.clikt.core.CliktCommand
import com.github.ajalt.clikt.parameters.options.flag
import com.github.ajalt.clikt.parameters.options.option
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import me.obsilabor.chemicae.job.jobs.SchedulerJob
import me.obsilabor.chemicae.job.jobs.ServerLifecycleJob
import me.obsilabor.chemicae.routes.ServerRoutes
import me.obsilabor.laboratory.config.Config

class ChemicaeCommand : CliktCommand(
    name = "chemicae",
    help = "The root command of chemicae"
) {
    private val noObserveFlag by option("-o", "--no-observe", help = "If this flag is set, server observing will be disabled").flag()
    private val noHttpFlag by option("--no-http", help = "If this flag is set, no HTTP server will start (this may interfere with third-party apps and mods/plugins)").flag()

    override fun run() {
        if (!noObserveFlag) {
            ServerLifecycleJob().start()
        }
        SchedulerJob().start()
        if (!noHttpFlag) {
            embeddedServer(Netty, port = Config.chemicaeConfig.port) {
                routing {
                    route("/chemicae/api/") {
                        route("v1/") {
                            ServerRoutes.setup(this)
                        }
                    }
                }
            }.start(wait = true)
        }
    }
}