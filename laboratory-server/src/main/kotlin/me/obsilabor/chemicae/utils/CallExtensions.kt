package me.obsilabor.chemicae.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import me.obsilabor.laboratory.config.Config

suspend fun ApplicationCall.requireParameter(parameter: String): Boolean {
    if (parameters[parameter] == null) {
        respond(HttpStatusCode.BadRequest, "Parameter \"$parameter\" is required")
        return false
    }
    return true
}

suspend fun ApplicationCall.checkHeader(): Boolean {
    val request = this.request
    val token = request.header("Access-Token")
    val result = Config.chemicaeConfig.accessToken == token
    if (!result) {
        respond(HttpStatusCode.Unauthorized,"Authentication failed")
    }
    return result
}