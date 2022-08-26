package me.obsilabor.chemicae

import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    embeddedServer(Netty, port = 3373) {
        routing {
            route("/api/") {
                get("/") {
                    call.respond("/v1")
                }
                route("v1/") {

                }
            }
        }
    }.start(wait = true)
}