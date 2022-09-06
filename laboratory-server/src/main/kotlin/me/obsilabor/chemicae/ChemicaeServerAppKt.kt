package me.obsilabor.chemicae

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import me.obsilabor.chemicae.job.jobs.ServerLifecycleJob

fun main() {
    //TODO --observe and --http argument
    ServerLifecycleJob().start()

    embeddedServer(Netty, port = 3373) {
        routing {
            route("/chemicae/api/") {
                route("v1/") {

                }
            }
        }
    }.start(wait = true)

}