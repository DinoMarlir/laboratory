package me.obsilabor.laboratory.utils

import com.github.ajalt.mordant.rendering.TextColors
import io.ktor.client.call.*
import io.ktor.client.plugins.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import me.obsilabor.laboratory.mainScope
import me.obsilabor.laboratory.httpClient
import me.obsilabor.laboratory.terminal
import java.nio.file.Path
import kotlin.math.roundToInt

suspend fun downloadFile(url: String, destination: Path, silent: Boolean = false, callback: () -> Unit = {}) {
    if (silent) {
        downloadFile(url, destination, 1, 1, silent, callback)
    } else {
        downloadFileV2(url, destination, callback)
    }
}

suspend fun downloadFile(url: String, destination: Path, current: Int, total: Int, silent: Boolean = false, callback: () -> Unit) {
    val downloadContent = httpClient.get(url) {
        if (!silent) {
            onDownload { bytesSentTotal, contentLength ->
                val progress = bytesSentTotal.toDouble() / contentLength.toDouble()
                val hashtags = (progress * 30).roundToInt()
                val percentage = (progress * 100).roundToInt()
                mainScope.launch(Dispatchers.IO) {
                    val string = buildString {
                        append("Downloading ${destination.toFile().name} [")
                        repeat(hashtags) {
                            append(TextColors.brightGreen("#"))
                        }
                        repeat(30 - hashtags) {
                            append(' ')
                        }
                        append("] ${percentage}%")
                        if (total > 1) {
                            append(" ($current/$total)")
                        }
                    }
                    terminal.print("\r  $string")
                }.join()
            }
        }
    }.body<HttpResponse>().readBytes()
    if (!silent) {
        terminal.println()
    }
    destination.toFile().writeBytes(downloadContent)
    callback.invoke()
}

suspend fun downloadFileV2(url: String, destination: Path, callback: () -> Unit = {}) {
    val downloadedContent = httpClient.get(url) {
        onDownload { bytesSentTotal, contentLength ->
            val progress = bytesSentTotal.toDouble() / contentLength.toDouble()
            val dashes = (progress * 50).roundToInt()
            val percentage = (progress * 100).roundToInt()
            val string = buildString {
                append("Downloading ${destination.toFile().name}  ")
                repeat(dashes) {
                    append(TextColors.brightGreen(if (OperatingSystem.notWindows) "━" else "#"))
                }
                //append(" ")
                repeat(50 - dashes) {
                    append(TextColors.black(if (OperatingSystem.notWindows) "━" else "#"))
                }
                append("  $percentage%")
                append(" ${(bytesSentTotal / 1e+6).toInt()}/${(contentLength / 1e+6).toInt()}MB")
            }
            terminal.print("\r$string")
        }
    }.body<HttpResponse>().readBytes()
    destination.toFile().writeBytes(downloadedContent)
    callback.invoke()
    println()
}