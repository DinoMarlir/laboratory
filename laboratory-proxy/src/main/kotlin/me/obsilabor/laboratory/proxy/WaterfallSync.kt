package me.obsilabor.laboratory.proxy

import kotlinx.coroutines.*
import net.md_5.bungee.api.ProxyServer
import net.md_5.bungee.api.plugin.Plugin
import java.io.File
import java.net.InetSocketAddress

@Suppress("Deprecation")
class WaterfallSync : Plugin() {
    override fun onEnable() {
        val server = ProxyServer.getInstance()
        val config = server.config
        val coroutineScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
        server.servers.clear()
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
                                    val serverInfo = server.constructServerInfo(
                                        name,
                                        InetSocketAddress("127.0.0.1", port.toInt()),
                                        "Waterfall subserver managed by laboratory",
                                        false
                                    )
                                    server.servers[name] = serverInfo
                                }
                                "UNREGISTER" -> {
                                    val name = cmd.replace("${split[0]} ", "")
                                    server.servers.remove(name)
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