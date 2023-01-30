package me.obsilabor.laboratory.arch

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonObject

@Serializable
data class CustomProperties(
    val namespace: String,
    val json: JsonObject
)