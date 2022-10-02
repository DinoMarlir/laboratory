package me.obsilabor.chemicae.routes

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.obsilabor.chemicae.request.ServerSearchRequest
import me.obsilabor.chemicae.utils.checkHeader
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.json

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
            get("start") {
                if(!call.checkHeader()) return@get
                call.respondText(json.encodeToString(JsonDatabase.servers), ContentType.Application.Json)
            }
        }
    }
}