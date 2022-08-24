package me.obsilabor.laboratory.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.json

object Config {
    private val file = Architecture.Config

    init {
        if (!file.exists()) {
            writeFile(null)
        }
    }

    private fun writeFile(cfg: LaboratoryConfig?) {
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(json.encodeToString(cfg ?: LaboratoryConfig.DEFAULTS))
    }

    val config: LaboratoryConfig
        get() = json.decodeFromString(file.readText())
}