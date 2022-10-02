package me.obsilabor.chemicae.request

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import me.obsilabor.laboratory.arch.Server
import me.obsilabor.laboratory.json

@Serializable
data class ServerSearchRequest(
    val filters: HashMap<String, String> = hashMapOf(),
) {
    fun applyToList(list: List<Server>): List<Server> {
        var current = list
        for (filter in filters) {
            current = list.filter { json.encodeToJsonElement(it).jsonObject[filter.key]?.toString()?.removePrefix("\"")?.removeSuffix("\"")?.equals(filter.value) == true }
        }
        return current
    }
}
