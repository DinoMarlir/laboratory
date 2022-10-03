package me.obsilabor.chemicae.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.server.websocket.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.obsilabor.chemicae.request.ServerSearchRequest
import me.obsilabor.chemicae.utils.checkHeader
import me.obsilabor.chemicae.utils.requireParameter
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.json
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.chooseServerHeadless

object ServerRoutes {
    fun setup(route: Route) {
        route.route("servers") {
            get("all") {
                if(!call.checkHeader()) return@get
                call.respondText(json.encodeToString(JsonDatabase.servers), ContentType.Application.Json)
            }
            get("search") {
                if(!call.checkHeader()) return@get
                runCatching {
                    val body = json.decodeFromString<ServerSearchRequest>(call.receive())
                    call.respondText(json.encodeToString(body.applyToList(JsonDatabase.servers)), ContentType.Application.Json)
                }.onFailure {
                    call.respond(HttpStatusCode.BadRequest, "Bad request body")
                }
            }
            get("{server}/start") {
                if(!call.checkHeader()) return@get
                if (!call.requireParameter("server")) return@get
                val server = terminal.chooseServerHeadless(call.parameters["server"] ?: return@get)
                call.respondText(json.encodeToString(JsonDatabase.servers), ContentType.Application.Json)
            }
            webSocket("{server}/console") {
                if(!call.checkHeader()) return@webSocket
                if (!call.requireParameter("server")) return@webSocket
                call.respond(HttpStatusCode.ServiceUnavailable, "This route is lacking windows support. Sorry :/")
            }
        }
    }
}