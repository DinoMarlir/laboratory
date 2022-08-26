package me.obsilabor.laboratory.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.config.user.UserLaboratoryConfig
import me.obsilabor.laboratory.json

object Config {
    private val file = Architecture.Config

    init {
        if (!file.exists()) {
            writeFile(null)
        }
    }

    private fun writeFile(cfg: UserLaboratoryConfig?) {
        if (!file.exists()) {
            file.createNewFile()
        }
        file.writeText(json.encodeToString(cfg ?: UserLaboratoryConfig.DEFAULTS))
    }

    val userConfig: UserLaboratoryConfig
        get() = json.decodeFromString(file.readText())
}