package me.obsilabor.laboratory.db

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.arch.Server
import me.obsilabor.laboratory.json

object JsonDatabase {
    private val file = Architecture.Database

    init {
        writeFile(null)
    }

    private fun writeFile(db: Database?) {
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(json.encodeToString(db ?: Database(arrayListOf())))
    }

    val db: Database
        get() = json.decodeFromString(file.readText())

    val servers: List<Server>
        get() = db.servers

    fun findServer(id: Int): Server {
        return servers.first { it.id == id }
    }

    fun findServer(name: String): List<Server> {
        return servers.filter { it.name.lowercase() == name.lowercase() }
    }

    fun registerServer(server: Server) {
        val database = db
        val serverList = arrayListOf<Server>(server)
        serverList.addAll(servers)
        database.servers = serverList
        writeFile(database)
    }

    fun editServer(server: Server) {
        val database = db
        val serverList = arrayListOf<Server>(server)
        servers.filter { it.id != server.id }.forEach { serverList.add(it) }
        database.servers = serverList
        writeFile(database)
    }

    fun deleteServer(server: Server) {
        val database = db
        val serverList = arrayListOf<Server>()
        servers.filter { it.id != server.id }.forEach { serverList.add(it) }
        database.servers = serverList
        writeFile(database)
    }
}

