package tech.marlonr.laboratory.ingamecontrol.velocity

import com.google.inject.Inject
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.proxy.ProxyServer

//TODO
@Plugin(
    id = "laboratory-ingamecontrol",
    name = "Laboratory ingame control",
    version = "1.0.0",
    authors = ["DinoMarlir"],
    description = "Plugin to control laboratory."
)
class VelocitySync @Inject constructor(private val server: ProxyServer) {

    init {
        println("ingamecontrol")
    }
}