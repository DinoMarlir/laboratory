package me.obsilabor.laboratory.proxy

import com.google.inject.Inject
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.ServerInfo
import kotlinx.coroutines.*
import java.io.File
import java.net.InetSocketAddress

@Plugin(
    id = "laboratory-proxy",
    name = "Laboratory Proxy Sync",
    version = "1.0.0",
    authors = ["mooziii"],
    description = "Plugin to automatically register laboratory servers."
)
class VelocitySync @Inject constructor(private val server: ProxyServer) {

    init {
        server.allServers.forEach {
            server.unregisterServer(it.serverInfo)
        }
        val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        coroutineScope.launch {
            val file = File(System.getProperty("user.home") + "/laboratory/live/proxysync.ps")
            if (!file.exists()) {
                file.createNewFile()
            }
            while (true) {
                val read = file.readLines()
                if (read.isNotEmpty() && read.any { it.isNotEmpty() }) {
                    file.writeText("")
                    for (cmd in read) {
                        if (!cmd.startsWith("#")) {
                            val split = cmd.split(" ")
                            when (split[0]) {
                                "REGISTER" -> {
                                    val port = split.last()
                                    val name = cmd.replace("${split[0]} ", "").replace(" $port", "")
                                    if (!server.allServers.any { it.serverInfo.name == name && it.serverInfo.address.port == port.toInt() }) {
                                        server.registerServer(ServerInfo(
                                            name,
                                            InetSocketAddress("127.0.0.1", port.toInt())
                                        ))
                                    }
                                }
                                "UNREGISTER" -> {
                                    val name = cmd.replace("${split[0]} ", "")
                                    server.unregisterServer(server.allServers.map { it.serverInfo }.first { it.name == name })
                                }
                                else -> {
                                    println("Received unknown command '${split[0]}'")
                                }
                            }
                        }
                    }
                }
                delay(1500)
            }
        }
    }
}