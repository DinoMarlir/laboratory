package me.obsilabor.laboratory.db

import kotlinx.serialization.Serializable
import me.obsilabor.laboratory.arch.Server

@Serializable
data class Database(
    var servers: ArrayList<Server>
)
