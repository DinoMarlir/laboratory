package me.obsilabor.chemicae.utils

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*

suspend fun ApplicationCall.requireParameter(parameter: String): Boolean {
    if (parameters[parameter] == null) {
        respond(HttpStatusCode.BadRequest, "Parameter \"$parameter\" is required")
        return false
    }
    return true
}

suspend fun ApplicationCall.checkHeader() {

}