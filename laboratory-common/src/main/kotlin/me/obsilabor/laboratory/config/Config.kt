package me.obsilabor.laboratory.config

import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import me.obsilabor.laboratory.arch.Architecture
import me.obsilabor.laboratory.config.user.ChemicaeConfig
import me.obsilabor.laboratory.config.user.UserLaboratoryConfig
import me.obsilabor.laboratory.json

object Config {
    private val userConfigFile = Architecture.UserConfig
    private val chemicaeConfigFile = Architecture.ChemicaeConfig

    init {
        if (!userConfigFile.exists()) {
            writeUserFile(null)
        }
        if (!chemicaeConfigFile.exists()) {
            writeChemicaeFile(null)
        }
    }

    fun writeUserFile(cfg: UserLaboratoryConfig?) {
        if (!userConfigFile.exists()) {
            userConfigFile.createNewFile()
        }
        userConfigFile.writeText(json.encodeToString(cfg ?: UserLaboratoryConfig.DEFAULTS))
    }

    private fun writeChemicaeFile(cfg: ChemicaeConfig?) {
        if (!chemicaeConfigFile.exists()) {
            chemicaeConfigFile.createNewFile()
        }
        chemicaeConfigFile.writeText(json.encodeToString(cfg ?: ChemicaeConfig.DEFAULTS))
    }

    val userConfig: UserLaboratoryConfig
        get() = json.decodeFromString(userConfigFile.readText())

    val chemicaeConfig: ChemicaeConfig
        get() = json.decodeFromString(chemicaeConfigFile.readText())
}