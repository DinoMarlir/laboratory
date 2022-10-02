package me.obsilabor.laboratory.config.user

import kotlinx.serialization.Serializable
import me.obsilabor.laboratory.utils.getRandomID

@Serializable
data class ChemicaeConfig(
    val port: Int,
    val accessToken: String,
) {
    companion object {
        val DEFAULTS = ChemicaeConfig(
            3373,
            getRandomID(16)
        )
    }
}
