package me.obsilabor.laboratory

import com.github.ajalt.mordant.terminal.Terminal
import io.ktor.client.HttpClient
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.serialization.json.Json
import java.time.ZoneId
import java.time.format.DateTimeFormatter

val terminal = Terminal()

val json = Json {
    prettyPrint = true
    ignoreUnknownKeys = true
}

lateinit var mainScope: CoroutineScope

val httpClient = HttpClient {
    install(ContentNegotiation) {
        json(json)
    }
    install(HttpTimeout) {
        requestTimeoutMillis = 120*1000
    }
}

const val VERSION = "0.5.2-dev/chemicae"

val DATE_FORMAT: DateTimeFormatter = DateTimeFormatter.BASIC_ISO_DATE.withZone(ZoneId.systemDefault())
val TIME_FORMAT: DateTimeFormatter = DateTimeFormatter.ISO_TIME.withZone(ZoneId.systemDefault())