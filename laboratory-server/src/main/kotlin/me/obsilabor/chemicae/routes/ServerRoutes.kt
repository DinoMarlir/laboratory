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
import me.obsilabor.laboratory.arch.Server
import me.obsilabor.laboratory.db.JsonDatabase
import me.obsilabor.laboratory.json
import me.obsilabor.laboratory.platform.PlatformResolver
import me.obsilabor.laboratory.terminal
import me.obsilabor.laboratory.terminal.SpinnerAnimation
import me.obsilabor.laboratory.terminal.awaitMemoryInput
import me.obsilabor.laboratory.terminal.chooseServerHeadless
import me.obsilabor.laboratory.terminal.promptYesOrNo
import java.awt.Desktop

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
            post("create{name,platform}") {
                if(!call.checkHeader()) return@post
                if (!call.requireParameter("name")) return@post
                if (!call.requireParameter("platform")) return@post
                kotlin.runCatching {
                    val platform = PlatformResolver.resolvePlatform(call.parameters["platform"] ?: return@post)
                    val db = JsonDatabase.db
                    val server = Server(
                        db.internalCounter,
                        call.parameters["name"] ?: return@post,
                        true,
                        true,
                        mutableSetOf(),
                        platform.name,
                        platform.getBuilds(platform.getMcVersions().last()).last(),
                        platform.getMcVersions().last(),
                        true,
                        1024,
                        mutableSetOf("-Dlog4j2.formatMsgNoLookups=true"),
                        if (!Desktop.isDesktopSupported()) mutableSetOf("nogui") else mutableSetOf(),
                        25565,
                        true,
                        true,
                        "java",
                    )
                    db.internalCounter++
                    JsonDatabase.writeFile(db)
                    JsonDatabase.registerServer(server)
                    call.respondText(json.encodeToString(server), ContentType.Application.Json)
                }.onFailure {
                    call.respond(HttpStatusCode.InternalServerError, it.message.toString())
                }
            }
            post("{server}/start") {
                if(!call.checkHeader()) return@post
                if (!call.requireParameter("server")) return@post
                terminal.chooseServerHeadless(call.parameters["server"] ?: return@post)?.start(disableIO = true)
                call.respondText(json.encodeToString(JsonDatabase.servers), ContentType.Application.Json)
            }
            post("{server}/stop{force}") {
                if(!call.checkHeader()) return@post
                if (!call.requireParameter("server")) return@post
                terminal.chooseServerHeadless(call.parameters["server"] ?: return@post)?.stop(call.parameters["force"] == "true")
                call.respondText(json.encodeToString(JsonDatabase.servers), ContentType.Application.Json)
            }
            /*
            webSocket("{server}/console") {
                if(!call.checkHeader()) return@webSocket
                if (!call.requireParameter("server")) return@webSocket
                call.respond(HttpStatusCode.ServiceUnavailable, "This route is lacking windows support. Sorry :/")
            }
             */
        }
    }
}